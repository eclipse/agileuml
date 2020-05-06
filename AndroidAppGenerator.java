import java.util.Vector; 
import java.io.*; 


public class AndroidAppGenerator extends AppGenerator
{ 

  public void modelFacade(Vector usecases, CGSpec cgs, Vector entities, Vector types, PrintWriter out)
  { // String ename = e.getName();
    // Vector atts = e.getAttributes(); 
	
    // String evc = ename + "ViewController";
    // String evo = ename + "ValueObject";
    // String resvo = ename.toLowerCase() + "_vo";
    // String populateResult = createVOStatement(e,atts);

    out.println("package com.example.app;");
    out.println(); 
    
    out.println("import android.content.Context;"); 
    out.println("import java.util.ArrayList;");
    out.println("import java.util.HashMap;");
    out.println("import java.util.List;");
    out.println("import java.util.Map;");
    out.println("");
    out.println("public class ModelFacade");
    out.println("{ Dbi dbi; ");
    out.println(); 
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
	
    out.println("  private ModelFacade(Context context)"); 
    out.println("  { dbi = new Dbi(context); }"); 
    out.println(); 
	
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
          partext = partext + partype.getJava7() + " " + par.getName(); 
          if (i < pars.size()-1)
          { partext = partext + ", "; } 
        }   
	  
	    String restype = "void"; 
	    if (res != null) 
        { restype = res.getType().getJava7(); } 
      
        out.println("  public " + restype + " " + uc.getName() + "(" + partext + ")"); 
      
        out.println("  { ");
        if (res != null) 
        { out.println("    " + res.getType().getJava7()  + " " + res.getName() + ";"); } 

      // out.println(extractatts);
        out.println(uc.cgActivity(cgs,entities,types));
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
	  
	  String item = ee.getName(); 
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

     Attribute key = ee.getPrincipalPK(); 
	 if (key != null) 
	 { Vector atts = ee.getAttributes(); 
        out.println("  private " + item + " get" + item + "ByPK(String _val)"); 
        out.println("  { ArrayList<" + item + "VO> _res = dbi.searchBy" + item + key + "(_val);"); 
        out.println("    if (_res.size() == 0)"); 
        out.println("    { return null; }"); 
        out.println("    else"); 
        out.println("    { " + item + "VO _vo = _res.get(0);"); 
        out.println("      " + item + " _itemx = new " + item + "(_val);");
	    for (int k = 0; k < atts.size(); k++) 
	    { Attribute att = (Attribute) atts.get(k); 
	      String aname = att.getName();  
          out.println("      _itemx." + aname + " = _vo." + aname + ";"); 
        } 
        out.println("      return _itemx;"); 
        out.println("    }"); 
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

      Vector atts = ee.getAttributes(); 
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
      }  
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

  public void singlePageApp(UseCase uc, CGSpec cgs, PrintWriter out)
  { String ucname = uc.getName();
    String evc = ucname + "ViewController";
    // String evo = ename + "ValueObject";
    // String vo = evo.toLowerCase();
    String resvo = "result";
    String restype = ""; 
    String ebean =  "ModelFacade";
    String bean = ebean.toLowerCase();
    Vector atts = uc.getParameters();
    Attribute res = uc.getResultParameter(); 

    // String evocreate = createVOStatement(e,atts);

    out.println("import UIKit");
    out.println();
    out.println("class " + evc + " : UIViewController");
    out.println("{");
    out.println("  var " + bean + " : " + ebean + " = " + ebean + "()");

    String parlist = ""; 
    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      // if (att.isInputAttribute())
      out.println("  @IBOutlet weak var " + att + "Input: UITextField!");
      
      parlist = parlist + att.getName(); 
      if (x < atts.size() - 1) 
      { parlist = parlist + ", "; } 
    } 
    
    if (res != null)
    { out.println("  @IBOutlet weak var " + ucname + "Output: UILabel!");
      restype = res.getType().getSwift(); 
    }

    out.println("  var userId : String = " + "\"0\"");
    out.println();
    out.println("  override func viewDidLoad()");
    out.println("  { super.viewDidLoad()");
    // out.println("    self." + elist + " = " + bean + "." + getlist + "()");
    out.println("  }");
    out.println("");
 
    String attdecoder = "    guard ";
    boolean previous = false;
    String localVars = "";

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      // if (att.isInputAttribute())
      { if (previous)
        { attdecoder = attdecoder + ", "; }
        attdecoder = attdecoder + " let " + att + " = " + 
                     Expression.unwrapSwift(att + "Input.text",att.getType());
        previous = true;
      }
      // else 
      // { Type atype = att.getType(); 
	 //  if (atype != null) 
      //  { localVars = localVars + "    var " + att + " : " + atype.getSwift() + "\n"; }
      // }  
    }
    attdecoder = attdecoder + " else { return }\n";

    String updateScreen = "";
   
    // for (int x = 0; x < atts.size(); x++)
    if (res != null) 
    { Attribute att = res; // (Attribute) atts.get(x);
      // if (att.isOutput())
      { updateScreen = updateScreen + "    " + att + "Output.text = String(" + att + ")";
      }
    }

    // for (int y = 0; y < usecases.size(); y++)
    // { UseCase uc = (UseCase) usecases.get(y);
    //  String ucname = uc.getName();
      out.println("  @IBAction func " + ucname + "(_ sender: Any) {");
      out.println(attdecoder);
      // out.println(localVars);
      // out.println(evocreate);
      if (res != null) 
      { out.println("    var " + resvo + " : " + restype + " = " + bean + "." + ucname + "(" + parlist + ")"); 
        out.println(updateScreen);
      } 
      else 
      { out.println("    " + bean + "." + ucname + "(" + parlist + ")"); } 
      out.println("  }");
    // }

    out.println("");
 
    out.println("  override func didReceiveMemoryWarning()");
    out.println("  { super.didReceiveMemoryWarning() }");
    out.println("");
    out.println("}");
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
    out.println("    setContentView(R.layout.list" + ename + "_layout);");
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

  public void androidDeleteViewActivity(String op, Entity ent, PrintWriter out)
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

  public void androidEditViewActivity(String op, Entity ent, PrintWriter out)
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
  
  public void generateManifest(String appname, PrintWriter out)
  { out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    out.println("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"");
    out.println("    package=\"com.example." + appname + "\">");
    out.println("    <application");
    out.println("  android:allowBackup=\"true\"");
    out.println("  android:icon=\"@mipmap/ic_launcher\"");
    out.println("  android:label=\"@string/app_name\"");   
    out.println("     android:roundIcon=\"@mipmap/ic_launcher_round\"");
    out.println("  android:supportsRtl=\"true\"");
    out.println("  android:theme=\"@style/AppTheme\">");
    out.println("  <activity");
    out.println("  android:name=\".MainActivity\"");
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

}
