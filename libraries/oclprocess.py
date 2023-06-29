import threading
import time
import os


class OclProcess:
  oclprocess_instances = []
  oclprocess_index = dict({})

  def __init__(self):
    self.name = ""
    self.priority = 0
    self.actualThread = None
    self.executes = None
    self.osProcess = 0
    self.deadline = 0
    self.delay = 0
    self.period = 0
    OclProcess.oclprocess_instances.append(self)


  def notify(obj) :
    obj.notify()

  def notifyAll(obj) :
    obj.notify_all()

  def wait(obj,t) :
    obj.wait(t/1000.0)

  def waitFor(self) :
    if self.osProcess > 0 : 
      (x,y) = os.waitpid(self.osProcess,0)
      return y
    return 0

  def getRuntime() : 
    return OclProcess.currentThread()

  def newOclProcess(obj,s) :
    result = None
    p = createOclProcess()
    p.name = s
    if obj != None : 
      p.actualThread = threading.Thread(target=obj,name=s)
      p.executes = obj
    else : 
      p.actualThread = None
      pid = os.spawnv(os.P_NOWAIT, s, ["."])
      p.osProcess = pid
    p.priority = 5
    result = p
    return result

  def getDeadline(self) : 
    return self.deadline

  def setDeadline(self,d) : 
    self.deadline = d

  def setDelay(self,d) : 
    self.delay = d

  def setPeriod(self,p) :
    self.period = p

  def setPriority(self,p) : 
    self.priority = p

  def activeCount() :
    return threading.active_count()

  def currentThread() :
    return threading.current_thread()

  def allActiveThreads() : 
    return threading.enumerate()

  def sleep(n) :
    time.sleep(n/1000.0)

  def sleepSeconds(d) :
    time.sleep(d)

  def getName(self) : 
    result = self.name
    if self.actualThread != None : 
      result = self.actualThread.name
    return result

  def setName(self,nme) : 
    self.name = nme
    if self.actualThread != None : 
      self.actualThread.name = nme
    return result

  def getPriority(self) :
    return self.priority

  def run(self) :
    if self.actualThread != None : 
      if hasattr(self.executes, "deadline") and hasattr(self.executes, "delay") and hasattr(self.executes, "period") and self.executes.deadline == 0 and self.executes.delay == 0 and self.executes.period == 0 :
        self.actualThread.run()
      elif hasattr(self.executes, "deadline") and hasattr(self.executes, "delay") and hasattr(self.executes, "period"): 
        now = time.time()*1000
        if self.executes.deadline > now :
          time.sleep((self.executes.deadline - now)/1000)  
        if self.executes.delay > 0 :
          time.sleep(self.executes.delay/1000) 
        if self.executes.period <= 0 : 
          self.actualThread.run()
        else : 
          next = now + self.executes.period
          while True : 
            self.actualThread.run() 
            now = time.time()*1000
            next = next + self.executes.period  
            if next > now :
              time.sleep((next - now)/1000)
            self.actualThread = threading.Thread(target=self.executes,name=self.name)
      else : 
        self.actualThread.run()

  def start(self) :
    if self.actualThread != None : 
      if hasattr(self.executes, "deadline") and hasattr(self.executes, "delay") and self.executes.deadline == 0 and self.executes.delay == 0 :
        self.actualThread.start()
      elif hasattr(self.executes, "deadline") and hasattr(self.executes, "delay") : 
        now = time.time()*1000
        if self.executes.deadline > now :
          time.sleep((self.executes.deadline - now)/1000)  
        if self.executes.delay > 0 :
          time.sleep(self.executes.delay/1000) 
        self.actualThread.start()
      else : 
        self.actualThread.start()

  def startProcess(self) : 
    self.start()
    return self

  def isAlive(self) : 
    if self.actualThread != None : 
      return self.actualThread.is_alive()
    return False

  def isDaemon(self) : 
    if self.actualThread != None : 
      return self.actualThread.daemon
    return False

  def join(self,ms) :
    if self.actualThread != None : 
      self.actualThread.join(ms/1000.0)

  def destroy(self) :
    if self.actualThread != None : 
      del self.actualThread
      self.actualThread = None

  def interrupt(self) :
    pass

  def cancel(self) :
    pass

  def getEnvironmentProperty(var) : 
    if var in os.environ : 
      return os.environ[var]
    return ""

  def getEnvironmentProperties() : 
    props = os.environ
    res = dict([])
    for k in props : 
      res[k] = props[k]
    return res

  def setEnvironmentProperty(var,value) : 
    res = ""
    if var in os.environ : 
      res = os.environ[var]
    os.environ[var] = value
    return res

  def clearEnvironmentProperty(var) : 
    res = ""
    if var in os.environ : 
      res = os.environ[var]
    del os.environ[var]
    return res

  def exit(n) : 
    exit(n)

  def killOclProcess(oclprocess_x) :
    oclprocess_instances = ocl.excludingSet(oclprocess_instances, oclprocess_x)
    free(oclprocess_x)

def createOclProcess():
  oclprocess = OclProcess()
  return oclprocess

def allInstances_OclProcess():
  return OclProcess.oclprocess_instances


# p = OclProcess.newOclProcess(None, "C:/Program Files/Windows NT/Accessories/wordpad.exe")
# print("Process id = " + str(p.osProcess))
# ec = p.waitFor()
# print(ec)

# print(OclProcess.getEnvironmentProperties())
# OclProcess.setEnvironmentProperty("alpha", "C:\\alpha")
# print(OclProcess.getEnvironmentProperty("alpha"))
