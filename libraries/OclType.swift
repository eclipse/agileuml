import Foundation
import Darwin

class OclAttribute
{ private static var instance : OclAttribute? = nil

  init() { }

  init(copyFrom: OclAttribute) {
    self.name = copyFrom.name
    self.type = copyFrom.type
  }

  func copy() -> OclAttribute
  { let res : OclAttribute = OclAttribute(copyFrom: self)
    addOclAttribute(instance: res)
    return res
  }

  static func defaultInstance() -> OclAttribute
  { if (instance == nil)
    { instance = createOclAttribute() }
    return instance!
  }

  deinit
  { killOclAttribute(obj: self) }

  var name : String = ""
  var type : OclType = OclType.defaultInstance()

  func getName() -> String
  {
    return name
  }


  func getType() -> OclType
  {
    return type
  }

}


var OclAttribute_allInstances : [OclAttribute] = [OclAttribute]()

func createOclAttribute() -> OclAttribute
{ let result : OclAttribute = OclAttribute()
  OclAttribute_allInstances.append(result)
  return result
}

func addOclAttribute(instance : OclAttribute)
{ OclAttribute_allInstances.append(instance) }

func killOclAttribute(obj: OclAttribute)
{ OclAttribute_allInstances = OclAttribute_allInstances.filter{ $0 !== obj } }


class OclOperation
{ private static var instance : OclOperation? = nil

  init() { }

  init(copyFrom: OclOperation) {
    self.name = copyFrom.name
    self.type = copyFrom.type
    self.parameters = Ocl.copySequence(s: copyFrom.parameters)
  }

  func copy() -> OclOperation
  { let res : OclOperation = OclOperation(copyFrom: self)
    addOclOperation(instance: res)
    return res
  }

  static func defaultInstance() -> OclOperation
  { if (instance == nil)
    { instance = createOclOperation() }
    return instance!
  }

  deinit
  { killOclOperation(obj: self) }

  var name : String = ""
  var type : OclType = OclType.defaultInstance()
  var parameters : [OclAttribute] = [OclAttribute]()

  func getName() -> String
  {
    return name
  }


  func getType() -> OclType
  {
    return type
  }

  func getReturnType() -> OclType
  {
    return type
  }

  func addParameter(par: OclAttribute) 
  { parameters.append(par) } 

  func getParameters() -> [OclAttribute]
  { return parameters } 

}


var OclOperation_allInstances : [OclOperation] = [OclOperation]()

func createOclOperation() -> OclOperation
{ let result : OclOperation = OclOperation()
  OclOperation_allInstances.append(result)
  return result
}

func addOclOperation(instance : OclOperation)
{ OclOperation_allInstances.append(instance) }

func killOclOperation(obj: OclOperation)
{ OclOperation_allInstances = OclOperation_allInstances.filter{ $0 !== obj } }



class OclType
{ private static var instance : OclType? = nil

  init() { }

  init(copyFrom: OclType) {
    self.name = "copy_" + copyFrom.name
    self.attributes = Ocl.copySequence(s: copyFrom.attributes)
    self.operations = Ocl.copySequence(s: copyFrom.operations)
    self.constructors = Ocl.copySequence(s: copyFrom.constructors)
    self.innerClasses = Ocl.copySequence(s: copyFrom.innerClasses)
    self.componentType = Ocl.copySequence(s: copyFrom.componentType)
    self.superclasses = Ocl.copySequence(s: copyFrom.superclasses)
    self.subclasses = Ocl.copySequence(s: copyFrom.subclasses)
  }

  func copy() -> OclType
  { let res : OclType = OclType(copyFrom: self)
    addOclType(instance: res)
    return res
  }

  static func defaultInstance() -> OclType
  { if (instance == nil)
    { instance = createOclType() }
    return instance!
  }

  deinit
  { killOclType(obj: self) }

  var name : String = "" /* principal key */

  static var OclType_index : Dictionary<String,OclType> = [String:OclType]()

  static func getByPKOclType(index : String) -> OclType?
  { return OclType_index[index] }

  var attributes : [OclAttribute] = []
  var operations : [OclOperation] = []
  var constructors : [OclOperation] = []
  var innerClasses : [OclType] = []
  var componentType : [OclType] = []
  var superclasses : [OclType] = []
  var subclasses : [OclType] = []
  var actualMetatype : Any.Type? = nil

