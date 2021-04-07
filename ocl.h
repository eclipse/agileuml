#include <ctype.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#define ALLOCATIONSIZE 50

#define TRUE 1
#define FALSE 0

/* This header provides ANSI C implementations of
   OCL library operations. OCL indexing conventions
   are used to access elements of collections and
   strings: numbering starts from 1 instead of 0.

   Note that all parameters may be regarded as const
   except in the case of treesort and modifying tree
   operations.

   NULL collections are treated as being empty collections. 
*/


int length(void* col[])
{ int result = 0;
  if (col == NULL)  { return result; }
  void* ex = col[0];
  while (ex != NULL)
  { result++;
    ex = col[result];
  }
  return result;
}

unsigned char isIn(void* x, void* col[])
{ int result = FALSE;
  if (col == NULL) { return result; }
  void* ex = col[0];
  int i = 0;
  while (ex != NULL)
  { if (ex == x) { return TRUE; }
    i++;
    ex = col[i];
  }
  return result;
}

unsigned char isEmpty(void* col[])
{ int result = TRUE; 
  if (length(col) > 0) 
  { return FALSE; } 
  return result; 
} 

unsigned char notEmpty(void* col[])
{ int result = FALSE; 
  if (length(col) > 0) 
  { return TRUE; } 
  return result; 
} 

void* any(void* col[])
{ if (col == NULL) { return NULL; }
  return col[0];
}

void* at(void* col[], int i)
{ int n = length(col);
  if (n == 0 || i < 1 || i > n) { return NULL; }
  return col[i-1];
}

void* first(void* col[])
{ if (col == NULL) { return NULL; }
  return col[0];
}

void* last(void* col[])
{ int n = length(col);
  if (n == 0) { return NULL; }
  return col[n-1];
}

char* concatenateStrings(const char* st1, const char* st2)
{ int n = strlen(st1);
  int m = strlen(st2);
  char* result = (char*) calloc(n + m + 1, sizeof(char));
  strcat(strcat(result, st1), st2);
  result[n+m] = '\0';
  return result;
}

unsigned char containsAll(void* col1[], void* col2[])
{ /* col2 <: col1 */
  int n2 = length(col2);
  unsigned char result = TRUE;
  int i = 0;
  for ( ; i < n2; i++)
  { void* ex = col2[i];
    if (ex == NULL) { return result; }
    if (isIn(ex,col1)) { }
    else { return FALSE; }
  }
  return result;
}

 unsigned char equalsSet(void* col1[], void* col2[])
 { return containsAll(col1,col2) && containsAll(col2,col1); }

 unsigned char equalsSequence(void* col1[], void* col2[])
 { unsigned char result = TRUE;
   int n1 = length(col1);
   int n2 = length(col2);
   if (n1 != n2) { return FALSE; }
   int i = 0;
   for ( ; i < n1; i++)
   { void* ex = col1[i];
     if (ex == NULL) { return result; }
     if (ex == col2[i]) { }
     else { return FALSE; }
   }
   return result;
 }

 unsigned char disjoint(void* col1[], void* col2[])
 { int n = length((void**) col1);
   unsigned char result = TRUE;
   int i = 0;
   for ( ; i < n; i++)
   { void* ex = col1[i];
     if (ex == NULL) { return result; }
     if (isIn(ex,col2)) { return FALSE; }
   }
   return result;
 }

int oclFloor(double x)
{ return (int) floor(x); } 

int oclRound(double x)
{ int f = (int) floor(x);
  int c = (int) ceil(x);
  if (x >= f + 0.5) { return c; }
  return f;
}

int oclCeil(double x)
{ return (int) ceil(x); }

double cbrt(double x)
{ return pow(x, 1.0/3); }

char* toLowerCase(const char* s)
{ int n = strlen(s);
  char* result = (char*) calloc(n+1,sizeof(char));
  int i = 0;
  for ( ; i < n; i++)
  { result[i] = tolower(s[i]); }
  result[n] = '\0';
  return result;
}

char* toUpperCase(const char* s)
{ int n = strlen(s);
  char* result = (char*) calloc(n+1,sizeof(char));
  int i = 0;
  for ( ; i < n; i++)
  { result[i] = toupper(s[i]); }
  result[n] = '\0';
  return result;
}

char* subString(const char* s, int i, int j)
{ /* Characters of s from s[i-1] to s[j-1], inclusive */
  int n = strlen(s);
  if (i < 1) { i = 1; } 
  if (j > n) { j = n; } 
  if (i > j) { return ""; }
  char* result = (char*) calloc(j-i+2,sizeof(char));

  int x = i-1;
  for ( ; x < j; x++)
  { result[x-i+1] = s[x]; }
  result[j-i+1] = '\0';
  return result;
}

char* firstString(const char* s)
{ return subString(s,1,1); }

char* lastString(const char* s)
{ int n = strlen(s);
  return subString(s,n,n);
}

char* frontString(const char* s)
{ int n = strlen(s);
  return subString(s,1,n-1);
}

char* tailString(const char* s)
{ int n = strlen(s);
  return subString(s,2,n);
}


  unsigned char startsWith(const char* s, const char* sb)
  { int n = strlen(s);
    int m = strlen(sb);
    if (m <= n && strncmp(s,sb,m) == 0)
    { return TRUE; }
    return FALSE;
  }

  unsigned char endsWith(const char* s, const char* sb)
  { int n = strlen(s);
    int m = strlen(sb);
    if (m <= n && strcmp(s + (n-m),sb) == 0)
    { return TRUE; }
    return FALSE;
  }

  int indexOfString(const char* s, const char* sb)
  { int n = strlen(s);
    int m = strlen(sb);
    int result = 0;
    if (n == 0 || m == 0) { return result; }
    int i = 0;
    for ( ; i < n; i++)
    { if (startsWith(&s[i],sb))
      { return i+1; }
    }
    return result;
  }

char* before(char* s, char* sep)
{ if (strlen(sep) == 0) 
  { return s; }
  char* ind = strstr(s,sep); 
  if (ind == NULL)
  { return s;} 
  char* res = (char*) calloc(strlen(s),sizeof(char)); 
  return strncpy(res,s,(ind-s)); 
}

