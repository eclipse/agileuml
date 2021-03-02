import java.util.Vector; 

/******************************
* Copyright (c) 2003,2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Requirements Formalisation */ 


public abstract class NLPPhraseElement
{ String tag = ""; 
  NLPSentence sentence = null; 
  
  public void setSentence(NLPSentence sx)
  { sentence = sx; }
  
  public abstract void linkToPhrases(NLPSentence s); 

  public abstract boolean isVerbPhrase(); 
  
  public NLPPhraseElement(String tg)
  { tag = tg; }

  public abstract String formQualifier(); 

  public abstract int indexing(int st); 

  public abstract Vector sequentialise(); 
  
  public abstract java.util.HashMap classifyWords(Vector background, Vector modelElements); 
  
  public abstract String getPrincipalNoun();

  public abstract String getMostSignificantVerb(); 
  
  public abstract void extractAssociationDefinitions(Entity ex,String nme,java.util.Map mp,Vector modelems); 
} 