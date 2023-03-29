import Foundation
import Darwin

class OclIteratorResult { 
  var done : Bool = true
  var value : Any? = nil
  
  static func newOclIteratorResult(v : Any?) -> OclIteratorResult
  { let res = OclIteratorResult()
    if v == nil { 
      res.done = true 
      res.value = v
    } else {
      res.done = false
      res.value = v!
    }
    return res
  } 
}

class OclIterator
{ static var instance : OclIterator? = nil

  var position : Int = 0
  var markedPosition : Int = 0
  var elements : [Any] = []
  var generatorFunction : (Int) -> Any? = { (i : Int) -> Any? in nil }
  var columnNames : [String] = [String]()

  init() { }

  init(copyFrom: OclIterator) {
    self.position = copyFrom.position
    self.markedPosition = copyFrom.markedPosition
    self.generatorFunction = copyFrom.generatorFunction
    self.elements = Ocl.copySequence(s: copyFrom.elements)
    self.columnNames = Ocl.copySequence(s: copyFrom.columnNames)
  }

  func copy() -> OclIterator
  { let res : OclIterator = OclIterator(copyFrom: self)
    OclIterator_allInstances.append(res)
    return res
  }

  static func defaultInstance() -> OclIterator
  { if (instance == nil)
    { instance = createOclIterator() }
    return instance!
  }


  func hasNext() -> Bool
  {
    var result : Bool = false
    if position >= 0 && position < elements.count
    {
      result = true
    }
    else {
      result = false
    }
    return result

  }


  func hasPrevious() -> Bool
  {
    var result : Bool = false
    if position > 1 && position <= elements.count + 1
    {
      result = true
    }
    else {
      result = false
    }
    return result

  }

  func isAfterLast() -> Bool
  {
    if position > elements.count
    {
      return true
    }
    return false
  }

  func isBeforeFirst() -> Bool
  {
    if position <= 0
    {
      return true
    }
    return false
  }

  func nextIndex() -> Int
  {
    var result : Int = 0
    result = position + 1
    return result
  }


  func previousIndex() -> Int
  {
    var result : Int = 0
    result = position - 1
    return result
  }


  func moveForward() -> Void
  {
    position = position + 1
  }


  func moveBackward() -> Void
  {
    position = position - 1
  }


  func moveTo(i : Int) -> Void
  {
    position = i
  }

  func moveToFirst()
  {
    position = 1
  }

  func moveToLast()
  {
    position = elements.count
  }
  
  func moveToStart()
  {
    position = 0
  }

  func moveToEnd()
  {
    position = elements.count + 1
  }
  
  func getPosition() -> Int
  { return position }
  
  func setPosition(i : Int)
  { position = i }
 
  func markPosition()
  { markedPosition = position }

  func moveToMarkedPosition()
  { position = markedPosition }
  
  func movePosition(i : Int)
  { position = position + i } 

  static func newOclIterator_Sequence(sq : [Any]) -> OclIterator
  {
    let ot : OclIterator = createOclIterator()
    ot.elements = sq
    ot.position = 0
    ot.markedPosition = 0
    return ot
  }


  static func newOclIterator_Set<T : Hashable>(st : Set<T>) -> OclIterator
  {
    let ot : OclIterator = createOclIterator()
    ot.elements = Ocl.toSequence(s: st)
    ot.position = 0
    ot.markedPosition = 0
    return ot
  }

  static func newOclIterator_String(ss : String) -> OclIterator
  { let ot : OclIterator = createOclIterator()
    ot.elements = Ocl.split(str: ss, pattern: "[ \n\t\r]+")
    ot.position = 0
    ot.markedPosition = 0
    return ot
  }


  static func newOclIterator_String_String(ss : String, seps : String) -> OclIterator
  { let ot : OclIterator = createOclIterator()
    ot.elements = Ocl.split(str: ss, pattern: "[" + seps + "]+")
    ot.position = 0
    ot.markedPosition = 0
    return ot
  }

  static func newOclIterator_Function(f : @escaping (Int) -> Any?) -> OclIterator
  {
    let ot : OclIterator = createOclIterator()
    ot.elements = []
    ot.position = 0
    ot.markedPosition = 0
    ot.generatorFunction = f
    return ot
  }

  func getCurrent() -> Any
  {
    var result : Any? = nil
    if position > 0 && position <= elements.count 
    { result = elements[position - 1]
      return result!
    } 
    return Double.nan
  }


  func set(x : Any) -> Void
  {
    elements[position-1] = x
  }


  func insert(x : Any) -> Void
  { elements.insert(x, at: position-1) }


  func remove() -> Void
  { elements.remove(at: position) }

  func nextResult() -> OclIteratorResult
  { let v = generatorFunction(position)
    position = position + 1
    if v == nil 
    { return OclIteratorResult.newOclIteratorResult(v: v) } 
    
    if position <= elements.count 
    { set(x: v!) } 
    else
    { elements.append(v!) }
      
    return OclIteratorResult.newOclIteratorResult(v: v)
  } 

  func next() -> Any
  {
    moveForward()
    return getCurrent()
  } 
  /* Signal end of iteration with Double.nan */ 


  func previous() -> Any
  {
    moveBackward()
    return getCurrent()
  }

  func at(i : Int) -> Any
  {
    var result : Any? = nil
    result = elements[i - 1]
    return result!
  }

  func length() -> Int
  { return elements.count }

  func getColumnCount() -> Int
  { return columnNames.count }

  func getColumnName(i : Int) -> String
  { return columnNames[i-1] } 

  func getCurrentFieldByIndex(i : Int) -> Any
  { let m = getCurrent() as! [String:Any]
    return m[columnNames[i-1]]!
  }  

  func setCurrentFieldByIndex(i : Int, v : Any)
  { var m = getCurrent() as! [String:Any]
    m[columnNames[i-1]] = v
  }

  func close()
  { elements = []
    position = 0
    columnNames = []
    markedPosition = 0
  } 
}

var OclIterator_allInstances : [OclIterator] = [OclIterator]()

func createOclIterator() -> OclIterator
{ let result : OclIterator = OclIterator()
  OclIterator_allInstances.append(result)
  return result
}

func killOclIterator(obj: OclIterator)
{ OclIterator_allInstances = OclIterator_allInstances.filter{ $0 !== obj } }


