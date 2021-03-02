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

public class NLPWord extends NLPPhraseElement
{ 
  String text = ""; 
  String root = ""; // The lemma or base form of the word
  NLPWord qualifies = null;  // If this is an adjectival modifier of the qualifies word.
  int index = 0; // the word position in the original sentence.  
  

  public NLPWord(String tg, String wd)
  { super(tg); 
    text = wd; 
  }

  public String toString()
  { return "(" + tag + " " + text + ")_" + index; }  

  public void linkToPhrases(NLPSentence s)
  { sentence = s; }
  
  public int indexing(int st) 
  { index = st; 
    return index + 1; 
  } 

  public void setIndex(int i)
  { index = i; } 

  public boolean isVerbPhrase() 
  { return false; } 

  public Vector sequentialise() 
  { Vector res = new Vector(); 
    res.add(this); 
    return res; 
  } 

  public boolean isKeyword() 
  { return text.equalsIgnoreCase("integer") || text.equalsIgnoreCase("numeric") || 
           text.equalsIgnoreCase("set") || text.equalsIgnoreCase("sequence") || 
		text.equals("instance") || text.equals("instances") ||
           text.equals("boolean") ||
           text.equalsIgnoreCase("real") || text.equalsIgnoreCase("double"); 
  } // or collection

  public static boolean isKeyword(String txt) 
  { return txt.equalsIgnoreCase("integer") || txt.equalsIgnoreCase("numeric") || 
           txt.equalsIgnoreCase("set") || txt.equalsIgnoreCase("sequence") || 
           txt.equals("boolean") ||
		txt.equals("instance") || txt.equals("instances") || 
           txt.equalsIgnoreCase("real") || txt.equalsIgnoreCase("double"); 
  } 

  public boolean isQualifier() 
  { return tag.equals("CD") || 
           text.equalsIgnoreCase("integer") || text.equalsIgnoreCase("numeric") || 
           text.equalsIgnoreCase("set") || text.equalsIgnoreCase("sequence") || 
		text.equalsIgnoreCase("collection") ||  
           text.equalsIgnoreCase("real") || text.equalsIgnoreCase("unique") ||
           text.equalsIgnoreCase("many") || text.equalsIgnoreCase("series") ||
           text.equalsIgnoreCase("several") || text.equalsIgnoreCase("list") || 
           text.equalsIgnoreCase("double") || (text.equalsIgnoreCase("ordered") && !isVerb()) ||
		text.equalsIgnoreCase("more") || text.equalsIgnoreCase("some") || 
           text.equals("boolean") || 
           text.equalsIgnoreCase("multiple"); 
  } // or collection



  public boolean isNounOrAdjective()
  { return isNoun() || isAdjective(); } 

  public boolean isNoun()
  { if (tag.equals("NNPS") || tag.equals("NNS") ||
        tag.equals("NN") || tag.equals("NNP"))
    { return true; } 
    return false; 
  } 

  public boolean isProperNoun()
  { if (tag.equals("NNPS") || tag.equals("NNP"))
    { return true; } 
    return false; 
  } 


  public boolean isPlural()
  { if (tag.equals("NNPS") || tag.equals("NNS"))
    { return true; } 
    return false; 
  } 

  public boolean isAdjective()
  { if (tag.equals("JJ") || tag.equals("JJS") ||
        tag.equals("JJR"))
    { return true; } 
    return false; 
  } 

  public boolean isNounPhraseWord()
  { if (tag.equals("DT") || tag.equals("PDT") || 
        tag.equals("NNPS") || tag.equals("WDT") ||
        tag.equals("WH") || tag.equals("FW") ||
        tag.equals("JJR") || 
        tag.equals("CD") || tag.equals("NNS") || 
        tag.equals("_LS") || tag.equals("JJS") ||
        tag.equals("NN") || tag.equals("NNP") || 
        tag.equals("JJ") || tag.equals("PRP") || 
        tag.equals("PRP$") || tag.equals("WP$"))
    { return true; } 
    return false; 
  } 
  
