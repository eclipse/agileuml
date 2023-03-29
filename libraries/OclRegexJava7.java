package oclregex;


import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.lang.*;
import java.lang.reflect.*;
import java.util.StringTokenizer;
import java.io.*;

class OclRegex
  implements SystemTypes
{
  private String regex; // internal
  private String text; // internal
  private int startpos; // internal
  private int endpos; // internal

  public OclRegex()
  {
    this.regex = "";
    this.text = "";
    this.startpos = 0;
    this.endpos = 0;

  }



  public String toString()
  { String _res_ = "(OclRegex) ";
    _res_ = _res_ + regex + ",";
    _res_ = _res_ + text + ",";
    _res_ = _res_ + startpos + ",";
    _res_ = _res_ + endpos;
    return _res_;
  }

  public void setregex(String regex_x) { regex = regex_x;  }


  public void settext(String text_x) { text = text_x;  }


  public void setstartpos(int startpos_x) { startpos = startpos_x;  }


  public void setendpos(int endpos_x) { endpos = endpos_x;  }


    public String getregex() { return regex; }

    public static HashSet<String> getAllregex(Collection<OclRegex> oclregex_s)
  { HashSet<String> result = new HashSet<String>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(oclregex_x.getregex()); }
    return result; }

    public static ArrayList<String> getAllOrderedregex(Collection<OclRegex> oclregex_s)
  { ArrayList<String> result = new ArrayList<String>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(oclregex_x.getregex()); } 
    return result; }

    public String gettext() { return text; }

    public static HashSet<String> getAlltext(Collection<OclRegex> oclregex_s)
  { HashSet<String> result = new HashSet<String>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(oclregex_x.gettext()); }
    return result; }

    public static ArrayList<String> getAllOrderedtext(Collection<OclRegex> oclregex_s)
  { ArrayList<String> result = new ArrayList<String>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(oclregex_x.gettext()); } 
    return result; }

    public int getstartpos() { return startpos; }

    public static HashSet<Integer> getAllstartpos(Collection<OclRegex> oclregex_s)
  { HashSet<Integer> result = new HashSet<Integer>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(new Integer(oclregex_x.getstartpos())); }
    return result; }

    public static ArrayList<Integer> getAllOrderedstartpos(Collection<OclRegex> oclregex_s)
  { ArrayList<Integer> result = new ArrayList<Integer>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(new Integer(oclregex_x.getstartpos())); } 
    return result; }

    public int getendpos() { return endpos; }

    public static HashSet<Integer> getAllendpos(Collection<OclRegex> oclregex_s)
  { HashSet<Integer> result = new HashSet<Integer>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(new Integer(oclregex_x.getendpos())); }
    return result; }

    public static ArrayList<Integer> getAllOrderedendpos(Collection<OclRegex> oclregex_s)
  { ArrayList<Integer> result = new ArrayList<Integer>();
    for (Object _o : oclregex_s)
    { OclRegex oclregex_x = (OclRegex) _o;
      result.add(new Integer(oclregex_x.getendpos())); } 
    return result; }

    public static OclRegex compile(String patt)
  {   OclRegex result = null;
  
  OclRegex x = new OclRegex();
    x.setregex(patt);
    x.settext("");
    x.setstartpos(0);
    x.setendpos(0);
    result = x;  
    return result;
  }


    public OclRegex matcher(String st)
  {   OclRegex result = null;
  
  text = st;
   startpos = 0;
   endpos = 0;
   result = this;
           
    return result;
  }


    public static boolean matches(String patt,String st)
  {   boolean result = false;
  
  result = ( st.matches(patt) );
  
    return result;
  }


    public boolean isMatch()
  {   boolean result = false;
  
  result = ( text.matches(regex) );
  
    return result;
  }


    public ArrayList<String> allMatches(String tx)
  {   ArrayList<String> result = new ArrayList<String>();
  
  result = ( Ocl.allMatches(tx,regex) );
  
    return result;
  }


  public boolean find()
  { boolean result;
    OclRegex oclregexx = this;
    String txt;
    txt = Ocl.subrange(oclregexx.gettext(),
                  oclregexx.getendpos() + 1,
                  oclregexx.gettext().length());
    if (Ocl.hasMatch(txt, oclregexx.getregex()))
    { String f;
      f = Ocl.firstMatch(txt, oclregexx.getregex());
      startpos = oclregexx.getendpos() + (txt.indexOf(f) + 1);
      endpos = oclregexx.getstartpos() + f.length() - 1;
      return true;
    }
    else {
      return false;
    }
  }

  public boolean hasNext()
  { return find(); } 

  public boolean hasNext(String tx)
  { text = tx; 
    return find(); 
  } 
}





