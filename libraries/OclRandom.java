import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;

class OclRandom { static ArrayList<OclRandom> OclRandom_allInstances = new ArrayList<OclRandom>();

  OclRandom() { OclRandom_allInstances.add(this); }

  static OclRandom createOclRandom() { OclRandom result = new OclRandom();
    return result; }

  int ix = 0;
  int iy = 0;
  int iz = 0;
  String oclrandomId = ""; /* primary */
  static Map<String,OclRandom> OclRandom_index = new HashMap<String,OclRandom>();

  static OclRandom createByPKOclRandom(String oclrandomIdx)
  { OclRandom result = OclRandom.OclRandom_index.get(oclrandomIdx);
    if (result != null) { return result; }
    result = new OclRandom();
    OclRandom.OclRandom_index.put(oclrandomIdx,result);
    result.oclrandomId = oclrandomIdx;
    return result; }

  static void killOclRandom(String oclrandomIdx)
  { OclRandom rem = OclRandom_index.get(oclrandomIdx);
    if (rem == null) { return; }
    ArrayList<OclRandom> remd = new ArrayList<OclRandom>();
    remd.add(rem);
    OclRandom_index.remove(oclrandomIdx);
    OclRandom_allInstances.removeAll(remd);
  }


  public static OclRandom newOclRandom()
  {
    OclRandom result = null;
    OclRandom rd = null;
    rd = OclRandom.createOclRandom();
    rd.ix = 1001;
    rd.iy = 781;
    rd.iz = 913;
    result = rd;
    return result;
  }


  public static OclRandom newOclRandom(long n)
  {
    OclRandom result = null;
    OclRandom rd = null;
    rd = OclRandom.createOclRandom();
    rd.ix = (int) (n % 30269);
    rd.iy = (int) (n % 30307);
    rd.iz = (int) (n % 30323);
    result = rd;
    return result;
  }


  public void setSeeds(int x, int y, int z)
  {
    this.ix = x;
    this.iy = y;
    this.iz = z;
  }


  public void setSeed(long n)
  {
    this.ix = (int) (n % 30269);
    this.iy = (int) (n % 30307);
    this.iz = (int) (n % 30323);
  }


  public double nrandom()
  {
    double result = 0.0;
    this.ix = (this.ix * 171) % 30269;
    this.iy = (this.iy * 172) % 30307;
    this.iz = (this.iz * 170) % 30323;
    return (this.ix / 30269.0 + this.iy / 30307.0 + this.iz / 30323.0);
  }


  public double nextDouble()
  {
    double result = 0.0;
    double r = 0.0;
    r = this.nrandom();
    result = (r - ((int) Math.floor(r)));
    return result;
  }


  public double nextFloat()
  {
    double result = 0.0;
    double r = 0.0;
    r = this.nrandom();
    result = (r - ((int) Math.floor(r)));
    return result;
  }


  public double nextGaussian()
  {
    double result = 0.0;
    double d = 0.0;
    d = this.nrandom();
    result = (d / 3.0 - 0.5);
    return result;
  }


  public int nextInt(int n)
  {
    int result = 0;
    double d = 0.0;
    d = this.nextDouble();
    result = ((int) Math.floor((d * n)));
    return result;
  }


  public int nextInt()
  {
    int result = 0;
    result = this.nextInt(2147483647);
    return result;
  }


  public long nextLong()
  {
    long result = 0;
    double d = 0.0;
    d = this.nextDouble();
    result = (long) Math.floor(d * 9223372036854775807L);
    return result;
  }


  public boolean nextBoolean()
  {
    boolean result = false;
    double d = 0.0;
    d = this.nextDouble();
    if (d > 0.5)
    {
      result = true;
    }
    return result;
  }


  public static ArrayList randomiseSequence(ArrayList sq)
  {
    OclRandom r = null;
    r = OclRandom.newOclRandom();
    ArrayList res = new ArrayList();
    res = (new ArrayList());
    ArrayList old = new ArrayList();
    old = Ocl.union((new ArrayList()),sq);
    while (old.size() > 0)
    {
      int x = 0;
      x = old.size();
      if (x == 1)
      {
        res = Ocl.includingSequence(res,((Object) old.get(1 - 1)));
        return res;
      }
      else {
        Object obj = 0;
        int n = r.nextInt(x); 
        obj = old.get(n);
        res = Ocl.includingSequence(res,obj);
        old = Ocl.removeAt(old,n+1);
      }
    }
    return res;
  }

  public static String randomString(int n) 
  { String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$";
    String res = ""; 
    for (int i = 0; i < n; i++) 
    { int code = (int) Math.floor(Math.random()*54); 
      res = res + characters.charAt(code); 
    } 
    return res; 
  }  

  public static Object randomElement(ArrayList col)
  { if (col.size() == 0) 
    { return null; } 
    int n = col.size(); 
    int ind = (int) Math.floor(Math.random()*n);
    return col.get(ind); 
  } 
   
}

