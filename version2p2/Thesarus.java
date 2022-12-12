import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 

/******************************
* Copyright (c) 2003-2022 Kevin Lano
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
      return tt.name.equalsIgnoreCase(name); 
    } 
    return false; 
  } 

  public String toString()
  { return name; } 
} 


public class Thesarus
{ Vector concepts = new Vector(); 
  Vector terms = new Vector(); 

  public static Vector loadThesaurus(String f)
  { BufferedReader br = null;
    BufferedWriter brout = null; 
    PrintWriter pwout = null; 
    BufferedWriter ebrout = null; 
    PrintWriter epwout = null; 

    Vector concepts = new Vector(); 

    String s;
    boolean eof = false;
    File infile = new File(f);  

    try
    { br = new BufferedReader(new FileReader(infile));
      brout = new BufferedWriter(new FileWriter("output/attributes.txt")); 
      pwout = new PrintWriter(brout);
      ebrout = new BufferedWriter(new FileWriter("output/classes.txt")); 
      epwout = new PrintWriter(ebrout); 
    }
    catch (Exception e)
    { System.out.println("!! Error loading file: " + f);
      return concepts; 
    }

    String xmlstring = ""; 

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException e)
      { System.out.println("!! Error: Reading " + f + " failed.");
        return concepts; 
      }
      if (s == null) 
      { eof = true; 
        break; 
      }
      else 
      { xmlstring = xmlstring + s + " "; } 
    }

    Compiler2 comp = new Compiler2();  
    comp.nospacelexicalanalysisxml(xmlstring); 
    XMLNode xml = comp.parseXML(); 
    // System.out.println(xml); 

    if (xml == null) 
    { System.err.println("!!!! Wrong format for XML file. Must start with <?xml header"); 
      return concepts; 
    } 


    Vector enodes = xml.getSubnodes(); // all instances
    for (int i = 0; i < enodes.size(); i++) 
    { XMLNode enode = (XMLNode) enodes.get(i); 
      String cname = enode.getTag(); 
      if ("CONCEPT".equals(cname))
      { ThesaurusConcept c = null; 
        Vector subnodes = enode.getSubnodes(); 
        for (int j = 0; j < subnodes.size(); j++) 
        { XMLNode sb = (XMLNode) subnodes.get(j); 
          String stag = sb.getTag(); 
          if ("DESCRIPTOR".equals(stag))
          { String cdef = sb.getContent(); 
            c = new ThesaurusConcept(cdef.toLowerCase());
            System.out.println(">> New concept: " + cdef); 
          } 
          else if ("PT".equals(stag) && c != null)
          { String ndef = sb.getContent(); 
            ThesaurusTerm tt = new ThesaurusTerm(ndef.toLowerCase()); 
            c.addPreferredTerm(tt); 
            tt.addConcept(c); 
            if (c.isAttribute())
            { pwout.println(tt.name); }   
            else if (c.isEntity())
            { epwout.println(tt.name); }   

          } 
          else if ("NT".equals(stag) && c != null)
          { String ndef = sb.getContent(); 
            ThesaurusTerm tt = new ThesaurusTerm(ndef.toLowerCase()); 
            c.addTerm(tt); 
            tt.addConcept(c);
            if (c.isAttribute())
            { pwout.println(tt.name); }   
            else if (c.isEntity())
            { epwout.println(tt.name); }   
          } 
          else if ("NTG".equals(stag) && c != null)
          { String ndef = sb.getContent(); 
            c.setGeneralisation(ndef);  
          } 
          else if ("POS".equals(stag) && c != null)
          { String ndef = sb.getContent(); 
            System.out.println(">> part of speech = " + ndef); 
            c.setPOS(ndef); 
          } 
          else if ("SEM".equals(stag) && c != null)
          { String ndef = sb.getContent(); 
            System.out.println(">> semantics = " + ndef);
            if (ndef.equals("attribute"))
            { String xsitype = sb.getAttributeValue("type"); 
              pwout.println(c.name);
              if (xsitype == null) 
              { xsitype = "String"; } 
              Attribute x = new Attribute(c.name,new Type(xsitype,null), ModelElement.INTERNAL); 
              c.addSemantics(x); 
            } 
            else if (ndef.equals("class"))
            { Entity ent = new Entity(c.name); 
              epwout.println(c.name);
              c.addSemantics(ent); 
            } 
            else if (ndef.equals("reference"))
            { } 
            else if (ndef.equals("system"))
            { UMLPackage p = new UMLPackage(c.name); 
              c.addSemantics(p); 
            }
            else if (ndef.equals("operation"))
            { // String xsitype = sb.getAttributeValue("type"); 
              // System.out.println(">> type = " + xsitype);
              // if (xsitype == null) 
              // { xsitype = "String"; } 
              BehaviouralFeature bf = new BehaviouralFeature(c.name); 
              c.addSemantics(bf); 
            }  
            else if (c.partOfSpeech.equals("VB"))
            { c.verbType = ndef; } 
          } 
        } 

        if (c != null) 
        { concepts.add(c); }  
      } 
    } 

    for (int i = 0; i < concepts.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) concepts.get(i); 
      tc.findLinkedConcepts(concepts); 
    } 

    try { 
      pwout.close(); 
      epwout.close(); 
      br.close(); 
    } catch (Exception e) { } 

    return concepts; 
  }       

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

  public static ThesaurusConcept lookupWord(Vector cs, String wd) 
  { for (int i = 0; i < cs.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) cs.get(i); 
      if (tc.hasAnyTerm(wd))
      { return tc; } 
    } 
    return null; 
  } 


  public static double findSimilarity(String fnme, String fenme, Vector thesaurus) 
  { String nme = fnme.toLowerCase(); 
    String enme = fenme.toLowerCase(); 

    int nlen = fnme.length(); 
    int elen = fenme.length(); 

    if (nme.equalsIgnoreCase(enme)) { return 1.0; } 
    if (nme.startsWith(enme)) { return elen/(1.0*nlen); } 
    if (nme.endsWith(enme)) { return elen/(1.0*nlen); } 
    if (enme.startsWith(nme)) { return nlen/(1.0*elen); } 
    if (enme.endsWith(nme)) { return nlen/(1.0*elen); }

    String suff = ModelElement.longestCommonSuffix(nme,enme); 
    if (suff.length() > 1)
    { return suff.length()/(1.0*Math.max(fnme.length(),fenme.length())); } 

    String pref = ModelElement.longestCommonPrefix(nme,enme); 
    if (pref.length() > 1)
    { return pref.length()/(1.0*Math.max(fnme.length(),fenme.length())); } 

    for (int i = 0; i < thesaurus.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) thesaurus.get(i); 
      if (tc.hasPreferredTerm(nme)) 
      { if (tc.hasPreferredTerm(enme)) 
        { return 1.0; } 
      } 
    } 

    for (int i = 0; i < thesaurus.size(); i++) 
    { ThesaurusConcept tc = (ThesaurusConcept) thesaurus.get(i); 
      if (tc.hasAnyTerm(nme)) 
      { if (tc.hasAnyTerm(enme)) 
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



