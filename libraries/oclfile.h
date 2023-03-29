#include <stdio.h>
#include <windows.h>
#include <direct.h> 
#include <sys/stat.h>
#include <shlwapi.h>
#include <stdarg.h>
#include <winhttp.h>
#include <netutils.h>
#include <WINSOCK2.h>

/* C header for OclFile library */


struct OclFile {
  char* name; 
  FILE* actualFile; 
  int port; 
  long position; 
  long markedPosition; 
  char* lastRead; 
  unsigned char isRemote;
  Session session; 
};

struct OclFile* newOclFile(char* nme)
{ struct OclFile* res = (struct OclFile*) malloc(sizeof(struct OclFile)); 
  res->name = nme;
  res->port = 0; 
  res->actualFile = NULL;
  res->position = 0L;   
  res->markedPosition = 0L; 
  res->isRemote = FALSE;
  return res; 
}

struct OclFile* newOclFile_Read(struct OclFile* self)
{ if (self->isRemote) 
  { return self; }
  if (strcmp("System.in",self->name) == 0)
  { self->actualFile = stdin; } 
  else 
  { self->actualFile = fopen(self->name, "r"); } 
  return self; 
} 

struct OclFile* newOclFile_Write(struct OclFile* self)
{ if (self->isRemote) 
  { return self; }
  if (strcmp("System.out",self->name) == 0)
  { self->actualFile = stdout; } 
  else if (strcmp("System.err",self->name) == 0)
  { self->actualFile = stderr; }
  else 
  { self->actualFile = fopen(self->name, "w+"); } 
  return self; 
} 

struct OclFile* newOclFile_ReadB(struct OclFile* self)
{ if (self->isRemote) 
  { return self; }
  if (strcmp("System.in",self->name) == 0)
  { self->actualFile = stdin; } 
  else 
  { self->actualFile = fopen(self->name, "rb"); } 
  return self; 
} 

struct OclFile* newOclFile_WriteB(struct OclFile* self)
{ if (self->isRemote) 
  { return self; }
  if (strcmp("System.out",self->name) == 0)
  { self->actualFile = stdout; } 
  else if (strcmp("System.err",self->name) == 0)
  { self->actualFile = stderr; }
  else 
  { self->actualFile = fopen(self->name, "wb+"); } 
  return self; 
} 

struct OclFile* newOclFile_Remote(char* nme, Session sess)
{ struct OclFile* res = newOclFile(nme);
  res->isRemote = TRUE; 
  res->session = sess; 
  return res; 
} 


void openRead_OclFile(struct OclFile* self)
{ if (self->isRemote) 
  { return; }
  if (strcmp("System.in",self->name) == 0)
  { self->actualFile = stdin; } 
  else 
  { self->actualFile = fopen(self->name, "r"); } 
} 

void openWrite_OclFile(struct OclFile* self)
{ if (self->isRemote) 
  { return; }
  if (strcmp("System.out",self->name) == 0)
  { self->actualFile = stdout; } 
  else if (strcmp("System.err",self->name) == 0)
  { self->actualFile = stderr; }
  else 
  { self->actualFile = fopen(self->name, "w+"); } 
} 

void openReadB_OclFile(struct OclFile* self)
{ if (self->isRemote) 
  { return; }
  if (strcmp("System.in",self->name) == 0)
  { self->actualFile = stdin; } 
  else 
  { self->actualFile = fopen(self->name, "rb"); } 
} 

void openWriteB(struct OclFile* self)
{ if (self->isRemote) 
  { return; }
  if (strcmp("System.out",self->name) == 0)
  { self->actualFile = stdout; } 
  else if (strcmp("System.err",self->name) == 0)
  { self->actualFile = stderr; }
  else 
  { self->actualFile = fopen(self->name, "wb+"); }  
} 

long length_OclFile(struct OclFile* self)
{ if (self->isRemote) 
  { return 0; }
  struct stat stbuf; 
  stat(self->name, &stbuf); 
  return (long) stbuf.st_size; 
}  


