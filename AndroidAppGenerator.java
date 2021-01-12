import java.util.Vector; 
import java.io.*; 

/* Package: Mobile */ 
/******************************
* Copyright (c) 2003,2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class AndroidAppGenerator extends AppGenerator
{ 

  public void modelFacade(String packageName, Vector usecases, CGSpec cgs, Vector entities, Vector clouds, Vector types, 
                          int remoteCalls, boolean needsMap, PrintWriter out)
  { // String ename = e.getName();
    // Vector atts = e.getAttributes(); 
	
    // String evc = ename + "ViewController";
    // String evo = ename + "ValueObject";
    // String resvo = ename.toLowerCase() + "_vo";
    // String populateResult = createVOStatement(e,atts);
	
    boolean hasDbi = false; 
    if (entities.size() > 0)
    { hasDbi = true; }

    boolean hasCloud = false; 
    if (clouds.size() > 0)
    { hasCloud = true; }

    out.println("package " + packageName + ";");
    out.println(); 
    
    out.println("import android.content.Context;"); 
    out.println("import java.util.ArrayList;");
    out.println("import java.util.HashMap;");
    out.println("import java.util.List;");
    out.println("import java.util.Map;");
    out.println("");
    out.println("public class ModelFacade");

    if (remoteCalls > 0) 
    { out.println("  implements InternetCallback"); } 
	out.println("{ "); 
    
    if (hasDbi) 
    { out.println("  Dbi dbi; "); } 
    
	if (hasCloud)
    { out.println("  FirebaseDbi cdbi = FirebaseDbi.getInstance();"); }
	
    if (needsMap) 
	{ out.println("  MapLocation currentLocation;"); 
      out.println("  private MapsComponent mapDelegate;"); 
    }
	
		
    out.println("  FileAccessor fileSystem;"); 
    out.println("  Context myContext;"); 
    out.println("  static ModelFacade instance = null; ");
    out.println(); 
    out.println("  public static ModelFacade getInstance(Context context)"); 
    out.println("  { if (instance == null) "); 
    out.println("    { instance = new ModelFacade(context); }"); 
    out.println("    return instance;");  
    out.println("  }");  
    out.println(); 
    out.println(); 
	
    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i); 
      if (ent.isDerived()) { } 
      else 
      { String ename = ent.getName(); 
        out.println("  " + ename + "VO current" + ename + " = null;"); 
        out.println(); 
        out.println("  public List<" + ename + "VO> current" + 
		            ename + "s = new ArrayList<" + ename + "VO>();");
        out.println();  
      } 
    } 
	
    for (int i = 0; i < clouds.size(); i++) 
    { Entity ent = (Entity) clouds.get(i); 
      if (ent.isDerived()) { } 
      else 
      { String ename = ent.getName(); 
        out.println("  " + ename + "VO current" + ename + " = null;"); 
        out.println(); 
        out.println("  public List<" + ename + "VO> current" + 
		            ename + "s = new ArrayList<" + ename + "VO>();");
        out.println();  
      } 
    } 

    out.println("  private ModelFacade(Context context)"); 
    if (hasDbi) 
    { out.println("  { dbi = new Dbi(context); "); 
	  out.println("    myContext = context; "); 
	  out.println("    fileSystem = new FileAccessor(context); "); 
	  for (int i = 0; i < entities.size(); i++) 
	  { Entity ex = (Entity) entities.get(i); 
	    String ename = ex.getName(); 
        if (ex.isPersistent())
		{ out.println("    load" + ename + "();"); }
	  }
	  out.println("  }"); 
	}  
    else 
    { out.println("  { myContext = context; ");
	  out.println("    fileSystem = new FileAccessor(context); ");
	  out.println("  }");  
    } 
    out.println();
	
	if (needsMap)
	{ out.println("  public void setMapDelegate(MapsComponent m)"); 
      out.println("  { mapDelegate = m; }");
	  out.println();  
    } 
	
    // if e is persistent, include a Dbi
   
    // System.out.println("import Foundation");
    // System.out.println("import Glibc");
    // System.out.println("");
    // System.out.println("class ModelFacade");
    // System.out.println("{ ");

    /* String extractatts = "";
    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
	 Type atype = att.getType(); 
	 if (atype != null)
	 { extractatts = extractatts + "    var " + att + " : " + atype.getSwift() + " = vo." + att + "\n"; 
       } 
     } */ 

     // System.out.println(usecases); 

    for (int y = 0; y < usecases.size(); y++)
    { if (usecases.get(y) instanceof UseCase) 
      { UseCase uc = (UseCase) usecases.get(y);
        Vector pars = uc.getParameters(); 
        Attribute res = uc.getResultParameter(); 
        String partext = ""; 
        for (int i = 0; i < pars.size(); i++) 
        { Attribute par = (Attribute) pars.get(i);
          Type partype = par.getType();  
          partext = partext + partype.getJava8() + " " + par.getName(); 
          if (i < pars.size()-1)
          { partext = partext + ", "; } 
        }   
	  
	   String restype = "void"; 
	   if (res != null) 
       { restype = res.getType().getJava8(); } 
      
       out.println("  public " + restype + " " + uc.getName() + "(" + partext + ")"); 
      
       out.println("  { ");
       if (res != null) 
       { out.println("    " + res.getType().getJava8()  + " " + res.getName() + " = " + res.getType().getDefaultJava7() + ";"); 
	     if ("WebDisplay".equals(res.getType().getName()))
		 { out.println("    " + res.getName() + " = new WebDisplay();"); }
	     else if ("ImageDisplay".equals(res.getType().getName()))
		 { out.println("    " + res.getName() + " = new ImageDisplay();"); }
         else if ("GraphDisplay".equals(res.getType().getName()))
		 { out.println("    " + res.getName() + " = new GraphDisplay();"); }
	   }  

      // out.println(extractatts);
       String uccode = uc.cgActivity(cgs,entities,types);
	   cgs.displayText(uccode,out);
          
      // out.println(uc.cg(cgs)); 
      // System.out.println("  func " + uc.getName() + "(_ vo : " + evo + ") : " + evo);
      // System.out.println("  { ");
      // System.out.println(extractatts);

      // out.println(uc.cgActivity(cgs,entities,types));
      // out.println(populateResult);
      // out.println("   return " + resvo);
       if (res != null) 
       { out.println("    return result;"); } 

       out.println("  }");
       out.println(); 
		 
      // String act = uc.activity.cg(cgs); 
      // System.out.println(uc.cgActivity(cgs,entities,types));
      // System.out.println(populateResult);
      // System.out.println("   return " + resvo);
      // System.out.println("  }");
      }
    }
	
    for (int j = 0; j < entities.size(); j++) 
    { Entity ee = (Entity) entities.get(j); 
      if (ee.isDerived()) { continue; } 
	  if (ee.isComponent()) { continue; } 
	  
      Vector atts = ee.getAttributes(); 
	  String item = ee.getName(); 
      Attribute key = ee.getPrincipalPK();
	  if (key != null) 
	  { String entId = key.getName(); 
	  
  	    out.println("  public void load" + item + "()");
        out.println("  { ArrayList<" + item + "VO> _res = list" + item + "();");
        out.println("    for (" + item + "VO _x : _res)"); 
        out.println("    { " + item + " _ex = " + item + ".createByPK" + item + "(_x.get" + entId + "());"); 
        for (int k = 0; k < atts.size(); k++) 
        { Attribute att = (Attribute) atts.get(k); 
          String aname = att.getName();  
          out.println("      _ex." + aname + " = _x." + aname + ";"); 
        } 
        out.println("    }"); 
		out.println("  }"); 
		out.println(); 
      } 
	  
	  
	  out.println("  public List<" + item + "VO> list" + item + "()"); 
      out.println("  { current" + item + "s = dbi.list" + item + "();"); 
      out.println("    return current" + item + "s;"); 
      out.println("  }"); 
      out.println(); 

      out.println("  public List<String> stringList" + item + "()"); 
      out.println("  { current" + item + "s = dbi.list" + item + "();"); 
      out.println("    List<String> res = new ArrayList<String>();"); 
      out.println("    for (int x = 0; x < current" + item + "s.size(); x++)"); 
      out.println("    { " + item + "VO _item = (" + item + "VO) current" + item + "s.get(x);"); 
      out.println("      res.add(_item + \"\");"); 
      out.println("    }"); 
      out.println("    return res;"); 
      out.println("  }"); 
      out.println(); 

      String pk = "";  
      if (key != null) 
      { pk = key.getName(); 
        out.println("  public " + item + " get" + item + "ByPK(String _val)"); 
        out.println("  { ArrayList<" + item + "VO> _res = dbi.searchBy" + item + key + "(_val);"); 
        out.println("    if (_res.size() == 0)"); 
        out.println("    { return null; }"); 
        out.println("    else"); 
        out.println("    { " + item + "VO _vo = _res.get(0);"); 
        out.println("      " + item + " _itemx = " + item + ".createByPK" + item + "(_val);");
        for (int k = 0; k < atts.size(); k++) 
        { Attribute att = (Attribute) atts.get(k); 
          String aname = att.getName();  
          out.println("      _itemx." + aname + " = _vo." + aname + ";"); 
        } 
        out.println("      return _itemx;"); 
        out.println("    }"); 
        out.println("  }"); 
		out.println(); 
		out.println("  public " + item + " retrieve" + item + "(String _val)"); 
        out.println("  { return get" + item + "ByPK(_val); }"); 
        out.println();
        out.println("  public List<String> all" + item + "ids()"); 
        out.println("  { current" + item + "s = dbi.list" + item + "();"); 
        out.println("    List<String> res = new ArrayList<String>();"); 
        out.println("    for (int x = 0; x < current" + item + "s.size(); x++)"); 
        out.println("    { " + item + "VO _item = (" + item + "VO) current" + item + "s.get(x);"); 
        out.println("      res.add(_item." + key + " + \"\");"); 
        out.println("    }"); 
        out.println("    return res;"); 
        out.println("  }"); 
	   out.println(); 
      }  

      out.println("  public void setSelected" + item + "(" + item + "VO x)"); 
      out.println("  { current" + item + " = x; }"); 
      out.println(); 

      out.println("  public void setSelected" + item + "(int i)"); 
      out.println("  { if (i < current" + item + "s.size())"); 
      out.println("    { current" + item + " = current" + item + "s.get(i); }");
      out.println("  }"); 
      out.println(); 

      out.println("  public " + item + "VO getSelected" + item + "()"); 
      out.println("  { return current" + item + "; }"); 
      out.println(); 

      out.println("  public void persist" + item + "(" + item + " _x)"); 
      out.println("  { " + item + "VO _vo = new " + item + "VO(_x);"); 
	  out.println("    dbi.edit" + item + "(_vo); "); 
      out.println("    current" + item + " = _vo;"); 
      out.println("  }"); 
      out.println(); 

      out.println("  public void edit" + item + "(" + item + "VO _x)"); 
      out.println("  { dbi.edit" + item + "(_x); "); 
      out.println("    current" + item + " = _x;"); 
      out.println("  }"); 
      out.println(); 
	  
      out.println("  public void create" + item + "(" + item + "VO _x)"); 
      out.println("  { dbi.create" + item + "(_x);");  
      out.println("    current" + item + " = _x;"); 
      out.println("  }"); 
      out.println(); 
	  
      out.println("  public void delete" + item + "(String _id)"); 
      out.println("  { dbi.delete" + item + "(_id);");  
      out.println("    current" + item + " = null;"); 
      out.println("  }");
      out.println(); 

      for (int i = 0; i < atts.size(); i++) 
      { Attribute att = (Attribute) atts.get(i); 
        String attnme = att.getName(); 
        Type atttyp = att.getType(); 
        String typ = atttyp.getJava7(); 

	    out.println("  public List<" + item + "VO> searchBy" + item + attnme + "(String " + attnme + "x)"); 
        out.println("  { current" + item + "s = dbi.searchBy" + item + attnme + "(" + attnme + "x);"); 
        out.println("    return current" + item + "s;"); 
        out.println("  }"); 
        out.println(); 
      }  // It is always a string? 
    } 


    for (int j = 0; j < clouds.size(); j++) 
    { Entity ee = (Entity) clouds.get(j); 
      if (ee.isDerived()) { continue; } 
	  
	  String item = ee.getName(); 
	  String items = item.toLowerCase() + "s"; 
	  
	  out.println("  public List<" + item + "VO> list" + item + "()"); 
      out.println("  { List<" + item + "> " + items + " = " + item + "." + item + "_allInstances;"); 
      out.println("    current" + item + "s.clear();"); 
	  out.println("    for (int i = 0; i < " + items + ".size(); i++)"); 
	  out.println("    { current" + item + "s.add(new " + item + "VO(" + items + ".get(i))); }"); 
	  out.println("    return current" + item + "s;"); 
      out.println("  }"); 
      out.println(); 

      out.println("  public List<String> stringList" + item + "()"); 
      out.println("  { List<String> res = new ArrayList<String>();"); 
      out.println("    for (int x = 0; x < current" + item + "s.size(); x++)"); 
      out.println("    { " + item + "VO _item = (" + item + "VO) current" + item + "s.get(x);"); 
      out.println("      res.add(_item + \"\");"); 
      out.println("    }"); 
      out.println("    return res;"); 
      out.println("  }"); 
	  out.println(); 

      Attribute key = ee.getPrincipalPK();
	  String pk = "";  
      if (key != null) 
      { pk = key.getName(); 
	    Vector atts = ee.getAttributes(); 
        out.println("  public " + item + " get" + item + "ByPK(String _val)"); 
        out.println("  { return " + item + "." + item + "_index.get(_val); }"); 
        out.println();
		out.println("  public " + item + " retrieve" + item + "(String _val)"); 
        out.println("  { return get" + item + "ByPK(_val); }"); 
        out.println();
        out.println("  public List<String> all" + item + "ids()"); 
        out.println("  { List<String> res = new ArrayList<String>();"); 
        out.println("    for (int x = 0; x < current" + item + "s.size(); x++)"); 
        out.println("    { " + item + "VO _item = (" + item + "VO) current" + item + "s.get(x);"); 
        out.println("      res.add(_item." + key + " + \"\");"); 
        out.println("    }"); 
        out.println("    return res;"); 
        out.println("  }"); 
        out.println(); 
      }  

      out.println("  public void setSelected" + item + "(" + item + "VO x)"); 
      out.println("  { current" + item + " = x; }"); 
      out.println(); 

      out.println("  public void setSelected" + item + "(int i)"); 
      out.println("  { if (i < current" + item + "s.size())"); 
      out.println("    { current" + item + " = current" + item + "s.get(i); }");
      out.println("  }"); 
      out.println(); 

      out.println("  public " + item + "VO getSelected" + item + "()"); 
      out.println("  { return current" + item + "; }"); 
      out.println(); 

      out.println("  public void persist" + item + "(" + item + " _x)"); 
      out.println("  { " + item + "VO _vo = new " + item + "VO(_x);"); 
	  out.println("    cdbi.persist" + item + "(_x); "); 
      out.println("    current" + item + " = _vo;"); 
      out.println("  }"); 
      out.println(); 

      out.println("  public void edit" + item + "(" + item + "VO _x)"); 
      out.println("  { " + item + " _obj = get" + item + "ByPK(_x." + pk + ");"); 
	  out.println("    if (_obj == null)"); 
	  out.println("    { _obj = " + item + ".createByPK" + item + "(_x." + pk + "); }"); 
      Vector eatts = ee.getAttributes(); 
	  for (int z = 0; z < eatts.size(); z++) 
      { Attribute att = (Attribute) eatts.get(z); 
        String aname = att.getName();  
        out.println("    _obj." + aname + " = _x." + aname + ";"); 
      } 
      out.println("    cdbi.persist" + item + "(_obj);"); 
      out.println("    current" + item + " = _x;"); 
      out.println("  }"); 
      out.println(); 
	  
      out.println("  public void create" + item + "(" + item + "VO _x)"); 
      out.println("  { edit" + item + "(_x); }");  
      out.println(); 
	  
      out.println("  public void delete" + item + "(String _id)"); 
      out.println("  { " + item + " _obj = get" + item + "ByPK(_id);"); 
      out.println("    if (_obj != null)"); 
      out.println("    { cdbi.delete" + item + "(_obj); "); 
      out.println("      " + item + ".kill" + item + "(_id); "); 
      out.println("    }"); 
      out.println("    current" + item + " = null;"); 
      out.println("  }");
      out.println();   
    } 
    out.println("}");
    // System.out.println("}")
	
  }

  public String createVOStatement(Entity e, Vector atts)
  { String ename = e.getName();
    // String evc = ename + "ViewController";
    String vo = ename.toLowerCase() + "_vo";
    String evo = ename + "ValueObject";
    String attlist = "";
    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      String aname = att.getName();
      attlist = attlist + aname;
      if (x < atts.size()-1)
      { attlist = attlist + ","; }
    }

    String res = "    var " + vo + " : " + evo + " = " + evo + "(" + attlist + ")";
    return res;
  }

  public void singlePageApp(UseCase uc, String systemName, String image, CGSpec cgs, Vector types, Vector entities, PrintWriter out)
  { String ucname = uc.getName();
    
    Vector referencedEntities = new Vector(); 
    Vector persistentEntities = new Vector(); 

    for (int i = 0; i < entities.size(); i++) 
    { Entity ee = (Entity) entities.get(i); 
      if (ee.isDerived()) {}
      else if (ee.isPersistent())
      { persistentEntities.add(ee); } 
    } 
	
    Vector extensions = uc.extensionUseCases(); 
    String nme = uc.getName(); 
    Vector atts = uc.getParameters(); 
    Attribute res = uc.getResultParameter(); 
    String lcnme = nme.toLowerCase(); 

    Vector allucs = new Vector(); 
    allucs.add(uc); 
    allucs.addAll(extensions); 
    for (int j = 0; j < allucs.size(); j++) 
    { UseCase ucj = (UseCase) allucs.get(j); 
      if (ucj.classifier != null) 
      { if (referencedEntities.contains(ucj.classifier)) { } 
        else 
        { referencedEntities.add(ucj.classifier); } 
      } 
    } 

    File opfile = new File("output/" + systemName + "/src/main/res/layout/activity_main.xml"); 
    try
    { PrintWriter opout = new PrintWriter(
                                 new BufferedWriter(
                                   new FileWriter(opfile)));
      if (extensions.size() > 0) 
      { androidTableLayoutForOps(nme, ".MainActivity", image, atts, res, extensions, opout); } 
      else
      { androidTableLayoutForOp(nme, ".MainActivity", image,atts,res,opout); }   
      opout.close(); 
    } catch (Exception e) { } 

    File odact = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/MainActivity.java"); 
    try
    { PrintWriter actout = new PrintWriter(
                                   new BufferedWriter(
                                     new FileWriter(odact)));
      androidOpViewActivity(nme,systemName,"MainActivity","activity_main",atts,res,extensions,actout); 
      actout.close(); 
    } catch (Exception e) { }

    for (int j = 0; j < referencedEntities.size(); j++) 
    { Entity ent = (Entity) referencedEntities.get(j);
      ent.generateOperationDesigns(types,entities);  
           
      String entfile = ent.getName() + ".java"; 
      File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
      try
      { PrintWriter ffout = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
        ffout.println("package com.example." + systemName + ";"); 
        ffout.println(); 
        ffout.println("import java.util.*;"); 
        ffout.println("import java.util.HashMap;"); 
        ffout.println("import java.util.Collection;");
        ffout.println("import java.util.List;");
        ffout.println("import java.util.ArrayList;");
        ffout.println("import java.util.Set;");
        ffout.println("import java.util.HashSet;");
        ffout.println("import java.util.TreeSet;");
        ffout.println("import java.util.Collections;");
        ffout.println(); 
        // ent.generateJava7(entities,types,ffout);
        String entcode = ent.cg(cgs);
        cgs.displayText(entcode,ffout); 
		 
        ffout.close(); 
      } catch (Exception e) { } 
    } 
  }

  public void singlePageMapApp(UseCase uc, String systemName, String image, CGSpec cgs, Vector types, Vector entities, PrintWriter out)
  { 
    Vector referencedEntities = new Vector(); 
    Vector persistentEntities = new Vector(); 

    for (int i = 0; i < entities.size(); i++) 
    { Entity ee = (Entity) entities.get(i); 
      if (ee.isDerived()) {}
      else if (ee.isPersistent())
      { persistentEntities.add(ee); } 
    } 
	
    
    
    for (int j = 0; j < referencedEntities.size(); j++) 
    { Entity ent = (Entity) referencedEntities.get(j);
      ent.generateOperationDesigns(types,entities);  
           
      String entfile = ent.getName() + ".java"; 
      File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
      try
      { PrintWriter ffout = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
        ffout.println("package com.example." + systemName + ";"); 
        ffout.println(); 
        ffout.println("import java.util.*;"); 
        ffout.println("import java.util.HashMap;"); 
        ffout.println("import java.util.Collection;");
        ffout.println("import java.util.List;");
        ffout.println("import java.util.ArrayList;");
        ffout.println("import java.util.Set;");
        ffout.println("import java.util.HashSet;");
        ffout.println("import java.util.TreeSet;");
        ffout.println("import java.util.Collections;");
        ffout.println(); 
        // ent.generateJava7(entities,types,ffout);
        String entcode = ent.cg(cgs);
        cgs.displayText(entcode,ffout); 
		 
        ffout.close(); 
      } catch (Exception e) { } 
    } 
  }

  public void listViewController(Entity e, PrintWriter out)
  { String ename = e.getName();
    String evc = "list" + ename + "Activity";
    String evo = ename + "VO";
    String ebean = "ModelFacade";
    String bean = "model";
    Vector atts = e.getAttributes();
    String elist = ename.toLowerCase() + "List";
    String getlist = "list" + ename;
	String layoutname = "R.layout." + getlist.toLowerCase() + "_layout"; 

    out.println("package com.example.app;");
    out.println(); 
    out.println("import android.os.Bundle;");
    out.println("import android.app.ListActivity;");
    out.println("import android.view.View;");
    out.println("import android.widget.ArrayAdapter;");
    out.println("import android.widget.ListView;");
    out.println("import android.widget.TextView;");
    out.println("import java.util.ArrayList;");
    out.println("import java.util.List;");
    out.println();
    out.println("public class " + evc + " extends ListActivity");
    out.println("{ private ModelFacade model;"); 
    out.println();
    out.println("  ArrayList<" + evo + "> " + elist + ";"); 
    out.println();
    out.println("  @Override");
    out.println("  protected void onCreate(Bundle savedInstanceState)");
    out.println("  { super.onCreate(savedInstanceState);");
    out.println("    setContentView(" + layoutname + ");");
    out.println("    model = ModelFacade.getInstance(this);");
    out.println("    ArrayList<String> itemList = model.stringList" + ename + "();");
    out.println("    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemList);");
    out.println("    setListAdapter(adapter);");
    out.println("  }");
    out.println();
    
    out.println("  public void onListItemClick(ListView parent, View v, int position, long id)"); 
    out.println("  { model.setSelected" + ename + "(position); }");
    out.println();
    out.println("}");
  }

  public static void androidDeleteViewActivity(String op, Entity ent, PrintWriter out)
  { String ename = ent.getName();
    String fullop = op + ename; 
    String beanclass = ename + "Bean";
    String bean = beanclass.toLowerCase();
    String evo = ename + "VO"; 

    out.println("package com.example.app;\n"); 
    out.println(); 
    out.println("import android.os.Bundle;");
    out.println("import android.app.Activity;");
    out.println("import android.view.View;");
    out.println("import android.util.Log;"); 
    out.println("import android.widget.EditText;\n\r");
    out.println(); 

    out.println("public class View" + fullop + " extends Activity");
    out.println("{ " + beanclass + " " + bean + ";");
    out.println("  ModelFacade model = null;"); 
    out.println(); 

    Vector pars = new Vector();
    Attribute pk = ent.getPrincipalPrimaryKey(); 
    if (pk == null) 
    { out.println("/* ERROR: no primary key for " + ename + " */"); 
      out.println("}"); 
      return; 
    } 
    String pkname = pk.getName(); 
    pars.add(pk); 

    for (int x = 0; x < pars.size(); x++)
    { Attribute par = (Attribute) pars.get(x);
      String pnme = par.getName(); 
      String tfnme = pnme + "TextField"; 
      String dname = pnme + "Data";
      out.println("  EditText " + tfnme + ";");
      out.println("  String " + dname + " = \"\";");
    }
    out.println();
    out.println();
    out.println("  @Override");
    out.println("  protected void onCreate(Bundle bundle)");
    out.println("  { super.onCreate(bundle);");
    out.println("    setContentView(R.layout." + fullop + "_layout);");

    for (int x = 0; x < pars.size(); x++)
    { Attribute par = (Attribute) pars.get(x);
      String pnme = par.getName(); 
      String tfnme = pnme + "TextField"; 
      out.println("    " + tfnme + " = (EditText) findViewById(R.id." + fullop + pnme + ");");
    }
    out.println("    " + bean + " = new " + beanclass + "(this);");
    out.println("    model = ModelFacade.getInstance(this);"); 
    out.println("    " + evo + " _current = model.getSelected" + ename + "();"); 
    out.println("    if (_current != null)"); 
    out.println("    { " + pkname + "TextField.setText(_current." + pkname + "); }"); 
    out.println("  }\n\r");
    out.println(); 
  
    out.println("  public void " + fullop + "OK(View _v) ");
    out.println("  {");
    for (int x = 0; x < pars.size(); x++)
    { Attribute att = (Attribute) pars.get(x);
      String aname = att.getName();
      String tfnme = aname + "TextField"; 
      String dname = aname + "Data";
      out.println("    " + dname + " = " + tfnme + ".getText() + \"\";");
      out.println("    " + bean + ".set" + aname + "(" + dname + ");"); 
    }
    out.println("    if (" + bean + ".is" + fullop + "error())"); 
    out.println("    { Log.w(getClass().getName(), " + bean + ".errors()); }"); 
    out.println("    else"); 
    out.println("    { " + bean + "." + fullop + "(); }"); 

    out.println("  }\n\r");
    out.println();

    out.println("  public void " + fullop + "Cancel(View _v) {}");
    out.println("}"); 
  }

  public static void androidEditViewActivity(String op, Entity ent, PrintWriter out)
  { String ename = ent.getName();
    String fullop = op + ename; 
    String beanclass = ename + "Bean";
    String bean = beanclass.toLowerCase();
    String evo = ename + "VO"; 

    out.println("package com.example.app;\n"); 
    out.println(); 
    out.println("import android.os.Bundle;");
    out.println("import android.app.Activity;");
    out.println("import android.view.View;");
    out.println("import android.util.Log;"); 
    out.println("import android.widget.EditText;\n\r");
    out.println(); 

    out.println("public class View" + fullop + " extends Activity");
    out.println("{ " + beanclass + " " + bean + ";");
    out.println("   model : ModelFacade = null;"); 
    out.println(); 

    Vector pars = ent.getAttributes();

    for (int x = 0; x < pars.size(); x++)
    { Attribute par = (Attribute) pars.get(x);
      String pnme = par.getName(); 
      String tfnme = pnme + "TextField"; 
      String dname = pnme + "Data";
      out.println("  EditText " + tfnme + ";");
      out.println("  String " + dname + " = \"\";");
    }
    out.println();
    out.println();
    out.println("  @Override");
    out.println("  protected void onCreate(Bundle bundle)");
    out.println("  { super.onCreate(bundle);");
    out.println("    setContentView(R.layout." + fullop + "_layout);");

    for (int x = 0; x < pars.size(); x++)
    { Attribute par = (Attribute) pars.get(x);
      String pnme = par.getName(); 
      String tfnme = pnme + "TextField"; 
      out.println("    " + tfnme + " = (EditText) findViewById(R.id." + fullop + pnme + ");");
    }
    out.println("    " + bean + " = new " + beanclass + "(this);");
    out.println("    model = ModelFacade.getInstance(this);"); 
    out.println("    " + evo + " _current = model.getSelected" + ename + "();"); 
    out.println("    if (_current != null)"); 
    out.println("    { "); 
    for (int x = 0; x < pars.size(); x++) 
    { Attribute par = (Attribute) pars.get(x); 
      String pnme = par.getName(); 
      String tfnme = pnme + "TextField"; 
      out.println("    " + tfnme + "TextField.setText(_current." + pnme + ");"); 
    }
    out.println("    }"); 
    out.println("  }\n\r");
    out.println(); 
  
    out.println("  public void " + fullop + "OK(View _v) ");
    out.println("  {");
    for (int x = 0; x < pars.size(); x++)
    { Attribute att = (Attribute) pars.get(x);
      String aname = att.getName();
      String tfnme = aname + "TextField"; 
      String dname = aname + "Data";
      out.println("    " + dname + " = " + tfnme + ".getText() + \"\";");
      out.println("    " + bean + ".set" + aname + "(" + dname + ");"); 
    }
    out.println("    if (" + bean + ".is" + fullop + "error())"); 
    out.println("    { Log.w(getClass().getName(), " + bean + ".errors()); }"); 
    out.println("    else"); 
    out.println("    { " + bean + "." + fullop + "(); }"); 

    out.println("  }\n\r");
    out.println();

    out.println("  public void " + fullop + "Cancel(View _v) {}");
    out.println("}"); 
  }
  
  public void generateManifest(String appname, boolean needsInternet, boolean needsMaps, PrintWriter out)
  { out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    out.println("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"");
    out.println("    package=\"com.example." + appname + "\">");
    out.println(); 
	
	String appMain = ".MainActivity"; 

    if (needsInternet) 
    { out.println("    <uses-permission android:name=\"android.permission.INTERNET\" />"); } 
 
    out.println(); 

    if (needsMaps) 
    { out.println("    <uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\" />"); } 
 
    out.println();
	
    out.println("  <application");
    out.println("    android:allowBackup=\"true\"");
    out.println("    android:icon=\"@mipmap/ic_launcher\"");
    out.println("    android:label=\"@string/app_name\"");   
    out.println("    android:roundIcon=\"@mipmap/ic_launcher_round\"");
    out.println("    android:supportsRtl=\"true\"");
    out.println("    android:theme=\"@style/AppTheme\">");
	
	out.println(); 
	
    if (needsMaps)
	{ out.println("  <meta-data"); 
      out.println("    android:name=\"com.google.android.geo.API_KEY\""); 
      out.println("    android:value=\"@string/google_maps_key\" />"); 
	  appMain = ".MapsComponent"; 
    } 

    out.println(); 

    out.println("  <activity");
    out.println("  android:name=\"" + appMain + "\"");
    out.println("  android:label=\"@string/app_name\">");
    out.println("     <intent-filter>");
    out.println("           <action android:name=\"android.intent.action.MAIN\" />");
    out.println();  
	out.println("         <category android:name=\"android.intent.category.LAUNCHER\" />");
    out.println("    </intent-filter>");
    out.println("  </activity>");
    out.println(" </application>");
    out.println("</manifest>");
  }

	
	
	
  public static void androidTableLayoutForOp(String op, String vc, String image, Vector atts, Attribute res, PrintWriter out)
  { out.println("<ScrollView"); 
    out.println("xmlns:android=\"http://schemas.android.com/apk/res/android\""); 
    out.println("xmlns:app=\"http://schemas.android.com/apk/res-auto\""); 
    out.println("xmlns:tools=\"http://schemas.android.com/tools\""); 
    out.println("android:layout_width=\"match_parent\""); 
    out.println("android:layout_height=\"match_parent\""); 
    out.println("tools:context=\"" + vc + "\" >");
    out.println(); 
    out.println("<TableLayout"); 
    out.println("  android:layout_width=\"fill_parent\"");
    out.println("  android:layout_height=\"fill_parent\"");
    out.println("  android:stretchColumns=\"1\" >"); 
    out.println(); 

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      String attnme = att.getName();
      String label = Named.capitalise(attnme);

      String attlabel = op + attnme + "Label";
      String attfield = op + attnme + "Field";
      out.println("  <TableRow>");
      out.println("  <TextView");  
      out.println("    android:id=\"@+id/" + attlabel + "\"");
      out.println("    android:hint=\"" + attnme + " for " + op + "\"");
      out.println("    android:textStyle=\"bold\"");
      out.println("    android:background=\"#EEFFBB\""); 
      out.println("    android:text=\"" + label + ":\" />");
      if (att.isSmallEnumeration()) // no more than 4 elements
      { String attgroup = att.androidRadioButtonGroup(op + attnme); 
        out.println(attgroup); 
      }
      else if (att.isLargeEnumeration() || att.isEntity())
      { out.println("<Spinner"); 
        out.println("  android:id=\"@+id/" + op + attnme + "Spinner\""); 
        out.println("  android:layout_width=\"fill_parent\""); 
        out.println("  android:layout_height=\"wrap_content\""); 
        out.println("  android:layout_span=\"4\" />"); 
      } 
      else  
      { out.println("  <EditText"); 
        out.println("    android:id=\"@+id/" + attfield + "\"");
        if (att.isInteger())
        { out.println("    android:inputType=\"number\""); } 
        else if (att.isDouble())
        { out.println("    android:inputType=\"number|numberDecimal\""); } 
        out.println("    android:layout_span=\"4\" />");
      } 
      out.println("  </TableRow>");
      out.println(); 
    }
    out.println("  <TableRow>");
    out.println("  <Button");
    out.println("  android:id=\"@+id/" + op + "OK\"");
    out.println("  android:layout_width=\"wrap_content\"");
    out.println("  android:layout_height=\"wrap_content\"");
    out.println("  android:layout_column=\"1\"");
    out.println("  android:background=\"#AAAAFF\""); 
    out.println("  android:text=\"" + op + "\"");
    out.println("  android:onClick=\"" + op + "OK\"");
    out.println("  />");
    out.println("  <Button");
    out.println("  android:id=\"@+id/" + op + "Cancel\"");
    out.println("  android:layout_width=\"wrap_content\"");
    out.println("  android:layout_height=\"wrap_content\"");
    out.println("  android:background=\"#FFAAAA\""); 
    out.println("  android:layout_column=\"3\"");
    out.println("  android:text=\"Cancel\"");
    out.println("  android:onClick=\"" + op + "Cancel\"");
    out.println("  />");
    out.println("  </TableRow>");
    out.println(); 
	
    if (res != null && "WebDisplay".equals(res.getType().getName())) 
    { out.println("  <WebView"); 
      out.println("    android:layout_width=\"match_parent\""); 
      out.println("    android:layout_height=\"wrap_content\""); 
      out.println("    android:id=\"@+id/" + op + "Result\" />"); 
    } 
    else if (res != null && "GraphDisplay".equals(res.getType().getName())) 
    { out.println("  <ImageView"); 
      out.println("     android:id=\"@+id/" + op + "Result\""); 
      out.println("     android:layout_height=\"500dp\""); 
      out.println("     android:layout_width=\"fill_parent\""); 
      out.println("     android:adjustViewBounds=\"true\" />"); 
    } 
    else if (res != null && "ImageDisplay".equals(res.getType().getName())) 
    { out.println("  <ImageView"); 
      out.println("     android:id=\"@+id/" + op + "Result\""); 
      out.println("     android:layout_height=\"wrap_content\""); 
      out.println("     android:layout_width=\"match_parent\""); 
      out.println("     android:src=\"@drawable/ic_launcher_background\" />"); 
    } 
    else if (res != null) 
	{ out.println("  <TableRow>");
      out.println("  <TextView");  
      out.println("    android:id=\"@+id/" + op + "ResultLabel\"");
      out.println("    android:hint=\"Result of " + op + "\"");
      out.println("    android:textStyle=\"bold\"");
      out.println("    android:background=\"#EEFFBB\""); 
      out.println("    android:text=\"Result:\" />");
      out.println("  <TextView");  
      out.println("    android:id=\"@+id/" + op + "Result\"");
      out.println("    android:hint=\"Result of " + op + "\"");
      out.println("    android:textStyle=\"bold\"");
      out.println("    android:layout_span=\"4\" />");
      out.println("  </TableRow>");
    }
 
    out.println("  <View"); 
    out.println("     android:layout_height=\"20dip\""); 
    out.println("     android:background=\"#FFFFFF\"/>"); 
	out.println(); 
    
	if (image != null && !("null".equals(image))) 
    { out.println("  <ImageView"); 
      out.println("   android:id=\"@+id/" + op + "image\""); 
      out.println("   android:src=\"@drawable/" + image + "\" />"); 
    }  
    
    out.println("  <View"); 
    out.println("     android:layout_height=\"20dip\""); 
    out.println("     android:background=\"#FFFFFF\"/>"); 
	out.println(); 
    
	out.println("</TableLayout>");
    out.println("</ScrollView>");
  }

  public static void androidTableLayoutForOps(String op, String vc, String image, Vector atts, Attribute res, Vector usecases, 
                                              PrintWriter out)
  { out.println("<ScrollView"); 
    out.println("xmlns:android=\"http://schemas.android.com/apk/res/android\""); 
    out.println("xmlns:app=\"http://schemas.android.com/apk/res-auto\""); 
    out.println("xmlns:tools=\"http://schemas.android.com/tools\""); 
    out.println("android:layout_width=\"match_parent\""); 
    out.println("android:layout_height=\"match_parent\""); 
    out.println("tools:context=\"" + vc + "\" >");
    out.println(); 
    out.println("<TableLayout"); 
    out.println("  android:layout_width=\"fill_parent\"");
    out.println("  android:layout_height=\"fill_parent\"");
    out.println("  android:stretchColumns=\"1\" >"); 
    out.println(); 

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      String attnme = att.getName();
      String label = Named.capitalise(attnme);

      String attlabel = op + attnme + "Label";
      String attfield = op + attnme + "Field";
      out.println("  <TableRow>");
      out.println("  <TextView");  
      out.println("    android:id=\"@+id/" + attlabel + "\"");
      out.println("    android:hint=\"" + attnme + " for " + op + "\"");
      out.println("    android:textStyle=\"bold\"");
      out.println("    android:background=\"#EEFFBB\""); 
      out.println("    android:text=\"" + label + ":\" />");
      if (att.isSmallEnumeration()) // no more than 4 elements
      { String attgroup = att.androidRadioButtonGroup(op + attnme); 
        out.println(attgroup); 
      }
      else if (att.isLargeEnumeration() || att.isEntity())
      { out.println("<Spinner"); 
        out.println("  android:id=\"@+id/" + op + attnme + "Spinner\""); 
        out.println("  android:layout_width=\"fill_parent\""); 
        out.println("  android:layout_height=\"wrap_content\""); 
        out.println("  android:layout_span=\"4\" />"); 
      } 
      else  
      { out.println("  <EditText"); 
        out.println("    android:id=\"@+id/" + attfield + "\"");
        if (att.isInteger())
        { out.println("    android:inputType=\"number\""); } 
        else if (att.isDouble())
        { out.println("    android:inputType=\"number|numberDecimal\""); } 
        else if (att.isPassword())
        { out.println("    android:inputType=\"textPassword\""); }
        out.println("    android:layout_span=\"4\" />");
      } 
      out.println("  </TableRow>");
      out.println(); 
    }
    out.println("  <TableRow>");
    out.println("  <Button");
    out.println("  android:id=\"@+id/" + op + "OK\"");
    out.println("  android:layout_width=\"wrap_content\"");
    out.println("  android:layout_height=\"wrap_content\"");
    out.println("  android:background=\"#AAAAFF\""); 
    out.println("  android:layout_column=\"1\"");
    String capop = Named.capitalise(op); 
    out.println("  android:text=\"" + capop + "\"");
    out.println("  android:onClick=\"" + op + "OK\"");
    out.println("  />");
    out.println("  <Button");
    out.println("  android:id=\"@+id/" + op + "Cancel\"");
    out.println("  android:layout_width=\"wrap_content\"");
    out.println("  android:layout_height=\"wrap_content\"");
	out.println("  android:background=\"#FFAAAA\""); 

    out.println("  android:layout_column=\"3\"");
    out.println("  android:text=\"Cancel\"");
    out.println("  android:onClick=\"" + op + "Cancel\"");
    out.println("  />");
    out.println("  </TableRow>");
    out.println(); 

    if (res != null)
    { out.println("<TableRow>"); 
      out.println("  <TextView");  
      out.println("     android:textStyle=\"bold\"");
      out.println("     android:text=\"Result:\" />");
      out.println("</TableRow>"); 
    } 
	  
    if (res != null && "WebDisplay".equals(res.getType().getName())) 
    { out.println("  <WebView"); 
      out.println("    android:layout_width=\"match_parent\""); 
      out.println("    android:layout_height=\"wrap_content\""); 
      out.println("    android:id=\"@+id/" + op + "Result\" />"); 
    } 
    else if (res != null && "ImageDisplay".equals(res.getType().getName())) 
    { out.println("  <ImageView"); 
      out.println("     android:id=\"@+id/" + op + "Result\" />"); 
      out.println("     android:layout_height=\"wrap_content\""); 
      out.println("     android:layout_width=\"match_parent\""); 
      out.println("     android:src=\"@drawable/ic_launcher_background\" />"); 
    } 
    else if (res != null) 
    { out.println("  <TableRow>");
      out.println("  <TextView");  
      out.println("    android:id=\"@+id/" + op + "ResultLabel\"");
      out.println("    android:hint=\"Result of " + op + "\"");
      out.println("    android:textStyle=\"bold\"");
      out.println("    android:background=\"#EEFFBB\""); 
      out.println("    android:text=\"Result of " + op + ":\" />");
      out.println("  <TextView");  
      out.println("    android:id=\"@+id/" + op + "Result\"");
      out.println("    android:hint=\"Result of " + op + "\"");
      out.println("    android:textStyle=\"bold\"");
      out.println("    android:layout_span=\"4\" />");
      out.println("  </TableRow>");
    } 
	
    out.println("  <View"); 
    out.println("     android:layout_height=\"20dip\""); 
    out.println("     android:background=\"#FFFFFF\"/>"); 
	out.println(); 
    
	for (int j = 0; j < usecases.size(); j++)
	{ UseCase extensionuc = (UseCase) usecases.get(j); 
	  String ucop = extensionuc.getName(); 
	  Vector ucatts = extensionuc.getParameters(); 
	  Attribute ucres = extensionuc.getResultParameter(); 
	  for (int x = 0; x < ucatts.size(); x++)
       { Attribute att = (Attribute) ucatts.get(x);
         String attnme = att.getName();
         String label = Named.capitalise(attnme);

         String attlabel = ucop + attnme + "Label";
         String attfield = ucop + attnme + "Field";
         out.println("  <TableRow>");
         out.println("  <TextView");  
         out.println("    android:id=\"@+id/" + attlabel + "\"");
         out.println("    android:hint=\"" + attnme + " for " + ucop + "\"");
         out.println("    android:background=\"#EEFFBB\""); 
         out.println("    android:textStyle=\"bold\"");
         out.println("    android:text=\"" + label + ":\" />");
         if (att.isSmallEnumeration()) // no more than 4 elements
         { String attgroup = att.androidRadioButtonGroup(ucop + attnme); 
           out.println(attgroup); 
         }
         else if (att.isLargeEnumeration() || att.isEntity())
         { out.println("<Spinner"); 
           out.println("  android:id=\"@+id/" + ucop + attnme + "Spinner\""); 
           out.println("  android:layout_width=\"fill_parent\""); 
           out.println("  android:layout_height=\"wrap_content\""); 
           out.println("  android:layout_span=\"4\" />"); 
         } 
         else  
         { out.println("  <EditText"); 
           out.println("    android:id=\"@+id/" + attfield + "\"");
           if (att.isInteger())
           { out.println("    android:inputType=\"number\""); } 
           else if (att.isDouble())
           { out.println("    android:inputType=\"number|numberDecimal\""); } 
           else if (att.isPassword())
           { out.println("    android:inputType=\"textPassword\""); }
           out.println("    android:layout_span=\"4\" />");
         }  
         out.println("  </TableRow>");
         out.println(); 
       }
       out.println("  <TableRow>");
       out.println("  <Button");
       out.println("  android:id=\"@+id/" + ucop + "OK\"");
       out.println("  android:layout_width=\"wrap_content\"");
       out.println("  android:layout_height=\"wrap_content\"");
       out.println("  android:background=\"#AAAAFF\""); 

       out.println("  android:layout_column=\"1\"");
       String caplabel = Named.capitalise(ucop); 
       out.println("  android:text=\"" + caplabel + "\"");
       out.println("  android:onClick=\"" + ucop + "OK\"");
       out.println("  />");
       out.println("  </TableRow>"); 
       out.println(); 
	   
       if (ucres != null) 
       { out.println("<TableRow>"); 
         out.println("  <TextView");  
         out.println("     android:textStyle=\"bold\"");
         out.println("     android:text=\"Result:\" />");
         out.println("</TableRow>");
       } 
	   
       if (ucres != null && "WebDisplay".equals(ucres.getType().getName())) 
       { out.println("  <WebView"); 
         out.println("    android:layout_width=\"match_parent\""); 
         out.println("    android:layout_height=\"wrap_content\""); 
         out.println("    android:id=\"@+id/" + ucop + "Result\" />"); 
       } 
       else if (ucres != null && "ImageDisplay".equals(ucres.getType().getName())) 
       { out.println("  <ImageView"); 
         out.println("     android:id=\"@+id/" + ucop + "Result\""); 
         out.println("     android:layout_height=\"wrap_content\""); 
         out.println("     android:layout_width=\"match_parent\""); 
         out.println("     android:src=\"@drawable/ic_launcher_background\" />"); 
       } 
       else if (ucres != null && "GraphDisplay".equals(ucres.getType().getName())) 
       { out.println("  <ImageView"); 
         out.println("     android:id=\"@+id/" + ucop + "Result\""); 
         out.println("     android:layout_height=\"500dp\""); 
         out.println("     android:layout_width=\"fill_parent\""); 
         out.println("     android:adjustViewBounds=\"true\" />"); 
       } 
       else if (ucres != null) 
       { out.println("  <TableRow>");
       /*  out.println("  <TextView");  
         out.println("    android:id=\"@+id/" + ucop + "ResultLabel\"");
         out.println("    android:hint=\"Result of " + ucop + "\"");
         out.println("    android:textStyle=\"bold\"");
         out.println("    android:background=\"#EEFFBB\""); 
         out.println("    android:text=\"Result:\" />"); */ 
         out.println("  <TextView");  
         out.println("    android:id=\"@+id/" + ucop + "Result\"");
         out.println("    android:hint=\"Result of " + ucop + "\"");
         out.println("    android:textStyle=\"bold\"");
         out.println("    android:layout_span=\"5\" />");
         out.println("  </TableRow>");
       } 
       out.println("  <View"); 
       out.println("     android:layout_height=\"20dip\""); 
       out.println("     android:background=\"#FFFFFF\"/>"); 
	   out.println(); 
       out.println(); 
	}

    if (image != null && !("null".equals(image)))         
    { out.println("  <ImageView"); 
      out.println("   android:id=\"@+id/" + op + "image\""); 
      out.println("   android:src=\"@drawable/" + image + "\" />"); 
    }  
    out.println("  <View"); 
    out.println("     android:layout_height=\"20dip\""); 
    out.println("     android:background=\"#FFFFFF\"/>"); 
	out.println(); 
    
    out.println("</TableLayout>");
    out.println("</ScrollView>");
  }

