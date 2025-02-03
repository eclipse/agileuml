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


class SpreadsheetFont : 
  spreadsheetfont_instances = []
  spreadsheetfont_index = dict({})

  def __init__(self):
    self.Name = ""
    self.Size = 0
    self.Color = None
    self.Bold = False
    self.Italic = False
    SpreadsheetFont.spreadsheetfont_instances.append(self)



  def killSpreadsheetFont(spreadsheetfont_x) :
    spreadsheetfont_instances = ocl.excludingSet(spreadsheetfont_instances, spreadsheetfont_x)
    free(spreadsheetfont_x)

class SpreadsheetCell : 
  spreadsheetcell_instances = []
  spreadsheetcell_index = dict({})

  def __init__(self):
    self.Name = ""
    self.RowNumber = 0
    self.ColumnName = ""
    self.Value = None
    self.Font = None
    self.Sheet = None
    SpreadsheetCell.spreadsheetcell_instances.append(self)


  def newSpreadsheetCell(nme,val) :
    result = None
    c = createSpreadsheetCell()
    c.Name = nme
    c.RowNumber = ocl.toInteger(ocl.firstMatch(nme, "[0-9]+"))
    c.ColumnName = ocl.firstMatch(nme, "[A-Z]+")
    c.Value = val
    result = c
    return result

  def Offset(self,x,y) : 
    col = ocl.byte2char(ocl.char2byte(self.ColumnName) + x)
    return self.Sheet.getACell(col, self.RowNumber + y)

  def precedes(self, c) :
    result = False
    if self.ColumnName <= c.ColumnName and ((self.RowNumber < c.RowNumber) if (self.ColumnName == c.ColumnName) else (True)) :
      result = True
    return result

  def startColumn(s) :
    result = ""
    result = ocl.firstMatch(s, "[A-Z]+")
    return result

  def endColumn(s) :
    result = ""
    if (s.find(":") + 1) > 0 :
      result = ocl.firstMatch(ocl.after(s, ":"), "[A-Z]+")
    else :
      if (s.find(":") + 1) == 0 :
        result = ocl.firstMatch(s, "[A-Z]+")
    return result

  def startRow(s) :
    result = 0
    result = ocl.toInteger(ocl.firstMatch(s, "[0-9]+"))
    return result

  def endRow(s) :
    result = ""
    if (s.find(":") + 1) > 0 :
      result = ocl.toInteger(ocl.firstMatch(ocl.after(s, ":"), "[0-9]+"))
    else :
      if (s.find(":") + 1) == 0 :
        result = ocl.toInteger(ocl.firstMatch(s, "[0-9]+"))
    return result

  def killSpreadsheetCell(spreadsheetcell_x) :
    spreadsheetcell_instances = ocl.excludingSet(spreadsheetcell_instances, spreadsheetcell_x)
    free(spreadsheetcell_x)

