//
//  Ocl.swift
//  cdoapp
//
//  Created by Kevin Lano on 07/09/2021.
//

import Foundation
import Darwin

func displayString(s: String)
{ print(s) }

func displayint(s: Int)
{ print(String(s)) }

func displaylong(s: Int64)
{ print(String(s)) }

func displaydouble(s: Double)
{ print(String(s)) }

func displayboolean(s: Bool)
{ print(String(s)) }

func displaySequence<T>(s: [T])
{ print(String(describing: s)) }

func displaySet<T>(s: Set<T>)
{ print(String(describing: s)) }


class Ocl
{

  static func includingSet<T>(s : Set<T>, x : T) -> Set<T>
  { var result : Set<T> = Set<T>()
    result.formUnion(s)
    result.insert(x)
    return result
  }

  static func includingSequence<T>(s : [T], x : T) -> [T]
  { var result : [T] = [T]()
    result = result + s
    result.append(x)
    return result
  }

  static func copySet<T>(s : Set<T>) -> Set<T>
  { var result : Set<T> = Set<T>()
    result.formUnion(s)
    return result
  }

  static func copySequence<T>(s : [T]) -> [T]
  { var result : [T] = [T]()
    result = result + s
    return result
  }

  static func prepend<T>(s : [T], x : T) -> [T]
  { var result : [T] = [T]()
    result = result + s
    result.insert(x, at: 0)
    return result
  }

  static func excludingSequence<T : Equatable>(s : [T], x : T) -> [T]
  { var result : [T] = [T]()
    for y in s
    { if x == y {}
      else
      { result.append(y) }
    }
    return result
  }

  static func excludingSequence<T : AnyObject>(s : [T], x : T) -> [T]
  { var result : [T] = [T]()
    for y in s
    { if x === y {}
      else
      { result.append(y) }
    }
    return result
  }

  static func sequenceSubtract<T : Equatable>(s1 : [T], s2 : [T]) -> [T]
  { var result : [T] = [T]()
    for y in s1
    { if containsSequence(s: s2, x: y) {}
      else
      { result.append(y) }
    }
    return result
  }

  static func sequenceSubtract<T : AnyObject>(s1 : [T], s2 : [T]) -> [T]
  { var result : [T] = [T]()
    for y in s1
    { if containsSequence(s: s2, x: y) {}
      else
      { result.append(y) }
    }
    return result
  }

  static func sequenceSubtract<T>(s1 : [T], s2 : Set<T>) -> [T]
  { var result : [T] = [T]()
    for y in s1
    { if s2.contains(y) {}
      else
      { result.append(y) }
    }
    return result
  }

  static func intersectionSequence<T : Equatable>(s1 : [T], s2 : [T]) -> [T]
  { var result : [T] = [T]()
    for y in s1
    { if containsSequence(s: s2, x: y)
      { result.append(y) }
    }
    return result
  }

  static func intersectionSequence<T : AnyObject>(s1 : [T], s2 : [T]) -> [T]
  { var result : [T] = [T]()
    for y in s1
    { if containsSequence(s: s2, x: y)
      { result.append(y) }
    }
    return result
  }


  static func containsSequence<T : Equatable>(s : [T], x : T) -> Bool
  { for y in s
    { if x == y
      { return true }
    }
    return false
  }

  static func containsSequence<T : AnyObject>(s : [T], x : T) -> Bool
  { for y in s
    { if x === y
      { return true }
    }
    return false
  }

  static func tail<T>(s : [T]) -> [T]
  { var result : [T] = [T]()
    for (i,x) in s.enumerated()
    { if i > 0
      { result.append(x) }
    }
    return result
  }

  static func subrange<T>(s : [T], st : Int, en : Int) -> [T]
  { var result : [T] = [T]()
    for (i,x) in s.enumerated()
    { if i+1 >= st && i < en
      { result.append(x) }
      else if i >= en
      { return result }
    }
    return result
  }

  static func subrange<T>(s : [T], st : Int) -> [T]
  { var result : [T] = [T]()
    for (i,x) in s.enumerated()
    { if i+1 >= st
      { result.append(x) }
    }
    return result
  }

  static func front<T>(s : [T]) -> [T]
  { var result : [T] = [T]()
    result = result + s
    result.removeLast()
    return result
  }

  static func reverse<T>(s : [T]) -> [T]
  { var result : [T] = [T]()
    for x in 0...(s.count-1)
    { result.append(s[s.count-x-1]) }
    return result
  }

    static func gcd(_ x : Int64, _ y : Int64) -> Int64
    { var xx = abs(x)
      var yy = abs(y)
      while (xx > 0 && yy > 0)
      { let zz = yy
        yy = xx % yy
        xx = zz
      }
      if (xx == 0)
      { return yy }
      if (yy == 0)
      { return xx }
      return 0
    }

  static func count<T : Equatable>(s : [T], x : T) -> Int
  { var result : Int = 0
    result = s.filter{ $0 == x }.count
    return result
  }
  // But it may be more efficient just to loop over s, testing ==

  static func count<T : AnyObject>(s : [T], x : T) -> Int
  { var result : Int = 0
    result = s.filter{ $0 === x }.count
    return result
  }
  
  static func sum(s : [Int]) -> Int
  { var result : Int = 0
    for x in s
    { result += x }
    return result
  }

  static func sum(s : [Int64]) -> Int64
  { var result : Int64 = 0
    for x in s
    { result += x }
    return result
  }

  static func sum(s : [Double]) -> Double
  { var result : Double = 0
    for x in s
    { result += x }
    return result
  }

  static func sum(s : [String]) -> String
  { var result : String = ""
    for x in s
    { result += x }
    return result
  }

  static func sum(s : Set<Int>) -> Int
  { var result : Int = 0
    for x in s
    { result += x }
    return result
  }

  static func sum(s : Set<Int64>) -> Int64
  { var result : Int64 = 0
    for x in s
    { result += x }
    return result
  }

  static func sum(s : Set<Double>) -> Double
  { var result : Double = 0
    for x in s
    { result += x }
    return result
  }

  static func sum(s : Set<String>) -> String
  { var result : String = ""
    for x in s
    { result += x }
    return result
  }

  static func prd(s : [Int]) -> Int
  { var result : Int = 1
    for x in s
    { result *= x }
    return result
  }

  static func prd(s : [Int64]) -> Int64
  { var result : Int64 = 1
    for x in s
    { result *= x }
    return result
  }