public static void androidOpViewActivity(String op, String systemName, 
                                         String classname, String layout, Vector pars, Attribute res, 
                                         Vector extensions, PrintWriter out)
{ // Entity ent = getEntity();
  // String entname = ent.getName();
  // String fullop = op + entname; 
  String beanclass = op + "Bean";
  String bean = beanclass.toLowerCase();

  String lcname = op.toLowerCase(); 
  String extraimports = ""; 
  String protocols = ""; 
  String extraops = ""; 
  
  if (UseCase.hasLargeEnumerationParameter(pars))
  { extraimports = "import android.widget.AdapterView;\n\r" + 
                   "import android.widget.ArrayAdapter;\n\r" + 
                   "import android.widget.Spinner;\n\r"; 
    protocols = " implements AdapterView.OnItemSelectedListener"; 
    extraops = UseCase.spinnerListenerOperations(op,pars); 
  } // including case of Entity-valued parameter
  
  out.println("package com.example." + systemName + ";\n"); 
  out.println(); 
  out.println("import androidx.appcompat.app.AppCompatActivity;\n\r");
  out.println("import android.os.Bundle;");
  out.println("import android.content.res.Resources;"); 
  out.println("import android.graphics.drawable.Drawable;"); 
  out.println("import androidx.core.content.res.ResourcesCompat;");
  out.println("import android.content.res.AssetManager;"); 
  out.println("import android.graphics.drawable.BitmapDrawable;"); 
  out.println("import java.io.InputStream;"); 

  out.println(extraimports);
  out.println("import java.util.List;"); 
  out.println("import java.util.ArrayList;"); 
  out.println("import android.view.View;");
  out.println("import android.util.Log;"); 
  out.println("import android.widget.Toast;");
  out.println("import android.widget.RadioGroup;"); 
  out.println("import android.view.inputmethod.InputMethodManager;"); 
  out.println("import android.widget.EditText;");
  out.println("import android.webkit.WebView;"); 
  out.println("import android.widget.ImageView;"); 
  out.println("import android.widget.TextView;\n\r");
  out.println(); 

  out.println("public class " + classname + " extends AppCompatActivity" + protocols);
  out.println("{ " + beanclass + " " + bean + ";");

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p);
    String extop = extension.getName();  
    Vector extpars = extension.getParameters(); 
    extraops = extraops + UseCase.spinnerListenerOperations(extop,extpars); 
    String extbeanclass = extop + "Bean";
    String extbean = extbeanclass.toLowerCase();
    out.println("  " + extbeanclass + " " + extbean + ";"); 
  } 
  out.println(); 

  // Vector pars = getParameters();
  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    String tfnme = pnme + "TextField"; 
    String dname = pnme + "Data";
    if (par.isSmallEnumeration())
    { out.println("  RadioGroup " + pnme + "Group;"); } 
    else if (par.isLargeEnumeration())
    { out.println("  Spinner " + op + pnme + "Spinner;");  
      out.println("  String[] " + op + pnme + "ListItems = " + par.androidValueList() + ";"); 
    }
    else if (par.isEntity())
    { out.println("  Spinner " + op + pnme + "Spinner;");  
      out.println("  List<String> " + op + pnme + "ListItems = new ArrayList<String>();"); 
    }
    else 
    { out.println("  EditText " + tfnme + ";"); } 
    out.println("  String " + dname + " = \"\";");
  }
  
  if (res != null && "WebDisplay".equals(res.getType().getName()))
  { out.println("  WebView " + op + "Result;"); }
  else if (res != null && "ImageDisplay".equals(res.getType().getName()))
  { out.println("  ImageView " + op + "Result;"); }
  else if (res != null && "GraphDisplay".equals(res.getType().getName()))
  { out.println("  ImageView " + op + "Result;"); }
  else if (res != null)  
  { out.println("  TextView " + op + "Result;"); }

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p); 
    String extop = extension.getName();    
    Vector extpars = extension.getParameters();
    Attribute extres = extension.getResultParameter(); 
	 
    for (int x = 0; x < extpars.size(); x++)
    { Attribute extpar = (Attribute) extpars.get(x);
      String pnme = extpar.getName(); 
      String tfnme = extop + pnme + "TextField"; 
      String dname = extop + pnme + "Data";
      if (extpar.isSmallEnumeration())
      { out.println("  RadioGroup " + extop + pnme + "Group;"); } 
      else if (extpar.isLargeEnumeration())
      { out.println("  Spinner " + extop + pnme + "Spinner;");  
        out.println("  String[] " + extop + pnme + "ListItems = " + extpar.androidValueList() + ";"); 
      }
      else if (extpar.isEntity())
      { out.println("  Spinner " + extop + pnme + "Spinner;");  
        out.println("  List<String> " + extop + pnme + "ListItems = new ArrayList<String>();"); 
      }
      else 
      { out.println("  EditText " + tfnme + ";"); } 
      out.println("  String " + dname + " = \"\";");
    }

    if (extres != null && "WebDisplay".equals(extres.getType().getName()))
    { out.println("  WebView " + extop + "Result;"); }
    else if (extres != null && "ImageDisplay".equals(extres.getType().getName()))
    { out.println("  ImageView " + extop + "Result;"); }
    else if (extres != null && "GraphDisplay".equals(extres.getType().getName()))
    { out.println("  ImageView " + extop + "Result;"); }
    else if (extres != null)  
    { out.println("  TextView " + extop + "Result;"); }
  } 
  
  out.println();
  out.println();
  out.println("  @Override");
  out.println("  protected void onCreate(Bundle bundle)");
  out.println("  { super.onCreate(bundle);");
  // out.println("    setContentView(R.layout." + lcname + "_layout);");
  out.println("    setContentView(R.layout." + layout + ");");

  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    if (par.isSmallEnumeration())
    { String gname = op + pnme + "Group"; 
      out.println("    " + gname + " = (RadioGroup) findViewById(R.id." + op + pnme + "Group);"); 
    } 
    else if (par.isLargeEnumeration() || par.isEntity())
    { out.println(par.androidSpinnerInitialisation(op,pnme,"","this")); }
    else
    { String tfnme = op + pnme + "TextField"; 
      out.println("    " + tfnme + " = (EditText) findViewById(R.id." + op + pnme + "Field);");
    } 
  }
  
  if (res != null && "WebDisplay".equals(res.getType().getName()))
  { out.println("    " + op + "Result = (WebView) findViewById(R.id." + op + "Result);"); }
  else if (res != null && "ImageDisplay".equals(res.getType().getName()))
  { out.println("    " + op + "Result = (ImageView) findViewById(R.id." + op + "Result);"); }
  else if (res != null && "GraphDisplay".equals(res.getType().getName()))
  { out.println("    " + op + "Result = (ImageView) findViewById(R.id." + op + "Result);"); }
  else if (res != null)  
  { out.println("    " + op + "Result = (TextView) findViewById(R.id." + op + "Result);"); }
	
  out.println("    " + bean + " = new " + beanclass + "(this);");

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p); 
    String extop = extension.getName();    
    String extbeanclass = extop + "Bean";
    Attribute extres = extension.getResultParameter(); 
    String extbean = extbeanclass.toLowerCase();
    Vector extpars = extension.getParameters(); 
    for (int y = 0; y < extpars.size(); y++)
    { Attribute par = (Attribute) extpars.get(y);
      String pnme = par.getName(); 
      if (par.isSmallEnumeration())
      { String gname = extop + pnme + "Group"; 
        out.println("    " + gname + " = (RadioGroup) findViewById(R.id." + extop + pnme + "Group);"); 
      } 
      else if (par.isLargeEnumeration() || par.isEntity())
      { out.println(par.androidSpinnerInitialisation(extop,pnme,"","this")); }
      else
      { String tfnme = extop + pnme + "TextField"; 
        out.println("    " + tfnme + " = (EditText) findViewById(R.id." + extop + pnme + "Field);");
      }
	  
      if (extres != null && "WebDisplay".equals(extres.getType().getName()))
      { out.println("    " + extop + "Result = (WebView) findViewById(R.id." + extop + "Result);"); }
      else if (extres != null && "ImageDisplay".equals(extres.getType().getName()))
      { out.println("    " + extop + "Result = (ImageView) findViewById(R.id." + extop + "Result);"); }
      else if (extres != null && "GraphDisplay".equals(extres.getType().getName()))
      { out.println("    " + extop + "Result = (ImageView) findViewById(R.id." + extop + "Result);"); }
      else if (extres != null)
      { out.println("    " + extop + "Result = (TextView) findViewById(R.id." + extop + "Result);"); }
    } 
    out.println("    " + extbean + " = new " + extbeanclass + "(this);");
  } 
  out.println("  }\n\r");
  out.println(); // for edit, the principal primary key does not have an EditText
  out.println(extraops); 
  out.println(); 
  	
  out.println("  public void " + op + "OK(View _v) ");
  out.println("  { InputMethodManager _imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);"); 
  out.println("    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }"); 

  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    String tfnme = op + aname + "TextField"; 
    String dname = op + aname + "Data";
    if (att.isSmallEnumeration())
    { String attextract = att.extractEnumerationValue(op,dname); 
	 out.println(attextract); 
    } 
    else if (att.isLargeEnumeration() || att.isEntity()) {}
    else 
    { out.println("    " + dname + " = " + tfnme + ".getText() + \"\";"); } 
    out.println("    " + bean + ".set" + aname + "(" + dname + ");"); 
  }
  out.println("    if (" + bean + ".is" + op + "error())"); 
  out.println("    { Log.w(getClass().getName(), " + bean + ".errors());");  
  out.println("      Toast.makeText(this, \"Errors: \" + " + bean + ".errors(), Toast.LENGTH_LONG).show();"); 
  out.println("    }"); 
  out.println("    else"); 
  
  if (res != null && "WebDisplay".equals(res.getType().getName())) 
  { out.println("    { " + op + "Result.loadUrl(" + bean + "." + op + "().url + \"\"); }"); }
  else if (res != null && "ImageDisplay".equals(res.getType().getName())) 
  { out.println("    { Resources _rsrcs = getResources();"); 
    out.println("      AssetManager _amanager = _rsrcs.getAssets();"); 
    out.println("      try {"); 
	out.println("        String _imgName = " + bean + "." + op + "().imageName;"); 
    out.println("        InputStream _imageStream = _amanager.open(_imgName);"); 
    out.println("        Drawable _drawable = new BitmapDrawable(_rsrcs, _imageStream);"); 
    out.println("        " + op + "Result.setImageDrawable(_drawable);"); 
    out.println("      }  catch (Exception _e) { _e.printStackTrace(); }"); 
    out.println("    }"); 
  }
  else if (res != null && "GraphDisplay".equals(res.getType().getName())) 
  { out.println("    { GraphDisplay _result = " + bean + "." + op + "();"); 
    out.println("      " + op + "Result.invalidate();"); 
    out.println("      " + op + "Result.refreshDrawableState();"); 
    out.println("      " + op + "Result.setImageDrawable(_result);"); 
    out.println("    }"); 
  }
  else if (res != null) 
  { out.println("    { " + op + "Result.setText(" + bean + "." + op + "() + \"\"); }"); } 
  else 
  { out.println("    { " + bean + "." + op + "(); }"); }  

  out.println("  }\n\r");
  out.println();
  out.println();

  out.println("  public void " + op + "Cancel(View _v)");
  out.println("  { " + bean + ".resetData();");
  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    if (att.isSmallEnumeration()) { } 
    else if (att.isLargeEnumeration() || att.isEntity()) { } 
    else  
    { String tfnme = op + aname + "TextField"; 
      out.println("    " + tfnme + ".setText(\"\");");
    } 
  } 
  
  if (res != null && "String".equals(res.getType().getName())) 
  { out.println("    " + op + "Result.setText(\"\");"); }
  
  out.println("  }");    
  

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p); 
    String extop = extension.getName();    
    Vector extpars = extension.getParameters(); 
    Attribute extres = extension.getResultParameter(); 
	
    String extbeanclass = extop + "Bean";
    String extbean = extbeanclass.toLowerCase();
    
    out.println("  public void " + extop + "OK(View _v) ");
    out.println("  { InputMethodManager _imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);"); 
    out.println("    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }"); 
    for (int x = 0; x < extpars.size(); x++)
    { Attribute att = (Attribute) extpars.get(x);
      String aname = att.getName();
      String tfnme = extop + aname + "TextField"; 
      String dname = extop + aname + "Data";
      if (att.isSmallEnumeration())
      { String attextract = att.extractEnumerationValue(extop,dname); 
        out.println(attextract); 
      }
      else if (att.isLargeEnumeration() || att.isEntity()) { } 
      else 
      { out.println("    " + dname + " = " + tfnme + ".getText() + \"\";"); } 
        out.println("    " + extbean + ".set" + aname + "(" + dname + ");"); 
      } 
	
      out.println("    if (" + extbean + ".is" + extop + "error())"); 
      out.println("    { Log.w(getClass().getName(), " + extbean + ".errors()); "); 
      out.println("      Toast.makeText(this, \"Errors: \" + " + extbean + ".errors(), Toast.LENGTH_LONG).show();"); 
      out.println("    }");
      out.println("    else"); 

      if (extres != null && "WebDisplay".equals(extres.getType().getName())) 
      { out.println("    { " + extop + "Result.loadUrl(" + extbean + "." + extop + "() + \"\"); }"); }
      else if (extres != null && "ImageDisplay".equals(extres.getType().getName())) 
      { out.println("    { Resources _rsrcs = getResources();"); 
        out.println("      AssetManager _amanager = _rsrcs.getAssets();"); 
        out.println("      try {"); 
	    out.println("        String _imgName = " + extbean + "." + extop + "().imageName;"); 
        out.println("        InputStream _imageStream = _amanager.open(_imgName);"); 
        out.println("        Drawable _drawable = new BitmapDrawable(_rsrcs, _imageStream);"); 
        out.println("        " + extop + "Result.setImageDrawable(_drawable);"); 
        out.println("      }  catch (Exception _e) { _e.printStackTrace(); }"); 
        out.println("    }"); 
      }
      else if (extres != null && "GraphDisplay".equals(extres.getType().getName())) 
      { out.println("    { GraphDisplay _result = " + extbean + "." + extop + "();"); 
        out.println("      " + extop + "Result.invalidate();"); 
        out.println("      " + extop + "Result.refreshDrawableState();"); 
        out.println("      " + extop + "Result.setImageDrawable(_result);"); 
        out.println("    }"); 
      }
      else if (extres != null) 
      { out.println("    { " + extop + "Result.setText(" + extbean + "." + extop + "() + \"\"); }"); } 
      else 
      { out.println("    { " + extbean + "." + extop + "(); }"); }
  
      out.println("  }\n\r");
      out.println();
   } 
  
   out.println("}"); 
 }

