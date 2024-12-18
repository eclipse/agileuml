import java.util.Vector; 

/******************************
* Copyright (c) 2003-2023 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Requirements Formalisation */ 

/* Superclass of NLPWord and NLPPhrase */ 

public abstract class NLPPhraseElement
{ String tag = ""; 
  NLPSentence sentence = null; 
  
  public void setSentence(NLPSentence sx)
  { sentence = sx; }
  
  public abstract void linkToPhrases(NLPSentence s); 

  public abstract String literalForm(); 

  public abstract boolean isVerbPhrase(); 
  
  public NLPPhraseElement(String tg)
  { tag = tg; }

  public abstract boolean isInputPhrase(); 

  public abstract boolean isOutputPhrase(); 

  public abstract String formQualifier(); 

  public abstract int indexing(int st); 

  public abstract Vector sequentialise(); 
  
  public abstract java.util.HashMap classifyWords(Vector background, Vector modelElements); 
  
  public abstract java.util.HashMap classifyVerbs(Vector verbs); 

  public abstract Vector extractVerbedNouns(java.util.Map quals, java.util.Map types, java.util.Map fromBackground, Vector currentQuals); 

  public abstract Vector extractNouns(java.util.Map quals, java.util.Map types, java.util.Map fromBackground, Vector currentQuals); 
  
  public abstract String getPrincipalNoun();

  public abstract NLPWord identifyNounForEntity(); 

  public abstract String getMostSignificantVerb(); 
  
  public abstract void extractAssociationDefinitions(Entity ex,String nme,java.util.Map mp,Vector modelems); 

  public abstract void extractRelationshipDefinitions(Entity ent, Vector modelElements);

} 