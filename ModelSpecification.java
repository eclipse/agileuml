import java.util.*; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ModelSpecification 
{ Vector objects = new Vector(); 
  Map correspondence = new Map();
  // needs to be a multi-map/relation
  Map inverseCorrespondence = new Map();  
   
  java.util.Map objectmap = new java.util.HashMap(); 
  java.util.Map objectsOfClass = new java.util.HashMap(); 

  public void addObject(ObjectSpecification obj) 
  { if (objects.contains(obj)) { } 
    else 
    { objects.add(obj); } 
	
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

  public void addCorrespondence(ObjectSpecification x, ObjectSpecification y) 
  { correspondence.add(x,y); 
    inverseCorrespondence.add(y,x); 
  } 
  // Assume both x and y are in the objects

  public String toString()
  { String res = "MODEL "; 
    for (int i = 0; i < objects.size(); i++) 
    { res = res + objects.get(i) + "\n"; } 
	
	res = res + "Correspondences: \n"; 
	res = res + correspondence + "\n"; 
	
    return res; 
  }

  public String toILP()
  { String res = ""; 
    for (int i = 0; i < objects.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objects.get(i); 
      res = res + obj.toILP() + "\n"; 
      Vector tobjs = correspondence.getAll(obj); 
      for (int j = 0; j < tobjs.size(); j++) 
      { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
	    res = res + obj.correspondence2ILP(tobj) + "\n"; 
	  } 
    } 
    return res; 
  } 


  public ObjectSpecification getObject(String nme)
  { return (ObjectSpecification) objectmap.get(nme); } 

  public ObjectSpecification getObjectValue(BasicExpression obj)
  { if (obj.objectRef != null) 
    { ObjectSpecification spec = getObjectValue((BasicExpression) obj.objectRef); 
      if (spec != null) 
      { return spec.getReferredObject(obj.data, this); }
      else 
      { return null; } 
    }
    return getObject(obj.data);  
  }  

  
  public ObjectSpecification getCorrespondingObject(String nme)
  { ObjectSpecification sobj = getObject(nme); 
    if (sobj == null) 
    { return null; } 
 
    ObjectSpecification tobj = (ObjectSpecification) correspondence.get(sobj); 
    if (tobj != null) 
    { return tobj; } 

    return null; 
  }  

  public ObjectSpecification getCorrespondingObject(String sename, String tename, String nme)
  { // Vector sobjs = getObjects(sename); 
    // Vector tobjs = getObjects(tename);
    ObjectSpecification sobj = getObject(nme); 
    if (sobj == null) 
    { return null; } 
 
    Vector objs = correspondence.getAll(sobj); 
    for (int i = 0; i < objs.size(); i++) 
	{ ObjectSpecification tobj = (ObjectSpecification) objs.get(i); 
      if (tobj != null && tename.equals(tobj.objectClass)) 
      { return tobj; } 
    } 

    // int sind = sobjs.indexOf(sobj); 
    // if (sind < tobjs.size())
    // { return (ObjectSpecification) tobjs.get(sind); }
    return null; 
  }  

  public ObjectSpecification getCorrespondingObject(ObjectSpecification sobj)
  { 
    if (sobj == null) 
    { return null; } 
 
    ObjectSpecification tobj = (ObjectSpecification) correspondence.get(sobj); 
    if (tobj != null) 
    { return tobj; } 

    return null; 
  }  

  public ObjectSpecification getCorrespondingObject(String sename, String tename, ObjectSpecification sobj)
  { // Vector sobjs = getObjects(sename); 
    // Vector tobjs = getObjects(tename);

    if (sobj == null) 
    { return null; } 
 
    Vector objs = correspondence.getAll(sobj); 
    for (int i = 0; i < objs.size(); i++) 
	{ ObjectSpecification tobj = (ObjectSpecification) objs.get(i); 
      if (tobj != null && tename.equals(tobj.objectClass)) 
      { return tobj; } 
    } 
	
    return null; 
  }  

  public ObjectSpecification getCorrespondingObject(ObjectSpecification sobj, String tename)
  { 
    if (sobj == null) 
    { return null; } 
 
    Vector objs = correspondence.getAll(sobj); 
    for (int i = 0; i < objs.size(); i++) 
	{ ObjectSpecification tobj = (ObjectSpecification) objs.get(i); 
      if (tobj != null && tename.equals(tobj.objectClass)) 
      { return tobj; } 
    } 
	
    return null; 
  }  

  public ObjectSpecification getSourceObject(String sename, String tename, String nme)
  { // Vector sobjs = getObjects(sename); 
    // Vector tobjs = getObjects(tename);
    ObjectSpecification tobj = getObject(nme); 
    if (tobj == null) 
    { return null; } 

    ObjectSpecification sobj = (ObjectSpecification) inverseCorrespondence.get(tobj); 
    if (sobj != null) 
    { return sobj; } 
 
    // int tind = tobjs.indexOf(tobj); 
    // if (tind < sobjs.size())
    // { return (ObjectSpecification) sobjs.get(tind); }
    return null; 
  }  

  public ObjectSpecification getSourceObject(String sename, String tename, ObjectSpecification tobj)
  { // Vector sobjs = getObjects(sename); 
    // Vector tobjs = getObjects(tename);

    if (tobj == null) 
    { return null; } 
 
    ObjectSpecification sobj = (ObjectSpecification) inverseCorrespondence.get(tobj); 
    if (sobj != null) 
    { return sobj; } 

    // int tind = tobjs.indexOf(tobj); 
    // if (tind < sobjs.size())
    // { return (ObjectSpecification) sobjs.get(tind); }
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

    Vector objs = new Vector(); 
    objs.addAll(sobjs); 

    for (int x = 0; x < objs.size(); x++) 
    { ObjectSpecification sobj = (ObjectSpecification) objs.get(x); 
      Vector tobjs = correspondence.getAll(sobj); 
      for (int i = 0; i < tobjs.size(); i++) 
	  { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(i); 
        if (tobj != null && tename.equals(tobj.objectClass)) 
        { res.add(tobj); } 
      } 
    } 
    return res; 
  }  

  public Vector getCorrespondingObjects(Collection sobjs, Vector restrictedsources, String tename) 
  { Vector res = new Vector(); 

    Vector objs = new Vector(); 
    objs.addAll(sobjs); 

    for (int x = 0; x < objs.size(); x++) 
    { ObjectSpecification sobj = (ObjectSpecification) objs.get(x); 
      Vector tobjs = correspondence.getAll(sobj); 
      for (int i = 0; i < tobjs.size(); i++) 
	  { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(i); 
        if (tobj != null && tename.equals(tobj.objectClass)) 
        { restrictedsources.add(sobj); 
          res.add(tobj); 
        }
      } 
	}
    return res; 
  }  

  public Vector getSourceObjects(String sename, String tename, Collection tobjs) 
  { Vector res = new Vector(); 

    Vector objs = new Vector(); 
    objs.addAll(tobjs); 

    for (int x = 0; x < objs.size(); x++) 
    { ObjectSpecification tobj = (ObjectSpecification) objs.get(x); 
      ObjectSpecification sobj = getSourceObject(sename, tename, tobj);
      if (sobj != null) 
      { res.add(sobj); }
    } 
    return res; 
  }  

  public Vector getSourceObjects(Collection tobjs) 
  { Vector res = new Vector(); 

    Vector objs = new Vector(); 
    objs.addAll(tobjs); 

    for (int x = 0; x < objs.size(); x++) 
    { ObjectSpecification tobj = (ObjectSpecification) objs.get(x); 
      Vector sources = inverseCorrespondence.getAll(tobj);
      if (sources != null) 
      { res.addAll(sources); }  
    } 
    return res; 
  }  


  public boolean correspondingObjects(String sename, String tename, ObjectSpecification sobj, ObjectSpecification tobj) 
  { // Vector sobjs = getObjects(sename); 
    // Vector tobjs = getObjects(tename); 
    // int ind1 = sobjs.indexOf(sobj); 
    // int ind2 = tobjs.indexOf(tobj); 
    // if (ind1 >= 0 && ind2 >= 0 && ind1 == ind2) 
    // { return true; }
	
	Vector sobjs = new Vector(); 
	sobjs.add(sobj); 
	
	Vector tobjs = getCorrespondingObjects(sename,tename,sobjs);
    if (tobjs.contains(tobj))
	{ return true; } 
    return false; 
  } 

  public boolean correspondingObjectSequences(String sename, String tename, Vector svals, Vector tvals) 
  { if (svals.size() != tvals.size())
    { return false; } 

    /* Vector sobjs = getObjects(sename); 
    Vector tobjs = getObjects(tename);
    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 
      ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
      if (sobjs.indexOf(sobj) >= 0 && 
          sobjs.indexOf(sobj) == tobjs.indexOf(tobj))
      { } 
      else 
      { return false; } 
    } */ 
	
	
    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 
      if (correspondingObjects(sename,tename,sobj,(ObjectSpecification) tvals.get(i))) {} 
	  else 
	  { return false; }
	} 
	return true; 
  }  

  public boolean correspondingObjectSets(Vector svals, Vector tvals) 
  { 
    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i);
      Vector tobjs = correspondence.getAll(sobj);
      boolean found = false; 
      for (int j = 0; j < tobjs.size(); j++) 
      { if (tvals.contains(tobjs.get(j)))
        { found = true; } 
      }  
      if (!found) { return false; }
    } 
    
    for (int i = 0; i < tvals.size(); i++) 
    { ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
      Vector sobjs = inverseCorrespondence.getAll(tobj); 
 
      boolean foundsource = false; 
      for (int j = 0; j < sobjs.size(); j++) 
      { if (svals.contains(sobjs.get(j)))
        { foundsource = true; } 
      } 
      if (!foundsource) { return false; } 
    } 
    
    return true; 
  }  

  public Vector correspondingObjectSupset(Vector svals, Vector tvals) 
  { // For each target element there is a corresponding source
    // Return the corresponding sources

    Vector res = new Vector(); 

    for (int i = 0; i < tvals.size(); i++) 
    { ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
      Vector sobjs = inverseCorrespondence.getAll(tobj); 

      boolean sourcefound = false; 

      for (int j = 0; j < sobjs.size(); j++)
      { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(j); 

        // System.out.println(">>> Corresponding source object of " + tobj.getName() + " is " + sobj.getName());
 
        if (svals.contains(sobj))
        { sourcefound = true; 
          res.add(sobj); 
        } 
      }

      if (!sourcefound) { return null; }   
    } 

    System.out.println(">>> Corresponding source objects of " + tvals + " are >> " + res); 

    return res; 
  }  

  public Vector correspondingObjectSubset(Vector svals, Vector tvals) 
  { // For each source element there is a corresponding target
    // Return the corresponding targets

    Vector res = new Vector(); 

    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 

      Vector tobjs = correspondence.getAll(sobj); 


	  boolean found = false; 
	  for (int j = 0; j < tobjs.size(); j++) 
	  { if (tvals.contains(tobjs.get(j)))
	    { found = true; 
          res.add(tobjs.get(j)); 
	    } 
	  }  
	  if (!found) { return null; }

    } 

    System.out.println(">>> Corresponding target objects of " + svals + " are >> " + res); 

    return res; 
  }  


  public boolean correspondingObjectSets(String sename, String tename, Vector svals, Vector tvals) 
  { 
    // Vector sobjs = getObjects(sename); 
    // Vector tobjs = getObjects(tename);

    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i);
	  Vector sobjs = new Vector(); 
	  sobjs.add(sobj);  
      Vector tobjs = getCorrespondingObjects(sename,tename,sobjs);
	  boolean found = false; 
	  for (int j = 0; j < tobjs.size(); j++) 
	  { if (tvals.contains(tobjs.get(j)))
	    { found = true; } 
	  }  
	  if (!found) { return false; }
    } 
    
    for (int i = 0; i < tvals.size(); i++) 
    { ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
      ObjectSpecification sobj = getSourceObject(tename,sename,tobj); 
      System.out.println(">>> Corresponding source object of " + tobj.getName() + " is " + sobj);
 
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
    // Return the corresponding targets

    Vector res = new Vector(); 

    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 

      Vector sobjs = new Vector(); 
	 sobjs.add(sobj);  
      Vector tobjs = getCorrespondingObjects(sename,tename,sobjs);
      System.out.println(">>> Corresponding objects of " + sobj.getName() + " are " + tobjs); 
	  boolean found = false; 
	  for (int j = 0; j < tobjs.size(); j++) 
	  { if (tvals.contains(tobjs.get(j)))
	    { found = true; 
            res.add(tobjs.get(j)); 
	    } 
	  }  
	  if (!found) { return null; }

    } 
    return res; 
  }  

  public Vector correspondingObjectSupset(String sename,
                                          String tename, Vector svals, Vector tvals) 
  { // For each target element there is a corresponding source
    // Return the corresponding sources

    Vector res = new Vector(); 

    for (int i = 0; i < tvals.size(); i++) 
    { ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
      ObjectSpecification sobj = getSourceObject(tename,sename,tobj); 

      if (sobj != null)
      { System.out.println(">>> Corresponding source object of " + tobj.getName() + " is " + sobj.getName());
 
        if (svals.contains(sobj))
        { res.add(sobj); } 
        else 
        { return null; }
      }  
    } 

    return res; 
  }  
  
  public boolean checkConditionInModel(Expression cond, Vector objects)
  { boolean res = true; 
    for (int i = 0; i < objects.size(); i++) 
	{ ObjectSpecification x = (ObjectSpecification) objects.get(i); 
	  if (x.satisfiesCondition(cond,this)) 
	  { System.out.println("** Instance " + x + " satisfies entity mapping condition " + cond); }
	  else 
	  { System.err.println("!! Instance " + x + " fails entity mapping condition " + cond);
	    res = false;
	  }
    }
	return res;  
  }

  public Vector validateSelectionConditions(Vector subclasses, Vector[] svals, Vector[] tvals, Attribute src) 
  { // For each sc : subclasses, check if all target elements 
    // correspond exactly to the sources with class sc
    Vector res = new Vector(); 
    int sn = svals.length; 
    BasicExpression sexpr = new BasicExpression(src);
    BasicExpression selfexp = new BasicExpression("self");  
          
    for (int d = 0; d < subclasses.size(); d++) 
    { Entity subc = (Entity) subclasses.get(d);
      Vector[] sourcesinsubc = new Vector[sn]; 
      for (int i = 0; i < sn; i++) 
      { Vector srcs = svals[i]; 
        Vector restrictedsrcs = new Vector(); 
        for (int j = 0; j < srcs.size(); j++) 
        { ObjectSpecification xobj = (ObjectSpecification) srcs.get(j); 
          if (xobj.getEntity() == subc) 
          { restrictedsrcs.add(xobj); } 
        } 
        sourcesinsubc[i] = restrictedsrcs; 
      } 
    
      // check for each i < sn that tvals[i] is derived from 
      // sourcesinsubc       

      for (int i = 0; i < tvals.length; i++) 
      { Vector trgs = tvals[i]; 
        Vector sobjs = correspondingObjectSupset(sourcesinsubc[i],trgs); 
        if (sobjs != null && 
            sobjs.containsAll(sourcesinsubc[i]) &&
            sourcesinsubc[i].containsAll(sobjs))
        { BinaryExpression oftype = new BinaryExpression("->oclIsTypeOf", selfexp, new BasicExpression(subc)); 
          res.add(new BinaryExpression("->select", sexpr, oftype)); 
        }  
      } 
    } 

    return res; 
  }  

  public Vector validateDiscriminatorConditions(Vector discriminators, Vector[] svals, Vector[] tvals, Attribute src) 
  { Vector res = new Vector();
    int sn = svals.length; 
    BasicExpression sexpr = new BasicExpression(src);
    BasicExpression selfexp = new BasicExpression("self");  

    for (int i = 0; i < discriminators.size(); i++) 
    { Attribute att = (Attribute) discriminators.get(i);
      String attname = att.getName(); 
 
      if (att.isBoolean())
      { // Try att=true and att=false as selection conditions
        Vector[] sourcessatisfyingcond = new Vector[sn]; 
        Vector[] sourcessatisfyingNotcond = new Vector[sn]; 
        
        for (int k = 0; k < sn; k++) 
        { Vector srcs = svals[k]; 
          Vector truesrcs = new Vector(); 
          Vector falsesrcs = new Vector();
 
          for (int j = 0; j < srcs.size(); j++) 
          { ObjectSpecification xobj = (ObjectSpecification) srcs.get(j); 
            if (xobj.getBoolean(attname) == true) 
            { truesrcs.add(xobj); }
            else 
            { falsesrcs.add(xobj); }  
          }
          
          sourcessatisfyingcond[k] = truesrcs; 
          sourcessatisfyingNotcond[k] = falsesrcs; 
        } 
 
        for (int k = 0; k < tvals.length; k++) 
        { Vector trgs = tvals[k]; 
          Vector sobjs = correspondingObjectSupset(sourcessatisfyingcond[k],trgs); 
          if (sobjs != null && 
              sobjs.containsAll(sourcessatisfyingcond[k]) &&
              sourcessatisfyingcond[k].containsAll(sobjs))
          { BasicExpression oftype = new BasicExpression(att); 
            res.add(new BinaryExpression("->select", sexpr, oftype)); 
          }  
        }

        for (int k = 0; k < tvals.length; k++) 
        { Vector trgs = tvals[k]; 
          Vector sobjs = correspondingObjectSupset(sourcessatisfyingNotcond[k],trgs); 
          if (sobjs != null && 
              sobjs.containsAll(sourcessatisfyingNotcond[k]) &&
              sourcessatisfyingNotcond[k].containsAll(sobjs))
          { UnaryExpression notatt = new UnaryExpression("not", new BasicExpression(att)); 
            res.add(new BinaryExpression("->select", sexpr, notatt)); 
          }  
        }
      }  
	  else if (att.isEnumeration())
	  { Type enumt = att.getType(); 
	    Vector values = enumt.getValues();
         Vector exprs = new Vector(); 
 
         for (int g = 0; g < values.size(); g++) 
         { String valg = (String) values.get(g); 
	     Vector[] sourceswithval = new Vector[sn]; 
        
          for (int k = 0; k < sn; k++) 
          { Vector srcs = svals[k]; 
            Vector valsrcs = new Vector();
 
            for (int j = 0; j < srcs.size(); j++) 
            { ObjectSpecification xobj = (ObjectSpecification) srcs.get(j);
              String attval = xobj.getEnumeration(attname);   
              if (attval != null && attval.equals(valg)) 
              { valsrcs.add(xobj); }
            }        
            sourceswithval[k] = valsrcs; 
          } 

          for (int k = 0; k < tvals.length; k++) 
          { Vector tobjs = tvals[k]; 
            Vector sobjs = correspondingObjectSupset(sourceswithval[k],tobjs); 
            if (sobjs != null && sobjs.size() > 0 && 
                sobjs.containsAll(sourceswithval[k]) &&
                sourceswithval[k].containsAll(sobjs))
            { BinaryExpression atteqval = new BinaryExpression("=", new BasicExpression(att), new BasicExpression(valg));
              if (exprs.contains(atteqval)) {} 
              else 
              { exprs.add(atteqval); }   
            }  
          }	
        }
        if (exprs.size() > 0)
        { Expression newlhs = Expression.formDisjunction(exprs); 
          res.add(new BinaryExpression("->select", sexpr, newlhs)); 
        } 
      }
    } 
    return res; 
  } 

  public Vector extraEntityMatches(Vector ems)
  { Vector res = new Vector(); 
  
    for (int i = 0; i < objects.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objects.get(i); 
      Vector corrs = correspondence.getAll(obj); 
      if (corrs == null || corrs.size() == 0)
      { }
      else 
      { Entity srcent = obj.getEntity(); 
        for (int j = 0; j < corrs.size(); j++) 
	   { ObjectSpecification tobj = (ObjectSpecification) corrs.get(j); 
	     Entity trgent = tobj.getEntity(); 
	     if (trgent != null)
	     { EntityMatching emx = ModelMatching.getRealEntityMatching(srcent,trgent,ems);
	       if (emx == null) 
	       { System.out.println(">>> Missing TL class mapping rule for correspondence " + obj.getName() + " |-> " + tobj.getName()); 
              EntityMatching newem = ModelMatching.getRealEntityMatching(srcent,trgent,res); 
              if (newem == null) 
              { newem = new EntityMatching(srcent,trgent);
                res.add(newem); 
              } 
			  // Deduce some feature mappings
             }		 
	      }
	    }
	  }
	}
	return res; 
  }
  
  public Vector objectPairsForEntityMatching(EntityMatching em, Vector removed)
  { Vector res = new Vector(); 
  
    Entity se = em.realsrc; 
    Entity te = em.realtrg; 
	Vector seobjs = (Vector) objectsOfClass.get(se.getName());
	if (seobjs == null) 
	{ System.err.println("!! No examples in the model of the class mapping " + se + " |--> " + te);
	  removed.add(em);  
	  return res; 
	}
	
	for (int i = 0; i < seobjs.size(); i++)
	{ ObjectSpecification sobj = (ObjectSpecification) seobjs.get(i); 
	  Vector tobjs = correspondence.getAll(sobj); 
	  for (int j = 0; j < tobjs.size(); j++) 
	  { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
	    if (tobj.getEntity() == te)
		{ Vector pair = new Vector(); 
		  pair.add(sobj); pair.add(tobj); 
		  res.add(pair); 
		}
	  }
	}
	return res; 
  } 
  
  public void extraAttributeMatches(Vector ems)
  { // for each em : ems, check if there are missing attribute matchings that are valid in the model
    Vector removed = new Vector(); 
	  
    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      Entity trgent = em.realtrg;
	  // Get the object pairs sx |-> tx instantiating em
      Vector pairs = objectPairsForEntityMatching(em,removed);
	  
      System.out.println(">>> Object matchings for " + em.realsrc + " |--> " + em.realtrg + " are: " + pairs); 
	  
      identifyConstantAttributeMatchings(trgent,em,pairs);
      em.realsrc.defineNonLocalFeatures(); 
       identifyCopyAttributeMatchings(em.realsrc,trgent,em,pairs,ems); 
	} 
	
     ems.removeAll(removed);     
  }   
			  
  public Vector identifyConstantAttributeMatchings(Entity ent, EntityMatching emx, Vector pairs)
  { Vector attributes = ent.allDefinedAttributes(); 
    int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    Vector res = new Vector();
	 
    for (int i = 0; i < attributes.size(); i++)
	{ Attribute att = (Attribute) attributes.get(i);
	  String attname = att.getName(); 
	   
	  if (att.isNumeric())
	  { double[] attvalues = new double[en]; 
	    boolean alldefined = true; 
		
	    for (int j = 0; j < en; j++) 
		{ Vector pair = (Vector) pairs.get(j); 
		  ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		  if (tobj.hasDefinedValue(attname)) { } 
		  else 
		  { alldefined = false; } 
		  attvalues[j] = tobj.getNumeric(attname); 
		}
		
		if (alldefined && AuxMath.isConstant(attvalues))
		{ double constv = attvalues[0]; 
		  System.out.println(">> Constant feature mapping " + constv + " |--> " + attname); 
		  AttributeMatching amx = new AttributeMatching(new BasicExpression(constv), att); 
		  res.add(amx); 
		  emx.addMapping(amx); 
		}
	  }
	  else if (att.isString())
	  { String[] attvalues = new String[en]; 
	    boolean alldefined = true; 
		
		for (int j = 0; j < en; j++) 
		{ Vector pair = (Vector) pairs.get(j); 
		  ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		  if (tobj.hasDefinedValue(attname)) { } 
		  else 
		  { alldefined = false; } 
		  attvalues[j] = tobj.getString(attname); 
		}
		
		if (alldefined && AuxMath.isConstant(attvalues))
		{ String constv = "\"" + attvalues[0] + "\""; 
		  System.out.println(">> Constant feature mapping " + constv + " |--> " + attname); 
		  AttributeMatching amx = new AttributeMatching(new BasicExpression(constv), att); 
		  res.add(amx); 
		  emx.addMapping(amx); 
		}
	  }	
	  else if (att.isBoolean())
	  { boolean[] attvalues = new boolean[en];
	    boolean alldefined = true; 
		 
	    for (int j = 0; j < en; j++) 
		{ Vector pair = (Vector) pairs.get(j); 
		  ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		  if (tobj.hasDefinedValue(attname)) { } 
		  else 
		  { alldefined = false; } 
		  attvalues[j] = tobj.getBoolean(attname); 
		}
		
		if (alldefined && AuxMath.isConstant(attvalues))
		{ boolean constv = attvalues[0]; 
		  System.out.println(">> Constant feature mapping " + constv + " |--> " + attname); 
		  AttributeMatching amx = new AttributeMatching(new BasicExpression(constv), att); 
		  res.add(amx); 
		  emx.addMapping(amx); 
		}
	  }   
	}
	return res; 
  }

  public Vector identifyCopyAttributeMatchings(Entity sent, Entity tent, EntityMatching emx, Vector pairs, Vector ems)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify additional copy feature mappings for " + sent + " |--> " + tent); 
    System.out.println(); 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    sattributes.addAll(s2atts); 
     
    Vector tattributes = tent.allDefinedProperties(); 
    Vector res = new Vector();
	 
    for (int i = 0; i < tattributes.size(); i++)
	{ Attribute tatt = (Attribute) tattributes.get(i);
	  String tattname = tatt.getName();
	  // System.out.println(">> " + tent + " target feature: " + tattname);
	  if (emx.isUnusedTargetByName(tatt)) { }
	  else 
	  { continue; } 
	  
	  System.out.println(); 
	  System.out.println(">> Unused target feature: " + tattname);
	   
	  Vector satts = ModelMatching.findCompatibleSourceAttributes(tatt,sattributes,emx,ems); 

	  for (int k = 0; k < satts.size(); k++) 
	  { Attribute satt = (Attribute) satts.get(k);
	    String sattname = satt.getName();  
	 
	    System.out.println(">> Checking possible map " + satt + " : " + satt.getType() + 
		                     " |--> " + tatt + " : " + tatt.getType()); 
	   	  
	    if (satt.isNumeric() && tatt.isNumeric())
	    { double[] sattvalues = new double[en]; 
	      double[] tattvalues = new double[en]; 
	      boolean alldefined = true; 
		
          for (int j = 0; j < en; j++) 
		  { Vector pair = (Vector) pairs.get(j); 
		    ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
		    ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
		    else 
		    { alldefined = false; } 
			sattvalues[j] = sobj.getNumeric(sattname);
		    tattvalues[j] = tobj.getNumeric(tattname); 
    	  }
		
		  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
		  { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx); 
	  	  }
	    }
	    else if (satt.isString() && tatt.isString())
	    { String[] sattvalues = new String[en]; 
	      String[] tattvalues = new String[en];
		  boolean alldefined = true; 
		
  		  for (int j = 0; j < en; j++) 
		  { Vector pair = (Vector) pairs.get(j); 
		    ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
		    ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
		    else 
		    { alldefined = false; } 
		    sattvalues[j] = sobj.getString(sattname); 
		    tattvalues[j] = tobj.getString(tattname); 
  		  }
		
	  	  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
		  { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx); 
		  }
	    }	
	    else if (satt.isBoolean() && tatt.isBoolean())
	    { boolean[] sattvalues = new boolean[en]; 
          boolean[] tattvalues = new boolean[en];
  	      boolean alldefined = true; 
		 
	      for (int j = 0; j < en; j++) 
		  { Vector pair = (Vector) pairs.get(j); 
		    ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
		    ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
		    else 
		    { alldefined = false; } 
		    sattvalues[j] = sobj.getBoolean(sattname); 
			tattvalues[j] = tobj.getBoolean(tattname); 
		  }
		
		  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
 		  { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx); 
		  }
		} 
	    else if (satt.isEntity() && tatt.isEntity())
	    { ObjectSpecification[] sattvalues = new ObjectSpecification[en]; 
          ObjectSpecification[] tattvalues = new ObjectSpecification[en];
  	      boolean alldefined = true; 
						 	 
	      for (int j = 0; j < en; j++) 
		  { Vector pair = (Vector) pairs.get(j); 
		    ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
		    ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
		    else 
		    { alldefined = false; } 
		    sattvalues[j] = sobj.getReferredObject(sattname,this); 
			tattvalues[j] = tobj.getReferredObject(tattname,this); 
		  }
		
		  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues,this))
 		  { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx); 
		  }
		} 
	    else if (satt.isCollection() && tatt.isCollection())
	    { Vector[] sattvalues = new Vector[en]; 
          Vector[] tattvalues = new Vector[en];
  	      boolean alldefined = true; 
	
							 	 
	      for (int j = 0; j < en; j++) 
		  { Vector pair = (Vector) pairs.get(j); 
		    ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
		    ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
		    else 
		    { alldefined = false; } 
		    sattvalues[j] = sobj.getCollection(sattname); 
			tattvalues[j] = tobj.getCollection(tattname); 
		  }
		
		  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues,this))
 		  { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx); 
		  }
		} 
	  }   
	}
	System.out.println(); 
	
	return res; 
  }

}