  public boolean isSignificantVerbPhraseWord(Vector quals, java.util.Map wordQuals)
  { String lctext = text.toLowerCase(); 

    if (lctext.startsWith("updat") || lctext.startsWith("assign") || lctext.equals("write") || lctext.equals("overwrite") ||
       lctext.startsWith("edit") || lctext.startsWith("link") || lctext.startsWith("manag") ||
       (lctext.equals("set") && isVerb()) || lctext.startsWith("validat") || (lctext.startsWith("insert") && isVerb()) ||
	   (lctext.equals("schedule") && isVerb()) || lctext.equals("upload") || lctext.equals("suspend") || 
       lctext.equals("merg") || lctext.equals("combin") || (lctext.startsWith("packag") && isVerb()) ||
	   (isVerb() && 
	     (lctext.equals("sets") || lctext.startsWith("rewrit") || lctext.startsWith("revis") || 
		  lctext.startsWith("approv") || lctext.startsWith("certif") ||
		  lctext.startsWith("customiz") || lctext.startsWith("customis"))) || 
       lctext.equals("setting") || lctext.startsWith("reset") || lctext.startsWith("append") ||
	   lctext.startsWith("annotat") || lctext.startsWith("resubmit") || lctext.equals("hide") ||
       lctext.equals("conceal") || lctext.startsWith("chang") || lctext.startsWith("modif"))
    { quals.add("edit"); 
      wordQuals.put(text, "edit");  
      return true; 
    } 

    if (lctext.startsWith("creat") || (lctext.startsWith("add") && isVerb()) || 
	    lctext.startsWith("initialis") ||
	    (lctext.equals("enroll") && isVerb()) || (lctext.equals("register") && isVerb()) ||
		(lctext.equals("hire") && isVerb()) ||
		(lctext.startsWith("onboard") && isVerb()) || lctext.startsWith("submit") || lctext.startsWith("send") || 
		(lctext.equals("issue") && isVerb()))
    { quals.add("create"); 
      wordQuals.put(text, "create");  
      return true; 
    } 

    if (lctext.startsWith("read") || lctext.startsWith("display") || lctext.startsWith("list") ||
	    lctext.startsWith("show") || lctext.startsWith("brows") || lctext.startsWith("check") ||
		lctext.startsWith("search") || (lctext.equals("track") && isVerb()) || (lctext.startsWith("navigat") && isVerb()) || 
	    lctext.startsWith("receiv") || lctext.equals("review") || (lctext.equals("monitor") && isVerb()) ||
        (lctext.startsWith("inspect") && isVerb()) || (lctext.equals("survey") && isVerb()) ||
		lctext.startsWith("provid") || (lctext.startsWith("scan") && isVerb()) ||
	    (lctext.startsWith("get") && isVerb()) || lctext.startsWith("retriev") || lctext.startsWith("select") || 
	    lctext.startsWith("view") || lctext.startsWith("access") || lctext.startsWith("choose") || 
		(lctext.startsWith("publish") && isVerb()) || lctext.equals("see") || lctext.startsWith("download") ||
		lctext.startsWith("visualiz") || lctext.startsWith("visualis") || (lctext.startsWith("detect") && isVerb()) ||
		(lctext.startsWith("know") && isVerb()) || (lctext.startsWith("explor") && isVerb()) ||
		(lctext.equals("chart") && isVerb()) || (lctext.equals("identif") && isVerb()) ||
		lctext.startsWith("request") || (lctext.startsWith("examin") && isVerb()) || 
		(lctext.startsWith("assess") && isVerb()) || (lctext.startsWith("measur") && isVerb()) ||
		lctext.startsWith("find") )
    { quals.add("read"); 
      wordQuals.put(text, "read");  

      return true; 
    } 


    if (lctext.startsWith("delet") || (lctext.startsWith("remov") && isVerb()) || 
	    (lctext.startsWith("eras") && isVerb()) || 
	    lctext.equals("terminate") || (lctext.equals("fire") && isVerb()) ||
	    lctext.startsWith("destroy") )
    { quals.add("delete");
      wordQuals.put(text, "delete");  
 
      return true; 
    } 

    if (lctext.startsWith("stor") || lctext.startsWith("record") || lctext.startsWith("save") ||
        lctext.startsWith("persist") || lctext.startsWith("commit")) 
    { quals.add("persistent");
	  quals.add("edit"); 
      wordQuals.put(text, "persistent");  
      return true; 
    }  // also counts as an edit

    if (lctext.startsWith("communicat") || lctext.startsWith("surf") || lctext.startsWith("model") || lctext.startsWith("login") ||
	    lctext.startsWith("prioritis") || lctext.startsWith("import") || lctext.startsWith("export") || lctext.startsWith("logout") ||
		lctext.startsWith("provid") || lctext.startsWith("filter") || lctext.startsWith("sort") || lctext.startsWith("signup") ||
        lctext.startsWith("aggregat") || lctext.startsWith("share") || lctext.startsWith("zoom") || lctext.startsWith("signin") ||
	    lctext.equals("warn") || lctext.equals("allocate") ||
		lctext.startsWith("understand") || lctext.startsWith("have") || lctext.startsWith("signout") || lctext.equals("revise") ||
		(lctext.equals("sign") && isVerb()) || (lctext.equals("mark") && isVerb()) || (lctext.equals("indicate") && isVerb()) ||
	    (lctext.equals("give") && isVerb()) || (lctext.startsWith("notif") && isVerb()) ||
		(lctext.startsWith("use") && isVerb()) || lctext.startsWith("enter") || lctext.startsWith("attend") ||
		lctext.equals("retake") || lctext.equals("resit") || lctext.equals("revisit") || lctext.equals("renew") || 
		lctext.equals("satisfy") || lctext.equals("prepare") || lctext.equals("support") || lctext.equals("capture") ||
		lctext.equals("perform") || lctext.equals("diagnose") || lctext.equals("debug") || lctext.equals("transfer") || 
		lctext.startsWith("generat") || (lctext.startsWith("produc") && isVerb()) || lctext.equals("promote") || 
		(lctext.startsWith("compute") && isVerb()) || lctext.startsWith("calculat") || lctext.equals("demote") || 
		lctext.startsWith("normaliz") || lctext.startsWith("normalis") || (lctext.equals("log") && isVerb()) ||
		lctext.startsWith("choose") || lctext.startsWith("help") ||
		lctext.startsWith("enabl") || lctext.startsWith("facilitat") || (lctext.startsWith("prov") && isVerb()) || 
		(lctext.startsWith("act") && isVerb()) || lctext.equals("reply") ||
		lctext.startsWith("highlight") || lctext.equals("translat") ||  lctext.startsWith("redirect") || lctext.equals("develop") ||
		(lctext.equals("denote") && isVerb()) || (lctext.equals("conduct") && isVerb()) || lctext.equals("evaluate") ||
		(lctext.startsWith("packag") && isVerb()) || lctext.startsWith("click") || lctext.startsWith("contact") ||
		lctext.startsWith("embed") || (lctext.startsWith("specif") && isVerb()) || lctext.equals("process") || 
		(lctext.startsWith("delegat") && isVerb()) || lctext.equals("apply") || lctext.equals("complete") ||
		lctext.equals("finalise") || lctext.equals("finalize") || lctext.equals("enact") || lctext.equals("authorise") ||
		lctext.equals("authorize") || lctext.equals("notify") || (lctext.equals("report") && isVerb()) ||
		(lctext.startsWith("integrat") && isVerb()) || (lctext.startsWith("map") && isVerb()) || 
		lctext.equals("pay") || (lctext.startsWith("connect") && isVerb()) || lctext.equals("keep") || 
	    lctext.equals("put") || (lctext.equals("reference") && isVerb()) || (lctext.equals("handle") && isVerb()) || 
		(lctext.equals("block") && isVerb()) || (lctext.startsWith("suggest") && isVerb()) ||
		(lctext.startsWith("calibrat") && isVerb()) )
    { quals.add("other"); 
      wordQuals.put(text, "other");  

      return true; 
    } 

    return false; 
  } 

