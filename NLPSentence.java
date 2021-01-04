import java.util.Vector; 

public class NLPSentence
{ Vector phrases = new Vector();  // of NLPPhrase
  
  public NLPSentence()
  { }

  public NLPSentence(String tag, Vector phs)
  { phrases = phs; }
  
  public void indexing()
  { int st = 1; 
    for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) phrases.get(i); 
      st = elem.indexing(st); 
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
		  nouns.contains("database") || nouns.contains("Database"))
	  { return true; }
	}
	return false; 
  }
  
  public String getMainVerb()
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
	
    if ("has".equals(verb) || "have".equals(verb) ||
        verb.startsWith("compris") || verb.startsWith("consist") || verb.equals("composed")) 
	{ return true; } 
	else if (verb.equals("records") || verb.equals("stores"))
    { quals.add("persistent"); 
	  return true; 
	} 
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
    if ("linked".equals(verb) || "associated".equals(verb) || "connected".equals(verb) || "for".equals(verb) || "belongs".equals(verb) || 
	   verb.startsWith("relate"))
    { return true; } 
    return false; 
  } 

  public Vector modelElements(Vector modelElements)
  { // assuming SVO && isClassDefinition

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    if (p2.tag.equals("ADVP"))
    { p2 = (NLPPhrase) phrases.get(2); }
	
    Entity ent = (Entity) ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
    if (ent == null) 
    { ent = new Entity(Named.capitalise(noun));
      modelElements.add(ent); 
      System.out.println(">>> New entity " + noun); 
    } 
    p2.extractAttributeDefinitions(ent); 
    res.add(ent);  
    return res;  
  } 

  public Vector relationElements(Vector modelElements)
  { // assuming SVO && isGeneralDefinition

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    Entity ent = (Entity) ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
    if (ent == null) 
    { ent = new Entity(Named.capitalise(noun));
	  System.out.println(">>> New entity: " + noun);
      modelElements.add(ent); 
    } 
    p2.extractRelationshipDefinitions(ent,modelElements); 
    res.add(ent);  
    return res;  
  }
 
  public Vector associationElements(Vector modelElements)
  { // assuming form "is associated with" ...

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    Entity ent = (Entity) ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
    if (ent == null) 
    { ent = new Entity(Named.capitalise(noun));
      modelElements.add(ent); 
    } 
    p2.extractAssociationDefinitions(ent,null,modelElements); 
    res.add(ent);  
    return res;  
  }

  public Vector otherRelationElements(Vector modelElements)
  { // assuming form "X verb quantifier Y" ...

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    Entity ent = (Entity) ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
    if (ent == null) 
    { ent = new Entity(Named.capitalise(noun));
      System.out.println(">>> New class: " + noun); 
      modelElements.add(ent); 
    } 
    else 
    { System.out.println(">>> Existing class: " + noun); }
   
    String verb = p2.getMostSignificantVerb();    
    if (verb != null && verb.length() > 0)
    { System.out.println(">>> association corresponding to verb " + verb); 
      p2.extractAssociationDefinitions(ent, verb, modelElements); 
    } 
    res.add(ent);  
    return res;  
  }

  public String getKM3(Vector elems)
  { String res = ""; 
    Vector quals = new Vector(); 
    if (isSVO() && isClassDefinition(quals))
    { System.out.println(">>> Class definition: " + this); 
      modelElements(elems);	 
    }
    else if (isSVO() && isAssociationDefinition())
    { System.out.println(">>> Association definition: " + this); 
      associationElements(elems);
    }
    else if (isSVO() && isGeneralDefinition())
    { System.out.println(">>> General definition: " + this); 
      relationElements(elems);
    }
    else if (isSVO() && isSystemDefinition())
    { System.out.println(">>> System definition: " + this); 
	  Vector seqs = sequentialise(); 
	  Vector np1 = new Vector(); 
	  Vector vb1 = new Vector(); 
	  Vector rem = new Vector();   
	  Vector quals1 = new Vector(); 
	  splitIntoPhrases(seqs,np1,vb1,rem,quals1); 
	  identifyClassesAndFeatures(rem,elems,quals1);
    }
    else if (isSVO())
    { System.out.println(">>> Constraint definition: " + this); 
      otherRelationElements(elems); 
    }
    else 
    { Vector seqs = sequentialise(); 
      Vector np1 = new Vector(); 
      Vector vb1 = new Vector(); 
      Vector rem = new Vector();   
	  Vector quals1 = new Vector(); 
      splitIntoPhrases(seqs,np1,vb1,rem,quals1); 
      System.out.println(">>> Not recognised as model elements definition: " + np1 + "; " + vb1 + "; " + rem);
      if (describesSystem(np1))
      { identifyClassesAndFeatures(rem,elems,quals1); }  
      else if (describesUseCase(np1,vb1,rem))
      { identifyUseCase(rem,elems); }
    }  
      
    for (int i = 0; i < elems.size(); i++) 
    { if (elems.get(i) instanceof Entity) 
	  { Entity ent = (Entity) elems.get(i); 
        res = res + ent.getKM3() + "\n\n";
	  } 
	  else if (elems.get(i) instanceof UseCase)
	  { UseCase uc = (UseCase) elems.get(i); 
        res = res + uc.getKM3() + "\n\n";
	  } 
    }
    return res; 
  } 
  
  public void splitIntoPhrases(Vector seq, Vector np1, Vector vb1, Vector rem, Vector quals)
  { int i = 0; 
    int en = seq.size(); 
	Vector quals1 = new Vector(); 
	
    boolean innoun1 = true; 
    while (i < en && innoun1)
    { NLPWord lex = (NLPWord) seq.get(i); 
	  if (lex.isVerbPhraseWord(quals1))
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
      if (lex.isVerbPhraseWord(quals1))
      { vb1.add(lex); 
        i++; 
      }
      else if (lex.isNounPhraseWord())
      { inverb1 = false; }
      else 
      { i++; } 
    } 
	
    for (int j = i; j < en; j++)
    { NLPWord lex = (NLPWord) seq.get(j); 
      rem.add(lex); 
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
  
  public void identifyClassesAndFeatures(Vector rem, Vector modelElements, Vector quals)
  { // First noun is usually a class, others features. 
    String firstNoun = null; 
    int found = 0; 
    for (int i = 0; i < rem.size(); i++) 
    { NLPWord wd = (NLPWord) rem.get(i); 
	  // if (wd.text.equals("details")) { } 
	  // else 
      if (wd.isNoun())
      { firstNoun = wd.getSingular();
        found = i;  
        break; 
      }
    }
	
	if (firstNoun == null) 
	{ return; }
	
    System.out.println(">>> first noun: " + firstNoun); 
    Entity mainent = (Entity) ModelElement.lookupByNameIgnoreCase(firstNoun,modelElements); 
    if (mainent == null) 
    { mainent = new Entity(Named.capitalise(firstNoun)); 
      modelElements.add(mainent); 
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
    Vector quals1 = new Vector(); 
	
    for (int j = 0; j < remnouns.size(); j++) 
    { NLPWord attx = (NLPWord) remnouns.get(j); 
      String attname = attx.text; 
      NLPPhrase.extractAtt(attname,quals1,mainent); 
    }

    NLPPhrase newpr = new NLPPhrase("NP"); 
    newpr.elements = remwords; 
    java.util.HashMap mp = new java.util.HashMap(); 
    Vector currentQuals = new Vector(); 
    Vector anal = newpr.extractNouns(mp,currentQuals); 
    System.out.println(">>> identified features: " + anal); 
    System.out.println(">>> identified qualifiers: " + mp);
	applyQualifiers(mainent,anal,mp);  
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
	  
  public void identifyUseCase(Vector rem, Vector elems)
  { int index = 0; 
    Vector quals = new Vector(); 
  
    for (int i = 0; i < rem.size(); i++) 
    { NLPWord wd = (NLPWord) rem.get(i); 
      String textlc = wd.text.toLowerCase(); 
      if (textlc.startsWith("wish") || textlc.startsWith("want"))
      { index = i+2; } 
    }
	
    if (index == 0) { return; }
	
    String uc = ""; 
	
    for (int j = index; j < rem.size(); j++) 
    { NLPWord wd = (NLPWord) rem.get(j); 
      if (wd.isVerbPhraseWord(quals) || wd.isAdjective() || wd.isNounPhraseWord() || wd.isConjunctionWord())
      { uc = uc + wd.text; }  
    }
	
    if (uc.length() > 0)
    { UseCase ucase = (UseCase) ModelElement.lookupByNameIgnoreCase(uc,elems); 
      if (ucase != null) 
      { System.out.println(">>> Duplicate use case: " + uc); } 
      else 
      { ucase = new UseCase(uc);
        System.out.println(">>> New use case: " + uc); 
        elems.add(ucase); 
      } 
    }  
  } 

} 