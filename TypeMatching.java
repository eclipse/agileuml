import java.util.Vector;
import java.util.Set;
import java.util.HashSet;
import java.io.*; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
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

  public TypeMatching(String nme)
  { name = nme; 
    src = new Type("String", null); 
    trg = new Type("String", null); 
  } 

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

  public void addValueMapping(String ss, String tt)
  { BasicExpression s = new BasicExpression(ss); 
    BasicExpression t = new BasicExpression(tt); 
    ValueMatching v = new ValueMatching(s,t);
    valueMappings.add(v);
  }

  public void addValueMap(AttributeMatching am)
  { // _1 _2 .. |-->_j

    if (am != null && am.srcvalue != null && 
        am.trgvalue != null) 
    { BasicExpression lhs = (BasicExpression) am.srcvalue; 
      BasicExpression rhs = (BasicExpression) am.trgvalue; 
      String rulelhs = 
        lhs.toCSTL();
      String rulerhs = 
        rhs.toLiteralCSTL();

      ValueMatching vm = 
        new ValueMatching(rulelhs,rulerhs);
      
      if (rulelhs.trim().equals(rulerhs.trim()) && 
          lhs.arity() == 1 && rhs.arity() == 1)
      { vm = new ValueMatching("_1", "_1"); 
        if (valueMappings.contains(vm)) { } 
        else 
        { valueMappings.add(vm); } // at end
      }  
      else if (valueMappings.contains(vm)) { } 
      else 
      { valueMappings.add(0,vm); } // at start
    } 
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

  public void addDefaultMapping(String var, String val)
  { Type stringtype = new Type("String", null); 
    BasicExpression s = new BasicExpression(var);
    BasicExpression t = new BasicExpression(val); 
    s.setType(stringtype); 
    t.setType(stringtype);  
    valueMappings.add(new ValueMatching(s,t)); 
  }   

  public void setValues(boolean[] svals, String[] tvals)
  { Type stringtype = new Type("String", null); 
    Type booltype = new Type("boolean", null); 
    
    for (int i = 0; i < svals.length && i < tvals.length; i++) 
    { BasicExpression s = new BasicExpression(svals[i]);
      BasicExpression t = new BasicExpression(tvals[i]); 
      s.setType(booltype); 
      t.setType(stringtype);  
      setValueMapping(s,t); 
    } 
  }

  public void setValues(String[] svals, boolean[] tvals)
  { Type stringtype = new Type("String", null); 
    Type booltype = new Type("boolean", null); 
    
    for (int i = 0; i < svals.length && i < tvals.length; i++) 
    { BasicExpression s = new BasicExpression(svals[i]);
      BasicExpression t = new BasicExpression(tvals[i]); 
      s.setType(stringtype); 
      t.setType(booltype);  
      setValueMapping(s,t); 
    } 
  }

  public void setValues(double[] svals, String[] tvals)
  { Type stringtype = new Type("String", null); 
    Type doubletype = new Type("double", null); 
    
    for (int i = 0; i < svals.length && i < tvals.length; i++) 
    { BasicExpression s = new BasicExpression(svals[i]);
      BasicExpression t = new BasicExpression(tvals[i]); 
      s.setType(doubletype); 
      t.setType(stringtype);  
      setValueMapping(s,t); 
    } 
  }

  public void setValues(double[] svals, Vector[] tvals)
  { Type stringtype = new Type("String", null); 
    Type doubletype = new Type("double", null); 
    
    for (int i = 0; i < svals.length && i < tvals.length; i++) 
    { BasicExpression s = new BasicExpression(svals[i]);
      Vector tv = tvals[i]; 
      
      BasicExpression t = new BasicExpression("" + tv.get(0)); 
      s.setType(doubletype); 
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

  public boolean isVacuous()
  { // Only has _1 |-->_1 or _0 |-->_0

    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      if (vm.isIdentity()) { } 
      else 
      { return false; } 
    }
    return true; 
  }

  public Vector usesCSTLfunctions()
  { // value mappings have substrings `f
    Vector res = new Vector();  
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      String rhs = vm.trg + ""; 
      Vector mfs = CGRule.metafeatures(rhs);
      for (int y = 0; y < mfs.size(); y++) 
      { String mf = (String) mfs.get(y); 
        int qind = mf.indexOf("`"); 
        res.add(mf.substring(qind+1));  
      } 
    } 
    return res; 
  } 

  public String toCSTL(CGSpec cg)
  { String res = name + "::\n"; 
    cg.addCategory(name); 
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      res = res + vm.src + " |-->" + vm.trg + "\n";
      String lhs = (vm.src + "").trim(); 
      String rhs = (vm.trg + "").trim();

      System.out.println(lhs + " |--> " + rhs + " " + vm.src.arity() + " " + vm.trg.arity()); 
 
      CGRule rr = new CGRule("" + vm.src, "" + vm.trg);

      if ("_0".equals(lhs) && "_0".equals(rhs))
      { } 
      else if ("_1".equals(lhs) && "_1".equals(rhs))
      { } 
      else if (lhs.equals(rhs) && 
          vm.src.arity() <= 1 && vm.trg.arity() <= 1)
      { rr = new CGRule("_1", "_1"); } 
      cg.addCategoryRuleInOrder(name,rr); 
    }

    return res;
  }

  public String toCSTL(String category, CGSpec cg)
  { String res = ""; 
    for (int x = 0; x < valueMappings.size(); x++)
    { ValueMatching vm = (ValueMatching) valueMappings.get(x);
      res = res + vm.src + " |-->" + vm.trg + "\n";
      String lhs = (vm.src + "").trim(); 
      String rhs = (vm.trg + "").trim(); 

      System.out.println(lhs + " |--> " + rhs + " " + vm.src.arity() + " " + vm.trg.arity()); 

      CGRule rr = new CGRule("" + vm.src, "" + vm.trg); 

      if ("_0".equals(lhs) && "_0".equals(rhs))
      { } 
      else if ("_1".equals(lhs) && "_1".equals(rhs))
      { } 
      else if (lhs.equals(rhs) && 
          vm.src.arity() <= 1 && vm.trg.arity() <= 1)
      { rr = new CGRule("_1", "_1"); } 
      cg.addCategoryRuleInOrder(category,rr); 
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
    else if (src.isNumeric() && trg.isString())
    { res = res + "(s : Real) : String =\n    "; 
      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = vm.src + ""; 
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
    { res = res + "(s : " + src.getName() + ") : " + trg.getName() + "\n"; 
      res = res + "  pre: true\n"; 
      res = res + "  post: \n"; 
	   
      String dft = trg.getDefault(); 

      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = vm.src + ""; 
        String valt = vm.trg + ""; 
        res = res + "    ( s = " + vals + " => result = " + valt + " ) &\n";  
      }
      res = res + "    ( true => result = " + dft + " );\n\n";  
    } 
    else if (src.isString() && trg.isEnumeration())
    { res = res + "(s : String) : " + trg.getName() + "\n"; 
      res = res + "  pre: true\n"; 
      res = res + "  post: \n"; 
	   
      String dft = trg.getDefault(); 

      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = "\"" + vm.src + "\""; 
        String valt = vm.trg + ""; 
        res = res + "    ( s = " + vals + " => result = " + valt + " ) &\n";  
      }
      res = res + "    ( true => result = " + dft + " );\n\n";  
    } 
    else if (trg.isString() && src.isEnumeration())
    { res = res + "(s : " + src.getName() + ") : String\n"; 
      res = res + "  pre: true\n"; 
      res = res + "  post: \n"; 
	   
      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = vm.src + ""; 
        String valt = "\"" + vm.trg + "\""; 
        res = res + "    ( s = " + vals + " => result = " + valt + " ) &\n";  
      }
      res = res + "    ( true => result = \"\" );\n\n";  
    } 
    else if (trg.isBoolean() && src.isEnumeration())
    { res = res + "(s : " + src.getName() + ") : boolean\n"; 
      res = res + "  pre: true\n"; 
      res = res + "  post: \n"; 
	   
      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = vm.src + ""; 
        String valt = vm.trg + ""; 
        res = res + "    ( s = " + vals + " => result = " + valt + " ) &\n";  
      }
      res = res + "    ( true => result = false );\n\n";
    } 
    else if (src.isBoolean() && trg.isEnumeration())
    { res = res + "(s : boolean) : " + trg.getName() + "\n"; 
      res = res + "  pre: true\n"; 
      res = res + "  post: \n"; 
	   
      String dft = trg.getDefault(); 

      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = vm.src + ""; 
        String valt = vm.trg + ""; 
        res = res + "    ( s = " + vals + " => result = " + valt + " ) &\n";  
      }
      res = res + "    ( true => result = " + dft + " );\n\n";  
    } 
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
    else if (src.isNumeric() && trg.isString())
    { res = res + "(s : double) : String\n"; 
      res = res + "  pre: true\n"; 
      res = res + "  post: \n"; 
	   
      for (int i = 0; i < valueMappings.size(); i++) 
      { ValueMatching vm = (ValueMatching) valueMappings.get(i); 
        String vals = vm.src + ""; 
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