public static void androidOpViewFragment(String op, String packageName, 
                                         String classname, String layout, Vector pars, Attribute res, 
                                         Vector extensions, PrintWriter out)
{ // Entity ent = getEntity();
  // String entname = ent.getName();
  // String fullop = op + entname; 
  String beanclass = op + "Bean";
  String bean = beanclass.toLowerCase();

  String lcname = op.toLowerCase(); 
  String extraimports = ""; 
  String protocols = " implements OnClickListener"; 
  Vector extraops = new Vector(); 
  
  if (UseCase.hasLargeEnumerationParameter(pars))
  { extraimports = "import android.widget.AdapterView;\n\r" + 
                   "import android.widget.ArrayAdapter;\n\r" + 
                   "import android.widget.Spinner;\n\r"; 
    protocols = protocols + ", AdapterView.OnItemSelectedListener"; 
    // extraops = UseCase.spinnerListenerOperations(op,pars); 
  }
  
  out.println("package " + packageName + ".ui.main;\n"); 
  out.println(); 
  out.println("import androidx.appcompat.app.AppCompatActivity;\n\r");
  out.println("import android.os.Bundle;");
  out.println("import android.content.res.Resources;"); 
  out.println("import android.graphics.drawable.Drawable;"); 
  out.println("import androidx.core.content.res.ResourcesCompat;");
  out.println("import android.content.res.AssetManager;"); 
  out.println("import android.graphics.drawable.BitmapDrawable;"); 
  out.println("import java.io.InputStream;"); 

  out.println(extraimports);
  out.println("import android.view.LayoutInflater;"); 
  out.println("import android.view.ViewGroup;"); 
  out.println("import android.widget.Button;"); 
  out.println("import android.view.inputmethod.InputMethodManager;") ; 

  out.println("import androidx.annotation.Nullable;"); 
  out.println("import androidx.annotation.NonNull;"); 
  out.println("import androidx.fragment.app.Fragment;"); 

  out.println("import " + packageName + ".R;"); // packageName + ".R"

  out.println("import android.content.Context;"); 

  out.println("import androidx.annotation.LayoutRes;"); 
  out.println("import androidx.fragment.app.FragmentPagerAdapter;"); 
  out.println("import androidx.viewpager.widget.ViewPager;"); 
  // import androidx.appcompat.app.AppCompatActivity;

  out.println("import androidx.fragment.app.FragmentManager;"); 
  out.println("import android.view.View.OnClickListener;"); 
  out.println("import java.util.List;"); 
  out.println("import java.util.ArrayList;"); 
  out.println("import android.view.View;");
  out.println("import android.util.Log;"); 
  out.println("import android.widget.Toast;");
  out.println("import android.widget.RadioGroup;"); 
  out.println("import android.widget.EditText;");
  out.println("import android.webkit.WebView;"); 
  out.println("import android.webkit.ImageView;"); 
  out.println("import android.widget.TextView;\n\r");
  out.println(); 

  out.println("public class " + classname + " extends Fragment" + protocols);
  out.println("{ View root;"); 
  out.println("  Context myContext;"); 
  out.println("  " + beanclass + " " + bean + ";");

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p);
    String extop = extension.getName();  
    Vector extpars = extension.getParameters(); 
    // extraops = extraops + UseCase.spinnerListenerOperations(extop,extpars); 
    String extbeanclass = extop + "Bean";
    String extbean = extbeanclass.toLowerCase();
    out.println("  " + extbeanclass + " " + extbean + ";"); 
  } 
  out.println(); 

  extraops = UseCase.spinnerListenerOps(op,pars,extensions); 
  
  // Vector pars = getParameters();
  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    String tfnme = op + pnme + "TextField"; 
    String dname = op + pnme + "Data";
    if (par.isSmallEnumeration())
    { out.println("  RadioGroup " + op + pnme + "Group;"); } 
    else if (par.isLargeEnumeration())
    { out.println("  Spinner " + op + pnme + "Spinner;");  
      out.println("  String[] " + op + pnme + "ListItems = " + par.androidValueList() + ";"); 
    }
    else if (par.isEntity())
    { out.println("  Spinner " + op + pnme + "Spinner;");  
      out.println("  List<String> " + op + pnme + "ListItems = new ArrayList<String>();"); 
    }
    else 
    { out.println("  EditText " + tfnme + ";"); } 
    out.println("  String " + dname + " = \"\";");
  }
  
  if (res != null && "WebDisplay".equals(res.getType().getName()))
  { out.println("  WebView " + op + "Result;"); }
  else if (res != null && "ImageDisplay".equals(res.getType().getName()))
  { out.println("  ImageView " + op + "Result;"); }
  else if (res != null && "GraphDisplay".equals(res.getType().getName()))
  { out.println("  ImageView " + op + "Result;"); }
  else if (res != null)  
  { out.println("  TextView " + op + "Result;"); }

  out.println("  Button " + op + "OkButton;"); 
  out.println("  Button " + op + "cancelButton;"); 

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p); 
    String extop = extension.getName();
	out.println("  Button " + extop + "OkButton;"); 
	    
    Vector extpars = extension.getParameters();
    Attribute extres = extension.getResultParameter(); 
	 
    for (int x = 0; x < extpars.size(); x++)
    { Attribute extpar = (Attribute) extpars.get(x);
      String pnme = extpar.getName(); 
      String tfnme = extop + pnme + "TextField"; 
      String dname = extop + pnme + "Data";
      if (extpar.isSmallEnumeration())
      { out.println("  RadioGroup " + extop + pnme + "Group;"); } 
      else if (extpar.isLargeEnumeration())
      { out.println("  Spinner " + extop + pnme + "Spinner;");  
        out.println("  String[] " + extop + pnme + "ListItems = " + extpar.androidValueList() + ";"); 
      }
      else if (extpar.isEntity())
      { out.println("  Spinner " + extop + pnme + "Spinner;");  
        out.println("  List<String> " + extop + pnme + "ListItems = new ArrayList<String>();"); 
      }
      else 
      { out.println("  EditText " + tfnme + ";"); } 
      out.println("  String " + dname + " = \"\";");
    }

    if (extres != null && "WebDisplay".equals(extres.getType().getName()))
    { out.println("  WebView " + extop + "Result;"); }
    else if (extres != null && "ImageDisplay".equals(extres.getType().getName()))
    { out.println("  ImageView " + extop + "Result;"); }
    else if (extres != null && "GraphDisplay".equals(extres.getType().getName()))
    { out.println("  ImageView " + extop + "Result;"); }
    else if (extres != null)  
    { out.println("  TextView " + extop + "Result;");  }
  } 
  
  out.println();
  out.println();
  
  out.println(" public " + classname + "() {}"); 
  out.println(); 

  out.println("  public static " + classname + " newInstance(Context c)"); 
  out.println("  { " + classname + " fragment = new " + classname + "();"); 
  out.println("    Bundle args = new Bundle();"); 
  out.println("    fragment.setArguments(args);"); 
  out.println("    fragment.myContext = c;"); 
  out.println("    return fragment;"); 
  out.println("  }"); 
  
  out.println(); 

  out.println("  @Override"); 
  out.println("  public void onCreate(Bundle savedInstanceState)"); 
  out.println("  { super.onCreate(savedInstanceState); }"); 

  out.println(); 

  out.println("  @Override");
  out.println("  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)"); 
  out.println("  { root = inflater.inflate(R.layout." + layout + ", container, false);"); 
  out.println("    Bundle data = getArguments();"); 

  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    if (par.isSmallEnumeration())
    { String gname = op + pnme + "Group"; 
      out.println("    " + gname + " = (RadioGroup) root.findViewById(R.id." + op + pnme + "Group);"); 
    } 
    else if (par.isLargeEnumeration() || par.isEntity())
    { out.println(par.androidSpinnerInitialisation(op,pnme,"root.","myContext")); }
    else
    { String tfnme = op + pnme + "TextField"; 
      out.println("    " + tfnme + " = (EditText) root.findViewById(R.id." + op + pnme + "Field);");
    } 
  }

  if (res != null && "WebDisplay".equals(res.getType().getName()))
  { out.println("    " + op + "Result = (WebView) root.findViewById(R.id." + op + "Result);"); }
  else if (res != null && "ImageDisplay".equals(res.getType().getName()))
  { out.println("    " + op + "Result = (ImageView) root.findViewById(R.id." + op + "Result);"); }
  else if (res != null && "GraphDisplay".equals(res.getType().getName()))
  { out.println("    " + op + "Result = (ImageView) root.findViewById(R.id." + op + "Result);"); }
  else if (res != null)  
  { out.println("    " + op + "Result = (TextView) root.findViewById(R.id." + op + "Result);"); }
	
  out.println("    " + bean + " = new " + beanclass + "(myContext);");

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p); 
    String extop = extension.getName();    
    String extbeanclass = extop + "Bean";
    Attribute extres = extension.getResultParameter(); 
    String extbean = extbeanclass.toLowerCase();
    Vector extpars = extension.getParameters(); 
    for (int y = 0; y < extpars.size(); y++)
    { Attribute par = (Attribute) extpars.get(y);
      String pnme = par.getName(); 
      if (par.isSmallEnumeration())
      { String gname = extop + pnme + "Group"; 
        out.println("    " + gname + " = (RadioGroup) root.findViewById(R.id." + extop + pnme + "Group);"); 
      } 
      else if (par.isLargeEnumeration() || par.isEntity())
      { out.println(par.androidSpinnerInitialisation(extop,pnme,"root.", "myContext")); }
      else
      { String tfnme = extop + pnme + "TextField"; 
        out.println("    " + tfnme + " = (EditText) root.findViewById(R.id." + extop + pnme + "Field);");
      }

      if (extres != null && "WebDisplay".equals(extres.getType().getName()))
      { out.println("    " + extop + "Result = (WebView) root.findViewById(R.id." + extop + "Result);"); }
      else if (extres != null && "ImageDisplay".equals(extres.getType().getName()))
      { out.println("    " + extop + "Result = (ImageView) findViewById(R.id." + extop + "Result);"); }
      else if (extres != null && "GraphDisplay".equals(extres.getType().getName()))
      { out.println("    " + extop + "Result = (ImageView) findViewById(R.id." + extop + "Result);"); }
      else if (extres != null)
      { out.println("    " + extop + "Result = (TextView) root.findViewById(R.id." + extop + "Result);"); }
    } 
    out.println("    " + extbean + " = new " + extbeanclass + "(myContext);");
  } 
  out.println("    " + op + "OkButton = root.findViewById(R.id." + op + "OK);"); 
  out.println("    " + op + "OkButton.setOnClickListener(this);"); 
  out.println("    " + op + "cancelButton = root.findViewById(R.id." + op + "Cancel);");  
  out.println("    " + op + "cancelButton.setOnClickListener(this);"); 
  for (int i = 0; i < extensions.size(); i++)
  { UseCase extop = (UseCase) extensions.get(i); 
    String extopname = extop.getName(); 
    out.println("    " + extopname + "OkButton = root.findViewById(R.id." + extopname + "OK);");  
    out.println("    " + extopname + "OkButton.setOnClickListener(this);"); 
  } 
  out.println("    return root;"); 
  out.println("  }\n\r");
  out.println(); // for edit, the principal primary key does not have an EditText
  
  out.println("  public void onClick(View _v)"); 
  out.println("  { InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);"); 
  out.println("    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }"); 
  out.println("    if (_v.getId() == R.id." + op + "OK)"); 
  out.println("    { " + op + "OK(_v); }"); 
  out.println("    else if (_v.getId() == R.id." + op + "Cancel)"); 
  out.println("    { " + op + "Cancel(_v); }");
  for (int i = 0; i < extensions.size(); i++)
  { UseCase extop = (UseCase) extensions.get(i); 
    String extopname = extop.getName(); 
    out.println("    else if (_v.getId() == R.id." + extopname + "OK)"); 
	out.println("    { " + extopname + "OK(_v); }"); 
  } 
   
  out.println("  }"); 
  out.println(); 
	
  out.println("  public void " + op + "OK(View _v) ");
  out.println("  { "); 

  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    String tfnme = op + aname + "TextField"; 
    String dname = op + aname + "Data";
    if (att.isSmallEnumeration())
    { String attextract = att.extractEnumerationValue(op,dname); 
	 out.println(attextract); 
    } 
    else if (att.isLargeEnumeration() || att.isEntity()) {}
    else 
    { out.println("    " + dname + " = " + tfnme + ".getText() + \"\";"); } 
    out.println("    " + bean + ".set" + aname + "(" + dname + ");"); 
  }
  out.println("    if (" + bean + ".is" + op + "error())"); 
  out.println("    { Log.w(getClass().getName(), " + bean + ".errors());");  
  out.println("      Toast.makeText(myContext, \"Errors: \" + " + bean + ".errors(), Toast.LENGTH_LONG).show();"); 
  out.println("    }"); 
  out.println("    else"); 

  if (res != null && "WebDisplay".equals(res.getType().getName())) 
  { out.println("    { " + op + "Result.loadUrl(" + bean + "." + op + "().url + \"\"); }"); }
  else if (res != null && "ImageDisplay".equals(res.getType().getName())) 
  { out.println("    { Resources _rsrcs = getResources();"); 
    out.println("      AssetManager _amanager = _rsrcs.getAssets();"); 
    out.println("      try {"); 
	out.println("        String _imgName = " + bean + "." + op + "().imageName;"); 
    out.println("        InputStream _imageStream = _amanager.open(_imgName);"); 
    out.println("        Drawable _drawable = new BitmapDrawable(_rsrcs, _imageStream);"); 
    out.println("        " + op + "Result.setImageDrawable(_drawable);"); 
    out.println("      }  catch (Exception _e) { _e.printStackTrace(); }"); 
    out.println("    }"); 
  }
  else if (res != null && "GraphDisplay".equals(res.getType().getName())) 
  { out.println("    { GraphDisplay _result = " + bean + "." + op + "();"); 
    out.println("      " + op + "Result.invalidate();"); 
    out.println("      " + op + "Result.refreshDrawableState();"); 
    out.println("      " + op + "Result.setImageDrawable(_result);"); 
    out.println("    }"); 
  }
  else if (res != null) 
  { out.println("    { " + op + "Result.setText(" + bean + "." + op + "() + \"\"); }"); } 
  else 
  { out.println("    { " + bean + "." + op + "(); }"); }  

  out.println("  }\n\r");
  out.println();

  out.println("  public void " + op + "Cancel(View _v)");
  out.println("  { " + bean + ".resetData();");
  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    if (att.isSmallEnumeration()) { } 
    else if (att.isLargeEnumeration() || att.isEntity()) { } 
    else  
    { String tfnme = op + aname + "TextField"; 
      out.println("    " + tfnme + ".setText(\"\");");
    } 
  } 
  
  if (res != null && "String".equals(res.getType().getName())) 
  { out.println("    " + op + "Result.setText(\"\");"); }
  
  out.println("  }");    
  

  for (int p = 0; p < extensions.size(); p++) 
  { UseCase extension = (UseCase) extensions.get(p); 
    String extop = extension.getName();    
    Vector extpars = extension.getParameters(); 
    Attribute extres = extension.getResultParameter(); 
	
    String extbeanclass = extop + "Bean";
    String extbean = extbeanclass.toLowerCase();
    
    out.println("  public void " + extop + "OK(View _v) ");
    out.println("  { InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);"); 
    out.println("    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }"); 
    for (int x = 0; x < extpars.size(); x++)
    { Attribute att = (Attribute) extpars.get(x);
      String aname = att.getName();
      String tfnme = extop + aname + "TextField"; 
      String dname = extop + aname + "Data";
      if (att.isSmallEnumeration())
      { String attextract = att.extractEnumerationValue(extop,dname); 
        out.println(attextract); 
      }
      else if (att.isLargeEnumeration() || att.isEntity()) { } 
      else 
      { out.println("    " + dname + " = " + tfnme + ".getText() + \"\";"); } 
        out.println("    " + extbean + ".set" + aname + "(" + dname + ");"); 
      } 
	
      out.println("    if (" + extbean + ".is" + extop + "error())"); 
      out.println("    { Log.w(getClass().getName(), " + extbean + ".errors()); "); 
      out.println("      Toast.makeText(myContext, \"Errors: \" + " + extbean + ".errors(), Toast.LENGTH_LONG).show();"); 
      out.println("    }");
      out.println("    else"); 

      if (extres != null && "WebDisplay".equals(extres.getType().getName())) 
      { out.println("    { " + extop + "Result.loadUrl(" + extbean + "." + extop + "().url + \"\"); }"); }
      else if (extres != null && "ImageDisplay".equals(extres.getType().getName())) 
      { out.println("    { Resources _rsrcs = getResources();"); 
        out.println("      AssetManager _amanager = _rsrcs.getAssets();"); 
        out.println("      try {"); 
	    out.println("        String _imgName = " + extbean + "." + extop + "().imageName;"); 
        out.println("        InputStream _imageStream = _amanager.open(_imgName);"); 
        out.println("        Drawable _drawable = new BitmapDrawable(_rsrcs, _imageStream);"); 
        out.println("        " + extop + "Result.setImageDrawable(_drawable);"); 
        out.println("      }  catch (Exception _e) { _e.printStackTrace(); }"); 
        out.println("    }"); 
      }
      else if (extres != null && "GraphDisplay".equals(extres.getType().getName())) 
      { out.println("    { GraphDisplay _result = " + extbean + "." + extop + "();"); 
        out.println("      " + extop + "Result.invalidate();"); 
        out.println("      " + extop + "Result.refreshDrawableState();"); 
        out.println("      " + extop + "Result.setImageDrawable(_result);"); 
        out.println("    }"); 
      }
      else if (extres != null) 
      { out.println("    { " + extop + "Result.setText(" + extbean + "." + extop + "() + \"\"); }"); } 
      else 
      { out.println("    { " + extbean + "." + extop + "(); }"); }
  
      out.println("  }\n\r");
      out.println();
   } 
   
   for (int i = 0; i < extraops.size(); i++)
   { out.println(extraops.get(i)); }  
  
   out.println("}"); 
 }

  public static void androidMainVCTabs(Vector usecases, String appPackage, boolean hasGraph, PrintWriter out)
  { out.println("package " + appPackage + ";"); 

    out.println("import android.os.Bundle;"); 
    out.println("import com.google.android.material.floatingactionbutton.FloatingActionButton;");
    out.println("import com.google.android.material.snackbar.Snackbar;");
    out.println("import com.google.android.material.tabs.TabLayout;");
    out.println("");
    out.println("import androidx.fragment.app.Fragment;");
    out.println("import androidx.fragment.app.FragmentPagerAdapter;");
    out.println("import androidx.viewpager.widget.ViewPager;");
    out.println("import androidx.appcompat.app.AppCompatActivity;");
    out.println("");
    out.println("import androidx.fragment.app.FragmentManager;");
    out.println("import android.view.View;");
	out.println("import android.view.Menu;"); 
    out.println("import android.view.MenuItem;"); 

    out.println("");
    out.println("import " + appPackage + ".ui.main.SectionsPagerAdapter;");
	out.println("import " + appPackage + ".ui.main.ModelFacade;"); 

	String extraimports = ""; 
	Vector extraops = new Vector(); 
	Vector extraimplements = new Vector(); 
	
	for (int i = 0; i < usecases.size(); i++)
	{ Object uc = usecases.get(i); 
	  if (uc instanceof OperationDescription)
	  { OperationDescription od = (OperationDescription) uc; 
	    String odnme = od.getName(); 
		if (odnme.startsWith("list"))
		{ Entity ent = od.getEntity();
		  String ename = ent.getName();  
		  String fullname = odnme + ename; 
		  extraimports = extraimports + "import " + appPackage + ".ui.main.list" + ename + "Fragment;\n"; 
		  extraimports = extraimports + "import " + appPackage + ".ui.main." + ename + "VO;\n\n"; 
		  extraops.add("   public void onListFragmentInteraction(" + ename + "VO x)\n" + 
                       "   { model.setSelected" + ename + "(x); }\n"); 
          extraimplements.add("list" + ename + "Fragment.OnListFragmentInteractionListener"); 
		}
	  } 
    } 
	out.println(extraimports); 
    out.println("");
    out.println("public class MainActivity extends AppCompatActivity");
	if (extraimplements.size() == 0) { } 
	else 
	{ out.print("        implements "); 
	  for (int k = 0; k < extraimplements.size(); k++) 
	  { String extimp = (String) extraimplements.get(k);
	    out.print(extimp); 
		if (k < extraimplements.size() - 1)
		{ out.print(", "); }
	  }
	  out.println();  
	} 
	
    out.println("{ ModelFacade model; ");
    out.println(""); 
    out.println("  @Override");
    out.println("  protected void onCreate(Bundle savedInstanceState)"); 
    out.println("  { super.onCreate(savedInstanceState);");
    out.println("    setContentView(R.layout.activity_main);");
    out.println("    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());");
    out.println("    ViewPager viewPager = findViewById(R.id.view_pager);");
    out.println("    viewPager.setAdapter(sectionsPagerAdapter);");
    out.println("    TabLayout tabs = findViewById(R.id.tabs);");
    out.println("    tabs.setupWithViewPager(viewPager);");
    out.println("    model = ModelFacade.getInstance(this);"); 
    out.println("  }");
    for (int j = 0; j < extraops.size(); j++) 
    { out.println(); 
	 out.println((String) extraops.get(j)); 
    }
    out.println("}"); 
  } 
  
  
  public static void generatePagerAdapter(String packageName, Vector tabNames, PrintWriter out)
  { String tabtitles = "";
    for (int x = 0; x < tabNames.size(); x++)
    { String tt = (String) tabNames.get(x);
	  String title = Named.capitalise(tt); 
	  if (tt.startsWith("create"))
	  { title = "+" + tt.substring(6,tt.length()); }
	  else if (tt.startsWith("delete"))
	  { title = "-" + tt.substring(6,tt.length()); }
	  else if (tt.startsWith("searchBy"))
	  { title = "?" + tt.substring(8,tt.length()); }

      tabtitles = tabtitles + "\"" + title + "\"";

      if (x < tabNames.size()-1)
      { tabtitles = tabtitles + ", "; }
    }

    out.println("package " + packageName + ".ui.main;");
    out.println("");
    out.println("import android.content.Context;");
    out.println("");
    out.println("import androidx.annotation.Nullable;");
    out.println("import androidx.annotation.StringRes;");
    out.println("import androidx.fragment.app.Fragment;");
    out.println("import androidx.fragment.app.FragmentManager;");
    out.println("import androidx.fragment.app.FragmentPagerAdapter;");
    out.println();
    out.println("import " + packageName + ".R;");
    out.println();
    out.println("public class SectionsPagerAdapter extends FragmentPagerAdapter"); 
    out.println("{");
    out.println("  private static final String[] TAB_TITLES = new String[]{ " + tabtitles + " };");
    out.println("    private final Context mContext;");
    out.println();
    out.println("  public SectionsPagerAdapter(Context context, FragmentManager fm)"); 
    out.println("  { super(fm);");
    out.println("    mContext = context;");
    out.println("  }");
    out.println();
    out.println("  @Override");
    out.println("  public Fragment getItem(int position)"); 
    out.println("  { // instantiate a fragment for the page.");     
    for (int y = 0; y < tabNames.size(); y++)
    { String tb = (String) tabNames.get(y);
      out.println("    if (position == " + y + ")");
      out.println("    { return " + tb + "Fragment.newInstance(mContext); }");
      if (y < tabNames.size()-1)
      { out.println("    else"); }
    }
    out.println("    return " + tabNames.get(tabNames.size() - 1) + "Fragment.newInstance(mContext); "); 
    out.println("  }");
    out.println();
    out.println("  @Nullable");
    out.println(" @Override");
    out.println("  public CharSequence getPageTitle(int position) ");
    out.println("  { return TAB_TITLES[position]; }");
    out.println("");
    out.println("  @Override");
    out.println("  public int getCount()");
    out.println("  { return " + tabNames.size() + "; }");
    out.println("}");
  }


  public static void generateAndroidDbi(String packageName, String appName, Vector ents, Vector operations, PrintWriter out) 
  { // String appName = "app"; // but supply as a parameter
    out.println("package " + packageName + ";");
    out.println();
    out.println();
    out.println("import android.content.Context;");
    out.println("import android.database.sqlite.SQLiteDatabase;");
    out.println("import android.database.sqlite.SQLiteOpenHelper;");
    out.println("import android.content.ContentValues;");
    out.println("import android.database.Cursor;");
    out.println("import java.util.List;");
    out.println("import java.util.ArrayList;");
    out.println();
    out.println("public class Dbi extends SQLiteOpenHelper");
    out.println("{ SQLiteDatabase database;");
    out.println("  private static final String DBNAME = \"" + appName + ".db\";");
    out.println("  private static final int DBVERSION = 1;");
    out.println();
  
    String createCode = ""; 
    for (int i = 0; i < ents.size(); i++) 
    { Entity e0 = (Entity) ents.get(i); 
      e0.androidDbiDeclarations(out);
      String ent = e0.getName(); 
      createCode = createCode + "db.execSQL(" + ent + "_CREATE_SCHEMA);\n  "; 
    }  

    out.println("  public Dbi(Context context)"); 
    out.println("  { super(context, DBNAME, null, DBVERSION); }");
    out.println(); 
    out.println("  @Override");
    out.println("  public void onCreate(SQLiteDatabase db)");
    out.println("  { " + createCode + " }");
    out.println(); 

    for (int i = 0; i < ents.size(); i++) 
    { Entity e0 = (Entity) ents.get(i); 
      e0.androidDbiOperations(out);
    }  

    out.println("  // @Override");
    out.println("  public void onDestroy()");
    out.println("  { // super.onDestroy();");
    out.println("    database.close(); }");
    out.println();
    out.println("  public void onUpgrade(SQLiteDatabase d, int x, int y) {}");
    out.println();
    out.println("}");
  } 

  public static void generateFirebaseDbi(Vector clouds, String appName, String packageName, PrintWriter out) 
  { out.println("package " + packageName + ";"); 
    out.println(); 
    out.println("import java.util.*;"); 
    out.println("import java.util.HashMap;"); 
    out.println("import java.util.Collection;");
    out.println("import java.util.List;");
    out.println("import java.util.ArrayList;");
    out.println("import java.util.Set;");
    out.println("import java.util.HashSet;");
    out.println("import java.util.TreeSet;");
    out.println("import java.util.Collections;");
    out.println("import java.util.StringTokenizer;"); 
    out.println("import java.util.Date; "); 
    out.println("import java.text.DateFormat;");  
    out.println("import java.text.SimpleDateFormat;");  
    out.println("import org.json.JSONArray;"); 
    out.println("import org.json.JSONObject;"); 
    out.println("import org.json.*;"); 
	out.println("import android.net.Uri;"); 
    out.println("import android.os.Bundle;"); 
    out.println("import com.google.android.gms.tasks.OnCompleteListener;"); 
    out.println("import com.google.android.gms.tasks.Task;"); 
    out.println("import com.google.firebase.auth.*;"); 
    out.println("import com.google.firebase.database.*;"); 

    out.println(); 
    out.println("public class FirebaseDbi"); 
    out.println("{ static FirebaseDbi instance = null;");  
    out.println("  DatabaseReference database = null;");  
    out.println(); 
    out.println("  public static FirebaseDbi getInstance()"); 
    out.println("  { if (instance == null)"); 
    out.println("    { instance = new FirebaseDbi(); }"); 
    out.println("    return instance;");  
    out.println("  }");
    out.println(""); 
    out.println("  FirebaseDbi() { }");
	out.println(); 
	out.println("  public void connectByURL(String url)"); 
    out.println("  { database = FirebaseDatabase.getInstance(url).getReference();"); 
    out.println("    if (database == null) { return; }");
    
    for (int i = 0; i < clouds.size(); i++) 
    { Entity ent = (Entity) clouds.get(i); 
      ent.generateCloudUpdateCode(out); 
    } 
    out.println("  }");
    out.println("  ");
    for (int i = 0; i < clouds.size(); i++) 
    { Entity ent = (Entity) clouds.get(i); 
      ent.generateFirebaseOps(out); 
    } 
    out.println("}"); 
  }


  public static void androidLayoutTabs(Vector ucs, PrintWriter out)
  { out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); 
    out.println("<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
    out.println("xmlns:app=\"http://schemas.android.com/apk/res-auto\"");
    out.println("xmlns:tools=\"http://schemas.android.com/tools\"");
    out.println("android:layout_width=\"match_parent\"");
    out.println("android:layout_height=\"match_parent\"");
    out.println("tools:context=\".MainActivity\">");
    out.println("");
    out.println("<com.google.android.material.appbar.AppBarLayout");
    out.println("    android:layout_width=\"match_parent\"");
    out.println("    android:layout_height=\"wrap_content\"");
    out.println("    android:theme=\"@style/AppTheme.AppBarOverlay\">");
    out.println("");
    out.println("    <TextView");
    out.println("        android:id=\"@+id/title\"");
    out.println("        android:layout_width=\"wrap_content\"");
    out.println("        android:layout_height=\"wrap_content\"");
    out.println("        android:gravity=\"center\"");
    out.println("        android:minHeight=\"?actionBarSize\"");
    out.println("        android:padding=\"@dimen/appbar_padding\"");
    out.println("        android:text=\"@string/app_name\"");
    out.println("        android:textAppearance=\"@style/TextAppearance.Widget.AppCompat.Toolbar.Title\" />");
    out.println("");
    out.println("<com.google.android.material.tabs.TabLayout");
    out.println("        android:id=\"@+id/tabs\"");
    out.println("        android:layout_width=\"match_parent\"");
    out.println("        android:layout_height=\"wrap_content\"");
    out.println("        android:background=\"?attr/colorPrimary\" />");
    out.println("</com.google.android.material.appbar.AppBarLayout>");
    out.println("");
    out.println("<androidx.viewpager.widget.ViewPager");
    out.println("    android:id=\"@+id/view_pager\"");
    out.println("    android:layout_width=\"match_parent\"");
    out.println("    android:layout_height=\"match_parent\"");
    out.println("    app:layout_behavior=\"@string/appbar_scrolling_view_behavior\" />");
    out.println("");
    out.println("</androidx.coordinatorlayout.widget.CoordinatorLayout>");
    out.println(""); 
  } 

