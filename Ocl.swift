import Foundation
import Glibc

/******************************
* Copyright (c) 2003,2020 Kevin Lano
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


  static func count<T : Equatable>(s : [T], x : T) -> Int
  { var result : Int = 0
    for y in s
    { if x == y 
      { result = result + 1 }
    }
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

  static func collectMap<T,R>(m : Dictionary<String,T>, f : (T) -> R) -> Dictionary<String,R>
  { var result : Dictionary<String,R> = [String:R]()
    for (k,v) in m
    { result[k] = f(v) }
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

 static func indexOf(str: String, ch: String) -> Int
{ var count = 0
  for index in str.indices
  { let c : Character = str[index]
    count = count + 1
    if (String(c) == ch) 
    { return count } 
  } 
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
    print(subtracted)
    print(result)
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

  static func characters(str: String) -> [Character]
  { var res : [Character] = [Character]()
    for ind in str.indices
    { res.append(str[ind]) } 
    return res
  }

  static func insertAtString(s1 : String, s2 : String, ind : Int) -> String
  { var result : [Character] = [Character]()
    let seq1 = Ocl.characters(str: s1)
    let seq2 = Ocl.characters(str: s2)
    result = Ocl.insertAt(s1: seq1, s2: seq2, ind: ind)
    return String(result)
  }

  static func reverseString(s : String) -> String
  { let result : [Character] = Ocl.characters(str: s)
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

  /* Only for Swift 5+ 
  static func before(str: String, sep: String) -> String
  { if let ind = str.firstIndex(of: sep)
    { return String(str[..<ind]) } 
    return str
  } */ 

}

