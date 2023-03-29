#include <stdio.h>
#include <stdarg.h>

/* C header for StringLib library */

char* ncopies_StringLib(int n, char* c)
{ /* n copies of c */ 

  int clen = strlen(c);
  char* res = (char*) calloc(n*clen + 1, sizeof(char)); 
  res = ""; 

  int i = 0; 
  for ( ; i < n; i++) 
  { res = concatenateStrings(res, c); } 
  
  return res; 
} 
 
char* leftTrim_StringLib(char* s)
{ /* remove leading whitespace */ 

  int n = strlen(s); 
  char* res = (char*) calloc(n+1, sizeof(char)); 
  int i = 0;
  int j = 0;  

  while (i < n && isspace(s[i])) 
  { i++; }
 
  while (i < n)
  { char x = s[i]; 
    res[j] = x; 
    j++;  
    i++; 
  } 

  res[j] = '\0'; 
  return res; 
} 

char* rightTrim_StringLib(char* s)
{ /* remove trailing whitespace */ 

  int n = strlen(s); 
  char* res = (char*) calloc(n+1, sizeof(char)); 
  int i = n-1;
  int j = 0;  

  while (i > 0 && isspace(s[i])) 
  { i--; }
 
  while (j <= i)
  { res[j] = s[j]; 
    j++;  
  } 

  res[j] = '\0'; 
  return res; 
} 

char* padLeftWithInto_StringLib(char* s, char* c, int n)
{ char* result = "";
  char* ncs = ncopies_StringLib(n - strlen(s), c); 
  result = concatenateStrings(ncs, s);
  return result;
}

char* leftAlignInto_StringLib(char* s, int n)
{ char* result = "";
  int slen = strlen(s); 
  if (n <= slen)
  { result = subStrings(s, 1, n); }
  else 
  { if (n > strlen(s))
    { char* nspaces = ncopies_StringLib(n - slen, " ");
      result = concatenateStrings(s, nspaces);
    }
  }
  return result;
}

char* rightAlignInto_StringLib(char* s, int n)
{ char* result = "";
  int slen = strlen(s); 
  if (n <= slen)
  { result = subStrings(s, 1, n); }
  else 
  { if (n > slen)
    { result = concatenateStrings(ncopies_StringLib(n - slen, " "), s); }
  }
  return result;
}


char* format_StringLib(char* f, void* sq[])
{ int n = length(sq);

  int flength = strlen(f); 
  if (n == 0 || flength == 0) 
  { return f; }

  int ressize = flength*(n+1) + 50;
  char* res = (char*) calloc(ressize, sizeof(char)); 
  int resindex = 0; 

  void* ap = NULL; 
  char* p = f; 
  int i = 0; 

  char* sval; 
  int ival;
  unsigned int uval;  
  double dval; 

  ap = sq[0]; 

  for ( ; *p != '\0'; p++)
  { if (*p != '%')
    { char* p0 = subStrings(p,1,1); 
      res = strcat(res, p0);
      resindex++;  
      continue; 
    } 
 
    if (resindex >= ressize)
    { res = (char*) realloc(res, ressize*2); }

    char* tmp = (char*) calloc(flength+1, sizeof(char)); 
    tmp[0] = '%';
    int k = 1; 
    p++;  
    while (*p != 'i' && *p != 'd' && *p != 'g' && *p != 'e' &&
           *p != 'o' && *p != 'x' && *p != 'X' && *p != 'E' &&
           *p != 'G' && *p != 's' &&  *p != '%' && 
           *p != 'u' && *p != 'c' && *p != 'f' && *p != 'p')
    { tmp[k] = *p; 
      k++; 
      p++;
    }  /* Now p points to flag after % */ 

    tmp[k] = *p; 
    tmp[k+1] = '\0';
   
    if (i >= n) 
    { continue; }

    switch (*p)
    { case 'i' : 
        ival = *((int*) sq[i]); 
        i++; 
        char* istr = calloc(15, sizeof(char));  
        int ichrs = sprintf(istr, tmp, ival);
        res = strcat(res,istr); 
        resindex += ichrs;
        break; 
      case 'o' : 
      case 'x' : 
      case 'X' : 
      case 'u' : 
        uval = *((unsigned int*) sq[i]); 
        i++; 
        char* ustr = calloc(15, sizeof(char));  
        int uchrs = sprintf(ustr, tmp, uval);
        res = strcat(res,ustr); 
        resindex += uchrs; 
        break; 
      case 'c' : 
      case 'd' : 
        ival = *((int*) sq[i]); 
        i++; 
        char* dstr = calloc(15, sizeof(char));  
        int dchrs = sprintf(dstr, tmp, ival);
        res = strcat(res,dstr); 
        resindex += dchrs; 
        break; 
      case 'f' : 
      case 'e' : 
      case 'E' : 
      case 'g' : 
      case 'G' : 
        dval = *((double*) sq[i]); 
        i++; 
        char* fstr = calloc(30, sizeof(char));  
        int fchrs = sprintf(fstr, tmp, dval);
        res = strcat(res,fstr); 
        
        resindex += fchrs;  
        break; 
      case 's' : 
        sval = ((char*) sq[i]); 
        i++; 
        char* strval = calloc(strlen(sval)+1,sizeof(char)); 
        int schrs = sprintf(strval, tmp, sval); 
        res = strcat(res,strval); 
        resindex += schrs; 
        break;
      case 'p' : 
        i++; 
        char* pstr = calloc(15, sizeof(char));  
        int pchrs = sprintf(pstr, tmp, sq[i]);
        res = strcat(res,pstr); 
        resindex += pchrs;  
        break; 
      default : 
        char* p0 = subStrings(p,1,1); 
        res = strcat(res,p0);
        resindex++; 
        break; 
    }
  } 
  return res; 
}

