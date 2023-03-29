/* C header for OclType library */

struct OclAttribute { 
  char* name; 
  struct OclType* type; 
}; 

struct OclOperation { 
  char* name; 
  struct OclType* type; 
  struct OclAttribute** parameters; 
}; 

struct OclType {
  char* name; 
  struct OclAttribute** attributes; 
  struct OclOperation** operations; 
  struct OclOperation** constructors; 
  struct OclType* superclass;
  char** stereotypes;
  void* (*creator)(void);  
};


char* getName_OclAttribute(struct OclAttribute* self)
{ return self->name; } 

struct OclType* getType_OclAttribute(struct OclAttribute* self)
{ return self->type; } 

char* getName_OclOperation(struct OclOperation* self)
{ return self->name; } 

struct OclType* getType_OclOperation(struct OclOperation* self)
{ return self->type; } 

struct OclType* getReturnType_OclOperation(struct OclOperation* self)
{ return self->type; } 

struct OclAttribute** getParameters_OclOperation(struct OclOperation* self)
{ return self->parameters; } 


char* getName_OclType(struct OclType* self)
{ return self->name; } 

unsigned char isArray_OclType(struct OclType* self)
{ if (strcmp(self->name,"Sequence") == 0)
  { return TRUE; } 
  return FALSE; 
} 

unsigned char isPrimitive_OclType(struct OclType* self)
{ if (strcmp(self->name,"int") == 0 ||
      strcmp(self->name,"long") == 0 ||
      strcmp(self->name,"double") == 0 ||
      strcmp(self->name,"boolean") == 0)
  { return TRUE; } 
  return FALSE; 
} 

struct OclType* getSuperclass_OclType(struct OclType* self)
{ return self->superclass; } 

unsigned char isAssignableFrom_OclType(struct OclType* self, struct OclType* other)
{ /* other = self or is a descendant of self */ 

  if (strcmp(self->name,other->name) == 0) 
  { return TRUE; } 
  if (other->superclass == NULL)
  { return FALSE; } 
  return isAssignableFrom_OclType(self,other->superclass); 
} 

void* newInstance_OclType(struct OclType* self)
{ if (self->creator != NULL) 
  { return (self->creator)(); }
  return NULL; 
} 

struct OclAttribute** getFields_OclType(struct OclType* self)
{ return self->attributes; } 

struct OclAttribute** allAttributes_OclType(void* obj)
{ struct ocltnode* tree = (struct ocltnode*) obj; 
  if (tree == NULL) 
  { return NULL; }

  char** keys = oclKeyset(tree); 
  int n = length((void**) keys);
  struct OclAttribute** res = 
    (struct OclAttribute**) calloc(n+1, sizeof(struct OclAttribute*)); 
 
  int i = 0; 
  for ( ; i < n; i++) 
  { char* nme = keys[i]; 
    struct OclAttribute* att = 
       (struct OclAttribute*) malloc(sizeof(struct OclAttribute)); 
    att->name = nme; 
    res[i] = att; 
  }
  res[i] = NULL; 
  return res; 
} 

unsigned char hasAttribute_OclType(void* obj, char* att)
{ struct ocltnode* tree = (struct ocltnode*) obj; 
  if (tree == NULL) 
  { return FALSE; }

  char** keys = oclKeyset(tree); 
  int n = length((void**) keys);
 
  int i = 0; 
  for ( ; i < n; i++) 
  { char* nme = keys[i]; 
    if (strcmp(nme,att) == 0) 
    { return TRUE; } 
  }
  return FALSE; 
} 
 
void* getAttributeValue_OclType(void* obj, char* att)
{ struct ocltnode* tree = (struct ocltnode*) obj; 
  if (tree == NULL) 
  { return NULL; }

  char** keys = oclKeyset(tree); 
  int n = length((void**) keys);
 
  int i = 0; 
  for ( ; i < n; i++) 
  { char* nme = keys[i]; 
    if (strcmp(nme, att) == 0) 
    { return lookupInMap(tree, nme); } 
  }
  return NULL; 
} 

void setAttributeValue_OclType(void* obj, char* att, void* val)
{ struct ocltnode* tree = (struct ocltnode*) obj; 
  if (tree == NULL) 
  { return; }

  tree = insertIntoMap(tree,att,val); 
} 

void removeAttribute_OclType(void* obj, char* att)
{ struct ocltnode* tree = (struct ocltnode*) obj; 
  if (tree == NULL) 
  { return; }

  tree = oclExcluding(tree,att); 
} 

struct OclOperation** getMethods_OclType(struct OclType* self)
{ return self->operations; } 

struct OclOperation** getConstructors_OclType(struct OclType* self)
{ return self->constructors; } 