void closeFile_OclFile(struct OclFile* self)
{ if (self->isRemote) 
  { CloseSession(&(self->session)); 
    return;
  }
  fclose(self->actualFile); 
  self->actualFile = NULL; 
} 

unsigned char isOpen_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL) 
  { return FALSE; } 
  return TRUE;
}

char* read_OclFile(struct OclFile* self)
{ if (self->isRemote) { 
    char* buf = "  "; 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) { return NULL; }
    return buf; 
  }

  if (self->actualFile == NULL) 
  { return NULL; }
  int ch = fgetc(self->actualFile); 
  self->position++;
  if (ch == EOF)
  { return NULL; } 
  return byte2char(ch); 
} 

int readByte_OclFile(struct OclFile* self)
{ if (self->isRemote) { 
    char* buf = "  "; 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) 
    { return -1; }
    return char2byte(buf); 
  }

  if (self->actualFile == NULL) 
  { return -1; }
  int ch = fgetc(self->actualFile); 
  self->position++;
  return ch; 
} 

char** readN_OclFile(struct OclFile* self, int n)
{ if (self->isRemote) { 
    char* buf = (char*) calloc(n+1, sizeof(char)); 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) { return NULL; }
    return characters(buf); 
  }

  if (self->actualFile == NULL) 
  { return NULL; }

  int ind = 0; 
  char** res = newStringList(); 

  int ch = fgetc(self->actualFile); 
  while (ind < n && ch != EOF)
  { res = appendString(res,byte2char(ch));
    self->position++; 
    ind++; 
  } 
  return res; 
} 

int** readNbytes_OclFile(struct OclFile* self, int n)
{ if (self->isRemote) 
  { char* buf = (char*) calloc(n+1, sizeof(char)); 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) { return NULL; }
    return bytesOf(buf);
  }
  
  if (self->actualFile == NULL) 
  { return NULL; }

  int ind = 0; 
  int** res = newintList(); 

  int ch = fgetc(self->actualFile); 
  while (ind < n && ch != EOF)
  { res = appendint(res,ch);
    self->position++; 
    ind++; 
  } 
  return res; 
} 

int** readAllBytes_OclFile(struct OclFile* self)
{ if (self->isRemote) { 
    char* buf = (char*) calloc(8192, sizeof(char)); 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) { return NULL; }
    return bytesOf(buf); 
  }

  if (self->actualFile == NULL) 
  { return NULL; }

  int ind = 0; 
  int** res = newintList(); 

  int ch = fgetc(self->actualFile); 
  while (ch != EOF)
  { res = appendint(res,ch);
    self->position++; 
    ind++; 
  } 
  return res; 
} 

char* copyFromTo_OclFile(struct OclFile* self, struct OclFile* target)
{ if (self->actualFile == NULL || self->isRemote) 
  { return NULL; }
  if (target->actualFile == NULL || target->isRemote)
  { return NULL; } 

  int ch = fgetc(self->actualFile); 
  while (ch != EOF)
  { fputc(ch, target->actualFile);
    ch = fgetc(self->actualFile);
  } 
  return target->name; 
} 
  

struct OclFile* getInputStream_OclFile(struct OclFile* self)
{ if (self->isRemote)
  { return self; }

  if (self->actualFile != NULL)
  { return self; }

  return NULL; 
}

struct OclFile* getOutputStream_OclFile(struct OclFile* self)
{ if (self->isRemote)
  { return self; }

  if (self->actualFile != NULL)
  { return self; }
  
  return NULL; 
}

unsigned char canRead_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL)
  { return FALSE; }
  struct stat stbuf; 
  stat(self->name, &stbuf);
  if ((stbuf.st_mode & S_IREAD) == S_IREAD)
  { return TRUE; }
  return FALSE; 
} 

unsigned char canWrite_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL)
  { return FALSE; }
  struct stat stbuf; 
  stat(self->name, &stbuf); 
   
  if ((stbuf.st_mode & S_IWRITE) == S_IWRITE)
  { return TRUE; }
  return FALSE; 
}

