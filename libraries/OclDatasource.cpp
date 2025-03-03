

OclIterator<void*>* SQLStatement::executeParameterisedQuery(vector<void*>* pars)
{ if (statement == NULL) 
  { return NULL; } 

  setParameters(pars); 

  vector<void*>* records = new vector<void*>();

  vector<string>* cnames = new vector<string>(); 

  while (sqlite3_step(statement) == SQLITE_ROW)
  { int ncol = sqlite3_column_count(statement);
 
      cnames = new vector<string>();
      map<string,void*>* record = new map<string,void*>(); 

      int idx = 0; 
      for ( ; idx < ncol; idx++)
      { const char* cname = sqlite3_column_name(statement, idx);
        cnames->push_back(string(cname)); 
        
        int ctype = sqlite3_column_type(statement, idx); 
        if (ctype == SQLITE_INTEGER)
        { int cvali = sqlite3_column_int(statement, idx); 
          (*record)[cname] = &cvali; 
        }
        else if (ctype == SQLITE_FLOAT)
        { double cvald = sqlite3_column_double(statement, idx);
          (*record)[cname] = &cvald; 
        } 
        else if (ctype == SQLITE_TEXT)
        { char* cvalt = (char*) sqlite3_column_text(statement, idx);
		  string sval = string(cvalt); 
          (*record)[cname] = &cvalt; 
        } 
      }
      
      records->push_back(record);  
    }
    resultSet = OclIterator<void*>::newOclIterator_Sequence(records); 
    resultSet->setColumnNames(cnames);  
 
  return resultSet; 
}





OclDatasource* OclDatasource::createOclDatasource() 
{ OclDatasource* result = new OclDatasource(); 
  result->url = "";
  result->protocol = "";
  result->host = "";
  result->file = "";
  result->port = 0;
  result->name = "";
  result->passwd = "";
  result->schema = "";
  result->requestMethod = "GET";
  result->database = NULL; 
  result->hasLocalSession = FALSE; 
  result->hasRemoteSession = FALSE;
  result->httpConnection = NULL; 
  result->httpRequest = NULL; 
  result->clientSocket = NULL; 
  result->serverSocket = NULL; 
  return result; 
}


OclDatasource* OclDatasource::getConnection(string url, string name, string passwd)
{ sqlite3* db;
  char* errs;  
  int succ = sqlite3_open(url.c_str(), &db);
 
  if (succ == SQLITE_OK)
  { 
    struct OclDatasource* res = OclDatasource::createOclDatasource();
    res->url = url;
    res->name = name;
    res->passwd = passwd;
    res->database = db; 
    return res; 
  }

  return NULL; 
}

OclDatasource* OclDatasource::newOclDatasource()
{
  return OclDatasource::createOclDatasource(); 
}

OclDatasource* OclDatasource::newURL(string s)
{
  OclDatasource* db = OclDatasource::createOclDatasource();
  db->url = s;
  return db; 
}


OclDatasource* OclDatasource::newURL_PHF(string p, string h, string f)
{
  struct OclDatasource* db = OclDatasource::createOclDatasource();
  db->protocol = p;
  db->host = h;
  db->file = f;
  string u1 = p + "://"; 
  string u2 = u1 + h;
  string u3 = u2 + "/"; 
  db->url = u3 + f; 
  return db; 
}

OclDatasource* OclDatasource::newURL_PHNF(string p, string h, int n, string f)
{
  OclDatasource* db = OclDatasource::createOclDatasource();
  db->protocol = p;
  db->host = h;
  db->port = n;
  db->file = f;
  ostringstream buff;
  buff <<  p + "://" + h + ":";
  buff << n;
  buff << "/" + f; 
  db->url = buff.str(); 
  return db; 
}

SQLStatement* OclDatasource::createStatement_OclDatasource()
{
  struct SQLStatement* ss = SQLStatement::createSQLStatement();
  ss->text = "";
  ss->connection = this; 

  if (database != NULL) 
  { sqlite3_stmt* statement; 
    const char* rem; 
    sqlite3_prepare(database, "", 0, &statement, &rem);
    ss->statement = statement;  
  } 

  return ss; 
}

