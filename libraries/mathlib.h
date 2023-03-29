
/* C header for mathlib library */



double pi_MathLib(void)
{ return 3.14159265; } 

double piValue_MathLib(void)
{ return 3.14159265; } 


double e_MathLib(void)
{ return exp(1); } 

double eValue_MathLib(void)
{ return exp(1); } 

void setSeeds_MathLib(int x, int y, int z)
{ srand(x); } 

double nrandom_MathLib(void) 
{ double total = 0.0; 
  int i = 0; 
  for ( ; i < 10; i++) 
  { total += rand()/(10.0*RAND_MAX); } 
  return total - 0.5; 
} 

double random_MathLib(void)
{ return rand()/(1.0*RAND_MAX); } 

long factorial_MathLib(int n) 
{ long res = 1; 
  int i = 2; 
  for ( ; i <= n; i++) 
  { res = res*i; } 
  return res; 
} 

long combinatorial_MathLib(int n, int m) 
{ if (n < m) 
  { return 0; } 
  if (m < 0)
  { return 0; } 

  long res = 1;
  if (n - m < m)
  { int i = m+1; 
    for ( ; i <= n; i++) 
    { res = res*i; }
    return res/factorial_MathLib(n-m); 
  }  
  else if (n - m >= m)
  { int i = n-m+1; 
    for ( ; i <= n; i++) 
    { res = res*i; }
    return res/factorial_MathLib(m); 
  }  
  return res; 
} 

double roundN_MathLib(double x, int n)
{ double y = x*pow(10,n); 
  double z = round(y); 
  return z/pow(10,n); 
} 

double truncateN_MathLib(double x, int n)
{ if (n < 0)
  {
    return (int) x;
  }
  double y = x * pow(10, n);
  return ((int) y) / pow(10, n);
}

double toFixedPoint_MathLib(double x, int m, int n)
{ int y = (int) (x*pow(10,n)); 
  int z = y % ((int) pow(10,m+n)); 
  return z/(1.0*pow(10,n)); 
} 

unsigned char toFixedPointOverflow_MathLib(double x, int m, int n)
{ int y = (int) (x*pow(10,n)); 
  int z = y / ((int) pow(10,m+n)); 
  if (z > 0)
  { return TRUE; } 
  return FALSE; 
} 

double toFixedPointRound_MathLib(double x, int m, int n)
{ int y = (int) round(x*pow(10,n)); 
  int z = y % ((int) pow(10,m+n)); 
  return z/(1.0*pow(10,n)); 
} 


double asinh_MathLib(double x)
{ return log(x + sqrt( x * x + 1 ) ); } 

double acosh_MathLib(double x)
{ return log(x + sqrt( x * x - 1 ) ); } 

double atanh_MathLib(double x)
{ return 0.5 * log( ( 1 + x ) / ( 1 - x ) ); } 


int bitwiseAnd_MathLib(int x, int y)
{ return x&y; } 

int bitwiseOr_MathLib(int x, int y)
{ return x|y; } 

int bitwiseXor_MathLib(int x, int y)
{ return x^y; } 

char* decimal2hex_MathLib(long x)
{ long y = x; 
  if (x < 0) { y = -x; } 
  int xwidth = (int) ceil(log10(y+1) + 3); 
  char* s = (char*) calloc(xwidth, sizeof(char)); 
  if (x < 0)
  { sprintf(s, "-0x%X", y); } 
  else 
  { sprintf(s, "0x%X", y); } 
 
  return s; 
}

char* decimal2octal_MathLib(long x)
{ long y = x; 
  if (x < 0) { y = -x; } 
  int xwidth = (int) ceil(2*log10(y+1) + 3); 
  char* s = (char*) calloc(xwidth, sizeof(char)); 
  if (x < 0) 
  { sprintf(s, "-0%o", y); } 
  else 
  { sprintf(s, "0%o", y); } 
 
  return s; 
}

char* decimal2bits_MathLib(long x)
{ /* char* sign = ""; 
  if (x < 0) 
  { sign = "-"; } */ 

  if (x == 0) 
  { return ""; }
  else 
  { char* res = decimal2bits_MathLib(x / 2);
    char* dig = "0"; 
    if (x % 2 == 1)
    { dig = "1"; } 
    char* s = concatenateStrings(res,dig); 
    return s;
  }
} 


char* decimal2binary_MathLib(long x)
{ long y = x; 
  if (x < 0) { y = -x; } 
  int xwidth = (int) ceil(4*log10(y+1) + 3); 
  char* s = (char*) calloc(xwidth, sizeof(char));
  if (x == 0)
  { return "0b0"; } 
  if (x > 0)
  { s[0] = '0'; s[1] = 'b'; } 
  else 
  { s[0] = '-'; s[1] = '0'; s[2] = 'b'; }
  return concatenateStrings(s, decimal2bits_MathLib(y));  
}

