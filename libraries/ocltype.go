// package ocliterator

package main

import "container/list"
// import "ocl"
import "fmt"
import "reflect"

type OclAttribute struct {
  name string
  fieldtype *OclType
}

type OclOperation struct {
  name string
  returntype *OclType
  parameters *list.List
}

type OclType struct {
  name string
  actualType reflect.Type
  attributes *list.List
  operations *list.List
  constructors *list.List
  superclasses *list.List
}

var Index_OclType map[string]*OclType = make(map[string]*OclType)


func GetByPKOclType(nme string) *OclType { 
  return Index_OclType[nme]
}


func createOclAttribute() *OclAttribute {
  var res *OclAttribute
  res = &OclAttribute{}
  return res
}

func (self *OclAttribute) GetName() string {
  return self.name
}

func (self *OclAttribute) GetType() *OclType {
  return self.fieldtype
}

func (self *OclOperation) GetName() string {
  return self.name
}

func (self *OclOperation) GetType() *OclType {
  return self.returntype
}

func (self *OclOperation) GetParameters() *list.List {
  return self.parameters
}

func (self *OclOperation) GetReturnType() *OclType {
  return self.returntype
}

func createOclOperation() *OclOperation {
  var res *OclOperation
  res = &OclOperation{}
  return res
}

func createOclType() *OclType {
  var res *OclType
  res = &OclType{}
  return res
}

func CreateByPKOclType(nme string) *OclType { 
  typ := Index_OclType[nme]
  if typ != nil { 
    return typ
  } 
  typ = createOclType()
  typ.name = nme
  Index_OclType[nme] = typ
  typ.attributes = list.New()
  typ.operations = list.New()
  typ.constructors = list.New()
  typ.superclasses = list.New()
  return typ
} 

func (self *OclType) GetName() string {
  return self.name
}

func (self *OclType) GetFields() *list.List {
  self.attributes = list.New()
  if self.actualType.Kind() == reflect.Struct { 
  } else { 
    return self.attributes
  } 

  nf := self.actualType.NumField()
  for i := 0; i < nf; i++ {
    fld := self.actualType.Field(i)
    att := createOclAttribute()
    att.name = fld.Name
    typ := createOclType()
    typ.name = fld.Type.Name()
    typ.actualType = fld.Type
    att.fieldtype = typ
    self.attributes.PushBack(att)
  } 
  return self.attributes
}

func (self *OclType) GetField(nme string) *OclAttribute {
  fld, ok := self.actualType.FieldByName(nme)
  if ok == true { 
    att := createOclAttribute()
    att.name = fld.Name
    typ := createOclType()
    typ.name = fld.Type.Name()
    typ.actualType = fld.Type
    att.fieldtype = typ
    return att
  }
  return nil
}


func (self *OclType) GetMethods() *list.List {
  self.operations = list.New()
  nf := self.actualType.NumMethod()
  for i := 0; i < nf; i++ {
    met := self.actualType.Method(i)
    op := createOclOperation()
    op.name = met.Name
    typ := createOclType()
    typ.name = met.Type.Name()
    typ.actualType = met.Type
    op.returntype = typ
    self.operations.PushBack(op)
  } 
  return self.operations
}

func (self *OclType) GetMethod(nme string) *OclOperation { 
  op, ok := self.actualType.MethodByName(nme)
  if ok == true { 
    met := createOclOperation()
    met.name = op.Name
    typ := createOclType()
    typ.name = op.Type.Name()
    typ.actualType = op.Type
    met.returntype = typ
    return met
  } 
  return nil
}


func (self *OclType) GetConstructors() *list.List {
  return self.constructors
}

func (self *OclType) AddSuperclass(sup *OclType) { 
  if self.superclasses == nil { 
    self.superclasses = list.New()
  } 

  self.superclasses.PushBack(sup)
} 

func (self *OclType) GetSuperclass() *OclType {
  if self.superclasses != nil && self.superclasses.Len() > 0 {
    frst := self.superclasses.Front()
    return frst.Value.(*OclType)
  } 
  return nil
}


type Person struct { 
  name string
  age int
} 

func main() { 
  ptyp := CreateByPKOclType("Person")
  ptyp.actualType = reflect.TypeOf(Person{})
  lst := ptyp.GetFields()

  for x := lst.Front(); x != nil; x = x.Next() { 
    att := (x.Value).(*OclAttribute)
    fmt.Println(att.name)
  }
} 