class Worksheet : 
  worksheet_instances = []
  worksheet_index = dict({})

  def __init__(self):
    self.Visible = False
    self.ColumnNames = []
    self.AllCells = []
    self.Range = dict({})
    Worksheet.worksheet_instances.append(self)


  def newWorksheet(cells) :
    w = Worksheet()
    w.AllCells = cells
    w.ColumnNames = [c.ColumnName for c in cells]
    w.Range = dict({c.Name:c for c in cells})
    return w 

  def Activate(self) :
    self.Visible = True
    for c in self.AllCells : 
      c.Sheet = self

  def getACell(self, col, row) :
    result = None
    result = ocl.any([c for c in self.AllCells if c.ColumnName == col and c.RowNumber == row])
    return result

  def getOneCell(self, s) :
    result = None
    result = ocl.any([c for c in self.AllCells if c.ColumnName == ocl.firstMatch(s, "[A-Z]+") and str(c.RowNumber) + "" == ocl.firstMatch(s, "[0-9]+")])
    return result

  def getCellRange(self, s) :
    result = []
    startcol = ""
    startcol = SpreadsheetCell.startColumn(s)
    endcol = ""
    endcol = SpreadsheetCell.endColumn(s)
    startrow = 0
    startrow = SpreadsheetCell.startRow(s)
    endrow = ""
    endrow = SpreadsheetCell.endRow(s)
    result = [c for c in self.AllCells if c.ColumnName >= startcol and c.ColumnName <= endcol and ((c.RowNumber <= endrow) if (c.ColumnName == endcol) else (True)) and ((c.RowNumber >= startrow) if (c.ColumnName == startcol) else (True))]
    return result

  def getRange(self, s) : 
    if (s.find(":") + 1) > 0 :
      return self.getCellRange(s)
    else : 
      return self.getOneCell(s)


  def getRow(self, j) :
    result = []
    result = sorted([c for c in self.AllCells if c.RowNumber == j], key = lambda cx : cx.ColumnName)
    return result

  def getColumn(self, i) :
    result = []
    result = sorted([c for c in self.AllCells if c.ColumnName == (self.ColumnNames)[i - 1]], key = lambda cx : cx.RowNumber)
    return result

  def getCell(self, i) :
    result = None
    result = (self.AllCells)[i - 1]
    return result

  def getCell(self, i, j) :
    result = None
    result = ocl.any([c for c in self.AllCells if c.ColumnName == (self.ColumnNames)[j - 1] and c.RowNumber == i])
    return result

  def killWorksheet(worksheet_x) :
    worksheet_instances = ocl.excludingSet(worksheet_instances, worksheet_x)
    free(worksheet_x)

