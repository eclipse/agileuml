import Foundation
import Darwin

class OclRandom
{ static var instance : OclRandom? = nil

  init() { }

  init(copyFrom: OclRandom) {
    self.ix = copyFrom.ix
    self.iy = copyFrom.iy
    self.iz = copyFrom.iz
  }

  func copy() -> OclRandom
  { let res : OclRandom = OclRandom(copyFrom: self)
    addOclRandom(instance: res)
    return res
  }

  static func defaultInstance() -> OclRandom
  { if (instance == nil)
    { instance = createOclRandom() }
    return instance!
  }

  deinit
  { killOclRandom(obj: self) }

  var ix : Int = 0
  var iy : Int = 0
  var iz : Int = 0

  static func newOclRandom() -> OclRandom
  {
    let rd : OclRandom = createOclRandom()
    rd.ix = 1001
    rd.iy = 781
    rd.iz = 913
    return rd
  }


  static func newOclRandom(n : Int64) -> OclRandom
  {
    let rd : OclRandom = createOclRandom()
    rd.ix = Int((n % 30269))
    rd.iy = Int((n % 30307))
    rd.iz = Int((n % 30323))
    return rd
  }


  func setSeeds(x : Int, y : Int, z : Int) -> Void
  {
    self.ix = x
    self.iy = y
    self.iz = z
  }


  func setSeed(n : Int64) -> Void
  {
    self.ix = Int((n % 30269))
    self.iy = Int((n % 30307))
    self.iz = Int((n % 30323))
  }


  func nrandom() -> Double
  {
    self.ix = ( self.ix * 171) % 30269
    self.iy = ( self.iy * 172) % 30307
    self.iz = ( self.iz * 170) % 30323
    return (Double(self.ix)/30269.0 + Double(self.iy)/30307.0 + Double(self.iz)/30323.0)
  }


  func nextDouble() -> Double
  {
    var result : Double = 0.0
    let r : Double = self.nrandom()
    result = (r - Double(Int(floor(_: r))))
    return result
  }


  func nextFloat() -> Double
  {
    var result : Double = 0.0
    let r : Double = self.nrandom()
    result = (r - Double(Int(floor(_: r))))
    return result
  }


  func nextGaussian() -> Double
  {
    var result : Double = 0.0
    let d : Double = self.nrandom()
    result = ((d / 3.0) - 0.5)
    return result
  }


  func nextInt(n : Int) -> Int
  {
    var result : Int = 0
    let d : Double = self.nextDouble()
    result = Int(floor(_: (d * Double(n))))
    return result
  }


  func nextInt() -> Int
  {
    let result : Int = self.nextInt(n: 2147483647)
    return result
  }


  func nextLong() -> Int
  {
    let result : Int = self.nextInt(n: 9223372036854775807)
    return result
  }


  func nextBoolean() -> Bool
  {
    var result : Bool = false
    let d = self.nextDouble()
    if d > 0.5
    {
      result = true
    }
    return result
  }

  static func randomiseSequence(sq : [Any]) -> [Any]
  { var res : [Any] = [] 
    var old : [Any] = [] 
    old = old + sq 
    while old.count > 0
    { let x = old.count 
      if (x == 1)
      { res = res + [old[0]] 
        return res 
      } 
      let n = Int.random(in: 0..<x) 
      let obj = old[n]
      res = res + [obj]  
      old.remove(at: n)
    }
    return res
  }  

  static func randomElement(sq : [Any]) -> Any?
  { let x = sq.count
    if x == 0 
    { return nil } 
    let n = Int.random(in: 0..<x) 
    let obj = sq[n]
    return obj
  } 

  static func randomString(n : Int) -> String
  { 
    let chs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$"
    var res : String = ""
    for _ in 0..<n 
    { let ind = Int.random(in: 0..<54)
      res = res + Ocl.charAt(str: chs, ind: ind)
    } 
    return res
  }
}


var OclRandom_allInstances : [OclRandom] = [OclRandom]()

func createOclRandom() -> OclRandom
{ let result : OclRandom = OclRandom()
  OclRandom_allInstances.append(result)
  return result
}

func addOclRandom(instance : OclRandom)
{ OclRandom_allInstances.append(instance) }

func killOclRandom(obj: OclRandom)
{ OclRandom_allInstances = OclRandom_allInstances.filter{ $0 !== obj } }


