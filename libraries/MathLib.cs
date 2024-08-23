
    class MathLib
    {
        private static int ix; // internal
        private static int iy; // internal
        private static int iz; // internal
        private static double defaultTolerance = 0.001; // internal

        private static ArrayList hexdigit; // internal

        public MathLib()
        {
            ix = 0;
            iy = 0;
            iz = 0;
            hexdigit = (new ArrayList());
        }

        static MathLib()
        { initialiseMathLib();  }

        public override string ToString()
        {
            string _res_ = "(MathLib) ";
            _res_ = _res_ + ix + ",";
            _res_ = _res_ + iy + ",";
            _res_ = _res_ + iz + ",";
            _res_ = _res_ + hexdigit;
            return _res_;
        }

        public static void setix(int ix_x) { ix = ix_x; }

        public void localSetix(int ix_x) { }

        public static void setiy(int iy_x) { iy = iy_x; }

        public void localSetiy(int iy_x) { }

        public static void setiz(int iz_x) { iz = iz_x; }

        public void localSetiz(int iz_x) { }

        public static void sethexdigit(ArrayList hexdigit_x) { hexdigit = hexdigit_x; }

        public void localSethexdigit(ArrayList hexdigit_x) { }

        public static void sethexdigit(int _ind, string hexdigit_x) { hexdigit[_ind] = hexdigit_x; }

        public void localSethexdigit(int _ind, string hexdigit_x) { }

        public static void addhexdigit(string hexdigit_x)
        { hexdigit.Add(hexdigit_x); }

        public static void removehexdigit(string hexdigit_x)
        { hexdigit = SystemTypes.subtract(hexdigit, hexdigit_x); }

        public static int getix() { return ix; }

        public static ArrayList getAllix(ArrayList mathlib_s)
        {
            ArrayList result = new ArrayList();
            if (mathlib_s.Count > 0)
            { result.Add(MathLib.ix); }
            return result;
        }

        public static ArrayList getAllOrderedix(ArrayList mathlib_s)
        {
            ArrayList result = new ArrayList();
            for (int i = 0; i < mathlib_s.Count; i++)
            {
                MathLib mathlibx = (MathLib)mathlib_s[i];
                result.Add(MathLib.ix);
            }
            return result;
        }

        public static int getiy() { return iy; }

        public static ArrayList getAlliy(ArrayList mathlib_s)
        {
            ArrayList result = new ArrayList();
            if (mathlib_s.Count > 0)
            { result.Add(MathLib.iy); }
            return result;
        }

        public static ArrayList getAllOrderediy(ArrayList mathlib_s)
        {
            ArrayList result = new ArrayList();
            for (int i = 0; i < mathlib_s.Count; i++)
            {
                MathLib mathlibx = (MathLib)mathlib_s[i];
                result.Add(MathLib.iy);
            }
            return result;
        }

        public static int getiz() { return iz; }

        public static ArrayList getAlliz(ArrayList mathlib_s)
        {
            ArrayList result = new ArrayList();
            if (mathlib_s.Count > 0)
            { result.Add(MathLib.iz); }
            return result;
        }

        public static ArrayList getAllOrderediz(ArrayList mathlib_s)
        {
            ArrayList result = new ArrayList();
            for (int i = 0; i < mathlib_s.Count; i++)
            {
                MathLib mathlibx = (MathLib)mathlib_s[i];
                result.Add(MathLib.iz);
            }
            return result;
        }

        public static ArrayList gethexdigit() { return hexdigit; }


        public static void initialiseMathLib()
        {
            MathLib.sethexdigit(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), "0"), "1"), "2"), "3"), "4"), "5"), "6"), "7"), "8"), "9"), "A"), "B"), "C"), "D"), "E"), "F"));

            MathLib.setSeeds(1001, 781, 913);
            MathLib.defaultTolerance = 0.001;
        }

        public static double pi()
        {
            double result = 3.14159265;
            return result;
        }


        public static double eValue()
        {
            double result = Math.Exp(1);
            return result;
        }

        public static double piValue()
        {
            double result = 3.14159265;
            return result;
        }


        public static double e()
        {
            double result = Math.Exp(1);
            return result;
        }


        public static void setSeeds(int x, int y, int z)
        {
            MathLib.setix(x);
            MathLib.setiy(y);
            MathLib.setiz(z);
        }

        public static void setSeed(int r)  
        { MathLib.setSeeds((r % 30269), (r % 30307), (r % 30323)); } 

        public static double nrandom()
        {
            MathLib.setix((MathLib.getix() * 171) % 30269);
            MathLib.setiy((MathLib.getiy() * 172) % 30307);
            MathLib.setiz((MathLib.getiz() * 170) % 30323);
            return (MathLib.getix() / 30269.0 + MathLib.getiy() / 30307.0 + MathLib.getiz() / 30323.0);
        }


        public static double random()
        {
            double result = 0.0;

            double r = MathLib.nrandom();
            result = (r - ((int)Math.Floor(r)));
            return result;
        }


        public static long combinatorial(int n, int m)
        {
            long result = 0;
            if (n < m || m < 0) { return result; }

            if (n - m < m)
            {
                result = SystemTypes.prdint(SystemTypes.integerSubrange(m + 1, n)) / 
                             SystemTypes.prdint(SystemTypes.integerSubrange(1, n - m));
            }
            else
              if (n - m >= m)
            {
                result = SystemTypes.prdint(SystemTypes.integerSubrange(n - m + 1, n)) / 
                             SystemTypes.prdint(SystemTypes.integerSubrange(1, m));
            }
            return result;
        }


        public static long factorial(int x)
        {
            long result = 0;

            if (x < 2)
            {
                result = 1;
            }
            else
              if (x >= 2)
            {
                result = SystemTypes.prdint(SystemTypes.integerSubrange(2, x));
            }
            return result;
        }


        public static double asinh(double x)
        {
            double result = Math.Log((x + Math.Sqrt((x * x + 1))));
            return result;
        }


        public static double acosh(double x)
        {
            double result = 0.0;
            if (x < 1) { return result; }

            result = Math.Log((x + Math.Sqrt((x * x - 1))));
            return result;
        }


        public static double atanh(double x)
        {
            double result = 0.0;
            if (x == 1) { return result; }

            result = 0.5 * Math.Log(((1 + x) / (1 - x)));
            return result;
        }


        public static string decimal2bits(long x)
        {
            string result = "";

            if (x == 0) { result = ""; }
            else 
            { result = MathLib.decimal2bits(x / 2) + "" + (x % 2); }
            return result;
        }


        public static string decimal2binary(long x)
        {
            string result = "";

            if (x < 0) 
            { result = "-" + MathLib.decimal2bits(-x); }
            else
            {
                if (x == 0) { result = "0"; }
                else { result = MathLib.decimal2bits(x); }
            }
            return result;
        }


        public static string decimal2oct(long x)
        {
            string result = "";

            if (x == 0) { result = ""; }
            else 
            { result = MathLib.decimal2oct(x / 8) + "" + (x % 8); }
            return result;
        }


        public static string decimal2octal(long x)
        {
            string result = "";

            if (x < 0) 
            { result = "-" + MathLib.decimal2oct(-x); }
            else
            {
                if (x == 0) { result = "0"; }
                else { result = MathLib.decimal2oct(x); }
            }
            return result;
        }


        public static string decimal2hx(long x)
        {
            string result = "";

            if (x == 0) 
            { result = ""; }
            else 
            { result = MathLib.decimal2hx(x / 16) + ("" + ((string)MathLib.gethexdigit()[((int)(x % 16))])); }
            return result;
        }


        public static string decimal2hex(long x)
        {
            string result = "";

            if (x < 0) 
            { result = "-" + MathLib.decimal2hx(-x); }
            else
            {
                if (x == 0) { result = "0"; }
                else 
                { result = MathLib.decimal2hx(x); }
            }
            return result;
        }

        public static long bytes2integer(ArrayList bs)
        {
            if (bs.Count == 0)
            { return 0; }
            if (bs.Count == 1)
            { return (int) bs[0]; }
            if (bs.Count == 2)
            { return (256 * ((int) bs[0])) + (int) bs[1]; }

            int lowdigit = (int) bs[bs.Count - 1];
            ArrayList highdigits = SystemTypes.front(bs);
            return 256 * MathLib.bytes2integer(highdigits) + lowdigit;
        }

        public static ArrayList integer2bytes(long x)
        {
            ArrayList result = new ArrayList();

            long y = x / 256;
            int z = (int)(x % 256);
            if (y == 0)
            {
                result.Add(z);
                return result;
            }
            ArrayList highbytes = MathLib.integer2bytes(y);
            result.AddRange(highbytes);
            result.Add(z);
            return result;
        }

        public static ArrayList integer2Nbytes(long x, int n)
        {
            ArrayList res = MathLib.integer2bytes(x);

            while (res.Count < n)
            { res.Insert(0, 0); }
            return res;
        }

        public static int bitwiseRotateLeft(int x, int n)
        {
            return (int) BitOperations.RotateLeft((uint) x, n);
        }

        public static int bitwiseRotateRight(int x, int n)
        {
            return (int) BitOperations.RotateRight((uint) x, n);
        }
		
        public static int bitwiseAnd(int x, int y)
        { return x & y; }

        public static long bitwiseAnd(long x, long y)
        { return x & y; }


        public static int bitwiseOr(int x, int y)
        { return x | y; }

       public static long bitwiseOr(long x, long y)
        {
            return x | y;
        }


        public static int bitwiseXor(int x, int y)
        { return x^y; }

        public static long bitwiseXor(long x, long y)
        {
            return x ^ y;
        }

        public static int bitwiseNot(int x)
        { return ~x; }

        public static long bitwiseNot(long x)
        { return ~x; }


        public static ArrayList toBitSequence(long x)
        {
            long x1;
            x1 = x;
            ArrayList res;
            res = (new ArrayList());
            while (x1 > 0)
            {
                if (x1 % 2 == 0)
                { res = SystemTypes.concatenate(SystemTypes.makeSet(false), res); }
                else { res = SystemTypes.concatenate(SystemTypes.makeSet(true), res); }

                x1 = x1 / 2;
            }
            return res;
        }


     
        public static long modInverse(long n, long p)
        {
            if (p <= 0) { return 0; }

            long x = (n % p);
            for (int i = 1; i < p; i++)
            {
                if (((i * x) % p) == 1)
                { return i; }
            }
            return 0;
        }


        public static long modPow(long n, long m, long p)
        { if (p <= 0) { return 0; }
          long res = 1;
          long x = (n % p);
          for (int i = 1; i <= m; i++)
          {
            res = ((res * x) % p);
          }
          return res;
        }

        public static long doubleToLongBits(double d)
        { return BitConverter.DoubleToInt64Bits(d); }

        public static double longBitsToDouble(long x)
        { return BitConverter.Int64BitsToDouble(x); }

       public static double roundN(double x, int n)
        {
            if (n < 0)
            { return Math.Round(x); }
            double y = x * Math.Pow(10, n);
            return Math.Round(y) / Math.Pow(10, n);
        }

       public static double truncateN(double x, int n)
        {
            if (n < 0)
            { return (int) x; }
            double y = x * Math.Pow(10, n);
            return ((int) y) / Math.Pow(10, n);
        }

        public static double toFixedPoint(double x, int m, int n)
        {
            if (m < 0 || n < 0)
            { return x; }
            int y = (int) (x * Math.Pow(10, n));
            int z = y % ((int) Math.Pow(10, m + n));
            return z / Math.Pow(10.0, n);
        }

        public static double toFixedPointRound(double x, int m, int n)
        {
            if (m < 0 || n < 0)
            { return x; }
            int y = (int)Math.Round(x * Math.Pow(10, n));
            int z = y % ((int)Math.Pow(10, m + n));
            return z / Math.Pow(10.0, n);
        }

        public static double leftTruncateTo(double x, int m)
        {
            int integerPart = (int)x;
            double fractionPart = x - integerPart;
            return (integerPart % ((int)Math.Pow(10, m))) + fractionPart;
        }

        public static bool isIntegerOverflow(double x, int m)
        {
            bool result = false;

            int y = (int) x;
            if (y > 0)
            {
                result = ((int) Math.Log10(y)) + 1 > m;
            }
            else
            {
                if (y < 0)
                {
                    result = ((int) Math.Log10(-y)) + 1 > m;
                }
                else
                {
                    result = (bool)((m < 1));
                }
            }
            return result;
        }


        public static double mean(ArrayList sq)
        {
            double result = 0.0;
            if ((sq).Count <= 0) { return result; }

            result = SystemTypes.sumdouble(sq) / (sq).Count;
            return result;
        }

        public static double median(ArrayList sq)
        {
            double result = 0.0;
            if ((sq).Count <= 0) { return result; }

            ArrayList s1 = SystemTypes.sort(sq);
            int sze = (sq).Count;
            if (sze % 2 == 1)
            {
                result = double.Parse("" + s1[(1 + sze) / 2 - 1]);
            }
            else
             if (sze % 2 == 0)
            {
                result = (double.Parse("" + s1[sze / 2 - 1]) + double.Parse("" + s1[1 + (sze / 2) - 1])) / 2.0;
            }
            return result;
        }

        private static ArrayList collect_3(ArrayList _l, double m)
        { // Implements: sq->collect( x | ( x - m )->sqr() )
            ArrayList _results_3 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                double x = double.Parse("" + _l[_icollect]);
                _results_3.Add((((x - m)) * ((x - m))));
            }
            return _results_3;
        }

        public static double variance(ArrayList sq)
        {
            double result = 0.0;
            if ((sq).Count <= 0) { return result; }

            double m = MathLib.mean(sq);
            result = SystemTypes.sumdouble(MathLib.collect_3(sq, m)) / (sq).Count;
            return result;
        }

        public static double standardDeviation(ArrayList sq)
        {
            double result = 0.0;
            if ((sq).Count <= 0) { return result; }

            double m = MathLib.variance(sq);
            result = Math.Sqrt(m);
            return result;
        }


        public static int lcm(int x, int y)
        {
            int result = 0;

            if (x == 0 && y == 0)
            {
                result = 0;
            }
            else
            {
                result = (int) ((x * y) / SystemTypes.gcd(x, y));
            }
            return result;
        }

        public static double bisectionAsc(double r, double rl, double ru, Func<double, double> f, double tol)
        {
            double result = 0.0;

            double v = f.Invoke(r);
            if (v < tol && v > -tol)
            {
                result = r;
            }
            else
            {
                if (v > 0)
                {
                    result = MathLib.bisectionAsc((rl + r) / 2, rl, r, f, tol);
                }
                else if (v < 0)
                {
                    result = MathLib.bisectionAsc((r + ru) / 2, r, ru, f, tol);
                }
            }
            return result;
        }

        private static ArrayList collect_4(ArrayList _l, ArrayList s, ArrayList m, int i)
        { // Implements: Integer.subrange(1,m.size)->collect( k | s[k] * ( m[k]->at(i) ) )
            ArrayList _results_4 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int k = (int) _l[_icollect];
                _results_4.Add(double.Parse("" + s[k - 1]) * double.Parse("" + ((ArrayList) m[k - 1])[i - 1]));
            }
            return _results_4;
        }

        private static ArrayList collect_5(ArrayList _l, ArrayList m, ArrayList s)
        { // Implements: Integer.subrange(1,s.size)->collect( i | Integer.Sum(1,m.size,k,s[k] * ( m[k]->at(i) )) )
            ArrayList _results_5 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int i = (int)_l[_icollect];
                _results_5.Add(SystemTypes.sumdouble(MathLib.collect_4(SystemTypes.integerSubrange(1, m.Count), s, m, i)));
            }
            return _results_5;
        }

        public static ArrayList rowMult(ArrayList s, ArrayList m)
        {
            ArrayList result = new ArrayList();

            result = MathLib.collect_5(SystemTypes.integerSubrange(1, s.Count), m, s);
            return result;
        }

        private static ArrayList collect_6(ArrayList _l, ArrayList m2)
        { // Implements: m1->collect( row | MathLib.rowMult(row,m2) )
            ArrayList _results_6 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                ArrayList row = (ArrayList) _l[_icollect];
                _results_6.Add(MathLib.rowMult(row, m2));
            }
            return _results_6;
        }

        public static ArrayList matrixMultiplication(ArrayList m1, ArrayList m2)
        {
            ArrayList result = new ArrayList();

            result = MathLib.collect_6(m1, m2);
            return result;
        }

       public static Func<double,double> differential(Func<double,double> f)
       { Func<double,double> result = null;
         double multiplier = 1.0/(2.0*MathLib.defaultTolerance); 

         result = (Func<double,double>) (x => (( multiplier * ( f.Invoke(x + MathLib.defaultTolerance) - f.Invoke(x - MathLib.defaultTolerance) ) ))); 
         return result;
       }

        public static double definiteIntegral(double st, double en, Func<double, double> f)
        {
            double tol = MathLib.defaultTolerance;
            double area = 0.0;
            double d = tol * (en - st);
            double cum = st;

            while (cum < en)
            {
                double next = cum + d;

                area = area + d * (f.Invoke(cum) + f.Invoke(next)) / 2.0;
                cum = next;
            }
            return area;
        }

        public static Func<double, double> indefiniteIntegral(Func<double, double> f)
        {
            Func<double, double> result = null;

            result = (Func<double, double>)(x => (MathLib.definiteIntegral(0, x, f)));
            return result;
        }

  }

