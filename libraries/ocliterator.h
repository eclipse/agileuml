
/* C header for OclIterator library component */
/* Requires ocl.h                             */ 


struct OclIteratorResult { 
  void* value; 
  unsigned char done; 
};

struct OclIteratorResult* newOclIteratorResult(void* v)
{ struct OclIteratorResult* res = (struct OclIteratorResult*) malloc(sizeof(struct OclIteratorResult)); 
  res->value = v; 
  if (v == NULL)
  { res->done = TRUE; } 
  else 
  { res->done = FALSE; } 
  return res; 
}

struct OclIterator {
  void** elements; 
  int position;
  int markedPosition; 
  int size;
  char** columnNames; 
  void* (*generatorFunction)(int);   
};

struct OclIterator* newOclIterator_Sequence(void* col[])
{ struct OclIterator* iter = (struct OclIterator*) malloc(sizeof(struct OclIterator)); 
  iter->elements = col; 
  iter->size = length(col);
  iter->position = 0; 
  iter->markedPosition = 0;
  iter->columnNames = NULL;  
  return iter; 
}

struct OclIterator* newOclIterator_Set(void* col[])
{ struct OclIterator* iter = (struct OclIterator*) malloc(sizeof(struct OclIterator)); 
  iter->elements = treesort(col, compareTo_OclAny); 
  iter->size = length(col);
  iter->position = 0; 
  iter->columnNames = NULL;  
  return iter; 
}

struct OclIterator* newOclIterator_String(char* s)
{ struct OclIterator* iter = (struct OclIterator*) malloc(sizeof(struct OclIterator)); 
  iter->elements = split(s, " \n\t\r"); 
  iter->size = length(iter->elements);
  iter->position = 0; 
  iter->columnNames = NULL;  
  return iter; 
}

struct OclIterator* newOclIterator_String_String(char* s, char* seps)
{ struct OclIterator* iter = (struct OclIterator*) malloc(sizeof(struct OclIterator)); 
  iter->elements = split(s, seps); 
  iter->size = length(iter->elements);
  iter->position = 0; 
  iter->columnNames = NULL;  
  return iter; 
}

struct OclIterator* newOclIterator_Function(void* (*f)(int))
{ struct OclIterator* iter = (struct OclIterator*) malloc(sizeof(struct OclIterator)); 
  iter->elements = NULL; 
  iter->size = 0;
  iter->position = 0; 
  iter->markedPosition = 0;
  iter->generatorFunction = f;  
  iter->columnNames = NULL;  
  return iter; 
}


unsigned char hasNext_OclIterator(struct OclIterator* self)
{ if (self->position >= 0 && self->position < self->size)
  { return TRUE; }
  return FALSE; 
} 

unsigned char isAfterLast_OclIterator(struct OclIterator* self)
{ if (self->position > self->size)
  { return TRUE; }
  return FALSE; 
} 

unsigned char hasPrevious_OclIterator(struct OclIterator* self)
{ if (self->position > 1 && self->position <= self->size)
  { return TRUE; }
  return FALSE; 
} 

unsigned char isBeforeFirst_OclIterator(struct OclIterator* self)
{ if (self->position <= 0)
  { return TRUE; }
  return FALSE; 
} 

void markPosition_OclIterator(struct OclIterator* self)
{ self->markedPosition = self->position; }  
 

int nextIndex_OclIterator(struct OclIterator* self)
{ return self->position + 1; } 

int previousIndex_OclIterator(struct OclIterator* self)
{ return self->position - 1; } 

void moveForward_OclIterator(struct OclIterator* self)
{ if (self->position <= self->size)
  { self->position = self->position + 1; }
}

void moveBackward_OclIterator(struct OclIterator* self)
{ if (self->position >= 1)
  { self->position = self->position - 1; }
}

void moveTo_OclIterator(struct OclIterator* self, int i)
{ if (0 <= i && i <= self->size + 1)
  { self->position = i; }
}

void setPosition_OclIterator(struct OclIterator* self, int i)
{ if (0 <= i && i <= length(self->elements) + 1)
  { self->position = i; }
}

