import java.util.Vector; 
import java.io.*; 


public class IOSAppGenerator extends AppGenerator
{ 

  public void modelFacade(Vector usecases, CGSpec cgs, Vector entities, Vector types, PrintWriter out)
  { // String ename = e.getName();
    // Vector atts = e.getAttributes(); 
	
    // String evc = ename + "ViewController";
    // String evo = ename + "ValueObject";
    // String resvo = ename.toLowerCase() + "_vo";
    // String populateResult = createVOStatement(e,atts);

    out.println("import Foundation");
    out.println("import Glibc");
    out.println("");
    out.println("class ModelFacade");
    out.println("{ static instance : ModelFacade = nil");
    // if e is persistent, include a Dbi
    out.println(); 
    out.println("  static func getInstance() -> ModelFacade"); 
    out.println("  { if (instance == nil) { instance = ModelFacade() }"); 
    out.println("    return instance }"); 
    out.println(); 

	for (int i = 0; i < entities.size(); i++) 
	{ Entity ent = (Entity) entities.get(i); 
	  if (ent.isDerived()) { } 
	  else 
	  { String ename = ent.getName(); 
	    out.println("  var current" + ename + " : " + ename + "VO = nil"); 
        out.println(); 
        out.println("  var current" + 
		            ename + "s : [" + ename + "VO] = [" + ename + "]()");
        out.println();  
      } 
	} 
   
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
    { UseCase uc = (UseCase) usecases.get(y);
      Vector pars = uc.getParameters(); 
      Attribute res = uc.getResultParameter(); 
      String partext = ""; 
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        Type partype = par.getType();  
        partext = partext + par.getName() + " : " + partype.getSwift(); 
        if (i < pars.size()-1)
        { partext = partext + ", "; } 
      }   
      out.print("  func " + uc.getName() + "(" + partext + ")"); 
      if (res != null) 
      { out.println(" -> " + res.getType().getSwift()); } 
      else 
      { out.println(); } 

      out.println("  { ");
      if (res != null) 
      { out.println("    var result : " + res.getType().getSwift()); } 

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
      { out.println("    return result"); } 

      out.println("  }");
      out.println(); 
	  
      // String act = uc.activity.cg(cgs); 
      // System.out.println(uc.cgActivity(cgs,entities,types));
      // System.out.println(populateResult);
      // System.out.println("   return " + resvo);
      // System.out.println("  }");
    }
	
	out.println(); 
	
    for (int j = 0; j < entities.size(); j++) 
    { Entity ee = (Entity) entities.get(j); 
	  if (ee.isDerived()) { continue; } 
	  
	  String item = ee.getName(); 
	  out.println("  func list" + item + "() -> [" + item + "VO]"); 
      out.println("  { // current" + item + "s = dbi.list" + item + "()"); 
      out.println("    return current" + item + "s"); 
      out.println("  }"); 
	  out.println(); 

      out.println("  func stringList" + item + "() -> [String]"); 
      out.println("  { // current" + item + "s = dbi.list" + item + "()"); 
      out.println("    var res : [String] = [String]()"); 
      out.println("    for (i,x) in current" + item + "s.enumerated()"); 
      out.println("    { var _item : " + item + "VO = current" + item + "s[i]"); 
      out.println("      res.append(_item + \"\")"); 
      out.println("    }"); 
      out.println("    return res"); 
      out.println("  }"); 
	  out.println(); 

      Attribute key = ee.getPrincipalPK(); 
	  if (key != null) 
	  { Vector atts = ee.getAttributes(); 
        out.println("  func get" + item + "ByPK(_val : String) -> " + item); 
        out.println("  { // var _res : [" + item + "VO] = dbi.searchBy" + item + key + "(_val)"); 
        out.println("    if (_res.count == 0)"); 
        out.println("    { return nil }"); 
        out.println("    else"); 
        out.println("    { var _vo : " + item + "VO = _res[0]"); 
        out.println("      var _itemx : " + item + " = " + item + "(_val)");
	    for (int k = 0; k < atts.size(); k++) 
	    { Attribute att = (Attribute) atts.get(k); 
	      String aname = att.getName();  
          out.println("      _itemx." + aname + " = _vo." + aname); 
        } 
        out.println("      return _itemx"); 
        out.println("    }"); 
        out.println("  }"); 
	    out.println();
      }  

      out.println("    func setSelected" + item + "(_x : " + item + "VO)"); 
      out.println("    { current" + item + " = _x }"); 
	  out.println(); 

      out.println("    func setSelected" + item + "(i : Int)"); 
      out.println("    { if (i < current" + item + "s.count)"); 
      out.println("      { current" + item + " = current" + item + "s[i] }");
      out.println("    }"); 
	  out.println(); 

      out.println("    func getSelected" + item + "() -> " + item + "VO"); 
      out.println("    { return current" + item + " }"); 
	  out.println(); 

      out.println("    func edit" + item + "(_x : " + item + "VO)"); 
      out.println("    { // dbi.edit" + item + "(_x) "); 
      out.println("      current" + item + " = _x"); 
      out.println("    }"); 
      out.println(); 
	  
      out.println("    func create" + item + "(_x : " + item + "VO)"); 
      out.println("    { // dbi.create" + item + "(_x)");  
      out.println("      current" + item + " = _x"); 
      out.println("    }"); 
      out.println(); 
	  
      out.println("   func delete" + item + "(_id : String)"); 
      out.println("   { // dbi.delete" + item + "(_id)");  
      out.println("     current" + item + " = nil"); 
      out.println("   }"); 
    } 

