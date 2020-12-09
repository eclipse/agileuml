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

  public boolean isClassDefinition()
  { // The VP verb is "consists"/"has"/"have", etc
    
    String verb = getMainVerb(); 
	
    if ("has".equals(verb) || "have".equals(verb) ||
        verb.startsWith("compris") || verb.startsWith("consist") || verb.equals("composed"))
    { return true; } 
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
    if (isSVO() && isClassDefinition())
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
    else if (isSVO())
    { System.out.println(">>> Constraint definition: " + this); 
      otherRelationElements(elems); 
    }
    else 
    { Vector seqs = sequentialise(); 
	  Vector np1 = new Vector(); 
	  Vector vb1 = new Vector(); 
	  Vector rem = new Vector();   
	  splitIntoPhrases(seqs,np1,vb1,rem); 
	  System.out.println(">>> Not recognised as model elements definition: " + np1 + "; " + vb1 + "; " + rem);
	  if (describesSystem(np1))
	  { identifyClassesAndFeatures(rem,elems); }  
    }  
      
    for (int i = 0; i < elems.size(); i++) 
    { Entity ent = (Entity) elems.get(i); 
      res = res + ent.getKM3() + "\n\n"; 
    }
    return res; 
  } 
  
  public void splitIntoPhrases(Vector seq, Vector np1, Vector vb1, Vector rem)
  { int i = 0; 
    int en = seq.size(); 
    boolean innoun1 = true; 
    while (i < en && innoun1)
    { NLPWord lex = (NLPWord) seq.get(i); 
	  if (lex.isVerbPhraseWord())
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
	  if (lex.isVerbPhraseWord())
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
  
  public void identifyClassesAndFeatures(Vector rem, Vector modelElements)
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
	
	System.out.println(">>> first noun: " + firstNoun); 
	Entity mainent = (Entity) ModelElement.lookupByNameIgnoreCase(firstNoun,modelElements); 
	if (mainent == null) 
	{ mainent = new Entity(firstNoun); 
	  modelElements.add(mainent); 
	} 
	
	Vector remnouns = new Vector(); 
	for (int j = found+1; j < rem.size(); j++) 
	{ NLPWord wx = (NLPWord) rem.get(j); 
	  if (wx.isNoun())
	  { remnouns.add(wx); }
	}
	
	System.out.println(">>> other nouns: " + remnouns); 
	Vector quals = new Vector(); 
	
	for (int j = 0; j < remnouns.size(); j++) 
    { NLPWord attx = (NLPWord) remnouns.get(j); 
	  String attname = attx.text; 
      NLPPhrase.extractAtt(attname,quals,mainent); 
    }
  }
} 