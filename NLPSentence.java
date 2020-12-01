import java.util.Vector; 

public class NLPSentence
{ Vector phrases = new Vector();  // of NLPPhrase
  
  public NLPSentence()
  { }

  public NLPSentence(String tag, Vector phs)
  { phrases = phs; }
  
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

  public boolean isClassDefinition()
  { // The VP verb is "consists"/"has"/"have", etc
    
	String verb = getMainVerb(); 
	
    if ("has".equals(verb) || "have".equals(verb) ||
        "comprises".equals(verb) || "consists".equals(verb))
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
    String verb = getMainVerb(); 
    if ("linked".equals(verb) || "associated".equals(verb) || "connected".equals(verb) || 
	    "related".equals(verb))
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
    p2.extractAssociationDefinitions(ent,modelElements); 
    res.add(ent);  
    return res;  
  }

  public String getKM3(Vector elems)
  { if (isSVO() && isClassDefinition())
    { System.out.println(">>> Class definition: " + this); 
      String res = ""; 
      modelElements(elems);
	 
      for (int i = 0; i < elems.size(); i++) 
      { Entity ent = (Entity) elems.get(i); 
        res = res + ent.getKM3() + "\n\n"; 
      }
      return res; 
    }
    else if (isSVO() && isAssociationDefinition())
    { System.out.println(">>> Association definition: " + this); 
      String res = ""; 
      associationElements(elems);
	 
      for (int i = 0; i < elems.size(); i++) 
      { Entity ent = (Entity) elems.get(i); 
        res = res + ent.getKM3() + "\n\n"; 
      }
      return res;
    }
    else if (isSVO() && isGeneralDefinition())
    { System.out.println(">>> General definition: " + this); 
      String res = ""; 
      relationElements(elems);
	 
      for (int i = 0; i < elems.size(); i++) 
      { Entity ent = (Entity) elems.get(i); 
        res = res + ent.getKM3() + "\n\n"; 
      }
      return res;
    }
    else
    { System.out.println(">>> Not recognised as model elements definition: " + this);  
      return "";
    }  
  } 
} 