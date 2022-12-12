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


public class CorrelationPattern
{ String name; 
  String description; 
  String warning = "!! Warning: source models may not map to valid target models!"; 
  boolean issueWarning = false; 

  Vector sourceEntities; // of Entity
  Vector targetEntities; // of Entity
  Vector sourceFeatures; // of ModelElement (Attribute or Association)
  Vector targetFeatures; // of ModelElement (Attribute or Association)

  public CorrelationPattern(String n, String d)
  { name = n; 
    description = d; 
    sourceEntities = new Vector(); 
    targetEntities = new Vector(); 
    sourceFeatures = new Vector(); 
    targetFeatures = new Vector(); 
  } 

  public String toString()
  { String res = "Correlation pattern " + name + " \n" + description; 
    if (issueWarning) 
    { res = res + " \n" + warning; }
    return res; 
  }  

  public boolean equals(Object x) 
  { if (x instanceof CorrelationPattern) 
    { return (((CorrelationPattern) x).description + "").equals(description + ""); } 
    return false; 
  } 

  void setWarning(boolean w)
  { issueWarning = w; } 


  public void addSourceEntity(Entity e) 
  { sourceEntities.add(e); } 

  public void addSourceEntities(Vector es) 
  { sourceEntities.addAll(es); } 

  public void addTargetEntity(Entity e) 
  { targetEntities.add(e); } 

  public void addTargetEntities(Vector et) 
  { targetEntities.addAll(et); } 

  public void addSourceFeature(ModelElement e) 
  { sourceFeatures.add(e); } 

  public void addTargetFeature(ModelElement e) 
  { targetFeatures.add(e); } 
} 
