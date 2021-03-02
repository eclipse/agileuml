import java.util.*; 
import java.io.*; 
import javax.swing.JOptionPane; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
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
                      // String -> ObjectSpecification 
  java.util.Map objectsOfClass = new java.util.HashMap(); 
                      // String -> Vector

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

  public Vector getClassNames(Vector objs)
  { Vector res = new Vector(); 
    for (int i = 0; i < objs.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objs.get(i); 
      String cname = obj.objectClass; 
      if (res.contains(cname)) { } 
      else 
      { res.add(cname); } 
    } 
    return res; 
  }   

  public Vector getClassNames(Vector sobjs, Attribute ref)
  { Vector res = new Vector(); 

    for (int i = 0; i < sobjs.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(i); 
      ObjectSpecification obj = sobj.getReferredObject(ref,this);
      if (obj != null) 
      { String cname = obj.objectClass; 
        if (res.contains(cname)) { } 
        else 
        { res.add(cname); }
      }  
    } 
	return res; 
  }  


  public void addCorrespondence(ObjectSpecification x, ObjectSpecification y) 
  { correspondence.add(x,y); 
    inverseCorrespondence.add(y,x); 
  } 
  // Assume both x and y are in the objects

  public void defineCorrespondences(String sf, String tf)
  { for (int i = 0; i < objects.size(); i++) 
    { ObjectSpecification obj1 = (ObjectSpecification) objects.get(i); 
      Entity e1 = obj1.entity; 
      if (e1 != null && e1.isSource() && e1.hasDefinedAttribute(sf))
      { for (int j = 0; j < objects.size(); j++) 
        { ObjectSpecification obj2 = (ObjectSpecification) objects.get(j); 
          Entity e2 = obj2.entity; 
          if (e2 != null && e2.isTarget() && e2.hasDefinedAttribute(tf))
          { Object v1 = obj1.getRawValue(sf); 
            Object v2 = obj2.getRawValue(tf); 
            if (v1 != null && v2 != null && v1.equals(v2))
            { addCorrespondence(obj1, obj2); 
              System.out.println(">>> Added correspondence " + obj1 + " |-> " + obj2); 
            }
          } 
	   } 
	 } 
     } 
  } 
		  
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

  public void defineComposedFeatureValues(Vector entities, Vector types)
  { // For 2-step source features f.g define the value of
    // the feature in each source object

    for (int i = 0; i < entities.size(); i++) 
    { Entity sent = (Entity) entities.get(i); 
      if (sent != null) // && sent.isSource()) 
      { sent.defineExtendedNonLocalFeatures(); 
        Vector allFeatures = sent.getNonLocalFeatures();
        String sentname = sent.getName(); 
        
        Vector sobjs = (Vector) objectsOfClass.get(sentname); 
        if (sobjs == null)
        { sobjs = new Vector(); }
		
        for (int j = 0; j < sobjs.size(); j++) 
        { ObjectSpecification obj = (ObjectSpecification) sobjs.get(j); 
          for (int k = 0; k < allFeatures.size(); k++) 
          { Attribute attk = (Attribute) allFeatures.get(k); 
            Object val = obj.getValueOf(attk,this); 
            System.out.println(">> Value of (" + obj + ")." + attk + " = " + val);
            String attkname = attk.getName(); 
            obj.setValue(attkname, val);  
          } 
        }  
      } 
    }  
      
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

  public ObjectSpecification[] getAllReferredObjects(Vector sobjs, Attribute ref)
  { ObjectSpecification[] res = new ObjectSpecification[sobjs.size()]; 
    for (int i = 0; i < sobjs.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(i); 
	 res[i] = sobj.getReferredObject(ref,this);
      // System.out.println(">> " + sobj + " " + ref + " = " + res[i]); 
	} 
	return res; 
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


  public Vector getCorrespondingObjects(ObjectSpecification sobj)
  { return correspondence.getAll(sobj); }
    

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

  public Vector getCorrespondingObjects(ObjectSpecification sobj, String tename)
  { Vector res = new Vector(); 
  
    Vector objs = correspondence.getAll(sobj); 
    for (int i = 0; i < objs.size(); i++) 
	{ ObjectSpecification tobj = (ObjectSpecification) objs.get(i); 
      if (tobj != null && tename.equals(tobj.objectClass)) 
      { res.add(tobj); } 
    } 

    return res; 
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
  
  public java.util.HashMap objectMergings(String tename, Vector sourceClasses, Vector unmerged)
  { Vector tobjs = (Vector) objectsOfClass.get(tename);
    if (tobjs == null) 
    { return null; }
	
	java.util.HashMap res = new java.util.HashMap(); 
	
	for (int i = 0; i < tobjs.size(); i++)
	{ ObjectSpecification tobj = (ObjectSpecification) tobjs.get(i); 
	  Vector sobjs = inverseCorrespondence.getAll(tobj); 
	  if (sobjs != null && sobjs.size() > 1)
	  { System.out.println(">>> Instance merging of " + sobjs + " into " + tobj); 
          res.put(tobj,sobjs); 
          for (int j = 0; j < sobjs.size(); j++) 
          { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(j); 
            if (sourceClasses.contains(sobj.objectClass)) { }
            else 
            { sourceClasses.add(sobj.objectClass); }
          }
        }
        else if (sobjs != null && sobjs.size() == 1) 
        { unmerged.add(sobjs.get(0)); } 
	} 
	return res; 
  } 
  
  public EntityMatching checkMergingCondition(Entity tent, Vector entities, Vector ems, java.util.HashMap mergings, Vector unmerged)
  { // for each tobj |-> sobjs in mergings, check for which features the sobjs have same values
    Vector identificationProperties = new Vector(); 
    Entity sent = null; 
	
    Vector keys = new Vector(); 
    keys.addAll(mergings.keySet()); 
	
    if (keys.size() == 0) { return null; } 
	
    ObjectSpecification key0 = (ObjectSpecification) keys.get(0); 
    Vector sobjs0 = (Vector) mergings.get(key0); 
    ObjectSpecification sobj0 = (ObjectSpecification) sobjs0.get(0); 
    sent = sobj0.getEntity(); 
    if (sent != null) 
    { sent.defineNonLocalFeatures(); 
      Vector allFeatures0 = sent.allDefinedProperties(); 
      allFeatures0.addAll(sent.getNonLocalFeatures()); 
      identificationProperties.addAll(allFeatures0); 
    } 
    else 
    { System.err.println("!! No class is known for object " + sobj0); 
      return null; 
    }

    EntityMatching sent2tent =  ModelMatching.getRealEntityMatching(sent,tent,ems); 
      // for merging
    EntityMatching sent2tentUnmerged = null; 
 
    if (unmerged.size() > 0) 
    { // Need two copies of the sent |--> tent mapping, one 
      // for merged elements & one for unmerged. 
      sent2tentUnmerged = new EntityMatching(sent,tent); 
      sent2tentUnmerged.mergeWith(sent2tent); 
      ems.add(sent2tentUnmerged); 
    } 

	 
    for (int i = 0; i < keys.size(); i++) 
    { ObjectSpecification key = (ObjectSpecification) keys.get(i); 
      Vector sobjs = (Vector) mergings.get(key); 
      ObjectSpecification sobj = (ObjectSpecification) sobjs.get(0); 
      sent = sobj.getEntity(); 
      if (sent != null) 
      { sent.defineNonLocalFeatures(); 
        Vector allFeatures = sent.allDefinedProperties(); 
        allFeatures.addAll(sent.getNonLocalFeatures()); 
        // System.out.println(">> All features of " + sobj + " are: " + allFeatures); 
        identificationProperties.retainAll(allFeatures); 
		
        for (int j = 0; j < allFeatures.size(); j++) 
        { Attribute satt = (Attribute) allFeatures.get(j); 
          if (satt.isString())
          { String[] xstrs = new String[sobjs.size()];
            for (int k = 0; k < sobjs.size(); k++)
            { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(k); 
              String srcstr = srcobj.getStringValue(satt,this); 
              xstrs[k] = srcstr; 
            } 
			
            if (AuxMath.isConstant(xstrs))
            { System.out.println(">>> " + satt + " is constant on the merged source instances."); } 
            else 
            { identificationProperties.remove(satt); }
          }
          else if (satt.isNumeric())
          { double[] xstrs = new double[sobjs.size()];
            for (int k = 0; k < sobjs.size(); k++)
            { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(k); 
              double srcd = srcobj.getNumericValue(satt,this); 
              xstrs[k] = srcd; 
            }
			 
            if (AuxMath.isConstant(xstrs))
            { System.out.println(">>> " + satt + " is constant on the merged source instances."); } 
            else 
            { identificationProperties.remove(satt); }
          }
          else if (satt.isBoolean())
          { identificationProperties.remove(satt); }
          else if (satt.isEnumeration())
          { identificationProperties.remove(satt); }
          else if (satt.isEntity())
          { ObjectSpecification[] xstrs = new ObjectSpecification[sobjs.size()];
            ObjectSpecification[] ystrs = new ObjectSpecification[sobjs.size()];
            for (int k = 0; k < sobjs.size(); k++)
            { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(k); 
              ObjectSpecification srcref = srcobj.getReferredObject(satt,this);
			  // System.out.println(">>> " + satt + " of " + srcobj + " is: " + srcref);  
              xstrs[k] = srcref; 
              /* Vector trgobjs = (Vector) correspondence.getAll(srcref); 
              if (trgobjs != null && trgobjs.size() > 0) 
              { ObjectSpecification trgobj = (ObjectSpecification) trgobjs.get(0); 
                ystrs[k] = trgobj;
              } 
              else 
              { ystrs[k] = null; } */  
            } 
			
            if (AuxMath.isConstant(xstrs))
            { System.out.println(">>> " + satt + " is constant on the merged source instances."); } 
			// else if (AuxMath.isConstant(ystrs))
            // { System.out.println(">>> " + satt + "' is constant in the target model.");
			  // if (identificationProperties.contains(satt)) { } 
			  // else 
			  // { identificationProperties.add(satt); }  
			// } 
             else 
             { identificationProperties.remove(satt); }
          }
          else if (satt.isCollection())
          { Vector[] xstrs = new Vector[sobjs.size()];
            for (int k = 0; k < sobjs.size(); k++)
            { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(k); 
              Vector srcvect = srcobj.getCollectionValue(satt,this);
              xstrs[k] = srcvect; 
            }  
            if (AuxMath.isConstant(xstrs))
            { System.out.println(">>> " + satt + " is constant on the merged sources."); } 
            else 
            { identificationProperties.remove(satt); }	        
          }
	    }
	  }
	}
	
	System.out.println(">>> Identification properties of " + sent + " = " + identificationProperties); 
	// Remove r.f if r is already an identification property
	Vector idprops = Attribute.reduceToInitialPaths(identificationProperties); 
	System.out.println(">>> Reduced properties are: " + idprops);
	System.out.println();  
	
	Attribute tkey = tent.getPrincipalPrimaryKey(); 

	if (tkey == null && sent != null && idprops.size() > 0)
	{ Expression lhs = Expression.combineBySum(idprops); 
	  AttributeMatching newam = null;   
	  String pkname = tent.getName().toLowerCase() + "Id"; 
	  
	  // if (tkey != null && tkey.isString())
	  // { newam = new AttributeMatching(lhs,tkey); }
	  // else 
	  
	  String yn = 
        JOptionPane.showInputDialog("Create an instance merging assignment to new identifier attribute?: " + lhs + " |--> " + pkname + "? (y/n):");

       if (yn != null && "y".equals(yn))         
       { Attribute newpk = new Attribute(pkname, new Type("String", null), ModelElement.INTERNAL);
         newpk.setIdentity(true); 
         tent.addAttribute(newpk);  
         newam = new AttributeMatching(lhs,newpk); 
         System.out.println(">>> new attribute mapping: " + newam); 
         if (sent2tent != null)
         { sent2tent.addAttributeMatching(newam);
           BasicExpression sx = new BasicExpression(sent.getName().toLowerCase() + "x");
		   sx.setType(new Type(sent)); 
		   BasicExpression selfexp = new BasicExpression("self"); 
		   selfexp.setType(new Type(sent)); 
           BinaryExpression sentexists = new BinaryExpression(":", sx, new BasicExpression(sent));
           BinaryExpression neqobjs = new BinaryExpression("/=", sx, selfexp); 
           Expression sxlhs = lhs.addReference(sx, new Type(sent)); 
           BinaryExpression eqidens = new BinaryExpression("=", lhs, sxlhs); 
           eqidens.setBrackets(true); 
           BinaryExpression eqlhs = new BinaryExpression("&", neqobjs, eqidens);   
           Expression mergecond = new BinaryExpression("#", sentexists, eqlhs); 
           sent2tent.addCondition(mergecond);
           if (sent2tentUnmerged != null) 
           { UnaryExpression notmerge = new UnaryExpression("not", mergecond); 
             sent2tentUnmerged.addCondition(notmerge); 
           }  
         }
	  }
	  
  	  // Else, create one.   
	} 
	// Create a new primary key for tent, and mapping  idprops(+) |--> tentId
     return sent2tent; 
  }

  public Vector checkMergingMappings(Entity tent, EntityMatching sent2tent, Vector sourceClasses, Vector entities, Vector ems, java.util.HashMap mergings, Vector unmerged)
  { // for each tobj |-> sobjs in mergings, see what specific feature mappings hold, eg, union of the sobjs.r gives tobj.r
    Vector res = new Vector(); 
    Entity sent = null; 
	
    if (tent != null) 
    { // tent.defineNonLocalFeatures(); 
      Vector allFeatures = tent.allDefinedProperties(); 
      // allFeatures.addAll(tent.getNonLocalFeatures()); 
  	
      Vector keys = new Vector(); 
      keys.addAll(mergings.keySet()); 
      if (keys.size() == 0) 
      { return res; }
	  
      for (int j = 0; j < allFeatures.size(); j++) 
      { Attribute tatt = (Attribute) allFeatures.get(j); 
        if (tatt.isCollection())
        { Vector mergingsourceatts = new Vector(); 
          ObjectSpecification tobj = (ObjectSpecification) keys.get(0); 
          Vector sobjs = (Vector) mergings.get(tobj); 
          if (sobjs.size() > 0)
          { sent = ((ObjectSpecification) sobjs.get(0)).entity; } 
		  
          mergingsourceatts = checkMergingUnion(tatt,sobjs,tobj,entities,ems);
		    
          for (int i = 1; i < keys.size(); i++) 
          { tobj = (ObjectSpecification) keys.get(i); 
            sobjs = (Vector) mergings.get(tobj); 
            // tent = tobj.getEntity(); 
            // System.out.println(">> All features of " + sobj + " are: " + allFeatures); 
            Vector satts = checkMergingUnion(tatt,sobjs,tobj,entities,ems);
            mergingsourceatts.retainAll(satts);  
          }
          System.out.println(">>> Identified merging source attributes: " + mergingsourceatts + " to " + tatt); 
          System.out.println();

          if (mergingsourceatts.size() > 0)
          { Attribute satt = (Attribute) mergingsourceatts.get(0); 
            AttributeMatching newam = new AttributeMatching(satt,tatt);
            newam.unionSemantics = true;  
            res.add(newam); 
            if (sent != null) 
            { // EntityMatching sent2tent = ModelMatching.getRealEntityMatching(sent,tent,ems);
	         if (sent2tent != null)
	         { sent2tent.addAttributeMatching(newam); }
	         System.out.println(">>> Added new merging mapping: " + newam);
              System.out.println();  
            } 
          } 
        } 
        else if (tatt.isEntity())  // look for assignments satt |--> tatt for each merging source
        { Vector assigningsourceatts = new Vector(); 
          ObjectSpecification tobj = (ObjectSpecification) keys.get(0); 
          Vector sobjs = (Vector) mergings.get(tobj); 
          if (sobjs.size() > 0)
          { sent = ((ObjectSpecification) sobjs.get(0)).entity; } 
		  
          assigningsourceatts = checkMergingAssignment(tatt,sobjs,tobj,entities,ems);

          for (int i = 1; i < keys.size(); i++) 
          { tobj = (ObjectSpecification) keys.get(i); 
            sobjs = (Vector) mergings.get(tobj); 
            // tent = tobj.getEntity(); 
            // System.out.println(">> All features of " + sobj + " are: " + allFeatures); 
            Vector satts = checkMergingAssignment(tatt,sobjs,tobj,entities,ems);
            assigningsourceatts.retainAll(satts);  
          }
          System.out.println(">>> Identified assigning source attributes: " + assigningsourceatts + " to " + tatt); 
          System.out.println();
          if (assigningsourceatts.size() > 0) 
          { Attribute satt = (Attribute) assigningsourceatts.get(0); 
            AttributeMatching newamx = new AttributeMatching(satt,tatt);
            if (sent2tent != null)
	      { sent2tent.addAttributeMatching(newamx); }        
          } 
        }
      }
    }
    return res; 
  }
  
  private Vector checkMergingUnion(Attribute tatt, Vector sobjs, ObjectSpecification tobj, Vector entities, Vector ems)
  { Vector res = new Vector(); 
  
    if (sobjs.size() == 0) 
    { return res; }
	
	ObjectSpecification sobj0 = (ObjectSpecification) sobjs.get(0); 
	Entity sent = (Entity) ModelElement.lookupByName(sobj0.objectClass,entities); 
	if (sent == null) 
	{ return res; }
	Vector sourceFeatures = sent.allDefinedProperties(); 
    
	for (int i = 0; i < sourceFeatures.size(); i++) 
	{ Attribute satt = (Attribute) sourceFeatures.get(i); 
      if (ModelMatching.compatibleType(satt,tatt,ems) || ModelMatching.compatibleElementType(satt,tatt,ems))
	  { if (satt.isCollection())	
        { Vector sattvalues = new Vector(); 
          Vector tattvalues = tobj.getCollectionValue(tatt,this);
	
          System.out.println(">> Checking collection feature mapping " + satt + " over all merged sources |--> " + tatt); 
							 	 
	      for (int j = 0; j < sobjs.size(); j++) 
		  { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(j); 
		    // if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
            // else 
		    // { alldefined = false; } 
		    sattvalues = VectorUtil.union(sattvalues,sobj.getCollectionValue(satt,this)); 
	      }
		
          System.out.println(sobjs + "." + satt + " = " + sattvalues); 
          System.out.println(tobj + "." + tatt + " = " + tattvalues); 

		  if (correspondingObjectSets(sattvalues,tattvalues))
 		  { System.out.println(">> Union feature mapping " + satt + " |--> " + tatt); 
		    // AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(satt); 
		    // emx.addMapping(amx);
		  }
		}
        // else if (satt.isEntity())
		
      }
    }
    return res; 
  } 
  
  private Vector checkMergingAssignment(Attribute tatt, Vector sobjs, ObjectSpecification tobj, Vector entities, Vector ems)
  { Vector res = new Vector(); 
  
    if (sobjs.size() == 0) 
    { return res; }
	
	ObjectSpecification sobj0 = (ObjectSpecification) sobjs.get(0); 
	Entity sent = (Entity) ModelElement.lookupByName(sobj0.objectClass,entities); 
	if (sent == null) 
	{ return res; }
	sent.defineNonLocalFeatures(); 
    Vector sourceFeatures = sent.allDefinedProperties(); 
    sourceFeatures.addAll(sent.getNonLocalFeatures()); 
	
	System.out.println(">>> Possible assignment features: " + sourceFeatures); 
    
	for (int i = 0; i < sourceFeatures.size(); i++) 
	{ Attribute satt = (Attribute) sourceFeatures.get(i); 
      if (ModelMatching.compatibleType(satt,tatt,ems) || ModelMatching.compatibleElementType(satt,tatt,ems) ||
	      ModelMatching.compatibleSourceElementType(satt,tatt,ems))
	  { System.out.println(">>> " + satt + " compatible type to " + tatt); 
	    if (satt.isCollection())	
        { Vector sattvalues = new Vector(); 
          Vector tattvalues = new Vector(); 
		  ObjectSpecification tref = tobj.getReferredObject(tatt,this);
	      tattvalues.add(tref); 
		
          System.out.println(">> Checking collection feature mapping " + satt + " over all merged sources |--> " + tatt); 
							 	 
	      for (int j = 0; j < sobjs.size(); j++) 
		  { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(j); 
		    // if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
            // else 
		    // { alldefined = false; } 
		    sattvalues = VectorUtil.union(sattvalues,sobj.getCollectionValue(satt,this)); 
	      }
		
          System.out.println(sobjs + "." + satt + " = " + sattvalues); 
          System.out.println(tobj + "." + tatt + " = " + tattvalues); 

		  if (correspondingObjectSets(sattvalues,tattvalues))
 		  { System.out.println(">> Assignment feature mapping " + satt + " |--> " + tatt); 
		    // AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(satt); 
		    // emx.addMapping(amx);
		  }
		}
        // else if (satt.isEntity())
		else if (satt.isEntity())	
        { Vector sattvalues = new Vector(); 
          Vector tattvalues = new Vector(); 
		  ObjectSpecification tref = tobj.getReferredObject(tatt,this);
	      tattvalues.add(tref); 
		  
          System.out.println(">> Checking assignment feature mapping " + satt + " over all merged sources |--> " + tatt); 
							 	 
	      for (int j = 0; j < sobjs.size(); j++) 
		  { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(j); 
		    // if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
            // else 
		    // { alldefined = false; }
			ObjectSpecification sref = sobj.getReferredObject(satt,this); 
			if (sattvalues.contains(sref)) { } 
			else 
			{ sattvalues.add(sref); } 
	      }
		
          System.out.println(sobjs + "." + satt + " = " + sattvalues); 
          System.out.println(tobj + "." + tatt + " = " + tattvalues); 

		  if (correspondingObjectSets(sattvalues,tattvalues))
 		  { System.out.println(">> Assignment feature mapping " + satt + " |--> " + tatt); 
		    // AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(satt); 
		    // emx.addMapping(amx);
		  }
		}
      }
    }
    return res; 
  } 
  
  public java.util.HashMap objectSplittings(String sename)
  { Vector sobjs = (Vector) objectsOfClass.get(sename);
    if (sobjs == null) 
    { return null; }
	
    java.util.HashMap res = new java.util.HashMap(); 
	
    for (int i = 0; i < sobjs.size(); i++)
    { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(i); 
      Vector tobjs = correspondence.getAll(sobj); 
      if (tobjs != null && tobjs.size() > 1)
      { // But sort into sets with the same target class.   
        java.util.HashMap tobjectsets = new java.util.HashMap();
 
        for (int j = 0; j < tobjs.size(); j++) 
        { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
          Entity tent = tobj.getEntity(); 
          if (tent != null)
          { Vector tentobjs = (Vector) tobjectsets.get(tent.getName()); 
            if (tentobjs == null)
            { tentobjs = new Vector(); } 
            tentobjs.add(tobj); 
            tobjectsets.put(tent.getName(), tentobjs); 
          }
        } 

        System.out.println(">>> Instance replication of " + sobj + " into " + tobjectsets); 
        System.out.println();
 
        Vector keys = new Vector(); 
        keys.addAll(tobjectsets.keySet()); 
        for (int y = 0; y < keys.size(); y++)
        { String key = (String) keys.get(y); 
          if (((Vector) tobjectsets.get(key)).size() > 1)
          { res.put(sobj,tobjectsets.get(key)); } 
        }  
        // Only counts those where at least 2 target objects in type key. 
        // Assumes there is only one target class for which 
        // there is replication. 
	 }
    } 
    return res; 
  } 

  public void checkSplittingCondition(Entity sent, Vector entities, Vector ems, java.util.HashMap mergings)
  { // for each sobj |-> tobjs in mergings, check for which features the tobjs all have different values

    Vector identificationProperties = new Vector(); 
    java.util.HashMap valuemap = new java.util.HashMap(); 
      // Map from target atts to (sobj -> tobj tatt values)
	
    Vector keys = new Vector(); 
    keys.addAll(mergings.keySet()); 
	
    Entity tent = null;
    java.util.Map perclassIdentificationProperties = new java.util.HashMap(); 
    // for each classname, the potential identification properties
    // of the target class.  
	
    for (int i = 0; i < keys.size(); i++) 
    { ObjectSpecification key = (ObjectSpecification) keys.get(i); 
      Vector tobjs = (Vector) mergings.get(key); 
      ObjectSpecification tobj = (ObjectSpecification) tobjs.get(0); 
      tent = tobj.getEntity(); 

      if (tent != null) 
      { tent.defineNonLocalFeatures(); 
        Vector allFeatures = tent.allDefinedProperties(); 
	    // allFeatures.addAll(tent.getNonLocalFeatures()); 
        System.out.println(">> All direct features of " + tobj + " are: " + allFeatures);
        String tentname = tent.getName(); 
        Vector tidents = (Vector) perclassIdentificationProperties.get(tentname); 
        if (tidents == null) 
        { tidents = new Vector(); 
          tidents.addAll(allFeatures); 
          perclassIdentificationProperties.put(tentname,tidents); 
        } 
        // remove a tatt from this set if it is not unique 
        // for all splitting targets for every source key.  
        
        for (int j = 0; j < allFeatures.size(); j++) 
        { Attribute tatt = (Attribute) allFeatures.get(j); 
          if (tatt.isString())
          { String[] xstrs = new String[tobjs.size()];
            for (int k = 0; k < tobjs.size(); k++)
            { ObjectSpecification trgobj = (ObjectSpecification) tobjs.get(k); 
              String trgstr = trgobj.getStringValue(tatt,this);
              System.out.println(">** (" + trgobj + ")." + tatt + " = " + trgstr);  
              xstrs[k] = trgstr; 
            } 
			
            if (AuxMath.allDifferent(xstrs))
            { System.out.println(">>> feature " + tatt + " has distinct values for different replicated target instances.");
              if (identificationProperties.contains(tatt)) { } 
              else 
              { identificationProperties.add(tatt); }  

              String tattname = tatt.getName(); 
              java.util.HashMap vmap = (java.util.HashMap) valuemap.get(tattname); 
              if (vmap == null) 
              { vmap = new java.util.HashMap(); }
              vmap.put(key,xstrs); 
              valuemap.put(tattname,vmap); 
            } 
            else 
            { Vector removedtatts = new Vector(); 
              removedtatts.add(tatt); 
              identificationProperties.removeAll(removedtatts);
              tidents.removeAll(removedtatts); 
            }
          }
          else if (tatt.isNumeric())
          { double[] xstrs = new double[tobjs.size()];
            for (int k = 0; k < tobjs.size(); k++)
            { ObjectSpecification srcobj = (ObjectSpecification) tobjs.get(k); 
              double srcd = srcobj.getNumericValue(tatt,this); 
              xstrs[k] = srcd; 
            } 

            if (AuxMath.allDifferent(xstrs))
            { System.out.println(">>> feature " + tatt + " has distinct values for each replicated target instance.");
              if (identificationProperties.contains(tatt)) { } 
              else 
              { identificationProperties.add(tatt); }
                String tattname = tatt.getName(); 
                java.util.HashMap vmap = (java.util.HashMap) valuemap.get(tattname); 
                if (vmap == null) 
                { vmap = new java.util.HashMap(); }
                vmap.put(key,xstrs); 
                valuemap.put(tattname,vmap);  
              } 
              else 
              { Vector removedtatts = new Vector(); 
                removedtatts.add(tatt); 
                identificationProperties.removeAll(removedtatts);
                tidents.removeAll(removedtatts); 
              }
            }
            else if (tatt.isEntity())
            { ObjectSpecification[] xstrs = new ObjectSpecification[tobjs.size()];
              Vector xvector = new Vector(); 
			
              for (int k = 0; k < tobjs.size(); k++)
              { ObjectSpecification trgobj = (ObjectSpecification) tobjs.get(k); 
                ObjectSpecification trgref = trgobj.getReferredObject(tatt,this);
                xstrs[k] = trgref; 
                xvector.add(trgref); 
              } 
			
              if (AuxMath.allDifferent(xstrs))
              { System.out.println(">>> feature " + tatt + " has distinct values for different replicated target instances.");
                if (identificationProperties.contains(tatt)) { } 
                else 
                { identificationProperties.add(tatt); }
                String tattname = tatt.getName(); 
                java.util.HashMap vmap = (java.util.HashMap) valuemap.get(tattname); 
                if (vmap == null) 
                { vmap = new java.util.HashMap(); }
                vmap.put(key,xstrs); 
                System.out.println(">>> values for " + tattname + " of target objects for " + key + " are: " + xvector); 
                valuemap.put(tattname,vmap);  
              } 
              else 
              { Vector removedtatts = new Vector(); 
                removedtatts.add(tatt); 
                identificationProperties.removeAll(removedtatts);
                tidents.removeAll(removedtatts); 
              }
            }
            else if (tatt.isCollection())
            { Vector[] xstrs = new Vector[tobjs.size()];
              for (int k = 0; k < tobjs.size(); k++)
              { ObjectSpecification trgobj = (ObjectSpecification) tobjs.get(k); 
                Vector trgvect = trgobj.getCollectionValue(tatt,this);
			  // System.out.println(">>> (" + srcobj + ")." + tatt + " = " + srcvect);
                xstrs[k] = trgvect; 
              }  

              if (AuxMath.allDifferent(xstrs))
              { System.out.println(">>> feature " + tatt + " has distinct values for each replicated target instance of " + key);
                if (identificationProperties.contains(tatt)) { } 
                else 
                { identificationProperties.add(tatt); } 
                String tattname = tatt.getName(); 
                java.util.HashMap vmap = (java.util.HashMap) valuemap.get(tattname); 
                if (vmap == null) 
                { vmap = new java.util.HashMap(); }
                vmap.put(key,xstrs); 
                valuemap.put(tattname,vmap); 
              } // actually vmap[key] should be the array of all base elements within any of the xstrs[k] vectors
              else 
              { Vector removedtatts = new Vector(); 
                removedtatts.add(tatt); 
                identificationProperties.removeAll(removedtatts);
                tidents.removeAll(removedtatts); 
              }	        
            }
	    }
	  }
	}
	
	System.out.println(">>> Instance replication properties for " + sent.getName() + " = " + identificationProperties);
	System.out.println(">>> Target instance replication properties for " + sent.getName() + " = " + perclassIdentificationProperties);
	
	Vector allsobjs = (Vector) objectsOfClass.get(sent.getName());
	// System.out.println(">>> All " + sent + " instances are = " + allsobjs);
    
     sent.defineNonLocalFeatures(); 
	    
     for (int j = 0; j < identificationProperties.size(); j++) 
	{ Attribute tatt = (Attribute) identificationProperties.get(j);
	  // Entity tent = tatt.getOwner(); 
	  String tattname = tatt.getName(); 
	  Type tattElementType = tatt.getElementType(); 
	  String telemName = ""; 
	  if (tattElementType != null) 
	  { telemName = tattElementType.getName(); }
	  
	  Vector possibleSourceFeatures = new Vector(); 
	  java.util.HashMap splittingConds = new java.util.HashMap(); 

        Vector s1atts = sent.allDefinedProperties();
        Vector s2atts = sent.getNonLocalFeatures(); 
        Vector sattributes = new Vector(); 
        sattributes.addAll(s1atts); 
        sattributes.addAll(s2atts); 
    
        Vector satts = ModelMatching.findBaseTypeCompatibleSourceAttributes(tatt,sattributes,sent,ems); 
        System.out.println(">>> Possible replication range source features are: " + satts); 
	  // Only interested in collection-valued satt. Could take unions of them. 
	  
	  java.util.HashMap vmap = (java.util.HashMap) valuemap.get(tattname); 

        for (int i = 0; i < keys.size(); i++) 
        { ObjectSpecification sobj = (ObjectSpecification) keys.get(i); 

          if (tatt.isEntity()) 
          { ObjectSpecification[] values = (ObjectSpecification[]) vmap.get(sobj); 
		  // compare these target values to mapped values of some feature sobj.f

            if (values != null) 
            { // System.out.println(">>> for source " + sobj + " target feature " + tatt + " values are: " + values.length); 
              for (int k = 0; k < satts.size(); k++) 
              { Attribute satt = (Attribute) satts.get(k); 
                String sattname = satt.getName();
                ObjectSpecification[] sattvalues = sobj.getCollectionAsObjectArray(satt,this); 
			     // getAllReferredObjects(allsobjs,satt); 
                if (sattvalues != null && AuxMath.isCopy(sattvalues,values,this))
                { System.out.println(">> " + sobj + " satisfies copy feature mapping " + sattname + " |--> " + tattname);
                  if (possibleSourceFeatures.contains(satt)) { } 
                  else 
                  { possibleSourceFeatures.add(satt); } 
                } 
                else 
                { possibleSourceFeatures.remove(satt); }
			  
                for (int m = k+1; m < satts.size(); m++) 
                { Attribute satt1 = (Attribute) satts.get(m); 
                  String satt1name = satt1.getName(); 
                  ObjectSpecification[] satt1values = sobj.getCollectionAsObjectArray(satt1,this); 
                  if (satt1values != null && AuxMath.isUnion(sattvalues,satt1values,values,telemName,this))
                  { System.out.println(">> " + sobj + " satisfies union feature mapping " + sattname + "->union(" + satt1name + ") |--> " + tattname);
                    BasicExpression sattbe = new BasicExpression(satt); 
                    sattbe.setUmlKind(Expression.ATTRIBUTE); 
                    BasicExpression satt1be = new BasicExpression(satt1); 
                    satt1be.setUmlKind(Expression.ATTRIBUTE); 
                    BinaryExpression binexpr = new BinaryExpression("->union", sattbe, satt1be);  
                    binexpr.setType(satt.getType()); 
                    binexpr.setElementType(satt.getElementType()); 
				  
                   if (possibleSourceFeatures.contains(binexpr)) { } 
                   else 
                   { possibleSourceFeatures.add(binexpr); }
                 }  
               }
             }
           }
         }
	    else if (tatt.isCollection()) // assume, an entity collection 
	    { Vector[] values = (Vector[]) vmap.get(sobj); 
		  // compare the values to values of some feature sobj.f

          
		  if (values != null) 
		  { // System.out.println(">>> for source " + sobj + " target feature " + tatt + " values are: " + values.length); 
		    Vector flattenedValues = VectorUtil.flattenVectorArray(values); 
		    // System.out.println(">>> for source " + sobj + " target feature " + tatt + " values are: " + flattenedValues); 
		    
            for (int k = 0; k < satts.size(); k++) 
               { Attribute satt = (Attribute) satts.get(k); 
                 String sattname = satt.getName();
                 Vector sattvalues = sobj.getCollectionValue(satt,this); 
			     // getAllReferredObjects(allsobjs,satt); 
              // System.out.println(">>> for source " + sobj + " source feature " + satt + " values are: " + sattvalues); 
		    
                 if (sattvalues != null && correspondingObjectSets(sattvalues,flattenedValues))
 		      { System.out.println(">> " + sobj + " satisfies copy feature mapping " + sattname + " |--> " + tattname);
                   if (possibleSourceFeatures.contains(satt)) { } 
                   else 
                   { possibleSourceFeatures.add(satt); } 
			 } 
			 else 
                 { possibleSourceFeatures.remove(satt); }
	          }
		  }
		}
        else if (tatt.isString())
		{ String[] values = (String[]) vmap.get(sobj); 
		  if (values != null) 
		  { // System.out.println(">>> for source " + sobj + " target feature " + tatt + " values are: " + values.length); 
 		    for (int k = 0; k < satts.size(); k++) 
			{ Attribute satt = (Attribute) satts.get(k); 
			  String sattname = satt.getName();
			  String[] sattvalues = sobj.getCollectionAsStringArray(satt,this); 
			     // getAllReferredObjects(allsobjs,satt); 
			  if (sattvalues != null && AuxMath.isCopy(sattvalues,values))
 		      { System.out.println(">> " + sobj + " satisfies copy feature mapping " + sattname + " |--> " + tattname);
			    if (possibleSourceFeatures.contains(satt)) { } 
				else 
				{ possibleSourceFeatures.add(satt); } 
			  } 
			  else if (sattvalues != null && AuxMath.allSuffixed(sattvalues,values)) 
			  { System.out.println(">> " + sobj + " satisfies feature mapping " + 
				                   sattname + " + suffix |--> " + tattname);
				Vector suffixes = AuxMath.getSuffixes(sattvalues,values); 
				System.out.println(">> All suffixes = " + suffixes); 
				
				SetExpression suffixesexpr = new SetExpression(); 
				for (int p = 0; p < suffixes.size(); p++) 
				{ String suff = (String) suffixes.get(p); 
				  BasicExpression suffbe = new BasicExpression("\"" + suff + "\"");
				  BasicExpression sattbe = new BasicExpression(satt); 
				  sattbe.setUmlKind(Expression.ATTRIBUTE); 
				  BinaryExpression sattplus = new BinaryExpression("+", 
				                                sattbe, suffbe); 
				  sattplus.setType(new Type("String", null)); 
				  sattplus.setElementType(new Type("String", null));
				  suffixesexpr.addElement(sattplus); 
				}
				
			    if (possibleSourceFeatures.contains(satt)) { } 
		        else 
		        { possibleSourceFeatures.add(satt); }  
                Attribute x$ = new Attribute("x$0", satt.getElementType(), ModelElement.INTERNAL); 
				BinaryExpression insuffixset = 
				  new BinaryExpression(":", new BasicExpression(x$), suffixesexpr); 
				splittingConds.put(sattname, insuffixset); 
			  } 
			  else if (sattvalues != null && AuxMath.allPrefixed(sattvalues,values)) 
			  { System.out.println(">> " + sobj + " satisfies feature mapping " + 
				                   "prefix + " + sattname + " |--> " + tattname);
				Vector prefixes = AuxMath.getPrefixes(sattvalues,values); 
				System.out.println(">> All prefixes = " + prefixes); 
				
				SetExpression prefixesexpr = new SetExpression(); 
				for (int p = 0; p < prefixes.size(); p++) 
				{ String pref = (String) prefixes.get(p); 
				  BasicExpression prefbe = new BasicExpression("\"" + pref + "\""); 
				  BasicExpression sattbe = new BasicExpression(satt); 
				  sattbe.setUmlKind(Expression.ATTRIBUTE); 
				  BinaryExpression plussatt = new BinaryExpression("+", prefbe, sattbe); 
				  plussatt.setType(new Type("String", null)); 
				  plussatt.setElementType(new Type("String", null));
				  prefixesexpr.addElement(plussatt); 
				}
			    if (possibleSourceFeatures.contains(satt)) { } 
		        else 
		        { possibleSourceFeatures.add(satt); }  
				
                Attribute x$ = new Attribute("x$0", satt.getElementType(), ModelElement.INTERNAL); 
				BinaryExpression inprefixset = 
				  new BinaryExpression(":", new BasicExpression(x$), prefixesexpr); 
				splittingConds.put(sattname, inprefixset); 
			  } 
			  else 
			  { possibleSourceFeatures.remove(satt); }
			}
		  }
		}
	  }
	  
	  System.out.println(">>> Possible instance replication variables for " + sent + " |--> " + tent + " are " + 
	                     possibleSourceFeatures); 
	// lookup the sent |-> tent matching, add the possible x |--> tatt mappings, for  
     // extra condition  x : sent

      if (possibleSourceFeatures.size() > 0 && tent != null) 
      { EntityMatching sent2tent = ModelMatching.getRealEntityMatching(sent,tent,ems);
        if (sent2tent != null)
        { Object satt = possibleSourceFeatures.get(0); 
          Type elemT = null; 
          Expression rang = null; 
		  
          if (satt instanceof Attribute) 
          { elemT = ((Attribute) satt).getElementType(); 
            rang = new BasicExpression((Attribute) satt);
			rang.setUmlKind(Expression.ATTRIBUTE);  
          }
          else if (satt instanceof Expression)
          { elemT = ((Expression) satt).getElementType(); 
            rang = (Expression) satt; 
          }
		  
          Attribute x = new Attribute("x$0", elemT, ModelElement.INTERNAL); 
          AttributeMatching newam = new AttributeMatching(x,tatt); 
          sent2tent.replaceAttributeMatching(newam);
          Expression cond = (Expression) splittingConds.get(satt + ""); 
          if (cond != null)
          { sent2tent.addReplicationCondition(cond);
            sent2tent.addCondition(cond);
          }
          else 
          { BinaryExpression xinsatt = new BinaryExpression(":", new BasicExpression(x), rang); 
            sent2tent.addReplicationCondition(xinsatt);
            sent2tent.addCondition(xinsatt);
          }   
        }
      } 
    } 
	
	
    System.out.println(); 
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
  
  public Vector getAllCorrespondingSourceObjects(Vector tobjs)
  { Vector res = new Vector(); 
    for (int i = 0; i < tobjs.size(); i++) 
	{ ObjectSpecification tobj = (ObjectSpecification) tobjs.get(i); 
	  Vector sobjs = inverseCorrespondence.getAll(tobj);
	  res = VectorUtil.union(res,sobjs);  
	}
	return res; 
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

  public Vector getCorrespondingElements(Collection sobjs) 
  { Vector res = new Vector(); 

    Vector objs = new Vector(); 
    objs.addAll(sobjs); 

    for (int x = 0; x < objs.size(); x++) 
    { if (objs.get(x) instanceof ObjectSpecification) 
      { ObjectSpecification sobj = (ObjectSpecification) objs.get(x); 
        Vector tobjs = correspondence.getAll(sobj); 
        for (int i = 0; i < tobjs.size(); i++) 
        { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(i); 
          if (tobj != null) 
          { res.add(tobj); }
        }  
      } 
      else 
      { res.add(objs.get(x)); } // a basic value 
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
  { // System.out.println(">>>Testing correspondence of " + svals + " ~ " + tvals); 
  
    for (int i = 0; i < svals.size(); i++) 
    { Vector tobjs = new Vector(); 
      if (svals.get(i) instanceof ObjectSpecification)
      { ObjectSpecification sobj = (ObjectSpecification) svals.get(i);
        tobjs = correspondence.getAll(sobj);
      } // else it is a simple value
      else 
      { tobjs.add(svals.get(i)); }
        
      boolean found = false; 
      for (int j = 0; j < tobjs.size(); j++) 
      { if (tvals.contains(tobjs.get(j)))
        { found = true; } 
      }  
      if (!found) { return false; }
    } // For every x : svals, there exists corresponding tx : tvals
    
    for (int i = 0; i < tvals.size(); i++) 
    { Vector sobjs = new Vector(); 
      if (tvals.get(i) instanceof ObjectSpecification)
      { ObjectSpecification tobj = (ObjectSpecification) tvals.get(i); 
        sobjs = inverseCorrespondence.getAll(tobj);
      } 
      else 
      { sobjs.add(tvals.get(i)); } 
 
      boolean foundsource = false; 
      for (int j = 0; j < sobjs.size(); j++) 
      { if (svals.contains(sobjs.get(j)))
        { foundsource = true; } 
      } 
      if (!foundsource) { return false; } 
    } // For every tx : tvals, there exists corresponding x : vals
    
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

        System.out.println(">>> A corresponding source object of " + tobj.getName() + " is " + sobj.getName());
 
        if (svals.contains(sobj))
        { sourcefound = true; 
          res.add(sobj); 
        } 
      }

      if (!sourcefound) 
	  { System.out.println(">!! Object " + tobj.getName() + " has no corresponding source in " + svals); 
	    return null;
	  }   
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
	  { System.out.println("** Instance " + x + " satisfies condition " + cond); }
	  else 
	  { System.err.println("!! Instance " + x + " fails condition " + cond);
	    res = false;
	  }
    }
	return res;  
  }
  
  public boolean isConstant(Attribute attr, Vector objects)
  { boolean res = false;
    boolean alldefined = true;
    if (objects.size() < 2) 
    { return false; } 
 
    String attname = attr.getName(); // assume it is a string
    String[] attvalues = new String[objects.size()]; 
		
    for (int j = 0; j < objects.size(); j++) 
    { ObjectSpecification tobj = (ObjectSpecification) objects.get(j); 
      if (tobj.hasDefinedValue(attname)) { } 
      else 
      { alldefined = false; } 
      attvalues[j] = tobj.getString(attname); 
		
      if (alldefined && AuxMath.isConstant(attvalues))
      { String constv = "\"" + attvalues[0] + "\""; 
        System.out.println(">> Constant attribute " + constv + " |--> " + attname);
        return true;  
      }	  
    }
	return res;  
  }
  
  public boolean disjointValueSets(Attribute att, Vector[] sobjs, Vector[] tobjs, Vector passedValues, Vector rejectedValues)
  { // The att values for the sobjs[i] corresponding to tobjs[i] are always in a set Passed which 
    // is disjoint from the set, Rejected, of values of the sobjs[i] not corresponding to the tobjs[i]
	
	boolean res = true; 
		
	for (int i = 0; i < sobjs.length && i < tobjs.length; i++) 
	{ Vector sourceobjs = sobjs[i]; 
	  Vector targetobjs = tobjs[i]; 
	  Vector reverseImage = getAllCorrespondingSourceObjects(targetobjs); 
	  Vector restrictedSources = new Vector(); 
	  restrictedSources.addAll(reverseImage); 
	  restrictedSources.retainAll(sourceobjs); 
	  System.out.println(">> subset of " + sourceobjs + " corresponding to " + targetobjs + " is: " + restrictedSources); 
	  Vector othersources = new Vector(); 
	  othersources.addAll(sourceobjs); 
	  othersources.removeAll(restrictedSources); 
	  System.out.println(">> subset of " + sourceobjs + " rejected from " + targetobjs + " is: " + othersources); 
	  
	  Vector passedAttValues = ObjectSpecification.getAllValuesOf(att,restrictedSources,this); 
	  Vector rejectedAttValues = ObjectSpecification.getAllValuesOf(att,othersources,this); 
	  
	  passedValues.addAll(passedAttValues); 
	  rejectedValues = VectorUtil.union(rejectedValues,rejectedAttValues); 
	  System.out.println(">> Passed values of " + att + " = " + passedValues + " rejected values: " + rejectedValues); 
	  // return false as soon as these intersect. 
    }
	
	Vector intersection = new Vector(); 
	intersection.addAll(passedValues); 
	intersection.retainAll(rejectedValues);
	System.out.println(">>> Common values of " + passedValues + " and " + rejectedValues + " are: " + intersection); 
	 
	if (intersection.size() == 0) 
	{ return true; } 
	return false; 
  }

  public Vector validateSelectionConditions(Vector subclasses, Vector[] svals, Vector[] tvals, Attribute src) 
  { // For each sc : subclasses, check if all target elements 
    // correspond exactly to the sources with class sc

    
    Vector res = new Vector(); 
    boolean allEmpty = true; // ignore cases where sources and targets are all empty
	
	if (tvals.length == 0) 
	{ return res; } 
	  
    for (int i = 0; i < tvals.length; i++) 
    { Vector trgs = tvals[i]; 
      if (trgs.size() == 0) 
	  { }
	  else 
	  { allEmpty = false; }
	} 
	
	if (allEmpty) 
	{ return res; }
		
    int sn = svals.length; 
    BasicExpression sexpr = new BasicExpression(src);
    BasicExpression selfexp = new BasicExpression("self");  
	selfexp.setType(sexpr.getElementType()); 
          
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
          Expression selexp = new BinaryExpression("->select", sexpr, oftype);
          selexp.setType(sexpr.getType()); 
          selexp.setElementType(sexpr.getElementType()); 
          res.add(selexp);  
        }  
      } 
    } 

    return res; 
  }  

  public Vector validateDiscriminatorConditions(Vector discriminators, Vector[] svals, Vector[] tvals, Attribute src) 
  { Vector res = new Vector();
    int sn = svals.length; 
    if (sn < 2) 
    { return res; }
	
	// Attributes are either booleans, enums, strings, or references to an abstract class; optional collections could 
	// also be possible. 
    boolean allEmpty = true; // ignore cases where sources and targets are all empty
	
	if (tvals.length == 0) 
	{ return res; } 
	  
    for (int i = 0; i < tvals.length; i++) 
    { Vector trgs = tvals[i]; 
      if (trgs.size() == 0) 
	  { }
	  else 
	  { allEmpty = false; }
	} 
	
	if (allEmpty) 
	{ return res; }
	
    BasicExpression sexpr = new BasicExpression(src);
    sexpr.setUmlKind(Expression.ATTRIBUTE);
		      
    BasicExpression selfexp = new BasicExpression("self");  
    selfexp.setType(src.getElementType()); 

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
		  // System.out.println(">>> objects " + truesrcs + " satisfy condition " + attname + " = true"); 
          sourcessatisfyingNotcond[k] = falsesrcs; 
		  // System.out.println(">>> objects " + falsesrcs + " fail condition " + attname + " = true"); 
        } 

        boolean validPositiveCondition = true;  
        for (int k = 0; k < tvals.length; k++) 
        { Vector trgs = tvals[k]; 
          Vector sobjs = correspondingObjectSupset(sourcessatisfyingcond[k],trgs); 
          if (sobjs != null && 
              sobjs.containsAll(sourcessatisfyingcond[k]) &&
              sourcessatisfyingcond[k].containsAll(sobjs))
          { System.out.println(">>> The collections " + sobjs + " and " + sourcessatisfyingcond[k] + " are equal"); }  
		  else 
		  { validPositiveCondition = false; }
		}
            
	    if (validPositiveCondition)
		{ BasicExpression oftype = new BasicExpression(att); 
		  Expression selexp = new BinaryExpression("->select", sexpr, oftype);
		  selexp.setType(sexpr.getType()); 
		  selexp.setElementType(sexpr.getElementType());
		  res.add(selexp);   
		}

        boolean validNegativeCondition = true;  
        for (int k = 0; k < tvals.length; k++) 
        { Vector trgs = tvals[k]; 
          Vector sobjs = correspondingObjectSupset(sourcessatisfyingNotcond[k],trgs); 
          if (sobjs != null && 
              sobjs.containsAll(sourcessatisfyingNotcond[k]) &&
              sourcessatisfyingNotcond[k].containsAll(sobjs))
          { System.out.println(">>> The collections " + sobjs + " and " + sourcessatisfyingNotcond[k] + " are equal"); }  
		  else 
		  { System.out.println(">>> The collections " + sobjs + " and " + sourcessatisfyingNotcond[k] + " are NOT EQUAL");
		    validNegativeCondition = false; 
		  }
        }
		
		if (validNegativeCondition)
		{ UnaryExpression notatt = new UnaryExpression("not", new BasicExpression(att)); 
		  Expression rejexp = new BinaryExpression("->select", sexpr, notatt);
		  rejexp.setType(sexpr.getType()); 
		  rejexp.setElementType(sexpr.getElementType()); 
		  res.add(rejexp);  
        }
      }  
	  else if (att.isEnumeration())
	  { Vector pvals = new Vector(); 
	    Vector rvals = new Vector();
		boolean areDisjoint = disjointValueSets(att,svals,tvals,pvals,rvals); 
		if (areDisjoint)
		{ Expression expr = Expression.disjoinCases(att,pvals); 
		  System.out.println(">> Identified discriminator condition: " + att + " : " + pvals + " ie: " + expr);
		  Expression selexp = new BinaryExpression("->select", sexpr, expr);
		  selexp.setType(sexpr.getType()); 
		  selexp.setElementType(sexpr.getElementType());
		  res.add(selexp);
	    }
	  
	  /* Type enumt = att.getType(); 
	    Vector values = enumt.getValues();
        Vector exprs = new Vector(); 
 
        for (int g = 0; g < values.size(); g++) 
        { String valg = (String) values.get(g); 
	      Vector[] sourceswithval = new Vector[sn]; 
	      Vector[] sourceswithNotval = new Vector[sn]; 
        
          for (int k = 0; k < sn; k++) 
          { Vector srcs = svals[k]; 
            Vector valsrcs = new Vector();
			Vector notvalsrcs = new Vector(); 
 
            for (int j = 0; j < srcs.size(); j++) 
            { ObjectSpecification xobj = (ObjectSpecification) srcs.get(j);
              String attval = xobj.getEnumeration(attname);   
			  System.out.println(">>> (" + xobj + ")." + att + " = " + attval); 

              if (attval != null && attval.equals(valg)) 
              { valsrcs.add(xobj); }
			  else if (attval != null) 
			  { notvalsrcs.add(xobj); }
            }        
            sourceswithval[k] = valsrcs;
			sourceswithNotval[k] = notvalsrcs;  
  		    System.out.println(">>> objects " + valsrcs + " satisfy condition " + att + " = " + valg); 
  		    System.out.println(">>> objects " + notvalsrcs + " satisfy condition " + att + " /= " + valg); 
          } 

          boolean validEqValCondition = true;   // att = val is a possible selection condition
		  boolean validNotEqValCondition = true;  // att /= val is a possible selection condition
		  
          for (int k = 0; k < tvals.length; k++) 
          { Vector tobjs = tvals[k]; 
            Vector sobjs = correspondingObjectSupset(sourceswithval[k],tobjs); 
            if (sobjs != null && // sobjs.size() > 0 && 
                sobjs.containsAll(sourceswithval[k]) &&
                sourceswithval[k].containsAll(sobjs))
            { System.out.println(">>> sources " + sobjs + " corresponding to " + tobjs + " = objects " + 
			                     sourceswithval[k] + " that satisfy the condition"); } 
			else 
			{ System.out.println(">>> sources " + sobjs + " of " + tobjs + " /= objects " + 
			                     sourceswithval[k] + " that satisfy condition"); 
              validEqValCondition = false; 
			} 
          }	
		  
		  if (validEqValCondition)
		  { BinaryExpression atteqval = new BinaryExpression("=", new BasicExpression(att), new BasicExpression(valg));
            if (exprs.contains(atteqval)) {} 
            else 
            { exprs.add(atteqval); }   
          } 
		  else 
		  { for (int k = 0; k < tvals.length; k++) 
            { Vector tobjs = tvals[k]; 
              Vector sobjs = correspondingObjectSupset(sourceswithNotval[k],tobjs); 
              if (sobjs != null && // sobjs.size() > 0 && 
                sobjs.containsAll(sourceswithNotval[k]) &&
                sourceswithNotval[k].containsAll(sobjs))
              { System.out.println(">>> sources " + sobjs + " corresponding to " + tobjs + " = objects " + 
			                     sourceswithNotval[k] + " that satisfy the condition"); } 
			  else 
			  { System.out.println(">>> sources " + sobjs + " of " + tobjs + " /= objects " + 
			                     sourceswithNotval[k] + " that satisfy condition"); 
                validNotEqValCondition = false; 
			  } 
            }	
		  
		    if (validNotEqValCondition)
		    { BinaryExpression atteqval = new BinaryExpression("/=", new BasicExpression(att), new BasicExpression(valg));
              if (exprs.contains(atteqval)) {} 
              else 
              { exprs.add(atteqval); }   
            }
		  } 
        }

        if (exprs.size() > 0)
        { Expression newlhs = Expression.formConjunction(exprs); 
          Expression selexp = new BinaryExpression("->select", sexpr, newlhs);
		  selexp.setType(sexpr.getType()); 
		  selexp.setElementType(sexpr.getElementType());
		  res.add(selexp);  
        } */ 
	 }
	 else if (att.isEntity()) // Treat the subclasses of att.type as an enumeration. 
     { Type enttype = att.getType(); 
	   Entity abstractEnt = enttype.getEntity(); 
       Vector esubs = abstractEnt.getAllSubclasses();
	   Vector abstractEntList = new Vector(); 
	   abstractEntList.add(abstractEnt);  
	   esubs.removeAll(abstractEntList); 

       Vector exprs = new Vector(); 
 
       for (int g = 0; g < esubs.size(); g++) 
       { Entity sub = (Entity) esubs.get(g); 
         String valg = sub.getName();
			 
	     Vector[] sourceswithval = new Vector[sn]; // The sources with att in subclass sub
        
         for (int k = 0; k < sn; k++) 
         { Vector srcs = svals[k]; 
           Vector valsrcs = new Vector();
 
           for (int j = 0; j < srcs.size(); j++) 
           { ObjectSpecification xobj = (ObjectSpecification) srcs.get(j);
             ObjectSpecification attobj = xobj.getReferredObject(attname,this);
             if (attobj != null)
             { String attval = attobj.objectClass;    
               // System.out.println(">>> " + xobj + "." + att + "->oclIsTypeOf(" + attval + ")"); 

               if (attval != null && attval.equals(valg)) 
               { valsrcs.add(xobj); }
			   else if (attobj.entity != null && Entity.isDescendantOrEqualTo(attobj.entity,valg))
			   { valsrcs.add(xobj); }
             } 
           }       
           sourceswithval[k] = valsrcs; 
           System.out.println(">>> objects " + valsrcs + " satisfy condition " + att + "->oclIsKindOf(" + valg + ")"); 
         } 

         boolean validEqValCondition = true;   // att->oclIsTypeOf(val) is a possible selection condition
		  
         for (int k = 0; k < tvals.length; k++) 
         { Vector tobjs = tvals[k]; 
           Vector sobjs = correspondingObjectSupset(sourceswithval[k],tobjs); 
           if (sobjs != null && // sobjs.size() > 0 && 
               sobjs.containsAll(sourceswithval[k]) &&
               sourceswithval[k].containsAll(sobjs))
           { System.out.println(">>> the source objects " + sobjs + " corresponding to targets, and sources " + sourceswithval[k] + " with the condition, are EQUAL"); } 
           else 
           { System.out.println(">>> source objects " + sobjs + " corresponding to " + tobjs + ", and sources " + sourceswithval[k] + " with condition, are NOT EQUAL"); 
             validEqValCondition = false; 
           } 
         }	
		  
         if (validEqValCondition)
         { BinaryExpression atteqval = new BinaryExpression("->oclIsKindOf", new BasicExpression(att), new BasicExpression(valg));
           if (exprs.contains(atteqval)) {} 
           else 
           { exprs.add(atteqval); }   
         } 
       }
		
       if (exprs.size() > 0)
       { Expression newlhs = Expression.formConjunction(exprs); 
         Expression selexp = new BinaryExpression("->select", sexpr, newlhs);
		 selexp.setType(sexpr.getType()); 
		 selexp.setElementType(sexpr.getElementType());
		 res.add(selexp);  
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
			  // Deduce some feature mappings, and abstract mappings? 
            }		 
	      }
	    }
	  }
	}
	return res; 
  }
  
  public Vector completeClassMappings(Vector entities, Vector unusedSources, Vector res) 
  { // For each abstract source class srcabs, if each subclass has a mapping  src |--> trg
    // take least common superclass of the trg's as the trgabs for srcabs. 

    for (int i = 0; i < entities.size(); i++)
	{ Entity ent = (Entity) entities.get(i); 
	  if (ent.isAbstract() && ent.isSource())
	  { if (unusedSources.contains(ent))
	    { System.out.println("! Abstract source entity " + ent + " has no entity mapping. Trying to infer the mapping."); 
		  Vector allsubs = ent.getSubclasses();
		  Vector subtargets = new Vector(); 
		   
		  for (int j = 0; j < allsubs.size(); j++) 
		  { Entity sub = (Entity) allsubs.get(j); 
		    EntityMatching subem = ModelMatching.getEntityMatching(sub,res); 
			if (subem != null)
			{ subtargets.add(subem.realtrg); }
		  }
		  
		  if (subtargets.size() > 0)
		  { Entity suptarg = Entity.leastCommonSuperclass(subtargets); 
		    if (suptarg != null) 
			{ System.out.println(">>> Possible target for " + ent + " is " + suptarg); 
			  EntityMatching newem = new EntityMatching(ent,suptarg); 
			  res.add(newem); 
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
	Expression cond = em.condition; 
	
	Vector seobjs = (Vector) objectsOfClass.get(se.getName());
	if (seobjs == null) 
	{ System.err.println("!! No examples in the model of the class mapping " + se + " |--> " + te);
	  System.err.println("!! It will be removed from the TL specification !!");
	  removed.add(em);  
	  return res; 
	}
	
	for (int i = 0; i < seobjs.size(); i++)
	{ ObjectSpecification sobj = (ObjectSpecification) seobjs.get(i); 
	  if (cond == null || sobj.satisfiesCondition(cond,this))
	  { Vector tobjs = correspondence.getAll(sobj); 
	    for (int j = 0; j < tobjs.size(); j++) 
	    { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
	      if (tobj.getEntity() == te)
	  	  { Vector pair = new Vector(); 
		    pair.add(sobj); pair.add(tobj); 
		    res.add(pair); 
		  }
	    }
	  }
	} 
	
	System.out.println(">> The instantiating pairs of mapping "); 
	System.out.println(em); 
	System.out.println(">> are: " + res); 
	System.out.println(); 
	
	return res; 
  } // You only want the pairs where sobj satifies the em.condition
  
  public void extraAttributeMatches(Vector ems, Vector tms)
  { // for each em : ems, check if there are missing attribute matchings that are valid in the model
    Vector removed = new Vector(); 
	  
    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      Entity trgent = em.realtrg;
	  // Get the object pairs sx |-> tx instantiating em
      Vector pairs = objectPairsForEntityMatching(em,removed);
	  
      System.out.println(">>> Object matchings for " + em.realsrc + " |--> " + em.realtrg + " are: " + pairs); 
	  if (pairs.size() == 0) 
	  { System.out.println(">> This mapping has no instances, it will be removed from the TL specification."); 
	    removed.add(em); 
	  }
	  else 
      { trgent.defineLocalFeatures(); 
        identifyConstantAttributeMatchings(trgent,em,pairs);
        em.realsrc.defineNonLocalFeatures(); 
        identifyCopyAttributeMatchings(em.realsrc,trgent,em,pairs,ems,tms); 
	    identifyManyToOneAttributeMatchings(em.realsrc,trgent,em,pairs,ems);
	    identifyTwoToOneAttributeMatchings(em.realsrc,trgent,em,pairs,ems);
		// checkMissingTargetIdentities(em.realsrc,trgent,em,pairs,ems); 
	  } 
	} 
	
    ems.removeAll(removed);     
  }   
  
  public void checkPrimaryKeyAssignments(Vector ems)
  { // for each em : ems, check if there are missing attribute matchings that are valid in the model
    Vector removed = new Vector(); 
	  
    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      Entity trgent = em.realtrg;
      checkMissingTargetIdentities(em.realsrc,trgent,em,ems);  
	} 
  } 

  public void checkMissingTargetIdentities(Entity sent, Entity tent, EntityMatching emx, Vector ems)
  { Vector sattributes = sent.getAttributes(); 
    // allDefinedAttributes();
    System.out.println(">>> All defined attributes of " + sent + " are " + sattributes); 
	 
    Vector t1atts = tent.allDefinedProperties();
	Vector t2atts = tent.getNonLocalFeatures(); 
	Vector tattributes = new Vector(); 
	tattributes.addAll(t1atts); 
	tattributes.addAll(t2atts); 
	
	java.util.Map sattcount = new java.util.HashMap(); 
	// sattname |-> Vector  identifies how many target ids this satt has already been used for, so we can 
	// suggest a different id conversion function for each different target from satt. 
	 
    Vector unusedids = new Vector(); 
	
    for (int i = 0; i < tattributes.size(); i++)
	{ Attribute tatt = (Attribute) tattributes.get(i); 
	  Attribute fatt = tatt.getFinalFeature(); 
	  if (ModelMatching.isUnusedTarget(fatt,ems)) 
	  { System.out.println(); 
	    System.out.println(">> Target feature " + fatt + " of " + tatt + " unused in any class mapping");
	  
	    if (tatt.endsWithIdentity())
	    { System.out.println("!! ERROR: Unused target identify feature " + fatt + " --> it must be assigned for transformation to be valid");
		  unusedids.add(tatt); 
		}
	  }
    }
	
	for (int i = 0; i < unusedids.size(); i++) 
	{ Attribute tatt = (Attribute) unusedids.get(i); 
	  Attribute fatt = tatt.getFinalFeature();
	  
	  for (int j = 0; j < sattributes.size(); j++)
      { Attribute satt = (Attribute) sattributes.get(j); 
        String sattname = satt.getName(); 
		  
        Type stype = satt.getType(); 
        if (satt.isIdentity())
	    { if ((stype + "").equals(fatt.getType() + ""))
          { System.out.println(">> Matching source identity attribute: " + satt); 
            Vector usedtargets = (Vector) sattcount.get(sattname); 
            if (usedtargets == null) 
            { usedtargets = new Vector(); 
  		      sattcount.put(sattname, usedtargets); 
			}
		    int cnt = usedtargets.size(); 
			  
		    System.out.println(">> Add feature mapping \"" + cnt + "_\" + " + satt + " |--> " + tatt + " or alternative?"); 
		    String yn = 
                  JOptionPane.showInputDialog("Use this feature mapping or enter your alternative?: " + cnt + "_" + satt + " |--> " + tatt);
 
            if (yn != null)
            { BasicExpression sattbe = new BasicExpression(satt); 
			  sattbe.setUmlKind(Expression.ATTRIBUTE); 
			  BinaryExpression str = new BinaryExpression("+", new BasicExpression("\"" + cnt + "\""), sattbe); 
		  	  str.setType(new Type("String", null)); 
			  str.setElementType(new Type("String", null)); 
			  AttributeMatching amx = new AttributeMatching(str,tatt); 
			  emx.addAttributeMatching(amx); 
		  	  usedtargets.add(tatt);  
            }
		  } 
		  else if (satt.isString() && fatt.isNumeric() && !tatt.isCollection())
		  { // sent->select( sx | sx.satt <= self.satt )->size()  |-->  tatt
		    System.out.println(">> Source identity attribute: " + satt); 
		    Vector usedtargets = (Vector) sattcount.get(sattname); 
		    if (usedtargets == null) 
		    { usedtargets = new Vector(); 
		      sattcount.put(sattname, usedtargets); 
		    }
		    int cnt = usedtargets.size(); 
			  
		    String modifier = ""; 
		    if (cnt == 0)
		    { modifier = ""; } 
		    else if (cnt == 1) 
		    { modifier = "-1*"; }
			  
		    System.out.println(">> Add feature mapping " + modifier + 
			      sent + "->select( sx | sx." + satt + " <= self." + satt + ")->size() |--> " + tatt + " or alternative?"); 
		    String yn = 
                  JOptionPane.showInputDialog("Use this feature mapping or enter your alternative?");
 
            if (yn != null)
            { String sentname = sent.getName();
	 	      String sxname = sentname.toLowerCase() + "x";  
			  BasicExpression sx = new BasicExpression(sxname); 
		  	  sx.setType(new Type(sent)); 
			  BasicExpression sentbe = new BasicExpression(sent); 
				
			  BinaryExpression sxinsent = new BinaryExpression(":", sx, sentbe);
			  BasicExpression sxatt = new BasicExpression(satt); 
			  sxatt.setObjectRef(sx); 
			  sxatt.setType(satt.getType()); 
			  
			  BasicExpression selfbe = new BasicExpression("self"); 
			  selfbe.setType(new Type(sent)); 
			  
			  BasicExpression selfatt = new BasicExpression(satt); 
			  selfatt.setObjectRef(selfbe); 
			  selfatt.setType(satt.getType()); 
			  
			  BinaryExpression leq = new BinaryExpression("<=", sxatt, selfatt); 
			  leq.setType(new Type("boolean", null)); 
			  	 
			  BinaryExpression selexp = new BinaryExpression("|", sxinsent, leq);
			  selexp.setType(new Type("Set", null)); 
			  selexp.setElementType(sentbe.getType()); 
			   
			  UnaryExpression lhs = new UnaryExpression("->size", selexp);
			  Expression xlhs = lhs; 
			  if (cnt > 0)
			  { lhs.setBrackets(true); 
			    xlhs = new BinaryExpression("*", new BasicExpression(-1), lhs); 
			  }
			  xlhs.setType(new Type("int", null)); 
			    
			  AttributeMatching am = new AttributeMatching(xlhs,tatt); 
			  emx.addAttributeMatching(am); 
			  usedtargets.add(tatt); 
            }
		  }
	    }
	  }
	}
  }
  			  
  public Vector identifyConstantAttributeMatchings(Entity ent, EntityMatching emx, Vector pairs)
  { Vector attributes = ent.getLocalFeatures(); 
    // allDefinedAttributes();
    System.out.println(">>> All defined attributes of " + ent + " are " + attributes); 
	 
    int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
	  
    Vector res = new Vector();
	 
    for (int i = 0; i < attributes.size(); i++)
    { Attribute att = (Attribute) attributes.get(i);
      String attname = att.getName(); 
	  

  	  System.out.println(">>> Identifying possible constant assignments K |--> " + attname); 
	   
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
		  BasicExpression constvexp = new BasicExpression(constv); 
		  constvexp.setType(new Type("String", null)); 
		  constvexp.setElementType(new Type("String", null)); 
		   
		  System.out.println(">> Constant feature mapping " + constv + " |--> " + attname); 
		  AttributeMatching amx = new AttributeMatching(constvexp, att); 
		  res.add(amx); 
		  emx.addMapping(amx); 
		}
	  }	
	  else if (att.isCollection())
	  { Vector[] attvalues = new Vector[en]; 
	    boolean alldefined = true; 
	    for (int j = 0; j < en; j++) 
		{ Vector pair = (Vector) pairs.get(j); 
		  ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		  // if (tobj.hasDefinedValue(attname)) { } 
		  // else 
		  // { alldefined = false; } 
		  attvalues[j] = tobj.getCollectionValue(att,this); 
	    }  
		
		if (alldefined && AuxMath.isConstant(attvalues))
		{ String constv = "" + attvalues[0]; 
		  System.out.println(">> Constant feature mapping " + constv + " |--> " + attname); 
		  AttributeMatching amx = new AttributeMatching(new SetExpression(attvalues[0],att.isSequence()), att); 
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
	  else if (att.isEntity())
	  { ObjectSpecification[] attvalues = new ObjectSpecification[en]; 
        boolean alldefined = true; 
				 	 
        for (int j = 0; j < en; j++) 
        { Vector pair = (Vector) pairs.get(j); 
          ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
          // if (tobj.hasDefinedValue(attname)) { } 
          // else 
          // { alldefined = false; } 
		  attvalues[j] = tobj.getReferredObject(attname,this); 
        }
		
		if (alldefined && AuxMath.isConstant(attvalues))
		{ ObjectSpecification constv = attvalues[0]; 
		  System.out.println(">> Constant object mapping " + constv + " |--> " + attname); 
		  AttributeMatching amx = new AttributeMatching(new BasicExpression(constv), att); 
		  res.add(amx); 
		  emx.addMapping(amx); 
		}
	  }   
	}
	return res; 
  } // plus enumerations 

  public Vector identifyCopyAttributeMatchings(Entity sent, Entity tent, EntityMatching emx, Vector pairs, Vector ems, Vector tms)
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
	System.out.println(">>> All attributes of " + sent + " are " + sattributes); 
     
    Vector t1atts = tent.allDefinedProperties();
	Vector t2atts = tent.getNonLocalFeatures(); 
	Vector tattributes = new Vector(); 
	tattributes.addAll(t1atts); 
	tattributes.addAll(t2atts); 
	 
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
      boolean found = false; 
	  
	  for (int k = 0; k < satts.size() && !found; k++) 
	  { Attribute satt = (Attribute) satts.get(k);
	    if (satt.isComposed() && tatt.isComposed()) 
		{ continue; }

	    String sattname = satt.getName();
		  
	    System.out.println(); 
	    System.out.println(">> Checking possible map " + satt + " : " + satt.getType() + 
		                     " |--> " + tatt + " : " + tatt.getType());
        // System.out.println(satt.isCollection() + "  " + tatt.isCollection()); 
  
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
			found = true;  
	  	  }
		  else if (alldefined && AuxMath.linearCorrelation(sattvalues,tattvalues) > 0.98)
		  { double slope = AuxMath.linearSlope(); 
			double offset = AuxMath.linearOffset();
			slope = AuxMath.to3dp(slope); 
			offset = AuxMath.to3dp(offset);  
			System.out.println(">> Apparent linear correspondence " + sattname + "*" + slope + " + " + offset + " |--> " + tattname); 
		    BasicExpression slopebe = new BasicExpression(slope); 
			BasicExpression offsetbe = new BasicExpression(offset); 
			BasicExpression sattbe = new BasicExpression(satt); 
			BinaryExpression mbe = new BinaryExpression("*", sattbe, slopebe);
			BinaryExpression lhsbe = new BinaryExpression("+", mbe, offsetbe);
			lhsbe.setType(new Type("double", null));   
			AttributeMatching amx = new AttributeMatching(lhsbe, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx);
			found = true;  
	  	  }
		  else if (alldefined && satt.isInteger() && tatt.isInteger() && AuxMath.isFunctional(sattvalues,tattvalues))
		  { int possibleK = AuxMath.modFunction(sattvalues,tattvalues); 
		    if (possibleK != 1)
			{ System.out.println(">> " + sattname + " mod " + possibleK + " |--> " + tattname); 
			  BasicExpression slopebe = new BasicExpression(possibleK); 
			  BasicExpression sattbe = new BasicExpression(satt); 
			  sattbe.setUmlKind(Expression.ATTRIBUTE); 
			  BinaryExpression mbe = new BinaryExpression("mod", sattbe, slopebe);
			  mbe.setType(new Type("int", null));   
			  AttributeMatching amx = new AttributeMatching(mbe, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx);
			  found = true; 
			}  
			else 
			{ possibleK = AuxMath.divFunction(sattvalues,tattvalues); 
			  if (possibleK != 0)
			  { System.out.println(">> " + sattname + " div " + possibleK + " |--> " + tattname); 
			    BasicExpression slopebe = new BasicExpression(possibleK); 
			    BasicExpression sattbe = new BasicExpression(satt); 
				sattbe.setUmlKind(Expression.ATTRIBUTE);
			    BinaryExpression mbe = new BinaryExpression("/", sattbe, slopebe);
			    mbe.setType(new Type("int", null));   
			    AttributeMatching amx = new AttributeMatching(mbe, tatt); 
		        res.add(amx); 
		        emx.addMapping(amx);
			    found = true;
			  }  
			}
		  }
	    }
	    else if (satt.isString() && tatt.isString())
	    { String[] sattvalues = new String[en]; 
	      String[] tattvalues = new String[en];
		  boolean alldefined = true; 
		
          System.out.println(">> Checking string-to-string feature mapping " + satt + " |--> " + tatt); 

  		  for (int j = 0; j < en; j++) 
		  { Vector pair = (Vector) pairs.get(j); 
		    ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
		    ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
		    else 
		    { alldefined = false; } 
		    sattvalues[j] = sobj.getString(sattname); // sobj.getStringValue(satt,this); 
		    tattvalues[j] = tobj.getString(tattname); 
  		  }
		
	  	  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
		  { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx); 
			found = true; 
		  }
	  	  else if (alldefined && AuxMath.isSuffixed(sattvalues,tattvalues))
		  { String suff = AuxMath.commonSuffix(sattvalues,tattvalues); 
		    if (suff != null)
			{ System.out.println(">> Suffix feature mapping " + sattname + " + \"" + suff + "\" |--> " + tattname); 
		      BasicExpression sattbe = new BasicExpression(satt); 
			  sattbe.setUmlKind(Expression.ATTRIBUTE);
			  BinaryExpression addsuff = new BinaryExpression("+", sattbe, 
			                                                  new BasicExpression("\"" + suff + "\"")); 
			  AttributeMatching amx = new AttributeMatching(addsuff, tatt); 
		      addsuff.setType(new Type("String", null));  
			  addsuff.setElementType(new Type("String", null));  
			  res.add(amx); 
		      emx.addMapping(amx);
			  found = true;  
			} 
		  }
	  	  else if (alldefined && AuxMath.isPrefixed(sattvalues,tattvalues))
		  { String pref = AuxMath.commonPrefix(sattvalues,tattvalues); 
		    if (pref != null)
            { System.out.println(">> Prefix feature mapping \"" + pref + "\" + " + sattname + " |--> " + tattname); 
		      BasicExpression sattbe = new BasicExpression(satt); 
			  sattbe.setUmlKind(Expression.ATTRIBUTE);
			  BinaryExpression addsuff = new BinaryExpression("+",  
			                                                  new BasicExpression("\"" + pref + "\""),
															  sattbe);
			  addsuff.setType(new Type("String", null));  
			  addsuff.setElementType(new Type("String", null));  
			  AttributeMatching amx = new AttributeMatching(addsuff, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
			  found = true;  
			} 
		  }
	  	  else if (alldefined && AuxMath.isSuffixed(tattvalues,sattvalues))
		  { String suff = AuxMath.commonSuffix(tattvalues,sattvalues); 
		    if (suff != null && suff.length() > 0)
			{ System.out.println(">> feature mapping " + sattname + "->before(\"" + suff + "\") |--> " + tattname); 
		      BasicExpression sattbe = new BasicExpression(satt); 
			  sattbe.setUmlKind(Expression.ATTRIBUTE);
			  BinaryExpression removesuff = new BinaryExpression("->before", sattbe, 
			                                                  new BasicExpression("\"" + suff + "\"")); 
			  removesuff.setType(new Type("String", null));  
			  removesuff.setElementType(new Type("String", null));  
			  AttributeMatching amx = new AttributeMatching(removesuff, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx);
			  found = true;  
			}
			else 
			{ Vector suffs = AuxMath.allSuffixes(tattvalues,sattvalues); 
			  System.out.println("all suffixes = " + suffs); 
			  String cpref = AuxMath.longestCommonPrefix(suffs); 
			  System.out.println("Longest common prefix = " + cpref); 
			  if (cpref != null && cpref.length() > 0)
			  { System.out.println(">> feature mapping " + sattname + "->before(\"" + cpref + "\") |--> " + tattname); 
		        BasicExpression sattbe = new BasicExpression(satt); 
				sattbe.setUmlKind(Expression.ATTRIBUTE);
				BinaryExpression removesuff = new BinaryExpression("->before", sattbe, 
			                                                  new BasicExpression("\"" + cpref + "\"")); 
			    removesuff.setType(new Type("String", null));  
			    removesuff.setElementType(new Type("String", null));  
			    AttributeMatching amx = new AttributeMatching(removesuff, tatt); 
		        res.add(amx); 
		        emx.addMapping(amx);
			    found = true;  
			  } 
			} 
		  }
	  	  else if (alldefined && AuxMath.isPrefixed(tattvalues,sattvalues))
		  { String pref = AuxMath.commonPrefix(tattvalues,sattvalues); 
		    if (pref != null && pref.length() > 0)
			{ System.out.println(">> feature mapping " + sattname + "->after(\"" + pref + "\") |--> " + tattname); 
			  BasicExpression sattbe = new BasicExpression(satt); 
			  sattbe.setUmlKind(Expression.ATTRIBUTE);
		      BinaryExpression removesuff = new BinaryExpression("->after", sattbe, 
			                                                  new BasicExpression("\"" + pref + "\"")); 
			  removesuff.setType(new Type("String", null));  
			  removesuff.setElementType(new Type("String", null));  
			  AttributeMatching amx = new AttributeMatching(removesuff, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx);
			  found = true;  
			} 
            else 
			{ Vector prefs = AuxMath.allPrefixes(tattvalues,sattvalues); 
			  System.out.println("all prefixes = " + prefs); 
			  String csuff = AuxMath.longestCommonSuffix(prefs); 
			  System.out.println("Longest common suffix = " + csuff); 
			  if (csuff != null && csuff.length() > 0)
			  { System.out.println(">> feature mapping " + sattname + "->after(\"" + csuff + "\") |--> " + tattname);
			    BasicExpression sattbe = new BasicExpression(satt); 
			    sattbe.setUmlKind(Expression.ATTRIBUTE);
		       
		        BinaryExpression removepref = new BinaryExpression("->after", sattbe, 
			                                                  new BasicExpression("\"" + csuff + "\"")); 
			    removepref.setType(new Type("String", null));  
			    removepref.setElementType(new Type("String", null));  
			    AttributeMatching amx = new AttributeMatching(removepref, tatt); 
		        res.add(amx); 
		        emx.addMapping(amx);
			    found = true;  
			  } 
			} 

		  }
		  // Or - identify a specific string-to-string mapping. Eg., "Real" |-> "double", etc. 
		  else if (alldefined && AuxMath.isFunctional(sattvalues,tattvalues))
		  { System.out.println(">>>> Create a specific string-to-string function for these features?"); 
            for (int mk = 0; mk < en; mk++) 
            { System.out.println("  " + sattvalues[mk] + " |--> " + tattvalues[mk]); }
            String fname = "f" + sent + satt.underscoreName() + "2" + tent + tatt.underscoreName(); 
			String yn = 
                   JOptionPane.showInputDialog("Create a String-to-String function " + fname + "? (y/n):");

            if (yn != null && "y".equals(yn))
            { TypeMatching newfunction = new TypeMatching(satt.getType(), tatt.getType()); 
			  newfunction.setName(fname); 
              newfunction.setStringValues(sattvalues,tattvalues);
              System.out.println(">>> New function: " + newfunction);
			  BasicExpression fsatt = new BasicExpression(fname); 
			  BasicExpression sattbe = new BasicExpression(satt); 
			  sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
			  fsatt.addParameter(sattbe);   
      	      AttributeMatching amx = new AttributeMatching(fsatt, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
			  tms.add(newfunction);
			  found = true;   
		    }  
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
			found = true;  
		  }
		  else if (alldefined && AuxMath.isNegation(sattvalues,tattvalues))
 		  { System.out.println(">> Copy feature mapping not(" + sattname + ") |--> " + tattname);
		    BasicExpression sattbe = new BasicExpression(satt); 
	        sattbe.setUmlKind(Expression.ATTRIBUTE);
		       
		    UnaryExpression notatt = new UnaryExpression("not", sattbe);
			notatt.setType(new Type("boolean", null));  
			AttributeMatching amx = new AttributeMatching(notatt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx);
			found = true;  
		  }
		} 
	    else if (satt.isEntity() && tatt.isEntity())
	    { ObjectSpecification[] sattvalues = new ObjectSpecification[en]; 
          ObjectSpecification[] tattvalues = new ObjectSpecification[en];
  	      boolean alldefined = true; 

          System.out.println(">> Checking entity feature mapping " + satt + " |--> " + tatt); 
						 	 
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
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
			found = true;  
		  }
		} 
	    else if (satt.isCollection() && tatt.isCollection())
	    { Vector[] sattvalues = new Vector[en]; 
          Vector[] tattvalues = new Vector[en];
  	      boolean alldefined = true; 
	
          System.out.println(">> Checking collection feature mapping " + satt + " |--> " + tatt); 
							 	 
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
             ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
             ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    // if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
            // else 
		    // { alldefined = false; } 
             sattvalues[j] = sobj.getCollectionValue(satt,this); 
             tattvalues[j] = tobj.getCollectionValue(tatt,this);
			
             System.out.println(sobj + "." + satt + " = " + sattvalues[j]); 
             System.out.println(tobj + "." + tatt + " = " + tattvalues[j]); 
           }
		
           if (alldefined && AuxMath.isCopy(sattvalues,tattvalues,this))
           { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
             AttributeMatching amx = new AttributeMatching(satt, tatt); 
             res.add(amx); 
             emx.addMapping(amx);
             found = true;  
           }
           else if (alldefined && AuxMath.isSupsetSet(sattvalues,tattvalues,this))
           { if (satt.getElementType() != null && satt.getElementType().isEntity())
             { Entity srcref = satt.getElementType().getEntity(); 
               Vector discriminators = srcref.getDiscriminatorAttributes();
               if (srcref.isAbstract())
               { Attribute selfatt = new Attribute("self", new Type(srcref), ModelElement.INTERNAL); 
                 selfatt.setType(new Type(srcref)); 
                 selfatt.setElementType(new Type(srcref)); 
                 discriminators.add(selfatt); 
               }
               System.out.println(); 
               System.out.println(">>> Possible discriminator attributes of " + satt + " : " + srcref + " are: " + discriminators); 

                Vector selectionConds = validateDiscriminatorConditions(discriminators,sattvalues,tattvalues,satt); 
                if (selectionConds.size() > 0 && selectionConds.get(0) != null)
                { System.out.println(">>> Possible alternative source expressions are: " + selectionConds);
			   
                  System.out.println(">> Selection feature mapping: " + selectionConds.get(0) + " |--> " + tatt);
                  Expression selection = (Expression) selectionConds.get(0); 
                  selection.setMultiplicity(ModelElement.MANY); 
                  Vector auxvariables = new Vector(); 
                  Attribute var = new Attribute(Identifier.nextIdentifier("var$"),satt.getElementType(),
                                  ModelElement.INTERNAL);
                  var.setElementType(satt.getElementType());
                  auxvariables.add(var);   
                  AttributeMatching amx = new AttributeMatching(selection, tatt, var, auxvariables); 
                  res.add(amx); 
                  emx.addMapping(amx);
                  found = true;  
                } 
              }
              System.out.println(">> Possible selection feature mapping " + sattname + "->selection(condition) |--> " + tattname); 
            }
            else if (alldefined && AuxMath.isSubsetSet(sattvalues,tattvalues,this))
            { System.out.println(">> Possible union feature mapping " + sattname + "->union(something) |--> " + tattname); 
		    // AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    // res.add(amx); 
		    // emx.addMapping(amx); 
            }
          } 
	     else if (satt.isEntity() && tatt.isCollection())
          { Vector[] sattvalues = new Vector[en]; 
            Vector[] tattvalues = new Vector[en];
  	       boolean alldefined = true; 
	
	      System.out.println(">> Checking entity-to-collection feature mapping " + satt + " |--> " + tatt); 

							 	 
	      for (int j = 0; j < en; j++) 
		  { Vector pair = (Vector) pairs.get(j); 
		    ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
		    ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    // if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
            // else 
		    // { alldefined = false; } 
			ObjectSpecification xobj = sobj.getReferredObject(sattname,this);
			// Object xobj = sobj.getValueOf(satt,this); 
		    sattvalues[j] = new Vector(); 
			sattvalues[j].add(xobj);  
	        tattvalues[j] = tobj.getCollection(tattname);
			System.out.println(">> Checking inclusion feature mapping " + sattvalues[j] + " |--> " + tattvalues[j]); 
		     
		  }
		
		  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues,this))
 		  { System.out.println(">> Inclusion feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx);
			found = true;  
		  }
		} 
	  }   
	}
	System.out.println(); 
	
	return res; 
  }

  public Vector identifyManyToOneAttributeMatchings(Entity sent, Entity tent, EntityMatching emx, Vector pairs, Vector ems)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify Collection to 1 feature mappings for " + sent + " |--> " + tent); 
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
	  boolean found = false; 
	   
      Vector satts = ModelMatching.findBaseTypeCompatibleSourceAttributes(tatt,sattributes,emx,ems); 

      for (int k = 0; k < satts.size() && !found; k++) 
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
            if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
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
			found = true;  // no need to look for another satt for tatt 
          }
        }   
        else if (satt.isCollection() && tatt.isNumeric())
        { Vector[] sattvalues = new Vector[en]; 
          Vector[] tattvalues = new Vector[en]; 
          boolean alldefined = true; 
		
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = sobj.getCollectionValue(satt,this);
            tattvalues[j] = new Vector();
            double dd = tobj.getNumeric(tattname); 
            tattvalues[j].add(new Double(dd));  
          }
		
          if (alldefined && AuxMath.isNumericSum(sattvalues,tattvalues))
          { System.out.println(">> Sum numeric mapping " + sattname + "->sum() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->sum", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx); 
			found = true; 
          }
          else if (alldefined && AuxMath.isNumericPrd(sattvalues,tattvalues))
          { System.out.println(">> Product numeric mapping " + sattname + "->prd() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->prd", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx);
			found = true;  
          }
          else if (alldefined && AuxMath.isNumericMin(sattvalues,tattvalues))
          { System.out.println(">> Min numeric mapping " + sattname + "->min() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->min", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx);
			found = true;  
          }
          else if (alldefined && AuxMath.isNumericMax(sattvalues,tattvalues))
          { System.out.println(">> Max numeric mapping " + sattname + "->max() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->max", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx);
			found = true;  
          }
          else if (alldefined && AuxMath.isNumericAverage(sattvalues,tattvalues))
          { System.out.println(">> Average numeric mapping " + sattname + "->average() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->average", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx);
			found = true;  
          }
        }   
        else if (satt.isCollection() && tatt.isString())
        { Vector[] sattvalues = new Vector[en]; 
          Vector[] tattvalues = new Vector[en]; 
          boolean alldefined = true; 
		
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = sobj.getCollectionValue(satt,this);
            tattvalues[j] = new Vector();
            String dd = tobj.getString(tattname); 
            tattvalues[j].add(dd);  
          }
		
          if (alldefined && AuxMath.isStringSum(sattvalues,tattvalues))
          { System.out.println(">> String sum feature mapping " + sattname + "->sum() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->sum", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx); 
			found = true; 
          }
          else if (alldefined && AuxMath.isStringMax(sattvalues,tattvalues))
          { System.out.println(">> String max feature mapping " + sattname + "->max() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->max", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx);
			found = true;  
          }
          else if (alldefined && AuxMath.isStringMin(sattvalues,tattvalues))
          { System.out.println(">> String min feature mapping " + sattname + "->min() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(new UnaryExpression("->min", new BasicExpression(satt)), tatt); 
            res.add(amx); 
            emx.addMapping(amx); 
			found = true; 
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
            if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = sobj.getCollectionValue(satt,this); 
	        tattvalues[j] = tobj.getCollectionValue(tatt,this); 
          }
		
		  if (alldefined && AuxMath.isCopy(sattvalues,tattvalues,this))
 		  { System.out.println(">> Copy collection feature mapping " + sattname + " |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(satt, tatt); 
		    res.add(amx); 
		    emx.addMapping(amx);
			found = true;  
          }
        } // ->flatten() and ->reverse() cases. 
        else if (satt.isCollection() && tatt.isEntity())
        { Vector[] sattvalues = new Vector[en]; 
          Vector[] tattvalues = new Vector[en];
          boolean alldefined = true; 
	
							 	 
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
		    else 
		    { alldefined = false; } 
            sattvalues[j] = sobj.getCollectionValue(satt,this); 
	        // System.out.println(">>> (" + sobj + ")." + satt + " = " + sattvalues[j]); 
            tattvalues[j] = new Vector(); 
	
            tattvalues[j].add(tobj.getReferredObject(tattname,this)); 
             // System.out.println(">>> (" + tobj + ")." + tattname + " = " + tattvalues[j]); 
          }
		
          if (alldefined && AuxMath.isCopy(sattvalues,tattvalues,this))
          { System.out.println(">> Collection-to-object feature mapping " + sattname + "->any() |--> " + tattname); 
            UnaryExpression anysatt = new UnaryExpression("->any", new BasicExpression(satt)); 
            AttributeMatching amx = new AttributeMatching(anysatt, tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
        } 
      }   
	}
	System.out.println(); 
	
	return res; 
  }

  public Vector identifyTwoToOneAttributeMatchings(Entity sent, Entity tent, EntityMatching emx, Vector pairs, Vector ems)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify 2 source feature to 1 target feature mappings for " + sent + " |--> " + tent); 
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
	   
      Vector satts = ModelMatching.findBaseTypeCompatibleSourceAttributes(tatt,sattributes,emx,ems); 
      boolean found = false; 
	  
      for (int k = 0; k < satts.size(); k++) 
      { Attribute satt = (Attribute) satts.get(k);
        String sattname = satt.getName(); 

        for (int p = k+1; p < satts.size(); p++)
        { Attribute satt1 = (Attribute) satts.get(p);  
          String satt1name = satt1.getName(); 
	 
          System.out.println(">> Checking possible map " + satt + " : " + satt.getType() + " ->op " + satt1 + " : " + 
		                     satt1.getType() + 
		                     " |--> " + tatt + " : " + tatt.getType());
	      if (satt.isNumeric() && satt1.isNumeric() && tatt.isNumeric())
	      { double[] sattvalues = new double[en];
		    double[] satt1values = new double[en];
		    double[] tattvalues = new double[en];
		    boolean alldefined = true; 
		
            for (int j = 0; j < en; j++) 
            { Vector pair = (Vector) pairs.get(j); 
              ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
              ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
              if (sobj.hasDefinedValue(satt,this) && sobj.hasDefinedValue(satt1,this) && tobj.hasDefinedValue(tattname)) { } 
              else 
              { alldefined = false; } 
              sattvalues[j] = sobj.getNumeric(sattname);
              satt1values[j] = sobj.getNumeric(satt1name);
              tattvalues[j] = tobj.getNumeric(tattname); 
            }
		  
		    // check satt + satt1 = tatt, etc
	  	    if (alldefined && AuxMath.isNumericSum(sattvalues,satt1values,tattvalues))
			{ BinaryExpression add1st = new BinaryExpression("+", new BasicExpression(satt), 
			                                                 new BasicExpression(satt1)); 
			  AttributeMatching amx = new AttributeMatching(add1st, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx); 
              found = true;
			}
	  	    else if (alldefined && AuxMath.isNumericProduct(sattvalues,satt1values,tattvalues))
			{ BinaryExpression add1st = new BinaryExpression("*", new BasicExpression(satt), 
			                                                 new BasicExpression(satt1)); 
			  AttributeMatching amx = new AttributeMatching(add1st, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx); 
              found = true;
			}
	  	    else if (alldefined && AuxMath.isNumericSubtraction(sattvalues,satt1values,tattvalues))
			{ BinaryExpression add1st = new BinaryExpression("-", new BasicExpression(satt), 
			                                                 new BasicExpression(satt1)); 
			  AttributeMatching amx = new AttributeMatching(add1st, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx); 
              found = true;
			}
	  	    else if (alldefined && AuxMath.isNumericDivision(sattvalues,satt1values,tattvalues))
			{ BinaryExpression add1st = new BinaryExpression("/", new BasicExpression(satt), 
			                                                 new BasicExpression(satt1)); 
			  AttributeMatching amx = new AttributeMatching(add1st, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx); 
              found = true;
			}
		  } 
	      else if (satt.isString() && satt1.isString() && tatt.isString())
	      { String[] sattvalues = new String[en]; 
            String[] satt1values = new String[en]; 
	        String[] tattvalues = new String[en];
		  
            boolean alldefined = true; 
		
            System.out.println(">> Checking string combination-to-string feature mappings " + satt + " ->op " + satt1 + " |--> " + tatt); 
            System.out.println(">> and " + satt1 + " ->op " + satt + " |--> " + tatt); 

  		    for (int j = 0; j < en; j++) 
		    { Vector pair = (Vector) pairs.get(j); 
              ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
              ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
              if (sobj.hasDefinedValue(sattname) && sobj.hasDefinedValue(satt1name) && tobj.hasDefinedValue(tattname)) { } 
		      else 
  		      { alldefined = false; } 
		      sattvalues[j] = sobj.getString(sattname); 
		      satt1values[j] = sobj.getString(satt1name); 
		      tattvalues[j] = tobj.getString(tattname); 
  		    }
		
	  	    if (alldefined && AuxMath.isConcatenation(sattvalues,satt1values,tattvalues))
		    { String infix = AuxMath.commonInfix(sattvalues,satt1values,tattvalues); 
		      if (infix != null)
              { System.out.println(">> Composition feature mapping " + sattname + " + \"" + infix + "\" + " + satt1name + " |--> " + tattname); 
			    BasicExpression sattbe = new BasicExpression(satt); 
				sattbe.setUmlKind(Expression.ATTRIBUTE); 
				BasicExpression satt1be = new BasicExpression(satt1); 
				satt1be.setUmlKind(Expression.ATTRIBUTE); 
		        BinaryExpression add1st = new BinaryExpression("+", sattbe, 
			                                                  new BasicExpression("\"" + infix + "\"")); 
			    BinaryExpression add2nd = new BinaryExpression("+", add1st, satt1be);
				add2nd.setType(new Type("String", null));  
			    add2nd.setElementType(new Type("String", null));  
			    AttributeMatching amx = new AttributeMatching(add2nd, tatt); 
		        res.add(amx); 
		        emx.addMapping(amx); 
				found = true; 
			  }
			}  
	  	    else if (alldefined && AuxMath.isConcatenation(satt1values,sattvalues,tattvalues))
		    { String infix = AuxMath.commonInfix(satt1values,sattvalues,tattvalues); 
		      if (infix != null)
			  { System.out.println(">> Composition feature mapping " + satt1name + " + \"" + infix + "\" + " + sattname + " |--> " + tattname); 
		        BasicExpression sattbe = new BasicExpression(satt); 
				sattbe.setUmlKind(Expression.ATTRIBUTE); 
				BasicExpression satt1be = new BasicExpression(satt1); 
				satt1be.setUmlKind(Expression.ATTRIBUTE); 
				BinaryExpression add1st = new BinaryExpression("+", satt1be, 
			                                                  new BasicExpression("\"" + infix + "\"")); 
			    BinaryExpression add2nd = new BinaryExpression("+", add1st, sattbe); 
			    add2nd.setType(new Type("String", null));  
			    add2nd.setElementType(new Type("String", null));  
			    AttributeMatching amx = new AttributeMatching(add2nd, tatt); 
		        res.add(amx); 
		        emx.addMapping(amx);
				found = true;  
              }
            }  
		  }
		  else if (satt.isCollection() && satt1.isCollection() && tatt.isCollection())
	      { // could be satt->union(satt1)  |--> tatt
             Vector[] sattvalues = new Vector[en]; 
             Vector[] satt1values = new Vector[en]; 
             Vector[] tattvalues = new Vector[en];
             boolean alldefined = true; 
	
							 	 
             for (int j = 0; j < en; j++) 
             { Vector pair = (Vector) pairs.get(j); 
               ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
               ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
               if (sobj.hasDefinedValue(satt,this) && sobj.hasDefinedValue(satt1,this) && tobj.hasDefinedValue(tattname)) { } 
               else 
               { alldefined = false; } 
               sattvalues[j] = sobj.getCollectionValue(satt,this);
               satt1values[j] = sobj.getCollectionValue(satt1,this); 
	           tattvalues[j] = tobj.getCollectionValue(tatt,this); 
             }

             BasicExpression sattbe = new BasicExpression(satt); 
             sattbe.setUmlKind(Expression.ATTRIBUTE); 
             BasicExpression satt1be = new BasicExpression(satt1); 
             satt1be.setUmlKind(Expression.ATTRIBUTE); 
			   
		     if (AuxMath.isUnion(sattvalues,satt1values,tattvalues,this))
             { System.out.println(">> Union feature mapping " + sattname + "->union(" + satt1name + ") |--> " + tattname); 
		       BinaryExpression add1st = new BinaryExpression("->union", sattbe, satt1be); 
			   AttributeMatching amx = new AttributeMatching(add1st, tatt); 
		       res.add(amx); 
               emx.addMapping(amx); 
			   found = true; 
		     }
			 else if (AuxMath.isUnion(satt1values,sattvalues,tattvalues,this))
             { System.out.println(">> Union feature mapping " + satt1name + "->union(" + sattname + ") |--> " + tattname); 
		       BinaryExpression add1st = new BinaryExpression("->union", satt1be, sattbe); 
			   AttributeMatching amx = new AttributeMatching(add1st, tatt); 
		       res.add(amx); 
               emx.addMapping(amx); 
			   found = true; 
		     } 
           } 
         }  
       } 
	}
	return res; 
  } 
}
