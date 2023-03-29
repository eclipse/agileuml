package ocldate

import "container/list"
import "time"
import "ocl"

var SystemTime int64

type OclDate struct {
  time int64
  year int
  month int
  day int
  weekday int
  hour int
  minute int
  second int
}

func createOclDate() *OclDate {
  var res *OclDate
  res = &OclDate{}
  return res
}

func NewOclDate() *OclDate {
  var result *OclDate
  result = createOclDate()
  t := time.Now()
  
  result.time = t.Unix()*1000
  SystemTime = result.time
  result.year = t.Year()
  result.month = int(t.Month())
  result.day = t.Day()
  result.weekday = int(t.Weekday())
  result.hour = t.Hour()
  result.minute = t.Minute()
  result.second = t.Second()

  return result
}

func NewOclDate_Time(t int64) *OclDate {
  var result *OclDate
  result = createOclDate()
  result.time = t
  tt := time.Unix(t/1000,0)
  result.year = tt.Year()
  result.month = int(tt.Month())
  result.day = tt.Day()
  result.weekday = int(tt.Weekday())
  result.hour = tt.Hour()
  result.minute = tt.Minute()
  result.second = tt.Second()
  return result
}

func NewOclDate_YMD(y int, m int, d int) *OclDate {
  var result *OclDate
  result = createOclDate()
  leapyears := (y-1968)/4
  var t int64 = 0
  t = int64((y-1970)*31536000000) + int64((m-1)*2628000000) + int64((d+leapyears-1)*86400000) 
  tt := time.Unix(t/1000,0)
  result.time = t
  result.year = y
  result.month = m
  result.day = d
  result.weekday = int(tt.Weekday())
  result.hour = 0
  result.minute = 0
  result.second = 0
  return result
}

func NewOclDate_YMDHMS(y int, m int, d int, h int, mn int, s int) *OclDate {
  var result *OclDate
  result = createOclDate()
  leapyears := (y-1968)/4
  var t int64 = 0
  t = int64((y-1970)*31536000000) + int64((m-1)*2628000000) + int64((d+leapyears-1)*86400000) + int64(h*3600000) + int64(mn*60000) + int64(s*1000)
  tt := time.Unix(t/1000,0)
  result.time = t
  result.year = y
  result.month = m
  result.day = d
  result.weekday = int(tt.Weekday())
  result.hour = h
  result.minute = mn
  result.second = s
  return result
}

func (self *OclDate) SetTime(t int64) { 
  tt := time.Unix(t/1000,0)
  self.time = t
  self.year = tt.Year()
  self.month = int(tt.Month())
  self.day = tt.Day()
  self.weekday = int(tt.Weekday())
  self.hour = tt.Hour()
  self.minute = tt.Minute()
  self.second = tt.Second()
}

func newOclDate_String(s string) *OclDate {
  var items *list.List = list.New()
  items = ocl.AllMatches(s, "[0-9]+")
  var d *OclDate = createOclDate()
  if items.Len() >= 3 {
    d.year = ocl.ToInteger(ocl.At(items,1).(string))
    d.month = ocl.ToInteger(ocl.At(items,2).(string))
    d.day = ocl.ToInteger(ocl.At(items,3).(string))
  } 
  if items.Len() >= 6 {
    d.hour = ocl.ToInteger(ocl.At(items,4).(string))
    d.minute = ocl.ToInteger(ocl.At(items,5).(string))
    d.second = ocl.ToInteger(ocl.At(items,6).(string))
  } 
  return d
}

func (self *OclDate) ToString() string { 
   tt := time.Unix(self.time/1000,0)
   return tt.Format(time.UnixDate)
} 


func (self *OclDate) GetTime() int64 { 
  return self.time
}

func GetSystemTime() int64 {
  t := time.Now()
  SystemTime = t.Unix()*1000
  // return time.UnixMilli(time.Now())
  return SystemTime
}

func (self *OclDate) GetYear() int { 
  return self.year
} 

func (self *OclDate) GetMonth() int { 
  return self.month
} 

func (self *OclDate) GetDate() int { 
  return self.day
} 

func (self *OclDate) GetDay() int { 
  return self.weekday
} 

func (self *OclDate) GetHour() int { 
  return self.hour
} 

func (self *OclDate) GetMinute() int { 
  return self.minute
} 

func (self *OclDate) GetSecond() int { 
  return self.second
} 

func (self *OclDate) GetMinutes() int { 
  return self.minute
} 

func (self *OclDate) GetSeconds() int { 
  return self.second
} 

func (self *OclDate) YearDifference(d *OclDate) int64 {
  return (self.time - d.time)/(31536000000)
} 

func (self *OclDate) MonthDifference(d *OclDate) int64 {
  return (self.time - d.time)/(2628000000)
} 

func (self *OclDate) DayDifference(d *OclDate) int64 {
  return (self.time - d.time)/(86400000)
} 

func (self *OclDate) HourDifference(d *OclDate) int64 {
  return (self.time - d.time)/(3600000)
}

func (self *OclDate) MinuteDifference(d *OclDate) int64 {
  return (self.time - d.time)/(60000)
}
 
func (self *OclDate) SecondDifference(d *OclDate) int64 {
  return (self.time - d.time)/(1000)
} 

func (self *OclDate) AddYears(y int) *OclDate {
  newtime := self.time + int64(y*31536000000)
  return NewOclDate_Time(newtime)
} 

func (self *OclDate) AddMonths(mn int) *OclDate {
  newtime := self.time + int64(mn*2628000000)
  return NewOclDate_Time(newtime)
} 

func (self *OclDate) AddDays(d int) *OclDate {
  newtime := self.time + int64(d*86400000)
  return NewOclDate_Time(newtime)
} 

func (self *OclDate) AddHours(h int) *OclDate {
  newtime := self.time + int64(h*3600000)
  return NewOclDate_Time(newtime)
} 

func (self *OclDate) AddMinutes(m int) *OclDate {
  newtime := self.time + int64(m*60000)
  return NewOclDate_Time(newtime)
} 

func (self *OclDate) AddSeconds(s int) *OclDate {
  newtime := self.time + int64(s*1000)
  return NewOclDate_Time(newtime)
} 




func (self *OclDate) DateAfter(d *OclDate) bool { 
  if self.time > d.time { 
    return true
  } 
  return false
} 

func (self *OclDate) DateBefore(d *OclDate) bool { 
  if self.time < d.time { 
    return true
  } 
  return false
} 