OclDatasource* OclDatasource::newSocket(string host, int port)
{ WSADATA wsaData;

  int iResult;

  // Initialize Winsock
  iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
  if (iResult != 0) {
    printf("WSAStartup failed: %d\n", iResult);
    return NULL;
  }

  struct addrinfo *result = NULL,
                *ptr = NULL,
                hints;

  ZeroMemory( &hints, sizeof(hints) );
  hints.ai_family = AF_UNSPEC;
  hints.ai_socktype = SOCK_STREAM;
  hints.ai_protocol = IPPROTO_TCP;

  ostringstream ss; 
  ss << port; 
  string portstring = ss.str();  
  // Resolve the server address and port
  iResult = getaddrinfo(host.c_str(), portstring.c_str(), &hints, &result);
  if (iResult != 0) {
    printf("getaddrinfo failed: %d\n", iResult);
    WSACleanup();
    return NULL;
  }

  SOCKET ConnectSocket = INVALID_SOCKET;

  // Attempt to connect to the first address returned by
  // the call to getaddrinfo
  ptr=result;

  // Create a SOCKET for connecting to server
  ConnectSocket = socket(ptr->ai_family, ptr->ai_socktype, 
                         ptr->ai_protocol);

  if (ConnectSocket == INVALID_SOCKET) {
    printf("Error at socket(): %ld\n", WSAGetLastError());
    freeaddrinfo(result);
    WSACleanup();
    return NULL;
  }

  OclDatasource* res = OclDatasource::newOclDatasource(); 
  res->port = port; 
  res->host = host;
  res->clientSocket = ConnectSocket; 
  return res; 
}

OclDatasource* OclDatasource::newServerSocket(int port, int limit)
{ struct addrinfo *result = NULL, *ptr = NULL, hints;

  ZeroMemory(&hints, sizeof (hints));
  hints.ai_family = AF_INET;
  hints.ai_socktype = SOCK_STREAM;
  hints.ai_protocol = IPPROTO_TCP;
  hints.ai_flags = AI_PASSIVE;

// Resolve the local address and port to be used by the server
  ostringstream ss; 
  ss << port; 
  string portstring = ss.str();  
  int iresult = getaddrinfo(NULL, portstring.c_str(), &hints, &result);
  if (iresult != 0) {
    printf("getaddrinfo failed: %d\n", iresult);
    WSACleanup();
    return NULL;
  }

  SOCKET ListenSocket = INVALID_SOCKET;

  // Create a SOCKET for the server to listen for client connections

  ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);

  if (ListenSocket == INVALID_SOCKET) {
    printf("Error at socket(): %ld\n", WSAGetLastError());
    freeaddrinfo(result);
    WSACleanup();
    return NULL;
  }

  sockaddr_in service;
  service.sin_family = AF_INET;
  service.sin_addr.s_addr = inet_addr("127.0.0.1");
  service.sin_port = htons(port);

  bind(ListenSocket,
             (SOCKADDR*) &service, sizeof(service)); 

    //----------------------
    // Listen for incoming connection requests.
    // on the created socket
    if (listen(ListenSocket, 1) == SOCKET_ERROR) {
        wprintf(L"listen failed with error: %ld\n", WSAGetLastError());
        closesocket(ListenSocket);
        WSACleanup();
        return NULL;
    }

  OclDatasource* res = OclDatasource::newOclDatasource(); 
  res->port = port; 
	  // res->host = host; 
  res->serverSocket = ListenSocket; 
  return res; 
} 

OclDatasource* OclDatasource::accept()
{ SOCKET AcceptSocket;
  wprintf(L"Waiting for client to connect...\n");

  if (serverSocket == NULL)
  { return NULL; } 

  AcceptSocket = ::accept(serverSocket, NULL, NULL);
  if (AcceptSocket == INVALID_SOCKET) {
        wprintf(L"accept failed with error: %ld\n", WSAGetLastError());
        closesocket(serverSocket);
        WSACleanup();
        return NULL;
  } else
	{ wprintf(L"Client connected.\n"); }
 

    // No longer need server socket
    closesocket(serverSocket);

    WSACleanup();

	clientSocket = AcceptSocket; 
	return this;
}

