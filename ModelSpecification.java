import java.util.*; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ModelSpecification 
{ Vector objects = new Vector(); 
  java.util.Map objectmap = new java.util.HashMap(); 
  java.util.Map objectsOfClass = new java.util.HashMap(); 

  public void addObject(ObjectSpecification obj) 
  { objects.add(obj);
    objectmap.put(obj.getName(),obj); 
    if (obj.entity != null) 
    { Entity ent = obj.entity; 
      Vector res = (Vector) objectsOfClass.get(ent.getName()); 
      if (res == null) 
      { res = new Vector(); } 
      res.add(obj);
      objectsOfClass.put(ent.getName(),res);  
    } 
  } 


  public String toString()
  { String res = "MODEL "; 
    for (int i = 0; i < objects.size(); i++) 
    { res = res + objects.get(i) + "\n"; } 
    return res; 
  }

  public ObjectSpecification getObject(String nme)
  { return (ObjectSpecification) objectmap.get(nme); } 

  public ObjectSpecification getCorrespondingObject(String sename, String tename, String nme)
  { Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);
    ObjectSpecification sobj = getObject(nme); 
    if (sobj == null) 
    { return null; } 
 
    int sind = sobjs.indexOf(sobj); 
    if (sind < tobjs.size())
    { return (ObjectSpecification) tobjs.get(sind); }
    return null; 
  }  

  public ObjectSpecification getCorrespondingObject(String sename, String tename, ObjectSpecification sobj)
  { Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);

    if (sobj == null) 
    { return null; } 
 
    int sind = sobjs.indexOf(sobj); 
    if (sind < tobjs.size())
    { return (ObjectSpecification) tobjs.get(sind); }
    return null; 
  }  

  public ObjectSpecification getSourceObject(String sename, String tename, String nme)
  { Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);
    ObjectSpecification tobj = getObject(nme); 
    if (tobj == null) 
    { return null; } 
 
    int tind = tobjs.indexOf(tobj); 
    if (tind < sobjs.size())
    { return (ObjectSpecification) sobjs.get(tind); }
    return null; 
  }  

  public ObjectSpecification getSourceObject(String sename, String tename, ObjectSpecification tobj)
  { Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);

    if (tobj == null) 
    { return null; } 
 
    int tind = tobjs.indexOf(tobj); 
    if (tind < sobjs.size())
    { return (ObjectSpecification) sobjs.get(tind); }
    return null; 
  }  

  public Vector getObjects(String ename) 
  { Vector res = (Vector) objectsOfClass.get(ename);
    if (res == null) 
    { return new Vector(); }
    return res; 
  }  

  public Vector getCorrespondingObjects(String sename, String tename, Collection sobjs) 
  { Vector res = new Vector(); 

    for (Object x : sobjs) 
    { ObjectSpecification sobj = (ObjectSpecification) x; 
      ObjectSpecification tobj = getCorrespondingObject(sename, tename, sobj);
      if (tobj != null) 
      { res.add(tobj); }
    } 
    return res; 
  }  

  public Vector getSourceObjects(String sename, String tename, Collection tobjs) 
  { Vector res = new Vector(); 

    for (Object x : tobjs) 
    { ObjectSpecification tobj = (ObjectSpecification) x; 
      ObjectSpecification sobj = getSourceObject(sename, tename, tobj);
      if (sobj != null) 
      { res.add(sobj); }
    } 
    return res; 
  }  


  public boolean correspondingObjects(String sename, String tename, ObjectSpecification sobj, ObjectSpecification tobj) 
  { Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename); 
    int ind1 = sobjs.indexOf(sobj); 
    int ind2 = tobjs.indexOf(tobj); 
    if (ind1 >= 0 && ind2 >= 0 && ind1 == ind2) 
    { return true; } 
    return false; 
  } 

  public boolean correspondingObjectSequences(String sename, String tename, Vector svals, Vector tvals) 
  { if (svals.size() != tvals.size())
    { return false; } 

    Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);
    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 
      ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
      if (sobjs.indexOf(sobj) >= 0 && 
          sobjs.indexOf(sobj) == tobjs.indexOf(tobj))
      { } 
      else 
      { return false; } 
    } 
    return true; 
  }  

  public boolean correspondingObjectSets(String sename, String tename, Vector svals, Vector tvals) 
  { 
    Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);

    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 
      ObjectSpecification tobj = getCorrespondingObject(sename,tename,sobj.getName()); 
      System.out.println(">>> Corresponding object of " + sobj.getName() + " is " + tobj); 

      if (tobj != null && tvals.contains(tobj))
      { } 
      else 
      { return false; } 
    } 
    
    for (int i = 0; i < tvals.size(); i++) 
    { ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
      ObjectSpecification sobj = getCorrespondingObject(tename,sename,tobj.getName()); 
      System.out.println(">>> Corresponding object of " + tobj.getName() + " is " + sobj);
 
      if (sobj != null && svals.contains(sobj))
      { } 
      else 
      { return false; } 
    } 
    
    return true; 
  }  

  public Vector correspondingObjectSubset(String sename,
                    String tename, Vector svals, Vector tvals) 
  { // For each source element there is a corresponding target

    Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);
    Vector res = new Vector(); 

    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 
      ObjectSpecification tobj = getCorrespondingObject(sename,tename,sobj.getName()); 
      // System.out.println(">>> Corresponding object of " + sobj.getName() + " is " + tobj); 

      if (tobj != null && tvals.contains(tobj))
      { res.add(tobj); } 
      else 
      { return null; } 
    } 
    return res; 
  }  


}
