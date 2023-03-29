package ocl

import "container/list"
import "fmt"
import "strings"
import "strconv"
import "regexp"
import "reflect"
import "sort"
import "math"

/* This header provides GO Version 1.15 implementations of
   OCL library operations. OCL indexing conventions
   are used to access elements of collections and
   strings: numbering starts from 1 instead of 0.

   nil collections are treated as being empty collections. 
*/

type Ordered interface {
	int | int64 | float64 | string
}


var TYPEint reflect.Type = reflect.TypeOf(int(0))
var TYPElong reflect.Type = reflect.TypeOf(int64(0))
var TYPEdouble reflect.Type = reflect.TypeOf(float64(0.0))
var TYPEString reflect.Type = reflect.TypeOf("")
var TYPEboolean reflect.Type = reflect.TypeOf(true)
var TYPESequence reflect.Type = reflect.TypeOf(list.New())
var TYPESet reflect.Type = reflect.TypeOf(list.New())
var TYPEMap reflect.Type = reflect.TypeOf(make(map[interface{}]interface{}))

var TypeMapping map[string]reflect.Type = map[string]reflect.Type{"int":TYPEint, "long":TYPElong, "double":TYPEdouble, "String":TYPEString, "boolean":TYPEboolean, "Sequence":TYPESequence, "Set":TYPESet, "Map":TYPEMap}

// type comparable interface { 
//   int64 | float64 | string
// } 

// type Comparable interface {
//   compareTo(Comparable,Comparable) int
// }

func DisplayString(s string) { 
  fmt.Println(s)
}

func Displaydouble(d float64) { 
  fmt.Println(d)
}

func Displayint(d int) { 
  fmt.Println(d)
}

func Displaylong(d int64) { 
  fmt.Println(d)
}

func Displayboolean(d bool) { 
  fmt.Println(d)
}

func DisplaySequence(sq *list.List) { 
  fmt.Println(ToStringSequence(sq))
} 

func DisplaySet(sq *list.List) { 
  fmt.Println(ToStringSet(sq))
} 

func DisplayMap(m map[interface{}]interface{}) { 
  fmt.Println(ToStringMap(m))
} 


func Gcd(xx int64, yy int64) int64 {
  x := int64(math.Abs(float64(xx))) 
  y := int64(math.Abs(float64(yy))) 

  for x != 0 && y != 0 { 
    z := y 
    y = x % y 
    x = z
  } 

  if y == 0 { 
    return x
  } 

  if x == 0 { 
    return y
  } 

  return 0 
} 


func Conditional(test bool, ifExpr interface{}, elseExpr interface{}) interface{} {
  if test { 
    return ifExpr
  } 
  return elseExpr
} 

func SingletonSequence(x interface{}) *list.List { 
  lst := list.New()
  lst.PushBack(x)
  return lst
}

func InitialiseSequence(args ...interface{}) *list.List {
  lst := list.New()
  for x := range args {
    lst.PushBack(args[x]) 
  }
  return lst
}

      

func Size(col *list.List) int { 
  if col == nil { 
    return 0
  } 

  return col.Len()
}

func Equals(col1 *list.List, col2 *list.List) bool { 
  /* Sequence equality. Set equality is
     ocl.IncludesAll(col1,col2) && 
     ocl.IncludesAll(col2,col1) 
  */ 
  
  var result bool = true

  if col1 == nil && col2 == nil { 
    return result
  } 

  if col1 == nil { 
    return false
  } 

  if col2 == nil { 
    return false
  } 

  if col1.Len() != col2.Len() { 
    return false 
  }
  
  x := col1.Front(); 
  for e := col2.Front(); e != nil && x != nil; e = e.Next() { 
    if (e.Value != x.Value) { 
	  return false 
    }
    x = x.Next()
  }
  
  return result
}
  
func EqualsSet(col1 *list.List, col2 *list.List) bool { 
  /* Set equality is ocl.IncludesAll(col1,col2) && 
     ocl.IncludesAll(col2,col1) */ 
  
  return IncludesAll(col1,col2) && IncludesAll(col2,col1)
}

func Includes(col *list.List, x interface{}) bool { 
  var result bool = false

  if col == nil { 
    return result
  } 

  if col.Len() == 0 { 
    return result 
  }

  for e := col.Front(); e != nil; e = e.Next() { 
    if (e.Value == x) { 
	  return true 
    }
  }
  return result
}

func Excludes(col *list.List, x interface{}) bool { 
  var result bool = true

  if col == nil { 
    return result
  } 

  if col.Len() == 0 { 
    return result 
  }

  for e := col.Front(); e != nil; e = e.Next() { 
    if (e.Value == x) { 
	  return false 
    }
  }
  return result
}

func IncludesAll(col1 *list.List, col2 *list.List) bool { 
  /* col2 is a subset of col1 */ 
  
  var result bool = true
  if col2 == nil { 
    return result
  } 
  if col2.Len() == 0 { 
    return result 
  }
  for e := col2.Front(); e != nil; e = e.Next() { 
    if Excludes(col1,e.Value) { 
	  return false 
    }
  }
  return result
}

func ExcludesAll(col1 *list.List, col2 *list.List) bool { 
  /* The intersection of col1 and col2 is empty */ 

  var result bool = true

  if col1 == nil { 
    return result
  } 

  if col2 == nil { 
    return result
  } 

  if col2.Len() == 0 { 
    return result 
  }

  for e := col2.Front(); e != nil; e = e.Next() { 
    if Includes(col1,e.Value) { 
	  return false 
    }
  }

  return result
}

func IsEmpty(col *list.List) bool { 
  var result bool = true

  if col == nil { 
    return result
  } 
 
  if col.Len() > 0 { 
    return false
  } 
  return result 
} 

func NotEmpty(col *list.List) bool { 
  var result bool = false

  if col == nil { 
    return false
  } 
 
  if col.Len() > 0 { 
    return true
  } 
  return result 
} 

func AnyElement(col *list.List) interface{} { 
  if col == nil { 
    return nil
  } 

  if (col.Len() == 0) { 
    return nil 
  }
  return col.Front()
}

