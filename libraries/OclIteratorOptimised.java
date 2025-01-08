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


class OclIteratorResult { 
  static OclIteratorResult createOclIteratorResult() { 
    OclIteratorResult result = new OclIteratorResult();
    return result; 
  }

  boolean done = false;
  Object value = null;

  static OclIteratorResult newOclIteratorResult(Object v) 
  { OclIteratorResult res = createOclIteratorResult();
    res.value = v;
    if (v == null) 
    { res.done = true; }
    else
    { res.done = false; }
    return res;
  } 

}


class OclIterator { static ArrayList<OclIterator> OclIterator_allInstances = new ArrayList<OclIterator>();

  OclIterator() { OclIterator_allInstances.add(this); }

  static OclIterator createOclIterator() { OclIterator result = new OclIterator();
    return result; }

   int position = 0;
   int markedPosition = 0;
   Function<Integer,Object> generatorFunction = null; 

  Object[] elements;
  ArrayList<String> columnNames = (new ArrayList<String>());
 
  String ocliteratorId = ""; /* primary */
  static Map< String,OclIterator> OclIterator_index = new HashMap< String,OclIterator>();


  static OclIterator createByPKOclIterator( String ocliteratorIdx)
  { OclIterator result = OclIterator.OclIterator_index.get(ocliteratorIdx);
    if (result != null) { return result; }
    result = new OclIterator();
    OclIterator.OclIterator_index.put(ocliteratorIdx,result);
    result.ocliteratorId = ocliteratorIdx;
    return result; }

  static void killOclIterator( String ocliteratorIdx)
  { OclIterator rem = OclIterator_index.get(ocliteratorIdx);
    if (rem == null) { return; }
    ArrayList<OclIterator> remd = new ArrayList<OclIterator>();
    remd.add(rem);
    OclIterator_index.remove(ocliteratorIdx);
    OclIterator_allInstances.removeAll(remd);
  }


  public  boolean hasNext()
  {
    return (position >= 0 && position < elements.length); 
  }


  public boolean isAfterLast() 
  { boolean result = false; 
    if (position > elements.length)
    { return true; } 
    return result; 
  } 

  public boolean isBeforeFirst() 
  { boolean result = false; 
    if (position <= 0)
    { return true; } 
    return result; 
  } 

  public boolean hasPrevious()
  {
    boolean result = false;
    if (position > 1 && position <= elements.length + 1)
    {
      result = true;
    }
    else {
      result = false;
    }
    return result;
  }


  public int nextIndex()
  {
    int result = 0;
    result = position + 1;
    return result;
  }


  public int previousIndex()
  {
    int result = 0;
    result = position - 1;
    return result;
  }


  public void moveForward()
  {
    position = position + 1;
  }


  public void moveBackward()
  {
    position = position - 1;
  }


  public void moveTo( int i)
  {
    position = i;
  }

  public void setPosition(int i)
  {
    position = i;
  }

  public void markPosition()
  {
    markedPosition = position;
  }

  public void movePosition(int i)
  {
    position = i + position;
  }

  public void moveToMarkedPosition()
  {
    position = markedPosition;
  }

  public void moveToFirst()
  {
    position = 1;
  }

  public void moveToLast()
  {
    position = elements.length;
  }


  public void moveToStart()
  {
    position = 0;
  }


  public void moveToEnd()
  {
    position = elements.length + 1;
  }


  public static OclIterator newOclIterator_Sequence(ArrayList sq)
  { OclIterator ot = null;
    ot = OclIterator.createOclIterator();
	int n = sq.size(); 
    ot.elements = new Object[n];
	for (int i = 0; i < n; i++)
	{ ot.elements[i] = sq.get(i); }
    ot.position = 0;
    return ot; 
  }
 
  public static OclIterator newOclIterator_String(String str)
  { OclIterator ot = null;
    ot = OclIterator.createOclIterator();
	ArrayList<String> chars = Ocl.split(str, "[ \n\t\r]+"); 
    int n = chars.size(); 
	ot.elements = new Object[n];
	for (int i = 0; i < n; i++)
	{ ot.elements[i] = chars.get(i); }
    
    ot.position = 0;
    return ot; 
  }

  public static OclIterator newOclIterator_String_String(String str, String seps)
  { OclIterator ot = null;
    ot = OclIterator.createOclIterator();
    ArrayList<String> chars = Ocl.split(str, "[" + seps + "]+");
    int n = chars.size(); 
	ot.elements = new Object[n];
	for (int i = 0; i < n; i++)
	{ ot.elements[i] = chars.get(i); }
    ot.position = 0;
    return ot; 
  }