public static void androidScreenTabs(OperationDescription od, PrintWriter out)
{ String op = od.getAction();
  if (op.startsWith("create"))
  { androidEditScreen(op,od,out); }
  else if (op.startsWith("delete"))
  { androidDeleteScreen(op,od,out); }
  else if (op.startsWith("edit"))
  { androidEditScreen(op,od,out); }
  else if (op.startsWith("list"))
  { fragmentListScreen(op,od,out); }
  else if (op.startsWith("searchBy"))
  { androidSearchByScreen(op,od,out); }
  else 
  { System.err.println("No screen is defined yet for " + op); } 
}

public static void androidCreateScreen(String op, OperationDescription od, PrintWriter out)
{ Entity ent = od.getEntity(); 
  String ename = ent.getName(); 
  String viewname = op + ename + "Fragment";
  String opok = op + ename + "OK";
  String opcancel = op + ename + "Cancel";
  String fullop = op + ename; 

  String oklabel = "OK"; 
  if (op.startsWith("delete"))
  { oklabel = "Delete"; } 

  out.println("<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
  out.println("  xmlns:tools=\"http://schemas.android.com/tools\"");
  out.println("  android:layout_width=\"match_parent\"");
  out.println("  android:layout_height=\"wrap_content\"");
  out.println("  tools:context=\".ui.main." + viewname + "\" >");
  out.println();

  String previous = null; 

  Vector pars = od.getParameters();
  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String labelname = att.androidEntryFieldName(fullop); 
    String attfield = att.androidEntryField(fullop,previous,ename);
    out.println(attfield);
    previous = labelname; 
  }

  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opok + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:text=\"" + oklabel + "\"");
  out.println("    android:onClick=\"" + opok + "\"");
  if (previous != null) 
  { out.println("    android:layout_below=\"@id/" + previous + "\""); } 
  out.println("    android:layout_alignParentLeft=\"true\"/>");

  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opcancel + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:text=\"Cancel\"");
  out.println("    android:onClick=\"" + opcancel + "\"");
  out.println("    android:layout_toRightOf=\"@id/" + opok + "\"");
  out.println("    android:layout_alignTop=\"@id/" + opok + "\"/>");
  out.println("</RelativeLayout>");

}

