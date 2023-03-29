import Foundation
import Darwin
import SQLite3

class SQLStatement
{ private static var instance : SQLStatement? = nil
  var text : String = ""
  var statement: OpaquePointer?
  var dbPointer : OpaquePointer?
  var database : OclDatasource?
  var resultSet : OclIterator = OclIterator.defaultInstance() 
    
  init() { }

  init(copyFrom: SQLStatement) {
    self.text = copyFrom.text
    self.statement = copyFrom.statement
    self.dbPointer = copyFrom.dbPointer
    self.resultSet = copyFrom.resultSet
    self.database = copyFrom.database
  }

  func copy() -> SQLStatement
  { let res : SQLStatement = SQLStatement(copyFrom: self)
    addSQLStatement(instance: res)
    return res
  }

  static func defaultInstanceSQLStatement() -> SQLStatement
  { if (instance == nil)
    { instance = createSQLStatement() }
    return instance!
  }

  deinit
  { killSQLStatement(obj: self) }

  func close() -> Void
  {
    if (statement != nil)
    { sqlite3_finalize(statement!) } 
    if (dbPointer != nil)
    { sqlite3_close(dbPointer!) } 
  }


  func closeOnCompletion() -> Void
  {

  }

/* 
func insert() {
  var insertStatement: OpaquePointer?
  // 1
  if sqlite3_prepare_v2(db, insertStatementString, -1, &insertStatement, nil) == 
      SQLITE_OK {
    let id: Int32 = 1
    let name: NSString = "Ray"
    // 2
    sqlite3_bind_int(insertStatement, 1, id)
    // 3
    sqlite3_bind_text(insertStatement, 2, name.utf8String, -1, nil)
    // 4
    if sqlite3_step(insertStatement) == SQLITE_DONE {
      print("\nSuccessfully inserted row.")
    } else {
      print("\nCould not insert row.")
    }
  } else {
    print("\nINSERT statement is not prepared.")
  }
  // 5
  sqlite3_finalize(insertStatement)
}
 */
    
    func setParameters(pars : [Any])
    { for (i,x) in pars.enumerated() {
        setObject(field: i+1, value: x)
      }
    }
    
    func setObject(field : Int, value : Any) -> Void
    {
      if (statement != nil)
      { sqlite3_bind_value(statement!, Int32(field), value as? OpaquePointer) }
    }


  func setString(field : Int, value : String) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_text(statement!, Int32(field), value, -1, nil) }
  }


  func setInt(field : Int, value : Int) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_int(statement!, Int32(field), Int32(value)) }
  }


  func setByte(field : Int, value : Int) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_int(statement!, Int32(field), Int32(value)) }
  }


  func setShort(field : Int, value : Int) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_int(statement!, Int32(field), Int32(value)) }
  }


  func setBoolean(field : Int, value : Bool) -> Void
  { guard statement != nil 
    else 
    { return }

    if value == true 
    { sqlite3_bind_int(statement, Int32(field), 1) }
    else 
    { sqlite3_bind_int(statement, Int32(field), 0) }
  }


  func setLong(field : Int, value : Int64) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_int64(statement!, Int32(field), value) }
  }


  func setDouble(field : Int, value : Double) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_double(statement!, Int32(field), value) }
  }


  func setTimestamp(field : Int, value : OclDate) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_int64(statement!, Int32(field), value.getTime()) }
  }


  func setNull(field : Int, value : Any) -> Void
  {
    if (statement != nil) 
    { sqlite3_bind_null(statement!, Int32(field)) }
  }


  func executeUpdate() throws -> Void
  {
    guard statement != nil
    else 
    { try database!.execSQL(stat: text)
      return
    }

    sqlite3_step(statement!)    
  }


  func executeQuery(stat : String) throws -> OclIterator
  {
    guard dbPointer != nil
    else 
    { return OclIterator.newOclIterator_Sequence(sq: []) }

    resultSet = try database!.query_String(stat: stat)
    return resultSet
  }


  func executeQuery() throws -> OclIterator
  { let res = try executeQuery(stat: text)
    return res
  }


  func execute(stat : String) throws -> Void
  {
    guard dbPointer != nil
    else 
    { return }

    try database!.execSQL(stat: stat)
  }


  func execute() throws -> Void
  {
    try executeUpdate()
  }


  func cancel() -> Void
  {
    if statement != nil 
    { sqlite3_reset(statement!) }
  }


  func getConnection() -> OclDatasource
  {
    return database!
  }


  func getResultSet() -> OclIterator
  {
    return resultSet
  }

}




var SQLStatement_allInstances : [SQLStatement] = [SQLStatement]()

func createSQLStatement() -> SQLStatement
{ let result : SQLStatement = SQLStatement()
  SQLStatement_allInstances.append(result)
  return result
}

func addSQLStatement(instance : SQLStatement)
{ SQLStatement_allInstances.append(instance) }

func killSQLStatement(obj: SQLStatement)
{ SQLStatement_allInstances = SQLStatement_allInstances.filter{ $0 !== obj } }


