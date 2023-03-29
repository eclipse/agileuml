package oclrandom

import "math"
import "math/rand"
// import "fmt"
import "container/list"
// import "strconv"
import "../ocl"

type OclRandom struct { 
  ix int
  iy int
  iz int
}

func NewOclRandom() *OclRandom { 
  res := &OclRandom{}
  res.ix = 1001
  res.iy = 781
  res.iz = 913
  return res
} 

func NewOclRandom_Seed(n int64) *OclRandom { 
  res := &OclRandom{}
  res.ix = int(n % 30269)
  res.iy = int(n % 30307)
  res.iz = int(n % 30323)
  return res
} 

func (self *OclRandom) Nrandom() float64 {
  self.ix = ( self.ix * 171 ) % 30269 
  self.iy = ( self.iy * 172 ) % 30307 
  self.iz = ( self.iz * 170 ) % 30323 
  return float64(self.ix) / 30269.0 + float64(self.iy) / 30307.0 + 
         float64(self.iz) / 30323.0
}

func (self *OclRandom) NextGaussian() float64 { 
  d := self.Nrandom()
  return (d/3.0 - 0.5) 
}

func (self *OclRandom) NextDouble() float64 {
  r := self.Nrandom() 
  return r - math.Floor(r)
} 

func (self *OclRandom) NextFloat() float64 {
  r := self.Nrandom() 
  return r - math.Floor(r)
} 

func (self *OclRandom) NextInt(n int) int {
  d := self.NextDouble() 
  return int(math.Floor(d*float64(n)))
} 

func (self *OclRandom) NextIntInt() int { 
  return self.NextInt(2147483647)
}

func (self *OclRandom) NextLong() int64 { 
  d := self.NextDouble() 
  return int64(math.Floor(d*float64(9223372036854775807)))
}   

func (self *OclRandom) NextBoolean() bool { 
  d := self.NextDouble()
  if d > 0.5 { 
    return true
  }
  return false
} 

func RandomiseSequence(sq *list.List) *list.List {
  arr := ocl.AsArray(sq) 
  rand.Shuffle(len(arr), func(i, j int) { arr[i], arr[j] = arr[j], arr[i] } )
  return ocl.SequenceRange(arr)
}

func RandomString(n int) string {
  chrs := "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$"
  res := ""
  for i := 0; i < n; i++ { 
    code := int(math.Floor(rand.Float64()*54)) 
    res = res + string(chrs[code]) 
  }    
  return res
}  

func RandomElement(col *list.List) interface{} {
  n := col.Len() 
  if n == 0 {  
    return nil 
  } 
  ind := int(math.Floor(rand.Float64()*float64(n)))
  return ocl.At(col,ind+1) 
} 


