#include <sqlite3/sqlite3.h>
#include <netutils.h>

/* #include <winsock.h> */ 

#include <WINSOCK2.h>
#include <winhttp.h>


struct OclDatasource {
  char* url;
  char* protocol;
  char* host;
  char* file;
  int port;
  char* name;
  char* passwd;
  char* schema;
  sqlite3* database;
  Session localSocket;
  unsigned char hasLocalSession; 
  Session remoteSocket;
  unsigned char hasRemoteSession; 
};

struct SQLStatement {
  char* text; 
  struct OclDatasource* connection; 
  sqlite3_stmt* statement;
  struct OclIterator* resultSet;  
};

struct SQLStatement* createSQLStatement(void) { 
  struct SQLStatement* res = (struct SQLStatement*) malloc(sizeof(struct SQLStatement)); 
  res->text = ""; 
  res->connection = NULL; 
  res->statement = NULL; 
  res->resultSet = NULL; 
  return res;
} 

struct OclDatasource* createOclDatasource(void) 
{ struct OclDatasource* result = 
     (struct OclDatasource*) malloc(sizeof(struct OclDatasource));
  result->url = "";
  result->protocol = "";
  result->host = "";
  result->file = "";
  result->port = 0;
  result->name = "";
  result->passwd = "";
  result->schema = "";
  result->database = NULL; 
  result->hasLocalSession = FALSE; 
  result->hasRemoteSession = FALSE; 
  return result; 
}


struct OclDatasource* getConnection(char* url, char* name, char* passwd)
{ sqlite3* db;
  char* errs;  
  int succ = sqlite3_open(url, &db);
 
  if (succ == SQLITE_OK)
  { 
    struct OclDatasource* res = createOclDatasource();
    res->url = url;
    res->name = name;
    res->passwd = passwd;
    res->database = db; 
    return res; 
  }

  return NULL; 
}


struct OclDatasource* newOclDatasource(void)
{
  struct OclDatasource* result = createOclDatasource(); 
  return result;
}


struct OclDatasource* newSocket(char* host, int port)
{
  struct OclDatasource* db = createOclDatasource();
  db->host = host;
  db->port = port;
  Session session;
  char buf[256]; 
  memset(&session, 0, sizeof(session)); 
  session.port = port; 
  session.Host = host;
  if (ClientConnect(&session))
  { printf("!!! Unable to connect\n"); 
    return db; 
  } 
  printf(">>> Connection established\n"); 
  db->localSocket = session;
  db->hasLocalSession = TRUE;  
  return db; 
} 

 
struct OclDatasource* newServerSocket(int port, int limit)
{ /* SOCKET ss = socket( AF_INET, SOCK_STREAM, IPPROTO_TCP );

  struct sockaddr_in service; 
  service.sin_family = AF_INET; 
  service.sin_addr.s_addr = inet_addr( "127.0.0.1" ); 
  service.sin_port = htons(port); 
  int rc = bind( ss, (SOCKADDR*) &service, sizeof(service) );
  if (rc == SOCKET_ERROR ) { 
    printf("!! socket bind() failed.\n");
    return NULL; 
  }  */  

  struct OclDatasource* db = createOclDatasource();
  db->host = "localhost";
  db->port = port;
  /* db->serverSocket = ss; */  
  return db; 
} 

struct OclDatasource* accept_OclDatasource(struct OclDatasource* self)
{ /* SOCKET ss = self->serverSocket;

  if ( listen( ss, 1 ) == SOCKET_ERROR ) 
  { printf("!! Error listening on socket.\n");
    return NULL; 
  }

  SOCKET acceptedSocket; 
  while (1) { 
    acceptedSocket = SOCKET_ERROR; 
    while ( acceptedSocket == SOCKET_ERROR ) { 
      acceptedSocket = accept( ss, NULL, NULL ); 
    }
  } */ 

  Session session;
  memset(&session,0,sizeof(session));
  session.port = self->port;
  session.Host = self->host;
  if (ServerConnect(&session))
  { return NULL; }
  struct OclDatasource* db = createOclDatasource();
  db->host = "localhost";
  db->port = self->port;
  db->remoteSocket = session;
  db->hasRemoteSession = TRUE;  
  return db; 
} 


struct OclDatasource* newURL(char* s)
{
  struct OclDatasource* db = createOclDatasource();
  db->url = s;
  return db; 
}


struct OclDatasource* newURL_PHF(char* p, char* h, char* f)
{
  struct OclDatasource* db = createOclDatasource();
  db->protocol = p;
  db->host = h;
  db->file = f;
  char* u1 = concatenateStrings(p, "://"); 
  char* u2 = concatenateStrings(u1,h);
  char* u3 = concatenateStrings(u2, "/"); 
  db->url = concatenateStrings(u3,f); 
  return db; 
}


