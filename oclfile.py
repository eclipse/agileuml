import ocl
import math
import os
import pickle

from enum import Enum

def free(x):
  del x


class OclFile:
  oclfile_instances = []
  oclfile_index = dict({})

  def __init__(self):
    self.name = ""
    self.position = 0
    self.actualFile = None
    self.lastRead = None
    self.eof = False
    OclFile.oclfile_instances.append(self)

  def newOclFile(nme) : 
    f = createByPKOclFile(nme)
    return f

  def newOclFile_Read(f) : 
    f.openRead()
    return f

  def newOclFile_Write(f) : 
    f.openWrite()
    return f

  def newOclFile_ReadB(f) : 
    f.openReadB()
    return f

  def newOclFile_WriteB(f) : 
    f.openWriteB()
    return f

  def getName(self) : 
    return self.name

  def compareTo(self,f) : 
    if self.name < f.name : 
      return -1 
    if self.name > f.name : 
      return 1
    return 0

  def canRead(self) : 
    if self.actualFile != None : 
      return self.actualFile.readable()
    return False

  def canWrite(self) : 
    if self.actualFile != None : 
      return self.actualFile.writable()
    return False


  def openWrite(self) : 
    if self.name == "System.out" or self.name == "System.err" : 
      pass 
    else : 
      self.actualFile = open(self.name, 'w')
    self.position = 0

  def openRead(self) : 
    if self.name == "System.in" : 
      pass
    else : 
      self.actualFile = open(self.name, 'r')
    self.position = 0

  def openWriteB(self) : 
    if self.name == "System.out" or self.name == "System.err" : 
      pass 
    else : 
      self.actualFile = open(self.name, 'wb')
    self.position = 0

  def openReadB(self) : 
    if self.name == "System.in" : 
      pass
    else : 
      self.actualFile = open(self.name, 'rb')
    self.position = 0

  def exists(self) : 
    return os.path.isfile(self.name)

  def isFile(self) : 
    return os.path.isfile(self.name)

  def isDirectory(self) : 
    return os.path.isdir(self.name)

  def isAbsolute(self) : 
    return os.path.isabs(self.name)

  def getAbsolutePath(self) : 
    return os.path.abspath(self.name)

  def getPath(self) : 
    return os.path.realpath(self.name)

  def getParent(self) : 
    pth = os.path.abspath(self.name)
    return os.path.dirname(pth)

  def getParentFile(self) : 
    pth = os.path.abspath(self.name)
    str = os.path.dirname(pth)
    if str != None : 
      return OclFile.newOclFile(str)

  def lastModified(self) : 
    return int(os.path.getmtime(self.name))

  def length(self) : 
    return os.path.getsize(self.name)

  def delete(self) : 
    os.remove("./" + self.name)
    if os.path.isfile(self.name) : 
      return False
    return True

  def list(self) : 
    return os.listdir()

  def listFiles(self) : 
    sq = os.listdir()
    res = []
    for x in sq : 
      res.append(OclFile.newOclFile(x))
    return res

  def mkdir(self) : 
    try: 
      os.mkdir(self.name)
    except OSError as error: 
      return False
    return True

  def print(self, s) :
    if self.name == "System.out" or self.name == "System.err" : 
      print(str(s), end="")
    else : 
      self.write(s)

  def println(self, s) :
    if self.name == "System.out" or self.name == "System.err" : 
      print(str(s))
    else : 
      self.writeln(s)

  def printf(self, f, sq) :
    print(f % tuple(sq), end="")

  def write(self, s) :
    if self.actualFile != None : 
      self.actualFile.write(s)

  def writeObject(self, s) :
    if self.actualFile != None : 
      pickle.dump(s,self.actualFile)

  def writeln(self, s) :
    if self.actualFile != None : 
      self.actualFile.write(s + "\n")

  def flush(self) :
    if self.actualFile != None : 
      self.actualFile.flush()

  def hasNext(self) : 
    if self.name == "System.in" :
      if self.eof == True : 
        return False 
      try : 
        s = input("")
        self.lastRead = s
        return True
      except EOFError : 
        self.eof = True
        self.lastRead = None
        return False
    return False 
    

  def read(self) :
    if self.actualFile != None :
      try :  
        s = self.actualFile.read(1)
        self.lastRead = s
        return s
      except EOFError : 
        self.eof = True
        self.lastRead = None
        return None
    if self.name == "System.in" : 
      if self.lastRead != None : 
        return self.lastRead
      try : 
        s = input("")
        self.lastRead = s
        return s
      except EOFError : 
        self.eof = True
        self.lastRead = None
        return None
    return ""

  def getCurrent(self) : 
    return self.lastRead
      
  def readObject(self) :
    if self.actualFile != None : 
      return pickle.load(self.actualFile)
    return None

  def readLine(self) :
    if self.actualFile != None : 
      return self.actualFile.readline()
    if self.name == "System.in" : 
      s = input("")
      return s
    return ""

  def readAll(self) :
    if self.actualFile != None : 
      return self.actualFile.read()
    return ""

  def mark(self) : 
    if self.actualFile != None : 
      self.position = self.actualFile.tell()
    else : 
      self.position = 0

  def reset(self) : 
    if self.actualFile != None : 
      self.actualFile.seek(self.position)
    
  def skipBytes(self,n) : 
    if self.actualFile != None : 
      self.position = self.actualFile.tell()
      self.position = self.position + n
      self.actualFile.seek(self.position)


  def closeFile(self) : 
    if self.actualFile != None : 
      self.actualFile.close()
      self.actualFile = None


  def killOclFile(oclfile_x) :
    oclfile_instances = ocl.excludingSet(oclfile_instances, oclfile_x)
    free(oclfile_x)

