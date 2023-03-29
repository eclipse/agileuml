import time
import datetime
import ocl

class OclDate: 
  YEARS = 31536000
  MONTHS = 2628000
  DAYS = 86400
  HOURS = 3600
  MINUTES = 60

  # Time is stored as seconds internally.

  def __init__(self,t = int(time.time())): 
    self.time = t
    self.today = datetime.datetime.fromtimestamp(t)
    self.year = self.today.year
    self.month = self.today.month
    self.day = self.today.day
    self.weekday = self.today.weekday()
    self.hour = self.today.hour
    self.minute = self.today.minute
    self.second = self.today.second

  def __lt__(self,other) : 
    return self.time < other.time

  def __gt__(self,other) : 
    return self.time > other.time

  def __le__(self,other) : 
    return self.time <= other.time

  def __ge__(self,other) : 
    return self.time >= other.time

  def __eq__(self,other) : 
    return self.time == other.time

  def __ne__(self,other) : 
    return self.time != other.time

  def __sub__(self,other) : 
    return self.dayDifference(other)

  def getTime(self) : 
    return 1000*self.time

  def setTime(self,t) : 
    self.time = t/1000
    self.today = datetime.datetime.fromtimestamp(t/1000)
    self.year = self.today.year
    self.month = self.today.month
    self.day = self.today.day
    self.weekday = self.today.weekday()
    self.hour = self.today.hour
    self.minute = self.today.minute
    self.second = self.today.second

  def dateAfter(self,other) : 
    return self.time > other.time

  def dateBefore(self,other) : 
    return self.time < other.time

  def getSystemTime() : 
    return int(time.time())*1000

  def setSystemTime(t) : 
    pass

  def newOclDate() : 
    t = int(time.time()) 
    d = OclDate(t)
    return d

  def newOclDate_Time(t) :   
    d = OclDate(t)
    return d

  def newOclDate_YMD(y,m,d) :   
    dte = OclDate.newOclDate_Time(0)
    dte.year = y
    dte.month = m
    dte.day = d
    leapyears = (y-1968)/4
    dte.time = (y-1970)*OclDate.YEARS + (m-1)*OclDate.MONTHS + (d + leapyears)*OclDate.DAYS 
    return dte

  def newOclDate_YMDHMS(y,m,d,h,mn,s) :   
    dte = OclDate.newOclDate_Time(0)
    dte.year = y
    dte.month = m
    dte.day = d
    dte.hour = h
    dte.minute = mn
    dte.second = s

    leapyears = (y-1968)/4
    dte.time = (y-1970)*OclDate.YEARS + (m-1)*OclDate.MONTHS + (d + leapyears)*OclDate.DAYS + h*OclDate.HOURS + mn*60 + s
    return dte

  def newOclDate_String(s) :
    items = ocl.allMatches(s, "[0-9]+")
    d = OclDate(0)
    if len(items) >= 3 :
      d.year = int(items[0])
      d.month = int(items[1])
      d.day = int(items[2])
    if len(items) >= 6 :
      d.hour = int(items[3])
      d.minute = int(items[4])
      d.second = int(items[5])
    return OclDate.newOclDate_YMDHMS(d.year, d.month, d.day, d.hour, d.minute, d.second)
    

  def getYear(self) : 
    return self.year

  def getMonth(self) : 
    return self.month

  def getDate(self) : 
    return self.day

  def getDay(self) : 
    return self.today.weekday()

  def getHour(self) : 
    return self.hour

  def getHours(self) : 
    return self.hour

  def getMinute(self) : 
    return self.minute

  def getMinutes(self) : 
    return self.minute

  def getSecond(self) : 
    return self.second

  def getSeconds(self) : 
    return self.second

  def toString(self) : 
    return str(self.year) + "-" + str(self.month) + "-" + str(self.day) + " " + str(self.hour) + ":" + str(self.minute) + ":" + str(self.second)

  def __str__(self) : 
    return self.toString()

  def addYears(self,y) : 
    newtime = self.time + y*OclDate.YEARS
    return OclDate.newOclDate_Time(newtime)

  def addMonths(self,mn) : 
    newtime = self.time + mn*OclDate.MONTHS
    return OclDate.newOclDate_Time(newtime)

  def addDays(self,d) : 
    newtime = self.time + mn*OclDate.DAYS
    return OclDate.newOclDate_Time(newtime)

  def addHours(self,h) : 
    newtime = self.time + h*OclDate.HOURS
    return OclDate.newOclDate_Time(newtime)

  def addMinutes(self,m) : 
    newtime = self.time + m*OclDate.MINUTES
    return OclDate.newOclDate_Time(newtime)

  def addSeconds(self,s) : 
    newtime = self.time + mn
    return OclDate.newOclDate_Time(newtime)

  def yearDifference(self,d) :
    newtime = self.time - d.time
    return newtime/OclDate.YEARS
   
  def monthDifference(self,d) :
    newtime = self.time - d.time
    return newtime/OclDate.MONTHS

  def dayDifference(self,d) :
    newtime = self.time - d.time
    return newtime/OclDate.DAYS
 
  def hourDifference(self,d) :
    newtime = self.time - d.time
    return newtime/OclDate.HOURS

  def minuteDifference(self,d) :
    newtime = self.time - d.time
    return newtime/OclDate.MINUTES

  def secondDifference(self,d) :
    newtime = self.time - d.time
    return newtime


# t = OclDate.newOclDate_String("2022/09/28 11:30:30")
# print(t.toString())
# t = t.addMonths(12)
# print(t.toString())