  static func prd(s : [Double]) -> Double
  { var result : Double = 1.0
    for x in s
    { result *= x }
    return result
  }

  static func prd(s : Set<Int>) -> Int
  { var result : Int = 1
    for x in s
    { result *= x }
    return result
  }

  static func prd(s : Set<Int64>) -> Int64
  { var result : Int64 = 1
    for x in s
    { result *= x }
    return result
  }

  static func prd(s : Set<Double>) -> Double
  { var result : Double = 1.0
    for x in s
    { result *= x }
    return result
  }

  static func unionAll<T>(s : Set<Set<T>>) -> Set<T>
  { var result : Set<T> = Set<T>()
    for x in s
    { result.formUnion(x) }
    return result
  }

  static func unionAll<T>(s : [[T]]) -> [T]
  { var result : [T] = [T]()
    for x in s
    { result = result + x }
    return result
  }

  static func concatenateAll<T>(s : [[T]]) -> [T]
  { var result : [T] = [T]()
    for x in s
    { result = result + x }
    return result
  }

  static func unionAll<D,T>(s : Set<D>, f : (D) -> Set<T>) -> Set<T>
  { var result : Set<T> = Set<T>()
    for x in s
    { result.formUnion(f(x)) }
    return result
  }

  static func intersectAll<T>(s : Set<Set<T>>) -> Set<T>
  { var result : Set<T> = Set<T>()
    let y = Ocl.any(s: s)
    
    if (y == nil)
    { return result }
    else
    { result = y! }
    
    for x in s
    { result.formIntersection(x) }
    return result
  }

  static func intersectAll<D,T>(s : Set<D>, f : (D) -> Set<T>) -> Set<T>
  { var result : Set<T> = Set<T>()
    let y = any(s: s)
    
    if (y == nil)
    { return result }
    else
    { result = f(y!) }
    
    for x in s
    { result.formIntersection(f(x)) }
    return result
  }

  static func toSet<T>(s : Set<T>) -> Set<T>
  { var result : Set<T> = Set<T>()
    result.formUnion(s)
    return result
  }

  static func toSet<T>(s : [T]) -> Set<T>
  { var result : Set<T> = Set<T>()
    result.formUnion(s)
    return result
  }

  static func toSet<T>(s : Dictionary<String,T>) -> Set<T>
  { var result : Set<T> = Set<T>()
    for (_,x) in s
    { result.insert(x) }
    return result
  }

  static func toSequence<T>(s : Set<T>) -> [T]
  { var result : [T] = [T]()
    for x in s
    { result.append(x) }
    return result
  }

  static func toOrderedSet<T>(s : Set<T>) -> [T]
  { var result : [T] = [T]()
    for x in s
    { result.append(x) }
    return result
  }

  static func toOrderedSet<T : AnyObject>(s : [T]) -> [T]
  { var result : [T] = [T]()
    for x in s
    { var found : Bool = false
      for y in result
      { if x === y
        { found = true }
      }
      if !found
      { result.append(x) }
    }
    return result
  }

  static func toOrderedSet<T : Equatable>(s : [T]) -> [T]
  { var result : [T] = [T]()
    for x in s
    { if result.contains(x) {
      } 
      else
      { result.append(x) }
    } 
    return result
  }

  static func toSequence<T>(s : [T]) -> [T]
  { var result : [T] = [T]()
    result = result + s
    return result
  }

  static func toSequence<T>(s : Dictionary<String,T>) -> [T]
  { var result : [T] = [T]()
    for (_,x) in s
    { result.append(x) }
    return result
  }

  static func count<T>(s : Set<T>, x : T) -> Int
  { let result : Int = 0
    if s.contains(x)
    { return 1 }
    return result
  }

  static func count<T : Equatable>(s : Dictionary<String,T>, x : T) -> Int
  { var result : Int = 0
    for (_,v) in s
    { if v == x
      { result = result + 1 }
    }
    return result
  }

  static func count<T : AnyObject>(s : Dictionary<String,T>, x : T) -> Int
  { var result : Int = 0
    for (_,v) in s
    { if v === x
      { result = result + 1 }
    }
    return result
  }


  static func select<T>(s : Set<T>, f : (T) -> Bool) -> Set<T>
  { var result : Set<T> = Set<T>()
    for v in s
    { if (f(v))
      { result.insert(v) }
    }
    return result
  }

  static func any<T>(s : Set<T>, f : (T) -> Bool) -> T?
  { for v in s
    { if (f(v))
      { return v }
    }
    return nil
  }

  static func any<T>(s : Set<T>) -> T?
  { for v in s
    { return v }
    return nil
  }

  static func any<T>(s : [T], f : (T) -> Bool) -> T?
  { for v in s
    { if (f(v))
      { return v }
    }
    return nil
  }

  static func any<T>(s : [T]) -> T?
  { for v in s
    { return v }
    return nil
  }


  static func integerSubrange(st: Int, en: Int) -> [Int]
  { var res : [Int] = [Int]()
    for i in st...en { 
      res.append(i)
    } 
    return res
  } 

  static func exists<T>(s : Set<T>, f : (T) -> Bool) -> Bool
  { for v in s
    { if (f(v))
      { return true }
    }
    return false
  }

  static func exists1<T>(s : Set<T>, f : (T) -> Bool) -> Bool
  { var found : Bool = false
    for v in s
    { if (f(v))
      { if (found)
        { return false }
        found = true
      }
    }
    return found
  }

  static func forAll<T>(s : Set<T>, f : (T) -> Bool) -> Bool
  { for v in s
    { if !(f(v))
      { return false }
    }
    return true
  }

  static func forAll<T>(s : [T], f : (T) -> Bool) -> Bool
  { for v in s
    { if !(f(v))
      { return false }
    }
    return true
  }

  static func reject<T>(s : Set<T>, f : (T) -> Bool) -> Set<T>
  { var result : Set<T> = Set<T>()
    for v in s
    { if (f(v)) {}
      else
      { result.insert(v) }
    }
    return result
  }

  static func isUnique<T, R : Hashable>(s : Set<T>, f : (T) -> R) -> Bool
  { var found : Set<R> = Set<R>()

    for v in s
    { if found.contains(f(v))
      { return false }
      found.insert(f(v))
    }
    return true
  }

  static func select<T>(s : [T], f : (T) -> Bool) -> [T]
  { var result : [T] = [T]()
    for v in s
    { if (f(v))
      { result.append(v) }
    }
    return result
  }

