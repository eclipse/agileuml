import Foundation
import Darwin
import SQLite3
import SwiftSocket

class OclDatasource : InternetCallback
{ private static var instance : OclDatasource? = nil
  var url : String = ""
  var prot : String = ""
  var host : String = ""
  var file : String = ""
  var port : Int = 0
  var name : String = ""
  var passwd : String = ""
  var schema : String = ""
  var requestMethod : String = ""
  var dbPointer : OpaquePointer? 

  var delegate : InternetCallback? = nil

  var urlSession = URLSession.shared
  var internetFileContents : String = ""
    
    
    var clientSocket : TCPClient?
    var serverSocket : TCPServer?

  func setDelegate(d : InternetCallback)
  { delegate = d }

  init() { }

  init(copyFrom: OclDatasource) {
    self.url = copyFrom.url
    self.prot = copyFrom.prot
    self.host = copyFrom.host
    self.file = copyFrom.file
    self.port = copyFrom.port
    self.name = copyFrom.name
    self.passwd = copyFrom.passwd
    self.schema = copyFrom.schema
    self.requestMethod = copyFrom.requestMethod
    self.dbPointer = copyFrom.dbPointer
    self.delegate = copyFrom.delegate
    self.urlSession = copyFrom.urlSession
    self.clientSocket = copyFrom.clientSocket
    self.serverSocket = copyFrom.serverSocket
  }

  func copy() -> OclDatasource
  { let res : OclDatasource = OclDatasource(copyFrom: self)
    addOclDatasource(instance: res)
    return res
  }

  static func defaultInstanceOclDatasource() -> OclDatasource
  { if (instance == nil)
    { instance = createOclDatasource() }
    return instance!
  }

  deinit
  { killOclDatasource(obj: self) }


  static func getConnection(url : String, name : String, passwd : String) -> OclDatasource
  {
    let db : OclDatasource = createOclDatasource()
    db.url = url
    db.name = name
    db.passwd = passwd
    
    var dbp: OpaquePointer?
  
    if sqlite3_open(url, &dbp) == SQLITE_OK 
    { db.dbPointer = dbp }

    return db
  }


  static func newOclDatasource() -> OclDatasource
  {
    let db : OclDatasource = createOclDatasource()
    return db
  }

    private func sendRequest(string: String, using client: TCPClient) -> String? {
      
      switch client.send(string: string) {
      case .success:
        return readResponse(from: client)
      case .failure(let error):
        print(">> Socket request error: " + String(describing: error))
        return nil
      }
    }
    
    private func readResponse(from client: TCPClient) -> String? {
      guard let response = client.read(1024*10) else { return nil }
      
      return String(bytes: response, encoding: .utf8)
    }

    static func newSocket(host : String, port : Int) -> OclDatasource
    {
      let db : OclDatasource = createOclDatasource()
      db.host = host
      db.port = port
      db.clientSocket = TCPClient(address: host, port: Int32(port))
      switch db.clientSocket.connect(timeout: 10) {
      case .success:
        if let response = sendRequest(string: "GET / HTTP/1.0\n\n", using: db.clientSocket) {
          db.internetFileContents = response
        }
      case .failure(let error):
        print(">> Socket connection error: " + String(describing: error))
      }
      return db
    }

    static func newServerSocket(port : Int, limit : Int) -> OclDatasource
    { let db : OclDatasource = createOclDatasource()
      db.port = port
      let server = TCPServer(address: "127.0.0.1", port: Int32(port))
      db.serverSocket = server
      return db
    }

    func accept() -> OclDatasource
    { guard serverSocket != nil
      else
      { return self }

      switch serverSocket.listen() {
        case .success:
          while true {
              if var client = server.accept() {
                let db : OclDatasource = createOclDatasource()
                db.port = port
                db.clientSocket = client
                return db
              } else {
                return self
              }
          }
        case .failure(let error):
          print(error)
      }
      return self
    }

  static func newURL(s : String) -> OclDatasource
  {
    let db : OclDatasource = createOclDatasource()
    db.url = s
    return db
  }


  static func newURL_PHF(p : String, h : String, f : String) -> OclDatasource
  {
    let db : OclDatasource = createOclDatasource()
    db.prot = p
    db.host = h
    db.file = f
    db.url = p + "://" + h + "/" + f
    return db
  }


  static func newURL_PHNF(p : String, h : String, n : Int, f : String) -> OclDatasource
  {
    let db : OclDatasource = createOclDatasource()
    db.prot = p
    db.host = h
    db.port = n
    db.file = f
    db.url = p + "://" + h + ":" + n + "/" + f
    return db
  }


