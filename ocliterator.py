
import ocl
import math
import re
import copy

from oclfile import *
from enum import Enum

def free(x):
  del x


def displayint(x):
  print(str(x))

def displaylong(x):
  print(str(x))

def displaydouble(x):
  print(str(x))

def displayboolean(x):
  print(str(x))

def displayString(x):
  print(x)

def displaySequence(x):
  print(x)

def displaySet(x):
  print(x)

def displayMap(x):
  print(x)


class OclIteratorResult : 
  def __init__(self):
    self.value = None
    self.done = True

  def newOclIteratorResult(v) : 
    res = OclIteratorResult()
    res.value = v
    if v == None : 
      res.done = True
    else : 
      res.done = False
    return res



class OclIterator :
  ocliterator_instances = []
  ocliterator_index = dict({})

  def __init__(self):
    self.position = 0
    self.markedPosition = 0
    self.elements = []
    self.columnNames = []
    self.generatorFunction = None
    OclIterator.ocliterator_instances.append(self)


  def getPosition(self) : 
    return self.position

  def hasNext(self) :
    result = False
    if self.position >= 0 and self.position < len(self.elements) :
      result = True
    else :
      result = False
    return result

  def isAfterLast(self) :
    result = False
    if self.position > len(self.elements) : 
      return True
    return result

  def isBeforeFirst(self) :
    result = False
    if self.position <= 0 : 
      return True
    return result

  def hasPrevious(self) :
    result = False
    if self.position > 1 and self.position <= len(self.elements) + 1 :
      result = True
    else :
      result = False
    return result

  def nextIndex(self) :
    result = 0
    result = self.position + 1
    return result

  def previousIndex(self) :
    result = 0
    result = self.position - 1
    return result

  def moveForward(self) :
    self.position = self.position + 1

  def moveBackward(self) :
    self.position = self.position - 1

  def moveTo(self, i) :
    self.position = i

  def setPosition(self, i) :
    self.position = i

  def markPosition(self) :
    self.markedPosition = self.position

  def movePosition(self, i) :
    self.position = i + self.position

  def moveToFirst(self) :
    self.position = 1

  def moveToLast(self) :
    self.position = len(self.elements)

  def moveToStart(self) :
    self.position = 0

  def moveToEnd(self) :
    self.position = len(self.elements) + 1

  def moveToMarkedPosition(self) :
    self.position = self.markedPosition

  def close(self) : 
    self.position = 0
    self.markedPosition = 0
    self.columnNames = []
    self.elements = []

  def newOclIterator_Sequence(sq) :
    result = None
    ot = createOclIterator()
    ot.elements = sq
    ot.position = 0
    result = ot
    return result

  def newOclIterator_Set(st) :
    result = None
    ot = createOclIterator()
    ot.elements = ocl.unionSequence([], ocl.sortSet(st))
    ot.position = 0
    result = ot
    return result

  def newOclIterator_String(ss) :
    result = None
    ot = createOclIterator()
    ot.elements = ocl.split(ss, "[ \n\t\r]+")
    ot.position = 0
    result = ot
    return result

  def newOclIterator_String_String(ss,seps) :
    result = None
    ot = createOclIterator()
    ot.elements = ocl.split(ss, "[" + seps + "]+")
    ot.position = 0
    result = ot
    return result

  def newOclIterator_Function(f) :
    result = None
    ot = createOclIterator()
    ot.elements = []
    ot.position = 0
    ot.generatorFunction = f
    result = ot
    return result

  def trySplit(self) : 
    firstpart = self.elements[0:self.position]
    self.elements = self.elements[self.position:]
    self.position = 0
    return OclIterator.newOclIterator_Sequence(firstpart)

  def getCurrent(self) :
    result = None
    if self.position <= len(self.elements) and self.position >= 1 : 
      result = (self.elements)[self.position - 1]
    return result

  def set(self, x) :
    self.elements[self.position -1] = x

  def insert(self, x) :
    self.elements.insert(self.position-1,x)

  def remove(self) :
    self.elements[self.position -1] = None
    del self.elements[self.position -1]

  def tryAdvance(self, f) : 
    if self.position + 1 <= len(self.elements) : 
      x = self.next()  
      f(x) 
      return True
    return False 

  def next(self) :
    result = None
    self.moveForward()
    return self.getCurrent()

  def nextResult(self) :
    result = None
    if self.generatorFunction == None : 
      self.moveForward()
      v = self.getCurrent()
      return OclIteratorResult.newOclIteratorResult(v)
    else : 
      r = self.generatorFunction(self.position)
      self.position = self.position + 1
      if self.position <= len(self.elements) : 
        self.set(r)
      else : 
        self.elements.append(r)
      return OclIteratorResult.newOclIteratorResult(r)

  def forEachRemaining(self, f) : 
    for x in self.elements[self.position:] :
      f(x) 

  def previous(self) :
    result = None
    self.moveBackward()
    return self.getCurrent()

  def length(self) : 
    return len(self.elements)

  def at(self,i) : 
    return self.elements[i-1]

  def getColumnCount(self) : 
    return len(self.columnNames)

  def getColumnName(self,i) : 
    return self.columnNames[i-1]

  def getCurrentFieldByIndex(self,i) : 
    fld = self.columnNames[i-1]
    curr = self.getCurrent()
    if curr != None : 
      return curr[fld]
    return None

  def setCurrentFieldByIndex(self,i,v) : 
    fld = self.columnNames[i-1]
    curr = self.getCurrent()
    if curr != None : 
      curr[fld] = v


  def killOclIterator(ocliterator_x) :
    ocliterator_instances = ocl.excludingSet(ocliterator_instances, ocliterator_x)
    free(ocliterator_x)


def createOclIterator():
  ocliterator = OclIterator()
  return ocliterator

def allInstances_OclIterator():
  return OclIterator.ocliterator_instances