  static func exists<T>(s : [T], f : (T) -> Bool) -> Bool
  { for v in s
    { if (f(v))
      { return true }
    }
    return false
  }

  static func exists1<T>(s : [T], f : (T) -> Bool) -> Bool
  { var found : Bool = false
    for v in s
    { if (f(v))
      { if (found)
        { return false }
        found = true
      }
    }
    return found
  }

  static func reject<T>(s : [T], f : (T) -> Bool) -> [T]
  { var result : [T] = [T]()
    for v in s
    { if (f(v)) {}
      else
      { result.append(v) }
    }
    return result
  }

  static func isUnique<T,R: Hashable>(s : [T], f : (T) -> R) -> Bool
  { var found : Set<R> = Set<R>()

    for v in s
    { if found.contains(f(v))
      { return false }
      found.insert(f(v))
    }
    return true
  }

  static func max<T : Comparable>(s: [T]) -> T
  { var result : T = s[0]
    for x in s
    { if x > result
      { result = x }
    }
    return result
  }

  static func min<T : Comparable>(s: [T]) -> T
  { var result : T = s[0]
    for x in s
    { if x < result
      { result = x }
    }
    return result
  }

  static func max<T : Comparable>(s: Set<T>) -> T
  { var result : T = Ocl.any(s: s)!
    for x in s
    { if x > result
      { result = x }
    }
    return result
  }

  static func min<T : Comparable>(s: Set<T>) -> T
  { var result : T = Ocl.any(s: s)!
    for x in s
    { if x < result
      { result = x }
    }
    return result
  }



    static func selectMaximals<T,R: Comparable>(s : [T], f : (T) -> R) -> [T]
    { var result : [T] = [T]()
      var values : [R] = [R]()

      for (_,v) in s.enumerated()
      { if result.count == 0
        {
          result.append(v)
          values.append(f(v))
        }
        else
        { let y : R = f(v)
          if y > values[0]
          { result = [T]()
            result.append(v)
            values = [R]()
            values.append(y)
          }
          else if y == values[0]
          { result.append(v)
            values.append(y)
          }
        }
      }
      return result
    }

    static func selectMinimals<T,R: Comparable>(s : [T], f : (T) -> R) -> [T]
    { var result : [T] = [T]()
      var values : [R] = [R]()

      for (_,v) in s.enumerated()
      { if result.count == 0
        {
          result.append(v)
          values.append(f(v))
        }
        else
        { let y : R = f(v)
          if y < values[0]
          { result = [T]()
            result.append(v)
            values = [R]()
            values.append(y)
          }
          else if y == values[0]
          { result.append(v)
            values.append(y)
          }
        }
      }
      return result
    }
    

 static func includingMap<T>(m : Dictionary<String,T>, k : String, val : T) -> Dictionary<String,T>
  { var res: Dictionary<String,T> = [String:T]()
    for (key,value) in m
    { res[key] = value }
    res[k] = val
    return res
  }

  static func unionMap<T>(m1 : Dictionary<String,T>, m2 : Dictionary<String,T>) -> Dictionary<String,T>
  { var res : Dictionary<String,T> = [String:T]()
    for (key,value) in m1
    { res[key] = value }
    for (key,value) in m2
    { res[key] = value }
    return res
  }

  static func intersectionMap<T : Equatable>(m1 : Dictionary<String,T>, m2 : Dictionary<String,T>) -> Dictionary<String,T>
  { var res : Dictionary<String,T> = [String:T]()
    for (key,value) in m1
    { if (m2[key] == value)
      { res[key] = value }
    }
    return res
  }

  static func intersectionMap<T : AnyObject>(m1 : Dictionary<String,T>, m2 : Dictionary<String,T>) -> Dictionary<String,T>
  { var res : Dictionary<String,T> = [String:T]()
    for (key,value) in m1
    { if (m2[key] === value)
      { res[key] = value }
    }
    return res
  }

  static func excludeAllMap<T>(m1 : Dictionary<String,T>, m2 : Dictionary<String,T>) -> Dictionary<String,T>
  { var res : Dictionary<String,T> = [String:T]()
    for (key,value) in m1
    { if (m2[key] == nil)
      { res[key] = value }
    }
    return res
  }


  static func excludingMapValue<T : Equatable>(m : Dictionary<String,T>, v : T) -> Dictionary<String,T>
  { var res : Dictionary<String,T> = [String:T]()
    for (key,value) in m
    { if (value != v)
      { res[key] = value }
    }
    return res
  }

  static func excludingMapValue<T : AnyObject>(m : Dictionary<String,T>, v : T) -> Dictionary<String,T>
  { var res : Dictionary<String,T> = [String:T]()
    for (key,value) in m
    { if (value !== v)
      { res[key] = value }
    }
    return res
  }

  static func excludingMapKey<T>(m : Dictionary<String,T>, k : String) -> Dictionary<String,T>
  { var res : Dictionary<String,T> = [String:T]()
    for (key,value) in m
    { if (key != k)
      { res[key] = value }
    }
    return res
  }

  static func selectMap<T>(m : Dictionary<String,T>, f : (T) -> Bool) -> Dictionary<String,T>
  { var result : Dictionary<String,T> = [String:T]()
    for (k,v) in m
    { if (f(v))
      { result[k] = v }
    }
    return result
  }

  static func rejectMap<T>(m : Dictionary<String,T>, f : (T) -> Bool) -> Dictionary<String,T>
  { var result : Dictionary<String,T> = [String:T]()
    for (k,v) in m
    { if (f(v)) {}
      else
      { result[k] = v }
    }
    return result
  }

  static func collectMap<T,R>(m : Dictionary<String,T>, f : (T) throws -> R) -> Dictionary<String,R>
  { var result : Dictionary<String,R> = [String:R]()
    for (k,v) in m
    { do
      { try result[k] = f(v) }
      catch { }
    }
    return result
  }

  static func restrict<T>(m : Dictionary<String,T>, ks : Set<String>) -> Dictionary<String,T>
  { var result : Dictionary<String,T> = [String:T]()
    for (k,v) in m
    { if ks.contains(k)
      { result[k] = v }
    }
    return result
  }

  static func antirestrict<T>(m : Dictionary<String,T>, ks : Set<String>) -> Dictionary<String,T>
  { var result : Dictionary<String,T> = [String:T]()
    for (k,v) in m
    { if ks.contains(k)
      { }
      else
      { result[k] = v }
    }
    return result
  }

