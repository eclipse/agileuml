package main

import "container/list"
import "fmt"
import "ocl"
import "ocliterator"
import "oclfile"
// import "strings"
// import "math"
import "reflect"
import "database/sql"
// import "os"
import "github.com/go-sql-driver/mysql"

import "net/http"
import "io"



type OclDatasource struct {
  url string
  protocol string
  host string
  ipaddr *list.List
  file string
  port int
  requestMethod string
  connectionLimit int
  name string
  passwd string
  schema string
  actualDatabase *sql.DB
  internetContents string
}

var OclDatasource_instances = list.New()


func createOclDatasource() *OclDatasource {
  var res *OclDatasource
  res = &OclDatasource{}
  OclDatasource_instances.PushBack(res)
  return res
}

var TYPEOclDatasource = reflect.TypeOf(&OclDatasource{})

func GetConnection(url string, name string,  passwd string) *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  db.url = url
  db.name = name
  db.passwd = passwd
  cfg := mysql.Config{
        User:   name,
        Passwd: passwd,
        Net:    "tcp",
        Addr:   "127.0.0.1:3306",
        DBName: url,
    }
  // Get a database handle.
  var err error
  var dbase *sql.DB
  dbase, err = sql.Open("mysql", cfg.FormatDSN())
  db.actualDatabase = dbase 
  if err != nil { 
    fmt.Println(err) 
  } 
  result = db
  return result
}

func NewOclDatasource() *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  result = db
  return result
}

func NewSocket(host string,  port int) *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  db.host = host
  db.port = port
  result = db
  return result
}

func newServerSocket(port int,  limit int) *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  db.port = port
  db.connectionLimit = limit
  result = db
  return result
}

func (self *OclDatasource) accept() *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  result = db
  return result
}

func NewURL(s string) *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  db.url = s
  result = db
  return result
}

func NewURL_PHF(p string, h string,  f string) *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  db.protocol = p
  db.host = h
  db.file = f
  result = db
  return result
}

func NewURL_PHNF(p string, h string, n int,  f string) *OclDatasource {
  var result *OclDatasource = nil
  var db *OclDatasource = nil
  db = createOclDatasource()
  db.protocol = p
  db.host = h
  db.port = n
  db.file = f
  result = db
  return result
}

func (self *OclDatasource) CreateStatement() *SQLStatement {
  return self.Prepare("")
}

func (self *OclDatasource) Prepare(stat string) *SQLStatement {
  var ss *SQLStatement = nil
  ss = NewSQLStatement()
  ss.text = stat
  ss.databaseConnection = self
  if self.actualDatabase != nil { 
    stmt, _ := self.actualDatabase.Prepare(stat)
    ss.actualStatement = stmt
  } 
  return ss
}

func (self *OclDatasource) PrepareStatement(stat string) *SQLStatement {
  return self.Prepare(stat)
}

func (self *OclDatasource) PrepareCall(stat string) *SQLStatement {
  return self.Prepare(stat)
}

func (self *OclDatasource) Query_String(stat string) *ocliterator.OclIterator {
  var result *ocliterator.OclIterator = nil
  if self.actualDatabase != nil { 
    records := list.New()
    rows, err := self.actualDatabase.Query(stat)
    fmt.Println(err)
    defer rows.Close()

    var cols []string

    for rows.Next() { 
      cols, _ = rows.Columns()
      m := len(cols) 
      row := make(map[string]interface{})
      values := make([]interface{}, m)
      for i := 0; i < m; i++ { 
        var xi interface{}
        values[i] = &xi
      }
      rows.Scan(values...)
      for i := 0; i < m; i++ { 
        ptr := values[i].(*interface{})
        row[cols[i]] = *ptr
      } 
      records.PushBack(row)
    } 

    result = ocliterator.NewOclIterator_Sequence(records)

    n := len(cols)
    colNames := make([]interface{}, n)
    for i := 0; i < n; i++ { 
      colNames[i] = cols[i]
    } 
    result.SetColumnNames(ocl.SequenceRange(colNames))
  } 
  return result
}

func (self *OclDatasource) RawQuery(stat string,  pos *list.List) *ocliterator.OclIterator {
  var result *ocliterator.OclIterator = nil
  parvals := ocl.AsArray(pos)
  if self.actualDatabase != nil { 
    records := list.New()
    rows, err := self.actualDatabase.Query(stat, parvals...)
    fmt.Println(err)
    defer rows.Close()

    var cols []string

    for rows.Next() { 
      cols, _ = rows.Columns()
      m := len(cols) 
      row := make(map[string]interface{})
      values := make([]interface{}, m)
      for i := 0; i < m; i++ { 
        var xi interface{}
        values[i] = &xi
      }
      rows.Scan(values...)
      for i := 0; i < m; i++ { 
        ptr := values[i].(*interface{})
        row[cols[i]] = *ptr
      } 
      records.PushBack(row)
    } 

    result = ocliterator.NewOclIterator_Sequence(records)
    n := len(cols)
    colNames := make([]interface{}, n)
    for i := 0; i < n; i++ { 
      colNames[i] = cols[i]
    } 
    result.SetColumnNames(ocl.SequenceRange(colNames))
  } 

  return result

}