  public static OclIterator newOclIterator_Set(Collection st)
  { ArrayList elems = new ArrayList(); 
    elems.addAll(st); 
    Collections.sort(elems); 
    OclIterator ot = null;
    ot = OclIterator.createOclIterator();
    int n = elems.size(); 
	ot.elements = new Object[n];
	for (int i = 0; i < n; i++)
	{ ot.elements[i] = elems.get(i); }
    ot.position = 0;
    return ot; 
  }

/*
  public static OclIterator newOclIterator_Function(Function<Integer,Object> f)
  { OclIterator ot = null;
    ot = OclIterator.createOclIterator();
    ot.generatorFunction = f; 
    ot.position = 0;
    return ot; 
  }

  public OclIterator trySplit()  
  { ArrayList firstpart = Ocl.subrange(elements,1,position-1); 
    elements = Ocl.subrange(elements, position); 
    position = 0;
	markedPosition = 0; 
    return OclIterator.newOclIterator_Sequence(firstpart); 
  } */ 

  public Object getCurrent()
  {
    Object result = null;
    if (position > 0 && position <= elements.length)
    { result = elements[position - 1]; } 
    return result;
  }


  public void set(Object x)
  {
    elements[position - 1] = x;
  }


  /* public void insert(Object x)
  { elements.add(position-1,x); }


  public void remove()
  { elements.remove(position - 1); }

  public boolean tryAdvance(Function<Object,Object> f) 
  { if (position + 1 <= elements.size())  
    { Object x = this.next();   
      f.apply(x);  
      return true; 
    } 
    return false; 
  }  
  
  public void forEachRemaining(Function<Object,Object> f)  
  { ArrayList remainingElements = Ocl.subrange(elements, position); 
    for (Object x : remainingElements)
    { f.apply(x); } 
  }  


  public OclIteratorResult nextResult()
  { if (generatorFunction == null) 
    { Object v = next();
      return OclIteratorResult.newOclIteratorResult(v);
    }
 
    Object res = generatorFunction.apply(new Integer(position)); 
    position++; 
    if (position <= elements.size())  
    { set(res); } 
    else
    { elements.add(res); } 
    return OclIteratorResult.newOclIteratorResult(res); 
  } */ 
      
  public Object next()
  { /* if (generatorFunction != null) 
    { Object res = generatorFunction.apply(new Integer(position)); 
      position++; 
      if (position <= elements.size())  
      { set(res); } 
      else
      { elements.add(res); } 
      return res; 
    } */ 
     
    moveForward();
    return getCurrent();
  }


  public Object previous()
  {
    moveBackward();
    return getCurrent();
  }

  public Object at(int i)
  { return elements[i-1]; } 

  public int length()
  { return elements.length; } 

  public int getPosition()
  { return position; } 
  
  public int getColumnCount()  
  { return columnNames.size(); } 

  public String getColumnName(int i)  
  { if (i > 0 && i <= columnNames.size())
    { return columnNames.get(i-1); } 
    return null; 
  } 

  public Object getCurrentFieldByIndex(int i)  
  { String fld = columnNames.get(i-1); 
    Map curr = (Map) this.getCurrent();
    if (curr != null && fld != null)  
    { return curr.get(fld); } 
    return null; 
  } 

  public void setCurrentFieldByIndex(int i, Object v)  
  { String fld = columnNames.get(i-1); 
    Map curr = (Map) this.getCurrent();
    if (curr != null && fld != null)  
    { curr.put(fld,v); } 
  } 

  public void close()
  { position = 0;
    markedPosition = 0;
    elements = null;
    columnNames = new ArrayList<String>(); 
  } 
  
  
  public static void main(String[] args)
  { ArrayList lst = new ArrayList(); 
    for (int i = 0; i < 100000; i++)
    { lst.add(i); } 
    OclIterator iter1 = OclIterator.newOclIterator_Sequence(lst); 
	java.util.Date d1 = new java.util.Date(); 
	long t1 = d1.getTime(); 
	
	for (int j = 0; j < 100000; j++) 
	{ iter1.next(); 
	  iter1.set(1); 
	}
	
	java.util.Date d2 = new java.util.Date(); 
	long t2 = d2.getTime();
	System.out.println(t2-t1);  
  }  

}

