import ocl
import math
import re
import copy

from oclfile import *
from ocltype import *
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


class OclRandom : 
  oclrandom_instances = []
  oclrandom_index = dict({})

  def __init__(self):
    self.ix = 0
    self.iy = 0
    self.iz = 0
    OclRandom.oclrandom_instances.append(self)




  def newOclRandom(n=1111111) :
    result = None
    rd = createOclRandom()
    rd.ix = n % 30269
    rd.iy = n % 30307
    rd.iz = n % 30323
    result = rd
    return result

  def setSeeds(self, x, y, z) :
    self.ix = x
    self.iy = y
    self.iz = z

  def setSeed(self, n) :
    self.ix = n % 30269
    self.iy = n % 30307
    self.iz = n % 30323

  def nrandom(self) :
    result = 0.0
    self.ix = (self.ix * 171) % 30269
    self.iy = (self.iy * 172) % 30307
    self.iz = (self.iz * 170) % 30323
    return (self.ix/30269.0 + self.iy/30307.0 + self.iz/30323.0)

  def nextDouble(self) :
    result = 0.0
    r = 0.0
    r = self.nrandom()
    result = (r - int(math.floor(r)))
    return result

  def nextFloat(self) :
    result = 0.0
    r = 0.0
    r = self.nrandom()
    result = (r - int(math.floor(r)))
    return result

  def nextGaussian(self) :
    result = 0.0
    d = 0.0
    d = self.nrandom()
    result = (d/3.0 - 0.5)
    return result

  def nextInt(self, n=2147483647) :
    result = 0
    d = 0.0
    d = self.nextDouble()
    result = int(math.floor((d * n)))
    return result

  def nextLong(self) :
    result = 0
    result = self.nextInt(9223372036854775807)
    return result

  def nextBoolean(self) :
    result = False
    d = 0.0
    d = self.nextDouble()
    if d > 0.5 :
      result = True
    return result

  def randomiseSequence(sq) :
    result = []
    r = OclRandom.newOclRandom()
    res = []
    old = ocl.unionSequence([], sq)
    while len(old) > 0 :
      x = len(old)
      if x == 1 :
        res = ocl.includingSequence(res, (old)[1 - 1])
        return res
      else :
        n = r.nextInt(x) + 1
        obj = (old)[n - 1]
        res = ocl.includingSequence(res, obj)
        old = ocl.removeAt(old, n)
    return res

  def killOclRandom(oclrandom_x) :
    oclrandom_instances = ocl.excludingSet(oclrandom_instances, oclrandom_x)
    free(oclrandom_x)

def createOclRandom():
  oclrandom = OclRandom()
  return oclrandom

def allInstances_OclRandom():
  return OclRandom.oclrandom_instances


oclrandom_OclType = createByPKOclType("OclRandom")
oclrandom_OclType.instance = createOclRandom()
oclrandom_OclType.actualMetatype = type(oclrandom_OclType.instance)


r = OclRandom.newOclRandom(11111111111)
print(r.nextGaussian())
print(r.nextGaussian())
print(r.nextGaussian())

print(OclRandom.randomiseSequence([1,2,3,4,4,5]))

