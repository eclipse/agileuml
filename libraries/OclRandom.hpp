class OclRandom
{ private: 
    int ix; 
    int iy; 
    int iz;

public: 

  static OclRandom* newOclRandom(void)
  { OclRandom* res = new OclRandom();  
    res->ix = 1001; 
    res->iy = 781;
    res->iz = 913; 
    return res; 
  } 

  static OclRandom* newOclRandom(long n)
  { OclRandom* res = 
      new OclRandom(); 
    res->ix = (int) (n % 30269);
    res->iy = (int) (n % 30307);
    res->iz = (int) (n % 30323);
    return res;
  } 

  void setSeeds(int x, int y, int z)
  { ix = x;
    iy = y;
    iz = z;
  }

  void setSeed(long n)
  { ix = (int) (n % 30269);
    iy = (int) (n % 30307);
    iz = (int) (n % 30323);
  }

  double nrandom()
  { ix = ( ix * 171 ) % 30269; 
    iy = ( iy * 172 ) % 30307; 
    iz = ( iz * 170 ) % 30323; 
    return (ix / 30269.0 + iy / 30307.0 + iz / 30323.0);
  }

  double nextDouble()
  { double r = nrandom();
    return ( r - floor(r) );
  }

  double nextFloat()
  { return nextDouble(); }

  double nextGaussian()
  { double d = nrandom();
    return (d/3.0 - 0.5); 
  }

  int nextInt(int n)
  { double d = nextDouble();
    return (int) floor(d*n);
  } 

  int nextInt()
  { return nextInt(2147483647); }

  long nextLong()
  { double d = nextDouble(); 
    return (long) floor(d*9223372036854775807L); 
  }

  bool nextBoolean()
  { double d = nextDouble();
    if (d > 0.5)
    { return true; }
    return false; 
  } 

  static int randomComparator(const void* s1, const void* s2)
  { int rx = rand();
    double d = (1.0*rx)/RAND_MAX;
    if (d > 0.5)
    { return 1; }
    if (d < 0.5)
    { return -1; }
    return 0; 
  }

  template <class T>
  static vector<T>* randomiseSequence(vector<T>* col)
  { /* Sort col with a random comparator */
    return UmlRsdsLib<T>::randomiseSequence(col);
  }

  template <class T>
  static T randomElement(vector<T>* sq)
  { int n = sq->size(); 
    if (n == 0) 
	{ return NULL; }
	int rx = rand(); 
	int ind = (n*rx)/RAND_MAX;
	return sq->at(ind); 
  } 

  static string randomString(int n)
  { string chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$";
    string res = ""; 
    int i = 0; 
    for ( ; i < n; i++) 
    { int code = (int) floor(54*(1.0*rand())/RAND_MAX);
      string c = chrs.substr(code,1); 
    
      res = res.append(c); 
    } 
    return res; 
  }
};
