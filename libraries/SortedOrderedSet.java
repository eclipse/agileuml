import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator; 
import java.util.ListIterator; 
import java.util.Spliterator;


class SortedOrderedSet implements Set, List 
{ private HashSet elementSet = new HashSet(); 
  private SortedSequence elementSeq = new SortedSequence(); 

  // invariant elementSet = elementSeq.uniqueSet(); 

  public SortedOrderedSet() { } 

  public SortedOrderedSet(Set col)
  { elementSet = new HashSet(col); 
    elementSeq = new SortedSequence(col); 
  } 

  public SortedOrderedSet clone()
  { HashSet elems = (HashSet) elementSet.clone(); 
    SortedSequence ss = (SortedSequence) elementSeq.clone();
    SortedOrderedSet sos = new SortedOrderedSet();
    sos.elementSet = elems;
    sos.elementSeq = ss; 
    return sos;
  }

  public boolean contains(Object x)
  { return elementSet.contains(x); } 

  public boolean isEmpty()
  { return elementSet.isEmpty(); } 

  public int size()
  { return elementSet.size(); } 

  public void clear()
  { elementSet.clear(); 
    elementSeq.clear(); 
  } 

  public boolean removeAll(Collection col)
  { elementSeq.removeAll(col); 
    return elementSet.removeAll(col); 
  }

  public boolean retainAll(Collection col)
  { elementSeq.retainAll(col); 
    return elementSet.retainAll(col); 
  }      

  public boolean addAll(Collection col)
  { boolean changed = false; 
    for (Object obj : col)
    { boolean added = elementSet.add(obj); 
      if (added) 
      { changed = true; 
        elementSeq.add(obj);
      } 
    } 
    return changed;  
  }

  public boolean addAll(int i, Collection col)
  { return addAll(col); } // ignores i

  public boolean containsAll(Collection col)
  { return elementSet.containsAll(col); }

  public boolean remove(Object x)
  { boolean removed = elementSet.remove(x); 
    if (removed)
    { elementSeq.remove(x); }
    return removed; 
  } 

  public boolean add(Object x)
  { boolean added = elementSet.add(x); 
    if (added) 
    { elementSeq.add(x); } 
    return added; 
  } 

  public Object[] toArray()
  { return elementSeq.toArray(); } 

  public Object[] toArray(Object[] arr)
  { return elementSeq.toArray(arr); }

  public Set asSet()
  { return elementSet; } 

  public Iterator iterator()
  { return elementSeq.iterator(); }     

  public ListIterator listIterator(int x)
  { return elementSeq.listIterator(x); } 

  public ListIterator listIterator()
  { return elementSeq.listIterator(); } 

  public Spliterator spliterator()
  { return elementSeq.spliterator(); } 

  public Object get(int i) 
  { return elementSeq.get(i); } 

  public List subList(int i, int j)
  { return elementSeq.subList(i,j); } 

  public int lastIndexOf(Object x)
  { return elementSeq.lastIndexOf(x); } 

  public int indexOf(Object x)
  { return elementSeq.indexOf(x); } 

  public Object remove(int x)
  { return elementSeq.remove(x); }

  public void add(int i, Object x)
  { boolean added = elementSet.add(x); 
    if (added) 
    { elementSeq.add(x); }
  } // ignores i 
    
  public Object set(int i, Object x)
  { boolean added = elementSet.add(x); 
    if (added) 
    { return elementSeq.add(x); }
    return null; 
  } // ignores i 

  public String toString()
  { return elementSeq.toString(); }   

  public Object min()
  { return elementSeq.min(); }

  public Object max()
  { return elementSeq.max(); }

  public int getCount(Object x)
  { if (elementSet.contains(x))
    { return 1; } 
    return 0; 
  } 

  public boolean equals(Object col)
  { if (col instanceof SortedOrderedSet)
    { SortedOrderedSet ss = (SortedOrderedSet) col; 
      return elementSeq.equals(ss.elementSeq); 
    }
    return false; 
  }    

  public static void main(String[] args)
  { SortedOrderedSet sos = new SortedOrderedSet(); 
    sos.add("cc"); sos.add("bb"); 
    sos.add("aa"); sos.add("bb"); sos.add("aa"); 
    List newelems = new ArrayList(); 
    newelems.add("dd"); newelems.add("cc"); 
    newelems.add("bb");
    sos.addAll(newelems);  
    System.out.println(sos); 
	sos.remove("bb"); 
	System.out.println(sos); 
  } 
}