long bytes2integer_MathLib(int** bs)
{ long result = 0;
  if (length((void**) bs) == 0)
  { result = 0; }
  else
  { if (length((void**) bs) == 1)
    { result = (int) at((void**) bs, 1); }
    else
    { if (length((void**) bs) == 2)
      { result = 256 * atint(bs, 1) + atint(bs, 2); }
      else
      { if (length((void**) bs) > 2)
        { result = 256 * bytes2integer_MathLib(frontint(bs)) + lastint(bs); }
      }
    }
  }
  return result;
}

int** integer2bytes_MathLib(long x)
{ int** result = NULL;
  if ((x / 256) == 0)
  { result = appendint(newintList(), (x % 256)); }
  else
  { result = appendint(integer2bytes_MathLib(x / 256), x % 256); }
  return result;
}

int** integer2Nbytes_MathLib(long x, int n)
{ int** result = NULL;
  int** bs = NULL;
  bs = integer2bytes_MathLib(x);
  if (length((void**) bs) < n)
  { result = bs; }
  else
  { if (length((void**) bs) >= n)
    { result = subrangeint(bs,1,n); }
  }
  return result;
}


char* toBitSequence_MathLib(long x)
{ long y = x; 
  if (x < 0) { y = -x; } 
  int xwidth = (int) ceil(4*log10(y+1) + 3); 
  
  char* res = (char*) calloc(xwidth, sizeof(char));

  int index = 0; 
  while (y > 0)
  { if (y % 2 == 0)
    { res[index] = '\060'; }
    else 
    { res[index] = '\061'; } 
    y = y/2;
    index++; 
  } 
  res[index] = '\0'; 
  return reverseStrings(res);   
}

long modInverse_MathLib(long n, long p)
{ long x = n % p; 
  int i;
  i = 1;   
  for ( ; i < p; i++)
  { if (((i*x) % p) == 1)  
    { return i; }
  }  
  return 0; 
}

long modPow_MathLib(long n, long m, long p) 
{ long res = 1L; 
  long x = n % p;  
  int i = 1; 
  for ( ; i <= m; i++) 
  { res = ((res*x) % p); }  
  return res; 
}

long doubleToLongBits_MathLib(double d)
{ double* dx = &d; 
  long* lx = (long*) dx; 

  char* sbufl = (char*) calloc(21, sizeof(char)); 

  sprintf(sbufl, "%ld", *lx);
  return strtol(sbufl,NULL,10);  
} 

double longBitsToDouble_MathLib(long x) {
  long bits;
  memcpy(&bits, &x, sizeof bits);
  double* ptr = (double*) &bits;
  return *ptr; 
}

double discountDiscrete_MathLib(double amount, double rate, double time)
{ double result = 0;
  result = 0.0;
  if ((rate <= -1 || time < 0))
  { return result; }
  
  result = amount / pow((1 + rate), time);
  return result;
}

double netPresentValueDiscrete_MathLib(double rate, double* values[])
{ double result = 0;
  result = 0.0;
  if ((rate <= -1))
  { return result; }
  
  int upper = length((void**) values);
  int i = 0; 
  for ( ; i < upper; i++)
  { result = result + discountDiscrete_MathLib(*values[i], rate, i); }
  return result;
}


double presentValueBondDiscrete_MathLib(double term, double coupon, int frequency, double rate)
{ double result = 0;
  result = 0.0;
  if ((rate <= -1))
  { return result; }
  
  int upper = 0;
  upper = (int) (coupon * frequency);
  double c = 0;
  c = coupon / frequency;
  double period = 0;
  period = 1.0 / frequency;
  int i = 0;
  i = 1;
  while (i <= upper)
  { result = result + discountDiscrete_MathLib(c, rate, i * period);
    i = (i + 1);
  }
  result = result + discountDiscrete_MathLib(100, rate, term);
  return result;
}


double bisectionBondDiscrete_MathLib(double r, double rl, double ru, 
                   double price, double term, double coupon, int frequency)
{ double result = 0;
  result = 0;
  if ((r <= -1 || rl <= -1 || ru <= -1))
  { return result; }
  
  double v = 0;
  v = presentValueBondDiscrete_MathLib(term,coupon,frequency,r);
  if (ru - rl < 0.001)
  { return r; } 
  if (v > price)
  { return bisectionBondDiscrete_MathLib((ru + r) / 2, r, ru, price, term, coupon, frequency); } 
  else if (v < price)
  { return bisectionBondDiscrete_MathLib((r + rl) / 2, rl, r, price, term, coupon, frequency); }
  return r; 
}

