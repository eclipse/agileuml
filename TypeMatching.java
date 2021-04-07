import java.util.Vector;
import java.util.Set;
import java.util.HashSet;
import java.io.*; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: MT Synthesis */ 


public class TypeMatching
{ // Created from the enum-enum matchings etc, and
  // used to generate data-conversion functions in every MT language
  String name = ""; 
  Type src;
  Type trg;
  Vector valueMappings = new Vector();

  public TypeMatching(Type s, Type t)
  { src = s;
    trg = t;
    if (s != null && t != null && 
        s.isEnumeration() && t.isEnumeration())
    { name = "convert" + s.getName() + "_" + t.getName(); } 
    else 
    { name = s.getName() + "2" + t.getName(); }  
  }

  public static TypeMatching lookupByName(String nme, Vector tms)
  { for (int i = 0; i < tms.size(); i++) 
    { TypeMatching tm = (TypeMatching) tms.get(i); 
      if (tm.getName().equals(nme))
      { return tm; } 
    } 
    return null; 
  } 
  
  public ValueMatching lookupBySource(String val)
  { ValueMatching res = null; 
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      if (val.equals(vm.src + ""))
	  { return vm; };
    }
	return res; 
  }
  
  public void setName(String nme)
  { name = nme; }

  public String getName()
  { return name; }

  public void addValueMapping(Expression s, Expression t)
  { ValueMatching v = new ValueMatching(s,t);
    valueMappings.add(v);
  }

  public void setValueMapping(Expression s, Expression t)
  { ValueMatching v = lookupBySource(s + ""); 
    if (v == null) 
	{ v = new ValueMatching(s,t); 
	  valueMappings.add(v); 
	} 
	else 
	{ v.trg = t; }
  } // update or create, so there is no duplication of value mappings. 

  public void setStringValues(String[] svals, String[] tvals)
  { Type stringtype = new Type("String", null); 
    for (int i = 0; i < svals.length && i < tvals.length; i++) 
    { BasicExpression s = new BasicExpression(svals[i]);
      BasicExpression t = new BasicExpression(tvals[i]); 
      s.setType(stringtype); 
      t.setType(stringtype);  
	  setValueMapping(s,t); 
    } 
  }

  public void addValueMapping(ValueMatching vm)
  { 
    valueMappings.add(vm);
  }

  public String toString()
  { String res = name + ":\n" + "  " + src + " |--> " + trg + "\n";
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      res = res + "        " + vm + "\n";
    }
    return res;
  }

  public boolean equals(Object obj) 
  { if (obj instanceof TypeMatching)
    { TypeMatching tm = (TypeMatching) obj; 
      if ((tm + "").equals(this + ""))
      { return true; } 
    } 
    return false; 
  } // every ValueMatching of tm has equal one in this, 
    // & vice-versa

  public boolean isBijective()
  { Set domain = new HashSet();
    Set range = new HashSet();
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      domain.add(vm.src + "");
      range.add(vm.trg + "");
    }
    if (domain.size() == range.size() &&
        domain.size() == valueMappings.size())
    { return true; }
    return false;
  }  

  public TypeMatching invert()
  { TypeMatching res = new TypeMatching(trg,src);
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      ValueMatching ivm = vm.invert();
      res.addValueMapping(ivm);
    }
    return res;
  }

  public String qvtfunction()
  { String res = "  query " + name; 
    String restail = ""; 
      

    if (src.isEnumeration() && trg.isEnumeration())
    { res = Type.enumConversionOpQVT(src,trg); } 
    else if (src.isString() && trg.isEnumeration())
    { res = trg.stringEnumQueryOpQVTR(); } 
    else if (trg.isString() && src.isEnumeration())
    { res = src.enumStringQueryOpQVTR(); } 
    else if (trg.isBoolean() && src.isEnumeration())
    { res = Type.enumBooleanOpQVTR(src); } 
    else if (src.isBoolean() && trg.isEnumeration())
    { res = Type.booleanEnumOpQVTR(trg); } 
    else if (src.isString() && trg.isString())
    { res = res + "(s : String) : String =\n    "; 
      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = "\"" + vm.src + "\""; 
        String valt = "\"" + vm.trg + "\""; 
        res = res + "if s = " + vals + " then " + valt + " else "; 
        restail = restail + " endif "; 
      }
      res = res + " \"\" " + restail + ";\n\n";  
    } 
    return res;
  } 

  public String umlrsdsfunction()
  { String res = "  query " + name; 
    String restail = ""; 
      

    if (src.isEnumeration() && trg.isEnumeration())
    { res = Type.enum2enumOp(src,trg); } 
    else if (src.isString() && trg.isEnumeration())
    { res = trg.string2EnumOp(); } 
    else if (trg.isString() && src.isEnumeration())
    { res = src.enum2StringOp(); } 
    else if (trg.isBoolean() && src.isEnumeration())
    { res = Type.enumBooleanOp(src); } 
    else if (src.isBoolean() && trg.isEnumeration())
    { res = Type.booleanEnumOp(trg); } 
    else if (src.isString() && trg.isString())
    { res = res + "(s : String) : String\n"; 
      res = res + "  pre: true\n"; 
      res = res + "  post: \n"; 
	   
      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = "\"" + vm.src + "\""; 
        String valt = "\"" + vm.trg + "\""; 
        res = res + "    ( s = " + vals + " => result = " + valt + " ) &\n";  
      }
      res = res + "    ( true => result = \"\" );\n\n";  
    } 
    return res;
  }

  public void cstlfunction(PrintWriter out)
  { /* 
    if (src.isEnumeration() && trg.isEnumeration())
    { res = Type.enum2enumOp(src,trg); } 
    else if (src.isString() && trg.isEnumeration())
    { res = trg.string2EnumOp(); } 
    else if (trg.isString() && src.isEnumeration())
    { res = src.enum2StringOp(); } 
    else if (trg.isBoolean() && src.isEnumeration())
    { res = Type.enumBooleanOp(src); } 
    else if (src.isBoolean() && trg.isEnumeration())
    { res = Type.booleanEnumOp(trg); } 
    else */ 
    if (src.isString() && trg.isString())
    { out.println("Text::"); 
      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = vm.src + ""; 
        String valt = vm.trg + ""; 
        out.println(vals + " |-->" + valt);  
      }
    } 
  }
}
