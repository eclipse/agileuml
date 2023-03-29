import inspect
import importlib


class OclAttribute : 
  def __init__(self,nme,typ=None) : 
    self.name = nme
    self.type = typ

  def getName(self) : 
    return self.name

  def getType(self) : 
    return self.type


class OclMethod : 
  def __init__(self,nme,typ=None) : 
    self.name = nme
    self.type = typ
    self.parameters = []

  def getName(self) : 
    return self.name

  def getType(self) : 
    return self.type

  def getReturnType(self) : 
    return self.type

  def addParameter(self,par) : 
    self.parameters.append(par)

  def getParameters(self) : 
    return self.parameters


class OclType : 
  ocltype_index = dict({})

  def __init__(self,nme) : 
    self.name = nme
    self.instance = None
    self.stereotypes = []
    self.actualMetatype = None

  def getName(self) : 
    return self.name

  def typename(T) :
    if T == type("") : 
      return "String"
    if T == type(0) : 
      return "int"
    if T == type(0.0) :
      return "double"
    if T == type(True) : 
      return "boolean" 
    if T == type(None) : 
      return "void" 
    if T == type([]) : 
      return "Sequence"
    if T == type(set([])) : 
      return "Set"
    if T == type(dict([])) : 
      return "Map"
    if T == type(lambda x : x) : 
      return "Function"
    for nme in OclType.ocltype_index : 
      tx = OclType.ocltype_index[nme]
      if tx.actualMetatype == T : 
        return tx.getName()
    s = str(T)
    if len(s) > 7 :
      tname = s[7:-1]
      return tname
    return ""

  def getFields(self) : 
    res = []

    if self.instance == None : 
      return res

    ls = inspect.getmembers(self.instance)
    for (name,value) in ls :
      if name[0] == '_' : 
        pass
      elif inspect.ismethod(value) : 
        pass
      elif inspect.isfunction(value) : 
        pass
      else :
        T = type(value)
        tname = OclType.typename(T)
        typeobj = createByPKOclType(tname)
        res.append(OclAttribute(name,typeobj))
    return res


  def allAttributes(obj) : 
    res = []

    if obj == None : 
      return res

    if type(obj) == type(dict({})) : 
      for nme in obj :
        T = type(obj[nme])
        tname = OclType.typename(T)
        typeobj = createByPKOclType(tname)
        res.append(OclAttribute(nme,typeobj))
      return res

    ls = inspect.getmembers(obj)
    for (name,value) in ls :
      if name[0] == '_' : 
        pass
      elif inspect.ismethod(value) : 
        pass
      elif inspect.isfunction(value) : 
        pass
      else :
        T = type(value)
        tname = OclType.typename(T)
        typeobj = createByPKOclType(tname)
        res.append(OclAttribute(name,typeobj))
    return res

  def getMethods(self) :
    res = []
    if self.actualMetatype == None : 
      return res
    ls = inspect.getmembers(self.actualMetatype)
    for (name,value) in ls :
      if name[0] == '_' : 
        pass
      elif inspect.isfunction(value) : 
        newmethod = OclMethod(name)
        sig = inspect.signature(value)
        for x in sig.parameters : 
          parx = OclAttribute(str(x))
          newmethod.addParameter(parx)
        res.append(newmethod)
    return res

  def getConstructors(self) :
    res = []
    if self.actualMetatype == None : 
      return res
    ls = inspect.getmembers(self.actualMetatype)
    for (name,value) in ls :
      if name == '__init__' and inspect.isfunction(value) : 
        newmethod = OclMethod(name)
        sig = inspect.signature(value)
        for x in sig.parameters : 
          parx = OclAttribute(str(x))
          newmethod.addParameter(parx)
        res.append(newmethod)
    return res

  def getSuperclass(self) : 
    res = None
    if self.actualMetatype == None : 
      return res
    lst = inspect.getmro(self.actualMetatype)
    if len(lst) > 1 : 
      st = lst[1]
      tname = OclType.typename(st)
      typeobj = createByPKOclType(tname)
      return typeobj
    return res

  def getAttributeValue(obj,att) : 
    if type(obj) == type(dict({})) : 
      return obj[att]
    return getattr(obj,att)

  def setAttributeValue(obj,att,val) : 
    if type(obj) == type(dict({})) : 
      obj[att] = val
      return 
    setattr(obj,att,val)

  def hasAttribute(obj,att) :
    if type(obj) == type(dict({})) : 
      return att in obj 
    return hasattr(obj,att)

  def removeAttribute(obj,att) : 
    if type(obj) == type(dict({})) : 
      if att in obj : 
        del obj[att]
      return
    delattr(obj,att)

  def isArray(self) : 
    if "Sequence" == self.name : 
      return True
    return False 

  def isPrimitive(self) : 
    if self.name in ["int", "long", "double", "boolean"] : 
      return True
    return False 

  def isAssignableFrom(self,c) :
  # c.actualMetatype is a subclass of self.actualMetatype
    if c.actualMetatype == self.actualMetatype : 
      return True
    if issubclass(c.actualMetatype,self.actualMetatype) : 
      return True
    return False

  def isInstance(self,obj) :  
    if isinstance(obj,self.actualMetatype) : 
      return True
    return False
        
  def newInstance(self) : 
    return eval(self.name + '()')

  def loadExecutableObject(s) :
    return importlib.import_module(s)


def getOclTypeByPK(_ex) :
  if (_ex in OclType.ocltype_index) :
    return OclType.ocltype_index[_ex]
  else :
    return None


def createByPKOclType(_value):
  result = getOclTypeByPK(_value)
  if (result != None) : 
    return result
  else :
    result = OclType(_value)
    OclType.ocltype_index[_value] = result
    return result


string_OclType = createByPKOclType("String")
string_OclType.instance = ""
string_OclType.actualMetatype = type("")

int_OclType = createByPKOclType("int")
int_OclType.instance = 0
int_OclType.actualMetatype = type(0)

long_OclType = createByPKOclType("long")
long_OclType.instance = 0
long_OclType.actualMetatype = type(0)

double_OclType = createByPKOclType("double")
double_OclType.instance = 0.0
double_OclType.actualMetatype = type(0.0)

boolean_OclType = createByPKOclType("boolean")
boolean_OclType.instance = True
boolean_OclType.actualMetatype = type(True)

void_OclType = createByPKOclType("void")
void_OclType.instance = None
void_OclType.actualMetatype = type(None)

sequence_OclType = createByPKOclType("Sequence")
sequence_OclType.instance = []
sequence_OclType.actualMetatype = type([])

set_OclType = createByPKOclType("Set")
set_OclType.instance = set([])
set_OclType.actualMetatype = type(set([]))

map_OclType = createByPKOclType("Map")
map_OclType.instance = dict([])
map_OclType.actualMetatype = type(dict([]))

function_OclType = createByPKOclType("Set")
function_OclType.instance = (lambda x : x)
function_OclType.actualMetatype = type(lambda x : x)