def createOclFile():
  oclfile = OclFile()
  return oclfile

def allInstances_OclFile():
  return OclFile.oclfile_instances

def getOclFileByPK(_ex) :
  if (_ex in OclFile.oclfile_index) :
    return OclFile.oclfile_index[_ex]
  else :
    return None


def getOclFileByPKs(_exs) :
  result = []
  for _ex in _exs :
    if (_ex in OclFile.oclfile_index) :
      result.append(OclFile.oclfile_index[_ex])
  return result


def createByPKOclFile(_value):
  result = getOclFileByPK(_value)
  if (result != None) :
    return result
  else :
    result = OclFile()
    result.name = _value
    OclFile.oclfile_index[_value] = result
    return result


# f1 = OclFile.newOclFile("./subdir")
# f2 = OclFile.newOclFile_Read(f1)
# print(f2.length())
# print(f2.isFile())
# print(f2.isDirectory())
# print(f2.lastModified())
# print(f2.isAbsolute())

# f2.closeFile()
# print(f1.mkdir())

# print(f2.canRead())

# f = OclFile.newOclFile("f.txt")
# print(f.getAbsolutePath())
# print(f.getParent())
# d = f.getParentFile()
# print(d.getName())
# print(f.getPath())


# f = open("w.txt", 'w')
# f.write("some text")
# f.write("more text")
# f.flush()
# f.close()

System_in = OclFile.newOclFile("System.in")
System_out = OclFile.newOclFile("System.out")
System_err = OclFile.newOclFile("System.err")

# f = OclFile.newOclFile("obj.dat")
# fx = OclFile.newOclFile_ReadB(f)

# class Person : 
#   def __init__(self,nme) : 
#     self.name = nme

# p1 = Person("Hamlet")
# p2 = Person("Horatio")

# p1 = fx.readObject()
# p2 = fx.readObject()
# fx.closeFile()

# print(p1.name)
# print(p2.name)

# System_out.printf("%d %s", [120, "degrees"])

# at end of file - go to end of file, find the 
# position there, return to current position. 
# EOF if they are the same. 
# pos = fh.tell()
# fh.seek(0, 2)
# file_size = fh.tell()
# if pos == file_size : 
#   return True
# fh.seek(pos)
# return False

# sq = []
# while System_in.hasNext() : 
#   x = System_in.getCurrent()
#   sq.append(x)
# print(sq)


