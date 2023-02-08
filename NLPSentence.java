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

public class NLPSentence
{ Vector phrases = new Vector();  // of NLPPhraseElement
  Vector derivedElements = new Vector(); // of ModelElement
  String id = "0"; 

  public static double NMS_THRESHOLD = 0.5; 
  
  /* Used for operationsKM3: */ 
  private static Entity currentEntity = null; 
  private static BehaviouralFeature currentOperation = null; 
  private static java.util.Map inputEntities = new java.util.HashMap(); 
  private static java.util.Map outputEntities = new java.util.HashMap(); 


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
  
  public boolean isSystemDefinition(java.util.Map fromBackground)
  { // first noun is "system", "application", etc
    NLPPhraseElement p1 = (NLPPhraseElement) phrases.get(0); 
    if (p1 instanceof NLPPhrase && p1.tag.equals("NP"))
    { NLPPhrase pr = (NLPPhrase) p1; 
      Vector nouns = pr.getNouns(); 

      for (int i = 0; i < nouns.size(); i++) 
      { String noun = (String) nouns.get(i); 

        if (noun.equals("System") || noun.equals("system") || noun.equals("Application") || 
          noun.equals("application") || noun.equals("app") || noun.equals("App") || 
          noun.equals("software") || noun.equals("Software") || noun.equals("Program") || 
          noun.equals("program") || 
          noun.equals("database") || noun.equals("Database"))
        { return true; }

        Object obj = fromBackground.get(noun); 
        System.out.println(">>> " + noun + " background ==> " + obj); 
		
        if (obj != null)
        { Vector sem = (Vector) obj; 
          if (sem.size() > 0 && sem.get(0) instanceof UMLPackage)
          { return true; } 
        } 
       }
     }
     // Or if a noun has "system" semantics in the 
     // knowledge base. 

     return false; 
  }

  public boolean isOperationDefinition(Vector modelElems, java.util.Map fromBackground, Vector res)
  { // first noun includes "operation", "service", etc
    // or subject contains an operation name. 

    String opname = ""; 

    NLPPhraseElement p1 = (NLPPhraseElement) phrases.get(0); 
    if (p1 instanceof NLPPhrase && p1.tag.equals("NP"))
    { NLPPhrase pr = (NLPPhrase) p1; 
      Vector nouns = pr.getNouns(); 

      for (int i = 0; i < nouns.size(); i++) 
      { String noun = (String) nouns.get(i); 

        BehaviouralFeature bf = ModelElement.lookupOperationNMS(noun,modelElems,NMS_THRESHOLD); 
        if (bf != null) 
        { System.out.println(">> Operation " + noun);
          res.add(bf.getName()); 
          return true; 
        } 

        if (noun.equalsIgnoreCase("operation") || 
          noun.equalsIgnoreCase("service") || 
          noun.equalsIgnoreCase("procedure") || 
          noun.equalsIgnoreCase("function") || 
          noun.startsWith("function") || 
          noun.equalsIgnoreCase("usecase") || 
          noun.equalsIgnoreCase("process") || 
          noun.equalsIgnoreCase("routine") || 
          noun.equalsIgnoreCase("utility"))
        { System.out.println(">> Operation " + opname);
          res.add(opname);  
          return true; 
        }
        else if ("".equals(opname))
        { opname = noun; } 
        else 
        { opname = opname + Named.capitalise(noun); } 

        /* Object obj = fromBackground.get(noun); 
        System.out.println(">>> " + noun + " background ==> " + obj); 
		
        if (obj != null)
        { Vector sem = (Vector) obj; 
          if (sem.size() > 0 && sem.get(0) instanceof Package)
          { return true; } 
        } */ 
 
       } 
     }
     // Or if a noun is already known to be an operation

     return false; 
  }

  public boolean isOperationDefinition(Vector modelElems, java.util.Map fromBackground, Vector np1, Vector vb1, Vector res)
  { // first noun includes "operation", "service", etc
    // or subject contains an operation name. 

    String opname = ""; 

    Vector possibleOpNames = new Vector(); 
    possibleOpNames.addAll(np1); 
    possibleOpNames.addAll(vb1); 

    for (int i = 0; i < possibleOpNames.size(); i++) 
    { NLPWord wd = (NLPWord) possibleOpNames.get(i); 
      String noun = wd.text;  

      BehaviouralFeature bf = ModelElement.lookupOperationNMS(noun,modelElems,NMS_THRESHOLD); 
      if (bf != null) 
      { System.out.println(">> Operation " + noun);
        res.add(bf.getName()); 
        return true; 
      } 

      if (noun.equalsIgnoreCase("operation") || 
          noun.equalsIgnoreCase("service") || 
          noun.equalsIgnoreCase("procedure") || 
          noun.equalsIgnoreCase("function") || 
          noun.toLowerCase().startsWith("function") || 
          noun.equalsIgnoreCase("process") || 
          noun.equalsIgnoreCase("routine") ||
          noun.equalsIgnoreCase("usecase") ||  
          noun.equalsIgnoreCase("utility"))
      { System.out.println(">> Operation " + opname);
        res.add(opname);  
        return true; 
      }
      else if ("".equals(opname))
      { opname = noun; } 
      else 
      { opname = opname + Named.capitalise(noun); } 

        /* Object obj = fromBackground.get(noun); 
        System.out.println(">>> " + noun + " background ==> " + obj); 
		
        if (obj != null)
        { Vector sem = (Vector) obj; 
          if (sem.size() > 0 && sem.get(0) instanceof Package)
          { return true; } 
        } */  
     }
     // Or if a noun is already known to be an operation

     return false; 
  }

