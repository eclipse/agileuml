class OclDate {
private: 
  long long time; 
  long long systemTime; 
  int year; 
  int month; 
  int day; 
  int weekday; 
  int hour; 
  int minute; 
  int second; 

public:
	
  static OclDate* newOclDate_Time(long long t)
  { OclDate* res = new OclDate();
    res->time = t; 
	time_t tme = (time_t) (t/1000); 
	struct tm* dte = localtime(&tme); 
	res->year = dte->tm_year; 
	res->month = dte->tm_mon; 
	res->day = dte->tm_mday;
	res->weekday = dte->tm_wday; 
	res->hour = dte->tm_hour;
	res->minute = dte->tm_min;
	res->second = dte->tm_sec; 
    return res; 
  }

  static OclDate* newOclDate()
  { OclDate* res = new OclDate();
    res->systemTime = UmlRsdsLib<long>::getTime(); 
    res->time = res->systemTime; 
    time_t tme = (time_t) ((res->systemTime)/1000); 
    struct tm* dte = localtime(&tme); 
	res->year = dte->tm_year; 
	res->month = dte->tm_mon; 
	res->day = dte->tm_mday;
	res->weekday = dte->tm_wday; 
	res->hour = dte->tm_hour;
	res->minute = dte->tm_min;
	res->second = dte->tm_sec; 
    return res; 
  }

  static OclDate* newOclDate_YMD(int y, int m, int d)
  { OclDate* res = new OclDate();
    res->time = 0; 
    res->year = y; 
    res->month = m; 
    res->day = d;
    res->weekday = 0; 
    res->hour = 0;
    res->minute = 0;
    res->second = 0; 
    return res; 
  }

  static OclDate* newOclDate_YMDHMS(int y, int m, int d, int h, int mn, int s)
  { OclDate* res = new OclDate();
    res->time = 0; 
    res->year = y; 
    res->month = m; 
    res->day = d;
    res->weekday = 0; 
    res->hour = h;
    res->minute = mn;
    res->second = s; 
    return res; 
  }

  void setTime(long long t)
  { time = t; 
    time_t tme = (time_t) (t/1000); 
	struct tm* dte = localtime(&tme); 
	year = dte->tm_year; 
	month = dte->tm_mon; 
	day = dte->tm_mday;
	weekday = dte->tm_wday; 
	hour = dte->tm_hour;
	minute = dte->tm_min;
	second = dte->tm_sec; 
  } 

  long long getTime()
  { return time; } 

  long long getSystemTime()
  { systemTime = UmlRsdsLib<long>::getTime();
    return systemTime; 
  } 

  int getYear()
  { return year; } 

  int getMonth()
  { return month; } 

  int getDate()
  { return day; } 

  int getDay()
  { return weekday; } 

  int getHour()
  { return hour; } 

  int getMinute()
  { return minute; }

  int getMinutes()
  { return minute; } 
 
  int getSecond()
  { return second; } 

  int getSeconds()
  { return second; } 

  bool dateBefore(OclDate* d)
  { if (time < d->time)
    { return true; }
    return false; 
  }

  bool dateAfter(OclDate* d)
  { if (time > d->time)
    { return true; }
    return false; 
  } 

  string toString()
  { const time_t secs = (const time_t) (time/1000); 
    return string(ctime(&secs)); 
  } 


}; 