  static func copyMap<T>(m : Dictionary<String,T>) -> Dictionary<String,T>
  { var result : Dictionary<String,T> = [String:T]()

    for (k,v) in m
    { result[k] = v }

    return result
  }



  static func mapRange<T>(m : Dictionary<String,T>) -> [T]
  { var result : [T] = [T]()
    for (_,v) in m
    { result.append(v) }
    return result
  }

  static func mapKeys<T>(m : Dictionary<String,T>) -> Set<String>
  { var result : Set<String> = Set([])
    for (k,_) in m
    { result.insert(k) }
    return result
  }

  static func hasValue<T>(s : Dictionary<String,T>, v : T) -> Bool
  { for (_,x) in s
    { if (x == v)
      { return true }
    }
    return false
  }

  static func exists<T>(s : Dictionary<String,T>, f : (T) -> Bool) -> Bool
  { for (_,v) in s
    { if (f(v))
      { return true }
    }
    return false
  }

  static func exists1<T>(s : Dictionary<String,T>, f : (T) -> Bool) -> Bool
  { var found : Bool = false
    for (_,v) in s
    { if (f(v))
      { if (found)
        { return false }
        found = true
      }
    }
    return found
  }

  static func isUnique<T,R : Hashable>(s : Dictionary<String,T>, f : (T) -> R) -> Bool
  { var found : Set<R> = Set<R>()

    for (_,v) in s
    { if found.contains(f(v))
      { return false }
      found.insert(f(v))
    }
    return true
  }

  static func iterate<T,R>(s : [T], ini: R, f : (T) -> ((R) -> R)) -> R
  { var result : R = ini
    for x in s 
    { result = f(x)(result) } 
    return result
  } 

 static func sortedBy<T,R: Comparable>(s : Set<T>, f : (T) -> R) -> [T]
  { var result : [T] = [T]()
    result = result + s
    let res = result.sorted(by: {x1,x2 in f(x1) < f(x2)})
    return res
  }

  static func sortedBy<T,R: Comparable>(s : [T], f : (T) -> R) -> [T]
  { var result : [T] = [T]()
    result = result + s
    let res = result.sorted(by: {x1,x2 in f(x1) < f(x2)})
    return res
  }

  
  static func includesAllSequence<T: Equatable>(s1 : [T], s2 : [T]) -> Bool
  { for (_,y) in s2.enumerated()
    { if containsSequence(s: s1, x: y)
      {}
      else
      { return false }
    }
    return true
  }

 
  static func includesAllSequence<T: AnyObject>(s1 : [T], s2 : [T]) -> Bool
  { for (_,y) in s2.enumerated()
    { if containsSequence(s: s1, x: y)
      {}
      else
      { return false }
    }
    return true
  }


 static func at(str: String, ind: Int) -> String
 { var count = 0
   for index in str.indices
   { let c : Character = str[index]
     count = count + 1
     if (count == ind)
     { return String(c) }
   }
   return ""
 }

    static func charAt(str: String, ind: Int) -> String
    { var res : String = ""
      let sq = Ocl.chars(str: str)
      if ind > 0 && ind <= sq.count
      { res = String(sq[ind-1]) }
      return res
    }


  static func iswhitespace(s: String) -> Bool
  { if (" " == s || "\n" == s || "\t" == s)
    { return true }
    return false
  }
  
  static func trim(str : String) -> String
  {
    var result : String = ""
    result = ""
    var i : Int = 0
    i = 1
    while ((i < str.count && Ocl.iswhitespace(s: Ocl.at(str: str, ind: i))))
    {
      i = i + 1
    }
    var j : Int = 0
    j = str.count
    while (j >= 1 && Ocl.iswhitespace(s: Ocl.at(str: str, ind: j)))
    {
      j = j - 1
    }
    if j < i
    {
      result = ""
    }
    else {
      result = Ocl.stringSubrange(str: str, st: i, en: j)
    }
    return result
  }

  static func replace(str : String, delim : String, s2 : String) -> String
  { var s : String = "" + str
    var result : String = ""
    var i : Int = Ocl.indexOf(str: str, ch: delim)

    while (i > 0)
    {
      result = result + Ocl.stringSubrange(str: s, st: 1, en: i - 1) + s2
      s = Ocl.stringSubrange(str: s, st: i + delim.count, en: s.count)
      i = Ocl.indexOf(str: s, ch: delim)
    }
    result = result + s
    return result
  }

  static func equalsIgnoreCase(s1 : String, s2 : String) -> Bool
  {
    var result : Bool = false
    if s1.lowercased() == s2.lowercased()
    {
      result = true
    }
    return result
  }


 static func indexOf(str: String, ch: String) -> Int
 { var res : Int = 0
   var found : Bool = false
   if ch.count == 0
   { return 0 }
   
   let sepchars = Ocl.chars(str: ch)
   var ind : Int = 0
   
   for index in str.indices
   { let c : Character = str[index]
     
     if found && (ind < sepchars.count)
     { if (c == sepchars[ind])
       { ind = ind + 1 }
       else
       { found = false
         res = res + ind
         ind = 0
       }
     }
     else if found
     { return res + 1 }
     
     if found == false
     { if c == sepchars[0]
       { found = true
         ind = 1
       }
     }
    
     if found == false
     { res = res + 1 }
   }

   if found
   { return res + 1 }
   return 0
 }

 static func stringSubrange(str : String, st : Int, en : Int) -> String
  { var result : [Character] = [Character]()
    var count : Int = 0
    
    for index in str.indices
    { let c : Character = str[index]
      count = count + 1
      if count >= st && count <= en
      { result.append(c) }
      else if count > en
      { return String(result) }
    }
    return String(result)
  }

  static func stringSubrange(str : String, st : Int) -> String
  { var result : [Character] = [Character]()
    var count : Int = 0
    
    for index in str.indices
    { let c : Character = str[index]
      count = count + 1
      if count >= st
      { result.append(c) }
    }
    return String(result)
  }

  static func isInteger(str : String) -> Bool
  { var res : Int? = nil
    if str.hasPrefix("0x")
    { res = Int(Ocl.stringSubrange(str: str, st: 3), radix: 16)
      if res != nil
      { return true }
      return false
    } 

    if str.hasPrefix("0b")
    { res = Int(Ocl.stringSubrange(str: str, st: 3), radix: 2)       
      if res != nil
      { return true }
      return false
    }

    if str.hasPrefix("0") && str.count > 1
    { res = Int(str, radix: 8)
      if res != nil
      { return true }
      return false
    }

    res = Int(str)
    if res != nil
    { return true }
    return false
  }