char* after(char* s, char* sep)
{ if (strlen(sep) == 0) 
  { return ""; }
  char* ind = strstr(s,sep); 
  if (ind == NULL)
  { return "";} 
  char* res = (char*) calloc(strlen(s),sizeof(char)); 
  return strcpy(res,ind + strlen(sep)); 
}

  int count(void* x, void* col[])
  { int n = length(col);
    int result = 0;
    int i = 0;
    for ( ; i < n; i++)
    { if (x == col[i]) { result++; } }
    return result;
  }

  int countString(const char* x, const char* col[])
  { int n = length((void**) col);
    int result = 0;
    int i = 0;
    for ( ; i < n; i++)
    { char* y = col[i];
      if (strcmp(x,y) == 0) { result++; }
    }
    return result;
  }

  char* reverseString(const char* s)
  { int n = strlen(s);
    char* result = (char*) calloc(n+1,sizeof(char));
    int x = n-1;
    int i = 0;
    for ( ; i < n; i++, x--)
    { result[i] = s[x]; }
    return result;
  }


  char** characters(char* s)
  { int n = strlen(s);
    char** result = (char**) calloc(n+1, sizeof(char*));
    int i = 0;
    for ( ; i < n; i++)
    { char* x = (char*) calloc(2, sizeof(char));
      x[0] = s[i];
      x[1] = '\0';
      result[i] = x;
    }
    result[n] = NULL;
    return result;
  }

  char* subtractString(const char* s, const char* s1)
  { /* Characters of s not in s1 */
    int n = strlen(s);
    char* result = (char*) calloc(n+1,sizeof(char));
    int i = 0;
    int j = 0;
    for ( ; i < n; i++)
    { char c = s[i];
      if (strchr(s1,c) == NULL)
      { result[j] = c;
        j++;
      }
    }
    result[j] = '\0';
    return result;
  }


  int indexOf(void* x, void** col)
  { int n = length(col);
    int result = 0;
    int i = 0;
    for ( ; i < n; i++)
    { if (col[i] == x)
      { return i+1; }
    }
    return result;
  }

   void** append(void* col[], void* ex)
   { void** result;
     int len = length(col);
     if (len % ALLOCATIONSIZE == 0)
     { result = (void**) calloc(len + ALLOCATIONSIZE + 1, sizeof(void*));
       int i = 0;
       for ( ; i < len; i++)
       { result[i] = col[i]; }
     }
    else
    { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

/* These functions support lists of primitive types, strings, numerics and booleans */

  char** appendString(char* col[], char* ex)
  { char** result;
    int len = length((void**) col);
    if (len % ALLOCATIONSIZE == 0)
    { result = (char**) calloc(len + ALLOCATIONSIZE + 1, sizeof(char*));
      int i = 0;
      for ( ; i < len; i++)
      { result[i] = col[i]; }
    }
    else
    { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

  int** appendint(int* col[], int ex)
  { int** result;
    int len = length((void**) col);
    if (len % ALLOCATIONSIZE == 0)
    { result = (int**) calloc(len + ALLOCATIONSIZE + 1, sizeof(int*));
      int i = 0;
      for ( ; i < len; i++)
      { result[i] = col[i]; }
    }
    else
    { result = col; }

    int* elem = (int*) malloc(sizeof(int));
    *elem = ex; 
  
    result[len] = elem;
    result[len+1] = NULL;
    return result;
  }

  long** appendlong(long* col[], long ex)
  { long** result;
    int len = length((void**) col);
    if (len % ALLOCATIONSIZE == 0)
    { result = (long**) calloc(len + ALLOCATIONSIZE + 1, sizeof(long*));
      int i = 0;
      for ( ; i < len; i++)
      { result[i] = col[i]; }
    }
    else
    { result = col; }

    long* elem = (long*) malloc(sizeof(long));
    *elem = ex; 
  
    result[len] = elem;
    result[len+1] = NULL;
    return result;
  }

  double** appenddouble(double* col[], double ex)
  { double** result;
    int len = length((void**) col);
    if (len % ALLOCATIONSIZE == 0)
    { result = (double**) calloc(len + ALLOCATIONSIZE + 1, sizeof(double*));
      int i = 0;
      for ( ; i < len; i++)
      { result[i] = col[i]; }
    }
    else
    { result = col; }

    double* elem = (double*) malloc(sizeof(double));
    *elem = ex; 
  
    result[len] = elem;
    result[len+1] = NULL;
    return result;
  }


  char** insertString(char* col[], char* ex)
  { char** result;
    if (isIn((void*) ex, (void**) col)) { return col; }

    int len = length((void**) col);
    if (len % ALLOCATIONSIZE == 0)
    { result = (char**) calloc(len + ALLOCATIONSIZE + 1, sizeof(char*));
      int i = 0;
      for ( ; i < len; i++)
      { result[i] = col[i]; }
    }
    else
    { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

  char** newStringList(void)
  { char** result;
    result = (char**) calloc(ALLOCATIONSIZE + 1, sizeof(char*));
    result[0] = NULL;
    return result;
  }

  int** newintList(void)
  { int** result;
    result = (int**) calloc(ALLOCATIONSIZE + 1, sizeof(int*));
    result[0] = NULL;
    return result;
  }

  long** newlongList(void)
  { long** result;
    result = (long**) calloc(ALLOCATIONSIZE + 1, sizeof(long*));
    result[0] = NULL;
    return result;
  }

  double** newdoubleList(void)
  { double** result;
    result = (double**) calloc(ALLOCATIONSIZE + 1, sizeof(double*));
    result[0] = NULL;
    return result;
  }


void displayString(char* s)
{ printf("%s\n", s); }


void displayint(int s)
{ printf("%d\n", s); }

void displaylong(long s)
{ printf("%ld\n", s); }

void displaydouble(double s)
{ printf("%f\n", s); }

void displayboolean(unsigned int s)
{ if (s == TRUE) 
  { printf("%s\n", "true"); }
  else
  { printf("%s\n", "false"); }
}

char* toString_String(void* self)
{ return (char*) self; } 

char* toString_int(void* self)
{ int* p = (int*) self; 
  int x = *p; 
  int y = x; 
  if (x < 0) { y = -x; } 
  int xwidth = (int) ceil(log10(y+1) + 2); 
  char* s = (char*) calloc(xwidth, sizeof(char)); 
  sprintf(s, "%d", x); 
  return s; 
} 

char* toString_long(void* self)
{ long* p = (long*) self; 
  long x = *p; 
  long y = x; 
  if (x < 0) { y = -x; } 
  int xwidth = (int) ceil(log10((double) y+1) + 2); 
  char* s = (char*) calloc(xwidth, sizeof(char)); 
  sprintf(s, "%ld", x); 
  return s; 
} 

char* toString_double(void* self)
{ double* p = (double*) self; 
  double x = *p; 
  char* s = (char*) calloc(26, sizeof(char)); 
  sprintf(s, "%lf", x); 
  return s; 
} 

char* toString_boolean(void* self)
{ unsigned char* p = (unsigned char*) self; 
  unsigned char x = *p; 
  if (x == TRUE)  
  { return "true"; }  
  return "false"; 
} 


unsigned char isInteger(char* s)
{ int x = 0;
  if (sscanf(s, "%d", &x) == 1)
  { char* ss = toString_int(&x); 
    if (strcmp(ss,s) == 0) { return TRUE; }
  } 
  return FALSE;
}

unsigned char isLong(char* s)
{ long x = 0;
  if (sscanf(s, "%ld", &x) == 1)
  { char* ss = toString_long(&x); 
    if (strcmp(ss,s) == 0) { return TRUE; }
  } 
  return FALSE;
}

unsigned char isDouble(char* s)
{ char* tmp = (char*) calloc(26, sizeof(char)); 
  double x = strtod(s, &tmp);
  if (strlen(tmp) > 0)
  { free(tmp); return FALSE; }
  free(tmp); 
  /* printf("%lf\n", x); */   
  return TRUE;
}


int* intSubrange(int a, int b)
{ if (b < a) { return NULL; }
  int n = b-a+1;
  int* result = (int*) calloc(n, sizeof(int));
  int i = 0;
  for ( ; i < n; i++)
  { result[i] = a+i; }
  return result;
}

  int sumint(int col[], int n)
  { int result = 0;
    int i = 0;
    for ( ; i < n; i++)
    { result += col[i]; }
    return result;
  }

  int prdint(int col[], int n)
  { int result = 1;
    int i = 0;
    for ( ; i < n; i++)
    { result *= col[i]; }
    return result;
  }

  long sumlong(long col[], int n)
  { long result = 0;
    int i = 0;
    for ( ; i < n; i++)
    { result += col[i]; }
    return result;
  }

  long prdlong(long col[], int n)
  { long result = 1;
    int i = 0;
    for ( ; i < n; i++)
    { result *= col[i]; }
    return result;
  }

  double sumdouble(double col[], int n)
  { double result = 0;
    int i = 0;
    for ( ; i < n; i++)
    { result += col[i]; }
    return result;
  }

  double prddouble(double col[], int n)
  { double result = 1;
    int i = 0;
    for ( ; i < n; i++)
    { result *= col[i]; }
    return result;
  }

  int intSum(int a, int b, int (*fe)(int))
  { int result = 0;
    int i = a;
    for ( ; i <= b; i++)
    { result += (*fe)(i); }
    return result;
  }

  int intPrd(int a, int b, int (*fe)(int))
  { int result = 1;
    int i = a;
    for ( ; i <= b; i++)
    { result *= (*fe)(i); }
    return result;
  }

  long longSum(int a, int b, long (*fe)(int))
  { long result = 0;
    int i = a;
    for ( ; i <= b; i++)
    { result += (*fe)(i); }
    return result;
  }

  long longPrd(int a, int b, long (*fe)(int))
  { long result = 1;
    int i = a;
    for ( ; i <= b; i++)
    { result *= (*fe)(i); }
    return result;
  }

  double doubleSum(int a, int b, double (*fe)(int))
  { double result = 0;
    int i = a;
    for ( ; i <= b; i++)
    { result += (*fe)(i); }
    return result;
  }

  double doublePrd(int a, int b, double (*fe)(int))
  { double result = 1;
    int i = a;
    for ( ; i <= b; i++)
    { result *= (*fe)(i); }
    return result;
  }

 /* max/min operations assume col.size > 0 */ 

  double maxdouble(double col[], int n)
  { int i = 1;
    double result = col[0];
    for ( ; i < n; i++)
    { if (col[i] > result)
      { result = col[i]; }
    }
    return result;
  }

  double mindouble(double col[], int n)
  { int i = 1;
    double result = col[0];
    for ( ; i < n; i++)
    { if (col[i] < result)
      { result = col[i]; }
    }
    return result;
  }

  int maxint(int col[], int n)
  { int i = 1;
    int result = col[0];
    for ( ; i < n; i++)
    { if (col[i] > result)
      { result = col[i]; }
    }
    return result;
  }

  int minint(int col[], int n)
  { int i = 1;
    int result = col[0];
    for ( ; i < n; i++)
    { if (col[i] < result)
      { result = col[i]; }
    }
    return result;
  }

  long maxlong(long col[], int n)
  { int i = 1;
    long result = col[0];
    for ( ; i < n; i++)
    { if (col[i] > result)
      { result = col[i]; }
    }
    return result;
  }

  long minlong(long col[], int n)
  { int i = 1;
    long result = col[0];
    for ( ; i < n; i++)
    { if (col[i] < result)
      { result = col[i]; }
    }
    return result;
  }

  char* sumString(char* col[])
  { int n = 0;
    int len = length((void**) col);
    int i = 0;
    for ( ; i < len; i++)
    { n += strlen(col[i]); }
    char* result = (char*) calloc(n+1, sizeof(char));
    i = 0;
    for ( ; i < len; i++)
    { result = strcat(result, col[i]); }
    result[n] = '\0';
    return result;
  }

   char* maxString(char* col[])
   { int n = length((void**) col);
     if (n == 0) { return NULL; }
     char* result = col[0];
     int i = 0;
     for ( ; i < n; i++)
     { if (strcmp(result, col[i]) < 0)
       { result = col[i]; }
     }
     return result;
   }

   char* minString(char* col[])
   { int n = length((void**) col);
     if (n == 0) { return NULL; }
     char* result = col[0];
     int i = 0;
     for ( ; i < n; i++)
     { if (strcmp(result, col[i]) > 0)
       { result = col[i]; }
     }
     return result;
   }

  char* insertAtString(char* st1, int ind, char* st2)
  { if (ind <= 0) { return st1; }
    int n = strlen(st1);
    int m = strlen(st2);
    if (m == 0) { return st1; }
    if (ind > n) { ind = n+1; } 

    char* result = (char*) calloc(n + m + 1, sizeof(char));
    int i = 0;
    for ( ; i < ind - 1; i++)
    { result[i] = st1[i]; }
    if (i == ind - 1)
    { int j = 0;
      for ( ; j < m; j++, i++)
      { result[i] = st2[j]; }
      for ( ; i < n + m; i++)
      { result[i] = st1[i - m]; }
    }
    else
    { int j = 0;
      for ( ; j < m; j++, i++)
      { result[i] = st2[j]; }
    }
    result[n+m] = '\0';
    return result;
  }

 int compareTo_String(const void* s1, const void* s2)
 { char* c1 = (char*) s1;
   char* c2 = (char*) s2;
   return strcmp(c1,c2);
 }

int compareTo_int(const void* s1, const void* s2)
{ int* n1 = (int*) s1;
  int* n2 = (int*) s2;
  return *n1 - *n2;
}

int compareTo_long(const void* s1, const void* s2)
{ long* n1 = (long*) s1;
  long* n2 = (long*) s2;
  if (*n1 < *n2) { return -1; }
  if (*n1 == *n2) { return 0; }
  return 1;
}

int compareTo_double(const void* s1, const void* s2)
{ double* n1 = (double*) s1;
  double* n2 = (double*) s2;
  if (*n1 < *n2) { return -1; }
  if (*n1 == *n2) { return 0; }
  return 1;
}

int compareTo_boolean(const void* s1, const void* s2)
{ unsigned char* n1 = (unsigned char*) s1;
  unsigned char* n2 = (unsigned char*) s2;
  return (int) (*n1 - *n2);
}


/* The following implements BSTs for OCL sets, bags and maps */ 

struct ocltnode
{ void* object;
  struct ocltnode* left;
  struct ocltnode* right;
};

void printTree(struct ocltnode* self, char* (*tostr)(void*), int indent)
{ if (self != NULL)
  { printTree(self->left, tostr, indent+1);
    int i = 0; 
    for ( ; i < indent; i++) 
    { printf(" "); } 
    printf("%s\n", (*tostr)(self->object));
    printTree(self->right, tostr, indent+1);
  }
}

int copyTree(struct ocltnode* self, int i, void** res)
{ if (self != NULL)
  { int j = copyTree(self->left, i, res);
    res[j] = self->object;
    j++;
    return copyTree(self->right, j, res);
  }
  return i;
}


/* sorted insertion for bags */ 

struct ocltnode* addToTree(struct ocltnode* self, void* elem, int (*comp)(void*, void*))
{ if (self == NULL)
  { self = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    self->object = elem;
    self->left = NULL;
    self->right = NULL;
  }
  else
  { int cond = (*comp)(elem, self->object);
    if (cond < 0)
    { self->left = addToTree(self->left, elem, comp); }
    else
    { self->right = addToTree(self->right, elem, comp); }
  }
  return self;
}


/* sorted insertion for sets */ 

struct ocltnode* insertIntoTree(struct ocltnode* self, void* elem, int (*comp)(void*, void*))
{ if (self == NULL)
  { self = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    self->object = elem;
    self->left = NULL;
    self->right = NULL;
  }
  else
  { int cond = (*comp)(elem, self->object);
    if (cond < 0)
    { self->left = insertIntoTree(self->left, elem, comp); }
    else if (cond > 0)
    { self->right = insertIntoTree(self->right, elem, comp); }
  }
  return self;
}


/* Binary search */

void* lookupInTree(struct ocltnode* self, void* elem, int (*comp)(void*, void*))
{ if (self == NULL)
  { return NULL; }
  else
  { int cond = (*comp)(elem, self->object);
    if (cond == 0) 
    { return self->object; } 
    else if (cond < 0)
    { return lookupInTree(self->left, elem, comp); }
    else
    { return lookupInTree(self->right, elem, comp); }
  }
}



void** treesort(void* col[], int (*comp)(void*, void*))
{ if (col == NULL) { return NULL; }
  int len = length(col);
  void** res;
  res = (void**) calloc(len+1, sizeof(void*));
  struct ocltnode* tree = NULL;
  int i = 0;
  for ( ; i < len; i++)
  { tree = addToTree(tree, col[i], comp); }

  copyTree(tree, 0, res);
  res[len] = NULL;
  return res;
}


/* Max and Min for sorted sets or bags */

void* oclMax(struct ocltnode* self)
{ if (self == NULL)
  { return NULL; }
  if (self->right == NULL)
  { return self->object; }
  return oclMax(self->right);
}

void* oclMin(struct ocltnode* self)
{ if (self == NULL)
  { return NULL; }
  if (self->left == NULL)
  { return self->object; }
  return oclMin(self->left);
}

void* oclLast(struct ocltnode* self)
{ if (self == NULL)
  { return NULL; }
  if (self->right == NULL)
  { return self->object; }
  return oclLast(self->right);
}

void* oclFirst(struct ocltnode* self)
{ if (self == NULL)
  { return NULL; }
  if (self->left == NULL)
  { return self->object; }
  return oclFirst(self->left);
}


/* General-purpose self->includes(x) for any tree collection */

unsigned char oclIncludes(struct ocltnode* self, void* x)
{ if (self == NULL) { return FALSE; }
  if (self->object == x) { return TRUE; }
  if (oclIncludes(self->left, x)) { return TRUE; }
  return oclIncludes(self->right, x);
}

unsigned char oclExcludes(struct ocltnode* self, void* x)
{ if (self == NULL) { return TRUE; }
  if (self->object == x) { return FALSE; }
  if (oclExcludes(self->left, x)) { }
  else { return FALSE; }
  return oclExcludes(self->right, x);
}

unsigned char oclIncludesAll(struct ocltnode* t1, struct ocltnode* t2)
{ if (t2 == NULL)
  { return TRUE; }
  if (t1 == NULL)
  { return FALSE; }
  unsigned char b1 = oclIncludesAll(t1, t2->left);
  if (b1 == FALSE) { return FALSE; }
  if (oclIncludes(t1, t2->object)) {}
  else { return FALSE; }
  return oclIncludesAll(t1, t2->right);  
}

unsigned char oclExcludesAll(struct ocltnode* t1, struct ocltnode* t2)
{ if (t2 == NULL || t1 == NULL)
  { return TRUE; }
  if (oclIncludes(t1, t2->object)) { return FALSE; }
  if (oclExcludesAll(t1, t2->left))
  { return oclExcludesAll(t1, t2->right); }
  return FALSE; 
}

struct ocltnode* oclFront(struct ocltnode* self)
{ if (self == NULL)
  { return NULL; }
  if (self->right == NULL)
  { return self->left; }
  struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  newnode->object = self->object;
  newnode->left = self->left;
  newnode->right = oclFront(self->right);
  return newnode;
}

struct ocltnode* oclTail(struct ocltnode* self)
{ if (self == NULL)
  { return NULL; }
  if (self->left == NULL)
  { return self->right; }
  struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  newnode->object = self->object;
  newnode->left = oclTail(self->left);
  newnode->right = self->right;
  return newnode;
}

int oclSize(struct ocltnode* self)
{ if (self == NULL)
  { return 0; }
  int n1 = oclSize(self->left);
  int n2 = oclSize(self->right);
  return n1 + 1 + n2;
}

struct ocltnode* oclReverse(struct ocltnode* self)
{ if (self == NULL)
  { return NULL; }
  struct ocltnode* res = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  res->object = self->object;
  res->left = oclReverse(self->right);
  res->right = oclReverse(self->left);
  return res;
}

struct ocltnode* oclSelect(struct ocltnode* self, unsigned char (*f)(void*))
{ if (self == NULL)
  { return NULL; }

  struct ocltnode* resl = oclSelect(self->left, f);
  struct ocltnode* resr = oclSelect(self->right, f);

  if ((*f)(self->object))
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = self->object;
    newnode->left = resl;
    newnode->right = resr; 
    return newnode;
  }
  else if (resr != NULL)
  { if (resl == NULL)
    { return resr; }
    else
    { struct ocltnode* newnode2 = (struct ocltnode*) malloc(sizeof(struct ocltnode));
      newnode2->object = oclFirst(resr);
      newnode2->left = resl;
      newnode2->right = oclTail(resr);
      return newnode2;
    }
  }
  else 
  { return resl; }
}

struct ocltnode* oclReject(struct ocltnode* self, unsigned char (*f)(void*))
{ if (self == NULL)
  { return NULL; }

  struct ocltnode* resl = oclReject(self->left, f);
  struct ocltnode* resr = oclReject(self->right, f);

  if (!((*f)(self->object)))
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = self->object;
    newnode->left = resl;
    newnode->right = resr; 
    return newnode;
  }
  else 
  { if (resl == NULL) { return resr; }
    if (resr == NULL) { return resl; } 
    struct ocltnode* newnode2 = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode2->object = oclFirst(resr);
    newnode2->left = resl;
    newnode2->right = oclTail(resr);
    return newnode2;
  }
}

struct ocltnode* oclCollect(struct ocltnode* self, void* (*f)(void*))
{ if (self == NULL)
  { return NULL; }
  struct ocltnode* res = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  res->object = (*f)(self->object);
  res->left = oclCollect(self->left, f);
  res->right = oclCollect(self->right, f);
  return res;
}

unsigned char oclForAll(struct ocltnode* self, unsigned char (*f)(void*))
{ unsigned char result = TRUE;
  if (self == NULL)
  { return result; }
  if (oclForAll(self->left, f)) {}
  else { return FALSE; }
  if ((*f)(self->object)) {}
  else { return FALSE; }
  return oclForAll(self->right, f);
}

unsigned char oclExists(struct ocltnode* self, unsigned char (*f)(void*))
{ unsigned char result = FALSE;
  if (self == NULL)
  { return result; }
  if (oclExists(self->left, f))
  { return TRUE; }
  if ((*f)(self->object)) 
  { return TRUE; }
  return oclExists(self->right, f);
}

unsigned char oclExists1(struct ocltnode* self, unsigned char (*f)(void*))
{ struct ocltnode* res = oclSelect(self, f);
  int n = oclSize(res);
  if (n == 1) { return TRUE; }
  return FALSE;
}


/* For sets sorted using the default order on objects: */ 

struct ocltnode* oclIncluding(struct ocltnode* self, void* x)
{ struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  newnode->object = x;
  newnode->left = NULL;
  newnode->right = NULL;

  if (self == NULL)
  { return newnode; }
  if (self->object == x)
  { newnode->left = self->left; 
    newnode->right = self->right; 
    return newnode;
  }
  if ((long) self->object < (long) x)
  { newnode->object = self->object;
    newnode->left = oclIncluding(self->left, x);
    newnode->right = self->right; 
    return newnode; 
  }
  else 
  { newnode->object = self->object;
    newnode->left = self->left;
    newnode->right = oclIncluding(self->right, x); 
    return newnode; 
  }
}


/* For unsorted bags: */ 

struct ocltnode* oclIncludingBag(struct ocltnode* self, void* x)
{ struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  newnode->object = x;
  newnode->left = NULL;
  newnode->right = NULL;

  if (self == NULL)
  { return newnode; }
  if (self->left == NULL)
  { self->left = newnode;
    return self;
  }
  if (self->right == NULL)
  { self->right = newnode;
    return self;
  }
  newnode->object = self->object;
  newnode->right = self->right; 
  newnode->left = oclIncludingBag(self->left, x);
  return newnode;
}


/* For unsorted sets: */ 

struct ocltnode* oclIncludingSet(struct ocltnode* self, void* x)
{ struct ocltnode* newnode;
  if (self == NULL)
  { newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = x;
    newnode->left = NULL;
    newnode->right = NULL;
    return newnode;
  }
  if (self->object == x)
  { return self; }
  if (self->left == NULL)
  { self->left = oclIncludingSet(NULL, x);
    return self;
  }
  if (self->right == NULL)
  { self->right = oclIncludingSet(NULL, x);
    return self;
  }
  self->left = oclIncludingSet(self->left, x);
  return self;
}

struct ocltnode* oclConcatenate(struct ocltnode* t1, struct ocltnode* t2)
{ if (t1 == NULL)
  { return t2; }
  if (t2 == NULL)
  { return t1; }
  struct ocltnode* u1 = oclConcatenate(t1->right, t2);
  struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  newnode->object = t1->object;
  newnode->left = t1->left;
  newnode->right = u1;
  return newnode;
}


/* oclUnion, oclSelect, oclReject, oclExcluding, 
   oclIntersection, oclExcludesAll preserve the order 
   of the first argument in the result. */ 

struct ocltnode* oclUnion(struct ocltnode* t1, struct ocltnode* t2)
{ if (t1 == NULL)
  { return t2; }
  if (t2 == NULL)
  { return t1; }
  struct ocltnode* u1 = oclUnion(t1, t2->left);
  struct ocltnode* u2 = oclIncluding(u1, t2->object);
  return oclUnion(u2, t2->right);
}

struct ocltnode* oclUnionBag(struct ocltnode* t1, struct ocltnode* t2)
{ if (t1 == NULL)
  { return t2; }
  if (t2 == NULL)
  { return t1; }
  struct ocltnode* u1 = oclUnionBag(t1, t2->left);
  struct ocltnode* u2 = oclIncludingBag(u1, t2->object);
  return oclUnionBag(u2, t2->right);
}



struct ocltnode* oclExcluding(struct ocltnode* self, void* x)
{ if (self == NULL) { return NULL; }
  struct ocltnode* resl = oclExcluding(self->left, x);
  struct ocltnode* resr = oclExcluding(self->right, x);
  if (self->object != x)
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = self->object;
    newnode->left = resl;
    newnode->right = resr;
    return newnode;
  }
  else
  { if (resl == NULL) { return resr; }
    if (resr == NULL) { return resl; }
    struct ocltnode* newnode2 = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode2->object = oclFirst(resr);
    newnode2->left = resl;
    newnode2->right = oclTail(resr);
    return newnode2;
  }
}

struct ocltnode* oclSubtract(struct ocltnode* self, struct ocltnode* col)
{ if (self == NULL) { return NULL; }
  struct ocltnode* resl = oclSubtract(self->left, col);
  struct ocltnode* resr = oclSubtract(self->right, col);
  if (oclExcludes(col, self->object))
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = self->object;
    newnode->left = resl;
    newnode->right = resr;
    return newnode;
  }
  else
  { if (resl == NULL) { return resr; }
    if (resr == NULL) { return resl; }
    struct ocltnode* newnode2 = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode2->object = oclFirst(resr);
    newnode2->left = resl;
    newnode2->right = oclTail(resr);
    return newnode2;
  }
}

struct ocltnode* oclIntersection(struct ocltnode* self, struct ocltnode* col)
{ if (self == NULL) { return NULL; }
  struct ocltnode* resl = oclIntersection(self->left, col);
  struct ocltnode* resr = oclIntersection(self->right, col);
  if (oclIncludes(col, self->object))
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = self->object;
    newnode->left = resl;
    newnode->right = resr;
    return newnode;
  }
  else
  { if (resl == NULL) { return resr; }
    if (resr == NULL) { return resl; }
    struct ocltnode* newnode2 = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode2->object = oclFirst(resr);
    newnode2->left = resl;
    newnode2->right = oclTail(resr);
    return newnode2;
  }
}


unsigned char oclEquals(struct ocltnode* t1, struct ocltnode* t2)
{ if (t1 == NULL)
  { if (t2 == NULL)
    { return TRUE; }
    return FALSE;
  }
  if (t2 == NULL)
  { if (t1 == NULL)
    { return TRUE; }
    return FALSE;
  }
  if (t1->object != t2->object)
  { return FALSE; }
  if (oclEquals(t1->left, t2->left) == TRUE)
  { return oclEquals(t1->right, t2->right); }
  return FALSE;
}


struct ocltnode* oclAsSet(void* col[])
{ if (col == NULL) { return NULL; }
  int len = length(col);
  struct ocltnode* tree = NULL;
  int i = 0;
  for ( ; i < len; i++)
  { tree = oclIncluding(tree, col[i]); }
  return tree; 
}

void** oclAsSequence(struct ocltnode* tree)
{ if (tree == NULL) { return NULL; }
  int len = oclSize(tree);
  void** res = (void**) calloc(len+1, sizeof(void*));
  
  copyTree(tree, 0, res);
  res[len] = NULL;
  return res;
}


/* Maps, used in the loading of models, for caching, and for 
   object lookup by primary key. */

struct oclmnode
{ char* key;
  void* value;
};


struct ocltnode* insertIntoMap(struct ocltnode* self, char* _key, void* _value)
{ if (self == NULL)
  { struct oclmnode* elem = (struct oclmnode*) malloc(sizeof(struct oclmnode));
    elem->key = _key;
    elem->value = _value;
    struct ocltnode* result = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    result->object = elem;
    result->left = NULL;
    result->right = NULL;
    return result;
  }
  else 
  { struct oclmnode* e = (struct oclmnode*) self->object;
    if (strcmp(_key, e->key) < 0)
    { self->left = insertIntoMap(self->left, _key, _value); }
    else if (strcmp(_key, e->key) == 0)
    { e->value = _value; }
    else
    { self->right = insertIntoMap(self->right, _key, _value); }
  }
  return self;
}

struct ocltnode* insertIntoMapint(struct ocltnode* self, char* _key, int _value)
{ int* elem = (int*) malloc(sizeof(int));
  *elem = _value; 
  return insertIntoMap(self, _key, (void*) elem); 
}

struct ocltnode* insertIntoMaplong(struct ocltnode* self, char* _key, long _value)
{ long* elem = (long*) malloc(sizeof(long));
  *elem = _value; 
  return insertIntoMap(self, _key, (void*) elem); 
}

struct ocltnode* insertIntoMapdouble(struct ocltnode* self, char* _key, double _value)
{ double* elem = (double*) malloc(sizeof(double));
  *elem = _value; 
  return insertIntoMap(self, _key, (void*) elem); 
}


void* lookupInMap(struct ocltnode* self, char* _key)
{ if (self == NULL) 
  { return NULL; }
  struct oclmnode* elem = (struct oclmnode*) self->object;

  if (strcmp(_key, elem->key) == 0)
  { return elem->value; }
  else if (strcmp(_key, elem->key) < 0)
  { return lookupInMap(self->left, _key); }
  else
  { return lookupInMap(self->right, _key); }
} 

struct ocltnode* oclSelectMap(struct ocltnode* self, unsigned char (*f)(void*))
{ if (self == NULL)
  { return NULL; }

  struct ocltnode* resl = oclSelectMap(self->left, f);
  struct ocltnode* resr = oclSelectMap(self->right, f);
  struct oclmnode* node = (struct oclmnode*) self->object;

  if ((*f)(node->value))
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = node;
    newnode->left = resl;
    newnode->right = resr; 
    return newnode;
  }
  else if (resr != NULL)
  { if (resl == NULL)
    { return resr; }
    else
    { struct ocltnode* newnode2 = 
        (struct ocltnode*) malloc(sizeof(struct ocltnode));
      newnode2->object = oclFirst(resr);
      newnode2->left = resl;
      newnode2->right = oclTail(resr);
      return newnode2;
    }
  }
  else 
  { return resl; }
}

