import java.util.Vector; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: MT synthesis */ 

public class RequirementsSentence
{ 

  String sentencekind = "classMapping"; // or "conditionalClassMapping", "classUpdate", 
                                        // "conditionalClassUpdate", or "featureMapping" or "other"
  Vector phrases = new Vector();  // RequirementsPhrase 

  Vector sourceClasses = new Vector(); 
  Vector targetClasses = new Vector(); 
  Vector sourceFeatures = new Vector(); 
  Vector targetFeatures = new Vector(); 
  Vector conditions = new Vector(); // of Expression
  Vector postconditions = new Vector(); 
  

  public RequirementsSentence(String knd, Vector wds)
  { sentencekind = knd; 
    phrases = wds; 
  } 

  public String toString()
  { return sentencekind + " sentence: " + phrases; } 

  public void setSourceClasses(Vector sources) 
  { sourceClasses = sources; } 

  public void setTargetClasses(Vector targets) 
  { targetClasses = targets; } 

  public void setSourceFeatures(Vector sources) 
  { sourceFeatures = sources; } 

  public void setTargetFeatures(Vector targets) 
  { targetFeatures = targets; } 
  
  public boolean isClassMapping()
  { return "classMapping".equals(sentencekind); }

  public boolean isClassUpdate()
  { return "classUpdating".equals(sentencekind); }

  public boolean isClassDelete()
  { return "classDeleting".equals(sentencekind); }

  public boolean isClassMerge()
  { return "classMerging".equals(sentencekind); }

  public boolean isClassSplit()
  { return "classSplitting".equals(sentencekind); }

  public boolean isFeatureMapping()
  { return "featureMapping".equals(sentencekind); }

  public boolean isConditionalMapping() 
  { return "conditionalClassMapping".equals(sentencekind); } 

  public boolean isConditionalUpdate()
  { return "conditionalClassUpdate".equals(sentencekind); }

  public void addCondition(Expression cond)
  { conditions.add(cond); } 
  
  public Vector toEntityMappings(Vector precedingMappings)
  { Vector res = new Vector();
    
    boolean hasCopyWord = RequirementsPhrase.hasCopyWord(phrases); 
    if (hasCopyWord) 
    { System.out.println(">> Feature copying"); } 

    boolean hasCombineWord = RequirementsPhrase.hasCombineWord(phrases); 
    if (hasCombineWord) 
    { System.out.println(">> Feature combination"); } 

    Vector specificSources = new Vector(); 
    Vector specificTargets = new Vector(); 
    specificSources.addAll(sourceClasses); 
    specificTargets.addAll(targetClasses); 


    for (int i = 0; i < specificSources.size(); i++) 
    { Entity sent = (Entity) specificSources.get(i); 
      Vector sfeatures = sent.allDefinedFeatures(); 

      for (int j = 0; j < specificTargets.size(); j++) 
      { Entity tent = (Entity) specificTargets.get(j); 

        EntityMatching em = new EntityMatching(sent,tent); 
        res.add(em); 

        if (conditions.size() > 0)
        { for (int h = 0; h < conditions.size(); h++) 
          { Expression expr = (Expression) conditions.get(h); 
            if (expr.getEntity() == sent) 
            { expr.setType(new Type("boolean",null)); 
              em.addCondition(expr);
            } 
            else if (expr.getEntity() == null) 
            { Expression quotedcond = new BasicExpression("\"" + expr + "\""); 
              quotedcond.setType(new Type("boolean",null)); 
              em.addCondition(quotedcond);
            }   
          }
        }

        Vector tfeatures = tent.allDefinedFeatures();  

         for (int k = 0; k < sourceFeatures.size(); k++) 
         { Attribute sf = (Attribute) sourceFeatures.get(k);
           if (sfeatures.contains(sf.getName())) 
           { if (targetFeatures.size() == 0 && hasCopyWord)
             { System.out.println(">> Copying of " + sf + " from " + sent + " to " + tent + " " + tfeatures); 

               Attribute tf = tent.getDefinedProperty(sf.getName()); 
               if (tf != null) 
               { AttributeMatching am = new AttributeMatching(sf,tf); 
                 em.addAttributeMatching(am);
                 System.out.println(">>> Identified feature mapping: " + am); 
               }
             }  
             else 
             { 
              for (int l = 0; l < targetFeatures.size(); l++) 
              { Attribute tf = (Attribute) targetFeatures.get(l);
               if (tfeatures.contains(tf.getName()) && sf.getName().equals(tf.getName()))
               { AttributeMatching am = new AttributeMatching(sf,tf);
                 System.out.println(">>> Identified feature mapping: " + am);  
                 em.addAttributeMatching(am);
               } 
             }
           }   
	    }
       } 
     } 
   }	
	
   Vector newmappings = new Vector(); 
	
   for (int i = 0; i < res.size(); i++) 
   { EntityMatching em = (EntityMatching) res.get(i);
     boolean duplicate = false;  
     for (int j = 0; j < precedingMappings.size(); j++)
     { EntityMatching emx = (EntityMatching) precedingMappings.get(j);
       if (em.realsrc == emx.realsrc && em.realtrg == emx.realtrg && 
	       em.hasSameCondition(emx))
	   { emx.mergeWith(em); 
	     duplicate = true; 
	   }
	 }
	
  	 if (duplicate) { } 
	 else 
	 { newmappings.add(em); }
    }
	
    return newmappings; 
  } 
} 