double irrBondDiscrete_MathLib(double price, double term, double coupon, int frequency)
{ double res = bisectionBondDiscrete_MathLib(0.1,0.0,1.0,price,term,coupon,frequency); 
  return res; 
}

double bisectionDiscrete_MathLib(double r, double rl, double ru, 
                         double* values[])
{ double result = 0;
  result = 0;
  if ((r <= -1 || rl <= -1 || ru <= -1))
  { return result; }
  
  double v = 0;
  v = netPresentValueDiscrete_MathLib(r,values);
  if (ru - rl < 0.001)
  { return r; } 
  if (v > 0)
  { return bisectionDiscrete_MathLib((ru + r) / 2, r, ru, values); } 
  else if (v < 0)
  { return bisectionDiscrete_MathLib((r + rl) / 2, rl, r, values); }
  return r; 
}

double irrDiscrete_MathLib(double* values[])
{ double res = bisectionDiscrete_MathLib(0.1,0.0,1.0,values); 
  return res; 
}

unsigned char isIntegerOverflow_MathLib(double x, int m)
{ int y = ((int) x);
  if (y > 0)
  {
    if (((int) log10(y)) + 1 > m)
    { return TRUE; }
    else 
    { return FALSE; }
  } 
  else
  {
    if (y < 0)
    {
      if (((int) log10(-y)) + 1 > m)
      { return TRUE; } 
      else 
      { return FALSE; }
    }
    else
    { return (m < 1);  }
  }
}


double mean_MathLib(double* sq[])
{
  double result = 0.0;
  int n = length((void**) sq); 
  if (n <= 0) 
  { return result; }
  result = sum_double(sq) / n;
  return result;
}

double median_MathLib(double* sq[])
{
  double result = 0.0;
  int n = length((void**) sq); 
  if (n <= 0) 
  { return result; }
  double** s1 = (double**) treesort(sq, compareTo_double);
  if (n % 2 == 1)
  {
    result = *s1[(1 + n)/2 - 1];
  }
  else
  {
    result = (*s1[n/2 - 1] + *s1[n/2])/2.0;
  }
  return result;
}

double variance_MathLib(double* sq[])
{
  double result = 0.0;
  int sqsize = length(sq);  
  if (sqsize <= 0) 
  { return result; }
  double m = mean_MathLib(sq);
  double sumsq = 0.0; 
  int i = 0;
  for ( ; i < sqsize; i++)
  {
    double x = *sq[i];
    sumsq = sumsq + ((x - m)) * ((x - m));
  }
  result = sumsq / sqsize;
  return result;
}


double standardDeviation_MathLib(double* sq[])
{
  double result = 0.0;
  if (length(sq) <= 0) 
  { return result; }
  double m = variance_MathLib(sq);
  result = sqrt(m);
  return result;
}


int lcm_MathLib(int x, int y)
{
  int result = 0;
  if (x == 0 && y == 0)
  {
    result = 0;
  }
  else
  {
    result = (x * y) / gcd(x, y);
  }
  return result;
}



double** rowMult_MathLib(double* s[], double** m[])
{ int ssize = length(s); 
  int msize = length(m); 
  double** result = calloc(ssize+1, sizeof(double*));

  int _icollect = 0;
  for (; _icollect < ssize; _icollect++)
  {
    int i = _icollect + 1; 
    double** _results_4 = calloc(msize+1, sizeof(double*));
    int _icollect = 0;
    for ( ; _icollect < msize; _icollect++)
    {
      int k = _icollect + 1;
      double val = (*s[k-1]) * (*m[k-1][i-1]); 
      _results_4 = appenddouble(_results_4, val);
    }

    result = appenddouble(result, sum_double(_results_4));
  }

  return result;
}

double*** matrixMultiplication_MathLib(double** m1[], double** m2[])
{ int m1size = length(m1); 
  double*** result = calloc(m1size+1, sizeof(double**));
  int _icollect = 0;
  for ( ; _icollect < m1size; _icollect++)
  {
    double** row = m1[_icollect];
    result = append(result, rowMult_MathLib(row, m2));
  }
  return result;
}

double bisectionAsc_MathLib(double r, double rl, double ru, double (*f)(double), double tol)
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
      return bisectionAsc_MathLib((rl + r) / 2.0, rl, r, f, tol);
    }
    else if (v < 0)
    {
      return bisectionAsc_MathLib((r + ru) / 2.0, r, ru, f, tol);
    }
  }
  return result;
}