func At(col *list.List, i int) interface{} {
  if col == nil { 
    return nil
  } 

  var n int = col.Len()
  if (n == 0 || i < 1 || i > n) { 
    return nil 
  }
  var ind int = 1
  for e := col.Front(); e != nil && ind <= i; e = e.Next() {
    if ind >= i { 
	  return e.Value 
    }
    ind = ind + 1
  }
  return nil
}

func Count(col *list.List, x interface{}) int {
  var ind int = 0

  if col == nil { 
    return ind
  } 

  for e := col.Front(); e != nil; e = e.Next() { 
    if x == e.Value {
      ind = ind + 1
    } 
  }
  return ind
}

func First(col *list.List) interface{} {
  if col == nil { 
    return nil
  } 

  if col.Len() > 0 { 
    return col.Front().Value
  } 

  return nil
}

func Last(col *list.List) interface{} {
  if col == nil { 
    return nil
  } 
 
  if col.Len() == 0 { 
    return nil
  }
  return col.Back().Value
} 

func Tail(col *list.List) *list.List {
  result := list.New()

  if col == nil { 
    return result
  } 

  if col.Len() == 0 { 
    return result
  } 

  index := 0
  for e := col.Front(); e != nil; e = e.Next() { 
    if index > 0 { 
	  result.PushBack(e.Value) 
    }
    index++
  }
  return result
}

func Front(col *list.List) *list.List {
  result := list.New()

  if col == nil { 
    return result
  } 

  n := col.Len()
  if n == 0 { 
    return result
  } 

  index := 0
  for e := col.Front(); e != nil; e = e.Next() { 
    index++
    if index < n { 
	  result.PushBack(e.Value) 
    }
  }
  return result
}

func Concatenate(col1 *list.List, col2 *list.List) *list.List {
  /* Sequence union, col1^col2 */ 
  
  result := list.New()

  if col1 != nil { 
    for e := col1.Front(); e != nil; e = e.Next() { 
      result.PushBack(e.Value) 
    }
  } 
	
  if col2 != nil { 
    for x := col2.Front(); x != nil; x = x.Next() { 
      result.PushBack(x.Value) 
    }
  } 
	
  return result
}

func Including(col *list.List, x interface{}) *list.List {
  /* col->including(x) for set col */ 
  
  result := list.New()

  if col != nil { 
    for e := col.Front(); e != nil; e = e.Next() { 
      result.PushBack(e.Value) 
    }
  } 
	
  if Excludes(result,x) { 
    result.PushBack(x) 
  } 
  
  return result
}

func CopySequence(col *list.List) *list.List {
  /* For sequence or set col */ 
  
  result := list.New()

  if col == nil { 
    return result
  } 

  for e := col.Front(); e != nil; e = e.Next() { 
    result.PushBack(e.Value) 
  }
		
  return result
}

func RemoveAt(col *list.List, ind int) *list.List {
  /* For sequence or set col */ 
  
  index := 1
  result := list.New()

  if col != nil { 
    for e := col.Front(); e != nil; e = e.Next() { 
      if index != ind { 
        result.PushBack(e.Value)
      } 
      index++ 
    }
  } 
		
  return result
}

func RemoveFirst(col *list.List, x interface{}) *list.List {
  /* For sequence or set col */ 
  
  found := false
  result := list.New()

  if col == nil { 
    return result
  } 

  for e := col.Front(); e != nil; e = e.Next() { 
    if found { 
      result.PushBack(e.Value)
    } else { 
      if e.Value == x {
        found = true 
      } else { 
        result.PushBack(e.Value)
      } 
    }
  }
		
  return result
}

func Append(col *list.List, x interface{}) *list.List {
  /* col->including(x) for sequence col */ 
  
  result := list.New()

  if col != nil { 
    for e := col.Front(); e != nil; e = e.Next() { 
      result.PushBack(e.Value) 
    }
  }	

  result.PushBack(x) 
	
  return result
}

func Prepend(col *list.List, x interface{}) *list.List {
  /* col->prepend(x) for sequence col */ 
  
  result := list.New()

  if col != nil { 
    for e := col.Front(); e != nil; e = e.Next() { 
      result.PushBack(e.Value) 
    }
  } 
	
  result.PushFront(x) 
	
  return result
}

func Reverse(col *list.List) *list.List {
  /* col->reverse() for sequence col */ 
  
  result := list.New()
  for e := col.Front(); e != nil; e = e.Next() { 
    result.PushFront(e.Value) 
  }
		
  return result
}

func Union(col1 *list.List, col2 *list.List) *list.List {
  /* Set union, col1->union(col2) */ 
  
  result := list.New()
  for e := col1.Front(); e != nil; e = e.Next() { 
    result.PushBack(e.Value) 
  }
	
  for x := col2.Front(); x != nil; x = x.Next() { 
    if Excludes(result,x.Value) { 
	  result.PushBack(x.Value)
    }	  
  }
	
  return result
}

func Intersection(col1 *list.List, col2 *list.List) *list.List {
  /* intersection, col1->intersection(col2). Preserves order of col1 elements */ 
  
  result := list.New()
  for x := col1.Front(); x != nil; x = x.Next() { 
    if Includes(col2,x.Value) { 
	  result.PushBack(x.Value)
    }	  
  }
	
  return result
}

func UnionAll(col *list.List) *list.List {
  /* Set distributed union, col1->unionAll() */ 
  
  result := list.New()
  for e := col.Front(); e != nil; e = e.Next() { 
    var col1 *list.List = e.Value.(*list.List)
    for x := col1.Front(); x != nil; x = x.Next() { 
      if Excludes(result,x.Value) { 
        result.PushBack(x.Value)
      } 
    } 	  
  }
		
  return result
}

func ConcatenateAll(col *list.List) *list.List {
  /* Sequence distributed concatenation, col1->concatenateAll() */ 
  
  result := list.New()
  for e := col.Front(); e != nil; e = e.Next() { 
    var col1 *list.List = e.Value.(*list.List)
    for x := col1.Front(); x != nil; x = x.Next() { 
      result.PushBack(x.Value) 
    } 	  
  }
		
  return result
}

