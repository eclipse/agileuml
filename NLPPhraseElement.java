import java.util.Vector; 


public abstract class NLPPhraseElement
{ String tag = ""; 
  
  public NLPPhraseElement(String tg)
  { tag = tg; }

  public abstract String formQualifier(); 

  public abstract int indexing(int st); 

  public abstract Vector sequentialise(); 
} 