class FinanceLib
{ public FinanceLib() { }

  public static double discountDiscrete(double amount, double rate, double time)
  {
    double result = 0.0;
    if (rate <= -1 || time < 0)
    { return result; }

    result = amount / Math.Pow((1 + rate), time);
    return result;
  }

  public static double netPresentValueDiscrete(double rate, ArrayList values)
  {
    double result = 0.0;
    if (rate <= -1)
    { return result; }

    int upper = values.Count; 
    int i = 0;
    for (; i < upper; i++)
    { result = result + MathLib.discountDiscrete((double) values[i], rate, i); }
    return result;
  }

  public static double presentValueDiscrete(double rate, ArrayList values)
  {
    double result = 0.0;
    if (rate <= -1)
    { return result; }

    int upper = values.Count;

    for (int i = 0; i < upper; i++)
    {
      Object val = values[i];
      double dval = 0.0;
      if (val is double)
      { dval = (double) val; }
      else if (val is int)
      { dval = 1.0 * ((int)val); }
      result = result + MathLib.discountDiscrete(dval,
                                                rate, i + 1);
    }
    return result;
  }

  public static double bisectionDiscrete(double r, double rl, double ru, ArrayList values)
  {
    double result = 0.0;
    if (r <= -1 || rl <= -1 || ru <= -1)
    { return result; }

    double v = FinanceLib.netPresentValueDiscrete(r, values);
    if (ru - rl < MathLib.getdefaultTolerance())
    { return r; }
    if (v > 0)
    { return FinanceLib.bisectionDiscrete((ru + r) / 2, r, ru, values); }
    else if (v < 0)
    { return FinanceLib.bisectionDiscrete((r + rl) / 2, rl, r, values); }
    return r;
  }

