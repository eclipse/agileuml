import ocl
import math
import re
import copy

from mathlib import *
from oclfile import *
from ocltype import *
from ocldate import *
from oclprocess import *
from ocliterator import *
from oclrandom import *
from ocldatasource import *
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


class FromVB : 
  fromvb_instances = []
  fromvb_index = dict({})

  def __init__(self):
    FromVB.fromvb_instances.append(self)



  def FindArray(self, Arr, Elem, Lower, Upper) :
    result = 0
    while True :
      FindArray = 0
      I = 0
      if Lower == Upper :
        if Arr[Lower -1] == Elem :
          FindArray = Lower
          return FindArray
          pass
          break
        else :
          FindArray = -1
          return FindArray
          pass
          break
        pass
        pass
        break
      else :
        pass
      I = (Lower + ((Upper - Lower) // 2))
      if Elem <= I :
        Upper = I
      else :
        Lower = I+1
    return FindArray

  def FindMin(self) :
    Eps = [0]
    N = 0
    N = 10000
    Eps = [ocl.any(Eps) for self in range(1, N +1)]
    I = 0
    I = 1
    while I <= N :
      Eps[I -1] = I
      I = I + 1
    T1 = time.time()
    print(self.FindArray(Eps, 30000, 1, N))
    print(1000*(time.time() - T1))

  def initialise(self) :
    pass
    pass
    pass

  def killFromVB(fromvb_x) :
    fromvb_instances = ocl.excludingSet(fromvb_instances, fromvb_x)
    free(fromvb_x)

def createFromVB():
  fromvb = FromVB()
  return fromvb

def allInstances_FromVB():
  return FromVB.fromvb_instances


fromvb_OclType = createByPKOclType("FromVB")
fromvb_OclType.instance = createFromVB()
fromvb_OclType.actualMetatype = type(fromvb_OclType.instance)


vb = FromVB()
vb.FindMin()
