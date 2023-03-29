package main

import "container/list"
// import "fmt"
import "ocl"
// import "strings"
// import "math"
import "reflect"

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

func (self *OclComparator) compare(lhs interface{},  rhs interface{}) int {
  var result int = 0
  result = (ocl.Conditional((lhs < rhs), -1, ocl.Conditional((lhs > rhs), 1, 0).(int)).(int))
  return result

}

func lowerSegment(col *list.List, x interface{},  cmp *OclComparator) *list.List {
  var result *list.List = list.New()
  result = ocl.Select(col, func(_xselect interface{}) bool { y := _xselect.(interface{}); return cmp.compare(y, x) < 0 })
  return result

}

func sortWith(col *list.List,  cmp *OclComparator) *list.List {
  var result *list.List = list.New()
  f := func(x interface{}) int { return lowerSegment(col, x, cmp).Len() }
  result = ocl.SortedByint(col, f)
  return result
}

func maximumWith(col *list.List,  cmp *OclComparator) interface{} {
  var result interface{} = 0
  result = ocl.Any(col, func(_xselect interface{}) bool { x := _xselect.(interface{}); return ocl.ForAll(col, func(_zforall interface{}) bool { y := _zforall.(interface{}); return cmp.compare(y, x) <= 0 }) }).(interface{})
  return result
}

func minimumWith(col *list.List,  cmp *OclComparator) interface{} {
  var result interface{} = 0
  result = ocl.Any(col, func(_xselect interface{}) bool { x := _xselect.(interface{}); return ocl.ForAll(col, func(_zforall interface{}) bool { y := _zforall.(interface{}); return cmp.compare(y, x) >= 0 }) }).(interface{})
  return result

}

func binarySearch(col *list.List, x interface{},  cmp *OclComparator) int {
  var result int = 0
  result = lowerSegment(col, x, cmp).Len()
  return result

}




