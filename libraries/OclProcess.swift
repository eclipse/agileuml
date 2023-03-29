import Foundation
import Darwin


protocol Runnable
{ func run() -> Void } 


class OclProcess
{ var name : String = ""
  var priority : Int = 5 
  var process : Thread? = nil 
  var osprocess : Process? = nil
  private static var instance : OclProcess?

  static func defaultInstance() -> OclProcess
  { if instance == nil
    { instance = OclProcess() } 
    return instance!
  } 

  static func getRuntime() -> OclProcess
  { return OclProcess.defaultInstance() } 

  static func notify(obj : NSCondition) 
  { obj.signal() }

  static func notifyAll(obj : NSCondition) 
  { obj.broadcast() } 

  static func wait(obj : NSCondition, t : Double) 
  { if t <= 0
    { obj.wait() }
    else 
    { let d = Date(timeIntervalSinceNow: t/1000.0)
      obj.wait(until: d)
    }
  } 

  func waitFor() -> Int
  { if osprocess != nil
    { osprocess!.waitUntilExit() } 
    return 0
  } 

  /* BlockOperation could be used here
     with operation!.waitUntilFinished() */ 

  static func newOclProcess(obj : Runnable, s : String) -> OclProcess
  { let p = OclProcess()
    p.name = s
    p.priority = 5
    let actualp = 
      Thread(block: { obj.run() })
    p.process = actualp
    return p
  } 

  static func newOclProcess(obj : Any?, s : String) -> OclProcess
  { let p = OclProcess()
    p.name = s
    p.priority = 5
    let actualp = Process()
    actualp.executableURL = URL(fileURLWithPath: s)
    p.osprocess = actualp
    return p
  } 

  static func activeCount() -> Int
  { return 1 } 

  static func currentThread() -> OclProcess
  { let res = OclProcess()
    res.name = "Current thread"
    res.process = Thread.current
    return res
  } 
  
  static func allActiveThreads() -> [OclProcess]
  { return [] } 

  static func sleep(n : Int64)
  { Thread.sleep(forTimeInterval: Double(n)/1000.0) }  

  func getName() -> String
  { return name } 

  func setName(nme : String)
  { name = nme }

  func getPriority() -> Int
  { return priority } 

  func run() 
  { if process != nil
    { process!.start() }
    else if osprocess != nil
    { try? osprocess!.run() } 
  } 

  func start()
  { if process != nil
    { process!.start() }
    else if osprocess != nil
    { try? osprocess!.run() } 
  }

  func isAlive() -> Bool
  { if process != nil 
    { return process!.isExecuting } 
    if osprocess != nil
    { return osprocess!.isRunning } 
    return false
  } 

  func isDaemon() -> Bool
  { return false }

  func join(ms : Double) 
  { if osprocess != nil
    { osprocess!.waitUntilExit() }
  } 

  func interrupt() 
  { } 

  func destroy() 
  { if process != nil
    { process!.cancel() }
    if osprocess != nil
    { osprocess!.terminate() } 
  } 

  static func getEnvironmentProperty(vbl : String) -> String
  { let p = ProcessInfo.processInfo
    return p.environment[vbl] ?? ""
  } 
  
  static func getEnvironmentProperties() -> [String:String]
  { let p = ProcessInfo.processInfo
    return p.environment
  } 
  
  static func exit(n : Int)
  { Thread.exit() } 
}

func displayOclProcess(_ s: OclProcess)
{ print(String(describing: s)) } 

