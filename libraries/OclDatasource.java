import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.util.function.Function;
import java.io.Serializable;

import java.sql.Connection; 
import java.sql.Statement; 
import java.sql.PreparedStatement; 
import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.ResultSetMetaData;
import java.sql.SQLException; 
 
import java.net.URL; 
import java.net.HttpURLConnection;
import java.net.Socket; 
import java.net.ServerSocket; 
import java.net.InetAddress; 


class OclDatasource { 
  static ArrayList<OclDatasource> OclDatasource_allInstances = new ArrayList<OclDatasource>();

  OclDatasource() { OclDatasource_allInstances.add(this); }

  static OclDatasource createOclDatasource() 
  { OclDatasource result = new OclDatasource();
    return result; 
  }

  String url = "";
  URL address = null; 
  InetAddress inetaddr = null; 
  ArrayList<Integer> ipaddr = new ArrayList<Integer>(); 
  String protocol = "";
  String host = "";
  String file = "";
  int port = 0;
  String requestMethod = "GET"; 
  HttpURLConnection httpConnection = null; 

  String name = "";
  String passwd = "";
  String schema = "";

  Socket clientSocket = null; 
  ServerSocket serverSocket = null; 
  int connectionLimit = 0; 

  String ocldatasourceId = ""; /* primary */
  static Map<String,OclDatasource> OclDatasource_index = new HashMap<String,OclDatasource>();

  Connection connection = null; 
  Statement statement = null; 
  ResultSet resultSet = null; 

  static OclDatasource createByPKOclDatasource(String ocldatasourceIdx)
  { OclDatasource result = OclDatasource.OclDatasource_index.get(ocldatasourceIdx);
    if (result != null) { return result; }
    result = new OclDatasource();
    OclDatasource.OclDatasource_index.put(ocldatasourceIdx,result);
    result.ocldatasourceId = ocldatasourceIdx;
    return result; 
  }

  static void killOclDatasource(String ocldatasourceIdx)
  { OclDatasource rem = OclDatasource_index.get(ocldatasourceIdx);
    if (rem == null) { return; }
    ArrayList<OclDatasource> remd = new ArrayList<OclDatasource>();
    remd.add(rem);
    OclDatasource_index.remove(ocldatasourceIdx);
    OclDatasource_allInstances.removeAll(remd);
  }


  public static OclDatasource getConnection(String url, String name, String passwd)
  {
    OclDatasource db = createOclDatasource();
    db.url = url;
    db.name = name;
    db.passwd = passwd;

    try { 
      db.connection = 
        DriverManager.getConnection(url,name,passwd); 
      db.statement = db.connection.createStatement(); 
    } 
    catch (SQLException ex) 
    { ex.printStackTrace(); }
  
    return db; 
  }


  public static OclDatasource newOclDatasource()
  {
    OclDatasource result = createOclDatasource(); 
    return result;
  }


  public static OclDatasource newSocket(String host, int port)
  {
    OclDatasource db = createOclDatasource();
    db.host = host;
    db.port = port;
    try { 
      db.clientSocket = new Socket(host,port);
    } catch (Exception _ex) { }  
    return db; 
  }

  public static OclDatasource newServerSocket(int port, int limit)
  {
    OclDatasource db = createOclDatasource();
    db.connectionLimit = limit;
    db.port = port;
    try { 
      db.serverSocket = new ServerSocket(port,limit);
    } catch (Exception _ex) { }  
    return db; 
  }

  public static OclDatasource newURL(String s)
  {
    OclDatasource db = createOclDatasource();
    db.url = s;
    try { 
      db.address = new URL(s); 
      db.protocol = db.address.getProtocol(); 
      db.host = db.address.getHost(); 
      db.file = db.address.getFile(); 
      db.port = db.address.getPort(); 
    } catch (Exception _ex) { db.address = null; }  
    return db; 
  }

  public static OclDatasource getLocalHost()
  {
    OclDatasource db = createOclDatasource();
    db.url = "http://127.0.0.1";
    try { 
      db.address = new URL(db.url); 
      db.protocol = "http"; 
      db.host = "localhost"; 
      db.file = ""; 
      db.port = 80;
      db.inetaddr = InetAddress.getLocalHost(); 
      db.ipaddr = Ocl.initialiseSequence(127,0,0,1);  
    } catch (Exception _ex) { db.address = null; }  
    return db; 
  }


  public static OclDatasource newURL_PHF(String p, String h, String f)
  {
    OclDatasource db = createOclDatasource();
    db.protocol = p;
    db.host = h;
    db.file = f;
    try { 
      db.address = new URL(p,h,"/" + f); 
      db.port = db.address.getPort(); 
      db.url = db.address.toString(); 
    } catch (Exception _ex) 
      { _ex.printStackTrace(); 
        db.address = null; 
      }  
    return db; 
  }