  public boolean isVerbPhraseWord(Vector quals, java.util.Map wordQuals)
  { boolean res = isSignificantVerbPhraseWord(quals,wordQuals); 

    if (res) { return true; } 

    if (tag.equals("VB") || tag.equals("VBZ") || tag.equals("TO") || tag.equals("VBG") || 
        tag.equals("MD") || tag.equals("IN") || tag.equals("VBD") ||
	   tag.equals("VBN") || tag.equals("VBP") || tag.equals("RB") || tag.equals("WRB") || 
	   tag.equals("EX"))
    { return true; }

    if ("SYM".equals(tag) && text.equals("="))
    { return true; } 

    return false; 
  }
  
  public boolean isVerb()
  { if (tag.equals("VB") || tag.equals("VBZ") || tag.equals("TO") || tag.equals("VBG") || 
        tag.equals("VBD") ||
	    tag.equals("VBN") || tag.equals("VBP"))
    { return true; }
	return false; 
  } 

  public boolean isConjunctionWord()
  { if (tag.equals("IN") || tag.equals("CC"))
    { return true; }
	return false; 
  }

  public boolean isConjunction()
  { if (isSeparator() ||
        text.equalsIgnoreCase("and") || text.equalsIgnoreCase("of"))
    { return true; }
    return false; 
  }

  public boolean isSeparator()
  { if (text.equals("+") || text.equals("&") || text.equals(",") || text.equals(";") ||
        text.equalsIgnoreCase("|") || text.equalsIgnoreCase("/"))
    { return true; }
    return false; 
  }


