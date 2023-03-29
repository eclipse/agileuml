import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.util.function.Function;
import java.io.Serializable;

class OclRegex { static ArrayList<OclRegex> OclRegex_allInstances = new ArrayList<OclRegex>();

  OclRegex() { OclRegex_allInstances.add(this); }

  static OclRegex createOclRegex() { OclRegex result = new OclRegex();
    return result; }

  String regex = "";
  String text = "";
  int startpos = 0;
  int endpos = 0;
  String oclregexId = ""; /* primary */
  static Map<String,OclRegex> OclRegex_index = new HashMap<String,OclRegex>();

  static OclRegex createByPKOclRegex(String oclregexIdx)
  { OclRegex result = OclRegex.OclRegex_index.get(oclregexIdx);
    if (result != null) { return result; }
    result = new OclRegex();
    OclRegex.OclRegex_index.put(oclregexIdx,result);
    result.oclregexId = oclregexIdx;
    return result; }

  static void killOclRegex(String oclregexIdx)
  { OclRegex rem = OclRegex_index.get(oclregexIdx);
    if (rem == null) { return; }
    ArrayList<OclRegex> remd = new ArrayList<OclRegex>();
    remd.add(rem);
    OclRegex_index.remove(oclregexIdx);
    OclRegex_allInstances.removeAll(remd);
  }


  public static OclRegex compile(String patt)
  {
    OclRegex result = null;
    OclRegex x = null;
    x = createOclRegex();
    x.regex = patt;
    x.text = "";
    x.startpos = 0;
    x.endpos = 0;
    result = x;
    return result;
  }


  public OclRegex matcher(String st)
  {
    OclRegex result = null;
    text = st;
    startpos = 0;
    endpos = 0;
    result = this;
    return result;
  }


  public static boolean matches(String patt, String st)
  {
    boolean result = false;
    result = (Ocl.isMatch(st,patt));
    return result;
  }


  public boolean isMatch()
  {
    boolean result = false;
    result = (Ocl.isMatch(text,regex));
    return result;
  }


  public ArrayList<String> allMatches(String tx)
  {
    ArrayList<String> result = new ArrayList<String>();
    result = (Ocl.allMatches(tx,regex));
    return result;
  }


  public boolean find()
  {
    boolean result = false;
    String txt = null;
    txt = Ocl.subrange(text,endpos + 1,text.length());
    if (Ocl.hasMatch(txt,regex))
    {
      String f = null;
    f = Ocl.firstMatch(txt,regex);
    startpos = endpos + (txt.indexOf(f) + 1);
    endpos = startpos + f.length() - 1;
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