  func createStatement() throws -> SQLStatement
  {
    let ss : SQLStatement = createSQLStatement()
    ss.text = ""
    ss.dbPointer = dbPointer
    ss.database = self

    var statement: OpaquePointer?
    guard sqlite3_prepare_v2(dbPointer, ss.text, -1, &statement, nil) 
        == SQLITE_OK
    else 
    { return ss }

    ss.statement = statement
    return ss
  }


  func prepare(stat : String) throws -> SQLStatement
  { let ss : SQLStatement = createSQLStatement()
    ss.text = stat
    ss.dbPointer = dbPointer
    ss.database = self

    var statement: OpaquePointer?
    guard sqlite3_prepare_v2(dbPointer, ss.text, -1, &statement, nil) 
        == SQLITE_OK
    else 
    { return ss }

    ss.statement = statement
    return ss
  }


  func prepareStatement(stat : String) throws -> SQLStatement
  {
    let res = try prepare(stat: stat)
    return res
  }


  func prepareCall(stat : String) throws -> SQLStatement
  {
    let res = try prepare(stat: stat)
    return res
  }


  func query_String(stat : String) throws -> OclIterator
  {
    let sqlstat = try prepare(stat: stat)

    guard sqlstat.statement != nil 
    else 
    { return OclIterator.newOclIterator_Sequence(sq: []) }

    let queryStatement = sqlstat.statement

    var rows : [[String:Any]] = [[String:Any]]()
    var cnames : [String] = [String]()

    while (sqlite3_step(queryStatement) == SQLITE_ROW)
    { let cnt : Int32 = sqlite3_column_count(queryStatement)
      var colname : [String] = [String]()
      var row : [String:Any] = [String:Any]()

      for i in 0...(cnt-1) 
      { let nme = sqlite3_column_origin_name(queryStatement,i)
        colname.append(String(cString: nme!))

        let cde : Int32 = sqlite3_column_type(queryStatement,i)
        if cde == SQLITE_INTEGER  
        { let queryIntResult = sqlite3_column_int(queryStatement, i)
          row[colname[Int(i)]] = queryIntResult
        }
        else if cde == SQLITE_FLOAT
        { let queryDoubleResult = sqlite3_column_double(queryStatement, i)
          row[colname[Int(i)]] = queryDoubleResult
        } 
        else if cde == SQLITE_BLOB
        { /* array of bytes */ } 
        else if cde == SQLITE3_TEXT
        { guard let queryTextResult = sqlite3_column_text(queryStatement, i)
          else 
          { row[colname[Int(i)]] = ""
            continue
          }
          row[colname[Int(i)]] = String(cString: queryTextResult)
        } 
      }  

      cnames = colname
      rows.append(row)
    } 

    let result = OclIterator.newOclIterator_Sequence(sq: rows)
    result.columnNames = cnames

    sqlite3_finalize(queryStatement)
    return result
  }


  func rawQuery(stat : String, pos : [Any]) throws -> OclIterator
  {
    let sqlstat = try prepare(stat: stat)

    guard sqlstat.statement != nil
    else
    { return OclIterator.newOclIterator_Sequence(sq: []) }

    sqlstat.setParameters(pars: pos)
    
    let queryStatement = sqlstat.statement

    var rows : [[String:Any]] = [[String:Any]]()
    var cnames : [String] = [String]()

    while (sqlite3_step(queryStatement) == SQLITE_ROW)
    { let cnt : Int32 = sqlite3_column_count(queryStatement)
      var colname : [String] = [String]()
      var row : [String:Any] = [String:Any]()

      for i in 0...(cnt-1)
      { let nme = sqlite3_column_origin_name(queryStatement,i)
        colname.append(String(cString: nme!))

        let cde : Int32 = sqlite3_column_type(queryStatement,i)
        if cde == SQLITE_INTEGER
        { let queryIntResult = sqlite3_column_int(queryStatement, i)
          row[colname[Int(i)]] = queryIntResult
        }
        else if cde == SQLITE_FLOAT
        { let queryDoubleResult = sqlite3_column_double(queryStatement, i)
          row[colname[Int(i)]] = queryDoubleResult
        }
        else if cde == SQLITE_BLOB
        { /* array of bytes */ }
        else if cde == SQLITE3_TEXT
        { guard let queryTextResult = sqlite3_column_text(queryStatement, i)
          else
          { row[colname[Int(i)]] = ""
            continue
          }
          row[colname[Int(i)]] = String(cString: queryTextResult)
        }
      }

      cnames = colname
      rows.append(row)
    }

    let result = OclIterator.newOclIterator_Sequence(sq: rows)
    result.columnNames = cnames

    sqlite3_finalize(queryStatement)
    return result
  }