func IntersectAll(col *list.List) *list.List {
  /* Distributed intersection, col1->intersectAll() */ 
  
  if col.Len() == 0 { 
    return list.New()
  } 
  
  e := col.Front()
  result := AsSequence(e.Value.(*list.List)) 
  e = e.Next()
  
  for ; e != nil; e = e.Next() { 
    var col1 *list.List = e.Value.(*list.List)
    result = Intersection(result,col1)
  }
		
  return result
}

func Subtract(col1 *list.List, col2 *list.List) *list.List {
  /* The difference col1 - col2 */ 
  
  result := list.New()
  for e := col1.Front(); e != nil; e = e.Next() { 
    if Excludes(col2,e.Value) { 
	  result.PushBack(e.Value)
    } 	  
  }
		
  return result
}

func Excluding(col *list.List, x interface{}) *list.List {
  /* The difference col->excluding(x) */ 
  
  result := list.New()
  for e := col.Front(); e != nil; e = e.Next() { 
    if x != e.Value { 
	  result.PushBack(e.Value)
    } 	  
  }
		
  return result
}

func SubSequence(col *list.List, i int, j int) *list.List { 
  /* subsequence of i'th to j'th elements, with indexes starting at 1 */
  if i < 1 { 
    return nil
  } 
  
  result := list.New()
  index := 0
  for x := col.Front(); x != nil && index < j; x = x.Next() { 
    if index >= i-1 { 
	  result.PushBack(x.Value)
	} 
	index = index + 1
  } 
  return result
}   

func SubSequenceToEnd(col *list.List, i int) *list.List { 
  /* subsequence of i'th to end elements, with indexes starting at 1 */
  if i < 1 { 
    return nil
  } 
  
  result := list.New()
  index := 0
  for x := col.Front(); x != nil; x = x.Next() { 
    if index >= i-1 { 
	  result.PushBack(x.Value)
	} 
	index = index + 1
  } 
  return result
}   


func AsSet(col *list.List) *list.List {
  /* Convert to a set: col->asSet() */ 
  
  result := list.New()
	
  for x := col.Front(); x != nil; x = x.Next() { 
    if Excludes(result,x.Value) { 
	  result.PushBack(x.Value)
    }	  
  }
	
  return result
}

func AsSequence(col *list.List) *list.List {
  /* Convert to a sequence (make a copy): col->asSequence() */ 
  
  result := list.New()
	
  for x := col.Front(); x != nil; x = x.Next() { 
    result.PushBack(x.Value)
  }
	
  return result
}

func SequenceRange(col []interface{}) *list.List {
  /* Convert to a sequence */ 
  
  result := list.New()
	
  for x := 0; x < len(col); x++ { 
    result.PushBack(col[x])
  }
	
  return result
}

func AsArray(col *list.List) []interface{} {
  /* Convert to an array */ 
 
  var n int = col.Len()
  res := make([]interface{}, n)

  i := 0
  for x := col.Front(); x != nil; x = x.Next() { 
    res[i] = x.Value
    i++
  }
	
  return res
}

func ResizeTo(col []interface{}, n int) []interface{} { 
  res := make([]interface{}, n)
  m := len(col)
  for i := 0; i < m; i++ {
    res[i] = col[i]
  } 
  return res
} 
 
    
  

func SetAt(col *list.List, ind int, val interface{}) *list.List {
  pind := ind-1

  if pind < 0 { 
    return col
  } 

  if pind >= col.Len() { 
    return col
  } 

  result := list.New()

  i := 0
  for x := col.Front(); x != nil; x = x.Next() { 
    if pind == i {
      result.PushBack(val) 
    } else { 
      result.PushBack(x.Value)
    } 
    i++
  }

  return result
}

func UpdateAt(col *list.List, ind int, val interface{}) *list.List {
  pind := ind-1

  if pind < 0 { 
    return col
  } 

  if pind >= col.Len() { 
    return col
  } 

  i := 0
  for x := col.Front(); x != nil; x = x.Next() { 
    if pind == i {
      x.Value = val 
    } 
    i++
  }

  return col
}

func InsertAt(col *list.List, ind int, val interface{}) *list.List {
  pind := ind-1

  if pind < 0 { 
    return col
  } 

  if pind >= col.Len() { 
    return col
  } 

  result := list.New()

  i := 0
  for x := col.Front(); x != nil; x = x.Next() { 
    if pind == i {
      result.PushBack(val) 
      result.PushBack(x.Value)
    } else { 
      result.PushBack(x.Value)
    } 
    i++
  }

  return result
}

func InsertInto(col *list.List, ind int, sq *list.List) *list.List {
  pind := ind-1

  if pind < 0 { 
    return col
  } 

  if pind >= col.Len() { 
    return col
  } 

  result := list.New()

  i := 0
  for x := col.Front(); x != nil; x = x.Next() { 
    if pind == i {
      for y := sq.Front(); y != nil; y = y.Next() {
        result.PushBack(y.Value)
      }  
      result.PushBack(x.Value)
    } else { 
      result.PushBack(x.Value)
    } 
    i++
  }

  return result
}

  
func Sort(col *list.List) *list.List {
  arr := AsArray(col)
  var n = len(arr)
  res := list.New()

  if n == 0 { 
    return res
  } 

  if reflect.TypeOf(arr[0]) == TYPEint { 
    sort.Slice(arr, func(i int, j int) bool { return arr[i].(int) < arr[j].(int) }) 
  } else { 
    if reflect.TypeOf(arr[0]) == TYPEdouble { 
      sort.Slice(arr, func(i int, j int) bool { return arr[i].(float64) < arr[j].(float64) })
    } else {
      if reflect.TypeOf(arr[0]) == TYPElong { 
        sort.Slice(arr, func(i int, j int) bool { return arr[i].(int64) < arr[j].(int64) })
      } else { 
        if reflect.TypeOf(arr[0]) == TYPEString { 
          sort.Slice(arr, func(i int, j int) bool { return arr[i].(string) < arr[j].(string) })
        } 
      }  
    } 
  } 
  // Also dates. 
  
  for x := 0; x < n; x++ { 
    res.PushBack(arr[x])
  }
 
  return res
}

