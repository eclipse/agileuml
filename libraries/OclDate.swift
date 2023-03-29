import Foundation
import Darwin

class OclDate : Comparable
{ static var instance : OclDate? = nil


  static let YEARMS : Int64 = 31536000000; 
  static let MONTHMS : Int64 = 2628000000;
  static let DAYMS : Int64 = 86400000; 
  static let HOURMS : Int64 = 3600000; 
  static let MINUTEMS : Int64 = 60000; 
  static let SECONDMS : Int64 = 1000;
  
  init() { }

  init(copyFrom: OclDate) {
    self.time = copyFrom.time
    self.year = copyFrom.year
    self.month = copyFrom.month
    self.day = copyFrom.day
    self.weekday = copyFrom.weekday
    self.hour = copyFrom.hour
    self.minute = copyFrom.minute
    self.second = copyFrom.second
  }
    

  func copy() -> OclDate
  { let res : OclDate = OclDate(copyFrom: self)
    addOclDate(instance: res)
    return res
  }

  static func defaultInstance() -> OclDate
  { if (instance == nil)
    { instance = createOclDate() }
    return instance!
  }

  deinit
  { killOclDate(obj: self) }

  static var systemTime : Int64 = 0

  static func getSystemTime() -> Int64
  { let d = Date()
    return Int64(d.timeIntervalSince1970 * 1000)
  }
  
    
  var time : Int64 = 0
  var year : Int = 0
  var month : Int = 0
  var day : Int = 0
  var weekday : Int = 0
  var hour : Int = 0
  var minute : Int = 0
  var second : Int = 0

  static func newOclDate() -> OclDate
  { let dte = Date()
    let d : OclDate = createOclDate()
    d.time = Int64(dte.timeIntervalSince1970)*1000
    let cal = Calendar.current
    d.year = cal.component(Calendar.Component.year, from: dte)
    d.month = cal.component(Calendar.Component.month, from: dte)
    d.day = cal.component(Calendar.Component.day, from: dte)
    d.weekday = cal.component(Calendar.Component.weekday, from: dte)
    d.hour = cal.component(Calendar.Component.hour, from: dte)
    d.minute = cal.component(Calendar.Component.minute, from: dte)
    d.second = cal.component(Calendar.Component.second, from: dte)
    return d
  }


  static func newOclDate_Time(t : Int64) -> OclDate
  {
    let d : OclDate = createOclDate()
    let dte : Date = Date(timeIntervalSince1970: TimeInterval(t/1000))
    let cal = Calendar.current
    d.year = cal.component(Calendar.Component.year, from: dte)
    d.month = cal.component(Calendar.Component.month, from: dte)
    d.day = cal.component(Calendar.Component.day, from: dte)
    d.weekday = cal.component(Calendar.Component.weekday, from: dte)
    d.hour = cal.component(Calendar.Component.hour, from: dte)
    d.minute = cal.component(Calendar.Component.minute, from: dte)
    d.second = cal.component(Calendar.Component.second, from: dte)
    
    d.time = t
    return d
  }

  static func newOclDate_YMD(y : Int, m : Int, d : Int) -> OclDate
  { let leapyears = (y - 1968)/4
    let dx : OclDate = newOclDate_Time(t: (y-1970)*OclDate.YEARMS + m*OclDate.MONTHMS + (d + leapyears)*OclDate.DAYMS)
    dx.year = y
    dx.month = m
    dx.day = d
    return dx
  } 

  static func newOclDate_YMDHMS(y : Int, m : Int, d : Int, h : Int, mn : Int, s : Int) -> OclDate
  { let leapyears = (y - 1968)/4
    let dx : OclDate = newOclDate_Time(t: (y-1970)*OclDate.YEARMS + m*OclDate.MONTHMS + (d + leapyears)*OclDate.DAYMS + h*OclDate.HOURMS + mn*OclDate.MINUTEMS + s*1000)
    dx.year = y
    dx.month = m
    dx.day = d
    dx.hour = h
    dx.minute = mn
    dx.second = s
    return dx
  } 
  
  static func newOclDate_String(s : String) -> OclDate
  { let items = Ocl.allMatches(str: s, pattern: "[0-9]+")
    if items.count >= 3 
    { let yr = Int(items[0], radix: 10)! 
      let mon = Int(items[1], radix: 10)!
      let dd = Int(items[2], radix: 10)!
    } 
    else 
    { return OclDate.newOclDate() } 

    if items.count < 6
    { return OclDate.newOclDate_YMD(y : yr, m: mon, d: dd) }
    let hr = Int(items[3], radix: 10)! 
    let mins = Int(items[4], radix: 10)!
    let secs = Int(items[5], radix: 10)!
    return OclDate.newOclDate(y : yr, m : mon, d : dd, h : hr, mn : mins, s : secs)
  } 
  