  static func isLong(str : String) -> Bool
  { var res : Int64? = nil

    if str.hasPrefix("0x")
    { res = Int64(Ocl.stringSubrange(str: str, st: 3), radix: 16)
      if res != nil
      { return true }
      return false
    } 

    if str.hasPrefix("0b")
    { res = Int64(Ocl.stringSubrange(str: str, st: 3), radix: 2)       
      if res != nil
      { return true }
      return false
    }

    if str.hasPrefix("0") && str.count > 1
    { res = Int64(str, radix: 8)
      if res != nil
      { return true }
      return false
    }

    res = Int64(str)
    if res != nil
    { return true }
    return false
  }

  static func toInteger(str : String) -> Int
  {
    if str.hasPrefix("0x")
    { return Int(Ocl.stringSubrange(str: str, st: 3), radix: 16)! }
    if str.hasPrefix("0b")
    { return Int(Ocl.stringSubrange(str: str, st: 3), radix: 2)! }
    if str.hasPrefix("0") && str.count > 1
    { return Int(str, radix: 8)! }
    return Int(str)!
  }

    static func toLong(str : String) -> Int64
    {
      if str.hasPrefix("0x")
      { return Int64(Ocl.stringSubrange(str: str, st: 3), radix: 16)! }
      if str.hasPrefix("0b")
      { return Int64(Ocl.stringSubrange(str: str, st: 3), radix: 2)! }
      if str.hasPrefix("0") && str.count > 1
      { return Int64(str, radix: 8)! }
      return Int64(str)!
    }

  static func isReal(str: String) -> Bool
  { let res : Double? = Double(str)
    if res == nil
    { return false }
    return true
  }

  static func toReal(str: String) -> Double
  { let res : Double? = Double(str)
    if res == nil
    { return Double.nan }
    return res!
  }

  static func toBoolean(x: Any) -> Bool
  { let ss = String(describing: x)
    
    if ss.lowercased() == "true"
    { return true }
    
    if Ocl.isInteger(str: ss)  
    { if Ocl.toInteger(str: ss) == 1
      { return true }
    }
    
    return false
  } 
    
 static func stringSubtract(s1 : String, s2 : String) -> String
  { var result : [Character] = [Character]()
    var subtracted : Set<Character> = Set<Character>()

    for index in s2.indices
    { let c : Character = s2[index]
      subtracted.insert(c)
    }

    for index in s1.indices
    { let c1 : Character = s1[index]
      if subtracted.contains(c1)
      { }
      else
      { result.append(c1) }
    }
    // print(subtracted)
    // print(result)
    return String(result)
  }

  static func insertAt<T>(s1 : [T], s2 : [T], ind : Int) -> [T]
  { var result : [T] = [T]()
    for (i,x) in s1.enumerated()
    { if i < ind-1
      { result.append(x) }
    }
    for y in s2
    { result.append(y) }
    for (j,z) in s1.enumerated()
    { if j >= ind-1
      { result.append(z) }
    }
    return result
  }

  static func insertAt<T>(s1 : [T], s2 : T, ind : Int) -> [T]
  { var result : [T] = [T]()
    for (i,x) in s1.enumerated()
    { if i < ind-1
      { result.append(x) }
    }

    result.append(s2)

    for (j,z) in s1.enumerated()
    { if j >= ind-1
      { result.append(z) }
    }
    return result
  }

  static func insertInto<T>(s1 : [T], s2 : [T], ind : Int) -> [T]
  { var result : [T] = [T]()
    for (i,x) in s1.enumerated()
    { if i < ind-1
      { result.append(x) }
    }
    for y in s2
    { result.append(y) }
    for (j,z) in s1.enumerated()
    { if j >= ind-1
      { result.append(z) }
    }
    return result
  }

  static func excludingSubrange<T>(s : [T], ind1 : Int, ind2 : Int) -> [T]
  { var result : [T] = [T]()

    for (i,x) in s.enumerated()
    { if i < ind1-1
      { result.append(x) }
      if i >= ind2
      { result.append(x) }
    }

    return result
  }

  static func characters(str: String) -> [String]
  { var res : [String] = [String]()
    for ind in str.indices
    { res.append(String(str[ind])) }
    return res
  }

  static func chars(str: String) -> [Character]
  { var res : [Character] = [Character]()
    for ind in str.indices
    { res.append(str[ind]) }
    return res
  }

  static func firstCharacter(str: String) -> Character
  { var res : [Character] = [Character]()
    for ind in str.indices
    { res.append(str[ind])
      return str[ind]
    }
    return res[0]
  }

  static func insertAtString(s1 : String, s2 : String, ind : Int) -> String
  { var result : [Character] = [Character]()
    let seq1 = Ocl.chars(str: s1)
    let seq2 = Ocl.chars(str: s2)
    result = Ocl.insertAt(s1: seq1, s2: seq2, ind: ind)
    return String(result)
  }

    static func removeAtString(str : String, ind : Int) -> String
    { var result : [Character] = []
      let sq = Ocl.chars(str: str)
      for (i,x) in sq.enumerated()
      { if i < ind-1 || i >= ind
        { result.append(x) }
      }
      return String(result)
    }
    
    static func setAtString(str : String, ind: Int, value: String) -> String
    { var result : [Character] = []
      let sq = Ocl.chars(str: str)
      result = result + sq
      if ind >= 1 && ind < result.count
      { result[ind-1] = Ocl.firstCharacter(str: value) }
      return String(result)
    }

  static func reverseString(str : String) -> String
  { let result : [Character] = Ocl.chars(str: str)
    let rev = Ocl.reverse(s: result)
    return String(rev)
  }

   static func toLineSequence(str: String) -> [String]
   { var result : [String] = [String]()
     var buffer : String = ""
     
     for index in str.indices
     { let c : Character = str[index]
       if "\n" == String(c)
       { result.append(buffer)
         buffer = String()
       }
       else
       { buffer = buffer + String(c) }
     }
     result.append(buffer)
     return result
   }

   static func before(str: String, sep: String) -> String
   { let ind = indexOf(str: str, ch: sep)
     if ind > 0
     { return stringSubrange(str: str, st: 1, en: ind-1) }
     return str
   } // Single-character sep only