struct OclDatasource* newURL_PHNF(char* p, char* h, int n, char* f)
{
  struct OclDatasource* db = createOclDatasource();
  db->protocol = p;
  db->host = h;
  db->port = n;
  db->file = f;
  char* u1 = concatenateStrings(p, "://"); 
  char* u2 = concatenateStrings(u1,h);
  char* u3 = concatenateStrings(u2, ":");
  char* u4 = concatenateStrings(u3, toString_int(&n));  
  char* u5 = concatenateStrings(u4, "/"); 
  db->url = concatenateStrings(u3,f); 
  return db; 
}


struct SQLStatement* createStatement_OclDatasource(struct OclDatasource* self)
{
  struct SQLStatement* ss = createSQLStatement();
  ss->text = "";
  ss->connection = self; 

  if (self->database != NULL) 
  { sqlite3_stmt* statement; 
    const void* rem; 
    sqlite3_prepare(self->database, ss->text, 0, &statement, &rem);
    ss->statement = statement;  
  } 

  return ss; 
}

struct SQLStatement* prepare_OclDatasource(struct OclDatasource* self, char* stat)
{
  struct SQLStatement* ss = createSQLStatement();
  ss->text = stat;
  ss->connection = self; 

  if (self->database != NULL) 
  { sqlite3_stmt* statement; 
    const void* rem; 
    sqlite3_prepare(self->database, ss->text, -1, &statement, &rem);
    ss->statement = statement;  
  } 

  return ss; 
}

struct SQLStatement* prepareStatement_OclDatasource(struct OclDatasource* self, char* stat)
{
  return prepare_OclDatasource(self,stat);
}

struct SQLStatement* prepareCall_OclDatasource(struct OclDatasource* self, char* stat)
{
  return prepare_OclDatasource(self,stat);
}


struct OclIterator* query_String_OclDatasource(struct OclDatasource* self, char* stat)
{ char* errs;
  char** res; 
  int nrow; 
  int ncol; 
  char** cnames; 

  if (self->database != NULL) 
  { int succ = sqlite3_get_table(self->database, stat, &res, &nrow, &ncol, &errs); 
    if (succ == SQLITE_OK)
    { struct ocltnode** resultSet = 
        (struct ocltnode**) calloc(nrow+1, sizeof(struct ocltnode*)); 

      cnames = (char**) calloc(ncol+1, sizeof(char*)); 

      int cind = 0; 
      for ( ; cind < ncol; cind++) 
      { cnames[cind] = res[cind]; } 
      cnames[cind] = NULL; 

      int r = 1; 
      for ( ; r <= nrow; r++) 
      { int c = 0;
        struct ocltnode* record = NULL; 
 
        for ( ; c < ncol; c++) 
        { int indx = r*ncol + c; 

          /* printf("%s |-> %s ", res[c], res[indx]); */ 

          record = insertIntoMap(record, res[c], res[indx]);  
        }    

        /* printf("\n"); */
 
        resultSet[r-1] = record; 
      } 
      resultSet[r-1] = NULL; 

      /* printf("There are %d records\n", length(resultSet)); */
 
      struct OclIterator* iter = newOclIterator_Sequence(resultSet); 
      iter->columnNames = cnames; 
      return iter;
    }  
  } 
  return NULL; 
}


/*
public String nativeSQL(struct OclDatasource* self, String stat)
  {
    String result = null;
    return result;
  }
*/ 

struct OclIterator* query_String_Sequence_OclDatasource(struct OclDatasource* self, char* stat, char* cols[])
{
  return query_String_OclDatasource(self,stat);
}


void execSQL_OclDatasource(struct OclDatasource* self, char* stat)
{ char* errs; 

  if (self->database != NULL)
  { sqlite3_exec(self->database, 
      stat, 
      NULL, NULL, &errs);
  } 
}


void abort_OclDatasource(struct OclDatasource* self)
{ if (self->database != NULL)
  { sqlite3_interrupt(self->database); } 
}


void close_OclDatasource(struct OclDatasource* self)
{ if (self->database != NULL)
  { sqlite3_close(self->database); }
  self->hasLocalSession = FALSE; 
  self->url = ""; 
  self->host = ""; 
  self->file = ""; 
  self->port = 0;
  self->passwd = "";
  self->schema = "";
  self->database = NULL; 
  self->protocol = ""; 
}

void closeFile_OclDatasource(struct OclDatasource* self)
{ if (self->database != NULL)
  { sqlite3_close(self->database); }
  self->hasLocalSession = FALSE; 
  self->url = ""; 
  self->host = ""; 
  self->file = ""; 
  self->port = 0;
  self->passwd = "";
  self->schema = "";
  self->database = NULL; 
  self->protocol = ""; 
}