struct ocltnode* oclRejectMap(struct ocltnode* self, unsigned char (*f)(void*))
{ if (self == NULL)
  { return NULL; }

  struct ocltnode* resl = oclRejectMap(self->left, f);
  struct ocltnode* resr = oclRejectMap(self->right, f);

  struct oclmnode* maplet = (struct oclmnode*) self->object;

  if (!((*f)(maplet->value)))
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode->object = maplet;
    newnode->left = resl;
    newnode->right = resr; 
    return newnode;
  }
  else 
  { if (resl == NULL) { return resr; }
    if (resr == NULL) { return resl; } 
    struct ocltnode* newnode2 = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    newnode2->object = oclFirst(resr);
    newnode2->left = resl;
    newnode2->right = oclTail(resr);
    return newnode2;
  }
}

struct ocltnode* oclCollectMap(struct ocltnode* self, void* (*f)(void*))
{ if (self == NULL)
  { return NULL; }
  struct ocltnode* res = (struct ocltnode*) malloc(sizeof(struct ocltnode));
  struct oclmnode* maplet = self->object;
  struct oclmnode* emaplet = (struct oclmnode*) malloc(sizeof(struct oclmnode));
  emaplet->key = maplet->key;
  emaplet->value = (*f)(maplet->value);

  res->object = emaplet;
  res->left = oclCollectMap(self->left, f);
  res->right = oclCollectMap(self->right, f);
  return res;
}

