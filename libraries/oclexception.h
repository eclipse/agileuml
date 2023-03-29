struct OclException
{ char* message; 
  char* kind; 
  struct OclException* cause; 
}; 

struct OclException** oclexception_instances = NULL;

struct OclException** raised_exceptions = NULL;

int oclexception_size = 0;

struct OclException** newOclExceptionList(void)
{ return (struct OclException**) calloc(ALLOCATIONSIZE, sizeof(struct OclException*)); }

char* getOclException_message(struct OclException* self)
{ return self->message; }


char* getOclException_kind(struct OclException* self)
{ return self->kind; }


struct OclException* getOclException_cause(struct OclException* self)
{ return self->cause; }


void setOclException_message(struct OclException* self, char* _value)
{ self->message = _value; }


void setOclException_kind(struct OclException* self, char* _value)
{ self->kind = _value; }


void setOclException_cause(struct OclException* self, struct OclException* _value)
{ self->cause = _value; }


char** getAllOclException_message(struct OclException* _col[])
{ int n = length((void**) _col);
  char** result = (char**) calloc(n+1, sizeof(char*));
  int i = 0;
  for ( ; i < n; i++)
  { result[i] = getOclException_message(_col[i]); }
  result[n] = NULL;
  return result;
}

char** getAllOclException_kind(struct OclException* _col[])
{ int n = length((void**) _col);
  char** result = (char**) calloc(n+1, sizeof(char*));
  int i = 0;
  for ( ; i < n; i++)
  { result[i] = getOclException_kind(_col[i]); }
  result[n] = NULL;
  return result;
}

struct OclException** getAllOclException_cause(struct OclException* _col[])
{ int n = length((void**) _col);
  struct OclException** result = (struct OclException**) calloc(n+1, sizeof(struct OclException*));
  int i = 0;
  for ( ; i < n; i++)
  { result[i] = getOclException_cause(_col[i]); }
  result[n] = NULL;
  return result;
}

struct OclException** appendOclException(struct OclException* col[], struct OclException* ex)
   { struct OclException** result;
     int len = length((void**) col);
     if (len % ALLOCATIONSIZE == 0)
     { result = (struct OclException**) calloc(len + ALLOCATIONSIZE + 1, sizeof(struct OclException*));
       int i = 0;
       for ( ; i < len; i++) { result[i] = col[i]; }
     }
    else { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

struct OclException* createOclException(void)
{ struct OclException* result = (struct OclException*) malloc(sizeof(struct OclException));
  result->message = NULL;
  result->kind = NULL;
  result->cause = NULL;
  oclexception_instances = appendOclException(oclexception_instances, result);
  oclexception_size++;
  return result;
}

struct OclException* raiseOclException(char* typ)
{ struct OclException* result = createOclException();
  result->kind = typ;
  raised_exceptions = appendOclException(raised_exceptions, result); 
  return result;
} 

struct OclException* getRaisedOclException(char* typ)
{ struct OclException* ex = NULL;
  int n = length(raised_exceptions);
  int i = n-1; 

  for ( ; i >= 0; i--)
  { ex = raised_exceptions[i];
    if (ex != NULL && strcmp(getOclException_kind(ex),typ) == 0) 
    { return ex; }
  }

  return ex;
} 


struct OclException** insertOclException(struct OclException* col[], struct OclException* self)
  { if (isIn((void*) self, (void**) col))
    { return col; }
    return appendOclException(col,self);
  }

unsigned char existsOclException(struct OclException* col[], unsigned char (*test)(struct OclException* ex))
 { int n = length((void**) col);
   unsigned char result = FALSE;
   int i = 0;
   for ( ; i < n; i++)
   { struct OclException* ex = col[i];
     if (ex == NULL) { return result; }
     if ((*test)(ex))
     { return TRUE; }
   }
   return result;
 }

  struct OclException** subrangeOclException(struct OclException** col, int i, int j)
  { int len = length((void**) col);
    if (i > j || j > len) { return NULL; }
    struct OclException** result = (struct OclException**) calloc(j - i + 2, sizeof(struct OclException*));
     int k = i-1;
     int l = 0;
     for ( ; k < j; k++, l++)
     { result[l] = col[k]; }
    result[l] = NULL;
    return result;
  }

  struct OclException** reverseOclException(struct OclException** col)
  { int n = length((void**) col);
    struct OclException** result = (struct OclException**) calloc(n+1, sizeof(struct OclException*));
    int i = 0;
    int x = n-1;
    for ( ; i < n; i++, x--)
    { result[i] = col[x]; }
    result[n] = NULL;
    return result;
  }

struct OclException** removeOclException(struct OclException* col[], struct OclException* ex)
{ int len = length((void**) col);
  struct OclException** result = (struct OclException**) calloc(len+1, sizeof(struct OclException*));
  int j = 0;
  int i = 0;
  for ( ; i < len; i++)
  { struct OclException* eobj = col[i];
    if (eobj == NULL)
    { result[j] = NULL;
      return result; 
    }
    if (eobj == ex) { }
    else
    { result[j] = eobj; j++; }
  }
  result[j] = NULL;
  return result;
}

struct OclException** removeAllOclException(struct OclException* col1[], struct OclException* col2[])
{ int n = length((void**) col1);
  struct OclException** result = (struct OclException**) calloc(n+1, sizeof(struct OclException*));
  int i = 0; int j = 0;
  for ( ; i < n; i++)
  { struct OclException* ex = col1[i];
    if (isIn((void*) ex, (void**) col2)) {}
    else 
    { result[j] = ex; j++; }
  }
  result[j] = NULL;
  return result;
}

struct OclException** frontOclException(struct OclException* col[])
{ int n = length((void**) col);
  return subrangeOclException(col, 1, n-1);
}

struct OclException** tailOclException(struct OclException* col[])
{ int n = length((void**) col);
  return subrangeOclException(col, 2, n);
}

void printStackTrace_OclException(struct OclException* self)
{  displayString(getOclException_message(self));
}

char* getMessage_OclException(struct OclException* self)
{  char* result = "";
  result = getOclException_message(self);
  return result;
}

struct OclException* getCause_OclException(struct OclException* self)
{  struct OclException* result = NULL;
  result = getOclException_cause(self);
  return result;
}

struct OclException* newOclException(void)
{  struct OclException* result = NULL;
  struct OclException* ex = createOclException();
  setOclException_message(ex, "");
  setOclException_cause(ex, NULL);
  result = ex;
  return result;
}

void killOclException(struct OclException* oclexception_x)
{  oclexception_instances = removeOclException(oclexception_instances, oclexception_x);
  free(oclexception_x);
}


