import java.util.Vector; 

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

  public int indexing(int st) 
  { index = st; 
    return index + 1; 
  } 

  public Vector sequentialise() 
  { Vector res = new Vector(); 
    res.add(this); 
    return res; 
  } 

  public boolean isKeyword() 
  { return text.equalsIgnoreCase("integer") || text.equalsIgnoreCase("numeric") || 
           text.equalsIgnoreCase("set") || text.equalsIgnoreCase("sequence") || 
           text.equalsIgnoreCase("real"); 
  } // or collection

  public static boolean isKeyword(String txt) 
  { return txt.equalsIgnoreCase("integer") || txt.equalsIgnoreCase("numeric") || 
           txt.equalsIgnoreCase("set") || txt.equalsIgnoreCase("sequence") || 
           txt.equalsIgnoreCase("real"); 
  } 

  public boolean isQualifier() 
  { return tag.equals("CD") || 
           text.equalsIgnoreCase("integer") || text.equalsIgnoreCase("numeric") || 
           text.equalsIgnoreCase("set") || text.equalsIgnoreCase("sequence") || 
           text.equalsIgnoreCase("real") || text.equalsIgnoreCase("unique") ||
           text.equalsIgnoreCase("many") || 
           text.equalsIgnoreCase("several") || 
           text.equalsIgnoreCase("more") || text.equalsIgnoreCase("some") || 
           text.equalsIgnoreCase("multiple"); 
  } // or collection

  public boolean isNoun()
  { if (tag.equals("NNPS") || tag.equals("NNS") ||
        tag.equals("NN") || tag.equals("NNP"))
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
  
  public boolean isVerbPhraseWord(Vector quals)
  { if (text.startsWith("store") || text.startsWith("record")) 
    { quals.add("persistent"); 
      return true; 
    }  
    if (tag.equals("VB") || tag.equals("VBZ") || tag.equals("TO") || tag.equals("VBG") || 
        tag.equals("MD") || tag.equals("IN") || tag.equals("VBD") ||
	   tag.equals("VBN") || tag.equals("VBP") || tag.equals("RB") || tag.equals("WRB") || 
	   tag.equals("EX"))
    { return true; }
    if ("SYM".equals(tag) && text.equals("="))
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
  { if (text.endsWith("s"))
    { return text.substring(0,text.length()-1); } 
    else if (text.endsWith("es"))
    { return text.substring(0,text.length()-2); } 
    return text; 
  } 

  public static Type identifyType(String text, Vector quals)
  { Type res = null; 
    for (int i = 0; i < quals.size(); i++) 
    { String wd = (String) quals.get(i); 
      if (wd.equals("numeric") ||  wd.equals("real") ||          
          wd.startsWith("realvalue") || wd.startsWith("real-value") ||
          wd.startsWith("realnumber") || wd.startsWith("real-number")) 
      { res = new Type("double", null); } 
      else if (wd.equals("integer") || wd.equals("whole") || 
               wd.startsWith("wholenumber") || wd.startsWith("whole-number"))
      { res = new Type("int", null); } 
      else if (wd.startsWith("bool"))
      { res = new Type("boolean", null); } 
    } 

    if (res == null) 
    { if ("age".equals(text) || "weight".equals(text) || 
          "height".equals(text) || "time".equals(text) || 
          "years".equals(text) || "longitude".equals(text) || 
          "latitude".equals(text) || "altitude".equals(text) || 
          "duration".equals(text) || "distance".equals(text) || 
          "velocity".equals(text) || "acceleration".equals(text) || "speed".equals(text) || "depth".equals(text))
      { res = new Type("double", null); } 
      else if ("year".equals(text) || "frequency".equals(text))
      { res = new Type("int", null); } 
      else 
      { res = new Type("String", null); } 
    } 

    return res; 
  } // but nouns such as age, year are always numeric. 

  public static void identifyStereotypes(Attribute att, Vector quals)
  { for (int i = 0; i < quals.size(); i++) 
    { String wd = (String) quals.get(i); 
      if (wd.equals("identifier") ||           
          wd.equals("key") || 
          wd.equals("unique")) 
      { att.setIdentity(true); } 
      else if (wd.equals("constant") ||
               wd.equals("fixed") || wd.equals("frozen"))
      { att.setFrozen(true); } 
    } 
  } 

} 