func SortedByint(col *list.List, f func(x interface{}) int) *list.List {
  arr := AsArray(col)
  var n = len(arr)
  res := list.New()

  if n == 0 { 
    return res
  } 

  sort.Slice(arr, func(i int, j int) bool { return f(arr[i]) < f(arr[j]) }) 
  
  for x := 0; x < n; x++ { 
    res.PushBack(arr[x])
  }
 
  return res
}

func SortedByint64(col *list.List, f func(x interface{}) int64) *list.List {
  arr := AsArray(col)
  var n = len(arr)
  res := list.New()

  if n == 0 { 
    return res
  } 

  sort.Slice(arr, func(i int, j int) bool { return f(arr[i]) < f(arr[j]) }) 
  
  for x := 0; x < n; x++ { 
    res.PushBack(arr[x])
  }
 
  return res
}

func SortedByfloat64(col *list.List, f func(x interface{}) float64) *list.List {
  arr := AsArray(col)
  var n = len(arr)
  res := list.New()

  if n == 0 { 
    return res
  } 

  sort.Slice(arr, func(i int, j int) bool { return f(arr[i]) < f(arr[j]) }) 
  
  for x := 0; x < n; x++ { 
    res.PushBack(arr[x])
  }
 
  return res
}

func SortedBystring(col *list.List, f func(x interface{}) string) *list.List {
  arr := AsArray(col)
  var n = len(arr)
  res := list.New()

  if n == 0 { 
    return res
  } 

  sort.Slice(arr, func(i int, j int) bool { return f(arr[i]) < f(arr[j]) }) 
  
  for x := 0; x < n; x++ { 
    res.PushBack(arr[x])
  }
 
  return res
}

func IntegerSumString(st int, en int, fn func(int) string) string { 
  res := ""
  for i := st; i <= en; i++ { 
    res = res + fn(i)
  } 
  return res
}   

func IntegerSumint(st int, en int, fn func(int) int) int { 
  res := 0
  for i := st; i <= en; i++ { 
    res = res + fn(i)
  } 
  return res
}   

func IntegerSumdouble(st int, en int, fn func(int) float64) float64 { 
  res := 0.0
  for i := st; i <= en; i++ { 
    res = res + fn(i)
  } 
  return res
}   

func IntegerSumlong(st int, en int, fn func(int) int64) int64 { 
  res := int64(0)
  for i := st; i <= en; i++ { 
    res = res + fn(i)
  } 
  return res
}   

func IntegerPrdint(st int, en int, fn func(int) int) int { 
  res := 1
  for i := st; i <= en; i++ { 
    res = res * fn(i)
  } 
  return res
}   

func IntegerPrddouble(st int, en int, fn func(int) float64) float64 { 
  res := 1.0
  for i := st; i <= en; i++ { 
    res = res * fn(i)
  } 
  return res
}   

func IntegerPrdlong(st int, en int, fn func(int) int64) int64 { 
  res := int64(1)
  for i := st; i <= en; i++ { 
    res = res * fn(i)
  } 
  return res
}   

 
func Select(col *list.List, fn func(interface{}) bool) *list.List { 
  result := list.New()
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if fn(x.Value) { 
	  result.PushBack(x.Value)
	} 
  }
	
  return result
}

func SelectMaximals[T Ordered](col *list.List, fn func(interface{}) T) *list.List { 
  result := list.New()
  
  for x := col.Front(); x != nil; x = x.Next() {
    if result.Len() == 0 { 
      result.PushBack(x.Value)
    } else { 
      frst := result.Front()
      fval := fn(frst)
      if fn(x.Value) == fval { 
	  result.PushBack(x.Value)
	 } else { 
        if fn(x.Value) > fval { 
          result := list.New()
          result.PushBack(x.Value)
        } 
      }
    }        
  }
	
  return result
}

func SelectMinimals[T Ordered](col *list.List, fn func(interface{}) T) *list.List { 
  result := list.New()
  
  for x := col.Front(); x != nil; x = x.Next() {
    if result.Len() == 0 { 
      result.PushBack(x.Value)
    } else { 
      frst := result.Front()
      fval := fn(frst)
      if fn(x.Value) == fval { 
	  result.PushBack(x.Value)
	 } else { 
        if fn(x.Value) < fval { 
          result := list.New()
          result.PushBack(x.Value)
        } 
      }
    }        
  }
	
  return result
}

func Reject(col *list.List, fn func(interface{}) bool) *list.List { 
  result := list.New()
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if fn(x.Value) == false { 
	  result.PushBack(x.Value)
	} 
  }
	
  return result
}

func Collect(col *list.List, fn func(interface{}) interface{}) *list.List { 
  result := list.New()
  
  for x := col.Front(); x != nil; x = x.Next() { 
	  result.PushBack(fn(x.Value))
  }
	
  return result
}

func ForAll(col *list.List, fn func(interface{}) bool) bool { 
  result := true
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if !fn(x.Value) { 
	  return false
	} 
  }
	
  return result
}

func Exists(col *list.List, fn func(interface{}) bool) bool { 
  result := false
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if fn(x.Value) { 
	  return true
	} 
  }
	
  return result
}

func Exists1(col *list.List, fn func(interface{}) bool) bool { 
  result := false
  found := false
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if fn(x.Value) { 
	  if found { 
	    return false
	  } 
	  found = true
	  result = true
	} 
  }
	
  return result
}

func Any(col *list.List, fn func(interface{}) bool) interface{} { 
  result := interface{}(nil)
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if fn(x.Value) { 
	  return x.Value
	} 
  }
	
  return result
}

func IsUnique(col *list.List) bool { 
  result := true
  found := list.New()
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if Includes(found,x.Value) { 
	  return false
	} 
	found.PushBack(x.Value)
  }
	
  return result
}

func AsOrderedSet(col *list.List) *list.List { 
  result := list.New()
  
  for x := col.Front(); x != nil; x = x.Next() { 
    if Excludes(result,x.Value) {
      result.PushBack(x.Value)
    }
  }
	
  return result
}

func MaxString(col *list.List) string {
  /* col->max() for non-empty collections of strings */ 
  
  result := First(col).(string)
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(string) > result { 
	  result = e.Value.(string)
    } 	  
  }
	
  return result
}