void write_OclFile(struct OclFile* self, char* s)
{ if (self->isRemote)
  { Send(&(self->session), strlen(s)+1, s); 
    return; 
  } 

  if (self->actualFile != NULL)
  { fprintf(self->actualFile, "%s", s); } 
}

struct OclFile* append_OclFile(struct OclFile* self, char* s)
{ if (self->isRemote)
  { Send(&(self->session), strlen(s)+1, s); 
    return self; 
  } 
  
  if (self->actualFile != NULL)
  { fprintf(self->actualFile, "%s", s); } 
  return self; 
} 

void writeN_OclFile(struct OclFile* self, char** col, int n)
{ int m = length(col); 
  int i = 0; 
  
  if (self->isRemote)
  { for ( ; i < n && i < m; i++) 
    { char* s = col[i]; 
      Send(&(self->session), strlen(s)+1, s);
    }  
    return; 
  } 

  if (self->actualFile == NULL)
  { return; }

  for ( ; i < n && i < m; i++) 
  { char* s = col[i]; 
    fprintf(self->actualFile, "%s", s); 
  } 
}

void writeByte_OclFile(struct OclFile* self, int c)
{ if (self->isRemote) 
  { char* s = (char*) calloc(2, sizeof(char));
    s[0] = c; 
    s[1] = '\0'; 
    Send(&(self->session), 2, s); 
    return; 
  } 
 
  if (self->actualFile == NULL)
  { return; }

  fputc(c, self->actualFile);
} 

void writeNbytes_OclFile(struct OclFile* self, int** col, int n)
{ int m = length(col); 
  int i = 0; 

  if (self->isRemote)
  { for ( ; i < n && i < m; i++) 
    { char* s = calloc(2, sizeof(char)); 
      s[0] = *col[i];
      s[1] = '\0'; 
      Send(&(self->session), 2, s);
    }  
    return; 
  } 

  if (self->actualFile == NULL)
  { return; }

  for ( ; i < n && i < m; i++) 
  { int c = *col[i]; 
    fputc(c, self->actualFile); 
  }
}
 

void print_OclFile(struct OclFile* self, char* s)
{ if (strcmp("System.out",self->name) == 0)
  { printf("%s",s); } 
  else if (strcmp("System.err",self->name) == 0)
  { fprintf(stderr, "%s", s); } 
  else 
  { write_OclFile(self,s); } 
} 

char* readLine_OclFile(struct OclFile* self)
{ if (self->isRemote) { 
    char* buf = (char*) calloc(128, sizeof(char)); 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) { return NULL; }
    return buf; 
  }

  if (self->actualFile == NULL)
  { return NULL; }
  char* result = (char*) calloc(1024, sizeof(char));
  char* res = fgets(result, 1024, self->actualFile); 
  return res; 
} 

char** readAllLines_OclFile(struct OclFile* self)
{ if (self->isRemote) { 
    char* buf = (char*) calloc(8192, sizeof(char)); 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) { return NULL; }
    return split(buf, "\n\r"); 
  }

  if (self->actualFile == NULL)
  { return NULL; }
  int n = length_OclFile(self); 
  char** lines = (char**) calloc(n, sizeof(char*)); 
  int i = 0; 

  char* result = (char*) calloc(1024, sizeof(char));
  char* res = fgets(result, 1024, self->actualFile); 
  while (res != NULL)
  { lines[i] = res; 
    i++; 
    res = fgets(result, 1024, self->actualFile);
  } 
  lines[i] = NULL; 
  return lines; 
} 

unsigned char hasNext_OclFile(struct OclFile* self)
{ self->lastRead = readLine_OclFile(self);
  if (self->lastRead == NULL || strlen(self->lastRead) == 0)
  { return FALSE; }
  return TRUE; 
}

char* getCurrent_OclFile(struct OclFile* self)
{ return self->lastRead; }