  func toString() -> String
  { let dte : Date = Date(timeIntervalSince1970: TimeInterval(time/1000))
    return String(describing: dte)
  } 

  func setTime(t : Int64) -> Void
  {
    time = t
    let dte : Date = Date(timeIntervalSince1970: TimeInterval(t/1000))
    let cal = Calendar.current
    year = cal.component(Calendar.Component.year, from: dte)
    month = cal.component(Calendar.Component.month, from: dte)
    day = cal.component(Calendar.Component.day, from: dte)
    weekday = cal.component(Calendar.Component.weekday, from: dte)
    hour = cal.component(Calendar.Component.hour, from: dte)
    minute = cal.component(Calendar.Component.minute, from: dte)
    second = cal.component(Calendar.Component.second, from: dte)
  }


  func getTime() -> Int64
  { return time }


  func dateBefore(d : OclDate) -> Bool
  {
    var result : Bool = false
    if time < d.time
    {
      result = true
    }
    else {
      result = false
    }
    return result

  }


  func dateAfter(d : OclDate) -> Bool
  {
    var result : Bool = false
    if time > d.time
    {
      result = true
    }
    else {
      result = false
    }
    return result
  }
  
  func getYear() -> Int
  { return year } 
  
  func getMonth() -> Int
  { return month } 
  
  func getDate() -> Int
  { return day } 
  
  func getDay() -> Int
  { return weekday }
  
  func getHour() -> Int
  { return hour } 
  
  func getMinute() -> Int
  { return minute }
  
  func getMinutes() -> Int
  { return minute } 
  
  func getSecond() -> Int
  { return second } 

  func getSeconds() -> Int
  { return second } 
  
  func addYears(y : Int) -> OclDate
  { let newtime = Int64(time + Int64(y)*OclDate.YEARMS)
    return OclDate.newOclDate_Time(t: newtime)
  } 

  func addMonths(m : Int) -> OclDate
  { let newtime = Int64(time + Int64(m)*OclDate.MONTHMS)
    return OclDate.newOclDate_Time(t: newtime)
  } 

  func addDays(d : Int) -> OclDate
  { let newtime = time + Int64(d)*OclDate.DAYMS
    return OclDate.newOclDate_Time(t: newtime)
  } 

  func addHours(h : Int) -> OclDate
  { let newtime = time + Int64(h)*OclDate.HOURMS
    return OclDate.newOclDate_Time(t: newtime)
  } 

  func addMinutes(m : Int) -> OclDate
  { let newtime = time + Int64(m)*OclDate.MINUTEMS
    return OclDate.newOclDate_Time(t: newtime)
  } 

  func addSeconds(s : Int) -> OclDate
  { let newtime = time + Int64(s)*OclDate.SECONDMS
    return OclDate.newOclDate_Time(t: newtime)
  } 

  func yearDifference(d: OclDate) -> Int64
  { let newtime = time - d.time
    return newtime/OclDate.YEARMS
  } 

  func monthDifference(d: OclDate) -> Int64
  { let newtime = time - d.time
    return newtime/OclDate.MONTHMS
  } 

  func dayDifference(d: OclDate) -> Int64
  { let newtime = time - d.time
    return newtime/OclDate.DAYMS
  } 

  func hourDifference(d: OclDate) -> Int64
  { let newtime = time - d.time
    return newtime/OclDate.HOURMS
  } 

  func minuteDifference(d: OclDate) -> Int64
  { let newtime = time - d.time
    return newtime/OclDate.MINUTEMS
  } 

  func secondDifference(d: OclDate) -> Int64
  { let newtime = time - d.time
    return newtime/OclDate.SECONDMS
  } 

  static func ==(lhs : OclDate, rhs : OclDate) -> Bool
  { return lhs.time == rhs.time }
  
  static func <(lhs : OclDate, rhs : OclDate) -> Bool
  { return lhs.dateBefore(d: rhs) }
}


var OclDate_allInstances : [OclDate] = [OclDate]()

func createOclDate() -> OclDate
{ let result : OclDate = OclDate()
  OclDate_allInstances.append(result)
  return result
}

func addOclDate(instance : OclDate)
{ OclDate_allInstances.append(instance) }

func killOclDate(obj: OclDate)
{ OclDate_allInstances = OclDate_allInstances.filter{ $0 !== obj } }