struct ocltnode* oclComposeMaps(struct ocltnode* self, struct ocltnode* t)
{ if (self == NULL)
  { return NULL; }

  struct ocltnode* u1 = oclComposeMaps(self->left, t);
  struct ocltnode* u2 = oclComposeMaps(self->right, t);
  struct oclmnode* node = (struct oclmnode*) self->object;

  void* val = lookupInMap(t,(char*) node->value); 

  if (val != NULL)
  { struct ocltnode* newnode = (struct ocltnode*) malloc(sizeof(struct ocltnode));
    struct oclmnode* emaplet = (struct oclmnode*) malloc(sizeof(struct oclmnode));
    emaplet->key = node->key;
    emaplet->value = val;
    newnode->object = emaplet;
    newnode->left = u1;
    newnode->right = u2; 
    return newnode;
  }
  else if (u2 != NULL)
  { if (u1 == NULL)
    { return u2; }
    else
    { struct ocltnode* newnode2 = 
        (struct ocltnode*) malloc(sizeof(struct ocltnode));
      newnode2->object = oclFirst(u2);
      newnode2->left = u1;
      newnode2->right = oclTail(u2);
      return newnode2;
    }
  }
  else 
  { return u1; }
}



unsigned char oclIncludesKey(struct ocltnode* self, char* _key)
{ if (self == NULL)
  { return FALSE; }
  struct oclmnode* elem = (struct oclmnode*) self->object;

  if (strcmp(_key, elem->key) == 0)
  { return TRUE; }
  else if (strcmp(_key, elem->key) < 0)
  { return oclIncludesKey(self->left, _key); }
  else
  { return oclIncludesKey(self->right, _key); }
}

