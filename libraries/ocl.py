# OCL library ocl.py
import re
import random
import math

from sortedcontainers import *
# Needed for operations on sorted sets

def sqr(x) : 
  return x*x

def sqrt(x) : 
  return math.sqrt(x)

def cbrt(x) : 
  return pow(x, 1.0/3)

def size(x) :
  res = 0
  for v in x : 
    res = res + 1
  return res

def abs(x) : 
  if x < 0 : 
    return -x
  else : 
    return x

def gcd(xx,yy) :
  x = abs(xx)
  y = abs(yy) 
  while (x != 0 and y != 0) :
    z = y 
    y = x % y 
    x = z
  if (y == 0) :
    return x  
  if (x == 0) :
    return y 
  return 0

def roundTo(x,n) :
  if n == 0 : 
    return round(x)
  y = x*math.pow(10,n) 
  return round(y)/math.pow(10,n) 

def truncateTo(x,n) :
  if n <= 0 : 
    return int(x)
  y = x*math.pow(10,n) 
  return int(y)/math.pow(10,n) 

def isInteger(sx) : 
  ss = str(sx)
  if isMatch(ss,"-?[0-9]+") :
    return True
  if int(sx) == sx : 
    return True
  return False

def isLong(sx) : 
  return isInteger(sx)

def isReal(sx) : 
  ss = str(sx)
  if isMatch(ss,"-?[0-9]+") :
    return True
  return isMatch(ss,"-?[0-9]+.[0-9]+")

def toInteger(sx) :
  ss = str(sx)
  ind = ss.find(".")
  if ind >= 0 : 
    ss = ss[0:ind] 
  if ss.startswith("0x") : 
    return int(ss[2:],16)
  if ss.startswith("0b") : 
    return int(ss[2:],2)
  if ss.startswith("0") and len(ss) > 1 :
    return int(ss[1:],8)
  return int(ss)

def toLong(sx) :
  return toInteger(sx)

def toReal(sx) : 
  ss = str(sx)
  return float(ss)

def toBoolean(xx) : 
  if xx == None : 
    return False
  if xx == 0 : 
    return False
  if str(xx) == "False" : 
    return False
  if str(xx) == "false" : 
    return False
  if str(xx) == "" : 
    return False
  return True



def char2byte(ch) : 
  if ch == "" : 
    return -1
  return ord(ch)

def byte2char(bb) : 
  if bb == -1 : 
    return ""
  return chr(bb)

def asBag(sq) : 
#  ln = len(sq)
#  if ln == 0 : 
#    return sq
#  res = []
#  inds = random.sample(range(0,ln),ln)
#  for x in inds :
#    res.append(sq[x])
#  return res
  return sortSequence(sq)

def asOrderedSet(sq) :
  elems = set([])
  res = []
  for x in sq :
    if x in elems :
      pass 
    else :
      res.append(x)
      elems.add(x)
  return res


def isUnique(s) : 
  return len(set(s)) == len(s)

def any(s) : 
  lst = list(s)
  if 1 <= len(lst) : 
    return lst[0]
  return None

def anyMap(s) : 
  for k in s : 
    return s[k]
  return None

def includesAll(supset,subset) :
  for x in subset :
    if x in supset :
      pass
    else :
      return False
  return True



def excludesAll(supset,subset) :
  for x in subset :
    if x in supset :
      return False
  return True


def iterate(sq,init,f) : 
  acc = init
  for x in sq : 
    acc = f(x,acc)
  return acc

def exists(col,f) :
  for x in col :
    if f(x) :
      return True
  return False


def forAll(col,f) :
  for x in col :
    if f(x) :
      pass
    else :
      return False
  return True


def exists1(col,f) :
  found = False
  for x in col :
    if f(x) :
      if found :
        return False
      else :
        found = True
  return found


def toLowerCase(str) :
  s = '' + str
  return s.lower()


