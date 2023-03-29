package main

import "container/list"
// import "fmt"
import "ocl"
// import "strings"
// import "math"
import "reflect"
import "ocliterator"
import "ocldate"

import "database/sql"


type SQLStatement struct {
  text string
  actualStatement *sql.Stmt
  databaseConnection *OclDatasource
  parameterMap map[int]interface{}
  parameters []interface{}
  maxfield int
  resultSet *ocliterator.OclIterator
}

var SQLStatement_instances = list.New()


func createSQLStatement() *SQLStatement {
  var res *SQLStatement
  res = &SQLStatement{}
  SQLStatement_instances.PushBack(res)
  return res
}

func NewSQLStatement() *SQLStatement {
  res := createSQLStatement()
  res.actualStatement = nil
  res.databaseConnection = nil
  res.parameterMap = make(map[int]interface{})
  res.resultSet = nil
  res.maxfield = 0
  return res
}

var TYPESQLStatement = reflect.TypeOf(&SQLStatement{})

func (self *SQLStatement) SetStatement(s *sql.Stmt) { 
   self.actualStatement = s 
}

func (self *SQLStatement) SetConnection(c *OclDatasource) { 
   self.databaseConnection = c 
}

func (self *SQLStatement) Close() {
  if self.actualStatement != nil { 
    self.actualStatement.Close()
    self.actualStatement = nil
  } 
  self.parameterMap = make(map[int]interface{})
  self.parameters = nil
  self.resultSet = nil
  self.maxfield = 0
}

func (self *SQLStatement) CloseOnCompletion() {

}

func (self *SQLStatement) SetString(field int,  value string) {
  self.parameterMap[field] = value
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetInt(field int,  value int) {
  self.parameterMap[field] = value
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetByte(field int,  value int) {
  self.parameterMap[field] = value
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetShort(field int,  value int) {
  self.parameterMap[field] = value
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetBoolean(field int,  value bool) {
  self.parameterMap[field] = value
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetLong(field int,  value int64) {
  self.parameterMap[field] = value
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetDouble(field int,  value float64) {
  self.parameterMap[field] = value
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetTimestamp(field int,  value *ocldate.OclDate) {
  self.parameterMap[field] = value.GetTime()
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) SetNull(field int,  value interface{}) {
  self.parameterMap[field] = nil
  if field > self.maxfield { 
    self.maxfield = field
  } 
}

func (self *SQLStatement) parameterMapToFields() {
    self.parameters = make([]interface{}, self.maxfield)
    for x := 1; x <= self.maxfield; x++ {
      val := self.parameterMap[x] 
      self.parameters[x-1] = val
    } 
}

func (self *SQLStatement) ExecuteUpdate() {
  if self.actualStatement != nil { 
    self.actualStatement.Exec()
  } 
}

func (self *SQLStatement) query_String_Array(parvals []interface{}) *ocliterator.OclIterator {
  var result *ocliterator.OclIterator = nil
  if self.actualStatement != nil { 
    records := list.New()
    rows, _ := self.actualStatement.Query(parvals...)
    
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

func (self *SQLStatement) ExecuteQuery(stat ...string) *ocliterator.OclIterator {
  var result *ocliterator.OclIterator = nil
  if len(stat) == 0 { 
    self.parameterMapToFields()
    self.resultSet = self.query_String_Array(self.parameters)
    return self.resultSet
  } else { 
    ss := stat[0]
    if self.databaseConnection != nil { 
      self.resultSet = self.databaseConnection.Query_String(ss)
      return self.resultSet
    } 
  } // execute on the databaseConnection
  return result
}

func (self *SQLStatement) update_String_Array(parvals []interface{}) {
  if self.actualStatement != nil { 
    self.actualStatement.Exec(parvals...)
  }
}

func (self *SQLStatement) Execute(stat ...string) {
  if len(stat) == 0 { 
    self.parameterMapToFields()
    self.update_String_Array(self.parameters)
  } else { 
    ss := stat[0]
    if self.databaseConnection != nil { 
      self.databaseConnection.ExecSQL(ss)
    } 
  } // execute on the databaseConnection
}

func (self *SQLStatement) Cancel() {
}

func (self *SQLStatement) GetConnection() *OclDatasource {
  return self.databaseConnection
}

func (self *SQLStatement) GetResultSet() *ocliterator.OclIterator {
  return self.resultSet
}




