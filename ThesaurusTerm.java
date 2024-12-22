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



public class ThesaurusTerm
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