func Maxstring(col *list.List) string {
  /* col->max() for non-empty collections of strings */ 
  
  result := First(col).(string)
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(string) > result { 
	  result = e.Value.(string)
    } 	  
  }
	
  return result
}

func MinString(col *list.List) string {
  /* col->min() for non-empty collections of strings */ 
  
  var result string = First(col).(string) 
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(string) < result { 
	  result = e.Value.(string)
    } 	  
  }
	
  return result
}

func Minstring(col *list.List) string {
  /* col->min() for non-empty collections of strings */ 
  
  var result string = First(col).(string) 
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(string) < result { 
	  result = e.Value.(string)
    } 	  
  }
	
  return result
}

func Maxdouble(col *list.List) float64 {
  /* col->max() for non-empty collections of doubles */ 
  
  result := First(col).(float64)
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(float64) > result { 
	  result = e.Value.(float64)
    } 	  
  }
	
  return result
}

func Maxfloat64(col *list.List) float64 {
  /* col->max() for non-empty collections of doubles */ 
  
  result := First(col).(float64)
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(float64) > result { 
	  result = e.Value.(float64)
    } 	  
  }
	
  return result
}


func Mindouble(col *list.List) float64 {
  /* col->min() for non-empty collections of doubles */ 
  
  var result float64 = First(col).(float64) 
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(float64) < result { 
	  result = e.Value.(float64)
    } 	  
  }
	
  return result
}

func Minfloat64(col *list.List) float64 {
  /* col->min() for non-empty collections of doubles */ 
  
  var result float64 = First(col).(float64) 
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(float64) < result { 
	  result = e.Value.(float64)
    } 	  
  }
	
  return result
}


func Maxint(col *list.List) int {
  /* col->max() for non-empty collections of ints */ 
  
  result := First(col).(int)
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(int) > result { 
	  result = e.Value.(int)
    } 	  
  }
	
  return result
}


func Minint(col *list.List) int {
  /* col->min() for non-empty collections of ints */ 
  
  var result int = First(col).(int) 
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(int) < result { 
	  result = e.Value.(int)
    } 	  
  }
	
  return result
}

func Maxlong(col *list.List) int64 {
  /* col->max() for non-empty collections of longs */ 
  
  result := First(col).(int64)
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(int64) > result { 
	  result = e.Value.(int64)
    } 	  
  }
	
  return result
}

func Maxint64(col *list.List) int64 {
  /* col->max() for non-empty collections of longs */ 
  
  result := First(col).(int64)
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(int64) > result { 
	  result = e.Value.(int64)
    } 	  
  }
	
  return result
}


func Minlong(col *list.List) int64 {
  /* col->min() for non-empty collections of longs */ 
  
  var result int64 = First(col).(int64) 
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(int64) < result { 
	  result = e.Value.(int64)
    } 	  
  }
	
  return result
}

func Minint64(col *list.List) int64 {
  /* col->min() for non-empty collections of longs */ 
  
  var result int64 = First(col).(int64) 
  for e := col.Front(); e != nil; e = e.Next() { 
    if e.Value.(int64) < result { 
	  result = e.Value.(int64)
    } 	  
  }
	
  return result
}

func SumString(col *list.List) string {
  /* col->sum() for collections of strings */ 
  
  result := ""
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result + e.Value.(string)
  }
	
  return result
}

func Sumstring(col *list.List) string {
  /* col->sum() for collections of strings */ 
  
  result := ""
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result + e.Value.(string)
  }
	
  return result
}

func Sumint(col *list.List) int {
  /* col->sum() for collections of ints */ 
  
  result := 0
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result + e.Value.(int)
  }
	
  return result
}

func Sumlong(col *list.List) int64 {
  /* col->sum() for collections of longs */ 
  
  var result int64 = 0
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result + e.Value.(int64)
  }
	
  return int64(result)
}

func Sumint64(col *list.List) int64 {
  /* col->sum() for collections of longs */ 
  
  var result int64 = 0
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result + e.Value.(int64)
  }
	
  return int64(result)
}

func Sumdouble(col *list.List) float64 {
  /* col->sum() for collections of doubles */ 
  
  result := 0.0
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result + e.Value.(float64)
  }
	
  return result
}

func Sumfloat64(col *list.List) float64 {
  /* col->sum() for collections of doubles */ 
  
  result := 0.0
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result + e.Value.(float64)
  }
	
  return result
}

func Prdint(col *list.List) int {
  /* col->prd() for collections of ints */ 
  
  result := 1
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result * e.Value.(int)
  }
	
  return result
}

func Prdlong(col *list.List) int64 {
  /* col->prd() for collections of longs */ 
  
  var result int64 = 1
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result * e.Value.(int64)
  }
	
  return int64(result)
}

func Prdint64(col *list.List) int64 {
  /* col->prd() for collections of longs */ 
  
  var result int64 = 1
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result * e.Value.(int64)
  }
	
  return int64(result)
}

func Prddouble(col *list.List) float64 {
  /* col->prd() for collections of doubles */ 
  
  result := 1.0
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result * e.Value.(float64)
  }
	
  return result
}

func Prdfloat64(col *list.List) float64 {
  /* col->prd() for collections of doubles */ 
  
  result := 1.0
  for e := col.Front(); e != nil; e = e.Next() { 
    result = result * e.Value.(float64)
  }
	
  return result
}

func IntegerSubrange(lower int, upper int) *list.List { 
  res := list.New()
  for x := lower; x <= upper; x++ { 
    res.PushBack(x)
  } 
  return res
}

func IsSequencePrefix(sq *list.List, col *list.List) bool { 
  
  f := col.Front()

  for e := sq.Front(); e != nil; e = e.Next() { 
     if f == nil { 
       return false
     } 

     if e.Value != f.Value { 
       return false
     } 

     f = f.Next()
  }
  
  return true
} 

