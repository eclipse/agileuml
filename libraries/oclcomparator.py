import ocl
from ocltype import *

def free(x):
  del x



class OclComparator : 
  oclcomparator_instances = []
  oclcomparator_index = dict({})

  def __init__(self):
    OclComparator.oclcomparator_instances.append(self)



  def compare(self, lhs, rhs) :
    result = ocl.compareTo(lhs,rhs)
    return result

  def lowerSegment(col,x,cmp) :
    result = [y for y in col if cmp.compare(y, x) < 0]
    return result

  def sortWith(col,cmp) :
    result = sorted(col, key = lambda x : len(OclComparator.lowerSegment(col,x,cmp)))
    return result

  def maximumWith(col,cmp) :
    result = ocl.any([x for x in col if ocl.forAll(col, lambda y : cmp.compare(y, x) <= 0)])
    return result

  def minimumWith(col,cmp) :
    result = ocl.any([x for x in col if ocl.forAll(col, lambda y : cmp.compare(y, x) >= 0)])
    return result

  def binarySearch(col,x,cmp) :
    sq = OclComparator.lowerSegment(col,x,cmp)
    return len(sq)

  def killOclComparator(oclcomparator_x) :
    oclcomparator_instances = ocl.excludingSet(oclcomparator_instances, oclcomparator_x)
    free(oclcomparator_x)

def createOclComparator():
  oclcomparator = OclComparator()
  return oclcomparator

def allInstances_OclComparator():
  return OclComparator.oclcomparator_instances


oclcomparator_OclType = createByPKOclType("OclComparator")
oclcomparator_OclType.instance = createOclComparator()
oclcomparator_OclType.actualMetatype = type(oclcomparator_OclType.instance)


# cmp = OclComparator()
# sq = ["aa", "bb", "kk", "cc", "aa"]
# res = OclComparator.lowerSegment(sq,"cc",cmp)
# print(res)
# rr = OclComparator.sortWith(sq,cmp)
# print(rr)
# print(OclComparator.binarySearch(sq,"dd",cmp))