def toUpperCase(str) :
  s = '' + str
  return s.upper()


def before(s,sep) : 
  if len(sep) <= 0 : 
    return s
  i = s.find(sep)
  if i < 0 : 
    return s
  else : 
    return s[0:i]

def after(s,sep) : 
  j = len(sep)
  if j <= 0 : 
    return ""
  i = s.find(sep)
  if i < 0 : 
    return ""
  else : 
    return s[i+j:]


def characters(str) :
  res = []
  for x in str :
    res.append('' + x)
  return res


def insertAtString(x,i,s) :
  # i must be > 0
  if i <= 0 : 
    return x
  x1 = x[0:i-1]
  x2 = x[i-1:]
  return x1 + s + x2

def setAtString(x,i,s) :
  # i must be > 0, s is length 1
  if i <= 0 : 
    return x
  x1 = x[0:i-1]
  x2 = x[i:]
  return x1 + s + x2

def reverseString(st) :
  if len(st) == 0 : 
    return st
  else :
    return reverseString(st[1:]) + st[0:1]
  

def subtractString(s1,s2) :
  res = ''
  for x in s1 :
    if x in s2 :
      pass
    else :
      res = res + x
  return res


def equalsIgnoreCase(s1,s2) :
  result = False
  if toLowerCase(s1) == toLowerCase(s2) :
    result = True
  return result

def compareTo(s1,s2) : 
  result = 0
  if s1 < s2 : 
    return -1
  if s1 > s2 : 
    return 1
  return result

def indexOfSequence(sq,x) : 
  if x in sq : 
    return sq.index(x) + 1
  else : 
    return 0

def lastIndexOfSequence(sq,x) : 
  if x in sq : 
    sqr = reverseSequence(sq)
    i = indexOfSequence(sqr,x)
    return len(sq) - i + 1
  else : 
    return 0

def lastIndexOf(s,d) :
  result = 0
  dlen = len(d)
  if dlen == 0 : 
    return 0
  i = (reverseString(s).find(reverseString(d)) + 1)
  if i <= 0 :
    result = 0
  else :
    if i > 0 :
      result = len(s) - i - dlen + 2
  return result

def lastIndexOfString(s, d) : 
  return lastIndexOf(s,d)

def hasPrefixSequence(sq,sq1,j) : 
  m = len(sq1)
  n = len(sq)
  if m == 0 or n == 0 or n < j+m : 
    return False
  i = 0
  while i < m and i+j < n : 
    if sq[i+j] != sq1[i] : 
      return False
    i = i+1
  return True

def indexOfString(ss,sub) : 
  n = ss.find(sub)
  return n + 1

def indexOfSubSequence(sq,sq1) : 
  m = len(sq1)
  n = len(sq)
  if m == 0 or n == 0 or n < m : 
    return 0
  i = 0
  while i+m <= n : 
    if hasPrefixSequence(sq,sq1,i) : 
      return i+1
    i = i + 1
  return 0


def lastIndexOfSubSequence(sq,sq1) :
  m = len(sq1)
  n = len(sq)
  if m == 0 or n == 0 or n < m : 
    return 0
  rsq = reverseSequence(sq)
  rsq1 = reverseSequence(sq1)
  i = indexOfSubSequence(rsq,rsq1)
  if i <= 0 :
    return 0
  return n - i - m + 2
 
def at(sq,i) : 
  if i <= 0 : 
    return None
  if len(sq) > i : 
    return None
  return sq[i-1]

def first(sq) : 
  if len(sq) == 0 : 
    return None
  return sq[0]

def last(sq) : 
  if len(sq) == 0 : 
    return None
  return sq[len(sq)-1]

def front(sq) :
  if len(sq) == 0 : 
    return sq 
  return sq[:-1]

def tail(sq) :
  if len(sq) == 0 : 
    return sq 
  return sq[1:]


def trim(str) : 
  res = '' + str
  return res.strip()


