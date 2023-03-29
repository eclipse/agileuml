class MathLib
{ 
 private:
  static int ix;
  static int iy;
  static int iz;
  static vector<string>* hexdigit;

 public:
   MathLib() {
     ix = 0;
     iy = 0;
     iz = 0;
     hexdigit = (new vector<string>());
  }

  static void setix(int ix_x) { ix = ix_x; }

  static void setiy(int iy_x) { iy = iy_x; }

  static void setiz(int iz_x) { iz = iz_x; }

  static void sethexdigit(vector<string>* hexdigit_x) 
  { hexdigit = hexdigit_x; }

  static void sethexdigit(int _ind, string hexdigit_x)   
  { (*hexdigit)[_ind] = hexdigit_x; }

  static void addhexdigit(string hexdigit_x)
  { hexdigit->push_back(hexdigit_x); }

  static int getix() { return ix; }

  static int getiy() { return iy; }
  
  static int getiz() { return iz; }
  
  static vector<string>* gethexdigit() { return hexdigit; }
  
  static double pi();

  static double e();

  static void setSeeds(int x,int y,int z);

  static double nrandom();

  static double random();

  static long combinatorial(int n,int m);

  static long factorial(int x);

  static double asinh(double x);

  static double acosh(double x);

  static double atanh(double x);

  static string decimal2bits(long x);

  static string decimal2binary(long x);

  static string decimal2oct(long x);

  static string decimal2octal(long x);

  static string decimal2hx(long x);

  static string decimal2hex(long x);

  static int bitwiseAnd(int x,int y);

  static int bitwiseOr(int x,int y);

  static int bitwiseXor(int x,int y);

  static int bitwiseNot(int x);

  static long bitwiseAnd(long x, long y);

  static long bitwiseOr(long x, long y);

  static long bitwiseXor(long x, long y);

  static long bitwiseNot(long x);

  static vector<bool>* toBitSequence(long x);

  static long modInverse(long n,long p);

  static long modPow(long n,long m,long p);

  static inline long doubleToLongBits(double x) {
    uint64_t bits;
    memcpy(&bits, &x, sizeof bits);
    return long(bits);
  }

  static inline double longBitsToDouble(long x) {
    uint64_t bits;
    memcpy(&bits, &x, sizeof bits);
    return double(bits);
  }

    static double discountDiscrete(double amount, double rate, double time)
    {
        double result = 0;
        result = 0.0;
        if ((rate <= -1 || time < 0))
        {
            return result;
        }

        result = amount / pow((1 + rate), time);
        return result;
    }

    static double bisectionDiscrete(double r, double rl, double ru,
        vector<double>* values); 

    static double netPresentValueDiscrete(double rate, vector<double>* values)
    {
        double result = 0;
        result = 0.0;
        if ((rate <= -1))
        {
            return result;
        }

        int upper = values->size();
        int i = 0;
        for (; i < upper; i++)
        {
            result = result + discountDiscrete(values->at(i), rate, i);
        }
        return result;
    }


    static double irrDiscrete(vector<double>* values)
    {
        double res = bisectionDiscrete(0.1, -0.5, 1.0, values);
        return res;
    }

    static double roundN(double x, int n)
    {
        if (n < 0)
        {
            return round(x);
        }
        double y = x * pow(10, n);
        return round(y) / pow(10, n);
    }

    static double truncateN(double x, int n)
    {
        if (n < 0)
        {
            return (int)x;
        }
        double y = x * pow(10, n);
        return ((int)y) / pow(10, n);
    }

    static double toFixedPoint(double x, int m, int n)
    {
        if (m < 0 || n < 0)
        {
            return x;
        }
        int y = (int)(x * pow(10, n));
        int z = y % ((int) pow(10, m + n));
        return z / pow(10.0, n);
    }

    static double toFixedPointRound(double x, int m, int n)
    {
        if (m < 0 || n < 0)
        {
            return x;
        }
        int y = (int) round(x * pow(10, n));
        int z = y % ((int) pow(10, m + n));
        return z / pow(10.0, n);
    }

static bool isIntegerOverflow(double x, int m)
    {
        bool result = false;
        int y = ((int)x);
        if (y > 0)
        {
            result = ((int) log10(y)) + 1 > m;
        }
        else
        {
            if (y < 0)
            {
                result = (((int) log10(-y)) + 1 > m);
            }
            else
            { result = (m < 1);  }
        }
        return result;
    }

    static double mean(vector<double>* sq)
    {
        double result = 0.0;
        if (sq->size() <= 0) { return result; }
        result = UmlRsdsLib<double>::sum(sq) / sq->size();
        return result;
    }

    static double median(vector<double>* sq)
    {
        double result = 0.0;
        if (sq->size() <= 0) { return result; }
        vector<double>* s1 = UmlRsdsLib<double>::sort(sq);
        int sze = sq->size();
        if (sze % 2 == 1)
        {
            result = ((double) (s1)->at((1 + sze) / 2 - 1));
        }
        else
            if (sze % 2 == 0)
            {
                result = (((double) (s1)->at(sze / 2 - 1)) + ((double) (s1)->at(sze / 2))) / 2.0;
            }
        return result;
    }

    static double variance(vector<double>* sq)
    {
        double result = 0.0;
        if (sq->size() <= 0) { return result; }
        double m = MathLib::mean(sq);
        double sumsq = 0.0; 
        for (int _icollect = 0; _icollect < sq->size(); _icollect++)
        {
          double x = (*sq)[_icollect];
          sumsq = sumsq + ((x - m)) * ((x - m));
        }
        result = sumsq / sq->size();
        return result;
    }


    static double standardDeviation(vector<double>* sq)
    {
        double result = 0.0;
        if (sq->size() <= 0) { return result; }
        double m = MathLib::variance(sq);
        result = sqrt(m);
        return result;
    }


    static int lcm(int x, int y)
    {
        int result = 0;
        if (x == 0 && y == 0)
        {
            result = 0;
        }
        else
        {
           result = (x * y) / UmlRsdsLib<long>::gcd(x, y);
        }
        return result;
    }


    static double bisectionAsc(double r, double rl, double ru, std::function<double(double)> f, double tol)
    {
        double result = 0.0;
        double v = f(r);
        
        if (v < tol && v > -tol)
        {
            return r;
        }
        else
        {
            if (v > 0)
            {
                return MathLib::bisectionAsc((rl + r) / 2.0, rl, r, f, tol);
            }
            else
                if (v < 0)
                {
                    return MathLib::bisectionAsc((r + ru) / 2.0, r, ru, f, tol);
                }
        }
        return result;
    }

    static vector<double>* rowMult(vector<double>* s, vector<vector<double>*>* m)
    {
        vector<double>* result = new vector<double>();
        vector<double>* _results_5 = new vector<double>();
        for (int _icollect = 0; _icollect < s->size(); _icollect++)
        {
            int i = _icollect + 1; 
            vector<double>* _results_4 = new vector<double>();
            for (int _icollect = 0; _icollect < m->size(); _icollect++)
            {
                int k = _icollect + 1;
                _results_4->push_back((s)->at(k - 1) * ((double)((m)->at(k - 1))->at(i - 1)));
            }
            _results_5->push_back(UmlRsdsLib<double>::sum(_results_4));
        }
        result = _results_5;
        return result;
    }

    static vector<vector<double>*>* matrixMultiplication(vector<vector<double>*>* m1, vector<vector<double>*>* m2)
    {
        vector<vector<double>*>* result = new vector<vector<double>*>();
        for (int _icollect = 0; _icollect < m1->size(); _icollect++)
        {
            vector<double>* row = (*m1)[_icollect];
            result->push_back(MathLib::rowMult(row, m2));
        }
        return result;
    }

  ~MathLib() {
  }

};
