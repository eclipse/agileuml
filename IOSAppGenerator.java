import java.util.Vector; 
import java.io.*; 

/* Package: Mobile */ 
/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class IOSAppGenerator extends AppGenerator
{ 

  public void modelFacade(String nestedPackageName, Vector usecases, CGSpec cgs, 
                          Vector entities, Vector clouds, Vector types, int remoteCalls, boolean needsMap,
                          PrintWriter out)
  { // String ename = e.getName();
    // Vector atts = e.getAttributes(); 
	
    // String evc = ename + "ViewController";
    // String evo = ename + "ValueObject";
    // String resvo = ename.toLowerCase() + "_vo";
    // String populateResult = createVOStatement(e,atts);

    out.println("import Foundation");
    out.println("import Glibc");
    out.println("");
    out.print("class ModelFacade");
	if (remoteCalls > 0) 
    { out.println(" : InternetCallback"); } 
	else 
	{ out.println(); }
	
    out.println("{ static var instance : ModelFacade? = nil");
    out.println("  var fileSystem : FileAccessor = FileAccessor()");
    // if e is persistent, include a Dbi
    out.println(); 
    
    boolean hasCloud = false; 
    if (clouds.size() > 0)
    { hasCloud = true; }

    if (hasCloud)
    { out.println("  var cdbi : FirebaseDbi = FirebaseDbi.getInstance()"); }

    if (needsMap)
    { out.println("  var markedLocations : [MapLocation] = [MapLocation]()"); 
      out.println("  var currentLocation : MapLocation? = nil"); 
      out.println("  var mapDelegate : MapsComponent? = nil"); 
    } 

    out.println(); 
    out.println("  static func getInstance() -> ModelFacade"); 
    out.println("  { if (instance == nil)"); 
    out.println("    { instance = ModelFacade() }"); 
    out.println("    return instance! }"); 
    out.println(); 

    if (needsMap) 
    { out.println("  func getMarkedLocations() -> [MapLocation]"); 
      out.println("  { return markedLocations }"); 
      out.println(); 
      out.println("  func setMapDelegate(delegate: MapsComponent)"); 
      out.println("  { mapDelegate = delegate }"); 
      out.println(); 
      out.println("  func locationChanged(locations : [MapLocation])"); 
      out.println("  { if locations.count > 0");
      out.println("    { currentLocation = locations[0] }"); 
      out.println("  }");  
      out.println(); 
      out.println("  func addLocation(location : MapLocation)"); 
      out.println("  { markedLocations.append(location) }"); 
      out.println(); 
    } 

	Vector persistentEntities = new Vector(); 
	persistentEntities.addAll(entities); 
	persistentEntities.removeAll(clouds); 

    for (int i = 0; i < persistentEntities.size(); i++) 
    { Entity ent = (Entity) persistentEntities.get(i); 
	 if (ent.isDerived()) { } 
	 else if (ent.isComponent()) { } 
	 else 
	 { String ename = ent.getName(); 
	   out.println("  var current" + ename + " : " + ename + "VO? = nil"); 
        out.println(); 
        out.println("  var current" + 
		            ename + "s : [" + ename + "VO] = [" + ename + "VO]()");
        out.println();  
      } 
    } 

    for (int i = 0; i < clouds.size(); i++) 
    { Entity ent = (Entity) clouds.get(i); 
	 if (ent.isDerived()) { } 
	 else if (ent.isComponent()) { } 
	 else 
	 { String ename = ent.getName(); 
	   out.println("  var current" + ename + " : " + ename + "VO? = nil"); 
        out.println(); 
        out.println("  var current" + 
		            ename + "s : [" + ename + "VO] = [" + ename + "VO]()");
        out.println();  
      } 
    } 

    out.println(); 
    out.println("  init() { }"); 
    out.println(); 
	   
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
      if (res != null && "WebDisplay".equals(res.getType().getName())) 
      { out.println("    var result : WebDisplay = WebDisplay()"); } 
      else if (res != null && "ImageDisplay".equals(res.getType().getName())) 
      { out.println("    var result : ImageDisplay = ImageDisplay()"); } 
      else if (res != null && "GraphDisplay".equals(res.getType().getName())) 
      { out.println("    var result : GraphDisplay = GraphDisplay()"); } 
      else if (res != null) 
      { out.println("    var result : " + res.getType().getSwift()); } 

      // out.println(extractatts);

      String uccode = uc.cgActivity(cgs,entities,types);
      cgs.displayText(uccode,out);      

      if (res != null) 
      { out.println("    return result"); } 

      out.println("  }");
      out.println(); 
    }
	
    out.println(); 
	
    for (int j = 0; j < persistentEntities.size(); j++) 
    { Entity ee = (Entity) persistentEntities.get(j); 
      if (ee.isDerived()) { continue; } 
      if (ee.isComponent()) { continue; } 
	  
      Vector atts = ee.getAttributes(); 
      String item = ee.getName(); 
    
      out.println("  func list" + item + "() -> [" + item + "VO]"); 
      out.println("  { // current" + item + "s = dbi.list" + item + "()");
      out.println("    current" + item + "s = [" + item + "VO]()"); 
      out.println("    var _list : [" + item + "] = " + item + "_allInstances"); 
      out.println("    for (_,x) in _list.enumerated()"); 
      out.println("    { current" + item + "s.append(" + item + "VO(_x: x)) }");  
      out.println("    return current" + item + "s"); 
      out.println("  }"); 
      out.println(); 

      out.println("  func stringList" + item + "() -> [String]"); 
      out.println("  { // current" + item + "s = dbi.list" + item + "()"); 
      out.println("    var _res : [String] = [String]()"); 
      out.println("    for (_,x) in current" + item + "s.enumerated()"); 
      out.println("    { _res.append(x.toString()) }"); 
      out.println("    return _res"); 
      out.println("  }"); 
      out.println(); 

      for (int k = 0; k < atts.size(); k++) 
      { Attribute byatt = (Attribute) atts.get(k); 
        String attname = byatt.getName(); 
        String intype = byatt.getType().getSwift(); 
        out.println("  func searchBy" + item + attname + "(_val : " + intype + ") -> [" + item + "VO]"); 
        out.println("  { // current" + item + "s = dbi.list" + item + "()");
        out.println("    current" + item + "s = [" + item + "VO]()"); 
        out.println("    let _list : [" + item + "] = " + item + "_allInstances"); 
        out.println("    for (_,x) in _list.enumerated()"); 
        out.println("    { if x." + attname + " == _val"); 
        out.println("      { current" + item + "s.append(" + item + "VO(_x: x)) }");
        out.println("    }");  
        out.println("    return current" + item + "s"); 
        out.println("  }"); 
        out.println(); 
      } 

      Attribute key = ee.getPrincipalPK(); 
	  
      if (key != null) 
      { String pk = key.getName(); 
        out.println("  func get" + item + "ByPK(_val : String) -> " + item + "?"); 
        out.println("  { let _res : " + item + "? = " + item + ".getByPK" + item + "(index: _val)"); 
        out.println("    return _res");  
        out.println("  }"); 
        out.println();
		out.println("  func retrieve" + item + "(_val : String) -> " + item + "?"); 
        out.println("  { let _res : " + item + "? = " + item + ".getByPK" + item + "(index: _val)"); 
        out.println("    return _res");  
        out.println("  }"); 
        out.println(); 
      }  

      out.println("  func setSelected" + item + "(_x : " + item + "VO)"); 
      out.println("  { current" + item + " = _x }"); 
      out.println(); 

      out.println("  func setSelected" + item + "(i : Int)"); 
      out.println("  { if 0 <= i && i < current" + item + "s.count"); 
      out.println("    { current" + item + " = current" + item + "s[i] }");
      out.println("  }"); 
      out.println(); 

      out.println("  func getSelected" + item + "() -> " + item + "VO?"); 
      out.println("  { return current" + item + " }"); 
      out.println(); 

      if (key != null) 
      { String pk = key.getName(); 
        out.println("  func edit" + item + "(_x : " + item + "VO)"); 
        out.println("  { let _val : String = _x." + pk + ""); 
        out.println("    var _res : " + item + "? = " + item + ".getByPK" + item + "(index: _val)");
        out.println("    if _res != nil {");  
        for (int i = 0; i < atts.size(); i++) 
        { Attribute att = (Attribute) atts.get(i);
          String attname = att.getName();  
          out.println("      _res!." + attname + " = _x." + attname); 
        } 
        out.println("    }");
        out.println("    current" + item + " = _x"); 
        out.println("  }"); 
        out.println();
	 } 
       else 
       { out.println("  func edit" + item + "(_x : " + item + "VO)"); 
         out.println("  { // dbi.edit" + item + "(_x) "); 
         out.println("    current" + item + " = _x"); 
         out.println("  }"); 
         out.println();
       } 
	  
       if (key != null) 
       { String pk = key.getName(); 
         out.println("  func create" + item + "(_x : " + item + "VO)"); 
         out.println("  { var _res : " + item + " = createByPK" + item + "(key: _x." + pk + ")"); 
         for (int i = 0; i < atts.size(); i++) 
         { Attribute att = (Attribute) atts.get(i);
           String attname = att.getName();  
           out.println("    _res." + attname + " = _x." + attname); 
         } 
         out.println("    current" + item + " = _x"); 
         out.println("  }"); 
         out.println();
       } 
       else 
       { out.println("  func create" + item + "(_x : " + item + "VO)"); 
         out.println("  { // dbi.create" + item + "(_x)");  
         out.println("    current" + item + " = _x"); 
         out.println("  }"); 
         out.println(); 
	   } 
	  
       out.println("  func delete" + item + "(_id : String)"); 
       out.println("  { // dbi.delete" + item + "(_id)");  
       out.println("    current" + item + " = nil"); 
       out.println("  }");
	   out.println();  
     }  

     for (int j = 0; j < clouds.size(); j++) 
     { Entity ee = (Entity) clouds.get(j); 
       if (ee.isDerived()) { continue; } 
       if (ee.isComponent()) { continue; } 
	  
       String item = ee.getName(); 
       String items = item.toLowerCase() + "s"; 
       String itemvo = item + "VO"; 
       Vector atts = ee.getAttributes(); 
      
       Attribute key = ee.getPrincipalPK();
       String pk = "";  
       if (key == null) 
       { System.err.println("!! Warning: a string-typed primary key is needed for class " + item); }
	   
       out.println("  func list" + item + "() -> [" + itemvo + "]"); 
       out.println("  { let " + items + " : [" + item + "] " + " = " + item + "." + item + "_allInstances"); 
       out.println("    current" + item + "s.clear()"); 
       out.println("    for (_,obj) in " + items + ".enumerated()"); 
       out.println("    { current" + item + "s.append(" + itemvo + "(_x: obj)) }"); 
       out.println("    return current" + item + "s"); 
       out.println("  }"); 
       out.println(); 

       out.println("  func stringList" + item + "() -> [String]"); 
       out.println("  { var res : [String] = [String]()"); 
       out.println("    for (_,obj) in current" + item + "s.enumerated()"); 
       out.println("    { res.append(obj.toString()) }"); 
       out.println("    return res"); 
       out.println("  }"); 
       out.println(); 

       if (key != null) 
       { pk = key.getName(); 
         out.println("  func get" + item + "ByPK(_val: String) -> " + item + "?"); 
         out.println("  { return " + item + "." + item + "_index[_val] }"); 
         out.println();
         out.println("  func retrieve" + item + "(_val: String) -> " + item + "?"); 
         out.println("  { return " + item + "." + item + "_index[_val] }"); 
         out.println();
         out.println("  func all" + item + "ids() -> [String]"); 
         out.println("  { var res : [String] = [String]()"); 
         out.println("    for (_,_item) in current" + item + "s.enumerated()"); 
         out.println("    { res.append(_item." + key + " + \"\") }"); 
         out.println("    return res"); 
         out.println("  }"); 
         out.println(); 
       }  

       out.println("  func setSelected" + item + "(_x : " + item + "VO)"); 
       out.println("  { current" + item + " = _x }"); 
       out.println(); 

       out.println("  func setSelected" + item + "(i : Int)"); 
       out.println("  { if i < current" + item + "s.count"); 
       out.println("    { current" + item + " = current" + item + "s[i] }");
       out.println("  }"); 
       out.println(); 

       out.println("  func getSelected" + item + "() -> " + itemvo); 
       out.println("  { return current" + item + " }"); 
       out.println(); 

       out.println("  func persist" + item + "(_x : " + item + ")"); 
       out.println("  { let _vo : " + item + "VO = " + item + "VO(_x: _x)"); 
       out.println("    cdbi.persist" + item + "(ex: _x) "); 
       out.println("    current" + item + " = _vo"); 
       out.println("  }"); 
       out.println(); 

       out.println("  func edit" + item + "(_x : " + itemvo + ")"); 
       out.println("  { if let _obj = get" + item + "ByPK(_val: _x." + pk + ") {"); 
	   for (int k = 0; k < atts.size(); k++)
 	   { Attribute att = (Attribute) atts.get(k); 
	     String attname = att.getName(); 
		 if (att != key)
		 { out.println("      _obj." + attname + " = _x." + attname); }  
	   } 
       out.println("      cdbi.persist" + item + "(ex: _obj) }"); 
       out.println("    current" + item + " = _x"); 
       out.println("  }"); 
       out.println(); 
	  
       out.println("  func create" + item + "(_x : " + itemvo + ")"); 
       out.println("  { if let _obj = get" + item + "ByPK(_val: _x." + pk + ")"); 
       out.println("    { cdbi.persist" + item + "(ex: _obj) }"); 
       out.println("    else "); 
       out.println("    { let _item = " + item + ".createByPK" + item + "(key: _x." + pk + ")");
       for (int k = 0; k < atts.size(); k++)
       { Attribute att = (Attribute) atts.get(k); 
         String attname = att.getName(); 
         if (att != key)
         { out.println("      _item." + attname + " = _x." + attname); } 
	   } 
	   out.println("      cdbi.persist" + item + "(ex: _item)"); 
       out.println("    }");
       out.println("    current" + item + " = _x"); 
       out.println("  }");  
       out.println(); 
	  
       out.println("  func delete" + item + "(_id : String)"); 
       out.println("  { if let _obj = get" + item + "ByPK(_val: _id)"); 
       out.println("    { cdbi.delete" + item + "(ex: _obj) }"); 
       out.println("    current" + item + " = nil"); 
       out.println("  }");
       out.println();   
     } 

     out.println("}");
    // System.out.println("}");  
  }

  public void swiftUIModelFacade(String nestedPackageName, Vector usecases, CGSpec cgs, 
                                 Vector entities, Vector clouds, Vector types, 
								 int remoteCalls, boolean needsMap,
                                 PrintWriter out)
  { // String ename = e.getName();
    // Vector atts = e.getAttributes(); 
	
    // String evc = ename + "ViewController";
    // String evo = ename + "ValueObject";
    // String resvo = ename.toLowerCase() + "_vo";
    // String populateResult = createVOStatement(e,atts);

    out.println("import Foundation");
    out.println("import Darwin");
    out.println("import Combine");
    out.println("import SwiftUI");
	out.println("import CoreLocation"); 
	
    out.println("");
    out.print("class ModelFacade : ObservableObject");
	if (needsMap) 
	{ out.print(", MKMapViewDelegate"); }
	if (remoteCalls > 0) 
    { out.println(", InternetCallback"); } 
	else 
	{ out.println(); }
	
    out.println("{ static var instance : ModelFacade? = nil");
    out.println("  var fileSystem : FileAccessor = FileAccessor()");
    // if e is persistent, include a Dbi
    out.println(); 
    
    boolean hasCloud = false; 
    if (clouds.size() > 0)
    { hasCloud = true; }

    if (hasCloud)
    { out.println("  var cdbi : FirebaseDbi = FirebaseDbi.getInstance()"); }

    if (needsMap)
    { out.println("  var mapDelegate : MapsComponent? = nil"); } 

    out.println(); 
    out.println("  static func getInstance() -> ModelFacade"); 
    out.println("  { if instance == nil"); 
    out.println("    { instance = ModelFacade() }"); 
    out.println("    return instance! }"); 
    out.println(); 

    if (needsMap) 
    { out.println("  func getMarkedLocations() -> [MapLocation]"); 
      out.println("  { return mapDelegate?.markedLocations }"); 
      out.println(); 
      out.println("  func setMapDelegate(delegate: MapsComponent)"); 
      out.println("  { mapDelegate = delegate }"); 
      out.println(); 
      out.println("  func locationChanged(locations : [MapLocation])"); 
      out.println("  { if locations.count > 0");
      out.println("    { mapDelegate?.moveTo(location: locations[0]) }"); 
      out.println("  }");  
      out.println(); 
      out.println("  func addLocation(location : MapLocation)"); 
      out.println("  { mapDelegate?.addMarker(location: location, label: location.name) }"); 
      out.println(); 
      out.println("  func MapView(mapView: MKMapView, didUpdate userLocation : MKUserLocation)"); 
      out.println("  { let loc = MapLocation(latitudex: userLocation.location.latitude, longitudex: userLocation.location.longitude)");   
	  out.println("    mapDelegate?.moveTo(location: loc, label: userLocation.title)"); 
	  out.println("  }"); 
      out.println(); 
      out.println("  func MapView(mapView: MKMapView, didAdd views : [MKAnnotationView])"); 
      out.println("  { }"); 
      out.println(); 
      out.println("  func MapView(mapView: MKMapView, didSelect view : MKAnnotationView)"); 
      out.println("  { let annotation = view.annotation"); // an MKPlacemark
	  out.println("    let coord = annotation.coordinate"); // a CLLocationCoordinate2D
	  out.println("    markerClicked(label: annotation.title, location: MapLocation(latitudex: coord.latitude, longitudex: coord.longitude))"); 
	  out.println("  }"); 
      out.println(); 
       } 

	Vector persistentEntities = new Vector(); 
	persistentEntities.addAll(entities); 
	persistentEntities.removeAll(clouds); 

    for (int i = 0; i < persistentEntities.size(); i++) 
    { Entity ent = (Entity) persistentEntities.get(i); 
      if (ent.isDerived()) { } 
      else if (ent.isComponent()) { } 
      else 
      { String ename = ent.getName(); 
        out.println("  @Published var current" + ename + " : " + ename + "VO = " + ename + "VO.default" + ename + "VO()"); 
        out.println(); 
        out.println("  @Published var current" + 
	                ename + "s : [" + ename + "VO] = [" + ename + "VO]()");
        out.println();  
      }  
    } 

    for (int i = 0; i < clouds.size(); i++) 
    { Entity ent = (Entity) clouds.get(i);   
      if (ent.isDerived()) { } 
      else if (ent.isComponent()) { } 
      else 
      { String ename = ent.getName(); 
        out.println("  @Published var current" + ename + " : " + ename + "VO = " + ename + "VO.default" + ename + "VO()"); 
        out.println(); 
        out.println("  @Published var current" + 
		            ename + "s : [" + ename + "VO] = [" + ename + "VO]()");
        out.println();  
      } 
    } 

    out.println(); 
    out.println("  init() { }"); 
    out.println(); 
	   
    for (int y = 0; y < usecases.size(); y++)
    { UseCase uc = (UseCase) usecases.get(y);
	  String ucname = uc.getName(); 
      Vector pars = uc.getParameters(); 
      Attribute res = uc.getResultParameter();

      
      String restext = "";  
      String partext = "";
	  String pardecs = ""; 
	   
      for (int i = 0; i < pars.size(); i++) 
      { Attribute par = (Attribute) pars.get(i);
        String parname = par.getName(); 
        Type partype = par.getType();  

        partext = partext + "    let " + parname + " : " + partype.getSwift() + " = _x.get" + parname + "()\n"; 
		pardecs = pardecs + parname + " : " + partype.getSwift(); 
		if (i < pars.size() - 1)
		{ pardecs = pardecs + ", "; }
      }   

      if (uc.isPrivate())
      { out.print("  func " + ucname + "(" + pardecs + ")"); }
	  else 
	  { out.println("  func cancel" + ucname + "() { }"); 
        out.println(); 
        out.print("  func " + ucname + "(_x : " + ucname + "VO)"); 
	  } 
	  
      if (res != null) 
      { out.println(" -> " + res.getType().getSwift()); 
        restext = "result"; 
      } 
      else 
      { out.println(); } 

      out.println("  { ");

      if (res != null && "WebDisplay".equals(res.getType().getName())) 
      { out.println("    var result : WebDisplay = WebDisplay()"); } 
      else if (res != null && "ImageDisplay".equals(res.getType().getName())) 
      { out.println("    var result : ImageDisplay = ImageDisplay()"); } 
	  else if (res != null && "GraphDisplay".equals(res.getType().getName())) 
      { out.println("    var result : GraphDisplay = GraphDisplay()"); } 
      else if (res != null) 
      { Type restype = res.getType(); 
        out.println("    var result : " + restype.getSwift() + " = " + restype.getSwiftDefaultValue()); 
      } 

      if (uc.isPublic())
      { out.println("    if _x.is" + ucname + "error()"); 
        out.println("    { return " + restext + " }");
      // _x.result is undefined.
 
        out.println(partext); 
      } 
	  
      String uccode = uc.cgActivity(cgs,entities,types);
      cgs.displayText(uccode,out);      

      if (uc.isPublic())
	  { if (res != null) 
        { out.println("    _x.setresult(_x: result)"); 
          out.println("    return result"); 
        }
	  } 
	  else 
	  { if (res != null) 
        { out.println("    return result"); }
	  } 

      out.println("  }");
      out.println(); 
    }
	
    out.println(); 
	
    for (int j = 0; j < persistentEntities.size(); j++) 
    { Entity ee = (Entity) persistentEntities.get(j); 
      if (ee.isDerived()) { continue; } 
      if (ee.isComponent()) { continue; } 
	  
      Vector atts = ee.getAttributes(); 
      String item = ee.getName(); 
    
      out.println("  func list" + item + "() -> [" + item + "VO]"); 
      out.println("  { // current" + item + "s = dbi.list" + item + "()");
      out.println("    current" + item + "s = [" + item + "VO]()"); 
      out.println("    let _list : [" + item + "] = " + item + "_allInstances"); 
      out.println("    for (_,x) in _list.enumerated()"); 
      out.println("    { current" + item + "s.append(" + item + "VO(_x: x)) }");  
      out.println("    return current" + item + "s"); 
      out.println("  }"); 
      out.println(); 

      out.println("  func stringList" + item + "() -> [String]"); 
      out.println("  { // current" + item + "s = dbi.list" + item + "()"); 
      out.println("    var _res : [String] = [String]()"); 
      out.println("    for (_,x) in current" + item + "s.enumerated()"); 
      out.println("    { _res.append(x.toString()) }"); 
      out.println("    return _res"); 
      out.println("  }"); 
      out.println(); 

      for (int k = 0; k < atts.size(); k++) 
      { Attribute byatt = (Attribute) atts.get(k); 
        String attname = byatt.getName(); 
        String intype = byatt.getType().getSwift(); 
        out.println("  func searchBy" + item + attname + "(_val : " + intype + ") -> [" + item + "VO]"); 
        out.println("  { // current" + item + "s = dbi.list" + item + "()");
        out.println("    current" + item + "s = [" + item + "VO]()"); 
        out.println("    let _list : [" + item + "] = " + item + "_allInstances"); 
        out.println("    for (_,x) in _list.enumerated()"); 
        out.println("    { if x." + attname + " == _val"); 
        out.println("      { current" + item + "s.append(" + item + "VO(_x: x)) }");
        out.println("    }");  
        out.println("    return current" + item + "s"); 
        out.println("  }"); 
        out.println(); 
      } 

      Attribute key = ee.getPrincipalPK(); 
	  
      if (key != null) 
      { String pk = key.getName(); 
        out.println("  func get" + item + "ByPK(_val : String) -> " + item + "?"); 
        out.println("  { let _res : " + item + "? = " + item + ".getByPK" + item + "(index: _val)"); 
        out.println("    return _res");  
        out.println("  }"); 
        out.println();
        out.println("  func retrieve" + item + "(_val : String) -> " + item + "?"); 
        out.println("  { let _res : " + item + "? = " + item + ".getByPK" + item + "(index: _val)"); 
        out.println("    return _res");  
        out.println("  }"); 
        out.println();
      }  

      out.println("  func setSelected" + item + "(_x : " + item + "VO)"); 
      out.println("  { current" + item + " = _x }"); 
      out.println(); 

      out.println("  func setSelected" + item + "(i : Int)"); 
      out.println("  { if 0 <= i && i < current" + item + "s.count"); 
      out.println("    { current" + item + " = current" + item + "s[i] }");
      out.println("  }"); 
      out.println(); 

      out.println("  func getSelected" + item + "() -> " + item + "VO?"); 
      out.println("  { return current" + item + " }"); 
      out.println(); 

      out.println("  func canceledit" + item + "() { }"); 
      out.println(); 

      if (key != null) 
      { String pk = key.getName(); 
        out.println("  func edit" + item + "(_x : " + item + "VO)"); 
        out.println("  { if _x.isedit" + item + "error()"); 
        out.println("    { return }"); 
        out.println("    let _val : String = _x." + pk); 
        out.println("    let _res : " + item + "? = " + item + ".getByPK" + item + "(index: _val)");
        out.println("    if _res != nil {");  
        for (int i = 0; i < atts.size(); i++) 
        { Attribute att = (Attribute) atts.get(i);
          String attname = att.getName();  
          out.println("      _res!." + attname + " = _x.get" + attname + "()"); 
        } 
        out.println("    }");
        out.println("    current" + item + " = _x"); 
        out.println("  }"); 
        out.println();
      } 
      else 
      { out.println("  func edit" + item + "(_x : " + item + "VO)"); 
         out.println("  { // dbi.edit" + item + "(_x) "); 
         out.println("    current" + item + " = _x"); 
         out.println("  }"); 
         out.println();
       } 
	  
	   /* create and edit call the vo to check validity */ 

       out.println("  func cancelcreate" + item + "() { }"); 
       out.println(); 
  
       if (key != null) 
       { String pk = key.getName(); 
         out.println("  func create" + item + "(_x : " + item + "VO)"); 
         out.println("  { if _x.iscreate" + item + "error()"); 
         out.println("    { return }"); 
         out.println("    let _res : " + item + " = createByPK" + item + "(key: _x." + pk + ")"); 
	     for (int i = 0; i < atts.size(); i++) 
	     { Attribute att = (Attribute) atts.get(i);
	       String attname = att.getName();  
	       out.println("    _res." + attname + " = _x.get" + attname + "()"); 
	     } 
	     out.println("    current" + item + " = _x"); 
         out.println("  }"); 
         out.println();
       } 
       else 
       { out.println("  func create" + item + "(_x : " + item + "VO)"); 
         out.println("  { // dbi.create" + item + "(_x)");  
         out.println("    current" + item + " = _x"); 
         out.println("  }"); 
         out.println(); 
	   } 
	  
       out.println("  func delete" + item + "(_id : String)"); 
       out.println("  { // dbi.delete" + item + "(_id)");  
       out.println("    // current" + item + " = nil"); 
       out.println("  }");
	   out.println();
	   out.println("  func persist" + item + "(_x: " + item + ") { }"); 
	   out.println();  
     }  

     for (int j = 0; j < clouds.size(); j++) 
     { Entity ee = (Entity) clouds.get(j); 
       if (ee.isDerived()) { continue; } 
       if (ee.isComponent()) { continue; } 
	  
	  String item = ee.getName(); 
	  String items = item.toLowerCase() + "s"; 
	  String itemvo = item + "VO"; 
       Vector atts = ee.getAttributes(); 
      
       Attribute key = ee.getPrincipalPK();
       String pk = "";  
       if (key == null) 
	   { System.err.println("!! Warning: a string-typed primary key is needed for class " + item); }
	   
       out.println("  func list" + item + "() -> [" + itemvo + "]"); 
       out.println("  { var " + items + " : [" + item + "] " + " = " + item + "." + item + "_allInstances"); 
       out.println("    current" + item + "s.clear()"); 
       out.println("    for (_,obj) in " + items + ".enumerated()"); 
       out.println("    { current" + item + "s.append(" + itemvo + "(_x: obj)) }"); 
       out.println("    return current" + item + "s"); 
       out.println("  }"); 
       out.println(); 

       out.println("  func stringList" + item + "() -> [String]"); 
       out.println("  { var res : [String] = [String]()"); 
       out.println("    for (_,obj) in current" + item + "s.enumerated()"); 
       out.println("    { res.append(obj.toString()) }"); 
       out.println("    return res"); 
       out.println("  }"); 
       out.println(); 

       if (key != null) 
       { pk = key.getName(); 
         out.println("  func get" + item + "ByPK(_val: String) -> " + item + "?"); 
         out.println("  { return " + item + "." + item + "_index[_val] }"); 
         out.println();
         out.println("  func retrieve" + item + "(_val: String) -> " + item + "?"); 
         out.println("  { return " + item + "." + item + "_index[_val] }"); 
         out.println();
         out.println("  func all" + item + "ids() -> [String]"); 
         out.println("  { var res : [String] = [String]()"); 
         out.println("    for (_,_item) in current" + item + "s.enumerated()"); 
         out.println("    { res.append(_item." + key + " + \"\") }"); 
         out.println("    return res"); 
         out.println("  }"); 
         out.println(); 
       }  

       out.println("  func setSelected" + item + "(_x : " + item + "VO)"); 
       out.println("  { current" + item + " = _x }"); 
       out.println(); 

       out.println("  func setSelected" + item + "(i : Int)"); 
       out.println("  { if i < current" + item + "s.count"); 
       out.println("    { current" + item + " = current" + item + "s[i] }");
       out.println("  }"); 
       out.println(); 

       out.println("  func getSelected" + item + "() -> " + itemvo); 
       out.println("  { return current" + item + " }"); 
       out.println(); 

       out.println("  func persist" + item + "(_x : " + item + ")"); 
       out.println("  { let _vo : " + item + "VO = " + item + "VO(_x: _x)"); 
       out.println("    cdbi.persist" + item + "(ex: _x) "); 
       out.println("    current" + item + " = _vo"); 
       out.println("  }"); 
       out.println(); 

       out.println("  func canceledit" + item + "() { }"); 

       out.println("  func edit" + item + "(_x : " + itemvo + ")"); 
       out.println("  { if let _obj = get" + item + "ByPK(_val: _x." + pk + ") {"); 
	   for (int k = 0; k < atts.size(); k++)
 	   { Attribute att = (Attribute) atts.get(k); 
	     String attname = att.getName(); 
	     if (att != key)
          { out.println("      _obj." + attname + " = _x.get" + attname + "()"); }  
	   } 
       out.println("      cdbi.persist" + item + "(ex: _obj) }"); 
       out.println("    current" + item + " = _x"); 
       out.println("  }"); 
       out.println(); 

       out.println("  func cancelcreate" + item + "() { }"); 
       out.println(); 
       out.println("  func create" + item + "(_x : " + itemvo + ")"); 
       out.println("  { if let _obj = get" + item + "ByPK(_val: _x." + pk + ")"); 
       out.println("    { cdbi.persist" + item + "(ex: _obj) }"); 
       out.println("    else "); 
       out.println("    { let _item = " + item + ".createByPK" + item + "(key: _x." + pk + ")");
       for (int k = 0; k < atts.size(); k++)
       { Attribute att = (Attribute) atts.get(k); 
         String attname = att.getName(); 
         if (att != key)
         { out.println("      _item." + attname + " = _x.get" + attname + "()"); } 
	   } 
	   out.println("      cdbi.persist" + item + "(ex: _item)"); 
	   out.println("    }");
       out.println("    current" + item + " = _x"); 
	   out.println("  }");  
       out.println(); 
	  
       out.println("  func delete" + item + "(_id : String)"); 
       out.println("  { if let _obj = get" + item + "ByPK(_val: _id)"); 
       out.println("    { cdbi.delete" + item + "(ex: _obj) }"); 
       out.println("    // current" + item + " = nil"); 
       out.println("  }");
       out.println();   
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
      attlist = attlist + aname + "x: " + aname;
      if (x < atts.size()-1)
      { attlist = attlist + ","; }
    }

    String res = "    var " + vo + " : " + evo + " = " + evo + "(" + attlist + ")";
    return res;
  }

  public void singlePageApp(UseCase uc, String appName, String image, CGSpec cgs, Vector types, Vector entities, PrintWriter out)
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
    String validationBean = ucname + "ValidationBean"; 
    // String evocreate = createVOStatement(e,atts);
    String validator = ucname + "Validator"; 
	
    out.println("import UIKit");
    out.println("import WebKit"); 
    if (res != null && "GraphDisplay".equals(res.getType().getName()))
    { out.println("import Charts"); } 
    out.println();
    out.print("class " + evc + " : UIViewController");
    if (res != null && "WebDisplay".equals(res.getType().getName()))
    { out.println(", WKUIDelegate"); } 
    else if (res != null && "GraphDisplay".equals(res.getType().getName()))
    { out.println(", IAxisValueFormatter"); } 
    else 
    { out.println(); } 
    out.println("{");
    out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
    out.println("  var " + validator + " : " + validationBean + " = " + validationBean + "()");
    out.println(); 

    String parlist = ""; 
    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      String iosdeclaration = att.uiKitDeclaration(); 
      out.println(iosdeclaration);
      
      parlist = parlist + att.getName(); 
      if (x < atts.size() - 1) 
      { parlist = parlist + ", "; } 
    } 
    
    if (res != null && "WebDisplay".equals(res.getType().getName()))
    { out.println("  @IBOutlet var resultOutput: WKWebView!"); 
      restype = "WebDisplay"; 
    } 
    else if (res != null && "ImageDisplay".equals(res.getType().getName()))
    { out.println("  @IBOutlet var resultOutput: UIImageView!"); 
      restype = "ImageDisplay"; 
    }
    else if (res != null && "GraphDisplay".equals(res.getType().getName()))
    { out.println("  @IBOutlet var resultOutput: LineChartView!"); 
      out.println("  var graph: GraphDisplay = GraphDisplay.getInstance()"); 
      restype = "GraphDisplay"; 
    }
    else if (res != null) 
    { out.println("  @IBOutlet weak var resultOutput: UILabel!");
      restype = res.getType().getSwift(); 
    }
	
    out.println(); 
    out.println("  var userId : String = " + "\"0\"");
    out.println();

    out.println("  override func viewDidLoad()");
    out.println("  { super.viewDidLoad()");
    if (res != null && "WebDisplay".equals(res.getType().getName()))
    { out.println("    let myURL = URL(string: \"https://www.apple.com\")"); 
      out.println("    let myRequest = URLRequest(url: myURL)"); 
      out.println("    resultOutput.load(myRequest)"); 
    } 
    else if (res != null && "GraphDisplay".equals(res.getType().getName()))
    { out.println("    resultOutput.pinchZoomEnabled = true"); } 
    // out.println("    self." + elist + " = " + bean + "." + getlist + "()");
    out.println("  }");
    out.println("");

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      String iosattop = att.uiKitOp(); 
      out.println(iosattop);
    } 
    out.println(); 
	
    String attdecoder = "    guard ";
    boolean previous = false;
    String localVars = "";

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      // if (att.isInputAttribute())
      String attname = att.getName(); 
      { if (previous)
        { attdecoder = attdecoder + ", "; }
		
	   String inputvar = attname + "Input.text";
	   if (att.isSmallEnumeration())
	   { inputvar = attname + "Input"; }
		
        attdecoder = attdecoder + " let " + att + " = " + 
                     Expression.unwrapSwift(inputvar,att.getType());
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
      if ("WebDisplay".equals(att.getType().getName()))
      { updateScreen = updateScreen + "    let myURL = URL(string: result.url)\n"; 
        updateScreen = updateScreen + "    let myRequest = URLRequest(url: myURL)\n"; 
        updateScreen = updateScreen + "    resultOutput.load(myRequest)"; 
		
        out.println("  override func loadView()"); 
        out.println("  { let webConfiguration = WKWebViewConfiguration()"); 
        out.println("    resultOutput = WKWebView(frame: .zero, configuration: webConfiguration)"); 
        out.println("    resultOutput.uiDelegate = self"); 
        out.println("  }"); 
        out.println(); 
      }
      else if ("ImageDisplay".equals(att.getType().getName()))
      { updateScreen = updateScreen + "    resultOutput.image = UIImage(named: result.imageName)\n"; }  
      else if ("GraphDisplay".equals(att.getType().getName()))
      { updateScreen = updateScreen + "    graph = result\n" + 
	                                  "    let xpts = result.xpoints\n" + 
                                      "    let ypts = result.ypoints\n" + 
                                      "    let xlbs = result.xlabels\n" + 
                                      "    if xlbs.count > 0\n" + 
          "    { drawNominalChart(dataPoints: xlbs, values: ypts.map{ Double($0) }, name: result.yname) }\n" + 
          "    else if xpts.count > 0\n" + 
          "    { drawScalarChart(dataPoints: xpts, values: ypts.map{ Double($0) }, name: result.yname) }\n"; 
	  } 
      else 
      { updateScreen = updateScreen + "    resultOutput.text = String(result)"; }
    }

    // for (int y = 0; y < usecases.size(); y++)
    // { UseCase uc = (UseCase) usecases.get(y);
    //  String ucname = uc.getName();
      out.println("  @IBAction func " + ucname + "(_ sender: Any) {");
      if (atts.size() > 0) 
	  { out.println(attdecoder); } 
      // out.println(localVars);
      // out.println(evocreate);
	  
	  out.println(); 
	  String errorcall = "is" + ucname + "error"; 
	  out.println("    if " + validator + "." + errorcall + "(" + parlist + ")"); 
	  out.println("    { return }"); 
	  out.println(); 
	  
      if (res != null) 
      { out.println("    var " + resvo + " : " + restype + " = " + bean + "." + ucname + "(" + parlist + ")"); 
        out.println(updateScreen);
      } 
      else 
      { out.println("    " + bean + "." + ucname + "(" + parlist + ")"); } 
      out.println("  }");
    // }

    out.println("");
    if (res != null && "GraphDisplay".equals(res.getType().getName()))
	{ printGraphDisplayOperations(out); }
	
    out.println("  override func didReceiveMemoryWarning()");
    out.println("  { super.didReceiveMemoryWarning() }");
    out.println("");
    out.println("}");
  }
  
  private void printGraphDisplayOperations(PrintWriter out)
  { out.println("  func stringForValue(_ dataPointIndex: Double, axis: AxisBase?) -> String "); 
    out.println("  { let xlbs = graph.xlabels"); 
    out.println("    let xpts = graph.xpoints"); 
    out.println("    let ind = Int(dataPointIndex)"); 
    out.println("    if xlbs.count > ind"); 
    out.println("    { return xlbs[ind] } "); 
    out.println("    else if xpts.count > ind"); 
    out.println("    { return String(xpts[ind]) }");  
    out.println("    return \"\""); 
    out.println("  }"); 
    out.println("  "); 
    out.println("  func drawNominalChart(dataPoints: [String], values: [Double], name : String)");  
    out.println("  { var dataEntries: [ChartDataEntry] = []"); 
    out.println("    "); 
    out.println("    for i in 0..<dataPoints.count "); 
    out.println("    { let dataEntry = ChartDataEntry(x: Double(i), y: values[i])"); 
    out.println("      dataEntries.append(dataEntry)"); 
    out.println("    }"); 
    out.println("    "); 
    out.println("    let xAxis = resultOutput.xAxis"); 
    out.println("    xAxis.valueFormatter = self"); 
    out.println("  "); 
    out.println("    let lineChartDataSet = LineChartDataSet(values: dataEntries, label: name)"); 
    out.println("    let lineChartData = LineChartData(dataSet: lineChartDataSet)"); 
    out.println("    resultOutput.data = lineChartData"); 
    out.println("  }"); 
    out.println("  "); 
    out.println("  func drawScalarChart(dataPoints: [Double], values: [Double], name : String)");  
    out.println("  { var dataEntries: [ChartDataEntry] = []"); 
    out.println("    "); 
    out.println("    for i in 0..<dataPoints.count "); 
    out.println("    { let dataEntry = ChartDataEntry(x: dataPoints[i], y: values[i])"); 
    out.println("      dataEntries.append(dataEntry)"); 
    out.println("    }"); 
    out.println("  "); 
    out.println("    let xAxis = resultOutput.xAxis"); 
    out.println("    xAxis.valueFormatter = self"); 
    out.println("    "); 
    out.println("    let lineChartDataSet = LineChartDataSet(values: dataEntries, label: name)"); 
    out.println("    let lineChartData = LineChartData(dataSet: lineChartDataSet)"); 
    out.println("    resultOutput.data = lineChartData"); 
    out.println("  }"); 
	out.println(); 
  }

  public void singlePageAppSwiftUI(UseCase uc, String appName, String image, CGSpec cgs, Vector types, Vector entities, PrintWriter out)
  { String ucname = uc.getName();
    String op = ucname; 
	
    String evc = ucname + "Screen";
    String evo = ucname + "VO";
    String vo = "bean";
    String resvo = "result";
    String restype = ""; 
    String ebean =  "ModelFacade";
    String bean = ebean.toLowerCase();
    Vector atts = uc.getParameters();
    Attribute res = uc.getResultParameter(); 
    String validationBean = ucname + "ValidationBean"; 
    // String evocreate = createVOStatement(e,atts);
    String validator = ucname + "Validator"; 
	
    String label = Named.capitalise(ucname);
    String opbean = ucname + "VO"; // The VO also provides validation checks
    Vector extradeclarations = new Vector(); 
    Vector extraactions = new Vector(); 
    String formfields = ""; 

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      formfields = formfields + att.swiftUIEntryField(ucname,op,extradeclarations,extraactions);
    }
    
    out.println("import SwiftUI");
    out.println("");
    out.println("struct " + op + "Screen : View {");
    out.println("  @State var bean : " + opbean + " = " + opbean + "()");
    // for (int i = 0; i < extradeclarations.size(); i++)
    // { out.println(extradeclarations.get(i)); } 
    out.println("  @ObservedObject var model : ModelFacade"); 
    out.println("");  
    out.println("  var body: some View {");
    out.println("    VStack(alignment: .leading, spacing: 20) {");
    out.println(formfields); 
    out.println("      HStack(spacing: 20) ");
    out.println("      { Button(action: { self.model.cancel" + op + "() } ) { Text(\"Cancel\") }"); 
    out.println("        Button(action: { self.model." + op + "(_x: bean) } ) { Text(\"" + label + "\") }"); 
    out.println("      }.buttonStyle(PlainButtonStyle())"); 

    if (res != null && "WebDisplay".equals(res.getType().getName()))
    { out.println("      WebView(request: URLRequest(string: bean.result.url))"); } 
    else if (res != null && "ImageDisplay".equals(res.getType().getName()))
    { out.println("      Image(bean.result.imageName)"); } 
    else if (res != null && "GraphDisplay".equals(res.getType().getName()))
    { out.println("      GraphDisplayView(graph: bean.result)"); } 
    else if (res != null) 
    { out.println("      HStack(spacing: 20) {");
      out.println("        Text(\"Result:\")"); 
      out.println("        Text(String(bean.result))");
      out.println("      }"); 
    } 
    out.println("    }.padding(.top)");
    out.println("  }");
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

  out.println("import Foundation"); 
  out.println("import UIKit");
  out.println();
  out.println("class " + evc + " : UIViewController, UITableViewDataSource, UITableViewDelegate");
  out.println("{");
  out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
  out.println("  @IBOutlet weak var tableView: UITableView!");
  out.println(); 
  out.println("  var userId : String = " + "\"0\"");
  out.println("  var " + elist + " : [" + evo + "] = [" + evo + "]()");
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
    if (att.isHidden()) { } 
    else 
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

