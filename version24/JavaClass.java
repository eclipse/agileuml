import java.util.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class JavaClass
{ String cname = "";
  List instanceVariables = new ArrayList(); // Attribute
  List operations = new ArrayList(); // JavaOperation
  JavaOperation constructor;
  String declaration = "";
  String maindef = "";
  String extendsList = null;
  List implementsList = new ArrayList(); // String 

  public JavaClass(String nme)
  { cname = nme; }

  public void addinstanceVariables(Attribute att)
  { instanceVariables.add(att); }

  public void addoperations(JavaOperation jo)
  { operations.add(jo); }

  public void setdeclaration(String dec)
  { declaration = dec; }

  public void setconstructor(JavaOperation cons)
  { constructor = cons; }

  public void setmaindef(String md)
  { maindef = md; }

  public void setextendsList(String ext)
  { extendsList = ext; }

  public void addimplementsList(String intf)
  { implementsList.add(intf); }

  public String toString()
  { String res = "public class " + cname;
    if (extendsList == null) {}
    else 
    { res = res + " extends " + extendsList; }
    if (implementsList == null || implementsList.size() == 0) {}
    else 
    { res = res + " implements ";
      for (int i = 0; i < implementsList.size(); i++)
      { res = res + implementsList.get(i);
        if (i < implementsList.size() - 1)
        { res = res + ", "; }
      }
    }
    res = res + "\n{ ";
    /* for (int i = 0; i < instanceVariables.size(); i++)
    { Attribute att = (Attribute) instanceVariables.get(i);
      res = res + att.toJava() + ";\n  "; 
    } */ 
    res = res + declaration + "\n\n";

    if (constructor == null) {}
    { res = res + "  " + constructor.toString(); }
    res = res + "\n\n";

    for (int j = 0; j < operations.size(); j++)
    { JavaOperation jo = (JavaOperation) operations.get(j);
      res = res + "  " + jo + "\n\n";
    }

    if (maindef == null || maindef.equals(""))
    { }
    else 
    { res = res +
        "  public static void main(String[] args)\n" +
        "  { " + maindef + "\n  }\n";
    }
    res = res + "}\n";
    return res;
  }
}

