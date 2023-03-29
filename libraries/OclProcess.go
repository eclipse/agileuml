package OclProcess

// import "container/list"
// import "fmt"
import "../ocl"
import "os"
// import "math"
import "os/exec"
import "sync"
import "time"


type OclProcess struct {
  name string 
  priority int 
  executes interface{}
  command *exec.Cmd

  deadline int64 
  delay int64 
  period int64 
}

func createOclProcess() *OclProcess {
  var res *OclProcess
  res = &OclProcess{}
  return res
}

func NewOclProcess(obj interface{}, s string) *OclProcess {
  var result *OclProcess
  result = createOclProcess()
  result.executes = obj
  result.name = s
  return result
}

func (self *OclProcess) SetName(s string) { 
  self.name = s
}

func (self *OclProcess) GetName() string { 
  return self.name
}

func GetRuntime() *OclProcess {
  result := NewOclProcess(nil, "System")
  return result
}

func Notify(c *sync.Cond) {
  c.Signal()
}

func NotifyAll(c *sync.Cond) {
  c.Broadcast()
}

func Wait(c *sync.Cond, t float64) {
  c.Wait()
}


func (self *OclProcess) Start() {
  if self.executes == nil { 
    cmd := exec.Command(self.name)
    self.command = cmd
    cmd.Start()
  } else { 
    go ((self.executes).(func() int))()
  }
} 

func (self *OclProcess) Run() {
  if self.executes == nil { 
    cmd := exec.Command(self.name)
    self.command = cmd
    cmd.Run()
  } else { 
    go ((self.executes).(func() int))()
  }
} 

func (self *OclProcess) WaitFor() int {
  if self.command != nil { 
    err := self.command.Wait()
    if err != nil { 
      return -1
    } 
    return 0
  } 
  return 0
}

func (self *OclProcess) SetDeadline(d int64) {
  self.deadline = d
} 

func (self *OclProcess) SetDelay(d int64) {
  self.delay = d
}

func (self *OclProcess) SetPeriod(p int64) {
  self.period = p
}

func (self *OclProcess) GetDeadline() int64 {
  return self.deadline
} 

func (self *OclProcess) GetDelay() int64 {
  return self.delay
}

func (self *OclProcess) GetPeriod() int64 {
  return self.period
}

    
func GetEnvironmentProperty(vbl string) string {
  return os.Getenv(vbl)
} 

func GetEnvironmentProperties() map[string]string { 
  res := make(map[string]string)
  vars := os.Environ()
  for i := 0; i < len(vars); i++ {
      strs := ocl.Split(vars[i],"=")
      if strs.Len() > 1 { 
        res[ocl.First(strs).(string)] = ocl.Last(strs).(string)
      } 
  } 
  return res
}

func Sleep(n int64) { 
  time.Sleep(time.Duration(n)*time.Millisecond)
}

func Exit(n int) { 
  os.Exit(n)
} 





