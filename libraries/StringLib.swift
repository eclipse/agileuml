import Foundation
import Darwin


class StringLib
{ 

  static func leftTrim(s : String) -> String
  {
    var result : String = ""
    result = Ocl.stringSubrange(str: s, st: Ocl.indexOf(str: s, ch: Ocl.trim(str: s)), en: s.count)
    return result
  }

  static func rightTrim(s : String) -> String
  {
    var result : String = ""
    let x : String = Ocl.trim(str: s)
    result = Ocl.before(str: s, sep: x) + x
    return result
  }

  static func padLeftWithInto(s : String, c : String, n : Int) -> String
  {
    var result : String = ""
    result = Ocl.sum(s: Ocl.integerSubrange(st: 1, en: n - s.count).map({var1 in c})) + s
    return result
  }


  static func leftAlignInto(s : String, n : Int) -> String
  {
    var result : String = ""
    if n <= s.count
    {
      result = Ocl.stringSubrange(str: s, st: 1, en: n)
    }
    else {
      if n > s.count
      {
        result = s + Ocl.sum(s: Ocl.integerSubrange(st: 1, en: n - s.count).map({var3 in " "}))
      }
    }
    return result
  }

  static func rightAlignInto(s : String, n : Int) -> String
  {
    var result : String = ""
    if n <= s.count
    {
      result = Ocl.stringSubrange(str: s, st: 1, en: n)
    }
    else {
      if n > s.count
      {
        result = Ocl.sum(s: Ocl.integerSubrange(st: 1, en: n - s.count).map({var5 in " "})) + s
      }
    }
    return result
  }

  static func format(fmt : String, sq : [CVarArg]) -> String
  { let swiftfmt = fmt.replacingOccurrences(of: "%s", with: "%@")
    let txt = String(format: swiftfmt, arguments: sq)
    return txt
  }

  static func scan(s : String, fmt : String) -> [String]
  { var result : [String] = []
    
    var ind : Int = 0 // s upto ind has been consumed
    let slen : Int = s.count

    var i : Int = 0
    while i < fmt.count  
    { let c : String = Ocl.at(str: fmt, ind: i+1) 
      if c == "%" && i < fmt.count - 1  
      { let d : String = Ocl.at(str: fmt, ind: i+2) 
        if d == "s"  
        { var schars : String = ""
          for j in ind...slen  
          { let z : String = Ocl.at(str: s, ind: j+1) 
            if Ocl.iswhitespace(s: z) 
            { break }
            else 
            { schars = schars + z }
          }
          result.append(schars)
          ind = ind + schars.count
          i = i + 1  
        }
        else if d == "d"
        { var ichars : String = ""
          for j in ind...slen  
          { let z : String = Ocl.at(str: s, ind: j+1) 
            if Ocl.isMatch(str: z, pattern: "[0-9]") 
            { ichars = ichars + z }
            else 
            { break }
          }
          result.append(ichars)
          ind = ind + ichars.count
          i = i + 1  
        }
        else if d == "f"
        { var fchars : String = ""
          for j in ind...slen  
          { let z : String = Ocl.at(str: s, ind: j+1) 
            if Ocl.isMatch(str: z, pattern: "[0-9.]") 
            { fchars = fchars + z }
            else 
            { break }
          }
          result.append(fchars)
          ind = ind + fchars.count
          i = i + 1  
        }
      }
      else 
      { if Ocl.at(str: s, ind: ind+1) == c   
        { ind = ind+1 }  
        else 
        { return result }
      }
      i = i + 1
    }
    return result 
  }
}