class Excel : 
  excel_instances = []
  excel_index = dict({})
  Worksheets = dict({})

  def __init__(self):
    Excel.excel_instances.append(self)

  def AccrInt(issueDate,firstInterestDate,settlement,rate,par,freq,basis) :
    result = 0.0
    pass
    return result

  def AccrIntM(issueDate,maturityDate,couponRate,par,basis) :
    result = 0.0
    pass
    return result

  def Acos(v) :
    result = 0.0
    result = math.acos(v)
    return result

  def Acosh(v) :
    result = 0.0
    result = MathLib.acosh(v)
    return result

  def Acot(v) :
    result = 0.0
    result = math.atan(1.0/v)
    return result

  def Acoth(v) :
    result = 0.0
    result = MathLib.atanh(1.0/v)
    return result

  def AmorDegrc(cost,purchaseDate,firstPeriodEnd,salvageValue,period,rate,basis) :
    pass

  def AmorLinc(cost,purchaseDate,firstPeriodEnd,salvageValue,period,rate,basis) :
    pass

  def And(sq) :
    result = False
    result = ocl.forAll(sq, lambda v : v == True)
    return result

  def Arabic(romanString) :
    result = 0.0
    pass
    return result

  def Asc(s) :
    result = ""
    result = s
    return result

  def Asin(v) :
    result = 0.0
    result = math.asin(v)
    return result

  def Asinh(v) :
    result = 0.0
    result = MathLib.asinh(v)
    return result

  def Atan2(x,y) :
    result = 0.0
    if x > 0 :
      result = math.atan(y/x)
    else :
      if x < 0 and y >= 0 :
        result = math.atan(y/x) + MathLib.piValue()
      else :
        if x < 0 and y < 0 :
          result = math.atan(y/x) - MathLib.piValue()
        else :
          if x == 0 and y > 0 :
            result = MathLib.piValue()/2
          else :
            if x == 0 and y < 0 :
              result = -MathLib.piValue()/2
    return result

  def Atanh(v) :
    result = 0.0
    result = MathLib.atanh(v)
    return result

  def AveDev(sq) :
    result = False
    n = 0
    n = len(sq)
    m = 0.0
    m = MathLib.mean(sq)
    result = 1.0/n * ocl.sum([math.abs(v-m) for v in sq])
    return result

  def Average(sq) :
    result = False
    result = MathLib.mean(sq)
    return result

  def Base(v,bse) :
    result = ""
    if bse == 2 :
      result = MathLib.decimal2binary(v)
    else :
      if bse == 8 :
        result = MathLib.decimal2octal(v)
      else :
        if bse == 16 :
          result = MathLib.decimal2hex(v)
        else :
          if True :
            result = v
    return result

  def BesselI(x,ord) :
    result = 0.0
    pass
    return result

  def BesselJ(x,ord) :
    result = 0.0
    for m in range(0,10) : 
      result = result + math.pow(-1,m)*math.pow(x/2,2*m+ord)/(math.factorial(m)*math.gamma(m+ord+1))
    return result

  def BesselK(x,ord) :
    result = 0.0
    pass
    return result

  def BesselY(x,ord) :
    result = 0.0
    pass
    return result

  def Beta_Dist(v,a,b) :
    result = 0.0
    pass
    return result

  def Beta_Inv(v,a,b) :
    result = 0.0
    pass
    return result

  def BetaDist(v,a,b) :
    result = 0.0
    pass
    return result

  def BetaInv(v,a,b) :
    result = 0.0
    pass
    return result

  def Bin2Dec(v) :
    result = ""
    result = "" + str(float(str(v) + ""))
    return result

  def Bin2Hex(v) :
    result = ""
    pass
    return result

  def Bin2Oct(v) :
    result = ""
    pass
    return result

  def Binom_Dist(num,trials,prob,cum) :
    result = 0.0
    if cum == False :
      result = MathLib.combinatorial(int(num), int(trials)) * math.pow(prob, trials) * math.pow((1 - prob), num - trials)
    else :
      if cum == True :
        result = ocl.sum([Excel.Binom_Dist(num, i, prob, False) for i in range(0, trials +1)])
    return result

  def Binom_Dist_Range(num,prob,mintrials,maxtrials) :
    result = ocl.sum([Excel.Binom_Dist(num, i, prob, False) for i in range(mintrials, maxtrials +1)])
    return result

  def Binom_Inv(trials,prob,alpha) :
    result = 0.0
    pass
    return result

  def BinomDist(num,trials,prob,cum) :
    result = 0.0
    result = self.Binom_Dist(num, trials, prob, cum)
    return result

  def Bitand(x,y) :
    result = 0.0
    result = MathLib.bitwiseAnd(x, y)
    return result

  def Bitor(x,y) :
    result = 0.0
    result = MathLib.bitwiseOr(x, y)
    return result

  def Bitxor(x,y) :
    result = 0.0
    result = MathLib.bitwiseXor(x, y)
    return result

  def Bitlshift(x,y) :
    result = 0.0
    result = x * math.pow(2, y)
    return result

  def Bitrshift(x,y) :
    result = 0.0
    result = x/math.pow(2, y)
    return result

  def Ceiling(x,y) :
    result = 0.0
    result = int(math.ceil(x))
    return result

  def Ceiling_Math(x,y,z) :
    result = 0.0
    result = int(math.ceil(x))
    return result

  def Ceiling_Precise(x,y) :
    result = 0.0
    result = int(math.ceil(x))
    return result

  def ChiDist(x,y) :
    result = 0.0
    pass
    return result

  def ChiInv(x,y) :
    result = 0.0
    pass
    return result

  def ChiSq_Dist(x,y) :
    result = 0.0
    pass
    return result

  def ChiSq_Dist_RT(x,y) :
    result = 0.0
    pass
    return result

  def ChiSq_Inv(x,y) :
    result = 0.0
    pass
    return result

  def ChiSq_Inv_RT(x,y) :
    result = 0.0
    pass
    return result

  def ChiSq_Test(obs,ratios) :
    result = 0.0
    pass
    return result

  def ChiTest(obs,ratios) :
    result = 0.0
    pass
    return result

  def Choose(ind,sq) :
    result = None
    indx = 0
    indx = ocl.toInteger("" + str(ind))
    result = (sq)[indx - 1]
    return result

  def Clean(s) :
    result = ""
    chrs = []
    chrs = ocl.characters(s)
    result = ocl.sumString([c for c in chrs if ord(c) > 31])
    return result

  def Combin(n,m) :
    result = 0
    result = MathLib.combinatorial(n, m)
    return result

  def Combina(n,m) :
    result = 0.0
    result = MathLib.combinatorial(int(n), int(m))
    return result

  def Complex(n,m,suff) :
    result = ""
    if (suff == None) :
      result = str(n) + " + (" + str(m) + ")*i"
    else :
      if True :
        result = str(n) + " + (" + str(m) + ")*" + suff
    return result

  def Confidence(signif,sigma,n) :
    result = 0.0
    pass
    return result

  def Confidence_Norm(signif,sigma,n) :
    result = 0.0
    pass
    return result

  def Confidence_T(signif,sigma,n) :
    result = 0.0
    pass
    return result

  def Convert(x,inUnit,outUnit) :
    result = None
    pass
    return result

  def Correl(xs,ys) :
    result = 0.0
    xmean = 0.0
    xmean = MathLib.mean(xs)
    ymean = 0.0
    ymean = MathLib.mean(ys)
    sumdiffs = 0.0
    sumdiffs = ocl.sum([(xs[i -1] - xmean) * (ys[i -1] - ymean) for i in range(1, len(xs) +1)])
    sumx = 0.0
    sumx = ocl.sum([(xs[i -1] - xmean) * (xs[i -1] - xmean) for i in range(1, len(xs) +1)])
    sumy = 0.0
    sumy = ocl.sum([(ys[i -1] - ymean) * (ys[i -1] - ymean) for i in range(1, len(ys) +1)])
    result = sumdiffs/math.sqrt((sumx * sumy))
    return result

  def Cosh(x) :
    result = 0.0
    result = math.cosh(x)
    return result

  def Cot(x) :
    result = 0.0
    result = 1.0/math.tan(x)
    return result

  def Coth(x) :
    result = 0.0
    result = 1.0/math.tanh(x)
    return result

  def Count(sq) :
    result = 0.0
    result = len([x for x in sq if ocl.isReal(str(x) + "")])
    return result

  def CountA(sq) :
    result = 0.0
    result = len([x for x in sq if len(str(x) + "") > 0])
    return result

  def CountBlank(sq) :
    result = 0.0
    result = len([x for x in sq if len(str(x) + "") == 0])
    return result

  def Covar(sq1,sq2) :
    result = 0.0
    m1 = MathLib.mean(sq1)
    m2 = MathLib.mean(sq2)
    sumprod = 0
    n = len(sq1)
    for i in range(0,n) : 
      sumprod = sumprod + (sq1[i] - m1)*(sq2[i] - m2)
    result = sumprod/n
    return result

  def Covariance_P(sq1,sq2) :
    result = 0.0
    result = Excel.Covar(sq1, sq2)
    return result

  def Covariance_S(sq1,sq2) :
    result = 0.0
    numbs1 = []
    numbs1 = [x for x in sq1 if (type(x) == float)]
    numbs2 = []
    numbs2 = [y for y in sq2 if (type(y) == float)]
    result = Excel.Covar(numbs1, numbs2)
    return result

  def CritBinom(x,y,z) :
    result = 0.0
    pass
    return result

  def Csc(x) :
    result = 0.0
    result = 1.0/math.sin(x)
    return result

  def Csch(x) :
    result = 0.0
    result = 1.0/math.sinh(x)
    return result

  def CumlPmt(rate,periods,presentValue,firstPeriod,lastPeriod,timing) :
    result = 0.0
    pass
    return result

  def CumPric(rate,periods,presentValue,firstPeriod,lastPeriod,timing) :
    result = 0.0
    pass
    return result

  def DAverage(x,y,z) :
    result = 0.0
    pass
    return result

  def Days(x,y) :
    result = 0.0
    result = x.daysDifference(y)
    return result

  def Days360(x,y) :
    result = 0.0
    if x > y :
      result = 0
    else :
      if x <= y :
        result = 360 * y.getYear() - x.getYear() + 30 * y.getMonth() - x.getMonth() + y.getDate() - x.getDate()
    return result

  def Db(cost,salvage,life,period,month) :
    result = 0.0
    pass
    return result

  def Dbcs(s) :
    result = ""
    result = s
    return result

  def Dec2Bin(x,places) :
    result = ""
    result = MathLib.decimal2binary(int(x))
    return result

  def Dec2Hex(x,places) :
    result = ""
    result = MathLib.decimal2hex(int(x))
    return result

  def Dec2Oct(x,places) :
    result = ""
    result = MathLib.decimal2octal(int(x))
    return result

  def Decimal(x,places) :
    result = 0.0
    result = float(x)
    return result

  def Degrees(x) :
    result = 0.0
    result = x * 57.29577951308232
    return result

  def Delta(x,y) :
    result = 0
    if x == y :
      result = 1
    else :
      if True :
        result = 0
    return result

  def DevSq(sq) :
    result = 0.0
    m = 0.0
    m = MathLib.mean(sq)
    result = ocl.sum([ocl.sqr((sq[i -1] - m)) for i in range(1, len(sq) +1)])
    return result

  def Duration(settle,matur,coup,irr,freq,basis) :
    result = 0.0
    term = 0.0
    term = matur - settle / 365.0
    npayments = 0
    npayments = int((term * freq))
    period = 0
    period = 1//freq
    result = ocl.sum([MathLib.discountDiscrete(100 * coup, irr, _ind * period) * _ind * period for _ind in range(1, npayments +1)]) + MathLib.discountDiscrete(100, irr, term) * term
    return result

  def Effect(nrate,nperiods) :
    result = 0.0
    result = math.pow((1 + nrate/nperiods), nperiods) - 1
    return result

  def Expon_Dist(x,lam,cum) :
    result = 0.0
    if cum == False :
      result = lam * math.exp((-lam * x))
    else :
      if cum == True :
        result = 1 - math.exp((-lam * x))
    return result

  def ExponDist(x,lam,cum) :
    result = 0.0
    result = self.Expon_Dist(x, lam, cum)
    return result

  def Fact(x) :
    result = 0.0
    result = MathLib.factorial(int(x))
    return result

  def FactDouble(x) :
    result = 0.0
    y = 0
    y = int(x)
    res = 0
    res = y
    while y > 0 :
      y = y - 2
      res = res * y
    return res

  def Fisher(x) :
    result = 0.0
    result = 0.5 * math.log((1 + x)/(1 - x))
    return result

  def FisherInv(x) :
    result = 0.0
    y = 0.0
    y = math.exp((2 * x))
    result = (y - 1)/(y + 1)
    return result

  def Floor(x,n) :
    result = 0.0
    result = MathLib.roundN(x, n)
    return result

  def Floor_Math(x,n,y) :
    result = 0.0
    result = MathLib.roundN(x, n)
    return result

  def Forecast(x,ys,xs) :
    result = 0.0
    pass
    return result

  def Gamma(x) :
    result = 0.0
    if x == 0 : 
      return 1
    if (int(x) == x) and x > 0 : 
      return MathLib.factorial(x-1)
    z = 2*x
    if (int(z) == z) and z > 0 : 
      n = int(x - 0.5)
      return math.sqrt(MathLib.piValue()) * MathLib.factorial(2*n)/(math.pow(4,n)*MathLib.factorial(n))
    # y = x - 1
    # f1 = math.sqrt((2 * MathLib.piValue() * y))
    # f2 = math.pow(y/MathLib.eValue(), y)
    # result = f1 * f2
    result = math.gamma(x)
    return result

  def Gcd(sq) :
    result = 0
    d = 0
    d = int((sq)[1 - 1])
    for x in sq :
      d = ocl.gcd(d, int(x))
    return d

  def Geo(sq) :
    result = 0.0
    result = math.pow(ocl.prd(sq), 1.0/len(sq))
    return result

  def HarMean(sq) :
    result = 0.0
    pass
    return result

  def Intercept(ys,xs) :
    result = 0.0
    result = Excel.Forecast(0, ys, xs)
    return result

  def IntRate(settle,matur,invest,redempt,basis) :
    result = 0.0
    termDays = None
    termDays = matur - settle
    result = (redempt - invest)/invest * 365 / termDays
    return result

  def Irr(paymnts,guess) :
    result = 0.0
    result = MathLib.irrDiscrete(paymnts)
    return result

  def IsEven(x) :
    result = False
    if (type(x) == int) and int(x) % 2 == 0 :
      result = True
    return result

  def IsNumber(x) :
    result = False
    if (type(x) == int) or (type(x) == float) :
      result = True
    return result

  def IsOdd(x) :
    result = False
    if (type(x) == int) and int(x) % 2 == 1 :
      result = True
    return result

  def IsText(x) :
    result = False
    if (type(x) == str) :
      result = True
    return result

  def Kurt(sq) :
    result = 0.0
    n = 0
    n = len(sq)
    m = 0.0
    m = MathLib.mean(sq)
    s = None
    s = allInstances_MathLib().standardDeviation()
    adjustment = 0.0
    adjustment = (3 * ocl.sqr((n - 1)))/((n - 2) * (n - 3))
    result = (n * (n + 1)//((n - 1) * (n - 2) * (n - 3))) * ocl.sum([math.pow((sq[i -1] - m)/s, 4) for i in range(1, n +1)]) - adjustment
    return result

  def Large(sq,k) :
    result = 0.0
    n = 0
    n = len(sq)
    ssq = []
    ssq = ocl.sortSequence(sq)
    result = (ssq)[n - k + 1 - 1]
    return result

  def Lcm(sq) :
    result = 0.0
    if len(sq) == 1 :
      result = (sq)[1 - 1]
    else :
      if len(sq) == 2 :
        result = MathLib.lcm(sq[1 -1], sq[2 -1])
      else :
        if len(sq) > 2 :
          result = MathLib.lcm(sq[1 -1], Excel.Lcm((sq)[1:]))
    return result

  def Ln(x) :
    result = 0.0
    result = math.log(x)
    return result

  def Log(x,base) :
    result = 0.0
    pass
    return result

  def Log10(x) :
    result = 0.0
    result = math.log10(x)
    return result

  def Min(r) :
    result = None
    result = ocl.minSequence(r)
    return result

  def Max(r) :
    result = None
    result = ocl.maxSequence(r)
    return result

  def NormalDist(x,mu,sigma) :
    result = 0.0
    disp = 0.0
    disp = (x - mu)/sigma
    twopi = 0.0
    twopi = 2 * MathLib.piValue()
    result = 1.0/(sigma * math.sqrt(twopi)) * math.exp((-0.5 * disp * disp))
    return result

  def cumulativeNormalDist(x,m,sigma) :
    result = 0.0
    k = 0.0
    k = 1/(1 + 0.2316419 * (x - m))
    poly = 0.0
    poly = (0.31938153 * k) + (-0.356563782 * k * k) + (1.781477937 * k * k * k) + (-1.821255978 * k * k * k * k) + (1.330274429 * k * k * k * k * k)
    result = 1 - Excel.NormalDist(x, m, sigma) * poly
    return result

  def cdfNormalDist(x,m,sigma) :
    result = 0.0
    if x >= m :
      result = Excel.cumulativeNormalDist(x, m, sigma)
    else :
      if x < m :
        result = 1 - Excel.cumulativeNormalDist(m + (m - x), m, sigma)
    return result

  def Norm_Dist(x,mu,sigma,cum) :
    result = 0.0
    if cum == False :
      result = Excel.NormalDist(x, mu, sigma)
    else :
      if cum == True :
        result = Excel.cdfNormalDist(x, mu, sigma)
    return result

  def NormDist(x,mu,sigma,cum) :
    result = 0.0
    result = Excel.Norm_Dist(x, mu, sigma, cum)
    return result

  def Norm_S_Dist(x,cum) :
    result = 0.0
    if cum == False :
      result = Excel.NormalDist(x, 0, 1)
    else :
      if cum == True :
        result = Excel.cdfNormalDist(x, 0, 1)
    return result

  def NormSDist(x) :
    result = 0.0
    result = Excel.cdfNormalDist(x, 0, 1)
    return result

  def NormSInv(x) :
    result = 0.0
    result = MathLib.bisectionAsc(0, -1000, 1000, lambda y : Excel.cdfNormalDist(y, 0, 1) - x, 0.0001)
    return result


  def killExcel(excel_x) :
    excel_instances = ocl.excludingSet(excel_instances, excel_x)
    free(excel_x)

def createSpreadsheetFont():
  spreadsheetfont = SpreadsheetFont()
  return spreadsheetfont

def allInstances_SpreadsheetFont():
  return SpreadsheetFont.spreadsheetfont_instances


spreadsheetfont_OclType = createByPKOclType("SpreadsheetFont")
spreadsheetfont_OclType.instance = createSpreadsheetFont()
spreadsheetfont_OclType.actualMetatype = type(spreadsheetfont_OclType.instance)


def createSpreadsheetCell():
  spreadsheetcell = SpreadsheetCell()
  return spreadsheetcell

def allInstances_SpreadsheetCell():
  return SpreadsheetCell.spreadsheetcell_instances


spreadsheetcell_OclType = createByPKOclType("SpreadsheetCell")
spreadsheetcell_OclType.instance = createSpreadsheetCell()
spreadsheetcell_OclType.actualMetatype = type(spreadsheetcell_OclType.instance)


def createWorksheet():
  worksheet = Worksheet()
  return worksheet

def allInstances_Worksheet():
  return Worksheet.worksheet_instances


worksheet_OclType = createByPKOclType("Worksheet")
worksheet_OclType.instance = createWorksheet()
worksheet_OclType.actualMetatype = type(worksheet_OclType.instance)


def createExcel():
  excel = Excel()
  return excel

def allInstances_Excel():
  return Excel.excel_instances


excel_OclType = createByPKOclType("Excel")
excel_OclType.instance = createExcel()
excel_OclType.actualMetatype = type(excel_OclType.instance)


cell1 = SpreadsheetCell.newSpreadsheetCell("A1", 100)
cell2 = SpreadsheetCell.newSpreadsheetCell("A2", 200)
cell3 = SpreadsheetCell.newSpreadsheetCell("A3", 300)
# cell4 = SpreadsheetCell.newSpreadsheetCell("A4", 400)
# cell5 = SpreadsheetCell.newSpreadsheetCell("B1", 100)
# cell6 = SpreadsheetCell.newSpreadsheetCell("B2", 200)

# print(cell3.Value)
# print(cell3.ColumnName)
# print(cell3.RowNumber)

# cells = [cell1, cell2, cell3]
# wk = Worksheet.newWorksheet(cells)
# print(wk.AllCells)
# wk.Activate()

# print(cell1.Offset(0,2))

# wk.AllCells = [cell1, cell2, cell3, cell4, cell5, cell6]

# c = wk.Range["A2"]
# print(c.Value)

# c = wk.getOneCell("A3")
# print(c.Value)
# print(c.ColumnName)
# print(c.RowNumber)

# cs = wk.getRange("A1:A3")

# print(len(cs))
# print(cs[0].Value)
# print(cs[1].Value)
# print(cs[2].Value)
# print(cs[3].Value)


# print(Excel.Norm_S_Dist(0,False))
# print(Excel.Norm_S_Dist(2,True))
# print(Excel.NormSDist(1.0))
# print(Excel.NormSDist(1.1))
# print(Excel.NormSDist(1.2))
# print(Excel.NormSDist(1.3))
# print(Excel.NormSDist(1.4))
# print(Excel.NormSDist(1.5))
# print(Excel.NormSDist(1.6))
# print(Excel.NormSDist(1.7))
# print(Excel.NormSDist(1.8))
# print(Excel.NormSDist(1.9))
# print(Excel.NormSDist(2.0))

# print(Excel.NormSInv(0.99))

# print(Excel.Gamma(0))
# print(Excel.Gamma(0.5))

# print(Excel.Gamma(1))
# print(Excel.Gamma(1.5))
# print(Excel.Gamma(2))
# print(Excel.Gamma(2.5))
# print(Excel.Gamma(3))
# print(Excel.Gamma(3.5))
# print(Excel.Gamma(3.7))
# print(Excel.Gamma(4))
# print(Excel.BesselJ(5,1))

# print(Excel.Binom_Dist_Range(5,0.2,3,5))



