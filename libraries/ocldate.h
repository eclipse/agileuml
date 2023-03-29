#include <time.h>


/* C header for OclDate library */


struct OclDate {
  long long time; 
  long long systemTime; 
  int year;
  int month;
  int day; 
  int weekday; 
  int hour; 
  int minute; 
  int second; 
};

struct OclDate* newOclDate(void)
{ struct OclDate* res = (struct OclDate*) malloc(sizeof(struct OclDate));
  time_t tp = time(NULL); 
  res->systemTime = (long long) tp*1000;
  res->time = res->systemTime; 
  struct tm* dd = localtime(&tp); 
  res->year = dd->tm_year + 1900;  
  res->month = dd->tm_mon + 1;
  res->day = dd->tm_mday;  
  res->weekday = dd->tm_wday; 
  res->hour = dd->tm_hour;  
  res->minute = dd->tm_min;  
  res->second = dd->tm_sec;  
  
  return res; 
}

struct OclDate* newOclDate_Time(long long t)
{ struct OclDate* res = (struct OclDate*) malloc(sizeof(struct OclDate));
  res->systemTime = t;
  res->time = res->systemTime; 
  time_t secs = (time_t) (t/1000); 
  struct tm* dd = localtime(&secs); 
  res->year = dd->tm_year + 1900;  
  res->month = dd->tm_mon + 1;
  res->day = dd->tm_mday;  
  res->weekday = dd->tm_wday; 
  res->hour = dd->tm_hour;  
  res->minute = dd->tm_min;  
  res->second = dd->tm_sec;  
  
  return res; 
}

struct OclDate* newOclDate_YMD(int yr, int mn, int da)
{ struct OclDate* res = (struct OclDate*) malloc(sizeof(struct OclDate));
  res->year = yr;  
  res->month = mn;
  res->day = da;  
  res->weekday = 0; 
  res->hour = 0;  
  res->minute = 0;  
  res->second = 0;  
  struct tm* dd = (struct tm*) malloc(sizeof(struct tm));
  dd->tm_year = yr - 1900;
  dd->tm_mon = mn - 1;
  dd->tm_mday = da;
  dd->tm_hour = 0;
  dd->tm_min = 0;
  dd->tm_sec = 0;
  time_t xt = mktime(dd); 
  res->time = (long long) xt*1000; 
  return res; 
}

struct OclDate* newOclDate_YMDHMS(int yr, int mn, int da, int h, int m, int s)
{ struct OclDate* res = (struct OclDate*) malloc(sizeof(struct OclDate));
  res->year = yr;  
  res->month = mn;
  res->day = da;  
  res->weekday = 0; 
  res->hour = h;  
  res->minute = m;  
  res->second = s;  
  struct tm* dd = (struct tm*) malloc(sizeof(struct tm));
  dd->tm_year = yr - 1900;
  dd->tm_mon = mn - 1;
  dd->tm_mday = da;
  dd->tm_hour = h;
  dd->tm_min = m;
  dd->tm_sec = s;
  time_t xt = mktime(dd); 
  res->time = (long long) xt*1000; 
  
  return res; 
}


void setTime_OclDate(struct OclDate* self, long long t)
{ self->time = t;
  time_t secs = (time_t) (t/1000); 
  struct tm* dd = localtime(&secs); 
  self->year = dd->tm_year + 1900;  
  self->month = dd->tm_mon + 1;
  self->day = dd->tm_mday;  
  self->weekday = dd->tm_wday; 
  self->hour = dd->tm_hour;  
  self->minute = dd->tm_min;  
  self->second = dd->tm_sec;  
} 

struct OclDate* newOclDate_String(char* s) 
{ char** items = allMatches(s, "[0-9]+"); 
  int n = length(items);
  struct OclDate* res = newOclDate_Time(0); 
  if (n >= 3)
  { res->year = toInteger(items[0]); 
    res->month = toInteger(items[1]); 
    res->day = toInteger(items[2]); 
  } 
  if (n >= 6)
  { res->hour = toInteger(items[3]); 
    res->minute = toInteger(items[4]); 
    res->second = toInteger(items[5]); 
  }
  return newOclDate_YMDHMS(res->year, res->month, res->day,
                           res->hour, res->minute, res->second); 
} 