    out.println("}");
    // System.out.println("}");  
  }

  public String createVOStatement(Entity e, Vector atts)
  { String ename = e.getName();
    // String evc = ename + "ViewController";
    String vo = ename.toLowerCase() + "_vo";
    String evo = ename + "VO";
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
    out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
	out.println(); 

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
    { out.println("  @IBOutlet weak var resultOutput: UILabel!");
      restype = res.getType().getSwift(); 
    }
    out.println(); 
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
      if (atts.size() > 0) 
	  { out.println(attdecoder); } 
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
  String evc = ename + "ListViewController";
  String evo = ename + "VO";
  String ebean = "ModelFacade";
  String bean = "model";
  Vector atts = e.getAttributes();
  String elist = ename.toLowerCase() + "List";
  String getlist = "list" + ename;


  out.println("import UIKit");
  out.println();
  out.println("class " + evc + " : UIViewController, UITableViewDataSource, UITableViewDelegate");
  out.println("{");
  out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
  out.println("  @IBOutlet weak var tableView: UITableView!");
  out.println(); 
  out.println("  var userId : String = " + "\"0\"");
  out.println("  var " + elist + " : [" + evo + "] = []()");
  out.println();
  out.println("  override func viewDidLoad()");
  out.println("  { super.viewDidLoad()");
  out.println("    self." + elist + " = " + bean + "." + getlist + "()");
   out.println("  }");
   out.println("");
   out.println("  override func didReceiveMemoryWarning()");
   out.println("  { super.didReceiveMemoryWarning() }");
   out.println("");
   // For UITableViewDataSource
   out.println("  func numberOfSections(in tableView: UITableView) -> Int"); 
   out.println("  { return 1 }");
   out.println("");
   out.println("  func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int");
   out.println("  { return self." + elist + ".count }");
   out.println("");
   out.println("");
   out.println("  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell"); 
   out.println("  { let cell = self.tableView.dequeueReusableCell(withIdentifier: \"Cell\", for: indexPath)");
   out.println("");
   out.println("    if let item = self." + elist + "[indexPath.row]"); 
   out.println("    {");
   for (int x = 0; x < atts.size(); x++)
   { Attribute att = (Attribute) atts.get(x);
     if (att.isSummary())
     { String attnme = att.getName();
       out.println("      cell." + attnme + "Label?.text = item." + attnme);
     }
   }
   out.println("    }");
   out.println("    return cell");
  out.println("  }");
  out.println("  ");
   
  out.println("  func tableView(_ tableView: UITableView, didSelectRowAt indexPath : IndexPath)");
  out.println("  { guard let item = " + elist + "[indexPath.row] else { return }");
  out.println("    " + bean + ".setSelected" + ename + "(item)");
  out.println("  }");
  out.println("}");
}


  public static void main(String[] args)
  { // System.out.println(Double.MAX_VALUE); 

   }  
}
