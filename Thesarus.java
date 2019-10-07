import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 


class ThesaurusConcept 
{ String name; 
  Vector preferredTerms = new Vector(); 
  Vector terms = new Vector(); 
  Vector linkedConcepts = new Vector(); 

  ThesaurusConcept(String n) 
  { name = n; } 

  Vector getTerms()
  { return terms; } 

  Vector getPreferredTerms()
  { return preferredTerms; } 

  void addTerm(ThesaurusTerm t) 
  { if (terms.contains(t)) {} 
    else 
    { terms.add(t); } 
  } 

  void addPreferredTerm(ThesaurusTerm t) 
  { if (preferredTerms.contains(t)) {} 
    else 
    { preferredTerms.add(t); } 
  } 

  public boolean hasTerm(String t)
  { boolean res = false; 
    for (int i = 0; i < terms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) terms.get(i); 
      if (t.equals(tt.name))
      { return true; } 
    } 
    return res; 
  } 

  public boolean hasPreferredTerm(String t)
  { boolean res = false; 
    for (int i = 0; i < preferredTerms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) preferredTerms.get(i); 
      if (t.equals(tt.name))
      { return true; } 
    } 
    return res; 
  } 

  public boolean hasAnyTerm(String t)
  { if (hasPreferredTerm(t)) 
    { return true; } 
    if (hasTerm(t))
    { return true; } 
    return false; 
  } 

  public String toString()
  { String res = name + " = [ "; 
    for (int i = 0; i < preferredTerms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) preferredTerms.get(i); 
      res = res + tt; 
      if (i < preferredTerms.size()-1)
      { res = res + ", "; }  
    } 
    res = res + " ] { "; 
    for (int i = 0; i < terms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) terms.get(i); 
      res = res + tt; 
      if (i < terms.size()-1)
      { res = res + ", "; }  
    } 
    return res + " }"; 
  }     

  public void findLinkedConcepts(Vector thesaurus)
  { for (int i = 0; i < thesaurus.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) thesaurus.get(i); 
      if (tc != this) 
      { for (int j = 0; j < terms.size(); j++) 
        { ThesaurusTerm tt = (ThesaurusTerm) terms.get(j); 
          if (tc.hasAnyTerm(tt.name))
          { if (linkedConcepts.contains(tc)) { } 
            else 
            { linkedConcepts.add(tc); }
          }  
        } 
      } 
    } 
  } 


} 

class ThesaurusTerm
{ String name; 
  Vector concepts = new Vector(); 

  ThesaurusTerm(String n) 
  { name = n; } 

  Vector getConcepts()
  { return concepts; } 

  void addConcept(ThesaurusConcept c) 
  { if (concepts.contains(c)) { } 
    else 
    { concepts.add(c); } 
  } 

  public boolean equals(Object x) 
  { if (x instanceof ThesaurusTerm)
    { ThesaurusTerm tt = (ThesaurusTerm) x; 
      return tt.name.equals(name); 
    } 
    return false; 
  } 

  public String toString()
  { return name; } 
} 


public class Thesarus
{ Vector concepts = new Vector(); 
  Vector terms = new Vector(); 

  void addConceptTerm(ThesarusConcept c, ThesarusTerm t)
  { if (concepts.contains(c)) {}
    else 
    { concepts.add(c); } 
    if (terms.contains(t)) {} 
    else 
    { terms.add(t); }  
    c.addTerm(t); 
    t.addConcept(c); 
  } 

  public static double findSimilarity(String fnme, String fenme, Vector thesaurus) 
  { String nme = fnme.toLowerCase(); 
    String enme = fenme.toLowerCase(); 

    if (nme.equals(enme)) { return 1.0; } 
    if (nme.startsWith(enme)) { return 2.0/3; } 
    if (nme.endsWith(enme)) { return 2.0/3; } 
    if (enme.startsWith(nme)) { return 2.0/3; } 
    if (enme.endsWith(nme)) { return 2.0/3; }

    String suff = ModelElement.longestCommonSuffix(nme,enme); 
    if (suff.length() > 0)
    { return suff.length()/(1.0*Math.max(fnme.length(),fenme.length())); } 

    for (int i = 0; i < thesaurus.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) thesaurus.get(i); 
      if (tc.hasPreferredTerm(nme)) 
      { if (tc.hasPreferredTerm(enme)) 
        { return 1.0; } 
      } 
    } 

    for (int i = 0; i < thesaurus.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) thesaurus.get(i); 
      if (tc.hasTerm(nme)) 
      { if (tc.hasTerm(enme)) 
        { return 2.0/3; } 
      } 
    } 

    for (int i = 0; i < thesaurus.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) thesaurus.get(i); 
      if (tc.hasAnyTerm(nme)) 
      { for (int j = 0; j < tc.linkedConcepts.size(); j++) 
        { ThesaurusConcept tcj = (ThesaurusConcept) tc.linkedConcepts.get(j); 
          if (tcj.hasAnyTerm(enme)) 
          { return 1.0/3; } 
        } 
      }
    } 
    return 0; 
  } 

} 