void** scan_StringLib(char* ss, char* f)
{ int n = countStrings('%',f);
  void** sq = calloc(n+1, sizeof(void*)); 

  int flength = strlen(f); 
  int slength = strlen(ss); 
  if (n == 0 || flength == 0 || slength == 0) 
  { return sq; }

  int resindex = 0; 
  
  char* p = f; /* fpointer */ 
  char* spointer = ss; 
 
  int i = 0; 

  for ( ; *p != '\0'; p++)
  { /* printf("Input pointer: %s Format pointer: %s\n", spointer, p); */ 
  
    if (*p != '%')
    { if (*spointer == *p)
      { spointer++; }
      else 
      { return sq; }
      continue; 
    } 

    /* *p is % */ 
 
    char* tmp = (char*) calloc(flength+1, sizeof(char)); 
    tmp[0] = '%';
    int k = 1; 
    p++;  
    while (*p != 'i' && *p != 'd' && *p != 'g' && *p != 'e' &&
           *p != 'o' && *p != 'x' && *p != 'X' && *p != 'E' &&
           *p != 'G' && *p != 's' &&  *p != '%' && 
           *p != 'u' && *p != 'c' && *p != 'f' && *p != 'p')
    { tmp[k] = *p; 
      k++; 
      p++;
    } 

    tmp[k] = *p; 
    tmp[k+1] = '\0';
   
    /* printf("%d arguments of list %d\n", i, n); */

    if (i >= n) 
    { return sq; }


    /* printf("Extracting from %s with %s\n", spointer, tmp); */ 

    switch (*p)
    { case 'o' : 
      case 'x' : 
      case 'X' : 
      case 'i' : 
      case 'd' : 
        if (endsWith(tmp,"ld") == TRUE || endsWith(tmp,"li") == TRUE)  
        { long* lptr = (long*) malloc(sizeof(long)); 
          int lchrs = sscanf(spointer, tmp, lptr);
          /* printf("Scanned %s with %s: %ld\n", spointer, tmp, *lptr); */ 
          if (lchrs > 0)
          { char* ltmp = (char*) calloc(slength,sizeof(char)); 
            sprintf(ltmp,"%ld",*lptr); 
            spointer = spointer + strlen(ltmp); 
          }
          else 
          { spointer++; } 
          sq[i] = lptr; 
          i++;
        }
        else 
        { int* iptr = (int*) malloc(sizeof(int)); 
          int ichrs = sscanf(spointer, tmp, iptr);
          /* printf("Scanned %s with %s: %d\n", spointer, tmp, *iptr);  */
          if (ichrs > 0)
          { char* itmp = (char*) calloc(slength,sizeof(char)); 
            sprintf(itmp,"%d",*iptr); 
            spointer = spointer + strlen(itmp); 
          }
          else 
          { spointer++; } 
          sq[i] = iptr; 
          i++;
        }  
        break; 
      case 'u' : 
        { unsigned int *uptr = (unsigned int*) malloc(sizeof(unsigned int)); 
          int uchrs = sscanf(spointer, tmp, uptr);
          if (uchrs > 0)
          { char* utmp = (char*) calloc(slength,sizeof(char)); 
            sprintf(utmp,"%u",*uptr); 
            spointer = spointer + strlen(utmp); 
          }
          else 
          { spointer++; } 
          sq[i] = uptr; 
          i++;
        }  
        break; 
      case 'f' : 
      case 'e' : 
      case 'E' : 
      case 'g' : 
      case 'G' : 
        { double* fptr = (double*) malloc(sizeof(double)); 
          int fchrs = sscanf(spointer, tmp, fptr);
          /* printf("Scanned %s with %s: %f\n", spointer, tmp, *fptr); */   
          if (fchrs > 0)
          { 
            /* printf("%c\n", spointer[0]); */   
            while (isdigit(spointer[0]) || '.' == spointer[0])
            { spointer++; 
              /* printf("%c\n", spointer[0]); */
            } 
          }
          else 
          { spointer++; } 
          sq[i] = fptr; 
          i++;
        }
        break; 
      case 'c' : 
      case 's' : 
        { char* sptr = (char*) calloc(slength+1, sizeof(char)); 
          int schrs = sscanf(spointer, tmp, sptr);
          if (schrs > 0)
          { spointer = spointer + strlen(sptr); }
          else 
          { spointer++; } 
          sq[i] = sptr; 
          i++;
        }  
        break;
      case 'p' : 
        { void* pptr = (void*) malloc(sizeof(long)); 
          int pchrs = sscanf(spointer, tmp, pptr);
          if (pchrs > 0)
          { char* ptmp = (char*) calloc(slength,sizeof(char)); 
            sprintf(ptmp,"%p",pptr); 
            spointer = spointer + strlen(ptmp); 
          }
          else 
          { spointer++; } 
          sq[i] = pptr; 
          i++;
        } 
        break; 
      default : 
        break; 
    }
  } 
  sq[i] = NULL; 
  return sq; 
}