func (self *OclDatasource) NativeSQL(stat string) string {
  var result string = ""
  if self.actualDatabase != nil { 
    rows, _ := self.actualDatabase.Query(stat)
    defer rows.Close()

    var cols []string

    for rows.Next() { 
      cols, _ = rows.Columns()
      m := len(cols) 

      values := make([]interface{}, m)
      for i := 0; i < m; i++ { 
        var xi interface{}
        values[i] = &xi
      }
      rows.Scan(values...)
      ptr := values[0].(*interface{})
      return ocl.ToStringOclAny(*ptr)
    } 
  } 
  return result
}

func (self *OclDatasource) Query_String_Sequence(stat string,  cols *list.List) *ocliterator.OclIterator {
  return self.Query_String(stat)
}

func (self *OclDatasource) query_String_Array(stat string,  parvals []interface{}) *ocliterator.OclIterator {
  var result *ocliterator.OclIterator = nil
  if self.actualDatabase != nil { 
    records := list.New()
    rows, _ := self.actualDatabase.Query(stat, parvals...)
    
    defer rows.Close()

    var cols []string

    for rows.Next() { 
      cols, _ = rows.Columns()
      m := len(cols) 
      row := make(map[string]interface{})
      values := make([]interface{}, m)
      for i := 0; i < m; i++ { 
        var xi interface{}
        values[i] = &xi
      }
      rows.Scan(values...)
      for i := 0; i < m; i++ { 
        ptr := values[i].(*interface{})
        row[cols[i]] = *ptr
      } 
      records.PushBack(row)
    } 

    result = ocliterator.NewOclIterator_Sequence(records)
    n := len(cols)
    colNames := make([]interface{}, n)
    for i := 0; i < n; i++ { 
      colNames[i] = cols[i]
    } 
    result.SetColumnNames(ocl.SequenceRange(colNames))
  } 

  return result
}

func (self *OclDatasource) ExecSQL(stat string) {
  if self.actualDatabase != nil { 
    self.actualDatabase.Exec(stat)
  } 
}

func (self *OclDatasource) Abort() {

}

func (self *OclDatasource) Close() {

}

func (self *OclDatasource) Commit() {
}

func (self *OclDatasource) Rollback() {
}

 
func (self *OclDatasource) connect() { 
  resp, err := http.Get(self.url)
  if err != nil { 
    return
  } 
  defer resp.Body.Close()
  body, err := io.ReadAll(resp.Body)
  if err != nil { 
    self.internetContents = string(body)
  } 
} 

func (self *OclDatasource) openConnection() *OclDatasource {
  var result *OclDatasource = nil
  result = self
  return result
}

func (self *OclDatasource) SetRequestMethod( method string) {
  self.requestMethod = method
}

func (self *OclDatasource) SetSchema( s string) {
  self.schema = s
}

func (self *OclDatasource) GetSchema() string {
  var result string = ""
  result = self.schema
  return result
}

func (self *OclDatasource) getInputStream() *oclfile.OclFile {
  var res *oclfile.OclFile = oclfile.NewOclFile(self.url)
  res.SetInputStream(self.internetContents)
  return oclfile.NewOclFile_Read(res)
}

func (self *OclDatasource) getOutputStream() *oclfile.OclFile {
  var result *oclfile.OclFile = nil
  return result
}

func (self *OclDatasource) GetURL() string {
  var result string = ""
  result = self.url
  return result
}

func (self *OclDatasource) getContent() interface{} {
  var result interface{} = 0
  return result
}

func (self *OclDatasource) GetFile() string {
  var result string = ""
  result = self.file
  return result
}

func (self *OclDatasource) GetHost() string {
  var result string = ""
  result = self.host
  return result
}

func GetLocalHost() *OclDatasource {
  var result *OclDatasource = nil
  var d *OclDatasource = nil
  d = createOclDatasource()
  d.host = "localhost"
  d.ipaddr = ocl.InitialiseSequence(127,0,0,1)
  result = d
  return result
}

func (self *OclDatasource) GetAddress() *list.List {
  var result *list.List = list.New()
  result = self.ipaddr
  return result
}

func (self *OclDatasource) GetPort() int {
  var result int = 0
  result = self.port
  return result
}

func (self *OclDatasource) GetProtocol() string {
  var result string = ""
  result = self.protocol
  return result
}

/* func assignValues(vals ...interface{}) { 
  n := len(vals)
  for i := 0; i < n; i++ { 
    ptr := vals[i].(*interface{})
    *ptr = i
  } 
} 

func main() { 
  cols := ocl.InitialiseSequence("a", "b", "c")
  row := make(map[string]interface{})
  values := make([]interface{}, 3)
  
  for i := 0; i < 3; i++ { 
    var xi interface{}
    values[i] = &xi
  }
  
//  *values[0] = 0
//  *values[1] = 1
//  *values[2] = 2

  assignValues(values...)

  for i := 0; i < 3; i++ { 
     ptr := values[i].(*interface{})
     row[(ocl.At(cols,i+1)).(string)] = *ptr
  } 
  fmt.Println(row["b"])
} 

*/ 

