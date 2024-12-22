import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 

/******************************
* Copyright (c) 2003-2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 


public class ThesaurusConcept 
{ String name; 
  Vector preferredTerms = new Vector(); 
  Vector terms = new Vector(); 
  Vector linkedConcepts = new Vector(); 
  String generalisation = "";  // superclass
  String partOfSpeech = "";
  String verbType = "other";
  Vector semantics = new Vector(); 
    // eg., attribute with type, class, association, etc, stereotypes.  

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

  public void setPOS(String pos) 
  { partOfSpeech = pos; } 

  public void addSemantics(ModelElement me) 
  { semantics.add(me); } 

  public Vector getSemantics()
  { return semantics; } 
 
  public boolean isAttribute()
  { if (semantics == null) 
    { return false; } 
    for (int i = 0; i < semantics.size(); i++) 
    { ModelElement me = (ModelElement) semantics.get(i); 
      if (me instanceof Attribute) 
      { return true; } 
    } 
    return false; 
  } 

  public boolean isEntity()
  { if (semantics == null) 
    { return false; } 
    for (int i = 0; i < semantics.size(); i++) 
    { ModelElement me = (ModelElement) semantics.get(i); 
      if (me instanceof Entity) 
      { return true; } 
    } 
    return false; 
  } 

  public Entity getEntity()
  { if (semantics == null) 
    { return null; } 
    for (int i = 0; i < semantics.size(); i++) 
    { ModelElement me = (ModelElement) semantics.get(i); 
      if (me instanceof Entity) 
      { return (Entity) me; } 
    } 
    return null; 
  } 

  public void setGeneralisation(String g)
  { generalisation = g; }
  
  public String getGeneralisation()
  { return generalisation; }

  public boolean hasTerm(String t)
  { boolean res = false; 
    for (int i = 0; i < terms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) terms.get(i); 
      if (t.equalsIgnoreCase(tt.name))
      { return true; } 
    } 
    return res; 
  } 

  public boolean hasPreferredTerm(String t)
  { boolean res = false; 
    for (int i = 0; i < preferredTerms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) preferredTerms.get(i); 
      if (t.equalsIgnoreCase(tt.name))
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

  public ThesaurusConcept lookupTerm(String wd) 
  { for (int i = 0; i < preferredTerms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) preferredTerms.get(i); 
      if (wd.equalsIgnoreCase(tt.name))
      { return this; } 
    } 
    for (int i = 0; i < terms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) terms.get(i); 
      if (wd.equalsIgnoreCase(tt.name))
      { return this; } 
    } 
    return null; 
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

  public void toList(PrintWriter out)
  { for (int i = 0; i < preferredTerms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) preferredTerms.get(i); 
      out.println(tt.name); 
    } 
    for (int i = 0; i < terms.size(); i++) 
    { ThesaurusTerm tt = (ThesaurusTerm) terms.get(i); 
      out.println(tt.name); 
    } 
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