  public static OclDatasource newURL_PHNF(String p, String h, int n, String f)
  {
    OclDatasource db = createOclDatasource();
    db.protocol = p;
    db.host = h;
    db.port = n;
    db.file = f;
    try { 
      db.address = new URL(p,h,n,"/" + f);
      db.url = db.address.toString(); 
    } catch (Exception _ex) { db.address = null; }  
    return db; 
  }


  public SQLStatement createStatement()
  {
    SQLStatement ss = SQLStatement.createSQLStatement();
    ss.text = "";
    ss.connection = connection; 

    if (connection != null) 
    { try { 
        statement = connection.createStatement();
        ss.statement = statement;  
      } 
      catch (SQLException ex) 
      { ex.printStackTrace(); }
    } 

    return ss; 
  }


  public SQLStatement prepare(String stat)
  {
    SQLStatement ss = SQLStatement.createSQLStatement();
    ss.text = stat;
    ss.connection = connection; 
    ss.database = this; 

    if (connection != null) 
    { try { 
        statement = connection.prepareStatement(stat);
        ss.statement = statement;  
      } 
      catch (SQLException ex) 
      { ex.printStackTrace(); }
    } 

    return ss; 
  }


  public SQLStatement prepareStatement(String stat)
  {
    return prepare(stat);
  }


  public SQLStatement prepareCall(String stat)
  {
    SQLStatement ss = SQLStatement.createSQLStatement();
    ss.text = stat;
    ss.connection = connection; 
    ss.database = this; 

    if (connection != null) 
    { try { 
        statement = connection.prepareCall(stat);
        ss.statement = statement;  
      } 
      catch (SQLException ex) 
      { ex.printStackTrace(); }
    } 

    return ss; 
  }


  public OclIterator query_String(String stat)
  {
    OclIterator result = null;
    ArrayList<String> cnames = new ArrayList<String>(); 
    ArrayList<Map<String,Object>> records = 
        new ArrayList<Map<String,Object>>(); 

    SQLStatement queryStatement = prepare(stat); 
    
    if (statement != null) 
    { try { 
        ResultSet rs = 
          ((PreparedStatement) statement).executeQuery(); 

        ResultSetMetaData md = rs.getMetaData(); 
        int nc = md.getColumnCount(); 
        
        for (int i = 1; i <= nc; i++) 
        { cnames.add(md.getColumnName(i)); } 

        while (rs.next())
        { Map<String,Object> record = 
            new HashMap<String,Object>();
          for (int j = 1; j <= nc; j++) 
          { Object val = rs.getObject(j); 
            record.put(md.getColumnName(j), val); 
          } 
          records.add(record); 
        }  
      } 
      catch (SQLException ex) 
      { ex.printStackTrace(); } 
    } 

    result = OclIterator.newOclIterator_Sequence(records); 
    result.columnNames = cnames;  
    return result;
  }


  public OclIterator rawQuery(String stat, ArrayList pos)
  {
 OclIterator result = null;
    ArrayList<String> cnames = new ArrayList<String>(); 
    ArrayList<Map<String,Object>> records = 
        new ArrayList<Map<String,Object>>(); 

    SQLStatement queryStatement = prepare(stat); 
    
    if (statement != null) 
    { try { 
        SQLStatement.setParameters((PreparedStatement)
                                          statement,pos); 
        ResultSet rs = ((PreparedStatement) 
                                statement).executeQuery(); 

        ResultSetMetaData md = rs.getMetaData(); 
        int nc = md.getColumnCount(); 
        
        for (int i = 1; i <= nc; i++) 
        { cnames.add(md.getColumnName(i)); } 

        while (rs.next())
        { Map<String,Object> record = 
            new HashMap<String,Object>();
          for (int j = 1; j <= nc; j++) 
          { Object val = rs.getObject(j); 
            record.put(md.getColumnName(j), val); 
          } 
          records.add(record); 
        }  
      } 
      catch (SQLException ex) 
      { ex.printStackTrace(); } 
    } 

    result = OclIterator.newOclIterator_Sequence(records); 
    result.columnNames = cnames;  
    return result;
  }


  public String nativeSQL(String stat)
  {
    if (connection != null) 
    try { 
      return connection.nativeSQL(stat); 
    } catch (Exception ex) { } 

    return null;
  }


  public OclIterator query_String_Sequence(String stat, ArrayList<String> cols)
  {
    return query_String(stat);
  }


