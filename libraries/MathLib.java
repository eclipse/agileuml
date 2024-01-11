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
  public static double defaultTolerance = 0.001; 

  static 
  { String[] hdigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
    hexdigit = Arrays.asList(hdigits); 
    MathLib.setSeeds(1001,781,913);
    MathLib.defaultTolerance = 0.001; 
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

  public static void sethexdigit(int _ind, String hexdigit_x)   
  { hexdigit.set(_ind, hexdigit_x); }

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

  public static double piValue()
  {
    return Math.PI;
  }

  public static double e()
  { return Math.E; }

  public static double eValue()
  { return Math.E; }

  public static void setSeeds(int x,int y,int z)
  { MathLib.setix(x);
    MathLib.setiy(y);
    MathLib.setiz(z);
  }

  public static void setSeed(int r)  
  { MathLib.setSeeds((r % 30269), (r % 30307), (r % 30323)); }

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
    else if (x1 instanceof Long)
    { d1 = 1.0*((long) x1); } 

    if (x2 instanceof Double)
    { d2 = (double) x2; } 
    else if (x2 instanceof Integer) 
    { d2 = 1.0*((int) x2); } 
    else if (x2 instanceof Long)
    { d2 = 1.0*((long) x2); } 

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

  public static int leftTruncateTo(int x, int m)
  { return x % ((int) Math.pow(10,m)); }  

  public static double leftTruncateTo(double x, int m)
  { int integerPart = (int) x;
    double fractionPart = x - integerPart;  
    return (integerPart % ((int) Math.pow(10,m))) + fractionPart; 
  }  

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

  public static Function<Double,Double> differential(Function<Double,Double> f)
  {
    Function<Double,Double> result = (_x10) -> { return 0.0; };

    double tol = MathLib.defaultTolerance;
    double multiplier = 1.0/(2*tol); 

    result = (x) -> { return (multiplier*((f).apply(x + tol) - (f).apply(x - tol))); };
    return result;
  }

  public static double definiteIntegral(double st, double en, Function<Double,Double> f)
  {
    double tol = MathLib.defaultTolerance;
    double area = 0.0;
    double delta = tol * (en - st);
    double cum = st;
    while (cum < en)
    {
      double next = cum + delta;
      area = area + delta * ((f).apply(cum) + (f).apply(next)) / 2.0;
      cum = next;
    }
    return area;
  }

  public static Function<Double,Double> indefiniteIntegral(Function<Double,Double> f)
  {
    Function<Double,Double> result = (_x8) -> { return 0.0; };
    result = (x) -> { return MathLib.definiteIntegral(0, x, f); };
    return result;
  }

  public static void main(String[] args)
  { System.out.println(MathLib.leftTruncateTo(1024,3)); 
    System.out.println(MathLib.leftTruncateTo(1024.55,3)); 
  }  

}

