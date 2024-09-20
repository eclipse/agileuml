import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;

class OclRandom { 
  static ArrayList<OclRandom> OclRandom_allInstances = new ArrayList<OclRandom>();

  OclRandom() { OclRandom_allInstances.add(this); }

  static OclRandom createOclRandom() { OclRandom result = new OclRandom();
    return result; }

  int ix = 0;
  int iy = 0;
  int iz = 0;
  String distribution = "uniform"; 
  String algorithm = "LCG"; /* also PCG */ 

  double bernoulliP = 0.0; 
  int binomialN = 1; 
  double binomialP = 0.0; 
  double normalMean = 0.0; 
  double normalVariance = 1.0; 
  private double uniformLower = 0.0; 
  private double uniformUpper = 1.0; 

  private static OclRandom _defaultInstanceOclRandom = null; 
  private Pcg32 pcg; 

  String oclrandomId = ""; /* primary */
  static Map<String,OclRandom> OclRandom_index = new HashMap<String,OclRandom>();

  static OclRandom createByPKOclRandom(String oclrandomIdx)
  { OclRandom result = OclRandom.OclRandom_index.get(oclrandomIdx);
    if (result != null) { return result; }
    result = new OclRandom();
    OclRandom.OclRandom_index.put(oclrandomIdx,result);
    result.oclrandomId = oclrandomIdx;
    result.distribution = "uniform";
    return result; 
  }

  static void killOclRandom(String oclrandomIdx)
  { OclRandom rem = OclRandom_index.get(oclrandomIdx);
    if (rem == null) 
    { return; }
    ArrayList<OclRandom> remd = new ArrayList<OclRandom>();
    remd.add(rem);
    OclRandom_index.remove(oclrandomIdx);
    OclRandom_allInstances.removeAll(remd);
  }

  public static OclRandom defaultInstanceOclRandom()
  { if (OclRandom._defaultInstanceOclRandom == null)
    { 
      OclRandom._defaultInstanceOclRandom = OclRandom.newOclRandom();
    }
    return OclRandom._defaultInstanceOclRandom; 
  }

  public void setix(int ix_x) { ix = ix_x;  }

  public void setiy(int iy_x) { iy = iy_x;  }

  public void setiz(int iz_x) { iz = iz_x;  }

  public void setdistribution(String distribution_x) { distribution = distribution_x;  }

  public void setbernoulliP(double bernoulliP_x) { bernoulliP = bernoulliP_x;  }

  public void setnormalMean(double normalMean_x) { normalMean = normalMean_x;  }

  public void setnormalVariance(double normalVariance_x) { normalVariance = normalVariance_x;  }

  public void setuniformLower(double uniformLower_x) { uniformLower = uniformLower_x;  }

  public void setuniformUpper(double uniformUpper_x) { uniformUpper = uniformUpper_x;  }

  public int getix() { return ix; }

  public int getiy() { return iy; }

  public int getiz() { return iz; }

  public String getdistribution() { return distribution; }

  public double getbernoulliP() { return bernoulliP; }

  public double getnormalMean() { return normalMean; }

  public double getnormalVariance() { return normalVariance; }

  public double getuniformLower() { return uniformLower; }

  public double getuniformUpper() { return uniformUpper; }

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

  public static OclRandom newOclRandom_PCG()
  {
    OclRandom result = null;
    OclRandom rd = null;
    rd = OclRandom.createOclRandom();
    rd.ix = 1001;
    rd.iy = 781;
    rd.iz = 913;
    rd.pcg = new Pcg32();
    rd.algorithm = "PCG"; 
    result = rd;
    return result;
  }

  public static OclRandom newOclRandom_Seed(long n)
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

  public static OclRandom newOclRandomBernoulli(double p)
  {
    OclRandom rd = OclRandom.createOclRandom();
    rd.ix = 1001;
    rd.iy = 781;
    rd.iz = 913;
    rd.distribution = "bernoulli";
    rd.bernoulliP = p;
    return rd;
  }

  public static OclRandom newOclRandomBinomial(int n, double p)
  {
    OclRandom rd = OclRandom.createOclRandom();
    rd.ix = 1001;
    rd.iy = 781;
    rd.iz = 913;
    rd.distribution = "binomial";
    rd.binomialN = n; 
    rd.binomialP = p;
    return rd;
  }

