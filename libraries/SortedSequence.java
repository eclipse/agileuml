import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Spliterator;


class SortedSequence implements List 
{ ArrayList elements = new ArrayList(); 

  public SortedSequence() { }

  public SortedSequence(List col)
  { elements = new ArrayList(); 
    elements.addAll(col); 
    Collections.sort(elements); 
  } 

  public SortedSequence(Collection col)
  { elements = new ArrayList(); 
    elements.addAll(col); 
    Collections.sort(elements); 
  } 

  public SortedSequence clone()
  { ArrayList elems = (ArrayList) elements.clone(); 
    SortedSequence ss = new SortedSequence();
    ss.elements = elems; 
    return ss;
  }
  
  public Object get(int i) 
  { return elements.get(i); } 

  public List subList(int i, int j)
  { ArrayList subelems = new ArrayList(); 
    SortedSequence ss = new SortedSequence();
    ss.elements.addAll(elements.subList(i,j)); 
    return ss; 
  } 

  public ListIterator listIterator(int x)
  { return elements.listIterator(x); } 

  public ListIterator listIterator()
  { return elements.listIterator(); } 

  public Iterator iterator()
  { return elements.iterator(); } 

  public int lastIndexOf(Object x)
  { int insertPoint = Collections.binarySearch(elements, x); 
    
    if (insertPoint < 0)
    { return insertPoint; }
	
    for (int i = insertPoint+1; i < elements.size(); i++) 
    { if (x.equals(elements.get(i)))
      { insertPoint++; }
      else 
      { return insertPoint; }
    } 
    return insertPoint; 
  }  

  public int indexOf(Object x)
  { return elements.indexOf(x); } 
  // can be optimised using binarySearch

  public boolean remove(Object x)
  { return elements.remove(x); } 

  public Object remove(int x)
  { return elements.remove(x); }

  public void add(int i, Object x)
  { // Only changes the list if i is the correct position

    int insertionPoint = 
           Collections.binarySearch(elements, x); 
               // log(elements.size()) time complexity

    if (insertionPoint < 0)
    { int ip = -(insertionPoint + 1); 
      if (i == ip)
      { elements.add(i,x); } 
    } 
    else 
    { // in list already 
      if (i == insertionPoint)
      { elements.add(i,x); } 
    } 
  } 

  public boolean add(Object x)
  { int insertionPoint = 
           Collections.binarySearch(elements, x); 
               // log(elements.size()) time complexity

    if (insertionPoint < 0)
    { int ip = -(insertionPoint + 1); 
      elements.add(ip,x);  
    } 
    else 
    { elements.add(insertionPoint,x); } 
    return true; 
  } 

  public boolean add(Object x, int nCopies)
  { int insertionPoint = 
           Collections.binarySearch(elements, x); 
               // log(elements.size()) time complexity

    if (insertionPoint < 0)
    { int ip = -(insertionPoint + 1); 
      for (int i = 0; i < nCopies; i++) 
      { elements.add(ip,x); }  
    } 
    else 
    { for (int i = 0; i < nCopies; i++) 
      { elements.add(insertionPoint,x); }
    }
	 
    return true; 
  } 

  public Object set(int i, Object x)
  { int insertionPoint = 
           Collections.binarySearch(elements, x); 
               // log(elements.size()) time complexity

    if (insertionPoint < 0)
    { int ip = -(insertionPoint + 1); 
      if (i == ip)
      { return elements.set(i,x); }
      return null;  
    } 
    else 
    { // in list already 
      return x;  
    } 
  } // another interpretation is to remove the element at 
    // position i, and insert x. 

  public void clear()
  { elements.clear(); }

  public boolean addAll(int index, Collection col)
  { boolean res = elements.addAll(col); 
    Collections.sort(elements);
    return res;  
  } // ignores the index.  