def replace(str,sub,rep) : 
  res = '' + str
  return res.replace(sub,rep)


def splitByAll(str, delimiters) :
  if 0 < len(delimiters) :  
    delim = delimiters[0] 
    taildelims = (delimiters)[1:]
    splits = str.split(delim)
    res = [] 
    for st in splits : 
      if len(st) > 0 : 
        res.extend(splitByAll(st, taildelims))
    return res
  else : 
    result = [] 
    result.append(str) 
    return result


def split(str, pattern) : 
  splits = re.split(pattern,str)
  res = [] 
  for st in splits : 
    if len(st) > 0 : 
      res.append(st)
  return res


def isMatch(str, pattern) : 
  if re.fullmatch(pattern,str) == None : 
    return False
  else : 
    return True

def hasMatch(str, pattern) : 
  if re.search(pattern,str) == None : 
    return False
  else : 
    return True


def firstMatch(str, pattern) : 
  m = re.search(pattern,str)
  if m == None :  
    return None
  else : 
    return m.group()


def allMatches(str, pattern) : 
  res = []
  res.extend(re.findall(pattern,str)) 
  return res
  

def replaceAll(str, pattern, repl) : 
  res = '' + str
  res = re.sub(pattern, repl, res)
  return res


def replaceAllMatches(str, pattern, repl) : 
  res = '' + str
  res = re.sub(pattern, repl, res)
  return res


def replaceFirstMatch(str, pattern, repl) : 
  res = '' + str
  res = re.sub(pattern, repl, res, 1)
  return res


def insertInto(x,i,s) :
  # i must be > 0
  if i <= 0 : 
    return x
  x1 = x[0:i-1]
  x2 = x[i-1:]
  x1.extend(s)
  x1.extend(x2)
  return x1

def excludingSubrange(x,i,j) :
  # i must be > 0, i <= j
  if i <= 0 : 
    return x
  if i > j : 
    return x
  x1 = x[0:i-1]
  x2 = x[j:]
  x1.extend(x2)
  return x1

def insertAt(x,i,s) :
  # i must be > 0
  if i <= 0 : 
    return x
  sq = []
  sq.extend(x)
  sq.insert(i-1,s)
  return sq


def setAt(sq,i,val) : 
  res = []
  res.extend(sq)
  if i >= 1 and i <= len(sq) : 
    res[i-1] = val
  return res

def removeAt(sq,i) : 
  res = []
  res.extend(sq)
  if i >= 1 and i <= len(sq) :
    del res[i-1]
  return res

def setAtMap(sq,k,val) : 
  res = sq.copy()
  res[k] = val
  return res

def removeAtMap(sq,k) : 
  res = sq.copy()
  del res[k]
  return res

def removeAtString(ss,i) : 
  res = "" + ss
  if i == 1 and i <= len(ss) : 
    return res[1:]
  if i >= 2 and i <= len(ss) :
    return ss[0:(i-1)] + ss[i:]
  return res

def excludingAtString(ss,i) : 
  return removeAtString(ss,i)


def excludingFirst(sq,x) : 
  res = []
  res.extend(sq)
  try : 
    res.remove(x)
  except : 
    pass
  return res



def includingSequence(s,x) :
  res = []
  res.extend(s)
  res.append(x)
  return res

def includingSet(s,x) :
  res = s.copy()
  res.add(x)
  return res

def includingSortedSet(s,x) :
  res = s.copy()
  res.add(x)
  return res


def excludeAllSequence(s1,s2) :
  res = []
  for x in s1 :
    if x in s2 :
      pass
    else :
      res.append(x)
  return res


def excludeAllSet(s1,s2) :
  res = s1.copy()
  return res.difference(s2)

def excludeAllSortedSet(s1,s2) :
  res = s1.copy()
  return res.difference(s2)


def excludingSequence(s,y) :
  res = []
  for x in s :
    if x == y :
      pass
    else :
      res.append(x)
  return res


