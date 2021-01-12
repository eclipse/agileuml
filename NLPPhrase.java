import java.util.Vector; 

/* Package: Requirements Engineering */ 
/******************************
* Copyright (c) 2003,2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

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

  public int indexing(int st)
  { int index = st; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      index = elem.indexing(index); 
    }
    return index; 
  }

  public Vector sequentialise()
  { Vector res = new Vector();  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      Vector st = elem.sequentialise(); 
      res.addAll(st); 
    }
    return res; 
  }

  public boolean isVerbPhrase() 
  { return "VP".equals(tag); } 

  public boolean isVerbOrPrepositionPhrase() 
  { return "VP".equals(tag) || "PP".equals(tag); } 

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

  public Vector extractNouns(java.util.Map quals, Vector currentQuals)
  { Vector nouns = new Vector(); 
    String current = "";
	NLPWord currentWord = null; 
	// Vector currentQuals = new Vector(); 
	 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag;
        if (word.isQualifier()) 
        { currentQuals.add(word.text); }  
        else if (lex.equals("NNPS") || lex.equals("NNS") ||
            lex.equals("NN") || lex.equals("NNP"))
        { if (current.equals(""))
          { current = word.text; 
		    currentWord = word; 
          }
		  else 
		  { current = current + word.text; 
		    currentWord = word; 
		  }
		  if (lex.equals("NNPS") || lex.equals("NNS"))
		  { currentQuals.add("many"); }
		} 
		else if (word.isAdjective())
        { if (current.equals(""))
          { current = word.text; 
		    currentWord = word; 
          }
		  else if (currentWord != null && currentWord.isNoun())
		  { nouns.add(current); 
		    quals.put(current,currentQuals); 
			current = word.text; 
			currentWord = word; 
			currentQuals = new Vector(); 
		  }
		  else 
		  { current = current + word.text; 
		    currentWord = word; 
		  }
		} 
		else if (word.isConjunction())
		{ nouns.add(current); 
		  quals.put(current,currentQuals); 
          current = ""; 
          currentWord = null; 
          currentQuals = new Vector(); 
        }  
      }
	  else if (elem instanceof NLPPhrase) 
      { NLPPhrase phrase = (NLPPhrase) elem; 
        Vector nouns1 = phrase.extractNouns(quals,currentQuals);
		nouns.addAll(nouns1); 
      }  
    } 
	
	if (currentWord != null && currentWord.isNoun())
	{ nouns.add(current); 
      quals.put(current,currentQuals); 
    }
	    
    return nouns; 
  }   
  
  public Vector getSingularNouns(Vector plurals)
  { Vector nouns = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag;
        if (word.isKeyword()) { }  
        if (lex.equals("NNPS") || lex.equals("NNS")) 
        { nouns.add(word.getSingular()); 
		  plurals.add(word); 
		} 
        else if (lex.equals("NN") || lex.equals("NNP"))
        { nouns.add(word.text); 
		  plurals.add(word); 
		} 
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
  
  public Vector getAttributeQualifiers()
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
      { Vector qualssub = ((NLPPhrase) elem).getAttributeQualifiers(); 
        quals.addAll(qualssub); 
      } 
    } 
    return quals; 
  }   

  public Vector getAssociationQualifiers()
  { Vector quals = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag; 
        if (lex.equals("JJ") || lex.equals("JJS") || lex.equals("CD") || 
            lex.equals("JJR") || word.text.equals("unique") || word.text.equals("single") ||
            word.text.equals("several") || word.text.equals("many") || word.text.equals("more") || 
            word.text.equals("some") || word.text.equals("multiple"))
        { quals.add(word.text); } 
      } 
      else if (elem.tag.equals("ADJP") || elem.tag.equals("NML"))
      { String qual = elem.formQualifier(); 
        if (qual != null) 
        { quals.add(qual); }
      }
      else if (elem instanceof NLPPhrase)
      { Vector qualssub = ((NLPPhrase) elem).getAssociationQualifiers(); 
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

  public String getPrincipalVerbOrPreposition()
  { String verb = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag; 
        if (lex.equals("VB") || lex.equals("VBZ") || lex.equals("IN") || 
            lex.equals("VBG") || lex.equals("VBD") ||
            lex.equals("VBN") || lex.equals("VBP"))
        { return word.text; } 
      } 
    } 
    return verb; 
  }   

  public String getMostSignificantVerb()
  { String verb = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag; 
        if (lex.equals("VB") || lex.equals("VBZ") || 
            lex.equals("VBG") || lex.equals("VBD") ||
            lex.equals("VBN") || lex.equals("VBP"))
        { verb = word.text; } 
      } 
	  else if (elem instanceof NLPPhrase && "VP".equals(elem.tag))
	  { return ((NLPPhrase) elem).getMostSignificantVerb(); }
    } 
    return verb; 
  }  // Actually the textually last verb. 

  public void extractAttributeDefinitions(Entity ent)
  { // or get all the obj elements of the sentence

    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && allWordsOrADJPs()) 
      { Vector atts = getNouns();
        // The final one is usually the attribute name. But for a conjunctive clause, take all nouns. 
        Vector quals = getAttributeQualifiers();
        System.out.println("--> Element: " + this); 
        System.out.println("--> Nouns: " + atts); 
        System.out.println("--> Attribute qualifiers: " + quals); 

        int n = atts.size(); 
        if (isConjunction())
        { for (int j = 0; j < n; j++) 
          { String attx = (String) atts.get(j); 
            extractAtt(attx,quals,ent); 
          }
        }
        else if (n > 0) 
        { String attname = (String) atts.get(n-1);
          extractAtt(attname,quals,ent); 
        } 
        return; 
      } 
      else if (elem instanceof NLPPhrase) 
      { ((NLPPhrase) elem).extractAttributeDefinitions(ent); } 
    } 
  } 

  public static void extractAtt(String attname, Vector quals, Entity ent)
  { Type t = NLPWord.identifyType(attname, quals); 
    Attribute att = new Attribute(attname, t, ModelElement.INTERNAL); 
          
    if (ent.hasAttribute(attname))
    { System.err.println("!! Possible conflict: Class " + ent + " already has an attribute called " + attname); } 
    else
    { ent.addAttribute(att); 
      System.out.println(">> Added attribute " + attname + " : " + t); 
    } 
    NLPWord.identifyStereotypes(att,quals); 
  }
  
  public void extractClassReferences(Entity ent, String role, Vector modelElements)
  { // or get all the obj elements of the sentence

    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && allWordsOrADJPs()) 
      { Vector plurals = new Vector(); 
	    Vector atts = getSingularNouns(plurals);
        // The final one is usually a class name
        Vector quals = getAssociationQualifiers();
        System.out.println("--> Element: " + this); 
        System.out.println("--> Nouns: " + atts);
		System.out.println("--> Original forms: " + plurals);  
        System.out.println("--> Association qualifiers: " + quals); 

        int n = atts.size(); 
        if (n > 0) 
        { String attname = (String) atts.get(n-1); // singular form of it.
          if (NLPWord.isKeyword(attname)) { continue; } 
		  NLPWord attword = (NLPWord) plurals.get(n-1); 
 
          Entity tent = (Entity) ModelElement.lookupByNameIgnoreCase(attname,modelElements); 
          if (tent != null) 
          { System.out.println(">>> Existing class: " + attname); }
          else 
          { tent = new Entity(Named.capitalise(attname)); 
            System.out.println(">>> Creating new class: " + attname);
            modelElements.add(tent);  
          }
          String role2 = attname.toLowerCase();
          if (role != null) 
          { role2 = role; }
			
		  int card1 = ModelElement.MANY; 
          if (quals.contains("unique"))
		  { card1 = ModelElement.ZEROONE; } 
		  int card2 = ModelElement.ONE;  
		  if (quals.contains("many") || quals.contains("several") || quals.contains("more") ||
			  quals.contains("some") || quals.contains("multiple") || attword.isPlural()) // or if the object is plural
		  { card2 = ModelElement.MANY; }
		  if (ent.hasRole(role2))
		  { System.err.println("Possible conflict in requirements: role " + role2 + " of class " + attname + " already exists"); 
		    role2 = role2 + "_" + ent.getAssociations().size(); 
		  }
          Association newast = new Association(ent,tent,card1,card2,"",role2); 
          System.out.println(">>> new association " + newast + " for class " + ent.getName()); 
          ent.addAssociation(newast); 
        }  
        return; 
      } 
      else if (elem instanceof NLPPhrase) 
      { NLPPhrase pr = (NLPPhrase) elem;
	    java.util.HashMap mp = new java.util.HashMap(); 
		Vector currentQuals = new Vector();  
	    Vector nouns = pr.extractNouns(mp, currentQuals); 
		System.out.println(">>>- identified possible features: " + nouns); 
		System.out.println(">>>- identified possible qualifiers: " + mp); 
		
		pr.extractClassReferences(ent, role, modelElements); 
	  } 
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
      /* else if ("PP".equals(np.tag) && elements.size() > 2) 
      { // and np is (PP (IN for) np2) or similar
        NLPPhraseElement np2 = (NLPPhraseElement) elements.get(2);
        if (np2 instanceof NLPPhrase && ((NLPPhrase) np2).isDisjunction())
        { ((NLPPhrase) np2).extractRelations(ent,modelElements); } 
      } */ 
    }
     
  } 

  public void extractAssociationDefinitions(Entity ent, String role, Vector modelElements)
  { if ("PP".equals(tag) && elements.size() > 1)
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(1); 

      if ("NP".equals(np.tag) && np instanceof NLPPhrase)
      { NLPPhrase nphrase = (NLPPhrase) np; 
         // As association statement 
         // System.out.println(elements.get(1));
         // if first element of np.elements is a (CC either) then it is a disjunction (cc either) (np subtype1) ... (CC or) (np subtype2)
         // Look for (CC either) (CC or) and at least 2 (NP )  
        nphrase.extractClassReferences(ent,role,modelElements); 
      }
    } 
    else if ("VP".equals(tag) && elements.size() >= 2) 
    { // and np is (ADVP (RB either)) or similar
      NLPPhraseElement np2 = (NLPPhraseElement) elements.get(1);
      if (np2 instanceof NLPPhrase)
      { ((NLPPhrase) np2).extractAssociationDefinitions(ent,role,modelElements); } 
    }
    else if ("NP".equals(tag))
    { extractClassReferences(ent,role,modelElements); }     
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

  public boolean isConjunction()
  { boolean res = false; 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && elem.tag.equals("CC")) 
      { NLPWord wd = (NLPWord) elem; 
        if ("and".equals(wd.text.toLowerCase()) || "also".equals(wd.text.toLowerCase()))
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