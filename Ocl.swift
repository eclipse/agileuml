//
//  Ocl.swift
//
//  Created by Kevin Lano on 31/12/2020.
//

import Foundation
import Darwin

/******************************
* Copyright (c) 2003,2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* OCL library for Swift version 4+ */

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

  static func sequenceSubtract<T : Equatable>(s1 : [T], s2 : [T]) -> [T]
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


  static func containsSequence<T : Equatable>(s : [T], x : T) -> Bool
  { for y in s
    { if x == y
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

  static func count<T : Equatable>(s : [T], x : T) -> Int
  { var result : Int = 0
    result = s.filter{ $0 == x }.count
	return result
  } 
  // But it may be more efficient just to loop over s, testing ==
  
  static func sum(s : [Int]) -> Int
  { var result : Int = 0
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

  static func mapRange<T>(m : Dictionary<String,T>) -> [T]
  { var result : [T] = [T]()
    for (_,v) in m
    { result.append(v) }
    return result
  }

  static func mapKeys<T>(m : Dictionary<String,T>) -> [String]
  { var result : [String] = [String]()
    for (k,_) in m
    { result.append(k) }
    return result
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
   
   let sepchars = chars(str: ch)
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

  static func insertAt<T : Equatable>(s1 : [T], s2 : [T], ind : Int) -> [T]
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

  static func insertAtString(s1 : String, s2 : String, ind : Int) -> String
  { var result : [Character] = [Character]()
    let seq1 = Ocl.chars(str: s1)
    let seq2 = Ocl.chars(str: s2)
    result = Ocl.insertAt(s1: seq1, s2: seq2, ind: ind)
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

     if prev < str.count
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
}
