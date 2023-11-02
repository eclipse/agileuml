import ocl
import math
import re
import copy

from enum import Enum

def free(x):
  del x


def displayint(x):
  print(str(x))

def displaylong(x):
  print(str(x))

def displaydouble(x):
  print(str(x))

def displayboolean(x):
  print(str(x))

def displayString(x):
  print(x)

def displaySequence(x):
  print(x)

def displaySet(x):
  print(x)

def displayMap(x):
  print(x)


class OclDate : 
  ocldate_instances = []
  ocldate_index = dict({})
  systemTime = 0

  def __init__(self):
    self.time = 0
    self.year = 0
    self.month = 0
    self.day = 0
    self.weekday = 0
    self.hour = 0
    self.minute = 0
    self.second = 0
    OclDate.ocldate_instances.append(self)



  def newOclDate() :
    result = None
    d = createOclDate()
    d.time = OclDate.systemTime
    result = d
    return result

  def newOclDate_String(s) :
    result = None
    items = []
    items = ocl.allMatches(s, "[0-9]+")
    d = None
    d = OclDate.newOclDate_Time(0)
    if len(items) >= 3 :
      d.year = ocl.toInteger((items)[1 - 1])
      d.month = ocl.toInteger((items)[2 - 1])
      d.day = ocl.toInteger((items)[3 - 1])
    else :
      pass
    if len(items) >= 6 :
      d.hour = ocl.toInteger((items)[4 - 1])
      d.minute = ocl.toInteger((items)[5 - 1])
      d.second = ocl.toInteger((items)[6 - 1])
    else :
      pass
    return d

  def newOclDate_Time(t) :
    result = None
    d = createOclDate()
    d.time = t
    result = d
    return result

  def newOclDate_YMD(y,m,d) :
    result = None
    dte = createOclDate()
    dte.year = y
    dte.month = m
    dte.day = d
    result = dte
    return result

  def newOclDate_YMDHMS(y,m,d,h,mn,s) :
    result = None
    dte = createOclDate()
    dte.year = y
    dte.month = m
    dte.day = d
    dte.hour = h
    dte.minute = mn
    dte.second = s
    dte.result = dte
    return result

  def setTime(self, t) :
    self.time = t

  def getTime(self) :
    result = 0
    result = self.time
    return result

  def dateBefore(self, d) :
    result = False
    if self.time < d.time :
      result = True
    else :
      result = False
    return result

  def dateAfter(self, d) :
    result = False
    if self.time > d.time :
      result = True
    else :
      result = False
    return result

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
      if True :
        result = d1
    return result

  def yearDifference(self, d) :
    result = 0
    result = (self.time - d.time)//31536000000
    return result

  def monthDifference(self, d) :
    result = 0
    result = (self.time - d.time)//2628000000
    return result

  def dayDifference(self, d) :
    result = 0
    result = (self.time - d.time)//86400000
    return result

  def hourDifference(self, d) :
    result = 0
    result = (self.time - d.time)//3600000
    return result

  def minuteDifference(self, d) :
    result = 0
    result = (self.time - d.time)//60000
    return result

  def secondDifference(self, d) :
    result = 0
    result = (self.time - d.time)//1000
    return result

  def getSystemTime() :
    result = 0
    result = OclDate.systemTime
    return result

  def setSystemTime(t) :
    OclDate.systemTime = t

  def getYear(self) :
    result = 0
    result = self.year
    return result

  def getMonth(self) :
    result = 0
    result = self.month
    return result

  def getDate(self) :
    result = 0
    result = self.day
    return result

  def getDay(self) :
    result = 0
    result = self.weekday
    return result

  def getHour(self) :
    result = 0
    result = self.hour
    return result

  def getHours(self) :
    result = 0
    result = self.hour
    return result

  def getMinute(self) :
    result = 0
    result = self.minute
    return result

  def getMinutes(self) :
    result = 0
    result = self.minute
    return result

  def getSecond(self) :
    result = 0
    result = self.second
    return result

  def getSeconds(self) :
    result = 0
    result = self.second
    return result

  def addYears(self, y) :
    result = None
    d = createOclDate()
    d.time = self.time + y * 31536000000
    result = d
    return result

  def addMonths(self, m) :
    result = None
    d = createOclDate()
    d.time = self.time + m * 2628000000
    result = d
    return result

  def addMonthYMD(self, m) :
    result = None
    if self.month + m > 12 :
      result = OclDate.newOclDate_YMD(self.year + 1, (self.month + m) % 12, self.day)
    else :
      if self.month + m <= 12 :
        result = OclDate.newOclDate_YMD(self.year, self.month + m, self.day)
    return result

  def subtractMonthYMD(self, m) :
    result = None
    if self.month - m <= 0 :
      result = OclDate.newOclDate_YMD(self.year - 1, 12 - (m - self.month), self.day)
    else :
      if self.month + m <= 12 :
        result = OclDate.newOclDate_YMD(self.year, self.month - m, self.day)
    return result

  def addDays(self, d) :
    result = None
    dt = createOclDate()
    dt.time = self.time + d * 86400000
    result = dt
    return result

  def addHours(self, h) :
    result = None
    dt = createOclDate()
    dt.time = self.time + h * 3600000
    result = dt
    return result

  def addMinutes(self, n) :
    result = None
    dt = createOclDate()
    dt.time = self.time + n * 60000
    result = dt
    return result

  def addSeconds(self, s) :
    result = None
    dt = createOclDate()
    dt.time = self.time + s * 1000
    result = dt
    return result

  def toString(self) :
    result = ""
    result = str(self.day) + "-" + str(self.month) + "-" + str(self.year) + " " + str(self.hour) + ":" + str(self.minute) + ":" + str(self.second)
    return result

  def leapYear(yr) :
    result = False
    if yr % 4 == 0 and yr % 100 != 0 :
      result = True
    else :
      if yr % 400 == 0 :
        result = True
      else :
        if True :
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
          if True :
            result = 31
    return result

  def daysInMonth(self) :
    result = 0
    result = OclDate.monthDays(self.month, self.year)
    return result

  def isEndOfMonth(self) :
    result = False
    if self.day == OclDate.monthDays(self.month, self.year) :
      result = True
    else :
      if True :
        result = False
    return result

  def daysBetweenDates(d1,d2) :
    result = 0
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
      else :
        pass
    days = days + endDay - startDay
    return days

  def killOclDate(ocldate_x) :
    ocldate_instances = ocl.excludingSet(ocldate_instances, ocldate_x)
    free(ocldate_x)

def createOclDate():
  ocldate = OclDate()
  return ocldate

def allInstances_OclDate():
  return OclDate.ocldate_instances



d1 = OclDate.newOclDate_YMD(2023, 8, 1)
d2 = OclDate.newOclDate_YMD(2023, 11, 1)

print(d1.daysInMonth())
print(d2.daysInMonth())
print(d1.isLeapYear())

print(OclDate.daysBetweenDates(d1,d2))
# 31 + 30 + 31 = 92

print(d2.addMonthYMD(2).toString())
print(d1.subtractMonthYMD(2).toString())