SQLStatement* OclDatasource::prepare(string stat)
{
  SQLStatement* ss = SQLStatement::createSQLStatement();
  ss->text = stat;
  ss->connection = this; 

  if (database != NULL) 
  { sqlite3_stmt* statement; 
    const char* rem; 
    sqlite3_prepare(database, (ss->text).c_str(), -1, &statement, &rem);
    ss->statement = statement;  
  } 

  return ss; 
}

SQLStatement* OclDatasource::prepareStatement(string stat)
{
  return prepare(stat);
}

SQLStatement* OclDatasource::prepareCall(string stat)
{
  return prepare(stat);
}

OclIterator<void*>* OclDatasource::query_String(string stat)
{ char* errs;
  char** res; 
  int nrow; 
  int ncol; 
  vector<string>* cnames; 
  vector<void*>* resultSet = new vector<void*>();  

  if (database != NULL) 
  { int succ = sqlite3_get_table(database, stat.c_str(), &res, &nrow, &ncol, &errs); 
    if (succ == SQLITE_OK)
    { 
      cnames = new vector<string>(); 

      int cind = 0; 
      for ( ; cind < ncol; cind++) 
      { cnames->push_back(string(res[cind])); } 

      int r = 1; 
      for ( ; r <= nrow; r++) 
      { int c = 0;
        map<string,string>* record = new map<string,string>(); 
 
        for ( ; c < ncol; c++) 
        { int indx = r*ncol + c; 

          (*record)[string(res[c])] = string(res[indx]);  
        }    
 
        resultSet->push_back(record); 
      } 
 
      OclIterator<void*>* iter = OclIterator<void*>::newOclIterator_Sequence(resultSet); 
      iter->setColumnNames(cnames); 
      return iter;
    }  
  } 
  return NULL; 
}

OclIterator<void*>* OclDatasource::query_String_Sequence(string stat, vector<string>* cols)
{
  return query_String(stat);
}


OclIterator<void*>* OclDatasource::rawQuery(string stat, vector<void*>* pos)
{ SQLStatement* sql = this->prepare(stat); 
  return sql->executeParameterisedQuery(pos); 
}

void OclDatasource::execSQL(string stat)
{ char* errs; 

  if (database != NULL)
  { sqlite3_exec(database, 
                 stat.c_str(), 
                 NULL, NULL, &errs);
  } 
}

void OclDatasource::abort()
{ if (database != NULL)
  { sqlite3_interrupt(database); } 
}


void OclDatasource::close()
{ if (database != NULL)
  { sqlite3_close(database); }
  hasLocalSession = FALSE; 
  url = ""; 
  host = ""; 
  file = ""; 
  port = 0;
  passwd = "";
  schema = "";
  database = NULL; 
  protocol = ""; 
  if ( httpRequest != NULL ) WinHttpCloseHandle( httpRequest );
  if ( httpConnection != NULL ) WinHttpCloseHandle( httpConnection );
  httpRequest = NULL; 
  httpConnection = NULL;
}

void OclDatasource::closeFile()
{ if (database != NULL)
  { sqlite3_close(database); }
  hasLocalSession = FALSE; 
  url = ""; 
  host = ""; 
  file = ""; 
  port = 0;
  passwd = "";
  schema = "";
  database = NULL; 
  protocol = ""; 
  if ( httpRequest != NULL ) WinHttpCloseHandle( httpRequest );
  if ( httpConnection != NULL ) WinHttpCloseHandle( httpConnection );
  httpRequest = NULL; 
  httpConnection = NULL;
}

void OclDatasource::commit()
{  }


void OclDatasource::rollback()
{  }


void OclDatasource::connect()
{ BOOL bResults = FALSE;
  if ( httpConnection != NULL )
    httpRequest = WinHttpOpenRequest( httpConnection, L"GET", NULL,
                                   NULL, WINHTTP_NO_REFERER, 
                                   WINHTTP_DEFAULT_ACCEPT_TYPES, 
                                   WINHTTP_FLAG_SECURE );

  // Send a request.
  if ( httpRequest != NULL)
    bResults = WinHttpSendRequest( httpRequest,
                                   WINHTTP_NO_ADDITIONAL_HEADERS, 0,
                                   WINHTTP_NO_REQUEST_DATA, 0, 
                                   0, 0 );
}