  func nativeSQL(stat : String) throws -> String
  {
    let sqlstat = try prepare(stat: stat)

    guard sqlstat.statement != nil
    else
    { return "" }

    let queryStatement = sqlstat.statement

    while (sqlite3_step(queryStatement) == SQLITE_ROW)
    { let cnt : Int32 = sqlite3_column_count(queryStatement)
      
      for i in 0...(cnt-1)
      {
        let cde : Int32 = sqlite3_column_type(queryStatement,i)
        if cde == SQLITE_INTEGER
        { let queryIntResult = sqlite3_column_int(queryStatement, i)
          return String(queryIntResult)
        }
        else if cde == SQLITE_FLOAT
        { let queryDoubleResult = sqlite3_column_double(queryStatement, i)
          return String(queryDoubleResult)
        }
        else if cde == SQLITE_BLOB
        { /* array of bytes */ }
        else if cde == SQLITE3_TEXT
        { guard let queryTextResult = sqlite3_column_text(queryStatement, i)
          else
          { return "" }
          return String(cString: queryTextResult)
        }
      }
    }
    return ""
  } // First column of first result


  func query_String_Sequence(stat : String, cols : [String]) throws -> OclIterator
  {
    let res = try query_String(stat: stat)
    return res
  }


  func execSQL(stat : String) throws -> Void
  {
    let sqlStat = try prepare(stat: stat)
    defer 
    { sqlite3_finalize(sqlStat.statement)
    }
    sqlite3_step(sqlStat.statement)
  }

  func abort() -> Void
  {

  }


  func close() -> Void
  {
    guard self.dbPointer != nil
    else 
    { 
      return 
    } 
    sqlite3_close(self.dbPointer)
  }

  func closeFile() -> Void
  { self.close() } 

  func commit() -> Void
  {

  }


  func rollback() -> Void
  {

  }


  func connect() -> Void
  {

  }


  func openConnection() -> OclDatasource
  { if (url.count > 0)
    { self.delegate = self
      accessURL(url: url)
    } 
    return self
  }


  func setSchema(s : String) -> Void
  {
    schema = s
  }

  func setRequestMethod(method : String) -> Void
  {
    requestMethod = method
  }


  func getSchema() -> String
  {
    return schema
  }


  func getInputStream() -> OclFile
  { if internetFileContents.count == 0 && clientSocket != nil
    { let data = readResponse(from: clientSocket!)
      if data != nil {
        internetFileContents = data!
      }
    }

    let result : OclFile = OclFile.newOclFile(nme: url)
    result.lines = Ocl.toLineSequence(str: internetFileContents)
    result.characters = Ocl.chars(str: internetFileContents)
    result.writeMode = false
    return result
  }


  func getOutputStream() -> OclFile
  {
    let result : OclFile = OclFile.defaultInstance()
    return result
  }


  func getURL() -> String
  {
    return url
  }

  func internetAccessCompleted(response : String)
  { internetFileContents = response }

  func getContent() -> Any
  {
    return internetFileContents
  }


  func getFile() -> String
  {
    return file
  }


  func getHost() -> String
  {
    return host
  }


  func getPort() -> Int
  {
    return port
  }


  func getProtocol() -> String
  {
    return prot
  }

  func accessURL(url : String)
  { let urlref = URL(string: url)
    let task = urlSession.dataTask(with: urlref!)
    { (data,response,error) in
      if let _ = error
      { self.delegate?.internetAccessCompleted(response: "") }
      else if let _ = response
      { var res : String = ""
        for (_,x) in data!.enumerated()
        { res = res + String(Character(Unicode.Scalar(x))) }
        
        self.delegate?.internetAccessCompleted(response: res)
      }
    }
    task.resume()
  }
}

protocol InternetCallback
{ func internetAccessCompleted(response : String) }



var OclDatasource_allInstances : [OclDatasource] = [OclDatasource]()

func createOclDatasource() -> OclDatasource
{ let result : OclDatasource = OclDatasource()
  OclDatasource_allInstances.append(result)
  return result
}

func addOclDatasource(instance : OclDatasource)
{ OclDatasource_allInstances.append(instance) }

func killOclDatasource(obj: OclDatasource)
{ OclDatasource_allInstances = OclDatasource_allInstances.filter{ $0 !== obj } }


