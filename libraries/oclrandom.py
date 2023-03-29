import ocl
import math
import time
import random


from enum import Enum

def free(x):
  del x



class OclRandom : 
  oclrandom_instances = []
  oclrandom_index = dict({})

  def __init__(self):
    self.ix = 0
    self.iy = 0
    self.iz = 0
    OclRandom.oclrandom_instances.append(self)

  def newOclRandom(n = 0) :
    result = None
    rd = createOclRandom()
    if n == 0 : 
      rd.ix = 1001
      rd.iy = 781
      rd.iz = 913
    else : 
      rd.ix = (n % 30269)
      rd.iy = (n % 30307)
      rd.iz = (n % 30323) 
    result = rd
    return result

  def setSeed(self, n) :
    self.ix = (n % 30269)
    self.iy = (n % 30307)
    self.iz = (n % 30323) 
 
  def setSeeds(self, x, y, z) :
    self.ix = x
    self.iy = y
    self.iz = z

  def nrandom(self) :
    result = 0.0
    self.ix = (self.ix * 171) % 30269
    self.iy = (self.iy * 172) % 30307
    self.iz = (self.iz * 170) % 30323
    return (self.ix/30269.0 + self.iy/30307.0 + self.iz/30323.0)

  def nextDouble(self) :
    result = 0.0
    r = self.nrandom()
    result = (r - int(math.floor(r)))
    return result

  def nextFloat(self) :
    result = 0.0
    r = self.nrandom()
    result = (r - int(math.floor(r)))
    return result

  def nextGaussian(self) :
    result = 0.0
    d = self.nrandom()
    result = (d/3.0 - 0.5)
    return result

  def nextInt(self, n = 2147483647) :
    result = 0
    d = self.nextDouble()
    result = int(math.floor(d * n))
    return result

  def nextLong(self) : 
    return self.nextInt(9223372036854775807)

  def nextBoolean(self) :
    result = False
    d = self.nextDouble()
    if d > 0.5 :
      result = True
    return result

  def randomiseSequence(sq) : 
    ln = len(sq)
    if ln == 0 : 
      return sq
    res = []
    inds = random.sample(range(0,ln),ln)
    for x in inds :
      res.append(sq[x])
    return res

  def randomString(n) : 
    chs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$"
    res = ""
    for x in range(0,n) : 
      res = res + chs[int(random.random()*54)]
    return res

  def randomElement(col) : 
    ln = len(col)
    if ln == 0 : 
      return None
    x = int(random.random()*ln)
    return col[x]
    

  def killOclRandom(oclrandom_x) :
    oclrandom_instances = ocl.excludingSet(oclrandom_instances, oclrandom_x)
    free(oclrandom_x)

def createOclRandom():
  oclrandom = OclRandom()
  return oclrandom

def allInstances_OclRandom():
  return OclRandom.oclrandom_instances


