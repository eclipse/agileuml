import java.util.Collection; 
import java.util.List; 
import java.util.Vector; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.function.Function;



class MathLib
{
  private static int ix; // internal
  private static int iy; // internal
  private static int iz; // internal
  private static List hexdigit; 

  static 
  { String[] hdigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
    hexdigit = Arrays.asList(hdigits); 
    MathLib.setSeeds(1001,781,913);
  }

  public MathLib()
  {
    this.ix = 0;
    this.iy = 0;
    this.iz = 0;
    this.hexdigit = new Vector();
  }


  public String toString()
  { String _res_ = "(MathLib) ";
    _res_ = _res_ + ix + ",";
    _res_ = _res_ + iy + ",";
    _res_ = _res_ + iz + ",";
    return _res_;
  }

  public static void setix(int ix_x) { ix = ix_x; }

  public static void setiy(int iy_x) { iy = iy_x; }

  public static void setiz(int iz_x) { iz = iz_x; }

  public static void sethexdigit(List hexdigit_x) { hexdigit = hexdigit_x; }

  public static void sethexdigit(int _ind, String hexdigit_x) { hexdigit.set(_ind, hexdigit_x); }

    public static void addhexdigit(String hexdigit_x)
  { hexdigit.add(hexdigit_x); }

  public static void removehexdigit(String hexdigit_x)
  { Vector _removedhexdigit = new Vector();
    _removedhexdigit.add(hexdigit_x);
    hexdigit.removeAll(_removedhexdigit);
  }

    public static int getix() { return ix; }

    public static int getiy() { return iy; }

    public static int getiz() { return iz; }

    public static List gethexdigit() { return hexdigit; }



  public static double pi()
  { return Math.PI; }


  public static double e()
  { return Math.E; }


  public static void setSeeds(int x,int y,int z)
  { MathLib.setix(x);
    MathLib.setiy(y);
    MathLib.setiz(z);
  }

  public static double nrandom()
  { double result;
    MathLib.setix(( MathLib.getix() * 171 ) % 30269);
    MathLib.setiy(( MathLib.getiy() * 172 ) % 30307);
    MathLib.setiz(( MathLib.getiz() * 170 ) % 30323);
    return ( MathLib.getix() / 30269.0 + MathLib.getiy() / 30307.0 + MathLib.getiz() / 30323.0 );
  }


  public static double random()
  { double result = 0; 
    final double r = MathLib.nrandom(); 
    result = ( r - ((int) Math.floor(r)) );
    return result;
  }


  public static long combinatorial(int n,int m)
  { long result = 0;
    if (n < m || m < 0) { return result; } 
   
    if (n - m < m) 
    { result = Ocl.prdint(Ocl.integerSubrange(m + 1,n)) / Ocl.prdint(Ocl.integerSubrange(1,n - m)); }  
    else
      if (n - m >= m) 
      { result = Ocl.prdint(Ocl.integerSubrange(n - m + 1,n)) / Ocl.prdint(Ocl.integerSubrange(1,m)); }
    return result;
  }


  public static long factorial(int x)
  { long result = 0;
 
    if (x < 2) 
    { result = 1; }  
    else
      if (x >= 2) 
      { result = Ocl.prdint(Ocl.integerSubrange(2,x)); }          
    return result;
  }


  public static double asinh(double x)
  { double result = 0;
 
    result = Math.log(( x + Math.sqrt(( x * x + 1 )) ));
    return result;
  }


  public static double acosh(double x)
  { double result = 0;
    if (x < 1) { return result; } 
   
    result = Math.log(( x + Math.sqrt(( x * x - 1 )) ));
    return result;
  }


  public static double atanh(double x)
  { double result = 0;
    if (x == 1) { return result; } 
   
    result = 0.5 * Math.log(( ( 1 + x ) / ( 1 - x ) ));
    return result;
  }


  public static String decimal2bits(long x)
  { String result = "";
 
    if (x == 0) { result = ""; }
    else { result = MathLib.decimal2bits(x / 2) + "" + ( x % 2 ); }
    return result;
  }


  public static String decimal2binary(long x)
  { String result = "";
 
    if (x < 0) { result = "-" + MathLib.decimal2bits(-x); }
    else {     
      if (x == 0) { result = "0"; }
      else { result = MathLib.decimal2bits(x); } 
    }
    return result;
  }


  public static String decimal2oct(long x)
  { String result = "";
 
    if (x == 0) { result = ""; }
    else { result = MathLib.decimal2oct(x / 8) + "" + ( x % 8 ); }
    return result;
  }


  public static String decimal2octal(long x)
  { String result = "";
 
    if (x < 0) 
    { result = "-" + MathLib.decimal2oct(-x); }
    else {     
      if (x == 0) { result = "0"; }
      else { result = MathLib.decimal2oct(x); }
    }
    return result;
  }