public static void androidEditScreen(String op, OperationDescription od, PrintWriter out)
{ Entity ent = od.getEntity(); 
  String ename = ent.getName(); 
  String viewname = op + ename + "Fragment";
  String opok = op + ename + "OK";
  String opcancel = op + ename + "Cancel";

  out.println("<TableLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
  out.println("  xmlns:tools=\"http://schemas.android.com/tools\"");
  out.println("  android:layout_width=\"match_parent\"");
  out.println("  android:layout_height=\"match_parent\"");
  out.println("  android:stretchColumns=\"1\"");
  out.println("  tools:context=\".ui.main." + viewname + "\" >");
  out.println();

  Vector pars = od.getParameters();
  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String attfield = att.androidTableEntryField(ename,op);
    out.println(attfield);
  }
  out.println(" <TableRow>");
  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opok + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:layout_column=\"1\"");
  out.println("    android:background=\"#AAAAFF\""); 
  out.println("    android:text=\"OK\"");
  out.println("    android:onClick=\"" + opok + "\"");
  out.println("    />");

  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opcancel + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:layout_column=\"3\"");
  out.println("    android:background=\"#FFAAAA\""); 
  out.println("    android:text=\"Cancel\"");
  out.println("    android:onClick=\"" + opcancel + "\"");
  out.println("    />");
  out.println(" </TableRow>");
  out.println("</TableLayout>");
}

public static void androidDeleteScreen(String op, OperationDescription od, PrintWriter out)
{ Entity ent = od.getEntity(); 
  String ename = ent.getName(); 
  String viewname = op + ename + "Fragment";
  String opok = op + ename + "OK";
  String opcancel = op + ename + "Cancel";

  out.println("<TableLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
  out.println("  xmlns:tools=\"http://schemas.android.com/tools\"");
  out.println("  android:layout_width=\"match_parent\"");
  out.println("  android:layout_height=\"match_parent\"");
  out.println("  android:stretchColumns=\"1\"");
  out.println("  tools:context=\".ui.main." + viewname + "\" >");
  out.println();

  Vector pars = od.getParameters();
  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String attfield = att.androidTableEntryField(ename,op);
    out.println(attfield);
  }

  out.println(" <TableRow>"); 
  
  out.println("  <TextView"); 
  out.println("    android:id=\"@+id/" + op + ename + "Label\""); 
  out.println("    android:hint=\"" + ename + " id\""); 
  out.println("    android:textStyle=\"bold\""); 
  out.println("    android:background=\"#EEFFBB\""); 
  out.println("    android:text=\"" + ename + ":\" />"); 

  out.println("<Spinner"); 
  out.println("  android:id=\"@+id/" + op + ename + "Spinner\""); 
  out.println("  android:layout_width=\"fill_parent\"");  
  out.println("  android:layout_height=\"wrap_content\" "); 
  out.println("  android:layout_span=\"4\" />"); 

  out.println(" </TableRow>"); 

  out.println(" <TableRow>");
  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opok + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:layout_column=\"1\"");
  out.println("    android:background=\"#AAAAFF\""); 
  out.println("    android:text=\"OK\"");
  out.println("    android:onClick=\"" + opok + "\"");
  out.println("    />");

  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opcancel + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:layout_column=\"3\"");
  out.println("    android:background=\"#FFAAAA\""); 
  out.println("    android:text=\"Cancel\"");
  out.println("    android:onClick=\"" + opcancel + "\"");
  out.println("    />");
  out.println(" </TableRow>");
  out.println("</TableLayout>");
}

public static void androidListScreen(String op, OperationDescription od, PrintWriter out)
{ Entity ent = od.getEntity(); 
  String ename = ent.getName(); 
  String viewname = op + ename + "Fragment";
  
  out.println("<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
  out.println("  xmlns:tools=\"http://schemas.android.com/tools\"");
  out.println("  android:layout_width=\"match_parent\"");
  out.println("  android:layout_height=\"match_parent\"");
  out.println("  tools:context=\".ui.main." + viewname + "\" >");
  
  out.println("   <ListView"); 
  out.println("         android:id=\"@android:id/list\""); 
  out.println("         android:layout_width=\"match_parent\"");  
  out.println("         android:layout_height=\"match_parent\""); 
  out.println("         android:drawSelectorOnTop=\"false\" />");     
  out.println("</RelativeLayout>"); 
  out.println();
} 

public static void androidSearchByScreen(String op, OperationDescription od, PrintWriter out)
{ Entity ent = od.getEntity(); 
  String ename = ent.getName(); 
  String viewname = "searchBy" + ename + od.oprole + "Fragment";
  String label = Named.capitalise(od.oprole); 
  String opok = "searchBy" + ent + od.oprole + "OK"; 
  String opcancel = "searchBy" + ent + od.oprole + "Cancel"; 
  Attribute att = ent.getAttribute(od.oprole); 
  String field = "searchBy" + ent + od.oprole + "Field"; 
  
  out.println("<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
  out.println("  xmlns:tools=\"http://schemas.android.com/tools\"");
  out.println("  android:layout_width=\"match_parent\"");
  out.println("  android:layout_height=\"match_parent\"");
  out.println("  tools:context=\".ui.main." + viewname + "\" >");
  out.println("  <TextView"); 
  out.println("        android:id=\"@+id/" + viewname + "Label\"");  
  out.println("        android:textStyle=\"bold\"");
  out.println("        android:text=\"" + label + ":\"");
  out.println("        android:background=\"#EEFFBB\""); 
  out.println("        android:layout_width=\"match_parent\""); 
  out.println("        android:layout_height=\"wrap_content\" />"); 
  out.println("  <EditText"); 
  out.println("    android:id=\"@+id/" + field + "\"");
  if (att.isInteger())
  { out.println("    android:inputType=\"number\""); } 
  else if (att.isDouble())
  { out.println("    android:inputType=\"number|numberDecimal\""); } 
  out.println("    android:layout_toRightOf=\"@id/" + viewname + "Label\"");
  out.println("    android:layout_alignTop=\"@id/" + viewname + "Label\"/>");
      
  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opok + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:text=\"Search\"");
  out.println("    android:background=\"#AAAAFF\""); 
  out.println("    android:onClick=\"" + opok + "\"");
  out.println("    android:layout_below=\"@id/" + viewname + "Label\"");  
  out.println("    android:layout_alignParentLeft=\"true\"/>");

  out.println("  <Button");
  out.println("    android:id=\"@+id/" + opcancel + "\"");
  out.println("    android:layout_width=\"wrap_content\"");
  out.println("    android:layout_height=\"wrap_content\"");
  out.println("    android:background=\"#FFAAAA\""); 
  out.println("    android:text=\"Cancel\"");
  out.println("    android:onClick=\"" + opcancel + "\"");
  out.println("    android:layout_toRightOf=\"@id/" + opok + "\"");
  out.println("    android:layout_alignTop=\"@id/" + opok + "\"/>");
    
  out.println("   <ListView"); 
  out.println("         android:id=\"@android:id/" + viewname + "Results\""); 
  out.println("         android:layout_width=\"match_parent\"");  
  out.println("         android:layout_height=\"match_parent\""); 
  out.println("         android:drawSelectorOnTop=\"false\" />");     
  out.println("</RelativeLayout>"); 
  out.println();
} 

public static void androidViewFragment(String packageName, OperationDescription od, PrintWriter out)
{ String op = od.getAction();
  Entity entity = od.getEntity(); 
    
  if (op.startsWith("create"))
  { androidCreateViewFragment(op,od,packageName,out); }
  else if (op.startsWith("delete"))
  { androidDeleteViewFragment(op,od,packageName,out); }
  else if (op.startsWith("edit"))
  { androidEditViewFragment(op,od,packageName,out); }
  else if (op.startsWith("list"))
  { listViewFragment(entity,od,packageName,out); }
  // else if (op.startsWith("searchBy"))
  // { od.androidSearchByViewActivity(op,packageName,out); }
}

public static void androidCreateViewFragment(String op, OperationDescription od, String packageName, PrintWriter out)
{ Entity ent = od.getEntity();
  String entname = ent.getName();
  String fullop = op + entname; 
  String beanclass = entname + "Bean";
  String bean = beanclass.toLowerCase();
  String lcname = fullop.toLowerCase(); 
  String layout = lcname + "_layout"; 
  
  Vector pars = od.getParameters(); 
  
  String classname = fullop + "Fragment"; 

  String extraimports = ""; 
  String protocols = " implements OnClickListener"; 
  String extraops = ""; 
  
  if (UseCase.hasLargeEnumerationParameter(pars))
  { extraimports = "import android.widget.AdapterView;\n\r" + 
                   "import android.widget.ArrayAdapter;\n\r" + 
                   "import android.widget.Spinner;\n\r"; 
    protocols = protocols + ", AdapterView.OnItemSelectedListener"; 
    extraops = UseCase.spinnerListenerOperations(op,pars); 
  }
  
  out.println("package " + packageName + ".ui.main;\n"); 
  out.println(); 
  out.println("import androidx.appcompat.app.AppCompatActivity;\n\r");
  out.println("import android.os.Bundle;");
  out.println(extraimports);
  out.println("import android.view.LayoutInflater;"); 
  out.println("import android.view.ViewGroup;"); 
  out.println("import android.widget.Button;"); 
  out.println("import android.view.inputmethod.InputMethodManager;") ; 

  out.println("import androidx.annotation.Nullable;"); 
  out.println("import androidx.annotation.NonNull;"); 
  out.println("import androidx.fragment.app.Fragment;"); 

  out.println("import " + packageName + ".R;"); // packageName + ".R"

  out.println("import android.content.Context;"); 

  out.println("import androidx.annotation.LayoutRes;"); 
  out.println("import androidx.fragment.app.FragmentPagerAdapter;"); 
  out.println("import androidx.viewpager.widget.ViewPager;"); 
  // import androidx.appcompat.app.AppCompatActivity;

  out.println("import androidx.fragment.app.FragmentManager;"); 
  out.println("import android.view.View.OnClickListener;"); 

  out.println("import android.view.View;");
  out.println("import android.util.Log;"); 
  out.println("import android.widget.Toast;");
  out.println("import android.widget.RadioGroup;"); 
  out.println("import android.widget.EditText;");
  out.println("import android.widget.TextView;\n\r");
  out.println(); 

  out.println("public class " + classname + " extends Fragment" + protocols);
  out.println("{ View root;"); 
  out.println("  Context myContext;"); 
  out.println("  " + beanclass + " " + bean + ";");
  
  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    String tfnme = pnme + "TextField"; 
    String dname = pnme + "Data";
    if (par.isSmallEnumeration())
    { out.println("  RadioGroup " + pnme + "Group;"); } 
    else if (par.isLargeEnumeration())
    { out.println("  Spinner " + pnme + "Spinner;");  
      out.println("  String[] " + pnme + "ListItems = " + par.androidValueList() + ";"); 
    }
    else 
    { out.println("  EditText " + tfnme + ";"); } 
    out.println("  String " + dname + " = \"\";");
  }
  
  out.println("  Button okButton;"); 
  out.println("  Button cancelButton;"); 

  
  out.println();
  out.println();
  
  out.println(" public " + classname + "() {}"); 
  out.println(); 

  out.println("  public static " + classname + " newInstance(Context c)"); 
  out.println("  { " + classname + " fragment = new " + classname + "();"); 
  out.println("    Bundle args = new Bundle();"); 
  out.println("    fragment.setArguments(args);"); 
  out.println("    fragment.myContext = c;"); 
  out.println("    return fragment;"); 
  out.println("  }"); 
  
  out.println(); 

  out.println("  @Override"); 
  out.println("  public void onCreate(Bundle savedInstanceState)"); 
  out.println("  { super.onCreate(savedInstanceState); }"); 

  out.println(); 

  out.println("  @Override");
  out.println("  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)"); 
  out.println("  { root = inflater.inflate(R.layout." + layout + ", container, false);"); 
  out.println("    Bundle data = getArguments();"); 

  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    if (par.isSmallEnumeration())
    { String gname = pnme + "Group"; 
      out.println("    " + gname + " = (RadioGroup) root.findViewById(R.id." + fullop + pnme + "Group);"); 
    } 
    else if (par.isLargeEnumeration() || par.isEntity())
    { out.println(par.androidSpinnerInitialisation(fullop,pnme,"root.","myContext")); }
    else
    { String tfnme = pnme + "TextField"; 
      out.println("    " + tfnme + " = (EditText) root.findViewById(R.id." + fullop + pnme + "Field);");
    } 
  }
	
  out.println("    " + bean + " = new " + beanclass + "(myContext);");

  out.println("    okButton = root.findViewById(R.id." + fullop + "OK);"); 
  out.println("    okButton.setOnClickListener(this);"); 
  out.println("    cancelButton = root.findViewById(R.id." + fullop + "Cancel);");  
  out.println("    cancelButton.setOnClickListener(this);"); 
  out.println("    return root;"); 
  out.println("  }\n\r");
  out.println(); // for edit, the principal primary key does not have an EditText
  out.println(extraops); 
  out.println(); 
  
  out.println("  public void onClick(View _v)"); 
  out.println("  { InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);"); 
  out.println("    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }"); 
  out.println("    if (_v.getId() == R.id." + fullop + "OK)"); 
  out.println("    { " + fullop + "OK(_v); }"); 
  out.println("    else if (_v.getId() == R.id." + fullop + "Cancel)"); 
  out.println("    { " + fullop + "Cancel(_v); }"); 
  out.println("  }"); 
  out.println(); 
	
  out.println("  public void " + fullop + "OK(View _v) ");
  out.println("  { "); 

  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    String tfnme = aname + "TextField"; 
    String dname = aname + "Data";
    if (att.isSmallEnumeration())
    { String attextract = att.extractEnumerationValue(op,dname); 
      out.println(attextract); 
    } 
    else if (att.isLargeEnumeration()) {}
    else 
    { out.println("    " + dname + " = " + tfnme + ".getText() + \"\";"); } 
    out.println("    " + bean + ".set" + aname + "(" + dname + ");"); 
  }
  out.println("    if (" + bean + ".is" + fullop + "error())"); 
  out.println("    { Log.w(getClass().getName(), " + bean + ".errors());");  
  out.println("      Toast.makeText(myContext, \"Errors: \" + " + bean + ".errors(), Toast.LENGTH_LONG).show();"); 
  out.println("    }"); 
  out.println("    else"); 
  out.println("    { " + bean + "." + fullop + "(); }"); 

  out.println("  }\n\r");
  out.println();

  out.println("  public void " + fullop + "Cancel(View _v)");
  out.println("  { " + bean + ".resetData();");
  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    if (att.isSmallEnumeration()) { } 
    else if (att.isLargeEnumeration()) { } 
    else  
    { String tfnme = aname + "TextField"; 
      out.println("    " + tfnme + ".setText(\"\");");
    } 
  } 
  
  out.println("  }");    
    
   out.println("}"); 
 }

