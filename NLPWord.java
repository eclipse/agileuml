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
  { return "(" + tag + " " + text + ")"; }  

  public boolean isKeyword() 
  { return text.equals("integer") || text.equals("numeric") || 
           text.equals("real"); 
  } 

  public boolean isNoun()
  { if (tag.equals("NNPS") || tag.equals("NNS") ||
        tag.equals("NN") || tag.equals("NNP"))
    { return true; } 
    return false; 
  } 

  public boolean isAdjective()
  { if (tag.equals("JJ") || tag.equals("JJS") ||
        tag.equals("JJR"))
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

  public static Type identifyType(Vector quals)
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
    { res = new Type("String", null); } 

    return res; 
  } 

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