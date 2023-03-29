import ocl
import socket

# import math
# import re
# import copy

from oclfile import *
from ocltype import *
from ocliterator import *
from ocldate import *

import urllib
from urllib import *
from urllib.parse import *
from urllib.request import *

import http
from http import *
from http.client import *

from enum import Enum

import sqlite3


def free(x):
  del x


class SQLTypes(Enum) :
  sqlARRAY = 1
  sqlBIGINT = 2
  sqlBINARY = 3
  sqlBIT = 4
  sqlBLOB = 5
  sqlBOOLEAN = 6
  sqlCHAR = 7
  sqlCLOB = 8
  sqlDATALINK = 9
  sqlDATE = 10
  sqlDECIMAL = 11
  sqlDISTINCT = 12
  sqlDOUBLE = 13
  sqlFLOAT = 14
  sqlINTEGER = 15
  sqlJAVA_OBJECT = 16
  sqlLONGNVARCHAR = 17
  sqlLONGVARBINARY = 18
  sqlNCHAR = 19
  sqlNCLOB = 20
  sqlNULL = 21
  sqlNUMERIC = 22
  sqlNVARCHAR = 23
  sqlOTHER = 24
  sqlREAL = 25
  sqlREF = 26
  sqlREF_CURSOR = 27
  sqlROWID = 28
  sqlSMALLINT = 29
  sqlSQLXML = 30
  sqlSTRUCT = 31
  sqlTIME = 32
  sqlTIME_WITH_TIMEZONE = 33
  sqlTIMESTAMP = 34
  sqlTIMESTAMP_WITH_TIMEZONE = 35
  sqlTINYINT = 36
  sqlVARBINARY = 37
  sqlVARCHAR = 38