public static void androidEditViewFragment(String op, OperationDescription od, String packageName, PrintWriter out)
{ Entity ent = od.getEntity();
  String ename = ent.getName();
  String evo = ename + "VO"; 
  String fullop = op + ename; 
  String beanclass = ename + "Bean";
  String bean = beanclass.toLowerCase();
  String lcname = fullop.toLowerCase(); 
  String layout = lcname + "_layout"; 
  
  Vector pars = od.getParameters(); 
  
  String classname = fullop + "Fragment"; 

  String extraimports = ""; 
  String protocols = " implements OnClickListener"; 
  String extraops = ""; 
  
  if (UseCase.hasLargeEnumerationParameter(pars))
  { extraimports = "import android.widget.AdapterView;\n\r" + 
                   "import android.widget.ArrayAdapter;\n\r" + 
                   "import android.widget.Spinner;\n\r"; 
    protocols = protocols + ", AdapterView.OnItemSelectedListener"; 
    extraops = UseCase.spinnerListenerOperations(op,pars); 
  }
  
  out.println("package " + packageName + ".ui.main;\n"); 
  out.println(); 
  out.println("import androidx.appcompat.app.AppCompatActivity;\n\r");
  out.println("import android.os.Bundle;");
  out.println(extraimports);
  out.println("import android.view.LayoutInflater;"); 
  out.println("import android.view.ViewGroup;"); 
  out.println("import android.widget.Button;"); 
  out.println("import android.view.inputmethod.InputMethodManager;") ; 

  out.println("import androidx.annotation.Nullable;"); 
  out.println("import androidx.annotation.NonNull;"); 
  out.println("import androidx.fragment.app.Fragment;"); 

  out.println("import " + packageName + ".R;"); // packageName + ".R"

  out.println("import android.content.Context;"); 

  out.println("import androidx.annotation.LayoutRes;"); 
  out.println("import androidx.fragment.app.FragmentPagerAdapter;"); 
  out.println("import androidx.viewpager.widget.ViewPager;"); 
  // import androidx.appcompat.app.AppCompatActivity;

  out.println("import androidx.fragment.app.FragmentManager;"); 
  out.println("import android.view.View.OnClickListener;"); 

  out.println("import android.view.View;");
  out.println("import android.util.Log;"); 
  out.println("import android.widget.Toast;");
  out.println("import android.widget.RadioGroup;"); 
  out.println("import android.widget.EditText;");
  out.println("import android.widget.TextView;\n\r");
  out.println(); 

  out.println("public class " + classname + " extends Fragment" + protocols);
  out.println("{ View root;"); 
  out.println("  Context myContext;"); 
  out.println("  ModelFacade model;"); 
  out.println("  " + beanclass + " " + bean + ";");
  
  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    String tfnme = pnme + "TextField"; 
    String dname = pnme + "Data";
    if (par.isSmallEnumeration())
    { out.println("  RadioGroup " + pnme + "Group;"); } 
    else if (par.isLargeEnumeration())
    { out.println("  Spinner " + pnme + "Spinner;");  
      out.println("  String[] " + pnme + "ListItems = " + par.androidValueList() + ";"); 
    }
    else 
    { out.println("  EditText " + tfnme + ";"); } 
    out.println("  String " + dname + " = \"\";");
  }
  // But the id should not be editable. Fill the fields with the selected item data. 
  
  out.println("  Button okButton;"); 
  out.println("  Button cancelButton;"); 

  
  out.println();
  out.println();
  
  out.println(" public " + classname + "() {}"); 
  out.println(); 

  out.println("  public static " + classname + " newInstance(Context c)"); 
  out.println("  { " + classname + " fragment = new " + classname + "();"); 
  out.println("    Bundle args = new Bundle();"); 
  out.println("    fragment.setArguments(args);"); 
  out.println("    fragment.myContext = c;"); 
  out.println("    return fragment;"); 
  out.println("  }"); 
  
  out.println(); 

  out.println("  @Override"); 
  out.println("  public void onCreate(Bundle savedInstanceState)"); 
  out.println("  { super.onCreate(savedInstanceState); }"); 

  out.println(); 

  out.println("  @Override");
  out.println("  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)"); 
  out.println("  { root = inflater.inflate(R.layout." + layout + ", container, false);"); 
  out.println("    Bundle data = getArguments();");
  out.println("    model = ModelFacade.getInstance(myContext);");  
  out.println("    " + evo + " selectedItem = model.getSelected" + ename + "();"); 
  
  for (int x = 0; x < pars.size(); x++)
  { Attribute par = (Attribute) pars.get(x);
    String pnme = par.getName(); 
    if (par.isSmallEnumeration())
    { String gname = pnme + "Group"; 
      out.println("    " + gname + " = (RadioGroup) root.findViewById(R.id." + fullop + pnme + "Group);");
	  out.println(par.setEnumerationValue(fullop,"selected." + pnme));  
    } // set the selected item value
    else if (par.isLargeEnumeration() || par.isEntity())
    { out.println(par.androidSpinnerInitialisation(fullop,pnme,"root.", "myContext")); }
    else
    { String tfnme = pnme + "TextField"; 
      out.println("    " + tfnme + " = (EditText) root.findViewById(R.id." + fullop + pnme + "Field);");
	  out.println("    if (selected != null)");
	  out.println("    { " + tfnme + ".setText(selected." + pnme + " + \"\"); }"); 
    } 
  }
	
  out.println("    " + bean + " = new " + beanclass + "(myContext);");

  out.println("    okButton = root.findViewById(R.id." + fullop + "OK);"); 
  out.println("    okButton.setOnClickListener(this);"); 
  out.println("    cancelButton = root.findViewById(R.id." + fullop + "Cancel);");  
  out.println("    cancelButton.setOnClickListener(this);"); 
  out.println("    return root;"); 
  out.println("  }\n\r");
  out.println(); // for edit, the principal primary key does not have an EditText
  out.println(extraops); 
  out.println(); 
  
  out.println("  public void onClick(View _v)"); 
  out.println("  { InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);"); 
  out.println("    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }"); 
  out.println("    if (_v.getId() == R.id." + fullop + "OK)"); 
  out.println("    { " + fullop + "OK(_v); }"); 
  out.println("    else if (_v.getId() == R.id." + fullop + "Cancel)"); 
  out.println("    { " + fullop + "Cancel(_v); }"); 
  out.println("  }"); 
  out.println(); 
	
  out.println("  public void " + fullop + "OK(View _v) ");
  out.println("  { "); 

  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    String tfnme = aname + "TextField"; 
    String dname = aname + "Data";
    if (att.isSmallEnumeration())
    { String attextract = att.extractEnumerationValue(op,dname); 
      out.println(attextract); 
    } 
    else if (att.isLargeEnumeration()) {}
    else 
    { out.println("    " + dname + " = " + tfnme + ".getText() + \"\";"); } 
    out.println("    " + bean + ".set" + aname + "(" + dname + ");"); 
  }
  out.println("    if (" + bean + ".is" + fullop + "error())"); 
  out.println("    { Log.w(getClass().getName(), " + bean + ".errors());");  
  out.println("      Toast.makeText(myContext, \"Errors: \" + " + bean + ".errors(), Toast.LENGTH_LONG).show();"); 
  out.println("    }"); 
  out.println("    else"); 
  out.println("    { " + bean + "." + fullop + "(); }"); 

  out.println("  }\n\r");
  out.println();

  out.println("  public void " + fullop + "Cancel(View _v)");
  out.println("  { " + bean + ".resetData();");
  for (int x = 0; x < pars.size(); x++)
  { Attribute att = (Attribute) pars.get(x);
    String aname = att.getName();
    if (att.isSmallEnumeration()) { } 
    else if (att.isLargeEnumeration()) { } 
    else  
    { String tfnme = aname + "TextField"; 
      out.println("    " + tfnme + ".setText(\"\");");
    } 
  } 
  
  out.println("  }");    
    
   out.println("}"); 
 }

public static void androidDeleteViewFragment(String op, OperationDescription od, String packageName, PrintWriter out)
{ Entity ent = od.getEntity();
  String ename = ent.getName();
  String evo = ename + "VO"; 
  String fullop = op + ename; 
  String beanclass = ename + "Bean";
  String bean = beanclass.toLowerCase();
  String lcname = fullop.toLowerCase(); 
  String layout = lcname + "_layout"; 
  
  Vector pars = od.getParameters(); 
  
  String classname = fullop + "Fragment"; 

  String extraimports = ""; 
  String protocols = " implements OnClickListener"; 
  String extraops = ""; 
  
  extraimports = "import android.widget.AdapterView;\n\r" + 
                 "import android.widget.ArrayAdapter;\n\r" + 
                 "import android.widget.Spinner;\n\r"; 
  protocols = protocols + ", AdapterView.OnItemSelectedListener"; 
  // extraops = UseCase.spinnerListenerOperations(op,pars); 
  
  
  out.println("package " + packageName + ".ui.main;\n"); 
  out.println(); 
  out.println("import androidx.appcompat.app.AppCompatActivity;\n\r");
  out.println("import android.os.Bundle;");
  out.println(extraimports);
  out.println("import android.view.LayoutInflater;"); 
  out.println("import android.view.ViewGroup;"); 
  out.println("import android.widget.Button;"); 
  out.println("import android.view.inputmethod.InputMethodManager;") ; 

  out.println("import androidx.annotation.Nullable;"); 
  out.println("import androidx.annotation.NonNull;"); 
  out.println("import androidx.fragment.app.Fragment;"); 

  out.println("import " + packageName + ".R;"); // packageName + ".R"

  out.println("import android.content.Context;"); 

  out.println("import androidx.annotation.LayoutRes;"); 
  out.println("import androidx.fragment.app.FragmentPagerAdapter;"); 
  out.println("import androidx.viewpager.widget.ViewPager;"); 
  // import androidx.appcompat.app.AppCompatActivity;

  out.println("import androidx.fragment.app.FragmentManager;"); 
  out.println("import android.view.View.OnClickListener;"); 

  out.println("import android.view.View;");
  out.println("import android.util.Log;"); 
  out.println("import android.widget.Toast;");
  out.println("import android.widget.RadioGroup;"); 
  out.println("import android.widget.EditText;");
  out.println("import android.widget.TextView;\n\r");
  out.println("import java.util.ArrayList;"); 
  out.println("import java.util.List;"); 
  out.println(); 

  out.println("public class " + classname + " extends Fragment" + protocols);
  out.println("{ View root;"); 
  out.println("  Context myContext;"); 
  out.println("  ModelFacade model;"); 
  out.println("  " + beanclass + " " + bean + ";");
  out.println("  Spinner " + lcname + "Spinner;");
  out.println("  List<String> all" + ename + "ids = new ArrayList<String>();"); 
  
  Attribute par = ent.getPrincipalPrimaryKey();
  if (par == null) 
  { out.println("}"); 
    return; 
  } 
  String pname = par.getName(); 
  String tfnme = pname + "TextField"; 
  String dname = pname + "Data";
  out.println("  EditText " + tfnme + ";");  
  out.println("  String " + dname + " = \"\";");
  
  out.println("  Button okButton;"); 
  out.println("  Button cancelButton;"); 

  
  out.println();
  out.println();
  
  out.println(" public " + classname + "() {}"); 
  out.println(); 

  out.println("  public static " + classname + " newInstance(Context c)"); 
  out.println("  { " + classname + " fragment = new " + classname + "();"); 
  out.println("    Bundle args = new Bundle();"); 
  out.println("    fragment.setArguments(args);"); 
  out.println("    fragment.myContext = c;"); 
  out.println("    return fragment;"); 
  out.println("  }"); 
  
  out.println(); 

  out.println("  @Override"); 
  out.println("  public void onCreate(Bundle savedInstanceState)"); 
  out.println("  { super.onCreate(savedInstanceState); }"); 

  out.println(); 

  out.println("  @Override");
  out.println("  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)"); 
  out.println("  { root = inflater.inflate(R.layout." + layout + ", container, false);"); 
  out.println("    Bundle data = getArguments();");
  out.println("    return root;"); 
  out.println("  }"); 
  out.println(); 
  
  out.println("  @Override");
  out.println("  public void onResume()"); 
  out.println("  { super.onResume();"); 
  out.println("    model = ModelFacade.getInstance(myContext);");  
  out.println("    " + evo + " selectedItem = model.getSelected" + ename + "();"); 
  out.println("    all" + ename + "ids = model.all" + ename + "ids();"); 
  out.println("    " + tfnme + " = (EditText) root.findViewById(R.id." + fullop + pname + "Field);");
  out.println("    " + lcname + "Spinner = (Spinner) root.findViewById(R.id.delete" + ename + "Spinner);");
  out.println("    ArrayAdapter<String> " + lcname + "Adapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_item,all" + ename + "ids);"); 
  out.println("    " + lcname + "Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);"); 
  out.println("    " + lcname + "Spinner.setAdapter(" + lcname + "Adapter);"); 
  out.println("    " + lcname + "Spinner.setOnItemSelectedListener(this);"); 

  out.println("    if (selectedItem != null)");
  out.println("    { " + tfnme + ".setText(selectedItem." + pname + " + \"\"); }"); 
  
  out.println("    " + bean + " = new " + beanclass + "(myContext);");

  out.println("    okButton = root.findViewById(R.id." + fullop + "OK);"); 
  out.println("    okButton.setOnClickListener(this);"); 
  out.println("    cancelButton = root.findViewById(R.id." + fullop + "Cancel);");  
  out.println("    cancelButton.setOnClickListener(this);"); 
  out.println("  }\n\r");
  out.println(); // for edit, the principal primary key does not have an EditText
  out.println(); 
  
  out.println("  public void onItemSelected(AdapterView<?> _parent, View _v, int _position, long _id)"); 
  out.println("  {  if (_parent == " + lcname + "Spinner)"); 
  out.println("     { " + tfnme + ".setText(all" + ename + "ids.get(_position)); }"); 
  out.println("  }"); 
  out.println(); 

  out.println("  public void onNothingSelected(AdapterView<?> _parent) { }"); 
  out.println(); 

  out.println("  public void onClick(View _v)"); 
  out.println("  { InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);"); 
  out.println("    try { _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0); } catch (Exception _e) { }"); 
  out.println("    if (_v.getId() == R.id." + fullop + "OK)"); 
  out.println("    { " + fullop + "OK(_v); }"); 
  out.println("    else if (_v.getId() == R.id." + fullop + "Cancel)"); 
  out.println("    { " + fullop + "Cancel(_v); }"); 
  out.println("  }"); 
  out.println(); 
	
  out.println("  public void " + fullop + "OK(View _v) ");
  out.println("  { "); 

  out.println("    " + dname + " = " + tfnme + ".getText() + \"\";"); 
  out.println("    " + bean + ".set" + pname + "(" + dname + ");"); 
  out.println("    if (" + bean + ".is" + fullop + "error())"); 
  out.println("    { Log.w(getClass().getName(), " + bean + ".errors());");  
  out.println("      Toast.makeText(myContext, \"Errors: \" + " + bean + ".errors(), Toast.LENGTH_LONG).show();"); 
  out.println("    }"); 
  out.println("    else"); 
  out.println("    { " + bean + "." + fullop + "(); }"); 

  out.println("  }\n\r");
  out.println();

  out.println("  public void " + fullop + "Cancel(View _v)");
  out.println("  { " + bean + ".resetData();");
  out.println("    " + tfnme + ".setText(\"\");");
  out.println("  }");    
    
   out.println("}"); 
 }

   public static void fragmentListScreen(String op, OperationDescription od, PrintWriter out)
   { Entity ent = od.getEntity(); 
     String ename = ent.getName(); 
     out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); 
     out.println("<androidx.recyclerview.widget.RecyclerView xmlns:android=\"http://schemas.android.com/apk/res/android\""); 
     out.println("xmlns:app=\"http://schemas.android.com/apk/res-auto\""); 
     out.println("xmlns:tools=\"http://schemas.android.com/tools\""); 
     out.println("android:id=\"@+id/list" + ename + "\""); 
     out.println("android:layout_width=\"match_parent\""); 
     out.println("android:layout_height=\"match_parent\""); 
     out.println("android:layout_marginLeft=\"16dp\""); 
     out.println("android:layout_marginRight=\"16dp\""); 
     out.println("app:layoutManager=\"LinearLayoutManager\""); 
     out.println("tools:context=\".ui.main.list" + ename + "Fragment\""); 
     out.println("tools:listitem=\"@layout/fragment_" + ename.toLowerCase() + "\" />"); 
  } 
  