char* readAll_OclFile(struct OclFile* self)
{ if (self->isRemote) { 
    char* buf = (char*) calloc(8192, sizeof(char)); 
    memset(buf,0,sizeof(buf));
    if (Receive(&(self->session),sizeof(buf)-1,buf)) { return NULL; }
    return buf; 
  }

  if (self->actualFile == NULL) 
  { return NULL; }

  long sze = length_OclFile(self);
  char* result = (char*) calloc(sze+1, sizeof(char)); 
  int i = 0; 
  for ( ; i < sze; i++) 
  { int ch = fgetc(self->actualFile); 
    if (ch == EOF)
    { result[i] = '\0';
      return result; 
    } 
    result[i] = ch; 
  }
  result[i] = '\0';
  return result; 
}

void writeln_OclFile(struct OclFile* self, char* s)
{ if (self->isRemote)
  { Send(&(self->session), strlen(s)+1, s); 
    Send(&(self->session), 2, "\n"); 
    return; 
  } 

  if (self->actualFile == NULL)
  { return; }
  fputs(s, self->actualFile);
  fputs("\n", self->actualFile);
}

void println_OclFile(struct OclFile* self, char* s)
{ if (strcmp("System.out",self->name) == 0)
  { printf("%s\n",s); } 
  else if (strcmp("System.err",self->name) == 0)
  { fprintf(stderr, "%s\n", s); } 
  else 
  { writeln_OclFile(self,s); } 
} 

void printf_OclFile(struct OclFile* self, char* f, void* sq[])
{ int n = length(sq);
  int flength = strlen(f); 
  if (n == 0 || flength == 0 || self->actualFile == NULL) 
  { return; }

  void* ap; 
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
      fputs(p0, self->actualFile); 
      continue; 
    } 
 
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
        fprintf(self->actualFile, tmp, ival);
        break; 
      case 'o' : 
      case 'x' : 
      case 'X' : 
      case 'u' : 
        uval = *((unsigned int*) sq[i]); 
        i++; 
        fprintf(self->actualFile, tmp, uval);
        break; 
      case 'c' : 
      case 'd' : 
        ival = *((int*) sq[i]); 
        i++; 
        fprintf(self->actualFile, tmp, ival);
        break; 
      case 'f' : 
      case 'e' : 
      case 'E' : 
      case 'g' : 
      case 'G' : 
        dval = *((double*) sq[i]); 
        i++; 
        fprintf(self->actualFile, tmp, dval); 
        break; 
      case 's' : 
        sval = ((char*) sq[i]); 
        i++; 
        fprintf(self->actualFile, tmp, sval); 
        break;
      case 'p' : 
        i++; 
        fprintf(self->actualFile, tmp, sq[i]); 
        break; 
      default : 
        char* p0 = subStrings(p,1,1); 
        fputs(p0, self->actualFile);
        break; 
    }
  } 
}

char* getName_OclFile(struct OclFile* self)
{ return self->name; } 

char* getInetAddress_OclFile(struct OclFile* self)
{ return self->name; } 

char* getLocalAddress_OclFile(struct OclFile* self)
{ return self->name; } 

void setPort_OclFile(struct OclFile* self, int portNumber)
{ self->port = portNumber; }

int getPort_OclFile(struct OclFile* self)
{ return self->port; }  

int getLocalPort_OclFile(struct OclFile* self)
{ return self->port; }  

int compareTo_OclFile(struct OclFile* self, struct OclFile* f)
{ return strcmp(self->name, f->name); }

char** list_OclFile(struct OclFile* self)
{ char** res = newStringList(); 

  unsigned char currentdir[MAX_PATH]; 
  getcwd(currentdir, sizeof(currentdir)-1); 
  char* completePath = concatenateStrings(currentdir, "\\*.*"); 
  WIN32_FIND_DATA data; 
  HANDLE h; 

  h = FindFirstFile(completePath, &data); 

  if (h != INVALID_HANDLE_VALUE) 
  { res = appendString(res, data.cFileName); 
    while (FindNextFile(h,&data))
    { res = appendString(res, data.cFileName); }
    FindClose(h); 
  }  
  return res; 
}  