class SQLStatement : 
  sqlstatement_instances = []
  sqlstatement_index = dict({})

  def __init__(self):
    self.text = ""
    self.statement = None   # Cursor
    self.connection = None  # Connection
    self.datasource = None  # OclDatasource
    self.resultSet = None   # OclIterator
    self.parameters = dict({}) 
    self.maxfield = 0
    SQLStatement.sqlstatement_instances.append(self)

  def convertRowsToMaps(self) : 
    if self.statement != None : 
      resset = self.statement.fetchall()
      if resset == None : 
        return [] 
      if len(resset) == 0 : 
        return []
      row1 = resset[0]
      keys = row1.keys()
      result = [] 
      for x in resset : 
        record = dict({})
        for k in keys : 
          record[k] = x[k]
        result.append(record)
      return (result,keys)
    return []

  def parameterMapToFields(n,d) :
    if n == 0 : 
      return [] 
    res = [None for x in range(0,n)]
    for x in range(1,n+1) : 
      if x in d : 
        res[x-1] = d[x]
      else : 
        res[x-1] = None
    return res

  def close(self) :
    if self.statement != None : 
      self.statement.close()

  def closeOnCompletion(self) :
    pass

  def setParameters(self, values) :
    for i in range(0,len(values)) : 
      self.parameters[i+1] = values[i]
      if i+1 > self.maxfield : 
        self.maxfield = i+1

  def setObject(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setString(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setInt(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setByte(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setShort(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setBoolean(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setLong(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setDouble(self, field, value) :
    self.parameters[field] = value
    if field > self.maxfield : 
      self.maxfield = field

  def setTimestamp(self, field, value) :
    self.parameters[field] = value.time
    if field > self.maxfield : 
      self.maxfield = field

  def setNull(self, field, value) :
    if field > self.maxfield : 
      self.maxfield = field

  def executeUpdate(self) :
    if self.statement != None :
      pars = SQLStatement.parameterMapToFields(
                     self.maxfield,self.parameters) 
      self.statement.execute(self.text,pars)
      if self.connection != None : 
        self.connection.commit()


  def executeQuery(self, stat=None) :
    result = None
    if self.statement != None : 
      if stat == None : 
        stat = self.text
      pars = SQLStatement.parameterMapToFields(
                     self.maxfield,self.parameters) 
      self.statement.execute(stat,pars)
      (resset,ks) = self.convertRowsToMaps()
      result = OclIterator.newOclIterator_Sequence(resset)
      result.columnNames = ks
      self.resultSet = result
    return result


  def execute(self, stat=None) :
    if self.statement != None :
      if stat == None : 
        stat = self.text
      pars = SQLStatement.parameterMapToFields(
                    self.maxfield,self.parameters)  
      self.statement.execute(stat,pars)
      if self.connection != None : 
        self.connection.commit()

  def cancel(self) :
    if self.statement != None : 
      self.statement.close()

  def getConnection(self) :
    return self.datasource

  def getResultSet(self) :
    return self.resultSet

  def killSQLStatement(sqlstatement_x) :
    sqlstatement_instances = ocl.excludingSet(sqlstatement_instances, sqlstatement_x)
    free(sqlstatement_x)


class OclDatasource : 
  ocldatasource_instances = []
  ocldatasource_index = dict({})

  def __init__(self):
    self.url = ""
    self.protocol = ""
    self.host = ""
    self.ipaddr = []
    self.file = ""
    self.port = ""
    self.name = ""
    self.passwd = ""
    self.schema = ""
    self.con = None
    self.requestMethod = "GET"
    self.request = None
    self.httpConnection = None
    self.clientSocket = None
    self.serverSocket = None
    self.connectionLimit = 0
    OclDatasource.ocldatasource_instances.append(self)

  def getConnection(url,name,passwd) :
    result = None
    db = createOclDatasource()
    db.url = url
    db.name = name
    db.passwd = passwd
    db.con = sqlite3.connect(url)
    db.con.row_factory = sqlite3.Row
    result = db
    return result

  def newOclDatasource() :
    result = None
    db = createOclDatasource()
    result = db
    return result

  def newSocket(host,port) :
    result = None
    db = createOclDatasource()
    db.host = host
    db.port = port
    with socket.socket(
             socket.AF_INET, socket.SOCK_STREAM) as s : 
      s.connect((host,port))
      db.clientSocket = s
      result = db
    return result

  def newServerSocket(port,limit) :
    db = createOclDatasource()
    db.port = port
    db.connectionLimit = limit
    db.serverSocket = socket.create_server(('',port))
    return db

  def accept(self) : 
    if self.serverSocket != None : 
      self.serverSocket.listen(self.connectionLimit)
      (conn,addr) = self.serverSocket.accept()
      ds = createOclDatasource()
      ds.clientSocket = conn
      ds.host = addr
      ds.port = self.port
      return ds
    return self

  def newURL(s) :
    result = None
    db = createOclDatasource()
    db.url = s
    parsedURL = urllib.parse.urlparse(s)
    db.protocol = parsedURL.scheme
    db.host = parsedURL.netloc
    db.port = parsedURL.port
    db.file = parsedURL.path
    result = db
    return result

  def getLocalHost() :
    db = createOclDatasource()
    db.url = "http://127.0.0.1/"
    db.protocol = "http"
    db.host = "localhost"
    db.port = 80
    db.file = ""
    db.ipaddr = [127,0,0,1]
    result = db
    return result

  def newURL_PHF(p,h,f) :
    result = None
    db = createOclDatasource()
    db.protocol = p
    db.host = h
    db.file = f
    mp = (p,h,f,None,None,None)
    db.url = urllib.parse.urlunparse(mp)
    result = db
    return result

  def newURL_PHNF(p,h,n,f) :
    result = None
    db = createOclDatasource()
    db.protocol = p
    db.host = h
    db.port = n
    db.file = f
    mp = (p,h + ':' + str(n),f,None,None,None)
    db.url = urllib.parse.urlunparse(mp)
    result = db
    return result

  def createStatement(self) :
    result = None
    ss = createSQLStatement()
    ss.text = ""
    if self.con != None : 
      ss.statement = self.con.cursor()
      ss.connection = self.con
    ss.datasource = self
    result = ss
    return result

  def prepare(self, stat) :
    result = None
    ss = createSQLStatement()
    ss.text = stat
    if self.con != None : 
      ss.statement = self.con.cursor()
      ss.connection = self.con
    ss.datasource = self
    result = ss
    return result

  def prepareStatement(self, stat) :
    result = None
    ss = createSQLStatement()
    ss.text = stat
    if self.con != None : 
      ss.statement = self.con.cursor()
      ss.connection = self.con
    ss.datasource = self
    result = ss
    return result

  def prepareCall(self, stat) :
    result = None
    ss = createSQLStatement()
    ss.text = stat
    if self.con != None : 
      ss.statement = self.con.cursor()
      ss.connection = self.con
    ss.datasource = self
    result = ss
    return result

  def query_String(self, stat) :
    result = None
    sqlstat = self.createStatement()
    sqlstat.text = stat
    result = sqlstat.executeQuery()
    return result

  def rawQuery(self, stat, pos) :
    result = None
    sqlstat = self.createStatement()
    sqlstat.text = stat
    sqlstat.setParameters(pos)
    result = sqlstat.executeQuery()
    return result

  def nativeSQL(self, stat) :
    result = ""
    sqlstat = self.createStatement()
    sqlstat.text = stat
    result = sqlstat.executeQuery()
    return result

  def query_String_Sequence(self, stat, cols) :
    result = query_String(self,stat)
    return result

  def execSQL(self, stat) :
    sqlstat = self.createStatement()
    sqlstat.text = stat
    sqlstat.execute()
    
  def abort(self) :
    pass

  def close(self) :
    if self.con != None : 
      self.con.close()
    if self.httpConnection != None :
      self.httpConnection.close()
    if self.clientSocket != None : 
      self.clientSocket.close()

  def commit(self) :
    if self.con != None : 
      self.con.commit()

  def rollback(self) :
    pass

  def connect(self) :
    if self.request != None : 
      self.httpConnection = urllib.request.urlopen(
                                       self.request)

  def openConnection(self) :
    self.request = urllib.request.Request(
                self.url,method=self.requestMethod)
    return self

  def setRequestMethod(self, method) :
    self.requestMethod = method

  def setSchema(self, s) :
    self.schema = s

  def getSchema(self) :
    return self.schema

  def getInputStream(self) :
    if self.httpConnection != None : 
      fle = OclFile.newOclFile(self.url)
      fle.delegate = self.httpConnection
      return fle
    if self.clientSocket != None : 
      # new remote file on this host,port
      fle = OclFile.newOclFile_Remote(self.host + ":" + str(self.port),self.port)
      fle.actualFile = self.clientSocket
      return fle
    return None
    
  def getOutputStream(self) :
    if self.httpConnection != None : 
      fle = OclFile.newOclFile(self.url)
      fle.delegate = self.httpConnection
      return fle
    if self.clientSocket != None : 
      # new remote file on this host,port
      fle = OclFile.newOclFile_Remote(self.host + ":" + str(self.port),self.port)
      fle.actualFile = self.clientSocket
      return fle
    return None
    
  def getURL(self) :
    return self.url
    
  def getContent(self) :
    result = None
    if self.httpConnection != None : 
      return self.httpConnection.read()
    return result

  def getFile(self) :
    return self.file
    
  def getHost(self) :
    return self.host

  def getAddress(self) : 
    return self.ipaddr; 
    
  def getPort(self) :
    return self.port
    
  def getProtocol(self) :
    return self.protocol
    
  def killOclDatasource(ocldatasource_x) :
    ocldatasource_instances = ocl.excludingSet(ocldatasource_instances, ocldatasource_x)
    free(ocldatasource_x)

def createSQLStatement():
  sqlstatement = SQLStatement()
  return sqlstatement

def allInstances_SQLStatement():
  return SQLStatement.sqlstatement_instances


sqlstatement_OclType = createByPKOclType("SQLStatement")
sqlstatement_OclType.instance = createSQLStatement()
sqlstatement_OclType.actualMetatype = type(sqlstatement_OclType.instance)


def createOclDatasource():
  ocldatasource = OclDatasource()
  return ocldatasource

def allInstances_OclDatasource():
  return OclDatasource.ocldatasource_instances


ocldatasource_OclType = createByPKOclType("OclDatasource")
ocldatasource_OclType.instance = createOclDatasource()
ocldatasource_OclType.actualMetatype = type(ocldatasource_OclType.instance)


# https://nms.kcl.ac.uk/kevin.lano/

# ds = OclDatasource.newURL_PHF('https', 'nms.kcl.ac.uk', 'kevin.lano/index.html')
# print(ds.url)

# ws = ds.openConnection()
# ws.connect()
## print(ws.getContent())
# fle = ws.getInputStream()
# s = fle.readLine()
# print(s)
# s = fle.readLine()
# print(s)
# s = fle.readLine()
# print(s)