  public static OclRandom newOclRandomNormal(double mu, double vari)
  {
    OclRandom rd = OclRandom.createOclRandom();
    rd.ix = 1001;
    rd.iy = 781;
    rd.iz = 913;
    rd.distribution = "normal";
    rd.normalMean = mu;
    rd.normalVariance = vari; 
    return rd;
  }

  public static OclRandom newOclRandomLogNormal(double mu, double vari)
  {
    OclRandom rd = OclRandom.createOclRandom();
    rd.ix = 1001;
    rd.iy = 781;
    rd.iz = 913;
    rd.distribution = "lognormal";
    rd.normalMean = mu;
    rd.normalVariance = vari; 
    return rd;
  }

  public static OclRandom newOclRandomUniform(double lwr,double upr)
  { 
    OclRandom rd = OclRandom.createOclRandom();
    rd.setix(1001);
    rd.setiy(781);
    rd.setiz(913);
    rd.setdistribution("uniform");
    rd.setuniformLower(lwr);
    rd.setuniformUpper(upr);
    return rd;
  }


  public void setSeeds(int x, int y, int z)
  {
    this.ix = x;
    this.iy = y;
    this.iz = z;
    if ("PCG".equals(algorithm))
    { pcg.seed(x,y); } 
  }


  public void setSeed(long n)
  {
    this.ix = (int) (n % 30269);
    this.iy = (int) (n % 30307);
    this.iz = (int) (n % 30323);
    if ("PCG".equals(algorithm))
    { pcg.seed(n,n); } 
  }

