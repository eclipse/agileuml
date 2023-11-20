
import ocl
import math
import struct

from ocldate import *


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

    if v < tol and v > -tol:
      return r

    if v > 0 :
      return MathLib.bisectionAsc((rl + r) / 2, rl, r, f, tol)
    else :
      if v < 0 :
        return MathLib.bisectionAsc((r + ru) / 2, r, ru, f, tol)
    return r; 



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


class FinanceLib : 

  def discountDiscrete(amount, rate, time) :
    result = 0.0
    if rate <= -1 or time < 0 :
      return result
  
    result = amount / math.pow((1 + rate), time)
    return result

  def presentValueDiscrete(rate, values) :
    result = 0.0;
    if rate <= -1 :
      return result
  
    upper = len(values)
    
    for i in range(1,upper+1) :
      result = result + FinanceLib.discountDiscrete(values[i-1], rate, i-1)
    return result
  
  def netPresentValueDiscrete(rate, values) :
    result = 0.0;
    if rate <= -1 :
      return result
  
    upper = len(values)
    
    for i in range(0,upper) :
      result = result + FinanceLib.discountDiscrete(values[i], rate, i)
    return result

  def bisectionDiscrete(r, rl, ru, values) :
    result = 0.0;
    if r <= -1 or rl <= -1 or ru <= -1 :
      return result
  
    v = FinanceLib.netPresentValueDiscrete(r,values)

    if ru - rl < 0.001 :
      return r

    if v > 0 :
      return FinanceLib.bisectionDiscrete((ru + r) / 2, r, ru, values)
    else :
      if v < 0 :
        return FinanceLib.bisectionDiscrete((r + rl) / 2, rl, r, values)
    return r; 
  

  def irrDiscrete(values) :
    res = FinanceLib.bisectionDiscrete(0.1,-0.5,1.0,values) 
    return res

  def straddleDates(d1, d2, period) : 
    cd = d1 
    while cd.compareToYMD(d2) <= 0 : 
      cd = cd.addMonthYMD(period) 
    return [cd.subtractMonthYMD(period),cd] 

  def numberOfPeriods(settle, matur, period) : 
    monthsToMaturity = OclDate.differenceMonths(matur,settle)
    return math.ceil(monthsToMaturity/period)

  def sequenceOfPeriods(sett, mat, period) : 
    numPeriods = FinanceLib.numberOfPeriods(sett, mat, period)
    return list(range(1, numPeriods+1))

  def couponDates(matur, period, numPeriods) : 
    cpdates = [matur]
    cpdate = matur

    for i in range(numPeriods - 1) :  
      mo = cpdate.month - period    
      prevMonth = mo
      prevYear = cpdate.year
      prevDay = cpdate.day
      
      if mo <= 0 :
        prevMonth = 12 + mo  
        prevYear = cpdate.year - 1
        
      cpdate = OclDate.newOclDate_YMD(prevYear,prevMonth,prevDay)
      cpdates.append(cpdate)

    cpdates.reverse()  
    return cpdates



  def days360(d1,d2,num,mat) :
    if (d1.year, d1.month, d1.day) > (d2.year, d2.month, d2.day) :
      d1, d2 = d2, d1
    if (num == "30/360") :
      return 360*(d2.year - d1.year) + 30*(d2.month - d1.month) + (d2.day - d1.day)
    elif (num == "30/360B") :
      dd1 = d1.day
      dd2 = d2.day
      dd1 = min(dd1,30)
      if dd1 > 29 :
        dd2 = min(dd2,30)
      return 360*(d2.year - d1.year) + 30*(d2.month -d1.month) + (dd2 - dd1)
    elif (num == "30/360US") :
      mm1 = d1.month
      mm2 = d2.month
      dd1 = d1.day
      dd2 = d2.day
      if (mm1 == 2 and (dd1 == 28 or dd1 == 29) and mm2 == 2 and (dd2 == 28 or dd2 == 29)) :
        dd2 = 30
      if (mm1 == 2 and (dd1 == 28 or dd1 == 29)) :
        dd1 = 30
      if (dd2 == 31 and (dd1 == 30 or dd1 == 31)):
        dd2 = 30
      if dd1 == 31 :
         dd1 = 30
      return 360*(d2.year - d1.year) + 30*(d2.month - d1.month) + (dd2-dd1)
    elif (num == "30E/360") :
      dd1 = d1.day
      dd2 = d2.day
      if dd1 == 31 :
        dd1 = 30
      if dd2 == 31 :
        dd2 = 30
      return 360*(d2.year - d1.year) + 30*(d2.month - d1.month) + (dd2 - dd1)
    elif (num == "30E/360ISDA") :
      dd1 = d1.day
      dd2 = d2.day
      mm1 = d1.month
      mm2 = d2.month
      if (d1.isEndOfMonth()) :
        dd1 = 30
      if not(d2 == mat and mm2 == 2) and d2.isEndOfMonth() :
        dd2 = 30
      return 360*(d2.year - d1.year) + 30*(d2.month - d1.month) + (dd2 - dd1)
    else:
      return 360*(d2.year - d1.year) + 30*(d2.month - d1.month) + (d2.day - d1.day)
        
        
  def numberOfMonths(PD, settle, coupDate1, dayCount, matur) : 
    if dayCount == "Actual/360" or dayCount == "Actual/365F" or dayCount == "Actual/ActualICMA"\
       or dayCount == "Actual/364" or dayCount == "Actual/ActualISDA" : 
      daysBetween = OclDate.daysBetweenDates(PD, settle)
      sv = (coupDate1 - daysBetween)/coupDate1
      return [sv, (coupDate1 - daysBetween)]
    else:
      daysBetween360 = days360(PD, settle, dayCount, matur)
      sv = (coupDate1 - daysBetween360)/coupDate1
      return [sv, (coupDate1 - daysBetween360)]

  def calculateCouponPayments(paymentDates, annualCouponRate, dayCountC, freq) :
    coupon_payments = []
    dates_payments = []
    cum_days = 0

    for i in range(1, len(paymentDates)):
      start_date_str = paymentDates[i - 1]
      end_date_str = paymentDates[i]   
     
      if (dayCountC == "30/360" or dayCountC == "30/360B" or dayCountC == "30/360US" \
          or dayCountC == "30E/360" or dayCountC == "30E/360ISDA" or dayCountC == "Actual/360") :
        days = FinanceLib.days360(start_date_str, end_date_str,
                                  dayCountC, paymentDates[-1])
      elif (dayCountC == "Actual/365F") :
        days = 365/freq
      elif (dayCountC == "Actual/364") :
        days = 364/freq            
      else: #actual/actual calculations
        days = OclDate.daysBetweenDates(start_date_str, end_date_str)      
      coupon_payment = annualCouponRate/freq
    
      coupon_payments.append(coupon_payment)
      cum_days += days
      dates_payments.append(cum_days)

    return [coupon_payments, dates_payments]

  def bondCashFlows(settle,matur,coupon,dayCount,freq) :
    period = int(12/freq)
    np = FinanceLib.numberOfPeriods(settle, matur, period)
    snp = FinanceLib.sequenceOfPeriods(settle, matur, period)
    cd = FinanceLib.couponDates(matur, period, np)
      # could filter based on buisiness days

    pm = cd[0].subtractMonthYMD(period)
    cdn = [pm] + cd
    coupPayments = FinanceLib.calculateCouponPayments(
                                     cdn,coupon,dayCount,freq)
    cumd = coupPayments[1]
    cp = coupPayments[0]
    nm = FinanceLib.numberOfMonths(pm,settle,
                                   cumd[0],dayCount,matur)
   
    if settle.compareToYMD(pm) == 0 :
      results = [cp]+[cd]+[snp]+[cumd]
    else :
      snp = [x-(snp[0]-nm[0]) for x in snp]
      cumd = [x-(cumd[0]-nm[1]) for x in cumd]
      results = [cp]+[cd]+[snp]+[cumd]
    return results
    
  def bondPrice(yld,settle,matur,coup,dayCount,freq) :
    res = FinanceLib.bondCashFlows(settle,matur,
                                   coup,dayCount,freq)
    coupRates = res[0]
    timePoints = res[2]
    discountFactors = [math.pow(1/(1 + yld/freq),x) for x in timePoints]
    coupRates[-1] += 1
    sp = sum([x*y for x,y in zip(discountFactors,coupRates)])
    return sp


  def accInterest(issue,settle,freq,coup) :
    period = int(12/freq)
    st = FinanceLib.straddleDates(issue,settle,period)
    aif = OclDate.daysBetweenDates(st[0],settle)/OclDate.daysBetweenDates(st[0],st[1])
    return aif*(coup/freq)

  def accumulatedInterest(issue,settle,freq,coup,dayCount,matur) :
    period = int(12/freq)
    st = FinanceLib.straddleDates(issue,settle,period)
    aif = 0.0
    d1 = st[0]
    d2 = st[1]
    # print(issue)
    # print(settle)
    # print(d1)
    # print(d2)
    ys = d1.year
    ye = settle.year
    ysEnd = OclDate.newOclDate_String(str(ys) + "/12/31")
    yeStart = OclDate.newOclDate_String(str(ye) + "/01/01")
        
    if (dayCount == "Actual/365F") :
      aif = (OclDate.daysBetweenDates(d1,settle)/365)*coup
    elif (dayCount == "Actual/ActualISDA") :
      if (d1.isLeapYear() and settle.isLeapYear()):
        aif = (OclDate.daysBetweenDates(d1,settle)/366)*coup
      elif (not(d1.isLeapYear()) and not(settle.isLeapYear())) :
        aif = (OclDate.daysBetweenDates(d1,settle)/365)*coup
      elif (d1.isLeapYear() and not(settle.isLeapYear())) :
        aif = (OclDate.daysBetweenDates(d1,ysEnd)/366) * coup +\
           (OclDate.daysBetweenDates(yeStart,settle)/365)*coup
      else:
        aif = (OclDate.daysBetweenDates(d1,ysEnd)/365)*coup +\
           (OclDate.daysBetweenDates(yeStart,settle)/366)*coup
    
    elif (dayCount == "Actual/364") :
      aif = (OclDate.daysBetweenDates(d1,settle)/364)*coup
    elif (dayCount == "Actual/360") :
      aif = (OclDate.daysBetweenDates(d1,settle)/360)*coup
    elif (dayCount == "Actual/ActualICMA") :
      aif = (OclDate.daysBetweenDates(d1,settle)/(freq*OclDate.daysBetweenDates(d1,d2)))*coup
    else :
      aif = (FinanceLib.days360(d1,settle,dayCount,matur)/360)*coup
    return aif
    
  def bondPriceClean(Y,I,S,M,c,dcf,f) :
    return FinanceLib.bondPrice(Y,S,M,c,dcf,f) -FinanceLib.accumulatedInterest(I,S,f,c,dcf,M)


