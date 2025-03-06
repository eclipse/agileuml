import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator; 
import java.util.ListIterator; 
import java.util.Spliterator;


class SortedOrderedSet<T extends Comparable<T>> implements Set<T>, List<T> 
{ private HashSet<T> elementSet = new HashSet<T>(); 
  private SortedSequence<T> elementSeq = new SortedSequence<T>(); 

  // invariant elementSet = elementSeq.uniqueSet(); 

  public SortedOrderedSet() { } 

  public SortedOrderedSet(Set col)
  { elementSet = new HashSet<T>(col); 
    elementSeq = new SortedSequence<T>(col); 
  } 

  public SortedOrderedSet<T> clone()
  { HashSet<T> elems = (HashSet<T>) elementSet.clone(); 
    SortedSequence<T> ss = (SortedSequence<T>) elementSeq.clone();
    SortedOrderedSet<T> sos = new SortedOrderedSet<T>();
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
    { boolean added = elementSet.add((T) obj); 
      if (added) 
      { changed = true; 
        elementSeq.add((T) obj);
      } 
    } 
    return changed;  
  }

  public boolean addAll(int i, Collection col)
  { return addAll(col); } // ignores i

  public boolean containsAll(Collection col)
  { return elementSet.containsAll(col); }

  public boolean remove(Object x)
  { boolean removed = elementSet.remove((T) x); 
    if (removed)
    { elementSeq.remove((T) x); }
    return removed; 
  } 

  public boolean add(T x)
  { boolean added = elementSet.add(x); 
    if (added) 
    { elementSeq.add(x); } 
    return added; 
  } 

  public Object[] toArray()
  { return elementSeq.toArray(); } 

  public Object[] toArray(Object[] arr)
  { return elementSeq.toArray(arr); }

  public Set<T> asSet()
  { return elementSet; } 

  public Iterator iterator()
  { return elementSeq.iterator(); }     

  public ListIterator listIterator(int x)
  { return elementSeq.listIterator(x); } 

  public ListIterator listIterator()
  { return elementSeq.listIterator(); } 

  public Spliterator spliterator()
  { return elementSeq.spliterator(); } 

  public T get(int i) 
  { return elementSeq.get(i); } 

  public List<T> subList(int i, int j)
  { return elementSeq.subList(i,j); } 

  public int lastIndexOf(Object x)
  { return elementSeq.lastIndexOf((T) x); } 

  public int indexOf(Object x)
  { return elementSeq.indexOf((T) x); } 

  public T remove(int i)
  { return elementSeq.remove(i); }

  public void add(int i, T x)
  { boolean added = elementSet.add((T) x); 
    if (added) 
    { elementSeq.add((T) x); }
  } // ignores i 
    
  // @Override
  public T set(int i, T x)
  { boolean added = elementSet.add((T) x); 
    if (added) 
    { elementSeq.add(x); }
    return null; 
  } // ignores i 

  public String toString()
  { return elementSeq.toString(); }   

  public T min()
  { return elementSeq.min(); }

  public T max()
  { return elementSeq.max(); }

  public int getCount(Object x)
  { if (elementSet.contains((T) x))
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
  { SortedOrderedSet<String> sos = new SortedOrderedSet<String>(); 
    sos.add("cc"); sos.add("bb"); 
    sos.add("aa"); sos.add("bb"); sos.add("aa"); 
    System.out.println(sos); 
	
    List<String> newelems = new ArrayList<String>(); 
    newelems.add("dd"); newelems.add("cc"); 
    newelems.add("bb");
    sos.addAll(newelems);  
    System.out.println(sos); 
	
    sos.remove("bb"); 
    System.out.println(sos); 
  } 
}

