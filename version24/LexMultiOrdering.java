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
/* Package: Utilities */ 

public class LexMultiOrdering
  implements SystemTypes
{
  private Map alphabets = new Map(); // internal
  private int n = 0; // internal
  private Map sizes = new Map(); 

  public LexMultiOrdering(Map abs)
  { Vector endflag = new Vector(); 
    endflag.add(""); 
    Maplet mend = new Maplet("",endflag); 
    this.alphabets = new Map(); 
    alphabets.elements.add(mend);
    alphabets.elements.addAll(abs.elements); 
    this.n = abs.elements.size();
    /* for (int i = 0; i < n; i++) 
    { Maplet mm = (Maplet) alphabets.elements.get(i);
      Vector alphi = (Vector) mm.dest;  
      sizes.set(new Integer(i), new Integer(alphi.size())); 
    } */ 
  }


  public String toString()
  { String _res_ = "(LexicographicOrdering) ";
    _res_ = _res_ + alphabets + ",";
    _res_ = _res_ + n;
    return _res_;
  }

  //  public void init()
  // { setn(this.getalphabet().size());
  // }

    public List incrementRec(int pos, List s)
  {   List result = new Vector();
    if (pos < 0) { return result; } 
    if (s.size() <= 0) { return result; } 
    int ssize = s.size(); 

    // last element of s is member of last element of alphabets, etc.    

    Maplet posmm = (Maplet) alphabets.elements.get(pos);
    Vector posalph = (Vector) posmm.dest;  
    int m = posalph.size();  
 
  int ind = (posalph.indexOf(Ocl.last(s)) + 1); 
     if (ind < m) 
  {   result = Ocl.concatenate(Ocl.front(s),
         (new SystemTypes.Ocl()).add(posalph.get(ind)).getElements());
 
  }  else
      if (ind == m) 
  {   result = Ocl.concatenate(this.increment(pos-1,Ocl.front(s)),
                     (new SystemTypes.Ocl()).add(posalph.get(0)).getElements());
 
  }          return result;
  }


  public List increment(int pos, List s)
  { List result = new Vector();

    if (pos < 0) { return result; } 

    Maplet mpos = (Maplet) alphabets.elements.get(pos); 
    Vector posalphabet = (Vector) mpos.dest; 
      // the elements permitted in the pos position
 
  if (s.size() == 0) 
  {   result.add(posalphabet.get(0));
 
  }  else
      if (s.size() > 0) 
  {   result = this.incrementRec(pos,s);
 
  }       return result;
  }

  public static Map getMap(List word, List sources)
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

public static void main(String[] args) 
{ Vector v1 = new Vector(); 
  v1.add("a"); v1.add("b"); v1.add("c"); 
  Vector v2 = new Vector(); 
  v2.add("d");  
  Vector v3 = new Vector(); 
  v3.add("e"); v3.add("f");  
  Map mtest = new Map();
  mtest.set("1", v1); 
  mtest.set("2", v2); 
  mtest.set("3", v3); 
  LexMultiOrdering lex = new LexMultiOrdering(mtest); 
  List w1 = lex.increment(3, new Vector()); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
  w1 = lex.increment(3, w1); 
  System.out.println(w1); 
} 

}