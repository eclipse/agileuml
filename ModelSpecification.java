import java.util.*; 
import java.io.*; 
import javax.swing.JOptionPane; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
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

  public static int maxSourcePath = 1; 
  public static int maxTargetPath = 1; 
  // The maximum feature composition lengths considered 
  // for feature mappings. 

  public Vector getObjectsOf(String ename)
  { Vector res = (Vector) objectsOfClass.get(ename); 
    if (res == null) 
    { return new Vector(); } 
    return res; 
  } 


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
  { String res = ""; 
    for (int i = 0; i < objects.size(); i++) 
    { ObjectSpecification spec = (ObjectSpecification) objects.get(i); 
      res = res + spec.details() + "\n"; 
    } 
	
    for (int j = 0; j < correspondence.elements.size(); j++)
    { Maplet mm = (Maplet) correspondence.elements.get(j); 
      ObjectSpecification sobj = (ObjectSpecification) mm.source; 
      ObjectSpecification tobj = (ObjectSpecification) mm.dest; 
      res = res + sobj.getName() + " |-> " + tobj.getName() + "\n"; 
    } 

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

  public void defineComposedFeatureValues(int slen, int tlen, Vector entities, Vector types)
  { // For 2-step source features f.g define the value of
    // the feature in each source object

    maxSourcePath = slen; 
    maxTargetPath = tlen; 

    for (int i = 0; i < entities.size(); i++) 
    { Entity sent = (Entity) entities.get(i); 
      if (sent != null) // && sent.isSource()) 
      { if (sent.isSource())
        { sent.defineExtendedNonLocalFeatures(slen); } 
        else 
        { sent.defineExtendedNonLocalFeatures(tlen); } 
         
        Vector allFeatures = sent.getNonLocalFeatures();
        String sentname = sent.getName(); 

        System.out.println(">> Non-local features of " + sentname + " are: " + allFeatures); 
        
        Vector sobjs = (Vector) objectsOfClass.get(sentname); 
        if (sobjs == null)
        { sobjs = new Vector(); }
		
        for (int j = 0; j < sobjs.size(); j++) 
        { ObjectSpecification obj = (ObjectSpecification) sobjs.get(j); 
          for (int k = 0; k < allFeatures.size(); k++) 
          { Attribute attk = (Attribute) allFeatures.get(k); 
            Object val = obj.getActualValueOf(attk,this); 
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

  public boolean correspondingTrees(ASTTerm s, ASTTerm t) 
  { // s, t are the asts of corresponding objects

    if (s == null) 
    { return false; } 
    if (t == null) 
    { return false; } 

    for (int i = 0; i < correspondence.elements.size(); i++) 
    { Maplet mm = (Maplet) correspondence.elements.get(i); 
      ObjectSpecification src = (ObjectSpecification) mm.source; 
      ObjectSpecification trg = (ObjectSpecification) mm.dest; 
      if (src.hasAttribute("ast") && trg.hasAttribute("ast"))
      { ASTTerm stree = src.getTree("ast");
        ASTTerm ttree = trg.getTree("ast");  

        if (stree == null) 
        { System.err.println("!WARNING -- invalid AST for " + src); 
          return false; 
        } 

        if (ttree == null) 
        { System.err.println("!WARNING -- invalid AST for " + trg); 
          return false; 
        } 

        if (s.equals(stree) && t.equals(ttree))
        { return true; } 

        if (s.equals(stree) && t.arity() == 1 && 
            ttree.arity() == 1)
        { ASTTerm telem = ttree.getTerm(0);
          // System.out.println(">>> Comparing " + t + " and " + telem); 
          // System.out.println(); 
 
          if (t.equals(telem))
          { return true; } 
        } 
      } 
    } 
    return false; 
  } 

  public boolean correspondingTrees(Entity sent, ASTTerm s, ASTTerm t) 
  { if (s == null) 
    { return false; } 
    if (t == null) 
    { return false; } 

    for (int i = 0; i < correspondence.elements.size(); i++) 
    { Maplet mm = (Maplet) correspondence.elements.get(i); 
      ObjectSpecification src = (ObjectSpecification) mm.source; 

      // System.out.println(">>> Object " + src + " " + src.entity + " " + src.objectClass); 

      /* These tests should be inverted: */ 
      if (src.entity != null && src.entity.equals(sent)) 
      { continue; } 
      if (sent.getName().equals(src.objectClass)) 
      { continue; } 

      ObjectSpecification trg = (ObjectSpecification) mm.dest; 
      if (src.hasAttribute("ast") && trg.hasAttribute("ast"))
      { ASTTerm stree = src.getTree("ast");
        ASTTerm ttree = trg.getTree("ast");  

        if (stree == null) 
        { System.err.println("!WARNING -- invalid AST for " + src); 
          return false; 
        } 

        if (ttree == null) 
        { System.err.println("!WARNING -- invalid AST for " + trg); 
          return false; 
        } 

        if (s.equals(stree) && t.equals(ttree))
        { return true; } 

        if (s.equals(stree) && t.arity() == 1 && 
            ttree.arity() == 1)
        { ASTTerm telem = ttree.getTerm(0);
          // System.out.println(">>> Comparing " + t + " and " + telem); 
          // System.out.println(); 
 
          if (t.equals(telem))
          { return true; } 
        } 
      } 
    } 
    return false; 
  } 

  public boolean correspondingTrees(ASTTerm[] sarr, ASTTerm[] tarr) 
  { if (sarr.length < 2 || tarr.length < 2) 
    { return false; } 

    for (int i = 0; i < sarr.length && i < tarr.length; i++) 
    { if (correspondingTrees(sarr[i], tarr[i])) { } 
      else 
      { return false; }  
    } 
    return true; 
  } 

  public boolean correspondingTrees(Entity sent, ASTTerm[] sarr, ASTTerm[] tarr) 
  { if (sarr.length < 2 || tarr.length < 2) 
    { return false; } 

    for (int i = 0; i < sarr.length && i < tarr.length; i++) 
    { if (correspondingTrees(sent, sarr[i], tarr[i])) { } 
      else 
      { return false; }  
    } 
    return true; 
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

  public Vector getCorrespondingObjects(ObjectSpecification sobj, Entity targ)
  { Vector res = new Vector(); 
  
    Vector objs = correspondence.getAll(sobj); 
    for (int i = 0; i < objs.size(); i++) 
    { ObjectSpecification tobj = (ObjectSpecification) objs.get(i); 
      if (tobj != null && 
          tobj.entity != null && 
          Entity.isDescendantOrEqualTo(tobj.entity,targ)) 
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

            if (satt.isSequence() && AuxMath.isConstantSequences(xstrs))
            { System.out.println(">>> " + satt + " is constant on the merged sources."); } 
            else if (satt.isSet() && AuxMath.isConstantSets(xstrs))
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
		
          // System.out.println(sobjs + "." + satt + " = " + sattvalues); 
          // System.out.println(tobj + "." + tatt + " = " + tattvalues); 

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
  
  public java.util.HashMap objectSplittings(String sename, Vector replicatedSobjects, Vector replicationTargets)
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
          { Vector replicatedTobjs = (Vector) tobjectsets.get(key); 
		    res.put(sobj, replicatedTobjs);  // only one set for each sobj, ie., assuming only one tent
			for (int z = 0; z < replicatedTobjs.size(); z++) 
			{ ObjectSpecification rtobj = (ObjectSpecification) replicatedTobjs.get(z); 
			  replicatedSobjects.add(sobj); 
			  replicationTargets.add(rtobj); 
			}
		  } 
        }  
        // Only counts those where at least 2 target objects in type key. 
        // Assumes there is only one target class for which 
        // there is replication. And no condition, restricting replication even if there are available source values.  
	  }
    } 
	
	System.out.println(">>> All sources: " + replicatedSobjects); 
	System.out.println(">>> All targets: " + replicationTargets); 
	
    return res; 
  } 

  public void checkSplittingCondition(Entity sent, Vector entities, Vector ems, java.util.HashMap mergings)
  { // for each sobj |-> {tent1 |-> tobjs1, ..., tentn |-> tobjsn}
    // in mergings, and each tenti, check for which target features tatt the tobjsi all have different values
	// Create a potential instance replication rule 
	// { v : satt } sent |--> tenti 
    //    v |--> tatt
	// if there is a source satt (or expression) mapping to tatt
	// so that elements of satt correspond to the different tatt values in the tobjsi.  

    Vector identificationProperties = new Vector(); 
    java.util.HashMap valuemap = new java.util.HashMap(); 
      // Map from target atts to (sobj -> tobj tatt values)
	
    Vector keys = new Vector(); 
    keys.addAll(mergings.keySet()); 
	
    Entity tent = null;
    java.util.Map perclassIdentificationProperties = new java.util.HashMap(); 
	// for each target classname tenti, the potential identification properties tatt
    // of the target class.  
    
	java.util.Map perTattSobjs = new java.util.HashMap(); 
	java.util.Map perTattTobjs = new java.util.HashMap(); 
	
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
		
		  Vector sourceObjectsForTatt = new Vector(); 
		  Vector targetObjectsForTatt = new Vector(); 
		  
          if (tatt.isString())
          { String[] xstrs = new String[tobjs.size()];
		  
            Vector localSourceObjectsForTatt = new Vector(); 
            Vector localTargetObjectsForTatt = new Vector(); 
		              
            for (int k = 0; k < tobjs.size(); k++)
            { ObjectSpecification trgobj = (ObjectSpecification) tobjs.get(k); 
              String trgstr = trgobj.getStringValue(tatt,this);
              // System.out.println(">** (" + trgobj + ")." + tatt + " = " + trgstr);  
              xstrs[k] = trgstr;
              localSourceObjectsForTatt.add(key);
              localTargetObjectsForTatt.add(trgobj);  
            } 
			
            if (AuxMath.allDifferent(xstrs))
            { System.out.println(">>> feature " + tatt + " has distinct values for different replicated target instances.");
              if (identificationProperties.contains(tatt)) { } 
              else 
              { identificationProperties.add(tatt); }
                sourceObjectsForTatt.addAll(localSourceObjectsForTatt); 
              targetObjectsForTatt.addAll(localTargetObjectsForTatt); 
              perTattSobjs.put(tatt,sourceObjectsForTatt); 		    
              perTattTobjs.put(tatt,targetObjectsForTatt);

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

              Vector localSourceObjectsForTatt = new Vector(); 
              Vector localTargetObjectsForTatt = new Vector(); 
			
              for (int k = 0; k < tobjs.size(); k++)
              { ObjectSpecification trgobj = (ObjectSpecification) tobjs.get(k); 
                ObjectSpecification trgref = trgobj.getReferredObject(tatt,this);
                xstrs[k] = trgref; 
                xvector.add(trgref); 
                localSourceObjectsForTatt.add(key);
                localTargetObjectsForTatt.add(trgobj);
              } 
			
              if (AuxMath.allDifferent(xstrs))
              { System.out.println(">>> feature " + tatt + " has distinct values for different replicated target instances.");
                if (identificationProperties.contains(tatt)) { } 
                else 
                { identificationProperties.add(tatt); }
                String tattname = tatt.getName(); 
				sourceObjectsForTatt.addAll(localSourceObjectsForTatt); 
			    targetObjectsForTatt.addAll(localTargetObjectsForTatt);
				perTattSobjs.put(tatt,sourceObjectsForTatt); 		    
                perTattTobjs.put(tatt,targetObjectsForTatt);

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
              Vector localSourceObjectsForTatt = new Vector(); 
              Vector localTargetObjectsForTatt = new Vector(); 
			
              for (int k = 0; k < tobjs.size(); k++)
              { ObjectSpecification trgobj = (ObjectSpecification) tobjs.get(k); 
                Vector trgvect = trgobj.getCollectionValue(tatt,this);
			  // System.out.println(">>> (" + srcobj + ")." + tatt + " = " + srcvect);
                xstrs[k] = trgvect; 
                localSourceObjectsForTatt.add(key);
                localTargetObjectsForTatt.add(trgobj);
              }  

              if ((tatt.isSequence() && 
                   AuxMath.allDifferentSequences(xstrs)) || 
                  (tatt.isSet() && 
                   AuxMath.allDifferentSets(xstrs)) )
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
				sourceObjectsForTatt.addAll(localSourceObjectsForTatt); 
			    targetObjectsForTatt.addAll(localTargetObjectsForTatt);
				perTattSobjs.put(tatt,sourceObjectsForTatt); 		    
                perTattTobjs.put(tatt,targetObjectsForTatt);
              } // actually vmap[key] should be the array of all base elements within any of the xstrs[k] vectors
              else 
              { Vector removedtatts = new Vector(); 
                removedtatts.add(tatt); 
                // identificationProperties.removeAll(removedtatts);
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

      Vector removedems = new Vector(); 
	  Vector addedems = new Vector(); 
  	  java.util.HashMap replicationMap = new java.util.HashMap(); // of InstanceReplicationData, one for each tatt
	    
      for (int j = 0; j < identificationProperties.size(); j++) 
      { Attribute tatt = (Attribute) identificationProperties.get(j);
	  
	    System.out.println(">>> Replication target feature " + tatt + " source objects=" + perTattSobjs.get(tatt) + 
		                   " target objects= " + perTattTobjs.get(tatt)); 
						   
	  // Entity tent = tatt.getOwner(); 
	    String tattname = tatt.getName(); 
	    Type tattElementType = tatt.getElementType(); 
	    String telemName = ""; 
	    if (tattElementType != null) 
	    { telemName = tattElementType.getName(); }
	  
	    Vector possibleSourceFeatures = new Vector(); 
	    java.util.HashMap splittingConds = new java.util.HashMap(); 
        InstanceReplicationData repData = new InstanceReplicationData(tatt); 
		repData.sent = sent; 
		repData.tent = tent; // assuming, only one tent for each sent involved in instance replication. 
		
        Vector s1atts = sent.allDefinedProperties();
        Vector s2atts = sent.getNonLocalFeatures(); 
        Vector sattributes = new Vector(); 
        sattributes.addAll(s1atts); 
        sattributes.addAll(s2atts); 
    
        Vector satts = ModelMatching.findBaseTypeCompatibleSourceAttributes(tatt,sattributes,sent,ems); 
        System.out.println(">>> Possible replication range source features for " + tatt + " are: " + satts); 
	  // Only interested in collection-valued satt. Could take unions/closures of them. 
	  
	    java.util.HashMap vmap = (java.util.HashMap) valuemap.get(tattname); 

        for (int i = 0; i < keys.size(); i++) 
        { ObjectSpecification sobj = (ObjectSpecification) keys.get(i); 

          if (tatt.isEntity()) 
          { ObjectSpecification[] values = (ObjectSpecification[]) vmap.get(sobj); 
  		    // compare these target values of tatt for tobjs derived from sobj, to mapped values of some feature sobj.satt

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
                  if (satt1values != null && AuxMath.isUnionSets(sattvalues,satt1values,values,telemName,this))
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
		  // compare the union of the values to the values of some feature sobj.satt

          
		  if (values != null) 
		  { // System.out.println(">>> for source " + sobj + " target feature " + tatt + " values are: " + values.length); 
		    Vector flattenedValues = VectorUtil.flattenVectorArray(values); 
		    System.out.println(">>> for source " + sobj + " target feature " + tatt + " values are: " + flattenedValues); 
		    
            for (int k = 0; k < satts.size(); k++) 
            { Attribute satt = (Attribute) satts.get(k); 
              String sattname = satt.getName();
              Vector sattvalues = sobj.getCollectionValue(satt,this);   // it could be single-valued, but treated as collection
			     // getAllReferredObjects(allsobjs,satt); 
              System.out.println(">>> for source " + sobj + " source feature " + satt + " values are: " + sattvalues); 
		    
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
          { System.out.println(">>> for source " + sobj + " target feature " + tatt + " values are: " + values.length); 
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
                  BinaryExpression sattplus = new BinaryExpression("+", sattbe, suffbe); 
                  sattplus.setType(new Type("String", null)); 
                  sattplus.setElementType(new Type("String", null));
                  suffixesexpr.addElement(sattplus); 
				  suffixesexpr.setType(new Type("Set", null)); 
				  suffixesexpr.setElementType(new Type("String", null));
                }
				
                if (possibleSourceFeatures.contains(satt)) { } 
		        else 
		        { possibleSourceFeatures.add(satt);   
                  Attribute x$ = new Attribute("x$", satt.getElementType(), ModelElement.INTERNAL); 
                  BinaryExpression insuffixset = 
				    new BinaryExpression(":", new BasicExpression(x$), suffixesexpr); 
				  splittingConds.put(sattname, insuffixset);
				}  
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
				  prefixesexpr.setType(new Type("Set", null)); 
				  prefixesexpr.setElementType(new Type("String", null));
				}
			    if (possibleSourceFeatures.contains(satt)) { } 
		        else 
		        { possibleSourceFeatures.add(satt);  
				
                  Attribute x$ = new Attribute("x$", satt.getElementType(), ModelElement.INTERNAL); 
			  	  BinaryExpression inprefixset = 
				    new BinaryExpression(":", new BasicExpression(x$), prefixesexpr); 
				  splittingConds.put(sattname, inprefixset);
				}  
			  } 
			  else 
			  { possibleSourceFeatures.remove(satt); }
			}
		  }
		}
	  }
	  
	  // we are still in the big loop for tatt. Terrible programming. 
	  
	  System.out.println(">>> Possible instance replication sources for " + sent + " |--> " + tent + " to " + tatt +  
	                     " are: " + possibleSourceFeatures); 
	  // lookup the sent |-> tent matching, add the possible x |--> tatt mappings, for  
      // extra condition  x : sent

	  
      if (possibleSourceFeatures.size() > 0 && tent != null) 
      { // choose just one as the basis of the mapping  { v : satt } sent |--> tent  that has  v |--> tatt
	    Object satt = possibleSourceFeatures.get(0);
		if (satt instanceof Attribute) 
		{ repData.satt = (Attribute) satt; }
		else if (satt instanceof Expression)
		{ repData.sexpr = (Expression) satt; } 
        
		Expression spcond = (Expression) splittingConds.get(satt + ""); 
        if (spcond != null) 
		{ repData.setSplittingCondition(spcond); }
		
        repData.sourceObjects = (Vector) perTattSobjs.get(tatt); 
		repData.targetObjects = (Vector) perTattTobjs.get(tatt);       
	    EntityMatching sent2tent = ModelMatching.getRealEntityMatching(sent,tent,ems);
        if (sent2tent != null)
	    { repData.entityMapping = sent2tent; 
		  replicationMap.put(tatt, repData);  // This is the preferred satt for tatt, a new rule will arise from this.
		  
		  System.out.println(">>> Potential replication rule: " + repData);
		}   
	  }  
	} // End of loop for tatt; now we use the replicationMap entries
	
	System.out.println(">> Potential instance replication rules are: " + replicationMap); 
		  
    Vector targetAttributes = new Vector(); 
	targetAttributes.addAll(replicationMap.keySet()); 
	
	if (targetAttributes.size() == 1)
	{ // only one splitting rule, just modify the existing rule
	  Attribute tatt = (Attribute) targetAttributes.get(0); 
	  InstanceReplicationData repData = (InstanceReplicationData) replicationMap.get(tatt); 
	  EntityMatching newem = repData.entityMapping; 
	  
	  Type elemT = null; 
      Expression rang = null; 
      	  
      if (repData.scond != null && (repData.scond instanceof BinaryExpression)) 
	  { BinaryExpression inr = (BinaryExpression) repData.scond; 
	    rang = inr.getRight();
		elemT = rang.getElementType();  
	  } 
	  else if (repData.satt != null) 
      { elemT = repData.satt.getElementType(); 
        rang = new BasicExpression(repData.satt);
        rang.setUmlKind(Expression.ATTRIBUTE);  
      }
      else if (repData.sexpr != null)
      { elemT = repData.sexpr.getElementType(); 
        rang = repData.sexpr;  
      }
		  
      Attribute x = new Attribute("x$0", elemT, ModelElement.INTERNAL);
      x.setElementType(elemT);  
      AttributeMatching newam = new AttributeMatching(x,tatt); 
      newem.replaceAttributeMatching(newam);
            /* Expression cond = (Expression) splittingConds.get(satt + ""); 
            if (cond != null)
            { newem.addReplicationCondition(cond);
              newem.addCondition(cond);
            }
            else */  
      BasicExpression xbe = new BasicExpression(x); 
      xbe.setElementType(elemT); 
      BinaryExpression xinsatt = new BinaryExpression(":", xbe, rang); 
      newem.addReplicationCondition(xinsatt);
      newem.addCondition(xinsatt);  
	}
	else if (targetAttributes.size() > 1)  // multiple splitting rules, one for each tatt
	{ for (int p = 0; p < targetAttributes.size(); p++)
	  { Attribute tatt = (Attribute) targetAttributes.get(p); 
  	    InstanceReplicationData repData = (InstanceReplicationData) replicationMap.get(tatt); 
	    EntityMatching newem = (EntityMatching) repData.entityMapping.clone();
		removedems.add(repData.entityMapping); 
		addedems.add(newem);  

  	    Type elemT = null; 
        Expression rang = null; 
      	  
        if (repData.scond != null && (repData.scond instanceof BinaryExpression)) 
	    { BinaryExpression inr = (BinaryExpression) repData.scond; 
	      rang = inr.getRight();
		  elemT = rang.getElementType();  
	    } 
        else if (repData.satt != null) 
        { elemT = repData.satt.getElementType(); 
          rang = new BasicExpression(repData.satt);
          rang.setUmlKind(Expression.ATTRIBUTE);  
        }
        else if (repData.sexpr != null)
        { elemT = repData.sexpr.getElementType(); 
          rang = repData.sexpr;  
        }
		  
        Attribute x = new Attribute("x$" + p, elemT, ModelElement.INTERNAL);
        x.setElementType(elemT);  
        AttributeMatching newam = new AttributeMatching(x,tatt); 
        newem.replaceAttributeMatching(newam);
            /* Expression cond = (Expression) splittingConds.get(satt + ""); 
            if (cond != null)
            { newem.addReplicationCondition(cond);
              newem.addCondition(cond);
            }
            else */  
        BasicExpression xbe = new BasicExpression(x); 
        xbe.setElementType(elemT); 
        BinaryExpression xinsatt = new BinaryExpression(":", xbe, rang); 
        newem.addReplicationCondition(xinsatt);
        newem.addCondition(xinsatt);  
	  } 
	}
	  /* 
	  EntityMatching sent2tent = ModelMatching.getRealEntityMatching(sent,tent,ems);
        if (sent2tent != null)
        { removedems.add(sent2tent); 
		
		  for (int i = 0; i < possibleSourceFeatures.size(); i++) 
		  { Object satt = possibleSourceFeatures.get(i); 
            Type elemT = null; 
            Expression rang = null; 
			EntityMatching newem = (EntityMatching) sent2tent.clone(); 
			
		  
            if (satt instanceof Attribute) 
            { elemT = ((Attribute) satt).getElementType(); 
              rang = new BasicExpression((Attribute) satt);
              rang.setUmlKind(Expression.ATTRIBUTE);  
            }
            else if (satt instanceof Expression)
            { elemT = ((Expression) satt).getElementType(); 
              rang = (Expression) satt; 
            }
		  
            Attribute x = new Attribute("x$" + i, elemT, ModelElement.INTERNAL);
            x.setElementType(elemT);  
            AttributeMatching newam = new AttributeMatching(x,tatt); 
            newem.replaceAttributeMatching(newam);
            Expression cond = (Expression) splittingConds.get(satt + ""); 
            if (cond != null)
            { newem.addReplicationCondition(cond);
              newem.addCondition(cond);
            }
            else 
            { BasicExpression xbe = new BasicExpression(x); 
              xbe.setElementType(elemT); 
              BinaryExpression xinsatt = new BinaryExpression(":", xbe, rang); 
              newem.addReplicationCondition(xinsatt);
              newem.addCondition(xinsatt);
            }
			addedems.add(newem);    
		  } 
		  
		  ems.removeAll(removedems); 
		  ems.addAll(addedems); 
        }
      } 
    } */ 
	
	ems.removeAll(removedems); 
    ems.addAll(addedems); 
       
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

  public boolean correspondingObjects(ObjectSpecification sobj, ObjectSpecification tobj) 
  { Vector tobjs = getCorrespondingObjects(sobj);
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

  public boolean correspondingObjectSequences(Vector svals, Vector tvals) 
  { if (svals.size() != tvals.size())
    { return false; } 
	
    for (int i = 0; i < svals.size(); i++) 
    { ObjectSpecification sobj = (ObjectSpecification) svals.get(i); 
      if (correspondingObjects(sobj,(ObjectSpecification) tvals.get(i))) {} 
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
      { System.out.println("** Instance " + x + " satisfies condition " + cond); 
      }
      else 
      { System.err.println("!! Instance " + x + " fails condition " + cond);
        res = false;
      }
    }
    return res;  
  }

  public boolean checkConstraintInModel(Constraint con, Vector objects)
  { boolean res = true; 
    Expression cond = con.antecedent(); 
    Expression succ = con.succedent(); 

    for (int i = 0; i < objects.size(); i++) 
    { ObjectSpecification x = (ObjectSpecification) objects.get(i); 
      if (x.satisfiesCondition(cond,this)) 
      { System.out.println("** Instance " + x + " satisfies condition " + cond); 
        if (x.satisfiesCondition(succ,this))
        { System.out.println("** Instance " + x + " positively satisfies constraint " + cond + " => " + succ); 
        }
        else 
        { System.out.println("!! Instance " + x + " fails constraint " + cond + " => " + succ);
          res = false;  
        }
      } 
      else 
      { System.err.println("!! Instance " + x + " fails condition " + cond);
        System.err.println("!! Instance " + x + " vacuously satifies constraint " + cond + " => " + succ);
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
    }

    if (alldefined == false) 
    { return false; } 	
      
    if (AuxMath.isConstant(attvalues))
    { String constv = "\"" + attvalues[0] + "\""; 
      System.out.println(">> Constant attribute " + constv + " |--> " + attname);
      return true;  
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

  public boolean disjointValueSetsOfObjects(Attribute att, Vector sobjs, Vector otherobjs, Vector passedValues, Vector rejectedValues)
  { // The att values for the sobjs are always in a set Passed which 
    // is disjoint from the set, Rejected, of values of att for the otherobjs
	
	boolean res = true; 
		
    Vector passedAttValues = ObjectSpecification.getAllValuesOf(att, sobjs, this); 
    Vector rejectedAttValues = ObjectSpecification.getAllValuesOf(att, otherobjs, this); 
	  
    passedValues.addAll(passedAttValues); 
    rejectedValues.addAll(rejectedAttValues); 
	System.out.println(">> Passed values of " + att + " = " + passedValues + " rejected values: " + rejectedValues); 
	
	Vector intersection = new Vector(); 
	intersection.addAll(passedValues); 
	intersection.retainAll(rejectedValues);
	System.out.println(">>> Common values of " + passedValues + " and " + rejectedValues + " are: " + intersection); 
	 
	if (intersection.size() == 0) 
	{ return true; } 
	return false; 
  }

  public boolean disjointValueSets(Expression expr, Vector[] sobjs, Vector[] tobjs, Vector passedValues, Vector rejectedValues)
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
	  
	  Vector passedAttValues = ObjectSpecification.getAllValuesOf(expr,restrictedSources,this); 
	  Vector rejectedAttValues = ObjectSpecification.getAllValuesOf(expr,othersources,this); 
	  
	  passedValues.addAll(passedAttValues); 
	  rejectedValues = VectorUtil.union(rejectedValues,rejectedAttValues); 
	  System.out.println(">> Passed values of " + expr + " = " + passedValues + " rejected values: " + rejectedValues); 
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
    // Set of pairs [s,t] where s |-> t and satisfy em
  
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
      // System.out.println(sobj + " satisfies? " + cond); 
      // sobj.satisfiesCondition(cond,this); 

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
	  
      System.out.println(">>> Object matchings for { " + em.condition + " } " + em.realsrc + " |--> " + em.realtrg + " are: " + pairs); 
      if (pairs.size() == 0) 
      { System.out.println(">> This mapping has no instances, it will be removed from the TL specification."); 
        removed.add(em); 
      }
      else 
      { trgent.defineLocalFeatures(); 
        identifyConstantAttributeMatchings(trgent,em,pairs);

        if (maxSourcePath > 1) 
        { em.realsrc.defineNonLocalFeatures(); }
 
        identifyCopyAttributeMatchings(em.realsrc, trgent, em, pairs, ems, tms); 
        identifyPrimitiveTypeAttributeMatchings(em.realsrc, trgent, em, pairs, ems, tms);

        identifyManyToOneAttributeMatchings(em.realsrc, trgent, em, pairs, ems);
        identifyTwoToOneAttributeMatchings(em.realsrc, trgent, em, pairs, ems);
        identifyTargetQueryFunctions(em.realsrc, trgent, em, pairs, ems, tms); 
      
        Vector tatts = trgent.getLocalFeatures();

        System.out.println(">>> Checking composed sequence/set/tree functions for " + tatts); 
        System.out.println(); 

        for (int p = 0; p < tatts.size(); p++) 
        { Attribute tatt = (Attribute) tatts.get(p);

          System.out.println(tatt + " is unused: " + em.isUnusedTargetByName(tatt)); 
          System.out.println(tatt + " is sequence: " + tatt.isSequence()); 
  
          if (em.isUnusedTargetByName(tatt) && 
           // ModelMatching.isUnusedTarget(tatt,ems) && 
              tatt.isSequence())
          { System.out.println(">>> Checking composed functions for " + tatt);
 
            Vector newams = identifyComposedSequenceFunctions(tatt, em.realsrc, trgent, pairs,em,ems);
            System.out.println(">>> New attribute mappings: " + newams);  
            if (newams != null)
            { for (int q = 0; q < newams.size(); q++) 
              { AttributeMatching amx = (AttributeMatching) newams.get(q); 
                em.addAttributeMapping(amx); 
              } 
            } 
          } 
          else if (em.isUnusedTargetByName(tatt) && 
           // ModelMatching.isUnusedTarget(tatt,ems) && 
                   tatt.isSet())
          { System.out.println(">>> Checking composed functions for " + tatt);
 
            Vector newams = identifyComposedSetFunctions(tatt, em.realsrc, trgent, pairs,em,ems);
            System.out.println(">>> New attribute mappings: " + newams);  
            if (newams != null) 
            { for (int q = 0; q < newams.size(); q++) 
              { AttributeMatching amx = (AttributeMatching) newams.get(q); 
                em.addAttributeMapping(amx); 
              }
            }  
          } 
          else if (em.isUnusedTargetByName(tatt) && 
           // ModelMatching.isUnusedTarget(tatt,ems) && 
                   tatt.isTree())
          { System.out.println(">>> Checking composed tree functions for " + tatt);
 
            Vector newams = identifyComposedTreeFunctions(
                tatt, em.realsrc, trgent, pairs,em,ems,tms);
            System.out.println(">>> New attribute mappings: " + newams);  
            if (newams != null) 
            { for (int q = 0; q < newams.size(); q++) 
              { AttributeMatching amx = (AttributeMatching) newams.get(q); 
                em.addAttributeMapping(amx); 
              }
            }  
          } 
        } 
		// checkMissingTargetIdentities(em.realsrc,trgent,em,pairs,ems); 
      } 
    } 
	
    ems.removeAll(removed);     
  }   
  
  public void defineTargetQueryFunctions(Vector ems, Vector tms)
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      Entity trgent = em.realtrg;
      Vector tobjs = (Vector) objectsOfClass.get(trgent.getName()); 
      identifyQueryFunctions(trgent,tobjs,tms);
    } // The target query functions are functions of trgent. 
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
    if (maxTargetPath > 1) 
    { tattributes.addAll(t2atts); } 
	
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
        { System.out.println("!! ERROR: Unused target identity feature " + fatt + " --> it must be assigned for transformation to be valid");
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
      BasicExpression tattbe = new BasicExpression(att); 
      tattbe.setUmlKind(Expression.ATTRIBUTE);  
           

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
          attvalues[j] = tobj.getNumericValue(att,this); 
        }
		
        if (alldefined && AuxMath.isConstant(attvalues))  
        { double constv = attvalues[0]; 
          System.out.println(">> Constant numeric feature mapping " + constv + " |--> " + attname); 
		  
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
		   
            System.out.println(">> Constant string feature mapping " + constv + " |--> " + attname);
            System.out.println();  
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
		
	    if (alldefined && att.isSequence() && AuxMath.isConstantSequences(attvalues))
	    { String constv = "" + attvalues[0]; 
           System.out.println(">> Constant sequence feature mapping " + constv + " |--> " + attname);
           System.out.println();  
           AttributeMatching amx = new AttributeMatching(new SetExpression(attvalues[0],att.isSequence()), tattbe); 
           res.add(amx); 
           emx.addMapping(amx); 
         }
         else if (alldefined && att.isSet() && AuxMath.isConstantSets(attvalues))
	    { String constv = "" + attvalues[0]; 
           System.out.println(">> Constant set feature mapping " + constv + " |--> " + attname);
           System.out.println();  
           AttributeMatching amx = new AttributeMatching(new SetExpression(attvalues[0],att.isSequence()), tattbe); 
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
    System.out.println(">>>> Trying to identify additional copy feature mappings for { " + emx.condition + " } " + sent + " |--> " + tent); 
    System.out.println(); 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1) 
    { sattributes.addAll(s2atts); }
 
    System.out.println(">>> All attributes of " + sent + " are " + sattributes);
    Attribute selfattx = new Attribute("self", new Type(sent), ModelElement.INTERNAL); 
    selfattx.setType(new Type(sent)); 
    selfattx.setElementType(new Type(sent));
    sattributes.add(selfattx); 

    Vector t1atts = tent.allDefinedProperties();
    Vector t2atts = tent.getNonLocalFeatures(); 
    Vector tattributes = new Vector(); 
    tattributes.addAll(t1atts); 
    if (maxTargetPath > 1)
    { tattributes.addAll(t2atts); }  
	 
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
             sattvalues[j] = sobj.getNumericValue(satt,this);
             tattvalues[j] = tobj.getNumericValue(tatt,this); 
    	      }
		
		 if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
	      { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
	        AttributeMatching amx = new AttributeMatching(satt, tatt); 
	        res.add(amx); 
	        emx.addMapping(amx);
	        found = true;  
	  	 }
		 else if (alldefined && en > 2 &&  AuxMath.linearCorrelation(sattvalues,tattvalues) > 0.98)
            { double slope = AuxMath.linearSlope(); 
              double offset = AuxMath.linearOffset();
              slope = AuxMath.to3dp(slope); 
		   offset = AuxMath.to3dp(offset);  
		   System.out.println(">> Apparent linear correspondence " + sattname + "*" + slope + " + " + offset + " |--> " + tattname); 
		   BasicExpression slopebe = new BasicExpression(slope); 
	        BasicExpression offsetbe = new BasicExpression(offset); 
		   BasicExpression sattbe = new BasicExpression(satt);
		   sattbe.setUmlKind(Expression.ATTRIBUTE); 
			 
		   BinaryExpression mbe = new BinaryExpression("*", sattbe, slopebe);
		   BinaryExpression lhsbe = new BinaryExpression("+", mbe, offsetbe);
		   lhsbe.setType(new Type("double", null));   
		   AttributeMatching amx = new AttributeMatching(lhsbe, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
              found = true;  
            }
            else if (alldefined && satt.isInteger() && 
              tatt.isInteger() && en > 2 &&
              AuxMath.isFunctional(sattvalues,tattvalues))
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
	    else if (satt.isNumeric() && tatt.isNumericCollection())
	    { double[] sattvalues = new double[en]; 
	      Vector[] tattvalues = new Vector[en]; 
	      boolean alldefined = true; 
		
           // JOptionPane.showInputDialog("Numeric to numeric collection mapping for: " + satt + " |--> " + tatt);

           for (int j = 0; j < en; j++) 
           { Vector pair = (Vector) pairs.get(j); 
             ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
             ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
             if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
	        else 
	        { alldefined = false; } 
             sattvalues[j] = sobj.getNumericValue(satt,this);
             tattvalues[j] = tobj.getCollectionValue(tatt,this); 
    	      }
		
		 if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
	      { System.out.println(">> Singleton feature mapping " + sattname + " |--> " + tattname); 
	        AttributeMatching amx = new AttributeMatching(satt, tatt); 
	        res.add(amx);
               amx.unionSemantics = true; 
             emx.addMapping(amx);
	        found = true;  
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
          else if (alldefined && AuxMath.isUpperCased(sattvalues,tattvalues)) 
          { BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE);
            UnaryExpression upperc = new UnaryExpression("->toUpperCase", sattbe); 
            upperc.setType(new Type("String", null)); 
            upperc.setElementType(new Type("String", null)); 
            System.out.println(">> Uppercased mapping " + sattname + "->toUpperCase() |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(upperc, tatt); 
            res.add(amx); 
            emx.addMapping(amx); 
            found = true; 
          }  
          else if (alldefined && AuxMath.isLowerCased(sattvalues,tattvalues)) 
          { BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE);
		    UnaryExpression lowerc = new UnaryExpression("->toLowerCase", sattbe); 
		    lowerc.setType(new Type("String", null)); 
		    lowerc.setElementType(new Type("String", null)); 
		    System.out.println(">> Lowercased mapping " + sattname + "->toLowerCase() |--> " + tattname); 
		    AttributeMatching amx = new AttributeMatching(lowerc, tatt); 
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
			  BasicExpression suffexpr = new BasicExpression("\"" + suff + "\""); 
			  suffexpr.setType(new Type("String", null));  
			  suffexpr.setElementType(new Type("String", null));  
			  
			  BinaryExpression removesuff = new BinaryExpression("->before", sattbe, suffexpr); 
			  removesuff.setType(new Type("String", null));  
			  removesuff.setElementType(new Type("String", null));  
			  AttributeMatching amx = new AttributeMatching(removesuff, tatt); 
		      res.add(amx); 
		      emx.addMapping(amx);
			  found = true;  
			}
			else 
			{ Vector suffs = AuxMath.allSuffixes(tattvalues,sattvalues); 
			  System.out.println(">> all suffixes = " + suffs); 
			  String cpref = AuxMath.longestCommonPrefix(suffs); 
			  System.out.println(">> Longest common prefix = " + cpref); 
			  if (cpref != null && cpref.length() > 0)
			  { System.out.println(">> feature mapping " + sattname + "->before(\"" + cpref + "\") |--> " + tattname); 
                    BasicExpression sattbe = new BasicExpression(satt); 
                    sattbe.setUmlKind(Expression.ATTRIBUTE);
                    BasicExpression suffexpr = new BasicExpression("\"" + cpref + "\""); 
                    suffexpr.setType(new Type("String", null));  
                    suffexpr.setElementType(new Type("String", null));  
			    
                    BinaryExpression removesuff = new BinaryExpression("->before", sattbe, suffexpr); 
				
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
              System.out.println(">> all prefixes = " + prefs); 
              String csuff = AuxMath.longestCommonSuffix(prefs); 
              System.out.println(">> Longest common suffix = " + csuff); 
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
          { System.out.println(">>>> Create a specific string-to-string function for " + satt + " to " + tatt + " conversion?"); 
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
              fsatt.setUmlKind(Expression.QUERY);  
              BasicExpression sattbe = new BasicExpression(satt); 
              sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
              fsatt.addParameter(sattbe); 
              fsatt.setType(new Type("String", null)); 
              fsatt.setElementType(new Type("String", null)); 
			    
              AttributeMatching amx = new AttributeMatching(fsatt, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
              tms.add(newfunction);
              found = true;   
            }  
          } 
        }	
        else if (satt.isString() && tatt.isStringCollection())
        { String[] sattvalues = new String[en];     
          Vector[] tattvalues = new Vector[en]; 
          boolean alldefined = true;
 
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = sobj.getStringValue(satt,this); 
            tattvalues[j] = tobj.getCollectionValue(tatt,this); 
          }
	        	
          if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
          { System.out.println(">> Copy String-to-StringSet mapping " + sattname + " |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(satt, tatt); 
            res.add(amx); 
            amx.unionSemantics = true; 
            emx.addMapping(amx); 
            found = true; 
          } 
          // JOptionPane.showInputDialog("Create a String-to-String-set function " + satt + " " + tatt + "? (y/n):");
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

           boolean allEmpty = true; 
		  				 	 
           for (int j = 0; j < en; j++) 
           { Vector pair = (Vector) pairs.get(j); 
             ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
             ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
		    // if (sobj.hasDefinedValue(satt,this) && tobj.hasDefinedValue(tattname)) { } 
               // else 
		    // { alldefined = false; } 
             sattvalues[j] = sobj.getCollectionValue(satt,this); 
             tattvalues[j] = tobj.getCollectionValue(tatt,this);
		
             if (sattvalues[j].size() > 0)
             { allEmpty = false; }
             if (tattvalues[j].size() > 0)
             { allEmpty = false; }
			 	
                 // System.out.println(sobj + "." + satt + " = " + sattvalues[j]); 
                 // System.out.println(tobj + "." + tatt + " = " + tattvalues[j]); 
           }
		
           if (alldefined && satt.isSequence() &&
               tatt.isSequence() && 
               AuxMath.isCopySequences(sattvalues,tattvalues,this) && !allEmpty)
           { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
             AttributeMatching amx = new AttributeMatching(satt, tatt); 
             res.add(amx); 
             emx.addMapping(amx);
             found = true;  
           }
           else if (alldefined && !allEmpty &&
             satt.isSet() && tatt.isSet() && 
             AuxMath.isCopySets(sattvalues,tattvalues,this) )
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
              System.out.println(">> Possible selection feature mapping " + sattname + "->select(condition) |--> " + tattname); 
            }
            else if (alldefined && 
              satt.isSet() && tatt.isSet() && 
              AuxMath.isSubsetSet(sattvalues,tattvalues,this))
            { System.out.println(">> Possible union feature mapping " + sattname + "->union(something) |--> " + tattname);
              System.out.println(">> or " + sattname + "->including(something)");
              int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Imprecise: LHS <: RHS", JOptionPane.YES_NO_OPTION);
              if (acceptIt == JOptionPane.YES_OPTION) 
              { AttributeMatching amx = new AttributeMatching(satt, tatt); 
                amx.unionSemantics = true; 
                res.add(amx); 
                emx.addMapping(amx);
              } 
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
              tattvalues[j] = tobj.getCollectionValue(tatt,this);
              System.out.println(">> Checking inclusion feature mapping " + sattvalues[j] + " |--> " + tattvalues[j]); 
            }
		
            if (alldefined && AuxMath.allSubsets(sattvalues,tattvalues,this))
            { System.out.println(">> Inclusion feature mapping " + sattname + " |--> " + tattname); 

               int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Imprecise: LHS <: RHS", JOptionPane.YES_NO_OPTION);
               if (acceptIt == JOptionPane.YES_OPTION) 
               { AttributeMatching amx = new AttributeMatching(satt, tatt); 
                 amx.unionSemantics = true;  
                 res.add(amx); 
                 emx.addMapping(amx);
                 found = true;
               }   
		  }
		} 
	  }   
	}
	System.out.println(); 
	
	return res; 
  }

  public Vector identifyPrimitiveTypeAttributeMatchings(Entity sent, Entity tent, EntityMatching emx, Vector pairs, Vector ems, Vector tms)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    Vector res = new Vector(); 

    System.out.println(); 
    System.out.println(">>>> Trying to identify type-conversion feature mappings for { " + emx.condition + " } " + sent + " |--> " + tent); 
    System.out.println(); 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1)
    { sattributes.addAll(s2atts); }
     
    Vector tattributes = tent.allDefinedProperties(); 
    Vector t2atts = tent.getNonLocalFeatures(); 
    Vector tatts = new Vector(); 
    tatts.addAll(tattributes); 
    if (maxTargetPath > 1)
    { tatts.addAll(t2atts); }  	 
    
    for (int i = 0; i < tatts.size(); i++)
    { Attribute tatt = (Attribute) tatts.get(i);
      String tattname = tatt.getName();
      
      if (emx.isUnusedTargetByName(tatt)) { }
      else 
      { continue; } 
	
      if (tatt.isValueType())
      { } 
      else 
      { continue; } 
  
      System.out.println(); 
      System.out.println(">> Unused target feature: " + tattname);
      boolean found = false; 
	   
      for (int k = 0; k < sattributes.size() && !found; k++) 
      { Attribute satt = (Attribute) sattributes.get(k);
        if (satt.isPrimitiveType()) { } 
        else 
        { continue; } 

        String sattname = satt.getName();  
	 
        System.out.println(">> Checking possible map " + satt + " : " + satt.getType() + 
		                     " |--> " + tatt + " : " + tatt.getType()); 
	   	  
        if (satt.isBoolean() && tatt.isEnumeration())
        { // functional relationship? 
          boolean[] sattvalues = new boolean[en]; 
          String[] tattvalues = new String[en];
          boolean alldefined = true; 
		 
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = ((Boolean) sobj.getActualValueOf(satt,this)).booleanValue(); 
            tattvalues[j] = (String) tobj.getActualValueOf(tatt,this); 
          }
		
          if (alldefined && AuxMath.isFunctional(sattvalues,tattvalues))
          { System.out.println(">> Functional boolean-to-Enumeration mapping " + sattname + " |--> " + tattname); 
            /* int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Type conversion", JOptionPane.YES_NO_OPTION);
            */ 

            // if (acceptIt == JOptionPane.YES_OPTION)
            { TypeMatching newfunction = new TypeMatching(satt.getType(), tatt.getType()); 
              String fname = "convert_" + sent.getName() + satt.underscoreName() + "_to_" + tent.getName() + tatt.underscoreName(); 
              newfunction.setName(fname); 
           
              newfunction.setValues(sattvalues, tattvalues);
              BasicExpression fsatt = new BasicExpression(fname);
              fsatt.setUmlKind(Expression.QUERY);  
              BasicExpression sattbe = new BasicExpression(satt); 
              sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
              fsatt.addParameter(sattbe); 
              fsatt.setType(new Type("String", null)); 
              fsatt.setElementType(new Type("String", null)); 
              tms.add(newfunction);

              AttributeMatching amx = new AttributeMatching(fsatt, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
              System.out.println(">>> Added new function: " + newfunction);
              found = true; 
            }  
          }
        }
        else if (satt.isEnumeration() && tatt.isBoolean())
        { // functional relationship? 
          String[] sattvalues = new String[en]; 
          boolean[] tattvalues = new boolean[en];
          boolean alldefined = true; 
		 
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            tattvalues[j] = ((Boolean) tobj.getActualValueOf(tatt,this)).booleanValue(); 
            sattvalues[j] = (String) sobj.getActualValueOf(satt,this); 
          }
		
          if (alldefined && AuxMath.isFunctional(sattvalues,tattvalues))
          { System.out.println(">> Functional Enumeration-to-boolean mapping " + sattname + " |--> " + tattname); 
            int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Type conversion", JOptionPane.YES_NO_OPTION);
            

            if (acceptIt == JOptionPane.YES_OPTION)
            { TypeMatching newfunction = new TypeMatching(satt.getType(), tatt.getType()); 
              String fname = "convert_" + sent.getName() + satt.underscoreName() + "_to_" + tent.getName() + tatt.underscoreName(); 
              newfunction.setName(fname); 
           
              newfunction.setValues(sattvalues, tattvalues);
              BasicExpression fsatt = new BasicExpression(fname);
              fsatt.setUmlKind(Expression.QUERY);  
              BasicExpression sattbe = new BasicExpression(satt); 
              sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
              fsatt.addParameter(sattbe); 
              fsatt.setType(new Type("boolean", null)); 
              fsatt.setElementType(new Type("boolean", null)); 
              tms.add(newfunction);

              AttributeMatching amx = new AttributeMatching(fsatt, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
              System.out.println(">>> Added new function: " + newfunction);
              found = true; 
            }  
          }
        }
        // and other cases, enum-to-enum, enum-to-boolean, etc 
        else if (satt.isEnumeration() && tatt.isEnumeration())
        { // functional relationship? 
          String[] sattvalues = new String[en]; 
          String[] tattvalues = new String[en];
          boolean alldefined = true; 
		 
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = (String) sobj.getActualValueOf(satt,this); 
            tattvalues[j] = (String) tobj.getActualValueOf(tatt,this); 
          }
		
          if (alldefined && AuxMath.isFunctional(sattvalues,tattvalues))
          { System.out.println(">> Functional Enumeration-to-Enumeration mapping " + sattname + " |--> " + tattname); 
            int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Type conversion", JOptionPane.YES_NO_OPTION);
             

            if (acceptIt == JOptionPane.YES_OPTION)
            { TypeMatching newfunction = new TypeMatching(satt.getType(), tatt.getType()); 
              String fname = "convert_" + sent.getName() + satt.underscoreName() + "_to_" + tent.getName() + tatt.underscoreName(); 
              newfunction.setName(fname); 
           
              newfunction.setStringValues(sattvalues, tattvalues);
              BasicExpression fsatt = new BasicExpression(fname);
              fsatt.setUmlKind(Expression.QUERY);  
              BasicExpression sattbe = new BasicExpression(satt); 
              sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
              fsatt.addParameter(sattbe); 
              fsatt.setType(new Type("String", null)); 
              fsatt.setElementType(new Type("String", null)); 
              tms.add(newfunction);

              AttributeMatching amx = new AttributeMatching(fsatt, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
              System.out.println(">>> Added new function: " + newfunction);
              found = true; 
            }  
          }
        }
        else if (satt.isNumeric() && tatt.isString())
        { // functional relationship? 
          double[] sattvalues = new double[en]; 
          String[] tattvalues = new String[en];
          boolean alldefined = true; 
		 
          // JOptionPane.showConfirmDialog(null,
          // "Numeric to string mapping?", 
          // "Type conversion", JOptionPane.YES_NO_OPTION);
            
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = sobj.getNumericValue(satt,this); 
            tattvalues[j] = (String) tobj.getActualValueOf(tatt,this); 
          }
		
          if (alldefined && AuxMath.isFunctional(sattvalues,tattvalues))
          { System.out.println(">> Functional numeric-to-String mapping " + sattname + " |--> " + tattname); 
            int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Type conversion", JOptionPane.YES_NO_OPTION);
             

            if (acceptIt == JOptionPane.YES_OPTION)
            { TypeMatching newfunction = new TypeMatching(satt.getType(), tatt.getType()); 
              String fname = "convert_" + sent.getName() + satt.underscoreName() + "_to_" + tent.getName() + tatt.underscoreName(); 
              newfunction.setName(fname); 
           
              newfunction.setValues(sattvalues, tattvalues);
              BasicExpression fsatt = new BasicExpression(fname);
              fsatt.setUmlKind(Expression.QUERY);  
              BasicExpression sattbe = new BasicExpression(satt); 
              sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
              fsatt.addParameter(sattbe); 
              fsatt.setType(new Type("String", null)); 
              fsatt.setElementType(new Type("String", null)); 
              tms.add(newfunction);

              AttributeMatching amx = new AttributeMatching(fsatt, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
              System.out.println(">>> Added new function: " + newfunction);
              found = true; 
            }  
          }
        }
        else if (satt.isNumeric() && tatt.isStringCollection())
        { // functional relationship? 
          double[] sattvalues = new double[en]; 
          Vector[] tattvalues = new Vector[en];
          boolean alldefined = true; 
		 
          // JOptionPane.showConfirmDialog(null,
          // "Functional numeric to string collection", 
          // "Type conversion", JOptionPane.YES_NO_OPTION);
            
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
            ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
            if (sobj.hasDefinedValue(sattname) && tobj.hasDefinedValue(tattname)) { } 
            else 
            { alldefined = false; } 
            sattvalues[j] = sobj.getNumericValue(satt,this); 
            tattvalues[j] = tobj.getCollectionValue(tatt,this); 
          }
		
          if (alldefined && AuxMath.isFunctionalToSingletons(sattvalues,tattvalues))
          { System.out.println(">> Functional numeric-to-String collection mapping " + sattname + " |--> " + tattname); 
            int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Type conversion", JOptionPane.YES_NO_OPTION);
             

            if (acceptIt == JOptionPane.YES_OPTION)
            { TypeMatching newfunction = new TypeMatching(satt.getType(), tatt.getType()); 
              String fname = "convert_" + sent.getName() + satt.underscoreName() + "_to_" + tent.getName() + tatt.underscoreName(); 
              newfunction.setName(fname); 
           
              newfunction.setValues(sattvalues, tattvalues);
              BasicExpression fsatt = new BasicExpression(fname);
              fsatt.setUmlKind(Expression.QUERY);  
              BasicExpression sattbe = new BasicExpression(satt); 
              sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
              fsatt.addParameter(sattbe); 
              fsatt.setType(new Type("String", null)); 
              fsatt.setElementType(new Type("String", null)); 
              tms.add(newfunction);

              AttributeMatching amx = new AttributeMatching(fsatt, tatt); 
              amx.unionSemantics = true; 
              res.add(amx); 
              emx.addMapping(amx);
              System.out.println(">>> Added new function: " + newfunction);
              found = true; 
            }  
          }
        }
      } 
    } 
    return res; 
  } 

  public Vector identifyManyToOneAttributeMatchings(Entity sent, Entity tent, EntityMatching emx, Vector pairs, Vector ems)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify Collection to 1 feature mappings for { " + emx.condition + " } " + sent + " |--> " + tent); 
    System.out.println(); 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1)
    { sattributes.addAll(s2atts); }  
     
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

        // JOptionPane.showConfirmDialog(null,
        // "" + sattname + " to " + tattname, 
        // "Many-to-one", JOptionPane.YES_NO_OPTION);
             
	   	  
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
            sattvalues[j] = sobj.getNumericValue(satt,this);
            tattvalues[j] = tobj.getNumericValue(tatt,this); 
          }
		
          if (alldefined && AuxMath.isCopy(sattvalues,tattvalues))
          { System.out.println(">> Copy feature mapping " + sattname + " |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(satt, tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  // no need to look for another satt for tatt 
          }
        }   
        else if (satt.isNumericCollection() && tatt.isNumeric())
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
            double dd = tobj.getNumericValue(tatt,this); 
            tattvalues[j].add(new Double(dd));  
          }
		
          if (alldefined && AuxMath.isNumericSum(sattvalues,tattvalues))
          { System.out.println(">> Sum numeric mapping " + sattname + "->sum() |--> " + tattname); 
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->sum", sattbe); 
            ue.setType(satt.getElementType()); 
            AttributeMatching amx = new AttributeMatching(ue,tatt); 
            res.add(amx); 
            emx.addMapping(amx); 
            found = true; 
          }
          else if (alldefined && AuxMath.isNumericPrd(sattvalues,tattvalues))
          { System.out.println(">> Product numeric mapping " + sattname + "->prd() |--> " + tattname); 
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->prd", sattbe); 
            ue.setType(satt.getElementType()); 
            AttributeMatching amx = new AttributeMatching(ue, tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
          else if (alldefined && AuxMath.isNumericMin(sattvalues,tattvalues))
          { System.out.println(">> Min numeric mapping " + sattname + "->min() |--> " + tattname); 
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->min", sattbe); 
            ue.setType(satt.getElementType()); 
            
            AttributeMatching amx = new AttributeMatching(ue, tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
          else if (alldefined && AuxMath.isNumericMax(sattvalues,tattvalues))
          { System.out.println(">> Max numeric mapping " + sattname + "->max() |--> " + tattname); 
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->max", sattbe); 
            ue.setType(satt.getElementType()); 
            
            AttributeMatching amx = new AttributeMatching(ue, tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
          else if (alldefined && AuxMath.isNumericAverage(sattvalues,tattvalues))
          { System.out.println(">> Average numeric mapping " + sattname + "->average() |--> " + tattname); 
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->average", sattbe); 
            ue.setType(satt.getElementType()); 
                 
            AttributeMatching amx = new AttributeMatching(ue,tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
        }   
        else if (satt.isStringCollection() && tatt.isString())
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
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->sum", sattbe); 
            ue.setType(satt.getElementType()); 
            
            AttributeMatching amx = new AttributeMatching(ue, tatt); 
            res.add(amx); 
            emx.addMapping(amx); 
            found = true; 
          }
          else if (alldefined && AuxMath.isStringMax(sattvalues,tattvalues))
          { System.out.println(">> String max feature mapping " + sattname + "->max() |--> " + tattname); 
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->max", sattbe); 
            ue.setType(satt.getElementType()); 
            
            AttributeMatching amx = new AttributeMatching(ue,tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
          else if (alldefined && AuxMath.isStringMin(sattvalues,tattvalues))
          { System.out.println(">> String min feature mapping " + sattname + "->min() |--> " + tattname); 
            BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE); 
            UnaryExpression ue = new UnaryExpression("->min", sattbe); 
            ue.setType(satt.getElementType()); 
            
            AttributeMatching amx = new AttributeMatching(ue, tatt); 
            res.add(amx); 
            emx.addMapping(amx); 
            found = true; 
          }
        }   
        else if (satt.isCollection() && tatt.isCollection())     
        { Vector[] sattvalues = new Vector[en]; 
          Vector[] tattvalues = new Vector[en];
          boolean alldefined = true; 
	
          // JOptionPane.showConfirmDialog(null,
          // "collection-to-collection", 
          // sattname + "|-->" + tattname, JOptionPane.YES_NO_OPTION);
							 	 
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
		
          if (alldefined &&
              satt.isSet() && tatt.isSet() && 
              AuxMath.isCopySets(sattvalues,tattvalues,this))
          { System.out.println(">> Copy set feature mapping " + sattname + " |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(satt, tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
          else if (alldefined &&
            satt.isSequence() && tatt.isSequence() && 
            AuxMath.isCopySequences(sattvalues,tattvalues,this))
          { System.out.println(">> Copy sequence feature mapping " + sattname + " |--> " + tattname); 
            AttributeMatching amx = new AttributeMatching(satt, tatt); 
            res.add(amx); 
            emx.addMapping(amx);
            found = true;  
          }
        } // ->flatten() and ->reverse() cases. 
        else if (satt.isEntityCollection() && tatt.isEntity())
        { Vector[] sattvalues = new Vector[en]; 
          Vector[] tattvalues = new Vector[en];
          boolean alldefined = true; 
	
          // JOptionPane.showConfirmDialog(null,
          // "Entity-coll-to-entity", 
          // sattname + "|-->" + tattname, 
          // JOptionPane.YES_NO_OPTION);
             
					 	 
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
		
          if (alldefined && AuxMath.isCopySets(sattvalues,tattvalues,this))
          { System.out.println(">> Collection-to-object feature mapping " + sattname + "->any() |--> " + tattname);
            int acceptIt = JOptionPane.showConfirmDialog(null,
"Accept this?", "Imprecise: RHS <: LHS", JOptionPane.YES_NO_OPTION);
            if (acceptIt == JOptionPane.YES_OPTION) 
            { 
              BasicExpression sattbe = new BasicExpression(satt);
              sattbe.setUmlKind(Expression.ROLE);  
              UnaryExpression anysatt = new UnaryExpression("->any", sattbe);
              anysatt.setType(satt.getElementType());  
              AttributeMatching amx = new AttributeMatching(anysatt, tatt); 
              res.add(amx); 
              emx.addMapping(amx);
              found = true;
            }  
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
    System.out.println(">>>> Trying to identify 2 source feature to 1 target feature mappings for { " + emx.condition + " } " + sent + " |--> " + tent); 
    System.out.println(); 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1)
    { sattributes.addAll(s2atts); } 

    Attribute selfatt = new Attribute("self", new Type(sent), ModelElement.INTERNAL); 
    selfatt.setType(new Type(sent)); 
    selfatt.setElementType(new Type(sent)); 
    sattributes.add(selfatt);              
     
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
	 
          System.out.println(">> Checking possible maps " + satt + " : " + satt.getType() + " ->op " + satt1 + " : " + 
		                     satt1.getType() + 
		                     " |--> " + tatt + " : " + tatt.getType());
           if (satt.isNumeric() && satt1.isNumeric() && tatt.isNumeric())
           { double[] sattvalues = new double[en];
             double[] satt1values = new double[en];
             double[] tattvalues = new double[en];
             boolean alldefined = true; 
		
             BasicExpression sattbe = new BasicExpression(satt); 
             sattbe.setUmlKind(Expression.ATTRIBUTE); 
             BasicExpression satt1be = new BasicExpression(satt1); 
             satt1be.setUmlKind(Expression.ATTRIBUTE); 
		        
            for (int j = 0; j < en; j++) 
            { Vector pair = (Vector) pairs.get(j); 
              ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
              ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
              if (sobj.hasDefinedValue(satt,this) && sobj.hasDefinedValue(satt1,this) && tobj.hasDefinedValue(tattname)) { } 
              else 
              { alldefined = false; } 
              sattvalues[j] = sobj.getNumericValue(satt,this);
              satt1values[j] = sobj.getNumericValue(satt1,this);
              tattvalues[j] = tobj.getNumericValue(tatt,this); 
            }
		  
		    // check satt + satt1 = tatt, etc
            if (alldefined && AuxMath.isNumericSum(sattvalues,satt1values,tattvalues))
            { BinaryExpression add1st = new BinaryExpression("+", sattbe, satt1be); 
              AttributeMatching amx = new AttributeMatching(add1st, tatt); 
              res.add(amx); 
              emx.addMapping(amx); 
              found = true;
            }
            else if (alldefined && AuxMath.isNumericProduct(sattvalues,satt1values,tattvalues))
            { BinaryExpression add1st = new BinaryExpression("*", sattbe, satt1be); 
              AttributeMatching amx = new AttributeMatching(add1st, tatt); 
              res.add(amx); 
              emx.addMapping(amx); 
              found = true;
            }
            else if (alldefined && AuxMath.isNumericSubtraction(sattvalues,satt1values,tattvalues))
            { BinaryExpression add1st = new BinaryExpression("-", sattbe, satt1be); 
              AttributeMatching amx = new AttributeMatching(add1st, tatt); 
              res.add(amx); 
              emx.addMapping(amx); 
              found = true;
            }
            else if (alldefined && AuxMath.isNumericDivision(sattvalues,satt1values,tattvalues))
            { BinaryExpression add1st = new BinaryExpression("/", sattbe, satt1be); 
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
		  else if (satt.isCollection() && satt1.isEntity() && tatt.isCollection())
             { // could be satt->including(satt1)  |--> tatt
               // or satt->excluding(satt1) |--> tatt

               Vector[] sattvalues = new Vector[en]; 
               ObjectSpecification[] satt1values = new ObjectSpecification[en]; 
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
                 satt1values[j] = sobj.getObjectValue(satt1,this); 
                 tattvalues[j] = tobj.getCollectionValue(tatt,this); 
               }

               BasicExpression sattbe = new BasicExpression(satt); 
               sattbe.setUmlKind(Expression.ATTRIBUTE); 
               BasicExpression satt1be = new BasicExpression(satt1); 
               satt1be.setUmlKind(Expression.ATTRIBUTE); 
			   
               if (tatt.isSequence() && satt.isSequence() && 
                   AuxMath.isAppendSequences(sattvalues,satt1values,tattvalues,this))
               { System.out.println(">> Concatenation feature mapping " + sattname + "->append(" + satt1name + ") |--> " + tattname); 
                 BinaryExpression add1st = new BinaryExpression("->append", sattbe, satt1be); 
                 add1st.setType(satt.getType()); 
                 add1st.setElementType(satt.getElementType());  
                 AttributeMatching amx = new AttributeMatching(add1st, tatt); 
                 res.add(amx); 
                 emx.addMapping(amx); 
                 found = true; 
               }
               else if (tatt.isSet() && satt.isSet() && 
                   AuxMath.isIncludingSets(sattvalues,satt1values,tattvalues,this))
               { System.out.println(">> Set union feature mapping " + sattname + "->including(" + satt1name + ") |--> " + tattname); 
                 BinaryExpression add1st = new BinaryExpression("->incuding", sattbe, satt1be); 
                 add1st.setType(satt.getType()); 
                 add1st.setElementType(satt.getElementType());  
                 AttributeMatching amx = new AttributeMatching(add1st, tatt); 
                 res.add(amx); 
                 emx.addMapping(amx); 
                 found = true; 
               }
               else if (tatt.isSet() && satt.isSet() && 
                   AuxMath.isExcludingSets(sattvalues,satt1values,tattvalues,this))
               { System.out.println(">> Set subtraction feature mapping " + sattname + "->excluding(" + satt1name + ") |--> " + tattname); 
                 BinaryExpression add1st = new BinaryExpression("->excluding", sattbe, satt1be); 
                 add1st.setType(satt.getType()); 
                 add1st.setElementType(satt.getElementType());  
                 AttributeMatching amx = new AttributeMatching(add1st, tatt); 
                 res.add(amx); 
                 emx.addMapping(amx); 
                 found = true; 
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
			   
               if (tatt.isSequence() && satt.isSequence() && 
                   AuxMath.isConcatenation(sattvalues,satt1values,tattvalues,this))
               { System.out.println(">> Concatenation feature mapping " + sattname + " ^ " + satt1name + " |--> " + tattname); 
                 BinaryExpression add1st = new BinaryExpression("->union", sattbe, satt1be); 
                 add1st.setType(satt.getType()); 
			   add1st.setElementType(satt.getElementType());  
                 AttributeMatching amx = new AttributeMatching(add1st, tatt); 
                 res.add(amx); 
                 emx.addMapping(amx); 
                 found = true; 
               }
               else if (tatt.isSet() && satt.isSet() && 
                   AuxMath.isUnionSets(sattvalues,satt1values,tattvalues,this))
               { System.out.println(">> Set union feature mapping " + sattname + "->union(" + satt1name + ") |--> " + tattname); 
                 BinaryExpression add1st = new BinaryExpression("->union", sattbe, satt1be); 
                 add1st.setType(satt.getType()); 
			   add1st.setElementType(satt.getElementType());  
                 AttributeMatching amx = new AttributeMatching(add1st, tatt); 
                 res.add(amx); 
                 emx.addMapping(amx); 
                 found = true; 
               }
               else if (tatt.isSequence() && satt1.isSequence() &&  
                      AuxMath.isConcatenation(satt1values,sattvalues,tattvalues,this))
               { System.out.println(">> Concatenation feature mapping " + satt1name + " ^ " + sattname + " |--> " + tattname); 
                 BinaryExpression add1st = new BinaryExpression("->union", satt1be, sattbe);
                 add1st.setType(satt1.getType()); 
			   add1st.setElementType(satt1.getElementType());  
                 AttributeMatching amx = new AttributeMatching(add1st, tatt); 
                 res.add(amx); 
                 emx.addMapping(amx); 
                 found = true; 
               } 
               else if (tatt.isSet() && satt1.isSet() &&  
                      AuxMath.isUnionSets(satt1values,sattvalues,tattvalues,this))
               { System.out.println(">> Set union feature mapping " + satt1name + "->union(" + sattname + ") |--> " + tattname); 
                 BinaryExpression add1st = new BinaryExpression("->union", satt1be, sattbe);
                 add1st.setType(satt1.getType()); 
			   add1st.setElementType(satt1.getElementType());  
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
  
  public Vector identifyTargetQueryFunctions(Entity sent, Entity tent, EntityMatching emx, 
                                             Vector pairs, Vector ems, Vector tms)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
	// Only really for $text : String, representing the concrete syntax of the target object
	// Future work will extend to  $q : Sequence(T) and Set(T)
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify definitions of target string attributes for { " + emx.condition + " } " + 
	                   sent + " |--> " + tent); 
    System.out.println(); 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1)
    { sattributes.addAll(s2atts); }  
	
	System.out.println(">> All source attributes are: " + sattributes); 
     
    Vector tattributes = tent.allDefinedProperties(); 
	// Vector othertattributes = new Vector(); 
	// othertattributes.addAll(tattributes); 
	
    Vector res = new Vector();
	 
    for (int i = 0; i < tattributes.size(); i++)
    { Attribute tatt = (Attribute) tattributes.get(i);
      String tattname = tatt.getName();
	  // System.out.println(">> " + tent + " target feature: " + tattname);
      if (tatt.isString() && emx.isUnusedTargetByName(tatt)) 
	  { // Vector qattributes = new Vector(); 
	    // qattributes.add(tatt); 
		// othertattributes.removeAll(qattributes); 
	  }
      else 
      { continue; } 
	  
	  String[] tattvalues = new String[en]; 
	  Vector tvalues = new Vector(); 
   	  
      boolean alltdefined = true; 
		
      for (int j = 0; j < en; j++) 
      { Vector pair = (Vector) pairs.get(j); 
        ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
        if (tobj.hasDefinedValue(tattname)) { } 
        else 
        { alltdefined = false; } 
        tattvalues[j] = tobj.getString(tattname);
        tvalues.add(tattvalues[j]);  
      }
	  
      if (alltdefined) { } 
      else 
      { System.err.println("! Not every target instance has a " + tattname + " value"); 
	    continue; 
      }
		
      System.out.println(); 
      System.out.println(">> Target feature: " + tattname);
      System.out.println(">> Target values are: " + tvalues); 
      System.out.println(); 
	   
      Vector satts = ModelMatching.findBaseTypeCompatibleSourceAttributes(tatt, sattributes, emx, ems); // sattributes,emx,ems); 
      boolean found = false; 
	  
      Vector sourceattributes = new Vector();  // attributes considered for combination into tatt
      java.util.Map sattvalueMap = new java.util.HashMap();  // for each satt in sourceattributes, its values for sobjs of pairs
	  
      for (int k = 0; k < satts.size(); k++) 
      { Attribute satt = (Attribute) satts.get(k);
        String sattname = satt.getName(); 
	 
        if (satt.isNumeric())
        { String[] sattvalues = new String[en];
          boolean alldefined = true; 
		        
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0);   
            // if (sobj.hasDefinedValue(satt,this)) { } 
            // else 
            // { alldefined = false; } 
            sattvalues[j] = sobj.getNumericValue(satt,this) + "";
          }
		
	     if (alldefined)
	     { sattvalueMap.put(satt,sattvalues); 
	       sourceattributes.add(satt); 
	     }  
		    // check satt + satt1 = tatt, etc
	    } 
	    else if (satt.isString())
	    { String[] sattvalues = new String[en]; 
   	  
          boolean allempty = true; 
		
          for (int j = 0; j < en; j++) 
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification sobj = (ObjectSpecification) pair.get(0); // 0
            // if (sobj.hasDefinedValue(sattname)) { } 
		    // else 
  		    // { alldefined = false; } 
            Object vv = sobj.getValueOf(satt,this); 
			// System.out.println(">>> valueof " + sobj + "." + satt + " = " + vv); 
            if (vv != null && (vv instanceof String) && ((String) vv).startsWith("\"") && ((String) vv).endsWith("\""))
            { sattvalues[j] = ((String) vv).substring(1,((String) vv).length()-1); } 
            else 
            { sattvalues[j] = "" + vv; }
			
			if (sattvalues[j].length() > 0)
			{ allempty = false; }
          }
		
          if (allempty) { } 
          else 
          { sattvalueMap.put(satt,sattvalues); 
            sourceattributes.add(satt); 
          } 
        }
        else if (satt.isStringCollection())
        { Vector[] sattvalues = new Vector[en]; 
		
          boolean allempty = true; 
		  
          for (int j = 0; j < en; j++)
          { Vector pair = (Vector) pairs.get(j); 
            ObjectSpecification srcobj = (ObjectSpecification) pair.get(0); // 0
            Vector srcvect = srcobj.getCollectionValue(satt,this);
            sattvalues[j] = srcvect;
			System.out.println(">>> Value of (" + srcobj + ")." + satt + " = " + srcvect); 
			
			if (srcvect.size() > 0)
			{ allempty = false; } 
          }  
           
		  if (allempty) { } 
		  else 
		  { sattvalueMap.put(satt,sattvalues); 
		    sourceattributes.add(satt); 
		  } 
		}
      }

      Expression cexpr = composedStringFunction(tatt,sourceattributes,sattvalueMap,tattvalues,tvalues,tms); 
      if (cexpr != null) 
      { System.out.println(">>> Composed string function " + cexpr + " |--> " + tatt); 
        AttributeMatching amx = new AttributeMatching(cexpr, tatt);
        emx.addMapping(amx);  
        res.add(amx); 
      }
    }
	return res; 
  } 
  
  public Vector identifyQueryFunctions(Entity tent, Vector tobjs, Vector tms)
  { int en = tobjs.size();  
    if (en <= 1)
    { return null; }
	
	// Only really for $text : String, representing the concrete syntax of the target object
	// Future work will extend to  $q : Sequence(T) and Set(T)
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify definitions of target query functions $tatt for " + tent); 
    System.out.println(); 

    Vector sattributes = new Vector(); 

    Vector s1atts = tent.allDefinedProperties();
    Vector s2atts = tent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts);
    if (maxTargetPath > 1) 
    { sattributes.addAll(s2atts); }  
	
    System.out.println(">> All source attributes are: " + sattributes); 
     
    Vector tattributes = tent.allQueryOperationProperties(); 
	// Vector othertattributes = new Vector(); 
	// othertattributes.addAll(tattributes); 
	
    Vector res = new Vector();
	 
    for (int i = 0; i < tattributes.size(); i++)
    { Attribute tatt = (Attribute) tattributes.get(i);
      String tattname = tatt.getName();
      System.out.println(">> " + tent + " target query function: " + tattname);
	  
      if (tatt.isString()) //  && emx.isUnusedTargetByName(tatt)) 
      { Vector qattributes = new Vector(); 
        qattributes.add(tatt); 
        sattributes.removeAll(qattributes); 
      }
      else 
      { continue; } 
	  
      String[] tattvalues = new String[en]; 
      Vector tvalues = new Vector(); 
   	  
      boolean alltdefined = true; 
		
      for (int j = 0; j < en; j++) 
      { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
        if (tobj.hasDefinedValue(tattname)) { } 
        else 
        { alltdefined = false; } 
        tattvalues[j] = tobj.getString(tattname);
        tvalues.add(tattvalues[j]);  
      }
	  
      if (alltdefined) { } 
      else 
      { System.err.println("! Not every target instance has a " + tattname + " value"); 
        continue; 
      }
		
      System.out.println(); 
      System.out.println(">> Undefined target query function: " + tattname);
      System.out.println(">> Target values are: " + tvalues); 
      System.out.println(); 
	   
      Vector satts = Entity.typeCompatibleFeatures(tatt, sattributes); // sattributes,emx,ems); 
      boolean found = false; 
     
      System.out.println(">> Type-compatible source attributes are: " + satts); 
     
	  
      Vector sourceattributes = new Vector();  // attributes considered for combination into tatt
      java.util.Map sattvalueMap = new java.util.HashMap();  // for each satt in sourceattributes, its values for sobjs of pairs
	  
      for (int k = 0; k < satts.size(); k++) 
      { Attribute satt = (Attribute) satts.get(k);
        String sattname = satt.getName(); 
	 
        if (satt.isNumeric())
        { String[] sattvalues = new String[en];
          boolean alldefined = true; 
		        
          for (int j = 0; j < en; j++) 
          { ObjectSpecification sobj = (ObjectSpecification) tobjs.get(j);  // 0 
            // if (sobj.hasDefinedValue(satt,this)) { } 
            // else 
            // { alldefined = false; } 
            sattvalues[j] = sobj.getNumericValue(satt,this) + "";
          }
		
          if (alldefined)
          { sattvalueMap.put(satt,sattvalues); 
            sourceattributes.add(satt); 
          }  
		    // check satt + satt1 = tatt, etc
        } 
        else if (satt.isString())
        { String[] sattvalues = new String[en]; 
   	  
          boolean allempty = true; 
		
          for (int j = 0; j < en; j++) 
          { ObjectSpecification sobj = (ObjectSpecification) tobjs.get(j); // 0
               // if (sobj.hasDefinedValue(sattname)) { } 
		    // else 
  		    // { alldefined = false; } 
            Object vv = sobj.getValueOf(satt,this); 
			// System.out.println(">>> valueof " + sobj + "." + satt + " = " + vv); 
            if (vv != null && (vv instanceof String) && ((String) vv).startsWith("\"") && ((String) vv).endsWith("\""))
            { sattvalues[j] = ((String) vv).substring(1,((String) vv).length()-1); } 
            else 
            { sattvalues[j] = "" + vv; }
			
            if (sattvalues[j].length() > 0)
            { allempty = false; }
          }
		
          if (allempty) { } 
          else 
          { sattvalueMap.put(satt,sattvalues); 
            sourceattributes.add(satt); 
          } 
        }
        else if (satt.isStringCollection())
        { Vector[] sattvalues = new Vector[en]; 
		
          boolean allempty = true; 
		  
          for (int j = 0; j < en; j++)
          { ObjectSpecification srcobj = (ObjectSpecification) tobjs.get(j); 
            Vector srcvect = srcobj.getCollectionValue(satt,this);
            sattvalues[j] = srcvect;
            System.out.println(">>> Value of (" + srcobj + ")." + satt + " = " + srcvect); 
			
            if (srcvect.size() > 0)
            { allempty = false; } 
          }  
           
          if (allempty) { } 
          else 
          { sattvalueMap.put(satt,sattvalues); 
            sourceattributes.add(satt); 
          } 
        }
        else if (satt.isEntityCollection())
        { Vector[] sattvalues = new Vector[en]; 
		
          boolean allempty = true; 
		  
          for (int j = 0; j < en; j++)
          { ObjectSpecification srcobj = (ObjectSpecification) tobjs.get(j); 
            Vector srcvect = srcobj.getCollectionValue(satt,this);
            sattvalues[j] = new Vector(); 
            for (int p = 0; p < srcvect.size(); p++) 
            { ObjectSpecification sx = (ObjectSpecification) srcvect.get(p); 
              String val = sx.getString("toString"); 
              sattvalues[j].add(val); 
            }
			
            System.out.println(">>> Value of (" + srcobj + ")." + satt + ".toString = " + sattvalues[j]); 
			
            if (srcvect.size() > 0)
            { allempty = false; } 
          }  
           
          if (allempty) { } 
          else 
          { sattvalueMap.put(satt,sattvalues); 
            sourceattributes.add(satt); 
          } 
        }
      }

      Expression cexpr = composedStringFunction(tatt,sourceattributes,sattvalueMap,tattvalues,tvalues,tms); 
      if (cexpr != null) 
      { System.out.println(">>> Composed function " + cexpr + " |--> " + tatt); 
        // AttributeMatching amx = new AttributeMatching(cexpr, tatt);
		// emx.addMapping(amx);  
        // res.add(amx); 
		tent.addQueryOperation(tatt,cexpr); 
      }
    }
    return res; 
  } 

  public Expression composedStringFunction(Attribute tatt, Vector sourceatts, java.util.Map sattvalueMap, 
    String[] targetValues, Vector tvalues, Vector tms)
  { // check what combinations of sourceatts values compose to tatt's values targetValues (tvalues)

	System.out.println(">> Checking all combination functions based on source attributes: " + sourceatts); 
  
    // Are the targetvalues constant? If so, return the function  K |--> tatt
    if (AuxMath.isConstant(targetValues))
    { String constv = targetValues[0]; 
      System.out.println(">> Constant String function \"" + constv + "\" |--> " + tatt); 
		  
      return new BasicExpression("\"" + constv + "\""); 
    }
	
	
    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
      // check if tatt = satt, if tatt = K + satt, tatt = satt + K, etc. 
	  
      if (satt.isCollection()) 
      { continue; }  // we try these later.  
	  
      String[] sattvalues = (String[]) sattvalueMap.get(satt); 

      if (AuxMath.isCopy(sattvalues,targetValues)) 
      { BasicExpression sattbe = new BasicExpression(satt); 
        sattbe.setUmlKind(Expression.ATTRIBUTE);
        return sattbe;  
      }
      else if (AuxMath.isUpperCased(sattvalues,targetValues)) 
      { BasicExpression sattbe = new BasicExpression(satt); 
        sattbe.setUmlKind(Expression.ATTRIBUTE);
        UnaryExpression upperc = new UnaryExpression("->toUpperCase", sattbe); 
        upperc.setType(new Type("String", null)); 
        upperc.setElementType(new Type("String", null)); 
        return upperc;  
      }
      else if (AuxMath.isLowerCased(sattvalues,targetValues)) 
      { BasicExpression sattbe = new BasicExpression(satt); 
        sattbe.setUmlKind(Expression.ATTRIBUTE);
        UnaryExpression lowerc = new UnaryExpression("->toLowerCase", sattbe); 
        lowerc.setType(new Type("String", null)); 
        lowerc.setElementType(new Type("String", null)); 
        return lowerc;  
      }
      else if (AuxMath.isSuffixed(sattvalues,targetValues))
      { System.out.println(">> Suffix feature mapping " + satt + " + ??? |--> " + tatt); 
        Vector remd = AuxMath.removePrefix(targetValues,sattvalues); 
        System.out.println(">> removed the " + satt + " prefixes: " + remd);
        System.out.println(); 
		
        int vlen = targetValues.length; 
        String[] newTargetValues = new String[vlen];  
        for (int j = 0; j < vlen && j < remd.size(); j++) 
        { newTargetValues[j] = (String) remd.get(j); }
	  
        Expression subexpr = composedStringFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd,tms); 
	  
        if (subexpr != null)
        { BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
          BinaryExpression addsuff = new BinaryExpression("+", sattbe, subexpr); 
          addsuff.setType(new Type("String", null));  
          addsuff.setElementType(new Type("String", null));
          return addsuff; 
        }    
      } 
      else if (AuxMath.isPrefixed(sattvalues,targetValues))
      { System.out.println(">> Prefix feature mapping ??? + " + satt + " |--> " + tatt); 
        Vector remd = AuxMath.removeSuffix(targetValues,sattvalues); 
        System.out.println(">> removed the " + satt + " suffixes: " + remd);
        System.out.println(); 
		
        int vlen = targetValues.length; 
        String[] newTargetValues = new String[vlen];  
        for (int j = 0; j < vlen && j < remd.size(); j++) 
        { newTargetValues[j] = (String) remd.get(j); }
	  
        Expression subexpr = composedStringFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd,tms); 
	  
        if (subexpr != null)
        { BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
          BinaryExpression addsuff = new BinaryExpression("+",subexpr,sattbe);   
          addsuff.setType(new Type("String", null));  
          addsuff.setElementType(new Type("String", null));  
          return addsuff; 
        } 
      }
    }

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
	  // check if tatt = satt, if tatt = K + satt, tatt = satt + K, etc. 
	  
      if (satt.isCollection())   
      { Vector[] sattvalues = (Vector[]) sattvalueMap.get(satt); 
	    // sattvalues are Vectors of Strings. Check if tatt is a sum of these, or a separator-sum
	    
        if (AuxMath.isStringSum(sattvalues,targetValues))
        { BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
          UnaryExpression sumsatt = new UnaryExpression("->sum",sattbe);   
          sumsatt.setType(new Type("String", null));  
          sumsatt.setElementType(new Type("String", null));  
          return sumsatt;
        }
    
        int en = targetValues.length; 
        String[] newtvalues = new String[en]; 
		 
        String K = AuxMath.initialSeparatorStringSum(sattvalues,targetValues,newtvalues); 
         if (K != null) 
         { BasicExpression Kbe = new BasicExpression("\"" + K + "\""); 
           Kbe.setUmlKind(Expression.VALUE);
           Kbe.setType(new Type("String", null));  
          
           BasicExpression sattbe = new BasicExpression(satt); 
           sattbe.setUmlKind(Expression.ATTRIBUTE);
           BinaryExpression sumsatt = new BinaryExpression("->separatorSum",sattbe,Kbe);   
           sumsatt.setType(new Type("String", null));  
           sumsatt.setElementType(new Type("String", null));  
		   
           Vector tailtvalues = new Vector();
           boolean allEmpty = true;  
           for (int q = 0; q < en; q++)
           { tailtvalues.add(newtvalues[q]); 
             if (newtvalues[q] != null && newtvalues[q].length() > 0) 
             { allEmpty = false; }
           } 
		   
           if (allEmpty) // no tail
           { return sumsatt; }
		    
           Expression subexpr = composedStringFunction(tatt,sourceatts,sattvalueMap,newtvalues,tailtvalues,tms); 
	  
           if (subexpr != null)
           { Expression res = new BinaryExpression("+", sumsatt, subexpr); 
             res.setType(new Type("String", null));  
             res.setElementType(new Type("String", null));
             return res; 
           }  
         } 	
       }
     }

    // Remove common suffixes and prefixes one token at a time. 
	
    // Do targetValues have a common prefix? (a token) 
    // If so, remove it and recursively check the 
    // remainder of the targetValues

    String cpref = AuxMath.longestCommonPrefix(tvalues); 
    System.out.println(">> Longest common prefix of target values = " + cpref); 
    if (cpref != null && cpref.length() > 0)
    { // System.out.println(">> feature mapping " + sattname + "->before(\"" + cpref + "\") |--> " + tattname); 
      // BasicExpression sattbe = new BasicExpression(satt); 
      // sattbe.setUmlKind(Expression.ATTRIBUTE);
      // Remove the common prefix from the targetValues and tattvalues and recurse, prepend the prefix to the result:

      Vector removedtvalues = AuxMath.removeCommonPrefix(tvalues,cpref); 
      System.out.println(">> removed prefix " + cpref + " of target values = " + removedtvalues); 
       
      int vlen = targetValues.length; 
      String[] newTargetValues = new String[vlen];  

      for (int i = 0; i < vlen && i < removedtvalues.size(); i++) 
      { newTargetValues[i] = (String) removedtvalues.get(i); }
	  
	  Expression subexpr = composedStringFunction(tatt,sourceatts,sattvalueMap,newTargetValues,removedtvalues,tms); 
	  
       if (subexpr != null)
       { BasicExpression prefexpr = new BasicExpression("\"" + cpref + "\"");  
         prefexpr.setType(new Type("String", null));  
         prefexpr.setElementType(new Type("String", null));  
         Expression res = new BinaryExpression("+", prefexpr, subexpr); 
         res.setType(new Type("String", null));  
         res.setElementType(new Type("String", null));
         return res; 
       }  
     }
    // Do targetValues have a common suffix? If so, remove it and recursively check the remainder of the targetValues
	
    String csuff = AuxMath.longestCommonSuffix(tvalues); 
    System.out.println(">> Longest common suffix = " + csuff); 
    if (csuff != null && csuff.length() > 0)
    { // System.out.println(">> feature mapping " + sattname + "->before(\"" + cpref + "\") |--> " + tattname); 
      // BasicExpression sattbe = new BasicExpression(satt); 
      // sattbe.setUmlKind(Expression.ATTRIBUTE);
      Vector removedtvalues = AuxMath.removeCommonSuffix(tvalues,csuff);
	  int vlen = targetValues.length; 
  	  String[] newTargetValues = new String[vlen];  
	  for (int i = 0; i < vlen && i < removedtvalues.size(); i++) 
	  { newTargetValues[i] = (String) removedtvalues.get(i); }
	   
	  System.out.println(">> removed suffix of target values = " + removedtvalues); 
      Expression subexpr = composedStringFunction(tatt,sourceatts,sattvalueMap,newTargetValues,removedtvalues,tms); 
	  
	 if (subexpr != null)
      { BasicExpression suffexpr = new BasicExpression("\"" + csuff + "\""); 
        suffexpr.setType(new Type("String", null));  
        suffexpr.setElementType(new Type("String", null));
        Expression res = new BinaryExpression("+", subexpr, suffexpr); 
        res.setType(new Type("String", null));  
        res.setElementType(new Type("String", null));
        return res; 
      }  
    } 

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
	  // check if tatt = f(satt) for custom function, either already defined or new

      if (satt.isCollection()) 
      { continue; }
	  
      String[] sattvalues = (String[]) sattvalueMap.get(satt); 
      int en = sattvalues.length; 
	  	   
      if (AuxMath.isFunctional(sattvalues,targetValues))
      { System.out.println(">>>> Create or reuse a specific string-to-string function for " + satt + " to " + tatt + " conversion?"); 

        for (int mk = 0; mk < en; mk++) 
        { System.out.println("  " + sattvalues[mk] + " |--> " + targetValues[mk]); }
          Entity sent = satt.getOwner(); 
          Entity tent = tatt.getOwner(); 
		
          String fname = "f" + sent + satt.underscoreName() + "2" + tent + tatt.underscoreName();
		
		TypeMatching newfunction = null; 
		TypeMatching tm = TypeMatching.lookupByName(fname,tms); 
		if (tm != null) 
           { System.out.println(">> Use this existing type matching?: " + tm); 
             String yn = 
                   JOptionPane.showInputDialog("Use the String-to-String function " + fname + "? (y/n):");
             if (yn != null && "y".equals(yn))
             { newfunction = tm; 
               BasicExpression fsatt = new BasicExpression(fname);
               fsatt.setUmlKind(Expression.QUERY);  
               BasicExpression sattbe = new BasicExpression(satt); 
               sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
               fsatt.addParameter(sattbe); 
               fsatt.setType(new Type("String", null)); 
               fsatt.setElementType(new Type("String", null)); 
               return fsatt; 
             }  
           } 
		
           String yn2 = 
              JOptionPane.showInputDialog("Create a custom String-to-String function " + fname + "? (y/n):");

        if (yn2 != null && "y".equals(yn2))
        { newfunction = new TypeMatching(satt.getType(), tatt.getType()); 
          newfunction.setName(fname); 
          newfunction.setStringValues(sattvalues,targetValues);
          System.out.println(">>> New function: " + newfunction);
          BasicExpression fsatt = new BasicExpression(fname);
          fsatt.setUmlKind(Expression.QUERY);  
          BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
		      
          fsatt.addParameter(sattbe); 
          fsatt.setType(new Type("String", null)); 
          fsatt.setElementType(new Type("String", null)); 
          tms.add(newfunction);
          return fsatt; 
        }
      } 
    }  	
    return null; 
  }

  public Vector identifyComposedSequenceFunctions(Attribute tatt, Entity sent, Entity tent, Vector pairs, EntityMatching emx, Vector ems)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    String tattname = tatt.getName(); 
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify composite sequence function to sequence-valued " + tent + "::" + tatt); 
    System.out.println(); 

    Vector sobjs = new Vector(); 
    Vector tobjs = new Vector(); 

    for (int j = 0; j < en; j++) 
    { Vector pair = (Vector) pairs.get(j); 
      ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
      ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
      sobjs.add(sobj); 
      tobjs.add(tobj); 
    } 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1)
    { sattributes.addAll(s2atts); } 

    Attribute selfatt = new Attribute("self", new Type(sent), ModelElement.INTERNAL); 
    selfatt.setType(new Type(sent)); 
    selfatt.setElementType(new Type(sent)); 
    sattributes.add(selfatt); 
	
    System.out.println(">> All source attributes are: " + sattributes); 
     	
    Vector res = new Vector();
	  
    Vector[] tattvalues = new Vector[en]; 
    Vector tvalues = new Vector(); 
   	  
    boolean alltdefined = true; 
		
    for (int j = 0; j < en; j++) 
    { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
      if (tobj.hasDefinedValue(tattname)) { } 
      else 
      { alltdefined = false; } 
      tattvalues[j] = tobj.getCollectionValue(tatt,this);
      tvalues.add(tattvalues[j]);  
    }
	  
    if (alltdefined) { } 
    else 
    { System.err.println("! Not every target instance has a " + tattname + " value"); 
      return res; 
    }
		
    System.out.println(); 
    System.out.println(">> Target values of " + tatt + " are: " + tvalues); 
    System.out.println(); 
	   
    Vector satts = ModelMatching.findCompatibleSourceAttributes(tatt,sattributes,emx,ems); 
    boolean found = false; 
     
    System.out.println(">> Type-compatible source attributes for " + tatt + " are: " + satts); 
     
	  
    Vector sourceattributes = new Vector();  // attributes considered for combination into tatt
    java.util.Map sattvalueMap = new java.util.HashMap();  // for each satt in sourceattributes, its values for sobjs of pairs
	  
    for (int k = 0; k < satts.size(); k++) 
    { Attribute satt = (Attribute) satts.get(k);
      String sattname = satt.getName(); 
	 
      if (satt.isEntity())
      { ObjectSpecification[] sattvalues = new ObjectSpecification[en]; 
		
        boolean allempty = true; 
		  
        for (int j = 0; j < en; j++)
        { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(j); 
          ObjectSpecification srcvect = srcobj.getObjectValue(satt,this);
          sattvalues[j] = srcvect; 
			
          // System.out.println(">>> Value of (" + srcobj + ")." + satt + " = " + sattvalues[j]);   
        }  
           
        sattvalueMap.put(satt,sattvalues); 
        sourceattributes.add(satt); 
      } 
      else if (satt.isEntityCollection() && satt.isSequence())
      { Vector[] sattvalues = new Vector[en]; 
		
        boolean allempty = true; 
		  
        for (int j = 0; j < en; j++)
        { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(j); 
          Vector srcvect = srcobj.getCollectionValue(satt,this);
          sattvalues[j] = new Vector(); 
          for (int p = 0; p < srcvect.size(); p++) 
          { ObjectSpecification sx = (ObjectSpecification) srcvect.get(p); 
            sattvalues[j].add(sx); 
          }
			
          System.out.println(">>> Value of (" + srcobj + ")." + satt + " = " + sattvalues[j]); 
			
          if (srcvect.size() > 0)
          { allempty = false; } 
        }  
           
        if (allempty) { } 
        else 
        { sattvalueMap.put(satt,sattvalues); 
          sourceattributes.add(satt); 
        } 
      }
    }


    Expression cexpr = 
       composedSequenceFunction(tatt,sourceattributes,
                      sattvalueMap,tattvalues,tvalues); 
    if (cexpr != null) 
    { System.out.println(">>> Composed sequence function " + cexpr + " |--> " + tatt); 
      Vector auxvariables = new Vector(); 
      Attribute var = new Attribute(Identifier.nextIdentifier("var$"),cexpr.getElementType(),
                                  ModelElement.INTERNAL);
      var.setElementType(cexpr.getElementType());
      auxvariables.add(var);   
      AttributeMatching amx = new AttributeMatching(cexpr, tatt, var, auxvariables); 
        // emx.addMapping(amx);  
      res.add(amx); 
        // tent.addQueryOperation(tatt,cexpr); 
    }
    
    return res; 
  } 

  public Expression composedSequenceFunction(Attribute tatt, Vector sourceatts, java.util.Map sattvalueMap,     
         Vector[] targetValues, Vector tvalues)
  { // check what combinations of sourceatts values compose to tatt's values targetValues (tvalues)

    System.out.println(">> Checking all combination functions based on source attributes: " + sourceatts); 
  
    // Are the targetvalues constant? If so, return the function  K |--> tatt
    if (AuxMath.isConstantSequences(targetValues))
    { Vector constv = targetValues[0]; 
      System.out.println(">> Constant sequence function " + constv + " |--> " + tatt); 
		  
      return new SetExpression(constv,true); 
    }

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
      // check if tatt = satt, if tatt = K + satt, tatt = satt + K, etc. 
	  
      if (satt.isCollection()) 
      { continue; }  // we try these later.  
	  
      ObjectSpecification[] sattvalues = (ObjectSpecification[]) sattvalueMap.get(satt); 

      if (AuxMath.isSingletonSequences(sattvalues,targetValues,this)) 
      { BasicExpression sattbe = new BasicExpression(satt); 
        sattbe.setUmlKind(Expression.ATTRIBUTE);
        SetExpression sexpr = new SetExpression();
        sexpr.setOrdered(true);  
        sexpr.addElement(sattbe); 
        System.out.println(">> Singleton sequence mapping Sequence{" + satt + "} |--> " + tatt); 
        return sexpr;  
      }
      else if (AuxMath.isPrependSequences(sattvalues,targetValues,this))
      { System.out.println(">> Prepend sequence mapping Sequence{" + satt + "} ^ ??? |--> " + tatt); 
        Vector remd = AuxMath.tailSequences(targetValues); 
     
        System.out.println(">> removed the " + satt + " suffixes, leaving remainders: " + remd);
        System.out.println(); 
		
        int vlen = targetValues.length; 
        Vector[] newTargetValues = new Vector[vlen];  
        for (int j = 0; j < vlen && j < remd.size(); j++) 
        { newTargetValues[j] = (Vector) remd.get(j); }
	
        Expression subexpr = composedSequenceFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd); 
	  
        if (subexpr != null)
        { BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
          BinaryExpression addsuff = new BinaryExpression("->prepend", subexpr, sattbe); 
          addsuff.setType(new Type("Sequence", null));  
          addsuff.setElementType(satt.getElementType());
          return addsuff; 
        }    
      }
      else if (AuxMath.isAppendSequences(sattvalues,targetValues,this))
      { System.out.println(">> Appended sequences mapping ??? ^ Sequence{" + satt + "} |--> " + tatt); 
        Vector remd = AuxMath.frontSequences(targetValues); 
        System.out.println(">> removed the " + satt + " suffixes, leaving remainders: " + remd);
        System.out.println(); 
		
        int vlen = targetValues.length; 
        Vector[] newTargetValues = new Vector[vlen];  
        for (int j = 0; j < vlen && j < remd.size(); j++) 
        { newTargetValues[j] = (Vector) remd.get(j); }
	  
        Expression subexpr = composedSequenceFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd); 
	  
        if (subexpr != null)
        { BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
          BinaryExpression addsuff = new BinaryExpression("->append",subexpr,sattbe);   
          addsuff.setType(new Type("Sequence", null));  
          addsuff.setElementType(satt.getElementType());  
          return addsuff; 
        } 
      }
    } 

    System.out.println(">>> Checking ->union compositions for target " + tatt); 

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
      // check if tatt = satt, if tatt = K + satt, tatt = satt + K, etc. 
	  
      if (satt.isCollection()) 
      { Vector[] sattvalues = (Vector[]) sattvalueMap.get(satt); 

        if (AuxMath.isCopySequences(sattvalues,targetValues,this)) 
        { System.out.println(">> Copy sequence mapping " + satt + " |--> " + tatt);
 
          BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
          return sattbe;  
        }
        else if (AuxMath.isSuffixedSequences(sattvalues,targetValues,this))
        { System.out.println(">> Suffixed sequence mapping " + satt + " ^ ??? |--> " + tatt); 

          Vector remd = AuxMath.removePrefixSequences(targetValues,sattvalues); 
          System.out.println(">> removed the " + satt + " prefixes, leaving remainders: " + remd);
          System.out.println(); 
		
          int vlen = targetValues.length; 
          Vector[] newTargetValues = new Vector[vlen];  
          for (int j = 0; j < vlen && j < remd.size(); j++) 
          { newTargetValues[j] = (Vector) remd.get(j); }
	  
          Expression subexpr = composedSequenceFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd); 
	  
          if (subexpr != null)
          { BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE);
            BinaryExpression addsuff = new BinaryExpression("->union", sattbe, subexpr); 
            addsuff.setType(new Type("Sequence", null));  
            addsuff.setElementType(satt.getElementType());
            return addsuff; 
          }    
        }
        else if (AuxMath.isPrefixedSequences(sattvalues,targetValues,this))
        { System.out.println(">> Prefixed sequences mapping ??? ^ " + satt + " |--> " + tatt); 

          Vector remd = AuxMath.removeSuffixSequences(targetValues,sattvalues); 
          System.out.println(">> removed the " + satt + " suffixes, leaving remainders: " + remd);
          System.out.println(); 
		
          int vlen = targetValues.length; 
          Vector[] newTargetValues = new Vector[vlen];  
          for (int j = 0; j < vlen && j < remd.size(); j++) 
          { newTargetValues[j] = (Vector) remd.get(j); }
	  
          Expression subexpr = composedSequenceFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd); 
	  
          if (subexpr != null)
          { BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE);
            BinaryExpression addsuff = new BinaryExpression("->union",subexpr,sattbe);   
            addsuff.setType(new Type("Sequence", null));  
            addsuff.setElementType(satt.getElementType());  
            return addsuff; 
          }
        }  
      }
    } 
    
    return null; 
  } 

  public Vector identifyComposedSetFunctions(Attribute tatt, Entity sent, Entity tent, Vector pairs, EntityMatching emx, Vector ems)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    String tattname = tatt.getName(); 
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify composite set function to set-valued " + tent + "::" + tatt); 
    System.out.println(); 

    Vector sobjs = new Vector(); 
    Vector tobjs = new Vector(); 

    for (int j = 0; j < en; j++) 
    { Vector pair = (Vector) pairs.get(j); 
      ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
      ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
      sobjs.add(sobj); 
      tobjs.add(tobj); 
    } 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1)
    { sattributes.addAll(s2atts); }
 
    Attribute selfatt = new Attribute("self", new Type(sent), ModelElement.INTERNAL); 
    selfatt.setType(new Type(sent)); 
    selfatt.setElementType(new Type(sent)); 
    sattributes.add(selfatt); 
	
    System.out.println(">> All source attributes are: " + sattributes); 
     	
    Vector res = new Vector();
	  
    Vector[] tattvalues = new Vector[en]; 
    Vector tvalues = new Vector(); 
   	  
    boolean alltdefined = true; 
		
    for (int j = 0; j < en; j++) 
    { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
      if (tobj.hasDefinedValue(tattname)) { } 
      else 
      { alltdefined = false; } 
      tattvalues[j] = tobj.getCollectionValue(tatt,this);
      tvalues.add(tattvalues[j]);  
    }
	  
    if (alltdefined) { } 
    else 
    { System.err.println("! Not every target instance has a " + tattname + " value"); 
      return res; 
    }
		
    System.out.println(); 
    System.out.println(">> Target values of " + tatt + " are: " + tvalues); 
    System.out.println(); 
	   
    Vector satts = ModelMatching.findCompatibleSourceAttributes(tatt,sattributes,emx,ems); 
    boolean found = false; 
     
    System.out.println(">> Type-compatible source attributes for " + tatt + " are: " + satts); 
     
	  
    Vector sourceattributes = new Vector();  // attributes considered for combination into tatt
    java.util.Map sattvalueMap = new java.util.HashMap();  // for each satt in sourceattributes, its values for sobjs of pairs
	  
    for (int k = 0; k < satts.size(); k++) 
    { Attribute satt = (Attribute) satts.get(k);
      String sattname = satt.getName(); 
	 
      if (satt.isEntity())
      { ObjectSpecification[] sattvalues = new ObjectSpecification[en]; 
		
        boolean allempty = true; 
		  
        for (int j = 0; j < en; j++)
        { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(j); 
          ObjectSpecification srcvect = srcobj.getObjectValue(satt,this);
          sattvalues[j] = srcvect; 
			
          // System.out.println(">>> Value of (" + srcobj + ")." + satt + " = " + sattvalues[j]);   
        }  
           
        sattvalueMap.put(satt,sattvalues); 
        sourceattributes.add(satt); 
      } 
      else if (satt.isEntityCollection())
      { Vector[] sattvalues = new Vector[en]; 
		
        boolean allempty = true; 
		  
        for (int j = 0; j < en; j++)
        { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(j); 
          Vector srcvect = srcobj.getCollectionValue(satt,this);
          sattvalues[j] = new Vector(); 
          for (int p = 0; p < srcvect.size(); p++) 
          { ObjectSpecification sx = (ObjectSpecification) srcvect.get(p); 
            sattvalues[j].add(sx); 
          }
			
          System.out.println(">>> Value of (" + srcobj + ")." + satt + " = " + sattvalues[j]); 
			
          if (srcvect.size() > 0)
          { allempty = false; } 
        }  
           
        if (allempty) { } 
        else 
        { sattvalueMap.put(satt,sattvalues); 
          sourceattributes.add(satt); 
        } 
      }
    }


    Expression cexpr = 
       composedSetFunction(tatt,sourceattributes,
                      sattvalueMap,tattvalues,tvalues); 
    if (cexpr != null) 
    { System.out.println(">>> Composed Set function " + cexpr + " |--> " + tatt); 
      Vector auxvariables = new Vector(); 
      Attribute var = new Attribute(Identifier.nextIdentifier("var$"),cexpr.getElementType(),
                                  ModelElement.INTERNAL);
      var.setElementType(cexpr.getElementType());
      auxvariables.add(var);   
      AttributeMatching amx = new AttributeMatching(cexpr, tatt, var, auxvariables); 
        // emx.addMapping(amx);  
      res.add(amx); 
        // tent.addQueryOperation(tatt,cexpr); 
    }
    
    return res; 
  } 

  public Expression composedSetFunction(Attribute tatt, Vector sourceatts, java.util.Map sattvalueMap,     
         Vector[] targetValues, Vector tvalues)
  { // check what combinations of sourceatts values compose to tatt's values targetValues (tvalues)

    System.out.println(">> Checking all combination functions based on source attributes: " + sourceatts); 
  
    // Are the targetvalues constant? If so, return the function  K |--> tatt
    if (AuxMath.isConstantSets(targetValues))
    { Vector constv = targetValues[0]; 
      System.out.println(">> Constant set function " + constv + " |--> " + tatt); 
		  
      return new SetExpression(constv,false); 
    }

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
      // check if tatt = satt, if tatt = K + satt, tatt = satt + K, etc. 
	  
      if (satt.isCollection()) 
      { continue; }  // we try these later.  
	  
      ObjectSpecification[] sattvalues = (ObjectSpecification[]) sattvalueMap.get(satt); 

      if (AuxMath.isSingletonSequences(sattvalues,targetValues,this)) 
      { BasicExpression sattbe = new BasicExpression(satt); 
        sattbe.setUmlKind(Expression.ATTRIBUTE);
        SetExpression sexpr = new SetExpression();
        sexpr.setOrdered(false);  
        sexpr.addElement(sattbe); 
        System.out.println(">> Singleton set mapping Set{" + satt + "} |--> " + tatt); 
        return sexpr;  
      }
      else if (AuxMath.allIncludes(targetValues,tatt.getElementType(),sattvalues,this))
      { System.out.println(">> Inclusion set mapping ???->including(" + satt + ") |--> " + tatt); 
        Vector remd = AuxMath.removeElements(targetValues,sattvalues,this); 
     
        System.out.println(">> removed the " + satt + " elements, leaving remainders: " + remd);
        System.out.println(); 
		
        boolean changed = false; 
        int vlen = targetValues.length; 
        Vector[] newTargetValues = new Vector[vlen];  
        for (int j = 0; j < vlen && j < remd.size(); j++) 
        { newTargetValues[j] = (Vector) remd.get(j); 
          if (newTargetValues[j].size() != targetValues[j].size())
          { changed = true; }
        } 
	
        if (changed == true)
        { Expression subexpr = composedSetFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd); 
	  
          if (subexpr != null)
          { BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE);
            BinaryExpression addsuff = new BinaryExpression("->including", subexpr, sattbe); 
            addsuff.setType(new Type("Set", null));  
            addsuff.setElementType(satt.getElementType());
            return addsuff; 
          }   
        }  
      }
    } 

    System.out.println(">>> Checking ->union compositions for target " + tatt); 

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
      // check if tatt = satt, if tatt = K + satt, tatt = satt + K, etc. 
	  
      if (satt.isCollection()) 
      { Vector[] sattvalues = (Vector[]) sattvalueMap.get(satt); 

        if (AuxMath.isCopySets(sattvalues,targetValues,this)) 
        { System.out.println(">> Copy set mapping " + satt + " |--> " + tatt);
 
          BasicExpression sattbe = new BasicExpression(satt); 
          sattbe.setUmlKind(Expression.ATTRIBUTE);
          return sattbe;  
        }
        else if (AuxMath.allSubsets(sattvalues,targetValues,this))
        { System.out.println(">> Union set mapping ???->union(" + satt + ") |--> " + tatt); 

          Vector remd = AuxMath.removeSubsets(targetValues,sattvalues,this); 
          System.out.println(">> removed the " + satt + " subsets, leaving remainders: " + remd);
          System.out.println(); 
		
          int vlen = targetValues.length; 
          Vector[] newTargetValues = new Vector[vlen];  
          for (int j = 0; j < vlen && j < remd.size(); j++) 
          { newTargetValues[j] = (Vector) remd.get(j); }
	  
          Expression subexpr = composedSetFunction(tatt,sourceatts,sattvalueMap,newTargetValues,remd); 
	  
          if (subexpr != null)
          { BasicExpression sattbe = new BasicExpression(satt); 
            sattbe.setUmlKind(Expression.ATTRIBUTE);
            BinaryExpression addsuff = new BinaryExpression("->union", subexpr, sattbe); 
            addsuff.setType(new Type("Set", null));  
            addsuff.setElementType(satt.getElementType());
            return addsuff; 
          }    
        }
      }
    } 
    
    return null; 
  } 

  public Vector identifyComposedTreeFunctions(Attribute tatt, 
    Entity sent, Entity tent, Vector pairs, 
    EntityMatching emx, Vector ems, Vector tms)
  { int en = pairs.size();  
    if (en <= 1)
    { return null; }
	
    String tattname = tatt.getName(); 
	
    System.out.println(); 
    System.out.println(">>>> Trying to identify composite tree function to tree-valued " + tent + "::" + tatt); 
    System.out.println(); 

    Vector sobjs = new Vector(); 
    Vector tobjs = new Vector(); 

    for (int j = 0; j < en; j++) 
    { Vector pair = (Vector) pairs.get(j); 
      ObjectSpecification sobj = (ObjectSpecification) pair.get(0); 
      ObjectSpecification tobj = (ObjectSpecification) pair.get(1); 
      sobjs.add(sobj); 
      tobjs.add(tobj); 
    } 

    Vector sattributes = new Vector(); 

    Vector s1atts = sent.allDefinedProperties();
    Vector s2atts = sent.getNonLocalFeatures(); 
    sattributes.addAll(s1atts); 
    if (maxSourcePath > 1)
    { sattributes.addAll(s2atts); } 

    Attribute selfatt = new Attribute("self", new Type(sent), ModelElement.INTERNAL); 
    selfatt.setType(new Type(sent)); 
    selfatt.setElementType(new Type(sent)); 
    sattributes.add(selfatt); 
	
    System.out.println(">> All source attributes are: " + sattributes); 
     	
    Vector res = new Vector();
	  
    ASTTerm[] tattvalues = new ASTTerm[en]; 
    Vector tvalues = new Vector(); 
   	  
    boolean alltdefined = true; 
		
    for (int j = 0; j < en; j++) 
    { ObjectSpecification tobj = (ObjectSpecification) tobjs.get(j); 
      if (tobj.hasDefinedValue(tattname)) { } 
      else 
      { alltdefined = false; } 
      tattvalues[j] = tobj.getTreeValue(tatt,this);
      tvalues.add(tattvalues[j]);  
    }
	  
    if (alltdefined) { } 
    else 
    { System.err.println("! Not every target instance has a " + tattname + " value"); 
      return res; 
    }
		
    System.out.println(); 
    System.out.println(">> Target values of " + tatt + " are: " + tvalues); 
    System.out.println(); 
	   
    Vector satts = ModelMatching.findCompatibleSourceAttributes(tatt,sattributes,emx,ems); 
    boolean found = false; 
     
    System.out.println(">> Type-compatible source attributes for " + tatt + " are: " + satts); 
     
	  
    Vector sourceattributes = new Vector();  // attributes considered for combination into tatt
    java.util.Map sattvalueMap = new java.util.HashMap();  // for each satt in sourceattributes, its values for sobjs of pairs
	  
    for (int k = 0; k < satts.size(); k++) 
    { Attribute satt = (Attribute) satts.get(k);
      String sattname = satt.getName(); 
	 
      if (satt.isTree())
      { ASTTerm[] sattvalues = new ASTTerm[en]; 
		
        boolean allempty = true; 
		  
        for (int j = 0; j < en; j++)
        { ObjectSpecification srcobj = (ObjectSpecification) sobjs.get(j); 
          ASTTerm srcvect = srcobj.getTreeValue(satt,this);
          sattvalues[j] = srcvect; 
			
          System.out.println(">>> Value of (" + srcobj + ")." + satt + " = " + sattvalues[j]);   
        }  
           
        sattvalueMap.put(satt,sattvalues); 
        sourceattributes.add(satt); 
      } 
    }

    Vector cexprs = new Vector(); 

    AttributeMatching cexpr = 
       composedTreeFunction(sent,tatt,sourceattributes,
                      sattvalueMap,tattvalues,tvalues,
                      tms,cexprs); 
    if (cexpr != null) 
    { System.out.println(">>> Composed tree functions " + cexprs); 
      res.add(cexpr); 
      res.addAll(cexprs); 
    }
  
    return res; 
  } 

  public AttributeMatching composedTreeFunction(Entity sent, 
         Attribute tatt, Vector sourceatts, 
         java.util.Map sattvalueMap,     
         ASTTerm[] targetValues, Vector tvalues, 
         Vector tms, Vector ams)
  { // check what combinations of sourceatts values compose to tatt's values targetValues (tvalues)
    // It is assumed that all source trees of the same category
    // (Entity sent) have the same tag 
    // and arity and non-constant terms (variables _i in CSTL).

    Attribute var_1 = 
        new Attribute("_1", new Type("OclAny",null),
                   ModelElement.INTERNAL);
    Expression var1expr = new BasicExpression(var_1); 
    Attribute var_star = 
        new Attribute("_*", new Type("OclAny",null),
                   ModelElement.INTERNAL);
    Expression varstarexpr = new BasicExpression(var_star); 

    System.out.println(">> Checking all combinations of tree functions based on source attributes: " + sourceatts); 
  
    // Are the targetvalues constant? If so, return the function  K |--> tatt
    if (ASTTerm.constantTrees(targetValues))
    { ASTTerm constv = targetValues[0]; 
      System.out.println(">> Constant tree function " + constv + " |--> " + tatt); 
		  
      Expression targexpr = new BasicExpression(constv);

      if (sourceatts.size() == 1) 
      { Attribute sattr = (Attribute) sourceatts.get(0); 
        ASTTerm[] sattrvalues = 
                 (ASTTerm[]) sattvalueMap.get(sattr);
        if (ASTTerm.constantTrees(sattrvalues))
        { ASTTerm constsv = sattrvalues[0]; 
          Expression srcexpr = 
            new BasicExpression(constsv);
          ams = new Vector();  
          return new AttributeMatching(srcexpr, targexpr); 
        } 
      } 

      // Should be _* |--> constv     
        
      Vector auxvariables = new Vector(); 
      auxvariables.add(varstarexpr);   
      ams = new Vector();  
          
      AttributeMatching amx = 
        new AttributeMatching(varstarexpr, targexpr, 
                              var_star, auxvariables);
      return amx;   
    }

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i);
      // There is normally only one ast variable.  
      // check if tatt = satt, if tatt = (K satt) etc. 
	  	  
      ASTTerm[] sattvalues = 
                 (ASTTerm[]) sattvalueMap.get(satt); 

      if (ASTTerm.equalTrees(sattvalues,targetValues,this)) 
      { Vector idmaps = 
          ASTTerm.createIdentityMappings(
                     sattvalues,targetValues,this); 

        System.out.println(">> Identity mapping _* |-->_*"); 

        AttributeMatching amx = (AttributeMatching) idmaps.get(0); 
        ams.addAll(idmaps); 
        return amx;   
      } // Actually tag:: _* |-->_* for each source tag
      else if (ASTTerm.allSymbolTerms(sattvalues) && 
               ASTTerm.allSymbolTerms(targetValues) &&
               ASTTerm.functionalSymbolMapping(
                            sattvalues,targetValues))
      { String fid = 
          Identifier.nextIdentifier("func"); 
        TypeMatching tm = 
          ASTTerm.createNewFunctionalMapping(
                     fid, sattvalues, targetValues); 
        System.out.println(">> New functional mapping of symbols: " + tm); 
        tms.add(tm);
         
        BasicExpression fexpr = 
                          new BasicExpression(fid); 
        fexpr.setUmlKind(Expression.FUNCTION); 
        fexpr.addParameter(var1expr); 
        ams = new Vector();  
        AttributeMatching amx = 
          new AttributeMatching(var1expr, fexpr);
        return amx;   
      } 
      else if (ASTTerm.allNestedSymbolTerms(sattvalues) && 
               ASTTerm.allSymbolTerms(targetValues) &&
               ASTTerm.functionalSymbolMapping(
                                sattvalues,targetValues))
      { String fid = 
          Identifier.nextIdentifier("func"); 
        TypeMatching tm = 
          ASTTerm.createNewFunctionalMapping(fid, sattvalues, targetValues); 
        System.out.println(">> New nested functional mapping of symbols: " + tm); 
        tms.add(tm);
         
        BasicExpression fexpr = 
                          new BasicExpression(fid); 
        fexpr.setUmlKind(Expression.FUNCTION); 
        fexpr.addParameter(var1expr); 
        ams = new Vector();  
        AttributeMatching amx = 
          new AttributeMatching(var1expr, fexpr);
        return amx;   
      } 
      else if (ASTTerm.sameTag(targetValues) && 
         ASTTerm.singletonTrees(sent, sattvalues,
                                targetValues,this)) 
      { ASTTerm targ0 = targetValues[0]; 
        BasicExpression targexpr = new BasicExpression(targ0);
        Vector newpars = new Vector(); 
        newpars.add(var1expr); 
        targexpr.setParameters(newpars); 
        System.out.println(">> Singleton mapping _1 |--> " + targexpr); 
        ams = new Vector();  
          
        AttributeMatching amx = 
          new AttributeMatching(var1expr, targexpr);
        return amx;   
      } // again, for the source tag:: if any. 
      else if (ASTTerm.sameTag(targetValues) && 
         ASTTerm.sameTails(targetValues) && 
         ASTTerm.embeddedTrees(
                    sent,sattvalues,targetValues,this)) 
      { ASTTerm targ0 = targetValues[0]; 
        BasicExpression targexpr = new BasicExpression(targ0);
        targexpr.setParameter(1,var1expr); 
        System.out.println(">> Embedded mapping _1 |--> " + targexpr); 
        ams = new Vector();  
          
        AttributeMatching amx = 
          new AttributeMatching(var1expr, targexpr);
        return amx;   
      } // again, for the source tag:: if any. 
      else if (ASTTerm.sameTag(targetValues) && 
         ASTTerm.nestedSingletonTrees(sent,sattvalues, 
                                      targetValues, this)) 
      { ASTTerm sarg0 = sattvalues[0];
        BasicExpression sexpr = new BasicExpression(sarg0);
        Vector newpars = new Vector(); 
        newpars.add(var1expr); 
        sexpr.setParameters(newpars); 
 
        ASTTerm targ0 = targetValues[0]; 
        BasicExpression texpr = new BasicExpression(targ0);
        Vector tpars = new Vector(); 
        tpars.add(var1expr); 
        texpr.setParameters(tpars); 

        System.out.println(">> Tag rename mapping " + sexpr + " |--> " + texpr);
        ams = new Vector();  
           
        AttributeMatching amx = 
          new AttributeMatching(sexpr, texpr);
        return amx;   
      } // sexpr.data:: sexpr.parameters |--> texpr
    } 

    System.out.println(">>> Checking composite tree functions for target " + tatt); 

    for (int i = 0; i < sourceatts.size(); i++) 
    { Attribute satt = (Attribute) sourceatts.get(i); 
      // check if tatt = (tag satt), if tatt = (tag K satt), tatt = (tag satt K), etc. 

      ASTTerm[] sattvalues = 
                 (ASTTerm[]) sattvalueMap.get(satt); 
	  
      if (ASTTerm.sameTagSingleArgument(targetValues))
      { String commonTag = ASTTerm.commonTag(targetValues); 
        System.out.println(">***> Possible composed tree mapping " + satt + " |--> " + commonTag + "(" + tatt + ")"); 

        Vector remd = new Vector(); 
        ASTTerm[] newTargetValues = 
           ASTTerm.removeOuterTag(targetValues,remd); 
		
        AttributeMatching subexpr = 
          composedTreeFunction(sent, tatt, sourceatts, 
              sattvalueMap, newTargetValues,remd,tms,ams); 
	  
        if (subexpr != null)
        { Expression sattbe = subexpr.srcvalue; 
          BasicExpression addtag = new BasicExpression(targetValues[0]); 
          Vector parsx = new Vector(); 
          parsx.add(subexpr.trgvalue);
          addtag.setParameters(parsx);  
               
          AttributeMatching amx = 
            new AttributeMatching(sattbe, addtag);
          System.out.println(">> Found composed mapping " + amx);
          System.out.println();  
          return amx;   
        } // sattbe.data:: sattbe.parameters |--> addtag
      }
      
      if (ASTTerm.sameTagSameArity(targetValues))
      { // More than 1 subterm (ttag trm1 trm2 ...)
        // Look for matches of (stag trms...) term-by-term
        // to the target terms.

        int sarit = sattvalues[0].arity();  
        int arit = targetValues[0].arity();

        Vector foundstermpars = new Vector(); 
        Vector stermpars = new Vector(); 
        Vector ttermpars = new Vector(); 

        boolean found = false; 
        boolean allsfound = true; 

        /* 2nd phase, search from target terms */ 

        foundstermpars.clear(); 
        stermpars.clear(); 
        ttermpars.clear(); 
        boolean allfound = true;

        Vector sfoundvars = new Vector();  
        Vector localams = new Vector();            

        
        for (int tindex = 0; tindex < arit; tindex++) 
        { // if the tterms at tindex are constant, ttermpars 
          // is that constant. Otherwise if they are function 
          // of some source term or group of source terms, 
          // ttermpars is that function. 

          Vector trgJValues = new Vector(); 
          ASTTerm[] targetJValues = 
            ASTTerm.subterms(targetValues,tindex,trgJValues);

          if (ASTTerm.hasNullTerm(trgJValues))
          { continue; } // Should never happen

          if (ASTTerm.constantTrees(targetJValues))
          { ASTTerm constv = targetJValues[0]; 
            System.out.println(">**> Target terms " + (tindex+1) + " are constant: " + constv); 
		  
            Expression targexpr = new BasicExpression(constv);
            ttermpars.add(targexpr); 
            foundstermpars.add("_1"); 
            sfoundvars.add("_0"); 
            // actually _0, the entire source AST. 
          } 
          else 
          { boolean foundsource = false; 
            for (int sindex = 0; sindex < sarit && 
                                 !foundsource; 
                 sindex++) 
            { Vector srcJValues = new Vector(); 
              ASTTerm[] sourceJValues = 
                ASTTerm.subterms( 
                     sattvalues,sindex,srcJValues);

              if (ASTTerm.hasNullTerm(srcJValues))
              { break; } 

              System.out.println(">->->> Comparing source term " + (sindex+1) + " values " + srcJValues + " to target term " + (tindex+1) + " values " + trgJValues); 

              if (correspondingTrees(
                    sent,sourceJValues,targetJValues))
              { int k = tindex+1; 
                System.out.println(">> Direct correspondence _" + (sindex+1) + " |--> _" + k + " for target terms " + k); 
                ttermpars.add(new BasicExpression("_" + (sindex+1))); 
                foundstermpars.add("_" + (sindex + 1));
                sfoundvars.add("_" + (sindex + 1));  
                foundsource = true; 
              } 
            } // cases of simple functional mappings

            
            if (!foundsource)
            { for (int sindex = 0; sindex < sarit && 
                                 !foundsource; 
                   sindex++) 
              { Vector srcJValues = new Vector(); 
                ASTTerm[] sourceJValues = 
                  ASTTerm.subterms( 
                     sattvalues,sindex,srcJValues);

                if (ASTTerm.hasNullTerm(srcJValues))
                { break; } 

                java.util.Map newSMap = 
                     new java.util.HashMap(); 
                newSMap.putAll(sattvalueMap);
                newSMap.put(satt, sourceJValues);

                System.out.println(">>-->> Trying to map " + srcJValues + " to " + trgJValues); 
                // If not functional, fail.
 
                if (ASTTerm.functionalTermMapping(srcJValues,                    
                                                  trgJValues))
                { System.out.println(">>-->> Functional mapping " + srcJValues + " to " + trgJValues);
                  System.out.println();  
  
                  AttributeMatching amjx = 
                    composedTreeFunction(sent,
                           tatt,sourceatts,newSMap,     
                           targetJValues, trgJValues, 
                           tms, localams); 
                  if (amjx != null) 
                  { System.out.println(">>-->> Found nested mapping for source term " + (sindex+1) + " to target " + (tindex+1) + ": " + amjx);
                    // Create new function ff with rule amjx; 
                    // if non-trivial. Add to tms. 

                    if (srcJValues.get(0) instanceof ASTSymbolTerm) { } 
                    else 
                    { String fid = 
                         Identifier.nextIdentifier("subruleset");
                      TypeMatching tmnew = new TypeMatching(fid);
                      String slhs = 
                        ((BasicExpression) amjx.srcvalue).toCSTL(); 
                      String rrhs = 
                        ((BasicExpression) amjx.trgvalue).toLiteralCSTL();  
                      tmnew.addValueMapping(slhs, rrhs); 
                      tms.add(tmnew);
                      System.out.println(">>-->> Found nested mapping for source term _" + (sindex+1) + "`" + fid + " |--> " + "_" + (tindex+1));
                    }

                    sfoundvars.add("_" + (sindex+1)); 
                    if ((amjx.trgvalue + "").indexOf("_*") >= 0)
                    { foundstermpars.add("_*"); 
                      ttermpars.add(amjx.trgvalue); 
                    } 
                    else 
                    { Expression newtjexpr = 
                         amjx.trgvalue.substituteEq("_1", new BasicExpression("_" + (sindex+1))); 
                      ttermpars.add(newtjexpr); 
                      foundstermpars.add("_" + (sindex+1));
                    }  
                    foundsource = true; 
                  }
                } 
              }
            }

            if (!foundsource)
            { // Compare entire source to tindex term
              // This should follow the i-to-j search for 
              // individual source terms. 

              AttributeMatching amjw = 
                  composedTreeFunction(sent,
                     tatt,sourceatts,sattvalueMap,     
                     targetJValues, trgJValues, 
                     tms, localams); 
              if (amjw != null) 
              { System.out.println(">>> Found mapping of complete source term to target term " + (tindex+1) + ": " + amjw); 
                foundsource = true; 

                /* System.out.println(">>--> Is single _* target: " + ((BasicExpression) amjw.trgvalue).isSingleListTerm()); */ 

                // sfoundvars.add("_0"); 

                if ((amjw.trgvalue + "").indexOf("_*") >= 0)
                { foundstermpars.add("_*"); 
                  int svarind = 
                    BasicExpression.starIndex(
                        (BasicExpression) amjw.srcvalue); 
                  if (svarind > 0)
                  { sfoundvars.add("_" + svarind); } 
                  else 
                  { sfoundvars.add("_0"); } 
                  // actually the source var in trgvalue
                } 
                else 
                { foundstermpars.add(amjw.srcvalue + ""); 
                  sfoundvars.add("_0"); 
                }

                System.out.println(">>> foundstermpars: " + foundstermpars); 
 
                ttermpars.add(amjw.trgvalue); 
                System.out.println(">>> ttermpars: " + ttermpars); 
              } 
            } 

            if (!foundsource)
            { allfound = false; 
              ams = new Vector();  
              return null; 
            } // No match for tindex, so abandon search
          } 
        } 

        if (allfound)
        { String stag = sattvalues[0].getTag(); // all have 
                                                // same tag 
          String ttag = targetValues[0].getTag(); 
          BasicExpression sexpr = 
            new BasicExpression(stag); 
          sexpr.setIsEvent(); 

          System.out.println("**** sfoundvars = " + sfoundvars); 
          System.out.println("**** ttermpars = " + ttermpars); 
          System.out.println(); 

          // What if the source arities vary? 
          if (ASTTerm.sameTagSameArity(sattvalues))
          {
            for (int sindex = 0; sindex < sarit; sindex++) 
            { String svar = "_" + (sindex+1); 

              Vector srcJValues = new Vector(); 
              ASTTerm[] sourceJValues = 
                  ASTTerm.subterms( 
                     sattvalues,sindex,srcJValues);

            // if (ASTTerm.hasNullTerm(srcJValues))
            // { break; }

            // boolean hastargetStar = false; 
            // for (int d = 0; d < ttermpars.size(); d++) 
            // { String ttermpar = "" + ttermpars.get(d); 
            //   if (ttermpar.indexOf("_*") >= 0)
            //   { hastargetStar = true; } 
            // }  

            // If the ttermpars has a _* or _*`f then 
            // entire source is _*. 

              if (ASTTerm.constantTrees(sourceJValues))
              { Expression termsindex = 
                  new BasicExpression(sourceJValues[0]); 
                stermpars.add(termsindex); 
              } 
              else 
              { int pindex = sfoundvars.indexOf(svar); 
                if (pindex >= 0 && pindex < ttermpars.size())
                { String tvar = ttermpars.get(pindex) + ""; 
                  if (tvar.indexOf("_*") >= 0)
                  { stermpars.add(new BasicExpression("_*")); }
                  else 
                  { stermpars.add(new BasicExpression(svar)); }
                } 
              // else if (hastargetStar)
              // { stermpars.add(new BasicExpression("_*")); }
                else     
                { stermpars.add(new BasicExpression(svar)); }
              } 
            } 
          }
          else // If arities of sources vary
          { stermpars.add(new BasicExpression("_*")); } 
        
          sexpr.setParameters(stermpars); 
          BasicExpression texpr = 
            new BasicExpression(ttag); 
          texpr.setIsEvent(); 
          texpr.setParameters(ttermpars); 
          AttributeMatching fmx = 
            new AttributeMatching(sexpr,texpr); 
          ams = new Vector();  
          return fmx; 
        } 
      }  
      else if (ASTTerm.sameTag(targetValues) && 
         ASTTerm.sameArityTrees(sattvalues,targetValues))
      { Vector localams = new Vector(); 
        System.out.println(">> Checking direct correspondence of subterms of trees of varying arity: " + sattvalues[0] + " ---> " + targetValues[0]); 
        System.out.println();           

        AttributeMatching amts = 
          treeSequenceMapping(sent,sattvalues,
                              targetValues,localams); 
        
        if (amts != null) 
        { System.out.println(">> Found direct correspondence of each subterm of source/target trees: " + amts); 
          ams = new Vector();  
          return amts; 
        } 

        System.out.println(">++> Checking tree-tree mapping with indirect correspondence of subterms: " + sattvalues[0] + " ---> " + targetValues[0]); 
        System.out.println();           

        amts = treeSequenceMapping2(satt, sent, tatt, 
                 sourceatts, sattvalues, targetValues, 
                 sattvalueMap, tms, localams); 
        if (amts != null) 
        { System.out.println(">++> Found functional subterm mapping of varying arity trees: " + amts); 
          ams = new Vector();  
          return amts; 
        } 

      }
      else if (ASTTerm.sameTag(targetValues) && 
         ASTTerm.sameNonSymbolArity(sattvalues,targetValues))
      { Vector localams = new Vector(); 
        
        System.out.println(">**> Checking tree-2-tree mapping with symbol deletion/replacement: " + sattvalues[0] + " ---> " + targetValues[0]); 
        System.out.println();           

        AttributeMatching amts = 
          treeSequenceMapping3(satt, sent, tatt, 
                 sourceatts, sattvalues, targetValues, 
                 sattvalueMap, tms, localams); 
        
        if (amts != null) 
        { System.out.println(">**> Found tree-2-tree mapping with symbol deletion/replacement: " + amts); 
          ams = new Vector();  
          return amts; 
        } 
      }
    } 

    ams = new Vector();            
    return null; 
  } 

  private AttributeMatching treeSequenceMapping(
      Entity sent, ASTTerm[] strees, ASTTerm[] ttrees,
      Vector ams)
  { // Each strees[i] has same arity as ttrees[i]
    // and the strees[i].terms match to the ttrees[i].terms
    // Result mapping is  tagsource(_*) |--> tagtarget(_*)

    System.out.println(">> Trying to find tree sequence mapping for " + strees.length + " source terms to " + ttrees.length + " target terms"); 
    System.out.println();  

    int n = strees.length; 
    for (int i = 0; i < n; i++) 
    { ASTTerm st = strees[i]; 
      ASTTerm tt = ttrees[i]; 

      if (st == null || tt == null) 
      { return null; } 

      int sn = st.arity(); 
      int tn = tt.arity(); 
      if (sn != tn) 
      { return null; } 
      // Can replace one symbol by another one.

      for (int j = 0; j < sn; j++) 
      { ASTTerm subst = st.getTerm(j); 
        ASTTerm subtt = tt.getTerm(j); 
        if (ASTTerm.matchingTrees(sent,subst,subtt,this)) { } 
        else 
        { return null; }
      }  
    } 

    Vector stermpars = new Vector(); 
    stermpars.add(new BasicExpression("_*")); 
    Vector ttermpars = new Vector(); 
    ttermpars.add(new BasicExpression("_*")); 

    String stag = strees[0].getTag(); 
    String ttag = ttrees[0].getTag(); 
    BasicExpression sexpr = 
            new BasicExpression(stag); 
    sexpr.setIsEvent(); 
    sexpr.setParameters(stermpars); 
    BasicExpression texpr = 
            new BasicExpression(ttag); 
    texpr.setIsEvent(); 
    texpr.setParameters(ttermpars); 
    AttributeMatching fmx = 
            new AttributeMatching(sexpr,texpr); 
    return fmx; 
  } 


  private AttributeMatching treeSequenceMapping2(
      Attribute satt, 
      Entity sent, Attribute tatt, 
      Vector sourceatts, ASTTerm[] strees, ASTTerm[] ttrees, 
      java.util.Map sattvalueMap,     
      Vector tms, Vector ams)
  { // Each strees[i] has same arity as ttrees[i]
    // Each strees[i] element maps to ttrees[i] via 
    // a consistent mapping f. Result is 
    // tagsource(_*) |--> tagtarget(_*`f)

    System.out.println(">> Trying to find tree sequence function mapping for " + strees.length + " source terms to " + ttrees.length + " target terms"); 
    System.out.println();  


    Vector sSymbolTerms = new Vector(); 
    Vector sTreeTerms = new Vector(); 
    Vector tSymbolTerms = new Vector(); 
    Vector tTreeTerms = new Vector(); 

    int n = strees.length; 

    for (int i = 0; i < n; i++) 
    { ASTTerm st = strees[i]; 
      ASTTerm tt = ttrees[i]; 

      if (st == null || tt == null) 
      { return null; } 

      int sn = st.arity(); 
      int tn = tt.arity(); 
      if (sn != tn) 
      { return null; }       

      for (int j = 0; j < sn; j++) 
      { ASTTerm subst = st.getTerm(j); 
        ASTTerm subtt = tt.getTerm(j);
        if (subst.arity() == 0) 
        { if (subtt.arity() == 0)
          { sSymbolTerms.add(subst); 
            tSymbolTerms.add(subtt); 
          } 
          else 
          { return null; } // symbols only map to symbols
        } 
        else if (subtt.arity() > 0)
        { sTreeTerms.add(subst); 
          tTreeTerms.add(subtt); 
        } 
        else 
        { return null; } // non-symbols must go to non-symbols
      }  
    } 

    System.out.println(">>--- Trying to match: " + sSymbolTerms + " and " + tSymbolTerms); 
    System.out.println(">>--- And: " + sTreeTerms + " and " + tTreeTerms); 
    System.out.println(); 
                
    ASTTerm[] sourceJValues = new ASTTerm[sTreeTerms.size()]; 
    for (int i = 0; i < sTreeTerms.size(); i++) 
    { ASTTerm srct = (ASTTerm) sTreeTerms.get(i); 
      sourceJValues[i] = srct; 
    } 

    ASTTerm[] targetJValues = new ASTTerm[tTreeTerms.size()]; 
    for (int i = 0; i < tTreeTerms.size(); i++) 
    { ASTTerm trgt = (ASTTerm) tTreeTerms.get(i); 
      targetJValues[i] = trgt; 
    } 

    java.util.Map newSMap = new java.util.HashMap(); 
    newSMap.putAll(sattvalueMap);
    newSMap.put(satt, sourceJValues);  
    AttributeMatching amjx = 
       composedTreeFunction(sent,
                     tatt,sourceatts,newSMap,     
                     targetJValues, tTreeTerms, tms, ams); 
    if (amjx != null) 
    { System.out.println(">>--- List function mapping: " + amjx); 
      String fid = 
          Identifier.nextIdentifier("ruleset");
      TypeMatching tmnew = new TypeMatching(fid);

      Vector ssTerms = new Vector(); 
      Vector deletedSymbols = new Vector(); 
      for (int y = 0; y < sSymbolTerms.size(); y++) 
      { String slf = 
          ((ASTTerm) sSymbolTerms.get(y)).literalForm();
        ssTerms.add(slf);   
        if (deletedSymbols.contains(slf)) { } 
        else 
        { deletedSymbols.add(slf); } 
      }  

      Vector ttTerms = new Vector(); 
      for (int y = 0; y < tSymbolTerms.size(); y++) 
      { String tlf = 
          ((ASTTerm) tSymbolTerms.get(y)).literalForm();
        ttTerms.add(tlf);  
      }  

      Vector replacedSymbols = new Vector(); 
      Vector replacements = new Vector(); 
      for (int j = 0; j < deletedSymbols.size(); j++) 
      { String ds = (String) deletedSymbols.get(j); 
        if (replacedSymbols.contains(ds)) { } 
        else if (ttTerms.contains(ds)) { } 
        else 
        { boolean replacementFound = false; 
          for (int z = 0; z < ttTerms.size() && 
                        !replacementFound; z++) 
          { String rs = (String) ttTerms.get(z); 
            if (replacements.contains(rs)) { } 
            else if (Collections.frequency(ttTerms, rs) == 
                   Collections.frequency(ssTerms, ds))
            { // Look for a replacement
              replacedSymbols.add(ds); 
              replacements.add(rs);
              replacementFound = true;  
              System.out.println(">> replaced symbol " + ds + " |--> " + rs); 
            } 
          }
        } 
      } 
      deletedSymbols.removeAll(ttTerms); 
      deletedSymbols.removeAll(replacedSymbols); 
      System.out.println(">>-- deleted symbols: " + deletedSymbols); 

      for (int r = 0; r < replacedSymbols.size(); r++) 
      { String rs = (String) replacedSymbols.get(r);
        String repls = (String) replacements.get(r);  
        tmnew.addValueMapping(rs, repls); 
      } 

      for (int d = 0; d < deletedSymbols.size(); d++) 
      { String ds = (String) deletedSymbols.get(d); 
        tmnew.addValueMapping(ds, " "); 
      } 


      String slhs = 
        ((BasicExpression) amjx.srcvalue).toCSTL(); 
      String rrhs = 
        ((BasicExpression) amjx.trgvalue).toLiteralCSTL();  
      tmnew.addValueMapping(slhs, rrhs); 
      tms.add(tmnew);
      BasicExpression fexpr = new BasicExpression(fid); 
      fexpr.setUmlKind(Expression.FUNCTION);
      fexpr.addParameter(new BasicExpression("_*"));
      BasicExpression srcstar = new BasicExpression("_*");
      tmnew.addValueMapping("_*", "_*`" + fid);    
      AttributeMatching amres = 
        new AttributeMatching(srcstar, fexpr); 
      return amres; 
    } 
    return amjx; 
  }             

  private AttributeMatching treeSequenceMapping3(
      Attribute satt, 
      Entity sent, Attribute tatt, 
      Vector sourceatts, ASTTerm[] strees, ASTTerm[] ttrees, 
      java.util.Map sattvalueMap,     
      Vector tms, Vector ams)
  { // Each strees[i] non-symbol element maps to 
    // corresponding ttrees[i] non-symbol element via 
    // a consistent mapping f. Result is 
    // tagsource(_*) |--> tagtarget(_*`f)
    // Symbols can be consistently deleted or replaced

    System.out.println(">> Trying to find tree sequence add/delete mapping for " + strees.length + " source terms to " + ttrees.length + " target terms"); 
    System.out.println();  


    Vector sSymbolTerms = new Vector(); 
    Vector sTreeTerms = new Vector(); 
    Vector tSymbolTerms = new Vector(); 
    Vector tTreeTerms = new Vector(); 

    int n = strees.length; 

    for (int i = 0; i < n; i++) 
    { ASTTerm st = strees[i]; 
      ASTTerm tt = ttrees[i];

      if (st == null || tt == null) 
      { return null; } 
 
      int sn = st.nonSymbolArity(); 
      int tn = tt.nonSymbolArity(); 
      if (sn != tn) 
      { return null; }       
    
      sSymbolTerms.addAll(st.symbolTerms()); 
      tSymbolTerms.addAll(tt.symbolTerms()); 
      sTreeTerms.addAll(st.nonSymbolTerms()); 
      tTreeTerms.addAll(tt.nonSymbolTerms());   
    } 

    System.out.println(">>--- Trying to match: " + sSymbolTerms + " and " + tSymbolTerms); 
    System.out.println(">>--- And: " + sTreeTerms + " and " + tTreeTerms); 
    System.out.println(); 

    Vector ssTerms = new Vector(); 
    Vector deletedSymbols = new Vector(); 
    for (int y = 0; y < sSymbolTerms.size(); y++) 
    { String slf = 
        ((ASTTerm) sSymbolTerms.get(y)).literalForm();
      ssTerms.add(slf);  
      if (deletedSymbols.contains(slf)) { } 
      else 
      { deletedSymbols.add(slf); } 
    }  

    Vector ttTerms = new Vector(); 
    for (int y = 0; y < tSymbolTerms.size(); y++) 
    { String tlf = 
        ((ASTTerm) tSymbolTerms.get(y)).literalForm();
      ttTerms.add(tlf);  
    }  

    Vector replacedSymbols = new Vector(); 
    Vector replacements = new Vector(); 
    for (int j = 0; j < deletedSymbols.size(); j++) 
    { String ds = (String) deletedSymbols.get(j); 
      if (replacedSymbols.contains(ds)) { } 
      else if (ttTerms.contains(ds)) { } 
      else 
      { boolean replacementFound = false; 
        for (int z = 0; z < ttTerms.size() && 
                        !replacementFound; z++) 
        { String rs = (String) ttTerms.get(z); 
          if (replacements.contains(rs)) { } 
          else if (Collections.frequency(ttTerms, rs) == 
                   Collections.frequency(ssTerms, ds))
          { // Look for a replacement
            replacedSymbols.add(ds); 
            replacements.add(rs);
            replacementFound = true;  
            System.out.println(">> Replacement of symbols: " + ds + " |--> " + rs); 
          } 
        }
      } 
    } 
    deletedSymbols.removeAll(ttTerms); 
    deletedSymbols.removeAll(replacedSymbols); 
    System.out.println(">>-- Deleted symbols: " + deletedSymbols); 


  /*
    boolean directMatch = true; 
    for (int j = 0; j < sTreeTerms.size() && directMatch; j++) 
    { ASTTerm subst = (ASTTerm) sTreeTerms.get(j); 
      ASTTerm subtt = (ASTTerm) tTreeTerms.get(j); 
      if (ASTTerm.matchingTrees(sent,subst,subtt,this)) { } 
      else 
      { directMatch = false; }  
    } 

    if (directMatch)
    { Vector stermpars = new Vector(); 
      stermpars.add(new BasicExpression("_*")); 
      Vector ttermpars = new Vector(); 
      ttermpars.add(new BasicExpression("_*")); 

      String stag = strees[0].getTag(); 
      String ttag = ttrees[0].getTag(); 
      BasicExpression sexpr = 
            new BasicExpression(stag); 
      sexpr.setIsEvent(); 
      sexpr.setParameters(stermpars); 
      BasicExpression texpr = 
            new BasicExpression(ttag); 
      texpr.setIsEvent(); 
      texpr.setParameters(ttermpars); 
      AttributeMatching fmx = 
            new AttributeMatching(sexpr,texpr); 
      return fmx; 
    } 
     */ 
                
    ASTTerm[] sourceJValues = new ASTTerm[sTreeTerms.size()]; 
    for (int i = 0; i < sTreeTerms.size(); i++) 
    { ASTTerm srct = (ASTTerm) sTreeTerms.get(i); 
      sourceJValues[i] = srct; 
    } 

    ASTTerm[] targetJValues = new ASTTerm[tTreeTerms.size()]; 
    for (int i = 0; i < tTreeTerms.size(); i++) 
    { ASTTerm trgt = (ASTTerm) tTreeTerms.get(i); 
      targetJValues[i] = trgt; 
    } 

    java.util.Map newSMap = new java.util.HashMap(); 
    newSMap.putAll(sattvalueMap);
    newSMap.put(satt, sourceJValues);  
    AttributeMatching amjx = 
       composedTreeFunction(sent,
                     tatt,sourceatts,newSMap,     
                     targetJValues, tTreeTerms, tms, ams); 
    if (amjx != null) 
    { System.out.println(">>--- List function mapping: " + amjx); 
      String fid = 
          Identifier.nextIdentifier("ruleset");
      TypeMatching tmnew = new TypeMatching(fid);

      for (int r = 0; r < replacedSymbols.size(); r++) 
      { String rs = (String) replacedSymbols.get(r);
        String repls = (String) replacements.get(r);  
        tmnew.addValueMapping(rs, repls); 
      } 

      for (int d = 0; d < deletedSymbols.size(); d++) 
      { String ds = (String) deletedSymbols.get(d); 
        tmnew.addValueMapping(ds, " "); 
      } 

      String slhs = 
        ((BasicExpression) amjx.srcvalue).toCSTL(); 
      String rrhs = 
        ((BasicExpression) amjx.trgvalue).toLiteralCSTL();  
      tmnew.addValueMapping(slhs, rrhs); 
      tmnew.addValueMapping("_*", "_*`" + fid);    
      tmnew.addValueMapping("_0", "_0");    
      tms.add(tmnew);
      BasicExpression fexpr = new BasicExpression(fid); 
      fexpr.setUmlKind(Expression.FUNCTION);
      fexpr.addParameter(new BasicExpression("_*"));  
      AttributeMatching amres = 
        new AttributeMatching(new BasicExpression("_*"), 
                              fexpr); 
      return amres; 
    } 
    return amjx; 
  }             

  public Vector conditionalTreeMappings(
      Entity sent, Entity tent, Vector sobjs, Vector tobjs,
      Vector tms) 
  { // Look for conditions on sent::ast attribute which 
    // partition the sobjs-->tobjs pairs that satisfy 
    // mappings. 
    Vector res = new Vector(); 
    Vector ams = new Vector(); 

    int nobjs = sobjs.size(); 
	
    if (nobjs != tobjs.size())
    { System.err.println("!! Non-matching source/target objects: " + sobjs + " " + tobjs); 
      return res; 
    }

    
    Attribute sast = sent.getDefinedAttribute("ast"); 
    Attribute tast = tent.getDefinedAttribute("ast"); 

    if (sast == null || tast == null) 
    { return res; } 

    Vector sourceattributes = new Vector(); 
    sourceattributes.add(sast); 

    // Source trees should all have same tag & same arity
    // Look for term position i which is always a symbol. 
    // conditions are  _i = v  for the values v that occur.

    ASTTerm[] srcasts = new ASTTerm[nobjs]; 
    ASTTerm[] trgasts = new ASTTerm[nobjs]; 

    for (int i = 0; i < nobjs; i++) 
    { ObjectSpecification sobj = (ObjectSpecification) sobjs.get(i); 
      ASTTerm trm = (ASTTerm) sobj.getTree("ast"); 
      srcasts[i] = trm;
      ObjectSpecification tobj = (ObjectSpecification) tobjs.get(i); 
      ASTTerm ttrm = (ASTTerm) tobj.getTree("ast"); 
      trgasts[i] = ttrm; 
    } 

    if (ASTTerm.sameTagSameArity(srcasts))
    { } 
    else 
    { return res; }

    java.util.Map sattvalueMap = new java.util.HashMap(); 

    int sarity = srcasts[0].arity(); 
    for (int i = 0; i < sarity; i++) 
    { if (ASTTerm.alwaysSymbol(i,srcasts))
      { System.out.println(">> Source term " + i + " is always a symbol"); 
        Vector svals = ASTTerm.symbolValues(i,srcasts); 
        System.out.println(">> Symbol values: " + svals);

        // For each distinct val : svals, form EntityMatching
        // { _(i+1) = val } sent |--> tent
        // and pairset for this em, & try to find feature map

        Expression _i = 
          BasicExpression.newVariableBasicExpression("_" + 
                                                     (i+1)); 
        
        java.util.Set distinctVals = new java.util.HashSet();
        distinctVals.addAll(svals); 
        if (distinctVals.size() > 1)
        { Vector dvals = new Vector(); 
          dvals.addAll(distinctVals); 
          for (int j = 0; j < dvals.size(); j++) 
          { String dval = (String) dvals.get(j); 
            Expression condv = 
              new BinaryExpression("=", _i, 
                    new BasicExpression(dval)); 
            EntityMatching emx = 
              new EntityMatching(sent,tent); 
            // emx.setCondition(condv); 
            System.out.println(">>> New candidate Entity matching: " + emx + " for condition " + condv); 

            Vector svalues = new Vector(); 
            Vector tvalues = new Vector(); 

            for (int k = 0; k < nobjs; k++) 
            { if (ASTTerm.hasTermValue(srcasts[k],i,dval))
              { svalues.add(srcasts[k]); 
                tvalues.add(trgasts[k]); 
              } 
            } 
              
            int en = svalues.size();
            if (en < 2) 
            { continue; } // next value 
 
            ASTTerm[] sattvalues = new ASTTerm[en]; 
            ASTTerm[] tattvalues = new ASTTerm[en]; 
            
            for (int k = 0; k < en; k++) 
            { sattvalues[k] = (ASTTerm) svalues.get(k);
              tattvalues[k] = (ASTTerm) tvalues.get(k); 
            }

            sattvalueMap.put(sast,sattvalues); 

            AttributeMatching cexpr = 
              composedTreeFunction(sent,tast,sourceattributes,
                      sattvalueMap,tattvalues,tvalues,
                      tms,ams);
             
            System.out.println(">>> New candidate attribute matching: " + cexpr); 
            System.out.println(); 
            if (cexpr != null) 
            { emx.addAttributeMapping(cexpr); 
              res.add(emx); 
            } 
          }
        }    
      } 
    }  

    return res;  
  } 

  //    Entity sent, ASTTerm[] strees, ASTTerm[] ttrees)

  public void checkMetamodelConstraints(Vector cons, Vector entities, Vector types)
  { ObjectSpecification anyobj = ObjectSpecification.getDefaultInstance(); 
    Vector anyobjs = new Vector(); 
    anyobjs.add(anyobj); 

    for (int i = 0; i < cons.size(); i++) 
    { Constraint con = (Constraint) cons.get(i); 
      System.out.println("--- Checking global constraint " + con); 
     
      boolean valid = checkConstraintInModel(con, anyobjs); 
      if (valid) { } 
      else 
      { System.err.println("!! Warning -- constraint " + con + " Fails in the model!"); }
      System.out.println();  
    } 

    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i); 
      String ename = ent.getName(); 
      Vector einvs = ent.getAllInvariants(); 
      for (int j = 0; j < einvs.size(); j++) 
      { Constraint con = (Constraint) einvs.get(j);
        System.out.println("--- Checking " + ename + " constraint " + con); 
 
        Vector eobjects = (Vector) objectsOfClass.get(ename); 
        boolean valid = checkConstraintInModel(con, eobjects);
        if (valid) { } 
        else 
        { System.err.println("!! Warning -- constraint " + con + " Fails in the model!"); }  
        System.out.println();  
      }   
    }
  } 
 
}

class InstanceReplicationData
{ Attribute tatt; 
  Attribute satt = null; 
  Expression sexpr = null; 
  Expression scond = null; 
  Entity sent; 
  Entity tent; 
  Vector sourceObjects; 
  Vector targetObjects; 
  EntityMatching entityMapping; 
  
  Vector pairs = new Vector();  // of 2-element Vectors [sobj, tobj]  
  
  InstanceReplicationData(Attribute t)
  { tatt = t; }
  
  void setSplittingCondition(Expression c)
  { scond = c; }

  public String toString()
  { String res = ""; 
  
    if (scond != null) 
    { res = "{ " + scond + " } " + sent + " |--> " + tent + "\n"; }
    else if (satt != null) 
    { res = "{ $x : " + satt + " } " + sent + " |--> " + tent + "\n"; }
    else 
    { res = "{ $x : " + sexpr + " } " + sent + " |--> " + tent + "\n"; } 
	
    res = res + "  $x |--> " + tatt + "\n\n"; 
    res = res + "  " + sourceObjects + " |==> " + targetObjects + "\n"; 
    return res; 
  } 
}