public void iOSViewController(String systemName, String op, String feature, Entity entity, PrintWriter out)
{ // String op = getAction();
  // AndroidAppGenerator gen = new AndroidAppGenerator(); 
    
  if (op.startsWith("create"))
  { createViewController(systemName,entity,out); }
  else if (op.startsWith("delete"))
  { deleteViewController(systemName,entity,out); }
  // else if (op.startsWith("edit"))
  // { gen.androidEditViewActivity(op,entity,out); }
  else if (op.startsWith("list"))
  { listViewController(entity,out); }
  else if (op.startsWith("searchBy"))
  { Attribute byatt = entity.getAttribute(feature); 
    searchByViewController(systemName,entity,byatt,out); 
  }
}

  public void createViewController(String systemName, Entity entity, 
                                    PrintWriter out)
  { String ename = entity.getName(); 
    String ucname = "Create" + ename; 
	String opname = "create" + ename; 
    String evc = opname + "ViewController";
    // String evo = ename + "ValueObject";
    // String vo = evo.toLowerCase();
    String resvo = "result";
    String restype = ""; 
    String ebean =  "ModelFacade";
    String bean = ebean.toLowerCase();
    Vector atts = entity.getAttributes();
    Vector invariants = entity.getAllInvariants(); 

    Attribute res = null; 
	String validator = "validator" + ename; 
	
    // String evocreate = createVOStatement(e,atts);

    out.println("import Foundation");
    out.println("import UIKit");
    out.println();
    out.println("class " + evc + " : UIViewController");
    out.println("{");
    out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
    out.println("  var " + validator + " : " + ename + "Bean = " + ename + "Bean()");
	out.println(); 

    String parlist = ""; 
    String parlistx = ""; 

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      // if (att.isInputAttribute())
      out.println("  @IBOutlet weak var " + att + "Input: UITextField!");
      
      parlist = parlist + att.getName() + ": " + att.getName(); 
      parlistx = parlistx + att.getName() + "x: " + att.getName(); 
      if (x < atts.size() - 1) 
      { parlist = parlist + ", "; 
        parlistx = parlistx + ", "; 
      } 
    } 
    
    out.println(); 
    out.println("  var userId : String = " + "\"0\"");
    out.println();
    out.println("  override func viewDidLoad()");
    out.println("  { super.viewDidLoad()");
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
   
    out.println("  @IBAction func " + opname + "(_ sender: Any) {");
    if (atts.size() > 0) 
    { out.println(attdecoder); } 
      
    if (invariants.size() > 0)
    { out.println("    if " + validator + ".is" + opname + "error(" + parlist + ") { }"); 
      out.println("    else"); 
      out.println("    { " + bean + "." + opname + "(_x: " + ename + "VO(" + parlistx + ")) }");  
    }
    else 
    { out.println("    " + bean + "." + opname + "(_x: " + ename + "VO(" + parlistx + ")"); } 

    out.println("  }");
    out.println("");
 
    out.println("  override func didReceiveMemoryWarning()");
    out.println("  { super.didReceiveMemoryWarning() }");
    out.println("");
    out.println("}");
  }

  public void deleteViewController(String systemName, Entity entity, 
                                    PrintWriter out)
  { String ename = entity.getName(); 
    String ucname = "Delete" + ename; 
    String opname = "delete" + ename; 
    String evc = opname + "ViewController";
    String evo = ename + "VO";
    String vo = evo.toLowerCase();
    String resvo = "result";
    String restype = ""; 
    String ebean =  "ModelFacade";
    String bean = ebean.toLowerCase();
    Vector atts = new Vector();
    Attribute res = null; 
	
    // String evocreate = createVOStatement(e,atts);

    out.println("import Foundation");
    out.println("import UIKit");
    out.println();
    out.println("class " + evc + " : UIViewController");
    out.println("{");
    out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
    out.println(""); 

    Attribute key = entity.getPrincipalPK(); 
    if (key == null) 
    { System.err.println("!! ERROR: primary key needed for deletion operation on " + ename); 
	 out.println("}");
      return; 
    }
    atts.add(key); 
    String pk = key.getName(); 

    String parlist = pk; 
    out.println("  @IBOutlet weak var " + pk + "Input: UITextField!");
      
    	
    out.println(""); 
    out.println("  var userId : String = " + "\"0\"");
    out.println("");
    out.println("  override func viewDidLoad()");
    out.println("  { super.viewDidLoad()");
    out.println("    let " + vo + " : " + evo + "? = " + bean + ".getCurrent" + ename + "()"); 
    out.println("    if " + vo + " != nil"); 
    out.println("    { " + pk + "Input.text = " + vo + "." + pk + " }");
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
   
    out.println("  @IBAction func " + opname + "(_ sender: Any) {");
    if (atts.size() > 0) 
    { out.println(attdecoder); } 
    out.println("    " + bean + "." + opname + "(_id: " + parlist + ")");  
    out.println("  }");
    
    out.println("");
 
    out.println("  override func didReceiveMemoryWarning()");
    out.println("  { super.didReceiveMemoryWarning() }");
    out.println("");
    out.println("}");
  }

  public void editViewController(String systemName, Entity entity, 
                                    PrintWriter out)
  { String ename = entity.getName(); 
    String ucname = "Edit" + ename; 
    String opname = "edit" + ename; 
    String evc = opname + "ViewController";
    String evo = ename + "VO";
    String vo = evo.toLowerCase();
    String resvo = "result";
    String restype = ""; 
    String ebean =  "ModelFacade";
    String bean = ebean.toLowerCase();
    Vector atts = entity.getAttributes(); // The writable ones, in fact
    Vector invariants = entity.getAllInvariants(); 

    Attribute res = null; 
	String validator = "validator" + ename; 
	
    // String evocreate = createVOStatement(e,atts);

    out.println("import Foundation");
    out.println("import UIKit");
    out.println();
    out.println("class " + evc + " : UIViewController");
    out.println("{");
    out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
    out.println(); 

    String parlist = ""; 
    String parlistx = ""; 
    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      // if (att.isPassword() || att.isHidden()) { } else
      out.println("  @IBOutlet weak var " + att + "Input: UITextField!");
      
      parlist = parlist + att.getName(); 
      parlistx = parlistx + att.getName() + "x: " + att.getName(); 
      if (x < atts.size() - 1) 
      { parlist = parlist + ", "; 
        parlistx = parlistx + ", "; 
      } 
    } 
      
    out.println(); 
    out.println("  var userId : String = \"0\"");
    out.println();
    out.println("  override func viewDidLoad()");
    out.println("  { super.viewDidLoad()");
    out.println("    let " + vo + " : " + evo + "? = " + bean + ".getCurrent" + ename + "()"); 
    out.println("    if " + vo + " != nil {");
    for (int j = 0; j < atts.size(); j++)
    { Attribute att = (Attribute) atts.get(j); 
      String attname = att.getName(); 
      out.println("      " + attname + "Input.text = " + vo + "!." + attname);
    } 
    out.println("    }"); 
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
    }
    attdecoder = attdecoder + " else { return }\n";

    String updateScreen = "";
   
    out.println("  @IBAction func " + opname + "(_ sender: Any) {");
    if (atts.size() > 0) 
    { out.println(attdecoder); } 
    
    if (invariants.size() > 0)
    { out.println("    if " + validator + ".is" + opname + "error(" + parlist + ") { }"); 
      out.println("    else"); 
      out.println("    { " + bean + "." + opname + "(_x: " + ename + "VO(" + parlistx + ")) }");  
    }
    else 
    { out.println("    " + bean + "." + opname + "(_x: " + ename + "VO(" + parlistx + ")"); } 
    
    out.println("  }");
    
    out.println("");
 
    out.println("  override func didReceiveMemoryWarning()");
    out.println("  { super.didReceiveMemoryWarning() }");
    out.println("");
    out.println("}");
  }
  
  public void searchByViewController(String systemName, Entity e, Attribute byatt, PrintWriter out)
  { String ename = e.getName();
    String attname = byatt.getName(); 
    String evc = "searchBy" + ename + attname + "ViewController";
    String evo = ename + "VO";
    String ebean = "ModelFacade";
    String bean = "model";
    Vector atts = e.getAttributes();
    String elist = ename.toLowerCase() + "List";
    String getlist = "list" + ename;
    String opname = "searchBy" + ename + attname; 

    out.println("import Foundation"); 
    out.println("import UIKit");
    out.println();
    out.println("class " + evc + " : UIViewController, UITableViewDataSource, UITableViewDelegate");
    out.println("{");
    out.println("  var " + bean + " : " + ebean + " = " + ebean + ".getInstance()");
    out.println("  @IBOutlet weak var tableView: UITableView!");
    out.println("  @IBOutlet weak var " + attname + "Input: UITextField!");
    out.println(); 
    out.println("  var userId : String = " + "\"0\"");
    out.println("  var " + elist + " : [" + evo + "] = [" + evo + "]()");
    out.println();
    out.println("  override func viewDidLoad()");
    out.println("  { super.viewDidLoad()");
    out.println("    self." + elist + " = " + bean + "." + getlist + "()");
    out.println("  }");
    out.println("");
    out.println("  override func didReceiveMemoryWarning()");
    out.println("  { super.didReceiveMemoryWarning() }");
    out.println("");
    out.println("  @IBAction func " + opname + "(_ sender: Any)");
    out.println("  { guard let " + attname + " = " + 
                     Expression.unwrapSwift(attname + "Input.text",byatt.getType()));
    out.println("    else { return } "); 
    out.println("    self." + elist + " = " + bean + "." + opname + "(_val: " + attname + ")");
    out.println("   }"); 
    out.println(); 
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
      if (att.isHidden()) { } 
      else 
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
  
  public static void generateInternetAccessor(String packagename, PrintWriter out)
  { out.println("import UIKit");
    out.println("import Foundation");  
    out.println(); 
    out.println("class InternetAccessor"); 
    out.println("{ var delegate : InternetCallback? = nil");
    out.println("");
    out.println("  static var instance : InternetAccessor? = nil");
    out.println("");
    out.println("  var urlSession = URLSession.shared");
    out.println("");
    out.println("  func setDelegate(d : InternetCallback)");
    out.println("  { delegate = d }");
    out.println("");
    out.println("  func getInstance() -> InternetAccessor");
    out.println("  { if instance == nil");
    out.println("    { instance = InternetAccessor() }");
    out.println("    return instance!");
    out.println("  }");
    out.println("");
    out.println("  func execute(url : String)");
    out.println("  { let urlref = URL(string: url)");
    out.println("    let task = urlSession.dataTask(with: urlref!)");
    out.println("    { (data,response,error) in");
    out.println("      if let e = error"); 
    out.println("      { delegate?.internetAccessCompleted(nil) }");
    out.println("      else if let _ = response");
    out.println("      { delegate?.internetAccessCompleted(String(data)) }");
    out.println("    }");
    out.println("    task.resume()");
    out.println("  }");
    out.println("}");
    out.println("");
    out.println("protocol InternetCallback");
    out.println("{ func internetAccessCompleted(response : String?) }");   
    out.println("");
  }

public static void iosDateComponent(PrintWriter out)
{ out.println("import Foundation");
  out.println("");
  out.println("class DateComponent");
  out.println("{ ");
  out.println("  static func getEpochSeconds(date : String) -> Int");
  out.println("  { let df = DateFormatter()");
  out.println("    df.dateFormat = \"yyyy-MM-dd\" ");
  out.println("    if let d = df.date(from: date)");
  out.println("    { let time = d.timeIntervalSince1970");
  out.println("      return Int(time)");
  out.println("    }");
  out.println("	   else ");
  out.println("	   { return -1 }");
  out.println("  }");
  out.println("");
  out.println("  static func getEpochMilliseconds(format : String, date : String) -> Int");
  out.println("  { let df = DateFormatter()");
  out.println("    df.dateFormat = format");
  out.println("    if let d = df.date(from: date) ");
  out.println("    { let time = d.timeIntervalSince1970 ");
  out.println("      return Int(1000*time)");
  out.println("    } ");
  out.println("    else ");
  out.println("    { return -1 }");
  out.println("  }");
  out.println("");
  out.println("  static func getTime() -> Int");
  out.println("  { let d = Date() ");
  out.println("    return Int(1000*d.timeIntervalSince1970) ");
  out.println("  }");
  out.println("");
  out.println("}");
}


public static void generateIOSFileAccessor(PrintWriter out)
{ out.println("import Foundation");
  out.println("import Darwin");
  out.println("import UIKit");
  out.println("");
  out.println("class FileAccessor");
  out.println("{");
  out.println("  init() { }"); 
  out.println(""); 
  out.println("  func createFile(filename : String) ");
  out.println("  { let fm = FileManager.default");
  out.println("    do");
  out.println("    { let path = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)");
  out.println("      let furl = path.appendingPathComponent(filename)");
  out.println("      try \"\".write(to: furl, atomically: true, encoding: .utf8)");
  out.println("    }");
  out.println("    catch { }");
  out.println("  }");
  out.println("");
  out.println("  func fileExists(filename : String) -> Bool ");
  out.println("  { let filemgr = FileManager.default");
  out.println("    let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask) ");
  out.println("    let docsDir = dirPaths[0]");
  out.println("    let path = docsDir.appendingPathComponent(filename)");
  out.println("    return filemgr.fileExists(atPath: path) ");
  out.println("  }");
  out.println("");
  out.println("  func fileIsWritable(filename : String) -> Bool");
  out.println("  { let filemgr = FileManager.default");
  out.println("    let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask) ");
  out.println("    let docsDir = dirPaths[0]");
  out.println("    let path = docsDir.appendingPathComponent(filename)");
  out.println("    return filemgr.isWritableFile(atPath: path) ");
  out.println("  }");
  out.println("");
  out.println("  func deleteFile(filename : String) -> String");  
  out.println("  { let filemgr = FileManager.default");
  out.println("    let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask) ");
  out.println("    let docsDir = dirPaths[0]");
  out.println("    let path = docsDir.appendingPathComponent(filename)");
  out.println("    do ");
  out.println("    { try filemgr.removeItem(atPath: path) ");
  out.println("      return \"Success\") ");
  out.println("    } ");
  out.println("    catch let error ");
  out.println("    { return \"Error: \" + error.localizedDescription }");
  out.println("  }");
  out.println("  ");
  out.println("   func readFile(filename : String) -> [String]");
  out.println("    { var res : [String] = [String]()");
  out.println("      let filemgr = FileManager.default");
  out.println("      let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask) ");
  out.println("      let docsDir = dirPaths[0]");
  out.println("      let path = docsDir.appendingPathComponent(filename)");
  out.println("      do");
  out.println("      { let text = ");
  out.println("          try String(contentsOf: path, encoding: .utf8)");
  out.println("        res = Ocl.toLineSequence(str: text)");
  out.println("        return res");
  out.println("      }");
  out.println("      catch { return res } ");
  out.println("    }");
  out.println("  ");
  out.println("    func writeFile(filename : String, contents : [String])");
  out.println("    { var text : String = \"\"");
  out.println("      let filemgr = FileManager.default");
  out.println("      let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask) ");
  out.println("      let docsDir = dirPaths[0]");
  out.println("      let path = docsDir.appendingPathComponent(filename)");
  out.println("      for s in contents");
  out.println("      { text = text + s + '\\n' } ");
  out.println("      let file: FileHandle? = FileHandle(forUpdatingAtPath: path) ");
  out.println("      if file != nil ");
  out.println("      { let data = (text as NSString).data(using: String.Encoding.utf8.rawValue)");
  out.println("        file?.write(data!) ");
  out.println("        file?.closeFile() ");
  out.println("      }");
  out.println("    }");
  out.println("  }");
}


  public static void swiftUIContentView(PrintWriter out, String title)
  { out.println("import SwiftUI"); 
    out.println(); 
    out.println("struct ContentView: View"); 
    out.println("{"); 
    out.println("  var body: some View"); 
    out.println("  { Text(\"" + title + "\").font(.title) }");  
    out.println("}"); 
    out.println(); 
    out.println("struct ContentView_Previews: PreviewProvider {"); 
    out.println("  static var previews: some View {"); 
    out.println("    ContentView()"); 
    out.println("  }"); 
    out.println("}"); 
  }  
  
  public static void generateSwiftUIAppDelegate(PrintWriter out)
  { out.println("import UIKit");
    out.println("");
    out.println("@UIApplicationMain");
    out.println("class AppDelegate: UIResponder, UIApplicationDelegate");
    out.println("{");
    out.println("  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool");
    out.println("  { return true }");
    out.println("");
    out.println("  func applicationWillTerminate(_ application: UIApplication) { }");
    out.println("");
    out.println("  func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration");
    out.println("  { return UISceneConfiguration(name: \"Default Configuration\", sessionRole: connectingSceneSession.role) }");
    out.println(" ");
    out.println("  func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) { }");    
    out.println("");
    out.println("}");
  }
  
  public static void generateUIKitAppDelegate(PrintWriter out)
  { out.println("import UIKit");
    out.println("");
    out.println("@UIApplicationMain @IBObject public class AppDelegate : IUIApplicationDelegate {"); 
    out.println("  override var window : UIWindow?"); 
    out.println(); 
    out.println("  override func application(_ application: UIApplication!, didFinishLaunchingWithOptions launchOptions: NSDictionary<UIApplicationLaunchOptionsKey!,rtl.id!>!) -> Bool"); 
    out.println("  { window = UIWindow()"); 
    out.println("    window!.rootViewController = UINavigationController(rootViewController: RootViewController())"); 
    out.println("    window!.makeKeyAndVisible()"); 
    out.println("    return true"); 
    out.println("  }"); 
    out.println(""); 
    out.println("  override func applicationWillResignActive(_ application: UIApplication) {}"); 
    out.println(); 
    out.println("  override func applicationDidEnterBackground(_ application: UIApplication) {}"); 
    out.println(); 
    out.println("  override func applicationWillEnterForeground(_ application: UIApplication) {}"); 
    out.println(); 
    out.println("  override func applicationDidBecomeActive(_ application: UIApplication) {}"); 
    out.println(); 
    out.println("  override func applicationWillTerminate(_ application: UIApplication) {}"); 
    out.println(); 
  }

public static void generateSceneDelegate(String mainscreen, PrintWriter out)
{ out.println("import UIKit");
  out.println("import SwiftUI");
  out.println("");
  out.println("class SceneDelegate: UIResponder, UIWindowSceneDelegate");    
  out.println("{");
  out.println("  var window: UIWindow?");
  out.println("");
  out.println("  func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions)");
  out.println("  { if let windowScene = scene as? UIWindowScene ");
  out.println("    { let window = UIWindow(windowScene: windowScene)");
  out.println("      window.rootViewController = UIHostingController(rootView: " + mainscreen + "())");
  out.println("      self.window = window");
  out.println("      window.makeKeyAndVisible()");
  out.println("    }");
  out.println("  }");
  out.println("");
  out.println("  func sceneDidDisconnect(_ scene: UIScene) { }");
  out.println("");
  out.println("  func sceneDidBecomeActive(_ scene: UIScene) { }");
  out.println("");
  out.println("  func sceneWillResignActive(_ scene: UIScene) { }");
  out.println(); 
  out.println("  func sceneWillEnterForeground(_ scene: UIScene) { }");
  out.println("");
  out.println("  func sceneDidEnterBackground(_ scene: UIScene) {}");
  out.println("");
  out.println("}");
}

public static void swiftuiScreen(String op, Entity entity, PrintWriter out)
{ String ename = entity.getName(); 
  
  if (op.startsWith("create"))
  { swiftuiCreateScreen(op,entity,out); }
  else if (op.startsWith("delete"))
  { swiftuiDeleteScreen(op,entity,out); }
  else if (op.startsWith("edit"))
  { swiftuiEditScreen(op,entity,out); }
  else if (op.startsWith("list"))
  { entity.swiftUIList(out);
    String outfile = "output/swiftuiapp/" + ename + "ListRowView.swift";
    try { 
      PrintWriter pwout = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(outfile)));
          
      entity.swiftUIListRow(pwout); 
      pwout.close(); 
    }
    catch (Exception e)
    { System.out.println("Errors with file: " + outfile);
      return; 
    }  
  }
  // else if (op.startsWith("searchBy"))
  // { Attribute byatt = entity.getAttribute(feature); 
  //   searchByViewController(systemName,entity,byatt,out); 
  // }
}


  public static void swiftuiCreateScreen(String op, Entity ent, PrintWriter out)
  { // op is "create" + ent.getName()
    String label = Named.capitalise(op);
    String ename = ent.getName(); 
    Vector atts = ent.getAttributes(); 
    String opbean = ename + "VO"; // The VO also provides validation checks
    Vector extradeclarations = new Vector(); 
    Vector extraactions = new Vector(); 
    String formfields = ""; 

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      formfields = formfields + att.swiftUIEntryField(ename,op,extradeclarations,extraactions);
    }
    
    out.println("import SwiftUI");
    out.println("");
    out.println("struct " + op + "Screen : View {");
    out.println("  @State var bean : " + opbean + " = " + opbean + "()");
    // for (int i = 0; i < extradeclarations.size(); i++)
    // { out.println(extradeclarations.get(i)); } 
    out.println("  @ObservedObject var model : ModelFacade"); 
    out.println("");  
    out.println("  var body: some View {");
    out.println("    VStack(alignment: HorizontalAlignment.leading, spacing: 20) {");
    out.println(formfields); 
    out.println("      HStack(spacing: 20) {");
    out.println("        Button(action: { self.model.cancel" + op + "() } ) { Text(\"Cancel\") }"); 
    out.println("        Button(action: { self.model." + op + "(_x: bean) } ) { Text(\"" + label + "\") }"); 
    out.println("      }.buttonStyle(PlainButtonStyle())"); 
    out.println("    }.padding(.top)");
    out.println("  }");
    out.println("}");
  }

  public static void swiftuiEditScreen(String op, Entity ent, PrintWriter out)
  { // op is "edit" + ent.getName()
    String label = Named.capitalise(op);
    String ename = ent.getName(); 
    Vector atts = ent.getAttributes(); 
    String opbean = ename + "VO"; // The VO also provides validation checks
    Vector extradeclarations = new Vector(); 
    Vector extraactions = new Vector(); 
    String formfields = ""; 

    for (int x = 0; x < atts.size(); x++)
    { Attribute att = (Attribute) atts.get(x);
      formfields = formfields + att.swiftUIEntryField(ename,op,extradeclarations,extraactions);
    }
    
    out.println("import SwiftUI");
    out.println("");
    out.println("struct " + op + "Screen : View {");
    out.println("  @State var bean: " + opbean + " = " + opbean + ".default" + opbean + "()");
    // for (int i = 0; i < extradeclarations.size(); i++)
    // { out.println(extradeclarations.get(i)); } 
    out.println("  @ObservedObject var model : ModelFacade"); 
    out.println("");  
    out.println("  var body: some View {");
    out.println("    VStack(alignment: HorizontalAlignment.leading, spacing: 20) {");
    out.println(formfields); 
    out.println("      HStack(spacing: 20) {");
    out.println("        Button(action: { self.model.cancel" + op + "() } ) { Text(\"Cancel\") }"); 
    out.println("        Button(action: { self.model." + op + "(_x: bean) } ) { Text(\"" + label + "\") }"); 
    out.println("      }.buttonStyle(PlainButtonStyle())"); 
    out.println("    }.padding(.top)");
    out.println("  }.onAppear { bean = model.current" + ename + " }");
    out.println("}");
  }

  public static void swiftuiDeleteScreen(String op, Entity ent, PrintWriter out)
  { // op is "delete" + ent.getName()
    String label = Named.capitalise(op);
    String ename = ent.getName(); 
    Vector atts = ent.getAttributes(); 
    String opbean = ename + "VO"; // The VO also provides validation checks
    Vector extradeclarations = new Vector(); 
    Vector extraactions = new Vector(); 
    String formfields = ""; 
	
    Attribute id = ent.getPrincipalPrimaryKey(); 
    if (id == null) { return; }
	
    String pk = id.getName(); 
    
    out.println("import SwiftUI");
    out.println("");
    out.println("struct " + op + "Screen : View {");
    out.println("  @State var objectId: String = \"\"");
    out.println("  @ObservedObject var model : ModelFacade"); 
    out.println("");  
    out.println("  var body: some View {");
    out.println("    VStack(alignment: HorizontalAlignment.leading, spacing: 20) {");
    out.println("      Picker(\"" + ename + "\", selection: $objectId)"); 
    out.println("      { ForEach(model.current" + ename + "s) { Text($0." + pk + ").tag($0." + pk + ") } }");
    out.println(""); 
    out.println("      HStack(spacing: 20) {");
    out.println("        Button(action: { self.model.cancel" + op + "() } ) { Text(\"Cancel\") }"); 
    out.println("        Button(action: { self.model." + op + "(_x: bean) } ) { Text(\"" + label + "\") }"); 
    out.println("      }.buttonStyle(PlainButtonStyle())"); 
    out.println("    }.padding(.top)");
    out.println("  }.onAppear { objectId = model.current" + ename + "?." + pk + " }");
    out.println("}");
  }
  
  public static void swiftuiOptionsScreen(PrintWriter out)
  { 
    out.println("import SwiftUI");
    out.println("");
    out.println("struct OptionsDialog : View {");
    out.println("  var title : String");
    out.println("  var labels : [String]");
    out.println("  @State var selected : String = \"\""); 
    out.println();  
    out.println("  @ObservedObject var model : ModelFacade"); 
    out.println("");  
    out.println("  var body: some View {");
    out.println("    VStack(alignment: HorizontalAlignment.leading, spacing: 20) {");
    out.println("      Picker(\"title\", selection: $selected)"); 
    out.println("      { ForEach(labels) { Text($0).tag($0) } }");
    out.println(""); 
    out.println("      HStack(spacing: 20) {");
    out.println("        Button(\"Cancel\")"); 
    out.println("        Button(\"OK\", action: model.dialogResponse(label: selected))"); 
    out.println("      }.buttonStyle(BorderedButtonStyle())"); 
    out.println("    }.padding(.top)");
    out.println("  }");
    out.println("}");
  }

  public static void swiftUITabScreen(Vector operations, Vector labels, PrintWriter out)
  { out.println("import SwiftUI");
    out.println("");
    out.println("struct MainScreen : View");
    out.println("{");  
    out.println("  var body: some View {");
    out.println("    TabView {");
    for (int i = 0; i < operations.size(); i++) 
    { String op = (String) operations.get(i); 
      String label = (String) labels.get(i); 
      out.println("      " + op + "().tabItem"); 
      out.println("      { Image(systemName: \"" + (i+1) + ".square.fill\")"); 
      out.println("        Text(\"" + label + "\")"); 
      out.println("      }"); 
    }
    out.println("    }.font(.headline)"); 
    out.println("  }"); 
    out.println("}"); 
  } 

  public static void generateIOSDbi(String packageName, String appName, Vector ents, Vector operations, PrintWriter out) 
  { // String appName = "app"; // but supply as a parameter
    // out.println("package " + packageName + ";");
    // out.println();
    // out.println();
    out.println("import Foundation");
    out.println("import Glibc");
    out.println("import SQLite3"); 
    out.println();
    out.println("class Dbi");
    out.println("{ private let dbPointer : OpaquePointer?");
    out.println("  private static let DBNAME = \"" + appName + ".db\"");
    out.println("  private static let DBVERSION = 1");
    out.println();
  
    String createCode = ""; 
    for (int i = 0; i < ents.size(); i++) 
    { Entity e0 = (Entity) ents.get(i); 
      e0.iosDbiDeclarations(out);
      String ent = e0.getName(); 
      createCode = createCode + "createTable(table: " + ent + "_CREATE_SCHEMA)\n    "; 
    }  

    out.println("  private init(dbPointer: OpaquePointer?)"); 
    out.println("  { self.dbPointer = dbPointer }");
    out.println(); 

    out.println("  func createDatabase(db : Dbi)"); 
    out.println("  { " + createCode);
    out.println("  }"); 
    out.println();  
	
    out.println("  fileprivate var errorMessage: String"); 
    out.println("  { if let errorPointer = sqlite3_errmsg(dbPointer)");
    out.println("    { let errorMessage = String(cString: errorPointer)");
    out.println("      return errorMessage");
    out.println("    } ");
    out.println("    else ");
    out.println("    { return \"Unknown error from sqlite.\" }");
    out.println("  }");
    out.println("  ");
    out.println("  func prepareStatement(sql: String) throws -> OpaquePointer?   ");
    out.println("  { var statement: OpaquePointer?");
    out.println("    guard sqlite3_prepare_v2(dbPointer, sql, -1, &statement, nil) ");
    out.println("        == SQLITE_OK"); 
    out.println("    else ");
    out.println("    { return nil }");
    out.println("    return statement");
    out.println("  }");
    out.println("  ");
    out.println("  static func open(path: String) throws -> Dbi? ");
    out.println("  { var db: OpaquePointer?");
    out.println("  ");
    out.println("    if sqlite3_open(path, &db) == SQLITE_OK ");
    out.println("    { return Dbi(dbPointer: db) }"); 
    out.println("    else ");
    out.println("    { defer ");
    out.println("      { if db != nil ");
    out.println("        { sqlite3_close(db) }");
    out.println("      }");
    out.println("  ");
    out.println("      if let errorPointer = sqlite3_errmsg(db)"); 
    out.println("      { let message = String(cString: errorPointer)");
    out.println("        print(\"Error opening database: \" + message)");
    out.println("      } ");
    out.println("      else ");
    out.println("      { print(\"Unknown error opening database\") }");
    out.println("      return nil"); 
    out.println("    }");
    out.println("  }"); 
    out.println("  "); 

    out.println("  func createTable(table: String) throws");  
    out.println("  { let createTableStatement = try prepareStatement(sql: table)"); 
    out.println("    defer "); 
    out.println("    { sqlite3_finalize(createTableStatement) }"); 
    out.println("    "); 
    out.println("    guard sqlite3_step(createTableStatement) == SQLITE_DONE "); 
    out.println("    else");  
    out.println("    { print(\"Error creating table\") ");
    out.println("      return"); 
    out.println("    }"); 
    out.println("    print(\"table \" + table + \" created.\")"); 
    out.println("  }"); 
    out.println(); 

    for (int i = 0; i < ents.size(); i++) 
    { Entity e0 = (Entity) ents.get(i); 
      e0.iosDbiOperations(out);
    }  

    out.println("  deinit()");
    out.println("  { sqlite3_close(self.dbPointer) }");
    out.println();
    // out.println("  func onUpgrade(Dbi d, int x, int y) {}");
    // out.println();
    out.println("}");
  } 

  public static void generateWebDisplay(String packageName)
  { String entfile = "WebDisplay.swift"; 
    File entff = new File("output/" + packageName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      // out.println("package " + packageName + ";"); 
      out.println(); 

      out.println("");
      out.println("class WebDisplay");
      out.println("{ var url : String = \"\"");
      out.println("");
      out.println("  init()");
      out.println("  { }");
      out.println("");
      out.println("  func loadURL(url : String)");
      out.println("  { self.url = url }");
      out.println("");
      out.println("  func reload()");
      out.println("  { }");
      out.println("}"); 
      out.close();  
    } catch (Exception _e) { }  
  }

  public static void generateImageDisplay(String packageName)
  { String entfile = "ImageDisplay.swift"; 
    File entff = new File("output/" + packageName + "/" + entfile); 
    try
    { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(entff)));
      // out.println("package " + packageName + ";"); 
      out.println(); 
	
      out.println("");
      out.println("class ImageDisplay");
      out.println("{ var imageName : String = \"\"");
      out.println(""); 
      out.println("");
      out.println("  init()");
      out.println("  { }");
      out.println("");
      out.println("  func setImageName(name : String)");
      out.println("  { imageName = name }");
      out.println("}"); 
      out.close();  
    } catch (Exception _e) { }  
  }

  public static void main(String[] args)
  { // System.out.println(Double.MAX_VALUE); 

   }  
}