  public SortedSequence merge(SortedSequence sq)
  { ArrayList res = new ArrayList();

    int reached = 0; 
 
    for (int i = 0; i < elements.size(); i++) 
    { Comparable obj = (Comparable) elements.get(i);
      Comparable sqelem = 
           (Comparable) sq.elements.get(reached);  
      if (obj.compareTo(sqelem) < 0)
      { res.add(obj); } 
      else 
      { res.add(sqelem); 
        reached++; 
        while (reached < sq.elements.size() && 
               obj.compareTo(sq.elements.get(reached)) >= 0)
        { res.add(sq.elements.get(reached)); 
          reached++; 
        } 
        res.add(obj); 
      }
    } 

    for (int i = reached; i < sq.elements.size(); i++) 
    { res.add(sq.elements.get(i)); } 
 
    SortedSequence newsq = new SortedSequence(); 
    newsq.elements = res;   
    return newsq; 
  } 
    
  public java.util.Set uniqueSet()
  { return asSet(); }

  public java.util.Set asSet()
  { java.util.TreeSet res = new java.util.TreeSet(); 
    if (elements.size() == 0) 
    { return res; } 

    Object elem0 = elements.get(0); 
    res.add(elem0); 

    Object lastAdded = elem0;         

    for (int i = 1; i < elements.size(); i++) 
    { Object elem = elements.get(i); 
      if (elem.equals(lastAdded)) { } 
      else 
      { res.add(elem); 
        lastAdded = elem; 
      } 
    } 

    return res; 
  }

  public boolean contains(Object x)
  { int insertionPoint = 
           Collections.binarySearch(elements, x); 
               // log(elements.size()) time complexity

    if (insertionPoint < 0)
    { return false; } 
    else 
    { return true; } 
  } 
  
  public boolean retainAll(Collection col)
  { return elements.retainAll(col); }   

  public int size()
  { return elements.size(); }   

  public boolean isEmpty()
  { return elements.isEmpty(); }   

  public String toString()
  { return elements.toString(); }   

  public boolean removeAll(Collection col)
  { return elements.removeAll(col); }   

  public boolean addAll(Collection col)
  { boolean res = elements.addAll(col); 
    Collections.sort(elements);
    return res;  
  } 
   
  public boolean containsAll(Collection col)
  { return elements.containsAll(col); }   
  // can be optimised using binarySearch repeatedly
  
  public Object[] toArray()
  { return elements.toArray(); } 

  public Object[] toArray(Object[] arr)
  { return elements.toArray(arr); }
  
  public Object min()
  { return elements.get(0); }

  public Object max()
  { return elements.get(elements.size()-1); }
  
  public int getCount(Object x)
  { int insertionPoint = 
           this.lastIndexOf(x); 
               // log(elements.size()) time complexity

    // System.out.println(">>> Insertion point for " + x + " is " + insertionPoint); 
	
    if (insertionPoint < 0)
    { return 0; } 
    else 
    { int cnt = 0; 
	  for (int i = insertionPoint; i >= 0; i--) 
	  { if (x.equals(elements.get(i)))
	    { cnt++; }
		else 
		{ break; }
      } 
      return cnt; 
    }  
  } 
  
  public boolean equals(Object col)
  { if (col instanceof SortedSequence)
    { SortedSequence ss = (SortedSequence) col; 
      return elements.equals(ss.elements); 
    }
    return false; 
  }    

  public Spliterator spliterator()
  { Spliterator splt = elements.spliterator(); 
    // splt.SORTED = 1; 
    return splt; 
  } 

  public static void main(String[] args)
  { SortedSequence ss = new SortedSequence(); 
     
    ss.add("bb", 2); ss.add("aa", 3); ss.add("cc", 1);  ss.add("bb", 6); 
    System.out.println(ss); 
	System.out.println(ss.lastIndexOf("dd")); 
	System.out.println(ss.lastIndexOf("bb"));
	System.out.println(ss.getCount("dd")); 
	System.out.println(ss.getCount("bb"));

    SortedSequence tt = new SortedSequence(); 
    tt.add("aa"); tt.add("ee"); 

    SortedSequence pp = ss.merge(tt); 
    System.out.println(pp); 

    System.out.println(pp.asSet()); 
  }  
}