void commit_OclDatasource(struct OclDatasource* self)
{  }


void rollback_OclDatasource(struct OclDatasource* self)
{  }


void connect_OclDatasource(struct OclDatasource* self)
{  }


struct OclDatasource* openConnection_OclDatasource(struct OclDatasource* self)
{ return self; }

void setSchema_OclDatasource(struct OclDatasource* self, char* s)
{
  self->schema = s;
}


char* getSchema_OclDatasource(struct OclDatasource* self)
{
  return self->schema;
}


struct OclFile* getInputStream_OclDatasource(struct OclDatasource* self)
{ if (self->hasLocalSession)
  { struct OclFile* r = newOclFile_Remote(self->url, self->localSocket); 
    return r; 
  } 

  if (self->hasRemoteSession)
  { struct OclFile* r = newOclFile_Remote(self->url, self->remoteSocket); 
    return r; 
  } 

  if (self->url != NULL)
  { struct OclFile* res = OclFile_FromURL(self->url);
    return res;
  } 
  return NULL;  
}


struct OclFile* getOutputStream_OclDatasource(struct OclDatasource* self)
{ if (self->hasLocalSession)
  { struct OclFile* r = newOclFile_Remote(self->url, self->localSocket); 
    return r; 
  } 

  if (self->hasRemoteSession)
  { struct OclFile* r = newOclFile_Remote(self->url, self->remoteSocket); 
    return r; 
  } 

  return NULL; 
}


char* getURL_OclDatasource(struct OclDatasource* self)
{
  return self->url; 
}


void* getContent_OclDatasource(struct OclDatasource* self)
{ struct OclFile* fle = getInputStream_OclDatasource(self); 
  if (fle != NULL) 
  { return readAll_OclFile(fle); } 

  return NULL; 
}


char* getFile_OclDatasource(struct OclDatasource* self)
{
  return self->file; 
}


char* getHost_OclDatasource(struct OclDatasource* self)
{
  return self->host; 
}


int getPort_OclDatasource(struct OclDatasource* self)
{
  return self->port; 
}


char* getProtocol_OclDatasource(struct OclDatasource* self)
{
  return self->protocol;
}




struct SQLStatement* newSQLStatement(void) { 
  struct SQLStatement* res = (struct SQLStatement*) malloc(sizeof(struct SQLStatement)); 
  res->text = ""; 
  res->connection = NULL; 
  res->statement = NULL; 
  res->resultSet = NULL; 
  return res;
} 


void close_SQLStatement(struct SQLStatement* self)
{ if (self->statement != NULL) 
  { sqlite3_finalize(self->statement); }

  if (self->connection != NULL) 
  { close_OclDatasource(self->connection); } 
} 

void closeOnCompletion_SQLStatement(struct SQLStatement* self)
{ } 


void setObject_SQLStatement(struct SQLStatement* self, int field, void* value)
{ if (self->statement != NULL) 
  { sqlite3_bind_value(self->statement, field, value); } 
} 

void setParameters_SQLStatement(struct SQLStatement* self, void* pars[])
{ if (self->statement != NULL) 
  { int i = 1; 
    int n = length(pars); 
    for ( ; i <= n; i++) 
    { setObject_SQLStatement(self, i, pars[i-1]); } 
  } 
} 
 

void setString_SQLStatement(struct SQLStatement* self, int field, char* value)
{ if (self->statement != NULL) 
  { sqlite3_bind_text(self->statement, field, value, -1, NULL); } 
} 

void setInt_SQLStatement(struct SQLStatement* self, int field, int value)
{ if (self->statement != NULL) 
  { sqlite3_bind_int(self->statement, field, value); } 
} 

void setByte_SQLStatement(struct SQLStatement* self, int field, int value)
{ if (self->statement != NULL) 
  { sqlite3_bind_int(self->statement, field, value); } 
} 

void setShort_SQLStatement(struct SQLStatement* self, int field, int value)
{ if (self->statement != NULL) 
  { sqlite3_bind_int(self->statement, field, value); } 
} 

void setBoolean_SQLStatement(struct SQLStatement* self, int field, unsigned char value)
{ if (self->statement != NULL) 
  { sqlite3_bind_int(self->statement, field, value); } 
} 

void setLong_SQLStatement(struct SQLStatement* self, int field, long value)
{ if (self->statement != NULL) 
  { sqlite3_bind_int64(self->statement, field, value); } 
} 

void setDouble_SQLStatement(struct SQLStatement* self, int field, double value)
{ if (self->statement != NULL) 
  { sqlite3_bind_double(self->statement, field, value); } 
} 