unsigned char oclIncludesValue(struct ocltnode* self, void* _value)
{ if (self == NULL)
  { return FALSE; }
  struct oclmnode* elem = (struct oclmnode*) self->object;

  if (_value == elem->value)
  { return TRUE; }
  else if (oclIncludesValue(self->left, _value))
  { return TRUE; }
  else
  { return oclIncludesValue(self->right, _value); }
}

struct ocltnode* oclIncludingMap(struct ocltnode* m, char* key, void* value)
{ return insertIntoMap(m,key,value); }
 
char** oclKeyset(struct ocltnode* m)
{ struct oclmnode** maplets = (struct oclmnode**) oclAsSequence(m);
  int n = length(maplets);
  char** res = (char**) calloc(n+1, sizeof(char*));
  int x = 0;
  for ( ; x < n; x++)
  { struct oclmnode* mx = maplets[x];
    res[x] = mx->key;
  }
  res[x] = NULL;
  return res;
}

void** oclValues(struct ocltnode* m)
{ struct oclmnode** maplets = (struct oclmnode**) oclAsSequence(m);
  int n = length(maplets);
  void** res = calloc(n+1, sizeof(void*));
  int x = 0;
  for ( ; x < n; x++)
  { struct oclmnode* mx = maplets[x];
    res[x] = mx->value;
  }
  res[x] = NULL;
  return res;
}