   static func after(str: String, sep: String) -> String
   { let revstr = reverseString(str: str)
     let revsep = reverseString(str: sep)
     let ind = indexOf(str: revstr, ch: revsep)
     if ind > 0
     { let res = stringSubrange(str: revstr, st: 1, en: ind-1)
       return reverseString(str: res)
     }
     return ""
   } // Single-character sep only
   
   static func hasMatch(str: String, pattern: String) -> Bool
   { let rge = NSRange(location: 0, length: str.utf16.count)
     let regexp = try! NSRegularExpression(pattern: pattern)
     let pred = regexp.firstMatch(in: str, options: [], range: rge)
     return pred != nil
  }
  
   static func isMatch(str: String, pattern: String) -> Bool
   { let rge = NSRange(location: 0, length: str.utf16.count)
     let regexp = try! NSRegularExpression(pattern: pattern)
     let pred = regexp.firstMatch(in: str, options: [], range: rge)
     if pred == nil
     { return false }
     let res = pred!.range
     return res.length == str.count
  }

   static func replaceFirstMatch(str: String, pattern: String, rep: String) -> String
   { let rge = NSRange(location: 0, length: str.utf16.count)
     let regexp = try! NSRegularExpression(pattern: pattern)
     let p = regexp.rangeOfFirstMatch(in: str, options: [], range: rge)
     var result : String = str

     if p.length <= 0
     { return result }

     let modText =
        regexp.stringByReplacingMatches(in: str, options: [],
              range: NSRange(location: 0, length: p.location + p.length), withTemplate: rep)
    return modText
 }


  static func replaceAll(str: String, pattern: String, rep: String) -> String
  { let regex = try! NSRegularExpression(pattern: pattern)
    
    let modText =
      regex.stringByReplacingMatches(in: str, options: [],
              range: NSRange(location: 0, length: str.count), withTemplate: rep)
    return modText
  }

  static func allMatches(str: String, pattern: String) -> [String]
   { let rge = NSRange(location: 0, length: str.utf16.count)
     let regexp = try! NSRegularExpression(pattern: pattern)
     let pred = regexp.matches(in: str, options: [], range: rge)
     var result : [String] = [String]()

     for p in pred
     { let range = p.range
       let matchString = Ocl.stringSubrange(str: str, st: range.location+1, en: range.location + range.length)
       result.append(matchString)
     }
     return result
  }
  
  static func firstMatch(str: String, pattern: String) -> String?
   { let rge = NSRange(location: 0, length: str.utf16.count)
     let regexp = try! NSRegularExpression(pattern: pattern)
     let p = regexp.rangeOfFirstMatch(in: str, options: [], range: rge)
     var result : String? = nil

     if p.length <= 0
     { return result }

     let matchString = Ocl.stringSubrange(str: str, st: p.location+1, en: p.location + p.length)
     return matchString
 }

   static func split(str: String, pattern: String) -> [String]
   { let rge = NSRange(location: 0, length: str.utf16.count)
     let regexp = try! NSRegularExpression(pattern: pattern)
     let pred = regexp.matches(in: str, options: [], range: rge)
     var result : [String] = [String]()
     var prev : Int = 1;

     for p in pred
     { let range = p.range
       let splitString = Ocl.stringSubrange(str: str, st: prev, en: range.location)
       prev = range.location + range.length + 1
       if splitString.count > 0
       { result.append(splitString) }
     }

     if prev <= str.count
     { result.append(Ocl.stringSubrange(str: str, st: prev, en: str.count)) }
     return result
  }

  static func lastIndexOf(s : String, d : String) -> Int
  {
    var result : Int = 0
    var i : Int = 0
    i = Ocl.indexOf(str: Ocl.reverseString(str: s), ch: Ocl.reverseString(str: d))
    if i <= 0
    {
      result = 0
    }
    else
    {
      if i > 0
      {
        result = s.count - i - d.count + 2
      }
    }
    return result
  }

  static func setAt<T>(sq : [T], ind: Int, value: T) -> [T]
  { var result : [T] = [T]()
    result = result + sq
    if ind >= 1 && ind < result.count
    { result[ind-1] = value }
    return result
  }


  static func removeFirst<T : Equatable>(sq : [T], x : T) -> [T]
  { var res : [T] = [T]()
    res = res + sq

    for (i,y) in sq.enumerated()
    { if x == y
      { res.remove(at: i)
        return res
      }
    }
    return res
  }

  static func removeFirst<T : AnyObject>(sq : [T], x : T) -> [T]
  { var res : [T] = [T]()
    res = res + sq

    for (i,y) in sq.enumerated()
    { if x === y
      { res.remove(at: i)
        return res
      }
    }
    return res
  }

  static func removeAt<T>(sq : [T], ind : Int) -> [T]
  { var result : [T] = [T]()
    for (i,x) in sq.enumerated()
    { if i < ind-1 || i >= ind
      { result.append(x) }
    }
    return result
  }

  static func removeObject<T : AnyObject>(sq : [T], obj: T) -> [T]
  { var result : [T] =
          sq.filter{ $0 !== obj }
    return result
  }

  static func indexOfSequence<T : Equatable>(sq : [T], x : T) -> Int
  { for (i,y) in sq.enumerated()
    { if x == y
      { return i+1 }
    }
    return 0
  }

  static func indexOfSequence<T : AnyObject>(sq : [T], x : T) -> Int
  { for (i,y) in sq.enumerated()
    { if x === y
      { return i+1 }
    }
    return 0
  }
  
  static func lastIndexOfSequence<T : Equatable>(sq : [T], x : T) -> Int
  { if sq.count == 0
    { return 0 }

    let revsq = reverse(s: sq)
    let i = indexOfSequence(sq: revsq, x: x)
    
    if i == 0
    { return 0 }
    return sq.count - i + 1
  }

  static func lastIndexOfSequence<T : AnyObject>(sq : [T], x : T) -> Int
  { if sq.count == 0
    { return 0 }

    let revsq = reverse(s: sq)
    let i = indexOfSequence(sq: revsq, x: x)
    
    if i == 0
    { return 0 }
    return sq.count - i + 1
  }
 
  static func hasPrefixSequence<T : Equatable>(sq : [T], sq1 : [T], i : Int) -> Bool
  { let n = sq.count
    let m = sq1.count
    if n == 0 || m == 0 || i+m > n
    { return false }
    
    var j : Int = 0
    while j < m && i+j < n
    { if sq[i+j] != sq1[j]
      { return false }
      j = j+1
    }
    return true
  }
  
