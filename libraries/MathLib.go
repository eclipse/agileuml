package mathlib

import "math"
// import "fmt"
import "container/list"
import "strconv"
import "../ocl"

var ix int = 1001
var iy int = 781
var iz int = 913
var hexdigit = []string{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"}

func SetSeeds(x int, y int, z int) { 
  ix = x
  iy = y
  iz = z
} 

func GetIx() int { 
  return ix
}

func Pi() float64 { 
  return 3.14159265
}

func E() float64 { 
  return math.Exp(1)
}

func nrandom() float64 {
  ix = ( ix * 171 ) % 30269 
  iy = ( iy * 172 ) % 30307 
  iz = ( iz * 170 ) % 30323 
  return float64(ix) / 30269.0 + float64(iy) / 30307.0 + float64(iz) / 30323.0
}

func Random() float64 {
  r := nrandom() 
  return r - math.Floor(r)
} 


func BitwiseAnd(x int, y int) int { 
  return x & y
}

func BitwiseOr(x int, y int) int { 
  return x | y
}

func BitwiseXor(x int, y int) int { 
  return x ^ y
}

func BitwiseNot(x int) int { 
  mx := (1 << 31) - 1
  return mx^x
}

func prd(i int, j int) int { 
  res := 1
  for k := i; k <= j; k++ { 
    res = res*k
  } 
  return res
}

func Factorial(i int) int { 
  return prd(1,i)
} 

func Combinatorial(n int, m int) int { 
  if m > n { 
    return 0
  } 
  if m < 0 { 
    return 0
  } 
  if n - m < m { 
    return prd(m+1,n) / prd(1, n-m)
  } 
  return prd(n-m+1,n) / prd(1,m)
}

func Acosh(x float64) float64 { 
  return math.Acosh(x)
} 

func Asinh(x float64) float64 { 
  return math.Asinh(x)
} 

func Atanh(x float64) float64 { 
  return math.Atanh(x)
} 

func decimal2bits(x int64) string { 
  if x == 0 { 
    return "" 
  } 
  m := x % 2
  if m == 0 { 
    return decimal2bits(x / 2) + "0"
  }
  return decimal2bits(x / 2) + "1"
}

func Decimal2binary(x int64) string {
  if x < 0 {
    return "-" + decimal2bits(-x) 
  }
  if x == 0 {
    return "0" 
  }
  return decimal2bits(x)
}

func decimal2oct(x int64) string { 
  if x == 0 {
    return ""
  }
  result := decimal2oct(x / 8) 
  mods := strconv.Itoa( int(x % 8) )
  return result + mods
}

// In general, strconv.FormatInt(i,10)

func Decimal2octal(x int64) string {
  if x < 0 {
    return "-" + decimal2oct(-x) 
  }
  if x == 0 {
    return "0" 
  }
  return decimal2oct(x)
}

func decimal2hx(x int64) string {
  if x == 0 { 
    return "" 
  }
  result := decimal2hx(x/16)
  hdigit := hexdigit[int(x % 16)]
  return result + hdigit
} 

func Decimal2hex(x int64) string {
  if x < 0 {
    return "-" + decimal2hx(-x) 
  } 
  if x == 0 { 
    return "0" 
  } 
  return decimal2hx(x)
} 

func Bytes2integer(bs *list.List) int64 { 
  if bs.Len() == 0 {  
    return 0
  } 

  if bs.Len() == 1 {
    return ocl.At(bs,1).(int64)
  } 

  if bs.Len() == 2 {  
    return int64(256*ocl.At(bs,1).(int) + ocl.At(bs,2).(int))
  }
    
  lowdigit := ocl.Last(bs).(int) 
  highdigits := ocl.Front(bs) 
  return 256*Bytes2integer(highdigits) + int64(lowdigit)  
} 

func Integer2bytes(x int64) *list.List { 
  result := list.New()

  y := x/256 
  z := int(x % 256) 
    
  if y == 0 { 
    result.PushBack(z) 
    return result
  }

  highbytes := Integer2bytes(y)
  highbytes.PushBack(z)
  return highbytes
}

func Integer2Nbytes(x int64, n int) *list.List {
  res := Integer2bytes(x) 
  for res.Len() < n {
    res.PushFront(0)
  }   
  return res 
} 

func ToBitSequence(x int64) *list.List {
  x1 := x   
  res := list.New() 
  for x1 > 0 {
    if x1 % 2 == 0 {
      res.PushFront(false)
    } else { 
      res.PushFront(true) 
    } 
    x1 = x1/2
  }
  return res
} 

func ModInverse(n int64, p int64) int64 {
  x := (n % p) 
  for i := int64(1); i < p; i++ {
    if ((i*x) % p) == 1 {  
      return i
    }
  }
  return 0
}  

func ModPow(n int64, m int64, p int64) int64 {
  res := int64(1)  
  x := n % p  
  for i := 1; int64(i) <= m; i++ { 
    res = ((res*x) % p) 
  } 
  return res 
}