def excludingSet(s,x) :
  res = s.copy()
  subtr = {x}
  return res.difference(subtr)

def excludingSortedSet(s,x) :
  res = s.copy()
  res.discard(x)
  return res

def prepend(s,x) :
  res = [x]
  res.extend(s)
  return res


def append(s,x) :
  res = []
  res.extend(s)
  res.append(x)
  return res


def union(s,t) :
  res = []
  res.extend(s)
  for x in t : 
    if x in s : 
      pass
    else : 
      res.append(x)
  return res


def unionSequence(s,t) :
  res = []
  res.extend(s)
  res.extend(t)
  return res


def unionSet(s,t) :
  res = s.copy()
  return res.union(t)

def unionSortedSet(s,t) :
  res = s.copy()
  return res.union(t)
 

def concatenate(s,t) :
  res = []
  res.extend(s)
  res.extend(t)
  return res
 

def intersectionSequence(s,t) :
  res = [x for x in s if x in t]
  return res


def intersectionSet(s,t) :
  res = s.copy()
  return res.intersection(t) 

def intersectionSortedSet(s,t) :
  res = s.copy()
  return res.intersection(t) 


def concatenateAll(sq) :
  res = []
  for s in sq : 
    res.extend(s)
  return res


def unionAll(sq) :
  res = any(sq)
  for s in sq : 
    res = res.union(s)
  return res



def intersectAllSet(sq) :
  res = any(sq)
  for s in sq : 
    res = res.intersection(s)
  return res


def intersectAllSequence(sq) :
  res = any(sq)
  for s in sq : 
    res = intersectionSequence(res,s)
  return res


def reverseSequence(s) :
  res = []
  res.extend(s)
  res.reverse()
  return res


def reverseSet(s) :
  return s.copy()


def sortSequence(s) :
  res = []
  res.extend(s)
  res.sort()
  return res


def sortSet(s) :
  return s.copy()

def sortMap(s) :
  ks = sorted(s)
  m = dict({})
  for k in ks : 
    m[k] = s[k]
  return m


def selectSet(s,f) : 
  return set({x for x in s if f(x)})

def selectSortedSet(s,f) : 
  return SortedSet({x for x in s if f(x)})

def selectSequence(s,f) : 
  return [x for x in s if f(x)]

def rejectSet(s,f) : 
  return set({x for x in s if not f(x)})

def rejectSortedSet(s,f) : 
  return SortedSet({x for x in s if not f(x)})

def rejectSequence(s,f) : 
  return [x for x in s if not f(x)]

def collectSet(s,f) : 
  return set({f(x) for x in s})

def collectSequence(s,f) : 
  return [f(x) for x in s]

def any1(s,f) : 
  for x in s : 
    if f(x) : 
      return x
  return None



def selectMaximalsSequence(col,f) :
  result = []
  if len(col) == 0 :
    return result
  maximal = f(col[0])
  result = [col[0]]
  for x in col[1:] :
    value = f(x)
    if value > maximal :
      result = [x]
      maximal = value
    else : 
      if value == maximal :
        result.append(x)
  return result
 

def selectMaximalsSet(col,f) :
  result = {}
  elems = col.copy()
  if len(col) == 0 :
    return result
  x = elems.pop()
  maximal = f(x)
  result = {x}
  for y in elems :
    value = f(y)
    if value > maximal :
      result = {y}
      maximal = value
    else : 
      if value == maximal :
        result.add(y)
  return result


def selectMinimalsSequence(col,f) :
  result = []
  if len(col) == 0 :
    return result
  minimal = f(col[0])
  result = [col[0]]
  for x in col[1:] :
    value = f(x)
    if value < minimal :
      result = [x]
      minimal = value
    else : 
      if value == minimal :
        result.append(x)
  return result
 

