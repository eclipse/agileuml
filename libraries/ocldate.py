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

  def addMonthYMD(self, m) :
    result = self
    if self.month + m > 12 :
      result = OclDate.newOclDate_YMD(self.year + 1, (self.month + m) % 12, self.day)
    else :
      result = OclDate.newOclDate_YMD(self.year, self.month + m, self.day)
    return result

  def subtractMonthYMD(self, m) :
    result = self
    if self.month - m <= 0 :
      result = OclDate.newOclDate_YMD(self.year - 1, 12 - (m - self.month), self.day)
    else :
      result = OclDate.newOclDate_YMD(self.year, self.month - m, self.day)
    return result

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

  def compareToYMD(self, d) :
    result = 0
    if self.year != d.year :
      result = (self.year - d.year) * 365
    else :
      if self.year == d.year and self.month != d.month :
        result = (self.month - d.month) * 30
      else :
        if self.year == d.year and self.month == d.month :
          result = (self.day - d.day)
    return result

  def maxDateYMD(d1,d2) :
    result = None
    if 0 < d1.compareTo(d2) :
      result = d2
    else :
      result = d1
    return result

  def yearDifference(self,d) :
    newtime = self.time - d.time
    return newtime/OclDate.YEARS
   
  def monthDifference(self,d) :
    newtime = self.time - d.time
    return newtime/OclDate.MONTHS

  def differenceMonths(d1,d2) : 
    return (d1.year - d2.year)*12 + (d1.month - d2.month)

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

  def leapYear(yr) :
    result = False
    if yr % 4 == 0 and yr % 100 != 0 :
      result = True
    else :
      if yr % 400 == 0 :
        result = True
      else :
        result = False
    return result

  def isLeapYear(self) :
    result = False
    result = OclDate.leapYear(self.year)
    return result

  def monthDays(mn,yr) :
    result = 0
    if mn in set({4,6,9,11}) :
      result = 30
    else :
      if mn == 2 and OclDate.leapYear(yr) :
        result = 29
      else :
        if mn == 2 and not OclDate.leapYear(yr) :
          result = 28
        else :
          result = 31
    return result

  def daysInMonth(self) :
    return OclDate.monthDays(self.month, self.year)
    
  def isEndOfMonth(self) :
    result = False
    if self.day == OclDate.monthDays(self.month, self.year) :
      result = True
    else :
      result = False
    return result

  def daysBetweenDates(d1,d2) :
    startDay = d1.day
    startMonth = d1.month
    startYear = d1.year
    endDay = d2.day
    endMonth = d2.month
    endYear = d2.year
    days = 0

    while startYear < endYear or (startYear == endYear and startMonth < endMonth) :
      daysinmonth = OclDate.monthDays(startMonth, startYear)
      days = days + daysinmonth - startDay + 1
      startDay = 1
      startMonth = startMonth + 1
      if startMonth > 12 :
        startMonth = 1
        startYear = startYear + 1

    days = days + endDay - startDay
    return days


# d1 = OclDate.newOclDate_YMD(2023, 8, 1)
# d2 = OclDate.newOclDate_YMD(2023, 11, 1)
# print(OclDate.daysBetweenDates(d1,d2))





