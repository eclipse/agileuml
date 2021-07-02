/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 
import java.io.*; 

public class ASTSymbolTerm extends ASTTerm
{ String symbol = ""; 

  public ASTSymbolTerm(String s) 
  { symbol = s; } 

  public String cg(CGSpec cgs)
  { return symbol; } 

  public String cgRules(CGSpec cgs, Vector rules)
  { return symbol; } 

  public String toString()
  { return symbol; } 

  public String literalForm()
  { return symbol; } 

  public String asTextModel(PrintWriter out)
  { return "\"" + symbol + "\""; } 

  public String toKM3()
  { if ("{".equals(symbol)) 
    { return ""; } 
    if ("}".equals(symbol)) 
    { return ""; } 
    if (";".equals(symbol)) 
    { return ""; } 
    if ("=".equals(symbol)) 
    { return " := "; } 
    if ("==".equals(symbol)) 
    { return " = "; } 
    if ("!=".equals(symbol)) 
    { return " /= "; } 

    if ("&&".equals(symbol) || "&".equals(symbol)) 
    { return " & "; }
    if ("||".equals(symbol) || "|".equals(symbol)) 
    { return " or "; }
 
    if ("<".equals(symbol)) 
    { return " < "; } 
    if (">".equals(symbol)) 
    { return " > "; } 
    if ("=<".equals(symbol)) 
    { return " =< "; } 
    if (">=".equals(symbol)) 
    { return " >= "; } 

    if ("'".equals(symbol)) 
    { return "\""; } 
    if ("^".equals(symbol)) 
    { return " xor "; } 
    if ("!".equals(symbol) || "~".equals(symbol)) 
    { return " not "; } 
    if ("%".equals(symbol)) 
    { return " mod "; } 

    if ("+".equals(symbol)) 
    { return " + "; } 
    if ("-".equals(symbol)) 
    { return " - "; } 
    if ("*".equals(symbol)) 
    { return " * "; } 
    if ("/".equals(symbol)) 
    { return " / "; } 

    if ("this".equals(symbol))
    { return "self"; } 

    if ("List".equals(symbol)) 
    { return "Sequence"; } 
    if ("ArrayList".equals(symbol)) 
    { return "Sequence"; } 
    if ("Vector".equals(symbol)) 
    { return "Sequence"; } 
    if ("LinkedList".equals(symbol)) 
    { return "Sequence"; } 
    if ("Set".equals(symbol)) 
    { return "Set"; } 
    if ("HashSet".equals(symbol)) 
    { return "Set"; } 
    if ("TreeSet".equals(symbol)) 
    { return "Set"; } 
    if ("HashMap".equals(symbol)) 
    { return "Map"; } 
    if ("TreeMap".equals(symbol)) 
    { return "Map"; } 

    return symbol; 
  } 

  public boolean updatesObject()
  { return false; } 
} 