def selectMinimalsSet(col,f) :
  result = {}
  elems = col.copy()
  if len(col) == 0 :
    return result
  x = elems.pop()
  minimal = f(x)
  result = {x}
  for y in elems :
    value = f(y)
    if value < minimal :
      result = {y}
      minimal = value
    else : 
      if value == minimal :
        result.add(y)
  return result
 

def minSequence(sq) :
  result = sq[0]
  for x in sq :
    if x < result :
      result = x
  return result


def minSet(col) :
  elems = col.copy()
  result = elems.pop()
  for x in elems :
    if x < result :
      result = x
  return result


def maxSequence(sq) :
  result = sq[0]
  for x in sq :
    if x > result :
      result = x
  return result


def maxSet(col) :
  elems = col.copy()
  result = elems.pop()
  for x in elems :
    if x > result :
      result = x
  return result


def sum(col) : 
  result = 0
  for x in col : 
    result = result + x
  return result

def sumint(col) : 
  return sum(col)

def sumlong(col) : 
  return sum(col)

def sumdouble(col) : 
  result = 0.0
  for x in col : 
    result = result + x
  return result

def sumString(col) : 
  result = ""
  for x in col : 
    result = result + x
  return result


def prd(col) : 
  result = 1
  for x in col : 
    result *= x
  return result


def includesAllMap(supset,subset) :
  for x in subset :
    if x in supset :
      if subset[x] != supset[x] :
        return False       
    else :
      return False
  return True



def excludesAllMap(supset,subset) :
  for x in subset :
    if x in supset :
      if subset[x] == supset[x] :
        return False
  return True



def existsMap(m,f) :
  for x in m :
    if f(m[x]) :
      return True
  return False


def forAllMap(m,f) :
  for x in m :
    if f(m[x]) :
      pass
    else :
      return False
  return True


def exists1Map(m,f) :
  found = False
  for x in m :
    if f(m[x]) :
      if found :
        return False
      else :
        found = True
  return found


def includingMap(m,x,y) :
  res = m.copy()
  res[x] = y
  return res


def excludeAllMap(s1,s2) :
  res = s1.copy()
  for x in s1 :
    if x in s2 :
      del res[x]
  return res


def excludingMapKey(m,y) :
  res = m.copy()
  if y in m :  
    del res[y]
  return res


def excludingMapValue(s,y) :
  res = s.copy()
  for x in s :
    if s[x] == y :
      del res[x]
  return res


def unionMap(s,t) :
  res = s.copy()
  for x in t :
    res[x] = t[x]
  return res

def unionAllMap(sq) :
  res = dict({})
  for s in sq : 
    res = unionMap(res,s)
  return res


def intersectionMap(s,t) :
  res = s.copy()
  for x in s :
    if x in t and s[x] == t[x] :
      pass
    else : 
      del res[x]
  return res

def intersectAllMap(sq) :
  res = dict({})
  if len(sq) == 0 : 
    return res
  res = sq[0]

  for s in sq : 
    res = intersectionMap(res,s)
  return res


def selectMap(m,p) :
  res = m.copy()
  for x in m :
    if p(m[x]) :
      pass
    else : 
      del res[x]
  return res


def rejectMap(m,p) :
  res = m.copy()
  for x in m :
    if p(m[x]) :
      del res[x]
  return res


def collectMap(m,e) :
  res = dict({})
  for x in m :
    res[x] = e(m[x])
  return res



def restrict(m,ks) :
  res = m.copy()
  for x in m :
    if x in ks :
      pass
    else :
      del res[x]
  return res


def antirestrict(m,ks) :
  res = m.copy()
  for x in m :
    if x in ks :
      del res[x]
  return res

def excludingAtMap(m,k) : 
  res = m.copy()
  if x in m : 
    del res[k]
  return res


def keys(m) : 
  res = set({})
  for x in m : 
    res.add(x)
  return res


def values(m) : 
  res = set({})
  for x in m : 
    res.add(m[x])
  return res



