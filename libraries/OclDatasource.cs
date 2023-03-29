

    class OclDatasource
    { protected SqlConnection connection;
      public String url = ""; 
      public String protocol = ""; 
      public String host = "";
      public ArrayList ipaddr = new ArrayList(); 

      public String file = ""; 
      public int port = 0;
      public Uri address = null;
      static readonly HttpClient client = new HttpClient();
      string requestMethod = "GET"; 

      public String name = ""; 
      public String passwd = ""; 
      public String schema = "";
      public TcpClient socket = null;
      public Socket clientSocket = null; 
      public TcpListener serverSocket = null;
      public int connectionLimit = 0; 

        public static OclDatasource createOclDatasource()
        { return new OclDatasource(); }

        public static OclDatasource newOclDatasource()
        { return new OclDatasource(); }

        public static OclDatasource getConnection(String url,
            String name, String passwd)
        { OclDatasource res = createOclDatasource();
            res.url = url;
            res.name = name;
            res.passwd = passwd;
            res.connection = new SqlConnection(url);
            try { res.connection.Open(); }
            catch (Exception ex) { } 

            return res; 
        }

        public SQLStatement prepare(string stat)
        { if (connection != null)
            {
                SqlCommand command = new SqlCommand(null, connection);
                command.CommandText = stat;
                command.CommandType = CommandType.StoredProcedure; 
                command.Prepare();
                SQLStatement res = new SQLStatement();
                res.text = stat;
                res.command = command;
                res.connection = connection;
                res.database = this; 
                return res; 
            }
            return null; 
        }

        public SQLStatement createStatement()
        {
            if (connection != null)
            {
                SqlCommand command = new SqlCommand(null, connection);
                SQLStatement res = new SQLStatement();
                res.text = "";
                res.command = command;
                res.connection = connection;
                res.database = this;
                return res;
            }
            return null;
        }

        public OclIterator queryString(string stat)
        {
            SQLStatement sql = prepare(stat);
            if (sql != null)
            { return sql.executeQuery(); }
            return null;
        }

        public OclIterator rawQuery(string stat, ArrayList pos)
        { /* pos is list of parameter values, in order */ 

            SQLStatement sql = prepare(stat);
            if (sql != null)
            { sql.setParameters(pos); 
              return sql.executeQuery(); 
            }
            return null;
        }

        public OclIterator query_String_Sequence(string stat, ArrayList cols)
        { /* cols are the column names to select */ 
            SQLStatement sql = prepare(stat);
            if (sql != null)
            { return sql.executeQuery(); }
            return null;
        }

        public SQLStatement prepareStatement(string stat)
        { return prepare(stat); }

        public SQLStatement prepareCall(string stat)
        { return prepare(stat); }

        public String nativeSQL(string stat)
        {
            if (connection != null)
            {
                SqlCommand command = new SqlCommand(stat, connection);
                command.CommandType = CommandType.Text;
                connection.Open();
                return "" + command.ExecuteScalar();
            }
            return null; 
        }
        
        public void execSQL(string stat)
        {
            if (connection != null)
            {
              SqlCommand command = new SqlCommand(stat, connection);
              command.CommandType = CommandType.Text;
              connection.Open();
              command.ExecuteNonQuery();
              return;
            }
        }

        public void abort() { }

        public void close()
        {
            if (clientSocket != null)
            { clientSocket.Close(); }
        } 

        public void commit() { }

        public void rollback() { } 

        public void setRequestMethod(string met)
        { requestMethod = met;  }

        public static OclDatasource newSocket(string host, int port)
        {
            OclDatasource res = createOclDatasource();
            res.name = host;
            res.host = host; 
            res.port = port;
            try
            {
                TcpClient client = new TcpClient(host, port);
                res.socket = client; 
            }
            catch { } 

            return res; 
        }

        public static OclDatasource newServerSocket(int port, int limit)
        {
            OclDatasource res = createOclDatasource();
            
            res.port = port;
            res.connectionLimit = limit; 

            try
            {
                byte[] localhost = { 127, 0, 0, 1 }; 
                TcpListener server = new TcpListener(new System.Net.IPAddress(localhost), port);
                res.serverSocket = server;
            }
            catch { }

            return res;
        }

        public OclDatasource accept()
        {  if (serverSocket != null)
            { Socket client = serverSocket.AcceptSocket(); 
              if (client.Connected)
                {
                    OclDatasource ds = new OclDatasource(); 
                    ds.clientSocket = client;
                    return ds; 
                }
            }
            return this; 
        }

        public static OclDatasource newURL(string s)
        { Uri uri = new Uri(s);
            OclDatasource res = new OclDatasource();
            res.url = s;
            res.address = uri;
            res.protocol = uri.Scheme;
            res.host = uri.Host;
            res.file = uri.PathAndQuery;
            res.port = uri.Port; 
            return res; 
        }

        public static OclDatasource getLocalHost()
        {
            Uri uri = new Uri("http://127.0.0.1/");
            OclDatasource res = new OclDatasource();
            res.url = "http://127.0.0.1";
            res.address = uri;
            res.protocol = "http";
            res.host = "localhost";
            res.file = "";
            res.port = 80;
            res.ipaddr = new ArrayList();
            res.ipaddr.Add(127); res.ipaddr.Add(0);
            res.ipaddr.Add(0); res.ipaddr.Add(1);
            return res;
        }

        public static OclDatasource newURL_PHF(string p, string h, string f)
        {
            string s = p + "://" + h + "/" + f; 
            Uri uri = new Uri(s);
            OclDatasource res = new OclDatasource();
            res.url = s;
            res.address = uri;
            res.protocol = p;
            res.host = h;
            res.file = f;
            res.port = 0;
            return res;
        }

        public static OclDatasource newURL_PHNF(string p, string h, int n, string f)
        {
            string s = p + "://" + h + ":" + n + "/" + f;
            Uri uri = new Uri(s);
            OclDatasource res = new OclDatasource();
            res.url = s;
            res.address = uri;
            res.protocol = p;
            res.host = h;
            res.file = f;
            res.port = n;
            return res;
        }

        public string getURL() 
        { return url; }

        public async Task<object> getContent()
        {
            if (address != null)
            {
                try
                {
                    HttpResponseMessage response = await client.GetAsync(address);
                    response.EnsureSuccessStatusCode();
                    string responseBody = await response.Content.ReadAsStringAsync();
                    return responseBody;
                }
                catch (Exception _ex) { return null; }
            }
            return null;
        } 

    public string getFile()
    { return file; }

    public string getHost() 
    { return host; }

    public ArrayList getAddress()
    { return ipaddr; }

    public int getPort() 
    { return port; }

    public string getProtocol() 
    { return protocol; }

    public void setSchema(string s)
    { schema = s; }

    public string getSchema()
    { return schema; }

    public void connect() { }  

    public OclDatasource openConnection()
    { return this; }

    public async Task<OclFile> getInputStream()
    { if (address != null)
      {
         try
         {
                    System.IO.Stream stream = await client.GetStreamAsync(address);
                    OclFile res = new OclFile();
                    res.setname(url);
                    res.setInputStream(stream);
                    return res;
         }
         catch (Exception _ex) { return null; }
       }

       if (clientSocket != null)
       { NetworkStream ns = new NetworkStream(clientSocket);
         OclFile res = new OclFile();
         res.setname("ClientSocket");
         res.setInputStream(ns);
         return res; 
       }
       return null; 
    }


        public async Task<OclFile> getOutputStream()
        { if (address != null)
            {
                try
                {
                    System.IO.Stream stream = await client.GetStreamAsync(url);
                    OclFile res = new OclFile();
                    res.setname(url);
                    res.setOutputStream(stream);
                    return res;
                }
                catch (Exception _ex) { return null; }
            }

            if (clientSocket != null)
            {
                NetworkStream ns = new NetworkStream(clientSocket);
                OclFile res = new OclFile();
                res.setname("ClientSocket");
                res.setOutputStream(ns);
                return res;
            }

            return null;
        }


  }

    class SQLStatement
    { public String text = "";
        public SqlCommand command = null;
        public SqlConnection connection = null;
        public OclDatasource database = null;
        public OclIterator resultSet = null; 

        public void close()
        {  if (command != null)
            { command.Cancel();  }
        }


        public void closeOnCompletion() { }

        public void setString(int field, String value)
        {
            if (command != null)
            { command.Parameters[field].Value = value; }
        }

        public void setInt(int field, int value)
        {
            if (command != null)
            { command.Parameters[field].Value = value; }
        }

        public void setByte(int field, int value)
        {
            if (command != null)
            { command.Parameters[field].Value = value; }
        }

        public void setShort(int field, int value)
        {
            if (command != null)
            { command.Parameters[field].Value = value; }
        }

        public void setBoolean(int field, bool value)
        {
            if (command != null)
            { command.Parameters[field].Value = value; }
        }

        public void setLong(int field, long value)
        {
            if (command != null)
            { command.Parameters[field].Value = value; }
        }

        public void setDouble(int field, double value)
        {
            if (command != null)
            { command.Parameters[field].Value = value; }
        }

        public void setTimestamp(int field, OclDate value)
        {
            if (command != null)
            { command.Parameters[field].Value = value.getTime(); }
        }

        public void setParameters(ArrayList pars)
        {
            if (command == null)
            { return; }

            for (int i = 0; i < pars.Count; i++)
            {
                object par = pars[i];
                command.Parameters[i].Value = par;
            }
        }

        public void executeUpdate()
        { if (command != null)
          { command.ExecuteNonQuery(); }
          else if (text != null && connection != null)
          { command = new SqlCommand(text, connection);
            command.CommandType = CommandType.Text;
            connection.Open();
            command.ExecuteNonQuery(); 
          }
        }

       public void execute(String stat)
        {
            if (stat != null && connection != null)
            {
                command = new SqlCommand(stat, connection);
                command.CommandType = CommandType.Text;
                connection.Open();
                command.ExecuteNonQuery();
            }
            else if (command != null)
            { command.ExecuteNonQuery(); }
        }

        public void execute()
        { executeUpdate(); }
 
        public OclIterator executeQuery(String stat)
        { OclIterator res = null;
            ArrayList records = new ArrayList();
            ArrayList columnNames = new ArrayList();

            if (command == null && connection != null)
            { command = new SqlCommand(stat, connection);
                command.CommandType = CommandType.Text;
                connection.Open();
            } 
            
            if (command != null)
            { SqlDataReader rdr = command.ExecuteReader();

                while (rdr.Read())
                {
                    columnNames = new ArrayList(); 

                    int n = rdr.FieldCount;
                    Hashtable record = new Hashtable();

                    for (int i = 0; i < n; i++)
                    {
                        String fname = rdr.GetName(i);
                        columnNames.Add(fname); 
                        record[fname] = rdr.GetValue(i);
                    }
                    records.Add(record); 
                } 
                rdr.Close();
                res = OclIterator.newOclIterator_Sequence(records);
                res.columnNames = columnNames; 
            }
            return res; 
        }

        public OclIterator executeQuery()
        { return executeQuery(text); }

        public OclDatasource getConnection()
        { return database; }

        public OclIterator getResultSet()
        { return resultSet; }

    }