  public static String decimal2hx(long x)
  { String result = "";
 
    if (x == 0) { result = ""; }
    else 
    { result = MathLib.decimal2hx(x / 16) + 
        ((String) MathLib.gethexdigit().get((int) ( x % 16 ))); 
    }
    return result;
  }


  public static String decimal2hex(long x)
  { String result = "";
 
    if (x < 0) { result = "-" + MathLib.decimal2hx(-x); }
    else {
      if (x == 0) { result = "0"; }
      else { result = MathLib.decimal2hx(x); }
    }
    return result;
  }

  public static long bytes2integer(ArrayList<Integer> bs) 
  { int res = 0; 
    if (bs.size() == 0) 
    { return 0; } 
    if (bs.size() == 1) 
    { return bs.get(0); } 
    if (bs.size() == 2) 
    { return 256*bs.get(0) + bs.get(1); }
    
    int lowdigit = bs.get(bs.size()-1); 
    ArrayList<Integer> highdigits = Ocl.front(bs); 
    return 256*MathLib.bytes2integer(highdigits) + lowdigit;  
  } 

  public static ArrayList<Integer> integer2bytes(long x)
  { ArrayList<Integer> result = new ArrayList<Integer>();
 
    long y = x/256; 
    int z = (int) (x % 256); 
    if (y == 0)
    { result.add(z); 
      return result; 
    }
    ArrayList<Integer> highbytes = MathLib.integer2bytes(y);
    result.addAll(highbytes); 
    result.add(z);
    return result;
  }

  public static ArrayList<Integer> integer2Nbytes(long x, int n)
  { ArrayList<Integer> res = MathLib.integer2bytes(x); 
    while (res.size() < n) 
    { res.add(0,0); }   
    return res; 
  } 

  public static int bitwiseAnd(int x,int y)
  { return x&y; }

  public static long bitwiseAnd(long x, long y)
  { return x&y; }


  public static int bitwiseOr(int x,int y)
  { return x | y; }


  public static long bitwiseOr(long x, long y)
  { return x | y; }

  public static int bitwiseXor(int x,int y)
  { return x^y; }

  public static long bitwiseXor(long x, long y)
  { return x^y; }

  public static int bitwiseNot(int x)
  { return ~x; } 

  public static long bitwiseNot(long x)
  { return ~x; } 

  public static List toBitSequence(long x)
  { List result;
    long x1 = x;
    List res = new Vector();
    while (x1 > 0) 
    { if (x1 % 2 == 0)
      { res.add(0,false); }
      else 
      { res.add(0,true); }

      x1 = x1 / 2;
    }
    return res;
  }


  public static long modInverse(long n, long p)
  {
    long x = (n % p);
    for (int i = 1; i < p; i++)
    {
      if (((i * x) % p) == 1)
      { return i; }
    }
    return 0;
  }

  public static long modPow(long n, long m, long p)
  { long res = 1;
    long x = (n % p);
    for (int i = 1; i <= m; i++)
    {
      res = ((res * x) % p);
    }
    return res; 
  }

  public static long doubleToLongBits(double d)
  { return Double.doubleToLongBits(d); } 

  public static double longBitsToDouble(long x)
  { return Double.longBitsToDouble(x); } 
  
  public static double discountDiscrete(double amount, double rate, double time)
  { double result = 0;
    result = 0.0;
    if ((rate <= -1 || time < 0))
    { return result; }
  
    result = amount / Math.pow((1 + rate), time);
    return result;
  }

  public static double netPresentValueDiscrete(double rate, ArrayList values)
  { double result = 0;
    result = 0.0;
    if ((rate <= -1))
    { return result; }
  
    int upper = values.size();
    
    for (int i = 0; i < upper; i++)
    { Object val = values.get(i);
      double dval = 0.0;  
      if (val instanceof Double)
      { dval = (double) val; } 
      else if (val instanceof Integer)
      { dval = 1.0*((int) val); } 
      result = result + MathLib.discountDiscrete(dval, 
                                          rate, i); 
    }
    return result;
  }

  public static double presentValueDiscrete(double rate, ArrayList values)
  { double result = 0;
    result = 0.0;
    if ((rate <= -1))
    { return result; }
  
    int upper = values.size();
    
    for (int i = 0; i < upper; i++)
    { Object val = values.get(i);
      double dval = 0.0;  
      if (val instanceof Double)
      { dval = (double) val; } 
      else if (val instanceof Integer)
      { dval = 1.0*((int) val); } 
      result = result + MathLib.discountDiscrete(dval, 
                                          rate, i+1); 
    }
    return result;
  }


