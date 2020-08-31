import java.util.Vector; 

/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: MT synthesis */ 

public class RequirementsPhrase
{ 
  static String[] classMappingVerbs = {"created", "creates", "creating", "create", 
                                       "generated", "generates", "generate", "generating", 
									   "engendered", "engender", "engenders", "transformed", "transforms", "transform", 
									   "transforming", "introduced", "introduces", "introduce", "introducing", 
                                       "mapped", "maps", "map", "mapping", 
									   "becomes", "become", "becoming", "obtain", "obtains", "obtained", "obtaining" };
									   
  static String[] classDeletingVerbs = { "retained", "retains", "retaining", "preserved", "preserves", "preserving",  
                                       "deleted", "deletes", "deleting", "filter", "filtered", "filtering", 
                                       "removed", "removes", "removing" };  

  static String[] classMergingVerbs = { "merged", "merges", "merging", "folded", "folds", "folding",  
                                        "collapses", "collapsed", "collapsing", 
                                       "combined", "combines", "combining", "coalesce", "coalescing", "coalesces", 
                                       "amalgamate", "amalgamates", "amalgamating" };  

  static String[] classSplittingVerbs = { "divided", "divides", "dividing", "unfolded", "unfolds", "unfolding",  
                                       "split", "splits", "splitting", "duplicated", "duplicates", "duplicating",
                                       "replicate", "replicates", "replicating" };  

  static String[] classUpdatingVerbs = { "rewrites", "rewritten", "rewriting", "refactor", "refactors", "refactoring",  
                                       "moved", "moves", "moving", "modified", "modifies", "modifying",
                                       "update", "updated", "updates", "updating" };  

  static String[] featureMappingVerbs = { "copy", "copied", "corresponds", "correspond", "corresponding", 
                                          "accepts", "set-to", "set-from", "set-by", "set-as", 
                                          "initialized", "initialised", 
                                          "assign", "assigned", "assigns", "encoded", "associated", "linked", "link", "linking",  
                                          "connected", "connecting", "connects", "connect", "composed", "compose", "comprised", "comprise", 
										  "equals", "equal", "equalling", 
										  "same", "identical", "represented", "represents", "computed", "computes", 
										  "provided", "provides", "translated", "translation" };
										  
  static String[] relationalVerbs = { "corresponds", "encoded", "associated", "linked",   
                                      "connected", "connects", "composed", "comprised", "comprises", 
									  "equals", "equal", "equalling", 
									  "same", "identical", "represented", "represents"}; 
										  
  static String[] featureCopyingWords = { "same", "equal", "equals", "equalling", "copy", "copied", "identical" }; 
  // also "is"

  static String[] featureCombinationWords = { "combined", "combining", "combines", "composition", 
                                              "composed", "concatenation", "joined", "appended" }; 
  

  static String[] conditionalPredicates = { "if", "when", "which", "that", "where" }; 
                                          // Others, ambiguous: "with", "case", "not" 
  // also, any adjective on a source class name


  String phrasekind = "noun"; // or "verb"
  Vector words = new Vector(); 
  java.util.Set verbClassifications = new java.util.HashSet();  // for verb phrase
  java.util.Set nounClassifications = new java.util.HashSet();  // for noun phrase
  

  public RequirementsPhrase(String knd, Vector wds)
  { phrasekind = knd; 
    words = wds; 
  } 

  public String toString()
  { return phrasekind + " phrase: " + words; } 
  
  public boolean isNounPhrase()
  { return "noun".equals(phrasekind); }

  public boolean isVerbPhrase()
  { return "verb".equals(phrasekind); }
  
  public static boolean isNounWord(String lex)
  { if (lex.endsWith("_NNPS") || lex.endsWith("_NNS") || 
	    lex.endsWith("_NN") || lex.endsWith("_NNP"))
	 { return true; } 
	 return false; 
  } 

  public static boolean isAdjective(String lex)
  { return lex.endsWith("_JJ"); }
  
  public static boolean isQuantifier(String lex)
  { return lex.endsWith("_CD"); }

