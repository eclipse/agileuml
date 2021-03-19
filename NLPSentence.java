import java.util.Vector; 

/* Package: Requirements Engineering */ 
/******************************
* Copyright (c) 2003-2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class NLPSentence
{ Vector phrases = new Vector();  // of NLPPhraseElement
  Vector derivedElements = new Vector(); // of ModelElement
  String id = "0"; 
   
  public NLPSentence()
  { }

  public NLPSentence(String tag, Vector phs)
  { phrases = phs;
    for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement phr = (NLPPhraseElement) phrases.get(i); 
      phr.setSentence(this); 
    }
  } 
  
  public void setId(String x)
  { id = x; } // for tracing
  
  public void indexing()
  { int st = 1; 
    for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) phrases.get(i); 
      st = elem.indexing(st);
      elem.sentence = this;  
	  elem.linkToPhrases(this); 
    }
  }
  
  public void linkToPhrases()
  { for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) phrases.get(i); 
      elem.sentence = this;  
      elem.linkToPhrases(this); 
    }
  }

  public Vector sequentialise()
  { Vector res = new Vector();  
    for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) phrases.get(i); 
      Vector st = elem.sequentialise(); 
	  res.addAll(st); 
    }
	return res; 
  }

  public void addElement(NLPPhrase elem)
  { phrases.add(elem); }
  
  public String toString()
  { String res = "(S "; 
    for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) phrases.get(i); 
      res = res + "\n    " + elem; 
    }
    return res + ")"; 
  }

  public boolean isSVO()
  { // elements are (NP ...) (VP ...)
    if (phrases.size() < 2) { return false; } 

    NLPPhraseElement p1 = (NLPPhraseElement) phrases.get(0); 
    NLPPhraseElement p2 = (NLPPhraseElement) phrases.get(1);
    if (p1.tag.equals("NP") && p2.tag.equals("VP"))
    { return true; } 
	
    if (p1.tag.equals("NP") && p2.tag.equals("ADVP") && phrases.size() > 2)
    { NLPPhraseElement p3 = (NLPPhraseElement) phrases.get(2);
      return p3.tag.equals("VP"); 
    } 
	
	if (p1.tag.equals("PP") && p2.tag.equals("VP"))
	{ return true; }
	
	if (p1.tag.equals("PP") && p2.tag.equals(",") && phrases.size() > 2)
	{ NLPPhraseElement p3 = (NLPPhraseElement) phrases.get(2);
      return p3.tag.equals("VP"); 
    } 

    return false; 
  }  
  
  public boolean isSystemDefinition()
  { // first noun is "system", "application", etc
    NLPPhraseElement p1 = (NLPPhraseElement) phrases.get(0); 
    if (p1 instanceof NLPPhrase && p1.tag.equals("NP"))
	{ NLPPhrase pr = (NLPPhrase) p1; 
	  Vector nouns = pr.getNouns(); 
	  if (nouns.contains("System") || nouns.contains("system") || nouns.contains("Application") || 
	      nouns.contains("application") || nouns.contains("app") || nouns.contains("App") || 
		  nouns.contains("software") || nouns.contains("Software") || nouns.contains("Program") || 
		  nouns.contains("program") || 
		  nouns.contains("database") || nouns.contains("Database"))
	  { return true; }
	}
	return false; 
  }
  
  public String getMainVerb()
  { // The VP verb is "consists"/"has"/"have", etc
    // ((subject) (VP ... main verb ... (NP object)))
	
    if (phrases.get(1) instanceof NLPPhrase) { } 
    else 
    { return ""; }
	
    String verb = ""; 
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    if (p2.tag.equals("VP"))
    { verb = p2.getPrincipalVerb(); 
      if (("is".equals(verb) || "are".equals(verb) || "shall".equals(verb) || "should".equals(verb)) && 
	      p2.elements.size() > 1 && p2.elements.get(1) instanceof NLPPhrase && 
		  ((NLPPhrase) p2.elements.get(1)).isVerbPhrase())
      { NLPPhrase p3 = (NLPPhrase) p2.elements.get(1); 
        return p3.getPrincipalVerb(); 
      } // treat "is" as auxiliary
    }  
    else if (p2.tag.equals("ADVP") && phrases.size() > 2 && phrases.get(2) instanceof NLPPhrase)
    { NLPPhrase p3 = (NLPPhrase) phrases.get(2);
      verb = p3.getPrincipalVerb(); 
    } 
	
    return verb; 
  } 

  public String getMainVerbOrPreposition()
  { // The VP verb is "consists"/"has"/"have", etc
    if (phrases.get(1) instanceof NLPPhrase) { } 
    else 
    { return ""; }
	
    String verb = ""; 
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    if (p2.tag.equals("VP"))
    { verb = p2.getPrincipalVerb(); 
      if (("is".equals(verb) || "are".equals(verb)) && 
	      p2.elements.size() > 1 && p2.elements.get(1) instanceof NLPPhrase && 
		  ((NLPPhrase) p2.elements.get(1)).isVerbOrPrepositionPhrase())
      { NLPPhrase p3 = (NLPPhrase) p2.elements.get(1); 
        return p3.getPrincipalVerbOrPreposition(); 
      } // treat "is" as auxiliary
    }  
    else if (p2.tag.equals("ADVP") && phrases.size() > 2 && phrases.get(2) instanceof NLPPhrase)
    { NLPPhrase p3 = (NLPPhrase) phrases.get(2);
      verb = p3.getPrincipalVerb(); 
    } 
    return verb; 
  } 

  public boolean isClassDefinition(Vector quals)
  { // The VP verb is "consists"/"has"/"have", etc
    
    String verb = getMainVerb(); 
	
	System.out.println(">>> Main verb is: " + verb); 
	
    if ("has".equals(verb) || "have".equals(verb) || "=".equals(verb) || "equals".equals(verb) || verb.startsWith("define") || 
        verb.startsWith("compris") || verb.startsWith("consist") || verb.equals("composed") || verb.equals("made")) 
	{ return true; } 
	else if (verb.startsWith("record") || verb.startsWith("store") || verb.startsWith("persist") || verb.startsWith("retain"))
    { quals.add("persistent"); 
	  return true; 
	} // but likely to be a system/app in the subject. 
    return false; 
  } 
 
  public boolean isGeneralDefinition()
  { // The VP verb is "is"/"are", etc
    String verb = getMainVerb(); 
    if ("is".equals(verb) || "are".equals(verb))
    { return true; } 
    return false; 
  } // eg., (VP (VBZ is) (NP (CC either) ... ))

  public boolean isAssociationDefinition()
  { // The VP verb is "linked"/"associated"/"connected", etc
    String verb = getMainVerbOrPreposition(); 
    if (verb.startsWith("link") || verb.startsWith("associat") || verb.startsWith("connect") || "for".equals(verb) || "belongs".equals(verb) || 
	   verb.startsWith("relate"))
    { return true; } 
    return false; 
  } 

  public Vector modelElements(java.util.Map fromBackground, Vector modelElements)
  { // assuming SVO && isClassDefinition

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
	if (noun == null || noun.length() == 0) 
	{ return res; }
	
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    if (p2.tag.equals("ADVP"))
    { p2 = (NLPPhrase) phrases.get(2); }
	
	Entity ent = null; 
    Object obj = ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
    if (obj == null) 
    { String nme = Named.capitalise(noun); 
	  ent = new Entity(nme);
      modelElements.add(ent); 
      res.add(ent);  
      System.out.println(">>> New entity " + nme); 
	  derivedElements.add(ent); 
	  ent.addStereotype("originator=\"" + id + "\""); 
    } 
	else if (obj instanceof Entity)
	{ ent = (Entity) obj; }
	
    if (ent != null) 
	{ p2.extractAttributeDefinitions(ent, fromBackground, modelElements); } 
	
    return res;  
  } 

  public Vector relationElements(Vector modelElements)
  { // assuming SVO && isGeneralDefinition

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
	if (noun == null || noun.length() == 0) 
	{ return res; }
	if (NLPWord.isKeyword(noun))
	{ return res; } 
	
	String singular = NLPWord.getSingular(noun); 
	
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    Entity ent = null; 
	Object obj = ModelElement.lookupByNameIgnoreCase(singular, modelElements); 
    if (obj == null) 
    { ent = ModelElement.featureBelongsTo(noun, modelElements); 
	  if (ent != null) // it is a feature, a boolean or effective enumeration
	  { Attribute att = ent.getDefinedPropertyIgnoreCase(noun); 
	    p2.extractAlternativeValues(att, ent, modelElements); 
		derivedElements.add(att); 
		return res; 
	  }
	
	  ent = new Entity(Named.capitalise(singular));
	  System.out.println(">>> New entity: " + singular);
      modelElements.add(ent); 
	  res.add(ent); 
 	  derivedElements.add(ent); 
	  ent.addStereotype("originator=\"" + id + "\""); 
    } 
	else if (obj instanceof Entity)
	{ ent = (Entity) obj; }
	
    if (ent != null) 
	{ p2.extractRelationshipDefinitions(ent,modelElements); }
	
    return res;  
  }
 
  public Vector associationElements(java.util.Map fromBackground, Vector modelElements)
  { // assuming form "is associated with" ...

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
    if (noun == null || noun.length() == 0) 
	{ return res; }
	
	NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    Entity ent = null; 
	
	Object obj = ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
    if (obj == null) 
    { ent = new Entity(Named.capitalise(noun));
      modelElements.add(ent);
	  res.add(ent);  
      derivedElements.add(ent); 
	  ent.addStereotype("originator=\"" + id + "\""); 
      System.out.println(">>> Added new class: " + ent.getName()); 
    } 
	else if (obj instanceof Entity)
	{ ent = (Entity) obj; }
	
    if (ent != null) 
	{ p2.extractAssociationDefinitions(ent,null,fromBackground,modelElements); } 
	 
    return res;  
  }

  public Vector otherRelationElements(java.util.Map fromBackground, Vector modelElements)
  { // assuming form "X verb quantifier Y" ...

    Vector res = new Vector();
    NLPPhraseElement p1 = (NLPPhraseElement) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
	if (noun == null || noun.length() == 0)
	{ return res; }
	
	if (phrases.size() < 2) 
	{ return res; }
	
    NLPPhraseElement p2 = (NLPPhraseElement) phrases.get(1);
	boolean notfound = true; 
    for (int x = 1; x < phrases.size() && notfound; x++) 
	{ p2 = (NLPPhraseElement) phrases.get(x);
	  if (p2.isVerbPhrase()) 
	  { notfound = false; }
	} 
	
	Entity ent = null; 
	String singular = NLPWord.getSingular(noun); 
	
	Object obj = ModelElement.lookupByNameIgnoreCase(singular, modelElements); 
    if (obj == null) 
    { ent = new Entity(Named.capitalise(singular));
      System.out.println(">>> New class: " + ent.getName()); 
      modelElements.add(ent); 
	  res.add(ent); 
  	  derivedElements.add(ent); 
	  ent.addStereotype("originator=\"" + id + "\""); 
    } 
    else 
    { System.out.println(">>> Existing model element: " + singular); 
	  if (obj instanceof Entity) 
	  { ent = (Entity) obj; }
	} 
   
    if (notfound)
	{ System.err.println("!!! No subject part in this sentence: " + this); 
	  return res; 
	}
	
    String verb = p2.getMostSignificantVerb();    
    if (ent != null && verb != null && verb.length() > 0)
    { System.out.println(">>> possible association corresponding to verb " + verb); 
      p2.extractAssociationDefinitions(ent, verb, fromBackground, modelElements); 
    } 
    return res;  
  }

  public String getKM3(Vector elems, java.util.Map fromBackground)
  { String res = ""; 
    Vector quals = new Vector(); 
	java.util.HashMap verbClassifications = new java.util.HashMap(); 
	
    if (isSVO() && isSystemDefinition())
    { System.out.println(">>> System definition: " + this); 
	  Vector seqs = sequentialise(); 
	  Vector np1 = new Vector(); 
	  Vector vb1 = new Vector(); 
	  Vector rem = new Vector();   
	  Vector comment = new Vector(); 
	  Vector quals1 = new Vector(); 
	  splitIntoPhrases(seqs,np1,vb1,rem,comment); 
	  System.out.println(">>> Sentence split as: " + np1 + "; " + vb1 + "; " + rem + "; " + comment); 
	  
	  identifyClassesAndFeatures(fromBackground,rem,elems,quals1);
    }
	else if (isSVO() && isClassDefinition(quals))
    { System.out.println(">>> Class definition: " + this); 
      modelElements(fromBackground,elems);	 
    }
    else if (isSVO() && isAssociationDefinition())
    { System.out.println(">>> Association definition: " + this); 
      associationElements(fromBackground,elems);
    }
    else if (isSVO() && isGeneralDefinition())
    { System.out.println(">>> Generalisation definition: " + this); 
      relationElements(elems);
    }
    else if (isSVO())
    { System.out.println(">>> Constraint definition: " + this); 
      otherRelationElements(fromBackground,elems); 
    }
    else 
    { Vector seqs = sequentialise(); 
      Vector np1 = new Vector(); 
      Vector vb1 = new Vector(); 
      Vector rem = new Vector();   
	  Vector comment = new Vector(); 
      Vector quals1 = new Vector(); 
      splitIntoPhrases(seqs,np1,vb1,rem,comment); 
      System.out.println(">>> Not recognised as model elements definition: " + np1 + "; " + vb1 + "; " + rem);
      if (describesSystem(np1))
      { System.out.println(">>> It may be a system-level requirement"); 
	    identifyClassesAndFeatures(fromBackground,rem,elems,quals1); 
	  }  
      else if (describesUseCase(np1,vb1,rem))
      { System.out.println(">>> It may be a use case description"); 
	    // identifyUseCase(rem,elems); 
        
        System.out.println(">>> Split as follows (noun phrase, verb phrase, remainder, comment): " + np1 + "; " + vb1 + "; " + 
	                   rem + "; " + comment);
        java.util.Map mp = new java.util.HashMap(); 
	    UseCase uc = identifyUseCase(np1,vb1,rem,elems,mp,verbClassifications); 
	    System.out.println(">>> Identified use case " + uc); 
		
  	    identifyModelElements(uc,np1,vb1,rem,fromBackground,verbClassifications,mp,elems);   
	  }
	  else 
	  { identifyClassesAndFeatures(fromBackground,rem,elems,quals1); }
    }  
      
	String ucs = ""; 
	for (int i = 0; i < elems.size(); i++) 
    { if (elems.get(i) instanceof Type) 
      { Type tt = (Type) elems.get(i); 
        res = res + tt.getKM3() + "\n\n";
      }
	}
	
	
	for (int i = 0; i < elems.size(); i++) 
    { if (elems.get(i) instanceof Entity) 
      { Entity ent = (Entity) elems.get(i); 
        res = res + ent.getKM3() + "\n\n";
      }
	} 
	
	 
    for (int i = 0; i < elems.size(); i++) 
    { if (elems.get(i) instanceof UseCase)
      { UseCase ucx = (UseCase) elems.get(i);
	    // System.out.println(">>> Linked entity of " + ucx.getName() + " is " + ucx.ent);  
        ucs = ucs + ucx.getKM3() + "\n\n";
      } 
    }
    return res + ucs; 
  } 
  
  public void splitIntoPhrases(Vector seq, Vector np1, Vector vb1, Vector rem, Vector comment)
  { int i = 0; 
    int en = seq.size(); 
    Vector quals1 = new Vector(); 
	java.util.HashMap mp = new java.util.HashMap(); 
	
    boolean innoun1 = true; 
    while (i < en && innoun1)
    { NLPWord lex = (NLPWord) seq.get(i); 
      if (lex.isVerbPhraseWord(quals1,mp))
      { innoun1 = false; }
      else if (lex.isNounPhraseWord())
      { np1.add(lex);
        i++;  
      }
      else 
      { i++; } 
    } 
	
    boolean inverb1 = true;
    while (i < en && inverb1)
    { NLPWord lex = (NLPWord) seq.get(i); 
      if (i < en-1 && "so".equalsIgnoreCase(lex.text.trim()) && "that".equalsIgnoreCase(((NLPWord) seq.get(i+1)).text.trim()))
	  { inverb1 = false; }
	  else if (lex.isVerbPhraseWord(quals1,mp))
      { vb1.add(lex); 
        i++; 
      }
      else if (lex.isNounPhraseWord())
      { inverb1 = false; }
      else 
      { i++; } 
    } 
	
	int k = 0; 
	
    for (int j = i; j < en && k == 0; j++)
    { NLPWord lex = (NLPWord) seq.get(j); 
      if (j < en-1 && "so".equalsIgnoreCase(lex.text.trim()) && "that".equalsIgnoreCase(((NLPWord) seq.get(i+1)).text.trim()))
	  { k = j; 
	    break; 
	  }
	  else 
	  { rem.add(lex); }  
	  
    }
	
	if (k > 0) 
	{ for (int h = k; h < en; h++)
	  { comment.add(seq.get(h)); }
	} 
  }
  
  public boolean describesSystem(Vector np)
  { for (int i = 0; i < np.size(); i++) 
    { NLPWord wd = (NLPWord) np.get(i); 
      String textlc = wd.text.toLowerCase(); 
      if (textlc.startsWith("system") || textlc.startsWith("app"))
	  { return true; }
	}
    return false; 
  } 
  
  public void identifyClassesAndFeatures(java.util.Map fromBackground, Vector rem, Vector modelElements, Vector quals)
  { // First noun is usually a class, others features.
    // But may be a series of nouns "Patient Details"
	 
    String firstNoun = null; 
    int found = 0; 
    for (int i = 0; i < rem.size(); i++) 
    { NLPWord wd = (NLPWord) rem.get(i); 
	  // if (wd.text.equals("details")) { } 
	  // else 
      if (wd.isNoun() && firstNoun == null)
      { firstNoun = wd.getSingular();
        found = i;  
      }
      else if (wd.isNoun() && firstNoun != null) 
      { firstNoun = firstNoun + wd.getSingular(); 
        found = i; 
      } 
      else if (firstNoun != null)
      { break; } 
    }
	
    if (firstNoun == null) 
    { return; }
	
    System.out.println(">>> first noun: " + firstNoun); 
    Entity mainent = (Entity) ModelElement.lookupByNameIgnoreCase(firstNoun,modelElements); 
    if (mainent == null) 
    { mainent = new Entity(Named.capitalise(firstNoun)); 
      modelElements.add(mainent); 
      derivedElements.add(mainent); 
	  mainent.addStereotype("originator=\"" + id + "\""); 
      System.out.println(">>> Added new class " + mainent.getName()); 
    } 
	
    mainent.addStereotypes(quals); 
	
    Vector remnouns = new Vector(); 
    Vector remwords = new Vector(); 
    for (int j = found+1; j < rem.size(); j++) 
    { NLPWord wx = (NLPWord) rem.get(j); 
      if (wx.isNoun())
      { remnouns.add(wx); }
      remwords.add(wx); 
    } // ignore words like "information" and "details"? 
	
    System.out.println(">>> other nouns: " + remnouns); 
    java.util.Map quals1 = new java.util.HashMap(); 
	
    for (int j = 0; j < remnouns.size(); j++) 
    { NLPWord attx = (NLPWord) remnouns.get(j); 
      String attname = attx.text; 
      java.util.Map types = new java.util.HashMap(); 
      NLPPhrase.extractAtt(this,attx,attname,quals1,types,mainent,modelElements); 
    }

    NLPPhrase newpr = new NLPPhrase("NP"); 
    newpr.elements = remwords; 
    java.util.Map mp = new java.util.HashMap(); 
    Vector currentQuals = new Vector(); 
    java.util.Map types1 = new java.util.HashMap(); 
    Vector anal = newpr.extractNouns(mp,types1,fromBackground,currentQuals); 
    System.out.println(">>> identified features: " + anal); 
    System.out.println(">>> identified qualifiers: " + mp);
    System.out.println(">>> identified types: " + types1);
	applyTypes(mainent,anal,types1); 
    applyQualifiers(mainent,anal,mp);  
  }

  public void applyTypes(Entity ent, Vector features, java.util.Map types)
  { for (int i = 0; i < features.size(); i++) 
    { String fname = (String) features.get(i);
      Attribute f = ent.getAttribute(fname); 
      if (f == null)
      { continue; }
 
      Type t = (Type) types.get(f.getName()); 
      if (t != null) 
	  { f.setType(t); }
    }
  }
  
  public void applyQualifiers(Entity ent, Vector features, java.util.Map quals)
  { for (int i = 0; i < features.size(); i++) 
    { String fname = (String) features.get(i);
      Attribute f = ent.getAttribute(fname); 
      if (f == null)
      { continue; }
 
      Vector quallist = (Vector) quals.get(f.getName()); 
      for (int j = 0; j < quallist.size(); j++) 
      { String qual = (String) quallist.get(j); 
        if (qual != null)
        { if (qual.equals("many") && !f.isCollection())
          { Type atype = f.getType(); 
             Type colltype = new Type("Sequence", null);
             colltype.setElementType(atype); 
             f.setType(colltype); 
             f.setElementType(atype); 
          } 
          else if (qual.equals("identity"))
          { f.setUnique(true); }
        }  
      }
    }
  }
  
  public boolean describesUseCase(Vector np, Vector vb1, Vector rem)
  { if (np.size() == 0 && vb1.size() == 1 && rem.size() >= 5)
    { NLPWord vb = (NLPWord) vb1.get(0); 
      if (vb.text.equalsIgnoreCase("As"))
      { return true; }
    } 
    return false; 
  }	  
	  
  /* No longer used */ 
  public java.util.Map identifyUseCase(Vector rem, Vector elems)
  { int index = 0; 
    Vector quals = new Vector(); 
    java.util.Map mp = new java.util.HashMap(); 
  
    for (int i = 0; i < rem.size(); i++) 
    { NLPWord wd = (NLPWord) rem.get(i); 
      String textlc = wd.text.toLowerCase(); 
      if (textlc.startsWith("wish") || textlc.startsWith("want"))
      { index = i+2; } 
    } // also "be able to"
	
    if (index == 0) 
    { return mp; }
	
    String uc = ""; 
    Entity ent = null; 
	
    for (int j = index; j < rem.size(); j++) 
    { NLPWord wd = (NLPWord) rem.get(j); 
      if (wd.isVerbPhraseWord(quals,mp) || wd.isAdjective() || wd.isNounPhraseWord() || wd.isConjunctionWord())
      { uc = uc + wd.text; }  
	  if (ent == null) 
	  { ent = (Entity) ModelElement.lookupByNameIgnoreCase(wd.text,elems); } 
    }
	
    if (uc.length() > 0)
    { UseCase ucase = (UseCase) ModelElement.lookupByNameIgnoreCase(uc,elems); 
      if (ucase != null) 
      { System.out.println(">>> Duplicate use case: " + uc); } 
      else 
      { ucase = new UseCase(uc);
        System.out.println(">>> New use case: " + uc); 
        elems.add(ucase); 
	    derivedElements.add(ucase); 
        ucase.addStereotype("originator=\"" + id + "\""); 
      } 
	  
      if (ent != null) 
      { System.out.println(">>> use case on entity " + ent); 
        if (uc.indexOf("creat") >= 0) 
        { ucase.setResultType(new Type(ent)); 
		  ucase.defineCreateCode(ent); 
		} 
		if (uc.indexOf("edit") >= 0 || uc.indexOf("delete") >= 0)
		{ String ex = ent.getName().toLowerCase(); 
		  ucase.addParameter(ex, new Type(ent)); 
		}
      } // and other cases
    }  
	
    return mp; 
  } 

  public UseCase identifyUseCase(Vector np1, Vector vb1, Vector rem, Vector elems, java.util.Map mp, java.util.Map verbClassifications)
  { int index = 0; 
    Vector quals = new Vector(); 
    String uc = ""; 
	String shortName = ""; 
    String actor = null; // Normally the noun of the noun clause (if any) which precedes the first verb. 
	
	if (np1.size() > 0)
	{ actor = NLPPhrase.getPrincipalNoun(np1); 
	  if (actor != null && actor.length() > 0)
	  { System.out.println(">>> Use case Actor is " + Named.capitalise(actor)); }
	  else 
	  { actor = null; }
	}
	
	if (vb1.size() == 0 && rem.size() == 0) // take all words from np1
	{ for (int j = 0; j < np1.size(); j++) 
      { NLPWord wd = (NLPWord) np1.get(j); 
        uc = uc + wd.text;   
		shortName = shortName + wd.text; 
      }
	}
	else if (np1.size() > 0)
	{ for (int j = 0; j < np1.size() && index == 0; j++) 
      { NLPWord wd = (NLPWord) np1.get(j); 
        if (wd.isVerbPhraseWord(quals,mp))
		{ uc = uc + wd.text; 
		  index = j+1; 
		}  
	  } 
		
      if (index > 0)
	  { for (int j = index; j < np1.size(); j++) 
        { NLPWord wd = (NLPWord) np1.get(j); 
          if (wd.isVerbPhraseWord(quals,mp) || wd.isAdjective() || wd.isNounPhraseWord() || wd.isConjunctionWord())
          { uc = uc + wd.text; }  
		} 
      }
	}
	  
	index = 0; 
	
    if (vb1.size() > 0)
	{ // starts with a verb "Update/create/add etc ...."
	
	  if (actor == null) 
	  { actor = NLPPhrase.getPrincipalNoun(vb1); 
	    if (actor != null && actor.length() > 0)
	    { System.out.println(">>> Use case Actor is " + Named.capitalise(actor)); }
		else 
		{ actor = null; }
	  }
	  
	  for (int j = 0; j < vb1.size(); j++) 
      { NLPWord wd = (NLPWord) vb1.get(j); 
        if (index == 0 && wd.isSignificantVerbPhraseWord(verbClassifications,quals,mp))
		{ uc = uc + wd.text; 
		  index = j+1;
		  shortName = shortName + wd.text;  
		} 
		else if (wd.isVerbPhraseWord(quals,mp) || wd.isAdjective() || 
		      wd.isNounPhraseWord() || wd.isConjunctionWord())
        { uc = uc + Named.capitalise(wd.text); 
	      if (index > 0) 
	      { shortName = shortName + Named.capitalise(wd.text); } 
		}    
		
        // Entity ent = (Entity) ModelElement.lookupByNameIgnoreCase(wd.text,elems);
		// if (ent != null) 
		// { System.out.println(">>> verb clause refers to entity " + ent); } 
      }
	}
	
    /* for (int i = 0; i < rem.size(); i++) 
    { NLPWord wd = (NLPWord) rem.get(i); 
      String textlc = wd.text.toLowerCase(); 
      if (textlc.startsWith("wish") || textlc.startsWith("want"))
      { index = i+2; } 
    }
	
    if (index == 0) { return; }
	*/ 
	
	// index = 0; 

	if (actor == null && index == 0) // no verb yet 
	{ actor = NLPPhrase.getPrincipalNoun(rem); 
      if (actor != null && actor.length() > 0)
      { System.out.println(">>> Use case Actor is " + Named.capitalise(actor)); }
	  else 
	  { actor = null; }
    }
    
    for (int j = 0; j < rem.size(); j++) 
    { NLPWord wd = (NLPWord) rem.get(j); 
      if (wd.text.equals("so"))
	  { break; } 

      if (index == 0 && wd.isSignificantVerbPhraseWord(verbClassifications,quals,mp))
      { uc = uc + wd.text; 
        index = j+1;
	    shortName = shortName + wd.text;  
      } 
      else if (wd.isVerbPhraseWord(quals,mp) || wd.isAdjective() || 
		       wd.isNounPhraseWord() || wd.isConjunctionWord())
      { uc = uc + Named.capitalise(wd.text); 
        if (index > 0) 
        { shortName = shortName + Named.capitalise(wd.text); } 
      }    
    }
	
	UseCase ucase = null; 
	
    if (shortName.length() == 0)
	{ shortName = uc; }
	
	if (shortName.length() > 0)
    { Object obj = ModelElement.lookupByNameIgnoreCase(shortName,elems); 
      if (obj != null) 
      { System.out.println(">>> Duplicate element: " + shortName); } 
      else 
      { String sname = Named.decapitalise(shortName); 
	    ucase = new UseCase(sname);
        System.out.println(">>> New use case: " + sname + " qualifiers: " + quals + " mapping: " + mp); 
        elems.add(ucase);
   	    derivedElements.add(ucase); 
	    ucase.addStereotype("originator=\"" + id + "\""); 

        /* Vector vals = new Vector(); 
        vals.addAll(mp.values()); 
        ucase.addStereotypes(vals);  
        if (vals.size() == 0)
        { ucase.addStereotype("other"); } */ 
		
        if (actor != null) 
		{ String actr = Named.capitalise(actor); 
		  ucase.addStereotype("actor=\"" + actr + "\""); 
		  Object actobj = ModelElement.lookupByNameIgnoreCase(actr,elems);
		  if (actobj != null && (actobj instanceof Entity))
		  { String aname = actor.toLowerCase() + "x"; 
		    ucase.addParameter(aname, new Type((Entity) actobj)); 
		  }
		  else 
		  { Entity newent = new Entity(actr); 
   		    System.out.println(">>> New entity for use case actor: " + actr); 
            elems.add(newent);
    	    derivedElements.add(newent); 
	        newent.addStereotype("originator=\"" + id + "\""); 
		    String aname = actor.toLowerCase() + "x"; 
		    ucase.addParameter(aname, new Type(newent)); 
		  }	   
		}  
      } 
    }  
	
	return ucase; 
  } 


  public java.util.Map classifyWords(Vector background, Vector modelElements)
  { java.util.HashMap res = new java.util.HashMap(); 
    for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement phr = (NLPPhraseElement) phrases.get(i); 
      java.util.HashMap mp = phr.classifyWords(background, modelElements); 
      res.putAll(mp);  
    } 
    return res; 
  } 
  
  public java.util.Map classifyVerbs(Vector verbs)
  { java.util.HashMap res = new java.util.HashMap(); 
    for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement phr = (NLPPhraseElement) phrases.get(i); 
      java.util.HashMap mp = phr.classifyVerbs(verbs); 
      res.putAll(mp);  
    } 
    return res; 
  } 

  public String getBehaviourKM3(Vector seqs, Vector elems, Vector background, java.util.Map fromBackground, java.util.Map verbClassifications)
  { String res = ""; 
    Vector quals = new Vector(); 
    // Vector seqs = sequentialise(); 
    Vector np1 = new Vector(); 
    Vector vb1 = new Vector(); 
    Vector rem = new Vector();   
    Vector comment = new Vector(); 
    java.util.Map mp = new java.util.HashMap(); 

    splitIntoPhrases(seqs,np1,vb1,rem,comment); 
    System.out.println(">>> Split as follows (noun phrase, verb phrase, remainder, comment): " + np1 + "; " + vb1 + "; " + 
	                   rem + "; " + comment);
    UseCase uc = identifyUseCase(np1,vb1,rem,elems,mp,verbClassifications); 
    System.out.println(">>> Identified use case " + uc); 
	
    identifyModelElements(uc,np1,vb1,rem,fromBackground,verbClassifications,mp,elems);   
      
    String ucs = ""; 
	
    for (int i = 0; i < elems.size(); i++) 
    { if (elems.get(i) instanceof Type) 
      { Type tt = (Type) elems.get(i); 
        res = res + tt.getKM3() + "\n\n";
      }
    }
	
	
    for (int i = 0; i < elems.size(); i++) 
    { if (elems.get(i) instanceof Entity) 
      { Entity ent = (Entity) elems.get(i); 
        // check if it has a superclass in elems, according to
        // background: 
        ThesaurusConcept tc = Thesarus.lookupWord(background,ent.getName()); 
        if (tc != null && tc.getGeneralisation() != null) 
        { String gen = tc.getGeneralisation(); 
          Object gencl = ModelElement.lookupByName(gen,elems); 
          if (gencl != null && (gencl instanceof Entity))
          { ent.setSuperclass((Entity) gencl); } 
        } 
        res = res + ent.getKM3() + "\n\n";
      }
    } 
	
	 
    for (int i = 0; i < elems.size(); i++) 
    { if (elems.get(i) instanceof UseCase)
      { UseCase ucx = (UseCase) elems.get(i);
	   ucs = ucs + ucx.getKM3() + "\n\n";
      } 
    }
    return res + ucs; 
  } 

  public void identifyModelElements(UseCase uc, Vector np1, Vector vb1, Vector rem, 
                                    java.util.Map fromBackground, java.util.Map verbClassifications, 
									java.util.Map mp, Vector elems)
  { // check if the definite classes and features already exist, if not, create them 
  
    // After a "create", "delete" we expect a class name 
	// After an "edit", "read" expect attributes & posssibly a class.
	
	// System.out.println("MP= " + mp); 
	// System.out.println("UC= " + uc); 
	
	if (uc == null) 
	{ return; }
	 
	int index = 0; 
	Vector quals = new Vector(); 
	Vector localElems = new Vector(); // in this use case
	// String currStereo = null; 
	Vector foundStereotypes = new Vector(); 
	
    if (vb1.size() > 0)
	{ // starts with a verb "Update/create/add etc ...."
	
	  for (int j = 0; j < vb1.size(); j++) 
      { NLPWord wd = (NLPWord) vb1.get(j); 
        if (index > 0) 
		{ addWordSemantics(wd,uc,elems,localElems,fromBackground,mp); }
		
        if (wd.isSignificantVerbPhraseWord(verbClassifications,quals,mp))
		{ index = j+1;     
		  String stereotype = (String) mp.get(wd.text + ""); 
          if (stereotype != null)
          { System.out.println("Found stereotype >> " + stereotype); 
	        if (foundStereotypes.size() == 0) 
			{ foundStereotypes.add(stereotype); 
			  uc.addStereotype(stereotype); 
			} 
	      }
        } 		    
      }
	}
	
	
    for (int j = 0; j < rem.size(); j++) 
    { NLPWord wd = (NLPWord) rem.get(j); 
      if (index > 0) 
	  { addWordSemantics(wd,uc,elems,localElems,fromBackground,mp); }
		
      if (wd.isSignificantVerbPhraseWord(verbClassifications,quals,mp))
	  { index = j+1; 
        String stereotype = (String) mp.get(wd.text + ""); 
        if (stereotype != null)
        { System.out.println("Current stereotype >> " + stereotype); 
	      if (foundStereotypes.size() == 0) 
          { foundStereotypes.add(stereotype); 
            uc.addStereotype(stereotype); 
          } 
	    }
      } 
    }
  }

  private void addWordSemantics(NLPWord wd, UseCase uc, Vector elems, Vector localElems, java.util.Map fromBackground, java.util.Map mp)  
  { // System.out.println("MP= " + mp); 
	// System.out.println("UC= " + uc); 
	 
	if (uc == null) 
	{ return; }
	else 
    { ModelElement elm = ModelElement.findElementByNameIgnoreCase(wd.text,elems);
	  if (elm == null && wd.isPlural()) 
	  { String sing = wd.getSingular(); 
	    elm = ModelElement.findElementByNameIgnoreCase(sing,elems);
	  }
	  
      if (elm != null) 
      { System.out.println(">>> Linked existing model element: " + elm); 
        if (elm instanceof Entity)
        { localElems.add(elm);
          uc.ent = (Entity) elm; 
          Vector stereos = uc.getStereotypes(); 
          if (stereos.contains("create") && uc.hasNoResult())
          { uc.setResultType(new Type(uc.ent)); 
		    uc.defineCreateCode(uc.ent); 
		  }  // Only for the *first* entity after the main verb. 
          if (stereos.contains("edit") ||
                stereos.contains("persistent") ||
                stereos.contains("delete") || 
                stereos.contains("read"))
          { String ex = uc.ent.getName().toLowerCase() + "x"; 
            if (stereos.contains("read"))
            { uc.defineReadCode(ex,uc.ent); } 
            uc.addParameter(ex, new Type(uc.ent)); 
          }
          // resolve any unattached attributes at this point
        } 
	  }
	  else 
	  { Vector knd = (Vector) fromBackground.get(wd.text); 
        if (knd != null && knd.size() > 0)
        { System.out.println(">>> Model element from knowledge base: " + wd.text + " --> " + knd); 
          elm = (ModelElement) knd.get(0);
          if (elm instanceof Entity) 
          { Entity mainent = new Entity(Named.capitalise(wd.text));
            System.out.println(">>> Added new class " + wd.text);  
            elems.add(mainent);
            localElems.add(mainent);
			derivedElements.add(mainent); 
	        mainent.addStereotype("originator=\"" + id + "\""); 

            if (uc != null) 
            { uc.ent = mainent;  
  		      Vector stereos = uc.getStereotypes(); 
              if (stereos.contains("create") && uc.hasNoResult())
              { uc.setResultType(new Type(uc.ent)); 
			    uc.defineCreateCode(uc.ent); 
		      }
              if (stereos.contains("edit") ||
                  stereos.contains("persistent") || 
                  stereos.contains("delete") || stereos.contains("other") ||  
                  stereos.contains("read"))
              { String ex = uc.ent.getName().toLowerCase() + "x";
			    if (stereos.contains("read"))
				{ uc.defineReadCode(ex,uc.ent); } 
                uc.addParameter(ex, new Type(uc.ent)); 
              }
            }
          } 
          else if (elm instanceof Attribute)
          { String attname = Named.decapitalise(wd.text); 
				   
            if (uc != null) 
            { Vector stereos = uc.getStereotypes(); 
              if (stereos.contains("edit") || stereos.contains("create"))
              { uc.addParameter(attname + "x", ((Attribute) elm).getType()); }
            } 
            Entity ent = ModelElement.featureBelongsTo(wd.text, localElems); 
            if (ent == null) 
            { ent = ModelElement.featureBelongsTo(wd.text, elems); }
              boolean addedToClass = false; 
              if (ent == null && localElems.size() > 0)
              { // add to the first class there.
                for (int k = 0; k < localElems.size() && !addedToClass; k++) 
                { ModelElement mm = (ModelElement) localElems.get(k); 
                  if (mm instanceof Entity)
                  { Attribute newatt = new Attribute(attname, ((Attribute) elm).getType(), ModelElement.INTERNAL); 
                    ent = (Entity) mm; 
                    ent.addAttribute(newatt); 
                    addedToClass = true; 
				    derivedElements.add(newatt); 
	                ent.addStereotype("modifiedFeatures=\"" + id + "\""); 
                  }
                } 
              }
              else if (!addedToClass)
              { localElems.add(elm); } // add it when a class is found
           } 
         }
	  }
	}  
	// return currentStereotype; 
  }
} 