void* oclAtMap(struct ocltnode* m, char* key)
{ return lookupInMap(m,key); } 


struct ocltnode* oclUnionMap(struct ocltnode* t1, struct ocltnode* t2)
{ if (t1 == NULL)
  { return t2; }
  if (t2 == NULL)
  { return t1; }

  struct ocltnode* u1 = oclUnionMap(t1, t2->left);

  struct ocltnode* u2 = oclUnionMap(u1, t2->right);

  struct oclmnode* elem = (struct oclmnode*) t2->object;

  return insertIntoMap(u2, elem->key, elem->value);
}

struct ocltnode* oclIntersectionMap(struct ocltnode* t1, struct ocltnode* t2)
{ if (t1 == NULL)
  { return NULL; }
  if (t2 == NULL)
  { return NULL; }

  struct ocltnode* u1 = oclIntersectionMap(t1->left, t2);

  struct ocltnode* u2 = oclIntersectionMap(t1->right, t2);

  struct ocltnode* u = oclUnionMap(u1,u2); 

  struct oclmnode* elem = (struct oclmnode*) t1->object;
  void* v = lookupInMap(t2, elem->key); 
  if (v == NULL)
  { return u; } 
  else if (v != elem->value)
  { return u; } 
  return insertIntoMap(u, elem->key, elem->value);
}

