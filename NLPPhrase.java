import java.util.Vector; 

/* Package: Requirements Engineering */ 
/******************************
* Copyright (c) 2003-2023 Kevin Lano
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

  public void linkToPhrases(NLPSentence s)
  { sentence = s; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      elem.linkToPhrases(s); 
    }
  }

  
  public String toString()
  { String res = "(" + tag + " "; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      res = res + " " + elem; 
    }
    return res + ")"; 
  }

  public String literalForm()
  { String res = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      res = res + " " + elem.literalForm(); 
    }
    return res; 
  }

  public boolean isVerbPhrase()
  { return "VP".equals(tag); } 

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

  public boolean allWordsOrVPs()
  { if (isSimpleVP())
    { return true; } 
    return allWordsOrADJPs(); 
  }

  public boolean isSimpleNP()
  { if ("NP".equals(tag) && allWords())
    { return true; } 
    return false; 
  } 

  public boolean isSimpleVP()
  { if ("VP".equals(tag))
    { if (elements.size() > 1)
      { // first element is a verb & rest is a simple NP
        NLPPhraseElement np = (NLPPhraseElement) elements.get(1); 
        if (np instanceof NLPWord)
        { return true; }
        else if (np instanceof NLPPhrase)
        { return ((NLPPhrase) np).isSimpleNP(); }  
      }
      else if (elements.get(0) instanceof NLPWord)
      { return true; }  // a single verb 
    }
    return false; 
  }

  public boolean allWordsOrSimpleVPs()
  { for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { } 
      else if (elem instanceof NLPPhrase && 
               "ADJP".equals(((NLPPhrase) elem).tag))
      { } 
      else if (elem instanceof NLPPhrase && 
               "NML".equals(((NLPPhrase) elem).tag))
      { } 
      else if (elem instanceof NLPPhrase &&
               ((NLPPhrase) elem).isVerbPhrase())
      { // first element is a verb & rest is a simple NP
        NLPPhrase vp = (NLPPhrase) elem; 
        if (vp.allWordsOrNPs())
        { }
        else 
        { return false; }  
      } 
      else  
      { return false; }  
    }
    return true; 
  }

  public boolean allWordsOrNPs()
  { for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) { } 
      else if (elem instanceof NLPPhrase && 
               "ADJP".equals(((NLPPhrase) elem).tag))
      { } 
      else if (elem instanceof NLPPhrase && 
               "NML".equals(((NLPPhrase) elem).tag))
      { } 
      else if (elem instanceof NLPPhrase && 
               "NP".equals(((NLPPhrase) elem).tag))
      { } 
      else 
      { return false; }  
    }
    return true; 
  }

  public static String getPrincipalNoun(Vector elems)
  { NLPPhrase phr = new NLPPhrase("NP", elems); 
    return phr.getPrincipalNoun(); 
  } 

  // Also, restrict only to NNPS, NNP

  public String getPrincipalNoun()
  { String noun = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag;
		 
        if (lex.equals("NNPS") || lex.equals("NNS")) 
        { if (noun.length() > 0)
          { noun = noun + Named.capitalise(word.getSingular() + ""); } 
          else 
          { noun = noun + word.getSingular(); } 
        } 
        else if (lex.equals("NN") || lex.equals("NNP"))
        { if (noun.length() > 0)
          { noun = noun + Named.capitalise(word.text); } 
          else 
          { noun = noun + word.text; }
        } 
        else if (word.isAdjective())
        { if (noun.length() > 0)
          { noun = noun + Named.capitalise(word.text); } 
          else 
          { noun = noun + word.text; } 
        } 
        else if (noun.length() > 0)
        { return noun; }  
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        return noun + phr.getPrincipalNoun(); 
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

  public Vector extractNouns(java.util.Map quals, java.util.Map types, java.util.Map fromBackground, Vector currentQuals)
  { Vector nouns = new Vector(); 
    String current = "";
    NLPWord currentWord = null; 
	// Vector currentQuals = new Vector(); 
	 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag;
		
        Object obj = fromBackground.get(word.text); 
	    // System.out.println(">>> " + word.text + " background ==> " + obj); 
		
        if (obj != null)
        { Vector sem = (Vector) obj; 
          if (sem.size() > 0 && sem.get(0) instanceof Attribute)
          { Attribute att = (Attribute) sem.get(0); 
            Type t = att.getType(); 
            System.out.println(">>> " + word.text + " is likely to be an attribute of type " + t); 
		    
            if (t != null) 
            { currentQuals.add(t.getName()); 
              types.put(word.text, t); 
            } 
			
            if (att.isIdentity())
            { currentQuals.add("identity"); }
          }  
        }
		
        if (word.isQualifier()) 
        { currentQuals.add(word.text); }  
        
        if (lex.equals("NNPS") || lex.equals("NNS") ||
            lex.equals("NN") || lex.equals("NNP"))
        { // System.out.println(">>> noun " + word.text + " " + current + " " + currentWord); 
		
          if (current.equals(""))
          { current = word.text; 
            currentWord = word; 
          }
          else 
          { current = current + Named.capitalise(word.text); 
            currentWord = word; 
          }
		  
		  // System.out.println(">>> Formed word: " + current); 
		  
          if (lex.equals("NNPS") || lex.equals("NNS"))
          { currentQuals.add("many"); }
        } 
        else if (word.isAdjective())
        { if (current.equals(""))
          { current = word.text; 
            currentWord = word; 
          }
          else if (currentWord != null && currentWord.isNoun()) // noun followed by adjective: new noun
          { nouns.add(current); 
            quals.put(current,currentQuals); 
            current = word.text; 
            currentWord = word; 
            currentQuals = new Vector(); 
          }
          else // adjective followed by adjective is ok 
          { current = current + word.text; 
            currentWord = word; 
          }
        } 
        else if (word.isConjunction() || word.isPreposition() || 
                 word.isVerb())
        { nouns.add(current); 
          quals.put(current,currentQuals); 
          current = ""; 
          currentWord = null; 
          currentQuals = new Vector(); 
        }  
      }
      else if (elem instanceof NLPPhrase) 
      { NLPPhrase phrase = (NLPPhrase) elem;
        Vector newquals = new Vector();  
        Vector nouns1 = phrase.extractNouns(quals,types,fromBackground,newquals);
        nouns.addAll(nouns1); 
      }  
    } 
	
    if (currentWord != null && currentWord.isNoun())
    { nouns.add(current); 
      quals.put(current,currentQuals); 
    }
	    
    return nouns; 
  }   

  public Vector extractVerbedNouns(java.util.Map quals, java.util.Map types, java.util.Map fromBackground, Vector currentQuals)
  { // Either nouns, verbs, 
    // (NP ...) or (VP (VB verb) (NP ...))
 
    Vector nouns = new Vector(); 
    String current = "";
    NLPWord currentWord = null; 
	// Vector currentQuals = new Vector(); 

    if (isSimpleNP())
    { for (int i = 0; i < elements.size(); i++) 
      { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
        if (elem instanceof NLPWord) 
        { NLPWord word = (NLPWord) elem; 
          String lex = word.tag;
		
          Object obj = fromBackground.get(word.text); 
	    // System.out.println(">>> " + word.text + " background ==> " + obj); 
		
          if (obj != null)
          { Vector sem = (Vector) obj; 
            if (sem.size() > 0 && sem.get(0) instanceof BehaviouralFeature)
            { BehaviouralFeature op = (BehaviouralFeature) sem.get(0); 
              Type t = op.getType(); 
              System.out.println(">>> " + word.text + " is likely to be an operation of type " + t); 
		    
              if (t != null) 
              { currentQuals.add(t.getName()); 
                types.put(word.text, t); 
              } 
			
            // if (att.isIdentity())
            // { currentQuals.add("identity"); }
            }  
          }
		
          if (word.isQualifier()) 
          { currentQuals.add(word.text); }
  
          if (lex.equals("NNPS") || lex.equals("NNS") ||
            lex.equals("NN") || lex.equals("NNP") )
          { // System.out.println(">>> noun " + word.text + " " + current + " " + currentWord); 
		
            if (current.equals(""))
            { current = word.text; 
              currentWord = word; 
            }
            else 
            { current = current + Named.capitalise(word.text); 
              currentWord = word; 
            }
		  
		  // System.out.println(">>> Formed word: " + current); 
		  
            if (lex.equals("NNPS") || lex.equals("NNS"))
            { currentQuals.add("many"); }
          } 
          else if (word.isAdjective())
          { if (current.equals(""))
            { current = word.text; 
              currentWord = word; 
            }
            else if (currentWord != null && currentWord.isNoun()) // noun followed by adjective: new noun
            { nouns.add(current); 
              quals.put(current,currentQuals); 
              current = word.text; 
              currentWord = word; 
              currentQuals = new Vector(); 
            }
            else // adjective followed by adjective is ok 
            { current = current + word.text; 
              currentWord = word; 
            }
          } 
          else if (word.isConjunction() || word.isSeparator() || word.isVerb())
          { nouns.add(current); 
            quals.put(current,currentQuals); 
            current = ""; 
            currentWord = null; 
            currentQuals = new Vector(); 
          }  
        }

        if (currentWord != null && (currentWord.isNoun() ||
                                currentWord.isPureVerb()))
        { nouns.add(current); 
          quals.put(current,currentQuals); 
        }
      } 
    }
    else if (isSimpleVP()) 
    { NLPPhraseElement vb = (NLPPhraseElement) elements.get(0);
      if (vb instanceof NLPWord && elements.size() > 1) 
      { NLPPhraseElement np = (NLPPhraseElement) elements.get(1); 
        
        Vector newquals = new Vector();  
        Vector nouns1 = np.extractNouns(quals, types, fromBackground, newquals);
        for (int j = 0; j < nouns1.size(); j++) 
        { String nn = (String) nouns1.get(j); 
          nouns.add(((NLPWord) vb).text + Named.capitalise(nn));
        }  
      }  
    } 
    else if (elements.size() > 0) 
    { for (int i = 0; i < elements.size(); i++) 
      { NLPPhraseElement p1 = (NLPPhraseElement) elements.get(i); 
        Vector vns = p1.extractVerbedNouns(quals,types, fromBackground, currentQuals);
        nouns.addAll(vns); 
      }  
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

  public NLPWord identifyNounForEntity() // Expect just one. 
  { String res = ""; 
    String tag = "NN"; 

    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        if (word.isNoun() || word.isAdjective())
        { res = res + Named.capitalise(word.text); 
          tag = word.tag; 
        } 
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        NLPWord subword = phr.identifyNounForEntity();
        res = res + subword.text; 
        tag = subword.tag;  
      } 
    } 

    return new NLPWord(tag,res); 
  }



  public String identifyNoun() // Expect just one. 
  { String res = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        if (word.isNoun() || word.isAdjective())
        { res = res + Named.capitalise(word.text); } 
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        res = res + phr.identifyNoun(); 
      } 
    } 
    return res; 
  }

  public String identifyNounOrAdjective() // Expect just one. 
  { String res = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        if (word.isNoun() || word.isAdjective())
        { res = res + Named.capitalise(word.text); } 
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        res = res + phr.identifyNounOrAdjective(); 
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
            word.text.startsWith("real") ||  
            word.text.startsWith("whole") || 
            word.text.startsWith("string") || word.text.equals("many") || word.text.startsWith("double") ||
            word.text.startsWith("bool"))
        { quals.add(word.text); }   // Also "list", "series" 
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
        else if (lex.equals("SYM") && word.text.equals("="))
        { return word.text; } 
        else if (lex.equals(":") && word.text.equals(":"))
        { return word.text; } 
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        verb = phr.getPrincipalVerb(); 
        if (verb != null && verb.length() > 0)
        { return verb; } 
      } 
    } 
    return verb; 
  }   

  public NLPWord getMainVerb()
  { NLPWord verb = null; 
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord word = (NLPWord) elem; 
        String lex = word.tag; 
        if (lex.equals("VB") || lex.equals("VBZ") || 
            lex.equals("VBG") || lex.equals("VBD") ||
            lex.equals("VBN") || lex.equals("VBP"))
        { return word; }
        else if (lex.equals("SYM") && word.text.equals("="))
        { return word; } 
        else if (lex.equals(":") && word.text.equals(":"))
        { return word; } 
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        verb = phr.getMainVerb(); 
        if (verb != null)
        { return verb; } 
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

  public Vector extractAttributeDefinitions(Entity ent, java.util.Map fromBackground, Vector modelems)
  { // or get all the obj elements of the sentence
    Vector res = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && allWordsOrADJPs()) 
      { // Vector atts = getNouns();
        // The final one is usually the attribute name. But for a conjunctive clause, take all nouns.
        NLPWord wd = (NLPWord) elem; 
        java.util.HashMap qm = new java.util.HashMap(); 
        java.util.HashMap types = new java.util.HashMap(); 
        Vector quals = new Vector();  
        Vector allAtts = extractNouns(qm, types, fromBackground, quals); 
        // Vector quals = getAttributeQualifiers();
        System.out.println("--> Element: " + this); 
        System.out.println("--> Nouns: " + allAtts); 
        System.out.println("--> Attribute qualifiers: " + qm); 
        System.out.println("--> Attribute types: " + types); 

        int n = allAtts.size(); 
        // if (isConjunction())
        // { 
        for (int j = 0; j < n; j++) 
        { String attx = (String) allAtts.get(j); 
          if (attx != null && attx.length() > 0)
          { res.add(attx); 
            extractAtt(sentence, wd, attx, qm, types, ent, modelems); 
          }  
        }
        // }
        // else if (n > 0) 
        // { String attname = (String) allAtts.get(n-1);
        //   extractAtt(attname,quals,ent); 
        // } 
        return res; 
      } 
      else if (elem instanceof NLPPhrase) 
      { Vector resp = ((NLPPhrase) elem).extractAttributeDefinitions(ent, fromBackground, modelems); 
        res.addAll(resp); 
      } 
    } 
    return res; 
  } 

  public static void extractAtt(NLPSentence sent, NLPWord wd, String attname, java.util.Map qm, java.util.Map types, Entity ent, Vector modelems)
  { Type t = wd.identifyType(attname, qm, types, modelems); 
    Attribute att = new Attribute(attname, t, ModelElement.INTERNAL); 
          
    if (ent != null && ent.hasAttributeIgnoreCase(attname))
    { System.err.println("!! Possible conflict: Class " + ent + " already has an attribute called " + attname); } 
    else if (ent != null) 
    { ent.addAttribute(att); 
      System.out.println(">> Added attribute " + attname + " : " + t);
	  String id = sent.id; 
	  ent.addStereotype("modifiedBy=\"" + id + "\""); 
	  sent.derivedElements.add(att);  
    } 
    NLPWord.identifyStereotypes(att,qm); 
  }

  public void extractOperationDefinitions(Entity ent, java.util.Map fromBackground, Vector modelems)
  { // or get all the obj elements of the sentence

    String ename = ent.getName(); 

    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord && allWordsOrVPs()) 
      { NLPWord wd = (NLPWord) elem; 
        java.util.HashMap qm = new java.util.HashMap(); 
        java.util.HashMap types = new java.util.HashMap(); 
        Vector quals = new Vector();  
        Vector allAtts = extractVerbedNouns(qm, types, fromBackground, quals); 
        // Vector quals = getAttributeQualifiers();
        System.out.println("--> Element: " + this); 
        System.out.println("--> Verbed nouns: " + allAtts); 
        System.out.println("--> Operation qualifiers: " + qm); 
        System.out.println("--> Operation types: " + types); 

        int n = allAtts.size(); 
        // if (isConjunction())
        // { 
        for (int j = 0; j < n; j++) 
        { String attx = (String) allAtts.get(j); 
          if (attx != null && attx.length() > 0 && 
              !attx.equals(ename))
          { extractOperation(sentence, wd, attx, qm, types, ent, modelems); }  
        }
        // }
        // else if (n > 0) 
        // { String attname = (String) allAtts.get(n-1);
        //   extractAtt(attname,quals,ent); 
        // } 
        return; 
      } 
      else if (elem instanceof NLPPhrase) 
      { ((NLPPhrase) elem).extractOperationDefinitions(ent, fromBackground, modelems); } 
    } 
  } 

  public Vector possibleOperationNames()
  { // verb followed by determiner, adjectives/nouns
    // Only for sequences of words. 
  
    Vector res = new Vector(); 

    String currentOp = ""; 

    for (int i = 0; i < elements.size(); i++) 
    { NLPWord wd = (NLPWord) elements.get(i); 
      String lcname = wd.text.toLowerCase(); 

      if (currentOp.equals(""))
      { if (wd.isPureVerb() || wd.isNoun())
        { currentOp = wd.text.toLowerCase(); } 
        // else if (wd.isArticle() || wd.isPreposition() || 
        //          wd.isConjunction())
        // { } // skip it
      } 
      else if (lcname.startsWith("operation") ||
                lcname.startsWith("service") ||
                lcname.startsWith("function") ||
                lcname.equals("process") ||
                lcname.equals("utility") ||
                lcname.startsWith("procedure")) 
      { } 
      else if (wd.isNoun() || wd.isAdjective())
      { currentOp = currentOp + Named.capitalise(wd.text); } 
      else if (wd.isArticle())
      { } // skip
      else // start a new candidate operation name 
      { res.add(currentOp); 
        currentOp = new String(""); 
      } 
    } 

    if (currentOp.equals("")) {} 
    else  
    { res.add(currentOp); }

    System.out.println(">>> Candidate operation names = " + res); 
    return res; 
  } 


  public Vector extractOperationDefs(Entity ent, java.util.Map fromBackground, Vector modelems)
  { // verb followed by determiner, adjectives/nouns
    // Only for sequences of words. 
  
    Vector res = possibleOperationNames(); 
    String ename = ent.getName(); 


    for (int j = 0; j < res.size(); j++) 
    { String attx = (String) res.get(j); 
      if (attx != null && attx.length() > 0 && 
          !attx.equals(ename))
      { extractOp(sentence, attx, ent, modelems); }  
    }
 
    return res; 
  } 

  public static void extractOperation(NLPSentence sent, NLPWord wd, String attname, java.util.Map qm, java.util.Map types, Entity ent, Vector modelems)
  { String lcattname = attname.toLowerCase(); 
    if (lcattname.startsWith("operation") ||
        lcattname.startsWith("service") ||
        lcattname.startsWith("function") ||
        lcattname.startsWith("procedure")) 
    { return; } 

    Type t = wd.identifyType(attname, qm, types, modelems); 
    BehaviouralFeature op = new BehaviouralFeature(attname); 
    op.setResultType(t); 
      
    if (ent.hasOperationIgnoreCase(attname))
    { System.err.println("!! Possible conflict: Class " + ent + " already has an operation called " + attname); } 
    else
    { ent.addOperation(op); 
      System.out.println(">> Added operation " + attname + " : " + t);
      modelems.add(op); 
      String id = sent.id; 
      ent.addStereotype("modifiedBy=\"" + id + "\""); 
      sent.derivedElements.add(op);  
    } 
    // NLPWord.identifyStereotypes(att,qm); 
  }
  
  public static void extractOp(NLPSentence sent, String attname, Entity ent, Vector modelems)
  { BehaviouralFeature op = new BehaviouralFeature(attname); 
      
    if (ent.hasOperationIgnoreCase(attname))
    { System.err.println("!! Possible conflict: Class " + ent + " already has an operation called " + attname); } 
    else
    { ent.addOperation(op); 
      System.out.println(">> Added operation " + attname + " to " + ent.getName());
      modelems.add(op); 
      String id = sent.id; 
      ent.addStereotype("modifiedBy=\"" + id + "\""); 
      sent.derivedElements.add(op);  
    } 
  }
  
  public void extractClassReferences(Entity ent, String role, java.util.Map fromBackground, Vector modelElements)
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

          String cleanName = 
             Named.removeInvalidCharacters(attname); 
 
          Entity tent = (Entity) 
            ModelElement.lookupByNameIgnoreCase(
                                cleanName, modelElements); 
          if (tent != null) 
          { System.out.println(">>> Existing class: " + attname); }
          else 
          { tent = new Entity(Named.capitalise(cleanName)); 
            System.out.println(">>> Creating new class: " + attname);
            modelElements.add(tent);  
            String id = sentence.id; 
            tent.addStereotype("originator=\"" + id + "\""); 
            sentence.derivedElements.add(tent);
          }
          
          String role2 = cleanName.toLowerCase();
          if (role != null) 
          { role2 = role; }
			
          int card1 = ModelElement.MANY; 
          if (quals.contains("unique"))
          { card1 = ModelElement.ZEROONE; } 
          int card2 = ModelElement.ONE;  
          if (quals.contains("many") || quals.contains("several") || quals.contains("more") ||
              quals.contains("list") || quals.contains("series") || quals.contains("collection") || 
              quals.contains("sequence") || quals.contains("set") || 
              quals.contains("some") || quals.contains("multiple") || attword.isPlural()) // or if the object is plural
          { card2 = ModelElement.MANY; }

          if (ent.hasRole(role2))
          { System.err.println("Possible conflict in requirements: role " + role2 + " of class " + attname + " already exists"); 
            // role2 = role2 + "_" + ent.getAssociations().size(); 
          }
          else 
          { Association newast = new Association(ent,tent,card1,card2,"",role2); 
            System.out.println(">>> new association " + newast + " for class " + ent.getName()); 
            ent.addAssociation(newast); 
            String id = sentence.id; 
            ent.addStereotype("modifiedBy=\"" + id + "\""); 
            sentence.derivedElements.add(newast);
          } 
        }  
        return; 
      } 
      else if (elem instanceof NLPPhrase) 
      { NLPPhrase pr = (NLPPhrase) elem;
        java.util.HashMap mp = new java.util.HashMap(); 
        Vector currentQuals = new Vector();  
        java.util.HashMap types = new java.util.HashMap(); 
        Vector nouns = pr.extractNouns(mp, types, fromBackground, currentQuals); 
        System.out.println(">>>- identified possible features: " + nouns); 
        System.out.println(">>>- identified possible qualifiers: " + mp); 
        System.out.println(">>>- identified possible types: " + types); 
		
        pr.extractClassReferences(ent, role, fromBackground, modelElements); 
      } 
    } 
  } 

  public void extractRelationshipDefinitions(Entity ent, Vector modelElements)
  { System.out.println(">>> Identifying alternative subclasses of " + ent.getName() + " in " + this); 
  
    if ("VP".equals(tag) && elements.size() > 1)
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(1); 
      np.extractRelationshipDefinitions(ent,modelElements); 
      return; 
    } 

    if ("NP".equals(tag))
    { if (isDisjunction())
      { extractRelations(ent,modelElements); }
    }
    else if ("ADVP".equals(tag) && elements.size() > 2) 
    { // and np is (ADVP (RB either)) or similar
      NLPPhraseElement np2 = (NLPPhraseElement) elements.get(2);
      if (np2 instanceof NLPPhrase && ((NLPPhrase) np2).isDisjunction())
      { ((NLPPhrase) np2).extractRelations(ent,modelElements); } 
    }      
  } 

  public void extractAlternativeValues(Attribute att, Entity ent, Vector modelElements)
  { Vector vals = new Vector(); // in case it is an enumeration
    Vector atts = new Vector(); // in case it is a constraint

    if ("VP".equals(tag) && elements.size() > 1)
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(1); 

      if ("NP".equals(np.tag) && np instanceof NLPPhrase)
      { NLPPhrase nphrase = (NLPPhrase) np; 
  
        if (nphrase.isDisjunction())
        { vals = nphrase.extractValues(att,ent,modelElements,atts); }
      }
      else if ("ADVP".equals(np.tag) && elements.size() > 2) 
      { // and np is (ADVP (RB either)) or similar
        NLPPhraseElement np2 = (NLPPhraseElement) elements.get(2);
        if (np2 instanceof NLPPhrase && ((NLPPhrase) np2).isDisjunction())
        { vals = ((NLPPhrase) np2).extractValues(att,ent,modelElements,atts); } 
      } 
      else if ("ADJP".equals(np.tag) && elements.size() > 2) 
      { // and np is (ADJP (CC either)) or similar
        NLPPhraseElement np2 = (NLPPhraseElement) elements.get(2);
        if (np2 instanceof NLPPhrase && ((NLPPhrase) np2).isDisjunction())
        { vals = ((NLPPhrase) np2).extractValues(att,ent,modelElements,atts); }
      } 
      else if (((NLPPhrase) np).isDisjunction() && ((NLPPhrase) np).elements.size() > 1)
      { System.out.println(">>>> ADJP: " + ((NLPPhrase) np).elements); 
        vals = ((NLPPhrase) np).extractValues(att,ent,modelElements,atts); 
      } 
    } 
      /* else if ("PP".equals(np.tag) && elements.size() > 2) 
      { // and np is (PP (IN for) np2) or similar
        NLPPhraseElement np2 = (NLPPhraseElement) elements.get(2);
        if (np2 instanceof NLPPhrase && ((NLPPhrase) np2).isDisjunction())
        { ((NLPPhrase) np2).extractRelations(ent,modelElements); } 
      } */ 

    if (vals != null && vals.size() > 0) 
    { String tname = Named.capitalise(att.getName()) + "TYPE"; 
      Type enumt = new Type(tname, vals); 
      modelElements.add(enumt); 
      System.out.println("New enumerated type: " + enumt + " = " + vals); 
      sentence.derivedElements.add(enumt); 
      att.setType(enumt); 
    } 
     
  } 

  public void extractAssociationDefinitions(Entity ent, String role, java.util.Map fromBackground, Vector modelElements)
  { if ("PP".equals(tag) && elements.size() > 1)
    { for (int x = 1; x < elements.size(); x++) 
      { NLPPhraseElement np = (NLPPhraseElement) elements.get(x); 

        if ("NP".equals(np.tag) && np instanceof NLPPhrase)
        { NLPPhrase nphrase = (NLPPhrase) np; 
         // As association statement 
         // System.out.println(elements.get(1));
         // if first element of np.elements is a (CC either) then it is a disjunction (cc either) (np subtype1) ... (CC or) (np subtype2)
         // Look for (CC either) (CC or) and at least 2 (NP )  
          nphrase.extractClassReferences(ent, role, fromBackground, modelElements);
          return;  
        } 
      }
    } 
    else if ("VP".equals(tag) && elements.size() >= 2) 
    { // and np is (ADVP (RB either)) or similar
      for (int x = 1; x < elements.size(); x++) 
      { NLPPhraseElement np2 = (NLPPhraseElement) elements.get(x);
        if (np2 instanceof NLPPhrase)
        { ((NLPPhrase) np2).extractAssociationDefinitions(ent,role,fromBackground,modelElements); 
          return; 
        }
      }  
    }
    else if ("NP".equals(tag))
    { extractClassReferences(ent,role, fromBackground, modelElements); }     
  } 

  public boolean isInputPhrase()
  { boolean res = false; 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord wd = (NLPWord) elem; 
        String lctext = wd.text.toLowerCase(); 

        if (lctext.startsWith("input") || 
            lctext.equals("receives") || 
            lctext.equals("requires") || 
            lctext.startsWith("parameter"))
        { return true; }
      } 
      else if (elem instanceof NLPPhrase) 
      { if (((NLPPhrase) elem).isInputPhrase())
        { return true; } 
      } 
    }
    return res;
  } 

  public boolean isPureInputPhrase()
  { // input phrase with no output sub-phrase
    if (isInputPhrase())
    { for (int i = 0; i < elements.size(); i++) 
      { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
        if (elem.isOutputPhrase())
        { return false; } 
      } 
      return true; 
    } 
    return false; 
  } 

  public NLPPhrase identifyInputPhrases()
  { for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement pr = (NLPPhraseElement) elements.get(i); 
      if (pr instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) pr; 
        if (phr.isPureInputPhrase())
        { System.out.println(">> Input phrase: " + pr);
          return phr;
        }
        else 
        { NLPPhrase res = phr.identifyInputPhrases();
          if (res != null) 
          { return res; }
        } 
      } 
    } 
    return null; 
  } 

  public boolean isOutputPhrase()
  { boolean res = false; 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord wd = (NLPWord) elem; 
        String lctext = wd.text.toLowerCase(); 

        if (lctext.startsWith("return") || 
            lctext.startsWith("result") ||
            lctext.equals("delivers") ||
            lctext.equals("sends") ||
            lctext.startsWith("output"))
        { return true; }
      } 
      else if (elem instanceof NLPPhrase) 
      { if (((NLPPhrase) elem).isOutputPhrase())
        { return true; } 
      } 
    }
    return res;
  } 

  public boolean isPureOutputPhrase()
  { // output phrase with no input sub-phrase
    if (isOutputPhrase())
    { for (int i = 0; i < elements.size(); i++) 
      { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
        if (elem.isInputPhrase())
        { return false; } 
      } 
      return true; 
    } 
    return false; 
  } 
  
  public NLPPhrase identifyOutputPhrases()
  { for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement pr = (NLPPhraseElement) elements.get(i); 
      if (pr instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) pr; 
        if (phr.isPureOutputPhrase())
        { System.out.println(">> Output phrase: " + phr); 
          return phr; 
        }
        else 
        { NLPPhrase res = phr.identifyOutputPhrases(); 
          if (res != null) 
          { return res; }
        } 
      } 
    } 
    return null; 
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

  public static boolean isDisjunction(Vector words)
  { boolean res = false; 
  
    for (int i = 0; i < words.size(); i++) 
    { NLPWord wd = (NLPWord) words.get(i); 
      if (wd.tag.equals("CC")) 
      { if ("or".equals(wd.text.toLowerCase()) || "either".equals(wd.text.toLowerCase()))
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

  public static boolean isConditional(Vector words)
  { if (words.size() > 0) 
    { NLPWord wd = (NLPWord) words.get(0); 
      if (wd.isConditional())
      { return true; } 
    } 
    return false; 
  } // Other cases, such as "only if", "otherwise"
      

  public NLPPhrase getObjectPart()
  { if ("NP".equals(tag) || "ADJP".equals(tag))
    { return this; } 

    if ("VP".equals(tag) && elements.size() > 1 && elements.get(1) instanceof NLPPhrase)
    { NLPPhrase p1 = (NLPPhrase) elements.get(1); 
      return p1.getObjectPart(); 
    } 

    return null; 

  } 

  public void extractRelations(Entity ent, Vector modelElements)
  { // Find the NP disjunctions which name alternatives and identify the classes & specialisation relations

    System.out.println(">>> Identifying alternative subclasses of " + ent.getName() + " in " + this); 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(i); 
     if ("NP".equals(np.tag) && np instanceof NLPPhrase)
     { NLPPhrase nphrase = (NLPPhrase) np; 
       NLPWord nounwd = nphrase.identifyNounForEntity(); 
       System.out.println(">>> Alternative subclass " + nounwd.text + " of " + nphrase); 
       String singular = nounwd.getSingular(); 
       String cleanName = 
          Named.removeInvalidCharacters(singular);  

       Entity entnew = (Entity) 
         ModelElement.lookupByNameIgnoreCase(
                          cleanName, modelElements); 
       if (entnew == null) 
       { entnew = new Entity(Named.capitalise(cleanName));
         modelElements.add(entnew);
         String id = sentence.id; 
         entnew.addStereotype("originator=\"" + id + "\""); 
         sentence.derivedElements.add(entnew);
       }
       entnew.setSuperclass(ent);
       ent.addSubclass(entnew);  
       System.out.println(">>> possible alternative subclass: " + entnew.getName()); 
     }
     else if (np instanceof NLPWord && ((NLPWord) np).isNoun())
     { NLPWord npword = (NLPWord) np; 
       String noun = npword.text; 
       String singular = npword.getSingular(); 
       
       String cleanName = 
          Named.removeInvalidCharacters(singular);  
       Entity entnew = (Entity) 
         ModelElement.lookupByNameIgnoreCase(
                          cleanName, modelElements); 
       if (entnew == null) 
       { entnew = new Entity(Named.capitalise(cleanName));
         modelElements.add(entnew);
         String id = sentence.id; 
         entnew.addStereotype("originator=\"" + id + "\""); 
         sentence.derivedElements.add(entnew);
       }
       entnew.setSuperclass(ent);
       ent.addSubclass(entnew);  
       System.out.println(">>> alternative class: " + entnew.getName()); 
      } 	   
    }
  } 

  public Vector extractValues(Attribute att, Entity ent, Vector modelElements, Vector atts)
  { // Find the NP disjunctions which name alternatives and identify the classes & specialisation relations
    Vector values = new Vector(); 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement np = (NLPPhraseElement) elements.get(i); 
      if ("NP".equals(np.tag) && np instanceof NLPPhrase)
      { NLPPhrase nphrase = (NLPPhrase) np; 
        String noun = nphrase.identifyNounOrAdjective(); 
       // String singular = NLPWord.getSingular(noun); 

        Entity ex = ModelElement.featureBelongsTo(noun, modelElements); 
        if (ex == null) 
        { // Assume it is a value 
          values.add(noun); 
        }
        else 
        { Attribute otheratt = ex.getDefinedPropertyIgnoreCase(noun); 
          atts.add(otheratt); 
        } 
        System.out.println(">>> alternative value: " + noun); 
      }
      else if ("ADJP".equals(np.tag) && np instanceof NLPPhrase)
      { Vector vals1 = ((NLPPhrase) np).extractValues(att, ent, modelElements, atts); 
        values.addAll(vals1); 
      }
      else if (np instanceof NLPWord && ((NLPWord) np).isNounOrAdjective())
      { NLPWord npword = (NLPWord) np; 
        String noun = npword.text; 
       // String singular = NLPWord.getSingular(noun); 
       
        Entity entnew =  ModelElement.featureBelongsTo(noun, modelElements); 
        if (entnew == null) 
        { values.add(noun); }
        else 
        { Attribute otheratt = entnew.getDefinedPropertyIgnoreCase(noun); 
          atts.add(otheratt); 
        } 
        System.out.println(">>> alternative value: " + noun); 
      } 	   
    }
    return values;
  } 

  public java.util.HashMap classifyWords(Vector background, Vector modelElements)
  { java.util.HashMap res = new java.util.HashMap(); 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord wd = (NLPWord) elem; 
        java.util.HashMap mp = wd.classifyWords(background, modelElements);
        res.putAll(mp); 
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        java.util.HashMap mp = phr.classifyWords(background, modelElements); 
        res.putAll(mp); 
	 }
    }
    return res; 
  }

  public java.util.HashMap classifyVerbs(Vector verbs)
  { java.util.HashMap res = new java.util.HashMap(); 
  
    for (int i = 0; i < elements.size(); i++) 
    { NLPPhraseElement elem = (NLPPhraseElement) elements.get(i); 
      if (elem instanceof NLPWord) 
      { NLPWord wd = (NLPWord) elem; 
        java.util.HashMap mp = wd.classifyVerbs(verbs);
        res.putAll(mp); 
      } 
      else if (elem instanceof NLPPhrase)
      { NLPPhrase phr = (NLPPhrase) elem; 
        java.util.HashMap mp = phr.classifyVerbs(verbs); 
        res.putAll(mp); 
	 }
    }
    return res; 
  }
} 