  public Vector identifyOperations(Vector modelElems, java.util.Map fromBackground, Vector words)
  { // A word or successive words are a known operation 
    Vector res = new Vector(); 

    for (int i = 0; i < words.size(); i++) 
    { NLPWord wd = (NLPWord) words.get(i); 
      String noun = wd.text.toLowerCase();  

      BehaviouralFeature bf = ModelElement.lookupOperationNMS(noun,modelElems,NMS_THRESHOLD); 
      if (bf != null) 
      { System.out.println(">> Operation " + noun);
        res.add(bf); 
      } 
      else if (i+1 < words.size())
      { NLPWord wd2 = (NLPWord) words.get(i+1); 
        String nme = noun + Named.capitalise(wd2.text); 
        bf = ModelElement.lookupOperationNMS(nme, modelElems, NMS_THRESHOLD); 
        if (bf != null) 
        { System.out.println(">> Operation " + nme);
          res.add(bf); 
        } 
        else if (i+2 < words.size())
        { NLPWord wd3 = (NLPWord) words.get(i+2); 
          String nme2 = noun + Named.capitalise(wd2.text) + Named.capitalise(wd3.text); 
          bf = ModelElement.lookupOperationNMS(nme2, modelElems, NMS_THRESHOLD); 
          if (bf != null) 
          { System.out.println(">> Operation " + nme2);
            res.add(bf); 
          }
        }  
      } 
    }
    return res; 
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

  public NLPPhrase getObjectPart()
  { // The part after the main verb:
    // ((subject) (VP ... main verb ... (NP object)))
	
    if (phrases.get(1) instanceof NLPPhrase) { } 
    else 
    { return null; }
	
    String verb = ""; 
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    if (p2.tag.equals("VP"))
    { return p2.getObjectPart(); }  
    else if (p2.tag.equals("ADVP") && phrases.size() > 2 && phrases.get(2) instanceof NLPPhrase)
    { NLPPhrase p3 = (NLPPhrase) phrases.get(2);
      return p3.getObjectPart(); 
    } 
	
    return p2; 
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

  public boolean isClassDefinition(Vector np, Vector vb, Vector quals)
  { // The VP verb is "consists"/"has"/"have", etc
    
    String verb0 = getMainVerb(); 
    if (verb0 == null) 
    { return false; } 
	
    String verb = verb0.toLowerCase(); 

    System.out.println(">>> Main verb is: " + verb); 
	
    if ("has".equals(verb) || "have".equals(verb) || 
        "=".equals(verb) || "equals".equals(verb) || 
        verb.startsWith("define") || 
        verb.startsWith("compris") || verb.startsWith("consist") || 
        verb.equals("composed") ||
        verb.equalsIgnoreCase("specify") ||
        verb.startsWith("include") || verb.equals("made")) 
	{ return true; } 
	else if (verb.startsWith("record") || verb.startsWith("store") || 
        verb.startsWith("persist") || verb.startsWith("retain"))
    { quals.add("persistent"); 
      return true; 
    } // but likely to be a system/app in the subject.
 
    return false; 
  } 

  public boolean isOperationBehaviourDefinition(Vector np, Vector vb, Vector rem)
  { // The main verb is classified as an update, deletion, creation, etc
    
    String verb0 = getMainVerb(); 
    if (verb0 == null) 
    { return false; } 
	
    String verb = verb0.toLowerCase(); 

    System.out.println(">>> Main verb is: " + verb); 

    NLPWord vrb = new NLPWord("VB", verb); 
	
    java.util.Map mp = new java.util.HashMap(); 
    Vector quals = new Vector(); 

    if (vrb.isSignificantVerbPhraseWord(quals,mp)) 
    { return true; } 

    // Or, it features some result parameter of the operation

    // Get the extractOperationDefs from np1, vb1
    // if not null, return true. 
	
    return false; 
  } 

  public boolean isOperationBehaviourDefinition(
      BehaviouralFeature op, 
      java.util.Map bk, 
      Vector elems, Vector np1, Vector vb1, Vector rem)
  { // Is an identifier from outputEntities[op.name] also
    // in  np1^vb1^rem?

    if (op == null) 
    { return false; } 
 
    String opname = op.getName(); 

    NLPPhrase pr = new NLPPhrase("NP"); 
    pr.sentence = this; 
    
    pr.elements.addAll(np1); 
    pr.elements.addAll(vb1); 
    pr.elements.addAll(rem); 
    
    // java.util.Map mp = new java.util.HashMap(); 
    // Vector currentQuals = new Vector(); 
    // java.util.Map types1 = new java.util.HashMap(); 
    Entity ex = new Entity("Behaviour" + opname);  
    Vector anal = 
            pr.extractAttributeDefinitions(ex, bk, elems); 
 
    System.out.println(">>-->> Extracted nouns: " + anal); 
 
    Entity outEnt = (Entity) outputEntities.get(opname); 
    Entity inEnt = (Entity) inputEntities.get(opname); 

    System.out.println(">>-->> for behaviour of: " + opname);
    System.out.println(">>-->> input entity: " + inEnt);
    System.out.println(">>-->> output entity: " + outEnt);
 
    String sem = NLPWord.literalForm(pr.elements); 

    if (outEnt != null && outEnt.hasAnyAttribute(anal)) 
    { System.out.println(">>> " + sem + " is Postcondition/effect on: " + anal);
      op.addPostcondition(sem); 
      return true; 
    }
    else if (inEnt != null && inEnt.hasAnyAttribute(anal)) 
    { System.out.println(">>> " + sem + " is Precondition on: " + anal);
      op.addPrecondition(sem); 
      return true; 
    }  
    else 
    { System.out.println(">>> " + sem + " is activity of: " + opname);
      op.addActivity(sem); 
      return true; 
    } 

     
  } 

  public boolean refersToOperation(Vector elems, Vector np1, Vector vb1, Vector rem, Vector ops)
  { Vector allwords = new Vector(); 
    allwords.addAll(np1); 
    allwords.addAll(vb1); 
    allwords.addAll(rem); 
    NLPPhrase p1 = new NLPPhrase("VP", allwords); 
    Vector opnames = p1.possibleOperationNames(); 
   
    for (int i = 0; i < opnames.size(); i++) 
    { String opname = (String) opnames.get(i); 
      BehaviouralFeature bf = 
        ModelElement.lookupOperationNMS(opname, elems, NMS_THRESHOLD); 
      if (bf != null) 
      { ops.add(bf); } 
    } 
    
    if (ops.size() > 0)
    { return true; } 
    return false; 
  } 

  public boolean isClassOperationsDefinition(Vector np, Vector vb, Vector quals)
  { // The VP verb is "provides"/"supports"/"supplies", etc
    
    String verb0 = getMainVerb(); 
    if (verb0 == null) 
    { return false; } 
	
    String verb = verb0.toLowerCase(); 

    System.out.println(">>> Main verb is: " + verb); 
	
    if ("has".equals(verb) || "supplies".equals(verb) || 
        "offers".equals(verb) || 
        "provides".equals(verb) || "supports".equals(verb) || 
        verb.startsWith("define") || 
        verb.equalsIgnoreCase("specify") ||
        verb.startsWith("include")) 
    { return true; } 
 
    return false; 
  } 

  public boolean isClassOperationsDefinition(Vector np1, Vector vb1, Vector rem, Vector elems)
  { if (np1.size() > 0 && vb1.size() > 0)
    { NLPWord subj = (NLPWord) np1.get(0); 
      String wd = subj.text.toLowerCase(); 
      if ("services".equals(wd) || 
          "operations".equals(wd) || 
          "methods".equals(wd) || 
          "functionalities".equals(wd) ||
          "functions".equals(wd))
      { NLPWord vb = (NLPWord) vb1.get(0); 
        String vbwd = vb.text.toLowerCase(); 
        if ("provided".equals(vbwd) ||
            "offered".equals(vbwd) ||
            "supported".equals(vbwd)) 
        { return true; } 
      } 
    }
    return false; 
  } 

  public boolean isClassOperationsDefinition2(Vector np1, Vector vb1, Vector rem, Vector elems)
  { if (rem.size() > 0 && vb1.size() > 0)
    { NLPWord subj = (NLPWord) rem.get(0); 
      String wd = subj.text.toLowerCase(); 
      if ("services".equals(wd) || 
          "operations".equals(wd) || 
          "methods".equals(wd) || 
          "functionalities".equals(wd) ||
          "functions".equals(wd))
      { NLPWord vb = (NLPWord) vb1.get(0); 
        String vbwd = vb.text.toLowerCase(); 
        if ("provides".equals(vbwd) ||
            "supplies".equals(vbwd) || 
            "offers".equals(vbwd) ||
            "has".equals(vbwd) || 
            "supports".equals(vbwd) ||
            vbwd.startsWith("define") || 
            vbwd.equalsIgnoreCase("specify") ||
            vbwd.startsWith("include")) 
        { return true; } 
      } 
    }
    return false; 
  } 

  public boolean isGeneralisationDefinition(Vector np, Vector vb, Vector rem)
  { // np non-empty, vb is "shall be" or "is", 
    // rem is disjunctive. 
    
    if (np.size() > 0 && vb.size() >= 2)
    { NLPWord v1 = (NLPWord) vb.get(0); 
      NLPWord v2 = (NLPWord) vb.get(1); 
      if (v1.isModalVerb() && "be".equalsIgnoreCase(v2.text) &&
          NLPPhrase.isDisjunction(rem))
      { return true; } 
    }
    else if (vb.size() > 0 && NLPPhrase.isDisjunction(rem))
    { NLPWord v1 = (NLPWord) vb.get(0); 
      if ("is".equals(v1.text) || "are".equals(v1.text))
      { return true; } 
    }

    if (isGeneralDefinition()) 
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
    String cleanNoun = Named.removeInvalidCharacters(noun); 
    Object obj = 
      ModelElement.lookupByNameIgnoreCase(
                               cleanNoun, modelElements); 
    if (obj == null) 
    { String nme = Named.capitalise(cleanNoun); 
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

  public Vector operationModelElements(java.util.Map fromBackground, Vector modelElements)
  { // assuming SVO && isClassOperationsDefinition

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
    if (noun == null || noun.length() == 0) 
    { return res; }
	
    NLPPhrase p2 = (NLPPhrase) phrases.get(1);
    if (p2.tag.equals("ADVP"))
    { p2 = (NLPPhrase) phrases.get(2); }
	
    Entity ent = null;
    String cleanNoun = Named.removeInvalidCharacters(noun);  
    Object obj = 
      ModelElement.lookupByNameIgnoreCase(
                           cleanNoun, modelElements); 
    if (obj == null) 
    { String nme = Named.capitalise(cleanNoun); 
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
    { p2.extractOperationDefinitions(ent, fromBackground, modelElements); } 
	
    return res;  
  } 

  public Vector operationModelElements(Vector np1, Vector vb1, Vector rem, java.util.Map fromBackground, Vector modelElements)
  { // for isClassOperationsDefinition

    Vector res = new Vector();
    NLPPhrase p1 = new NLPPhrase("NP");
    p1.elements = rem;
    p1.sentence = this; 
  
    String noun = p1.getPrincipalNoun(); 
    if (noun == null || noun.length() == 0) 
    { return res; }
		
    String cleanNoun = Named.removeInvalidCharacters(noun);  
    Entity ent = null; 
    Object obj = 
      ModelElement.lookupByNameIgnoreCase(
                            cleanNoun, modelElements); 
    if (obj == null) 
    { String nme = Named.capitalise(cleanNoun); 
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
    { p1.extractOperationDefs(ent, fromBackground, modelElements); } 
	
    return res;  
  } 

  public Vector operationModelElements2(Vector np1, Vector vb1, Vector rem, java.util.Map fromBackground, Vector modelElements)
  { // for isClassOperationsDefinition

    Vector res = new Vector();
    NLPPhrase p1 = new NLPPhrase("NP");
    p1.elements = np1;
    p1.sentence = this; 
  
    String noun = p1.getPrincipalNoun(); 
    if (noun == null || noun.length() == 0) 
    { return res; }
		
    Entity ent = null; 
    String cleanNoun = Named.removeInvalidCharacters(noun);  

    Object obj = ModelElement.lookupByNameIgnoreCase(
                                  cleanNoun, modelElements); 
    if (obj == null) 
    { String nme = Named.capitalise(cleanNoun); 
      ent = new Entity(nme);
      modelElements.add(ent); 
      res.add(ent);  
      System.out.println(">>> New entity " + nme); 
      derivedElements.add(ent); 
      ent.addStereotype("originator=\"" + id + "\""); 
    } 
    else if (obj instanceof Entity)
    { ent = (Entity) obj; }

    NLPPhrase p2 = new NLPPhrase("NP");
    p2.elements = rem;
    p2.sentence = this; 
	
    if (ent != null) 
    { p2.extractOperationDefs(ent, fromBackground, modelElements); } 
	
    return res;  
  } 


  public Vector generalisationElements(Vector rem, Vector melems)
  { return relationElements(melems); } 

  public Vector relationElements(Vector modelElements)
  { // assuming SVO && isGeneralisationDefinition

    Vector res = new Vector();
    NLPPhrase p1 = (NLPPhrase) phrases.get(0); 
    String noun = p1.getPrincipalNoun(); 
    if (noun == null || noun.length() == 0) 
    { return res; }

    System.out.println(">> Principal subject noun is: " + noun); 
    
    if (NLPWord.isKeyword(noun))
    { return res; } 
	
    String singular = noun; 
      // NLPWord.getSingular(noun); 
	
    NLPPhrase p2 = (NLPPhrase) getObjectPart();
    System.out.println(">===> Object part of sentence = " + p2); 
    if (p2 == null) 
    { return res; }

    String cleanNoun = 
              Named.removeInvalidCharacters(singular);  

    Entity ent = null; 
    Object obj = ModelElement.lookupByNameIgnoreCase(
                                 cleanNoun, modelElements); 
    if (obj == null) 
    { String nme = Named.capitalise(cleanNoun); 
      ent = ModelElement.featureBelongsTo(
                            cleanNoun, modelElements); 
      if (ent != null) // it is a feature, a boolean or effective enumeration
      { Attribute att = 
           ent.getDefinedPropertyIgnoreCase(cleanNoun); 
        p2.extractAlternativeValues(att, ent, modelElements); 
        derivedElements.add(att); 
        return res; 
      }
	
      ent = new Entity(nme);
      System.out.println(">>> New entity: " + ent.getName());
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
	
    String cleanNoun = Named.removeInvalidCharacters(noun);  

    Object obj = ModelElement.lookupByNameIgnoreCase(
                                 cleanNoun, modelElements); 
    if (obj == null) 
    { ent = new Entity(Named.capitalise(cleanNoun));
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
    NLPWord nounwd = p1.identifyNounForEntity();  
    if (nounwd == null || nounwd.text.length() == 0)
    { return res; }
	
    if (phrases.size() < 2) 
    { return res; }

    System.out.println(">>> Principal noun: " + nounwd); 
	
    NLPPhraseElement p2 = (NLPPhraseElement) phrases.get(1);
    boolean notfound = true; 
    for (int x = 1; x < phrases.size() && notfound; x++) 
    { p2 = (NLPPhraseElement) phrases.get(x);
      if (p2.isVerbPhrase()) 
      { notfound = false; }
    } 
	
    Entity ent = null; 
    String singular = nounwd.getSingular(); 
    String cleanNoun = 
         Named.removeInvalidCharacters(singular);  

    Object obj = ModelElement.lookupByNameIgnoreCase(
                          cleanNoun, modelElements); 
    if (obj == null) 
    { ent = new Entity(Named.capitalise(cleanNoun));

      System.out.println(">>> New class: " + ent.getName()); 
      modelElements.add(ent); 
      res.add(ent); 
      derivedElements.add(ent); 
      ent.addStereotype("originator=\"" + id + "\""); 
    } 
    else 
    { System.out.println(">>> Existing model element: " + 
                         cleanNoun); 
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
    Vector seqs = sequentialise(); 
    Vector np1 = new Vector(); 
    Vector vb1 = new Vector(); 
    Vector rem = new Vector();   
    Vector comment = new Vector(); 

    splitIntoPhrases(seqs,np1,vb1,rem,comment); 

    System.out.println(">>> Sentence split as: " + np1 + "; " + vb1 + "; " + rem + "; " + comment); 
	
    if (isSVO() && isSystemDefinition(fromBackground) && !describesUseCase(np1,vb1,rem))
    { System.out.println(">>> System definition: " + this); 
      Vector quals1 = new Vector(); 	  
      identifyClassesAndFeatures(fromBackground, rem, elems, quals1);
    }
    else if (isSVO() && isClassDefinition(np1,vb1,quals))
    { System.out.println(">>> Class definition: " + this); 
      modelElements(fromBackground,elems);	 
    }
    else if (isSVO() && isAssociationDefinition())
    { System.out.println(">>> Association definition: " + this); 
      associationElements(fromBackground,elems);
    }
    else if (isSVO() && isGeneralisationDefinition(np1,vb1,rem))
    { System.out.println(">>> Generalisation definition: " + this); 
      generalisationElements(rem,elems);
    }
    else 
    { if (describesUseCase(np1,vb1,rem))
      { System.out.println(">>> This may be a use case description"); 
	    // identifyUseCase(rem,elems); 
        
        System.out.println(">>> Split as follows (noun phrase, verb phrase, remainder, comment): " + np1 + "; " + vb1 + "; " + 
	                   rem + "; " + comment);
        java.util.Map mp = new java.util.HashMap(); 
        UseCase uc = identifyUseCase(np1,vb1,rem,elems,mp,verbClassifications); 
        System.out.println(">>> Identified use case " + uc); 
		
        identifyModelElements(uc, np1, vb1, rem, fromBackground, verbClassifications, mp, elems);   
      }
      else if (describesSystem(np1))
      { System.out.println(">>> This may be a system-level requirement"); 
        Vector quals1 = new Vector(); 	  
        identifyClassesAndFeatures(fromBackground, rem, elems, quals1); 
      }  
      else if (isSVO() || constraintDefinition(np1,vb1,rem))
      { System.out.println(">>> Possible constraint definition: " + this); 
        otherRelationElements(fromBackground,elems); 
        Vector quals1 = new Vector(); 	  
        identifyClassesAndFeatures(fromBackground, rem, elems, quals1); 
      }
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

  public Vector getOperationsKM3(Vector elems, java.util.Map fromBackground)
  { Vector quals = new Vector(); 
    java.util.HashMap verbClassifications = new java.util.HashMap(); 
    Vector seqs = sequentialise(); 
    Vector np1 = new Vector(); 
    Vector vb1 = new Vector(); 
    Vector rem = new Vector();   
    Vector comment = new Vector();
 
    // Entity currentEntity = null; 
    // java.util.Map inputEntities = new java.util.HashMap(); 
    // java.util.Map outputEntities = new java.util.HashMap(); 
 
    splitIntoPhrases(seqs,np1,vb1,rem,comment); 

    System.out.println(">>> Sentence split as: " + np1 + "; " + vb1 + "; " + rem + "; " + comment); 
	
    Vector ops = new Vector(); 
    // BehaviouralFeature currentOperation = null; 

    if (isSVO() && isClassOperationsDefinition(np1, vb1, quals))
    { System.out.println(">>> Class operations definition (1): " + this);
      Vector ents = operationModelElements(fromBackground,elems);	
      if (ents.size() > 0) 
      { currentEntity = (Entity) ents.get(0); 
        currentOperation = null; 
      } // and reset the inputEntities, outputEntities 
    } 
    else  
    if (isClassOperationsDefinition(np1,vb1,rem,elems))
    { System.out.println(">>> Class operations definition (2): " + this);
      Vector ents = operationModelElements(np1,vb1,rem,fromBackground,elems);	
      if (ents.size() > 0) 
      { currentEntity = (Entity) ents.get(0); 
        currentOperation = null; 
      } // and reset the inputEntities, outputEntities  
    } 
    else if (isClassOperationsDefinition2(np1,vb1,rem,elems))
    { System.out.println(">>> Class operations definition (3): " + this);
      Vector ents = operationModelElements2(np1,vb1,rem,fromBackground,elems);	
      if (ents.size() > 0) 
      { currentEntity = (Entity) ents.get(0); 
        currentOperation = null; 
      } // and reset the inputEntities, outputEntities  
    } 
    else if (isSVO() && isOperationDefinition(elems,fromBackground,ops))
    { System.out.println(); 
      System.out.println(">>> Operation definition (1): " + this); 
      // Vector quals1 = new Vector();
      if (ops.size() > 0) 
      { String opname = (String) ops.get(0);
        ops.clear(); 
        BehaviouralFeature bf = 
           // (BehaviouralFeature)     
           // ModelElement.lookupByNameNMS(opname,elems,0.4); 
           ModelElement.lookupOperationNMS(opname,elems,NMS_THRESHOLD); 
        currentOperation = bf; 

        Entity entin;
        Entity entout; 
 
        if (bf == null) // Not a known entity operation
        { bf = new BehaviouralFeature(opname);
          currentOperation = bf; 
          elems.add(bf);
          entin = new Entity(opname + "Inputs"); 
          inputEntities.put(opname, entin); 
          entout = new Entity(opname + "Outputs");  
          outputEntities.put(opname, entout); 
        }  // effectively, a use case.  
        else 
        { entin = (Entity) inputEntities.get(opname); 
          if (entin == null) 
          { entin = new Entity(opname + "Inputs"); 
            inputEntities.put(opname, entin); 
          }
          entout = (Entity) outputEntities.get(opname);
          if (entout == null) 
          { entout = new Entity(opname + "Outputs"); 
            outputEntities.put(opname, entout); 
          }
        } 

        NLPPhrase inphrase = identifyInputPhrases();
        if (inphrase != null) 
        { inphrase.extractAttributeDefinitions(entin, fromBackground, elems);
        } 
       
        NLPPhrase outphrase = identifyOutputPhrases(); 	  
        if (outphrase != null) 
        { outphrase.extractAttributeDefinitions(entout, fromBackground, elems); } 

        if (inphrase == null && outphrase == null) 
        { // is it a precondition/postcondition? 
          // It is a postcondition if any entout variable 
          // occurs in it, otherwise a precondition. 

          // NLPPhrase objpart = getObjectPart(); 
          NLPPhrase pr = new NLPPhrase("NP"); 
          pr.elements.addAll(vb1); 
          pr.elements.addAll(rem); 
          pr.sentence = this; 

          System.out.println(">===> Operation assertion: " + pr);
    
          // java.util.Map mp = new java.util.HashMap(); 
          // Vector currentQuals = new Vector(); 
          // java.util.Map types1 = new java.util.HashMap();
          Entity ex = new Entity("Behaviour" + opname);  
          Vector anal = 
            pr.extractAttributeDefinitions(ex, fromBackground, elems); 
 
          if (bf != null) 
          { String sem = NLPWord.literalForm(pr.elements); 
            if (entout != null && entout.hasAnyAttribute(anal))
            { bf.addPostcondition(sem); } 
            else if (entin != null && entin.hasAnyAttribute(anal))
            { bf.addPrecondition(sem); }
            else 
            { bf.addActivity(sem); }  
          } 
        } 

        if (bf != null) 
        { System.out.println(">>> Defining " + bf + " from " + entin + " and " + entout); 
          bf.defineParameters(entin,entout);
          if (inphrase != null) 
          { bf.addPrecondition(inphrase.literalForm()); } 
          if (outphrase != null) 
          { bf.addPostcondition(outphrase.literalForm()); }  
        } 
      } 
      // Derive the operation from the entin, entout
      // conjoin preconditions & postconditions
      // precondition if main verb is "assumes", "requires"
      // "is"
      // postcondition if "establishes", "ensures"
    }
    else if (isOperationDefinition(elems, fromBackground, np1, vb1, ops))
    { System.out.println(">>> Operation definition (2): " + ops); 
      if (ops.size() > 0) 
      { String opname = (String) ops.get(0);
        ops.clear(); 
        BehaviouralFeature bf = 
           // (BehaviouralFeature)     
           // ModelElement.lookupByNameNMS(opname,elems,0.4); 
           ModelElement.lookupOperationNMS(opname, elems, NMS_THRESHOLD); 
        Entity entin = (Entity) inputEntities.get(opname); 
        Entity entout = (Entity) outputEntities.get(opname); 
        if (bf != null) 
        { currentOperation = bf; }  
        Vector quals1 = new Vector();
        identifyInputsAndOutputs(bf, entin, entout, fromBackground, vb1, rem, elems, quals1);
      } 
      else 
      { Vector quals1 = new Vector(); 	  
        identifyClassesAndFeatures(fromBackground, rem, elems, quals1);
      } 
    }
    else if (isConditionalBehaviour(np1,vb1,rem))
    { // if something ...  
      // assumed to be a postcondition
      System.out.println(">>> Conditional behaviour: " + np1 + vb1 + rem); 
      
      NLPPhrase p1 = new NLPPhrase("NP");
      p1.elements = new Vector();
      p1.elements.addAll(np1); 
      p1.elements.addAll(vb1); 
      p1.elements.addAll(rem); 
      p1.sentence = this; 
      Vector op1s = identifyOperations(elems, fromBackground, p1.elements); 
      if (op1s.size() > 0)
      { BehaviouralFeature bf1 = (BehaviouralFeature) op1s.get(0); 
        currentOperation = bf1; 
        bf1.addActivity(
               NLPWord.literalForm(p1.elements)); 
      } 
      else if (currentOperation != null) 
      { currentOperation.addActivity(
            NLPWord.literalForm(p1.elements)); 
      } 
    } 
    else if (isOperationBehaviourDefinition(np1,vb1,rem))
    { System.out.println(">>> Processing behaviour (1): " + np1 + vb1 + rem);
      Vector wds = new Vector(); 
      wds.addAll(np1); 
      wds.addAll(vb1); 
      wds.addAll(rem); 
      Vector op1s = identifyOperations(elems, fromBackground, wds); 
      System.out.println(">>> for operation: " + op1s);

      Vector wds1 = new Vector(); 
      wds1.addAll(vb1); 
      wds1.addAll(rem); 
          
      if (op1s.size() > 0)
      { BehaviouralFeature bf1 = (BehaviouralFeature) op1s.get(0); 
        currentOperation = bf1; 
        bf1.addActivity(
               NLPWord.literalForm(wds1)); 
      } 
      else if (currentOperation != null) 
      { currentOperation.addActivity( 
                NLPWord.literalForm(wds1) );
      }  
    } 
    else if (refersToOperation(elems, np1, vb1, rem, ops))
    { System.out.println(">>> Processing behaviour (2): " + np1 + vb1 + rem);
      
      Vector wds1 = new Vector(); 
      wds1.addAll(vb1); 
      wds1.addAll(rem); 
          
      if (ops.size() > 0)
      { BehaviouralFeature bf2 = (BehaviouralFeature) ops.get(0); 
        ops.clear(); 
        currentOperation = bf2; 
        bf2.addActivity(
               NLPWord.literalForm(wds1)); 
      } 
      else if (currentOperation != null) 
      { currentOperation.addActivity( 
                NLPWord.literalForm(wds1) );
      }
    } 
    else if (isSVO() && isClassDefinition(np1,vb1,quals))
    { System.out.println(">>> Class definition: " + this); 
      modelElements(fromBackground,elems);	 
    }
    else 
    { Vector quals1 = new Vector(); 	  
      identifyClassesAndFeatures(fromBackground, rem, elems, quals1);
    } 
    
    return elems; 
  } 

  public static String operationsKM3(Vector elems)
  { String res = ""; 

    currentOperation = null; 
    inputEntities = new java.util.HashMap(); 
    outputEntities = new java.util.HashMap(); 

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
    { if (elems.get(i) instanceof BehaviouralFeature) 
      { BehaviouralFeature bf = (BehaviouralFeature) elems.get(i); 
        UseCase uc = bf.toUseCase(); 
        res = res + uc.getKM3() + "\n\n";
      }
    } 
 
    return res; 
  } 

  public boolean isConditionalBehaviour(Vector np, Vector vb, Vector rem)
  { // if ...; when ...; 
    if (NLPPhrase.isConditional(np)) 
    { return true; } 
    else if (np.size() == 0 && NLPPhrase.isConditional(vb))
    { return true; } 
    return false; 
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
      if (j < en-1 && "so".equalsIgnoreCase(lex.text.trim()) && "that".equalsIgnoreCase(((NLPWord) seq.get(j+1)).text.trim()))
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
      if (textlc.startsWith("system") || textlc.startsWith("app") ||
          textlc.startsWith("software") || 
          textlc.equals("program"))
      { return true; }
    }
    return false; 
  } // Or wd is known to be a system. 
  
  public NLPPhrase identifyInputPhrases()
  { for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement pr = (NLPPhraseElement) phrases.get(i); 
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

  public NLPPhrase identifyOutputPhrases()
  { for (int i = 0; i < phrases.size(); i++) 
    { NLPPhraseElement pr = (NLPPhraseElement) phrases.get(i); 
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
    String cleanNoun = 
        Named.removeInvalidCharacters(firstNoun);  
 
    Entity mainent = (Entity) 
       ModelElement.lookupByNameIgnoreCase(
                         cleanNoun, modelElements); 
    if (mainent == null) 
    { mainent = new Entity(Named.capitalise(cleanNoun)); 
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
    newpr.sentence = this; 
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

  public void identifyInputsAndOutputs(BehaviouralFeature op, Entity inputEnt, Entity outputEnt, java.util.Map fromBackground, Vector vb1, Vector rem, Vector modelElements, Vector quals)
  { // After "inputs" is the input part, after "outputs"
    // is the output part

    if (op == null) 
    { return; } 
    String opname = op.getName(); 

    Vector inputPart = new Vector(); 
    Vector outputPart = new Vector(); 
	 
    boolean inInput = false; 
    boolean inOutput = false; 

    for (int i = 0; i < vb1.size(); i++) 
    { NLPWord wd = (NLPWord) vb1.get(i); 
      
      String lctext = wd.text.toLowerCase(); 

      if (lctext.startsWith("input") || 
          lctext.equals("receives") || 
          lctext.startsWith("parameter"))
      { inInput = true; } 
      else if (inInput)
      { inputPart.add(wd); } 
    } 

    for (int i = 0; i < rem.size(); i++) 
    { NLPWord wd = (NLPWord) rem.get(i); 
       
      String lctext = wd.text.toLowerCase(); 

      if (lctext.startsWith("input") || 
          lctext.equals("receives") || 
          lctext.startsWith("parameter"))
      { inInput = true; } 
      else if (lctext.startsWith("return") || 
            lctext.startsWith("result") ||
            lctext.equals("sends") ||
            lctext.startsWith("output"))
      { inOutput = true; } 
      else if (inInput) 
      { inputPart.add(wd); } 
      else if (inOutput) 
      { outputPart.add(wd); } 
    } 

    if (inputEnt == null) 
    { inputEnt = new Entity(opname + "Inputs"); } 

    if (outputEnt == null)  
    { outputEnt = new Entity(opname + "Outputs"); } 
     
    System.out.println(">>> Input part: " + inputPart); 
    System.out.println(">>> Output part: " + outputPart); 

    java.util.Map quals1 = new java.util.HashMap(); 

    if (inputPart.size() > 0)
    { NLPPhrase newpr = new NLPPhrase("NP"); 
      newpr.elements = inputPart; 
      newpr.sentence = this; 
    
      op.addPrecondition(newpr.literalForm()); 

      java.util.Map mp = new java.util.HashMap(); 
      Vector currentQuals = new Vector(); 
      java.util.Map types1 = new java.util.HashMap(); 
      Vector anal = 
        newpr.extractNouns(mp, types1, fromBackground, currentQuals); 
      System.out.println(">>> identified features: " + anal); 
      System.out.println(">>> identified qualifiers: " + mp);
      System.out.println(">>> identified types: " + types1);
      for (int i = 0; i < anal.size(); i++) 
      { String f = (String) anal.get(i); 
        if (f != null && f.trim().length() > 0)
        { inputEnt.addAttribute(f, new Type("String", null)); 
          System.out.println(">>> Added attribute " + f + " to " + inputEnt); 
        } 
      } 
      applyTypes(inputEnt,anal,types1); 
      applyQualifiers(inputEnt,anal,mp);
    } 

    System.out.println(); 

    /* for (int j = 0; j < inputPart.size(); j++) 
    { NLPWord attx = (NLPWord) inputPart.get(j); 
      if (attx.isNoun())
      { String attname = attx.text; 
        java.util.Map types = new java.util.HashMap(); 
        NLPPhrase.extractAtt(this,attx,attname,quals1,types,inputEnt,modelElements); 
      } 
    } */ 

    if (outputPart.size() > 0)
    { NLPPhrase outpr = new NLPPhrase("NP"); 
      outpr.elements = outputPart; 
      outpr.sentence = this; 
    
      op.addPostcondition(outpr.literalForm()); 

      java.util.Map mp = new java.util.HashMap(); 
      Vector currentQuals = new Vector(); 
      java.util.Map types1 = new java.util.HashMap(); 
      Vector anal = 
        outpr.extractNouns(mp, types1, fromBackground, currentQuals); 
      System.out.println(">>> identified features: " + anal); 
      System.out.println(">>> identified qualifiers: " + mp);
      System.out.println(">>> identified types: " + types1);
      for (int i = 0; i < anal.size(); i++) 
      { String f = (String) anal.get(i); 
        if (f != null && f.trim().length() > 0)
        { outputEnt.addAttribute(f, new Type("String", null)); 
          System.out.println(">>> Added attribute " + f + " to " + outputEnt); 
        } 
      } 
      applyTypes(outputEnt,anal,types1); 
      applyQualifiers(outputEnt,anal,mp);
    } 

    /* for (int j = 0; j < outputPart.size(); j++) 
    { NLPWord attx = (NLPWord) outputPart.get(j); 
      if (attx.isNoun())
      { String attname = attx.text; 
        java.util.Map types = new java.util.HashMap(); 
        NLPPhrase.extractAtt(this,attx,attname,quals1,types,outputEnt,modelElements); 
      }   
    } */ 

    op.defineParameters(inputEnt,outputEnt);
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

    if (vb1.size() >= 2)
    { NLPWord v1 = (NLPWord) vb1.get(0); 
      NLPWord v2 = (NLPWord) vb1.get(1); 
      if (v1.isModalVerb() && "be".equalsIgnoreCase(v2.text) &&
          rem.size() > 2)
      { NLPWord v3 = (NLPWord) rem.get(0); 
        NLPWord v4 = (NLPWord) rem.get(1); 
        if ("able".equalsIgnoreCase(v3.text) && 
            "to".equalsIgnoreCase(v4.text))
        { return true; } 
      } 
      else if (v1.isModalVerb() && "provide".equalsIgnoreCase(v2.text))
      { return true; } 
      else if (v1.isModalVerb() && "support".equalsIgnoreCase(v2.text))
      { return true; } 
    }

    return false; 
  }	  

  public boolean constraintDefinition(Vector np, Vector vb1, Vector rem)
  { System.out.println(">>> Checking constraint definition " + vb1 + "; " + rem); 

    if (vb1.size() >= 2 && rem.size() >= 1)
    { NLPWord v1 = (NLPWord) vb1.get(0); 
      NLPWord v2 = (NLPWord) vb1.get(1); 
      if (v1.isModalVerb() && 
          ("have".equalsIgnoreCase(v2.text) || 
           "comprise".equalsIgnoreCase(v2.text)))
      { if (nounClause(rem))
        { return true; } 
      }
    } 

    return false; 
  }	  
	 
  boolean nounClause(Vector rem)
  { // a noun before any verb-like 
    for (int i = 0; i < rem.size(); i++) 
    { NLPWord wd = (NLPWord) rem.get(i); 
      if (wd.isNoun())
      { return true; } 
      if (wd.isVerb())
      { return false; } 
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
      if (wd.isVerbPhraseWord(quals,mp) || 
          wd.isAdjective() || wd.isNounPhraseWord() || 
          wd.isConjunctionWord())
      { uc = uc + wd.text; }  
      if (ent == null) 
      { ent = (Entity) 
           ModelElement.lookupByNameIgnoreCase(
                                          wd.text,elems); 
      } 
    }
	
    if (uc.length() > 0)
    { String ucCorrect = Named.removeInvalidCharacters(uc); 
      UseCase ucase = 
        (UseCase) ModelElement.lookupByNameIgnoreCase(
                                            ucCorrect,elems); 
      if (ucase != null) 
      { System.out.println(">>> Duplicate use case: " + uc); } 
      else 
      { ucase = new UseCase(ucCorrect);
        System.out.println(">>> New use case: " + ucCorrect); 
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
        
        if (uc.indexOf("edit") >= 0 || 
            uc.indexOf("delete") >= 0)
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
        else if (wd.isVerbPhraseWord(quals,mp) || 
                 wd.isAdjective() || 
		      wd.isNounPhraseWord() || 
                 wd.isConjunctionWord())
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
    { String ucCorrect = 
         Named.removeInvalidCharacters(shortName); 
      Object obj = ModelElement.lookupByNameIgnoreCase(
                                    ucCorrect,elems); 
      if (obj != null) 
      { System.out.println(">>> Duplicate element: " + shortName); } 
      else 
      { String sname = Named.decapitalise(ucCorrect); 
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
        { String cleanNoun = 
            Named.removeInvalidCharacters(actor);  

          String actr = Named.capitalise(cleanNoun); 
          ucase.addStereotype("actor=\"" + actr + "\""); 
          Object actobj = 
            ModelElement.lookupByNameIgnoreCase(actr,elems);
          if (actobj != null && (actobj instanceof Entity))
          { String aname = actr.toLowerCase() + "x"; 
            ucase.addParameter(aname, new Type((Entity) actobj)); 
          }
          else if (actobj == null)
          { Entity newent = new Entity(actr); 
            System.out.println(">>> New entity for use case actor: " + actr); 
            elems.add(newent);
            derivedElements.add(newent); 
            newent.addStereotype("originator=\"" + id + "\""); 
            String aname = actr.toLowerCase() + "x"; 
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
            if (foundStereotypes.size() <= 1) 
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
          if (foundStereotypes.size() <= 1) 
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
          { String cleanName = 
              Named.removeInvalidCharacters(wd.text); 
            Entity mainent = 
              (Entity) ModelElement.lookupByName(cleanName,
                                                 elems); 
            if (mainent == null)
            { mainent = 
                new Entity(Named.capitalise(cleanName));
              System.out.println(">>> Added new class " + wd.text);  
              elems.add(mainent);
            } 

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
              if (stereos.contains("edit") || 
                  stereos.contains("create"))
              { uc.addParameter(attname + "x", ((Attribute) elm).getType()); }
            } 

            Entity ent = 
              ModelElement.featureBelongsTo(wd.text, localElems); 
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