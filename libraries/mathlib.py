
import ocl
import math
import struct

# import ocldate


def free(x):
  del x



class MathLib:
  mathlib_instances = []
  mathlib_index = dict({})
  ix = 0
  iy = 0
  iz = 0
  defaultTolerance = 0.001
  hexdigit = []

  def __init__(self):
    MathLib.mathlib_instances.append(self)


  def initialiseMathLib() :
    MathLib.hexdigit = ["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"]
    MathLib.setSeeds(1001, 781, 913)

  def pi() :
    result = 3.14159265
    return result

  def piValue() :
    result = 3.14159265
    return result

  def e() :
    result = math.exp(1)
    return result

  def eValue() :
    result = math.exp(1)
    return result

  def setSeeds(x,y,z) :
    MathLib.ix = x
    MathLib.iy = y
    MathLib.iz = z

  def setSeed(r) : 
    MathLib.setSeeds((r % 30269), (r % 30307), (r % 30323))

  # normally-distributed with mean 1.5

  def nrandom() :
    result = 0.0
    MathLib.ix = (MathLib.ix * 171) % 30269
    MathLib.iy = (MathLib.iy * 172) % 30307
    MathLib.iz = (MathLib.iz * 170) % 30323
    return (MathLib.ix/30269.0 + MathLib.iy/30307.0 + MathLib.iz/30323.0)

  def random() :
    result = 0.0
    r = 0.0
    r = MathLib.nrandom()
    result = (r - int(math.floor(r)))
    return result

  def combinatorial(n,m) :
    result = 0
    if n - m < m :
      result = ocl.prd([(i) for i in range(m + 1, n + 1)])//ocl.prd([(j) for j in range(1, n - m + 1)])
    else :
      if n - m >= m :
        result = ocl.prd([(i) for i in range(n - m + 1, n + 1)])//ocl.prd([(j) for j in range(1, m + 1)])
    return result

  def factorial(x) :
    result = 0
    if x < 2 :
      result = 1
    else :
      if x >= 2 :
        result = ocl.prd([(i) for i in range(2, x + 1)])
    return result

  def asinh(x) :
    result = math.log((x + math.sqrt((x * x + 1))))
    return result

  def acosh(x) :
    result = math.log((x + math.sqrt((x * x - 1))))
    return result

  def atanh(x) :
    result = 0.5 * math.log((1 + x)/(1 - x))
    return result

  def decimal2bits(x) :
    result = ""
    if x == 0 :
      result = ""
    else :
      result = MathLib.decimal2bits(x//2) + "" + str(x % 2)
    return result

  def decimal2binary(x) :
    result = ""
    if x < 0 :
      result = "-" + MathLib.decimal2bits(-x)
    else :
      if x == 0 :
        result = "0"
      else :
        result = MathLib.decimal2bits(x)
    return result

  def decimal2oct(x) :
    result = ""
    if x == 0 :
      result = ""
    else :
      result = MathLib.decimal2oct(x//8) + "" + str(x % 8)
    return result

  def decimal2octal(x) :
    result = ""
    if x < 0 :
      result = "-" + MathLib.decimal2oct(-x)
    else :
      if x == 0 :
        result = "0"
      else :
        result = MathLib.decimal2oct(x)
    return result

  def decimal2hx(x) :
    result = ""
    if x == 0 :
      result = ""
    else :
      result = MathLib.decimal2hx(x//16) + "" + (MathLib.hexdigit)[int(x % 16)]
    return result

  def decimal2hex(x) :
    result = ""
    if x < 0 :
      result = "-" + MathLib.decimal2hx(-x)
    else :
      if x == 0 :
        result = "0"
      else :
        result = MathLib.decimal2hx(x)
    return result

  def bytes2integer(bs) :  
    res = 0; 
    if len(bs) == 0 :  
      return 0
    if len(bs) == 1 :  
      return bs[0] 
    if len(bs) == 2 :  
      return 256*bs[0] + bs[1]
    
    lowdigit = ocl.last(bs) 
    highdigits = ocl.front(bs) 
    return 256*MathLib.bytes2integer(highdigits) + lowdigit  

  def integer2bytes(x) :
    result = []
 
    y = x//256
    z = x % 256 
    if y == 0 : 
      return [z]
    
    highbytes = MathLib.integer2bytes(y)
    highbytes = ocl.append(highbytes,z)
    return highbytes

  def integer2Nbytes(x,n) : 
    res = MathLib.integer2bytes(x) 
    while len(res) < n :  
      res = ocl.prepend(res,0)   
    return res


  def bitwiseAnd(x,y) :
    return (x & y)

  def bitwiseOr(x,y) :
    return (x | y)

  def bitwiseXor(x,y) :
    return (x ^ y)

  def bitwiseNot(x) : 
    return ~x


  def toBitSequence(x) : 
    x1 = x  
    res = [] 
    while x1 > 0 :
      if x1 % 2 == 0 : 
        res = ocl.prepend(res,False)
      else :
        res = ocl.prepend(res,True)  
      x1 = x1//2
    return res   

  def modInverse(n,p) : 
    x = (n % p)
    for i in range(1,p) : 
      if ((i*x) % p) == 1 : 
        return i
    return 0

  def modPow(n,m,p) : 
    res = 1
    x = (n % p) 
    for i in range(1,m+1) : 
      res = (res*x) % p
    return res

  def doubleToLongBits(d) : 
    bts = struct.pack('d',d)
    return MathLib.bytes2integer(bts)

  def longBitsToDouble(x) : 
    bts = MathLib.integer2bytes(x)
    d, = struct.unpack('d',bytes(bts))
    return d

  def discountDiscrete(amount, rate, time) :
    result = 0.0
    if rate <= -1 or time < 0 :
      return result
  
    result = amount / math.pow((1 + rate), time)
    return result
  
  def netPresentValueDiscrete(rate, values) :
    result = 0.0;
    if rate <= -1 :
      return result
  
    upper = len(values)
    
    for i in range(0,upper) :
      result = result + MathLib.discountDiscrete(values[i], rate, i)
    return result

  def bisectionAsc(r, rl, ru, f, tol) :
    # find a zero of f(x) in range [rl,ru]
    # f non-descending. Start with r
    result = r

    if ru <= rl : 
      return r

    if r < rl :
      r = rl

    if r > ru : 
      r = ru
  
    v = f(r)
    # print("value at " + str(r) + " is " + str(v))

    if v < tol and v > -tol:
      return r

    if v > 0 :
      return MathLib.bisectionAsc((rl + r) / 2, rl, r, f, tol)
    else :
      if v < 0 :
        return MathLib.bisectionAsc((r + ru) / 2, r, ru, f, tol)
    return r; 


  def bisectionDiscrete(r, rl, ru, values) :
    result = 0.0;
    if r <= -1 or rl <= -1 or ru <= -1 :
      return result
  
    v = MathLib.netPresentValueDiscrete(r,values)

    if ru - rl < 0.001 :
      return r

    if v > 0 :
      return MathLib.bisectionDiscrete((ru + r) / 2, r, ru, values)
    else :
      if v < 0 :
        return MathLib.bisectionDiscrete((r + rl) / 2, rl, r, values)
    return r; 
  

  def irrDiscrete(values) :
    res = MathLib.bisectionDiscrete(0.1,-0.5,1.0,values) 
    return res

  def roundN(x,n) :
    if n == 0 : 
      return round(x)
    y = x*math.pow(10,n) 
    return round(y)/math.pow(10,n) 

  def truncateN(x,n) :
    if n <= 0 : 
      return int(x)
    y = x*math.pow(10,n) 
    return int(y)/math.pow(10,n) 
  
  def toFixedPoint(x,m,n) :  
    y = int(x*math.pow(10,n)) 
    z = y % math.pow(10,m+n) 
    return z/math.pow(10,n) 

  def toFixedPointRound(x,m,n) :  
    y = int(round(x*math.pow(10,n))) 
    z = y % math.pow(10,m+n) 
    return z/math.pow(10,n) 

  def isIntegerOverflow(x, m) : 
    y = int(x)
    if y == 0 : 
      return (m < 1)
    if y > 0 : 
      return int(math.log10(y)) + 1 > m
    if y < 0 :  
      return int(math.log10(-y)) + 1 > m
    return False 


  def mean(sq) : 
    n = len(sq)
    if n == 0 : 
      return 0
    return ocl.sumdouble(sq)/n 

  def median(sq) : 
    n = len(sq)
    if n == 0 : 
      return 0
    s1 = ocl.sortSequence(sq)
    if n % 2 == 1 :
      return s1[(1 + n)//2 - 1]
    else :
      return (s1[n//2 - 1] + s1[n//2])/2.0 

  def variance(sq) :  
    n = len(sq)
    if n == 0 : 
      return 0
    m = MathLib.mean(sq)
    return ocl.sumdouble([(x - m)*(x - m) for x in sq]) / n 

  def standardDeviation(sq) :  
    n = len(sq)
    if n == 0 : 
      return 0
    m = MathLib.variance(sq)
    return math.sqrt(m)  

  def lcm(x,y) :
    g = ocl.gcd(x,y)
    return (x*y)//g

  def rowMult(s, m) :   
    result = []
    for i in range(1, len(s)+1) : 
      rowsum = 0 
      for k in range(1, len(m)+1) : 
        rowsum = rowsum + s[k-1]*(m[k-1][i-1])
      result.append(rowsum)
    return result  

  def matrixMultiplication(m1, m2) : 
    result = []
    for row in m1 : 
      result.append( MathLib.rowMult(row, m2) )
    return result

  def differential(f) :
    result = lambda x : ((1.0/(2.0*MathLib.defaultTolerance)) * ((f)(x + MathLib.defaultTolerance) - (f)(x - MathLib.defaultTolerance)))
    return result

  def definiteIntegral(st,en,f) :
    area = 0.0
    d = MathLib.defaultTolerance * (en - st)
    cum = st
    while cum < en :
      next = cum + d
      area = area + d * ((f)(cum) + (f)(next))/2.0
      cum = next
    return area

  def indefiniteIntegral(f) :
    result = lambda x : MathLib.definiteIntegral(0, x, f)
    return result


  def killMathLib(mathlib_x) :
    mathlib_instances = ocl.excludingSet(mathlib_instances, mathlib_x)
    free(mathlib_x)


def createMathLib():
  mathlib = MathLib()
  return mathlib

def allInstances_MathLib():
  return MathLib.mathlib_instances

MathLib.initialiseMathLib()


# Examples: 
# print(MathLib.bytes2integer([1,1,10]))
# print(MathLib.integer2bytes(2147483647))
# print(MathLib.integer2Nbytes(65802,4))
# print(MathLib.doubleToLongBits(5.6))
# print(MathLib.longBitsToDouble(7378697629483800128))

# print(MathLib.discountDiscrete(100,0.1,5))
# print(MathLib.netPresentValueDiscrete(0.01, [-100,2,102]))
# print(MathLib.irrDiscrete([-100,2,102]))

# print(MathLib.roundN(22.553,2))
# print(MathLib.roundN(33.5,0))

# print(MathLib.toFixedPoint(1033.55,3,1))
# print(MathLib.toFixedPointRound(33.55,1,2))

# ss = [1,3,4,6]
# print(MathLib.mean(ss))
# print(MathLib.median(ss))
# print(MathLib.standardDeviation(ss))
# print(MathLib.variance(ss))

# print(MathLib.lcm(15,10))

# x = MathLib.bisectionAsc(0.5,-1,1, lambda x : x*x - 0.5, 0.00001)
# print(x)

# print(MathLib.isIntegerOverflow(0, 1))

# print(MathLib.truncateN(-2.126, 2))
# print(MathLib.roundN(2126.5, 0))

# tt = ocldate.OclDate.getSystemTime()
# print(tt)
# MathLib.setSeeds(tt % 30269, tt % 30307, tt % 30323)
# print(MathLib.random())
# print(MathLib.random())
# print(MathLib.random())

# m1 = [[1,3], [7,5]]
# m2 = [[6,8], [4,2]]

# print(MathLib.matrixMultiplication(m1,m2))

# lin = lambda x : x
# sq = lambda x : x*x

# df = MathLib.differential(sq)

# print(df(0.1))

# print(MathLib.definiteIntegral(1,2,lin))