public static void listItemLayout(Entity ent, PrintWriter out)
{ Vector atts = ent.getAttributes();
  String ename = ent.getName();
  String op = "list" + ename;
  out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
  out.println("<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
  out.println("  android:layout_width=\"wrap_content\"");
  out.println("  android:layout_height=\"wrap_content\"");
  out.println("  android:columnCount=\"3\""); 
  out.println("  android:orientation=\"horizontal\">");

   for (int x = 0; x < atts.size(); x++)
   { Attribute att = (Attribute) atts.get(x);
     // if (att.isSummary())
	 if (att.isHidden() || att.isPassword()) { } 
	 else 
     { String attname = att.getName();
       String attView = op + attname + "View";
       out.println("    <TextView");
       out.println("     android:id=\"@+id/" + attView + "\"");     
       out.println("     android:layout_width=\"wrap_content\"");
       out.println("     android:layout_height=\"wrap_content\"");
	   if (att.isIdentity())
	   { out.println("     android:background=\"#99FFFF\""); }
       out.println("     android:textAppearance=\"?attr/textAppearanceListItem\" />");
    }
  } 
  out.println("</GridLayout>");
}


public static void listViewFragment(Entity ent, OperationDescription op, String packageName, PrintWriter out)
{ String ename = ent.getName();
  String layout = "list" + ename.toLowerCase() + "_layout";
  String evo = ename + "VO";

  out.println("package " + packageName + ".ui.main;");
  out.println("");
  out.println("import android.content.Context;");
  out.println("import android.os.Bundle;");

  out.println("import androidx.fragment.app.Fragment;");
  out.println("import androidx.recyclerview.widget.GridLayoutManager;");
  out.println("import androidx.recyclerview.widget.LinearLayoutManager;");
  out.println("import androidx.recyclerview.widget.RecyclerView;");

  out.println("import android.view.LayoutInflater;");
  out.println("import android.view.View;");
  out.println("import android.view.ViewGroup;");
  out.println("");
  out.println("import " + packageName + ".R;");
  out.println("");
  out.println("public class list" + ename + "Fragment extends Fragment");
  out.println("{ private static final String ARG_COLUMN_COUNT = \"column-count\";");
  out.println("  private int mColumnCount = 1;");
  out.println("  private OnListFragmentInteractionListener mListener;");
  out.println("  private ModelFacade model;");
  out.println("  private Context myContext;");
  out.println("  private View root;");
  out.println();
  out.println("  public list" + ename + "Fragment() { }");
  out.println();
  out.println("  @SuppressWarnings(\"unused\")");
  out.println("  public static list" + ename + "Fragment newInstance(Context context)");
  out.println("  { list" + ename + "Fragment fragment = new list" + ename + "Fragment();");
  out.println("    Bundle args = new Bundle();");
  out.println("    args.putInt(ARG_COLUMN_COUNT, 1);");
  out.println("    fragment.setArguments(args);");
  out.println("    fragment.myContext = context;");
  out.println("    return fragment;");
  out.println("  }");
  out.println("");
  out.println("  @Override");
  out.println("  public void onCreate(Bundle savedInstanceState)");
  out.println("  { super.onCreate(savedInstanceState);");
  out.println("    if (getArguments() != null)"); 
  out.println("    { mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT); }");
  out.println("  }");
  out.println("");
  out.println("  @Override");
  out.println("  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
  out.println("  { View view = inflater.inflate(R.layout." + layout + ", container, false);");
  out.println("    model = ModelFacade.getInstance(myContext);");
    // Set the adapter
  out.println("    if (view instanceof RecyclerView)"); 
  out.println("    { Context context = view.getContext();");
  out.println("      RecyclerView recyclerView = (RecyclerView) view;");
  out.println("      if (mColumnCount <= 1)");   
  out.println("      { recyclerView.setLayoutManager(new LinearLayoutManager(context)); }");
  out.println("      else");
  out.println("      { recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount)); }");
  out.println("    }");
  out.println("    root = view;");
  out.println("    return view;");
  out.println("  }");
  out.println("");
  out.println("  @Override");
  out.println("  public void onAttach(Context context) ");
  out.println("  { super.onAttach(context);");
  out.println("    if (context instanceof OnListFragmentInteractionListener) ");
  out.println("    { mListener = (OnListFragmentInteractionListener) context; }");
  out.println("    else");
  out.println("    { throw new RuntimeException(context.toString() + \" must implement OnListFragmentInteractionListener\"); }");
  out.println("  }");
  out.println(); 
  out.println("  @Override"); 
  out.println("  public void onResume()"); 
  out.println("  { super.onResume();");  
  out.println("    ((RecyclerView) root).setAdapter(new " + ename + "RecyclerViewAdapter(model.list" + ename + "(), mListener));");
  out.println("  }"); 
  out.println("");
  out.println("  @Override");
  out.println("  public void onDetach()");
  out.println("  { super.onDetach();");
  out.println("    mListener = null;");
  out.println("  }");
  out.println("");
  out.println("  public interface OnListFragmentInteractionListener {");
  // TODO: Update argument type and name
  out.println("    void onListFragmentInteraction(" + evo + " item);");
  out.println("  }");
  out.println("}");
}

public static void generateRecyclerViewAdapter(Entity ent, String packageName, PrintWriter out)
{ String ename = ent.getName();
  String op = "list" + ename;
  Vector atts = ent.getAttributes();
  String elc = ename.toLowerCase();
  String evo = ename + "VO";
  
  out.println("package " + packageName + ".ui.main;");
  out.println("");
  out.println("import androidx.recyclerview.widget.RecyclerView;");

  out.println("import android.view.LayoutInflater;");
  out.println("import android.view.View;");
  out.println("import android.view.ViewGroup;");
  out.println("import android.widget.TextView;");

  out.println("import " + packageName + ".R;");

  out.println("import " + packageName + ".ui.main." + op + "Fragment.OnListFragmentInteractionListener;");
  out.println("import java.util.List;");
  out.println();
  out.println("public class " + ename + "RecyclerViewAdapter extends RecyclerView.Adapter<" + ename + "RecyclerViewAdapter.ViewHolder>");
  out.println("{ private final List<" + evo + "> mValues;");
  out.println("  private final OnListFragmentInteractionListener mListener;");
  out.println();
  out.println("  public " + ename + "RecyclerViewAdapter(List<" + evo + "> items, OnListFragmentInteractionListener listener)"); 
  out.println("  { mValues = items;");
  out.println("    mListener = listener;");
  out.println("  }");
  out.println("");  
  out.println("  @Override");
  out.println("  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)"); 
  out.println("  { View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_" + elc + ", parent, false);");
  out.println("    return new ViewHolder(view);");
  out.println("  }");
  out.println("  ");
  out.println("  @Override");
  out.println("  public void onBindViewHolder(final ViewHolder holder, int position)");
  out.println("  { holder.mItem = mValues.get(position);");
  for (int x = 0; x < atts.size(); x++)
  { Attribute att = (Attribute) atts.get(x);
    if (att.isHidden() || att.isPassword()) { } 
	else 
    { String attname = att.getName();
      String attView = op + attname + "View";
      out.println("  holder." + attView + ".setText(\" \" +  mValues.get(position)." + attname + " + \" \");");
    }
  }
  out.println(" "); 
  out.println("  holder.mView.setOnClickListener(new View.OnClickListener() {");
  out.println("      @Override");
  out.println("      public void onClick(View v) {"); 
  out.println("        if (null != mListener)");
  out.println("        { mListener.onListFragmentInteraction(holder.mItem); }");
  out.println("   }");   
  out.println("     });");
  out.println("   }");
  out.println("  ");
  out.println("  @Override");
  out.println("  public int getItemCount()");
  out.println("  { return mValues.size(); }");
  out.println("   ");
  out.println("  public class ViewHolder extends RecyclerView.ViewHolder ");
  out.println("  { public final View mView;");
  for (int x = 0; x < atts.size(); x++)
  { Attribute att = (Attribute) atts.get(x);
    if (att.isHidden() || att.isPassword()) { } 
	else 
    { String attname = att.getName();
      String attView = op + attname + "View";
      out.println("    public final TextView " + attView + ";");
    } 
  }
  out.println("    public " + evo + " mItem;");
  out.println("");   
  out.println("    public ViewHolder(View view)");
  out.println("    { super(view);");
  out.println("       mView = view;");
  for (int x = 0; x < atts.size(); x++)
  { Attribute att = (Attribute) atts.get(x);
    if (att.isHidden() || att.isPassword()) { } 
	else 
    { String attname = att.getName();
      String attView = op + attname + "View";
      out.println("     " + attView + " = (TextView) view.findViewById(R.id." + op +  attname + "View);");
	}
  }
  out.println("    }");
  out.println("");
  out.println("     @Override");
  out.println("     public String toString() ");
  out.println("     { return super.toString() + \" \" + mItem; }");
  out.println("      ");
  out.println("  }");
  out.println("}");
}

 
  public static void generateInternetAccessor(String systemName, String packageName)
  { String entfile = "InternetAccessor.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 

      out.println("import android.os.AsyncTask;"); 
      out.println("import android.os.Bundle;"); 

      out.println("import java.io.BufferedReader;"); 
      out.println("import java.io.IOException;"); 
      out.println("import java.io.InputStream;");
      out.println("import java.io.InputStreamReader;");
      out.println("import java.net.HttpURLConnection;");
      out.println("import java.net.URL;");
      out.println("import android.content.Context;");
      out.println("");
      out.println("public class InternetAccessor extends AsyncTask<String, Void, String>");
      out.println("{  private InternetCallback delegate = null;");
      out.println("   private static InternetAccessor instance = null;");
      out.println("");
      out.println("   public void setDelegate(InternetCallback c)");
      out.println("   { delegate = c; }");
      out.println("");
      out.println("   public static InternetAccessor getInstance()");
      out.println("   { if (instance == null) "); 
	  out.println("     { instance = new InternetAccessor(); }");
	  out.println("     return instance;"); 
	  out.println("   }"); 
      out.println("");
      out.println("   public static InternetAccessor createInternetAccessor()");
      out.println("   { InternetAccessor newinstance = new InternetAccessor();");
	  out.println("     return newinstance;"); 
	  out.println("   }"); 
      out.println("");
      out.println("   @Override");
      out.println("   protected void onPreExecute() {}");
      out.println("");
      out.println("   @Override");
      out.println("   protected String doInBackground(String... params)");
      out.println("   { String url = params[0];");
      out.println("     String myData = \"\";");
      out.println("     try { myData = fetchUrl(url); }");
      out.println("     catch (Exception _e)");
      out.println("     { delegate.internetAccessCompleted(null);");
      out.println("       return null;");
      out.println("     }");
      out.println("     return myData;");
      out.println("   }");
      out.println("");
      out.println("   private String fetchUrl(String url)");
      out.println("   { String urlContent = \"\";");
      out.println("     StringBuilder myStrBuff = new StringBuilder();");
      out.println("");
      out.println("     try{");
      out.println("          URL myUrl = new URL(url);");
      out.println("          HttpURLConnection myConn = (HttpURLConnection) myUrl.openConnection();");
      out.println("          myConn.setRequestProperty(\"User-Agent\", \"\");");
      out.println("          myConn.setRequestMethod(\"GET\");");
      out.println("          myConn.setDoInput(true);");
      out.println("          myConn.connect();");
      out.println("");
      out.println("          InputStream myInStrm = myConn.getInputStream();");
      out.println("          BufferedReader myBuffRdr = new BufferedReader(new InputStreamReader(myInStrm));");
      out.println("");
      out.println("          while ((urlContent = myBuffRdr.readLine()) != null) {");
      out.println("              myStrBuff.append(urlContent + '\\n');");
      out.println("          }");
      out.println("");
      out.println("      } catch (IOException e) {");
      out.println("          delegate.internetAccessCompleted(null);");
      out.println("          return null;");
      out.println("      }");
      out.println("");
      out.println("      return myStrBuff.toString();");
      out.println("  }");
      out.println("");
      out.println("  @Override");
      out.println("  protected void onPostExecute(String result) {");
      out.println("      delegate.internetAccessCompleted(result);");
      out.println("  }");
      out.println("");
      out.println("  @Override");
      out.println("  protected void onProgressUpdate(Void... values) {}");
      out.println(" }");  
	  out.println(""); 
	  out.println("interface InternetCallback"); 
      out.println("{ public void internetAccessCompleted(String response); }"); 
      out.println(""); 
	  out.println(""); 
	  out.close(); 
	} catch (Exception _e) { }  
  } 
  
  public static void generateFileAccessor(int screencount, String systemName, String packageName)
  { String entfile = "FileAccessor.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 

      out.println("import android.content.Context;");
      out.println("import android.app.Activity;");
      out.println("import android.os.Bundle;");
      out.println("");
      out.println("");
      out.println("import java.util.ArrayList;");
      out.println("import java.util.HashMap; ");
      out.println("import java.util.StringTokenizer;");
      out.println("");
      out.println("import android.widget.Toast;");
      out.println("");
      out.println("import java.io.BufferedReader;");
      out.println("import java.io.IOException;");
      out.println("import java.io.InputStream;");
      out.println("import java.io.InputStreamReader;");
      out.println("import java.io.OutputStreamWriter;");
      out.println("import java.io.File;");
      out.println("");
      out.println("public class FileAccessor");
      out.println("{ static Context myContext;");
      out.println("");
      out.println("  public FileAccessor(Context context)");
      out.println("  { myContext = context; }");
      out.println("");
      out.println("  public static void createFile(String filename)");
      out.println("  { try ");
      out.println("    { File newFile = new File(myContext.getFilesDir(), filename); }");
      out.println("    catch (Exception _e) { _e.printStackTrace(); }");
      out.println("  }"); 
      out.println("");
      out.println("   public static ArrayList<String> readFile(String filename)");
      out.println("   { ArrayList<String> result = new ArrayList<String>();");
      out.println("");
      out.println("     try {");
      out.println("           InputStream inStrm = myContext.openFileInput(filename);");
      out.println("           if (inStrm != null)");
      out.println("           { InputStreamReader inStrmRdr = new InputStreamReader(inStrm);");
      out.println("             BufferedReader buffRdr = new BufferedReader(inStrmRdr);");
      out.println("             String fileContent;");
      out.println("");
      out.println("             while ((fileContent = buffRdr.readLine()) != null)"); 
      out.println("               { result.add(fileContent); }");
      out.println("             inStrm.close();");
      out.println("           }");
      out.println("       } catch (Exception _e) { _e.printStackTrace(); }");
      out.println("     return result;");
      out.println("   }");
      out.println("");
      out.println("   public static void writeFile(String filename, ArrayList<String> contents)");
      out.println("   { try {");
      out.println("       OutputStreamWriter outStrm =");
      out.println("               new OutputStreamWriter(myContext.openFileOutput(filename, Context.MODE_PRIVATE));");
      out.println("       try {");
      out.println("         for (int i = 0; i < contents.size(); i++)");
      out.println("         { outStrm.write(contents.get(i) + \"\\n\"); }");
      out.println("       }");
      out.println("       catch (IOException _ix) { }"); 
      out.println("       outStrm.close();");
      out.println("     }");
      out.println("     catch (Exception e) { e.printStackTrace(); }");
      out.println("   }");
      out.println("");
      out.println("}"); 
      out.close();  
    } catch (Exception _e) { }  
  }
  
  public static void generateGraphComponentLayout(PrintWriter out)
  { 
    out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); 
    out.println("<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android=\"http://schemas.android.com/apk/res/android\""); 
    out.println("xmlns:tools=\"http://schemas.android.com/tools\"");
	out.println("xmlns:app=\"http://schemas.android.com/apk/res-auto\""); 
    out.println("android:layout_width=\"match_parent\"");
    out.println("android:layout_height=\"match_parent\"");
    out.println("tools_context=\".ui.main.GraphFragment\" >");
    out.println(); 
    out.println("  <ImageView");
    out.println("    android:id=\"@+id/graphdisplay\"");
    out.println("    android:layout_width=\"fill_parent\"");
    out.println("    android:layout_height=\"fill_parent\"");
    out.println("    android:adjustViewBounds=\"true\" />");
	out.println(); 
	out.println("<com.google.android.material.floatingactionbutton.FloatingActionButton"); 
    out.println("    android:id=\"@+id/fab\"");
    out.println("    android:layout_width=\"wrap_content\"");
    out.println("    android:layout_height=\"wrap_content\"");
    out.println("    android:layout_gravity=\"bottom|end\"");
    out.println("    android:layout_margin=\"@dimen/fab_margin\"");
    out.println("    app:srcCompat=\"@android:drawable/ic_media_next\" />");

    out.println("</androidx.coordinatorlayout.widget.CoordinatorLayout>");
  }

  public static void generateMapComponentLayout(PrintWriter out)
  { 
    out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); 
    out.println("<fragment xmlns:android=\"http://schemas.android.com/apk/res/android\"");
    out.println("xmlns:map=\"http://schemas.android.com/apk/res-auto\"");
    out.println("xmlns:tools=\"http://schemas.android.com/tools\"");
    out.println("android:id=\"@+id/map\"");
    out.println("android:name=\"com.google.android.gms.maps.SupportMapFragment\"");
    out.println("android:layout_width=\"match_parent\"");
    out.println("android:layout_height=\"match_parent\"");
    out.println("tools:context=\".MapsActivity\" />"); 
  } 

