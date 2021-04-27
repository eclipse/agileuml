/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ASTBasicTerm extends ASTTerm
{ String tag = ""; 
  String value = ""; 

  public ASTBasicTerm(String t, String v) 
  { tag = t; 
    value = v; 
  } 

  public void setTag(String t)
  { tag = t; } 

  public void setValue(String v)
  { value = v; } 

  public String toString()
  { String res = "(" + tag + " " + value + ")"; 
    return res; 
  } 

  public String toKM3()
  { if ("char".equals(value))
    { return "String"; } 
    if ("Character".equals(value))
    { return "String"; } 
    if ("StringBuffer".equals(value))
    { return "String"; } 
    if ("StringBuilder".equals(value))
    { return "String"; } 
    if ("Integer".equals(value))
    { return "int"; } 
    if ("Byte".equals(value))
    { return "int"; } 
    if ("Short".equals(value))
    { return "int"; } 
    if ("Double".equals(value))
    { return "double"; } 
    if ("Float".equals(value))
    { return "double"; } 
    if ("Long".equals(value))
    { return "long"; } 
    if ("Boolean".equals(value))
    { return "boolean"; }

    if ("ArrayList".equals(value))
    { return "Sequence"; } 
    if ("Vector".equals(value))
    { return "Sequence"; } 
    if ("LinkedList".equals(value))
    { return "Sequence"; } 
    if ("List".equals(value))
    { return "Sequence"; } 
    if ("Queue".equals(value))
    { return "Sequence"; } 
    if ("PriorityQueue".equals(value))
    { return "Sequence"; } 
     
    if ("HashSet".equals(value))
    { return "Set"; } 
    if ("TreeSet".equals(value))
    { return "Set"; } 
  
    return value; 
  } 
} 