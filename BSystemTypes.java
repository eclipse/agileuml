import java.util.Vector; 
import java.util.List; 
import java.io.*; 

/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Class Diagram */

public class BSystemTypes extends BComponent
{ public static java.util.Map selectOps = new java.util.HashMap(); 
  // stores map String +-> String defining operation for each predicate of 
  // a select used in the specification
  public static java.util.Map selectCodes = new java.util.HashMap(); 
  // stores numeric codes of the predicates
  public static java.util.Map selectDecs = new java.util.HashMap(); 
  public static Vector selectList = new Vector(); 

  private static int index = 0; 

  public static Vector existsList = new Vector(); 
  public static java.util.Map existsOps = new java.util.HashMap(); 
  public static java.util.Map existsDecs = new java.util.HashMap(); 
  public static java.util.Map existsCodes = new java.util.HashMap(); 

  public static Vector exists1List = new Vector(); 
  public static java.util.Map exists1Ops = new java.util.HashMap(); 
  public static java.util.Map exists1Decs = new java.util.HashMap(); 
  public static java.util.Map exists1Codes = new java.util.HashMap(); 

  public static Vector rejectList = new Vector(); 
  public static java.util.Map rejectOps = new java.util.HashMap(); 
  public static java.util.Map rejectCodes = new java.util.HashMap(); 
  public static java.util.Map rejectDecs = new java.util.HashMap(); 

  public static Vector forAllList = new Vector(); 
  public static java.util.Map forAllOps = new java.util.HashMap(); 
  public static java.util.Map forAllDecs = new java.util.HashMap(); 
  public static java.util.Map forAllCodes = new java.util.HashMap(); 

  public static java.util.Map collectOps = new java.util.TreeMap(); 
  public static java.util.Map collectCodes = new java.util.TreeMap(); 
  public static java.util.Map collectDecs = new java.util.TreeMap(); 
  public static Vector collectList = new Vector(); 

  public static java.util.Map anyOps = new java.util.TreeMap(); 
  public static java.util.Map anyCodes = new java.util.TreeMap(); 
  public static java.util.Map anyDecs = new java.util.TreeMap(); 
  public static Vector anyList = new Vector(); 


  public BSystemTypes(String nme)
  { super(nme,new Vector());
    clearVariables();
  } // SystemTypes has no variables, only types.


  public static void resetDesigns()
  { selectOps.clear(); selectCodes.clear(); 
    selectList.clear(); selectDecs.clear(); 
    
    existsOps.clear(); existsCodes.clear(); 
    existsList.clear(); 
    
    exists1Ops.clear(); exists1Codes.clear(); 
    exists1List.clear(); 
    
    exists1Decs.clear(); existsDecs.clear(); 
    
    forAllOps.clear(); forAllCodes.clear(); 
    forAllDecs.clear(); forAllList.clear(); 

    
    rejectOps.clear(); rejectCodes.clear(); 
    rejectList.clear(); rejectDecs.clear(); 
    
    collectList.clear(); collectDecs.clear(); 
    collectOps.clear(); collectCodes.clear(); 
    
    anyList.clear(); anyDecs.clear(); 
    anyOps.clear(); anyCodes.clear(); 

    index = 0; 
  } 

  public static String getSelectDefinition(Expression left, String lqf,
                                           Expression pred, String selectvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    // Only applies to left being a collection of objects, not numerics, strings or 
    // collections 

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set") || ename.equals("Sequence"))
    { tname = "List"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null)
    { tname = e.typeWrapper(); } 

    // System.out.println(left + " element type is " + e); 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) selectOps.get(pp); 
    // But left may be ordered in one case and not in other

    if (op == null) 
    { // add new definitions 

      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res = "  public static List select_" + oldindex + "(List _l"; 
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      res = res + "  { // Implements: " + left + "->select(" + var + " | " + pred + ")\n" + 
                  "    List _results_" + oldindex + " = new Vector();\n" + 
                  "    for (int _i = 0; _i < _l.size(); _i++)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res = res + "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n"; }   

      System.out.println(">>> Evaluating " + pred + " in environment " + newenv); 
	  
      String test = pred.queryForm(newenv,false); 
      
      System.out.println(">>> Result =  " + test); 
	  
      String wvar = Expression.wrap(e,var); 
      res = res + "      if (" + test + ")\n" + 
                  "      { _results_" + oldindex + ".add(" + wvar + "); }\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }\n\n"; 

      // Version for maps: 
	  res = res + "  public static Map select_" + oldindex + "(Map _l"; 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      res = res + "  { // Implements: " + left + "->select(" + var + " | " + pred + ")\n" + 
                  "    Map _results_" + oldindex + " = new java.util.HashMap();\n" +
				  "    java.util.Set _keys = _l.keySet();\n" +  
                  "    for (Object _i : _keys)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res = res + "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n"; }   

      res = res + "      if (" + test + ")\n" + 
                  "      { _results_" + oldindex + ".put(_i, " + wvar + "); }\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }\n"; 

	  
      selectList.add(pp); 
      selectOps.put(pp,res); 
      selectCodes.put(pp,"" + oldindex); 

      return "select_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) selectCodes.get(pp); 
      return "select_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getSelectDefinitionJava6(Expression left, String lqf,
                                           Expression pred, String selectvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String restype1 = "ArrayList"; 
    // if (left.isOrdered())
    // { restype = "ArrayList"; } 
    // else 
    // { restype = "HashSet"; } 
    String restype2 = "HashSet"; 

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "HashSet"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapper(); } 

    // System.out.println(left + " element type is " + e); 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) selectOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 

      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }
      String res1 = "  public static " + restype1 + " select_" + oldindex + "(" + restype1 + " _l"; 
      String res2 = "  public static " + restype2 + " select_" + oldindex + "(" + restype2 + " _l"; 

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res1 = res1 + "," + par.getType().getJava6() + " " + par.getName(); 
        res2 = res2 + "," + par.getType().getJava6() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res1 = res1 + ")\n"; 
      res1 = res1 + "  { // Implements: " + left + "->select(" + var + " | " + pred + ")\n" + 
                    "    " + restype1 + " _results_" + oldindex + " = new " + restype1 + "();\n" + 
                    "    for (int _i = 0; _i < _l.size(); _i++)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res1 = res1 + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res1 = res1 + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res1 = res1 + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res1 = res1 + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res1 = res1 + "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n"; }  
                      
      res2 = res2 + ")\n"; 
      res2 = res2 + "  { " + restype2 + " _results_" + oldindex + " = new " + restype2 + "();\n" + 
                    "    for (Object _i : _l)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res2 = res2 + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res2 = res2 + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res2 = res2 + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res2 = res2 + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res2 = res2 + "    { " + tname + " " + var + " = (" + tname + ") _i;\n"; }  

      String test = pred.queryFormJava6(newenv,false); 

      res1 = res1 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _results_" + oldindex + ";\n  }"; 

      res2 = res2 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _results_" + oldindex + ";\n  }"; 
      // no need to wrap var in Java 6. 

      selectList.add(pp); 
      selectOps.put(pp,res1 + "\n\n" + res2); 
      selectCodes.put(pp,"" + oldindex); 

      return "select_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) selectCodes.get(pp); 
      return "select_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getSelectDefinitionJava7(
                   Expression left, String lqf,
                   Expression pred, String selectvar, 
                   java.util.Map env,
                   Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         


    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "Set"; } // element type? 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; 
      if (e != null)
      { tname = e.getJava7(); }
    }  
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava7(); } 

    String partype1 = "List<" + tname + ">"; 
    String restype1 = "ArrayList<" + tname + ">"; 
    String restype2 = "HashSet<" + tname + ">"; 
    String restype3 = "TreeSet<" + tname + ">"; 
    String restype4 = "Set<" + tname + ">"; 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) selectOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 

      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }
      String res1 = "  public static " + restype1 + " select_" + oldindex + "(" + partype1 + " _l"; 
      String res2 = "  public static " + restype2 + " select_" + oldindex + "(" + restype2 + " _l"; 
      String res3 = "  public static " + restype3 + " select_" + oldindex + "(" + restype3 + " _l"; 
      String res4 = "  public static " + restype2 + " select_" + oldindex + "(" + restype4 + " _l"; 

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        if (par.getType() == null) 
        { System.err.println("!! ERROR: no type for " + par); } 
        else 
        { Type partype = par.getType(); 
          String jType = 
            partype.getJava7(partype.getElementType()); 
          res1 = res1 + ", " + jType + " " + par.getName(); 
          res2 = res2 + ", " + jType + " " + par.getName();  
          res3 = res3 + ", " + jType + " " + par.getName();
          res4 = res4 + ", " + jType + " " + par.getName();
        } 
      } 

      res1 = res1 + ")\n"; 
      res1 = res1 + "  { // implements: " + left + "->select( " +  var + " | " + pred + " )\n" + 
                    "    " + restype1 + " _results_" + oldindex + " = new " + restype1 + "();\n" + 
                    "    for (int _i = 0; _i < _l.size(); _i++)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res1 = res1 + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res1 = res1 + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res1 = res1 + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res1 = res1 + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res1 = res1 + "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n"; }  
                      
      res2 = res2 + ")\n"; 
      res2 = res2 + "  { // implements: " + lqf + "->select( " +  selectvar + " | " + pred + " )\n" + 
                    "    " + restype2 + " _results_" + oldindex + " = new " + restype2 + "();\n" + 
                    "    for (" + tname + " _i : _l)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res2 = res2 + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res2 = res2 + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res2 = res2 + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res2 = res2 + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res2 = res2 + "    { " + tname + " " + var + " = (" + tname + ") _i;\n"; }  

      res3 = res3 + ")\n"; 
      res3 = res3 + "  { " + restype3 + " _results_" + oldindex + " = new " + restype3 + "();\n" + 
                    "    for (" + tname + " _i : _l)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res3 = res3 + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res3 = res3 + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res3 = res3 + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res3 = res3 + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res3 = res3 + "    { " + tname + " " + var + " = (" + tname + ") _i;\n"; }  

      res4 = res4 + ")\n"; 
      res4 = res4 + "  { // implements: " + lqf + "->select( " +  selectvar + " | " + pred + " )\n" + 
                    "    " + restype2 + " _results_" + oldindex + " = new " + restype2 + "();\n" + 
                    "    for (" + tname + " _i : _l)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res4 = res4 + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res4 = res4 + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res4 = res4 + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res4 = res4 + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res4 = res4 + "    { " + tname + " " + var + " = (" + tname + ") _i;\n"; }  

      String test = pred.queryFormJava7(newenv,false); 

      res1 = res1 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _results_" + oldindex + ";\n  }"; 

      res2 = res2 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _results_" + oldindex + ";\n  }"; 
      
      res3 = res3 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res3 = res3 + "    }\n"; 
      res3 = res3 + "    return _results_" + oldindex + ";\n  }"; 

      res4 = res4 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res4 = res4 + "    }\n"; 
      res4 = res4 + "    return _results_" + oldindex + ";\n  }"; 

      selectList.add(pp); 
      selectOps.put(pp,res1 + "\n\n" + res2 + "\n\n" + res3 + "\n\n" + res4); 
      selectCodes.put(pp,"" + oldindex); 

      return "select_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) selectCodes.get(pp); 
      return "select_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getSelectDefinitionCSharp(Expression left, String lqf,
                                           Expression pred, String selectvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller.inst().get" + instances + "()"; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set") || ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("String"))
    { tname = "string"; } 
    else if (ename.equals("boolean"))
    { tname = "bool"; } 

    // System.out.println(left + " element type is " + e); 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) selectOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 

      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res = "  public static ArrayList select_" + oldindex + "(ArrayList _l"; 
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); }  

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getCSharp() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      res = res + "  { // Implements: " + left + "->select(" + var + " | " + pred + ")\n" + 
                  "    ArrayList _results_" + oldindex + " = new ArrayList();\n" + 
                  "    for (int _iselect = 0; _iselect < _l.Count; _iselect++)\n" + 
                  "    { " + tname + " " + var + " = (" + tname + ") _l[_iselect];\n"; 

      String test = pred.queryFormCSharp(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { _results_" + oldindex + ".Add(" + var + "); }\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }"; 


      selectList.add(pp); 
      selectOps.put(pp,res); 
      selectCodes.put(pp,"" + oldindex); 

      return "select_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) selectCodes.get(pp); 
      return "select_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getSelectDefinitionCPP(Expression left, String lqf,
                                              Expression pred, String selectvar,
                                              java.util.Map env,
                                              Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller::inst->get" + instances + "()"; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "void"; } 
    else
    { ename = e.getName(); }

    String tname = ename + "*"; 
    if (Type.isBasicType(e)) 
    { tname = e.getCPP("void*"); } 
    else if ("Sequence".equals(ename))
    { tname = e.getCPP(e.getElementType()); } // "vector<void*>*"; } 
    else if ("Set".equals(ename))
    { tname = e.getCPP(e.getElementType()); } // "set<void*>*"; } 

    String restype1 = "std::set<" + tname + ">"; 
    String addop1 = "insert"; 
    String restype2 = "std::vector<" + tname + ">"; 
    String addop2 = "push_back"; 
    
    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) selectOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 

      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res1 = "  static " + restype1 + "* select_" + oldindex + "(" + restype1 + "* _l"; 
      String res2 = "  static " + restype2 + "* select_" + oldindex + "(" + restype2 + "* _l"; 
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); }  

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String cpppt = par.getType().getCPP(par.getElementType());  
        res1 = res1 + ", " + cpppt + " " + par.getName();
        res2 = res2 + ", " + cpppt + " " + par.getName();
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res1 = res1 + ")\n"; 
      res1 = res1 + "  { // implements: " + left + "->select( " + var + " | " + pred + " )\n" + 
                  "    " + restype1 + "* _results_" + oldindex + " = new " + restype1 + "();\n" + 
                  "    for (" + restype1 + "::iterator _iselect = _l->begin(); _iselect != _l->end(); ++_iselect)\n" + 
                  "    { " + tname + " " + var + " = *_iselect;\n"; 
      res2 = res2 + ")\n"; 
      res2 = res2 + "  { " + restype2 + "* _results_" + oldindex + " = new " + restype2 + "();\n" + 
                  "    for (" + restype2 + "::iterator _iselect = _l->begin(); _iselect != _l->end(); ++_iselect)\n" + 
                  "    { " + tname + " " + var + " = *_iselect;\n"; 

      String test = pred.queryFormCPP(newenv,false); 
      res1 = res1 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + "->" + addop1 + "(" + var + "); }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _results_" + oldindex + ";\n  }"; 
      res2 = res2 + "      if (" + test + ")\n" + 
                    "      { _results_" + oldindex + "->" + addop2 + "(" + var + "); }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _results_" + oldindex + ";\n  }"; 

      String res = res1 + "\n\n" + res2; 

      selectList.add(pp); 
      selectOps.put(pp,res); 
      selectCodes.put(pp,"" + oldindex); 

      return "select_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) selectCodes.get(pp); 
      return "select_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getCollectDefinition(Expression left, String lqf,
                                           Expression exp, boolean rprim,
                                           String collectvar, java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set") || ename.equals("Sequence"))
    { tname = "List"; } 
    else if (ename.equals("int")) 
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapper(); } 

    String pp = "" + exp + " " + ename + "(" + signature + ")"; 
    String op = (String) collectOps.get(pp); 
    // But may be different left element type with same expression


    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (collectvar != null)
      { var = collectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res = "  public static List collect_" + oldindex + "(List _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava() + " " + par.getName(); 
      } 
      res = res + ")\n"; 
      res = res + "  { // implements: " + left + "->collect( " +  var + " | " + exp + " )\n" +
                  "    List _results_" + oldindex + " = new Vector();\n" + 
                  "    for (int _i = 0; _i < _l.size(); _i++)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res = res + "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n"; }  

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      if (collectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); }  
      String elem = exp.queryForm(newenv,false); 
      Type restype = exp.getType(); 
      String jtype = restype.typeWrapper();

      if (rprim) 
      { elem = Expression.wrap(exp.getType(),elem); }

      res = res + "      " + jtype + " collect_x = " + elem + ";\n" + 
                  "      if (collect_x != null) { _results_" + oldindex + ".add(collect_x); }\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }\n\n"; 

      // Also need a Map version: 
      res = res + "  public static Map collect_" + oldindex + "(Map _l"; 
	  for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava() + " " + par.getName(); 
      } 
      res = res + ")\n"; 
      res = res + 
         "  { // implements: " + left + "->collect( " +  var + " | " + exp + " )\n" +
         "    Map _results_" + oldindex + " = new HashMap();\n" + 
         "    java.util.Set _keys = _l.keySet();\n" +  
         "    for (Object _i : _keys)\n" + 
         "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n" + 
         "      " + jtype + " collect_x = " + elem + ";\n" + 
         "      if (collect_x != null) { _results_" + oldindex + ".put(_i,collect_x); }\n";
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }"; 

      collectList.add(pp); 
      collectOps.put(pp,res); 
      collectCodes.put(pp,"" + oldindex); 

      return "collect_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) collectCodes.get(pp); 
      return "collect_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getCollectDefinitionJava6(Expression left, String lqf,
                                           Expression exp, boolean rprim,
                                           String collectvar, java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left))  
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "HashSet"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava6(); } 

    String restype = ""; 
    // if (left.isOrdered())
    { restype = "ArrayList"; } 
    // else 
    // { restype = "HashSet"; } 

    String pp = "" + exp + " " + ename + "(" + signature + ")"; 
    String op = (String) collectOps.get(pp); 
    // But may be different left element type with same expression

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (collectvar != null)
      { var = collectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res = "  public static " + restype + " collect_" + oldindex + "(Collection _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava6() + " " + par.getName(); 
      } 
      res = res + ")\n"; 
      res = res + "  { // implements: " + left + "->collect( " +  var + " | " + exp + " )\n" +
                  "    " + restype + " _results_" + oldindex + " = new " + restype + "();\n" + 
                  "    for (Object _i : _l)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res +              
               "    { int " + var + " = ((Integer) _i).intValue();\n";
      } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res = res +  
            "    { " + tname + " " + var + " = (" + tname + ") _i;\n"; 
      } 

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      if (collectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); } 
      String elem = exp.queryFormJava6(newenv,false); 

      if (rprim) 
      { elem = Expression.wrap(exp.getType(),elem); }

      res = res + "     _results_" + oldindex + ".add(" + elem + ");\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }"; 

      collectList.add(pp); 
      collectOps.put(pp,res); 
      collectCodes.put(pp,"" + oldindex); 

      return "collect_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) collectCodes.get(pp); 
      return "collect_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getCollectDefinitionJava7(
           Expression left, String lqf,
           Expression exp, boolean rprim,
           String collectvar, java.util.Map env,
           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left))  
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "Set"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava7(); } 

    Type argtype = exp.getType(); 
    String exptype = "Object"; 
    if (argtype == null) 
    { System.err.println("!! ERROR: No type for " + exp + " in collect"); } 
    else 
    { exptype = 
        Type.getJava7Type(argtype, argtype.getKeyType(), 
                          exp.getElementType()); 
    } 

    String restype = "ArrayList<" + exptype + ">";  

    String pp = "" + exp + " " + ename + "(" + signature + ")"; 
    String op = (String) collectOps.get(pp); 
    // But may be different left element type with same expression

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (collectvar != null)
      { var = collectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res = "  public static " + restype + " collect_" + oldindex + "(Collection<" + tname + "> _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        Type partype = par.getType(); 
        if (partype != null)  
        { res = res + "," + 
                partype.getJava7(partype.getElementType()) + 
                                 " " + par.getName(); 
        } 
      } 
      res = res + ")\n"; 
      res = res + "  { // Implements: " + left + "->collect( " + var + " | " + exp + " )\n" + 
                  "   " + restype + " _results_" + oldindex + " = new " + restype + "();\n" + 
                  "    for (" + tname + " _i : _l)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res +              
               "    { int " + var + " = ((Integer) _i).intValue();\n";
      } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res = res +  
            "    { " + tname + " " + var + " = (" + tname + ") _i;\n"; 
      } 

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      if (collectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); } 
      String elem = exp.queryFormJava7(newenv,false); 

      if (rprim) 
      { elem = Expression.wrap(exp.getType(),elem); }

      res = res + "     _results_" + oldindex + ".add(" + elem + ");\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }"; 

      collectList.add(pp); 
      collectOps.put(pp,res); 
      collectCodes.put(pp,"" + oldindex); 

      return "collect_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) collectCodes.get(pp); 
      return "collect_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getCollectDefinitionCSharp(Expression left, String lqf,
                                           Expression exp, boolean rprim,
                                           String collectvar, java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

   

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller.inst().get" + instances + "()"; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set") || ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("String"))
    { tname = "string"; } 
    else if (ename.equals("boolean"))
    { tname = "bool"; } 

    String pp = "" + exp + " " + ename + "(" + signature + ")"; 
    String op = (String) collectOps.get(pp); 
    // But may be different left element type with same expression

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (collectvar != null)
      { var = collectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res = "  public static ArrayList collect_" + oldindex + "(ArrayList _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        System.out.println(">> Collect parameter " + par + " " + par.getType()); 

        res = res + ", " + par.getType().getCSharp() + " " + par.getName(); 
      } 
      res = res + ")\n"; 
      res = res + "  { // Implements: " + left + "->collect( " +  var + " | " + exp + " )\n" +
                  "    ArrayList _results_" + oldindex + " = new ArrayList();\n" + 
                  "    for (int _icollect = 0; _icollect < _l.Count; _icollect++)\n" + 
                  "    { " + tname + " " + var + " = (" + tname + ") _l[_icollect];\n"; 
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      if (collectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); } 
 
      String elem = exp.queryFormCSharp(newenv,false); 

      // if (rprim) 
      // { elem = Expression.wrap(exp.getType(),elem); }

      res = res + "     _results_" + oldindex + 
            ".Add(" + elem + ");\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }"; 

      collectList.add(pp); 
      collectOps.put(pp,res); 
      collectCodes.put(pp,"" + oldindex); 

      return "collect_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) collectCodes.get(pp); 
      return "collect_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getCollectDefinitionCPP(Expression left, String lqf,
                                           Expression exp, boolean rprim,
                                           String collectvar, java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller::inst->get" + instances + "()"; 
    }         


    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "void"; } 
    else 
    { ename = e.getName(); }

    String tname = ename + "*"; 
    if (Type.isBasicType(e)) 
    { tname = e.getCPP("void*"); } 
    else if ("Sequence".equals(ename))
    { tname = e.getCPP(e.getElementType()); } 
    else if ("Set".equals(ename))
    { tname = e.getCPP(e.getElementType()); } 

    String argtype1 = "std::vector<" + tname + ">"; 
    String argtype2 = "std::set<" + tname + ">"; 

    Type re = exp.getType(); 
    String restype = "void*"; 
    if (re != null)
    { restype = re.getCPP(exp.getElementType()); } 

    String pp = "" + exp + " " + ename + "(" + signature + ")"; 
    String op = (String) collectOps.get(pp); 
    // But may be different left element type with same expression

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (collectvar != null)
      { var = collectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res1 = "  static vector<" + restype + ">* collect_" + oldindex + 
                    "(" + argtype1 + "*  _l"; 
      String res2 = "  static vector<" + restype + ">* collect_" + oldindex + 
                    "(" + argtype2 + "*  _l"; 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String partype = par.getType().getCPP(par.getElementType());  
        res1 = res1 + ", " + partype + " " + par.getName(); 
        res2 = res2 + ", " + partype + " " + par.getName(); 
      } 

      res1 = res1 + ")\n"; 
      res1 = res1 + 
            "  { // Implements: " + left + "->collect( " + var + " | " + exp + " )\n" + 
            "    vector<" + restype + ">* _results_" + oldindex + 
                         " = new vector<" + restype + ">();\n" + 
            "    for (int _icollect = 0; _icollect < _l->size(); _icollect++)\n" + 
            "    { " + tname + " " + var + " = (*_l)[_icollect];\n"; 

      res2 = res2 + ")\n"; 
      res2 = res2 + "  { vector<" + restype + ">* _results_" + oldindex + 
                         " = new vector<" + restype + ">();\n" + 
            "    for (" + argtype2 + "::iterator _icollect = _l->begin(); _icollect != _l->end(); ++_icollect)\n" + 
            "    { " + tname + " " + var + " = *_icollect;\n"; 

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (collectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); }
      String elem = exp.queryFormCPP(newenv,false); 

      res1 = res1 + "     _results_" + oldindex + "->push_back(" + elem + ");\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _results_" + oldindex + ";\n  }"; 

      res2 = res2 + "     _results_" + oldindex + "->push_back(" + elem + ");\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _results_" + oldindex + ";\n  }"; 

      collectList.add(pp); 
      collectOps.put(pp,res1 + "\n\n" + res2); 
      collectCodes.put(pp,"" + oldindex); 

      return "collect_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) collectCodes.get(pp); 
      return "collect_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getAnyDefinitionCPP(Expression left, String lqf,
                                           Expression exp,
                                           String collectvar, java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller::inst->get" + instances + "()"; 
    }         


    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "void"; } 
    else 
    { ename = e.getName(); }

    String tname = ename + "*"; 
    if (Type.isBasicType(e)) 
    { tname = e.getCPP("void*"); } 
    else if ("Sequence".equals(ename))
    { tname = e.getCPP(e.getElementType()); } 
    else if ("Set".equals(ename))
    { tname = e.getCPP(e.getElementType()); } 

    String argtype1 = "std::vector<" + tname + ">"; 
    String argtype2 = "std::set<" + tname + ">"; 

    Type re = left.getElementType(); 
    String restype = "void*"; 
    if (re != null)
    { restype = re.getCPP(re.getElementType()); } 

    String pp = "" + exp + " " + ename + "(" + signature + ")"; 
    String op = (String) anyOps.get(pp); 
    // But may be different left element type with same expression

    // System.out.println(">>> CPP Any definition " + op); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (collectvar != null)
      { var = collectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (collectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); }
      String elem = exp.queryFormCPP(newenv,false); 

      String res1 = "  static " + restype + " any_" + oldindex + 
                    "(" + argtype1 + "*  _l"; 
      String res2 = "  static " + restype + " any_" + oldindex + 
                    "(" + argtype2 + "*  _l"; 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String partype = par.getType().getCPP(par.getElementType());  
        res1 = res1 + ", " + partype + " " + par.getName(); 
        res2 = res2 + ", " + partype + " " + par.getName(); 
      } 

      res1 = res1 + ")\n"; 

      if (ename.equals("int"))  // for Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCPP(env,false); 
            String endexpqf = endexp.queryFormCPP(env,false); 
            String inttest = exp.queryFormCPP(env,false);
            res1 = res1 + "  { // Implements: " + left + "->any(" + var + "|" + exp + ")\n" + 
                   "    for (int " + var + " = " + startexpqf + "; " + 
                   var + " <= " + endexpqf + "; " + var + "++)\n" + 
                   "    { if (" + inttest + ") { return " + var + "; } }\n"; 
            res1 = res1 + "    return 0;\n  }";
            
            anyList.add(pp); 
            anyOps.put(pp,res1); 
            // anyDecs.put(pp,decl1); 
            anyCodes.put(pp,"" + oldindex); 

            System.out.println(">>> CPP Any definition " + res1); 
           
            return "any_" + oldindex + "(NULL)"; 
          }
        }
      } 

      res1 = res1 + 
            "  { // Implements: " + left + "->any( " + var + " | " + exp + " )\n" + 
            "    " + restype + " _result_" + oldindex + 
                         " = NULL;\n" + 
            "    for (int _iany = 0; _iany < _l->size(); _iany++)\n" + 
            "    { " + tname + " " + var + " = (*_l)[_iany];\n"; 

      res2 = res2 + ")\n"; 
      res2 = res2 + "  { " + restype + " _result_" + oldindex + 
                         " = NULL;\n" + 
            "    for (" + argtype2 + "::iterator _iany = _l->begin(); _iany != _l->end(); ++_iany)\n" + 
            "    { " + tname + " " + var + " = *_iany;\n"; 


      res1 = res1 + "     if (" + elem + ") { return " + var +  "; }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _result_" + oldindex + ";\n  }"; 

      res2 = res2 + "     if (" + elem + ") { return " + var + "; }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _result_" + oldindex + ";\n  }"; 

      anyList.add(pp); 
      anyOps.put(pp,res1 + "\n\n" + res2); 
      anyCodes.put(pp,"" + oldindex); 

      System.out.println(">>> CPP Any definition " + res1 + "\n\n" + res2); 


      return "any_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) anyCodes.get(pp); 
      return "any_" + ind + "(" + lqf + ")"; 
    } 
  } 


  // distinguish cases where elem is an object and primitive: wrap(exp.getType(), 
  public static String getRejectDefinition(Expression left, String lqf,
                                           Expression pred, String selectvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set") || ename.equals("Sequence"))
    { tname = "List"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapper(); } 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) rejectOps.get(pp); 
    // But may be different left element type with same pred


    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      String res = "  public static List reject_" + oldindex + "(List _l"; 
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      res = res + "  { // implements: " + left + "->reject( " +  var + " | " + pred + " )\n" +
                  "    List _results_" + oldindex + 
                                    " = new Vector();\n" + 
                  "    for (int _i = 0; _i < _l.size(); _i++)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res = res +  
                  "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n"; 
      } 

      String test = pred.queryForm(newenv,false); 
      String wvar = Expression.wrap(e,var); 
      res = res + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".add(" + wvar + "); }\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }\n\n";
	  
	  /* Version for maps: */  

	  res = res + "  public static Map reject_" + oldindex + "(Map _l"; 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      res = res + "  { // Implements: " + left + "->reject(" + var + " | " + pred + ")\n" + 
                  "    Map _results_" + oldindex + " = new java.util.HashMap();\n" +
				  "    java.util.Set _keys = _l.keySet();\n" +  
                  "    for (Object _i : _keys)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res = res + "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n"; }
	     
      res = res + "      if (" + test + ") { } \n" + 
                  "      else\n" +  
                  "      { _results_" + oldindex + ".put(_i, " + wvar + "); }\n"; 
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }\n"; 


      rejectList.add(pp); 
      rejectOps.put(pp,res); 
      rejectCodes.put(pp,"" + oldindex); 

      return "reject_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) rejectCodes.get(pp); 
      return "reject_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getRejectDefinitionJava6(Expression left, String lqf,
                                           Expression pred, String selectvar,
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "HashSet"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava6(); } 

    String restype1 = "ArrayList"; 
    String restype2 = "HashSet"; 
    // Both versions are needed. 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) rejectOps.get(pp); 
    // But may be different left element type with same pred


    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); } 

      String res1 = "  public static " + restype1 + " reject_" + oldindex + "(ArrayList _l"; 
      String res2 = "  public static " + restype2 + " reject_" + oldindex + "(HashSet _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res1 = res1 + ", " + par.getType().getJava6() + " " + par.getName(); 
        res2 = res2 + ", " + par.getType().getJava6() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res1 = res1 + ")\n"; 
      res1 = res1 + "  { // implements: " + left + "->reject( " +  var + " | " + pred + " )\n" +
                  "    " + restype1 + " _results_" + oldindex + 
                                    " = new " + restype1 + "();\n" + 
                  "    for (int _i = 0; i < _l.size(); _i++)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res1 = res1 + "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res1 = res1 + "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res1 = res1 + "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res1 = res1 + "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; } 
      else 
      { res1 = res1 +  
                  "    { " + tname + " " + var + " = (" + tname + ") _l.get(_i);\n";
      }  
      String test = pred.queryFormJava6(newenv,false); 
      res1 = res1 + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _results_" + oldindex + ";\n  }"; 

      res2 = res2 + ")\n"; 
      res2 = res2 + "  { // implements: " + left + "->reject( " +  var + " | " + pred + " )\n" +
                  "    " + restype2 + " _results_" + oldindex + 
                                    " = new " + restype2 + "();\n" + 
                  "    for (Object _i : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res2 = res2 + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res2 = res2 + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res2 = res2 + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res2 = res2 + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res2 = res2 +  
                  "    { " + tname + " " + var + " = (" + tname + ") _i;\n";
      }  
      res2 = res2 + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _results_" + oldindex + ";\n  }"; 

      rejectList.add(pp); 
      rejectOps.put(pp,res1 + "\n\n" + res2); 
      rejectCodes.put(pp,"" + oldindex); 

      return "reject_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) rejectCodes.get(pp); 
      return "reject_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getRejectDefinitionJava7(
            Expression left, String lqf,
            Expression pred, String selectvar,
            java.util.Map env,
            Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "Set"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; 
      if (e != null)
      { tname = e.getJava7(); }
    } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava7(); } 

    String partype1 = "List<" + tname + ">"; 
    String restype1 = "ArrayList<" + tname + ">"; 
    String restype2 = "HashSet<" + tname + ">"; 
    String restype3 = "TreeSet<" + tname + ">"; 
    String restype4 = "Set<" + tname + ">"; 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) rejectOps.get(pp); 
    // But may be different left element type with same pred


    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity())
      { newenv.put(ename,var); } 

      String res1 = "  public static " + restype1 + " reject_" + oldindex + "(" + partype1 + " _l"; 
      String res2 = "  public static " + restype2 + " reject_" + oldindex + "(" + restype2 + " _l"; 
      String res3 = "  public static " + restype3 + " reject_" + oldindex + "(" + restype3 + " _l"; 
      String res4 = "  public static " + restype2 + " reject_" + oldindex + "(" + restype4 + " _l"; 
      
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype != null) 
        { String jType = 
            partype.getJava7(partype.getElementType()); 
          res1 = res1 + ", " + jType + " " + par.getName(); 
          res2 = res2 + ", " + jType + " " + par.getName(); 
          res3 = res3 + ", " + jType + " " + par.getName(); 
          res4 = res4 + ", " + jType + " " + par.getName(); 
        } 
      } 

      res1 = res1 + ")\n"; 
      res1 = res1 + "  { // implements: " + left + "->reject( " +  var + " | " + pred + " )\n" +
                  "    " + restype1 + " _results_" + oldindex + 
                                    " = new " + restype1 + "();\n" + 
                  "    for (int _ireject = 0; _ireject < _l.size(); _ireject++)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res1 = res1 + "    { int " + var + " = ((Integer) _l.get(_ireject)).intValue();\n"; } 
      else if (ename.equals("double"))
      { res1 = res1 + "    { double " + var + " = ((Double) _l.get(_ireject)).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res1 = res1 + "    { long " + var + " = ((Long) _l.get(_ireject)).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res1 = res1 + "    { boolean " + var + " = ((Boolean) _l.get(_ireject)).booleanValue();\n"; } 
      else 
      { res1 = res1 +  
                  "    { " + tname + " " + var + " = (" + tname + ") _l.get(_ireject);\n";
      }  

      res2 = res2 + ")\n"; 
      res2 = res2 + "  { " + restype2 + " _results_" + oldindex + 
                                    " = new " + restype2 + "();\n" + 
                  "    for (" + tname + " _ireject : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res2 = res2 + "    { int " + var + " = ((Integer) _ireject).intValue();\n"; } 
      else if (ename.equals("double"))
      { res2 = res2 + "    { double " + var + " = ((Double) _ireject).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res2 = res2 + "    { long " + var + " = ((Long) _ireject).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res2 = res2 + "    { boolean " + var + " = ((Boolean) _ireject).booleanValue();\n"; } 
      else 
      { res2 = res2 +  
                  "    { " + tname + " " + var + " = (" + tname + ") _ireject;\n";
      }  

      res3 = res3 + ")\n"; 
      res3 = res3 + "  { " + restype3 + " _results_" + oldindex + 
                                    " = new " + restype3 + "();\n" + 
                  "    for (" + tname + " _ireject : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res3 = res3 + "    { int " + var + " = ((Integer) _ireject).intValue();\n"; } 
      else if (ename.equals("double"))
      { res3 = res3 + "    { double " + var + " = ((Double) _ireject).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res3 = res3 + "    { long " + var + " = ((Long) _ireject).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res3 = res3 + "    { boolean " + var + " = ((Boolean) _ireject).booleanValue();\n"; } 
      else 
      { res3 = res3 +  
                  "    { " + tname + " " + var + " = (" + tname + ") _ireject;\n";
      }  

      res4 = res4 + ")\n"; 
      res4 = res4 + "  { " + restype2 + " _results_" + oldindex + 
                                    " = new " + restype2 + "();\n" + 
                  "    for (" + tname + " _ireject : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res4 = res4 + "    { int " + var + " = ((Integer) _ireject).intValue();\n"; } 
      else if (ename.equals("double"))
      { res4 = res4 + "    { double " + var + " = ((Double) _ireject).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res4 = res4 + "    { long " + var + " = ((Long) _ireject).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res4 = res4 + "    { boolean " + var + " = ((Boolean) _ireject).booleanValue();\n"; } 
      else 
      { res4 = res4 +  
                  "    { " + tname + " " + var + " = (" + tname + ") _ireject;\n";
      }  

      String test = pred.queryFormJava7(newenv,false); 

      res1 = res1 + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _results_" + oldindex + ";\n  }"; 

      res2 = res2 + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _results_" + oldindex + ";\n  }"; 

      res3 = res3 + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res3 = res3 + "    }\n"; 
      res3 = res3 + "    return _results_" + oldindex + ";\n  }"; 

      res4 = res4 + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".add(" + var + "); }\n"; 
      res4 = res4 + "    }\n"; 
      res4 = res4 + "    return _results_" + oldindex + ";\n  }"; 

      rejectList.add(pp); 
      rejectOps.put(pp,res1 + "\n\n" + res2 + "\n\n" + res3 + "\n\n" + res4); 
      rejectCodes.put(pp,"" + oldindex); 

      return "reject_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) rejectCodes.get(pp); 
      return "reject_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getRejectDefinitionCSharp(Expression left, String lqf,
                                           Expression pred, String selectvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left))
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller.inst().get" + instances + "()"; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set") || ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("String"))
    { tname = "string"; } 
    else if (ename.equals("boolean"))
    { tname = "bool"; } 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) rejectOps.get(pp); 
    // But may be different left element type with same pred


    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 

      String res = "  public static ArrayList reject_" + oldindex + "(ArrayList _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getCSharp() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      res = res + "  { // implements: " + left + "->reject( " +  var + " | " + pred + " )\n\n" +
                  "    ArrayList _results_" + oldindex + 
                                    " = new ArrayList();\n" + 
                  "    for (int _ireject = 0; _ireject < _l.Count; _ireject++)\n" + 
                  "    { " + tname + " " + var + " = (" + tname + ") _l[_ireject];\n";

      String test = ""; 
      if (Expression.isLambdaApplication(pred))
      { Expression subs = Expression.simplifyApply(pred);
        test = subs.queryFormCSharp(newenv,false);
      } 
      else
      { test = pred.queryFormCSharp(newenv,false); }  
 
      res = res + "      if (" + test + ") { } \n" + 
                  "      else { _results_" + oldindex + ".Add(" + var + "); }\n"; 
      
      res = res + "    }\n"; 
      res = res + "    return _results_" + oldindex + ";\n  }"; 

      rejectList.add(pp); 
      rejectOps.put(pp,res); 
      rejectCodes.put(pp,"" + oldindex); 

      return "reject_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) rejectCodes.get(pp); 
      return "reject_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getRejectDefinitionCPP(Expression left, String lqf,
                                              Expression pred, String selectvar,
                                              java.util.Map env,
                                              Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller::inst->get" + instances + "()"; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "void"; } 
    else 
    { ename = e.getName(); }

    String tname = ename + "*"; 
    /* if (Type.isBasicType(e)) 
    { tname = e.getCPP("void*"); } 
    else if ("Sequence".equals(ename))
    { tname = "vector<void*>*"; } 
    else if ("Set".equals(ename))
    { tname = "set<void*>*"; } */ 
    if (e != null) 
    { tname = e.getCPP(e.getElementType()); } 

    String restype1 = "std::set<" + tname + ">"; 
    String addop1 = "insert"; 
    String restype2 = "std::vector<" + tname + ">"; 
    String addop2 = "push_back"; 
   
    // System.out.println(left + " element type is " + e); 

    String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) rejectOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 

      int oldindex = index; 
      index++; 

      String var; 
      if (selectvar != null)
      { var = selectvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (selectvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 

      String res1 = "  static " + restype1 + "* reject_" + oldindex + "(" + restype1 + "* _l"; 
      String res2 = "  static " + restype2 + "* reject_" + oldindex + "(" + restype2 + "* _l"; 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String cpppt = par.getType().getCPP(par.getElementType());  
        res1 = res1 + ", " + cpppt + " " + par.getName();
        res2 = res2 + ", " + cpppt + " " + par.getName();
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res1 = res1 + ")\n"; 
      res1 = res1 + "  { // implements: " + left + "->reject( " +  var + " | " + pred + " )\n" +
                  "    " + restype1 + "* _results_" + oldindex + " = new " + restype1 + "();\n" + 
                  "    for (" + restype1 + "::iterator _ireject = _l->begin(); _ireject != _l->end(); ++_ireject)\n" + 
                  "    { " + tname + " " + var + " = *_ireject;\n"; 
      res2 = res2 + ")\n"; 
      res2 = res2 + "  { " + restype2 + "* _results_" + oldindex + " = new " + restype2 + "();\n" + 
                  "    for (" + restype2 + "::iterator _ireject = _l->begin(); _ireject != _l->end(); ++_ireject)\n" + 
                  "    { " + tname + " " + var + " = *_ireject;\n"; 

      String test = pred.queryFormCPP(newenv,false); 
      res1 = res1 + "      if (" + test + ") { }\n" + 
                    "      else { _results_" + oldindex + "->" + addop1 + "(" + var + "); }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return _results_" + oldindex + ";\n  }"; 
      res2 = res2 + "      if (" + test + ") { }\n" + 
                    "      else { _results_" + oldindex + "->" + addop2 + "(" + var + "); }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return _results_" + oldindex + ";\n  }"; 

      String res = res1 + "\n\n" + res2; 

      rejectList.add(pp); 
      rejectOps.put(pp,res); 
      rejectCodes.put(pp,"" + oldindex); 

      return "reject_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) rejectCodes.get(pp); 
      return "reject_" + ind + "(" + lqf + ")"; 
    } 
  } 

  // For:  left->any(pred)
  public static String getAnyDefinition(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left == null) 
    { System.err.println("!!!! ERROR: Null quantifier range: " + lqf + "->any(" + 
                          exvar + "|" + pred + ")");
      return "/* error in ->any */"; 
    }  

    if (Expression.isSimpleEntity(left))
    { BasicExpression bel = (BasicExpression) left; 
      String instances = bel.data.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); // left is null. 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else if ("Set".equals(e.getName()) || "Sequence".equals(e.getName()))
    { ename = "List"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null)
    { tname = e.typeWrapper(); } 

    String pp = lqf + " " + pred + " " + tname + "(" + signature + ")"; 
    String op = (String) anyOps.get(pp); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static " + tname + " any_" + oldindex + "(List _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype != null) 
        { res = res + ", " + partype.getJava() + " " + par.getName(); } 
        else 
        { res = res + ", Object " + par.getName(); } 
 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryForm(env,false); 
            String endexpqf = endexp.queryForm(env,false); 
            String inttest = pred.queryForm(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return new Integer(" + var + "); } }\n"; 
            res = res + "    return null;\n  }";

            anyList.add(pp); 
            anyOps.put(pp,res); 
            anyCodes.put(pp,"" + oldindex); 
            return "any_" + oldindex + "(null)"; 
          } 
        } 
      } 


      res = res + "  { // Implements: " + left + "->any(" + var + "|" + pred + ")\n" + 
                  "    for (int _iany = 0; _iany < _l.size(); _i++)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res + 
            "    { int " + var + " = ((Integer) _l.get(_iany)).intValue();\n";
      } 
      else if ("double".equals(ename))
      { res = res + 
            "    { double " + var + " = ((Double) _l.get(_iany)).doubleValue();\n";
      } 
      else if (ename.equals("boolean"))
      { res = res + 
            "    { boolean " + var + " = ((Boolean) _l.get(_iany)).booleanValue();\n"; 
      } 
      else if ("long".equals(ename))
      { res = res + 
            "    { long " + var + " = ((Long) _l.get(_iany)).longValue();\n";
      } 
      else
      { res = res + 
            "    { " + tname + " " + var + " = (" + tname + ") _l.get(_iany);\n";
      } 


 
      String test = pred.queryForm(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { return (" + tname + ") _l.get(_iany); }\n"; 
      res = res + "    }\n"; 
      res = res + "    return null;\n  }"; 

      anyList.add(pp); 
      anyOps.put(pp,res); 
      anyCodes.put(pp,"" + oldindex); 

      return "any_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) anyCodes.get(pp); 
      return "any_" + ind + "(" + lqf + ")"; 
    } 
  } 

  // For:  left->any(pred)
  public static String getAnyDefinitionJava6(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left == null) 
    { System.err.println("!!!! Null quantifier range: " + lqf + "->any(" + 
                          exvar + "|" + pred + ")");
      return "/* error in ->any */"; 
    }  

    if (Expression.isSimpleEntity(left))
    { BasicExpression bel = (BasicExpression) left; 
      String instances = bel.data.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); // left is null. 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else if ("Set".equals(e.getName()))
    { ename = "HashSet"; } 
    else if ("Sequence".equals(e.getName()))
    { ename = "ArrayList"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null)
    { tname = e.typeWrapperJava6(); } 

    String pp = lqf + " " + pred + " " + tname + "(" + signature + ")"; 
    String op = (String) anyOps.get(pp); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static " + tname + " any_" + oldindex + "(Collection _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype != null) 
        { res = res + ", " + partype.getJava6() + " " + par.getName(); } 
        else 
        { res = res + ", Object " + par.getName(); } 
 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava6(env,false); 
            String endexpqf = endexp.queryFormJava6(env,false); 
            String inttest = pred.queryFormJava6(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return new Integer(" + var + "); } }\n"; 
            res = res + "    return null;\n  }";

            anyList.add(pp); 
            anyOps.put(pp,res); 
            anyCodes.put(pp,"" + oldindex); 
            return "any_" + oldindex + "(null)"; 
          } 
        } 
      } 


      res = res + "  { // Implements: " + left + "->any(" + var + "|" + pred + ")\n" + 
                  "    for (Object _iany : _l)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res + 
            "    { int " + var + " = ((Integer) _iany).intValue();\n";
      } 
      else if ("double".equals(ename))
      { res = res + 
            "    { double " + var + " = ((Double) _iany).doubleValue();\n";
      } 
      else if (ename.equals("boolean"))
      { res = res + 
            "    { boolean " + var + " = ((Boolean) _iany).booleanValue();\n"; 
      } 
      else if ("long".equals(ename))
      { res = res + 
            "    { long " + var + " = ((Long) _iany).longValue();\n";
      } 
      else
      { res = res + 
            "    { " + tname + " " + var + " = (" + tname + ") _iany;\n";
      } 


 
      String test = pred.queryFormJava6(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { return (" + tname + ") _iany; }\n"; 
      res = res + "    }\n"; 
      res = res + "    return null;\n  }"; 

      anyList.add(pp); 
      anyOps.put(pp,res); 
      anyCodes.put(pp,"" + oldindex); 

      return "any_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) anyCodes.get(pp); 
      return "any_" + ind + "(" + lqf + ")"; 
    } 
  } 
  
  // For:  left->any(pred)
  public static String getAnyDefinitionJava7(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left == null) 
    { System.err.println("!!!! Null quantifier range: " + lqf + "->any(" + 
                          exvar + "|" + pred + ")");
      return "/* error in ->any */"; 
    }  

    if (Expression.isSimpleEntity(left))
    { BasicExpression bel = (BasicExpression) left; 
      String instances = bel.data.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); // left is null. 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else if ("Set".equals(e.getName()))
    { ename = "HashSet"; } 
    else if ("Sequence".equals(e.getName()))
    { ename = "ArrayList"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null)
    { tname = e.typeWrapperJava7(); } 

    String pp = lqf + " " + pred + " " + tname + "(" + signature + ")"; 
    String op = (String) anyOps.get(pp); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static " + tname + " any_" + oldindex + "(Collection<" + tname + "> _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype != null) 
        { res = res + ", " + partype.getJava7() + " " + par.getName(); } 
        else 
        { res = res + ", Object " + par.getName(); } 
 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava7(env,false); 
            String endexpqf = endexp.queryFormJava7(env,false); 
            String inttest = pred.queryFormJava7(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return new Integer(" + var + "); } }\n"; 
            res = res + "    return null;\n  }";

            anyList.add(pp); 
            anyOps.put(pp,res); 
            anyCodes.put(pp,"" + oldindex); 
            return "any_" + oldindex + "(null)"; 
          } 
        } 
      } 


      res = res + "  { // Implements: " + left + "->any(" + var + "|" + pred + ")\n" + 
                  "    for (Object _iany : _l)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res + 
            "    { int " + var + " = ((Integer) _iany).intValue();\n";
      } 
      else if ("double".equals(ename))
      { res = res + 
            "    { double " + var + " = ((Double) _iany).doubleValue();\n";
      } 
      else if (ename.equals("boolean"))
      { res = res + 
            "    { boolean " + var + " = ((Boolean) _iany).booleanValue();\n"; 
      } 
      else if ("long".equals(ename))
      { res = res + 
            "    { long " + var + " = ((Long) _iany).longValue();\n";
      } 
      else
      { res = res + 
            "    { " + tname + " " + var + " = (" + tname + ") _iany;\n";
      } 


 
      String test = pred.queryFormJava7(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { return (" + tname + ") _iany; }\n"; 
      res = res + "    }\n"; 
      res = res + "    return null;\n  }"; 

      anyList.add(pp); 
      anyOps.put(pp,res); 
      anyCodes.put(pp,"" + oldindex); 

      return "any_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) anyCodes.get(pp); 
      return "any_" + ind + "(" + lqf + ")"; 
    } 
  } 
  
  // For:  left->any(pred)
  public static String getAnyDefinitionCSharp(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left == null) 
    { System.err.println("!!!! Null quantifier range: " + lqf + "->any(" + 
                          exvar + "|" + pred + ")");
      return "/* error in ->any */"; 
    }  

    if (Expression.isSimpleEntity(left))
    { BasicExpression bel = (BasicExpression) left; 
      String instances = bel.data.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "object"; } 
    else if ("Set".equals(e.getName()))
    { ename = "ArrayList"; } 
    else if ("Sequence".equals(e.getName()))
    { ename = "ArrayList"; } 
    else
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("String"))
    { tname = "string"; } 
    else if (ename.equals("boolean"))
    { tname = "bool"; } 
    
    // if (ename.equals("int"))
    // { tname = "Integer"; } 
    // else if (e != null)
    // { tname = e.typeWrapperCSharp(); } 

    String pp = lqf + " " + pred + " " + tname + "(" + signature + ")"; 
    String op = (String) anyOps.get(pp); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static " + tname + " any_" + oldindex + "(ArrayList _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype != null) 
        { res = res + ", " + partype.getCSharp() + " " + par.getName(); } 
        else 
        { res = res + ", Object " + par.getName(); } 
 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCSharp(env,false); 
            String endexpqf = endexp.queryFormCSharp(env,false); 
            String inttest = pred.queryFormCSharp(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return new Integer(" + var + "); } }\n"; 
            res = res + "    return null;\n  }";

            anyList.add(pp); 
            anyOps.put(pp,res); 
            anyCodes.put(pp,"" + oldindex); 
            return "any_" + oldindex + "(null)"; 
          } 
        } 
      } 


      res = res + "  { // Implements: " + left + "->any(" + var + "|" + pred + ")\n" + 
                  "    for (int _iany = 0; _iany < _l.Count; _iany++)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res + 
            "    { int " + var + " = (int) _l[_iany];\n";
      } 
      else if ("double".equals(ename))
      { res = res + 
            "    { double " + var + " = (double) _l[_iany];\n";
      } 
      else if (ename.equals("boolean"))
      { res = res + 
            "    { bool " + var + " = (bool) _l[_iany];\n"; 
      } 
      else if ("long".equals(ename))
      { res = res + 
            "    { long " + var + " = (long) _l[_iany];\n";
      } 
      else
      { res = res + 
            "    { " + tname + " " + var + " = (" + tname + ") _l[_iany];\n";
      } 


 
      String test = pred.queryFormCSharp(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
	              "      { return (" + tname + ") _l[_iany]; }\n"; 
      res = res + "    }\n"; 
      res = res + "    return null;\n  }"; 

      anyList.add(pp); 
      anyOps.put(pp,res); 
      anyCodes.put(pp,"" + oldindex); 

      return "any_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) anyCodes.get(pp); 
      return "any_" + ind + "(" + lqf + ")"; 
    } 
  } 

  // For:  left->exists(pred)
  public static String getExistsDefinition(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left == null) 
    { System.err.println("!!!! Null quantifier range: " + lqf + "->exists(" + 
                          exvar + "|" + pred + ")");
      return "/* error in ->exists */"; 
    }  

    if (Expression.isSimpleEntity(left))
    { BasicExpression bel = (BasicExpression) left; 
      String instances = bel.data.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); // left is null. 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else if ("Set".equals(e.getName()) || "Sequence".equals(e.getName()))
    { ename = "List"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null)
    { tname = e.typeWrapper(); } 

    String pp = lqf + " " + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) existsOps.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean exists_" + oldindex + "(List _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype != null) 
        { res = res + ", " + partype.getJava() + " " + par.getName(); } 
        else 
        { res = res + ", Object " + par.getName(); } 
 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryForm(env,false); 
            String endexpqf = endexp.queryForm(env,false); 
            String inttest = pred.queryForm(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return true; } }\n"; 
            res = res + "    return false;\n  }";

            existsList.add(pp); 
            existsOps.put(pp,res); 
            existsCodes.put(pp,"" + oldindex); 
            return "exists_" + oldindex + "(null)"; 
          } 
        } 
      } 


      res = res + "  { // Implements: " + left + "->exists(" + var + "|" + pred + ")\n" + 
                  "    for (int _i = 0; _i < _l.size(); _i++)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res + 
            "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n";
      } 
      else if ("double".equals(ename))
      { res = res + 
            "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n";
      } 
      else if (ename.equals("boolean"))
      { res = res + 
            "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; 
      } 
      else if ("long".equals(ename))
      { res = res + 
            "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n";
      } 
      else
      { res = res + 
            "    { " + ename + " " + var + " = (" + ename + ") _l.get(_i);\n";
      } 


 
      String test = pred.queryForm(newenv,false); 
      res = res + "      if (" + test + ") { return true; }\n"; 
      res = res + "    }\n"; 
      res = res + "    return false;\n  }"; 

      existsList.add(pp); 
      existsOps.put(pp,res); 
      existsCodes.put(pp,"" + oldindex); 

      return "exists_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) existsCodes.get(pp); 
      return "exists_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExistsDefinitionJava6(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    // System.out.println("Quantifier range: " + left); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "HashSet"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava6(); } 

    String pp = lqf + " " + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) existsOps.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean exists_" + oldindex + "(Collection _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava6() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava6(env,false); 
            String endexpqf = endexp.queryFormJava6(env,false); 
            String inttest = pred.queryFormJava6(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return true; } }\n"; 
            res = res + "    return false;\n  }";

            existsList.add(pp); 
            existsOps.put(pp,res); 
            existsCodes.put(pp,"" + oldindex); 
            return "exists_" + oldindex + "(null)"; 
          }
        } 
      } 


      res = res +   "  { // Implements: " + left + "->exists(" + var + "|" + pred + ")\n" + 
                    "    for (Object _i : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res = res +  
                    "    { " + tname + " " + var + " = (" + tname + ") _i;\n";
      }  

      String test = pred.queryFormJava6(newenv,false); 
      res = res + "      if (" + test + ") { return true; }\n"; 
      res = res + "    }\n"; 
      res = res + "    return false;\n  }"; 

      existsList.add(pp); 
      existsOps.put(pp,res); 
      existsCodes.put(pp,"" + oldindex); 

      return "exists_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) existsCodes.get(pp); 
      return "exists_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExistsDefinitionJava7(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    // System.out.println("Quantifier range: " + left); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "Set"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava7(); } 

    String pp = lqf + " " + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) existsOps.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean exists_" + oldindex + "(Collection<" + tname + "> _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype == null) 
        { System.err.println("!! ERROR: no type for " + par); } 
        else 
        { res = res + ", " + 
            partype.getJava7(partype.getElementType()) + " " + par.getName(); 
        }  
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava7(env,false); 
            String endexpqf = endexp.queryFormJava7(env,false); 
            String inttest = pred.queryFormJava7(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return true; } }\n"; 
            res = res + "    return false;\n  }";

            existsList.add(pp); 
            existsOps.put(pp,res); 
            existsCodes.put(pp,"" + oldindex); 
            return "exists_" + oldindex + "(null)"; 
          }
        } 
      } 


      res = res +   "  { // Implements: " + left + "->exists(" + var + "|" + pred + ")\n" + 
                    "    for (" + tname + " _iexists : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _iexists).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _iexists).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _iexists).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _iexists).booleanValue();\n"; } 
      else 
      { res = res +  
                    "    { " + tname + " " + var + " = (" + tname + ") _iexists;\n";
      }  

      String test = pred.queryFormJava7(newenv,false); 
      res = res + "      if (" + test + ") { return true; }\n"; 
      res = res + "    }\n"; 
      res = res + "    return false;\n  }"; 

      existsList.add(pp); 
      existsOps.put(pp,res); 
      existsCodes.put(pp,"" + oldindex); 

      return "exists_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) existsCodes.get(pp); 
      return "exists_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExistsDefinitionCSharp(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    // System.out.println("Quantifier range: " + left); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller.inst().get" + instances + "()"; 
    }         

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set") || ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("String"))
    { tname = "string"; } 
    else if (ename.equals("boolean"))
    { tname = "bool"; } 

    String pp = lqf + " " + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) existsOps.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static bool exists_" + oldindex + "(ArrayList _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + ", " + par.getType().getCSharp() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCSharp(env,false); 
            String endexpqf = endexp.queryFormCSharp(env,false); 
            String inttest = pred.queryFormCSharp(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { return true; } }\n"; 
            res = res + "    return false;\n  }";

            existsList.add(pp); 
            existsOps.put(pp,res); 
            existsCodes.put(pp,"" + oldindex); 
            return "exists_" + oldindex + "(null)"; 
          }
        }
      } 


      res = res + "  { // Implements: " + left + "->exists(" + var + "|" + pred + ")\n" + 
            "    for (int _iexists = 0; _iexists < _l.Count; _iexists++)\n" + 
            "    { " + tname + " " + var + " = (" + tname + ") _l[_iexists];\n"; 
      String test = pred.queryFormCSharp(newenv,false); 
      res = res + "      if (" + test + ") { return true; }\n"; 
      res = res + "    }\n"; 
      res = res + "    return false;\n  }"; 

      existsList.add(pp); 
      existsOps.put(pp,res); 
      existsCodes.put(pp,"" + oldindex); 

      return "exists_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) existsCodes.get(pp); 
      return "exists_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExistsDefinitionCPP(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    // System.out.println("Quantifier range: " + left); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller::inst()->get" + instances + "()"; 
    }         


    String pp = lqf + " " + pred + "(" + signature + ")"; 
    String op = (String) existsOps.get(pp); 
    // System.out.println("Exists definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      String ename; 
      String argtype; 
      Type e = left.getElementType(); 
      if (e == null || "OclAny".equals(e.getName())) 
      { ename = "void"; 
        argtype = ename + "*"; 
      } 
      else if ("Set".equals(e.getName()))
      { ename = "set"; 
        argtype = e.getCPP(e.getElementType());  
      } 
      else if ("Sequence".equals(e.getName()))
      { ename = "vector";  
        argtype = e.getCPP(e.getElementType());  
      } 
      else if (e.isEntity()) 
      { ename = e.getName(); 
        argtype = ename + "*"; 
      }
      else if ("String".equals(e.getName()))
      { ename = e.getName(); 
        argtype = "string"; 
      }
      else if ("boolean".equals(e.getName()))
      { ename = e.getName(); 
        argtype = "bool"; 
      } 
      else // primitive types, enumerations
      { ename = e.getName(); 
        argtype = ename; 
      } // bool case?

    
      String cet = argtype; 


      String argtype1 = "std::vector<" + argtype + ">"; 
      String argtype2 = "std::set<" + argtype + ">"; 

      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res1 = "  static bool exists_" + oldindex + "(" + argtype1 + "* _l"; 
      String decl1 = "  static bool exists_" + oldindex + "(" + argtype1 + "* _l"; 
      String res2 = "  static bool exists_" + oldindex + "(" + argtype2 + "* _l"; 
      String decl2 = "  static bool exists_" + oldindex + "(" + argtype2 + "* _l"; 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String cpptype = par.getType().getCPP(par.getElementType());  
        res1 = res1 + ", " + cpptype + " " + par.getName(); 
        decl1 = decl1 + ", " + cpptype + " " + par.getName();
        res2 = res2 + ", " + cpptype + " " + par.getName(); 
        decl2 = decl2 + ", " + cpptype + " " + par.getName();
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res1 = res1 + ")\n"; 
      decl1 = decl1 + ");\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCPP(env,false); 
            String endexpqf = endexp.queryFormCPP(env,false); 
            String inttest = pred.queryFormCPP(env,false);
            res1 = res1 + "  { // Implements: " + left + "->exists(" + var + "|" + pred + ")\n" + 
                        "    for (int " + var + " = " + startexpqf + "; " + 
                                  var + " <= " + endexpqf + "; " + var + "++)\n" + 
                        "    { if (" + inttest + ") { return true; } }\n"; 
            res1 = res1 + "    return false;\n  }";
            
            existsList.add(pp); 
            existsOps.put(pp,res1); 
            existsDecs.put(pp,decl1); 
            existsCodes.put(pp,"" + oldindex); 
           
            return "exists_" + oldindex + "(NULL)"; 
          }
        }
      } 


      res1 = res1 + "  { // Implements: " + left + "->exists(" + var + "|" + pred + ")\n" + 
            "    for (" + argtype1 + "::iterator _iexists = _l->begin(); _iexists != _l->end(); ++_iexists)\n" + 
            "    { " + cet + " " + var + " = *_iexists;\n";
      String test = pred.queryFormCPP(newenv,false); 
      res1 = res1 + "      if (" + test + ") { return true; }\n"; 
      res1 = res1 + "    }\n"; 
      res1 = res1 + "    return false;\n  }"; 

      res2 = res2 + "  { // Implements: " + left + "->exists(" + var + "|" + pred + ")\n" + 
            "    for (" + argtype2 + "::iterator _iexists = _l->begin(); _iexists != _l->end(); ++_iexists)\n" + 
            "    { " + cet + " " + var + " = *_iexists;\n";
      res2 = res2 + "      if (" + test + ") { return true; }\n"; 
      res2 = res2 + "    }\n"; 
      res2 = res2 + "    return false;\n  }"; 

      existsList.add(pp); 
      existsOps.put(pp,res1 + "\n\n" + res2); 
      existsDecs.put(pp,decl1 + "\n\n" + decl2); 
      existsCodes.put(pp,"" + oldindex); 

      return "exists_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) existsCodes.get(pp); 
      return "exists_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExists1Definition(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String pp = lqf + " " + pred + "(" + signature + ")"; 
    String op = (String) exists1Ops.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists1 definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      String ename; 
      Type e = left.getElementType(); 
      if (e == null || "OclAny".equals(e.getName())) 
      { ename = "Object"; } 
      else if ("Set".equals(e.getName()) || "Sequence".equals(e.getName()))
      { ename = "List"; } 
      else 
      { ename = e.getName(); }

      String tname = ename; 
      if (ename.equals("int"))
      { tname = "Integer"; } 
      else if (e != null)
      { tname = e.typeWrapper(); } 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean exists1_" + oldindex + "(List _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + ", " + par.getType().getJava() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryForm(env,false); 
            String endexpqf = endexp.queryForm(env,false); 
            String inttest = pred.queryForm(env,false);
            res = res + 
                  "  { boolean _alreadyfound = false;\n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n" + 
                  "  }\n";

            exists1List.add(pp);          
            exists1Ops.put(pp,res); 
            exists1Codes.put(pp,"" + oldindex); 
            return "exists1_" + oldindex + "(null)"; 
          }
        }
      } 


      res = res + "  { \n" + 
            "    boolean _alreadyfound = false;\n" + 
            "    for (int _i = 0; _i < _l.size(); _i++)\n"; 
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res + 
            "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n";
      } 
      else if ("double".equals(ename))
      { res = res + 
            "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n";
      } 
      else if ("long".equals(ename))
      { res = res + 
            "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n";
      } 
      else if (ename.equals("boolean"))
      { res = res + 
            "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; 
      } 
      else
      { res = res + 
            "    { " + ename + " " + var + " = (" + ename + ") _l.get(_i);\n";
      } 

      String test = pred.queryForm(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n"; 
      res = res + "  }"; 

      exists1List.add(pp); 
      exists1Ops.put(pp,res); 
      exists1Codes.put(pp,"" + oldindex); 

      return "exists1_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) exists1Codes.get(pp); 
      return "exists1_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExists1DefinitionJava6(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         


    // System.out.println("Exists1 definition for " + pp + " is " + op); 

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String pp = lqf + " " + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) exists1Ops.get(pp); 
    // But may be different left element type with same pred

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "HashSet"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava6(); } 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean exists1_" + oldindex + "(Collection _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + ", " + par.getType().getJava6() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava6(env,false); 
            String endexpqf = endexp.queryFormJava6(env,false); 
            String inttest = pred.queryFormJava6(env,false);
            res = res + "  { boolean _alreadyfound = false;\n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n" + 
                  "  }\n";

            exists1List.add(pp);          
            exists1Ops.put(pp,res); 
            exists1Codes.put(pp,"" + oldindex); 
            return "exists1_" + oldindex + "(null)"; 
          }
        }
      } 


      res = res +   "  { \n" + 
                    "    boolean _alreadyfound = false;\n" + 
                    "    for (Object _i : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res = res +  
                    "    { " + tname + " " + var + " = (" + tname + ") _i;\n";
      }  
      
      String test = pred.queryFormJava6(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n"; 
      res = res + "  }"; 

      exists1List.add(pp); 
      exists1Ops.put(pp,res); 
      exists1Codes.put(pp,"" + oldindex); 

      return "exists1_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) exists1Codes.get(pp); 
      return "exists1_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExists1DefinitionJava7(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left.umlkind == Expression.CLASSID && (left instanceof BasicExpression) &&
        ((BasicExpression) left).arrayIndex == null) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    String op = (String) exists1Ops.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists1 definition for " + pp + " is " + op); 

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "Set"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava7(); } 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean exists1_" + oldindex + "(Collection<" + tname + "> _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype == null) 
        { System.err.println("!! ERROR: no type for " + par); } 
        else 
        { res = res + "," + 
            partype.getJava7(partype.getElementType()) + " " + par.getName(); 
        } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava7(env,false); 
            String endexpqf = endexp.queryFormJava7(env,false); 
            String inttest = pred.queryFormJava7(env,false);
            res = res + "  { boolean _alreadyfound = false;\n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n" + 
                  "  }\n";

            exists1List.add(pp);          
            exists1Ops.put(pp,res); 
            exists1Codes.put(pp,"" + oldindex); 
            return "exists1_" + oldindex + "(null)"; 
          }
        }
      } 


      res = res +   "  { \n" + 
                    "    boolean _alreadyfound = false;\n" + 
                    "    for (" + tname + " _i : _l)\n";  
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res = res +  
                    "    { " + tname + " " + var + " = (" + tname + ") _i;\n";
      }  
      
      String test = pred.queryFormJava7(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n"; 
      res = res + "  }"; 

      exists1List.add(pp); 
      exists1Ops.put(pp,res); 
      exists1Codes.put(pp,"" + oldindex); 

      return "exists1_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) exists1Codes.get(pp); 
      return "exists1_" + ind + "(" + lqf + ")"; 
    } 
  } 


  public static String getExists1DefinitionCSharp(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left))
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller.inst().get" + instances + "()"; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    String op = (String) exists1Ops.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists1 definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      String ename; 
      Type e = left.getElementType(); 
      if (e == null || "OclAny".equals(e.getName())) 
      { ename = "object"; } 
      else if ("Set".equals(e.getName()) || "Sequence".equals(e.getName()))
      { ename = "ArrayList"; } 
      else if ("String".equals(e.getName()))
      { ename = "string"; }
      else if ("boolean".equals(e.getName()))
      { ename = "bool"; } 
      else 
      { ename = e.getName(); }
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static bool exists1_" + oldindex + "(ArrayList _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + ", " + par.getType().getCSharp() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCSharp(env,false); 
            String endexpqf = endexp.queryFormCSharp(env,false); 
            String inttest = pred.queryFormCSharp(env,false);
            res = res + "  { bool _alreadyfound = false;\n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n" + 
                  "  }\n";

            exists1List.add(pp);          
            exists1Ops.put(pp,res); 
            exists1Codes.put(pp,"" + oldindex); 
            return "exists1_" + oldindex + "(null)"; 
          }
        }
      } 


      res = res + "  { \n" + 
            "    bool _alreadyfound = false;\n" + 
            "    for (int _iexists1 = 0; _iexists1 < _l.Count; _iexists1++)\n" + 
            "    { " + ename + " " + var + " = (" + ename + ") _l[_iexists1];\n"; 
      String test = pred.queryFormCSharp(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n"; 
      res = res + "  }"; 

      exists1List.add(pp); 
      exists1Ops.put(pp,res); 
      exists1Codes.put(pp,"" + oldindex); 

      return "exists1_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) exists1Codes.get(pp); 
      return "exists1_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getExists1DefinitionCPP(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    // System.out.println("Quantifier range: " + left); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller::inst()->get" + instances + "()"; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    // String pp = "" + pred + " " + ename + "(" + signature + ")"; 
    String op = (String) exists1Ops.get(pp); 
    // But may be different left element type with same pred

    // System.out.println("Exists1 definition for " + pp + " is " + op); 

    if (op == null) 
    { // add new definitions 
      String ename; 
      String argtype; 
      Type e = left.getElementType(); 
      if (e == null || "OclAny".equals(e.getName())) 
      { ename = "void"; 
        argtype = ename + "*"; 
      } 
      else if ("Set".equals(e.getName()))
      { ename = "set"; 
        argtype = e.getCPP(e.getElementType()); 
      } 
      else if ("Sequence".equals(e.getName()))
      { ename = "vector";   
        argtype = e.getCPP(e.getElementType()); 
      } 
      else if (e.isEntity()) 
      { ename = e.getName(); 
        argtype = ename + "*"; 
      }
      else if ("String".equals(e.getName()))
      { ename = e.getName(); 
        argtype = "string"; 
      }
      else if ("boolean".equals(e.getName()))
      { ename = e.getName(); 
        argtype = "bool"; 
      } 
      else // primitive types, enumerations
      { ename = e.getName(); 
        argtype = ename; 
      } // bool case? 

      String cet = argtype; 


      if (left.isOrdered())
      { argtype = "std::vector<" + argtype + ">"; } 
      else 
      { argtype = "std::set<" + argtype + ">"; } 
      // Both are needed. 

      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  static bool exists_" + oldindex + "(" + argtype + "* _l"; 
      String decl = "  static bool exists_" + oldindex + "(" + argtype + "* _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String cpptype = par.getType().getCPP(par.getElementType());  
        res = res + ", " + cpptype + " " + par.getName(); 
        decl = decl + ", " + cpptype + " " + par.getName();
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      decl = decl + ");\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCPP(env,false); 
            String endexpqf = endexp.queryFormCPP(env,false); 
            String inttest = pred.queryFormCPP(env,false);
            res = res + 
                  "  { bool _alreadyfound = false;\n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n" + 
                  "  }\n";

            exists1List.add(pp); 
            exists1Ops.put(pp,res); 
            exists1Decs.put(pp,decl); 
            exists1Codes.put(pp,"" + oldindex); 
            return "exists1_" + oldindex + "(NULL)"; 
          }
        } 
      } 


      res = res + "  { bool _alreadyfound = false;\n" + 
                  "    for (" + argtype + "::iterator _iexists1 = _l->begin(); _iexists1 != _l->end(); ++_iexists1)\n" + 
                  "    { " + cet + " " + var + " = *_iexists1;\n";
      String test = pred.queryFormCPP(newenv,false); 
      res = res + "      if (" + test + ")\n" + 
                  "      { if (_alreadyfound) { return false; }\n" + 
                  "        else { _alreadyfound = true; } \n" + 
                  "      } \n" + 
                  "    }\n" +  
                  "    return _alreadyfound; \n"; 

      exists1List.add(pp);  
      exists1Ops.put(pp,res); 
      exists1Decs.put(pp,decl); 
      exists1Codes.put(pp,"" + oldindex); 

      return "exists1_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) exists1Codes.get(pp); 
      return "exists1_" + ind + "(" + lqf + ")"; 
    } 
  } 

  // For:  left->forAll(pred)
  public static String getForAllDefinition(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    String op = (String) forAllOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 
      String ename; 
      Type e = left.getElementType(); 
      if (e == null || "OclAny".equals(e.getName())) 
      { ename = "Object"; } 
      else if ("Set".equals(e.getName()) || "Sequence".equals(e.getName()))
      { ename = "List"; } 
      else 
      { ename = e.getName(); }

      String tname = ename; 
      if (ename.equals("int"))
      { tname = "Integer"; } 
      else if (e != null)
      { tname = e.typeWrapper(); } 

      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean forAll_" + oldindex + "(List _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getJava() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryForm(env,false); 
            String endexpqf = endexp.queryForm(env,false); 
            String inttest = pred.queryForm(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { }\n" + 
                  "      else { return false; }\n" + 
                  "    }\n"; 
            res = res + "    return true;\n  }";

            forAllList.add(pp); 
            forAllOps.put(pp,res); 
            forAllCodes.put(pp,"" + oldindex); 
            return "forAll_" + oldindex + "(null)";
          }
        } 
      } 

      res = res + "  { \n" + 
            "    for (int _i = 0; _i < _l.size(); _i++)\n";  
      if ("int".equals(ename) || "Integer".equals(tname))
      { res = res + 
            "    { int " + var + " = ((Integer) _l.get(_i)).intValue();\n";
      } 
      else if ("double".equals(ename))
      { res = res + 
            "    { double " + var + " = ((Double) _l.get(_i)).doubleValue();\n";
      } 
      else if ("long".equals(ename))
      { res = res + 
            "    { long " + var + " = ((Long) _l.get(_i)).longValue();\n";
      } 
      else if (ename.equals("boolean"))
      { res = res + 
            "    { boolean " + var + " = ((Boolean) _l.get(_i)).booleanValue();\n"; 
      } 
      else
      { res = res + 
            "    { " + ename + " " + var + " = (" + ename + ") _l.get(_i);\n";
      } 

      String test = pred.queryForm(newenv,false); 
      res = res + "      if (" + test + ") { }\n" + 
                  "      else { return false; } \n"; 
      res = res + "    }\n"; 
      res = res + "    return true;\n  }"; 

      forAllList.add(pp); 
      forAllOps.put(pp,res); 
      forAllCodes.put(pp,"" + oldindex); 

      return "forAll_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) forAllCodes.get(pp); 
      return "forAll_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getForAllDefinitionJava6(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (Expression.isSimpleEntity(left)) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    String op = (String) forAllOps.get(pp); 
    // But may be different left element type with same pred

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "HashSet"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava6(); } 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean forAll_" + oldindex + "(Collection _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype == null) 
        { System.err.println("ERROR: no type for " + par); } 
        else 
        { res = res + ", " + partype.getJava6() + " " + par.getName(); }  
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava6(env,false); 
            String endexpqf = endexp.queryFormJava6(env,false); 
            String inttest = pred.queryFormJava6(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { }\n" + 
                  "      else { return false; }\n" + 
                  "    }\n"; 
            res = res + "    return true;\n  }";

            forAllList.add(pp); 
            forAllOps.put(pp,res); 
            forAllCodes.put(pp,"" + oldindex);           
            return "forAll_" + oldindex + "(null)";
          }
        }  
      } 

      res = res +   "  { \n" + 
                    "    for (Object _i : _l)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res = res +  
                    "    { " + tname + " " + var + " = (" + tname + ") _i;\n";
      }  

      String test = pred.queryFormJava6(newenv,false); 
      res = res + "      if (" + test + ") { }\n" + 
                  "      else { return false; } \n"; 
      res = res + "    }\n"; 
      res = res + "    return true;\n  }"; 

      forAllList.add(pp); 
      forAllOps.put(pp,res); 
      forAllCodes.put(pp,"" + oldindex); 

      return "forAll_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) forAllCodes.get(pp); 
      return "forAll_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getForAllDefinitionJava7(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left.umlkind == Expression.CLASSID && (left instanceof BasicExpression) &&
        ((BasicExpression) left).arrayIndex == null) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "s"; 
      lqf = "Controller.inst()." + instances; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    String op = (String) forAllOps.get(pp); 
    // But may be different left element type with same pred

    String ename; 
    Type e = left.getElementType(); 
    if (e == null || "OclAny".equals(e.getName())) 
    { ename = "Object"; } 
    else 
    { ename = e.getName(); }

    String tname = ename; 
    if (ename.equals("Set"))
    { tname = "Set"; } 
    else if (ename.equals("Sequence"))
    { tname = "ArrayList"; } 
    else if (ename.equals("int"))
    { tname = "Integer"; } 
    else if (e != null) 
    { tname = e.typeWrapperJava6(); } 

    if (op == null) 
    { // add new definitions 
      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static boolean forAll_" + oldindex + "(Collection<" + tname + "> _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        Type partype = par.getType(); 
        if (partype == null) 
        { System.err.println("!! ERROR: no type for parameter " + par); } 
        else 
        { res = res + "," + 
            partype.getJava7(partype.getElementType()) + " " + par.getName(); 
        }
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormJava7(env,false); 
            String endexpqf = endexp.queryFormJava7(env,false); 
            String inttest = pred.queryFormJava7(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { }\n" + 
                  "      else { return false; }\n" + 
                  "    }\n"; 
            res = res + "    return true;\n  }";

            forAllList.add(pp); 
            forAllOps.put(pp,res); 
            forAllCodes.put(pp,"" + oldindex);           
            return "forAll_" + oldindex + "(null)";
          }
        }  
      } 

      res = res +   "  { \n" + 
                    "    for (" + tname + " _i : _l)\n"; 
      if (ename.equals("int") || "Integer".equals(tname))
      { res = res + "    { int " + var + " = ((Integer) _i).intValue();\n"; } 
      else if (ename.equals("double"))
      { res = res + "    { double " + var + " = ((Double) _i).doubleValue();\n"; } 
      else if (ename.equals("long"))
      { res = res + "    { long " + var + " = ((Long) _i).longValue();\n"; } 
      else if (ename.equals("boolean"))
      { res = res + "    { boolean " + var + " = ((Boolean) _i).booleanValue();\n"; } 
      else 
      { res = res +  
                    "    { " + tname + " " + var + " = (" + tname + ") _i;\n";
      }  

      String test = pred.queryFormJava7(newenv,false); 
      res = res + "      if (" + test + ") { }\n" + 
                  "      else { return false; } \n"; 
      res = res + "    }\n"; 
      res = res + "    return true;\n  }"; 

      forAllList.add(pp); 
      forAllOps.put(pp,res); 
      forAllCodes.put(pp,"" + oldindex); 

      return "forAll_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) forAllCodes.get(pp); 
      return "forAll_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getForAllDefinitionCSharp(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left.umlkind == Expression.CLASSID && (left instanceof BasicExpression) &&
        ((BasicExpression) left).arrayIndex == null) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller.inst().get" + instances + "()"; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    String op = (String) forAllOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 
      String ename; 
      Type e = left.getElementType(); 
      if (e == null || "OclAny".equals(e.getName())) 
      { ename = "object"; } 
      else if ("Set".equals(e.getName()) || "Sequence".equals(e.getName()))
      { ename = "ArrayList"; } 
      else if ("String".equals(e.getName()))
      { ename = "string"; }
      else if ("boolean".equals(e.getName()))
      { ename = "bool"; } 
      else // entities, primitive types, enumerations
      { ename = e.getName(); }

      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  public static bool forAll_" + oldindex + "(ArrayList _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i); 
        res = res + "," + par.getType().getCSharp() + " " + par.getName(); 
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCSharp(env,false); 
            String endexpqf = endexp.queryFormCSharp(env,false); 
            String inttest = pred.queryFormCSharp(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { }\n" + 
                  "      else { return false; }\n" + 
                  "    }\n"; 
            res = res + "    return true;\n  }";

            forAllList.add(pp); 
            forAllOps.put(pp,res); 
            forAllCodes.put(pp,"" + oldindex); 
            return "forAll_" + oldindex + "(null)";
          }
        } 
      } 

      res = res + "  { \n" + 
            "    for (int _iforall = 0; _iforall < _l.Count; _iforall++)\n" + 
            "    { " + ename + " " + var + " = (" + ename + ") _l[_iforall];\n"; 
      String test = pred.queryFormCSharp(newenv,false); 
      res = res + "      if (" + test + ") { }\n" + 
                  "      else { return false; } \n"; 
      res = res + "    }\n"; 
      res = res + "    return true;\n  }"; 

      forAllList.add(pp); 
      forAllOps.put(pp,res); 
      forAllCodes.put(pp,"" + oldindex); 

      return "forAll_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) forAllCodes.get(pp); 
      return "forAll_" + ind + "(" + lqf + ")"; 
    } 
  } 

  public static String getForAllDefinitionCPP(Expression left, String lqf,
                                           Expression pred, String exvar, 
                                           java.util.Map env,
                                           Vector pars)
  { String signature = Attribute.parList(pars); 

    if (left.umlkind == Expression.CLASSID && (left instanceof BasicExpression) &&
        ((BasicExpression) left).arrayIndex == null) 
    { BasicExpression lbe = (BasicExpression) left;
      String lbedata = lbe.data;  
      String instances = lbedata.toLowerCase() + "_s"; 
      lqf = "Controller::inst->get" + instances + "()"; 
    }         

    String pp = "" + pred + " " + lqf + "(" + signature + ")"; 
    String op = (String) forAllOps.get(pp); 
    // But may be different left element type with same pred

    if (op == null) 
    { // add new definitions 
      String ename; 
      String argtype; 
      Type e = left.getElementType(); 
      if (e == null || "OclAny".equals(e.getName())) 
      { ename = "void"; 
        argtype = ename + "*"; 
      } 
      else if ("Set".equals(e.getName()) || "Sequence".equals(e.getName()))
      { ename = "void";  // actually set or vector of void*  
        argtype = e.getCPP(e.getElementType()); 
      } 
      else if (e.isEntity()) 
      { ename = e.getName(); 
        argtype = ename + "*"; 
      }
      else if ("String".equals(e.getName()))
      { ename = e.getName(); 
        argtype = "string"; 
      }
      else if ("boolean".equals(e.getName()))
      { ename = e.getName(); 
        argtype = "bool"; 
      } 
      else // primitive types, enumerations
      { ename = e.getName(); 
        argtype = ename; 
      }

      String cet = argtype; 


      if (left.isOrdered())
      { argtype = "std::vector<" + argtype + ">"; } 
      else 
      { argtype = "std::set<" + argtype + ">"; } 

      int oldindex = index; 
      index++; 
      
      String var; 
      if (exvar != null)
      { var = exvar; } 
      else 
      { var = ename.toLowerCase() + "_" + oldindex + "_xx"; }

      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      if (exvar == null && e != null && e.isEntity()) 
      { newenv.put(ename,var); } 
      
      String res = "  static bool forAll_" + oldindex + "(" + argtype + "* _l"; 
      String decl = "  static bool forAll_" + oldindex + "(" + argtype + "* _l"; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String cpptype = par.getType().getCPP(par.getElementType());  
        res = res + ", " + cpptype + " " + par.getName(); 
        decl = decl + ", " + cpptype + " " + par.getName();
        // if ("self".equals(par.getName()))
        // { newenv.put(par.getType().getName(),"self"); } 
      } 
      res = res + ")\n"; 
      decl = decl + ");\n"; 

      if (ename.equals("int"))  // quantification over Integer.subrange(st,en)
      { if (left instanceof BasicExpression)
        { BasicExpression leftbe = (BasicExpression) left; 
          Vector leftpars = leftbe.getParameters(); 
          if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
          { Expression startexp = (Expression) leftpars.get(0); 
            Expression endexp = (Expression) leftpars.get(1);
            String startexpqf = startexp.queryFormCPP(env,false); 
            String endexpqf = endexp.queryFormCPP(env,false); 
            String inttest = pred.queryFormCPP(env,false);
            res = res + "  { \n" + 
                  "    for (int " + var + " = " + startexpqf + "; " + 
                            var + " <= " + endexpqf + "; " + var + "++)\n" + 
                  "    { if (" + inttest + ") { }\n" + 
                  "      else { return false; }\n" + 
                  "    }\n"; 
            res = res + "    return true;\n  }";

            forAllList.add(pp); 
            forAllOps.put(pp,res); 
            forAllDecs.put(pp,decl); 
            forAllCodes.put(pp,"" + oldindex); 
            return "forAll_" + oldindex + "(NULL)";
          } 
        }  
      } 

      res = res + "  { // implements " + left + "->forAll( " + var + " | " + pred + " )\n" + 
                  "    for (" + argtype + "::iterator _iforall = _l->begin(); _iforall != _l->end(); ++_iforall)\n" + 
                  "    { " + cet + " " + var + " = *_iforall;\n";
      
      String test = pred.queryFormCPP(newenv,false); 
      res = res + "      if (" + test + ") { }\n" + 
                  "      else { return false; } \n"; 
      res = res + "    }\n"; 
      res = res + "    return true;\n  }"; 

      forAllList.add(pp); 
      forAllOps.put(pp,res); 
      forAllDecs.put(pp,decl); 
      forAllCodes.put(pp,"" + oldindex); 

      return "forAll_" + oldindex + "(" + lqf + ")"; 
    } 
    else 
    { String ind = (String) forAllCodes.get(pp);
       
      return "forAll_" + ind + "(" + lqf + ")"; 
    } 
  } 


  public void addCardinalityBound(String typ, String card)
  { if (card == null || 
        card.equals("") ||
        card.equals("*"))
    { return; }
    int num;
    try
    { num = Integer.parseInt(card); }
    catch (Exception e)
    { String nums = card.substring(3); // 0..nums
      try
      { num = Integer.parseInt(nums); }
      catch (Exception e2)
      { System.err.println("Invalid cardinality format: " +
                           card); 
        return;
      }
    }
    BExpression btyp = new BBasicExpression(typ); 
    BExpression numbe = new BBasicExpression(""+num); 
    BExpression ce = new BUnaryExpression("card",btyp); 
    BExpression inv = new BBinaryExpression("=",ce,numbe);   // <=
    addInvariant(inv); 
  } 


  public static String getSelectOps()
  { String res = ""; 
    // java.util.Iterator keys = selectOps.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next();
    for (int i = 0; i < selectList.size(); i++) 
    { Object k = selectList.get(i);  
      res = res + selectOps.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getCollectOps()
  { String res = ""; 
    // java.util.Iterator keys = collectOps.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next();
    for (int i = 0; i < collectList.size(); i++) 
    { Object k = collectList.get(i);  
      res = res + collectOps.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getRejectOps()
  { String res = ""; 
    // java.util.Iterator keys = rejectOps.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next(); 
    for (int i = 0; i < rejectList.size(); i++) 
    { Object k = rejectList.get(i);  
      res = res + rejectOps.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getAnyOps()
  { String res = ""; 
    System.out.println(">>> Any ops are: " + anyOps); 

    for (int i = 0; i < anyList.size(); i++) 
    { Object k = anyList.get(i);  
      res = res + anyOps.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getExistsOps()
  { String res = ""; 
    // java.util.Iterator keys = existsOps.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next();
    for (int i = 0; i < existsList.size(); i++) 
    { Object k = existsList.get(i);  
      res = res + existsOps.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getExistsDecs()
  { String res = ""; 
    // java.util.Iterator keys = existsDecs.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next(); 
    for (int i = 0; i < existsList.size(); i++) 
    { Object k = existsList.get(i);  
      res = res + existsDecs.get(k) + "\n\n"; 
    }  
    return res; 
  }     


  public static String getExists1Ops()
  { String res = ""; 
    // java.util.Iterator keys = exists1Ops.keySet().iterator();
    // while (keys.hasNext())
    for (int i = 0; i < exists1List.size(); i++) 
    { Object k = exists1List.get(i);  
      res = res + exists1Ops.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getExists1Decs()
  { String res = ""; 
    // java.util.Iterator keys = exists1Decs.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next(); 
    for (int i = 0; i < exists1List.size(); i++) 
    { Object k = exists1List.get(i);  
      res = res + exists1Decs.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getForAllOps()
  { String res = ""; 
    // java.util.Iterator keys = forAllOps.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next();
    for (int i = 0; i < forAllList.size(); i++) 
    { Object k = forAllList.get(i);   
      res = res + forAllOps.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String getForAllDecs()
  { String res = ""; 
    // java.util.Iterator keys = forAllDecs.keySet().iterator();
    // while (keys.hasNext())
    // { Object k = keys.next(); 
    for (int i = 0; i < forAllList.size(); i++) 
    { Object k = forAllList.get(i);  
      res = res + forAllDecs.get(k) + "\n\n"; 
    }  
    return res; 
  }     

  public static String generateCopyOps() // Java4
  { String res = "  public static Vector copyCollection(Vector s)\n" + 
      "  { Vector result = new Vector();\n" +  
      "    result.addAll(s);\n" +  
      "    return result;\n" + 
      "  }\n\n"; 

    res = res + 
      "  public static HashMap copyMap(Map s)\n" + 
      "  { HashMap result = new HashMap();\n" +  
      "    result.putAll(s);\n" +  
      "    return result;\n" +  
      "  }\n\n";

    res = res + 
      "  public static int[] newRefint(int x)\n" +
      "  { int[] res = new int[1]; \n" +
      "    res[0] = x; \n" +
      "    return res; \n" +
      "  } \n\n";

    res = res + 
      "  public static long[] newReflong(long x)\n" +
      "  { long[] res = new long[1]; \n" +
      "    res[0] = x; \n" +
      "    return res; \n" +
      "  } \n\n";

    res = res + 
      "  public static double[] newRefdouble(double x)\n" +
      "  { double[] res = new double[1]; \n" +
      "    res[0] = x; \n" +
      "    return res; \n" +
      "  } \n\n";

    res = res + 
      "  public static boolean[] newRefboolean(boolean x)\n" +
      "  { boolean[] res = new boolean[1]; \n" +
      "    res[0] = x; \n" +
      "    return res; \n" +
      "  } \n\n"; 

    res = res + 
      "  public static String[] newRefString(String x)\n" +
      "  { String[] res = new String[1]; \n" +
      "    res[0] = x; \n" +
      "    return res; \n" +
      "  } \n\n"; 

    res = res + 
      "  public static Object[] newRefObject(Object x)\n" +
      "  { Object[] res = new Object[1]; \n" +
      "    res[0] = x; \n" +
      "    return res; \n" +
      "  } \n\n"; 

    res = res + 
      "  public static Vector sequenceRange(Object[] arr, int n)\n" +
      "  { Vector res = new Vector();\n" + 
      "    for (int i = 0; i < n; i++) \n" +
      "    { res.add(arr[i]); } \n" +
      "    return res; \n" +
      "  }\n\n";

   res = res +  
      "  public static int sequenceCompare(List sq1, List sq2)\n" +
      "  { int res = 0;\n" +
      "    for (int i = 0; i < sq1.size() && i < sq2.size(); i++)\n" +
      "    { Object elem1 = sq1.get(i);\n" +
      "      if (((Comparable) elem1).compareTo(sq2.get(i)) < 0)\n" +
      "      { return -1; }\n" +
      "      else if (((Comparable) elem1).compareTo(sq2.get(i)) > 0)\n" +
      "      { return 1; }\n" +
      "    }\n" +
      "\n" +
      "    if (sq1.size() > sq2.size())\n" +
      "    { return 1; }\n" +
      "    if (sq2.size() > sq1.size())\n" +
      "    { return -1; }\n" +
      "    return res;\n" +
      "  }\n\n"; 
 
    return res; 
  }  

  public static String generateCopyOpsJava6() 
  { String res = "  public static HashSet copySet(HashSet s)\n" + 
      "  { HashSet result = new HashSet();\n" +  
      "    result.addAll(s);\n" +  
      "    return result;\n" + 
      "  }\n\n";
    res = "  public static ArrayList copySequence(ArrayList s)\n" + 
      "  { ArrayList result = new ArrayList();\n" +  
      "    result.addAll(s);\n" +  
      "    return result;\n" + 
      "  }\n\n"; 
    res = res + 
      "  public static HashMap copyMap(Map s)\n" + 
      "  { HashMap result = new HashMap();\n" +  
      "    result.putAll(s);\n" +  
      "    return result;\n" +  
      "  }\n\n";

    res = res + 
      "  public static ArrayList sequenceRange(Object[] arr, int n)\n" +
      "  { ArrayList res = new ArrayList();\n" + 
      "    for (int i = 0; i < n; i++) \n" +
      "    { res.add(arr[i]); } \n" +
      "    return res; \n" +
      "  }\n\n"; 

   res = res +  
      "  public static int sequenceCompare(List sq1, List sq2)\n" +
      "  { int res = 0;\n" +
      "    for (int i = 0; i < sq1.size() && i < sq2.size(); i++)\n" +
      "    { Object elem1 = sq1.get(i);\n" +
      "      if (((Comparable) elem1).compareTo(sq2.get(i)) < 0)\n" +
      "      { return -1; }\n" +
      "      else if (((Comparable) elem1).compareTo(sq2.get(i)) > 0)\n" +
      "      { return 1; }\n" +
      "    }\n" +
      "\n" +
      "    if (sq1.size() > sq2.size())\n" +
      "    { return 1; }\n" +
      "    if (sq2.size() > sq1.size())\n" +
      "    { return -1; }\n" +
      "    return res;\n" +
      "  }\n\n"; 
  
    return res; 
  }  

  public static String generateCopyOpsJava7() 
  { String res = "  public static <T> HashSet<T> copySet(Collection<T> s)\n" + 
      "  { HashSet<T> result = new HashSet<T>();\n" +  
      "    result.addAll(s);\n" +  
      "    return result;\n" +  
      "  }\n\n";  
    res = res + "  public static <T> TreeSet<T> copySortedSet(Collection<T> s)\n" + 
      "  { TreeSet<T> result = new TreeSet<T>();\n" +  
      "    result.addAll(s);\n" +  
      "    return result;\n" +  
      "  }\n\n";  
    res = res + "  public static <T> ArrayList<T> copySequence(Collection<T> s)\n" + 
      "  { ArrayList<T> result = new ArrayList<T>();\n" + 
      "    result.addAll(s); \n" +
      "    return result; \n" +
      "  }\n\n";  
    res = res + 
      "  public static <K,T> HashMap<K,T> copyMap(Map<K,T> s)\n" +
      "  { HashMap<K,T> result = new HashMap<K,T>();\n" + 
      "    result.putAll(s);\n" + 
      "    return result;\n" + 
      "  }\n\n";

    res = res + 
      "  public static <T> ArrayList<T> sequenceRange(T[] arr, int n)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" +
      "    for (int i = 0; i < n; i++) \n" +
      "    { res.add(arr[i]); } \n" +
      "    return res;  \n" +
      "  }\n\n"; 

   res = res +  
      "  public static int sequenceCompare(List sq1, List sq2)\n" +
      "  { int res = 0;\n" +
      "    for (int i = 0; i < sq1.size() && i < sq2.size(); i++)\n" +
      "    { Object elem1 = sq1.get(i);\n" +
      "      if (((Comparable) elem1).compareTo(sq2.get(i)) < 0)\n" +
      "      { return -1; }\n" +
      "      else if (((Comparable) elem1).compareTo(sq2.get(i)) > 0)\n" +
      "      { return 1; }\n" +
      "    }\n" +
      "\n" +
      "    if (sq1.size() > sq2.size())\n" +
      "    { return 1; }\n" +
      "    if (sq2.size() > sq1.size())\n" +
      "    { return -1; }\n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + 
      "  public static <S,T> T iterate(Collection<S> _s, T initialValue, Function<S,Function<T,T>> _f)\n" +
      "  { T acc = initialValue; \n" +
      "    for (S _x : _s) \n" +
      "    { acc = _f.apply(_x).apply(acc); }\n" +
      "    return acc; \n" +
      "  } \n\n"; 
 
    return res;  
  }


  public static String generateCopyOpsCSharp() 
  { String res = "  public static ArrayList copyCollection(ArrayList a)\n" +
      "  { ArrayList res = new ArrayList();\n" +
      "    res.AddRange(a); \n" +
      "    return res; \n" + 
      "  }\n\n"; 

    res = res + 
      "  public static Hashtable copyMap(Hashtable m)\n" + 
      "  { Hashtable res = new Hashtable(); \n" +
      "    foreach (DictionaryEntry pair in m) \n" +
      "    { res.Add(pair.Key, pair.Value); } \n" +
      "    return res; \n" + 
      "  }\n\n"; 

    res = res + 
      "  public static ArrayList collectSequence(ArrayList col, Func<object,object> f)\n" + 
      "  { ArrayList res = new ArrayList();\n" +  
      "    for (int i = 0; i < col.Count; i++)\n" + 
      "    { res.Add(f(col[i]));  }\n" + 
      "    return res; \n" + 
      "  }\n\n"; 

    res = res + 
      "  public static object iterate(ArrayList col, object init, Func<object,Func<object,object>> f)\n" + 
      "  { object res = init;\n" +  
      "    for (int i = 0; i < col.Count; i++)\n" + 
      "    { res = f(col[i])(res);  }\n" + 
      "    return res; \n" + 
      "  }\n\n"; 

   res = res + 
      "  public unsafe static T* resizeTo<T>(T* arr, int n) where T : unmanaged\n" + 
      "  { T* tmp = stackalloc T[n];\n" +
      "    for (int i = 0; i < n; i++)\n" +
      "    { tmp[i] = arr[i]; }\n" +
      "    return tmp;\n" +
      "  }\n\n" +
      "  public unsafe static ArrayList sequenceRange<T>(T* arr, int n) where T : unmanaged\n" + 
      "  { ArrayList res = new ArrayList();\n" +  
      "    for (int i = 0; i < n; i++)\n" + 
      "    { res.Add(arr[i]); }\n" + 
      "    return res; \n" + 
      "  }\n\n"; 

    res = res + 
      "  public static int sequenceCompare(ArrayList sq1, ArrayList sq2)\n" +
      "  { int res = 0;\n" +
      "    for (int i = 0; i < sq1.Count && i < sq2.Count; i++)\n" +
      "    { object elem1 = sq1[i];\n" +
      "      if (((IComparable) elem1).CompareTo(sq2[i]) < 0)\n" +
      "      { return -1; }\n" +
      "      else if (((IComparable) elem1).CompareTo(sq2[i]) > 0)\n" +
      "      { return 1; }\n" +
      "    }\n" +
      "\n" +
      "    if (sq1.Count > sq2.Count)\n" +
      "    { return 1; }\n" +
      "    if (sq2.Count > sq1.Count)\n" +
      "    { return -1; }\n" +
      "    return res;\n" +
      "  }\n\n";

    return res; 
  } 

  public static String generateCopyOpsCPP() 
  { String res = "  static set<_T>* copySet(set<_T>* a)\n" +
      "  { set<_T>* res = new set<_T>(); \n" +
      "    res->insert(a->begin(),a->end()); \n" +
      "    return res; \n" +
      "  }\n\n" +
      "  static vector<_T>* copySequence(vector<_T>* a)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    res->insert(res->end(), a->begin(),a->end());\n" + 
      "    return res; \n" +
      "  }\n\n" +
      "  static map<string,_T>* copyMap(map<string,_T>* m)\n" +  
      "  { map<string,_T>* res = new map<string,_T>();\n" + 
      "    map<string,_T>::iterator iter; \n" +
      "    for (iter = m->begin(); iter != m->end(); ++iter)\n" + 
      "    { string key = iter->first;\n" + 
      "      (*res)[key] = iter->second;  \n" +
      "    }     \n" +
      "    return res;\n" + 
      "  } \n\n";
   
   res = res + 
     "  static string collectionToString(vector<_T>* c)\n" +
     "  { ostringstream buff;\n" + 
     "    buff << \"Sequence{\";\n" + 
     "    for (vector<_T>::iterator _pos = c->begin(); _pos != c->end(); ++_pos)\n" +
     "    { buff << *_pos;\n" +
     "      if (_pos + 1 < c->end())\n" +
     "      { buff << \", \"; }\n" +
     "    }\n" +
     "    buff << \"}\";\n" + 
     "    return buff.str(); \n" +
     "  }\n\n"; 

   res = res + 
     "  static string collectionToString(set<_T>* c)\n" +
     "  { ostringstream buff;\n" +
     "    buff << \"Set{\"; \n" +
     "    for (set<_T>::iterator _pos = c->begin(); _pos != c->end(); ++_pos)\n" +
     "    { buff << *_pos;\n" +
     "      if (_pos + 1 < c->end())\n" +
     "      { buff << \", \"; }\n" +
     "    }\n" +
     "    buff << \"}\";\n" +
     "    return buff.str(); \n" +
     "  }\n\n"; 

   res = res + 
     "  static string collectionToString(map<string, _T>* c)\n" + 
     "  { ostringstream buff;\n" +
     "    buff << \"Map{\";\n" + 
     "    int sze = c->size();\n" +  
     "    int count = 0;\n" +  
     "    for (auto it = c->begin(); it != c->end(); it++)\n" + 
     "    {\n" + 
     "      buff << (*it).first;\n" + 
     "      buff << \" |-> \";\n" + 
     "      buff << (*it).second;\n" + 
     "      if (count + 1 < sze)\n" + 
     "      { buff << \", \"; }\n" + 
     "      count++; \n" + 
     "    }\n" + 
     "    buff << \"}\";\n" +
     "    return buff.str();\n" +  
     "  }\n\n"; 

  /* 
   res = res +  
      "  static int sequenceCompare(vector<T>* sq1, vector<T>* sq2)\n" +
      "  { int res = 0;\n" +
      "    for (int i = 0; i < sq1->size() && i < sq2->size(); i++)\n" +
      "    { T elem1 = sq1->at(i);\n" +
      "      if (elem1 < sq2->at(i))\n" +
      "      { return -1; }\n" +
      "      else if (elem1 > sq2->at(i))\n" +
      "      { return 1; }\n" +
      "    }\n" +
      "\n" +
      "    if (sq1->size() > sq2->size())\n" +
      "    { return 1; }\n" +
      "    if (sq2->size() > sq1->size())\n" +
      "    { return -1; }\n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + 
      "  template<class S>\n" + 
      "  static T iterate(vector<S>* _s, T initialValue, std::function<std::function<T(T)>(S)> _f)\n" +
      "  { T acc = initialValue; \n" +
      "    for (vector<S>::iterator _pos = _s->begin(); _pos != _s->end(); ++_pos) \n" +
      "    { S _x = *_pos;\n" +  
      "      acc = (_f(_x))(acc); \n" + 
      "    }\n" +
      "    return acc; \n" +
      "  } \n\n"; */ 
 
    return res; 
  }

  public static String generateCPPExtendedLibrary()
  { String res =
      "template<class S, class T>\n" + 
      "class UmlRsdsOcl {\n" + 
      "  public:\n\n" + 

      "  static T iterate(vector<S>* _s, T initialValue, std::function<T(S,T)> _f)\n" +
      "  { T acc = initialValue; \n" +
      "    for (vector<S>::iterator _pos = _s->begin(); _pos != _s->end(); ++_pos) \n" +
      "    { S _x = *_pos;\n" +  
      "      acc = _f(_x,acc); \n" + 
      "    }\n" +
      "    return acc; \n" +
      "  } \n\n";
 
    res = res +   
     "  static map<S,T>* includingMap(map<S,T>* m, S src, T trg)\n" + 
     "  { map<S,T>* copy = new map<S,T>();\n" + 
     "    map<S,T>::iterator iter; \n" +
     "    for (iter = m->begin(); iter != m->end(); ++iter)\n" + 
     "    { S key = iter->first;\n" + 
     "      (*copy)[key] = iter->second;\n" + 
     "    }     \n" +
     "    (*copy)[src] = trg;\n" + 
     "    return copy; \n" +
     "  }\n\n";  

   res = res + 
     "  static map<S,T>* unionMap(map<S,T>* m1, map<S,T>* m2)\n" +  
     "  { map<S,T>* res = new map<S,T>();\n" + 
     "    map<S,T>::iterator iter; \n" +
     "    for (iter = m1->begin(); iter != m1->end(); ++iter)\n" + 
     "    { S key = iter->first;\n" + 
     "      if (m2->count(key) == 0) \n" +
     "      { (*res)[key] = iter->second; }\n" + 
     "    }     \n" +
     "    for (iter = m2->begin(); iter != m2->end(); ++iter)\n" + 
     "    { S key = iter->first;\n" + 
     "      (*res)[key] = iter->second;\n" + 
     "    }     \n" +
     "    return res;\n" + 
     "  } \n\n"; 

    res = res + "};\n\n"; 
    return res; 
  }

  public static String generateSubrangeOp()  // for Java
  { String res = "  public static List integerSubrange(int i, int j)\n" + 
                 "  { List tmp = new Vector(); \n" + 
                 "    for (int k = i; k <= j; k++)\n" + 
                 "    { tmp.add(new Integer(k)); } \n" + 
                 "    return tmp;\n" + 
                 "  }\n\n";  

     res = res + "  public static String subrange(String s, int i, int j)\n" + 
                 "  { int len = s.length();\n" + 
                 "    if (len == 0) { return s; }\n" +   
                 "    if (j > len) { j = len; }\n" +  
                 "    if (j < i) { return \"\"; }\n" +  
                 "    if (i > len) { return \"\"; }\n" +  
                 "    if (i < 1) { i = 1; }\n" +  
                 "    return s.substring(i-1,j);\n" +
                 "  }\n\n";

     res = res + "  public static List subrange(List l, int i, int j)\n";
     res = res + "  { List tmp = new Vector(); \n" +
                 "    if (i < 0) { i = l.size() + i; }\n" + 
                 "    if (j < 0) { j = l.size() + j; }\n" + 
                 "    for (int k = i-1; k < j; k++)\n" + 
                 "    { tmp.add(l.get(k)); } \n" + 
                 "    return tmp; \n" + 
                 "  }\n\n";  
    return res;
  }

  public static String generateSubrangeOpJava6()  // for Java6
  { String res = "  public static ArrayList integerSubrange(int i, int j)\n" + 
                 "  { ArrayList tmp = new ArrayList(); \n" + 
                 "    for (int k = i; k <= j; k++)\n" + 
                 "    { tmp.add(new Integer(k)); } \n" + 
                 "    return tmp;\n" + 
                 "  }\n\n";  

     res = res + "  public static String subrange(String s, int i, int j)\n" + 
                 "  { int len = s.length();\n" + 
                 "    if (len == 0) { return s; }\n" +   
                 "    if (j > len) { j = len; }\n" +  
                 "    if (j < i) { return \"\"; }\n" +  
                 "    if (i > len) { return \"\"; }\n" +  
                 "    if (i < 1) { i = 1; }\n" +  
                 "    return s.substring(i-1,j);\n" +
                 "  }\n\n";

     res = res + "  public static ArrayList subrange(ArrayList l, int i, int j)\n";
     res = res + "  { ArrayList tmp = new ArrayList(); \n" + 
                 "    if (i < 0) { i = l.size() + i; }\n" + 
                 "    if (j < 0) { j = l.size() + j; }\n" + 
                 "    for (int k = i-1; k < j; k++)\n" + 
                 "    { tmp.add(l.get(k)); } \n" + 
                 "    return tmp; \n" + 
                 "  }\n\n";  
    return res;
  }

  public static String generateSubrangeOpJava7()  // for Java7
  { String res = "  public static ArrayList<Integer> integerSubrange(int i, int j)\n" + 
                 "  { ArrayList<Integer> tmp = new ArrayList<Integer>(); \n" + 
                 "    for (int k = i; k <= j; k++)\n" + 
                 "    { tmp.add(new Integer(k)); } \n" + 
                 "    return tmp;\n" + 
                 "  }\n\n";  

     res = res + "  public static String subrange(String s, int i, int j)\n" + 
                 "  { int len = s.length();\n" + 
                 "    if (len == 0) { return s; }\n" +   
                 "    if (j > len) { j = len; }\n" +  
                 "    if (j < i) { return \"\"; }\n" +  
                 "    if (i > len) { return \"\"; }\n" +  
                 "    if (i < 1) { i = 1; }\n" +  
                 "    return s.substring(i-1,j);\n" +
                 "  }\n\n";

    res = res + "  public static <T> ArrayList<T> subrange(ArrayList<T> l, int i, int j)\n";
    res = res + "  { ArrayList<T> tmp = new ArrayList<T>(); \n" + 
                "    if (i < 0) { i = l.size() + i; }\n" + 
                "    if (j < 0) { j = l.size() + j; }\n" + 
                "    for (int k = i-1; k < j; k++)\n" + 
                "    { tmp.add(l.get(k)); } \n" + 
                "    return tmp; \n" + 
                "  }\n\n";  
    return res;
  }

  public static String generateSubrangeOpCSharp()  // for CSharp
  { String res = "  public static ArrayList integerSubrange(int i, int j)\n" + 
                 "  { ArrayList tmp = new ArrayList(); \n" + 
                 "    for (int k = i; k <= j; k++)\n" + 
                 "    { tmp.Add(k); } \n" + 
                 "    return tmp;\n" + 
                 "  }\n\n" + 
                 "  public static string subrange(string s, int i, int j)\n";

    res = res + "  { if (i < 1)\n" + 
                "    { i = 1; }\n" + 
                "    if (j > s.Length)\n" + 
                "    { j = s.Length; }\n" + 
                "    if (i > s.Length || i > j)\n" + 
                "    { return \"\";  }\n" + 
                "    return s.Substring(i-1, j-i+1);\n" + 
                "  }\n\n";

    res = res + "  public static ArrayList subrange(ArrayList l, int i, int j)\n";
    res = res + "  { ArrayList tmp = new ArrayList(); \n" + 
                "    if (i < 0) { i = l.Count + i; }\n" + 
                "    if (j < 0) { j = l.Count + j; }\n" + 
                "    if (i < 1) { i = 1; }\n" + 
                "    for (int k = i-1; k < j; k++)\n" + 
                "    { tmp.Add(l[k]); } \n" + 
                "    return tmp; \n" + 
                "  }\n\n";  
    res = res + 
                "  public static int indexOfSubList(ArrayList a, ArrayList b)\n" +
                "  { /* Index of a subsequence a of sequence b in b */\n" +
                "    if (a.Count == 0 || b.Count == 0)\n" +
                "    { return 0; }\n" +
                "\n" +
                "    int i = 0;\n" +
                "    while (i < b.Count && b[i] != a[0])\n" +
                "    { i++; }\n" +
                "\n" +
                "    if (i >= b.Count)\n" +
                "    { return 0; }\n" +
                "\n" +
                "    int j = 0;\n" +
                "    while (j < a.Count && i + j < b.Count && b[i + j] == a[j])\n" +
                "    { j++; }\n" +
                "\n" +
                "    if (j >= a.Count)\n" +
                "    { return i + 1; }\n" +
                "\n" +
                "    ArrayList subr = subrange(b, i + 2, b.Count);\n" +
                "    int res1 = indexOfSubList(a, subr);\n" +
                "    if (res1 == 0)\n" +
                "    { return 0; }\n" +
                "    return res1 + i + 1;\n" +
                "  }\n\n"; 
    res = res + 
                "  public static int lastIndexOfSubList(ArrayList a, ArrayList b)\n" +
                "  { int res = 0;\n" +
                "    if (a.Count == 0 || b.Count == 0)\n" +
                "    { return res; }\n" +
                "\n" +
                "    ArrayList arev = reverse(a);\n" +
                "    ArrayList brev = reverse(b);\n" +
                "    int i = indexOfSubList(arev, brev);\n" +
                "    if (i == 0)\n" +
                "    { return res; }\n" +
                "    res = b.Count - i - a.Count + 2;\n" +
                "    return res;\n" +
                "  }\n\n"; 

    return res;
  }

  public static String generateIncludesAllMapOpCSharp()  // for CSharp
  { String res = "  public static bool includesAllMap(Hashtable sup, Hashtable sub) \n" +
    "  { foreach (DictionaryEntry pair in sub) \n" +
    "    { if (sup.ContainsKey(pair.Key)) \n" +
    "      { if (sup[pair.Key].Equals(pair.Value)) { } \n" +
    "        else \n" +
    "        { return false; } \n" +
    "      } \n" +
    "      else \n" +
    "      { return false; } \n" +
    "    } \n" +
    "    return true; \n" +
    "  } \n"; 
    return res; 
  } 

   public static String generateExcludesAllMapOpCSharp()  // for CSharp
   { String res = "  public static bool excludesAllMap(Hashtable sup, Hashtable sub) \n" +
     " { foreach (DictionaryEntry pair in sub) \n" +
     "   { if (sup.ContainsKey(pair.Key))  \n" +
     "     { if (pair.Value.Equals(sup[pair.Key])) \n" +
     "       { return false; } \n" +
     "     } \n" +
     "   } \n" +
     "   return true; \n" +
     " } \n"; 
     return res; 
  } 


   public static String generateIncludingMapOpCSharp()  // for CSharp
   { String res = 
     "  public static Hashtable includingMap(Hashtable m, object src, object trg) \n" +
     "  { Hashtable copy = new Hashtable(m); \n" +
     "    copy.Add(src, trg); \n" +
     "    return copy; \n" +
     "  } \n"; 
     return res; 
   } 

   public static String generateExcludeAllMapOpCSharp()  // for CSharp
   { String res = 
     "  public static Hashtable excludeAllMap(Hashtable m1, Hashtable m2) \n" +
     "  { // m1 - m2 \n" +
     "    Hashtable res = new Hashtable(); \n" +
     "    foreach (DictionaryEntry x in m1) \n" +
     "    { object key = x.Key; \n" +
     "      if (m2.ContainsKey(key)) { } \n" +
     "      else \n" +
     "      { res[key] = m1[key]; } \n" +
     "    } \n" +
     "    return res; \n" +
     "  } \n"; 
     return res; 
   } 

   public static String generateExcludingMapKeyOpCSharp()  // for CSharp
   { String res = 
     "   public static Hashtable excludingMapKey(Hashtable m, object k) \n" +
     "   { // m - { k |-> m(k) }  \n" +
     "     Hashtable res = new Hashtable(); \n" +
     "     foreach (DictionaryEntry pair in m) \n" +
     "     { if (pair.Key.Equals(k)) { } \n" +
     "       else \n" +
     "       { res.Add(pair.Key, pair.Value); } \n" +
     "     } \n" +
     "     return res; \n" +
     "   } \n"; 
     return res; 
   } 

   public static String generateExcludingMapValueOpCSharp()  // for CSharp
   { String res = 
     "  public static Hashtable excludingMapValue(Hashtable m, object v) \n" +
     "  { // m - { k |-> v }    \n" +
     "    Hashtable res = new Hashtable(); \n" +
     "    foreach (DictionaryEntry pair in m) \n" +
     "    { if (pair.Value.Equals(v)) { } \n" +
     "      else \n" +
     "      { res.Add(pair.Key, pair.Value); } \n" +
     "    } \n" +
     "    return res; \n" +
     "  } \n"; 
     return res; 
   } 

   public static String generateUnionMapOpCSharp()  // for CSharp
   { String res = 
     "  public static Hashtable unionMap(Hashtable m1, Hashtable m2) \n" +
     "  { /* Overrides m1 by m2 if they have pairs in common */ \n" +
     "    Hashtable res = new Hashtable(); \n" +
     "    foreach (DictionaryEntry pair in m2) \n" +
     "    { res.Add(pair.Key, pair.Value); } \n" +
     "    foreach (DictionaryEntry pair in m1) \n" +
     "    { if (res.ContainsKey(pair.Key)) { } \n" +
     "      else { res.Add(pair.Key, pair.Value); } \n" +
     "    }  \n" +
     "    return res; \n" +
     "  } \n"; 
     return res; 
   } 

   public static String generateIntersectionMapOpCSharp()  // for CSharp
   { String res = 
     "   public static Hashtable intersectionMap(Hashtable m1, Hashtable m2) \n" +
     "   { Hashtable res = new Hashtable();  \n" +
     "     foreach (DictionaryEntry pair in m1) \n" +
     "     { object key = pair.Key; \n" +
     "       if (m2.ContainsKey(key) && pair.Value != null && pair.Value.Equals(m2[key])) \n" +
     "       { res.Add(key, pair.Value); } \n" +
     "     } \n" +
     "     return res; \n" +
     "   } \n\n";
     res = res + 
     "   public static Hashtable restrictMap(Hashtable m1, ArrayList ks) \n" +
     "   { Hashtable res = new Hashtable();  \n" +
     "     foreach (DictionaryEntry pair in m1) \n" +
     "     { object key = pair.Key; \n" +
     "       if (ks.Contains(key)) \n" +
     "       { res.Add(key, pair.Value); } \n" +
     "     } \n" +
     "     return res; \n" +
     "   } \n\n"; 

     res = res + 
     "   public static Hashtable antirestrictMap(Hashtable m1, ArrayList ks) \n" +
     "   { Hashtable res = new Hashtable();  \n" +
     "     foreach (DictionaryEntry pair in m1) \n" +
     "     { object key = pair.Key; \n" +
     "       if (ks.Contains(key)) { }\n" +
     "       else \n" + 
     "       { res.Add(key, pair.Value); } \n" +
     "     } \n" +
     "     return res; \n" +
     "   } \n\n"; 
     res = res + 
     "   public static ArrayList mapKeys(Hashtable m) \n" +
     "   { ArrayList res = new ArrayList();  \n" +
     "     res.AddRange(m.Keys);\n" +
     "     return res; \n" +
     "   } \n\n"; 
     res = res + 
     "   public static ArrayList mapValues(Hashtable m) \n" +
     "   { ArrayList res = new ArrayList();  \n" +
     "     res.AddRange(m.Values);\n" +
     "     return res; \n" +
     "   } \n\n"; 

     res = res + 
     "   public static Hashtable selectMap(Hashtable m1, Func<object, bool> f) \n" +
     "   { Hashtable res = new Hashtable();  \n" +
     "     foreach (DictionaryEntry pair in m1) \n" +
     "     { object val = pair.Value; \n" +
     "       if (f(val))\n" + 
     "       { res.Add(pair.Key,val); } \n" +
     "     } \n" +
     "     return res; \n" +
     "   } \n\n"; 

     res = res + 
     "   public static Hashtable rejectMap(Hashtable m1, Func<object, bool> f) \n" +
     "   { Hashtable res = new Hashtable();  \n" +
     "     foreach (DictionaryEntry pair in m1) \n" +
     "     { object val = pair.Value; \n" +
     "       if (f(val)) { }\n" +
     "       else\n" +  
     "       { res.Add(pair.Key,val); } \n" +
     "     } \n" +
     "     return res; \n" +
     "   } \n\n"; 

     res = res + 
     "   public static Hashtable collectMap(Hashtable m1, Func<object, object> f) \n" +
     "   { Hashtable res = new Hashtable();  \n" +
     "     foreach (DictionaryEntry pair in m1) \n" +
     "     { object val = pair.Value; \n" +
     "       res.Add(pair.Key, f(val));  \n" +
     "     } \n" +
     "     return res; \n" +
     "   } \n\n"; 
 
     return res; 
   } 



  public static String generateTokeniseCSVOp()
  { String res = "  public static Vector tokeniseCSV(String line)\n" +
      "{ StringBuffer buff = new StringBuffer();\n" +
      "  int x = 0;\n" +
      "  int len = line.length();\n" +
      "  boolean instring = false;\n" +
      "  Vector res = new Vector();\n" +
      "  while (x < len)\n" +
      "  { char chr = line.charAt(x);\n" +
      "    x++;\n" +
      "    if (chr == ',')\n" +
      "    { if (instring) { buff.append(chr); }\n" +
      "      else\n" +
      "      { res.add(buff.toString().trim());\n" +
      "        buff = new StringBuffer();\n" +
      "      }\n" +
      "    }\n" +
      "    else if ('\"' == chr)\n" +
      "    { if (instring) { instring = false; }\n" +
      "      else { instring = true; } \n" +
      "    }\n" +
      "    else\n" +
      "    { buff.append(chr); }\n" +
      "  }\n" +
      "  res.add(buff.toString().trim());\n" + 
      "  return res;\n" +
      "}\n"; 
    return res; 
  }


  public static String generateIncludesAllMapOpCPP()
  { String res = "  static bool includesAllMap(map<string,_T>* sup, map<string,_T>* sub) \n" + 
      "  { map<string,_T>::iterator iter; \n" +
      "    for (iter = sub->begin(); iter != sub->end(); ++iter) \n" +
      "    { string key = iter->first; \n" +
      "      map<string,_T>::iterator f = sup->find(key); \n" +
      "      if (f != sup->end())  \n" +
      "      { if (iter->second == f->second) {} \n" +
      "        else \n" +
      "        { return false; } \n" +       
      "      } \n" +
      "      else  \n" +
      "      { return false; } \n" +
      "    }     \n" +
      "    return true; \n" +
      "  } \n"; 
    return res; 
  } 

  public static String generateExcludesAllMapOpCPP()
  { String res =       
      "  static bool excludesAllMap(map<string,_T>*  sup, map<string,_T>* sub) \n" + 
      "  { map<string,_T>::iterator iter; \n" +
      "    for (iter = sub->begin(); iter != sub->end(); ++iter) \n" +
      "    { string key = iter->first; \n" +
      "      map<string,_T>::iterator f = sup->find(key); \n" +
      "      if (f != sup->end())  \n" +
      "      { if (iter->second == f->second) \n" +
      "        { return false; } \n" +
      "      } \n" +
      "    }     \n" +
      "    return true; \n" +
      "  } \n"; 
    return res; 
  } 

  public static String generateIncludingMapOpCPP()
  { String res = 
      "   static map<string,_T>* includingMap(map<string,_T>* m, string src, _T trg) \n" + 
      "   { map<string,_T>* copy = new map<string,_T>(); \n" +
      "     map<string,_T>::iterator iter; \n" +
      "     for (iter = m->begin(); iter != m->end(); ++iter) \n" +
      "     { string key = iter->first; \n" +
      "       (*copy)[key] = iter->second; \n" + 
      "     }     \n" +
      "     (*copy)[src] = trg; \n" +
      "     return copy; \n" +
      "   } \n"; 
    return res; 
  } 

  public static String generateExcludeAllMapOpCPP()
  { String res = 
      "   static map<string,_T>* excludeAllMap(map<string,_T>* m1, map<string,_T>* m2) \n" + 
      "   { map<string,_T>* res = new map<string,_T>(); \n" + 
      "     map<string,_T>::iterator iter; \n" +
      "     for (iter = m1->begin(); iter != m1->end(); ++iter) \n" +
      "     { string key = iter->first; \n" +
      "       map<string,_T>::iterator f = m2->find(key); \n" +
      "       if (f != m2->end())  \n" +
      "       { if (iter->second == f->second)  {  } \n" +
      "         else  \n" +
      "  	   { (*res)[key] = iter->second; } \n" + 
      "       } \n" +
      "       else  \n" +
      "       { (*res)[key] = iter->second; } \n" + 
      "    }     \n" +
      "    return res; \n" +
      "  }   \n"; 
    return res; 
  } 

  public static String generateExcludingMapKeyOpCPP()
  { String res =       
      "  static map<string,_T>* excludingMapKey(map<string,_T>* m, string k) \n" + 
      "  { // m - { k |-> m(k) }  \n" +
      "    map<string,_T>* res = new map<string,_T>(); \n" +
      "    map<string,_T>::iterator iter; \n" +
      "    for (iter = m->begin(); iter != m->end(); ++iter) \n" +
      "    { string key = iter->first; \n" +
      "      if (key == k) {} \n" +
      "      else       \n" +
      "      { (*res)[key] = iter->second; } \n" +
      "    }     \n" +
      "    return res; \n" +
      "  } \n"; 
    return res; 
  } 


  public static String generateExcludingMapValueOpCPP()
  { String res = 
      "  static map<string,_T>* excludingMapValue(map<string,_T>* m, _T v) \n" + 
      "  { // m - { k |-> v }  \n" +
      "    map<string,_T>* res = new map<string,_T>(); \n" +
      "    map<string,_T>::iterator iter; \n" +
      "    for (iter = m->begin(); iter != m->end(); ++iter) \n" +
      "    { string key = iter->first; \n" +
      "      if (iter->second == v) {} \n" +
      "      else       \n" +
      "      { (*res)[key] = iter->second; } \n" +
      "    }     \n" +
      "    return res; \n" +
      "  } \n"; 
    return res; 
  } 



  public static String generateUnionMapOpCPP()
  { String res = 
      "  static map<string,_T>* unionMap(map<string,_T>* m1, map<string,_T>* m2)  \n" +
      "  { map<string,_T>* res = new map<string,_T>(); \n" +
      "    map<string,_T>::iterator iter; \n" +  
      "    for (iter = m1->begin(); iter != m1->end(); ++iter) \n" +
      "    { string key = iter->first; \n" +
      "      if (m2->count(key) == 0) \n" +
      "      { (*res)[key] = iter->second; } \n" +
      "    }     \n" +
      "    for (iter = m2->begin(); iter != m2->end(); ++iter) \n" +
      "    { string key = iter->first; \n" +
      "      (*res)[key] = iter->second; \n" + 
      "    }     \n" +
      "    return res; \n" +
      "  } \n"; 
    return res; 
  } 

  public static String generateIntersectionMapOpCPP()
  { String res = 
      "  static map<string,_T>* intersectionMap(map<string,_T>* m1, map<string,_T>* m2) \n" + 
      "  { map<string,_T>* res = new map<string,_T>(); \n" +
      "    map<string,_T>::iterator iter; \n" +
      "    for (iter = m1->begin(); iter != m1->end(); ++iter) \n" +
      "    { string key = iter->first; \n" +
      "      if (m2->count(key) > 0) \n" +
      "      { if (m2->at(key) == iter->second) \n" +
      "        { (*res)[key] = iter->second; } \n" +
      "      } \n" +
      "    }     \n" +
      "    return res; \n" +
      "  } \n\n"; 

    res = res + 
      "  static std::map<string, _T>* intersectAllMap(vector<map<string, _T>*>* se)\n" +
      "  { std::map<string, _T>* res = new std::map<string, _T>();\n" +
      "    if (se->size() == 0) { return res; }\n" +
      "    res = (*se)[0]; \n" +
      "    for (int i = 1; i < se->size(); ++i)\n" +
      "    { res = UmlRsdsLib<_T>::intersectionMap(res, (*se)[i]); }\n" +
      "    return res;\n" +
      "  }\n"; 

    return res; 
  } 

  public static String generateKeysMapOpCPP()
  { String res = 
      "  static std::set<string>* keys(map<string,_T>* s)\n" +  
      "  { map<string,_T>::iterator iter;\n" + 
      "    std::set<string>* res = new std::set<string>();\n" + 
      "  \n" + 
      "    for (iter = s->begin(); iter != s->end(); ++iter)\n" + 
      "    { string key = iter->first;\n" + 
      "      res->insert(key);\n" + 
      "    }    \n" + 
      "    return res;\n" + 
      "  }\n"; 
	return res; 
  } 
  
  public static String generateValuesMapOpCPP()
  { String res = 
      "  static vector<_T>* values(map<string,_T>* s)\n" +  
      "  { map<string,_T>::iterator iter;\n" + 
      "    vector<_T>* res = new vector<_T>();\n" + 
      "  \n" + 
      "    for (iter = s->begin(); iter != s->end(); ++iter)\n" + 
      "    { _T value = iter->second;\n" + 
      "      res->push_back(value);\n" + 
      "    }    \n" + 
      "    return res;\n" + 
      "  }\n"; 
	return res; 
  } 
  
  public static String generateRestrictOpCPP()
  { String res = 
      "  static map<string,_T>* restrict(map<string,_T>* m1, std::set<string>* ks)\n" +  
      "  { map<string,_T>* res = new map<string,_T>();\n" + 
      "    map<string,_T>::iterator iter;\n" + 
      "    for (iter = m1->begin(); iter != m1->end(); ++iter)\n" + 
      "    { string key = iter->first;\n" + 
      "      if (ks->find(key) != ks->end())\n" + 
      "      { (*res)[key] = iter->second; }\n" + 
      "    }    \n" + 
      "    return res;\n" + 
      "  }\n\n"; 
    res = res +  
      "  static map<string,_T>* antirestrict(map<string,_T>* m1, std::set<string>* ks)\n" +  
      "  { map<string,_T>* res = new map<string,_T>();\n" + 
      "    map<string,_T>::iterator iter;\n" + 
      "    for (iter = m1->begin(); iter != m1->end(); ++iter)\n" + 
      "    { string key = iter->first;\n" + 
      "      if (ks->find(key) == ks->end())\n" + 
      "      { (*res)[key] = iter->second; }\n" + 
      "    }    \n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 
  


  public static String generateTokeniseOpCPP()
  { String res = 
      "  static vector<string>* tokenise(vector<string>* res, string str)\n" + 
      "  { bool inspace = true; \n" +
      "    string* current = new string(\"\"); \n" +
      "    for (int i = 0; i < str.length(); i++)\n" + 
      "    { if (str[i] == \'.\' || isspace(str[i]) > 0)\n" +
      "      { if (inspace) {}\n" +
      "        else \n" +
      "        { res->push_back(*current);\n" + 
      "          current = new string(\"\"); \n" +
      "          inspace = true;\n" +
      "        }\n" +
      "      }\n" +
      "      else \n" +
      "      { if (inspace) { inspace = false; }\n" + 
      "        current->append(str.substr(i,1)); \n" +
      "      }\n" +
      "    }\n" +
      "    if (current->length() > 0) { res->push_back(*current); }\n" + 
      "    delete current;\n" +
      "    return res;\n" +
      "  }\n\n"; 
    return res; 
  } 

  public static String generateGCDOp()
  { String res = "  public static long gcd(long xx, long yy)\n" +
                 "  { long x = Math.abs(xx);\n" + 
                 "    long y = Math.abs(yy);\n" + 
                 "    while (x != 0 && y != 0)\n" +
                 "    { long z = y; \n" +
                 "      y = x % y; \n" +
                 "      x = z;\n" + 
                 "    } \n" +
                 "    if (y == 0)\n" +
                 "    { return x; }\n" + 
                 "    if (x == 0)\n" +
                 "    { return y; }\n" + 
                 "    return 0;\n" + 
                 "  } \n"; 
    return res; 
  } 

  public static String generateGCDOpCSharp()
  { String res = "  public static long gcd(long xx, long yy)\n" +
                 "  { long x = Math.Abs(xx);\n" + 
                 "    long y = Math.Abs(yy);\n" + 
                 "    while (x != 0 && y != 0)\n" +
                 "    { long z = y; \n" +
                 "      y = x % y; \n" +
                 "      x = z;\n" + 
                 "    } \n" +
                 "    if (y == 0)\n" +
                 "    { return x; }\n" + 
                 "    if (x == 0)\n" +
                 "    { return y; }\n" + 
                 "    return 0;\n" + 
                 "  } \n\n"; 
    res = res + 
        "  public static bool toBoolean(object sx)\n" + 
        "  { if (sx == null) { return false; }\n" + 
        "    if (\"\".Equals(sx + \"\") || \"false\".Equals(sx + \"\") || \"False\".Equals(sx + \"\"))\n" + 
        "    { return false; }\n" +
        "    try\n" + 
        "    { double d = double.Parse(sx + \"\");\n" + 
        "      if (Double.IsNaN(d) || (d <= 0.0 && d >= 0.0))\n" + 
        "      { return false; }\n" + 
        "    } catch { }\n" +  
        "    return true;\n" + 
        "  }\n\n";  

    res = res +         
       "  public static int toInteger(string sx)\n" + 
       "  { string tsx = sx.Trim();\n" + 
       "    if (tsx.StartsWith(\"0x\"))\n" + 
       "    { return Convert.ToInt32(tsx, 16); }\n" + 
       "    if (tsx.StartsWith(\"0b\"))\n" + 
       "    { return Convert.ToInt32(tsx, 2); }\n" + 
       "    if (tsx.StartsWith(\"0\") && tsx.Length > 1)\n" + 
       "    { return Convert.ToInt32(tsx, 8); }\n" + 
       "    return toInt(tsx);\n" + 
       "  } \n\n"; 

    res = res +         
       "  public static long toLong(string sx)\n" + 
       "  { string sxx = sx.Trim();\n" +  
       "    if (sxx.StartsWith(\"0x\"))\n" + 
       "    { return Convert.ToInt64(sxx, 16); }\n" + 
       "    if (sxx.StartsWith(\"0b\"))\n" + 
       "    { return Convert.ToInt64(sxx, 2); }\n" + 
       "    if (sxx.StartsWith(\"0\") && sxx.Length > 1)\n" + 
       "    { return Convert.ToInt64(sxx, 8); }\n" + 
       "    return long.Parse(sxx);\n" + 
       "  } \n\n"; 

      res = res + 
        "  public static int char2byte(string qf) \n" + 
        "  { if (qf.Length < 1) { return -1; }\n" + 
        "    return Char.ConvertToUtf32(qf, 0);\n" + 
        "  }\n" +  
        "\n" + 
        "  public static string byte2char(int qf)\n" +  
        "  { if (qf < 0) { return \"\"; }\n" + 
        "    return Char.ConvertFromUtf32(qf);\n" + 
        "  }\n\n"; 

    return res; 
  } 


  public static String generateGCDOpCPP()
  { String res = "  static long gcd(long xx, long yy)\n" +
                 "  { long x = labs(xx);\n" + 
                 "    long y = labs(yy);\n" + 
                 "    while (x != 0 && y != 0)\n" +
                 "    { long z = y; \n" +
                 "      y = x % y; \n" +
                 "      x = z;\n" + 
                 "    } \n" +
                 "    if (y == 0)\n" +
                 "    { return x; }\n" + 
                 "    if (x == 0)\n" +
                 "    { return y; }\n" + 
                 "    return 0;\n" + 
                 "  } \n"; 
    return res; 
  } 


  public static String generateRoundOpCPP()
  { String res = 
      "  static int oclRound(double d)\n" + 
      "  { int f = (int) floor(d);\n" +
      "    if (d >= f + 0.5)\n" +
      "    { return f+1; }\n" +
      "    else \n" +
      "    { return f; }\n" +
      "  }\n\n"; 
    return res; 
  } 

  public static String generateIsTypeOfOp() 
  { String res = "  public static boolean oclIsTypeOf(Object x, String E)\n" + 
      "  { try { \n" +
      "    if (x.getClass() == Class.forName(E))\n" + 
      "    { return true; } \n" +
      "    else \n" +
      "    { return false; }\n" + 
      "    } \n" +
      "    catch (Exception e) { return false; }\n" + 
      "  } \n"; 
    return res; 
  } 


  public static String generateSubrangeOpCPP()  // for C++
  { String res = "  static vector<int>* integerSubrange(int i, int j)\n" + 
                 "  { vector<int>* tmp = new vector<int>(); \n" + 
                 "    for (int k = i; k <= j; k++)\n" + 
                 "    { tmp->push_back(k); } \n" + 
                 "    return tmp;\n" + 
                 "  }\n\n" + 
                 "  static string subrange(string s, int i, int j)\n";
    res = res + "  { if (i < 1) { i = 1; }\n" + 
                "    return s.substr(i-1,j-i+1);\n" + 
                "  }\n\n";
    res = res + "  static vector<_T>* subrange(vector<_T>* l, int i, int j)\n";
    res = res + "  { if (i < 1) { i = 1; }\n" + 
                "    if (j > l->size()) { j = l->size(); }\n" + 
                "    vector<_T>* tmp = new vector<_T>(); \n" + 
                "    tmp->insert(tmp->end(), (l->begin()) + (i - 1), (l->begin()) + j);\n" +  
                "    return tmp; \n" + 
                "  }\n\n";  
    return res;
  }

  public static String generateAnyOp()
  { String res = "    public static Object any(List v)\n" +
    "    { if (v.size() == 0) { return null; }\n" +
    "      return v.get(0);\n" +
    "    }\n";
    res = res + "  public static Object any(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return any(range);  }\n\n"; 
    return res;
  }

  public static String generateAnyOpJava6()
  { String res = "    public static Object any(Collection v)\n" +
    "    { for (Object o : v) { return o; }\n" +
    "      return null;\n" + 
    "    }\n";
    return res;
  } // map

  public static String generateAnyOpJava7()
  { String res = "    public static <T> T any(Collection<T> v)\n" +
    "    { for (T o : v) { return o; }\n" +
    "      return null;\n" + 
    "    }\n";
    return res;
  } // map

  public static String generateAnyOpCSharp()
  { String res = "    public static object any(ArrayList v)\n" +
    "    { if (v.Count == 0) { return null; }\n" +
    "      return v[0];\n" +
    "    }\n";
    return res;
  } // map

  public static String generateAnyOpCPP()
  { String res = "  static _T any(vector<_T>* v)\n" +
    "    { if (v->size() == 0) { return 0; }\n" +
    "      return v->at(0);\n" +
    "    }\n\n" +
    "  static _T any(std::set<_T>* v)\n" +
    "    { if (v->size() == 0) { return 0; }\n" +
    "      std::set<_T>::iterator _pos = v->begin();\n" + 
    "      return *_pos;\n" +
    "    }\n\n";
    return res;
  } // map

  public static String generateFirstOp()
  { String res = "    public static Object first(List v)\n" +
    "    { if (v.size() == 0) { return null; }\n" +
    "      return v.get(0);\n" +
    "    }\n";
    return res;
  }

  public static String generateFirstOpJava6()
  { String res = "    public static Object first(Collection v)\n" +
    "    { for (Object o : v) { return o; }\n" +
    "      return null;\n" + 
    "    }\n";
    return res;
  }

  public static String generateFirstOpJava7()
  { String res = "    public static <T> T first(Collection<T> v)\n" +
    "    { for (T o : v) { return o; }\n" +
    "      return null;\n" + 
    "    }\n";
    return res;
  }

  public static String generateFirstOpCSharp()
  { String res = "    public static object first(ArrayList v)\n" +
    "    { if (v.Count == 0) { return null; }\n" +
    "      return v[0];\n" +
    "    }\n";
    return res;
  }

  public static String generateFirstOpCPP()
  { String res = "  static _T first(vector<_T>* v)\n" +
    "    { if (v->size() == 0) { return 0; }\n" +
    "      return v->at(0);\n" +
    "    }\n\n" +
    "  static _T first(std::set<_T>* v)\n" +
    "    { if (v->size() == 0) { return 0; }\n" +
    "      std::set<_T>::iterator _pos = v->begin();\n" + 
    "      return *_pos;\n" +
    "    }\n\n";
    return res;
  }

  public static String generateTimeOpCSharp()
  { String res = "    public static long getTime()\n" +
    "    { DateTimeOffset d = new DateTimeOffset(DateTime.Now);\n" + 
    "      return d.ToUnixTimeMilliseconds();\n" + 
    "    }\n\n";
    res = res + 
    "    public static long getTime(DateTime d)\n" +
    "    { DateTimeOffset doff = new DateTimeOffset(d);\n" + 
    "      return doff.ToUnixTimeMilliseconds();\n" + 
    "    }\n\n";

    res = res + 
    "    public static double roundTo(double x, int n)\n" +
    "    { if (n == 0) \n" +
    "      { return Math.Round(x); }\n" + 
    "      double y = x*Math.Pow(10,n); \n" +
    "      return Math.Round(y)/Math.Pow(10,n);\n" +
    "    }\n\n";   

    res = res + 
    "    public static double truncateTo(double x, int n)\n" +
    "    { if (n < 0) \n" +
    "      { return (int) x; }\n" + 
    "      double y = x*Math.Pow(10,n); \n" +
    "      return ((int) y)/Math.Pow(10,n);\n" +
    "    }\n\n";   

   return res;
  }

  public static String generateTimeOp()
  { String res = "    public static long getTime()\n" +
    "    { java.util.Date d = new java.util.Date();\n" +
    "      return d.getTime();\n" +
    "    }\n\n";

    res = res + 
    "    public static double roundN(double x, int n)\n" +
    "    { if (n == 0) \n" +
    "      { return Math.round(x); }\n" + 
    "      double y = x*Math.pow(10,n); \n" +
    "      return Math.round(y)/Math.pow(10,n);\n" +
    "    }\n\n";   

    res = res + 
    "    public static double truncateN(double x, int n)\n" +
    "    { if (n < 0) \n" +
    "      { return (int) x; }\n" + 
    "      double y = x*Math.pow(10,n); \n" +
    "      return ((int) y)/Math.pow(10,n);\n" +
    "    }\n\n";   

    return res;
  }

  public static String generateTimeOpCPP()
  { String res = "    static long long getTime()\n" +
    "    { return 1000*time(NULL); }\n\n";
    res = res + "    static struct tm* getDate()\n" +
    "    { time_t t = time(NULL);\n" + 
    "      struct tm* res = localtime(&t);\n" + 
    "      return res;\n" + 
    "    }\n";

    res = res + 
    "    static double roundTo(double x, int n)\n" +
    "    { if (n == 0) \n" +
    "      { return round(x); }\n" + 
    "      double y = x*pow(10,n); \n" +
    "      return round(y)/pow(10,n);\n" +
    "    }\n\n";   

    res = res + 
    "    static double truncateTo(double x, int n)\n" +
    "    { if (n < 0) \n" +
    "      { return (int) x; }\n" + 
    "      double y = x*pow(10,n); \n" +
    "      return ((int) y)/pow(10,n);\n" +
    "    }\n\n";   

    return res;
  }

  public static String generateSetEqualsOp()
  { String res = "    public static boolean equals(List a, List b)\n" + 
                 "    { return a.containsAll(b) && b.containsAll(a); }\n\n"; 
    return res; 
  }  // but should disregard null elements. 

  public static String generateSetEqualsOpCSharp()
  { String res = "    public static bool isSubset(ArrayList a, ArrayList b)\n" + 
                 "    { bool res = true; \n" + 
                 "      for (int i = 0; i < a.Count; i++)\n" +  
                 "      { if (a[i] != null && b.Contains(a[i])) { }\n" +  
                 "        else { return false; }\n" +  
                 "      }\n" +  
                 "      return res;\n" + 
                 "    }\n\n" +
                 "    public static bool equalsSet(ArrayList a, ArrayList b)\n" + 
                 "    { return isSubset(a,b) && isSubset(b,a); }\n\n"; 
    return res; 
  }  

  public static String generateSubcollectionsOp()
  { String res = "    public static List subcollections(List v)\n" +
    "    { Vector res = new Vector();\n" +
    "      if (v.size() == 0)\n" +
    "      { res.add(new Vector());\n" +
    "        return res;\n" +
    "      }\n" +
    "      if (v.size() == 1)\n" +
    "      { res.add(new Vector());\n" +
    "        res.add(v);\n" +
    "        return res;\n" +
    "      }\n" +
    "      Vector s = new Vector();\n" +
    "      Object x = v.get(0);\n" +
    "      s.addAll(v);\n" +
    "      s.remove(0);\n" +
    "      List scs = subcollections(s);\n" +
    "      res.addAll(scs);\n" +
    "      for (int i = 0; i < scs.size(); i++)\n" +
    "      { Vector sc = (Vector) scs.get(i);\n" +
    "        Vector scc = new Vector();\n" +
    "        scc.add(x);\n" +
    "        scc.addAll(sc);\n" +
    "        res.add(scc);\n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n";
    return res;
  }

  public static String generateSubcollectionsOpCSharp()
  { String res = "    public static ArrayList subcollections(ArrayList v)\n" +
    "    { ArrayList res = new ArrayList();\n" +
    "      if (v.Count == 0)\n" +
    "      { res.Add(new ArrayList());\n" +
    "        return res;\n" +
    "      }\n" +
    "      if (v.Count == 1)\n" +
    "      { res.Add(new ArrayList());\n" +
    "        res.Add(v);\n" +
    "        return res;\n" +
    "      }\n" +
    "      ArrayList s = new ArrayList();\n" +
    "      object x = v[0];\n" +
    "      s.AddRange(v);\n" +
    "      s.RemoveAt(0);\n" +
    "      ArrayList scs = subcollections(s);\n" +
    "      res.AddRange(scs);\n" +
    "      for (int i = 0; i < scs.Count; i++)\n" +
    "      { ArrayList sc = (ArrayList) scs[i];\n" +
    "        ArrayList scc = new ArrayList();\n" +
    "        scc.Add(x);\n" +
    "        scc.AddRange(sc);\n" +
    "        res.Add(scc);\n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n";
    return res;
  }



  public static String generateMaxOp()  // for Java
  { String res = "  public static Comparable max(List l)\n";
    res = res + "  { Comparable res = null; \n";
    res = res + "    if (l.size() == 0) { return res; }\n";
    res = res + "    res = (Comparable) l.get(0); \n";
    res = res + "    for (int i = 1; i < l.size(); i++)\n";
    res = res + "    { Comparable e = (Comparable) l.get(i);\n";
    res = res + "      if (res.compareTo(e) < 0) { res = e; } }\n";
    res = res + "    return res; }\n";
    res = res + "  public static Comparable max(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return max(range);  }\n\n"; 

    return res;
  }

  public static String generateMaxOpJava6()  // for Java6
  { String res = "  public static Comparable max(Collection l)\n";
    res = res + "  { Comparable res = null; \n";
    res = res + "    if (l.size() == 0) { return res; }\n";
    res = res + "    res = (Comparable) Set.any(l); \n";
    res = res + "    for (Object _o : l)\n";
    res = res + "    { Comparable e = (Comparable) _o;\n";
    res = res + "      if (res.compareTo(e) < 0) { res = e; } }\n";
    res = res + "    return res; }\n";
    return res;
  } // map

  public static String generateMaxOpJava7()  // for Java7
  { /* String res = "  public static <T> T max(Collection<T> l)\n";
    res = res +  "  { return Collections.max(l); }\n";
    return res;
    } */ 

    String res = "  public static <T extends Comparable> T max(Collection<T> s)\n" +
    "  { ArrayList<T> slist = new ArrayList<T>();\n" + 
    "    slist.addAll(s); \n" +
    "    T result = slist.get(0);\n" + 
    "    for (int i = 1; i < slist.size(); i++)\n" + 
    "    { T val = slist.get(i); \n" +
    "      if (0 < val.compareTo(result))\n" + 
    "      { result = val; } \n" +
    "    } \n" +
    "    return result;\n" + 
    "  } \n"; 
    return res; 
  } 


  public static String generateMaxOpCSharp()  // for CSharp
  { String res = "  public static object max(ArrayList l)\n";
    res = res + "  { IComparable res = null; \n";
    res = res + "    if (l.Count == 0) { return res; }\n";
    res = res + "    res = (IComparable) l[0]; \n";
    res = res + "    for (int i = 1; i < l.Count; i++)\n";
    res = res + "    { IComparable e = (IComparable) l[i];\n";
    res = res + "      if (res.CompareTo(e) < 0) { res = e; } }\n";
    res = res + "    return res; }\n";
    return res;
  } // map

  public static String generateMaxOpCPP()  // for C++
  { String res = "  static _T max(std::set<_T>* l)\n";
    res = res + "  { return *std::max_element(l->begin(), l->end()); }\n";
    res = res + "  static _T max(vector<_T>* l)\n";
    res = res + "  { return *std::max_element(l->begin(), l->end()); }\n";
    return res;
  } // map

  public static String generateMinOp()
  { String res = "  public static Comparable min(List l)\n";
    res = res + "  { Comparable res = null; \n";
    res = res + "    if (l.size() == 0) { return res; }\n";
    res = res + "    res = (Comparable) l.get(0); \n";
    res = res + "    for (int i = 1; i < l.size(); i++)\n";
    res = res + "    { Comparable e = (Comparable) l.get(i);\n";
    res = res + "      if (res.compareTo(e) > 0) { res = e; } }\n";
    res = res + "    return res; }\n";
    res = res + "  public static Comparable min(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return min(range);  }\n\n"; 
    return res;
  }

  public static String generateMinOpJava6()
  { String res = "  public static Comparable min(Collection l)\n";
    res = res + "  { Comparable res = null; \n";
    res = res + "    if (l.size() == 0) { return res; }\n";
    res = res + "    res = (Comparable) Set.any(l); \n";
    res = res + "    for (Object _o : l)\n";
    res = res + "    { Comparable e = (Comparable) _o;\n";
    res = res + "      if (res.compareTo(e) > 0) { res = e; } }\n";
    res = res + "    return res; }\n";
    return res;
  } // map

  public static String generateMinOpJava7()  // for Java7 - not needed. 
  { // String res = "  public static <T> T min(Collection<T> l)\n";
    // res = res +  "  { return Collections.min(l); }\n";
    // return res;
    String res = "  public static <T extends Comparable> T min(Collection<T> s)\n" +
    "  { ArrayList<T> slist = new ArrayList<T>();\n" + 
    "    slist.addAll(s); \n" +
    "    T result = slist.get(0);\n" + 
    "    for (int i = 1; i < slist.size(); i++)\n" + 
    "    { T val = slist.get(i); \n" +
    "      if (val.compareTo(result) < 0)\n" + 
    "      { result = val; } \n" +
    "    } \n" +
    "    return result;\n" + 
    "  } \n"; 
    return res; 
  } // and for map

  public static String generateMinOpCSharp()
  { String res = "  public static object min(ArrayList l)\n";
    res = res + "  { IComparable res = null; \n";
    res = res + "    if (l.Count == 0) { return res; }\n";
    res = res + "    res = (IComparable) l[0]; \n";
    res = res + "    for (int i = 1; i < l.Count; i++)\n";
    res = res + "    { IComparable e = (IComparable) l[i];\n";
    res = res + "      if (res.CompareTo(e) > 0) { res = e; } }\n";
    res = res + "    return res; }\n";
    return res;
  } // map

  public static String generateMinOpCPP()  // for C++
  { String res = "  static _T min(std::set<_T>* l)\n";
    res = res + "  { return *std::min_element(l->begin(), l->end()); }\n";
    res = res + "  static _T min(vector<_T>* l)\n";
    res = res + "  { return *std::min_element(l->begin(), l->end()); }\n";
    return res;
  } // map

  public static String generateBeforeOp()
  { String res = "  public static String before(String s, String sep)\n" +
      "  { if (sep.length() == 0) { return s; }\n" +
      "    int ind = s.indexOf(sep);\n" +
      "    if (ind < 0) { return s; }\n" +
      "    return s.substring(0,ind); \n" + 
      "  }\n";
    return res;
  }

  public static String generateAfterOp()
  { String res = "  public static String after(String s, String sep)\n" +
      "  { int ind = s.indexOf(sep);\n" +
      "    int seplength = sep.length();\n" +
      "    if (ind < 0) { return \"\"; }\n" +
      "    if (seplength == 0) { return \"\"; }\n" +
      "    return s.substring(ind + seplength, s.length()); \n" + 
      "  }\n";
    return res;
  }

  public static String generateIsMatchOp()
  { String res = "  public static boolean isMatch(String str, String regex)\n" + 
                 "  { return str.matches(regex); }\n\n"; 
    return res;
  }

  public static String generateHasMatchOp()
  { String res = "  public static boolean hasMatch(String str, String regex)\n" + 
                 "  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);\n" +  
                 "    java.util.regex.Matcher matcher = patt.matcher(str); \n" + 
                 "    if (matcher.find())\n" + 
                 "    { return true; }\n" + 
                 "    return false;\n" +  
                 "  }\n\n"; 
    return res; 
  } 

  public static String generateSplitOp()
  { String res = "  public static Vector split(String str, String delim)\n" + 
      "  { String[] splits = str.split(delim);\n" +        
      "    Vector res = new Vector();\n" +  
      "    for (int j = 0; j < splits.length; j++)\n" + 
      "    { if (splits[j].length() > 0)\n" + 
      "      { res.add(splits[j]); }\n" +
      "    }\n" + 
      "    return res;\n" +
      "  }\n"; 
    return res;  
  }
  
  public static String generateAllMatchesOp()
  { String res = "  public static Vector allMatches(String str, String regex)\n" + 
      "  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);\n" +  
      "    java.util.regex.Matcher matcher = patt.matcher(str);\n" +  
      "    Vector res = new Vector();\n" + 
      "    while (matcher.find())\n" +
      "    { res.add(matcher.group() + \"\"); }\n" +
      "    return res; \n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateFirstMatchOp()
  { String res = "  public static String firstMatch(String str, String regex)\n" + 
      "  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);\n" +  
      "    java.util.regex.Matcher matcher = patt.matcher(str);\n" +  
      "    String res = null;\n" + 
      "    if (matcher.find())\n" +
      "    { res = matcher.group() + \"\"; }\n" +
      "    return res; \n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateSplitOpJava6()
  { String res = "  public static ArrayList split(String str, String delim)\n" + 
      "  { String[] splits = str.split(delim);\n" +        
      "    ArrayList res = new ArrayList();\n" +  
      "    for (int j = 0; j < splits.length; j++)\n" + 
      "    { if (splits[j].length() > 0)\n" + 
      "      { res.add(splits[j]); }\n" +
      "    }\n" + 
      "    return res;\n" +
      "  }\n"; 
    return res;  
  }
  
  public static String generateAllMatchesOpJava6()
  { String res = "  public static ArrayList allMatches(String str, String regex)\n" + 
      "  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);\n" +  
      "    java.util.regex.Matcher matcher = patt.matcher(str);\n" +  
      "    ArrayList res = new ArrayList();\n" + 
      "    while (matcher.find())\n" +
      "    { res.add(matcher.group() + \"\"); }\n" +
      "    return res; \n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateSplitOpJava7()
  { String res = "  public static ArrayList<String> split(String str, String delim)\n" + 
      "  { String[] splits = str.split(delim);\n" +        
      "    ArrayList<String> res = new ArrayList<String>();\n" +  
      "    for (int j = 0; j < splits.length; j++)\n" + 
      "    { if (splits[j].length() > 0) \n" + 
      "      { res.add(splits[j]); }\n" +
      "    }\n" + 
      "    return res;\n" +
      "  }\n"; 
    return res;  
  }
  
  public static String generateAllMatchesOpJava7()
  { String res = "  public static ArrayList<String> allMatches(String str, String regex)\n" + 
      "  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);\n" +  
      "    java.util.regex.Matcher matcher = patt.matcher(str);\n" +  
      "    ArrayList<String> res = new ArrayList<String>();\n" + 
      "    while (matcher.find())\n" +
      "    { res.add(matcher.group() + \"\"); }\n" +
      "    return res; \n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateHasMatchOpCSharp()
  { String res = "  public static bool hasMatch(string s, string patt)\n" +
      "  { Regex r = new Regex(patt);\n" + 
      "    return r.IsMatch(s);\n" + 
      "  }\n";  
    return res; 
  } 

  public static String generateIsMatchOpCSharp()
  { String res = "  public static bool isMatch(string s, string patt)\n" +
      "  { Regex r = new Regex(patt);\n" + 
      "    Match m = r.Match(s);\n" + 
      "    return (m + \"\").Equals(s);\n" + 
      "  }\n"; 
    return res; 
  } 


  public static String generateAllMatchesOpCSharp()
  { String res = "  public static ArrayList allMatches(string s, string patt)\n" +
      "  { Regex r = new Regex(patt);\n" + 
      "    MatchCollection col = r.Matches(s);\n" +  
      "    ArrayList res = new ArrayList();\n" +  
      "    foreach (Match mm in col)\n" + 
      "    { res.Add(mm.Value + \"\"); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateFirstMatchOpCSharp()
  { String res = "  public static string firstMatch(string s, string patt)\n" +
      "  { Regex r = new Regex(patt);\n" + 
      "    Match m = r.Match(s);\n" + 
      "    if (m.Success)\n" + 
      "    { return m.Value + \"\"; }\n" + 
      "    return null;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateIsMatchOpCPP()
  { String res = "  static bool isMatch(string str, string patt)\n" + 
                 "  { std::regex rr(patt);\n" + 
                 "    return std::regex_match(str,rr);\n" + 
                 "  }\n\n"; 
    return res;
  }

  public static String generateHasMatchOpCPP()
  { String res = "  static bool hasMatch(string str, string patt)\n" + 
                 "  { std::regex rr(patt);\n" + 
                 "    return std::regex_search(str,rr);\n" + 
                 "  }\n\n"; 
    return res; 
  } 

  public static String generateTrimOpCPP()
  { String res = "  static string trim(string str)\n" + 
      "  { int i = str.find_first_not_of(\"\\n\\t \");\n" +  
      "    int j = str.find_last_not_of(\"\\n\\t \");\n" + 
      "    if (i > j) \n" +
      "    { return \"\"; }\n" +
      "    return str.substr(i, j-i+1);\n" + 
      "  } \n"; 
    return res; 
  } 

  public static String generateAllMatchesOpCPP()
  { String res = 
      "  static vector<string>* allMatches(string s, string patt)\n" + 
      "  { int slen = s.length(); \n" + 
      "    vector<string>* res = new vector<string>(); \n" +
      "    if (slen == 0)  \n" +
      "    { return res; }  \n" +
      "    std::regex patt_regex(patt);\n" + 
      "    auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);\n" + 
      "    auto words_end = std::sregex_iterator();\n" + 
      "    \n" + 
      "    for (std::sregex_iterator i = words_begin; i != words_end; ++i)\n" + 
      "    { std::smatch match = *i;\n" +  
      "      std::string match_str = match.str();\n" + 
      "      if (match_str.length() > 0)\n" + 
      "      { res->push_back(match_str); }\n" +    
      "    }\n" +    
      "    return res;\n" +  
      "  }\n"; 
    return res; 
  }  

  public static String generateFirstMatchOpCPP()
  { String res = 
      "  static string firstMatch(string s, string patt)\n" + 
      "  { int slen = s.length(); \n" + 
      "    string res = \"\"; \n" +
      "    if (slen == 0)  \n" +
      "    { return res; }  \n" +
      "    std::regex patt_regex(patt);\n" + 
      "    auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);\n" + 
      "    auto words_end = std::sregex_iterator();\n" + 
      "    \n" + 
      "    for (std::sregex_iterator i = words_begin; i != words_end; ++i)\n" + 
      "    { std::smatch match = *i;\n" +  
      "      std::string match_str = match.str();\n" + 
      "      if (match_str.length() > 0)\n" + 
      "      { return match_str; }\n" +    
      "    }\n" +    
      "    return res;\n" +  
      "  }\n"; 
    return res; 
  }  

  public static String generateReplaceOpCPP()
  { String res = 
      "  static string replace(string s1, string s2, string rep)\n" +
      "  { int s1len = s1.length(); \n" +
      "    int s2len = s2.length(); \n" +
      "    int replen = rep.length(); \n" +
      "    if (s1len == 0 || s2len == 0 || replen == 0)\n" + 
      "    { return s1; } \n" +
      "    string result = \"\";\n" + 
      "    int prev = 0; \n" +
      "    int m1 = s1.find(s2);\n" + 
      "    if (m1 >= 0)\n" + 
      "    { result = result + s1.substr(prev, m1 - prev) + rep; \n" +
      "      string remainder = s1.substr(m1 + s2len, s1len - (m1 + s2len)); \n" +
      "      return result + replace(remainder, s2, rep);\n" + 
      "    } \n" +
      "    return s1;\n" + 
      "  } \n"; 
    return res; 
  } // substituteAll

  public static String generateReplaceAllOpCPP()
  { String res = "  static string replaceAll(string text, string patt, string rep)\n" + 
                 "  { std::regex patt_re(patt);\n" + 
                 "    std::string res = std::regex_replace(text, patt_re, rep);\n" +    
                 "    return res;\n" +  
                 "  }\n"; 
    return res; 
  } // replaceAllMatches

  public static String generateReplaceFirstOpCPP()
  { String res = "  static string replaceFirstMatch(string text, string patt, string rep)\n" + 
                 "  { std::regex patt_re(patt);\n" + 
                 "    std::regex_constants::match_flag_type fonly =\n" + 
                 "           std::regex_constants::format_first_only;\n" + 

                 "    std::string res = std::regex_replace(text, patt_re, rep, fonly);\n" + 

                 "    return res;\n" +  
                 "  }\n"; 
    return res; 
  } 
 
  public static String generateSplitOpCPP()
  { String res = "  static vector<string>* split(string s, string patt)\n" +
      "  { int slen = s.length();\n" + 
      "    vector<string>* res = new vector<string>();\n" +
      "    if (slen == 0) \n" +
      "    { res->push_back(s);\n" + 
      "      return res; \n" +
      "    } \n" +
      "    std::regex patt_regex(patt);\n" +
      "    auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);\n" +
      "    auto words_end = std::sregex_iterator();\n" +
      "    int prev = 0; \n" +
      "    for (std::sregex_iterator i = words_begin; i != words_end; ++i)\n" +
      "    { std::smatch match = *i;\n" +
      "      int pos = match.position(0);\n" + 
      "      int ln = match.length(0); \n" +
      "      if (ln > 0)\n" +
      "      { string subst = s.substr(prev, pos - prev + 1);\n" + 
      "        res->push_back(subst);\n" + 
      "        prev = pos + ln; \n" +
      "      } \n" +
      "    }\n" +
      "    if (prev <= slen)\n" +
      "    { string lastst = s.substr(prev,slen - prev + 1);\n" + 
      "      res->push_back(lastst);\n" + 
      "    } \n" +
      "    return res;\n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateReplaceOp()
  { String res = "  public static String replace(String str, String delim, String rep)\n" + 
      "  { String result = \"\";\n" + 
      "    String s = str + \"\";\n" +  
      "    int i = (s.indexOf(delim) + 1);\n" + 
      "    if (i == 0)\n" +  
      "    { return s; }\n" + 
      "    \n" + 
      "    int sublength = delim.length();\n" + 
      "    if (sublength == 0)\n" + 
      "    { return s; }\n" +
      "    \n" + 
      "    while (i > 0)\n" +
      "    { result = result + Set.subrange(s,1,i - 1) + rep;\n" + 
      "      s = Set.subrange(s,i + delim.length(),s.length());\n" + 
      "      i = (s.indexOf(delim) + 1);\n" + 
      "    }\n" + 
      "    result = result + s;\n" + 
      "    return result;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateReplaceOpJava7()
  { String res = "  public static String replace(String str, String delim, String rep)\n" + 
      "  { String result = \"\";\n" + 
      "    String s = str + \"\";\n" +  
      "    int i = (s.indexOf(delim) + 1);\n" + 
      "    if (i == 0)\n" +  
      "    { return s; }\n" + 
      "    \n" + 
      "    int sublength = delim.length();\n" + 
      "    if (sublength == 0)\n" + 
      "    { return s; }\n" +
      "    \n" + 
      "    while (i > 0)\n" +
      "    { result = result + Ocl.subrange(s,1,i - 1) + rep;\n" + 
      "      s = Ocl.subrange(s,i + delim.length(),s.length());\n" + 
      "      i = (s.indexOf(delim) + 1);\n" + 
      "    }\n" + 
      "    result = result + s;\n" + 
      "    return result;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateReplaceAllOp()
  { String res = "  public static String replaceAll(String str, String regex, String rep)\n" + 
      "  { if (str == null) { return null; }\n" + 
      "    java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);\n" +  
      "    java.util.regex.Matcher matcher = patt.matcher(str);\n" +  
      "    return matcher.replaceAll(rep);\n" +  
      "  }\n"; 
    return res; 
  } 

  public static String generateReplaceFirstOp()
  { String res = "  public static String replaceFirstMatch(String str, String regex, String rep)\n" + 
      "  { if (str == null) { return null; }\n" + 
      "    java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);\n" +  
      "    java.util.regex.Matcher matcher = patt.matcher(str);\n" +  
      "    return matcher.replaceFirst(rep);\n" +  
      "  }\n"; 
    return res; 
  } 

  public static String generateReplaceOpCSharp()
  { String res = "  public static string replace(string str, string delim, string rep)\n" + 
        "  { if (str == null) { return null; }\n" + 
        "    String result = \"\";\n" +
        "    String s = str + \"\";\n" +
        "    int i = (s.IndexOf(delim) + 1);\n" +
        "    if (i == 0)\n" +
        "    { return s; }\n" +
        "\n" +
        "    int sublength = delim.Length;\n" +
        "    if (sublength == 0)\n" +
        "    { return s; }\n" +
        "\n" +
        "    while (i > 0)\n" +
        "    { result = result + SystemTypes.subrange(s, 1, i - 1) + rep;\n" +
        "      s = SystemTypes.subrange(s, i + delim.Length, s.Length);\n" +
        "      i = (s.IndexOf(delim) + 1);\n" +
        "    }\n" +
        "    result = result + s;\n" +
        "    return result;\n" +
        "  }\n"; 
     return res; 
  } 



  public static String generateReplaceAllOpCSharp()
  { String res = "  public static string replaceAll(string s, string patt, string rep)\n" +
      "  { if (s == null) { return null; }\n" + 
      "    Regex r = new Regex(patt);\n" + 
      "    return \"\" + r.Replace(s, rep);\n" + 
      "  }\n"; 
    return res; 
  }  

  public static String generateReplaceFirstOpCSharp()
  { String res = "  public static string replaceFirstMatch(string s, string patt, string rep)\n" +
      "  { if (s == null) { return null; }\n" + 
      "    Regex r = new Regex(patt);\n" + 
      "    return \"\" + r.Replace(s, rep, 1);\n" + 
      "  }\n"; 
    return res; 
  }  
 
  public static String generateSplitOpCSharp()
  { String res = "  public static ArrayList split(string s, string patt)\n" +
      "  { Regex r = new Regex(patt);\n" + 
      "    ArrayList res = new ArrayList();\n" +  
      "    string[] wds = r.Split(s);\n" + 
      "    for (int x = 0; x < wds.Length; x++)\n" + 
      "    { if (wds[x].Length > 0)\n" + 
      "      { res.Add(wds[x]); }\n" + 
      "    }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 
 
  public static String generateBeforeOpCSharp()
  { String res = "  public static string before(string s, string sep)\n" +
      "  { if (sep.Length == 0) { return s; }\n" +
      "    int ind = s.IndexOf(sep);\n" +
      "    if (ind < 0) { return s; }\n" +
      "    return s.Substring(0,ind); \n" + 
      "  }\n";
    return res;
  }

  public static String generateAfterOpCSharp()
  { String res = "  public static string after(string s, string sep)\n" +
      "  { int ind = s.IndexOf(sep);\n" +
      "    int seplength = sep.Length;\n" +
      "    if (ind < 0) { return \"\"; }\n" +
      "    if (seplength == 0) { return \"\"; }\n" +
      "    return s.Substring(ind + seplength, s.Length - (ind + seplength)); \n" + 
      "  }\n";
    return res;
  }

  public static String generateBeforeOpCPP()
  { String res = "  static string before(string s, string sep)\n" +
      "  { if (sep.length() == 0) { return s; }\n" +
      "    if (s.find(sep) == string::npos) { return s; }\n" +
      "    return s.substr(0,s.find(sep)); \n" + 
      "  }\n";
    return res;
  }

  public static String generateAfterOpCPP()
  { String res = "  static string after(string s, string sep)\n" +
      "  { int seplength = sep.length();\n" +
      "    if (s.find(sep) == string::npos) { return \"\"; }\n" +
      "    if (seplength == 0) { return \"\"; }\n" +
      "    return s.substr(s.find(sep) + seplength, s.length() - (s.find(sep) + seplength)); \n" + 
      "  }\n";
    return res;
  }

  public static String generateUnionOp()  // Should only be used if first one is a set. 
  { String res = "  public static List union(List a, List b)\n" +
      "  { List res = new Vector(); \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Object x = a.get(i); \n" + 
      "      if (x == null || res.contains(x)) { } else { res.add(x); } \n" + 
      "    }\n" +
      "    for (int j = 0; j < b.size(); j++)\n" +
      "    { Object y = b.get(j); \n" + 
      "      if (y == null || res.contains(y)) { } else { res.add(y); }\n" + 
      "    }\n" +
      "    return res; }\n";
    return res;
  }

  public static String generateUnionOpJava6()
  { String res = "  public static HashSet union(HashSet a, Collection b)\n" +
      "  { HashSet res = new HashSet(); \n" +
      "    res.addAll(a); res.addAll(b);\n" +
      "    return res; }\n\n" + 
      "  public static ArrayList union(ArrayList a, Collection b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    res.addAll(a); res.addAll(b);\n" +
      "    return res; }\n\n";  
    return res;
  }

  public static String generateUnionOpJava7()
  { String res = "  public static <T> HashSet<T> union(HashSet a, Collection<T> b)\n" +
      "  { HashSet<T> res = new HashSet<T>(); \n" +
      "    for (Object x : a)\n" +
      "    { try \n" +
      "      { T y = (T) x; \n" +
      "        res.add(y); \n" +
      "      } catch (Exception _e) { }\n" + 
      "    }\n" +
      "    res.addAll(b);\n" +
      "    return res;\n" + 
      "  }\n\n" + 
      "  public static <T> TreeSet<T> union(TreeSet<T> a, Collection<T> b)\n" +
      "  { TreeSet<T> res = (TreeSet<T>) a.clone(); \n" +
      "    res.addAll(b);\n" +
      "    return res;\n" + 
      "  }\n\n" + 
      "  public static <T> ArrayList<T> union(ArrayList<T> a, Collection<T> b)\n" +
      "  { ArrayList<T> res = new ArrayList<T>(); \n" +
      "    res.addAll(a); res.addAll(b);\n" +
      "    return res; }\n";
    return res;
  }

  public static String generateUnionOpCSharp()
  { String res = "  public static ArrayList union(ArrayList a, ArrayList b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { if (a[i] == null || res.Contains(a[i])) { } else { res.Add(a[i]); } }\n" +
      "    for (int j = 0; j < b.Count; j++)\n" +
      "    { if (b[j] == null || res.Contains(b[j])) { } else { res.Add(b[j]); } }\n" +
      "    return res; }\n";
    return res;
  } // if both are sequences, concatenate is used. 

  public static String generateUnionOpCPP()
  { String res = "  static std::set<_T>* unionSet(std::set<_T>* a, std::set<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    res->insert(a->begin(),a->end()); \n" +
      "    res->insert(b->begin(),b->end()); \n" +
      "    return res;\n" + 
      "  }\n\n";
    res = res + "  static std::set<_T>* unionSet(vector<_T>* a, std::set<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    res->insert(a->begin(),a->end()); \n" +
      "    res->insert(b->begin(),b->end()); \n" +
      "    return res;\n" + 
      "  }\n\n";
    res = res + "  static std::set<_T>* unionSet(std::set<_T>* a, vector<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    res->insert(a->begin(),a->end()); \n" +
      "    res->insert(b->begin(),b->end()); \n" +
      "    return res;\n" + 
      "  }\n\n";
    res = res + "  static std::set<_T>* unionSet(std::vector<_T>* a, vector<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    res->insert(a->begin(),a->end()); \n" +
      "    res->insert(b->begin(),b->end()); \n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  }

  public static String generateConcatOp()
  { String res = "  public static List concatenate(List a, List b)\n" +
      "  { List res = new Vector(); \n" +
      "    res.addAll(a); \n" + 
      "    res.addAll(b); \n" + 
      "    return res;\n" + 
      "  }\n";
    return res;
  }

  public static String generateConcatOpJava6()
  { String res = "  public static ArrayList concatenate(Collection a, Collection b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    res.addAll(a); \n" + 
      "    res.addAll(b); \n" + 
      "    return res;\n" + 
      "  }\n";
    return res;
  }

  public static String generateConcatOpJava7()
  { String res = "  public static <T> ArrayList<T> concatenate(Collection<T> a, Collection<T> b)\n" +
      "  { ArrayList<T> res = new ArrayList<T>(); \n" +
      "    res.addAll(a); \n" + 
      "    res.addAll(b); \n" + 
      "    return res;\n" + 
      "  }\n";
    return res;
  }

  public static String generateConcatOpCSharp()
  { String res = "  public static ArrayList concatenate(ArrayList a, ArrayList b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    res.AddRange(a); \n" + 
      "    res.AddRange(b); \n" + 
      "    return res;\n" + 
      "  }\n";
    res = res + 
      "  public static ArrayList prepend(ArrayList a, object x)\n" +
      "  {\n" +
      "    ArrayList res = new ArrayList();\n" +
      "    res.Add(x);\n" +
      "    res.AddRange(a);\n" +
      "    return res;\n" +
      "  }\n\n";

    res = res +
      "  public static ArrayList append(ArrayList a, object x)\n" +
      "  {\n" +
      "    ArrayList res = new ArrayList();\n" +    
      "    res.AddRange(a);\n" +
      "    res.Add(x); \n" +
      "    return res;\n" +
      "  }\n\n";

    return res;
  }

  public static String generateConcatOpCPP()
  { String res = "  static vector<_T>* concatenate(vector<_T>* a, vector<_T>* b)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    res->insert(res->end(), a->begin(),a->end()); \n" +
      "    res->insert(res->end(), b->begin(),b->end()); \n" +
      "    return res;\n" + 
      "  }\n\n";
    res = res + "  static vector<_T>* concatenate(vector<_T>* a, std::set<_T>* b)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    res->insert(res->end(), a->begin(),a->end()); \n" +
      "    res->insert(res->end(), b->begin(),b->end()); \n" +
      "    return res;\n" + 
      "  }\n";
    return res;
  }

  public static String generateSubtractOp()
  { String res = "  public static List subtract(List a, List b)\n" +
      "  { List res = new Vector(); \n" +
      "    res.addAll(a);\n" +
      "    res.removeAll(b);\n" +
      "    return res;\n" + 
      "  }\n\n" + 
      "  public static String subtract(String a, String b)\n" +
      "  { String res = \"\"; \n" +
      "    for (int i = 0; i < a.length(); i++)\n" +
      "    { if (b.indexOf(a.charAt(i)) < 0)\n" + 
      "      { res = res + a.charAt(i); }\n" + 
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  }

  public static String generateSubtractOpJava6()
  { String res = "  public static HashSet subtract(HashSet a, Collection b)\n" +
      "  { HashSet res = new HashSet(); \n" +
      "    res.addAll(a);\n" +
      "    res.removeAll(b);\n" +
      "    return res;\n" + 
      "  }\n\n" + 
      "  public static ArrayList subtract(ArrayList a, Collection b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    res.addAll(a);\n" +
      "    res.removeAll(b);\n" +
      "    return res;\n" + 
      "  }\n\n" + 
      "  public static String subtract(String a, String b)\n" +
      "  { String res = \"\"; \n" +
      "    for (int i = 0; i < a.length(); i++)\n" +
      "    { if (b.indexOf(a.charAt(i)) < 0) { res = res + a.charAt(i); } }\n" +
      "    return res; }\n\n";
    return res;
  }

  public static String generateSubtractOpJava7()
  { String res = "  public static <T> HashSet<T> subtract(HashSet<T> a, Collection<T> b)\n" +
      "  { HashSet<T> res = new HashSet<T>(); \n" +
      "    res.addAll(a);\n" +
      "    res.removeAll(b);\n" +
      "    return res; }\n\n" +
      "  public static <T> TreeSet<T> subtract(TreeSet<T> a, Collection<T> b)\n" +
      "  { TreeSet<T> res = new TreeSet<T>(); \n" +
      "    res.addAll(a);\n" +
      "    res.removeAll(b);\n" +
      "    return res; }\n\n" + 
      "  public static <T> ArrayList<T> subtract(ArrayList<T> a, Collection<T> b)\n" +
      "  { ArrayList<T> res = new ArrayList<T>(); \n" +
      "    res.addAll(a);\n" +
      "    res.removeAll(b);\n" +
      "    return res; }\n\n" + 
      "  public static String subtract(String a, String b)\n" +
      "  { String res = \"\"; \n" +
      "    for (int i = 0; i < a.length(); i++)\n" +
      "    { if (b.indexOf(a.charAt(i)) < 0) { res = res + a.charAt(i); } }\n" +
      "    return res; }\n\n";
    return res;
  }

  public static String generateSubtractOpCSharp()
  { String res = "  public static ArrayList subtract(ArrayList a, ArrayList b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < a.Count; i++)\n" + 
      "    { if (a[i] == null || b.Contains(a[i])) {}\n" + 
      "      else { res.Add(a[i]); }\n" +
      "    }\n" +
      "    return res; }\n\n" +
      "  public static ArrayList subtract(ArrayList a, object b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < a.Count; i++)\n" + 
      "    { if (a[i] == null || b == a[i]) {}\n" + 
      "      else { res.Add(a[i]); }\n" +
      "    }\n" +
      "    return res; }\n\n" + 
      "  public static string subtract(string a, string b)\n" +
      "  { string res = \"\"; \n" +
      "    for (int i = 0; i < a.Length; i++)\n" +
      "    { if (b.IndexOf(a[i]) < 0) { res = res + a[i]; } }\n" +
      "    return res; }\n\n";
    return res;
  }

  public static String generateSubtractOpCPP()
  { String res = "  static vector<_T>* subtract(vector<_T>* a, vector<_T>* b)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    for (int i = 0; i < a->size(); i++)\n" +
      "    { if (UmlRsdsLib<_T>::isIn((*a)[i],b)) { }\n" +
      "      else { res->push_back((*a)[i]); }\n" +
      "    }\n" + 
      "    return res;\n" + 
      "  }\n\n" +
      "  static vector<_T>* subtract(vector<_T>* a, std::set<_T>* b)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    for (int i = 0; i < a->size(); i++)\n" +
      "    { if (UmlRsdsLib<_T>::isIn((*a)[i],b)) { }\n" +
      "      else\n" + 
      "      { res->push_back((*a)[i]); }\n" +
      "    }\n" + 
      "    return res;\n" + 
      "  }\n\n" +
      "  static std::set<_T>* subtract(std::set<_T>* a, std::set<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    for (std::set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)\n" +
      "    { if (UmlRsdsLib<_T>::isIn(*_pos,b)) { }\n" +
      "      else\n" + 
      "      { res->insert(*_pos); }\n" +
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n" +  
      "  static std::set<_T>* subtract(std::set<_T>* a, vector<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    for (std::set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)\n" +
      "    { if (UmlRsdsLib<_T>::isIn(*_pos,b)) { }\n" +
      "      else\n" + 
      "      { res->insert(*_pos); }\n" +
      "    }\n" + 
      "    return res;\n" + 
      "  }\n\n" +  
      "  static string subtract(string a, string b)\n" +
      "  { string res = \"\"; \n" +
      "    for (int i = 0; i < a.length(); i++)\n" +
      "    { if (b.find(a[i]) == string::npos) { res = res + a[i]; } }\n" +
      "    return res; }\n\n";
    return res;
  }

  public static String generateIntersectionOp()
  { String res = "  public static List intersection(List a, List b)\n" +
      "  { List res = new Vector(); \n" +
      "    res.addAll(a);\n" +
      "    res.retainAll(b);\n" +
      "    return res; }\n\n";
    return res;
  }

  public static String generateIntersectionOpJava6()
  { String res = "  public static HashSet intersection(HashSet a, Collection b)\n" +
      "  { HashSet res = new HashSet(); \n" +
      "    res.addAll(a);\n" +
      "    res.retainAll(b);\n" +
      "    return res; }\n\n" + 
      "  public static ArrayList intersection(ArrayList a, Collection b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    res.addAll(a);\n" +
      "    res.retainAll(b);\n" +
      "    return res; }\n\n";
    return res;   // shouldn't it always be a set?
  }

  public static String generateIntersectionOpJava7()
  { String res = "  public static <T> HashSet<T> intersection(HashSet<T> a, Collection<T> b)\n" +
      "  { HashSet<T> res = new HashSet<T>(); \n" +
      "    res.addAll(a);\n" +
      "    res.retainAll(b);\n" +
      "    return res; }\n\n" +
      "  public static <T> TreeSet<T> intersection(TreeSet<T> a, Collection<T> b)\n" +
      "  { TreeSet<T> res = new TreeSet<T>(); \n" +
      "    res.addAll(a);\n" +
      "    res.retainAll(b);\n" +
      "    return res; }\n\n" + 
      "  public static <T> ArrayList<T> intersection(ArrayList<T> a, Collection<T> b)\n" +
      "  { ArrayList<T> res = new ArrayList<T>(); \n" +
      "    res.addAll(a);\n" +
      "    res.retainAll(b);\n" +
      "    return res; }\n\n";
    return res;   // shouldn't it always be a set?
  } // TreeSet version is valid? 

  public static String generateIntersectionOpCSharp()
  { String res = "  public static ArrayList intersection(ArrayList a, ArrayList b)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < a.Count; i++)\n" + 
      "    { if (a[i] != null && b.Contains(a[i])) { res.Add(a[i]); } }\n" + 
      "    return res; }\n\n";
    return res;
  }

  public static String generateIntersectionOpCPP()
  { String res = "  static std::set<_T>* intersection(std::set<_T>* a, std::set<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    for (std::set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)\n" +
      "    { if (UmlRsdsLib<_T>::isIn(*_pos, b))\n" + 
      "      { res->insert(*_pos); }\n" + 
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n" +
      "  static std::set<_T>* intersection(std::set<_T>* a, vector<_T>* b)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    for (std::set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)\n" +
      "    { if (UmlRsdsLib<_T>::isIn(*_pos, b))\n" + 
      "      { res->insert(*_pos); }\n" + 
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n" +
      "  static vector<_T>* intersection(vector<_T>* a, std::set<_T>* b)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    for (int i = 0; i < a->size(); i++)\n" +
      "    { if (UmlRsdsLib<_T>::isIn((*a)[i], b))\n" + 
      "      { res->push_back((*a)[i]); }\n" + 
      "    } \n" +
      "    return res;\n" + 
      "  }\n\n" +  
      "  static vector<_T>* intersection(vector<_T>* a, vector<_T>* b)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    for (int i = 0; i < a->size(); i++)\n" +
      "    { if (UmlRsdsLib<_T>::isIn((*a)[i], b))\n" + 
      "      { res->push_back((*a)[i]); }\n" + 
      "    } \n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  } // use the standard library op for sets. 

  public static String generateIntersectAllOp()  // for s->intersectAll(e)
  { String res = "  public static List intersectAll(List se)\n" +
      "  { List res = new Vector(); \n" +
      "    if (se.size() == 0) { return res; }\n" + 
      "    res.addAll((List) se.get(0));\n" + 
      "    for (int i = 1; i < se.size(); i++)\n" +  
      "    { res.retainAll((List) se.get(i)); }\n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  }

  public static String generateIntersectAllOpJava6()  // for s->intersectAll(e)
  { String res = "  public static HashSet intersectAll(Collection se)\n" +
      "  { HashSet res = new HashSet(); \n" +
      "    if (se.size() == 0) { return res; }\n" + 
      "    res.addAll((Collection) Set.any(se));\n" + 
      "    for (Object _o : se)\n" +  
      "    { res.retainAll((Collection) _o); }\n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  }

  public static String generateIntersectAllOpJava7()  // for s->intersectAll(e)
  { String res = "  public static <T> HashSet<T> intersectAll(Collection<Collection<T>> se)\n" +
      "  { HashSet<T> res = new HashSet<T>(); \n" +
      "    if (se.size() == 0) { return res; }\n" + 
      "    res.addAll((Collection<T>) Ocl.any(se));\n" + 
      "    for (Collection<T> _o : se)\n" +  
      "    { res.retainAll(_o); }\n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  }

  public static String generateIntersectAllOpCSharp()  // for s->intersectAll(e)
  { String res = "  public static ArrayList intersectAll(ArrayList se)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    if (se.Count == 0) { return res; }\n" + 
      "    res.AddRange((ArrayList) se[0]);\n" + 
      "    for (int i = 1; i < se.Count; i++)\n" +  
      "    { res = SystemTypes.intersection(res,(ArrayList) se[i]); }\n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  }

  public static String generateIntersectAllOpCPP()  // for s->intersectAll(e)
  { String res = "  static std::set<_T>* intersectAll(std::set<std::set<_T>*>* se)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    if (se->size() == 0) { return res; }\n" + 
      "    std::set<set<_T>*>::iterator _pos = se->begin();\n" + 
      "    std::set<_T>* frst = *_pos;\n" + 
      "    res->insert(frst->begin(), frst->end());\n" + 
      "    ++_pos; \n" + 
      "    for (; _pos != se->end(); ++_pos)\n" +  
      "    { res = UmlRsdsLib<_T>::intersection(res, *_pos); }\n" +
      "    return res;\n" + 
      "  }\n\n" +
      "  static std::set<_T>* intersectAll(std::set<vector<_T>*>* se)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    if (se->size() == 0) { return res; }\n" + 
      "    std::set<vector<_T>*>::iterator _pos = se->begin();\n" + 
      "    vector<_T>* frst = *_pos;\n" + 
      "    res->insert(frst->begin(), frst->end());\n" + 
      "    ++_pos; \n" + 
      "    for (; _pos != se->end(); ++_pos)\n" +  
      "    { res = UmlRsdsLib<_T>::intersection(res, *_pos); }\n" +
      "    return res;\n" + 
      "  }\n\n" +
      "  static std::set<_T>* intersectAll(vector<std::set<_T>*>* se)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    if (se->size() == 0) { return res; }\n" + 
      "    std::set<_T>* frst = (*se)[0];\n" + 
      "    res->insert(frst->begin(), frst->end());\n" + 
      "    for (int i = 1; i < se->size(); ++i)\n" +  
      "    { res = UmlRsdsLib<_T>::intersection(res, (*se)[i]); }\n" +
      "    return res;\n" + 
      "  }\n\n" + 
      "  static vector<_T>* intersectAll(vector<vector<_T>*>* se)\n" +
      "  { vector<_T>* res = new vector<_T>(); \n" +
      "    if (se->size() == 0) { return res; }\n" + 
      "    vector<_T>* frst = (*se)[0];\n" + 
      "    res->insert(res->end(), frst->begin(), frst->end());\n" + 
      "    for (int i = 1; i < se->size(); ++i)\n" +  
      "    { res = UmlRsdsLib<_T>::intersection(res, (*se)[i]); }\n" +
      "    return res;\n" + 
      "  }\n\n";
    return res;
  }   // and other possibilities

  public static String generateUnionAllOp()  // for s->unionAll(e)
  { String res = "  public static List unionAll(List se)\n" +
      "  { List res = new Vector(); \n" +
      "    for (int i = 0; i < se.size(); i++)\n" +  
      "    { List b = (List) se.get(i); \n" + 
      "      for (int j = 0; j < b.size(); j++)\n" +
      "      { Object y = b.get(j); \n" + 
      "        if (y == null || res.contains(y)) { } else { res.add(y); } \n" + 
      "      }\n" +
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n";

    res = res + "  public static HashMap unionAllMap(List se)\n" +
      "  { HashMap res = new HashMap(); \n" +
      "    for (int i = 0; i < se.size(); i++)\n" +  
      "    { Map b = (Map) se.get(i); \n" + 
      "      res.putAll(b);\n" + 
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n";
    
    return res;
  }  // and eliminate duplicates

  public static String generateUnionAllOpJava6()  // for s->unionAll(e)
  { String res = "  public static HashSet unionAll(Collection se)\n" +
      "  { HashSet res = new HashSet(); \n" +
      "    for (Object _o : se)\n" +  
      "    { Collection b = (Collection) _o; \n" + 
      "      res.addAll(b);\n" +
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n";

    res = res + "  public static HashMap unionAllMap(Collection se)\n" +
      "  { HashMap res = new HashMap(); \n" +
      "    for (Object _o : se)\n" +  
      "    { res.putAll((Map) _o); }\n" +
      "    return res;\n" + 
      "  }\n\n";

    return res;
  }  // and eliminate duplicates

  public static String generateUnionAllOpJava7()  // for s->unionAll(e)
  { String res = "  public static HashSet unionAll(Collection se)\n" +
      "  { HashSet res = new HashSet(); \n" +
      "    for (Object _o : se)\n" +  
      "    { res.addAll((Collection) _o); }\n" +
      "    return res;\n" + 
      "  }\n\n";

    res = res + "  public static HashMap unionAllMap(Collection se)\n" +
      "  { HashMap res = new HashMap(); \n" +
      "    for (Object _o : se)\n" +  
      "    { res.putAll((Map) _o); }\n" +
      "    return res;\n" + 
      "  }\n\n";

    return res;
  }  // Should be typed. Need to cast result to correct type. 

  public static String generateUnionAllOpCSharp()  // for s->unionAll(e)
  { String res = "  public static ArrayList unionAll(ArrayList se)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < se.Count; i++)\n" +  
      "    { ArrayList b = (ArrayList) se[i]; \n" + 
      "      for (int j = 0; j < b.Count; j++)\n" +
      "      { if (b[j] == null || res.Contains(b[j])) { } else { res.Add(b[j]); } }\n" +
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n";

    res = res + 
      "  public static Hashtable unionAllMap(ArrayList se)\n" +
      "  { Hashtable res = new Hashtable(); \n" +
      "    for (int i = 0; i < se.Count; i++)\n" +
      "    { Hashtable b = (Hashtable) se[i]; \n" +
      "      res = SystemTypes.unionMap(res,b); \n" +
      "    }\n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + 
      "  public static Hashtable intersectionAllMap(ArrayList se)\n" +
      "  { Hashtable res = (Hashtable) se[0]; \n" +
      "    for (int i = 1; i < se.Count; i++)\n" +
      "    { Hashtable b = (Hashtable) se[i]; \n" +
      "      res = SystemTypes.intersectionMap(res,b); \n" +
      "    }\n" +
      "    return res;\n" +
      "  }\n\n"; 

    return res;
  }  // and eliminate duplicates

  public static String generateUnionAllOpCPP()  // for s->unionAll(e)
  { String res = "  static std::set<_T>* unionAll(std::set<set<_T>*>* se)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    if (se->size() == 0) { return res; }\n" + 
      "    std::set<set<_T>*>::iterator _pos;\n" + 
      "    for (_pos = se->begin(); _pos != se->end(); ++_pos)\n" +  
      "    { res = UmlRsdsLib<_T>::unionSet(res, *_pos); }\n" +
      "    return res;\n" + 
      "  }\n\n";

    res = res + "  static std::set<_T>* unionAll(std::set<vector<_T>*>* se)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    if (se->size() == 0) { return res; }\n" + 
      "    std::set<vector<_T>*>::iterator _pos;\n" + 
      "    for (_pos = se->begin(); _pos != se->end(); ++_pos)\n" +  
      "    { res = UmlRsdsLib<_T>::unionSet(res, *_pos); }\n" +
      "    return res;\n" + 
      "  }\n\n";

    res = res + "  static std::set<_T>* unionAll(vector<set<_T>*>* se)\n" +
      "  { std::set<_T>* res = new std::set<_T>(); \n" +
      "    if (se->size() == 0) { return res; }\n" + 
      "    for (int i = 0; i < se->size(); ++i)\n" +  
      "    { res = UmlRsdsLib<_T>::unionSet(res, (*se)[i]); }\n" +
      "    return res;\n" + 
      "  }\n\n";

    res = res + 
      "  static std::map<string,_T>* unionAllMap(vector<map<string,_T>*>* se)\n" +
      "  { std::map<string,_T>* res = new std::map<string,_T>();\n" +
      "    if (se->size() == 0) { return res; }\n" +
      "    for (int i = 0; i < se->size(); ++i)\n" +
      "    { res = UmlRsdsLib<_T>::unionMap(res, (*se)[i]); }\n" +
      "    return res;\n" +
      "  }\n\n";

    return res;
  }  // vector<vector> is concatenateAll

  public static String generateReverseOp()
  { String res = "  public static List reverse(List a)\n" + 
                 "  { List res = new Vector(); \n" + 
                 "    for (int i = a.size() - 1; i >= 0; i--)\n" + 
                 "    { res.add(a.get(i)); } \n" + 
                 "    return res;\n" + 
                 "  }\n\n" + 
                 "  public static String reverse(String a)\n" + 
                 "  { String res = \"\"; \n" + 
                 "    for (int i = a.length() - 1; i >= 0; i--)\n" + 
                 "    { res = res + a.charAt(i); } \n" + 
                 "    return res;\n" + 
                 "  }\n\n"; 
    return res; 
  }  

  public static String generateReverseOpJava6()
  { String res = "  public static ArrayList reverse(Collection a)\n" + 
                 "  { ArrayList res = new ArrayList(); \n" + 
                 "    res.addAll(a); \n" + 
                 "    Collections.reverse(res); \n" + 
                 "    return res;\n" + 
                 "  }\n\n" + 
                 "  public static String reverse(String a)\n" + 
                 "  { String res = \"\"; \n" + 
                 "    for (int i = a.length() - 1; i >= 0; i--)\n" + 
                 "    { res = res + a.charAt(i); } \n" + 
                 "    return res;\n" + 
                 "  }\n\n"; 
    return res; 
  }  

  public static String generateReverseOpJava7()
  { String res = "  public static ArrayList reverse(Collection a)\n" + 
                 "  { ArrayList res = new ArrayList(); \n" + 
                 "    res.addAll(a); Collections.reverse(res); \n" + 
                 "    return res;\n" + 
                 "  }\n\n" + 
                 "  public static String reverse(String a)\n" + 
                 "  { String res = \"\"; \n" + 
                 "    for (int i = a.length() - 1; i >= 0; i--)\n" + 
                 "    { res = res + a.charAt(i); } \n" + 
                 "    return res;\n" + 
                 "  }\n\n"; 
    return res; 
  }  // cast to correct type. 

  public static String generateReverseOpCSharp()
  { String res = "  public static ArrayList reverse(ArrayList a)\n" + 
                 "  { ArrayList res = new ArrayList(); \n" + 
                 "    res.AddRange(a); \n" + 
                 "    res.Reverse(); \n" + 
                 "    return res;\n" + 
                 "  }\n\n" + 
                 "  public static string reverse(string a)\n" + 
                 "  { string res = \"\"; \n" + 
                 "    for (int i = a.Length - 1; i >= 0; i--)\n" + 
                 "    { res = res + a[i]; } \n" + 
                 "    return res;\n" + 
                 "  }\n\n"; 
    return res; 
  }  

  public static String generateReverseOpCPP()
  { String res = "  static vector<_T>* reverse(vector<_T>* a)\n" + 
                 "  { vector<_T>* res = new vector<_T>(); \n" + 
                 "    res->insert(res->end(), a->begin(), a->end()); \n" + 
                 "    std::reverse(res->begin(), res->end()); \n" + 
                 "    return res;\n" + 
                 "  }\n\n" + 
                 "  static string reverse(string a)\n" + 
                 "  { string res(\"\"); \n" + 
                 "    for (int i = a.length() - 1; i >= 0; i--)\n" + 
                 "    { res = res + a[i]; } \n" + 
                 "    return res;\n" + 
                 "  }\n\n"; 
    return res; 
  }  

  public static String generateFrontOp()
  { String res = "  public static List front(List a)\n" + 
                 "  { List res = new Vector(); \n" + 
                 "    for (int i = 0; i < a.size() - 1; i++)\n" + 
                 "    { res.add(a.get(i)); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateFrontOpJava6()
  { String res = "  public static ArrayList front(ArrayList a)\n" + 
                 "  { ArrayList res = new ArrayList(); \n" + 
                 "    for (int i = 0; i < a.size() - 1; i++)\n" + 
                 "    { res.add(a.get(i)); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateFrontOpJava7()
  { String res = "  public static <T> ArrayList<T> front(ArrayList<T> a)\n" + 
                 "  { ArrayList<T> res = new ArrayList<T>(); \n" + 
                 "    for (int i = 0; i < a.size() - 1; i++)\n" + 
                 "    { res.add(a.get(i)); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateFrontOpCSharp()
  { String res = "  public static ArrayList front(ArrayList a)\n" + 
                 "  { ArrayList res = new ArrayList(); \n" + 
                 "    for (int i = 0; i < a.Count - 1; i++)\n" + 
                 "    { res.Add(a[i]); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateFrontOpCPP()
  { String res = "  static vector<_T>* front(vector<_T>* a)\n" + 
                 "  { vector<_T>* res = new vector<_T>(); \n" + 
                 "    if (a->size() == 0) { return res; } \n" + 
                 "    vector<_T>::iterator _pos = a->end(); \n" +
                 "    _pos--; \n" + 
                 "    res->insert(res->end(), a->begin(), _pos); \n" +  
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateTailOp()
  { String res = "  public static List tail(List a)\n" + 
                 "  { List res = new Vector(); \n" + 
                 "    for (int i = 1; i < a.size(); i++)\n" + 
                 "    { res.add(a.get(i)); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateTailOpJava6()
  { String res = "  public static ArrayList tail(ArrayList a)\n" + 
                 "  { ArrayList res = new ArrayList(); \n" + 
                 "    for (int i = 1; i < a.size(); i++)\n" + 
                 "    { res.add(a.get(i)); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateTailOpJava7()
  { String res = "  public static <T> ArrayList<T> tail(ArrayList<T> a)\n" + 
                 "  { ArrayList<T> res = new ArrayList<T>(); \n" + 
                 "    for (int i = 1; i < a.size(); i++)\n" + 
                 "    { res.add(a.get(i)); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  // more efficient just to remove the first element

  public static String generateTailOpCSharp()
  { String res = "  public static ArrayList tail(ArrayList a)\n" + 
                 "  { ArrayList res = new ArrayList(); \n" + 
                 "    for (int i = 1; i < a.Count; i++)\n" + 
                 "    { res.Add(a[i]); } \n" + 
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateTailOpCPP()
  { String res = "  static vector<_T>* tail(vector<_T>* a)\n" + 
                 "  { vector<_T>* res = new vector<_T>(); \n" + 
                 "    if (a->size() == 0) { return res; } \n" + 
                 "    vector<_T>::iterator _pos = a->begin(); \n" +
                 "    _pos++; \n" + 
                 "    res->insert(res->end(), _pos, a->end()); \n" +  
                 "    return res; }\n"; 
    return res; 
  }  

  public static String generateSumOps()
  { String res = "  public static int sumint(List a)\n" +
      "  { int sum = 0; \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Integer x = (Integer) a.get(i); \n" +
      "      if (x != null) { sum += x.intValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static double sumdouble(List a)\n" +
      "  { double sum = 0.0; \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Double x = (Double) a.get(i); \n" +
      "      if (x != null) { sum += x.doubleValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static long sumlong(List a)\n" +
      "  { long sum = 0; \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Long x = (Long) a.get(i); \n" +
      "      if (x != null) { sum += x.longValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static String sumString(List a)\n" +
      "  { String sum = \"\"; \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Object x = a.get(i); \n" +
      "      sum = sum + x; }\n" + 
      "    return sum;  }\n\n";
    res = res + "  public static int sumint(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return sumint(range);  }\n\n"; 
    res = res + "  public static double sumdouble(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return sumdouble(range);  }\n\n";
    res = res + "  public static long sumlong(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return sumlong(range);  }\n\n"; 
    res = res + "  public static String sumString(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return sumString(range);  }\n\n"; 
    return res;  
  } 


  public static String generatePrdOps()
  { String res = "  public static int prdint(List a)\n" +
      "  { int res = 1; \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Integer x = (Integer) a.get(i); \n" +
      "      if (x != null) { res *= x.intValue(); }\n" + 
      "    } \n" + 
      "    return res; }\n\n";
    res = res + "  public static double prddouble(List a)\n" +
      "  { double res = 1; \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Double x = (Double) a.get(i); \n" +
      "      if (x != null) { res *= x.doubleValue(); }\n" + 
      "    } \n" + 
      "    return res; }\n\n";
    res = res + "  public static long prdlong(List a)\n" +
      "  { long res = 1; \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Long x = (Long) a.get(i); \n" +
      "      if (x != null) { res *= x.longValue(); }\n" +
      "    }\n" +  
      "    return res;  }\n\n";
    res = res + "  public static int prdint(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return prdint(range);  }\n\n"; 
    res = res + "  public static double prddouble(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return prddouble(range);  }\n\n";
    res = res + "  public static long prdlong(Map m)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return prdlong(range);  }\n\n"; 
    return res;
  }


  public static String generateSumOpsJava6()
  { String res = "  public static int sumint(Collection a)\n" +
      "  { int sum = 0; \n" +
      "    for (Object _o : a)\n" +
      "    { Integer x = (Integer) _o; \n" +
      "      if (x != null) { sum += x.intValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static double sumdouble(Collection a)\n" +
      "  { double sum = 0.0; \n" +
      "    for (Object _o : a)\n" +
      "    { Double x = (Double) _o; \n" +
      "      if (x != null) { sum += x.doubleValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static long sumlong(Collection a)\n" +
      "  { long sum = 0; \n" +
      "    for (Object _o : a)\n" +
      "    { Long x = (Long) _o; \n" +
      "      if (x != null) { sum += x.longValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static String sumString(Collection a)\n" +
      "  { String sum = \"\"; \n" +
      "    for (Object x : a)\n" +
      "    { sum = sum + x; }\n" + 
      "    return sum;  }\n\n";
    return res;
  } // and for maps

  public static String generatePrdOpsJava6()
  { String res = "  public static int prdint(Collection a)\n" +
      "  { int prd = 1; \n" +
      "    for (Object _o : a)\n" +
      "    { Integer x = (Integer) _o; \n" +
      "      if (x != null) { prd *= x.intValue(); }\n" + 
      "    } \n" + 
      "    return prd; }\n\n";
    res = res + "  public static double prddouble(Collection a)\n" +
      "  { double prd = 1; \n" +
      "    for (Object _o : a)\n" +
      "    { Double x = (Double) _o; \n" +
      "      if (x != null) { prd *= x.doubleValue(); }\n" + 
      "    } \n" + 
      "    return prd; }\n\n";
    res = res + "  public static long prdlong(Collection a)\n" +
      "  { long prd = 1; \n" +
      "    for (Object _o : a)\n" +
      "    { Long x = (Long) _o; \n" +
      "      if (x != null) { prd *= x.longValue(); }\n" + 
      "    } \n" + 
      "    return prd; }\n\n";
    return res;
  } // maps


  public static String generateSumOpsJava7()
  { String res = "  public static int sumint(Collection<Integer> a)\n" +
      "  { int sum = 0; \n" +
      "    for (Integer x : a)\n" +
      "    { if (x != null) { sum += x.intValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static double sumdouble(Collection<Double> a)\n" +
      "  { double sum = 0.0; \n" +
      "    for (Double x : a)\n" +
      "    { if (x != null) { sum += x.doubleValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static long sumlong(Collection<Long> a)\n" +
      "  { long sum = 0; \n" +
      "    for (Long x : a)\n" +
      "    { if (x != null) { sum += x.longValue(); }\n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static String sumString(Collection<String> a)\n" +
      "  { String sum = \"\"; \n" +
      "    for (String x : a)\n" +
      "    { sum = sum + x; }\n" + 
      "    return sum;  }\n\n";
    return res;
  } // maps

  public static String generatePrdOpsJava7()
  { String res = "  public static int prdint(Collection<Integer> a)\n" +
      "  { int prd = 1; \n" +
      "    for (Integer x : a)\n" +
      "    { if (x != null) { prd *= x.intValue(); }\n" + 
      "    } \n" + 
      "    return prd; }\n\n";
    res = res + "  public static double prddouble(Collection<Double> a)\n" +
      "  { double prd = 1; \n" +
      "    for (Double x : a)\n" +
      "    { if (x != null) { prd *= x.doubleValue(); }\n" + 
      "    } \n" + 
      "    return prd; }\n\n";
    res = res + "  public static long prdlong(Collection<Long> a)\n" +
      "  { long prd = 1; \n" +
      "    for (Long x : a)\n" +
      "    { if (x != null) { prd *= x.longValue(); }\n" + 
      "    } \n" + 
      "    return prd; }\n\n";
    return res;
  } // maps


  public static String generateSumOpsCSharp()
  { String res = "  public static int sumint(ArrayList a)\n" +
      "  { int sum = 0; \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { int x = (int) a[i]; \n" +
      "      sum += x; \n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static double sumdouble(ArrayList a)\n" +
      "  { double sum = 0.0; \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { double x = double.Parse(\"\" + a[i]); \n" +
      "      sum += x; \n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static long sumlong(ArrayList a)\n" +
      "  { long sum = 0; \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { long x = (long) a[i]; \n" +
      "      sum += x; \n" + 
      "    } \n" + 
      "    return sum; }\n\n";
    res = res + "  public static string sumString(ArrayList a)\n" +
      "  { string sum = \"\"; \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { object x = a[i]; \n" +
      "      sum = sum + x; }\n" + 
      "    return sum;  }\n\n";
    return res;
  } // maps

  public static String generatePrdOpsCSharp()
  { String res = "  public static int prdint(ArrayList a)\n" +
      "  { int _prd = 1; \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { int x = (int) a[i]; \n" +
      "      _prd *= x; \n" + 
      "    } \n" + 
      "    return _prd; }\n\n";
    res = res + "  public static double prddouble(ArrayList a)\n" +
      "  { double _prd = 1; \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { double x = (double) a[i]; \n" +
      "      _prd *= x; \n" + 
      "    } \n" + 
      "    return _prd; }\n\n";
    res = res + "  public static long prdlong(ArrayList a)\n" +
      "  { long _prd = 1; \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { long x = (long) a[i]; \n" +
      "      _prd *= x;\n" + 
      "    } \n" + 
      "    return _prd; }\n\n";
    return res;
  } // maps

  public static String generateSumOpsCPP()
  { String res = "  static string sumString(vector<string>* a)\n" +
      "  { string _sum(\"\"); \n" +
      "    for (int i = 0; i < a->size(); i++)\n" +
      "    { _sum.append( (*a)[i] ); }\n" + 
      "    return _sum; }\n\n";
    res = res + "  static string sumString(std::set<string>* a)\n" +
      "  { string _sum(\"\"); \n" +
      "    std::set<string>::iterator _pos;\n" + 
      "    for (_pos = a->begin(); _pos != a->end(); ++_pos)\n" +
      "    { _sum.append( *_pos ); }\n" +  
      "    return _sum; }\n\n"; 
    res = res + "  static _T sum(vector<_T>* a)\n" +
      "  { _T _sum(0); \n" +
      "    for (int i = 0; i < a->size(); i++)\n" +
      "    { _sum += (*a)[i]; }\n" + 
      "    return _sum; }\n\n";
    res = res + "  static _T sum(std::set<_T>* a)\n" +
      "  { _T _sum(0); \n" +
      "    std::set<_T>::iterator _pos;\n" + 
      "    for (_pos = a->begin(); _pos != a->end(); ++_pos)\n" +
      "    { _sum += *_pos; }\n" +  
      "    return _sum; }\n\n";
    return res;
  } // maps

  public static String generatePrdOpsCPP()
  { String res = "  static _T prd(vector<_T>* a)\n" +
      "  { _T _prd(1); \n" +
      "    for (int i = 0; i < a->size(); i++)\n" +
      "    { _prd *= (*a)[i]; }\n" + 
      "    return _prd; }\n\n";
    res = res + "  static _T prd(std::set<_T>* a)\n" +
      "  { _T _prd(1); \n" +
      "    std::set<_T>::iterator _pos;\n" + 
      "    for (_pos = a->begin(); _pos != a->end(); ++_pos)\n" +
      "    { _prd *= *_pos; }\n" +  
      "    return _prd; }\n\n";
    return res;
  } // maps

  public static String generateClosureOps(Vector assocs) 
  { String res = ""; 
    for (int i = 0; i < assocs.size(); i++)
    { Association ast = (Association) assocs.get(i); 
      String closureops = ast.generateClosureOperation(); 
      res = res + closureops; 
    } 
    return res; 
  }

  public static String generateClosureOpsJava6(Vector assocs) 
  { String res = ""; 
    for (int i = 0; i < assocs.size(); i++)
    { Association ast = (Association) assocs.get(i); 
      String closureops = ast.generateClosureOperationJava6(); 
      res = res + closureops; 
    } 
    return res; 
  }

  public static String generateClosureOpsJava7(Vector assocs) 
  { String res = ""; 
    for (int i = 0; i < assocs.size(); i++)
    { Association ast = (Association) assocs.get(i); 
      String closureops = ast.generateClosureOperationJava7(); 
      res = res + closureops; 
    } 
    return res; 
  }

  public static String generateClosureOpsCSharp(Vector assocs) 
  { String res = ""; 
    for (int i = 0; i < assocs.size(); i++)
    { Association ast = (Association) assocs.get(i); 
      String closureops = ast.generateClosureOperationCSharp(); 
      res = res + closureops; 
    } 
    return res; 
  }

  public static String generateClosureOpsCPP(Vector assocs) 
  { String res = ""; 
    for (int i = 0; i < assocs.size(); i++)
    { Association ast = (Association) assocs.get(i); 
      String closureops = ast.generateClosureOperationCPP(); 
      res = res + closureops; 
    } 
    return res; 
  }


  public static String generateAsSetOp()
  { String res = "  public static List asSet(List a)\n" +
      "  { Vector res = new Vector(); \n" +
      "    for (int i = 0; i < a.size(); i++)\n" +
      "    { Object obj = a.get(i);\n" +
      "      if (res.contains(obj)) { } \n" + 
      "      else { res.add(obj); }\n" + 
      "    } \n" + 
      "    return res; \n" + 
      "  }\n\n";
    res = res + "  public static List asOrderedSet(List a)\n" + 
      "  { return asSet(a); }\n\n";  
    res = res + "  public static List asSet(Map m)\n" + 
      "  { Vector range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return asSet(range);\n" + 
      "  }\n\n"; 

    res = res + 
      "  public static Vector mapAsSequence(Map m)\n" +
      "  { Vector range = new Vector();\n" +
      "    java.util.Set ss = m.entrySet();\n" + 
      "    for (Object x : ss)\n" +
      "    { Map.Entry ee = (Map.Entry) x;\n" + 
      "      HashMap mx = new HashMap(); \n" +
      "      mx.put(ee.getKey(), ee.getValue());\n" + 
      "      range.add(mx); \n" +
      "    } \n" +
      "    return range;\n" + 
      "  }\n\n"; 

    res = res + 
      "  public static Vector mapAsSet(Map m)\n" +
      "  { Vector range = mapAsSequence(m); \n" +
      "    return (Vector) asSet(range); \n" +
      "  }\n\n"; 
 
    return res;
  }

  public static String generateAsSetOpCSharp()
  { String res = "  public static ArrayList asSet(ArrayList a)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { object obj = a[i];\n" +
      "      if (res.Contains(obj)) { } \n" + 
      "      else { res.Add(obj); }\n" + 
      "    } \n" + 
      "    return res; \n" + 
      "  }\n\n"; 
    res = res + "  public static ArrayList asOrderedSet(ArrayList a)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < a.Count; i++)\n" +
      "    { object obj = a[i];\n" +
      "      if (res.Contains(obj)) { } \n" + 
      "      else { res.Add(obj); }\n" + 
      "    } \n" + 
      "    return res; \n" + 
      "  }\n\n"; 

    res = res + 
      "  public static T[] asReference<T>(ArrayList sq, T[] r)\n" +
      "  {\n" +
      "    for (int i = 0; i < sq.Count && i < r.Length; i++)\n" +
      "    { r[i] = (T) sq[i]; }\n" +
      "    return r;\n" +
      "  }\n\n"; 

    res = res + 
      "  public static ArrayList asSequence<T>(T[] r)\n" +
      "  { ArrayList res = new ArrayList(); \n" +
      "    for (int i = 0; i < r.Length; i++)\n" +
      "    { res.Add(r[i]); }\n" +
      "    return res;\n" +
      "  }\n\n";   

    res = res + 
      "  public static ArrayList asSequence(Hashtable m)\n" +
      "  { ArrayList res = new ArrayList();\n" +
      "    foreach (DictionaryEntry pair in m)\n" +
      "    { object key = pair.Key;\n" +
      "      Hashtable maplet = new Hashtable();\n" +
      "      maplet[key] = pair.Value;\n" + 
      "      res.Add(maplet);\n" +
      "    }\n" +
      "    return res;\n" + 
      "  }\n\n"; 

    return res;
  } // and map as a set

  public static String refOps() 
  { String res = ""; 
    res = "  public static <T> T[] asReference(Vector sq, T[] r)\n" + 
          "  {\n" + 
          "    for (int i = 0; i < sq.size() && i < r.length; i++)\n" + 
          "    { r[i] = (T) sq.get(i); }\n" + 
          "    return r;\n" + 
          "  }\n\n";

    res = res + "  public static int[] resizeTo(int[] arr, int n)\n" +
          "  { int[] tmp = new int[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

    res = res + "  public static long[] resizeTo(long[] arr, int n)\n" +
          "  { long[] tmp = new long[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

    res = res + "  public static double[] resizeTo(double[] arr, int n)\n" +
          "  { double[] tmp = new double[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

     res = res + "  public static boolean[] resizeTo(boolean[] arr, int n)\n" +
          "  { boolean[] tmp = new boolean[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

    res = res + "  public static Object[] resizeTo(Object[] arr, int n)\n" +
          "  { Object[] tmp = new Object[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n";
 
    res = res + 
          "  public static Vector sequenceRange(int[] arr, int n)\n" +
          "  { Vector res = new Vector();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Integer(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static Vector sequenceRange(long[] arr, int n)\n" +
          "  { Vector res = new Vector();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Long(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static Vector sequenceRange(double[] arr, int n)\n" +
          "  { Vector res = new Vector();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Double(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static Vector sequenceRange(boolean[] arr, int n)\n" +
          "  { Vector res = new Vector();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Boolean(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static <T> Vector asSequence(T[] r)\n" +
          "  { Vector res = new Vector(); \n" +
          "    for (int i = 0; i < r.length; i++)\n" +
          "    { res.add(r[i]); }\n" +
          "    return res;\n" +
          "  }\n\n";   

    return res; 
  } 


  public static String refOpsJava6() 
  { String res = ""; 
    res = "  public static <T> T[] asReference(ArrayList sq, T[] r)\n" + 
          "  {\n" + 
          "    for (int i = 0; i < sq.size() && i < r.length; i++)\n" + 
          "    { r[i] = (T) sq.get(i); }\n" + 
          "    return r;\n" + 
          "  }\n\n";

    res = res + "  public static int[] resizeTo(int[] arr, int n)\n" +
          "  { int[] tmp = new int[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

    res = res + "  public static long[] resizeTo(long[] arr, int n)\n" +
          "  { long[] tmp = new long[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

    res = res + "  public static double[] resizeTo(double[] arr, int n)\n" +
          "  { double[] tmp = new double[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

     res = res + "  public static boolean[] resizeTo(boolean[] arr, int n)\n" +
          "  { boolean[] tmp = new boolean[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n"; 

    res = res + "  public static Object[] resizeTo(Object[] arr, int n)\n" +
          "  { Object[] tmp = new Object[n];\n" +
          "    for (int i = 0; i < n; i++)\n" +
          "    { tmp[i] = arr[i]; }\n" +
          "    return tmp;\n" +
          "  }\n\n";
 
    res = res + 
          "  public static ArrayList sequenceRange(int[] arr, int n)\n" +
          "  { ArrayList res = new ArrayList();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Integer(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static ArrayList sequenceRange(long[] arr, int n)\n" +
          "  { ArrayList res = new ArrayList();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Long(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static ArrayList sequenceRange(double[] arr, int n)\n" +
          "  { ArrayList res = new ArrayList();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Double(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static ArrayList sequenceRange(boolean[] arr, int n)\n" +
          "  { ArrayList res = new ArrayList();\n" +
          "    for (int i = 0; i < n && i < arr.length; i++)\n" +
          "    { res.add(new Boolean(arr[i])); }\n" +
          "    return res; \n" +
          "  }\n\n"; 

    res = res + 
          "  public static <T> ArrayList asSequence(T[] r)\n" +
          "  { ArrayList res = new ArrayList(); \n" +
          "    for (int i = 0; i < r.length; i++)\n" +
          "    { res.add(r[i]); }\n" +
          "    return res;\n" +
          "  }\n\n";   

    return res; 
  } 


  public static String generateSortOp()
  { String res = "  public static List sort(final List a)\n" + 
      "  { int i = a.size()-1;\n" + 
      "    return mergeSort(a,0,i);\n" +  
      "  }\n\n"; 
    res = res + 
      "  public static List asSequence(final List a)\n" + 
      "  { return a; }\n\n";
    res = res + 
      "  public static List asBag(final List a)\n" + 
      "  { int i = a.size()-1;\n" + 
      "    return mergeSort(a,0,i);\n" +  
      "  }\n\n";
    res = res +  
      "  static List mergeSort(final List a, int ind1, int ind2)\n" + 
      "  { List res = new Vector();\n" +  
      "    if (ind1 > ind2)\n" +  
      "    { return res; }\n" +  
      "    if (ind1 == ind2)\n" + 
      "    { res.add(a.get(ind1));\n" +  
      "      return res;\n" +  
      "    }\n" +  
      "    int mid = (ind1 + ind2)/2;\n" +  
      "    List a1;\n" +  
      "    List a2;\n" + 
      "    if (mid == ind1)\n" + 
      "    { a1 = new Vector();\n" +  
      "      a1.add(a.get(ind1));\n" +  
      "      a2 = mergeSort(a,mid+1,ind2);\n" +  
      "    }\n" +  
      "    else\n" +  
      "    { a1 = mergeSort(a,ind1,mid-1);\n" +   
      "      a2 = mergeSort(a,mid,ind2);\n" + 
      "    }\n" + 
      "    int i = 0;\n" +  
      "    int j = 0;\n" +  
      "    while (i < a1.size() && j < a2.size())\n" + 
      "    { Comparable e1 = (Comparable) a1.get(i); \n" + 
      "      Comparable e2 = (Comparable) a2.get(j);\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(e1);\n" + 
      "        i++; // get next e1\n" + 
      "      } \n" + 
      "      else \n" + 
      "      { res.add(e2);\n" +  
      "        j++; \n" + 
      "      } \n" + 
      "    } \n" + 
      "    if (i == a1.size())\n" + 
      "    { for (int k = j; k < a2.size(); k++)\n" +  
      "      { res.add(a2.get(k)); } \n" + 
      "    } \n" + 
      "    else \n" + 
      "    { for (int k = i; k < a1.size(); k++) \n" + 
      "      { res.add(a1.get(k)); } \n" + 
      "    } \n" + 
      "    return res;\n" +  
      "  }\n"; 
    return res; 
  }

 /* public static String generateSortOpJava6()  // Not needed -- uses Collections.sort instead. 
  { String res = "  public static ArrayList sort(final ArrayList a)\n" + 
      "  { int i = a.size()-1;\n" + 
      "    return mergeSort(a,0,i);\n" +  
      "  }\n\n" +
      "  public static ArrayList sort(final HashSet a)\n" + 
      "  { ArrayList b = new ArrayList(); \n" + 
      "    b.addAll(a); \n" + 
      "    int i = b.size()-1;\n" + 
      "    return mergeSort(b,0,i);\n" +  
      "  }\n\n" +  
      "  static ArrayList mergeSort(final ArrayList a, int ind1, int ind2)\n" + 
      "  { ArrayList res = new ArrayList();\n" +  
      "    if (ind1 > ind2)\n" +  
      "    { return res; }\n" +  
      "    if (ind1 == ind2)\n" + 
      "    { res.add(a.get(ind1));\n" +  
      "      return res;\n" +  
      "    }\n" +  
      "    int mid = (ind1 + ind2)/2;\n" +  
      "    ArrayList a1;\n" +  
      "    ArrayList a2;\n" + 
      "    if (mid == ind1)\n" + 
      "    { a1 = new ArrayList();\n" +  
      "      a1.add(a.get(ind1));\n" +  
      "      a2 = mergeSort(a,mid+1,ind2);\n" +  
      "    }\n" +  
      "    else\n" +  
      "    { a1 = mergeSort(a,ind1,mid-1);\n" +   
      "      a2 = mergeSort(a,mid,ind2);\n" + 
      "    }\n" + 
      "    int i = 0;\n" +  
      "    int j = 0;\n" +  
      "    while (i < a1.size() && j < a2.size())\n" + 
      "    { Comparable e1 = (Comparable) a1.get(i); \n" + 
      "      Comparable e2 = (Comparable) a2.get(j);\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(e1);\n" + 
      "        i++; // get next e1\n" + 
      "      } \n" + 
      "      else \n" + 
      "      { res.add(e2);\n" +  
      "        j++; \n" + 
      "      } \n" + 
      "    } \n" + 
      "    if (i == a1.size())\n" + 
      "    { for (int k = j; k < a2.size(); k++)\n" +  
      "      { res.add(a2.get(k)); } \n" + 
      "    } \n" + 
      "    else \n" + 
      "    { for (int k = i; k < a1.size(); k++) \n" + 
      "      { res.add(a1.get(k)); } \n" + 
      "    } \n" + 
      "    return res;\n" +  
      "  }\n"; 
    return res; 
  }  */ 


  public static String generateSortOpJava6()
  { String res = "  public static ArrayList sort(Collection a)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.addAll(a);\n" +
      "    Collections.sort(res);\n" + 
      "    return res;\n" +   
      "  }\n\n"; 
    return res; 
  } 

  public static String generateSortOpJava7()
  { String res = 
      "  public static <T> TreeSet<T> sortSet(HashSet<T> col)\n" +
      "  { TreeSet<T> res = new TreeSet<T>();\n" + 
      "    res.addAll(col); \n" +
      "    return res; \n" +
      "  }\n\n" +  
      "  public static ArrayList sort(Collection a)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.addAll(a);\n" +
      "    Collections.sort(res);\n" + 
      "    return res;\n" +   
      "  }\n\n"; 
    return res; 
  } 

  public static String generateSortOpCSharp()
  { String res = "  public static ArrayList sort(ArrayList a)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.AddRange(a);\n" +
      "    res.Sort();\n" + 
      "    return res;\n" +   
      "  }\n\n"; 
    return res; 
  } 

  public static String generateAsBagOpCSharp()
  { String res = "  public static ArrayList asBag(ArrayList a)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.AddRange(a);\n" +
      "    res.Sort();\n" + 
      "    return res;\n" +   
      "  }\n\n"; 
    return res; 
  } 

  public static String generateSortOpCPP()
  { String res = "  static vector<_T>* sort(vector<_T>* a)\n" + 
      "  { vector<_T>* res = new vector<_T>();\n" + 
      "    res->insert(res->end(), a->begin(), a->end());\n" +
      "    std::sort(res->begin(), res->end());\n" + 
      "    return res;\n" +   
      "  }\n\n";
    res = res + "  static vector<_T>* sort(std::set<_T>* a)\n" + 
      "  { vector<_T>* res = new vector<_T>();\n" + 
      "    res->insert(res->end(), a->begin(), a->end());\n" +
      "    std::sort(res->begin(), res->end());\n" + 
      "    return res;\n" +   
      "  }\n\n";  
    return res; 
  } 

  public static String generateSortByOp()
  { String res = "  public static List sortedBy(final List a, List f)\n" + 
      "  { int i = a.size()-1;\n" + 
      "    java.util.Map f_map = new java.util.HashMap();\n" + 
      "    for (int j = 0; j < a.size(); j++)\n" + 
      "    { f_map.put(a.get(j), f.get(j)); }\n" + 
      "    return mergeSort(a,f_map,0,i);\n" +  
      "  }\n\n" +  
      "  static List mergeSort(final List a, java.util.Map f, int ind1, int ind2)\n" + 
      "  { List res = new Vector();\n" +  
      "    if (ind1 > ind2)\n" +  
      "    { return res; }\n" +  
      "    if (ind1 == ind2)\n" + 
      "    { res.add(a.get(ind1));\n" +  
      "      return res;\n" +  
      "    }\n" +  
      "    if (ind2 == ind1 + 1)\n" + 
      "    { Comparable e1 = (Comparable) f.get(a.get(ind1)); \n" + 
      "      Comparable e2 = (Comparable) f.get(a.get(ind2));\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(a.get(ind1)); res.add(a.get(ind2)); return res; }\n" + 
      "      else \n" + 
      "      { res.add(a.get(ind2)); res.add(a.get(ind1)); return res; }\n" + 
      "    }\n" + 
      "    int mid = (ind1 + ind2)/2;\n" +  
      "    List a1;\n" +  
      "    List a2;\n" + 
      "    if (mid == ind1)\n" + 
      "    { a1 = new Vector();\n" +  
      "      a1.add(a.get(ind1));\n" +  
      "      a2 = mergeSort(a,f,mid+1,ind2);\n" +  
      "    }\n" +  
      "    else\n" +  
      "    { a1 = mergeSort(a,f,ind1,mid-1);\n" +   
      "      a2 = mergeSort(a,f,mid,ind2);\n" + 
      "    }\n" + 
      "    int i = 0;\n" +  
      "    int j = 0;\n" +  
      "    while (i < a1.size() && j < a2.size())\n" + 
      "    { Comparable e1 = (Comparable) f.get(a1.get(i)); \n" + 
      "      Comparable e2 = (Comparable) f.get(a2.get(j));\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(a1.get(i));\n" + 
      "        i++; // get next e1\n" + 
      "      } \n" + 
      "      else \n" + 
      "      { res.add(a2.get(j));\n" +  
      "        j++; \n" + 
      "      } \n" + 
      "    } \n" + 
      "    if (i == a1.size())\n" + 
      "    { for (int k = j; k < a2.size(); k++)\n" +  
      "      { res.add(a2.get(k)); } \n" + 
      "    } \n" + 
      "    else \n" + 
      "    { for (int k = i; k < a1.size(); k++) \n" + 
      "      { res.add(a1.get(k)); } \n" + 
      "    } \n" + 
      "    return res;\n" +  
      "  }\n"; 
    return res; 
  }

  public static String generateSortByOpJava6()
  { String res = "  public static ArrayList sortedBy(final ArrayList a, ArrayList f)\n" + 
      "  { int i = a.size()-1;\n" + 
      "    java.util.Map f_map = new java.util.HashMap();\n" + 
      "    for (int j = 0; j < a.size(); j++)\n" + 
      "    { f_map.put(a.get(j), f.get(j)); }\n" + 
      "    return mergeSort(a,f_map,0,i);\n" +  
      "  }\n\n" +  
      "  static ArrayList mergeSort(final ArrayList a, java.util.Map f, int ind1, int ind2)\n" + 
      "  { ArrayList res = new ArrayList();\n" +  
      "    if (ind1 > ind2)\n" +  
      "    { return res; }\n" +  
      "    if (ind1 == ind2)\n" + 
      "    { res.add(a.get(ind1));\n" +  
      "      return res;\n" +  
      "    }\n" +  
      "    if (ind2 == ind1 + 1)\n" + 
      "    { Comparable e1 = (Comparable) f.get(a.get(ind1)); \n" + 
      "      Comparable e2 = (Comparable) f.get(a.get(ind2));\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(a.get(ind1)); res.add(a.get(ind2)); return res; }\n" + 
      "      else \n" + 
      "      { res.add(a.get(ind2)); res.add(a.get(ind1)); return res; }\n" + 
      "    }\n" + 
      "    int mid = (ind1 + ind2)/2;\n" +  
      "    ArrayList a1;\n" +  
      "    ArrayList a2;\n" + 
      "    if (mid == ind1)\n" + 
      "    { a1 = new ArrayList();\n" +  
      "      a1.add(a.get(ind1));\n" +  
      "      a2 = mergeSort(a,f,mid+1,ind2);\n" +  
      "    }\n" +  
      "    else\n" +  
      "    { a1 = mergeSort(a,f,ind1,mid-1);\n" +   
      "      a2 = mergeSort(a,f,mid,ind2);\n" + 
      "    }\n" + 
      "    int i = 0;\n" +  
      "    int j = 0;\n" +  
      "    while (i < a1.size() && j < a2.size())\n" + 
      "    { Comparable e1 = (Comparable) f.get(a1.get(i)); \n" + 
      "      Comparable e2 = (Comparable) f.get(a2.get(j));\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(a1.get(i));\n" + 
      "        i++; // get next e1\n" + 
      "      } \n" + 
      "      else \n" + 
      "      { res.add(a2.get(j));\n" +  
      "        j++; \n" + 
      "      } \n" + 
      "    } \n" + 
      "    if (i == a1.size())\n" + 
      "    { for (int k = j; k < a2.size(); k++)\n" +  
      "      { res.add(a2.get(k)); } \n" + 
      "    } \n" + 
      "    else \n" + 
      "    { for (int k = i; k < a1.size(); k++) \n" + 
      "      { res.add(a1.get(k)); } \n" + 
      "    } \n" + 
      "    return res;\n" +  
      "  }\n"; 
    return res; 
  }

  public static String generateSortByOpJava7()
  { String res = "  public static <T> ArrayList<T> sortedBy(final ArrayList<T> a, ArrayList<?> f)\n" + 
      "  { int i = a.size()-1;\n" + 
      "    if (i < 0) { return a; } \n\n" +
      "    if (f.get(i) instanceof Comparable)\n" +
      "    { java.util.Map<T,Comparable> f_map = new java.util.HashMap<T,Comparable>();\n" +
      "      for (int j = 0; j < a.size(); j++)\n" +
      "      { f_map.put(a.get(j), (Comparable) f.get(j)); }\n" +
      "      return mergeSort(a,f_map,0,i);\n" +
      "    } \n\n" +
      "    if (f.get(i) instanceof List)\n" + 
      "    { java.util.Map<T,List> list_map = new java.util.HashMap<T,List>();\n" +
      "      for (int j = 0; j < a.size(); j++)\n" +
      "      { list_map.put(a.get(j), (List) f.get(j)); }\n" +
      "      return mergeSortSequence(a, list_map, 0, i);\n" +
      "    } \n\n" +
      "    return a;\n" + 
      "  }\n\n" +  
      "  static <T> ArrayList<T> mergeSort(final ArrayList<T> a, java.util.Map<T,Comparable> f, int ind1, int ind2)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" +  
      "    if (ind1 > ind2)\n" +  
      "    { return res; }\n" +  
      "    if (ind1 == ind2)\n" + 
      "    { res.add(a.get(ind1));\n" +  
      "      return res;\n" +  
      "    }\n" +  
      "    if (ind2 == ind1 + 1)\n" + 
      "    { Comparable e1 = (Comparable) f.get(a.get(ind1)); \n" + 
      "      Comparable e2 = (Comparable) f.get(a.get(ind2));\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(a.get(ind1)); res.add(a.get(ind2)); return res; }\n" + 
      "      else \n" + 
      "      { res.add(a.get(ind2)); res.add(a.get(ind1)); return res; }\n" + 
      "    }\n" + 
      "    int mid = (ind1 + ind2)/2;\n" +  
      "    ArrayList<T> a1;\n" +  
      "    ArrayList<T> a2;\n" + 
      "    if (mid == ind1)\n" + 
      "    { a1 = new ArrayList<T>();\n" +  
      "      a1.add(a.get(ind1));\n" +  
      "      a2 = mergeSort(a,f,mid+1,ind2);\n" +  
      "    }\n" +  
      "    else\n" +  
      "    { a1 = mergeSort(a,f,ind1,mid-1);\n" +   
      "      a2 = mergeSort(a,f,mid,ind2);\n" + 
      "    }\n" + 
      "    int i = 0;\n" +  
      "    int j = 0;\n" +  
      "    while (i < a1.size() && j < a2.size())\n" + 
      "    { Comparable e1 = (Comparable) f.get(a1.get(i)); \n" + 
      "      Comparable e2 = (Comparable) f.get(a2.get(j));\n" +  
      "      if (e1.compareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.add(a1.get(i));\n" + 
      "        i++; // get next e1\n" + 
      "      } \n" + 
      "      else \n" + 
      "      { res.add(a2.get(j));\n" +  
      "        j++; \n" + 
      "      } \n" + 
      "    } \n" + 
      "    if (i == a1.size())\n" + 
      "    { for (int k = j; k < a2.size(); k++)\n" +  
      "      { res.add(a2.get(k)); } \n" + 
      "    } \n" + 
      "    else \n" + 
      "    { for (int k = i; k < a1.size(); k++) \n" + 
      "      { res.add(a1.get(k)); } \n" + 
      "    } \n" + 
      "    return res;\n" +  
      "  }\n\n"; 

    res = res + 
      "  static <T> ArrayList<T> mergeSortSequence(final ArrayList<T> a, java.util.Map<T,List> f, int ind1, int ind2)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" +  
      "    if (ind1 > ind2)\n" +  
      "    { return res; }\n" +  
      "    if (ind1 == ind2)\n" + 
      "    { res.add(a.get(ind1));\n" +  
      "      return res;\n" +  
      "    }\n" +  
      "    if (ind2 == ind1 + 1)\n" + 
      "    { List e1 = (List) f.get(a.get(ind1)); \n" + 
      "      List e2 = (List) f.get(a.get(ind2));\n" +  
      "      if (Ocl.sequenceCompare(e1,e2) < 0) // e1 < e2\n" + 
      "      { res.add(a.get(ind1)); res.add(a.get(ind2)); return res; }\n" + 
      "      else \n" + 
      "      { res.add(a.get(ind2)); res.add(a.get(ind1)); return res; }\n" + 
      "    }\n" + 
      "    int mid = (ind1 + ind2)/2;\n" +  
      "    ArrayList<T> a1;\n" +  
      "    ArrayList<T> a2;\n" + 
      "    if (mid == ind1)\n" + 
      "    { a1 = new ArrayList<T>();\n" +  
      "      a1.add(a.get(ind1));\n" +  
      "      a2 = mergeSortSequence(a,f,mid+1,ind2);\n" +  
      "    }\n" +  
      "    else\n" +  
      "    { a1 = mergeSortSequence(a,f,ind1,mid-1);\n" +   
      "      a2 = mergeSortSequence(a,f,mid,ind2);\n" + 
      "    }\n" + 
      "    int i = 0;\n" +  
      "    int j = 0;\n" +  
      "    while (i < a1.size() && j < a2.size())\n" + 
      "    { List e1 = (List) f.get(a1.get(i)); \n" + 
      "      List e2 = (List) f.get(a2.get(j));\n" +  
      "      if (Ocl.sequenceCompare(e1,e2) < 0) // e1 < e2\n" + 
      "      { res.add(a1.get(i));\n" + 
      "        i++; // get next e1\n" + 
      "      } \n" + 
      "      else \n" + 
      "      { res.add(a2.get(j));\n" +  
      "        j++; \n" + 
      "      } \n" + 
      "    } \n" + 
      "    if (i == a1.size())\n" + 
      "    { for (int k = j; k < a2.size(); k++)\n" +  
      "      { res.add(a2.get(k)); } \n" + 
      "    } \n" + 
      "    else \n" + 
      "    { for (int k = i; k < a1.size(); k++) \n" + 
      "      { res.add(a1.get(k)); } \n" + 
      "    } \n" + 
      "    return res;\n" +  
      "  }\n"; 

    return res; 
  }

  public static String generateSortByOpCSharp()
  { String res = "  public static ArrayList sortedBy(ArrayList a, ArrayList f)\n" + 
      "  { int i = a.Count - 1;\n" + 
      "    Hashtable f_map = new Hashtable();\n" + 
      "    for (int j = 0; j < a.Count; j++)\n" + 
      "    { f_map[a[j]] = f[j]; }\n" + 
      "    return mergeSort(a,f_map,0,i);\n" +  
      "  }\n\n" +  
      "  static ArrayList mergeSort(ArrayList a, Hashtable f, int ind1, int ind2)\n" + 
      "  { ArrayList res = new ArrayList();\n" +  
      "    if (ind1 > ind2)\n" +  
      "    { return res; }\n" +  
      "    if (ind1 == ind2)\n" + 
      "    { res.Add(a[ind1]);\n" +  
      "      return res;\n" +  
      "    }\n" +  
      "    if (ind2 == ind1 + 1)\n" + 
      "    { IComparable e1 = (IComparable) f[a[ind1]]; \n" + 
      "      IComparable e2 = (IComparable) f[a[ind2]];\n" +  
      "      if (e1.CompareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.Add(a[ind1]); res.Add(a[ind2]); return res; }\n" + 
      "      else \n" + 
      "      { res.Add(a[ind2]); res.Add(a[ind1]); return res; }\n" + 
      "    }\n" + 
      "    int mid = (ind1 + ind2)/2;\n" +  
      "    ArrayList a1;\n" +  
      "    ArrayList a2;\n" + 
      "    if (mid == ind1)\n" + 
      "    { a1 = new ArrayList();\n" +  
      "      a1.Add(a[ind1]);\n" +  
      "      a2 = mergeSort(a,f,mid+1,ind2);\n" +  
      "    }\n" +  
      "    else\n" +  
      "    { a1 = mergeSort(a,f,ind1,mid-1);\n" +   
      "      a2 = mergeSort(a,f,mid,ind2);\n" + 
      "    }\n" + 
      "    int i = 0;\n" +  
      "    int j = 0;\n" +  
      "    while (i < a1.Count && j < a2.Count)\n" + 
      "    { IComparable e1 = (IComparable) f[a1[i]]; \n" + 
      "      IComparable e2 = (IComparable) f[a2[j]];\n" +  
      "      if (e1.CompareTo(e2) < 0) // e1 < e2\n" + 
      "      { res.Add(a1[i]);\n" + 
      "        i++; // get next e1\n" + 
      "      } \n" + 
      "      else \n" + 
      "      { res.Add(a2[j]);\n" +  
      "        j++; \n" + 
      "      } \n" + 
      "    } \n" + 
      "    if (i == a1.Count)\n" + 
      "    { for (int k = j; k < a2.Count; k++)\n" +  
      "      { res.Add(a2[k]); } \n" + 
      "    } \n" + 
      "    else \n" + 
      "    { for (int k = i; k < a1.Count; k++) \n" + 
      "      { res.Add(a1[k]); } \n" + 
      "    } \n" + 
      "    return res;\n" +  
      "  }\n"; 
    return res; 
  }


  public static String symmetricDifferenceOp()
  { String res = 
    "  public static List symmetricDifference(List a, List b)\n" + 
    "  { List res = new Vector();\n" + 
    "    for (int i = 0; i < a.size(); i++)\n" +  
    "    { Object _a = a.get(i);\n" + 
    "      if (b.contains(_a) || res.contains(_a)) { }\n" +  
    "      else { res.add(_a); }\n" + 
    "    }\n" + 
    "    for (int j = 0; j < b.size(); j++)\n" +
    "    { Object _b = b.get(j);\n" + 
    "      if (a.contains(_b) || res.contains(_b)) { }\n" + 
    "      else { res.add(_b); }\n" + 
    "    }\n" + 
    "    return res;\n" + 
    "  }\n\n"; 
    return res; 
  }   

  public static String symmetricDifferenceOpJava6()
  { String res = 
    "  public static HashSet symmetricDifference(Collection a, Collection b)\n" + 
    "  { HashSet res = new HashSet();\n" + 
    "    for (Object _a : a)\n" +  
    "    { if (b.contains(_a)) { }\n" +  
    "      else { res.add(_a); }\n" + 
    "    }\n" + 
    "    for (Object _b : b)\n" +
    "    { if (a.contains(_b)) { }\n" + 
    "      else { res.add(_b); }\n" + 
    "    }\n" + 
    "    return res;\n" + 
    "  }\n\n"; 
    return res; 
  }   

  public static String symmetricDifferenceOpJava7()
  { String res = 
    "  public static <T> Set<T> symmetricDifference(Collection<T> a, Collection<T> b)\n" + 
    "  { Set<T> res = new HashSet<T>();\n" + 
    "    for (T _a : a)\n" +  
    "    { if (b.contains(_a)) { }\n" +  
    "      else { res.add(_a); }\n" + 
    "    }\n" + 
    "    for (T _b : b)\n" +
    "    { if (a.contains(_b)) { }\n" + 
    "      else { res.add(_b); }\n" + 
    "    }\n" + 
    "    return res;\n" + 
    "  }\n\n"; 
    return res; 
  }   

  public static String symmetricDifferenceOpCSharp()
  { String res = 
    "  public static ArrayList symmetricDifference(ArrayList a, ArrayList b)\n" + 
    "  { ArrayList res = new ArrayList();\n" + 
    "    for (int i = 0; i < a.Count; i++)\n" +  
    "    { object _a = a[i];\n" + 
    "      if (b.Contains(_a) || res.Contains(_a)) { }\n" +  
    "      else { res.Add(_a); }\n" + 
    "    }\n" + 
    "    for (int j = 0; j < b.Count; j++)\n" +
    "    { object _b = b[j];\n" + 
    "      if (a.Contains(_b) || res.Contains(_b)) { }\n" + 
    "      else { res.Add(_b); }\n" + 
    "    }\n" + 
    "    return res;\n" + 
    "  }\n\n"; 
    return res; 
  }   

  public static String maximalElementsOp()
  { String res = 
    "  public static Vector maximalElements(List s, List v)\n" +
    "  { Vector res = new Vector();\n" +
    "    if (s.size() == 0) { return res; }\n" +
    "    Comparable largest = (Comparable) v.get(0);\n" + 
    "    res.add(s.get(0));\n" +
    "    \n" +
    "    for (int i = 1; i < s.size(); i++)\n" +
    "    { Comparable next = (Comparable) v.get(i);\n" +
    "      if (largest.compareTo(next) < 0)\n" +
    "      { largest = next;\n" +
    "        res.clear();\n" +
    "        res.add(s.get(i));\n" +
    "      }\n" +
    "      else if (largest.compareTo(next) == 0)\n" +
    "      { res.add(s.get(i)); }\n" +
    "    }\n" +
    "    return res;\n" + 
    "  }"; 
    return res; 
  } 

  public static String maximalElementsOpJava6()
  { String res = 
    "  public static ArrayList maximalElements(ArrayList s, ArrayList v)\n" +
    "  { ArrayList res = new ArrayList();\n" +
    "    if (s.size() == 0) { return res; }\n" +
    "    Comparable largest = (Comparable) v.get(0);\n" + 
    "    res.add(s.get(0));\n" +
    "    \n" +
    "    for (int i = 1; i < s.size(); i++)\n" +
    "    { Comparable next = (Comparable) v.get(i);\n" +
    "      if (largest.compareTo(next) < 0)\n" +
    "      { largest = next;\n" +
    "        res.clear();\n" +
    "        res.add(s.get(i));\n" +
    "      }\n" +
    "      else if (largest.compareTo(next) == 0)\n" +
    "      { res.add(s.get(i)); }\n" +
    "    }\n" +
    "    return res;\n" + 
    "  }"; 
    return res; 
  } 

  public static String maximalElementsOpJava7()
  { String res = 
    "  public static <T, S extends Comparable> ArrayList<T> maximalElements(List<T> s, List<S> v)\n" +
    "  { ArrayList<T> res = new ArrayList<T>();\n" +
    "    if (s.size() == 0) { return res; }\n" +
    "    Comparable largest = (Comparable) v.get(0);\n" + 
    "    res.add(s.get(0));\n" +
    "    \n" +
    "    for (int i = 1; i < s.size(); i++)\n" +
    "    { Comparable next = (Comparable) v.get(i);\n" +
    "      if (largest.compareTo(next) < 0)\n" +
    "      { largest = next;\n" +
    "        res.clear();\n" +
    "        res.add(s.get(i));\n" +
    "      }\n" +
    "      else if (largest.compareTo(next) == 0)\n" +
    "      { res.add(s.get(i)); }\n" +
    "    }\n" +
    "    return res;\n" + 
    "  }\n\n";

    res = res + 
    "  public static <T, S extends Comparable> TreeSet<T> maximalElements(TreeSet<T> s, List<S> v)\n" +
    "  { ArrayList<T> lst = new ArrayList<T>();\n" +
    "    lst.addAll(s); \n" +
    "    ArrayList<T> lres = maximalElements(lst, v);\n" + 
    "    TreeSet<T> res = new TreeSet<T>(); \n" +
    "    res.addAll(lres); \n" +
    "    return res;\n" +
    "  }\n\n"; 

    return res; 
  } 

  public static String maximalElementsOpCSharp()
  { String res = 
    "  public static ArrayList maximalElements(ArrayList s, ArrayList v)\n" +
    "  { ArrayList res = new ArrayList();\n" +
    "    if (s.Count == 0) { return res; }\n" +
    "    IComparable largest = (IComparable) v[0];\n" + 
    "    res.Add(s[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s.Count; i++)\n" +
    "    { IComparable next = (IComparable) v[i];\n" +
    "      if (largest.CompareTo(next) < 0)\n" +
    "      { largest = next;\n" +
    "        res.Clear();\n" +
    "        res.Add(s[i]);\n" +
    "      }\n" +
    "      else if (largest.CompareTo(next) == 0)\n" +
    "      { res.Add(s[i]); }\n" +
    "    }\n" +
    "    return res;\n" + 
    "  }"; 
    return res; 
  } 

  public static String maximalElementsOpCPP()
  { String res = 
    "  static vector<_T>* maximalElements(vector<_T>* s, vector<int>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    int largest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { int next = (*v)[i];\n" +
    "      if (next > largest)\n" +
    "      { largest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (largest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n\n" + 
    "  static vector<_T>* maximalElements(vector<_T>* s, vector<long>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    long largest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { long next = (*v)[i];\n" +
    "      if (next > largest)\n" +
    "      { largest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (largest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n\n" + 
    "  static vector<_T>* maximalElements(vector<_T>* s, vector<string>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    string largest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { string next = (*v)[i];\n" +
    "      if (next > largest)\n" +
    "      { largest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (largest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n\n" + 
    "  static vector<_T>* maximalElements(vector<_T>* s, vector<double>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    double largest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { double next = (*v)[i];\n" +
    "      if (next > largest)\n" +
    "      { largest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (largest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n"; 
    return res; 
  } 


  public static String minimalElementsOp()
  { String res = 
    "  public static Vector minimalElements(List s, List v)\n" +
    "  { Vector res = new Vector();\n" +
    "    if (s.size() == 0) { return res; }\n" +
    "    Comparable smallest = (Comparable) v.get(0);\n" + 
    "    res.add(s.get(0));\n" +
    "    \n" +
    "    for (int i = 1; i < s.size(); i++)\n" +
    "    { Comparable next = (Comparable) v.get(i);\n" +
    "      if (next.compareTo(smallest) < 0)\n" +
    "      { smallest = next;\n" +
    "        res.clear();\n" +
    "        res.add(s.get(i));\n" +
    "      }\n" +
    "      else if (smallest.compareTo(next) == 0)\n" +
    "      { res.add(s.get(i)); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n"; 
    return res; 
  } 

  public static String minimalElementsOpJava6()
  { String res = 
    "  public static ArrayList minimalElements(ArrayList s, ArrayList v)\n" +
    "  { ArrayList res = new ArrayList();\n" +
    "    if (s.size() == 0) { return res; }\n" +
    "    Comparable smallest = (Comparable) v.get(0);\n" + 
    "    res.add(s.get(0));\n" +
    "    \n" +
    "    for (int i = 1; i < s.size(); i++)\n" +
    "    { Comparable next = (Comparable) v.get(i);\n" +
    "      if (next.compareTo(smallest) < 0)\n" +
    "      { smallest = next;\n" +
    "        res.clear();\n" +
    "        res.add(s.get(i));\n" +
    "      }\n" +
    "      else if (smallest.compareTo(next) == 0)\n" +
    "      { res.add(s.get(i)); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n"; 
    return res; 
  } 

  public static String minimalElementsOpJava7()
  { String res = 
    "  public static <T, S extends Comparable> ArrayList<T> minimalElements(List<T> s, List<S> v)\n" +
    "  { ArrayList<T> res = new ArrayList<T>();\n" +
    "    if (s.size() == 0) { return res; }\n" +
    "    Comparable smallest = (Comparable) v.get(0);\n" + 
    "    res.add(s.get(0));\n" +
    "    \n" +
    "    for (int i = 1; i < s.size(); i++)\n" +
    "    { Comparable next = (Comparable) v.get(i);\n" +
    "      if (next.compareTo(smallest) < 0)\n" +
    "      { smallest = next;\n" +
    "        res.clear();\n" +
    "        res.add(s.get(i));\n" +
    "      }\n" +
    "      else if (smallest.compareTo(next) == 0)\n" +
    "      { res.add(s.get(i)); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n\n";

    res = res + 
    "  public static <T, S extends Comparable> TreeSet<T> minimalElements(TreeSet<T> s, List<S> v)\n" +
    "  { ArrayList<T> lst = new ArrayList<T>();\n" +
    "    lst.addAll(s); \n" +
    "    ArrayList<T> lres = minimalElements(lst, v);\n" + 
    "    TreeSet<T> res = new TreeSet<T>(); \n" +
    "    res.addAll(lres); \n" +
    "    return res;\n" +
    "  }\n\n"; 
 
    return res; 
  } 

  public static String minimalElementsOpCSharp()
  { String res = 
    "  public static ArrayList minimalElements(ArrayList s, ArrayList v)\n" +
    "  { ArrayList res = new ArrayList();\n" +
    "    if (s.Count == 0) { return res; }\n" +
    "    IComparable smallest = (IComparable) v[0];\n" + 
    "    res.Add(s[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s.Count; i++)\n" +
    "    { IComparable next = (IComparable) v[i];\n" +
    "      if (next.CompareTo(smallest) < 0)\n" +
    "      { smallest = next;\n" +
    "        res.Clear();\n" +
    "        res.Add(s[i]);\n" +
    "      }\n" +
    "      else if (smallest.CompareTo(next) == 0)\n" +
    "      { res.Add(s[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n"; 
    return res; 
  } 

  public static String minimalElementsOpCPP()
  { String res = 
    "  static vector<_T>* minimalElements(vector<_T>* s, vector<int>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    int smallest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { int next = (*v)[i];\n" +
    "      if (next < smallest)\n" +
    "      { smallest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (smallest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n\n" + 
    "  static vector<_T>* minimalElements(vector<_T>* s, vector<long>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    long smallest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { long next = (*v)[i];\n" +
    "      if (next < smallest)\n" +
    "      { smallest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (smallest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n\n" + 
    "  static vector<_T>* minimalElements(vector<_T>* s, vector<string>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    string smallest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { string next = (*v)[i];\n" +
    "      if (next < smallest)\n" +
    "      { smallest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (smallest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n\n" + 
    "  static vector<_T>* minimalElements(vector<_T>* s, vector<double>* v)\n" +
    "  { vector<_T>* res = new vector<_T>();\n" +
    "    if (s->size() == 0) { return res; }\n" +
    "    double smallest = (*v)[0];\n" + 
    "    res->push_back((*s)[0]);\n" +
    "    \n" +
    "    for (int i = 1; i < s->size(); i++)\n" +
    "    { double next = (*v)[i];\n" +
    "      if (next < smallest)\n" +
    "      { smallest = next;\n" +
    "        res->clear();\n" +
    "        res->push_back((*s)[i]);\n" +
    "      }\n" +
    "      else if (smallest == next)\n" +
    "      { res->push_back((*s)[i]); }\n" +
    "    }\n" +
    "    return res;\n" +
    "  }\n"; 
    return res; 
  } 

  public static String countOp()
  { String res = 
      "  public static int count(List l, Object obj)\n" + 
      "  { int res = 0; \n" + 
      "    for (int _i = 0; _i < l.size(); _i++)\n" + 
      "    { if (obj == l.get(_i)) { res++; } \n" + 
      "      else if (obj != null && obj.equals(l.get(_i))) { res++; } \n" + 
      "    }\n" + 
      "    return res; \n" + 
      "  }\n\n";  
    res = res + "  public static int count(Map m, Object obj)\n" + 
      "  { List range = new Vector();\n" +  
      "    range.addAll(m.values());\n" +  
      "    return count(range,obj);  }\n\n"; 
    res = res + "  public static int count(String s, String x)\n" +
      "  { int res = 0; \n" +
      "    if (\"\".equals(s)) { return res; }\n" + 
      "    int ind = s.indexOf(x); \n" +
      "    if (ind == -1) { return res; }\n" +
      "    String ss = s.substring(ind+1,s.length());\n" +  
      "    res++; \n" +
      "    while (ind >= 0)\n" +
      "    { ind = ss.indexOf(x); \n" +
      "      if (ind == -1 || ss.equals(\"\")) { return res; }\n" + 
      "      res++; \n" +
      "      ss = ss.substring(ind+1,ss.length());\n" +
      "    } \n" + 
      "    return res;\n" +  
      "  }\n\n";  
    return res; 
  } 

  public static String countOpJava6()
  { String res = 
      "  public static int count(Collection l, Object obj)\n" + 
      "  { int res = 0; \n" + 
      "    for (Object _o : l)\n" + 
      "    { if (obj == _o) { res++; } \n" + 
      "      else if (obj != null && obj.equals(_o)) { res++; } \n" + 
      "    }\n" + 
      "    return res; \n" + 
      "  }\n\n" + 
      "  public static int count(String s, String x)\n" +
      "  { int res = 0; \n" +
      "    if (\"\".equals(s)) { return res; }\n" + 
      "    int ind = s.indexOf(x); \n" +
      "    if (ind == -1) { return res; }\n" +
      "    String ss = s.substring(ind+1,s.length());\n" +  
      "    res++; \n" +
      "    while (ind >= 0)\n" +
      "    { ind = ss.indexOf(x); \n" +
      "      if (ind == -1 || ss.equals(\"\")) { return res; }\n" + 
      "      res++; \n" +
      "      ss = ss.substring(ind+1,ss.length());\n" +
      "    } \n" + 
      "    return res;\n" +  
      "  }\n\n";  
    return res; 
  } // map case

  public static String countOpJava7()
  { String res = 
      "  public static <T> int count(Collection<T> l, T obj)\n" + 
      "  { return Collections.frequency(l,obj); }\n\n" + 
      "  public static int count(String s, String x)\n" +
      "  { int res = 0; \n" +
      "    if (\"\".equals(s)) { return res; }\n" + 
      "    int ind = s.indexOf(x); \n" +
      "    if (ind == -1) { return res; }\n" +
      "    String ss = s.substring(ind+1,s.length());\n" +  
      "    res++; \n" +
      "    while (ind >= 0)\n" +
      "    { ind = ss.indexOf(x); \n" +
      "      if (ind == -1 || ss.equals(\"\")) { return res; }\n" + 
      "      res++; \n" +
      "      ss = ss.substring(ind+1,ss.length());\n" +
      "    } \n" + 
      "    return res;\n" +  
      "  }\n\n";  
    return res; 
  } // map case

  public static String countOpCSharp()
  { String res = 
      "  public static int count(ArrayList l, object obj)\n" + 
      "  { int res = 0; \n" + 
      "    for (int _i = 0; _i < l.Count; _i++)\n" + 
      "    { if (obj == l[_i]) { res++; } \n" + 
      "      else if (obj != null && obj.Equals(l[_i])) { res++; } \n" + 
      "    }\n" + 
      "    return res; \n" + 
      "  }\n\n" + 
      "  public static int count(string s, string x)\n" +
      "  { int res = 0; \n" +
      "    if (\"\".Equals(s)) { return res; }\n" + 
      "    int ind = s.IndexOf(x); \n" +
      "    if (ind == -1) { return res; }\n" +
      "    string ss = s.Substring(ind+1,s.Length-ind-1);\n" +  
      "    res++; \n" +
      "    while (ind >= 0)\n" +
      "    { ind = ss.IndexOf(x); \n" +
      "      if (ind == -1 || ss.Equals(\"\")) { return res; }\n" + 
      "      res++; \n" +
      "      ss = ss.Substring(ind+1,ss.Length-ind-1);\n" +
      "    } \n" + 
      "    return res;\n" +  
      "  }\n\n";  
    return res; 
  } // maps

  public static String countOpCPP()
  { String res = 
      "  static int count(std::set<_T>* l, _T obj)\n" + 
      "  { if (l->find(obj) != l->end()) { return 1; } else { return 0; } \n" + 
      "  }\n\n" + 
      "  static int count(vector<_T>* l, _T obj)\n" + 
      "  { return std::count(l->begin(), l->end(), obj); }\n\n" + 
      "  static int count(string s, string x)\n" +
      "  { int res = 0; \n" +
      "    if (s.length() == 0) { return res; }\n" + 
      "    int ind = s.find(x); \n" +
      "    if (ind == string::npos) { return res; }\n" +
      "    string ss = s.substr(ind+1, s.length() - ind - 1);\n" +  
      "    res++; \n" +
      "    while (ind != string::npos)\n" +
      "    { ind = ss.find(x); \n" +
      "      if (ind == string::npos || ss.length() == 0) { return res; }\n" + 
      "      res++; \n" +
      "      ss = ss.substr(ind+1, ss.length() - ind - 1);\n" +
      "    } \n" + 
      "    return res;\n" +  
      "  }\n\n";  
    return res; 
  } // maps

  public static String charactersOp()
  { String res = 
      "  public static List characters(String str)\n" + 
      "  { char[] _chars = str.toCharArray();\n" +  
      "    Vector _res = new Vector();\n" +  
      "    for (int i = 0; i < _chars.length; i++)\n" +  
      "    { _res.add(\"\" + _chars[i]); }\n" +  
      "    return _res;\n" + 
      "  }\n\n"; 
    return res; 
  } 

  public static String charactersOpJava6()
  { String res = 
      "  public static ArrayList characters(String str)\n" + 
      "  { char[] _chars = str.toCharArray();\n" +  
      "    ArrayList _res = new ArrayList();\n" +  
      "    for (int i = 0; i < _chars.length; i++)\n" +  
      "    { _res.add(\"\" + _chars[i]); }\n" +  
      "    return _res;\n" + 
      "  }\n\n"; 
    return res; 
  } 

  public static String charactersOpJava7()
  { String res = 
      "  public static ArrayList<String> characters(String str)\n" + 
      "  { char[] _chars = str.toCharArray();\n" +  
      "    ArrayList<String> _res = new ArrayList<String>();\n" +  
      "    for (int i = 0; i < _chars.length; i++)\n" +  
      "    { _res.add(\"\" + _chars[i]); }\n" +  
      "    return _res;\n" + 
      "  }\n\n"; 
    return res; 
  } 

  public static String charactersOpCSharp()
  { String res = 
      "  public static ArrayList characters(string str)\n" + 
      "  { ArrayList _res = new ArrayList();\n" +  
      "    for (int i = 0; i < str.Length; i++)\n" +  
      "    { _res.Add(\"\" + str[i]); }\n" +  
      "    return _res;\n" + 
      "  }\n\n"; 
    return res; 
  } 

  public static String charactersOpCPP()
  { String res = 
      "  static vector<string>* characters(string str)\n" + 
      "  { vector<string>* _res = new vector<string>();\n" +  
      "    for (int i = 0; i < str.size(); i++)\n" +  
      "    { _res->push_back(str.substr(i,1)); }\n" +  
      "    return _res;\n" + 
      "  }\n\n"; 
    return res; 
  } 

  public static String prependOp()
  { String res = 
      "  public static List prepend(List l, Object ob)\n" + 
      "  { List res = new Vector();\n" + 
      "    res.add(ob);\n" + 
      "    res.addAll(l);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String prependOpJava6()
  { String res = 
      "  public static ArrayList prepend(ArrayList l, Object ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.add(ob);\n" + 
      "    res.addAll(l);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String prependOpJava7()
  { String res = 
      "  public static <T> ArrayList<T> prepend(List<T> l, T ob)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" + 
      "    res.add(ob);\n" + 
      "    res.addAll(l);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String prependOpCSharp()
  { String res = 
      "  public static ArrayList prepend(ArrayList l, object ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.Add(ob);\n" + 
      "    res.AddRange(l);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String prependOpCPP()
  { String res = 
      "  static vector<_T>* prepend(vector<_T>* l, _T ob)\n" + 
      "  { vector<_T>* res = new vector<_T>();\n" + 
      "    res->push_back(ob);\n" + 
      "    res->insert(res->end(), l->begin(), l->end());\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 


  public static String appendOp()  // also used for l->including(ob)
  { String res = 
      "  public static List append(List l, Object ob)\n" + 
      "  { List res = new Vector();\n" + 
      "    res.addAll(l);\n" + 
      "    res.add(ob);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String appendOpJava6()  // also used for l->including(ob)
  { String res = 
      "  public static ArrayList append(ArrayList l, Object ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.addAll(l);\n" + 
      "    res.add(ob);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String appendOpJava7()  // also used for l->including(ob)
  { String res = 
      "  public static <T> ArrayList<T> append(List<T> l, T ob)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" + 
      "    res.addAll(l);\n" + 
      "    res.add(ob);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String appendOpCSharp()  // also used for l->including(ob)
  { String res = 
      "  public static ArrayList append(ArrayList l, object ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    res.AddRange(l);\n" + 
      "    res.Add(ob);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String appendOpCPP()
  { String res = 
      "  static vector<_T>* append(vector<_T>* l, _T ob)\n" + 
      "  { vector<_T>* res = new vector<_T>();\n" + 
      "    res->insert(res->end(), l->begin(), l->end());\n" + 
      "    res->push_back(ob);\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateInsertAtOp()  // also used for l->including(ob)
  { String res = 
      "  public static List insertAt(List l, int ind, Object ob)\n" + 
      "  { List res = new Vector();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    if (ind <= l.size() + 1) { res.add(ob); }\n" + 
      "    for (int i = ind-1; i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static String insertAt(String l, int ind, Object ob)\n" + 
      "  { String res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    if (ind <= l.length() + 1) { res = res + ob; }\n" + 
      "    for (int i = ind-1; i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateInsertIntoOp()  
  { String res = 
      "  public static List insertInto(List l, int ind, List ob)\n" + 
      "  { List res = new Vector();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    for (int j = 0; j < ob.size(); j++)\n" + 
      "    { res.add(ob.get(j)); }\n" + 
      "    for (int i = ind-1; i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static String insertInto(String l, int ind, Object ob)\n" + 
      "  { String res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    res = res + ob; \n" + 
      "    for (int i = ind-1; i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateRemoveSetAtOps()
  { String res = 
      "  public static List removeAt(List l, int ind)\n" + 
      "  { List res = new Vector();\n" +
      "    res.addAll(l); \n" +
      "    if (ind <= res.size() && ind >= 1)\n" +
      "    { res.remove(ind - 1); } \n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res +
      "  public static String removeAt(String ss, int ind)\n" + 
      "  { StringBuffer sb = new StringBuffer(ss); \n" +
      "    if (ind <= ss.length() && ind >= 1)\n" +
      "    { sb.deleteCharAt(ind - 1); } \n" +
      "    return sb.toString();\n" +
      "  }\n\n"; 

    res = res + 
      "  public static List removeFirst(List l, Object x)\n" + 
      "  { List res = new Vector();\n" +
      "    res.addAll(l); \n" +
      "    res.remove(x);\n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + 
      "  public static List setAt(List l, int ind, Object val)\n" +
      "  { List res = new Vector();\n" +
      "    res.addAll(l); \n" +
      "    if (ind <= res.size() && ind >= 1)\n" +
      "    { res.set(ind - 1,val); } \n" +
      "    return res;\n" +
      "  }\n"; 
    res = res + "  public static String setAt(String ss, int ind, Object val)\n" +
      "  { String res = ss;\n" + 
      "    if (ind <= res.length() && ind >= 1)\n" +
      "    { res = ss.substring(0,ind-1); \n" +
      "      res = res + val + ss.substring(ind);\n" + 
      "    } \n" +
      "    return res;\n" +
      "  }\n"; 

    return res; 
  } 

  public static String generateInsertAtOpJava6()  // also used for l->including(ob)
  { String res = 
      "  public static ArrayList insertAt(ArrayList l, int ind, Object ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    if (ind <= l.size() + 1) { res.add(ob); }\n" + 
      "    for (int i = ind-1; i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static String insertAt(String l, int ind, Object ob)\n" + 
      "  { String res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    if (ind <= l.length() + 1) { res = res + ob; }\n" + 
      "    for (int i = ind-1; i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateInsertIntoOpJava6()  
  { String res = 
      "  public static ArrayList insertInto(ArrayList l, int ind, ArrayList ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    for (int j = 0; j < ob.size(); j++)\n" + 
      "    { res.add(ob.get(j)); }\n" + 
      "    for (int i = ind-1; i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static String insertInto(String l, int ind, Object ob)\n" + 
      "  { String res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    res = res + ob;\n" + 
      "    for (int i = ind-1; i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 


  public static String generateRemoveSetAtOpsJava6()
  { String res = 
      "  public static ArrayList removeAt(ArrayList l, int ind)\n" + 
      "  { ArrayList res = new ArrayList();\n" +
      "    res.addAll(l); \n" +
      "    if (ind <= res.size() && ind >= 1)\n" +
      "    { res.remove(ind - 1); } \n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res +
      "  public static String removeAt(String ss, int ind)\n" + 
      "  { StringBuffer sb = new StringBuffer(ss); \n" +
      "    if (ind <= ss.length() && ind >= 1)\n" +
      "    { sb.deleteCharAt(ind - 1); } \n" +
      "    return sb.toString();\n" +
      "  }\n\n"; 

    res = res + 
      "  public static ArrayList removeFirst(ArrayList l, Object x)\n" + 
      "  { ArrayList res = new ArrayList();\n" +
      "    res.addAll(l); \n" +
      "    res.remove(x);\n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + 
      "  public static ArrayList setAt(ArrayList l, int ind, Object val)\n" +
      "  { ArrayList res = new ArrayList();\n" +
      "    res.addAll(l); \n" +
      "    if (ind <= res.size() && ind >= 1)\n" +
      "    { res.set(ind - 1,val); } \n" +
      "    return res;\n" +
      "  }\n\n";
 
    res = res + "  public static String setAt(String ss, int ind, Object val)\n" +
      "  { String res = ss;\n" + 
      "    if (ind <= res.length() && ind >= 1)\n" +
      "    { res = ss.substring(0,ind-1); \n" +
      "      res = res + val + ss.substring(ind);\n" + 
      "    } \n" +
      "    return res;\n" +
      "  }\n"; 

    return res; 
  } 

  public static String generateInsertAtOpJava7()  
  { String res = 
      "  public static <T> ArrayList<T> insertAt(List<T> l, int ind, T ob)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    if (ind <= l.size() + 1) { res.add(ob); }\n" + 
      "    for (int i = ind-1; i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static String insertAt(String l, int ind, Object ob)\n" + 
      "  { String res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    if (ind <= l.length() + 1) { res = res + ob; }\n" + 
      "    for (int i = ind-1; i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateInsertIntoOpJava7()  
  { String res = 
      "  public static <T> ArrayList<T> insertInto(List<T> l, int ind, List<T> ob)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    for (int j = 0; j < ob.size(); j++)\n" + 
      "    { res.add(ob.get(j)); }\n" + 
      "    for (int i = ind-1; i < l.size(); i++)\n" +  
      "    { res.add(l.get(i)); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static String insertInto(String l, int ind, Object ob)\n" + 
      "  { String res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    res = res + ob;\n" + 
      "    for (int i = ind-1; i < l.length(); i++)\n" +  
      "    { res = res + l.charAt(i); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 


  public static String generateRemoveSetAtOpsJava7()
  { String res = 
      "  public static <T> ArrayList<T> removeAt(List<T> l, int ind)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" +
      "    res.addAll(l); \n" +
      "    if (ind <= res.size() && ind >= 1)\n" +
      "    { res.remove(ind - 1); } \n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res +
      "  public static String removeAt(String ss, int ind)\n" + 
      "  { StringBuffer sb = new StringBuffer(ss); \n" +
      "    if (ind <= ss.length() && ind >= 1)\n" +
      "    { sb.deleteCharAt(ind - 1); } \n" +
      "    return sb.toString();\n" +
      "  }\n\n"; 

    res = res + 
      "  public static <T> ArrayList<T> removeFirst(List<T> l, T x)\n" + 
      "  { ArrayList<T> res = new ArrayList<T>();\n" +
      "    res.addAll(l); \n" +
      "    res.remove(x);\n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + 
      "  public static <T> ArrayList<T> setAt(List<T> l, int ind, T val)\n" +
      "  { ArrayList<T> res = new ArrayList<T>();\n" +
      "    res.addAll(l); \n" +
      "    if (ind <= res.size() && ind >= 1)\n" +
      "    { res.set(ind - 1,val); } \n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + "  public static String setAt(String ss, int ind, Object val)\n" +
      "  { String res = ss;\n" + 
      "    if (ind <= res.length() && ind >= 1)\n" +
      "    { res = ss.substring(0,ind-1); \n" +
      "      res = res + val + ss.substring(ind);\n" + 
      "    } \n" +
      "    return res;\n" +
      "  }\n"; 

    return res; 
  } 

  public static String generateInsertAtOpCSharp()  
  { String res = 
      "  public static ArrayList insertAt(ArrayList l, int ind, object ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.Count; i++)\n" +  
      "    { res.Add(l[i]); }\n" + 
      "    if (ind <= l.Count + 1) { res.Add(ob); }\n" + 
      "    for (int i = ind-1; i < l.Count; i++)\n" +  
      "    { res.Add(l[i]); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static string insertAt(string l, int ind, object ob)\n" + 
      "  { string res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.Length; i++)\n" +  
      "    { res = res + l[i]; }\n" + 
      "    if (ind <= l.Length + 1) { res = res + ob; }\n" + 
      "    for (int i = ind-1; i < l.Length; i++)\n" +  
      "    { res = res + l[i]; }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateInsertIntoOpCSharp()  
  { String res = 
      "  public static ArrayList insertInto(ArrayList l, int ind, ArrayList ob)\n" + 
      "  { ArrayList res = new ArrayList();\n" + 
      "    for (int i = 0; i < ind-1 && i < l.Count; i++)\n" +  
      "    { res.Add(l[i]); }\n" + 
      "    for (int j = 0; j < ob.Count; j++) \n" + 
      "    { res.Add(ob[j]); }\n" + 
      "    for (int i = ind-1; i < l.Count; i++)\n" +  
      "    { res.Add(l[i]); }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    res = res + "  public static string insertInto(string l, int ind, object ob)\n" + 
      "  { string res = \"\";\n" + 
      "    for (int i = 0; i < ind-1 && i < l.Length; i++)\n" +  
      "    { res = res + l[i]; }\n" + 
      "    res = res + ob; \n" + 
      "    for (int i = ind-1; i < l.Length; i++)\n" +  
      "    { res = res + l[i]; }\n" + 
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateRemoveSetAtOpsCSharp()  
  { String res = 
       "  public static ArrayList removeFirst(ArrayList a, object x)\n" +
       "  { ArrayList res = new ArrayList();\n" +
       "    res.AddRange(a);\n" +
       "    res.Remove(x);\n" +
       "    return res; \n" +
       "   }\n\n"; 

    res = res + 
       "  public static ArrayList removeAt(ArrayList a, int i)\n" +
       "  {\n" +
       "    ArrayList res = new ArrayList();\n" +
       "    res.AddRange(a);\n" +
       "    if (i <= res.Count && i >= 1)\n" +
       "    { res.RemoveAt(i - 1); }\n" +
       "    return res;\n" +
       "  }\n\n"; 

     res = res + "  public static string removeAtString(string a, int i)\n" +
         "  { string res = \"\";\n" +
         "    for (int x = 0; x < i-1 && x < a.Length; x++)\n" +
         "    { res = res + a[x]; }\n" +
         "    for (int x = i; x >= 0 && x < a.Length; x++)\n" +
         "    { res = res + a[x]; }\n" +
         "    return res;\n" +
         "  }\n\n"; 

     res = res + 
       "  public static ArrayList setAt(ArrayList a, int i, object x)\n" +
       "  {\n" +
       "    ArrayList res = new ArrayList();\n" +
       "    res.AddRange(a);\n" +
       "    if (i <= res.Count && i >= 1)\n" +
       "    { res[i - 1] = x; }\n" +
       "    return res;\n" +
       "  }\n\n"; 

      res = res + "  public static string setAt(string a, int i, string x)\n" + 
         "  { string res = \"\";\n" + 
         "    for (int j = 0; j < i-1 && j < a.Length; j++)\n" + 
         "    { res = res + a[j]; }\n" + 
         "    if (i <= a.Length && i >= 1)\n" + 
         "    { res = res + x; }\n" + 
         "    for (int j = i; j >= 0 && j < a.Length; j++)\n" + 
         "    { res = res + a[j]; }\n" + 
         "    return res;\n" + 
         "  }\n\n"; 

    return res; 
  } 


  public static String generateInsertAtOpCPP()  
  { String res = 
      "  static vector<_T>* insertAt(vector<_T>* l, int ind, _T ob)\n" + 
      "  { vector<_T>* res = new vector<_T>();\n" + 
      "    res->insert(res->end(), l->begin(), l->end());\n" +  
      "    res->insert(res->begin() + (ind - 1), ob);\n" +
      "    return res; \n" +  
      "  }\n"; 
    res = res + "  static string insertAt(string l, int ind, string ob)\n" + 
      "  { string res(l);\n" + 
      "    res.insert(ind-1,ob);\n" +  
      "    return res;\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateRemoveSetAtOpsCPP()  
  { String res = 
      "  static vector<_T>* setAt(vector<_T>* l, int ind, _T ob)\n" +
      "  { vector<_T>* res = new vector<_T>();\n" +
      "    res->insert(res->end(), l->begin(), l->end());\n" +
      "    if (ind >= 1 && ind <= res->size())\n" +
      "    { (*res)[(ind - 1)] = ob; }\n" +
      "    return res; \n" +
      "  }\n\n";

  res = res + 
      "  static string setAt(string st, int ind, string ch)\n" +
      "  { string res = \"\";\n" +
      "    if (ind >= 1 && ind <= st.length())\n" +
      "    { res = st.substr(0,ind-1).append(ch).append(st.substr(ind, st.length()-ind)); }\n" +
      "    else\n" + 
      "    { res = st; }\n" +
      "    return res; \n" +
      "  }\n\n"; 

    res = res + 
      "  static vector<_T>* removeAt(vector<_T>* l, int ind)\n" +
      "  { if (ind >= 1 && ind <= l->size())\n" +
      "    { vector<_T>* res = new vector<_T>();\n" +
      "      res->insert(res->end(), l->begin(), l->begin() + (ind - 1));\n" +
      "      res->insert(res->end(), l->begin() + ind, l->end());\n" +
      "      return res;\n" +
      "    }\n" +
      "    return l;\n" + 
      "  }\n\n"; 

    res = res + "  static string removeAt(string ss, int ind)\n" +
      "  { if (ind >= 1 && ind <= ss.length())\n" +
      "    { string res = ss.substr(0,ind-1);\n" +
      "      res = res + ss.substr(ind);\n" + 
      "      return res;\n" +
      "    } \n" +
      "    return ss;\n" + 
      "  }\n"; 

    res = res + 
      "  static vector<_T>* removeFirst(vector<_T>* sq, _T x)\n" +
      "  { vector<_T>* res = new vector<_T>();\n" +
      "    res->insert(res->end(), sq->begin(), sq->end());\n" +
      "    auto iter = find(res->begin(), res->end(), x);\n" + 
      "    if (iter != res->end())\n" +
      "    { res->erase(iter); }\n" +
      "    return res;\n" + 
      "  }\n\n"; 
    return res; 
  }  


  public static String generateIndexOfOpCPP()  // also used for l->including(ob)
  { String res = 
      "  static int indexOf(_T x, vector<_T>* a)\n" + 
      "  { int res = 0; \n" +
      "    for (int i = 0; i < a->size(); i++)\n" + 
      "    { if (x == (*a)[i])\n" + 
      "      { return i+1; } }\n" +
      "    return res; \n" +
      "  }\n\n"; 

    res = res + "  static int indexOf(vector<_T>* a, vector<_T>* b)\n" + 
      "  { /* Index of a subsequence a of sequence b in b */\n" + 
      "\n" + 
      "    if (a->size() == 0 || b->size() == 0)\n" +  
      "    { return 0; }\n" +  
      "    int i = 0; \n" +
      "    while (i < b->size() && (*b)[i] != (*a)[0])\n" + 
      "    { i++; } \n" +
      "\n" + 
      "    if (i >= b->size())\n" + 
      "    { return 0; } \n" +
      "\n" + 
      "    int j = 0; \n" +
      "    while (j < a->size() && i+j < b->size() && (*b)[i+j] == (*a)[j]) \n" +
      "    { j++; }\n" +
      "\n" +
      "    if (j >= a->size())\n" + 
      "    { return i+1; }\n" +
      "\n" +
      "    vector<_T>* subr = subrange(b, i+2, b->size());\n" + 
      "    int res1 = indexOf(a,subr); \n" +
      "    if (res1 == 0) \n" +
      "    { return 0; } \n" +
      "    return res1 + i + 1;\n" + 
      "  }\n\n"; 

    res = res + "  static int lastIndexOf(vector<_T>* a, vector<_T>* b)\n" +
      "  { int res = 0; \n" +
      "    if (a->size() == 0 || b->size() == 0)\n" + 
      "    { return res; }\n" +
      "\n" +
      "    vector<_T>* arev = reverse(a); \n" +
      "    vector<_T>* brev = reverse(b);\n" +
      "    int i = indexOf(arev,brev);\n" + 
      "    if (i == 0) \n" +
      "    { return res; }\n" +
      "    res = b->size() - i - a->size() + 2;\n" + 
      "    return res;\n" + 
      "  }\n\n"; 

    res = res + "  static int indexOf(string x, string str)\n" + 
      "  { int res = str.find(x); \n" +
      "    if (res == string::npos) { return 0; }\n" + 
      "    return res + 1; \n" +
      "  } \n\n" + 
      "  static int lastIndexOf(_T x, vector<_T>* a)\n" + 
      "  { int res = 0; \n" +
      "    for (int i = a->size() - 1; i >= 0; i--)\n" + 
      "    { if (x == (*a)[i])\n" + 
      "      { return i+1; } }\n" +
      "    return res; \n" +
      "  }\n\n"; 

    res = res + "  static int lastIndexOf(string x, string str)\n" + 
      "  { int res = str.rfind(x); \n" +
      "    if (res == string::npos) { return 0; }\n" + 
      "    return res + 1; \n" +
      "  } \n\n"; 
    return res; 
  } 

  public static String generateUCLCOpsCPP()  // also used for l->including(ob)
  { String res = 
      "  static string toLowerCase(string str)\n" + 
      "  { string res(str);\n" + 
      "    for (int i = 0; i < str.length(); i++)\n" +  
      "    { res[i] = tolower(str[i]); }\n" +
      "    return res; \n" +  
      "  }\n\n"; 
    res = res + "  static string toUpperCase(string str)\n" + 
      "  { string res(str);\n" + 
      "    for (int i = 0; i < str.length(); i++)\n" +  
      "    { res[i] = toupper(str[i]); }\n" +
      "    return res;\n" + 
      "  }\n\n";
    res = res + "  static bool equalsIgnoreCase(string str1, string str2)\n" + 
      "  { int len1 = str1.length();\n" + 
      "    int len2 = str2.length();\n" + 
      "    if (len1 != len2) { return false; }\n" + 
      "    for (int i = 0; i < len1; i++)\n" +  
      "    { if (tolower(str1[i]) == tolower(str2[i]))\n" + 
      "      { }\n" + 
      "      else \n" + 
      "      { return false; }\n" +
      "    }\n" + 
      "    return true;\n" + 
      "  }\n\n";
 
    return res; 
  } 

  public static String generateSWEWOpsCPP()  // also used for l->including(ob)
  { String res = 
      "  static bool startsWith(string s1, string s2)\n" + 
      "  { int l1 = s1.length(); \n" + 
      "    int l2 = s2.length();\n" + 
      "    if (l1 < l2) { return false; }\n" + 
      "    if (s1.substr(0,l2) == s2) { return true; }\n" +  
      "    return false; \n" +  
      "  }\n\n"; 
    res = res + 
      "  static bool endsWith(string s1, string s2)\n" + 
      "  { int l1 = s1.length(); \n" + 
      "    int l2 = s2.length();\n" + 
      "    if (l1 < l2) { return false; }\n" + 
      "    if (s1.substr(l1-l2,l2) == s2) { return true; }\n" +  
      "    return false; \n" +  
      "  }\n\n"; 
    return res; 
  } 

  /* public static int count(String s, String x)
  { int res = 0; 
    if ("".equals(s)) { return res; } 
    int ind = s.indexOf(x); 
    if (ind == -1) { return res; }
    String ss = s.substring(ind+1,s.length());  
    res++; 
    while (ind >= 0)
    { ind = ss.indexOf(x); 
      if (ind == -1 || ss.equals("")) { return res; } 
      res++; 
      ss = ss.substring(ind+1,ss.length());
    } 
    return res; 
  } */ 

  public static String generateLastOp()
  { String res = "    public static Object last(List v)\n" +
    "    { if (v.size() == 0) { return null; }\n" +
    "      return v.get(v.size() - 1);\n" +
    "    }\n\n";
    return res;
  }

  public static String generateLastOpJava6()
  { String res = "    public static Object last(ArrayList v)\n" +
    "    { if (v.size() == 0) { return null; }\n" +
    "      return v.get(v.size() - 1);\n" +
    "    }\n\n";
    return res;
  }

  public static String generateLastOpJava7()
  { String res = "    public static <T> T last(List<T> v)\n" +
    "    { if (v.size() == 0) { return null; }\n" +
    "      return v.get(v.size() - 1);\n" +
    "    }\n\n";
    return res;
  }

  public static String generateLastOpCSharp()
  { String res = "    public static object last(ArrayList v)\n" +
    "    { if (v.Count == 0) { return null; }\n" +
    "      return v[v.Count - 1];\n" +
    "    }\n\n";
    return res;
  }

  public static String generateLastOpCPP()
  { String res = "  static _T last(vector<_T>* v)\n" +
    "  { if (v->size() == 0) { return 0; }\n" +
    "    return v->at(v->size() - 1);\n" +
    "  }\n\n" + 
    "  static _T last(std::set<_T>* v)\n" +
    "  { if (v->size() == 0) { return 0; }\n" +
    "    std::set<_T>::iterator _pos = v->end();\n" +
    "    _pos--;\n" + 
    "    return *_pos;\n" + 
    "  }\n\n";
    return res;
  }  // and for set<_T>*

  public static String generateSubcollectionsOpJava6()
  { String res = "    public static ArrayList subcollections(ArrayList v)\n" +
    "    { ArrayList res = new ArrayList();\n" +
    "      if (v.size() == 0) { res.add(new ArrayList()); return res; }\n" +
    "      if (v.size() == 1) { res.add(new ArrayList()); res.add(v); return res;\n " +
    "      }\n" +
    "      ArrayList s = new ArrayList();\n" +
    "      Object x = v.get(0);\n" + 
    "      s.addAll(v);\n" +
    "      s.remove(0);\n" +
    "      ArrayList scs = subcollections(s);\n" +
    "      res.addAll(scs);\n" +
    "      for (int i = 0; i < scs.size(); i++)\n" +
    "      { ArrayList sc = (ArrayList) scs.get(i);\n" +
    "        ArrayList scc = new ArrayList();\n" +
    "        scc.add(x); scc.addAll(sc); res.add(scc); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    res = res +
    "    public static HashSet subcollections(HashSet v)\n" +
    "    { HashSet res = new HashSet();\n" +
    "      if (v.size() == 0) { res.add(new HashSet()); return res; }\n" +
    "      if (v.size() == 1) { res.add(new HashSet()); res.add(v); return res;\n " +
    "      }\n" +
    "      HashSet s = new HashSet();\n" +
    "      Object x = null; int _i = 0;\n" +
    "      for (Object _o : v)\n" +
    "      { if (_i == 0) { x = _o; _i++; }\n" +
    "         else { s.add(_o); }\n" +  
    "      }\n" +
    "      HashSet scs = subcollections(s);\n" +
    "      res.addAll(scs);\n" +
    "      for (Object _obj : scs)\n" +
    "      { HashSet sc = (HashSet) _obj;\n" +
    "        HashSet scc = new HashSet();\n" +
    "        scc.add(x); scc.addAll(sc); res.add(scc); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n";
    return res;
  }

  public static String generateSubcollectionsOpJava7()
  { String res = "    public static <T> ArrayList<List<T>> subcollections(ArrayList<T> v)\n" +
    "    { ArrayList<List<T>> res = new ArrayList<List<T>>();\n" +
    "      if (v.size() == 0)\n" +  
    "      { res.add(new ArrayList<T>()); return res; }\n" +
    "      if (v.size() == 1)\n" + 
    "      { res.add(new ArrayList<T>()); res.add(v); return res;\n " +
    "      }\n" +
    "      ArrayList<T> s = new ArrayList<T>();\n" +
    "      T x = v.get(0);\n" + 
    "      s.addAll(v);\n" +
    "      s.remove(0);\n" +
    "      ArrayList<List<T>> scs = subcollections(s);\n" +
    "      res.addAll(scs);\n" +
    "      for (int i = 0; i < scs.size(); i++)\n" +
    "      { ArrayList<T> sc = (ArrayList<T>) scs.get(i);\n" +
    "        ArrayList<T> scc = new ArrayList<T>();\n" +
    "        scc.add(x); scc.addAll(sc); res.add(scc); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    res = res +
    "    public static <T> HashSet<Set<T>> subcollections(HashSet<T> v)\n" +
    "    { HashSet<Set<T>> res = new HashSet<Set<T>>();\n" +
    "      if (v.size() == 0) { res.add(new HashSet<T>()); return res; }\n" +
    "      if (v.size() == 1) { res.add(new HashSet<T>()); res.add(v); return res;\n " +
    "      }\n" +
    "      HashSet<T> s = new HashSet<T>();\n" +
    "      T x = null; int _i = 0;\n" +
    "      for (T _o : v)\n" +
    "      { if (_i == 0) { x = _o; _i++; }\n" +
    "         else { s.add(_o); }\n" +  
    "      }\n" +
    "      HashSet<Set<T>> scs = subcollections(s);\n" +
    "      res.addAll(scs);\n" +
    "      for (Set<T> _obj : scs)\n" +
    "      { HashSet<T> sc = (HashSet<T>) _obj;\n" +
    "        HashSet<T> scc = new HashSet<T>();\n" +
    "        scc.add(x); scc.addAll(sc); res.add(scc); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n";
    return res;
  }  // and for TreeSet

  public static String generateSubcollectionsOpCPP()
  { String res = "     static vector<vector<_T>*>* subcollections(vector<_T>*  v)\n" +
    "    { vector<vector<_T>*>* res = new vector<vector<_T>*>();\n" +
    "      vector<_T>* r = new vector<_T>();\n" +
    "      if (v->size() == 0)\n" +
    "      { res->push_back(r); return res; }\n" +
    "      if (v->size() == 1) { res->push_back(r); res->push_back(v); return res;\n " +
    "      }\n" +
    "      _T x = (*v)[0];\n" + 
    "      for (int i = 1; i < v->size(); i++)\n" +
    "      { r->push_back((*v)[i]); }\n" +
    "      vector<vector<_T>*>* scs = UmlRsdsLib<_T>::subcollections(r);\n" +
    "      res->insert(res->end(), scs->begin(), scs->end());\n" +
    "      for (int i = 0; i < scs->size(); i++)\n" +
    "      { vector<_T>* sc = (*scs)[i];\n" +
    "        vector<_T>* scc = new vector<_T>();\n" +
    "        scc->push_back(x);\n" + 
    "        scc->insert(scc->end(), sc->begin(), sc->end());\n" +
    "        res->push_back(scc); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    res = res +
    "     static std::set<set<_T>*>* subcollections(std::set<_T>* v)\n" +
    "    { std::set<set<_T>*>* res = new std::set<set<_T>*>();\n" +
    "      std::set<_T>* r = new std::set<_T>();\n" +
    "      if (v->size() == 0)\n" +
    "      { res->insert(r); return res; }\n" +
    "      if (v->size() == 1)\n" + 
    "      { res->insert(r);\n" + 
    "        res->insert(v);\n" + 
    "        return res;\n " +
    "      }\n" +
    "      std::set<_T>::iterator _pos = v->begin();\n" +
    "      _T x = *_pos;\n" +
    "      _pos++; \n" +
    "      for (; _pos != v->end(); _pos++)\n" +
    "      { r->insert(*_pos); }\n" +  
    "      std::set<set<_T>*>* scs = UmlRsdsLib<_T>::subcollections(r);\n" +
    "      res->insert(scs->begin(), scs->end());\n" +
    "      for (std::set<set<_T>*>::iterator _obj = scs->begin(); _obj != scs->end(); _obj++)\n" +
    "      { std::set<_T>* sc = *_obj;\n" +
    "        std::set<_T>* scc = new std::set<_T>();\n" +
    "        scc->insert(x); \n" +
    "        scc->insert(sc->begin(), sc->end());\n" +
    "        res->insert(scc); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n";
    return res;
  }

  public static String generateConcatAllOp()
  { String res = "    public static List concatenateAll(List a)\n" +
    "    { List res = new Vector();\n" +
    "      for (int i = 0; i < a.size(); i++)\n" +
    "      { List r = (List) a.get(i);\n" +
    "        res.addAll(r); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    return res;
  }

  public static String generateConcatAllOpJava6()
  { String res = "    public static ArrayList concatenateAll(List a)\n" +
    "    { ArrayList res = new ArrayList();\n" +
    "      for (int i = 0; i < a.size(); i++)\n" +
    "      { Collection r = (Collection) a.get(i);\n" +
    "        res.addAll(r); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    return res;
  }

  public static String generateConcatAllOpJava7()
  { String res = "    public static ArrayList concatenateAll(ArrayList a)\n" +
    "    { ArrayList res = new ArrayList();\n" +
    "      for (int i = 0; i < a.size(); i++)\n" +
    "      { Collection r = (Collection) a.get(i);\n" +
    "        res.addAll(r); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    return res;
  }

  public static String generateConcatAllOpCSharp()
  { String res = "    public static ArrayList concatenateAll(ArrayList a)\n" +
    "    { ArrayList res = new ArrayList();\n" +
    "      for (int i = 0; i < a.Count; i++)\n" +
    "      { ArrayList r = (ArrayList) a[i];\n" +
    "        res.AddRange(r); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    return res;
  }

  public static String generateConcatAllOpCPP()
  { String res = "    static vector<_T>* concatenateAll(vector<vector<_T>*>* a)\n" +
    "    { vector<_T>* res = new vector<_T>();\n" +
    "      for (int i = 0; i < a->size(); i++)\n" +
    "      { vector<_T>* r = (*a)[i];\n" +
    "        res->insert(res->end(), r->begin(), r->end()); \n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    return res;
  }

  public static String generateAsSetOpJava6()
  { String res = "    public static HashSet asSet(Collection c)\n" +
    "    { HashSet res = new HashSet();\n" +
    "      res.addAll(c);\n" +
    "      return res;\n" +
    "    }\n\n";

    res = res + "    public static ArrayList asOrderedSet(Collection c)\n" + 
    "    { ArrayList res = new ArrayList();\n" +  
    "      for (Object x : c)\n" + 
    "      { if (res.contains(x)) { }\n" +  
    "        else \n" + 
    "        { res.add(x); }\n" +  
    "      } \n" + 
    "      return res; \n" + 
    "    }\n\n"; 

    res = res + 
      "  public static ArrayList mapAsSequence(Map m)\n" +
      "  { ArrayList range = new ArrayList();\n" +
      "    java.util.Set ss = m.entrySet();\n" + 
      "    for (Object x : ss)\n" +
      "    { Map.Entry ee = (Map.Entry) x;\n" + 
      "      HashMap mx = new HashMap(); \n" +
      "      mx.put(ee.getKey(), ee.getValue());\n" + 
      "      range.add(mx); \n" +
      "    } \n" +
      "    return range;\n" + 
      "  }\n\n"; 

    res = res + 
      "  public static HashSet mapAsSet(Map m)\n" +
      "  { ArrayList range = mapAsSequence(m); \n" +
      "    return asSet(range); \n" +
      "  }\n\n"; 

    return res;
  }

  public static String generateAsSetOpJava7()
  { String res = 
    "    public static <T> HashSet<T> asSet(Collection<T> c)\n" +
    "    { HashSet<T> res = new HashSet<T>();\n" +
    "      res.addAll(c);\n" +
    "      return res;\n" +
    "    }\n\n";

    res = res + "    public static <S,T> HashSet<HashMap<S,T>> asSet(Map<S,T> m)\n" +
    "    { ArrayList<HashMap<S,T>> res = Ocl.asSequence(m);\n" +
    "      return Ocl.asSet(res);\n" +
    "    }\n\n";

    res = res + "    public static <T> ArrayList<T> asOrderedSet(Collection<T> c)\n" + 
    "    { ArrayList<T> res = new ArrayList<T>();\n" +  
    "      for (T x : c)\n" + 
    "      { if (res.contains(x)) { }\n" +  
    "        else \n" + 
    "        { res.add(x); }\n" +  
    "      } \n" + 
    "      return res;\n" +  
    "    }\n\n"; 

    res = res + "    public static <S,T> ArrayList<HashMap<S,T>> asSequence(Map<S,T> m)\n" + 
    "    { Set<Map.Entry<S,T>> ss = m.entrySet();\n" + 
    "      ArrayList<HashMap<S,T>> res = new ArrayList<HashMap<S,T>>();\n" + 
    "      for (Map.Entry<S,T> item : ss)\n" + 
    "      { HashMap<S,T> maplet = new HashMap<S,T>();\n" +  
    "        maplet.put(item.getKey(), item.getValue()); \n" + 
    "        res.add(maplet); \n" + 
    "      }  \n" + 
    "      return res;\n" +  
    "    }\n"; 

    return res;
  }

  public String generateAsSortedSetOpJava7()
  { String res = 
    "    public static <T> TreeSet<T> asSortedSet(Collection<T> c)\n" +
    "    { TreeSet<T> res = new TreeSet<T>();\n" +
    "      res.addAll(c);\n" +
    "      return res;\n" +
    "    }\n\n";
    return res;
  }

  public static String generateAsSetOpCPP()
  { String res = "     static std::set<_T>* asSet(vector<_T>* c)\n" +
    "    { std::set<_T>* res = new std::set<_T>();\n" +
    "      res->insert(c->begin(), c->end());\n" +
    "      return res;\n" +
    "    }\n\n" +
    "    static std::set<_T>* asSet(std::set<_T>* c)\n" +
    "    { return c; }\n\n";

    res = res + "    static vector<_T>* asOrderedSet(vector<_T>* c)\n" + 
    "    { vector<_T>* res = new vector<_T>();\n" + 
    "      for (vector<_T>::iterator _pos = c->begin(); _pos != c->end(); ++_pos)\n" + 
    "      { if (isIn(*_pos, res)) { }\n" +  
    "        else \n" + 
    "        { res->push_back(*_pos); }\n" +  
    "    } \n" + 
    "    return res;\n" +  
    "  }\n\n"; 

    res = res + "    static vector<_T>* asOrderedSet(set<_T>* c)\n" + 
    "    { vector<_T>* res = new vector<_T>();\n" + 
    "      for (set<_T>::iterator _pos = c->begin(); _pos != c->end(); ++_pos)\n" + 
    "      { res->push_back(*_pos); }\n" +  
    "      return res;\n" +  
    "    }\n\n"; 

    res = res + "    static vector<_T>* randomiseSequence(vector<_T>* sq)\n" + 
    "    { vector<_T>* res = new vector<_T>();\n" + 
    "      for (vector<_T>::iterator _pos = sq->begin(); _pos != sq->end(); ++_pos)\n" + 
    "      { res->push_back(*_pos); }\n" +  
    "      std::random_shuffle(res->begin(), res->end());\n" + 
    "      return res; \n" + 
    "    }\n\n"; 

    return res;
  } // also need asBag operations - same as sort

  public static String generateAsSequenceOpJava6()
  { String res = "    public static ArrayList asSequence(Collection c)\n" +
    "    { ArrayList res = new ArrayList();\n" +
    "      res.addAll(c);\n" +
    "      return res;\n" +
    "    }\n\n";

    res = res + 
    "    public static ArrayList asBag(Collection c)\n" + 
    "    { ArrayList res = new ArrayList(); \n" + 
    "      res.addAll(c); \n" + 
    "      Collections.sort(res);\n" +  
    "      return res;\n" +  
    "    }\n\n";  

    return res;
  }

  public static String generateAsSequenceOpJava7()
  { String res = "    public static <T> ArrayList<T> asSequence(Collection<T> c)\n" +
    "    { ArrayList res = new ArrayList<T>();\n" +
    "      res.addAll(c);\n" +
    "      return res;\n" +
    "    }\n\n";

    res = res + 
    "    public static <T> ArrayList asBag(Collection<T> c)\n" + 
    "    { ArrayList res = new ArrayList(); \n" + 
    "      res.addAll(c); \n" + 
    "      Collections.sort(res);\n" +  
    "      return res;\n" +  
    "    }\n\n";  

    return res;
  }

  public static String generateAsSequenceOpCPP()
  { String res = "     static vector<_T>* asSequence(std::set<_T>* c)\n" +
    "    { vector<_T>* res = new vector<_T>();\n" +
    "      res->insert(res->end(), c->begin(), c->end());\n" +
    "      return res;\n" +
    "    }\n\n" +
    "    static vector<_T>* asSequence(vector<_T>* c)\n" +
    "    { return c; }\n\n";
    return res;
  }

  public static String symmetricDifferenceOpCPP()
  { String res = "     static std::set<_T>* symmetricDifference(vector<_T>* a, vector<_T>* b)\n" +
    "    { std::set<_T>* res = new std::set<_T>();\n" +
    "      for (int i = 0; i < a->size(); i++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn((*a)[i], b)) { }\n" +
    "        else { res->insert((*a)[i]); }\n" +
    "      }\n" +
    "      for (int i = 0; i < b->size(); i++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn((*b)[i], a)) { }\n" +
    "        else { res->insert((*b)[i]); }\n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n" +
    "    static std::set<_T>* symmetricDifference(std::set<_T>* a, vector<_T>* b)\n" +
    "    { std::set<_T>* res = new std::set<_T>();\n" +
    "      for (std::set<_T>::iterator _pos = a->begin(); _pos != a->end(); _pos++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn(*_pos, b)) { }\n" +
    "        else { res->insert(*_pos); }\n" +
    "      }\n" +
    "      for (int i = 0; i < b->size(); i++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn((*b)[i], a)) { }\n" +
    "        else { res->insert((*b)[i]); }\n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n" +
    "     static std::set<_T>* symmetricDifference(vector<_T>* a, std::set<_T>* b)\n" +
    "    { std::set<_T>* res = new std::set<_T>();\n" +
    "      for (int i = 0; i < a->size(); i++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn((*a)[i], b)) { }\n" +
    "        else { res->insert((*a)[i]); }\n" +
    "      }\n" +
    "      for (std::set<_T>::iterator _pos = b->begin(); _pos != b->end(); _pos++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn(*_pos, a)) { }\n" +
    "        else { res->insert(*_pos); }\n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n" +
    "    static std::set<_T>* symmetricDifference(std::set<_T>* a, std::set<_T>* b)\n" +
    "    { std::set<_T>* res = new std::set<_T>();\n" +
    "      for (std::set<_T>::iterator _pos = a->begin(); _pos != a->end(); _pos++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn(*_pos, b)) { }\n" +
    "        else { res->insert(*_pos); }\n" +
    "      }\n" +
    "      for (std::set<_T>::iterator _pos = b->begin(); _pos != b->end(); _pos++)\n" +
    "      { if (UmlRsdsLib<_T>::isIn(*_pos, a)) { }\n" +
    "        else { res->insert(*_pos); }\n" +
    "      }\n" +
    "      return res;\n" +
    "    }\n\n";
    return res;
  }


  public static String generateIsUniqueOp()
  { String res = 
      "  public static boolean isUnique(List evals)\n" +
      "  { List vals = new Vector(); \n" +
      "    for (int i = 0; i < evals.size(); i++)\n" + 
      "    { Object ob = evals.get(i); \n" +
      "      if (vals.contains(ob)) { return false; }\n" + 
      "      vals.add(ob);\n" + 
      "    }\n" +
      "    return true;\n" +  
      "  }\n";  
    return res; 
  } 

  public static String generateIsUniqueOpJava6()
  { String res = 
      "  public static boolean isUnique(Collection evals)\n" +
      "  { HashSet vals = new HashSet(); \n" +
      "    for (Object ob : evals)\n" + 
      "    { if (vals.contains(ob)) { return false; }\n" + 
      "      vals.add(ob);\n" + 
      "    }\n" +
      "    return true;\n" +  
      "  }\n";  
    return res; 
  } 

  public static String generateIsUniqueOpJava7()
  { String res = 
      "  public static <T> boolean isUnique(Collection<T> evals)\n" +
      "  { HashSet<T> vals = new HashSet<T>(); \n" +
      "    for (T ob : evals)\n" + 
      "    { if (vals.contains(ob)) { return false; }\n" + 
      "      vals.add(ob);\n" + 
      "    }\n" +
      "    return true;\n" +  
      "  }\n";  
    return res; 
  } 

  public static String generateIsUniqueOpCSharp()
  { String res = 
      "  public static bool isUnique(ArrayList evals)\n" +
      "  { ArrayList vals = new ArrayList(); \n" +
      "    for (int i = 0; i < evals.Count; i++)\n" + 
      "    { object ob = evals[i]; \n" +
      "      if (vals.Contains(ob)) { return false; }\n" + 
      "      vals.Add(ob);\n" + 
      "    }\n" +
      "    return true;\n" +  
      "  }\n";  
    return res; 
  } 

  public static String generateIsUniqueOpCPP()
  { String res = 
      "  static bool isUnique(vector<_T>* evals)\n" +
      "  { std::set<_T> vals; \n" +
      "    for (int i = 0; i < evals->size(); i++)\n" + 
      "    { _T ob = (*evals)[i]; \n" +
      "      if (vals.find(ob) != vals.end()) { return false; }\n" + 
      "      vals.insert(ob);\n" + 
      "    }\n" +
      "    return true;\n" +  
      "  }\n";  
    res = res + "  static bool isUnique(std::set<_T>* evals)\n" +
      "  { return true; }\n";  
    return res; 
  } 

  public static String generateByte2CharOp()
  { String res = " public static String byte2char(int b)\n" + 
      "  { try { byte[] bb = {(byte) b}; \n" + 
      "      return new String(bb); }\n" + 
      "    catch (Exception _e)\n" + 
      "    { return \"\"; }\n" + 
      "  }\n\n"; 
    res = res + "  public static int char2byte(String s)\n" +
      "  { if (s == null || s.length() == 0)\n" +
      "    { return -1; } \n" +
      "    return (int) s.charAt(0);\n" +  
      "  }\n\n";

    return res; 
  } 

  public static String generateByte2CharOpCPP()
  { String res = "  static string byte2char(int b)\n" + 
      "  { int arr[] = {0};\n" +  
      "    arr[0] = b;\n" + 
      "    string str = string((char*) arr);\n" +  
      "    return str;\n" + 
      "  }\n\n"; 
    res = res + 
      "  static int char2byte(string str)\n" + 
      "  { if (str.length() == 0)\n" + 
      "    { return -1; } \n" + 
      "    char x = str[0];\n" + 
      "    return (int) x;\n" + 
      "  } \n"; 
    return res; 
  } 

  public static String generateIsIntegerOp()
  { String res = 
      " public static boolean isInteger(String str)\n" + 
      "  { try { Integer.parseInt(str.trim()); return true; }\n" + 
      "    catch (Exception _e) { return false; }\n" + 
      "  }\n\n"; 

    res = res + 
      " public static int toInt(String str)\n" + 
      "  { /* Trim leading 0's */\n" +  
      "    if (str == null || str.length() == 0)\n" + 
      "    { return 0; }\n" + 
      "    String trm = str.trim();\n" + 
      "    while (trm.length() > 0 && trm.charAt(0) == '0')\n" +
      "    { trm = trm.substring(1); }\n" +
      "    if (trm.indexOf(\".\") > 0)\n" +
      "    { trm = trm.substring(0,trm.indexOf(\".\")); }\n" +  
      "    try { int x = Integer.parseInt(trm.trim());\n" + 
      "          return x; }\n" + 
      "    catch (Exception _e) { return 0; }\n" + 
      "  }\n\n"; 

    res = res + 
      "  public static int toInteger(String str)\n" +
      "  { if (str == null || str.length() == 0)\n" + 
      "    { return 0; }\n" + 
      "    String trm = str.trim();\n" + 
      "    while (trm.length() > 0 && trm.charAt(0) == '0')\n" +
      "    { trm = trm.substring(1); }\n" + 
      "    if (trm.indexOf(\".\") > 0)\n" +
      "    { trm = trm.substring(0,trm.indexOf(\".\")); }\n" + 
      "    try { int x = Integer.decode(trm).intValue();\n" + 
      "      return x; \n" +
      "    }\n" +
      "    catch (Exception _e) { return 0; }\n" +
      "  }\n\n"; 

    return res; 
  } 

  public static String generateIsLongOp()
  { String res = " public static boolean isLong(String str)\n" + 
      "  { try { Long.parseLong(str.trim()); return true; }\n" + 
      "    catch (Exception _e) { return false; }\n" + 
      "  }\n\n"; 
    res = res + " public static long toLong(String str)\n" + 
      "  { try { long x = Long.parseLong(str.trim());\n" + 
      "          return x; }\n" + 
      "    catch (Exception _e) { return 0; }\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateIsRealOp()
  { String res = " public static boolean isReal(String str)\n" + 
      "  { try { double d = Double.parseDouble(str.trim()); \n" + 
      "          if (Double.isNaN(d)) { return false; }\n" + 
      "          return true; }\n" + 
      "    catch (Exception _e) { return false; }\n" + 
      "  }\n\n"; 
    res = res + " public static double toDouble(String str)\n" + 
      "  { try { double x = Double.parseDouble(str.trim());\n" + 
      "          return x; }\n" + 
      "    catch (Exception _e) { return 0; }\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateIsIntegerOpCSharp()
  { String res = " public static bool isInteger(string str)\n" + 
      "  { try { int.Parse(str); return true; }\n" + 
      "    catch (Exception _e) { return false; }\n" + 
      "  }\n\n"; 

    res = res + 
      " public static int toInt(string str)\n" + 
      "  { /* Trim leading 0's */\n" +  
      "    if (str == null || str.Length == 0)\n" + 
      "    { return 0; }\n" + 
      "    string trm = str.Trim();\n" + 
      "    while (trm.Length > 0 && trm[0] == '0')\n" +
      "    { trm = trm.Substring(1); }\n" + 
      "    try { int x = int.Parse(trm.Trim());\n" + 
      "          return x; }\n" + 
      "    catch (Exception _e) { return 0; }\n" + 
      "  }\n\n"; 

    return res; 
  } 

  public static String generateIsLongOpCSharp()
  { String res = " public static bool isLong(string str)\n" + 
      "  { try { long.Parse(str); return true; }\n" + 
      "    catch (Exception _e) { return false; }\n" + 
      "  }\n\n"; 

    /* res = res + " public static long toLong(String str)\n" + 
      "  { try { long x = long.Parse(str.Trim());\n" + 
      "          return x; }\n" + 
      "    catch (Exception _e) { return 0; }\n" + 
      "  }\n"; */ 

    return res; 
  } 

  public static String generateIsRealOpCSharp()
  { String res = " public static bool isReal(string str)\n" + 
      "  { try { double d = double.Parse(str); \n" + 
      "          if (Double.IsNaN(d)) { return false; }\n" + 
      "          return true; }\n" + 
      "    catch (Exception __e) { return false; }\n" + 
      "  }\n\n";

    res = res + " public static double toDouble(String str)\n" + 
      "  { try { double x = double.Parse(str.Trim());\n" + 
      "          return x; }\n" + 
      "    catch (Exception _e) { return 0; }\n" + 
      "  }\n"; 
 
    return res; 
  } 

  public static String generateIsIntegerOpCPP()
  { String res = " static bool isInteger(string str)\n" + 
      "  { try { std::stoi(str); return true; }\n" + 
      "    catch (exception _e) { return false; }\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateIsLongOpCPP()
  { String res = " static bool isLong(string str)\n" + 
      "  { try { std::stol(str); return true; }\n" + 
      "    catch (exception _e) { return false; }\n" + 
      "  }\n"; 
    return res; 
  } 

  public static String generateIsRealOpCPP()
  { String res = " static bool isReal(string str)\n" + 
      "  { try { std::stod(str); return true; }\n" + 
      "    catch (exception _e)\n" + 
      "    { return false; }\n" + 
      "  }\n\n"; 

    res = res + "  static bool toBoolean(string str)\n" +
                "  { if (\"true\" == str || \"1\" == str)\n" +
                "    { return true; }\n" +
                "    return false; \n" +
                "  }\n\n"; 

    res = res + 
       "  static int toInteger(string str)\n" +
       "  { if (str.length() == 0)\n" +
       "    { return 0; }\n" +
       "\n" +
       "    if (str[0] == '0' && str.length() > 1 && str[1] == 'x')\n" +
       "    { try {\n" +
       "        int x = std::stoi(str, 0, 16);\n" + 
       "        return x; \n" +
       "      }\n" +
       "      catch (exception e) { return 0; }\n" +
       "    }\n" +
       "    else if (str[0] == '0' && str.length() > 1)\n" +
       "    { try { \n" +
       "        int y = std::stoi(str, 0, 8);\n" + 
       "        return y;\n" +
       "      } catch (exception f)\n" +
       "        { return 0; }\n" +
       "    }  \n" +
       "    try { int z = std::stoi(str, 0, 10);\n" + 
       "          return z;\n" +
       "    } \n" +
       "    catch (exception g) { return 0; } \n" +
       "    return 0;\n" +
       "  }\n\n";

     res = res + 
       "  static long toLong(string str)\n" +
       "  { if (str.length() == 0)\n" +
       "    { return 0; }\n" +
       "\n" +
       "    if (str[0] == '0' && str.length() > 1 && str[1] == 'x')\n" +
       "    { try {\n" +
       "        long x = std::stol(str, 0, 16);\n" + 
       "        return x; \n" +
       "      }\n" +
       "      catch (exception e) { return 0; }\n" +
       "    }\n" +
       "    else if (str[0] == '0' && str.length() > 1)\n" +
       "    { try { \n" +
       "        long y = std::stol(str, 0, 8);\n" + 
       "        return y;\n" +
       "      } catch (exception f)\n" +
       "        { return 0; }\n" +
       "    }  \n" +
       "    try { long z = std::stol(str, 0, 10);\n" + 
       "          return z;\n" +
       "    } \n" +
       "    catch (exception g) { return 0; } \n" +
       "    return 0;\n" +
       "  }\n\n";
    res = res + 
       "  static double toReal(string str)\n" +
       "  { if (str.length() == 0)\n" +
       "    { return 0.0; }\n" +
       "\n" +
       "    try {\n" +
       "      double x = std::stod(str);\n" + 
       "      return x; \n" +
       "    }\n" +
       "    catch (exception e) { return 0.0; }\n" +
       "  }\n\n"; 

    return res; 
  } 

  public static String generateIncludesAllMapOp()
  { String res = "  public static boolean includesAllMap(Map sup, Map sub)\n" + 
      "  { Vector keys = new Vector();\n" +
      "    keys.addAll(sub.keySet());\n" +
      "  \n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object key = keys.get(x);\n" +
      "      if (sup.containsKey(key))\n" +
      "      { if (sub.get(key).equals(sup.get(key)))\n" +
      "        {}\n" +
      "        else\n" +
      "        { return false; }\n" +       
      "      }\n" +
      "      else \n" +
      "      { return false; }\n" +
      "    }    \n" +
      "    return true;\n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateExcludesAllMapOp()
  { String res =  
      "  public static boolean excludesAllMap(Map sup, Map sub)\n" + 
      "  { Vector keys = new Vector();\n" +
      "    keys.addAll(sub.keySet());\n" +
      "  \n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object key = keys.get(x);\n" +
      "      if (sup.containsKey(key))\n" +
      "      { if (sub.get(key).equals(sup.get(key)))\n" +
      "        { return false; }\n" +
      "      }\n" +
      "    }    \n" +
      "    return true;\n" +
      "  }\n"; 
    return res; 
  } 


  public static String generateIncludingMapOp()
  { String res =  
      "  public static HashMap includingMap(Map m, Object src, Object trg) \n" +
      "  { HashMap copy = new HashMap();\n" +
      "    copy.putAll(m); \n" +
      "    copy.put(src,trg);\n" +
      "    return copy;\n" +
      "  } \n"; 
    return res; 
  }  

  public static String generateExcludeAllMapOp()
  { String res =  
      "  public static HashMap excludeAllMap(Map m1, Map m2)  \n" +
      "  { // m1 - m2 \n" +
      "    Vector keys = new Vector(); \n" +
      "    keys.addAll(m1.keySet()); \n" +
      "    HashMap res = new HashMap(); \n" +
      "   \n" +
      "    for (int x = 0; x < keys.size(); x++) \n" +
      "    { Object key = keys.get(x); \n" +
      "      if (m2.containsKey(key)) \n" +
      "      { } \n" +
      "      else \n" +
      "      { res.put(key,m1.get(key));  } \n" +
      "    }     \n" +
      "    return res; \n" +
      "  } \n"; 
    return res; 
  } 

  public static String generateExcludingMapKeyOp()
  { String res =  
      "  public static HashMap excludingMapKey(Map m, Object k)   \n" +
      "  { // m - { k |-> m(k) }   \n" +
      "    HashMap res = new HashMap();  \n" +
      "    res.putAll(m);  \n" +
      "    res.remove(k);  \n" +
      "    return res;  \n" +
      "  }  \n"; 
    return res; 
  } 

  public static String generateExcludingMapValueOp()
  { String res =  
      "  public static HashMap excludingMapValue(Map m, Object v) \n" +
      "  { // m - { k |-> v } \n" +
      "    Vector keys = new Vector();\n" +
      "    keys.addAll(m.keySet());\n" +
      "    HashMap res = new HashMap();\n" +
      "  \n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object key = keys.get(x);\n" +
      "      if (v.equals(m.get(key)))\n" +
      "      { }\n" +
      "      else\n" +
      "      { res.put(key,m.get(key));  }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateUnionMapOp()
  { String res =  
      "  public static HashMap unionMap(Map m1, Map m2) \n" +
      "  { HashMap res = new HashMap();\n" +
      "    res.putAll(m1);\n" +
      "    res.putAll(m2);    \n" +
      "    return res;\n" + 
      "  }\n";   
    return res; 
  } 

  public static String generateIntersectionMapOp()
  { String res =  
      "  public static HashMap intersectionMap(Map m1, Map m2) \n" +
      "  { Vector keys = new Vector();\n" +
      "    keys.addAll(m1.keySet());\n" +
      "    HashMap res = new HashMap();\n" +
      "  \n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object key = keys.get(x);\n" +
      "      if (m2.containsKey(key) && m1.get(key) != null && m1.get(key).equals(m2.get(key)))\n" +
      "      { res.put(key,m1.get(key));  }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res + 
      "  public static HashMap intersectAllMap(List col)\n" +
      "  { HashMap res = new HashMap();\n" + 
      "    if (col.size() == 0) \n" +
      "    { return res; } \n" +
      "\n" +
      "    Map m0 = (Map) col.get(0);\n" +
      "    res.putAll(m0); \n" +
      "\n" +
      "    for (int i = 1; i < col.size(); i++)\n" +
      "    { Map m = (Map) col.get(i);\n" + 
      "      res = Set.intersectionMap(res,m);\n" + 
      "    }\n" + 
      "    return res; \n" +
      "  } \n\n";

	res = res + 
      "  public static HashMap restrictMap(Map m1, Vector ks) \n" +
      "  { Vector keys = new Vector();\n" +
      "    keys.addAll(m1.keySet());\n" +
      "    HashMap res = new HashMap();\n" +
      "  \n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object key = keys.get(x);\n" +
      "      if (ks.contains(key))\n" +
      "      { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 
	res = res + 
      "  public static HashMap antirestrictMap(Map m1, Vector ks) \n" +
      "  { Vector keys = new Vector();\n" +
      "    keys.addAll(m1.keySet());\n" +
      "    HashMap res = new HashMap();\n" +
      "  \n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object key = keys.get(x);\n" +
      "      if (ks.contains(key)) { }\n" +
      "      else { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 
	res = res + 
      "  public static boolean includesKey(Map m, Object key) \n" +
      "  { Object val = m.get(key); \n" + 
      "    if (val == null) { return false; }\n" +
      "    return true;\n" +
      "  }\n\n"; 
    res = res + 
      "  public static boolean excludesKey(Map m, Object key) \n" +
      "  { Object val = m.get(key); \n" + 
      "    if (val == null) { return true; }\n" +
      "    return false;\n" +
      "  }\n\n"; 
	res = res + 
      "  public static boolean includesValue(Map m, Object val) \n" +
      "  { Vector keys = new Vector();\n" +
      "    keys.addAll(m.keySet());\n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object v = m.get(x);\n" +
      "      if (v != null && v.equals(val))\n" +
      "      { return true;  }\n" +
      "    }    \n" +
      "    return false;\n" +
      "  }\n\n"; 
    res = res + 
      "  public static boolean excludesValue(Map m, Object val) \n" +
      "  { Vector keys = new Vector();\n" +
      "    keys.addAll(m.keySet());\n" +
      "    for (int x = 0; x < keys.size(); x++)\n" +
      "    { Object v = m.get(x);\n" +
      "      if (v != null && v.equals(val))\n" +
      "      { return false;  }\n" +
      "    }    \n" +
      "    return true;\n" +
      "  }\n\n"; 
    return res; 
  } 

  public static String generateIncludingMapOpJava7()
  { String res = "  public static <D,R> HashMap<D,R> includingMap(HashMap<D,R> m, D src, R trg)\n" + 
      "  { HashMap<D,R> copy = new HashMap<D,R>();\n" + 
      "    copy.putAll(m); \n" +
      "    copy.put(src,trg);\n" +
      "    return copy;\n" +
      "  } \n\n";
    res = res + 
      "  public static <D,R> TreeMap<D,R> includingMap(TreeMap<D,R> m, D src, R trg)\n" + 
      "  { TreeMap<D,R> copy = (TreeMap<D,R>) m.clone();\n" + 
      "    copy.put(src,trg);\n" +
      "    return copy;\n" +
      "  } \n";
    return res; 
  } 

  public static String generateIncludesAllMapOpJava7()
  { String res = "  public static <D,R> boolean includesAllMap(Map<D,R> sup, Map<D,R> sub)\n" + 
      "  { Set<D> keys = sub.keySet();\n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (sup.containsKey(key))\n" +
      "      { if (sub.get(key).equals(sup.get(key)))\n" +
      "        {}\n" +
      "        else\n" +
      "        { return false; }\n" +       
      "      }\n" +
      "      else \n" +
      "      { return false; }\n" +
      "    }    \n" +
      "    return true;\n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateExcludesAllMapOpJava7()
  { String res =  
      "  public static <D,R> boolean excludesAllMap(Map<D,R> sup, Map<D,R> sub)\n" + 
      "  { Set<D> keys = sub.keySet();\n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (sup.containsKey(key))\n" +
      "      { if (sub.get(key).equals(sup.get(key)))\n" +
      "        { return false; }\n" +
      "      }\n" +
      "    }    \n" +
      "    return true;\n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateExcludeAllMapOpJava7()
  { String res = 
      "  public static <D,R> HashMap<D,R> excludeAllMap(HashMap<D,R> m1, Map m2)\n" + 
      "  { // m1 - m2\n" +
      "    HashMap<D,R> res = new HashMap<D,R>();\n" +
      "    Set<D> keys = m1.keySet(); \n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (m2.containsKey(key))\n" +
      "      { }\n" +
      "      else\n" +
      "      { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n";

    res = res + 
      "  public static <D,R> TreeMap<D,R> excludeAllMap(TreeMap<D,R> m1, Map m2)\n" + 
      "  { // m1 - m2\n" +
      "    TreeMap<D,R> res = new TreeMap<D,R>();\n" +
      "    Set<D> keys = m1.keySet(); \n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (m2.containsKey(key))\n" +
      "      { }\n" +
      "      else\n" +
      "      { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateExcludingMapKeyOpJava7()
  { String res = 
      "  public static <D,R> HashMap<D,R> excludingMapKey(HashMap<D,R> m, D k)\n" + 
      "  { // m - { k |-> m(k) } \n" +
      "    HashMap<D,R> res = new HashMap<D,R>();\n" +
      "    res.putAll(m);\n" +
      "    res.remove(k);\n" +
      "    return res;\n" +
      "  }\n\n";

    res = res +  
      "  public static <D,R> TreeMap<D,R> excludingMapKey(TreeMap<D,R> m, D k)\n" + 
      "  { // m - { k |-> m(k) } \n" +
      "    TreeMap<D,R> res = (TreeMap<D,R>) m.clone();\n" +
      "    res.remove(k);\n" +
      "    return res;\n" +
      "  }\n";
    return res; 
  }  

  public static String generateExcludingMapValueOpJava7()
  { String res = 
      "  public static <D,R> HashMap<D,R> excludingMapValue(HashMap<D,R> m, R v)\n" + 
      "  { // m - { k |-> v }\n" + 
      "    HashMap<D,R> res = new HashMap<D,R>();\n" +
      "    Set<D> keys = m.keySet(); \n" +
      "    \n" +
      "    for (D key : keys)\n" +
      "    { if (v.equals(m.get(key)))\n" +
      "      { }\n" +
      "      else\n" +
      "      { res.put(key,m.get(key));  }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res +
      "  public static <D,R> TreeMap<D,R> excludingMapValue(TreeMap<D,R> m, R v)\n" + 
      "  { // m - { k |-> v }\n" + 
      "    TreeMap<D,R> res = new TreeMap<D,R>();\n" +
      "    Set<D> keys = m.keySet(); \n" +
      "    \n" +
      "    for (D key : keys)\n" +
      "    { if (v.equals(m.get(key)))\n" +
      "      { }\n" +
      "      else\n" +
      "      { res.put(key,m.get(key));  }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n"; 
    return res; 
  } 

  public static String generateUnionMapOpJava7()
  { String res = 
      "  public static <D,R> HashMap<D,R> unionMap(HashMap<D,R> m1, Map<D,R> m2)\n" + 
      "  { HashMap<D,R> res = new HashMap<D,R>();\n" +
      "    res.putAll(m1);\n" +
      "    res.putAll(m2);    \n" +
      "    return res;\n" +
      "  }\n\n"; 

    res = res +
      "  public static <D,R> TreeMap<D,R> unionMap(TreeMap<D,R> m1, Map<D,R> m2)\n" + 
      "  { TreeMap<D,R> res = (TreeMap<D,R>) m1.clone();\n" +
      "    res.putAll(m2);    \n" +
      "    return res;\n" +
      "  }\n"; 

    return res; 
  } 

  public static String generateIntersectionMapOpJava7()
  { String res = 
      "  public static <D,R> HashMap<D,R> intersectionMap(HashMap<D,R> m1, Map m2)\n" + 
      "  { HashMap<D,R> res = new HashMap<D,R>();\n" +
      "    Set<D> keys = m1.keySet(); \n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (m2.containsKey(key) && m1.get(key) != null && m1.get(key).equals(m2.get(key)))\n" +
      "      { res.put(key,m1.get(key));  }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n";

    res = res + 
      "  public static <D,R> TreeMap<D,R> intersectionMap(TreeMap<D,R> m1, Map m2)\n" + 
      "  { TreeMap<D,R> res = new TreeMap<D,R>();\n" +
      "    Set<D> keys = m1.keySet(); \n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (m2.containsKey(key) && m1.get(key) != null && m1.get(key).equals(m2.get(key)))\n" +
      "      { res.put(key,m1.get(key));  }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n";

    res = res + 
      "  public static HashMap intersectAllMap(Collection col)\n" +
      "  { HashMap res = new HashMap();\n" + 
      "    if (col.size() == 0) \n" +
      "    { return res; } \n" +
      "\n" +
      "    Map m0 = (Map) Ocl.any(col);\n" +
      "    res.putAll(m0); \n" +
      "\n" +
      "    for (Object obj : col)\n" +
      "    { Map m = (Map) obj;\n" + 
      "      res = Ocl.intersectionMap(res,m);\n" + 
      "    }\n" + 
      "    return res; \n" +
      "  } \n\n";


	res = res + 
	  "  public static <D,R> HashMap<D,R> restrictMap(HashMap<D,R> m1, Collection<D> ks) \n" +
      "  { Set<D> keys = new HashSet<D>();\n" +
      "    keys.addAll(m1.keySet());\n" +
      "    HashMap<D,R> res = new HashMap<D,R>();\n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (ks.contains(key))\n" +
      "      { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 

	res = res + 
	  "  public static <D,R> TreeMap<D,R> restrictMap(TreeMap<D,R> m1, Collection<D> ks) \n" +
      "  { Set<D> keys = new HashSet<D>();\n" +
      "    keys.addAll(m1.keySet());\n" +
      "    TreeMap<D,R> res = new TreeMap<D,R>();\n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (ks.contains(key))\n" +
      "      { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 
 
      res = res + 
      "  public static <D,R> HashMap<D,R> antirestrictMap(HashMap<D,R> m1, Collection<D> ks) \n" +
      "  { Set<D> keys = new HashSet<D>();\n" +
      "    keys.addAll(m1.keySet());\n" +
      "    HashMap<D,R> res = new HashMap<D,R>();\n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (ks.contains(key)) { }\n" +
      "      else \n" + 
      "      { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 

      res = res + 
      "  public static <D,R> TreeMap<D,R> antirestrictMap(TreeMap<D,R> m1, Collection<D> ks) \n" +
      "  { Set<D> keys = new HashSet<D>();\n" +
      "    keys.addAll(m1.keySet());\n" +
      "    TreeMap<D,R> res = new TreeMap<D,R>();\n" +
      "  \n" +
      "    for (D key : keys)\n" +
      "    { if (ks.contains(key)) { }\n" +
      "      else \n" + 
      "      { res.put(key,m1.get(key)); }\n" +
      "    }    \n" +
      "    return res;\n" +
      "  }\n\n"; 

      res = res + 
      "  public static <D,R> HashMap<D,R> selectMap(HashMap<D,R> m, Predicate<R> f)\n" +
      "  { HashMap<D,R> result = new HashMap<D,R>();\n" +
      "    Set<D> keys = m.keySet();\n" +
      "    for (D k : keys)\n" +
      "    { R value = m.get(k);\n" +
      "      if (f.test(value))\n" +
      "      { result.put(k,value); }\n" +
      "    }\n" +
      "    return result;\n" +
      "  }\n\n"; 

      res = res + 
      "  public static <D,R> TreeMap<D,R> selectMap(TreeMap<D,R> m, Predicate<R> f)\n" +
      "  { TreeMap<D,R> result = new TreeMap<D,R>();\n" +
      "    Set<D> keys = m.keySet();\n" +
      "    for (D k : keys)\n" +
      "    { R value = m.get(k);\n" +
      "      if (f.test(value))\n" +
      "      { result.put(k,value); }\n" +
      "    }\n" +
      "    return result;\n" +
      "  }\n\n"; 

      res = res + 
      "  public static <D,R> HashMap<D,R> rejectMap(HashMap<D,R> m, Predicate<R> f)\n" +
      "  { HashMap<D,R> result = new HashMap<D,R>();\n" +
      "    Set<D> keys = m.keySet();\n" +
      "    for (D k : keys)\n" +
      "    { R value = m.get(k);\n" +
      "      if (f.test(value)) {}\n" +
      "      else\n" +
      "      { result.put(k,value); }\n" +
      "    }\n" +
      "    return result;\n" +
      "  }\n\n"; 

      res = res + 
      "  public static <D,R> TreeMap<D,R> rejectMap(TreeMap<D,R> m, Predicate<R> f)\n" +
      "  { TreeMap<D,R> result = new TreeMap<D,R>();\n" +
      "    Set<D> keys = m.keySet();\n" +
      "    for (D k : keys)\n" +
      "    { R value = m.get(k);\n" +
      "      if (f.test(value)) {}\n" +
      "      else\n" +
      "      { result.put(k,value); }\n" +
      "    }\n" +
      "    return result;\n" +
      "  }\n\n"; 

      res = res +
      "  public static <D,R,T> HashMap<D,T> collectMap(Map<D,R> m, Function<R,T> _f)\n" +
      "  { HashMap<D,T> result = new HashMap<D,T>();\n" + 
      "    Set<D> keys = m.keySet();\n" +
      "    for (D k : keys)\n" +
      "    { R value = m.get(k);\n" +
      "      result.put(k, _f.apply(value));\n" + 
      "    }\n" +
      "    return result;\n" + 
      "  }\n\n";  

    return res; 
  } 

  public static String cppExceptionClass(
    String exceptionName, String oclname, String exceptionText)
  { String res = "  class " + exceptionName + " : exception\n" +
      "  { public: \n" +
      "      " + exceptionName + "() : message(" + exceptionText + ") { }\n" +
      "\n" +
      "      " + exceptionName + "* copy" + exceptionName + "(" + exceptionName + "* self)\n" +
      "      { " + exceptionName + "* ex = new " + exceptionName + "();\n" +
      "        ex->message = self->message;\n" + 
      "        return ex; \n" +
      "      }\n" +
      "\n" +
      "      const char* what() const { return message; }\n" +
      "\n" +
      "      string getMessage() { return string(message); }\n" +
      "\n" + 
      "      void printStackTrace()\n" +  
      "      { cout << what() << endl; }\n" + 
      "\n" +  
      "      exception* getCause()\n" +  
      "      { return this; }\n" + 
      "\n" +  
      "      static " + exceptionName + "* new" + oclname + "(string m)\n" + 
      "      { " + exceptionName + "* res = new " + exceptionName + "();\n" +  
      "        res->message = m.c_str();\n" +  
      "        return res;\n" +  
      "      }\n" + 
      "   private: \n" +
      "     const char* message;\n" +
      "  };\n\n"; 
    return res; 
  }


  public static String exceptionsCPP()
  { String exceptionName = "io_exception"; 
    String exceptionText = "\"IO exception\""; 
    String oclName = "IOException"; 

    String res = cppExceptionClass(exceptionName, oclName, exceptionText); 

    exceptionName = "null_access_exception"; 
    exceptionText = "\"Null access exception\"";
    oclName = "NullAccessException";  
    res = res + cppExceptionClass(exceptionName, oclName, exceptionText); 

    exceptionName = "assertion_exception"; 
    exceptionText = "\"Assertion exception\"";
    oclName = "AssertionException";  
    res = res + cppExceptionClass(exceptionName, oclName, exceptionText); 

    exceptionName = "accessing_exception"; 
    exceptionText = "\"Accessing exception\"";
    oclName = "AccessingException";  
    res = res + cppExceptionClass(exceptionName, oclName, exceptionText); 

    return res; 
  }

  public static void generateLibraryCSharp(String lib, 
                                 PrintWriter out)
  { // retrieve library code from libraries/lib.cs & print 
    // to out. 

    try
    { File libCPP = new File("libraries/" + lib + ".cs"); 
      BufferedReader br = null;
      String sline = null;
      boolean eof = false; 
      br = new BufferedReader(new FileReader(libCPP));
      out.println(); 
 
      while (!eof)
      { sline = br.readLine();
        if (sline == null) 
        { eof = true; } 
        else 
        { out.println(sline); }
      } 
      out.println(); 
      br.close();  
    } 
    catch (IOException _ex)
    { System.err.println("!! ERROR: libraries/" + lib + ".cs not found"); }
  } 

  public static void generateLibraryJava7(String lib, 
                                 PrintWriter out)
  { // retrieve library code from libraries/lib.java & print 
    // to out. 

    try
    { File libCPP = new File("libraries/" + lib + ".java"); 
      BufferedReader br = null;
      String sline = null;
      boolean eof = false; 
      br = new BufferedReader(new FileReader(libCPP));
      out.println(); 
 
      while (!eof)
      { sline = br.readLine();
        if (sline == null) 
        { eof = true; } 
        else 
        { out.println(sline); }
      } 
      out.println(); 
      br.close();  
    } 
    catch (IOException _ex)
    { System.err.println("!! ERROR: libraries/" + lib + ".java not found"); }
  } 

  public static void generateLibraryCPP(String lib, 
                                 PrintWriter out)
  { // retrieve library code from libraries/lib.cpp & print 
    // to out. 

    try
    { File libCPP = new File("libraries/" + lib + ".cpp"); 
      BufferedReader br = null;
      String sline = null;
      boolean eof = false; 
      br = new BufferedReader(new FileReader(libCPP));
      out.println(); 
 
      while (!eof)
      { sline = br.readLine();
        if (sline == null) 
        { eof = true; } 
        else 
        { out.println(sline); }
      } 
      out.println(); 
      br.close();  
    } 
    catch (IOException _ex)
    { System.err.println("!! ERROR: libraries/" + lib + ".cpp not found"); }
  } 

  public static void generateLibraryHPP(String lib, 
                                 PrintWriter out)
  { // retrieve library code from libraries/lib.hpp & print 
    // to out. 

    try
    { File libHPP = new File("libraries/" + lib + ".hpp"); 
      BufferedReader br = null;
      String sline = null;
      boolean eof = false; 
      br = new BufferedReader(new FileReader(libHPP));
      out.println(); 
 
      while (!eof)
      { sline = br.readLine();
        if (sline == null) 
        { eof = true; } 
        else 
        { out.println(sline); }
      } 
      out.println(); 
      br.close();  
    } 
    catch (IOException _ex)
    { System.err.println("!! ERROR: libraries/" + lib + ".hpp not found"); }
  } 


  public static void main(String[] args)
  { Vector test = new Vector(); 
    // test.add(new Integer(1)); test.add(new Integer(-4)); 
    // test.add(new Integer(0));
    test.add("aaa"); test.add("abb"); test.add("aaa");  
    // System.out.println(isUnique(test)); 
    // System.out.println(test.notEmpty()); 
    // java.util.Collections.sort(test); 
    // System.out.println(test.count("aaa")); 
    // System.out.println(Math.pow(0.5,0.5)); 
    // System.out.println("ddd hhh".indexOf("d h")); 
    // System.out.println(Math.sin(Math.PI/4));
    // System.out.println(Math.cos(Math.PI/4));
    // System.out.println(Math.tan(Math.PI/4));
    // System.out.println(Math.log(0.1));
    // System.out.println(Math.exp(1));


    // System.out.println(Math.abs(-2.2)); 
    // System.out.println(Math.round(-2.2)); 
    // System.out.println(Math.floor(-2.2)); 

    // char[] chars = "abc".toCharArray(); 
    // Vector res = new Vector(); 
    // for (int i = 0; i < chars.length; i++) 
    // { res.add("" + 	1i]); } 
    // System.out.println(count("ssdd gghh","dd ")); 

    // if ("aaa".compareTo("aba") >= 0)  { System.out.println(1); } 
    // else { System.out.println(2); }
    // res.add(1,"p"); 
    // System.out.println(res);  

    int ind = "ansc~~tr".indexOf("~~"); 
    System.out.println("ansc~~tr".substring(0,ind)); 
    System.out.println("ansc~~tr".substring(ind+2,8)); 
  } 
}

