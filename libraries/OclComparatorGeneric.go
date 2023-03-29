package main

import "container/list"
// import "fmt"
import "ocl"
// import "strings"
// import "math"
import "reflect"
// import "constraints"

type Ordered interface {
	int | int64 | float64 | string
}

type OclComparator struct {
}

var OclComparator_instances = list.New()


func createOclComparator() *OclComparator {
  var res *OclComparator
  res = &OclComparator{}
  OclComparator_instances.PushBack(res)
  return res
}

var TYPEOclComparator = reflect.TypeOf(&OclComparator{})

func compare[T Ordered](self *OclComparator, lhs T,  rhs T) int {
  var result int = 0
  if lhs < rhs { 
    return -1
  } 
  if rhs < lhs { 
    return 1
  }
  return result
}

func lowerSegment[T Ordered](col *list.List, x T,  cmp *OclComparator) *list.List {
  var result *list.List = list.New()
  result = ocl.Select(col, func(_xselect interface{}) bool { y := _xselect.(T); return compare(cmp, y, x) < 0 })
  return result

}

func sortWith[T Ordered](col *list.List,  cmp *OclComparator) *list.List {
  var result *list.List = list.New()
  result = ocl.SortedByint(col, func(_xsort interface{}) int { x := _xsort.(T); return lowerSegment(col, x, cmp).Len() })
  return result

}

func maximumWith[T Ordered](col *list.List,  cmp *OclComparator) interface{} {
  var result interface{} = 0
  result = ocl.AnyElement(ocl.Select(col, func(_xselect interface{}) bool { x := _xselect.(T); return ocl.ForAll(col, func(_zforall interface{}) bool { y := _zforall.(T); return compare(cmp, y, x) <= 0 }) })).(interface{})
  return result

}

func minimumWith[T Ordered](col *list.List,  cmp *OclComparator) interface{} {
  var result interface{} = 0
  result = ocl.AnyElement(ocl.Select(col, func(_xselect interface{}) bool { x := _xselect.(T); return ocl.ForAll(col, func(_zforall interface{}) bool { y := _zforall.(T); return compare(cmp, y, x) >= 0 }) })).(interface{})
  return result

}

func binarySearch[T Ordered](col *list.List, x T,  cmp *OclComparator) int {
  var result int = 0
  result = lowerSegment(col, x, cmp).Len()
  return result
}