struct OclDate* addYears_OclDate(struct OclDate* self, int y) 
{ long long newtime = self->time + (long long) y*(30758400000L); 
  return newOclDate_Time(newtime); 
} 

struct OclDate* addMonths_OclDate(struct OclDate* self, int y) 
{ long long newtime = self->time + (long long) y*(2563200000L); 
  return newOclDate_Time(newtime); 
} 

struct OclDate* addDays_OclDate(struct OclDate* self, int y) 
{ long long newtime = self->time + (long long) y*(86400000L); 
  return newOclDate_Time(newtime); 
} 

struct OclDate* addHours_OclDate(struct OclDate* self, int y) 
{ long long newtime = self->time + (long long) y*(3600000L); 
  return newOclDate_Time(newtime); 
} 

struct OclDate* addMinutes_OclDate(struct OclDate* self, int y) 
{ long long newtime = self->time + y*(60000); 
  return newOclDate_Time(newtime); 
} 

struct OclDate* addSeconds_OclDate(struct OclDate* self, int y) 
{ long long newtime = self->time + y*(1000); 
  return newOclDate_Time(newtime); 
} 


long yearDifference_OclDate(struct OclDate* self, struct OclDate* d)
{ long long result = (self->time - d->time)/31536000000L; 
  return (long) result; 
} 

long monthDifference_OclDate(struct OclDate* self, struct OclDate* d)
{ long long result = (self->time - d->time)/2563200000L; 
  return (long) result; 
} 

long dayDifference_OclDate(struct OclDate* self, struct OclDate* d)
{ long long result = (self->time - d->time)/86400000L; 
  return (long) result; 
} 

long hourDifference_OclDate(struct OclDate* self, struct OclDate* d)
{ long long result = (self->time - d->time)/3600000L; 
  return (long) result; 
} 

long minuteDifference_OclDate(struct OclDate* self, struct OclDate* d)
{ long long result = (self->time - d->time)/60000; 
  return (long) result; 
} 

long secondDifference_OclDate(struct OclDate* self, struct OclDate* d)
{ long long result = (self->time - d->time)/1000; 
  return (long) result; 
} 



char* toString_OclDate(struct OclDate* self)
{ /* const time_t secs = (const time_t) (self->time/1000); 
  return ctime(&secs); */ 
  char* res = calloc(20, sizeof(char)); 
  sprintf(res, "%d-%02d-%02d %02d:%02d:%02d", self->year, self->month, self->day,
               self->hour, self->minute, self->second);
  return res;  
} 

long long getTime_OclDate(struct OclDate* self)
{ return self->time; } 

long long getSystemTime(void)
{ return (long long) time(NULL)*1000; } 

unsigned char dateBefore_OclDate(struct OclDate* self, struct OclDate* d)
{ if (self->time < d->time)
  { return TRUE; }
  return FALSE; 
}

unsigned char dateAfter_OclDate(struct OclDate* self, struct OclDate* d)
{ if (self->time > d->time)
  { return TRUE; }
  return FALSE; 
} 

int getYear_OclDate(struct OclDate* self)
{ return self->year; }

int getMonth_OclDate(struct OclDate* self)
{ return self->month; }

int getDate_OclDate(struct OclDate* self)
{ return self->day; }

int getDay_OclDate(struct OclDate* self)
{ return self->weekday; }

int getHour_OclDate(struct OclDate* self)
{ return self->hour; }

int getMinute_OclDate(struct OclDate* self)
{ return self->minute; }

int getMinutes_OclDate(struct OclDate* self)
{ return self->minute; }

int getSecond_OclDate(struct OclDate* self)
{ return self->second; }

int getSeconds_OclDate(struct OclDate* self)
{ return self->second; }

 int compareTo_OclDate(const void* s1, const void* s2)
 { struct OclDate* c1 = (struct OclDate*) s1;
   struct OclDate* c2 = (struct OclDate*) s2;
   return compareTolonglong(c1->time,c2->time);
 }

 int compareToOclDate(struct OclDate* c1, struct OclDate* c2)
 { return compareTolonglong(c1->time,c2->time); }