  public String formQualifier()
  { return text; } 
  // but not for punctuation, etc. 

  public String getSingular()
  { if (text.endsWith("ies"))
    { return text.substring(0,text.length()-3) + "y"; } 
    else if (text.endsWith("s"))
    { return text.substring(0,text.length()-1); } 
    else if (text.endsWith("es"))
    { return text.substring(0,text.length()-2); } 
    return text; 
  } 

  public static String getSingular(String txt)
  { if (txt.endsWith("ies"))
    { return txt.substring(0,txt.length()-3) + "y"; } 
    else if (txt.endsWith("s"))
    { return txt.substring(0,txt.length()-1); } 
    else if (txt.endsWith("es"))
    { return txt.substring(0,txt.length()-2); } 
    return txt; 
  } 

  public Type identifyType(String text, java.util.Map qm, java.util.Map types, Vector modelems)
  { Type res = null; 

    res = (Type) types.get(text);
    if (res != null) 
    { return res; } 

    Vector quals = (Vector) qm.get(text); 
    if (quals == null) 
    { quals = new Vector(); } 

    for (int i = 0; i < quals.size(); i++) 
    { String wd = (String) quals.get(i); 
      if (wd.equals("numeric") || wd.equals("real") || wd.startsWith("double") || wd.startsWith("float") ||           
          wd.startsWith("realvalue") || wd.startsWith("real-value") ||
          wd.endsWith("number")) 
      { res = new Type("double", null); } 
      else if (wd.startsWith("int"))
      { res = new Type("int", null); } 
      else if (wd.startsWith("bool"))
      { res = new Type("boolean", null); } 
    } 


    // if res is lower case & 
    // res or res singular form is a class, 
    // res is a reference.
 
    if (res == null) 
    { Object ex = ModelElement.lookupByNameIgnoreCase(text,modelems); 
      if (ex != null && ex instanceof Entity)
      { // it is a singular reference of type ex
        res = new Type((Entity) ex); 
      } 
    }  

    if (res == null && isPlural()) 
    { String sing = getSingular(); 

      Object ex = ModelElement.lookupByNameIgnoreCase(sing,modelems); 
      if (ex != null && ex instanceof Entity)
      { // it is a singular reference of type ex
        Type reselem = new Type((Entity) ex);
       
        if (quals.contains("Sequence") || quals.contains("series") || quals.contains("list") || quals.contains("ordered")) 
        { res = new Type("Sequence", null); }
        else  
        { res = new Type("Set", null); } 
        res.setElementType(reselem); 
        return res;  
      } 
    }  

    if (res == null) 
    { if ("age".equals(text) || "weight".equals(text) || 
          "height".equals(text) || "time".equals(text) || 
          "years".equals(text) || "longitude".equals(text) || 
          "latitude".equals(text) || "altitude".equals(text) || 
          "duration".equals(text) || "distance".equals(text) || 
          "radius".equals(text) || "magnitude".equals(text) ||
		  "year".equals(text) || "frequency".equals(text) || 
          "velocity".equals(text) || text.startsWith("score") || 
          "acceleration".equals(text) || "speed".equals(text) || "depth".equals(text))
      { res = new Type("double", null); } 
      else if ("count".equals(text))
      { res = new Type("int", null); } 
      else 
      { res = new Type("String", null); } 
    } 

    Type elementType = res; 

    if (quals.contains("many") || quals.contains("Set") || quals.contains("collection")) 
    { res = new Type("Set", null); 
      res.setElementType(elementType); 
    } 
    else if (quals.contains("Sequence") || quals.contains("series") || quals.contains("list") || quals.contains("ordered")) 
    { res = new Type("Sequence", null); 
      res.setElementType(elementType); 
    } 

    return res; 
  } // but nouns such as age, year are always numeric. 

  public static void identifyStereotypes(Attribute att, java.util.Map qm)
  { String attname = att.getName(); 
    Vector quals = (Vector) qm.get(attname); 

    if (quals == null)
    { return; } 

    for (int i = 0; i < quals.size(); i++) 
    { String wd = (String) quals.get(i); 
      if (wd.equals("identifier") ||           
          wd.equals("key") || wd.equals("identity") ||
          wd.equals("unique")) 
      { att.setIdentity(true); } 
      else if (wd.startsWith("const") || wd.equals("readOnly") ||
               wd.equals("fixed") || wd.equals("frozen"))
      { att.setFrozen(true); } 
    } 
  } 

