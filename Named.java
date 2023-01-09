/*
      * Classname : Named
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class is a superclass of all elements with a 
      *               name. 
*/

/******************************
* Copyright (c) 2003--2023 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.io.*;
import java.util.Vector; 

abstract public class Named 
{
  String label;

  public Named(String id)
  { label = id; } 

  public String getName()
  { return label; } 

  public void setLabel(String l) 
  { label = l; } 

  public void addPrefix(String pre) 
  { label = pre + label; } 

  public abstract Object clone(); 

  // Also clones the items
  public static Vector prefixAll(String pre, Vector nameds) 
  { Vector res = new Vector(); 

    for (int i = 0; i < nameds.size(); i++) 
    { Named obj = (Named) nameds.get(i); 
      Named newobj = (Named) obj.clone(); 
      newobj.addPrefix(pre); 
      res.add(newobj); 
    } 
    return res; 
  } 

  public void display()
  { System.out.println(label); }

  public String toString() 
  { return label; }   /* Default, will get overridden */ 

  public static Vector getNames(Vector nameds) 
  { Vector res = new Vector(); 
    for (int i = 0; i < nameds.size(); i++) 
    { Named nn = (Named) nameds.elementAt(i); 
      res.add(nn.label); 
    } 
    return res; 
  } 

  public static Vector namesOfNumberRange(int s, int e)
  { Vector res = new Vector(); 
    for (int i = s; i <= e; i++) 
    { res.add("" + i); } 
    return res; 
  } 

  public static String nameFor(String[] arr)
  { String res = "";
    for (int i = 0; i < arr.length; i++)
    { res = res + arr[i];
      if (i < arr.length - 1)
      { res = res + "_"; }
    }
    return res;
   }

  public static String capitalise(String str) 
  { if (str.length() > 0) 
    { String s1 = str.charAt(0) + ""; 
      return s1.toUpperCase() + str.substring(1,str.length()); 
    } 
    return str; 
  } 

  public static String decapitalise(String str) 
  { if (str.length() > 0) 
    { String s1 = str.charAt(0) + ""; 
      return s1.toLowerCase() + str.substring(1,str.length()); 
    } 
    return str; 
  } 

  public static void main(String[] args) 
  { System.out.println(Named.capitalise("delta")); } 


}