  public static double bisectionDiscrete(double r, double rl, double ru, 
                                         ArrayList<Double> values)
  { double result = 0;
    result = 0;
    if ((r <= -1 || rl <= -1 || ru <= -1))
    { return result; }
  
    double v = 0;
    v = MathLib.netPresentValueDiscrete(r,values);
    if (ru - rl < 0.001)
    { return r; } 
    if (v > 0)
    { return MathLib.bisectionDiscrete((ru + r) / 2, r, ru, values); } 
    else if (v < 0)
    { return MathLib.bisectionDiscrete((r + rl) / 2, rl, r, values); }
    return r; 
  }

  public static double irrDiscrete(ArrayList<Double> values)
  { double res = MathLib.bisectionDiscrete(0.1,-0.5,1.0,values); 
    return res; 
  }

 public static double roundN(double x, int n)
 { if (n == 0) 
   { return Math.round(x); } 
   double y = x*Math.pow(10,n); 
   return Math.round(y)/Math.pow(10,n);
 }  

 public static double truncateN(double x, int n)
 { if (n <= 0) 
   { return (int) x; } 
   double y = x*Math.pow(10,n); 
   return ((int) y)/Math.pow(10,n);
 }  

 public static double toFixedPoint(double x, int m, int n)
 { if (m < 0 || n < 0) 
   { return x; } 
   int y = (int) (x*Math.pow(10,n)) ; 
   int z = y % ((int) Math.pow(10, m+n)) ; 
   return z/Math.pow(10.0,n);
 }  

 public static double toFixedPointRound(double x, int m,  int n)
 { if (m < 0 || n < 0) 
   { return x; } 
   int y = (int) Math.round(x*Math.pow(10,n)) ; 
   int z = y % ((int) Math.pow(10, m+n)) ; 
   return z/Math.pow(10.0,n);
 }   
  
  public static double mean(ArrayList sq)
  { int sze = sq.size(); 
    if (sze == 0) { return Double.NaN; }
    double total = 0.0;  
    for (int i = 0; i < sze; i++) 
    { Object x = sq.get(i);
      if (x instanceof Double) { total = total + (double) x; }
      else 
      if (x instanceof Integer) { total = total + (int) x; }
      else 
      if (x instanceof Long) { total = total + (long) x; }
    }  
    return total/sze; 
  } 

  public static double median(ArrayList sq)
  { int sze = sq.size();  
    if (sze == 0) { return Double.NaN; } 
    ArrayList s1 = Ocl.sort(sq);
 
    if (sze % 2 == 1)
    { Object x = s1.get((1 + sze)/2 - 1); 
      if (x instanceof Double) 
      { return (double) x; }
      else if (x instanceof Integer)
      { return 1.0*((int) x); } 
      else if (x instanceof Long)
      { return 1.0*((long) x); } 
      return Double.NaN; 
    } 

    Object x1 = s1.get(sze/2 - 1); 
    Object x2 = s1.get(sze/2); 
    double d1 = 0.0; 
    double d2 = 0.0; 

    if (x1 instanceof Double)
    { d1 = (double) x1; } 
    else if (x1 instanceof Integer) 
    { d1 = 1.0*((int) x1); } 

    if (x2 instanceof Double)
    { d2 = (double) x2; } 
    else if (x2 instanceof Integer) 
    { d2 = 1.0*((int) x2); } 
    return ( d1 + d2 )/2.0;
  }  

  public static double variance(ArrayList sq)
  { int sze = sq.size(); 
    if (sze <= 1) { return 0; }
    double m = MathLib.mean(sq); 
    double total = 0.0;  
    for (int i = 0; i < sze; i++) 
    { Object x = sq.get(i);
      if (x instanceof Double) 
      { total = total + ((double) x - m)*((double) x - m); }
      else if (x instanceof Long) 
      { total = total + ((long) x - m)*((long) x - m); }
      else if (x instanceof Integer) 
      { total = total + ((int) x - m)*((int) x - m); }
    }  
    return total/sze; 
  } 

  public static double standardDeviation(ArrayList sq)
  { int sze = sq.size(); 
    if (sze <= 1) { return 0; }
    double total = MathLib.variance(sq);  
    return Math.sqrt(total); 
  } 

  public static long lcm(long x, long y) 
  { if (x == 0 && y == 0) 
    { return 0; } 
    long g = Ocl.gcd(x,y); 
    return (x*y)/g; 
  } 

  public static double bisectionAsc(double r,double rl,double ru, Function<Double, Double> f,double tol)
  { double result = 0.0;
  
    final double v = (f).apply(r);
 
    if (v < tol && v > -tol) {
      result = r;
    } else {
      if (v > 0) {
        result = MathLib.bisectionAsc(( rl + r ) / 2,rl,r,f,tol);
      } else if (v < 0) {
        result = MathLib.bisectionAsc(( r + ru ) / 2,r,ru,f,tol);
      }   
    }      

    return result;
  }