OclDatasource* OclDatasource::openConnection()
{ HINTERNET hSession = NULL;

  // Use WinHttpOpen to obtain a session handle.
  hSession = WinHttpOpen( L"WinHTTP Example/1.0",  
                          WINHTTP_ACCESS_TYPE_DEFAULT_PROXY,
                          WINHTTP_NO_PROXY_NAME, 
                          WINHTTP_NO_PROXY_BYPASS, 0 );
  HINTERNET hConnect = NULL; 

  if ( hSession != NULL )
    hConnect = WinHttpConnect( hSession, LPCWSTR(url.c_str()),
                               INTERNET_DEFAULT_HTTPS_PORT, 0 );
  this->httpConnection = hConnect; 
  return this;
}

string OclDatasource::getContent()
{ BOOL bResults = FALSE;
  DWORD dwSize = 0;
  DWORD dwDownloaded = 0;
  LPSTR pszOutBuffer;
  ostringstream buff;

  if ( httpRequest != NULL )
    bResults = WinHttpReceiveResponse( httpRequest, NULL );

  // Keep checking for data until there is nothing left.
  if ( bResults )
  {
    do 
    {
      // Check for available data.
      dwSize = 0;
      if ( !WinHttpQueryDataAvailable( httpRequest, &dwSize ) )
        printf( "Error %u in WinHttpQueryDataAvailable.\n",
                GetLastError( ) );

      // Allocate space for the buffer.
      pszOutBuffer = new char[dwSize+1];
      if ( !pszOutBuffer )
      {
        printf( "Out of memory\n" );
        dwSize=0;
      }
      else
      {
        // Read the data.
        ZeroMemory( pszOutBuffer, dwSize+1 );

        if ( !WinHttpReadData( httpRequest, (LPVOID)pszOutBuffer, 
                              dwSize, &dwDownloaded ) )
          printf( "Error %u in WinHttpReadData.\n", GetLastError( ) );
        else
        { printf( "%s", pszOutBuffer );
		  buff << pszOutBuffer;
		}

        // Free the memory allocated to the buffer.
        delete [] pszOutBuffer;
      }
    } while ( dwSize > 0 );
  }

  // Report any errors.
  if( !bResults )
    printf( "Error %d has occurred.\n", GetLastError( ) );

  // Close any open handles.
  if ( httpRequest != NULL ) WinHttpCloseHandle( httpRequest );
  if ( httpConnection != NULL ) WinHttpCloseHandle( httpConnection );
  // if( hSession ) WinHttpCloseHandle( hSession );
  httpRequest = NULL; 
  httpConnection = NULL;
  return buff.str(); 
}


OclFile* OclDatasource::getInputStream()
{ if (clientSocket != NULL)
  { OclFile* r = OclFile::newOclFile_Remote(url, clientSocket); 
    return r; 
  } 

  if (serverSocket != NULL)
  { OclFile* r = OclFile::newOclFile_Remote(url, serverSocket); 
    return r; 
  }  

  if (httpRequest != NULL)
  { OclFile* fle = OclFile::newOclFile(url); 
    OclFile* res = OclFile::newOclFile_Write(fle);
    string contents = getContent(); 
	res->writeln(contents);
	res->flush(); 
	res->closeFile(); 
    return OclFile::newOclFile_Read(fle);
  } 
  return NULL;  
}


void OclDatasource::setSchema(string s)
{
  schema = s;
}


string OclDatasource::getSchema()
{
  return schema;
}

string OclDatasource::getURL()
{
  return url; 
}

string OclDatasource::getFile()
{
  return file; 
}


string OclDatasource::getHost()
{
  return host; 
}


int OclDatasource::getPort()
{
  return port; 
}


string OclDatasource::getProtocol()
{
  return protocol;
}

