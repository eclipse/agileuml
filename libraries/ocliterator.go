package ocliterator

import "container/list"
import "ocl"
// import "fmt"

type OclIterator struct {
  position int
  elements *list.List
  currentElement *list.Element
}

func createOclIterator() *OclIterator {
  var res *OclIterator
  res = &OclIterator{}
  return res
}

func NewOclIterator_Sequence(col *list.List) *OclIterator {
  var result *OclIterator
  result = createOclIterator()
  result.elements = col
  if col.Len() > 0 { 
    result.position = 1
    result.currentElement = col.Front()
  } else { 
    result.position = 0
    result.currentElement = nil
  } 

  return result
}

func NewOclIterator_Set(col *list.List) *OclIterator {
  var result *OclIterator
  result = createOclIterator()
  result.elements = col
  if col.Len() > 0 { 
    result.position = 1
    result.currentElement = col.Front()
  } else { 
    result.position = 0
    result.currentElement = nil
  } 
  return result
}

func NewOclIterator_String(s string) *OclIterator {
  var result *OclIterator
  result = createOclIterator()
  col := ocl.Split(s,"[ \\n\\t]+")
  result.elements = col
  if col.Len() > 0 { 
    result.position = 1
    result.currentElement = col.Front()
  } else { 
    result.position = 0
    result.currentElement = nil
  } 
  return result
}

func NewOclIterator_String_String(s string, r string) *OclIterator {
  var result *OclIterator
  result = createOclIterator()
  col := ocl.Split(s,"[" + r + "]+")
  result.elements = col
  if col.Len() > 0 { 
    result.position = 1
    result.currentElement = col.Front()
  } else { 
    result.position = 0
    result.currentElement = nil
  } 
  return result
}

func (self *OclIterator) At(i int) interface{} { 
  return ocl.At(self.elements,i)
} 

func (self *OclIterator) HasNext() bool { 
  return 0 <= self.position && 
         self.position <= self.elements.Len()
}

func (self *OclIterator) HasPrevious() bool {
  return self.position >= 1 &&
         self.position <= self.elements.Len() + 1
}

func (self *OclIterator) NextIndex() int { 
  return self.position + 1
}

func (self *OclIterator) PreviousIndex() int {
  return self.position - 1
}

func (self *OclIterator) MoveForward() { 
  if self.position <= self.elements.Len() { 
    self.position = self.position + 1
  }

  if (self.currentElement != nil) {
    self.currentElement = self.currentElement.Next() 
  }
}

func (self *OclIterator) MoveBackward() {
  if self.position >= 1 {
    self.position = self.position - 1
  }

  if (self.currentElement != nil) {
    self.currentElement = self.currentElement.Prev() 
  }
}

func (self *OclIterator) MoveTo(n int) { 
  if 0 <= n && n <= self.elements.Len() + 1 { 
    self.position = n
    i := 0
    for x := self.elements.Front(); x != nil; x = x.Next() { 
      if n == i+1 {
        self.currentElement = x
        return
      } 
      i = i+1
    }
  }
}

func (self *OclIterator) GetCurrent() interface{} { 
  if self.currentElement != nil { 
    return self.currentElement.Value
  } 

  if 1 <= self.position && 
     self.position <= self.elements.Len() {
    return ocl.At(self.elements,self.position) 
  }
  return nil
}

func (self *OclIterator) getElement() *list.Element {
  i := 0
  for x := self.elements.Front(); x != nil; x = x.Next() { 
    if self.position == i+1 {
      return x
    } 
    i = i+1
  }
  return nil
}

func (self *OclIterator) Set(v interface{}) { 
  if self.currentElement != nil { 
    self.currentElement.Value = v
    return
  } 

  i := 0
  for x := self.elements.Front(); x != nil; x = x.Next() { 
    if self.position == i+1 {
      x.Value = v
      return
    } 
    i = i+1
  }
}

func (self *OclIterator) Remove() { 
  if self.currentElement != nil { 
    newCurrent := self.currentElement.Next()
    self.elements.Remove(self.currentElement)
    self.currentElement = newCurrent
    return
  } 

  x := self.getElement()
  if x != nil {
    self.elements.Remove(x)
  }
}

func (self *OclIterator) Insert(v interface{}) { 
  if self.currentElement != nil { 
    self.currentElement = self.elements.InsertBefore(v,self.currentElement)
    return
  }

  x := self.getElement()
  if x != nil {
    self.elements.InsertBefore(v,x)
  }
}
  

func (self *OclIterator) Next() interface{} {
  curr := self.GetCurrent()
  self.MoveForward()
  return curr 
} 

func (self *OclIterator) Previous() interface{} {
  curr := self.GetCurrent()
  self.MoveBackward()
  return curr
} 

func (self *OclIterator) Length() int { 
  return self.elements.Len()
} 




