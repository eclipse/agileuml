import Foundation
import Darwin

class MathLib
{ static var instance : MathLib? = nil

  init() { }

  init(copyFrom: MathLib) {
  }

  func copy() -> MathLib
  { let res : MathLib = MathLib(copyFrom: self)
    return res
  }

  static func defaultInstance() -> MathLib
  { if (instance == nil)
    { instance = createMathLib() }
    return instance!
  }

  deinit
  { killMathLib(obj: self) }

  static var ix : Int = 0
  static var iy : Int = 0
  static var iz : Int = 0
  static var hexdigit : [String] = []

  static func initialiseMathLib() -> Void
  {
    MathLib.hexdigit = ["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"]
    MathLib.setSeeds(x: 1001, y: 781, z: 913)

  }


  static func pi() -> Double
  {
    var result : Double = 0.0
    result = 3.14159265
    return result

  }


  static func e() -> Double
  {
    var result : Double = 0.0
    result = exp(_: 1)
    return result

  }

  static func piValue() -> Double
  {
    var result : Double = 0.0
    result = 3.14159265
    return result
  }


  static func eValue() -> Double
  {
    var result : Double = 0.0
    result = exp(_: 1)
    return result
  }


  static func setSeeds(x : Int, y : Int, z : Int) -> Void
  {
    MathLib.ix = x
    MathLib.iy = y
    MathLib.iz = z
  }


  static func nrandom() -> Double
  {
    MathLib.ix = ( MathLib.ix * 171) % 30269
    MathLib.iy = ( MathLib.iy * 172) % 30307
    MathLib.iz = ( MathLib.iz * 170) % 30323
    return (Double(MathLib.ix)/30269.0 + Double(MathLib.iy)/30307.0 + Double(MathLib.iz)/30323.0)

  }


  static func random() -> Double
  {
    var result : Double = 0.0
    var r : Double = 0.0
    r = MathLib.nrandom()
    result = (r - Double(Int(floor(_: r))))
    return result
  }


  static func combinatorial(n : Int, m : Int) -> Int64
  {
    var result : Int64 = 0
    if n - m < m
    {
      result = Int64(Ocl.prd(s: ((m + 1)...(n)).map({i in i})) / Ocl.prd(s: ((1)...(n - m)).map({j in j})))
    }
    else {
      if n - m >= m
      {
        result = Int64(Ocl.prd(s: ((n - m + 1)...(n)).map({i in i})) / Ocl.prd(s: ((1)...(m)).map({j in j})))
      }
    }
    return result
  }


  static func factorial(x : Int) -> Int64
  {
    var result : Int64 = 0
    if x < 2
    {
      result = 1
    }
    else {
      if x >= 2
      {
        result = Int64(Ocl.prd(s: ((2)...(x)).map({i in i})))
      }
    }
    return result
  }

  static func asinh(x : Double) -> Double
  {
    var result : Double = 0.0
    result = log(_: (x + sqrt(_: ( x * x + 1))))
    return result
  }


  static func acosh(x : Double) -> Double
  {
    var result : Double = 0.0
    result = log(_: (x + sqrt(_: ( x * x - Double(1)))))
    return result
  }


  static func atanh(x : Double) -> Double
  {
    var result : Double = 0.0
    result =  0.5 * log(_: ((1 + x) / (Double(1) - x)))
    return result
  }


  static func decimal2bits(x : Int64) -> String
  {
    var result : String = ""
    if x == 0
    {
      result = ""
    }
    else {
      result = MathLib.decimal2bits(x: x / 2) + "" + String((x % 2))
    }
    return result
  }


  static func decimal2binary(x : Int64) -> String
  {
    var result : String = ""
    if x < 0
    {
      result = "-" + MathLib.decimal2bits(x: -x)
    }
    else {
      if x == 0
      {
        result = "0"
      }
      else {
        result = MathLib.decimal2bits(x: x)
      }
    }
    return result
  }


  static func decimal2oct(x : Int64) -> String
  {
    var result : String = ""
    if x == 0
    {
      result = ""
    }
    else {
      result = MathLib.decimal2oct(x: x / 8) + "" + String((x % 8))
    }
    return result
  }


  static func decimal2octal(x : Int64) -> String
  {
    var result : String = ""
    if x < 0
    {
      result = "-" + MathLib.decimal2oct(x: -x)
    }
    else {
      if x == 0
      {
        result = "0"
      }
      else {
        result = MathLib.decimal2oct(x: x)
      }
    }
    return result
  }


  static func decimal2hx(x : Int64) -> String
  {
    var result : String = ""
    if x == 0
    {
      result = ""
    }
    else {
      result = MathLib.decimal2hx(x: x / 16) + ("" + MathLib.hexdigit[Int((x % 16)) + 1 - 1])
    }
    return result
  }