struct ocltnode* oclRestrictMap(struct ocltnode* t, char* keys[])
{ /* the elements of t which have key in keys */ 

  if (t == NULL) 
  { return NULL; } 

  struct ocltnode* u1 = oclRestrictMap(t->left, keys);
  struct ocltnode* u2 = oclRestrictMap(t->right, keys);
  struct ocltnode* u = oclUnionMap(u1,u2); 

  struct oclmnode* elem = (struct oclmnode*) t->object;

  if (isIn(elem->key,keys))
  { return insertIntoMap(u, elem->key, elem->value); }
  return u; 
}

struct ocltnode* oclAntirestrictMap(struct ocltnode* t, char* keys[])
{ /* the elements of t which do not have key in keys */ 

  if (t == NULL) 
  { return NULL; } 

  struct ocltnode* u1 = oclAntirestrictMap(t->left, keys);
  struct ocltnode* u2 = oclAntirestrictMap(t->right, keys);
  struct ocltnode* u = oclUnionMap(u1,u2); 

  struct oclmnode* elem = (struct oclmnode*) t->object;

  if (isIn(elem->key,keys))
  { return u; } 
  return insertIntoMap(u, elem->key, elem->value); 
}

struct ocltnode* oclSubtractMap(struct ocltnode* m1, struct ocltnode* m2)
{ /* Elements of m1 whose key is not in m2->keys() */
  char** keyset2 = oclKeyset(m2); 
  struct ocltnode* result = oclAntirestrictMap(m1,keyset2); 
  return result; 
} 
 