  public void execSQL(String stat)
  {
    SQLStatement updateStatement = prepare(stat); 
    if (statement != null) 
    { try { 
        statement.execute(stat);   
      } 
      catch (SQLException ex) 
      { ex.printStackTrace(); } 
    } 
  }


  public void abort()
  { if (connection != null) 
    { try 
      { connection.abort(null); } 
      catch (Exception ex) { }
    }  
  }


  public void close()
  { if (connection != null)
    { try { 
        connection.close(); 
      } 
      catch (SQLException sq) { } 
    }

    if (clientSocket != null) 
    { try { 
        clientSocket.close(); 
      } catch (Exception _ex) { } 
    } 
  }

  public void closeFile()
  { this.close(); } 

  public void commit()
  { if (connection != null) 
    { try 
      { connection.commit(); } 
      catch (Exception ex) { }
    }
  }


  public void rollback()
  { if (connection != null) 
    { try 
      { connection.rollback(); } 
      catch (Exception ex) { }
    }
  }


  public void connect()
  { if (httpConnection != null) 
    { try { httpConnection.connect(); }
      catch (Exception _ex) { } 
    }  
  }

  public void setRequestMethod(String method) 
  { requestMethod = method; 
    if (httpConnection != null) 
    { try { 
        httpConnection.setRequestMethod(method); 
      } catch (Exception _ex) { } 
    }  
  } 

  public OclDatasource openConnection()
  { if (address != null) 
    { try { 
        httpConnection = 
          (HttpURLConnection) address.openConnection(); 
      } catch (Exception _ex) 
        { _ex.printStackTrace(); 
          httpConnection = null;
        }
    }  
    return this; 
  }

  public OclDatasource accept()
  { if (serverSocket != null) 
    { try { 
        Socket conn = serverSocket.accept();
        OclDatasource ds = new OclDatasource(); 
        ds.clientSocket = conn; 
        return ds; 
      } catch (Exception _ex) { } 
    } 
    return null; 
  }  

  public void setSchema(String s)
  {
    schema = s;
  }


  public String getSchema()
  {
    return schema; 
  }


  public OclFile getInputStream()
  { if (httpConnection != null) 
    { OclFile result = new OclFile(url);
      try { 
        result.inputStream = httpConnection.getInputStream();
      } catch (Exception _ex) 
        { _ex.printStackTrace(); }  

      return result;
    } 

    if (clientSocket != null) 
    { OclFile conn = new OclFile("ClientSocketIn"); 
      try { 
        conn.inputStream = clientSocket.getInputStream(); 
      } catch (Exception _ex) 
        { _ex.printStackTrace(); }  

      return conn;
    } 

    return null; 
  }


  public OclFile getOutputStream()
  {
    if (httpConnection != null) 
    { OclFile result = new OclFile(url);
      try { 
        result.outputStream = httpConnection.getOutputStream();
      } catch (Exception _ex) { }  
    
      return result;
    } 

    if (clientSocket != null) 
    { OclFile conn = new OclFile("ClientSocketOut"); 
      try { 
        conn.outputStream = clientSocket.getOutputStream(); 
      } catch (Exception _ex) 
        { _ex.printStackTrace(); }  

      return conn;
    } 

    return null; 
  }


  public String getURL()
  {
    return url; 
  }


  public Object getContent()
  {
    if (address != null) 
    { try { 
        return address.getContent(); 
      }
      catch (Exception _ex) { return null; } 
    }  
    return null;
  }


  public String getFile()
  {
    return file; 
  }


  public String getHost()
  {
    return host; 
  }

  public ArrayList<Integer> getAddress()
  { if (host != null) 
    { try { 
        inetaddr = InetAddress.getByName(host); 
        byte[] addr = inetaddr.getAddress(); 
        ipaddr = new ArrayList<Integer>(); 
        for (int i = 0; i < addr.length; i++) 
        { ipaddr.add(new Integer(addr[i])); } 
        return ipaddr;
      } catch (Exception _ex) { return ipaddr; }  
    } 
    else 
    { return ipaddr; } 
  } 

  public int getPort()
  {
    return port; 
  }


  public String getProtocol()
  {
    return protocol;
  }

/*   public static void main(String[] args) 
  { OclDatasource kcl = 
      OclDatasource.newURL_PHF(
        "https", "nms.kcl.ac.uk", "kevin.lano/index.html");
    System.out.println(kcl.getURL()); 
    OclDatasource ws = kcl.openConnection(); 
    ws.connect(); 
    OclFile fs = ws.getInputStream();
    OclFile br = OclFile.newOclFile_Read(fs);  
    System.out.println(br.readAll());  
} */  

}