func IndexOfSubSequence(col *list.List, sq *list.List) int { 
  /* sq is a subsequence of col starting at OCL index res */

  res := 1

  n := col.Len()
  m := sq.Len()

  if m == 0 { 
    return 0
  } 

  if m > n { 
    return 0
  } 
  
  head := At(sq,1)
  // fmt.Printf("%s %d\n", "Head: ", head.(int))
      
  for e := col.Front(); e != nil; e = e.Next() { 
    
    if (res-1) + m > n { 
      return 0
    } 

    // fmt.Printf("%s %d\n", "Value: ", e.Value.(int))
    
    if head == e.Value { 
      subsq := SubSequenceToEnd(col,res)

      // fmt.Printf("%s %d\n", "testing subsequence: ", subsq.Len())

      if IsSequencePrefix(sq,subsq) { 
        return res
      } 
    } 
    res++
  }
	
  return 0
}

func LastIndexOfSubSequence(col *list.List, sq *list.List) int { 
  /* sq is a subsequence of col starting at OCL index res */

  res := 1
  lastIndex := 0

  n := col.Len()
  m := sq.Len()

  if m == 0 { 
    return 0
  } 

  if m > n { 
    return 0
  } 
  
  head := At(sq,1)
  // fmt.Printf("%s %d\n", "Head: ", head.(int))
      
  for e := col.Front(); e != nil; e = e.Next() { 
    
    if (res-1) + m > n { 
      return lastIndex
    } 

    // fmt.Printf("%s %d\n", "Value: ", e.Value.(int))
    
    if head == e.Value { 
      subsq := SubSequenceToEnd(col,res)

      // fmt.Printf("%s %d\n", "testing subsequence: ", subsq.Len())

      if IsSequencePrefix(sq,subsq) {
        // fmt.Print("Found prefix") 
        lastIndex = res
      } 
    } 
    res++
  }
	
  return lastIndex
}


func IndexOfSequence(col *list.List, x interface{}) int { 
  /* x is in col at OCL index res */

  res := 1
  
  for e := col.Front(); e != nil; e = e.Next() { 
    if x == e.Value { 
      return res
    } 
    res++
  }
	
  return 0
}

func LastIndexOfSequence(col *list.List, x interface{}) int { 
  /* x is in col at OCL index res */

  res := 1
  lastIndex := 0
  
  for e := col.Front(); e != nil; e = e.Next() { 
    if x == e.Value { 
      lastIndex = res
    } 
    res++
  }
	
  return lastIndex
}


/* String functions, essentially these are adapted from the Go strings library */ 
/* ->hasPrefix, ->hasSuffix are implemented by strings.HasPrefix, strings.HasSuffix */ 
/* ->count is implemented by strings.Count, 
   ->toLowerCase, ->toUpperCase also implemented by
   the same-named Go operations of strings.
*/ 

func ToInteger(s string) int { 
  strim := strings.TrimSpace(s) 

  if strim == "0" { 
    return 0
  } 

  if strings.Index(strim,"0x") == 0 { 
    ss := SubStringToEnd(strim,3)
    res, _ := strconv.ParseInt(ss,16,32)
    return int(res)
  } 

  if strings.Index(strim,"0") == 0 { 
    ss := SubStringToEnd(strim,2)
    res, _ := strconv.ParseInt(ss,8,32)
    return int(res)
  } 

  res, _ := strconv.ParseInt(strim,10,32)
  return int(res)
} 

func ToLong(s string) int64 { 
  strim := strings.TrimSpace(s) 
  if strim == "0" { 
    return 0
  } 

  if strings.Index(strim,"0x") == 0 { 
    ss := SubStringToEnd(strim,3)
    res, _ := strconv.ParseInt(ss,16,64)
    return res
  } 

  if strings.Index(strim,"0") == 0 { 
    ss := SubStringToEnd(strim,2)
    res, _ := strconv.ParseInt(ss,8,64)
    return res
  } 

  res, _ := strconv.ParseInt(strim,10,64)
  return res
} 

func ToReal(s string) float64 {
  strim := strings.TrimSpace(s) 
  res, _ := strconv.ParseFloat(strim,64)
  return res
} 

func ToBoolean(s string) bool { 
  if "true" == strings.ToLower(s) { 
    return true
  } 
  return false
} 

func Byte2Char(x int) string {
  if x < 0 { 
    return ""
  } 
  return string(x)
} 

func Char2Byte(s string) int { 
  if Length(s) == 0 { 
    return -1
  } 
  var res int
  res = int(s[0])
  return res
} 

func ToStringint(x int) string { 
  return strconv.Itoa(x)
}

func ToStringlong(x int64) string { 
  return strconv.FormatInt(x,10)
} 

func ToStringdouble(x float64) string { 
  return fmt.Sprintf("%f", x)
} 

func ToStringboolean(x bool) string {
  if x { 
    return "true"
  } 
  return "false"
} 

func ToStringOclAny(x interface{}) string { 
  if reflect.TypeOf(x) == TYPEint { 
    return strconv.Itoa(x.(int)) 
  } 

  if reflect.TypeOf(x) == TYPEdouble { 
      return fmt.Sprintf("%f", x.(float64))
  } 

  if reflect.TypeOf(x) == TYPElong { 
    return strconv.FormatInt(x.(int64),10)
  } 

  if reflect.TypeOf(x) == TYPEString { 
    return x.(string)
  } 

  if reflect.TypeOf(x) == TYPEboolean { 
    return ToStringboolean(x.(bool))
  } 

  if reflect.TypeOf(x) == TYPESequence { 
    return ToStringSequence(x.(*list.List))
  } 

  if reflect.TypeOf(x) == TYPEMap { 
    return ToStringMap(x.(map[interface{}]interface{}))
  }

  return ""
}

func ToStringSequence(col *list.List) string { 
  res := "Sequence{"
  for e := col.Front(); e != nil; e = e.Next() { 
    res = res + ToStringOclAny(e.Value)
    if e.Next() != nil { 
      res = res + ", "
    } 
  } 
  return res + "}" 
}

func ToStringSet(col *list.List) string { 
  res := "Set{"
  for e := col.Front(); e != nil; e = e.Next() { 
    res = res + ToStringOclAny(e.Value)
    if e.Next() != nil { 
      res = res + ", "
    }
  } 
  return res + "}" 
}

