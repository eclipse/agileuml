/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class ASTSymbolTerm extends ASTTerm
{ String symbol = ""; 

  public ASTSymbolTerm(String s) 
  { symbol = s; } 

  public String toString()
  { return symbol; } 

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

    return symbol; 
  } 
} 