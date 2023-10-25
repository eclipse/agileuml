
    class MathLib
    {
        private static int ix; // internal
        private static int iy; // internal
        private static int iz; // internal
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
        }

        public static double pi()
        {
            double result = 0.0;

            result = 3.14159265;
            return result;
        }


        public static double eValue()
        {
            double result = 0.0;

            result = Math.Exp(1);
            return result;
        }

        public static double piValue()
        {
            double result = 0.0;

            result = 3.14159265;
            return result;
        }


        public static double e()
        {
            double result = 0.0;

            result = Math.Exp(1);
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
            double result = 0.0;

            result = Math.Log((x + Math.Sqrt((x * x + 1))));
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

        public static double discountDiscrete(double amount, double rate, double time)
        {
            double result = 0;
            result = 0.0;
            if ((rate <= -1 || time < 0))
            { return result; }

            result = amount / Math.Pow((1 + rate), time);
            return result;
        }

        public static double netPresentValueDiscrete(double rate, ArrayList values)
        {
            double result = 0;
            result = 0.0;
            if ((rate <= -1))
            { return result; }

            int upper = values.Count; 
            int i = 0;
            for (; i < upper; i++)
            { result = result + MathLib.discountDiscrete((double) values[i], rate, i); }
            return result;
        }

       public static double presentValueDiscrete(double rate, ArrayList values)
        {
            double result = 0;
            result = 0.0;
            if ((rate <= -1))
            { return result; }

            int upper = values.Count;

            for (int i = 0; i < upper; i++)
            {
                Object val = values[i];
                double dval = 0.0;
                if (val is double)
                { dval = (double)val; }
                else if (val is int)
                { dval = 1.0 * ((int)val); }
                result = result + MathLib.discountDiscrete(dval,
                                                rate, i + 1);
            }
            return result;
        }

        public static double bisectionDiscrete(double r, double rl, double ru, ArrayList values)
        {
            double result = 0;
            result = 0;
            if ((r <= -1 || rl <= -1 || ru <= -1))
            { return result; }

            double v = 0;
            v = MathLib.netPresentValueDiscrete(r, values);
            if (ru - rl < 0.001)
            { return r; }
            if (v > 0)
            { return MathLib.bisectionDiscrete((ru + r) / 2, r, ru, values); }
            else if (v < 0)
            { return MathLib.bisectionDiscrete((r + rl) / 2, rl, r, values); }
            return r;
        }

        public static double irrDiscrete(ArrayList values)
        {
            double res = bisectionDiscrete(0.1, -0.5, 1.0, values);
            return res;
        }

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
  
         result = (Func<double,double>) (x => (( 500.0 * ( f.Invoke(x + 0.001) - f.Invoke(x - 0.001) ) ))); 
         return result;
       }

        public static double definiteIntegral(double st, double en, Func<double, double> f)
        {
            double tol = 0.001;
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