/* Utility functions to parse strings and build formats */ 

unsigned char isCSVseparator(char c)
{ if (c == ',') { return TRUE; } 
  if (c == ';') { return TRUE; } 
  return FALSE; 
} 

char** tokenise(char* str, unsigned char (*isseperator)(char)) 
{ int maxtokens = strlen(str); 
  if (maxtokens == 0) 
  { return NULL; } 

  char** tokens = (char**) calloc(maxtokens, sizeof(char*)); 
  char* token = (char*) malloc(maxtokens*sizeof(char)); 
  int resultcount = 0; 

  int i = 0;
  int j = 0; 
 
  for ( ; i < maxtokens; i++) 
  { 
    char c = str[i]; 
    if (!(*isseperator)(c))
    { token[j] = c; 
      j++; 
    } 
    else 
    { token[j] = '\0'; 
      tokens[resultcount] = token;
      resultcount++; 

      /* printf("Extracted token %s\n", token); */ 
 
      while ((*isseperator)(str[i]) && i < maxtokens)
      { i++; }
      if (i >= maxtokens) 
      { tokens[resultcount] = NULL; 
        return tokens; 
      } 
      else 
      { token = (char*) malloc(maxtokens*sizeof(char));
        token[0] = str[i]; 
        j = 1; 
      } 
    }
  } 

  token[j] = '\0'; 
  tokens[resultcount] = token; 
  /* printf("Extracted token %s\n", token); */  
  resultcount++; 
  tokens[resultcount] = NULL; 

  /* printf("Parsed %d tokens\n", resultcount); */  
  return tokens;  
} 




char** getFileLines(char* file)
{ FILE* f = fopen(file,"r"); 
  if (f == NULL) 
  { return NULL; }
  int MAXLINES = 100; 
  int MAXLINE = 1024; 

  char** lines = (char**) calloc(MAXLINES, sizeof(char*));
  int linecount = 0; 
 
  char* line = (char*) malloc(MAXLINE*sizeof(char));
  char* eof = fgets(line,MAXLINE,f); 
  while (eof != NULL) 
  { lines[linecount] = line; 
    linecount++; 
    /* printf("Extracted line %s", line); */ 
    line = (char*) malloc(MAXLINE*sizeof(char));
    eof = fgets(line,MAXLINE,f); 
  } 
  fclose(f);
  lines[linecount] = NULL; 
  /* printf("Extracted %d lines\n", linecount); */  
  return lines; 
}  

char* buildFormat(char** tokens) 
{ int i = 0; 
  int tn = length(tokens);
  char* res = (char*) malloc(tn*4*sizeof(char));  
  int rind = 0; 
  res[0] = '%'; 
  res[1] = 's';
  res[2] = ' '; 
  rind = 3; 

  for ( ; i < tn-2; i++) 
  { char* tok = tokens[i];
    if (strcmp("int",tok) == 0)
    { res[rind] = '%'; 
      res[rind+1] = 'd'; 
      res[rind+2] = ' ';
      rind = rind + 3;
    } 
    else if (strcmp("double",tok) == 0)
    { res[rind] = '%'; 
      res[rind+1] = 'l'; 
      res[rind+2] = 'f';
      res[rind+3] = ' ';
      rind = rind + 4;
    } 
    else if (strcmp("long",tok) == 0)
    { res[rind] = '%'; 
      res[rind+1] = 'l'; 
      res[rind+2] = 'd';
      res[rind+3] = ' ';
      rind = rind + 4;
    } 
    else if (strcmp("String",tok) == 0)
    { res[rind] = '%'; 
      res[rind+1] = 's'; 
      res[rind+2] = ' ';
      rind = rind + 3;
    } 
    else if (strcmp("boolean",tok) == 0)
    { res[rind] = '%'; 
      res[rind+1] = 's'; 
      res[rind+2] = ' ';
      rind = rind + 3;
    } 
    else
    { res[rind] = '%'; 
      res[rind+1] = 'd'; 
      res[rind+2] = ' ';
      rind = rind + 3;
    } 
  } 
  res[rind] = '\0'; 
  return res; 
} 