  static func hasPrefixSequence<T : AnyObject>(sq : [T], sq1 : [T], i : Int) -> Bool
  { let n = sq.count
    let m = sq1.count
    if n == 0 || m == 0 || i+m > n
    { return false }
    
    var j : Int = 0
    while j < m && i+j < n
    { if sq[i+j] !== sq1[j]
      { return false }
      j = j+1
    }
    return true
  }

  static func hasPrefixSequence<T : Equatable>(sq : [T], sq1 : [T]) -> Bool
  { return hasPrefixSequence(sq: sq, sq1: sq1, i: 0) }

  static func hasPrefixSequence<T : AnyObject>(sq : [T], sq1 : [T]) -> Bool
  { return hasPrefixSequence(sq: sq, sq1: sq1, i: 0) }
  
  static func indexOfSubSequence<T : Equatable>(sq : [T], sq1 : [T]) -> Int
  { let m = sq1.count
    let n = sq.count
    if m == 0 || n == 0 || n < m
    { return 0 }
    var i : Int = 0

    while i+m <= n
    { if hasPrefixSequence(sq: sq, sq1: sq1, i: i)
      { return i+1 }
      i = i + 1
    }
    return 0
  }

  static func indexOfSubSequence<T : AnyObject>(sq : [T], sq1 : [T]) -> Int
  { let m = sq1.count
    let n = sq.count
    if m == 0 || n == 0 || n < m
    { return 0 }
    var i : Int = 0

    while i+m <= n
    { if hasPrefixSequence(sq: sq, sq1: sq1, i: i)
      { return i+1 }
      i = i + 1
    }
    return 0
  }
  
    static func lastIndexOfSubSequence<T : Equatable>(sq : [T], sq1 : [T]) -> Int
    { let m = sq1.count
      let n = sq.count
      if m == 0 || n == 0 || n < m
      { return 0 }
        
      var i : Int = n - m

      while i >= 0
      { if hasPrefixSequence(sq: sq, sq1: sq1, i: i)
        { return i+1 }
        i = i - 1
      }
      return 0
    }

    static func lastIndexOfSubSequence<T : AnyObject>(sq : [T], sq1 : [T]) -> Int
    { let m = sq1.count
      let n = sq.count
      if m == 0 || n == 0 || n < m
      { return 0 }
        
      var i : Int = n - m

      while i >= 0
      { if hasPrefixSequence(sq: sq, sq1: sq1, i: i)
        { return i+1 }
        i = i - 1
      }
      return 0
    }

  static func char2byte(ch: String) -> Int
  { if let res = Unicode.Scalar(ch)
    { return Int(res.value) }
    else
    { return -1 }
  }

  static func byte2char(n: Int) -> String
  { if let res = Unicode.Scalar(n)
    { return String(res) }
    else
    { return "" }
  }
    
  static func tokeniseCSV(line: String) -> [String]
  { // Assumes the separator is a comma
    var buff : String = ""
    // var x : Int = 0
    // var len : Int = line.count
    var instring : Bool = false
    var res : [String] = [String]()
   
    for x in line.indices
    { let chr : Character = line[x]
      if chr == ","
      { if instring
        { buff = buff + String(chr) }
        else
        { res.append(buff)
          buff = String()
        }
      }
      else if "\"" == chr
      { if instring
        { instring = false }
        else
        { instring = true }
      }
      else
      { buff = buff + String(chr) }
    }
    res.append(buff)
    return res
  }

  static func parseCSVtable(rows: String) -> [String]
  { var buff : String = ""
    var ind : Int = 0
    // var len : Int = rows.count
    // var instring : Bool = false
    var res : [String] = [String]()
   
    // Ignore the first row: column names
    
    for x in rows.indices
    { let chr : Character = rows[x]
      if chr == "\n" && ind > 0
      { res.append(buff)
        buff = String()
        ind = ind + 1
      }
      else if chr == "\n"
      { ind = ind + 1
        buff = String()
      }
      else
      { buff = buff + String(chr) }
    }
    res.append(buff)
    return res
  }

  static func sequenceRange<T>(arr : UnsafeMutableBufferPointer<T>, n : Int) -> [T]
  { var result : [T] = [T]()
    for ind in 0...(n-1)
    { result.append(arr[ind]) }
    return result
  }

  static func sequenceRange<T>(arr : [T], n : Int) -> [T]
  { var result : [T] = [T]()
    for ind in 0...(n-1)
    { result.append(arr[ind]) }
    return result
  }

  static func createArray<T>(n : Int, value: T) -> UnsafeMutableBufferPointer<T>
  { var startx : UnsafeMutablePointer<T>
    startx = UnsafeMutablePointer<T>.allocate(capacity: n)
    let res = UnsafeMutableBufferPointer<T>(start: startx, count: n)
    var sq = [T]()
    for i in 1...n
    { sq.append(value) }
    res.initialize(from: sq)
    return res
  }

  static func createRef<T>(p : UnsafeMutablePointer<T>) ->   UnsafeMutableBufferPointer<T>
  { var startx : UnsafeMutablePointer<T>
    startx = UnsafeMutablePointer<T>.allocate(capacity: 1)
    let res = UnsafeMutableBufferPointer<T>(start: startx, count: 1)
    var sq = [T]()
    sq.append(p.pointee)
    res.initialize(from: sq)
    return res
  }  

  static func tailRef<T>(p: UnsafeMutableBufferPointer<T>, n: Int) -> UnsafeMutableBufferPointer<T>
  { let sze = p.count
    if sze > n
    { let startx = (p.baseAddress)! + n
      let res = UnsafeMutableBufferPointer<T>(start: startx, count: sze-n)
      var sq = [T]()
      for i in n...sze-1
      { sq.append(p[i]) } 
      res.initialize(from: sq)
      return res
    } 
    return p
  }
}



class IndexingException : Error
{  var message : String
   private static var instance : IndexingException? = nil
    
    init()
    { message = "Indexing exception" }
    
    init(copyFrom: IndexingException)
    { message = copyFrom.message }

    static func defaultInstance() -> IndexingException
    { if instance == nil 
      { instance = IndexingException() } 
      return instance!
    } 
    
    func getMessage() -> String
    { return message }
    
    static func newIndexingException() -> IndexingException
    { let ex = IndexingException()
      return ex
    }
    
    static func newIndexingException(m : String) -> IndexingException
    { let ex = IndexingException()
      ex.message = m
      return ex
    }