func ToStringMap(m map[interface{}]interface{}) string { 
  res := "Map{"
  for i, v := range m { 
    res = res + ToStringOclAny(i) + " |-> " + ToStringOclAny(v) + " "
  }
  return res + "}"
} 


func Length(s string) int { 
  return strings.Count(s,"") - 1
} 

func IndexOf(s string, substr string) int { 
  return strings.Index(s,substr) + 1
} 

func AtString(s string, ind int) string { 
  if ind < 1 { 
    return ""
  } 

  if ind > Length(s) { 
    return ""
  } 

  chars := strings.Split(s,"")
  return chars[ind-1]
}

func SetAtString(s string, ind int, val string) string {
  pind := ind-1

  if pind < 0 { 
    return s
  } 

  chars := strings.Split(s,"")
  
  if pind >= len(chars) { 
    return s
  } 

  result := ""
  for x := 0; x < pind; x++ { 
    result = result + chars[x]
  } 
  result = result + val
  for y := pind+1; y < len(chars); y++ { 
    result = result + chars[y]
  } 
  return result
}

func InsertAtString(s string, ind int, val string) string {
  pind := ind-1

  if pind < 0 { 
    return s
  } 

  chars := strings.Split(s,"")
  
  if pind >= len(chars) { 
    return s
  } 

  result := ""
  for x := 0; x < pind; x++ { 
    result = result + chars[x]
  } 
  result = result + val
  for y := pind; y < len(chars); y++ { 
    result = result + chars[y]
  } 
  return result
}

func RemoveAtString(s string, ind int) string {
  pind := ind-1

  if pind < 0 { 
    return s
  } 

  chars := strings.Split(s,"")
  
  if pind >= len(chars) { 
    return s
  } 

  result := ""
  for x := 0; x < pind; x++ { 
    result = result + chars[x]
  } 

  for y := pind+1; y < len(chars); y++ { 
    result = result + chars[y]
  } 

  return result
}

  
  
func Characters(s string) *list.List { 
  chars := strings.Split(s,"")
  result := list.New()
  for x := 0; x < len(chars); x++ { 
    result.PushBack(chars[x])
  } 
  return result
} 
  
func SubString(s string, i int, j int) string { 
  /* substring of i'th to j'th characters, */
  /* with indexes numbered from 1 */

  if i < 1 { 
    i = 1
  } 
  
  chars := strings.Split(s,"")
  if j > len(chars) { 
    j = len(chars)
  } 
  
  result := ""
  for x := i-1; x < j; x++ { 
    result = result + chars[x]
  } 
  return result
}   

func SubStringToEnd(s string, i int) string { 
  /* substring of i'th to end characters, with indexes numbered from 1 */

  if i < 1 { 
    i = 1
  } 
  
  chars := strings.Split(s,"")
  
  result := ""
  for x := i-1; x < len(chars); x++ { 
    result = result + chars[x]
  } 

  return result
}   

func ReverseString(s string) string { 
  /* s->reverse() */
  
  chars := strings.Split(s,"")
  n := len(chars)
  
  result := ""
  for x := 0; x < n; x++ { 
    result = chars[x] + result
  } 
  return result
}   

func Before(s string, str string) string { 
  n := strings.Index(s,str)
  if n < 0 { 
    return ""
  } 
  return SubString(s,1,n)
} 

func After(s string, str string) string { 
  srev := ReverseString(s)
  strrev := ReverseString(str)
  resrev := Before(srev,strrev)
  return ReverseString(resrev)
}


/* Regular expression functions */ 

func HasMatch(s string, r string) bool { 
  m, _ := regexp.Match(r,[]byte(s))
   
  return m
}

func FirstMatch(s string, r string) string { 
  re, _ := regexp.Compile(r)
  m := re.Find([]byte(s))
   
  if m == nil { 
    return "" 
  } 
  return string(m)
}

func ReplaceFirstMatch(s string, patt string, rep string) string {
  fm := FirstMatch(s, patt)
  if fm == "" { 
    return s
  } 
  return strings.Replace(s, fm, rep, 1)
} 


func IsMatch(s string, r string) bool { 
  re, _ := regexp.Compile(r)
  m := re.Find([]byte(s))
   
  if m == nil { 
    return false 
  } 

  return s == string(m)
}

func AllMatches(s string, r string) *list.List { 
  re, _ := regexp.Compile(r)
  m := re.FindAll([]byte(s), -1)
   
  res := list.New()

  if m == nil { 
    return res 
  } 

  for i := 0; i < len(m); i++ { 
    res.PushBack(string(m[i]))
  } 
  return res
}

func Split(s string, r string) *list.List { 
  re, _ := regexp.Compile(r)
  m := re.Split(s, -1)
   
  res := list.New()

  if m == nil { 
    return res 
  } 

  for i := 0; i < len(m); i++ { 
    res.PushBack(m[i])
  } 
  return res
}

func ReplaceAllMatches(ss string, patt string, rep string) string { 
  re := regexp.MustCompile(patt)
  return re.ReplaceAllLiteralString(ss,rep) 
} 


/* Map functions */ 

func CopyStringMap(m map[string]string, s string, x string) map[string]string { 
  res := make(map[string]string)
  for i, v := range m { 
    res[i] = v
  } 
  return res
} 

func CopyMap(m map[interface{}]interface{}) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m { 
    res[i] = v
  } 
  return res
} 

func RestrictMap(m map[interface{}]interface{}, dm *list.List) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m { 
    if Includes(dm,i) {
      res[i] = v
    } 
  } 
  return res
} 

func AntirestrictMap(m map[interface{}]interface{}, dm *list.List) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m { 
    if Excludes(dm,i) {
      res[i] = v
    } 
  } 
  return res
} 

func IncludingStringMap(m map[string]string, s string, x string) map[string]string { 
  res := make(map[string]string)
  for i, v := range m { 
    res[i] = v
  } 
  res[s] = x
  return res
} 

func ExcludingStringMap(m map[string]string, s string) map[string]string { 
  res := make(map[string]string)
  for i, v := range m { 
    res[i] = v
  } 
  delete(res,s)
  return res
} 