  public static boolean isIntegerOverflow(double x, int m)
  { int y = (int) x; 
    if (y == 0)
    { return (m < 1); }
    if (y > 0)
    { return ((int) Math.log10(y)) + 1 > m; }  
    return ((int) Math.log10(-y)) + 1 > m;
  }  

  /* 
  public static ArrayList<Double> collect_4(Collection<Integer> _l,ArrayList<Double> s,ArrayList<ArrayList<Double>> m,int i)
  { // Implements: Integer.subrange(1,m.size)->collect( k | s[k] * ( m[k]->at(i) ) )
    ArrayList<Double> _results_4 = new ArrayList<Double>();
    for (Integer _i : _l)
    { int k = ((Integer) _i).intValue();
      _results_4.add(new Double(((Double) s.get(k - 1)).doubleValue() * ((Double) m.get(k - 1).get(i - 1)).doubleValue()));
    }
    return _results_4;
  }

    public static ArrayList<Double> collect_5(Collection<Integer> _l,ArrayList<ArrayList<Double>> m,ArrayList<Double> s)
  { // Implements: Integer.subrange(1,s.size)->collect( i | Integer.Sum(1,m.size,k,s[k] * ( m[k]->at(i) )) )

    ArrayList<Double> _results_5 = new ArrayList<Double>();
    for (Integer _i : _l)
    { int i = ((Integer) _i).intValue();
     _results_5.add(new Double(Ocl.sumdouble(MathLib.collect_4(Ocl.integerSubrange(1,m.size()),s,m,i))));
    }
    return _results_5;
  }

  public static ArrayList<ArrayList<Double>> collect_6(Collection<ArrayList<Double>> _l,ArrayList<ArrayList<Double>> m2)
  { // Implements: m1->collect( row | MathLib.rowMult(row,m2) )
   ArrayList<ArrayList<Double>> _results_6 = new ArrayList<ArrayList<Double>>();
    for (ArrayList<Double> _i : _l)
    { ArrayList<Double> row = (ArrayList<Double>) _i;
     _results_6.add(MathLib.rowMult(row,m2));
    }
    return _results_6;
  }

  public static ArrayList<Double> rowMult(ArrayList<Double> s,ArrayList<ArrayList<Double>> m)
  { ArrayList<Double> result = new ArrayList<Double>();
  
    result = MathLib.collect_5(Ocl.integerSubrange(1,s.size()),m,s);
  
    return result;
  }


    public static ArrayList<ArrayList<Double>> matrixMultiplication(ArrayList<ArrayList<Double>> m1,ArrayList<ArrayList<Double>> m2)
  { ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
  
    result = MathLib.collect_6(m1,m2);
  
    return result;
  }  */ 

  public static ArrayList<Double> rowMult(ArrayList<Double> s, ArrayList<ArrayList<Double>> m)
  {
    ArrayList<Double> result = new ArrayList<Double>();
    result = Ocl.collectSequence(Ocl.integerSubrange(1,s.size()),(i)->{return (double) Ocl.sum(Ocl.collectSequence(Ocl.integerSubrange(1,m.size()),(k)->{ return ((double) (s).get(k - 1)) * (((double) ((ArrayList<Double>) (m).get(k - 1)).get(i - 1))); }));});
    return result;
  }


  public static ArrayList<ArrayList<Double>> matrixMultiplication(ArrayList<ArrayList<Double>> m1, ArrayList<ArrayList<Double>> m2)
  {
    ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
    result = Ocl.collectSequence(m1,(row)->{return MathLib.rowMult(row, m2);});
    return result;
  }

 
  public static void main(String[] args)
  { ArrayList<Double> row1 = new ArrayList<Double>(); 
    ArrayList<Double> row2 = new ArrayList<Double>(); 
    row1.add(1.0); row1.add(3.0); 
    row2.add(7.0); row2.add(5.0); 
    ArrayList<ArrayList<Double>> m1 = new ArrayList<ArrayList<Double>>(); 
    m1.add(row1); m1.add(row2); 

    ArrayList<Double> row3 = new ArrayList<Double>(); 
    ArrayList<Double> row4 = new ArrayList<Double>(); 
    row3.add(6.0); row3.add(8.0); 
    row4.add(4.0); row4.add(2.0); 
    ArrayList<ArrayList<Double>> m2 = new    ArrayList<ArrayList<Double>>(); 
    m2.add(row3); m2.add(row4); 

    System.out.println(MathLib.matrixMultiplication(m1,m2));
  } 


}