# Examples: 
# print(MathLib.bytes2integer([1,1,10]))
# print(MathLib.integer2bytes(2147483647))
# print(MathLib.integer2Nbytes(65802,4))
# print(MathLib.doubleToLongBits(5.6))
# print(MathLib.longBitsToDouble(7378697629483800128))

# print(FinanceLib.discountDiscrete(100,0.1,5))
# print(FinanceLib.presentValueDiscrete(0.01, [-100,2,102]))
# print(FinanceLib.netPresentValueDiscrete(0.01, [-100,2,102]))
# print(FinanceLib.irrDiscrete([-100,2,102]))

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


mat = OclDate.newOclDate_String("2022/01/01")  #change this
sett = OclDate.newOclDate_String("2019/05/02")  #change this
issu = OclDate.newOclDate_String("2019/01/01")  #change this
coup=0.08  #change this
dc="Actual/ActualICMA"  #change this
f=2  #change this
Y=0.06  #change this

bcfs = FinanceLib.bondCashFlows(sett,mat,coup,dc,f)
print(bcfs)
dds = bcfs[1]
for dx in dds : 
  print(str(dx))

print(FinanceLib.bondPrice(Y,sett,mat,coup,dc,f))
print('%.6f'% FinanceLib.accumulatedInterest(issu,sett,f,coup,dc,mat))

print(FinanceLib.bondPriceClean(Y,issu,sett,mat,coup,dc,f))

f = lambda x : (x*x)
g = MathLib.differential(f)
print(g(0))
print(g(1))
print(g(2))
k = lambda x : x
print(MathLib.definiteIntegral(0,1,k))
print(MathLib.definiteIntegral(0,2,k))
print(MathLib.definiteIntegral(0,3,k))
p = MathLib.indefiniteIntegral(k)
print(p(1))
print(p(2))
print(p(3))
