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
            System.out.println(">> " + c + " is specialisation of " + ndef);
            Entity cent = c.getEntity(); 
            ThesaurusConcept suptc = 
                Thesarus.lookupWord(concepts, ndef); 
            if (suptc != null && cent != null) 
            { Vector supsem = suptc.getSemantics(); 
              for (int k = 0; k < supsem.size(); k++) 
              { Object obj = supsem.get(k); 
                if (obj instanceof Entity)
                { Entity sent = (Entity) obj;
                  cent.setSuperclass(sent); 
                } 
              } 
            }    
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
            { String cleanname = 
                 Named.removeInvalidCharacters(c.name); 
              Entity ent = new Entity(cleanname); 
              epwout.println(cleanname);
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

  void addConceptTerm(ThesaurusConcept c, ThesaurusTerm t)
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



