import java.util.Vector; 

public class NLPPhrase extends NLPPhraseElement
{ Vector elements = new Vector();  // of NLPPhraseElement
  
  public NLPPhrase(String tg)
  { super(tg); }
  
  public NLPPhrase(String tg, Vector phs)
  { super(tg); 
    elements = phs; 
  }

  public void addElement(NLPPhraseElement elem)
  { elements.add(elem); }
  
  public String toString()
  { String res = "(" + tag + " "; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      res = res + " " + elem; 
    }
    return res + ")"; 
  }

  public boolean isVerbPhrase() 
  { return "VP".equals(tag); } 

  public boolean allWords()
  { for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) { } 
      else 
      { return false; }  
    }
    return true; 
  }

  public boolean allWordsOrADJPs()
  { for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) { } 
      else if (elem instanceof NLPPhrase && 
               "ADJP".equals(((NLPPhrase) elem).tag))
      { } 
      else if (elem instanceof NLPPhrase && 
               "NML".equals(((NLPPhrase) elem).tag))
      { } 
      else 
      { return false; }  
    }
    return true; 
  }

  public String getPrincipalNoun()
  { String noun = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag; 
        if (lex.equals("NNPS") || lex.equals("NNS")) 
        { noun = noun + word.getSingular(); } 
        else if (lex.equals("NN") || lex.equals("NNP"))
        { noun = noun + word.text; } 
      } 
    } 
    return noun; 
  }   

  public Vector getNouns()
  { Vector nouns = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag;
        if (word.isKeyword()) { }  
        else if (lex.equals("NNPS") || lex.equals("NNS") ||
            lex.equals("NN") || lex.equals("NNP"))
        { nouns.add(word.text); } 
      } 
    } 
    return nouns; 
  }   
  
  public String identifyNoun()
  { String res = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        if (word.isNoun() || word.isAdjective())
        { res = res + Named.capitalise(word.text); } 
      } 
    } 
    return res; 
  }

  public String formQualifier()
  { String res = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        res = res + word.text; 
      } 
    } 
    return res; 
  }
  
  public Vector getQualifiers()
  { Vector quals = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag; 
        if (lex.equals("JJ") || lex.equals("JJS") || lex.equals("CD") || 
            lex.equals("JJR") || word.text.equals("unique") ||
            word.text.startsWith("numeric") || word.text.startsWith("integer") ||
            word.text.startsWith("realvalue") || word.text.startsWith("real-value") || 
            word.text.startsWith("realnumber") || word.text.startsWith("real-number") ||
            word.text.equals("real") || 
            word.text.startsWith("wholenumber") || word.text.startsWith("whole-number") ||
            word.text.startsWith("string") || word.text.equals("many") || 
            word.text.startsWith("bool"))
        { quals.add(word.text); } 
      } 
      else if (elem.tag.equals("ADJP") || elem.tag.equals("NML"))
      { String qual = elem.formQualifier(); 
        if (qual != null) 
        { quals.add(qual); }
      }
      else if (elem instanceof NLPPhrase)
      { Vector qualssub = ((NLPPhrase) elem).getQualifiers(); 
        quals.addAll(qualssub); 
      } 
    } 
    return quals; 
  }   

  public String getPrincipalVerb()
  { String verb = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag; 
        if (lex.equals("VB") || lex.equals("VBZ") || 
            lex.equals("VBG") || lex.equals("VBD") ||
            lex.equals("VBN") || lex.equals("VBP"))
        { return word.text; } 
      } 
    } 
    return verb; 
  }   

  public void extractAttributeDefinitions(Entity ent)
  { // or get all the obj elements of the sentence

    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && allWordsOrADJPs()) 
      { Vector atts = getNouns();
        // The final one is usually the attribute name
        Vector quals = getQualifiers();
        System.out.println("--> Element: " + this); 
        System.out.println("--> Nouns: " + atts); 
        System.out.println("--> Qualifiers: " + quals); 

        int n = atts.size(); 
        if (n > 0) 
        { String attname = (String) atts.get(n-1);
          Type t = NLPWord.identifyType(quals); 
          Attribute att = new Attribute(attname, t, ModelElement.INTERNAL); 
          if (ent.hasAttribute(attname))
          { System.err.println("!! Class " + ent + " already has an attribute called " + attname); } 
          else
          { ent.addAttribute(att); 
            System.out.println(">> Added attribute " + attname + " : " + t); 
          } 
          NLPWord.identifyStereotypes(att,quals); 
        } 
        return; 
      } 
      else if (elem instanceof NLPPhrase) 
      { ((NLPPhrase) elem).extractAttributeDefinitions(ent); } 
    } 
  } 

  public void extractClassReferences(Entity ent, Vector modelElements)
  { // or get all the obj elements of the sentence

    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && allWordsOrADJPs()) 
      { Vector atts = getNouns();
        // The final one is usually a class name
        Vector quals = getQualifiers();
        System.out.println("--> Element: " + this); 
        System.out.println("--> Nouns: " + atts); 
        System.out.println("--> Qualifiers: " + quals); 

        int n = atts.size(); 
        if (n > 0) 
        { String attname = (String) atts.get(n-1); // singular form of it. 
          Entity tent = (Entity) ModelElement.lookupByNameIgnoreCase(attname,modelElements); 
          if (tent != null) 
          { String role2 = attname.toLowerCase();
		    int card2 = ModelElement.ONE;  
		    if (quals.contains("many"))
			{ card2 = ModelElement.MANY; }
            Association newast = new Association(ent,tent,ModelElement.MANY,card2,"",role2); 
            ent.addAssociation(newast); 
          }
        }  
        return; 
      } 
      else if (elem instanceof NLPPhrase) 
      { ((NLPPhrase) elem).extractClassReferences(ent, modelElements); } 
    } 
  } 

  public void extractRelationshipDefinitions(Entity ent, Vector modelElements)
  { if ("VP".equals(tag) && elements.size() > 1)
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(1); 

      if ("NP".equals(np.tag) && np instanceof NLPPhrase)
      { NLPPhrase nphrase = (NLPPhrase) np; 
         // Could be a specialisation, generalisation or abstraction statement 
         // System.out.println(elements.get(1));
         // if first element of np.elements is a (CC either) then it is a disjunction (cc either) (np subtype1) ... (CC or) (np subtype2)
         // Look for (CC either) (CC or) and at least 2 (NP )  
        if (nphrase.isDisjunction())
        { nphrase.extractRelations(ent,modelElements); }
      }
      else if ("ADVP".equals(np.tag) && elements.size() > 2) 
      { // and np is (ADVP (RB either)) or similar
        NLPPhraseElement np2 = (NLPPhraseElement) elements.get(2);
        if (np2 instanceof NLPPhrase && ((NLPPhrase) np2).isDisjunction())
        { ((NLPPhrase) np2).extractRelations(ent,modelElements); } 
      } 
    }
     
  } 

  public void extractAssociationDefinitions(Entity ent, Vector modelElements)
  { if ("PP".equals(tag) && elements.size() > 1)
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(1); 

      if ("NP".equals(np.tag) && np instanceof NLPPhrase)
      { NLPPhrase nphrase = (NLPPhrase) np; 
         // As association statement 
         // System.out.println(elements.get(1));
         // if first element of np.elements is a (CC either) then it is a disjunction (cc either) (np subtype1) ... (CC or) (np subtype2)
         // Look for (CC either) (CC or) and at least 2 (NP )  
        nphrase.extractClassReferences(ent,modelElements); 
      }
	} 
    else if ("VP".equals(tag) && elements.size() >= 2) 
    { // and np is (ADVP (RB either)) or similar
      NLPPhraseElement np2 = (NLPPhraseElement) elements.get(1);
      if (np2 instanceof NLPPhrase)
      { ((NLPPhrase) np2).extractAssociationDefinitions(ent,modelElements); } 
    }
     
  } 
  
  public boolean isDisjunction()
  { boolean res = false; 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && elem.tag.equals("CC")) 
      { NLPWord wd = (NLPWord) elem; 
        if ("or".equals(wd.text.toLowerCase()) || "either".equals(wd.text.toLowerCase()))
        { return true; }
      } 
    }
    return res;
  } 

  public void extractRelations(Entity ent, Vector modelElements)
  { // Find the NP disjunctions which name alternatives and identify the classes & specialisation relations
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(i); 
     if ("NP".equals(np.tag) && np instanceof NLPPhrase)
     { NLPPhrase nphrase = (NLPPhrase) np; 
       String noun = nphrase.identifyNoun(); 
       Entity entnew = (Entity) ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
       if (entnew == null) 
       { entnew = new Entity(noun);
         modelElements.add(entnew);
         entnew.setSuperclass(ent);
         ent.addSubclass(entnew);  
       }
       System.out.println(">>> alternative class: " + noun); 
     }
     else if (np instanceof NLPWord && ((NLPWord) np).isNoun())
     { NLPWord npword = (NLPWord) np; 
       String noun = npword.text; 
       Entity entnew = (Entity) ModelElement.lookupByNameIgnoreCase(noun, modelElements); 
       if (entnew == null) 
       { entnew = new Entity(noun);
         modelElements.add(entnew);
         entnew.setSuperclass(ent);
         ent.addSubclass(entnew);  
       }
       System.out.println(">>> alternative class: " + noun); 
	} 	   
    }
	
  } 

} 