  public static boolean isClassMappingVerb(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < classMappingVerbs.length; i++) 
    { if (check.equals(classMappingVerbs[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isClassUpdateVerb(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < classUpdatingVerbs.length; i++) 
    { if (check.equals(classUpdatingVerbs[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isClassDeleteVerb(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < classDeletingVerbs.length; i++) 
    { if (check.equals(classDeletingVerbs[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isClassMergeVerb(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < classMergingVerbs.length; i++) 
    { if (check.equals(classMergingVerbs[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isClassSplitVerb(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < classSplittingVerbs.length; i++) 
    { if (check.equals(classSplittingVerbs[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isFeatureMappingVerb(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < featureMappingVerbs.length; i++) 
    { if (check.equals(featureMappingVerbs[i]))
      { return true; } 
    } 
    return false; 
  } 
  
  public static boolean isRelationalVerb(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < relationalVerbs.length; i++) 
    { if (check.equals(relationalVerbs[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isActiveVerb(String vb) 
  { if (isRelationalVerb(vb))
    { return false; } 
	return 
      (isFeatureMappingVerb(vb) || isClassSplitVerb(vb) || isClassMergeVerb(vb) || 
       isClassDeleteVerb(vb) || isClassUpdateVerb(vb) || isClassMappingVerb(vb));  
  }

  public static boolean isFeatureCopyingWord(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < featureCopyingWords.length; i++) 
    { if (check.equals(featureCopyingWords[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isFeatureCombineWord(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < featureCombinationWords.length; i++) 
    { if (check.equals(featureCombinationWords[i]))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isConditionalPredicate(String str) 
  { int subind = str.indexOf("_"); 
    if (subind < 0) 
	{ return false; }
    String check = str.substring(0,subind); 
    for (int i = 0; i < conditionalPredicates.length; i++) 
    { if (check.equals(conditionalPredicates[i]))
      { return true; } 
    } 
    return false; 
  } 

  public boolean hasConditionalVerb()
  { boolean res = false;  
    for (int i = 0; i < words.size() && !res; i++)
    { String word = (String) words.get(i); 
	  if (isConditionalPredicate(word)) 
	  { res = true; } 
    } 
    return res; 
  } // and return the remainder of the phrase as the condition, up to the next Target class name
  
  public static boolean hasCopyWord(Vector phrases)
  { boolean res = false;  
    for (int p = 0; p < phrases.size(); p++)
    { RequirementsPhrase pr = (RequirementsPhrase) phrases.get(p); 
	 
      for (int i = 0; i < pr.words.size() && !res; i++)
      { String word = (String) pr.words.get(i); 
        if (isFeatureCopyingWord(word)) 
        { res = true; } 
      } 
    }
    return res; 
  } 
  
  public static boolean hasCombineWord(Vector phrases)
  { boolean res = false;  
    for (int p = 0; p < phrases.size(); p++)
    { RequirementsPhrase pr = (RequirementsPhrase) phrases.get(p); 
	 
      for (int i = 0; i < pr.words.size() && !res; i++)
      { String word = (String) pr.words.get(i); 
        if (isFeatureCombineWord(word)) 
        { res = true; } 
      } 
    }
    return res; 
  } 

  public static boolean isClassName(String cwd, Vector entities)
  { if (ModelElement.hasInitialCapital(cwd))
    { Entity trg = (Entity) ModelElement.lookupByName(cwd,entities); 
      if (trg == null)
      { trg = (Entity) ModelElement.lookupByNameNMS(cwd,entities,0.5); }
      if (trg == null) 
	  { return false; }
	  return true;
	}
	return false;   
  } 

  public static boolean isFeatureName(String cwd, Vector features)
  { Attribute trg = (Attribute) ModelElement.lookupByName(cwd, features); 
    if (trg == null)
    { trg = (Attribute) ModelElement.lookupByNameNMS(cwd, features, 0.5); }
    if (trg == null) 
	{ return false; }
	return true; 
  } 

  public static Entity findClass(String cwd, Vector entities)
  { if (ModelElement.hasInitialCapital(cwd))
    { Entity trg = (Entity) ModelElement.lookupByName(cwd,entities); 
      if (trg == null)
      { trg = (Entity) ModelElement.lookupByNameNMS(cwd,entities,0.5); }
      return trg;
	} 
	return null;  
  } 

  public static Vector conditional(Vector phrases, Vector sources, Vector targets, Vector sourceFeatures,
                                   Vector scopeClass)
  { Vector res = new Vector(); 
    Entity currentSource = null; 
    Entity currentTarget = null; 
	
    for (int x = 0; x < phrases.size(); x++)
    { RequirementsPhrase pr = (RequirementsPhrase) phrases.get(x);  
      Vector wds = pr.words; 
      for (int i = 0; i < wds.size(); i++)
      { String word = (String) wds.get(i);
        int subind = word.indexOf("_"); 
        if (subind < 0) 
        { continue; }
		
        String subword = word.substring(0,subind);
        Entity src = findClass(subword,sources); 
        if (src != null)
        { currentSource = src; }
        Entity trg = findClass(subword,targets); 
        if (trg != null)
        { currentTarget = trg; }
		 
        String check = subword.toLowerCase(); 
        if ("if".equals(check) || "when".equals(check) || "which".equals(check) || "that".equals(check) || 
	        "where".equals(check) || "with".equals(check))
        { if ("with".equals(check))
          { res.add("with"); }
		  
          for (int j = i+1; j < wds.size(); j++) 
          { String xw = (String) wds.get(j); 
            int subxw = xw.indexOf("_"); 
            if (subxw >= 0)
            { xw = xw.substring(0,subxw); } 
            res.add(xw); 
          }
		  
          if (x < phrases.size() - 1)
          { RequirementsPhrase nextphrase = (RequirementsPhrase) phrases.get(x+1); 
            
            // res.addAll(nextphrase.words); 
            // should be a verb phrase - but not a verb phrase with a class mapping/update/etc verb or 
            // feature mapping verb. 
			
            for (int g = 0; g < nextphrase.words.size(); g++) 
            { String origgw = (String) nextphrase.words.get(g); 
              int subgw = origgw.indexOf("_");
              String gw = origgw;  
              if (subgw >= 0)
              { gw = origgw.substring(0,subgw); }
			   
              if (isActiveVerb(origgw) || isClassName(gw,targets) || "then".equals(gw))
              { if (currentSource != null) 
                { scopeClass.add(currentSource); }  
                return res; 
              }
              else  
              { res.add(gw); } 
            }
			
           if (x < phrases.size() - 2) 
           { RequirementsPhrase rnext = (RequirementsPhrase) phrases.get(x+2);
		  // if (rnext.isNounPhrase())
             Vector condwords = rnext.words; 
             for (int y = 0; y < condwords.size(); y++)
             { String origcwd = (String) condwords.get(y); 
               int wdind = origcwd.indexOf("_"); 
               String cwd = origcwd; 
               if (wdind >= 0)
               { cwd = origcwd.substring(0,wdind); }

               if (isActiveVerb(origcwd) || "then".equals(cwd) || isClassName(cwd,targets))
               { if (currentSource != null) 
                 { scopeClass.add(currentSource); }  
                 return res; 
               }
               else  
               { res.add(cwd); }
             }
           }	 

           if (x < phrases.size() - 3) 
           { RequirementsPhrase rnext = (RequirementsPhrase) phrases.get(x+3);
		     if (rnext.isNounPhrase())
             { Vector condwords = rnext.words; 
               for (int y = 0; y < condwords.size(); y++)
               { String origcwd = (String) condwords.get(y); 
                 int wdind = origcwd.indexOf("_"); 
                 String cwd = origcwd; 
                 if (wdind >= 0)
                 { cwd = origcwd.substring(0,wdind); }

                 if (isActiveVerb(origcwd) || "then".equals(cwd) || isClassName(cwd,targets))
                 { if (currentSource != null) 
	               { scopeClass.add(currentSource); }  
                   return res; 
                 }
                 else  
                 { res.add(cwd); }
               }
             }
           }	 
          
        }
      
        if (currentSource != null) 
        { scopeClass.add(currentSource); } 
        return res; 
      }	
      else if (isAdjective(word))
      { res.add(check); 
        for (int j = i+1; j < wds.size(); j++) 
        { String xw = (String) wds.get(j); 
          int subxw = xw.indexOf("_"); 
        if (subxw >= 0)
		{ xw = xw.substring(0,subxw); } 
		if (isAdjective(xw) || isQuantifier(xw))
		{ res.add(xw); } 
		else if (isFeatureName(xw,sourceFeatures))
		{ res.add(xw); }
		else if (isClassName(xw,sources))
		{ Entity srcclass = findClass(xw,sources);
		  scopeClass.add(srcclass);  
		  return res; 
		} 
        else 
		{ if (currentSource != null) 
          { scopeClass.add(currentSource); } 
          return res; 
		} 
	  }
	}  
  }
} 
    return res; 
  } // and return the remainder of the phrase as the condition, up to the next Target class name

  public String classifyVerbs()
  { java.util.Set classifications = new java.util.HashSet(); 
    String category = "unknown"; 
	
    for (int i = 0; i < words.size(); i++)
    { String word = (String) words.get(i); 
	  if (isClassMappingVerb(word)) 
	  { category = "classMapping"; 
	    classifications.add(category); 
	  }
	  else if (isClassDeleteVerb(word)) 
	  { category = "classDeleting"; 
	    classifications.add(category); 
	  }
	  else if (isClassMergeVerb(word)) 
	  { category = "classMerging"; 
	    classifications.add(category); 
	  }
	  else if (isClassSplitVerb(word)) 
	  { category = "classSplitting"; 
	    classifications.add(category); 
	  }
	  else if (isClassUpdateVerb(word)) 
	  { category = "classUpdating"; 
	    classifications.add(category); 
	  }
	  else if (isFeatureMappingVerb(word))
	  { category = "featureMapping"; 
	    classifications.add(category); 
      }
	} 

    verbClassifications.clear(); 
	verbClassifications.addAll(classifications); 
	
	if (classifications.size() == 1)
	{ return category; }
	else if (classifications.contains("classMapping"))
	{ return "classMapping"; }
	else if (classifications.contains("classDeleting"))
	{ return "classDeleting"; }
	else if (classifications.contains("classMerging"))
	{ return "classMerging"; }
	else if (classifications.contains("classSplitting"))
	{ return "classSplitting"; }
	else if (classifications.contains("classUpdating"))
	{ return "classUpdating"; }
	else if (classifications.contains("featureMapping"))
	{ return "featureMapping"; }
	return "unknown"; 
  } 
  
  public void classifyNounPhrase(Vector entities, Vector types, Vector mmnames, Vector sources, Vector targets, Vector sourcefeats, Vector targetfeats)
  { Vector sourceFeatures = Entity.allSourceFeatures(entities); 
    Vector targetFeatures = Entity.allTargetFeatures(entities); 

    for (int i = 0; i < words.size(); i++) 
    { String word = (String) words.get(i);
	  
	 if (isNounWord(word) || isAdjective(word)) { } // But may be misclassified as an adjective 
	 else { continue; }
	   
	 int subind = word.indexOf("_"); 
     if (subind < 0) { continue; }
	 String check = word.substring(0,subind); 
	  
      if (mmnames.contains(check)) { continue; } 

      // if ((sources.size() > 0 || targets.size() > 0) && check.startsWith("element")) 
      // { continue; } // vacuous word
	  
	  if (ModelElement.hasInitialCapital(check))
      { Entity ent = (Entity) ModelElement.lookupByNameIgnoreCase(check,entities);
	  
	    if (ent == null)
	    { ent = (Entity) ModelElement.lookupByNameNMS(check,entities,0.5); }
	  
        if (ent != null)
	    { if (ent.isSource())
	      { if (sources.contains(ent)) { } 
		    else 
		    { sources.add(ent); }
		  } 
	      else if (ent.isTarget())
		  { if (targets.contains(ent)) { } 
		    else 
		    { targets.add(ent); }
		  }
		} 
	  }
	  else 
	  { Attribute feat = (Attribute) ModelElement.lookupByNameIgnoreCase(check,sourceFeatures); 
	    if (feat != null) 
		{ if (sourcefeats.contains(feat)) {}
		  else 
		  { sourcefeats.add(feat); }
		} 
		else
		{ feat = (Attribute) ModelElement.lookupByNameIgnoreCase(check,targetFeatures); }
		
		if (feat != null) 
		{ if (targetfeats.contains(feat)) { }
		  else 
		  { targetfeats.add(feat); }
		} 
		else 
		{ Entity ent = (Entity) ModelElement.lookupByNameIgnoreCase(check,entities);
	  
	      if (ent == null)
	      { ent = (Entity) ModelElement.lookupByNameNMS(check,entities,0.5); }
	  
          if (ent != null)
	      { if (ent.isSource())
	        { if (sources.contains(ent)) { } 
		      else 
		      { sources.add(ent); }
		    } 
	        else if (ent.isTarget())
		    { if (targets.contains(ent)) { } 
		      else 
		      { targets.add(ent); }
		    }
		  }
		}
	  }
	}
	
	nounClassifications.clear(); 
	nounClassifications.addAll(sources);
	nounClassifications.addAll(targets);  
  }

  public static String classify(Vector phrases, Vector entities, Vector types, String[] mmnames, RequirementsSentence req)
  { java.util.Set classifications = new java.util.HashSet(); 
    String category = "unknown"; 
    Vector sourceClasses = new Vector(); 
    Vector targetClasses = new Vector(); 
    Vector sourceFeatures = new Vector(); 
    Vector targetFeatures = new Vector(); 
    Vector metamodelnames = new Vector(); 
    for (int i = 0; i < mmnames.length; i++) 
    { metamodelnames.add(mmnames[i]); } 
	
    for (int i = 0; i < phrases.size(); i++) 
    { RequirementsPhrase ph = (RequirementsPhrase) phrases.get(i); 
      if (ph.isNounPhrase())
      { ph.classifyNounPhrase(entities,types,metamodelnames,sourceClasses,targetClasses,sourceFeatures,targetFeatures); }
      else if (ph.isVerbPhrase())
      { category = ph.classifyVerbs();
        classifications.add(category);
        if (ph.hasConditionalVerb())
        { classifications.add("conditional"); }  
      }
    } 
	
     if (classifications.contains("classDeleting") || classifications.contains("classUpdating") ||
         classifications.contains("classMerging") || classifications.contains("classSplitting"))
     { for (int i = 0; i < sourceClasses.size(); i++)
       { Entity srcClass = (Entity) sourceClasses.get(i); 
         String srcName = srcClass.getName(); 
         Entity trgClass = (Entity) ModelElement.lookupByName(srcName + "1", entities); 
         if (trgClass != null && trgClass.isTarget() &&
             !targetClasses.contains(trgClass))
         { targetClasses.add(trgClass); }
       } 
     } 

	System.out.println("Source classes in sentence >> " + sourceClasses); 
	System.out.println("Target classes in sentence >> " + targetClasses); 
	System.out.println("Source features in sentence >> " + sourceFeatures); 
	System.out.println("Target features in sentence >> " + targetFeatures); 

    // if (sourceFeatures.size() > 0 && targetFeatures.size() == 0) 
    // { classifications.add("conditional"); } 

    Vector scopeClass = new Vector(); 	
	Vector cond = conditional(phrases,sourceClasses,targetClasses,sourceFeatures,scopeClass); 
    
	if (cond.size() > 0) 
	{ System.out.println(">> Possible condition = " + cond); 
      System.out.println(); 
	  classifications.add("conditional"); 
	  String cstring = ""; 
	  for (int i = 0; i < cond.size(); i++)
	  { cstring = cstring + cond.get(i) + " "; }

      if (scopeClass.size() > 0)
	  { Entity srcclass = (Entity) scopeClass.get(0); 
        System.out.println(">>> Condition " + cstring + " applies to class " + srcclass);
      
	    if (cond.size() == 1)
	    { Vector allatts = srcclass.allAttributes(); 
		  Vector best = ModelElement.findClosestNamed((String) cond.get(0), allatts); 
		  if (best.size() > 0)
		  { System.out.println(">>> condition is related to attribute " + best.get(0)); 
		    BasicExpression fcond = new BasicExpression((Attribute) best.get(0)); 
			fcond.setEntity(srcclass);
            req.addCondition(fcond); 
		  }
		} 
		else 
		{ BasicExpression xcond = new BasicExpression(cstring); 
		  xcond.setEntity(srcclass); 
		  req.addCondition(xcond); 
		}
	  } 
	  else 
	  { BasicExpression ycond = new BasicExpression(cstring); 
        req.addCondition(ycond); 
      }
	} // also numeric restrictions "have 0 neighbours", etc
	
	System.out.println(">> Classifications = " + classifications); 
	System.out.println(); 
	
	req.setSourceClasses(sourceClasses); 
	req.setTargetClasses(targetClasses); 
	req.setSourceFeatures(sourceFeatures); 
	req.setTargetFeatures(targetFeatures); 
	
	if (classifications.size() == 1)
	{ return category; }
	else if (classifications.contains("classMapping"))
	{ return "classMapping"; }
	else if (classifications.contains("classDeleting"))
	{ return "classDeleting"; }
	else if (classifications.contains("classMerging"))
	{ return "classMerging"; }
	else if (classifications.contains("classSplitting"))
	{ return "classSplitting"; }
    else if (classifications.contains("classUpdating"))
	{ return "classUpdating"; }
	else if (classifications.contains("featureMapping"))
	{ return "featureMapping"; }
	return "unknown"; 
  } 
} 