void setTimestamp_SQLStatement(struct SQLStatement* self, int field, struct OclDate* value)
{ if (self->statement != NULL) 
  { sqlite3_bind_int64(self->statement, field, value->time); } 
} 

void setNull_SQLStatement(struct SQLStatement* self, int field, void* value)
{ if (self->statement != NULL) 
  { sqlite3_bind_null(self->statement, field); } 
} 

 
struct OclIterator* executeQuery_SQLStatement(struct SQLStatement* self, char* stat)
{ if (self->connection != NULL && stat != NULL) 
  { self->resultSet = query_String_OclDatasource(self->connection, stat); }

  if (stat == NULL)
  { struct ocltnode** records = 
        (struct ocltnode**) calloc(50, sizeof(struct ocltnode*)); 
    records[0] = NULL; 

    char** cnames; 

    while (sqlite3_step(self->statement) == SQLITE_ROW)
    { int ncol = sqlite3_column_count(self->statement);
 
      cnames = (char**) calloc(ncol+1, sizeof(char*));
      struct ocltnode* record = NULL; 

      int idx = 0; 
      for ( ; idx < ncol; idx++)
      { const char* cname = sqlite3_column_name(self->statement, idx);
        cnames[idx] = cname; 
        
        int ctype = sqlite3_column_type(self->statement, idx); 
        if (ctype == SQLITE_INTEGER)
        { int cvali = sqlite3_column_int(self->statement, idx); 
          record = insertIntoMapint(record, cname, cvali); 
        }
        else if (ctype == SQLITE_FLOAT)
        { double cvald = sqlite3_column_double(self->statement, idx);
          record = insertIntoMapdouble(record, cname, cvald); 
        } 
        else if (ctype == SQLITE_TEXT)
        { char* cvalt = sqlite3_column_text(self->statement, idx);
          record = insertIntoMap(record, cname, cvalt); 
        } 
      }
      cnames[idx] = NULL;
      records = append(records, record);  
    }
    self->resultSet = newOclIterator_Sequence(records); 
    self->resultSet->columnNames = cnames;  
  } 
 
  return self->resultSet; 
}

struct OclIterator* executeParameterisedQuery_SQLStatement(struct SQLStatement* self, void* pars[])
{ if (self->statement == NULL) 
  { return NULL; } 

  setParameters_SQLStatement(self,pars); 

  struct ocltnode** records = 
        (struct ocltnode**) calloc(50, sizeof(struct ocltnode*)); 
  records[0] = NULL; 

  char** cnames; 

  while (sqlite3_step(self->statement) == SQLITE_ROW)
  { int ncol = sqlite3_column_count(self->statement);
 
      cnames = (char**) calloc(ncol+1, sizeof(char*));
      struct ocltnode* record = NULL; 

      int idx = 0; 
      for ( ; idx < ncol; idx++)
      { const char* cname = sqlite3_column_name(self->statement, idx);
        cnames[idx] = cname; 
        
        int ctype = sqlite3_column_type(self->statement, idx); 
        if (ctype == SQLITE_INTEGER)
        { int cvali = sqlite3_column_int(self->statement, idx); 
          record = insertIntoMapint(record, cname, cvali); 
        }
        else if (ctype == SQLITE_FLOAT)
        { double cvald = sqlite3_column_double(self->statement, idx);
          record = insertIntoMapdouble(record, cname, cvald); 
        } 
        else if (ctype == SQLITE_TEXT)
        { char* cvalt = sqlite3_column_text(self->statement, idx);
          record = insertIntoMap(record, cname, cvalt); 
        } 
      }
      cnames[idx] = NULL;
      records = append(records, record);  
    }
    self->resultSet = newOclIterator_Sequence(records); 
    self->resultSet->columnNames = cnames;  
 
  return self->resultSet; 
}

struct OclIterator* rawQuery_OclDatasource(struct OclDatasource* self, char* stat, void* pos[])
{ struct SQLStatement* sql = prepare_OclDatasource(self,stat); 
  return executeParameterisedQuery_SQLStatement(sql,pos); 
}


void execute_SQLStatement(struct SQLStatement* self, char* stat)
{ if (self->connection != NULL)
  { execSQL_OclDatasource(self->connection,stat); } 
}  

void executeUpdate_SQLStatement(struct SQLStatement* self)
{ if (self->statement != NULL) 
  { sqlite3_step(self->statement); } 
  else 
  { execute_SQLStatement(self, self->text); }  
}

struct OclDatasource* getConnection_SQLStatement(struct SQLStatement* self) 
{ return self->connection; } 

struct OclIterator* getResultSet_SQLStatement(struct SQLStatement* self) 
{ return self->resultSet; } 

void cancel_SQLStatement(struct SQLStatement* self) 
{ if (self->statement != NULL) 
  { sqlite3_reset(self->statement); } 
}




