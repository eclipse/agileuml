import threading
import time
import ocl 
import oclfile

class OclProcess:
  oclprocess_instances = []
  oclprocess_index = dict({})

  def __init__(self):
    self.name = ""
    self.priority = 0
    self.actualThread = None
    OclProcess.oclprocess_instances.append(self)


  def notify(obj) :
    pass

  def notifyAll(obj) :
    pass

  def wait(obj,t) :
    pass

  def newOclProcess(obj,s) :
    result = None
    p = createOclProcess()
    p.name = s
    p.actualThread = threading.Thread(target=obj,name=s)
    p.priority = 5
    result = p
    return result

  def activeCount() :
    return threading.active_count()

  def currentThread() :
    return threading.current_thread()

  def allActiveThreads() : 
    return threading.enumerate()

  def sleep(n) :
    time.sleep(n/1000.0)

  def getName(self) : 
    result = self.name
    if self.actualThread != None : 
      result = self.actualThread.name
    return result

  def setName(self,nme) : 
    self.name = nme
    if self.actualThread != None : 
      self.actualThread.name = nme


  def getPriority(self) :
    return self.priority

  def run(self) :
    if self.actualThread != None : 
      self.actualThread.run()

  def start(self) :
    if self.actualThread != None : 
      self.actualThread.start()

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

  def killOclProcess(oclprocess_x) :
    oclprocess_instances = ocl.excludingSet(oclprocess_instances, oclprocess_x)
    oclfile.free(oclprocess_x)

def createOclProcess():
  oclprocess = OclProcess()
  return oclprocess

def allInstances_OclProcess():
  return OclProcess.oclprocess_instances


# print("waiting")
# OclProcess.sleep(2000)
# print("waited")

print(OclProcess.activeCount())
print(OclProcess.allActiveThreads())

print(id(None))

class Person : 
  def __init__(self) : 
    self.name = "Task"

  def __call__(self) : 
    print(self.name)
    print(self.name)
    print(self.name)
    print(self.name)

p = Person()
proc = OclProcess.newOclProcess(p,"process1")
proc.start()
print(proc.isAlive())
print(proc.isDaemon())
print(proc.getName())
proc.destroy()