  func getName() -> String
  {
    return name
  }


  func getClasses() -> [OclType]
  { return innerClasses }


  func getDeclaredClasses() -> [OclType]
  {
    var result : [OclType] = []
    result = Ocl.sequenceSubtract(s1: innerClasses, s2: Ocl.unionAll(s: superclasses.map({sc in sc.getClasses()})))
    return result
  }


  func getComponentType() -> OclType
  {
    var result : OclType = OclType.defaultInstance()
    if componentType.count > 0
    {
      result = Ocl.any(s: componentType)!
    }
    else {
      if componentType.count == 0
      {
        result = createByPKOclType(key: "void")
      }
    }
    return result
  }


  func getFields() -> [OclAttribute]
  {
    return attributes
  }


  func getDeclaredField(s : String) -> OclAttribute
  {
    let result : OclAttribute = OclAttribute.defaultInstance()
    let x = Ocl.any(s: Ocl.select(s: attributes, f: { att in att.name == s }))
    if x != nil
    { return x! } 
    return result
  }


  func getField(s : String) -> OclAttribute
  {
    let result : OclAttribute = OclAttribute.defaultInstance()
    let x = Ocl.any(s: Ocl.select(s: attributes, f: { att in att.name == s }))
    if x != nil
    { return x! } 
    return result
  }


  func getDeclaredFields() -> [OclAttribute]
  {
    var result : [OclAttribute] = []
    result = Ocl.sequenceSubtract(s1: attributes, s2: Ocl.unionAll(s: superclasses.map({sc in sc.getFields()})))
    return result
  }


  func getMethods() -> [OclOperation]
  {
    return operations
  }


  func getDeclaredMethods() -> [OclOperation]
  {
    var result : [OclOperation] = []
    result = Ocl.sequenceSubtract(s1: operations, s2: Ocl.unionAll(s: superclasses.map({sc in sc.getMethods()})))
    return result
  }


  func getConstructors() -> [OclOperation]
  {
    return constructors
  }

  func addSuperclass(sup: OclType) 
  { superclasses.append(sup)
    sup.subclasses.append(self)
  } 

  func getSuperclass() -> OclType
  {
    var result : OclType? = nil
    if superclasses.count == 0
    { result = createByPKOclType(key: "OclAny")
      return result!
    } 
    result = Ocl.any(s: superclasses)
    return result!
  }

  static func hasAttribute(obj: Any, att: String) -> Bool
  { let m = Mirror(reflecting: obj)
    for e in m.children { 
      if e.label == att 
      { return true } 
    } 
    return false
  } 

  static func getAttributeValue(obj: Any, att: String) -> Any?
  { let m = Mirror(reflecting: obj)
    for e in m.children { 
      if e.label == att 
      { return e.value } 
    } 
    return nil
  } 

  func isArray() -> Bool
  { if name == "Sequence" 
    { return true } 
    return false
  } 

  func isPrimitive() -> Bool
  { if name == "int" || name == "long" || name == "double" || name == "boolean"  
    { return true } 
    return false
  } 

  func isAssignableFrom(c : OclType) -> Bool
  { if c.name == self.name 
    { return true } 
    for s in c.superclasses { 
      if isAssignableFrom(c: s)
      { return true } 
    } 
    return false
  } 

  func isInstance(obj : Any) -> Bool
  { if actualMetatype == type(of: obj)
    { return true } 
    for s in subclasses { 
      if s.isInstance(obj: obj)
      { return true } 
    } 
    return false
  } 

}


var OclType_allInstances : [OclType] = [OclType]()

func createOclType() -> OclType
{ let result : OclType = OclType()
  OclType_allInstances.append(result)
  return result
}

func addOclType(instance : OclType)
{ OclType_allInstances.append(instance) }

func killOclType(obj: OclType)
{ OclType_allInstances = OclType_allInstances.filter{ $0 !== obj } }

func createByPKOclType(key : String) -> OclType
{ var result : OclType? = OclType.getByPKOclType(index: key)
  if result != nil { return result! } 
  result = OclType()
  OclType_allInstances.append(result!)
  OclType.OclType_index[key] = result!
  result!.name = key
  return result! }

func killOclType(key : String)
{ OclType.OclType_index[key] = nil
  OclType_allInstances.removeAll(where: { $0.name == key })
}