public static void generateGraphComponentVC(String systemName, String packageName, String nestedPackage)
{ String entfile = "GraphFragment.java"; 
  File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
  try
  { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
    out.println("package " + nestedPackage + ";"); 
    out.println();
    out.println("import android.os.Bundle;");
    out.println("import android.view.LayoutInflater;");
    out.println("import android.view.View;");
    out.println("import android.view.ViewGroup;");
    out.println("import android.widget.ImageView;");
    out.println("import androidx.coordinatorlayout.widget.CoordinatorLayout;"); 
 
    out.println("import com.google.android.material.floatingactionbutton.FloatingActionButton;");
    out.println("");
    out.println("import androidx.annotation.Nullable;");
    out.println("import androidx.annotation.NonNull;");
    out.println("import androidx.fragment.app.Fragment;");
    out.println("import androidx.lifecycle.Observer;");
    out.println("import androidx.lifecycle.ViewModelProviders;");
    out.println("import java.util.ArrayList;");
    out.println("import android.graphics.Canvas;");
    out.println("import android.content.Context;");
    out.println("");
    out.println("import " + packageName + ".R;");
    out.println("");
    out.println("public class GraphFragment extends Fragment");
    out.println("{ private View root;");
    out.println("  private Context myContext;");
    out.println(); 
    out.println("  public static GraphFragment newInstance(Context context)");
    out.println("  { GraphFragment fragment = new GraphFragment();");
    out.println("    fragment.myContext = context;");
    out.println("    return fragment;");
    out.println("  }");
    out.println("");
    out.println("  @Override");
    out.println("  public void onCreate(Bundle savedInstanceState)");
    out.println("  { super.onCreate(savedInstanceState); }");
    out.println("");
    out.println("  @Override");
    out.println("  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
    out.println("  { root = inflater.inflate(R.layout.graph_fragment, container, false);");
    out.println("    GraphComponent graph = GraphComponent.getInstance();");
    out.println("    ImageView graphview = (ImageView) root.findViewById(R.id.graphdisplay);");
    out.println("    graphview.invalidate();");
    out.println("    graphview.refreshDrawableState();");
    out.println("    graphview.setImageDrawable(graph);");
    out.println("    FloatingActionButton fab = findViewById(R.id.fab);"); 
    out.println(); 
    out.println("    fab.setOnClickListener(new View.OnClickListener() {"); 
    out.println("      @Override"); 
    out.println("            public void onClick(View view) {"); 
    out.println("                GraphComponent gc = GraphComponent.getInstance();"); 
    out.println("                ImageView v = findViewById(R.id.graphdisplay);"); 
    out.println("                v.invalidate();"); 
    out.println("                v.refreshDrawableState();"); 
    out.println("                v.setImageDrawable(gc);"); 
    out.println("                gc.redraw();"); 
    out.println("             }"); 
    out.println("        });"); 
    out.println("    return root;");
    out.println("  }");
    out.println("");
    out.println("  public void onAttach(Context c)");
    out.println("  { super.onAttach(c);");
    out.println("    GraphComponent graph = GraphComponent.getInstance();");
    out.println("    graph.redraw();");
    out.println("  }");
    out.println("");
    out.println("  public void onResume()");
    out.println("  { super.onResume();");
    out.println("    GraphComponent graph = GraphComponent.getInstance();");
    out.println("    ImageView graphview = (ImageView) root.findViewById(R.id.graphdisplay);");
    out.println("    graphview.invalidate();");
    out.println("    graphview.refreshDrawableState();");
    out.println("    graphview.setImageDrawable(graph);");
    out.println("    graphview.refreshDrawableState();");
    out.println("    graph.redraw();");
    out.println("  }");
    out.println("}");
    out.close();  } catch (Exception _e) { }
  }

  public static void generateWebDisplay(String systemName, String packageName)
  { String entfile = "WebDisplay.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 

      out.println("");
      out.println("public class WebDisplay");
      out.println("{ String url = \"https://www.bbc.co.uk\";");
      out.println("");
      out.println("  public WebDisplay()");
      out.println("  { }");
      out.println("");
      out.println("  public void loadURL(String url)");
      out.println("  { this.url = url; }");
      out.println("");
      out.println("  public void reload()");
      out.println("  { }");
      out.println("}"); 
      out.close();  
    } catch (Exception _e) { }  
  }

  public static void generateImageDisplay(String systemName, String packageName)
  { String entfile = "ImageDisplay.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 
      out.println("import java.util.Map;"); 
      out.println("import java.util.HashMap;");
	  // out.println("import "); 

      out.println("");
      out.println("public class ImageDisplay");
      out.println("{ String imageName = \"image1\";");
      out.println(""); 
      out.println("");
      out.println("  public ImageDisplay()");
      out.println("  { }");
      out.println("");
      out.println("  public void setImageName(String name)");
      out.println("  { imageName = name; }");
      out.println("}"); 
      out.close();  
    } catch (Exception _e) { }  
  }
  
  public static void generateBuildGradle(String appName, boolean firebase, PrintWriter out)
  { out.println("apply plugin: 'com.android.application'"); 
    out.println(); 
    out.println("android {");
    out.println("  compileSdkVersion 29");
    out.println("  buildToolsVersion \"29.0.3\"");
    out.println("");
    out.println("  defaultConfig {");
    out.println("    applicationId \"com.example." + appName + "\""); 
    out.println("    minSdkVersion 25");
    out.println("    targetSdkVersion 29");
    out.println("    versionCode 1");
    out.println("    versionName \"1.0\"");
    out.println("");
    out.println("    testInstrumentationRunner \"androidx.test.runner.AndroidJUnitRunner\"");
    out.println("  }");
    out.println("");
    out.println("  compileOptions {");
    out.println("    sourceCompatibility JavaVersion.VERSION_1_8");
    out.println("    targetCompatibility JavaVersion.VERSION_1_8");
    out.println("  }");
    out.println("");
    out.println("  buildTypes {");
    out.println("    release {");
    out.println("        minifyEnabled false");
    out.println("        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'");
    out.println("    }");
    out.println("  }");
    out.println("}");
    out.println("");
    out.println("dependencies {");
    out.println("    implementation fileTree(dir: 'libs', include: ['*.jar'])");
    out.println("");
    out.println("    implementation 'androidx.appcompat:appcompat:1.2.0'");
    out.println("    implementation 'com.google.android.material:material:1.2.1'");
    out.println("    implementation 'androidx.constraintlayout:constraintlayout:2.0.1'");
    out.println("    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'");

    if (firebase)
    { out.println("    implementation platform('com.google.firebase:firebase-bom:26.1.1')"); 
      out.println("    implementation 'com.google.firebase:firebase-auth'"); 
      out.println("    implementation 'com.google.firebase:firebase-database'"); 
    } 

    out.println("    testImplementation 'junit:junit:4.13'");
    out.println("    androidTestImplementation 'androidx.test.ext:junit:1.1.2'");
    out.println("    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'");
    out.println("}");

    if (firebase) 
    { out.println("apply plugin: 'com.google.gms.google-services'"); } 
  }

  public static void generateFirebaseAuthenticator(PrintWriter out, String appName, String packageName)
  { out.println("package " + packageName + ";"); 
    
   out.println("import com.google.android.gms.tasks.OnCompleteListener;");
   out.println("import com.google.android.gms.tasks.Task;");
   out.println("import com.google.firebase.auth.*;");
   out.println(""); 
   out.println("public class FirebaseAuthenticator");
   out.println("{ static FirebaseAuth authenticator = FirebaseAuth.getInstance();");
   out.println(""); 
   out.println("  static FirebaseAuthenticator instance = null; ");
   out.println("");  
   out.println("  public static FirebaseAuthenticator getInstance()");
   out.println("  { if (instance == null)"); 
   out.println("    { instance = new FirebaseAuthenticator(); }");
   out.println("	return instance;"); 
   out.println("  }");
   out.println("");
   out.println("");
   out.println("  public static String signUp(String email, String password)");
   out.println("  {");
   out.println("    final String[] res = new String[1];");
   out.println("    authenticator.createUserWithEmailAndPassword(email,password).addOnCompleteListener(");
   out.println("          new OnCompleteListener<AuthResult>() {");
   out.println("            public void onComplete(Task<AuthResult> task) {");
   out.println("              if (task.isSuccessful()) {");
   out.println("                res[0] = \"Success\";");
   out.println("              } else {");
   out.println("                res[0] = task.getException().getMessage();");
   out.println("              }"); 
   out.println("            }");
   out.println("      });");
   out.println("    return res[0];");
   out.println("  }");
   out.println("");
   out.println("  public static String signIn(String email, String password) ");
   out.println("  { final String[] res = new String[1];");
   out.println("    authenticator.signInWithEmailAndPassword(email,password).addOnCompleteListener(");
   out.println("          new OnCompleteListener<AuthResult>() {");
   out.println("            public void onComplete(Task<AuthResult> task) {");
   out.println("              if (task.isSuccessful()) {");
   out.println("                res[0] = \"Success\";");
   out.println("              } else {");
   out.println("                res[0] = task.getException().getMessage();");
   out.println("              }");
   out.println("            }");
   out.println("      });");
   out.println("    return res[0];");
   out.println("  }");
   out.println("");
   out.println("  public String userId()");
   out.println("  { String res = null;");
   out.println("    FirebaseUser user = authenticator.getCurrentUser();");
   out.println("    if (user != null)");
   out.println("    { res = user.getUid(); }");
   out.println("    return res;");
   out.println("  }");
   out.println("");
   out.println("  public static String signOut()");
   out.println("  { try");
   out.println("    { authenticator.signOut();");
   out.println("      return \"Success\";");
   out.println("    } catch (Exception e)");
   out.println("      { return e + \"\"; }");
   out.println("  }"); 
   out.println("}"); 
  }

  public static void generateSMSComponent(String systemName, String packageName)
  { String entfile = "SMSComponent.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 
      out.println("import android.telephony.SmsManager;"); 
      out.println("");
      out.println("import java.util.List;");
	  out.println("import java.util.ArrayList;");
      out.println("");
      out.println("/* Note: Using this component requires");     
      out.println("   Manifest.permission.SEND_SMS permission */");
      out.println("");
      out.println("public class SMSComponent");
      out.println("{ public boolean canSendText()");
      out.println("  { return SmsManager.getDefault() != null; }");
      out.println("");
      out.println("  public void sendText(String text, List<String> receivers)");
      out.println("  { SmsManager sms = SmsManager.getDefault();");
      out.println("    for (int x = 0; x < receivers.size(); x++)");
      out.println("    { String r = receivers.get(x);");
      out.println("      sms.sendTextMessage(r,null,text,null,null);");
      out.println("    }");
      out.println("  }");
      out.println("}");
      out.close(); 
    } catch (Exception _e) { } 
  }

  public static void generatePhoneComponent(String systemName, String packageName)
  { String entfile = "PhoneComponent.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 
      out.println("import android.content.Intent;"); 
      out.println("import android.content.pm.PackageManager;");
      out.println("import android.net.Uri;");
      out.println("import android.content.Context;");
      out.println("import android.telephony.TelephonyManager;");
      out.println("");
      out.println("import static android.telephony.TelephonyManager.CALL_STATE_IDLE;");
      out.println("import static android.telephony.TelephonyManager.CALL_STATE_RINGING;");
      out.println("");
      out.println("public class PhoneComponent");
      out.println("{ Context myContext = null;");
      out.println("  static PhoneComponent instance = null;");
      out.println("");
      out.println("  public static PhoneComponent getInstance(Context context)"); 
      out.println("  { if (instance == null)"); 
      out.println("    { instance = new PhoneComponent(context); }");
      out.println("    return instance;");
      out.println("  }");
      out.println("");
      out.println("  private PhoneComponent(Context context)");
      out.println("  { myContext = context; }");
      out.println("");
      out.println("  public boolean hasPhoneFeature()");
      out.println("  { PackageManager man = myContext.getPackageManager();");
      out.println("    if (man.hasSystemFeature(PackageManager.FEATURE_TELEPHONY))");
      out.println("    { return true; }");
      out.println("    return false;");
      out.println("  }");
      out.println("");
      out.println("  public String getCallState()");
      out.println("  { TelephonyManager tman = null;");
      out.println("    int cstate = tman.getCallState();");
      out.println("    if (cstate == CALL_STATE_IDLE)");
      out.println("    { return \"IDLE\"; }");
      out.println("    else if (cstate == CALL_STATE_RINGING)");
      out.println("    { return \"RINGING\"; }");
      out.println("    else { return \"OFFHOOK\"; }");
      out.println("  }");
      out.println("");
      out.println("");
      out.println("  public void makeCall(String number)");
      out.println("  { Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(\"tel:\" + number));");
      out.println("    myContext.startActivity(callIntent);");
      out.println("  }");
      out.println("}");
	  out.close(); 
	} catch (Exception ex) {} 
  } 

  public static void generateMediaComponent(String systemName, String packageName)
  { String entfile = "MediaComponent.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 
      out.println("import android.media.AudioManager;"); 
      out.println("import android.media.MediaPlayer;");
      out.println("import android.media.MediaPlayer.OnPreparedListener;");
      out.println("");
      out.println("/* For non-local sources, android.permission.INTERNET"); 
      out.println("   is needed in the AndroidManifest */");
      out.println("");
      out.println("public class MediaComponent implements OnPreparedListener");
      out.println("{ private MediaPlayer mplay;");
      out.println("  private static MediaComponent instance = null;");
      out.println("");
      out.println("  private MediaComponent()");
      out.println("  { mplay = new MediaPlayer(); }");
      out.println("");
      out.println("  public static MediaComponent getInstance()");
      out.println("  { if (instance == null)");
      out.println("    { instance = new MediaComponent(); }");
      out.println("    return instance;");
      out.println("  }");
      out.println("");
      out.println("  public void playAudioAsync(String source)");
      out.println("  { mplay.setAudioStreamType(AudioManager.STREAM_MUSIC);");
      out.println("    try");
      out.println("    { mplay.setDataSource(source); }");
      out.println("    catch (Exception _e) { return; }");
      out.println("    mplay.setOnPreparedListener(this);");
      out.println("    mplay.prepareAsync();");
      out.println("  }");
      out.println("");
      out.println("  public void onPrepared(MediaPlayer mp)");
      out.println("  { mp.start(); }");
      out.println("");
      out.println("  public void stopPlay()");
      out.println("  { mplay.stop(); }");
      out.println("");
      out.println("  public void finalize()");
      out.println("  { mplay.release(); }");
      out.println("}");
	  out.close(); 
	} catch (Exception _ex) { } 
  } 
  
  
  public static void generateGraphDisplay(String systemName, String packageName)
  { String entfile = "GraphDisplay.java"; 
    File entff = new File("output/" + systemName + "/src/main/java/com/example/" + systemName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      out.println("package " + packageName + ";"); 
      out.println(); 
	  out.println("import android.graphics.Canvas;");
	  out.println("import android.graphics.ColorFilter;");
	  out.println("import android.graphics.PixelFormat;");
	  out.println("import android.graphics.drawable.Drawable;");
	  out.println("import android.graphics.Paint;");
	  out.println("");
	  out.println("import java.util.ArrayList;");
	  out.println("import java.util.HashMap; ");
	  out.println("");
	  out.println("public class GraphDisplay extends Drawable implements Drawable.Callback");
	  out.println("{ private String graphKind = \"line\"; // could also be \"scatter\" or \"bar\"");
	  out.println("  private Canvas canvas = null;");
	  out.println("");
	  out.println("  private final Paint bluePaint;");
	  out.println("  private final Paint blackPaint;");
	  out.println("  private final Paint orangePaint; ");
	  out.println("  private final Paint textPaint;");
	  out.println("  private final int offset = 50; // space for axes and labels");
	  out.println("  private final int divisions = 12; // scale divisions");
	  out.println("");
	  out.println("  public ArrayList<Double> xpoints;");
	  out.println("  public ArrayList<Double> ypoints;");
	  out.println("  private ArrayList<Double> zpoints;");
	  out.println("  private ArrayList<String> xlabels;");
	  out.println("  private HashMap<String,ArrayList<Double>> linesx;");
	  out.println("  private HashMap<String,ArrayList<Double>> linesy;");
	  out.println("  private HashMap<String,Double> labelsx;");
	  out.println("  private HashMap<String,Double> labelsy;");
	  out.println("");
	  out.println("  private String xName = \"\";");
	  out.println("  private String yName = \"\";");
	  out.println("");
	  out.println("  private static GraphDisplay instance = null;");
	  out.println("");
	  out.println("  public GraphDisplay() {");
	  out.println("        bluePaint = new Paint();");
	  out.println("        bluePaint.setARGB(255, 0, 0, 255);");
	  out.println("        blackPaint = new Paint();");
	  out.println("        blackPaint.setARGB(255, 0, 0, 0);");
	  out.println("        orangePaint = new Paint();");
	  out.println("        orangePaint.setARGB(255, 128, 128, 0);");
	  out.println("        textPaint = new Paint();");
	  out.println("        textPaint.setARGB(255, 0, 0, 0);");
	  out.println("        textPaint.setTextSize((float) 28.0);");
	  out.println("        xpoints = new ArrayList<Double>();");
	  out.println("        ypoints = new ArrayList<Double>();");
	  out.println("        zpoints = new ArrayList<Double>();");
	  out.println("        xlabels = new ArrayList<String>();");
	  out.println("        linesx = new HashMap<String,ArrayList<Double>>();"); 
	  out.println("        linesy = new HashMap<String,ArrayList<Double>>();");
	  out.println("        labelsx = new HashMap<String,Double>();");
	  out.println("        labelsy = new HashMap<String,Double>();");
 	  out.println("  }");
	  out.println("");
	  out.println("  public static GraphDisplay defaultInstance()"); 
	  out.println("  {");
	  out.println("    if (instance == null) {");
	  out.println("      instance = new GraphDisplay();");
	  out.println("    }");
	  out.println("    return instance;");
	  out.println("  }");
	  out.println("");
	  out.println("  public void invalidateDrawable(Drawable drawable)");
	  out.println("  { xpoints = new ArrayList<Double>();");
	  out.println("    ypoints = new ArrayList<Double>();");
	  out.println("    zpoints = new ArrayList<Double>();");
	  out.println("    xlabels = new ArrayList<String>();");
	  out.println("    linesx = new HashMap<String,ArrayList<Double>>(); ");
	  out.println("    linesy = new HashMap<String,ArrayList<Double>>();");
	  out.println("    labelsx = new HashMap<String,Double>();");
	  out.println("    labelsy = new HashMap<String,Double>();");
	  out.println("  }");
	  out.println("");
	  out.println("    public void scheduleDrawable (Drawable who, Runnable what, long when) { }");
	  out.println("");
	  out.println("    public void unscheduleDrawable(Drawable who, Runnable what) { }");
	  out.println("");
	  out.println("    public void setXScalar(ArrayList<Double> xvalues)");
	  out.println("    { xpoints = xvalues; }");
	  out.println("");
	  out.println("    public void setXNominal(ArrayList<String> xvalues)");
	  out.println("    { xlabels = xvalues; }");
	  out.println("");
	  out.println("    public void setYPoints(ArrayList<Double> yvalues)");
	  out.println("    { ypoints = yvalues; }");
	  out.println("");
	  out.println("    public void setZPoints(ArrayList<Double> zvalues)");
	  out.println("    { zpoints = zvalues; }");
	  out.println("");
	  out.println("    public void setxname(String xn)");
	  out.println("    { xName = xn; }");
	  out.println("");
	  out.println("    public void setyname(String yn)");
	  out.println("    { yName = yn; }");
	  out.println("");
	  out.println("    public void setGraphKind(String kind)");
	  out.println("    { graphKind = kind; }"); 
	  out.println("	");
	  out.println("  public void addLine(String name, ArrayList<Double> xvalues, ArrayList<Double> yvalues)");
	  out.println("  { linesx.put(name, xvalues); ");
	  out.println("    linesy.put(name, yvalues);"); 
	  out.println("  }");
	  out.println("");
	  out.println("  public void addLabel(String name, double x, double y)");
	  out.println("  { labelsx.put(name, new Double(x));");
	  out.println("    labelsy.put(name, new Double(y));");
	  out.println("  }");
	  out.println("");
	  out.println("    @Override");
	  out.println("    public void draw(Canvas canvas)");
	  out.println("    { this.canvas = canvas;");
	  out.println("");
	  out.println("      if (xpoints.size() == 0)");
	  out.println("      { if (xlabels.size() == 0) {");
	  out.println("            return;");
	  out.println("        } else {");
	  out.println("            this.drawNominalScaler(canvas);");
	  out.println("            return;");
	  out.println("        }");
	  out.println("      }");
	  out.println("");
	  out.println("     // Get the drawable's bounds");
	  out.println("     int width = getBounds().width();");
	  out.println("     int height = getBounds().height();");
	  out.println("     float radius = 10;");
	  out.println("");
	  out.println("     double minx = xpoints.get(0);");
	  out.println("     double maxx = xpoints.get(0);");
	  out.println("");
	  out.println("     for (int i = 1; i < xpoints.size(); i++)"); 
	  out.println("		{");
	  out.println("          double xcoord = xpoints.get(i);");
	  out.println("          if (xcoord < minx) ");
	  out.println("          { minx = xcoord; }");
	  out.println("          if (xcoord > maxx) ");
	  out.println("          { maxx = xcoord; }");
	  out.println("        }");
	  out.println("		");
	  out.println("		ArrayList<String> linekeys = new ArrayList<String>();"); 
	  out.println("		linekeys.addAll(linesx.keySet());"); 
	  out.println("		");
	  out.println("		for (int j = 0; j < linekeys.size(); j++)");
	  out.println("		{ String key = linekeys.get(j); ");
	  out.println("		  ArrayList<Double> linexvals = linesx.get(key);"); 
	  out.println("		  for (int k = 0; k < linexvals.size(); k++)"); 
	  out.println("		  { double linex = linexvals.get(k).doubleValue();");
	  out.println("		    if (linex < minx) ");
	  out.println("         { minx = linex; }");
	  out.println("         if (linex > maxx) ");
	  out.println("         { maxx = linex; }");
	  out.println("       }"); 
	  out.println("		}");
	  out.println("");
	  out.println("        ArrayList<String> labelkeys = new ArrayList<String>();");
	  out.println("        labelkeys.addAll(labelsx.keySet());");
	  out.println("");
	  out.println("        for (int i = 0; i < labelkeys.size(); i++) {");
	  out.println("            String labelkey = labelkeys.get(i);");
	  out.println("            double labelx = labelsx.get(labelkey);");
	  out.println("            if (labelx < minx) {");
	  out.println("                minx = labelx;");
	  out.println("            }");
	  out.println("            if (labelx > maxx) {");
	  out.println("                maxx = labelx;");
	  out.println("            }");
	  out.println("        }");
	  out.println("");
	  out.println("        double deltax = maxx - minx;");
	  out.println("		");
	  out.println("        double xstep = deltax/12.0;");
	  out.println("		 ");
	  out.println("        for (int i = 0; i <= 12; i++)");
	  out.println("        { double xcoord = i*xstep*(width - 100)/deltax;");
	  out.println("          int currx = (int) xcoord + 45;");
	  out.println("          double xvalue = minx + i*xstep;");
	  out.println("          xvalue = Math.round(xvalue*100);");
	  out.println("          canvas.drawText(\"\" + (xvalue/100), currx,height-20,textPaint);");
	  out.println("        } ");
	  out.println("                ");
	  out.println("        double miny = ypoints.get(0);");
	  out.println("        double maxy = ypoints.get(0);");
	  out.println("        ");
	  out.println("             for (int i = 1; i < ypoints.size(); i++)"); 
	  out.println("        		{ double ycoord = ypoints.get(i);");
	  out.println("                  if (ycoord < miny) {");
	  out.println("                        miny = ycoord;");
	  out.println("                  }");
	  out.println("                  if (ycoord > maxy) {");
	  out.println("                        maxy = ycoord;");
	  out.println("                  }");
	  out.println("                }");
	  out.println("        		");
	  out.println("              for (int i = 0; i < zpoints.size(); i++)");
	  out.println("              { double ycoord = zpoints.get(i);");
	  out.println("                if (ycoord < miny) {");
	  out.println("                        miny = ycoord;");
	  out.println("                }");
	  out.println("                if (ycoord > maxy) {");
	  out.println("                        maxy = ycoord;");
	  out.println("                }");
	  out.println("             }");
	  out.println("        ");
	  out.println("        		for (int j = 0; j < linekeys.size(); j++)");
	  out.println("        		{ String key = linekeys.get(j); ");
	  out.println("        		  ArrayList<Double> lineyvals = linesy.get(key);"); 
	  out.println("        		  for (int k = 0; k < lineyvals.size(); k++) ");
	  out.println("        		  { double liney = lineyvals.get(k).doubleValue();");
	  out.println("        		    if (liney < miny) ");
	  out.println("                    { miny = liney; }");
	  out.println("                 if (liney > maxy) ");
	  out.println("                    { maxy = liney; }");
	  out.println("               }"); 
	  out.println("        		}");
	  out.println("   ");     
	  out.println("             for (int i = 0; i < labelkeys.size(); i++) {");
	  out.println("                    String labelkey = labelkeys.get(i);");
	  out.println("                    double labely = labelsy.get(labelkey);");
	  out.println("                    if (labely < miny) {");
	  out.println("                        miny = labely;");
	  out.println("                    }");
	  out.println("                    if (labely > maxy) {");
	  out.println("                        maxy = labely;");
	  out.println("                    }");
	  out.println("              }");
	  out.println("        ");
	  out.println("              double deltay = maxy - miny;");
	  out.println("              double ystep = deltay/12.0;");
	  out.println("        ");
	  out.println("             for (int i = 0; i <= 12; i++)");
	  out.println("             { double ycoord = i*ystep*(height - 100)/deltay;");
	  out.println("                int curry = (int) ycoord + 45;");
	  out.println("                double yvalue = miny + i*ystep;");
	  out.println("                yvalue = Math.round(yvalue*1000);");
	  out.println("                canvas.drawText(\"\" + (yvalue/1000), 5,height-curry,textPaint);");
 	  out.println("             }");
	  out.println("        ");
	  out.println("             int prevx = (int) ((xpoints.get(0) - minx)*(width - 100)/deltax + 50);");
	  out.println("             int prevy = height - (int) ((ypoints.get(0) - miny)*(height - 100)/deltay + 50);");
	  out.println("             canvas.drawCircle(prevx, prevy, radius, bluePaint);");
	  out.println("             for (int i = 1; i < xpoints.size() && i < ypoints.size(); i++)"); 
	  out.println("        		{");
	  out.println("                    double xcoord = (xpoints.get(i) - minx)*(width - 100)/deltax;");
	  out.println("                    double ycoord = (ypoints.get(i) - miny)*(height - 100)/deltay;");
	  out.println("                    int currx = (int) xcoord + 50;");
	  out.println("                    int curry = height - ((int) ycoord + 50);");
	  out.println("                    canvas.drawCircle(currx, curry, radius, bluePaint);");
	  out.println("                    if (graphKind.equals(\"line\"))");
	  out.println("                    { canvas.drawLine(prevx, prevy, currx, curry, blackPaint); }"); 
	  out.println("                    prevx = currx;");
	  out.println("                    prevy = curry;");
	  out.println("             }");
	  out.println("        ");
	  out.println("             if (zpoints.size() > 0)"); 
	  out.println("        		{");
	  out.println("                 prevx = (int) ((xpoints.get(0) - minx) * (width - 100) / deltax + 50);");
	  out.println("                 int prevz = height - (int) ((zpoints.get(0) - miny) * (height - 100) / deltay + 50);");
	  out.println("                 canvas.drawCircle(prevx, prevz, radius, orangePaint);");
	  out.println("                 for (int i = 1; i < xpoints.size() && i < zpoints.size(); i++)"); 
	  out.println("        			{");
	  out.println("                        double xcoord = (xpoints.get(i) - minx) * (width - 100) / deltax;");
	  out.println("                        double zcoord = (zpoints.get(i) - miny) * (height - 100) / deltay;");
	  out.println("                        int currx = (int) xcoord + 50;");
	  out.println("                        int currz = height - ((int) zcoord + 50);");
	  out.println("                        canvas.drawCircle(currx, currz, radius, orangePaint);");
	  out.println("                        if (graphKind.equals(\"line\"))");
	  out.println("                        { canvas.drawLine(prevx, prevz, currx, currz, orangePaint); }"); 
	  out.println("                        prevx = currx;");
	  out.println("                        prevz = currz;");
	  out.println("                }");
	  out.println("             }");
	  out.println("      ");  
	  out.println("             for (int p = 0; p < linekeys.size(); p++)");
	  out.println("        		{ String key = linekeys.get(p);");
	  out.println("        		  ArrayList<Double> linexvals = linesx.get(key); ");
	  out.println("        		  ArrayList<Double> lineyvals = linesy.get(key);"); 
	  out.println("        				");
	  out.println("        	      int previousx = (int) ((linexvals.get(0) - minx)*(width - 100)/deltax + 50);");
	  out.println("               int previousy = height - (int) ((lineyvals.get(0) - miny)*(height - 100)/deltay + 50);");
	  out.println("               canvas.drawCircle(previousx, previousy, radius, bluePaint);");
	  out.println("        ");
	  out.println("               for (int i = 1; i < linexvals.size() && i < lineyvals.size(); i++)"); 
	  out.println("        		  {");
	  out.println("                    double xcoord = (linexvals.get(i) - minx)*(width - 100)/deltax;");
	  out.println("                    double ycoord = (lineyvals.get(i) - miny)*(height - 100)/deltay;");
	  out.println("                    int currx = (int) xcoord + 50;");
	  out.println("                    int curry = height - ((int) ycoord + 50);");
	  out.println("                    canvas.drawCircle(currx, curry, radius, bluePaint);");
	  out.println("                    if (graphKind.equals(\"line\"))");
	  out.println("                    { canvas.drawLine(previousx, previousy, currx, curry, blackPaint); }");
	  out.println("                    previousx = currx;");
	  out.println("                    previousy = curry;");
	  out.println("                  }");
	  out.println("        		}");
	  out.println("        ");
	  out.println("                for (int i = 0; i < labelkeys.size(); i++)");
	  out.println("                { String labkey = labelkeys.get(i);");
	  out.println("                  int labx = (int) ((labelsx.get(labkey) - minx)*(width - 100)/deltax) + 50;");
	  out.println("                  int laby = height - ((int) ((labelsy.get(labkey) - miny)*(height - 100)/deltay) + 50);");
	  out.println("                  canvas.drawText(labkey, labx,laby,textPaint);");
	  out.println("                }");
	  out.println("        ");
	  out.println("                canvas.drawText(xName, width - 50, height, textPaint);");
	  out.println("                canvas.drawText(yName, 15, 25, textPaint);");
	  out.println("            }");
	  out.println("");        
	  out.println("    private void drawNominalScaler(Canvas canvas)");
	  out.println("    { int width = getBounds().width();");
	  out.println("      int height = getBounds().height();");
	  out.println("      float radius = 10;");
	  out.println("");
	  out.println("      int nsize = xlabels.size();");
	  out.println("      double xstep = (width - 100)/nsize;");
	  out.println("");
	  out.println("      for (int i = 0; i < nsize ; i++)");
	  out.println("      { double xcoord = i*xstep;");
	  out.println("        int currx = (int) xcoord + 45;");
	  out.println("        String xvalue = xlabels.get(i);");
	  out.println("        canvas.drawText(xvalue, currx,height-20,textPaint);");
	  out.println("      }");
	  out.println("");
	  out.println("        double miny = ypoints.get(0);");
	  out.println("        double maxy = ypoints.get(0);");
	  out.println("");
	  out.println("        for (int i = 1; i < ypoints.size(); i++)");
	  out.println("        { double ycoord = ypoints.get(i);");
	  out.println("            if (ycoord < miny) {");
	  out.println("                miny = ycoord;");
	  out.println("            }");
	  out.println("            if (ycoord > maxy) {");
	  out.println("                maxy = ycoord;");
	  out.println("            }");
	  out.println("        }");
	  out.println("");
	  out.println("        for (int i = 0; i < zpoints.size(); i++)");
	  out.println("        { double ycoord = zpoints.get(i);");
	  out.println("            if (ycoord < miny) {");
	  out.println("                miny = ycoord;");
	  out.println("            }");
	  out.println("            if (ycoord > maxy) {");
	  out.println("                maxy = ycoord;");
	  out.println("            }");
	  out.println("        }");
	  out.println("");
	  out.println("        double deltay = maxy - miny;");
	  out.println("        double ystep = deltay/12.0;");
	  out.println("");
	  out.println("        for (int i = 0; i <= 12; i++)");
	  out.println("        { double ycoord = i*ystep*(height - 100)/deltay;");
	  out.println("          int curry = (int) ycoord + 45;");
	  out.println("          double yvalue = miny + i*ystep;");
	  out.println("          yvalue = Math.round(yvalue*1000);");
	  out.println("          canvas.drawText(\"\" + (yvalue/1000), 5,height-curry,textPaint);");
	  out.println("        }");
	  out.println("");
	  out.println("        int prevx = 50;");
	  out.println("        int prevy = height - (int) ((ypoints.get(0) - miny)*(height - 100)/deltay + 50);");
	  out.println("        canvas.drawCircle(prevx, prevy, radius, bluePaint);");
	  out.println("        for (int i = 1; i < nsize && i < ypoints.size(); i++) {");
	  out.println("            double xcoord = i*xstep;");
	  out.println("            int currx = (int) xcoord + 50;");
	  out.println("            double ycoord = (ypoints.get(i) - miny)*(height - 100)/deltay;");
	  out.println("");
	  out.println("            int curry = height - ((int) ycoord + 50);");
	  out.println("            canvas.drawCircle(currx, curry, radius, bluePaint);");
	  out.println("            if (graphKind.equals(\"line\"))");
	  out.println("            { canvas.drawLine(prevx, prevy, currx, curry, blackPaint); }"); 
	  out.println("            prevx = currx;");
	  out.println("            prevy = curry;");
	  out.println("        }");
	  out.println("");
	  out.println("        if (zpoints.size() > 0) {");
	  out.println("            prevx = 50;");
	  out.println("            int prevz = height - (int) ((zpoints.get(0) - miny) * (height - 100) / deltay + 50);");
	  out.println("            canvas.drawCircle(prevx, prevz, radius, orangePaint);");
	  out.println("            for (int i = 1; i < nsize && i < zpoints.size(); i++) {");
	  out.println("                double xcoord = i*xstep;");
	  out.println("                double zcoord = (zpoints.get(i) - miny) * (height - 100) / deltay;");
	  out.println("                int currx = (int) xcoord + 50;");
	  out.println("                int currz = height - ((int) zcoord + 50);");
	  out.println("                canvas.drawCircle(currx, currz, radius, orangePaint);");
	  out.println("                if (graphKind.equals(\"line\"))");
	  out.println("                { canvas.drawLine(prevx, prevz, currx, currz, orangePaint); }"); 
	  out.println("                prevx = currx;");
	  out.println("                prevz = currz;");
	  out.println("            }");
	  out.println("        }");
	  out.println("");
	  out.println("        canvas.drawText(xName, width - 50, height, textPaint);");
	  out.println("        canvas.drawText(yName, 15, 25, textPaint);");
	  out.println("    }");
	  out.println("");
	  out.println("    @Override");
	  out.println("    public void setAlpha(int alpha) {}");
	  out.println("");
	  out.println("    @Override");
	  out.println("    public void setColorFilter(ColorFilter colorFilter) { }");
	  out.println("");
	  out.println("    @Override");
	  out.println("    public int getOpacity() {");
	  out.println("        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE");
	  out.println("        return PixelFormat.OPAQUE;");
	  out.println("    }");
	  out.println("");
	  out.println("    public void redraw()");
	  out.println("    { draw(canvas); }");
	  out.println("}"); 
	  out.close();
	} catch(Exception _e) { } 
  } 

}  