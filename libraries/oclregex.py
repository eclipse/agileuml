import ocl
import math
import re
import copy

from ocltype import * 



class OclRegex : 
  oclregex_instances = []
  oclregex_index = dict({})

  def __init__(self):
    self.regex = ""
    self.text = ""
    self.startpos = 0
    self.endpos = 0
    OclRegex.oclregex_instances.append(self)



  def compile(patt) :
    result = None
    x = createOclRegex()
    x.regex = patt
    x.text = ""
    x.startpos = 0
    x.endpos = 0
    result = x
    return result

  def matcher(self, st) :
    result = None
    self.text = st
    self.startpos = 0
    self.endpos = 0
    result = self
    return result

  def matches(patt,st) :
    result = False
    result = ocl.isMatch(st, patt)
    return result

  def isMatch(self) :
    result = False
    result = ocl.isMatch(self.text, self.regex)
    return result

  def allMatches(self, tx) :
    result = []
    result = ocl.allMatches(tx, self.regex)
    return result

  def find(self) :
    result = False
    txt = ""
    txt = self.text[(self.endpos + 1-1):len(self.text)]
    if ocl.hasMatch(txt, self.regex) :
      f = ""
      f = ocl.firstMatch(txt, self.regex)
      self.startpos = self.endpos + (txt.find(f) + 1)
      self.endpos = self.startpos + len(f) - 1
      return True
    else :
      return False

  # same as find
  def findNext(self,tx=None) :
    result = False
    if tx <> None : 
      self.text = tx
    txt = self.text[(self.endpos + 1-1):len(self.text)]
    if ocl.hasMatch(txt, self.regex) :
      f = ""
      f = ocl.firstMatch(txt, self.regex)
      self.startpos = self.endpos + (txt.find(f) + 1)
      self.endpos = self.startpos + len(f) - 1
      return True
    else :
      return False

  def killOclRegex(oclregex_x) :
    oclregex_instances = ocl.excludingSet(oclregex_instances, oclregex_x)
    free(oclregex_x)

def createOclRegex():
  oclregex = OclRegex()
  return oclregex

def allInstances_OclRegex():
  return OclRegex.oclregex_instances


oclregex_OclType = createByPKOclType("OclRegex")
oclregex_OclType.instance = createOclRegex()
oclregex_OclType.actualMetatype = type(oclregex_OclType.instance)