  public void setAlgorithm(String algo)
  { if ("PCG".equals(algo))
    { pcg = new Pcg32(); }
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
  { if ("PCG".equals(algorithm))
    { return pcg.nextDouble(); } 

    double result = 0.0;
    double r = 0.0;
    r = this.nrandom();
    result = (r - ((int) Math.floor(r)));
    return result;
  }


  public double nextFloat()
  { if ("PCG".equals(algorithm))
    { return pcg.nextFloat(); } 

    double result = 0.0;
    double r = 0.0;
    r = this.nrandom();
    result = (r - ((int) Math.floor(r)));
    return result;
  }


  public double nextGaussian()
  { if ("PCG".equals(algorithm))
    { return pcg.nextGaussian(); } 

    double result = 0.0;
    double d = 0.0;
    d = this.nrandom();
    result = d*2.0 - 3.0;
    return result;
  }


  public int nextInt(int n)
  { if ("PCG".equals(algorithm))
    { return pcg.nextInt(n); } 

    int result = 0;
    double d = 0.0;
    d = this.nextDouble();
    result = ((int) Math.floor((d * n)));
    return result;
  }

  public int nextInt()
  { if ("PCG".equals(algorithm))
    { return pcg.nextInt(); } 

    int result = 0;
    result = this.nextInt(2147483647);
    return result;
  }

  public long nextLong()
  { if ("PCG".equals(algorithm))
    { return pcg.nextLong(); } 

    long result = 0;
    double d = 0.0;
    d = this.nextDouble();
    result = (long) Math.floor(d * 9223372036854775807L);
    return result;
  }

  public long nextLong(long n)
  { if ("PCG".equals(algorithm))
    { return pcg.nextLong(n); } 

    long result = 0;
    double d = 0.0;
    d = this.nextDouble();
    result = (long) Math.floor(d * n);
    return result;
  }

  public boolean nextBoolean()
  { if ("PCG".equals(algorithm))
    { return pcg.nextBoolean(); } 

    boolean result = false;
    double d = 0.0;
    d = this.nextDouble();
    if (d > 0.5)
    {
      result = true;
    }
    return result;
  }

  public int nextBernoulli(double p)
  {
    int result = 1;
    double d = 0.0;
    d = this.nextDouble();
    if (d > p)
    {
      result = 0;
    }
    return result;
  }

  public int nextBinomial(int n, double p) 
  { int res = 0; 
    for (int ind = 0; ind < n; ind++)  
    { double d = this.nextDouble(); 
      if (d <= p)
      { res = res + 1; }
    }  
    return res; 
  } 

  public double nextNormal(double mu, double vari)
  { double d = 0.0; 
    int i = 0; 
    for ( ; i < 12; i++) 
    { d = d + this.nextDouble(); }
    d = d - 6; 
    return mu + d*Math.sqrt(vari); 
  }

  public double nextLogNormal(double mu, double vari)
  { double d = nextNormal(mu,vari); 
    return Math.exp(d); 
  }

  public double nextUniform(double lwr,double upr)
  { double result = 0.0;
  
    final double d = this.nextDouble(); 
    result = lwr + ( upr - lwr ) * d;
     
    return result;
  }

  public double next()
  { if (distribution.equals("normal"))
    { return this.nextNormal(normalMean,normalVariance); }
    if (distribution.equals("lognormal"))
    { return this.nextLogNormal(normalMean,normalVariance); }
    if (distribution.equals("bernoulli"))
    { return this.nextBernoulli(bernoulliP); } 
    if (distribution.equals("binomial"))
    { return this.nextBinomial(binomialN, binomialP); } 
    if (distribution.equals("uniform")) 
    { return this.nextUniform(
               this.getuniformLower(),this.getuniformUpper());
    } 
    return this.nextDouble();
  } 

  public double mean()
  { if (distribution.equals("normal"))
    { return normalMean; }
    if (distribution.equals("lognormal"))
    { return Math.exp(normalMean + normalVariance/2.0); } 
    if (distribution.equals("bernoulli"))
    { return bernoulliP; } 
    if (distribution.equals("binomial"))
    { return binomialN*binomialP; } 
    if (distribution.equals("uniform")) 
    { return (this.getuniformUpper() + this.getuniformLower()) / 2.0; }
    return 0.5;
  } 
   
  public double variance()
  { if (distribution.equals("normal"))
    { return normalVariance; }
    if (distribution.equals("lognormal"))
    { return Math.exp(2*normalMean + 2*normalVariance) - Math.exp(2*normalMean + normalVariance); } 
    if (distribution.equals("bernoulli"))
    { return bernoulliP*(1 - bernoulliP); } 
    if (distribution.equals("binomial"))
    { return binomialN*binomialP*(1 - binomialP); } 
    if (distribution.equals("uniform")) 
    { return ( this.getuniformUpper() - this.getuniformLower() ) / 12.0; } 
    return 1.0/12;
  } 


  public static ArrayList randomiseSequence(ArrayList sq)
  {
    OclRandom r = null;
    r = OclRandom.newOclRandom();
    r.setAlgorithm("PCG"); 

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

  public static ArrayList randomElements(ArrayList col, int n)
  { ArrayList res = new ArrayList(); 
    int sze = col.size();
    while (res.size() < n) 
    { int ind = (int) Math.floor(Math.random()*sze);
      Object x = col.get(ind);
      res.add(x); 
    } 
    return res;  
  } 

  public static ArrayList randomUniqueElements(ArrayList col, int n)
  { ArrayList res = new ArrayList(); 
    int sze = col.size();
    while (res.size() < n) 
    { int ind = (int) Math.floor(Math.random()*sze);
      Object x = col.get(ind);
      if (res.contains(x)) { } 
      else 
      { res.add(x); } 
    } 
    return res;  
  } 

  public static ArrayList<Object> randomList(int n, OclRandom rd)
  { // Implements: Integer.subrange(1,sh->at(1))->collect( int_0_xx | rd.nextDouble() )

    ArrayList<Object> _results_0 = new ArrayList<Object>();
    for (int _i = 0; _i < n; _i++)
    { _results_0.add(new Double(rd.nextDouble())); }

    return _results_0;
  }

  public static ArrayList<Object> randomMatrix(int n, 
                                      ArrayList<Integer> sh)
  { // Implements: Integer.subrange(1,sh->at(1))->collect( int_1_xx | OclRandom.randomValuesMatrix(sh->tail()) )

    ArrayList<Object> _results_1 = new ArrayList<Object>();
    for (int _i = 0; _i < n; _i++)
    { _results_1.add(
         OclRandom.randomValuesMatrix(Ocl.tail(sh)));
    }

    return _results_1;
  }

  public static ArrayList<Object> randomValuesMatrix(ArrayList<Integer> sh)
  { ArrayList<Object> result;
    OclRandom rd = OclRandom.newOclRandom_PCG();

    if ((sh).size() == 0)
    { return (new ArrayList<Object>()); }

    if ((sh).size() == 1)
    { return OclRandom.randomList(sh.get(0), rd); }

    ArrayList<Object> res = 
                        OclRandom.randomMatrix(sh.get(0), sh);
    return res;
  }

   
  /*
  public static void main(String[] args)
  { ArrayList<Integer> sh = new ArrayList<Integer>(); 
    sh.add(2); sh.add(3); 
    ArrayList mtrx = OclRandom.randomValuesMatrix(sh); 
    System.out.println(mtrx);  
  } */  
}