  public java.util.HashMap classifyWords(Vector background, Vector modelElems)
  { java.util.HashMap res = new java.util.HashMap(); 
  
    if (isKeyword()) 
	{ return res; } 
	
    ThesaurusConcept tc = Thesarus.lookupWord(background,text); 
    if (tc != null && tc.semantics.size() > 0)
    { res.put(text, tc.semantics); }
	else if (isPlural())
	{ String sing = getSingular(); 
	  // System.out.println(">>> Singular of " + text + " is " + sing); 
	  
	  tc = Thesarus.lookupWord(background,sing); 
	  if (tc != null)
	  { Vector tcsem = tc.semantics; 
	    if (tcsem.size() > 0 && (tcsem.get(0) instanceof Entity))
		{ Type colltype = new Type("Set", null);
		  String ename = Named.capitalise(sing); 
		  Entity ee = null; 
		  
		  Object obj = ModelElement.lookupByNameIgnoreCase(ename,modelElems); 
		  if (obj != null && obj instanceof Entity)
		  { ee = (Entity) obj; }
		  else
		  { ee = new Entity(ename);
		    modelElems.add(ee); 
			System.out.println(">>> New entity: " + ename); 
			String id = sentence.id; 
			sentence.derivedElements.add(ee); 
			ee.addStereotype("originator=\"" + id + "\""); 
		  }   
		  colltype.setElementType(new Type(ee)); 
		  Attribute r = new Attribute(text, colltype, ModelElement.INTERNAL); 
		  Vector sem1 = new Vector(); 
		  sem1.add(r); 
		  res.put(text, sem1); 
		}
	  }
	}
    else if (isProperNoun())
    { String ename = getSingular(); 
      Vector sem = new Vector(); 
      Entity ee = null; 
		  
      Object obj = ModelElement.lookupByNameIgnoreCase(ename,modelElems); 
      if (obj != null && obj instanceof Entity)
      { ee = (Entity) obj; }
      else
      { ee = new Entity(ename);
        modelElems.add(ee); 
    	System.out.println(">>> New entity: " + ename); 
		String id = sentence.id; 
		sentence.derivedElements.add(ee); 
		ee.addStereotype("originator=\"" + id + "\""); 
      }   
      sem.add(ee); 
      res.put(text, sem); 
    }  
	// These need to be added to the model elements if they are not already there. 
	
    return res; 
  }

  public String getPrincipalNoun()
  { String noun = ""; 
    if (isNoun())
	{ noun = text; } 
    return noun; 
  }   

  public String getMostSignificantVerb()
  { String verb = ""; 
    String lex = tag; 
    if (lex.equals("VB") || lex.equals("VBZ") || 
            lex.equals("VBG") || lex.equals("VBD") ||
            lex.equals("VBN") || lex.equals("VBP"))
    { verb = text; } 
	   
    return verb; 
  }  // Actually the textually last verb. 

  public void extractAssociationDefinitions(Entity ent, String role, java.util.Map fromBackground, Vector modelElements)
  { extractClassReferences(ent,role, fromBackground, modelElements); } 

  public void extractClassReferences(Entity ent, String role, java.util.Map fromBackground, Vector modelElements)
  { 
    String attname = text; // singular form of it.

    if (NLPWord.isKeyword(attname)) 
	{ return; } 
	
    if (isPlural())
	{ attname = getSingular(); }
	
    Entity tent = (Entity) ModelElement.lookupByNameIgnoreCase(attname,modelElements); 
    if (tent != null) 
    { System.out.println(">>> Existing class: " + attname); }
    else 
    { tent = new Entity(Named.capitalise(attname)); 
      System.out.println(">>> Creating new class: " + attname);
      modelElements.add(tent);  
  	  String id = sentence.id; 
	  sentence.derivedElements.add(tent); 
  	  tent.addStereotype("originator=\"" + id + "\""); 
    }

    String role2 = attname.toLowerCase();
    if (role != null) 
    { role2 = role; }
			
    int card1 = ModelElement.MANY; 
    int card2 = ModelElement.ONE;  
	
    if (isPlural()) // or if the object is plural
    { card2 = ModelElement.MANY; }
	
    if (ent.hasRole(role2))
    { System.err.println("Possible conflict in requirements: role " + role2 + " of class " + attname + " already exists"); 
      role2 = role2 + "_" + ent.getAssociations().size(); 
    }
    Association newast = new Association(ent,tent,card1,card2,"",role2);   
    System.out.println(">>> new association " + newast + " for class " + ent.getName()); 
    ent.addAssociation(newast); 
  } 

} 