import ocl
import math
import time
import random
import numpy as np

from enum import Enum

def free(x):
  del x



class OclRandom : 
  oclrandom_instances = []
  oclrandom_index = dict({})
  _defaultInstanceOclRandom = None

  def __init__(self):
    self.ix = 0
    self.iy = 0
    self.iz = 0
    self.distribution = "uniform"
    self.bernoulliP = 0.0
    self.binomialN = 1
    self.binomialP = 0.0
    self.normalMean = 0.0
    self.normalVariance = 1.0
    self.uniformLower = 0.0
    self.uniformUpper = 1.0
    self.poissonLambda = 1.0
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

  def newOclRandom_Seed(n = int(time.time())) : 
    rd = createOclRandom()
    rd.ix = (n % 30269)
    rd.iy = (n % 30307)
    rd.iz = (n % 30323) 
    return rd

  def newOclRandomBernoulli(p) : 
    rd = createOclRandom()
    rd.ix = 1001
    rd.iy = 781
    rd.iz = 913 
    rd.distribution = "bernoulli"
    rd.bernoulliP = p
    return rd

  def newOclRandomBinomial(n,p) : 
    rd = createOclRandom()
    rd.ix = 1001
    rd.iy = 781
    rd.iz = 913 
    rd.distribution = "binomial"
    rd.binomialN = n
    rd.binomialP = p
    return rd

  def newOclRandomNormal(mu,vari) : 
    rd = createOclRandom()
    rd.ix = 1001
    rd.iy = 781
    rd.iz = 913 
    rd.distribution = "normal"
    rd.bernoulliP = 0.0
    rd.normalMean = mu
    rd.normalVariance = vari
    return rd

  def newOclRandomLogNormal(mu,vari) : 
    rd = createOclRandom()
    rd.ix = 1001
    rd.iy = 781
    rd.iz = 913 
    rd.distribution = "lognormal"
    rd.bernoulliP = 0.0
    rd.normalMean = mu
    rd.normalVariance = vari
    return rd

  def newOclRandomUniform(lwr,upr) :
    result = None
    rd = createOclRandom()
    rd.ix = 1001
    rd.iy = 781
    rd.iz = 913
    rd.distribution = "uniform"
    rd.uniformLower = lwr
    rd.uniformUpper = upr
    result = rd
    return result

  def newOclRandomPoisson(lm) :
    result = None
    rd = createOclRandom()
    rd.ix = 1001
    rd.iy = 781
    rd.iz = 913
    rd.distribution = "poisson"
    rd.poissonLambda = lm
    result = rd
    return result


  def setSeed(self, n = int(time.time())) :
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
    result = d*2.0 - 3.0
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

  def nextBernoulli(self,p) :
    result = 1
    d = self.nextDouble()
    if d > p :
      result = 0
    return result

  def nextBinomial(self,n,p) :
    result = 0
    for ind in range(0,n) : 
      d = self.nextDouble()
      if d <= p :
        result = result + 1
    return result

  def nextNormal(self, mu, vari) :
    d = 0.0 
    i = 0 
    while i < 12 : 
      d = d + self.nextDouble()
      i = i + 1
    d = d - 6 
    return mu + d*math.sqrt(vari)


  def nextLogNormal(self, mu, vari) :
    d = 0.0
    d = self.nextNormal(mu,vari) 
    return math.exp(d) 


  def nextUniform(self, lwr, upr) :
    result = 0.0
    d = 0.0
    d = self.nextDouble()
    result = lwr + (upr - lwr) * d
    return result

  def nextPoisson(self, lam) :
    result = 0.0
    x = 0.0
    p = math.exp((-lam))
    s = p
    u = self.nextDouble()
    while u > s :
      x = x + 1
      p = p * lam/x
      s = s + p
    return x

  def next(self) : 
    if self.distribution == "normal" :
      return self.nextNormal(self.normalMean, self.normalVariance)
    if self.distribution == "lognormal" :
      return self.nextLogNormal(self.normalMean, self.normalVariance)
    if self.distribution == "bernoulli" :
      return self.nextBernoulli(self.bernoulliP)
    if self.distribution == "binomial" :
      return self.nextBinomial(self.binomialN, self.binomialP)
    if self.distribution == "uniform" :
      return self.nextUniform(self.uniformLower, self.uniformUpper)
    if self.distribution == "poisson" :
      return self.nextPoisson(self.poissonLambda)
    return self.nextDouble()

  def mean(self) : 
    if self.distribution == "normal" :
      return self.normalMean
    if self.distribution == "lognormal" :
      return math.exp(self.normalMean + self.normalVariance/2.0)
    if self.distribution == "bernoulli" :
      return self.bernoulliP
    if self.distribution == "binomial" :
      return self.binomialN*self.binomialP
    if self.distribution == "uniform" :
      return (self.uniformUpper + self.uniformLower)/2.0
    if self.distribution == "poisson" :
      return self.poissonLambda
    return 0.5

  def variance(self) : 
    if self.distribution == "normal" :
      return self.normalVariance
    if self.distribution == "lognormal" :
      return math.exp(2*self.normalMean + 2*self.normalVariance) - math.exp(2*self.normalMean + self.normalVariance)
    if self.distribution == "bernoulli" :
      return (self.bernoulliP)*(1 - self.bernoulliP)
    if self.distribution == "binomial" :
      return self.binomialN*self.binomialP*(1 - self.binomialP)
    if self.distribution == "uniform" :
      return (self.uniformUpper - self.uniformLower)/12.0
    if self.distribution == "poisson" :
      return self.poissonLambda
    return 1.0/12

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
      res = res + chs[int(np.random.default_rng().random() * 54)]
    return res

  def randomElement(col) : 
    ln = len(col)
    if ln == 0 : 
      return None
    x = int(np.random.default_rng().random() * ln)
    return col[x]

  def randomUniqueElements(col,n) : 
    return random.sample(col,n)    

  def randomElements(col,n) : 
    return random.choices(col,k=n)    

  def defaultInstanceOclRandom() : 
    if OclRandom._defaultInstanceOclRandom == None : 
      OclRandom._defaultInstanceOclRandom = OclRandom.newOclRandom_Seed()
    return OclRandom._defaultInstanceOclRandom

  def killOclRandom(oclrandom_x) :
    oclrandom_instances = ocl.excludingSet(oclrandom_instances, oclrandom_x)
    free(oclrandom_x)

def createOclRandom() :
  oclrandom = OclRandom()
  return oclrandom

def allInstances_OclRandom() :
  return OclRandom.oclrandom_instances



# rr = OclRandom.newOclRandomLogNormal(0,1)

# print(rr.next())
# print(rr.next())
# print(rr.next())
# print(rr.next())
# print(rr.next())
# print(rr.next())

# rr = OclRandom.newOclRandomBinomial(5,0.3)

# print(rr.nextBinomial(5,0.3))
# print(rr.nextBinomial(5,0.3))
# print(rr.nextBinomial(5,0.3))
# print(rr.nextBinomial(5,0.3))
# print(rr.nextBinomial(5,0.3))