struct OclFile** listFiles_OclFile(struct OclFile* self)
{ char** names = list_OclFile(self); 
  int flen = length((void**) names); 
  struct OclFile** res = (struct OclFile**) calloc(flen+1, sizeof(struct OclFile*)); 
  int i = 0; 
  for ( ; i < flen; i++) 
  { char* nme = names[i]; 
    struct OclFile* fle = newOclFile(nme); 
    res[i] = fle; 
  }
  res[i] = NULL; 
  return res; 
}
 
unsigned char delete_OclFile(struct OclFile* self)
{ int flg = remove(self->name); 
  if (flg > 0)
  { return FALSE; } 
  self->actualFile = NULL; 
  self->position = 0L; 
  self->markedPosition = 0L; 
  return TRUE; 
} 

unsigned char deleteFile_OclFile(char* nme)
{ int flg = remove(nme); 
  if (flg > 0)
  { return FALSE; } 
  return TRUE; 
} 

void flush_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL)
  { return; }
  fflush(self->actualFile);
}

void mark_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL)
  { return; }
  self->markedPosition = ftell(self->actualFile);
} 

void reset_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL)
  { return; }
  fseek(self->actualFile, self->markedPosition, SEEK_SET); 
  self->position = self->markedPosition;
} 

void skipBytes_OclFile(struct OclFile* self, int n) 
{ if (self->isRemote)
  { readNbytes_OclFile(self,n); 
    return; 
  } 

  if (self->actualFile == NULL)
  { return; }

  int i = 0;  
  for ( ; i < n; i++) 
  { int ch = fgetc(self->actualFile);
    self->position++; 
    if (ch == EOF)
    { return; } 
  } 
} 

void setPosition_OclFile(struct OclFile* self, long pos)
{ if (self->actualFile == NULL)
  { return; }
  fseek(self->actualFile, pos, SEEK_SET); 
  self->position = pos;
}


struct OclFile* createTemporaryFile(char* nme, char* ext)
{ char* cname = concatenateStrings(nme,"."); 
  cname = concatenateStrings(cname,ext); 
  struct OclFile* res = newOclFile(cname); 
  struct OclFile* wr = newOclFile_Write(res); 
  return wr; 
} 

unsigned char mkdir_OclFile(struct OclFile* self)
{ int res = mkdir(self->name); 
  if (res == 0)
  { return TRUE; } 
  return FALSE; 
}

unsigned char isDirectory_OclFile(struct OclFile* self)
{ struct stat stbuf; 
  stat(self->name, &stbuf); 
  if (S_ISDIR(stbuf.st_mode) == 0)
  { return FALSE; }
  return TRUE; 
} 

unsigned char isFile_OclFile(struct OclFile* self)
{ struct stat stbuf; 
  stat(self->name, &stbuf); 
  if (S_ISREG(stbuf.st_mode) == 0)
  { return FALSE; }
  return TRUE; 
} 

unsigned char exists_OclFile(struct OclFile* self)
{ return isDirectory_OclFile(self) ||
         isFile_OclFile(self); 
} 

unsigned char isHidden_OclFile(struct OclFile* self)
{ if (strlen(self->name) == 0)
  { return FALSE; } 
  if (strcmp(".", subStrings(self->name,1,1)) == 0)
  { return TRUE; } 
  return FALSE; 
}

unsigned char isExecutable_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL)
  { return FALSE; }
  struct stat stbuf; 
  stat(self->name, &stbuf);
  if ((stbuf.st_mode & S_IEXEC) == S_IEXEC)
  { return TRUE; }
  return FALSE; 
} 

unsigned char isAbsolute_OclFile(struct OclFile* self)
{ if (self == NULL) 
  { return FALSE; }
  if (self->name == NULL)
  { return FALSE; } 
  if (strlen(self->name) == 0)
  { return FALSE; } 
  if ((self->name)[0] == '\\')
  { return TRUE; } 
  if (strlen(self->name) > 1 &&
      (self->name)[1] == ':')
  { return TRUE; } 
  return FALSE; 
} 

