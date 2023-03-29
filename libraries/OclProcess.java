import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.util.concurrent.TimeUnit; 


class OclProcess 
{ static ArrayList<OclProcess> OclProcess_allInstances = new ArrayList<OclProcess>();

  OclProcess() { OclProcess_allInstances.add(this); }

  static OclProcess createOclProcess() 
  { OclProcess result = new OclProcess();
    return result; }

  String name = "";
  int priority = 5;
  Thread actualThread = null; 
  Object executes = null;
  Process process = null; 


  public static OclProcess getRuntime()
  { Runtime rt = Runtime.getRuntime(); 
    OclProcess result = new OclProcess();
    result.executes = rt; 
    return result;
  }


  public static void notify(Object obj)
  { synchronized(obj) 
    { obj.notify(); }
  }


  public static void notifyAll(Object obj)
  { synchronized(obj) 
    { obj.notifyAll(); }
  } 


  public static void wait(Object obj, double t)
  { try { obj.wait((long) t); }
    catch (Exception e) 
    { Thread.currentThread().interrupt(); } 
  } 

  public int waitFor()
  { if (process != null) 
    { try 
      { return process.waitFor(); }
      catch (Exception e) { return -1; } 
    }  
    return 0; 
  }

  public int waitFor(long t)
  { if (process != null) 
    { try 
      { boolean res = process.waitFor(t, TimeUnit.MILLISECONDS); 
        if (res) { return 0; } 
        return -1; 
      }
      catch (Exception e) { return -1; } 
    }  
    return 0; 
  }


  public static OclProcess newOclProcess(Object obj, String s)
  {
    OclProcess p = null;
    p = OclProcess.createOclProcess();
    p.name = s;
    p.priority = 5;
    p.executes = obj;
    if (obj instanceof Runnable)
    { p.actualThread = new Thread((Runnable) obj); } 
    else 
    { try 
      { p.process = Runtime.getRuntime().exec(s); }
      catch (Exception _e) { } 
    }  
    return p; 
  }


  public static int activeCount()
  { return Thread.activeCount(); }


  public static OclProcess currentThread()
  { Thread curr = Thread.currentThread(); 
    OclProcess result = new OclProcess();
    result.actualThread = curr; 
    return result;
  }


  public static ArrayList<OclProcess> allActiveThreads()
  { ArrayList<OclProcess> result = new ArrayList<OclProcess>();
    Thread[] threads = new Thread[Thread.activeCount()]; 
    Thread.enumerate(threads); 
    for (int i = 0; i < threads.length; i++) 
    { OclProcess pr = new OclProcess(); 
      pr.actualThread = threads[i]; 
      result.add(pr); 
    } 
    return result;
  }


  public static void sleep(long n)
  { try { Thread.sleep(n); }
    catch (Exception e) 
    { Thread.currentThread().interrupt(); } 
  } 


  public String getName()
  { return name; }


  public void setName(String nme)
  {
    name = nme;
  }


  public int getPriority()
  { if (actualThread != null) 
    { priority = actualThread.getPriority(); } 
    return priority;
  }


  public void run()
  { if (actualThread != null) 
    { actualThread.start(); } 
  } // Should be actualThread.run()


  public void start()
  { if (actualThread != null) 
    { actualThread.start(); } 
  } // Should be actualThread.run()


  public boolean isAlive()
  { if (actualThread != null) 
    { return actualThread.isAlive(); }
    else if (process != null) 
    { return process.isAlive(); }  
    return false;
  }


  public boolean isDaemon()
  { if (actualThread != null) 
    { return actualThread.isDaemon(); } 
    return false;
  }


  public void join(double ms)
  { if (actualThread != null) 
    { try { actualThread.join((long) ms); } 
      catch (Exception e) { } 
    } 
  }


  public void destroy()
  { if (actualThread != null) 
    { actualThread.interrupt(); 
      actualThread = null; 
    } 
    else if (process != null) 
    { process.destroyForcibly(); } 
  }


  public void interrupt()
  { if (actualThread != null) 
    { actualThread.interrupt(); } 
  }


  public static String getEnvironmentProperty(String var)
  { return System.getProperty(var); }

  public static String setEnvironmentProperty(String var, String value)
  { String res = System.getProperty(var); 
    System.setProperty(var,value); 
    return res; 
  }

  public static String clearEnvironmentProperty(String var)
  { return System.clearProperty(var); }


  public static HashMap<String,String> getEnvironmentProperties()
  { Properties props = System.getProperties(); 
    Set<String> pnames = props.stringPropertyNames(); 
    HashMap<String,String> result = new HashMap<String,String>();
    for (String str : pnames)
    { result.put(str,(String) props.get(str)); } 
    return result;
  }


  public static void exit(int n)
  { System.exit(n); }

}

