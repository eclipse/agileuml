int MathLib::ix = 1001;

int MathLib::iy = 781;

int MathLib::iz = 913;

vector<string>* MathLib::hexdigit = UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequenceString(UmlRsdsLib<string>::addSequence((new vector<string>()),"0"),"1"),"2"),"3"),"4"),"5"),"6"),"7"),"8"),"9"),"A"),"B"),"C"),"D"),"E"),"F"); 


  double MathLib::pi()
  { double result = 0.0;
    result = 3.14159265; 
    return result;
  }


  double MathLib::e()
  { double result = 0.0;
    result = exp(1); 
    return result;
  }


  void MathLib::setSeeds(int x,int y,int z)
  { MathLib::setix(x);
    MathLib::setiy(y);
    MathLib::setiz(z);
  }

  double MathLib::nrandom()
  { double result;
MathLib::setix(( MathLib::getix() * 171 ) % 30269);
    MathLib::setiy(( MathLib::getiy() * 172 ) % 30307);
    MathLib::setiz(( MathLib::getiz() * 170 ) % 30323);
    return ( ( MathLib::getix() / 30269.0 ) + ( MathLib::getiy() / 30307.0 ) + ( MathLib::getiz() / 30323.0 ) );
  }


  double MathLib::random()
  { double result = 0.0;
    double r = MathLib::nrandom(); 
    result = ( r - ((int) floor(r)) );    
    return result;
  }

  bool MathLib::nextBoolean()
  { double r = MathLib::random(); 
    if (r > 0.5) 
    { return true; } 
    return false;
  }

  long MathLib::combinatorial(int n,int m)
  { long result = 0;

    if (n < m || m < 0) 
    { return result; } 

    if (n - m < m) 
    { result = UmlRsdsLib<int>::prd(UmlRsdsLib<int>::integerSubrange(m + 1,n)) / UmlRsdsLib<int>::prd(UmlRsdsLib<int>::integerSubrange(1,n - m)); 
    }  
    else if (n - m >= m) 
    { result = UmlRsdsLib<int>::prd(UmlRsdsLib<int>::integerSubrange(n - m + 1,n)) / UmlRsdsLib<int>::prd(UmlRsdsLib<int>::integerSubrange(1,m)); 
    }    
    return result;
  }

  long MathLib::factorial(int x)
  { long result = 0;
    if (x < 2) 
    { result = 1; }  
    else if (x >= 2) 
    { result = UmlRsdsLib<int>::prd(UmlRsdsLib<int>::integerSubrange(2,x)); 
    }    
    return result;
  }

  double MathLib::asinh(double x)
  { double result = 0.0;
    result = log(( x + sqrt(( x * x + 1 )) )); 
    return result;
  }

  double MathLib::acosh(double x)
  { double result = 0.0;
    if (x < 1) { return result; } 
    result = log(( x + sqrt(( x * x - 1 )) )); 
    return result;
  }

  double MathLib::atanh(double x)
  { double result = 0.0;
    if (x == 1) { return result; } 
    result = 0.5 * log(( ( 1 + x ) / ( 1 - x ) )); 
    return result;
  }

  string MathLib::decimal2bits(long x)
  { string result = "";
    if (x == 0) { result = ""; }
    else { result = MathLib::decimal2bits(x / 2).append(string("").append(std::to_string(( x % 2 )))); } 
    return result;
  }


  string MathLib::decimal2binary(long x)
  { string result = "";
    if (x < 0) { result = string("-").append(MathLib::decimal2bits(-x)); }
    else {   
      if (x == 0) { result = "0"; }
      else { result = MathLib::decimal2bits(x); } 
    } 
    return result;
  }


   string MathLib::decimal2oct(long x)
   { string result = "";
     if (x == 0) { result = ""; }
     else { result = MathLib::decimal2oct(x / 8).append(string("").append(std::to_string(( x % 8 )))); } 
     return result;
   }


   string MathLib::decimal2octal(long x)
   { string result = "";
     if (x < 0) { result = string("-").append(MathLib::decimal2oct(-x)); }
     else {   
       if (x == 0) { result = "0"; }
       else { result = MathLib::decimal2oct(x); } 
     } 
     return result;
   }


   string MathLib::decimal2hx(long x)
   { string result = "";
     if (x == 0) { result = ""; }
     else { result = MathLib::decimal2hx(x / 16).append(( string("").append(((string) MathLib::gethexdigit()->at(((int) ( x % 16 )) + 1 - 1))) )); } 
     return result;
   }


  string MathLib::decimal2hex(long x)
  { string result = "";
    if (x < 0) { result = string("-").append(MathLib::decimal2hx(-x)); }
    else {   if (x == 0) { result = "0"; }
    else { result = MathLib::decimal2hx(x); } } 
    return result;
  }

  int MathLib::bitwiseRotateRight(int x, int n)
  {
    if (n <= 0)
    {
        return x;
    }
    int m = n % 32;
    int arg1 = x % ((int) pow(2, m));
    return (arg1 << (32 - m)) | (x >> m);
  }

 int MathLib::bitwiseRotateLeft(int x, int n)
 {
    if (n <= 0)
    {
        return x;
    }

    int m = n % 32; 
    int arg1 = (int) (x << m); 
    return arg1 | (x >> (32 - m));
 }


  int MathLib::bitwiseAnd(int x,int y)
  { return x&y; }

  int MathLib::bitwiseOr(int x,int y)
  { return x|y; }

  int MathLib::bitwiseXor(int x,int y)
  { return x^y; }

  int MathLib::bitwiseNot(int x)
  { return ~x; }

  long MathLib::bitwiseAnd(long x, long y)
  { return x&y; }

  long MathLib::bitwiseOr(long x, long y)
  { return x|y; }

  long MathLib::bitwiseXor(long x, long y)
  { return x^y; }

  long MathLib::bitwiseNot(long x)
  { return ~x; }


  vector<bool>* MathLib::toBitSequence(long x)
  { vector<bool>* result;
    long x1;
    x1 = x;
    vector<bool>* res;
    res = (new vector<bool>());
    while (x1 > 0) 
    { if (x1 % 2 == 0)
      { res = UmlRsdsLib<bool>::concatenate(UmlRsdsLib<bool>::makeSequence(false),res); }
      else 
      { res = UmlRsdsLib<bool>::concatenate(UmlRsdsLib<bool>::makeSequence(true),res); }
      x1 = x1 / 2;
    }
    return res;
  }

  long MathLib::modInverse(long n,long p)
  { long x = ( n % p );
    for (int i = 1; i < p; i++)
    { 
      if (( ( i * x ) % p ) == 1)
      { return i; }
    }
    return 0;
  }

  long MathLib::modPow(long n,long m,long p)
  { long res = 1;
    long x = ( n % p );
    for (int i = 1; i <= m; i++)
    { res = ( ( res * x ) % p ); }
    return res;
  }

double MathLib::bisectionDiscrete(double r, double rl, double ru,
                                  vector<double>* values)
{
    double result = 0;
    result = 0;
    if ((r <= -1 || rl <= -1 || ru <= -1))
    {
        return result;
    }

    double v = 0;
    v = MathLib::netPresentValueDiscrete(r, values);
    if (ru - rl < 0.001)
    {
        return r;
    }
    if (v > 0)
    {
        return MathLib::bisectionDiscrete((ru + r) / 2, r, ru, values);
    }
    else if (v < 0)
    {
        return MathLib::bisectionDiscrete((r + rl) / 2, rl, r, values);
    }
    return r;
}
