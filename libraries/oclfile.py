import ocl
import math
import os
import pickle
import socket
import tempfile

from enum import Enum


def free(x):
  del x


class OclFile:
  oclfile_instances = []
  oclfile_index = dict({})

  def __init__(self):
    self.name = ""
    self.position = 0
    self.markedPosition = 0
    self.actualFile = None
    self.delegate = None
    self.lastRead = None
    self.remote = False
    self.port = 0
    self.eof = False
    self.readable = False
    self.writable = False
    self.bufferSize = 4096
    OclFile.oclfile_instances.append(self)

  def newOclFile(nme) : 
    f = createByPKOclFile(nme)
    return f

  def newOclFile_Remote(nme,portNumber) : 
    f = createByPKOclFile(nme)
    f.port = portNumber
    f.remote = True
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

  def getInetAddress(self) : 
    return self.name

  def getLocalAddress(self) : 
    return self.name

  def getPort(self) :
    return self.port

  def getLocalPort() : 
    return self.port

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

  def isReadOnly(self) : 
    if self.actualFile == None : 
      return False
    if self.actualFile.writable() : 
      return False
    return self.actualFile.readable()


  def openWrite(self) : 
    if self.name == "System.out" or self.name == "System.err" : 
      pass 
    elif self.remote or self.delegate != None : 
      pass
    else : 
      try : 
        self.actualFile = open(self.name, 'r+')
        self.writable = True
      except : 
        self.actualFile = open(self.name, 'w+')
        self.writable = True
    self.position = 0

  def openRead(self) : 
    if self.name == "System.in" : 
      pass
    elif self.remote or self.delegate != None : 
      pass
    else : 
      try : 
        self.actualFile = open(self.name, 'r')
        self.readable = True
      except :
        pass 
    self.position = 0

  def openWriteB(self) : 
    if self.name == "System.out" or self.name == "System.err" or self.delegate != None: 
      pass
    elif self.remote : 
      self.actualFile = socket.create_connection((self.name,self.port)) 
    else : 
      self.actualFile = open(self.name, 'r+b')
    self.position = 0

  def openReadB(self) : 
    if self.name == "System.in" or self.delegate != None : 
      pass
    elif self.remote : 
      self.actualFile = socket.create_connection((self.name,self.port)) 
    else : 
      self.actualFile = open(self.name, 'rb')
    self.position = 0

  def getInputStream(self) : 
    self.openReadB()
    return self

  def getOutputStream(self) : 
    self.openWriteB()
    return self

  def createTemporaryFile(nme,ext) : 
    (fle,pth) = tempfile.mkstemp("." + ext, nme)
    if fle != None : 
      res = createByPKOclFile(str(pth))
      res.actualFile = fle
      return res


  def setPort(self,portNumber) : 
    self.port = portNumber

  def exists(self) : 
    return os.path.isfile(self.name)

  def isFile(self) : 
    return os.path.isfile(self.name)

  def isDirectory(self) : 
    return os.path.isdir(self.name)

  def isHidden(self) : 
    return len(self.name) > 0 and self.name[0] == '.'

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
    return 1000*int(os.path.getmtime(self.name))

  def length(self) :
    if self.remote or self.delegate != None : 
      return 0 
    return os.path.getsize(self.name)

  def delete(self) : 
    if self.remote or self.delegate != None : 
      return False
    os.remove("./" + self.name)
    self.readable = False
    self.writable = False
    if os.path.isfile(self.name) : 
      return False
    return True

  def deleteFile(nme) : 
    os.remove("./" + nme)
    if os.path.isfile(nme) : 
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

  def renameFile(oldnme, newnme) : 
    try: 
      os.rename(oldnme,newnme)
    except OSError as error: 
      return False
    return True

  def mkdir(self) : 
    try: 
      os.mkdir(self.name)
    except OSError as error: 
      return False
    return True

  def mkdirs(self) : 
    try: 
      os.makedirs(self.name)
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

  def writeAllLines(self, sq) : 
    if self.name == "System.out" or self.name == "System.err" : 
      for x in sq : 
        print(str(x))
    else : 
      for x in sq : 
        self.writeln(x)
    

  def printf(self, f, sq) :
    if self.name == "System.out" or self.name == "System.err" :
      print(f % tuple(sq), end="")
    else : 
      self.write(f % tuple(sq))

  def write(self, s) :
    if self.delegate != None : 
      self.delegate.write(s)
      return
    if self.actualFile != None : 
      if self.remote : 
        self.actualFile.send(s)
      else : 
        self.actualFile.write(s)

  def append(self, s) :
    if self.delegate != None :
      self.delegate.write(s)
      return self 
    if self.actualFile != None : 
      self.actualFile.write(s)
      return self

  def writeN(self, sq, n) :
    if self.actualFile != None :
      for i in range(0,n) : 
        if i < len(sq) : 
          s = sq[i] 
          if self.remote : 
            self.actualFile.send(s)
          else : 
            self.actualFile.write(s)
 
  def writeByte(self, x) :
    s = ocl.byte2char(x) 
    if self.actualFile != None : 
      if self.remote : 
        self.actualFile.send(s)
      else : 
        self.actualFile.write(s)
 
  def writeNbytes(self, sq, n) :
     if self.actualFile != None :
      for i in range(0,n) : 
        if i < len(sq) : 
          x = sq[i]
          s = ocl.byte2char(x) 
          if self.remote : 
            self.actualFile.send(s)
          else : 
            self.actualFile.write(s)

  def writeObject(self, s) :
    if self.actualFile != None : 
      if self.remote : 
        self.actualFile.send(s)
      else : 
        pickle.dump(s,self.actualFile)

  def writeMap(self, s) :
    if self.actualFile != None : 
      pickle.dump(s,self.actualFile)

  def writeln(self, s) :
    if self.delegate != None : 
      self.delegate.write(s + "\n")
      return
    if self.actualFile != None :
      if self.remote : 
        self.actualFile.send(s + "\n")
      else :  
        self.actualFile.write(s + "\n")

  def flush(self) :
    if self.remote or self.delegate != None : 
      return
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
      except : 
        self.eof = True
        self.lastRead = None
        return False

    if self.actualFile != None :
      if self.eof == True : 
        return False 
      try :  
        s = self.actualFile.read(1)
        if s == '' : 
          self.eof = True
          self.lastRead = None
          return False
        self.position = self.position + 1
        while s.isspace() : 
          try : 
            s = self.actualFile.read(1)
            if s == '' : 
              self.eof = True
              self.lastRead = None
              return False
            self.position = self.position + 1
          except : 
            self.eof = True
            self.lastRead = None
            return False
 
        self.lastRead = ""
 
        while not(s.isspace()) : 
          try : 
            self.lastRead = self.lastRead + s
            s = self.actualFile.read(1)
            if s == '' : 
              self.eof = True
              return True
            self.position = self.position + 1
          except : 
            self.eof = True
            return True

        return True
      except : 
        self.eof = True
        self.lastRead = None
        return False
    return False 

  def hasNextLine(self) : 
    if self.name == "System.in" :
      if self.eof == True : 
        return False 
      try : 
        s = input("")
        self.lastRead = s
        return True
      except : 
        self.eof = True
        self.lastRead = None
        return False
    return False 

  def read(self) :
    if self.delegate != None : 
      s = self.delegate.read()
      return s
    if self.remote : 
      if self.actualFile != None : 
        s = self.actualFile.recv(self.bufferSize)
      return s
    if self.actualFile != None :
      try :  
        s = self.actualFile.read(1)
        if s == '' : 
          self.eof = True
          self.lastRead = None
          return s
        self.position = self.position + 1
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

  def readN(self,n) : 
    res = []
    if self.delegate != None : 
      s = self.delegate.read(n)
      for x in s :
        res.append(ocl.byte2char(x))
      return res
    if self.remote : 
      if self.actualFile != None : 
        s = self.actualFile.recv(n)
      return ocl.characters(s)
    if self.actualFile != None :
      ind = 0
      maxlen = self.length()
      while ind < n and self.position <= maxlen and self.eof == False : 
        try : 
          s = self.actualFile.read(1)
          self.lastRead = s
          if len(s) == 0 : 
            self.eof = True
            return res
          self.position = self.position + 1
          res.append(s)
          ind = ind + 1
        except Error : 
          self.eof = True
          self.lastRead = None
          return res
      return res
    return []

  def readByte(self) : 
    s = self.read()
    return ocl.char2byte(s)

  def readNbytes(self,n) : 
    s = self.readN(n)
    res = []
    for x in s :
      res.append(ocl.char2byte(x))
    return res

  def readAllBytes(self) : 
    maxlen = self.length()
    return self.readNbytes(maxlen)

  def getCurrent(self) : 
    return self.lastRead
      
  def readObject(self) :
    if self.remote : 
      if self.actualFile != None : 
        s = self.actualFile.recv(self.bufferSize)
      return s
    if self.actualFile != None : 
      return pickle.load(self.actualFile)
    return None

  def readMap(self) :
    if self.actualFile != None : 
      return pickle.load(self.actualFile)
    return None

  def readLine(self) :
    if self.delegate != None : 
      s = self.delegate.read(128)
      if len(s) == 0 : 
        self.eof = True
      return s
    if self.remote : 
      if self.actualFile != None : 
        s = self.actualFile.recv(self.bufferSize)
        if len(s) == 0 : 
          self.eof = True
        return s
    if self.actualFile != None : 
      ss = self.actualFile.readline()
      self.position = self.position + len(ss)
      if len(ss) == 0 : 
        self.eof = True
      return ss
    if self.name == "System.in" : 
      s = input("")
      return s
    self.eof = True
    return ""

  def readAll(self) :
    if self.delegate != None : 
      s = self.delegate.read()
      return s
    if self.remote : 
      if self.actualFile != None : 
        s = self.actualFile.recv(self.bufferSize)
      return s
    if self.actualFile != None : 
      return self.actualFile.read()
    return ""

  def readAllLines(self) :
    if self.remote : 
      return []
    if self.actualFile != None : 
      lns = []
      for line in self.actualFile :
        lns.append(line)
      return lns
    return []

  def copyFromTo(self,target) :
    if self.remote : 
      return
    if self.actualFile != None and target.actualFile != None : 
      for line in self.actualFile :
        target.write(line)
    
  def lineCount(self) :
    if self.remote : 
      return 0
    if self.actualFile != None :
      cnt = 0
      for line in self.actualFile :
        cnt = cnt + 1
      return cnt
    return 0

  def mark(self) :
    if self.remote : 
      return  
    if self.actualFile != None : 
      self.markedPosition = self.actualFile.tell()
    else : 
      self.markedPosition = 0

  def reset(self) :
    if self.remote : 
      return 
    if self.actualFile != None : 
      self.actualFile.seek(self.markedPosition)

  def getPosition(self) : 
    return self.position
    
  def skipBytes(self,n) : 
    if self.remote : 
      return
    if self.actualFile != None : 
      self.position = self.actualFile.tell()
      self.position = self.position + n
      self.actualFile.seek(self.position)

  def setPosition(self,n) : 
    if self.remote : 
      return
    if self.actualFile != None : 
      self.position = n
      self.actualFile.seek(n)

  def getEof(self) : 
    return self.eof

  def closeFile(self) :
    self.delegate = None 
    if self.actualFile != None : 
      self.actualFile.close()
      self.readable = False
      self.writable = False
      self.actualFile = None
      self.lastRead = None

  def isOpen(self) : 
    if self.actualFile == None : 
      return False
    return not(self.actualFile.closed)

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



System_in = OclFile.newOclFile("System.in")
System_out = OclFile.newOclFile("System.out")
System_err = OclFile.newOclFile("System.err")


# d = OclFile.newOclFile(".")
# dirfiles = d.listFiles()
# for f in dirfiles : 
#   f.openRead()
#   print(f.getName() + " " + str(f.lineCount()))