    func printStackTrace() -> Void
    { print(localizedDescription) }
}

class IOException : Error
{  var message : String
   private static var instance : IOException? = nil
    
    init()
    { message = "IO exception" }
    
    init(copyFrom: IOException)
    { message = copyFrom.message }
    
    static func defaultInstance() -> IOException
    { if instance == nil 
      { instance = IOException() } 
      return instance!
    } 

    func getMessage() -> String
    { return message }
    
    static func newIOException() -> IOException
    { let ex = IOException()
      return ex
    }
    
    static func newIOException(m : String) -> IOException
    { let ex = IOException()
      ex.message = m
      return ex
    }

    func printStackTrace() -> Void
    { print(localizedDescription) }

}

class IncorrectElementException : Error
{ private static var instance : IncorrectElementException? = nil

  var message : String
         
  init()
  { message = "Incorrect element exception" }
        
  init(copyFrom: IncorrectElementException)
  { message = copyFrom.message }
        
  static func defaultInstance() -> IncorrectElementException
  { if instance == nil 
    { instance = IncorrectElementException() } 
    return instance!
  } 

   func getMessage() -> String
   { return message }
        
   static func newIncorrectElementException() -> IncorrectElementException
   { let ex = IncorrectElementException()
     return ex
   }
        
   static func newIncorrectElementException(m : String) -> IncorrectElementException
   { let ex = IncorrectElementException()
     ex.message = m
     return ex
   }

   func printStackTrace() -> Void
   { print(localizedDescription) }

}

class CastingException : Error
{ private static var instance : CastingException? = nil

   var message : String
        
   init()
   { message = "Casting exception" }
        
   init(copyFrom: CastingException)
   { message = copyFrom.message }
        
   static func defaultInstance() -> CastingException
   { if instance == nil 
     { instance = CastingException() } 
     return instance!
   } 

   func getMessage() -> String
   { return message }
        
   static func newCastingException() -> CastingException
   { let ex = CastingException()
     return ex
   }
        
   static func newCastingException(m : String) -> CastingException
   { let ex = CastingException()
     ex.message = m
     return ex
   }

   func printStackTrace() -> Void
   { print(localizedDescription) }

}

class ArithmeticException : Error
{ private static var instance : ArithmeticException? = nil

    var message : String
        
    init()
    { message = "Arithmetic exception" }
        
    init(copyFrom: ArithmeticException)
    { message = copyFrom.message }
        
   static func defaultInstance() -> ArithmeticException
   { if instance == nil 
     { instance = ArithmeticException() } 
     return instance!
   } 

    func getMessage() -> String
    { return message }
        
    static func newArithmeticException() -> ArithmeticException
    { let ex = ArithmeticException()
      return ex
    }
        
    static func newArithmeticException(m : String) -> ArithmeticException
    { let ex = ArithmeticException()
      ex.message = m
      return ex
    }

    func printStackTrace() -> Void
    { print(localizedDescription) }
}

class AssertionException : Error
{ private static var instance : AssertionException? = nil

    var message : String
        
    init()
    { message = "Assertion exception" }
        
    init(copyFrom: AssertionException)
    { message = copyFrom.message }
        
   static func defaultInstance() -> AssertionException
   { if instance == nil 
     { instance = AssertionException() } 
     return instance!
   } 

    func getMessage() -> String
    { return message }
        
    static func newAssertionException() -> AssertionException
    { let ex = AssertionException()
      return ex
    }
        
    static func newAssertionException(m : String) -> AssertionException
    { let ex = AssertionException()
      ex.message = m
      return ex
    }
    
    func printStackTrace() -> Void
    { print(localizedDescription) }
}


class ProgramException : Error
{ private static var instance : ProgramException? = nil

    var message : String
        
    init()
    { message = "Program exception" }
        
    init(copyFrom: ProgramException)
    { message = copyFrom.message }
        
   static func defaultInstance() -> ProgramException
   { if instance == nil 
     { instance = ProgramException() } 
     return instance!
   } 

    func getMessage() -> String
    { return message }
        
    static func newProgramException() -> ProgramException
    { let ex = ProgramException()
      return ex
    }
        
    static func newProgramException(m : String) -> ProgramException
    { let ex = ProgramException()
      ex.message = m
      return ex
    }
    
    func printStackTrace() -> Void
    { print(localizedDescription) }
    
}

class SystemException : Error
{ private static var instance : SystemException? = nil

    var message : String
        
    init()
    { message = "System exception" }
        
    init(copyFrom: SystemException)
    { message = copyFrom.message }
        
   static func defaultInstance() -> SystemException
   { if instance == nil 
     { instance = SystemException() } 
     return instance!
   } 

    func getMessage() -> String
    { return message }
        
    static func newSystemException() -> SystemException
    { let ex = SystemException()
      return ex
    }
        
    static func newSystemException(m : String) -> SystemException
    { let ex = SystemException()
      ex.message = m
      return ex
    }
    
    func printStackTrace() -> Void
    { print(localizedDescription) }
}

class NullAccessException : Error
{ private static var instance : NullAccessException? = nil
  var message : String
    
    init()
    { message = "Null access exception" }
        
    init(copyFrom: NullAccessException)
    { message = copyFrom.message }
        
   static func defaultInstance() -> NullAccessException
   { if instance == nil 
     { instance = NullAccessException() } 
     return instance!
   } 

    func getMessage() -> String
    { return message }
        
    static func newNullAccessException() -> NullAccessException
    { let ex = NullAccessException()
      return ex
    }
        
    static func newNullAccessException(m : String) -> NullAccessException
    { let ex = NullAccessException()
      ex.message = m
      return ex
    }
    
    func printStackTrace() -> Void
    { print(localizedDescription) }
    
}

class AccessingException : Error
{ private static var instance : AccessingException? = nil

    var message : String
        
    init()
    { message = "Accessing exception" }
        
    init(copyFrom: AccessingException)
    { message = copyFrom.message }
        
   static func defaultInstance() -> AccessingException
   { if instance == nil 
     { instance = AccessingException() } 
     return instance!
   } 

    func getMessage() -> String
    { return message }
        
    static func newAccessingException() -> AccessingException
    { let ex = AccessingException()
      return ex
    }
        
    static func newAccessingException(m : String) -> AccessingException
    { let ex = AccessingException()
      ex.message = m
      return ex
    }
    
    func printStackTrace() -> Void
    { print(localizedDescription) }
}