  static func decimal2hex(x : Int64) -> String
  {
    var result : String = ""
    if x < 0
    {
      result = "-" + MathLib.decimal2hx(x: -x)
    }
    else {
      if x == 0
      {
        result = "0"
      }
      else {
        result = MathLib.decimal2hx(x: x)
      }
    }
    return result
  }


  static func bitwiseAnd(x : Int, y : Int) -> Int
  { return x & y 
  /*  var x1 : Int = x
    var y1 : Int = y
    var k : Int = 1
    var res : Int = 0
    
    while ((x1 > 0 && y1 > 0))
    {
      if x1 % 2 == 1 && y1 % 2 == 1
      {
        res = res + k
      }
      k =  k * 2
      x1 = x1 / 2
      y1 = y1 / 2
    }
    return res */ 
  }

  static func bitwiseAnd(x : Int64, y : Int64) -> Int64
  { return x & y }


  static func bytes2integer(bs : [Int]) -> Int64 
  { if bs.count == 0
    { return 0 } 
    if (bs.count == 1) 
    { return Int64(bs[0]) } 
    if (bs.count == 2) 
    { return Int64(256*bs[0] + bs[1]) }
    
    let lowdigit = bs[bs.count-1] 
    let highdigits = Ocl.front(s: bs) 
    return 256*MathLib.bytes2integer(bs: highdigits) + Int64(lowdigit)  
  } 
  
  static func integer2bytes(x : Int64) -> [Int]
  { var result = [Int]()
 
    let y = x/256 
    let z = Int(x % 256)
    
    if y == 0
    { result.append(z) 
      return result
    }

    let highbytes = MathLib.integer2bytes(x: y)
    result = highbytes + [z]
    return result
  }

  static func integer2Nbytes(x : Int64, n : Int) -> [Int]
  { var res : [Int] = MathLib.integer2bytes(x: x) 
    while res.count < n 
    { res = [0] + res }   
    return res
  } 

  static func bitwiseOr(x : Int, y : Int) -> Int
  { return x | y 
  /*  var x1 : Int = x
    var y1 : Int = y
    var k : Int = 1
    var res : Int = 0
    
    while ((x1 > 0 || y1 > 0))
    {
      if x1 % 2 == 1 || y1 % 2 == 1
      {
        res = res + k
      }

      k =  k * 2
      x1 = x1 / 2
      y1 = y1 / 2
    }
    return res */ 
  }


  static func bitwiseXor(x : Int, y : Int) -> Int
  { return x^y
    /* var x1 : Int = x
    var y1 : Int = y
    var k : Int = 1
    var res : Int = 0
    
    while ((x1 > 0 || y1 > 0))
    {
      if (x1 % 2) != (y1 % 2)
      {
        res = res + k
      }
      k =  k * 2
      x1 = x1 / 2
      y1 = y1 / 2
    }
    return res */ 
  }

  static func bitwiseNot(x : Int) -> Int
  { return ~x } 

  static func toBitSequence(x : Int64) -> [Bool]
  { var res : [Bool] = []
    var x1 : Int64 = x;
  
    while (x1 > 0) 
    { if x1 % 2 == 0
      { res = [false] + res }
      else 
      { res = [true] + res }
      x1 = x1 / 2;
    }
    return res
  }

  static func modInverse(n : Int64, p : Int64) -> Int64
  { if p <= 0 
    { return 0 }

    let x : Int64 = (n % p)
    for i in ((1)...(p - 1))
    {
      if (( i * x) % p) == 1
      {
        return i
      }
    }
    return 0
  }


  static func modPow(n : Int64, m : Int64, p : Int64) -> Int64
  { if p <= 0 
    { return 0 }

    var res : Int64 = Int64(1)
    let x : Int64 = (n % p)
    for _ in ((1)...(m))
    {
      res = (( res * x) % p)
    }
    return res
  }

  static func longBitsToDouble(x : Int64) -> Float64
  { return Float64(bitPattern: UInt64(x)) } 

  static func netPresentValueDiscrete(rate : Float64, values : [Float64]) -> Float64
  { var result : Float64 = 0.0
    if rate <= -1
    { return result }
  
    let upper = values.count
    for i in 0...(upper-1)
    { result = 
        result + MathLib.discountDiscrete(amount: values[i], rate: rate, time: Float64(i)) 
        
    }
    return result
  }

  static func bisectionDiscrete(r : Float64, rl : Float64, ru : Float64, 
                                values : [Float64]) -> Float64
  { let result = 0.0
    if r <= -1 || rl <= -1 || ru <= -1
    { return result }
  
    let v = netPresentValueDiscrete(rate: r, values: values)
    if ru - rl < 0.001
    { return r } 
    if (v > 0)
    { return bisectionDiscrete(r: (ru + r) / 2, rl: r, ru: ru, values: values) } 
    else if v < 0
    { return bisectionDiscrete(r: (r + rl) / 2, rl: rl, ru: r, values: values) }
    return r
  }

