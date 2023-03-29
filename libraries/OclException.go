package oclexception

import "errors"
import "fmt"
import "reflect"

type OclException struct {
  message string
}

var TYPEOclException = reflect.TypeOf(&OclException{})

func NewOclException(msg string) *OclException { 
  res := &OclException{}
  res.message = msg
  return res
} 

func (self *OclException) Error() string { 
  return self.message
} 


func (self *OclException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *OclException) GetMessage() string { 
  return self.message
}

func (self *OclException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 


type SystemException struct {
  message string
}

var TYPESystemException = reflect.TypeOf(&SystemException{})

func NewSystemException(msg string) *SystemException { 
  res := &SystemException{}
  res.message = msg
  return res
} 

func (self *SystemException) Error() string { 
  return self.message
} 


func (self *SystemException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *SystemException) GetMessage() string { 
  return self.message
}

func (self *SystemException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 


type ProgramException struct {
  message string
}

var TYPEProgramException = reflect.TypeOf(&ProgramException{})

func NewProgramException(msg string) *ProgramException { 
  res := &ProgramException{}
  res.message = msg
  return res
} 

func (self *ProgramException) Error() string { 
  return self.message
} 


func (self *ProgramException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *ProgramException) GetMessage() string { 
  return self.message
}

func (self *ProgramException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 



type ArithmeticException struct {
  message string
}

var TYPEArithmeticException = reflect.TypeOf(&ArithmeticException{})

func NewArithmeticException(msg string) *ArithmeticException { 
  res := &ArithmeticException{}
  res.message = msg
  return res
} 

func (self *ArithmeticException) Error() string { 
  return self.message
} 


func (self *ArithmeticException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *ArithmeticException) GetMessage() string { 
  return self.message
}

func (self *ArithmeticException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 



type IOException struct {
  message string
}

var TYPEIOException = reflect.TypeOf(&IOException{})

func NewIOException(msg string) *IOException { 
  res := &IOException{}
  res.message = msg
  return res
} 

func (self *IOException) Error() string { 
  return self.message
} 


func (self *IOException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *IOException) GetMessage() string { 
  return self.message
}

func (self *IOException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 



type IndexingException struct {
  message string
}

var TYPEIndexingException = reflect.TypeOf(&IndexingException{})

func NewIndexingException(msg string) *IndexingException { 
  res := &IndexingException{}
  res.message = msg
  return res
} 

func (self *IndexingException) Error() string { 
  return self.message
} 


func (self *IndexingException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *IndexingException) GetMessage() string { 
  return self.message
}

func (self *IndexingException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 


type CastingException struct {
  message string
}

var TYPECastingException = reflect.TypeOf(&CastingException{})

func NewCastingException(msg string) *CastingException { 
  res := &CastingException{}
  res.message = msg
  return res
} 

func (self *CastingException) Error() string { 
  return self.message
} 


func (self *CastingException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *CastingException) GetMessage() string { 
  return self.message
}

func (self *CastingException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 



type AssertionException struct {
  message string
}

var TYPEAssertionException = reflect.TypeOf(&AssertionException{})

func NewAssertionException(msg string) *AssertionException { 
  res := &AssertionException{}
  res.message = msg
  return res
} 

func (self *AssertionException) Error() string { 
  return self.message
} 


func (self *AssertionException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *AssertionException) GetMessage() string { 
  return self.message
}

func (self *AssertionException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 


type NullAccessException struct {
  message string
}

var TYPENullAccessException = reflect.TypeOf(&NullAccessException{})

func NewNullAccessException(msg string) *NullAccessException { 
  res := &NullAccessException{}
  res.message = msg
  return res
} 

func (self *NullAccessException) Error() string { 
  return self.message
} 


func (self *NullAccessException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *NullAccessException) GetMessage() string { 
  return self.message
}

func (self *NullAccessException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 



type AccessingException struct {
  message string
}

var TYPEAccessingException = reflect.TypeOf(&AccessingException{})

func NewAccessingException(msg string) *AccessingException { 
  res := &AccessingException{}
  res.message = msg
  return res
} 

func (self *AccessingException) Error() string { 
  return self.message
} 


func (self *AccessingException) PrintStackTrace() { 
  fmt.Println(self.message)
}

func (self *AccessingException) GetMessage() string { 
  return self.message
}

func (self *AccessingException) GetCause() *OclException { 
  err := errors.Unwrap(self)
  return NewOclException(err.Error())
} 





