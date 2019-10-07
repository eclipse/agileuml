import java.util.*; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ObjectSpecification extends ModelElement
{ // String objectName;
  
  String objectClass;
  Entity entity;
  List atts = new ArrayList();  // String
  java.util.Map attvalues = new HashMap(); // String --> String
  List elements = new ArrayList(); // ObjectSpecification
  boolean isSwingObject = true; 

  public ObjectSpecification(String nme, String typ)
  { // objectName = nme;
    super(nme);
    objectClass = typ;
    if (isJavaGUIClass(typ)) 
    { isSwingObject = true; } 
    else 
    { isSwingObject = false; } 
  }

  public String toString()
  { return getName() + " : " + objectClass + "\n" +
        attvalues + "\n" + elements +"\n\n";
  }

  public static boolean isJavaGUIClass(String s)
  { return s.equals("Frame") || s.equals("Panel") || s.equals("Button") ||
           s.equals("MenuBar") || s.equals("Menu") || s.equals("MenuItem") ||
           s.equals("Table") || s.equals("TextField") || s.equals("TextArea") ||
           s.equals("Dialog") || s.equals("Label"); 
  } 

  public void addAttribute(String att, String value)
  { atts.add(att);
    attvalues.put(att,value); 
  }

  public void addelement(ObjectSpecification elem)
  { elements.add(elem); }

  public List getelements() { return elements; }

  public List getatts() { return atts; }

  public boolean hasAttribute(String att)
  { return atts.contains(att); }

  public String getattvalue(String att)
  { return (String) attvalues.get(att); }

  public String getDeclaration()
  { if (isSwingObject == false)
    { return objectClass + " " + getName() + " = new " + objectClass + "();"; } 

    String res = "J" + objectClass + " " + getName() + " ";
    if (objectClass.equals("Button") || 
        objectClass.equals("MenuItem"))
    { String btext = getattvalue("text");
      res = res + " = new J" + objectClass + "(" + btext + ");"; 
    }
    else if (objectClass.equals("Label") || 
             objectClass.equals("Menu"))
    { String btext = getattvalue("text");
      res = res + " = new J" + objectClass + "(" + btext + ");"; 
    }
    else if (objectClass.equals("Table"))
    { res = res + ";"; } 
    else 
    { res = res + " = new J" + objectClass + "();"; }
    return res;
  }

  public String getDefinition()
  { String res = "";
    if (!isSwingObject)
    { for (int i = 0; i < atts.size(); i++) 
      { String att = (String) atts.get(i); 
        String val = getattvalue(att); 
        res = res + getName() + ".set" + att + "(" + val + ");\n      "; 
      } 
      return res; 
    }

    if (objectClass.equals("Button") || 
        objectClass.equals("MenuItem"))
    { String btext = getattvalue("text");
      res = getName() + ".addActionListener(this);"; 
    }
    else if (objectClass.equals("Table"))
    { String rows = getattvalue("rows"); 
      String cols = getattvalue("columns"); 
      String data = getattvalue("cells"); 
      res = getName() + " = JTableBuilder.buildTable(" + data + "," + cols + 
                                                     "," + rows + ");";
    } 


    for (int i = 0; i < elements.size(); i++)
    { ObjectSpecification elem = 
         (ObjectSpecification) elements.get(i);
      res = res + "\n    " + getName() + ".add(" +
               elem.getName() + ");";
    }
    return res;
  }

  public List getcomponents(List objs)
  { List res = new ArrayList();
     for (int i = 0; i < atts.size(); i++)
     { String att = (String) atts.get(i);
       String val = getattvalue(att);
        // if an object, add:
       ModelElement valobj = 
            ModelElement.lookupByName(val,objs);
       if (valobj != null && 
           valobj instanceof ObjectSpecification)
       { res.add(valobj); }
    }
    return res;
  }

  public List getallcomponents(List objs)
  { // all ObjectSpecs which it (recursively) contains
     List res = new ArrayList();
     for (int i = 0; i < atts.size(); i++)
     { String att = (String) atts.get(i);
       String val = getattvalue(att);
       ModelElement valobj = 
            ModelElement.lookupByName(val,objs);
       if (valobj != null && 
           valobj instanceof ObjectSpecification)
       { ObjectSpecification valos = (ObjectSpecification) valobj;
         res.add(valos); 
         res.addAll(valos.getallcomponents(objs)); 
       }
    }
    return res;
  }

  public void generateJava(PrintWriter out)
  { } 

}