  static func irrDiscrete(values : [Float64]) -> Float64
  { let res : Float64 = bisectionDiscrete(r: 0.1, rl: -0.5, ru: 1.0, values: values) 
    return res 
  }

   static func roundN(x : Double, n : Int) -> Double
  {
    var y : Double = 0.0
    y =  x * (pow(_: Double(10), _: Double(n)))
    return Double(Int(round(_: y)))/(pow(_: Double(10), _: Double(n)))
  }


  static func truncateN(x : Double, n : Int) -> Double
  {
    var y : Double = 0.0
    y =  x * (pow(_: Double(10), _: Double(n)))
    return Double((Int(y)))/(pow(_: Double(10.0), _: Double(n)))
  }


  static func toFixedPoint(x : Double, m : Int, n : Int) -> Double
  {
    var y : Int = 0
    y = Int(( x * (pow(_: Double(10), _: Double(n)))))
    var z : Int = 0
    z = y % Int((pow(_: Double(10), _: Double(m + n))))
    return Double(z)/(pow(_: Double(10), _: Double(n)))
  }

  static func toFixedPointRound(x : Double, m : Int, n : Int) -> Double
  {
    var y : Int = 0
    y = Int(Int(round(_: ( x * (pow(_: Double(10), _: Double(n)))))))
    var z : Int = 0
    z = y % Int((pow(_: Double(10), _: Double(m + n))))
    return Double(z)/(pow(_: Double(10), _: Double(n)))
  }

  static func isIntegerOverflow(x : Double, m : Int) -> Bool
  {
    var result : Bool = false
    var y : Int = 0
    y = Int(x)
    if y > 0
    {
      result = (Int(log10(_: Double(y))) + 1 > m)
    }
    else {
      if y < 0
      {
        result = (Int(log10(_: Double(-y))) + 1 > m)
      }
      else {
        result = (m < 1)
      }
    }
    return result
  }

  static func mean(sq : [Double]) -> Double
  {
    var result : Double = 0.0
    result = Ocl.sum(s: sq)/Double(sq.count)
    return result
  }

  static func median(sq : [Double]) -> Double
  {
    var result : Double = 0.0
    var s1 : [Double] = []
    s1 = sq.sorted()
    var sze : Int = 0
    sze = sq.count
    if sze % 2 == 1
    {
      result = s1[(1 + sze) / 2 - 1]
    }
    else {
      if sze % 2 == 0
      {
        result = (s1[sze / 2 - 1] + s1[1 + (sze / 2) - 1]) / 2.0
      }
    }
    return result
  }

  static func variance(sq : [Double]) -> Double
  {
    var result : Double = 0.0
    var m : Double = 0.0
    m = MathLib.mean(sq: sq)
    result = Ocl.sum(s: sq.map({x in ((x - m))*((x - m))}))/Double(sq.count)
    return result
  }

  static func standardDeviation(sq : [Double]) -> Double
  {
    var result : Double = 0.0
    var m : Double = 0.0
    m = MathLib.variance(sq: sq)
    result = sqrt(_: m)
    return result
  }

  static func lcm(x : Int, y : Int) -> Int
  {
    var result : Int = 0
    if x == 0 && y == 0
    {
      result = 0
    }
    else {
      result = Int(Int64( x * y) / (Ocl.gcd(_: Int64(x), _: Int64(y))))
    }
    return result
  }

  static func bisectionAsc(r : Double, rl : Double, ru : Double, f : (Double)->Double, tol : Double) -> Double
  {
    var result : Double = 0.0
    var v : Double = 0.0
    v = (f)(r)
    if v < tol && v > -tol
    {
      result = r
    }
    else {
      if v > 0
      {
        result = MathLib.bisectionAsc(r: (rl + r)/Double(2), rl: rl, ru: r, f: f, tol: tol)
      }
      else {
        if v < 0
        {
          result = MathLib.bisectionAsc(r: (r + ru)/Double(2), rl: r, ru: ru, f: f, tol: tol)
        }
      }
    }
    return result
  }

  static func rowMult(s : [Double], m : [[Double]]) -> [Double]
  {
    var result : [Double] = []
    result = Ocl.integerSubrange(st: 1, en: s.count).map({i in Ocl.sum(s: (Ocl.integerSubrange(st: 1, en: m.count)).map({k in  (s)[k - 1] * ((m)[k - 1][i - 1])}))})
    return result
  }

  static func matrixMultiplication(m1 : [[Double]], m2 : [[Double]]) -> [[Double]]
  {
    var result : [[Double]] = []
    result = m1.map({row in MathLib.rowMult(s: row, m: m2)})
    return result
  }
}


var MathLib_allInstances : [MathLib] = [MathLib]()

func createMathLib() -> MathLib
{ let result : MathLib = MathLib()
  MathLib_allInstances.append(result)
  return result
}

func killMathLib(obj: MathLib)
{ MathLib_allInstances = MathLib_allInstances.filter{ $0 !== obj } }


