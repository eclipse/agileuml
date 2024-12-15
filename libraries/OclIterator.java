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

  ArrayList<Object> elements = (new ArrayList());
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
     boolean result = false;
    if (position >= 0 && position < elements.size())
    {
      result = true;
    }
    else {
      result = false;
    }
    return result;
  }


  public boolean isAfterLast() 
  { boolean result = false; 
    if (position > elements.size())
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
    if (position > 1 && position <= elements.size() + 1)
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
    position = elements.size();
  }


  public void moveToStart()
  {
    position = 0;
  }


  public void moveToEnd()
  {
    position = elements.size() + 1;
  }


  public static OclIterator newOclIterator_Sequence(ArrayList sq)
  { OclIterator ot = null;
    ot = OclIterator.createOclIterator();
    ot.elements = sq;
    ot.position = 0;
    return ot; 
  }

  public static OclIterator newOclIterator_String(String str)
  { OclIterator ot = null;
    ot = OclIterator.createOclIterator();
    ot.elements.addAll(Ocl.split(str, "[ \n\t\r]+"));
    ot.position = 0;
    return ot; 
  }

  public static OclIterator newOclIterator_String_String(String str, String seps)
  { OclIterator ot = null;
    ot = OclIterator.createOclIterator();
    ot.elements.addAll(Ocl.split(str, "[" + seps + "]+"));
    ot.position = 0;
    return ot; 
  }


  public static OclIterator newOclIterator_Set(Collection st)
  { ArrayList elems = new ArrayList(); 
    elems.addAll(st); 
    Collections.sort(elems); 
     OclIterator ot = null;
    ot = OclIterator.createOclIterator();
    ot.elements = elems;
    ot.position = 0;
    return ot; 
  }

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
  } 

  public Object getCurrent()
  {
    Object result = null;
    if (position > 0 && position <= elements.size())
    { result = ((Object) elements.get(position - 1)); } 
    return result;
  }


  public void set(Object x)
  {
    elements.set(position - 1,x);
  }


  public void insert(Object x)
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
  } 
      
  public Object next()
  { if (generatorFunction != null) 
    { Object res = generatorFunction.apply(new Integer(position)); 
      position++; 
      if (position <= elements.size())  
      { set(res); } 
      else
      { elements.add(res); } 
      return res; 
    } 
     
    Object result = null;
    moveForward();
    return getCurrent();
  }


  public Object previous()
  {
    Object result = null;
    moveBackward();
    return getCurrent();
  }

  public Object at(int i)
  { return elements.get(i-1); } 

  public int length()
  { return elements.size(); } 

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
    elements = (new ArrayList());
    columnNames = new ArrayList<String>(); 
  } 
  
  /*
  public static void main(String[] args)
  { ArrayList lst = new ArrayList(); 
    lst.add(1); lst.add(2); lst.add(3); lst.add(4); lst.add(5); 
    OclIterator iter1 = OclIterator.newOclIterator_Sequence(lst); 
	System.out.println(iter1.elements); 
	iter1.setPosition(3); 
	OclIterator iter2 = iter1.trySplit(); 
	System.out.println(iter1.elements); 
	System.out.println(iter2.elements); 
	
	Function<Object,Object> ff = (y)->{ System.out.println(y); return y; }; 
	iter1.forEachRemaining(ff); 
  } */ 

}

