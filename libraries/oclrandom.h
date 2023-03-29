#include <stdlib.h>
#include <math.h>

/* C header for OclRandom library */

struct OclRandom
{ int ix; 
  int iy; 
  int iz; 
};

struct OclRandom* newOclRandom(void)
{ struct OclRandom* res = 
    (struct OclRandom*) malloc(sizeof(struct OclRandom)); 
  res->ix = 1001; 
  res->iy = 781;
  res->iz = 913; 
  return res; 
} 

struct OclRandom* newOclRandom_Seed(long n)
{ struct OclRandom* res = 
    (struct OclRandom*) malloc(sizeof(struct OclRandom)); 
  res->ix = (int) (n % 30269);
  res->iy = (int) (n % 30307);
  res->iz = (int) (n % 30323);
  return res;
} 

void setSeeds_OclRandom(struct OclRandom* self, int x, int y, int z)
{ self->ix = x;
  self->iy = y;
  self->iz = z;
}

void setSeed_OclRandom(struct OclRandom* self, long n)
{ self->ix = (int) (n % 30269);
  self->iy = (int) (n % 30307);
  self->iz = (int) (n % 30323);
}

double nrandom_OclRandom(struct OclRandom* self)
{ self->ix = ( self->ix * 171 ) % 30269; 
  self->iy = ( self->iy * 172 ) % 30307; 
  self->iz = ( self->iz * 170 ) % 30323; 
  return (self->ix / 30269.0 + self->iy / 30307.0 + self->iz / 30323.0);
}

double nextDouble_OclRandom(struct OclRandom* self)
{ double r = nrandom_OclRandom(self);
  return ( r - floor(r) );
}

double nextFloat_OclRandom(struct OclRandom* self)
{ return nextDouble_OclRandom(self); }

double nextGaussian_OclRandom(struct OclRandom* self)
{ double d = nrandom_OclRandom(self);
  return (d/3.0 - 0.5); 
}

int nextInt_OclRandom(struct OclRandom* self, int n)
{ double d = nextDouble_OclRandom(self);
  return (int) floor(d*n);
} 

int nextIntInt_OclRandom(struct OclRandom* self)
{ return nextInt_OclRandom(self, 2147483647); }

long nextLong_OclRandom(struct OclRandom* self)
{ double d = nextDouble_OclRandom(self); 
  return (long) floor(d*9223372036854775807L); 
}

unsigned char nextBoolean_OclRandom(struct OclRandom* self)
{ double d = nextDouble_OclRandom(self);
  if (d > 0.5)
  { return TRUE; }
  return FALSE; 
} 

int randomComparator(const void* s1, const void* s2)
{ int rx = rand();
  double d = (1.0*rx)/RAND_MAX;
  if (d > 0.5)
  { return 1; }
  if (d < 0.5)
  { return -1; }
  return 0; 
}


void** randomiseSequence_OclRandom(void* col[])
{ /* Sort col with a random comparator */
  void** res = treesort(col, randomComparator); 
  return res; 
} 


void* randomElement_OclRandom(void* col[])
{ int n = length(col); 
  if (n == 0) 
  { return NULL; }
  int rx = rand();
  double d = (1.0*rx)/RAND_MAX;
  int ind = (int) floor(n*d); 
  return col[ind]; 
} 
 
char* randomString_OclRandom(int n)
{ char* chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$";
  char* res = ""; 
  int i = 0; 
  for ( ; i < n; i++) 
  { int code = (int) floor(54*(1.0*rand())/RAND_MAX);
    char* schr = (char*) calloc(2,sizeof(char));
    schr[0] = chrs[code]; 
    schr[1] = '\0';   
    res = concatenateStrings(res, schr); 
  } 
  return res; 
}
