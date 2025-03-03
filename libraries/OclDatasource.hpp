class OclDatasource; 

class SQLStatement; 

class OclDatasource {
public: 
  string url;
  string protocol;
  string host;
  string file;
  int port;
  string name;
  string passwd;
  string schema;
  string requestMethod;
  sqlite3* database;
  HINTERNET httpConnection; 
  HINTERNET httpRequest; 
  SOCKET clientSocket;
  unsigned char hasLocalSession; 
  SOCKET serverSocket;
  unsigned char hasRemoteSession; 

  static OclDatasource* createOclDatasource();

  static OclDatasource* getConnection(string url, string name, string passwd);

  static OclDatasource* newOclDatasource();

  static OclDatasource* newURL(string s);

  static OclDatasource* newURL_PHF(string p, string h, string f);

  static OclDatasource* newURL_PHNF(string p, string h, int n, string f);

  SQLStatement* createStatement_OclDatasource();

  SQLStatement* prepare(string stat); 

  SQLStatement* prepareStatement(string stat);

  SQLStatement* prepareCall(string stat);

  OclIterator<void*>* query_String(string stat);

  OclIterator<void*>* query_String_Sequence(string stat, vector<string>* cols);

  OclIterator<void*>* rawQuery(string stat, vector<void*>* pos);

  void execSQL(string stat);

  void abort();

  void commit();

  void rollback();

  void close();

  void closeFile();

  void connect();

  OclDatasource* openConnection(); 

  void setSchema(string s);

  void setRequestMethod(string m)
  { requestMethod = m; }

  string getSchema();
  
  string getURL();

  string getFile();
  
  string getHost();

  int getPort();

  string getProtocol();

  OclFile* getInputStream(); 

  OclFile* getOutputStream();

  string getContent(); 

  static OclDatasource* newSocket(string host, int port); 

  static OclDatasource* newServerSocket(int port, int limit);
  
  OclDatasource* accept();

};

class SQLStatement {
public:
  string text; 
  OclDatasource* connection; 
  sqlite3_stmt* statement;
  OclIterator<void*>* resultSet;  

  static SQLStatement* createSQLStatement() { 
    SQLStatement* res = new SQLStatement(); 
    res->text = ""; 
    res->connection = NULL; 
    res->statement = NULL; 
    res->resultSet = NULL; 
    return res;
  } 

  static SQLStatement* newSQLStatement() { 
	  return createSQLStatement(); 
  } 

  void close()
  { if (statement != NULL) 
    { sqlite3_finalize(statement); }
  } 

  void closeOnCompletion() { } 

  void setObject(int field, sqlite3_value* value)
  {
        if (statement != NULL)
        {
            int typ = sqlite3_value_type(value); 
            if (typ == SQLITE_INTEGER)
            {
                sqlite3_bind_int(statement, field, sqlite3_value_int(value));
            } 
            else if (typ == SQLITE_FLOAT)
            {
                sqlite3_bind_double(statement, field, sqlite3_value_double(value)); 
            }
            else if (typ == SQLITE_TEXT)
            {
                const unsigned char* ss = sqlite3_value_text(value); 
                sqlite3_bind_text(statement, field, (const char*) ss, -1, NULL);
            }
        } 
  }

  void setParameters(vector<void*>* pars)
  { if (statement != NULL) 
    { int i = 1; 
      int n = pars->size(); 
      for ( ; i <= n; i++) 
      { setObject(i, (sqlite3_value*) pars->at(i-1)); } 
    }
  } 

  void setString(int field, string value)
  { if (statement != NULL) 
    { sqlite3_bind_text(statement, field, value.c_str(), -1, NULL); } 
  } 

  void setInt(int field, int value)
  { if (statement != NULL) 
    { sqlite3_bind_int(statement, field, value); } 
  } 

  void setByte(int field, int value)
  { if (statement != NULL) 
    { sqlite3_bind_int(statement, field, value); } 
  } 

  void setShort(int field, int value)
  { if (statement != NULL) 
    { sqlite3_bind_int(statement, field, value); } 
  } 

  void setBoolean(int field, bool value)
  { if (statement != NULL) 
    { sqlite3_bind_int(statement, field, value); } 
  } 

  void setLong(int field, long value)
  { if (statement != NULL) 
    { sqlite3_bind_int64(statement, field, value); } 
  } 

  void setDouble(int field, double value)
  { if (statement != NULL) 
    { sqlite3_bind_double(statement, field, value); } 
  } 

  void setTimestamp(int field, OclDate* value)
  { if (statement != NULL) 
    { sqlite3_bind_int64(statement, field, value->getTime()); } 
  } 

  void setNull(int field, void* value)
  { if (statement != NULL) 
    { sqlite3_bind_null(statement, field); } 
  } 

  void execute(string stat)
  { if (connection != NULL)
    { connection->execSQL(stat); } 
  }  

  
  OclIterator<void*>* executeQuery(string stat)
  { if (connection != NULL)
    { resultSet = connection->query_String(stat); 
      return resultSet; 
    }
    return NULL; 
  } 

  void executeUpdate()
  { if (statement != NULL) 
    { sqlite3_step(statement); } 
    else 
    { execute(text); }  
  }

  OclDatasource* getConnection() 
  { return connection; } 

  OclIterator<void*>* getResultSet() 
  { return resultSet; } 

  void cancel() 
  { if (statement != NULL) 
    { sqlite3_reset(statement); } 
  }

   OclIterator<void*>* executeParameterisedQuery(vector<void*>* pars);
}; 