char* getOclType_name(struct OclType* self)
{ return self->name; }

void setOclType_name(struct OclType* self, char* _value)
{ self->name = _value; }

void setOclType_superclass(struct OclType* self, struct OclType* sup)
{ self->superclass = sup; }

struct OclType* getOclTypeByPK(char* _ex); 

struct OclType* createOclType(char* _value); 

struct OclAttribute* createOclAttribute(void);

struct OclOperation* createOclOperation(void);

struct OclAttribute** oclattribute_instances = NULL;
int oclattribute_size = 0;

struct OclOperation** ocloperation_instances = NULL;
int ocloperation_size = 0;

struct OclType** ocltype_instances = NULL;
int ocltype_size = 0;

struct OclAttribute** newOclAttributeList()
{ return (struct OclAttribute**) calloc(ALLOCATIONSIZE, sizeof(struct OclAttribute*)); }

struct OclOperation** newOclOperationList()
{ return (struct OclOperation**) calloc(ALLOCATIONSIZE, sizeof(struct OclOperation*)); }

struct OclType** newOclTypeList()
{ return (struct OclType**) calloc(ALLOCATIONSIZE, sizeof(struct OclType*)); }


struct OclAttribute** appendOclAttribute(struct OclAttribute* col[], struct OclAttribute* ex)
{ struct OclAttribute** result;
  int len = length((void**) col);
  if (len % ALLOCATIONSIZE == 0)
  { result = (struct OclAttribute**) calloc(len + ALLOCATIONSIZE + 1, sizeof(struct OclAttribute*));
    int i = 0;
    for ( ; i < len; i++) { result[i] = col[i]; }
  }
  else { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

struct OclOperation** appendOclOperation(struct OclOperation* col[], struct OclOperation* ex)
   { struct OclOperation** result;
     int len = length((void**) col);
     if (len % ALLOCATIONSIZE == 0)
     { result = (struct OclOperation**) calloc(len + ALLOCATIONSIZE + 1, sizeof(struct OclOperation*));
       int i = 0;
       for ( ; i < len; i++) { result[i] = col[i]; }
     }
    else { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

struct OclType** appendOclType(struct OclType* col[], struct OclType* ex)
   { struct OclType** result;
     int len = length((void**) col);
     if (len % ALLOCATIONSIZE == 0)
     { result = (struct OclType**) calloc(len + ALLOCATIONSIZE + 1, sizeof(struct OclType*));
       int i = 0;
       for ( ; i < len; i++) { result[i] = col[i]; }
     }
    else { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

struct OclType* createOclType(char* _value)
{ struct OclType* result = NULL;
  result = getOclTypeByPK(_value);
  if (result != NULL) { return result; }
  result = (struct OclType*) malloc(sizeof(struct OclType));
  result->attributes = NULL;
  result->operations = NULL;
  setOclType_name(result, _value);
  ocltype_instances = appendOclType(ocltype_instances, result);
  ocltype_size++;
  return result;
}

struct OclAttribute* createOclAttribute()
{ struct OclAttribute* result = (struct OclAttribute*) malloc(sizeof(struct OclAttribute));
  result->name = NULL;
  result->type = NULL;
  oclattribute_instances = appendOclAttribute(oclattribute_instances, result);
  oclattribute_size++;
  return result;
}

struct OclOperation* createOclOperation()
{ struct OclOperation* result = (struct OclOperation*) malloc(sizeof(struct OclOperation));
  result->name = NULL;
  result->type = NULL;
  result->parameters = NULL;
  ocloperation_instances = appendOclOperation(ocloperation_instances, result);
  ocloperation_size++;
  return result;
}

struct OclType* getOclTypeByPK(char* _ex)
{ int n = length((void**) ocltype_instances);
  int i = 0;
  for ( ; i < n; i++)
  { char* _v = getOclType_name(ocltype_instances[i]);
    if (_v != NULL && strcmp(_v,_ex) == 0)
    { return ocltype_instances[i]; }
  }
  return NULL;
}

void addOclType_attributes(struct OclType* ocltype_x, struct OclAttribute* oclattribute_x)
{ ocltype_x->attributes = appendOclAttribute(ocltype_x->attributes, oclattribute_x);
}

void addOclType_operations(stru Ð„1    0ÈæÐ„1Ñº…<`Í@òÕ›t0‘ À1‰"ÉWñAê	&ýù­0Ý¹”+;áõã¶½8+@h[·þHlÇÑ[¾,¯³fP¡V'p_âÂÁsNâ8š’`È#“Éå±½Ÿ•ÓÿpsRu'~œ¡¹¾ÔOm×}€à,wN!^4‘'¡ûÔ°ˆQq¾•ýý