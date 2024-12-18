import java.util.Vector; 
import java.util.List; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: utilities */ 

public class LexicographicOrdering
  implements SystemTypes
{
  private List alphabet = new Vector(); // internal
  private int n = 0; // internal

  public LexicographicOrdering()
  {
    this.alphabet = new Vector();
    this.n = 0;

  }


  public String toString()
  { String _res_ = "(LexicographicOrdering) ";
    _res_ = _res_ + alphabet + ",";
    _res_ = _res_ + n;
    return _res_;
  }

  public static LexicographicOrdering parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = Ocl.tokeniseCSV(_line);
    LexicographicOrdering lexicographicorderingx = new LexicographicOrdering();
    lexicographicorderingx.n = Integer.parseInt((String) _line1vals.get(1));
    return lexicographicorderingx;
  }


  public void writeCSV(PrintWriter _out)
  { LexicographicOrdering lexicographicorderingx = this;
    _out.print("" + lexicographicorderingx.alphabet);
    _out.print(" , ");
    _out.print("" + lexicographicorderingx.n);
    _out.println();
  }


  public void setalphabet(List alphabet_x) { alphabet = alphabet_x;  }


    public void addalphabet(String alphabet_x)
  { alphabet.add(alphabet_x); }

  public void removealphabet(String alphabet_x)
  { alphabet.remove(alphabet_x); }
  

  public void setn(int n_x) { n = n_x;  }


    public List getalphabet() { return alphabet; }


    public int getn() { return n; }


    public void init()
  { setn(this.getalphabet().size());
  }

    public List incrementRec(List s)
  {   List result = new Vector();
    if (s.size() <= 0) { return result; } 
   
  int ind = (alphabet.indexOf(Ocl.last(s)) + 1); 
     if (ind < n) 
  {   result = Ocl.concatenate(Ocl.front(s),
         (new SystemTypes.Ocl()).add(alphabet.get(ind)).getElements());
 
  }  else
      if (ind == n) 
  {   result = Ocl.concatenate(this.increment(Ocl.front(s)),
                     (new SystemTypes.Ocl()).add(alphabet.get(0)).getElements());
 
  }          return result;
  }


    public List increment(List s)
  {   List result = new Vector();
 
  if (s.size() == 0) 
  {   result = (new SystemTypes.Ocl()).add(alphabet.get(0)).getElements();
 
  }  else
      if (s.size() > 0) 
  {   result = this.incrementRec(s);
 
  }       return result;
  }

  public Map getMap(List word, List sources)
  { // map source(i) -> word(i), etc until word exhausted.

    // int disp = sources.size() - word.size(); 
    // if (disp < 0) 
    // { disp = 0; } 

    int disp = 0; 

    Map res = new Map(); 
    for (int i = 0; i < word.size(); i++) 
    { Object t = word.get(i); 
      if (0 <= i + disp && i + disp < sources.size())
      { Object s = sources.get(i + disp); 
        res.set(s,t); 
      } 
      else 
      { return res; } 
    } 
    return res; 
  } 



    public boolean noduplicates(List s)
  {   boolean result = false;
 
  List st = Ocl.asSet(s); 
     if (st.size() == s.size()) 
  {   result = true;
 
  }       return result;
  }



}