func IncludingStringObjectMap(m map[string]interface{}, s string, x interface{}) map[string]interface{} { 
  res := make(map[string]interface{})
  for i, v := range m { 
    res[i] = v
  } 
  res[s] = x
  return res
} 

func ExcludingStringObjectMap(m map[string]interface{}, s string) map[string]interface{} { 
  res := make(map[string]interface{})
  for i, v := range m { 
    res[i] = v
  } 
  delete(res,s)
  return res
} 

func IncludingMap(m map[interface{}]interface{}, s string, x string) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m { 
    res[i] = v
  } 
  res[s] = x
  return res
} 

func ExcludingMap(m map[interface{}]interface{}, s string) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m { 
    res[i] = v
  } 
  delete(res,s)
  return res
} 

func SizeStringMap(m map[string]string) int {
  if m == nil { 
    return 0
  } 
  return len(m)
}

func SizeStringObjectMap(m map[string]interface{}) int {
  if m == nil { 
    return 0
  } 
  return len(m)
}

func SizeMap(m map[interface{}]interface{}) int {
  if m == nil { 
    return 0
  } 
  return len(m)
}

func KeysStringMap(m map[string]string) *list.List { 
  res := list.New()
  for i, _ := range m { 
    res.PushBack(i)
  } 
  return res
} 

func KeysStringObjectMap(m map[string]interface{}) *list.List { 
  res := list.New()
  for i, _ := range m { 
    res.PushBack(i)
  } 
  return res
} 

func Keys(m map[interface{}]interface{}) *list.List { 
  res := list.New()
  for i, _ := range m { 
    res.PushBack(i)
  } 
  return res
} 

func ValuesStringMap(m map[string]string) *list.List { 
  res := list.New()
  for _, v := range m { 
    res.PushBack(v)
  } 
  return res
} 

func ValuesStringObjectMap(m map[string]interface{}) *list.List { 
  res := list.New()
  for _, v := range m { 
    res.PushBack(v)
  } 
  return res
} 

func Values(m map[interface{}]interface{}) *list.List { 
  res := list.New()
  for _, v := range m { 
    res.PushBack(v)
  } 
  return res
} 

func ExcludeAllMap(m1 map[interface{}]interface{}, m2 map[interface{}]interface{}) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m1 { 
    _, ok := m2[i]
	if !ok { // m2 does not have key i
	  res[i] = v
	} 
  } 
    
  return res
} 


func UnionMap(m1 map[interface{}]interface{}, m2 map[interface{}]interface{}) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m1 { 
    _, ok := m2[i]
	if !ok { // m2 does not override m1
	  res[i] = v
	} 
  } 
  
  for j, w := range m2 { 
	res[j] = w
  } 
  
  return res
} 

func IntersectionMap(m1 map[interface{}]interface{}, m2 map[interface{}]interface{}) map[interface{}]interface{} { 
  res := make(map[interface{}]interface{})
  for i, v := range m1 { 
    v1, ok := m2[i]
	if ok && v1 == v  { // m2 has same pair as m1
	  res[i] = v
	} 
  } 
    
  return res
} 

func UnionStringMap(m1 map[string]string, m2 map[string]string) map[string]string { 
  res := make(map[string]string)
  for i, v := range m1 { 
    _, ok := m2[i]
	if !ok { // m2 does not override m1
	  res[i] = v
	} 
  } 
  
  for j, w := range m2 { 
	res[j] = w
  } 
  
  return res
} 

func UnionStringObjectMap(m1 map[string]interface{}, m2 map[string]interface{}) map[string]interface{} { 
  res := make(map[string]interface{})
  for i, v := range m1 { 
    _, ok := m2[i]
	if !ok { // m2 does not override m1
	  res[i] = v
	} 
  } 
  
  for j, w := range m2 { 
	res[j] = w
  } 
  
  return res
} 



func SelectMap(m map[interface{}]interface{}, fn func(interface{}) bool) map[interface{}]interface{} { 
  result := make(map[interface{}]interface{})
  for i, v := range m {
    if fn(v) { 
      result[i] = v
    } 
  }
	
  return result
}

func RejectMap(m map[interface{}]interface{}, fn func(interface{}) bool) map[interface{}]interface{} { 
  result := make(map[interface{}]interface{})
  for i, v := range m {
    if !(fn(v)) { 
      result[i] = v
    } 
  }
	
  return result
}


func CollectMap(m map[interface{}]interface{}, fn func(interface{}) interface{}) map[interface{}]interface{} { 
  
  result := make(map[interface{}]interface{})

  for i, v := range m {
    result[i] = fn(v)
  }
	
  return result
}


func Iterateint(col *list.List, ini int, 
             f func(int) (func(int) int)) int { 
  acc := ini
  for e := col.Front(); e != nil; e = e.Next() {
    acc = f((e.Value).(int))(acc)
  }
  return acc
} 

func Iteratelong(col *list.List, ini int64, 
             f func(int64) (func(int64) int64)) int64 { 
  acc := ini
  for e := col.Front(); e != nil; e = e.Next() {
    acc = f((e.Value).(int64))(acc)
  }
  return acc
} 

func Iteratedouble(col *list.List, ini float64, 
             f func(float64) (func(float64) float64)) float64 { 
  acc := ini
  for e := col.Front(); e != nil; e = e.Next() {
    acc = f((e.Value).(float64))(acc)
  }
  return acc
} 

func IterateString(col *list.List, ini string, 
             f func(string) (func(string) string)) string { 
  acc := ini
  for e := col.Front(); e != nil; e = e.Next() {
    acc = f((e.Value).(string))(acc)
  }
  return acc
} 

func Iterateboolean(col *list.List, ini bool, 
             f func(bool) (func(bool) bool)) bool { 
  acc := ini
  for e := col.Front(); e != nil; e = e.Next() {
    acc = f((e.Value).(bool))(acc)
  }
  return acc
} 



func Iterate(col *list.List, ini interface{}, 
             f func(interface{}) (func(interface{}) interface{})) interface{} { 
  acc := ini
  for e := col.Front(); e != nil; e = e.Next() {
    acc = f(e.Value)(acc)
  }
  return acc
} 

