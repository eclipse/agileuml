import Foundation
import Darwin

class OclComparator
{ private static var instance : OclComparator? = nil

  init() { }

  init(copyFrom: OclComparator) {
  }

  func copy() -> OclComparator
  { let res : OclComparator = OclComparator(copyFrom: self)
    addOclComparator(instance: res)
    return res
  }

  static func defaultInstanceOclComparator() -> OclComparator
  { if (instance == nil)
    { instance = createOclComparator() }
    return instance!
  }

  deinit
  { killOclComparator(obj: self) }


  func compare<T: Comparable>(lhs : T, rhs : T) -> Int
  {
    var result : Int = 0
    result = (((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0)))
    return result

  }

  static func lowerSegment<T : Comparable>(col : [T], x : T, cmp : OclComparator) -> [T]
  {
    var result : [T] = []
    result = Ocl.select(s: col, f: { y in cmp.compare(lhs: y, rhs: x) < 0 })
    return result
  }
  
  static func binarySearch<T : Comparable>(col : [T], x : T, cmp : OclComparator) -> Int
  {
    var result : Int = 0
    result = OclComparator.lowerSegment(col: col, x: x, cmp: cmp).count
    return result
  }

  static func sortWith<T : Comparable>(col : [T], cmp : OclComparator) -> [T]
  {
    var result : [T] = []
    result = Ocl.sortedBy(s: col, f: { x in  OclComparator.lowerSegment(col: col, x: x, cmp: cmp).count })
    return result
  }

  static func maximumWith<T : Comparable>(col : [T], cmp : OclComparator) -> T
  { 
    for x in col { 
      if Ocl.forAll(s: col, f: { y in cmp.compare(lhs: y, rhs: x) <= 0 })
      { return x }
    }
    return Ocl.any(s: col)!
  }


  static func minimumWith<T : Comparable>(col : [T], cmp : OclComparator) -> T
  { for x in col { 
      if Ocl.forAll(s: col, f: { y in cmp.compare(lhs: y, rhs: x) >= 0 })
      { return x }
    }
    return Ocl.any(s: col)!
  }

} 

var OclComparator_allInstances : [OclComparator] = [OclComparator]()

func createOclComparator() -> OclComparator
{ let result : OclComparator = OclComparator()
  OclComparator_allInstances.append(result)
  return result
}

func addOclComparator(instance : OclComparator)
{ OclComparator_allInstances.append(instance) }

func killOclComparator(obj: OclComparator)
{ OclComparator_allInstances = OclComparator_allInstances.filter{ $0 !== obj } }