  public static double irrDiscrete(ArrayList values)
  {
    double res = FinanceLib.bisectionDiscrete(0.1, -0.5, 1.0, values);
    return res;
  }

  public static ArrayList straddleDates(OclDate d1, OclDate d2, int period)
  {
    OclDate cd = d1;

    while (cd.compareToYMD(d2) <= 0)
    {
      cd = cd.addMonthYMD(period);
    }
          
    ArrayList res = new ArrayList();
    OclDate prevd = cd.subtractMonthYMD(period);
    res.Add(prevd);
    res.Add(cd);
    return res;
  }

  public static int numberOfPeriods(OclDate settle, OclDate matur, int period)
  {
    int result = 0;

    double monthsToMaturity = OclDate.differenceMonths(matur, settle) * 1.0);
    result = (int) Math.Ceiling((monthsToMaturity / period));
    return result;
  }

  public static ArrayList sequenceOfPeriods(OclDate sett, OclDate mat, int period)
  {
    ArrayList result = new ArrayList();

    int numPeriods = FinanceLib.numberOfPeriods(sett, mat, period); 
    result = SystemTypes.integerSubrange(1, numPeriods);
    return result;
  }


  public static ArrayList couponDates(OclDate matur, int period, int numPeriods)
  { ArrayList cpdates = new ArrayList(); 
    cpdates.Add(matur);

    OclDate cpdate = matur;

    for (int _i10 = 0; _i10 < numPeriods - 1; _i10++)
    {
      int mo = cpdate.getMonth() - period;

      int prevMonth = mo;
      int prevYear = cpdate.getYear();
      int prevDay = cpdate.getDate();

      if (mo <= 0)
      {
        prevMonth = 12 + mo;
        prevYear = cpdate.getYear() - 1;
      }

      cpdate = OclDate.newOclDate_YMD(prevYear, prevMonth, prevDay);
      cpdates.Add(cpdate);
    }

    cpdates = SystemTypes.reverse(cpdates);
    return cpdates;
  }

  public static int days360(OclDate d1, OclDate d2, string num, OclDate mat)
  { /* num is the dayCount convention */
            
    int dd1 = d1.getDate();
    int dd2 = d2.getDate();
    int mm1 = d1.getMonth();
    int mm2 = d2.getMonth();
    int yy1 = d1.getYear();
    int yy2 = d2.getYear();
    if (num.Equals("30/360"))
    {
      return 360*(yy2 - yy1) + 30*(mm2 - mm1) + (dd2 - dd1);
    }
    else if (num.Equals("30/360B"))
    {
      dd1 = Math.Min(dd1, 30);
      if (dd1 > 29)
      { dd2 = Math.Min(dd2, 30); }
      return 360*(yy2 - yy1) + 30*(mm2 - mm1) + (dd2 - dd1);
    }
    else if (num.Equals("30/360US"))
    {
      if (mm1 == 2 && (dd1 == 28 || dd1 == 29) && mm2 == 2 && (dd2 == 28 || dd2 == 29))
      {
        dd2 = 30;
      }
      if (mm1 == 2 && (dd1 == 28 || dd1 == 29))
      { dd1 = 30; }
      if (dd2 == 31 && (dd1 == 30 || dd1 == 31))
      { dd2 = 30; }
      if (dd1 == 31) { dd1 = 30; }
      return 360*(yy2 - yy1) + 30*(mm2 - mm1) + (dd2 - dd1);
    }
    else if (num.Equals("30E/360"))
    {
      if (dd1 == 31) { dd1 = 30; }
      if (dd2 == 31) { dd2 = 30; }
      return 360*(yy2 - yy1) + 30*(mm2 - mm1) + (dd2 - dd1);
    }
    else if (num.Equals("30E/360ISDA"))
    {
      if (d1.isEndOfMonth()) { dd1 = 30; }
      if (!(d2 == mat && mm2 == 2) && d2.isEndOfMonth()) { dd2 = 30; }
      return 360*(yy2 - yy1) + 30*(mm2 - mm1) + (dd2 - dd1);
    }
    else
    { return 360*(yy2 - yy1) + 30*(mm2 - mm1) + (dd2 - dd1); }
  }


  public static ArrayList numberOfMonths(OclDate pd, OclDate settle, double cd, string dayCount, OclDate matur)
  { /* Returns sequence of two doubles */ 
    double sv = 0.0;
    ArrayList result = new ArrayList();

    if (dayCount.Equals("Actual/360") || 
        dayCount.Equals("Actual/365F") ||
        dayCount.Equals("Actual/ActualICMA") ||
        dayCount.Equals("Actual/364") || 
        dayCount.Equals("Actual/ActualISDA"))
    {
      int daysBetween = OclDate.daysBetweenDates(pd, settle);
      sv = (cd - daysBetween) / cd;
      result.Add(sv); 
      result.Add(cd - daysBetween);
      return result;
    }
    else
    {
      int daysBetween360 = FinanceLib.days360(pd, settle, dayCount, matur);
      sv = (cd - daysBetween360) / cd;
      result.Add(sv); 
      result.Add(cd - daysBetween360);
      return result;
    }
  }


  public static ArrayList calculateCouponPayments(ArrayList paymentDates, double annualCouponRate, string dayCountC, int freq)
  { /* Result is sequence of two double sequences */ 

    ArrayList result = new ArrayList();
    ArrayList coupon_payments = new ArrayList();
    ArrayList dates_payments = new ArrayList();
    double cum_days = 0.0;
    double days = 0.0;

    for (int i = 1; i < paymentDates.Count; i++)
    {
      OclDate start_date_str = (OclDate) paymentDates[i - 1];
      OclDate end_date_str = (OclDate) paymentDates[i];

      if (dayCountC.Equals("30/360") || 
          dayCountC.Equals("30/360B") || 
          dayCountC.Equals("30/360US") ||
          dayCountC.Equals("30E/360") || 
          dayCountC.Equals("30E/360ISDA") || 
          dayCountC.Equals("Actual/360"))
      {
        days = FinanceLib.days360(start_date_str, end_date_str,
                     dayCountC, (OclDate)paymentDates[paymentDates.Count - 1]);
       }
       else if (dayCountC.Equals("Actual/365F"))
       { days = 365.0 / freq; }
       else if (dayCountC.Equals("Actual/364"))
       { days = 364.0 / freq; }
       else /* actual/actual calculations */
       { days = OclDate.daysBetweenDates(start_date_str, end_date_str); }

       double coupon_payment = annualCouponRate / freq;

       coupon_payments.Add(coupon_payment);
       cum_days += days;
       dates_payments.Add(cum_days);
     }
          
     result.Add(coupon_payments);
     result.Add(dates_payments); 
     return result;
   }


   public static ArrayList bondCashFlows(OclDate settle, OclDate matur, double coupon, string dayCount, int freq)
   {
      ArrayList results = new ArrayList();

      int period = (int)(12 / freq);
      int np = FinanceLib.numberOfPeriods(settle, matur, period);
      ArrayList snp = FinanceLib.sequenceOfPeriods(settle, matur, period);
      ArrayList cd = FinanceLib.couponDates(matur, period, np);

      OclDate pm = ((OclDate) cd[0]).subtractMonthYMD(period);
      ArrayList cdn = new ArrayList();
      cdn.Add(pm); cdn.AddRange(cd);
      ArrayList coupPayments = 
         FinanceLib.calculateCouponPayments(
                    cdn, coupon, dayCount, freq);
      ArrayList cumd = (ArrayList) coupPayments[1];
      ArrayList cp = (ArrayList) coupPayments[0];
      ArrayList nm = FinanceLib.numberOfMonths(pm, settle,
                                   (double) cumd[0], dayCount, matur); 


      if (settle.compareToYMD(pm) == 0) {
         results.Add(cp); 
         results.Add(cd); 
         results.Add(snp); 
         results.Add(cumd);  
      }
      else {
         ArrayList newsnp = new ArrayList();
         foreach (int x in snp) 
         { newsnp.Add(x - ((int) snp[0] - (double) nm[0])); }
         ArrayList newcumd = new ArrayList();
         foreach (double x in cumd) 
         { newcumd.Add(x - ((double) cumd[0] - (double) nm[1])); }
         results.Add(cp); 
         results.Add(cd); 
         results.Add(newsnp); 
         results.Add(newcumd);
       }
       return results;
     }


       public static double bondPrice(double yld, OclDate settle, OclDate matur, double coup, string dayCount, int freq)
       {
         ArrayList bcfs = FinanceLib.bondCashFlows(settle, matur, coup, dayCount, freq);

         ArrayList coupRates = (ArrayList)bcfs[0];

         ArrayList timePoints = (ArrayList)bcfs[2];

         ArrayList discountFactors = new ArrayList();
         for (int _icollect = 0; _icollect < timePoints.Count; _icollect++)
         {
           double x = (double)timePoints[_icollect];
           discountFactors.Add(Math.Pow(1.0 / (1 + (yld / freq)), x));
         }

         coupRates = SystemTypes.append(SystemTypes.front(coupRates), ((double)SystemTypes.last(coupRates)) + 1);
         double sp = 0.0;

         ArrayList _range13 = SystemTypes.integerSubrange(1, (coupRates).Count);
         for (int _i12 = 0; _i12 < _range13.Count; _i12++)
         {
           int i = (int)_range13[_i12];
           sp = sp + (((double)discountFactors[i - 1]) * ((double)coupRates[i - 1]));
         }
         return sp;
       }

       public static double accInterest(OclDate issue, OclDate settle, int freq, double coup)
       {
         int period = ((int)(12 / freq));

         ArrayList st = FinanceLib.straddleDates(issue, settle, period);
         double aif = (1.0 * OclDate.daysBetweenDates((OclDate)st[0], settle)) / OclDate.daysBetweenDates((OclDate)st[0], (OclDate)st[1]);

         return aif * (coup / freq);
       }


        public static double accumulatedInterest(OclDate issue, OclDate settle, int freq, double coup, string dayCount, OclDate matur)
        {
          int period = (int) (12 / freq);

          ArrayList st = FinanceLib.straddleDates(issue, settle, period);
          double aif = 0.0;
          OclDate d1 = (OclDate) st[0];
          OclDate d2 = (OclDate) st[1];
          int ys = d1.getYear();
          int ye = settle.getYear();
          OclDate ysEnd = OclDate.newOclDate_YMD(ys, 12, 31);
          OclDate yeStart = OclDate.newOclDate_YMD(ye, 1, 1);

            if (dayCount.Equals("Actual/365F"))
            {
                aif = (OclDate.daysBetweenDates(d1, settle) / 365.0) * coup;
            }
            else if (dayCount.Equals("Actual/ActualISDA"))
            {
                if (d1.isLeapYear() && settle.isLeapYear())
                { aif = (OclDate.daysBetweenDates(d1, settle) / 366.0) * coup; }
                else if (!(d1.isLeapYear()) && !(settle.isLeapYear()))
                {
                    aif = (OclDate.daysBetweenDates(d1, settle) / 365.0) * coup;
                }
                else if (d1.isLeapYear() && !(settle.isLeapYear()))
                {
                    aif = (OclDate.daysBetweenDates(d1, ysEnd) / 366.0) * coup +
                       (OclDate.daysBetweenDates(yeStart, settle) / 365.0) * coup;
                }
                else
                {
                    aif = (OclDate.daysBetweenDates(d1, ysEnd) / 365.0) * coup +
                       (OclDate.daysBetweenDates(yeStart, settle) / 366.0) * coup;
                }
            }
            else if (dayCount.Equals("Actual/364"))
            { aif = (OclDate.daysBetweenDates(d1, settle) / 364.0) * coup; }
            else if (dayCount.Equals("Actual/360"))
            { aif = (OclDate.daysBetweenDates(d1, settle) / 360.0) * coup; }
            else if (dayCount.Equals("Actual/ActualICMA"))
            { aif = coup* (1.0*OclDate.daysBetweenDates(d1, settle)) / (freq * OclDate.daysBetweenDates(d1, d2)); }
            else
            { aif = (FinanceLib.days360(d1, settle, dayCount, matur) / 360.0) * coup; }
            return aif; 
        }


        public static double bondPriceClean(double Y, OclDate I, OclDate S, OclDate M, double c, string dcf, int f)
        {
          double result = 0.0;

          result = FinanceLib.bondPrice(Y, S, M, c, dcf, f) - FinanceLib.accumulatedInterest(I, S, f, c, dcf, M);
          return result;
        }
  }