void moveToMarkedPosition_OclIterator(struct OclIterator* self)
{ self->position = self->markedPosition; }  

void movePosition_OclIterator(struct OclIterator* self, int i)
{ int pos = self->position; 

  if (0 <= i + pos && i + pos <= length(self->elements) + 1)
  { self->position = i + pos; }
}

void moveToFirst_OclIterator(struct OclIterator* self)
{ self->position = 1; }

void moveToStart_OclIterator(struct OclIterator* self)
{ self->position = 0; }

void moveToLast_OclIterator(struct OclIterator* self)
{ self->position = length(self->elements); }

void moveToEnd_OclIterator(struct OclIterator* self)
{ self->position = length(self->elements) + 1; }

void* getCurrent_OclIterator(struct OclIterator* self)
{ if (self->position >= 1 && self->position <= length(self->elements))
  { return (self->elements)[self->position - 1]; }
  return NULL; 
}

void set_OclIterator(struct OclIterator* self, void* x)
{ if (self->position >= 1 && self->position <= self->size)
  { (self->elements)[self->position - 1] = x; }
}

void remove_OclIterator(struct OclIterator* self)
{ /* overwrite elements[position-1] by elements[position], etc */ 

  if (self->position < 1 || self->position > self->size)
  { return; }

  void** col = self->elements; 
  int n = length(col);
  int i = self->position-1; 
  for ( ; i < n-1; i++)
  { col[i] = col[i+1]; }
  col[i] = NULL;
  self->size = n - 1;
}

void insert_OclIterator(struct OclIterator* self, void* x)
{ /* store x in elements[position-1] and shuffle following elements along 1
     step. No reallocation needed if self->size < ALLOCATIONSIZE */ 

  if (self->position < 1 || self->position > self->size + 1)
  { return; }

  if (self->size + 1 >= ALLOCATIONSIZE) 
  { return; }

  void** col = self->elements; 
  int n = length(col);
  int i = n-1; 
  for ( ; i >= self->position - 1; i--)
  { col[i+1] = col[i]; }
  col[n+1] = NULL;
  col[self->position - 1] = x; 
  self->size = n + 1;
}

void* next_OclIterator(struct OclIterator* self)
{ if (self->position < self->size)
  { self->position = self->position + 1;
    return (self->elements)[self->position - 1]; 
  }
  return NULL; 
}

void* nextResult_OclIterator(struct OclIterator* self)
{ if (self->generatorFunction != NULL) 
  { void* v = (self->generatorFunction)(self->position); 
    self->position = self->position + 1; 
    if (self->position <= length(self->elements)) 
    { set_OclIterator(self,v); } 
    else
    { self->elements = append(self->elements,v); }
    return newOclIteratorResult(v); 
  } 

  if (self->position < self->size)
  { self->position = self->position + 1;
    void* val = (self->elements)[self->position - 1];
    return newOclIteratorResult(val);  
  }

  return NULL; 
}

void* previous_OclIterator(struct OclIterator* self)
{ if (self->position > 1)
  { self->position = self->position - 1;
    return (self->elements)[self->position - 1]; 
  }
  return NULL; 
}

void* at_OclIterator(struct OclIterator* self, int i)
{ if (i >= 1 && i <= length(self->elements))
  { return (self->elements)[i-1]; }
  return NULL; 
}

int length_OclIterator(struct OclIterator* self)
{ return length(self->elements); } 

void close_OclIterator(struct OclIterator* self)
{ self->elements = NULL; 
  self->position = 0; 
} 

int getPosition_OclIterator(struct OclIterator* self)
{ return self->position; } 

int getColumnCount_OclIterator(struct OclIterator* self)
{ return length(self->columnNames); } 

char* getColumnName_OclIterator(struct OclIterator* self, int i)
{ return self->columnNames[i-1]; } 

void* getCurrentFieldByIndex_OclIterator(struct OclIterator* self, int i)
{ struct ocltnode* mm = (struct ocltnode*) getCurrent_OclIterator(self);
  if (mm != NULL)
  { char* nme = self->columnNames[i-1]; 
    void* val = lookupInMap(mm,nme); 
    return val; 
  } 
  return NULL; 
} 
 
 
