# OCL library ocl.py
import re

def sqr(x) : 
  return x*x

def cbrt(x) : 
  return pow(x, 1.0/3)


def isUnique(s) : 
  return len(set(s)) == len(s)

def any(s) : 
  return list(s)[0]



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
  x1 = x[0:i-1]
  x2 = x[i-1:]
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
  return re.split(pattern,str)


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


def allMatches(str, pattern) : 
  res = []
  res.extend(re.findall(pattern,str)) 
  return res
  

def replaceAll(str, pattern, repl) : 
  res = '' + str
  res = re.sub(pattern, repl, res)
  return res


def insertAt(x,i,s) :
  # i must be > 0
  x1 = x[0:i-1]
  x2 = x[i-1:]
  x1.extend(s)
  x1.extend(x2)
  return x1


def includingSequence(s,x) :
  res = []
  res.extend(s)
  res.append(x)
  return res


def includingSet(s,x) :
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


def prepend(s,x) :
  res = [x]
  res.extend(s)
  return res


def append(s,x) :
  res = []
  res.extend(s)
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


def concatenateAll(sq) :
  res = []
  for s in sq : 
    res.extend(s)
  return res


def unionAll(sq) :
  res = set({})
  for s in sq : 
    res = res.union(s)
  return res


def intersectionAllSet(sq) :
  res = any(sq)
  for s in sq : 
    res = res.intersection(s)
  return res


def intersectionAllSequence(sq) :
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


def selectSet(s,f) : 
  return set({x for x in s if f(x)})

def selectSequence(s,f) : 
  return [x for x in s if f(x)]

def rejectSet(s,f) : 
  return set({x for x in s if not f(x)})

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
    result += x
  return result


def prd(col) : 
  result = 1
  for x in col : 
    result *= x
  return result

 
def iterate(col,init,f) : 
  result = init
  for x in col : 
    result = f(x,result)
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
  res = dict({})
  for x in s1 :
    if x in s2 :
      pass
    else :
      res[x] = s1[x]
  return res


def excludingMapKey(s,y) :
  res = dict({})
  for x in s :
    if x == y :
      pass
    else :
      res[x] = s[x]
  return res


def excludingMapValue(s,y) :
  res = dict({})
  for x in s :
    if s[x] == y :
      pass
    else :
      res[x] = s[x]
  return res


def unionMap(s,t) :
  res = dict({})
  for x in s :
    res[x] = s[x]
  for x in t :
    res[x] = t[x]
  return res

def intersectionMap(s,t) :
  res = dict({})
  for x in s :
    if x in t and s[x] == t[x] :
      res[x] = s[x]
  return res

def selectMap(m,p) :
  res = dict({})
  for x in m :
    if p(m[x]) :
      res[x] = m[x]
  return res

def rejectMap(m,p) :
  res = dict({})
  for x in m :
    if p(m[x]) :
      pass
    else :
      res[x] = m[x]
  return res


def collectMap(m,e) :
  res = dict({})
  for x in m :
    res[x] = e(m[x])
  return res



def restrict(m,ks) :
  res = dict({})
  for x in m :
    if x in ks :
      res[x] = m[x]
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