char* getPath_OclFile(struct OclFile* self)
{ return self->name; }

char* getAbsolutePath_OclFile(struct OclFile* self)
{ if (isAbsolute_OclFile(self))
  { return self->name; } 

  unsigned char currentdir[MAX_PATH]; 
  getcwd(currentdir, sizeof(currentdir)-1); 
  char* completePath = concatenateStrings(currentdir, "\\"); 
  char* res = concatenateStrings(completePath, self->name); 
  return res; 
}

struct OclFile* getParentFile_OclFile(struct OclFile* self)
{ unsigned char currentdir[MAX_PATH]; 
  getcwd(currentdir, sizeof(currentdir)-1); 
  struct OclFile* res = newOclFile(currentdir); 
  return res; 
}

unsigned char* getParent_OclFile(struct OclFile* self)
{ unsigned char* currentdir = (unsigned char*) calloc(MAX_PATH, sizeof(unsigned char)); 
  getcwd(currentdir, MAX_PATH-1); 
  return currentdir; 
}


long long lastModified_OclFile(struct OclFile* self)
{ struct stat stbuf; 
  stat(self->name, &stbuf); 
  return stbuf.st_mtime; 
}  

unsigned char renameFile_OclFile(char* oldName, char* newName)
{ int r = rename(oldName,newName); 
  if (r == 0) 
  { return TRUE; }
  return FALSE; 
}

long getPosition_OclFile(struct OclFile* self)
{ if (self->actualFile != NULL)
  { self->position = ftell(self->actualFile); } 
  return self->position;
}

unsigned char getEof_OclFile(struct OclFile* self)
{ if (self->actualFile == NULL) 
  { return TRUE; }
  int x = feof(self->actualFile);
  if (x == 0)
  { return FALSE; } 
  return TRUE; 
}

struct OclFile** oclfile_instances = NULL;
int oclfile_size = 0;

struct OclFile* getOclFileByPK(char* _ex)
{ int n = length((void**) oclfile_instances);
  int i = 0;
  for ( ; i < n; i++)
  { char* _v = getName_OclFile(oclfile_instances[i]);
    if (_v != NULL && strcmp(_v,_ex) == 0)
    { return oclfile_instances[i]; }
  }
  return NULL;
}

struct OclFile** appendOclFile(struct OclFile* col[], struct OclFile* ex)
   { struct OclFile** result;
     int len = length((void**) col);
     if (len % ALLOCATIONSIZE == 0)
     { result = (struct OclFile**) calloc(len + ALLOCATIONSIZE + 1, sizeof(struct OclFile*));
       int i = 0;
       for ( ; i < len; i++) { result[i] = col[i]; }
     }
    else { result = col; }
    result[len] = ex;
    result[len+1] = NULL;
    return result;
  }

struct OclFile* createOclFile(char* _value)
{ struct OclFile* result = NULL;
  result = getOclFileByPK(_value);
  if (result != NULL) { return result; }
  result = newOclFile(_value); 
  oclfile_instances = appendOclFile(oclfile_instances, result);
  oclfile_size++;
  return result;
}

struct OclFile* createOclFile_Write(char* _value)
{ struct OclFile* result = NULL;
  struct OclFile* f = newOclFile(_value);
  result = newOclFile_Write(f);  
  oclfile_instances = appendOclFile(oclfile_instances, result);
  oclfile_size++;
  return result;
}

struct OclFile* createOclFile_Read(char* _value)
{ struct OclFile* result = NULL;
  struct OclFile* f = newOclFile(_value);
  result = newOclFile_Read(f);  
  oclfile_instances = appendOclFile(oclfile_instances, result);
  oclfile_size++;
  return result;
}

struct OclFile* OclFile_FromURL(char* url)
{ int response = GetHttpURL(url, "_tmp.txt"); 
  struct OclFile* res = newOclFile_Read(newOclFile("_tmp.txt"));  
  return res; 
} 

