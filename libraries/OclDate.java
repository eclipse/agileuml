import java.util.Date;
import java.util.ArrayList;


class OclDate implements Comparable { 

  static final long YEARMS = 31536000000L; 
  static final long MONTHMS = 2628000000L;
  static final long DAYMS = 86400000L; 
  static final long HOURMS = 3600000L; 
  static final long MINUTEMS = 60000L; 
  static final long SECONDMS = 1000L;  
   
  static OclDate createOclDate() 
  { OclDate result = new OclDate();
    return result; 
  }

  public static long systemTime = 0;
  public long time = 0;

  int year = 0;
  int month = 0;
  int day = 0;
  int weekday = 0; 
  int hour = 0;
  int minute = 0;
  int second = 0;

  public static OclDate newOclDate()
  { Date dd = new Date(); 
    OclDate d = new OclDate();
    d.time = dd.getTime();
    d.year = dd.getYear();
    d.month = dd.getMonth();
    d.day = dd.getDate(); // day of month
    d.weekday = dd.getDay(); 
    d.hour = dd.getHours(); 
    d.minute = dd.getMinutes(); 
    d.second = dd.getSeconds();   
    OclDate.systemTime = d.time; 
    return d; 
  }


  public static OclDate newOclDate_Time(long t)
  { Date dd = new Date(t); 
    
    OclDate d = new OclDate();
    d.time = t;
    d.year = dd.getYear();
    d.month = dd.getMonth();
    d.day = dd.getDate(); // day of month
    d.weekday = dd.getDay(); 
    d.hour = dd.getHours(); 
    d.minute = dd.getMinutes(); 
    d.second = dd.getSeconds();   
    return d; 
  }

  public static OclDate newOclDate_YMD(int yr, int mn, int da)
  { OclDate d = new OclDate();
    int leapyears = (yr - 1968)/4; 
    d.time = (yr - 1970)*YEARMS + (mn-1)*MONTHMS + (da+leapyears-1)*DAYMS;
    d.year = yr;
    d.month = mn;
    d.day = da; // day of month
	Date dd = new Date(d.time); 
    
    d.weekday = dd.getDay(); 
    d.hour = 0; 
    d.minute = 0; 
    d.second = 0;   
    return d; 
  }
  
  public static OclDate newOclDate_YMDHMS(int yr, int mn, int da, int hr, int mi, int sec)
  { OclDate d = new OclDate();
    int leapyears = (yr - 1968)/4; 
    d.time = (yr - 1970)*YEARMS + (mn-1)*MONTHMS + (da + leapyears -1)*DAYMS + hr*HOURMS + mi*MINUTEMS + sec*SECONDMS;
    d.year = yr;
    d.month = mn;
    d.day = da; // day of month
	Date dd = new Date(d.time); 
    
    d.weekday = dd.getDay(); 
    d.hour = hr; 
    d.minute = mi; 
    d.second = sec;   
    return d; 
  }

  public static OclDate newOclDate_String(String s)
  { ArrayList<String> items = Ocl.allMatches(s, "[0-9]+");
  
    // System.out.println(items); 
	
	if (items == null) { return null; }
	 
    int n = items.size(); 
	// year, month, day, hour, mins, secs
	
	if (n == 3) 
	{ try {
		int yr = Integer.parseInt("" + items.get(0)); 
		int mn = Integer.parseInt("" + items.get(1)); 
		int dd = Integer.parseInt("" + items.get(2)); 
		OclDate res = OclDate.newOclDate_YMD(yr,mn,dd); 
		return res; 
	  } catch (Exception ex) { return null; }
    }

	if (n == 6) 
	{ try {
		int yr = Integer.parseInt("" + items.get(0)); 
		int mn = Integer.parseInt("" + items.get(1)); 
		int dd = Integer.parseInt("" + items.get(2)); 
		int hr = Integer.parseInt("" + items.get(3)); 
		int mi = Integer.parseInt("" + items.get(4)); 
		int sec = Integer.parseInt("" + items.get(5)); 
		OclDate res = OclDate.newOclDate_YMDHMS(yr,mn,dd,hr,mi,sec); 
		return res; 
	  } catch (Exception ex) { return null; }
    }

    return null; 
  } 
  
  public Object clone()
  { return OclDate.newOclDate_Time(time); } 

  public String toString()
  { // Date dte = new Date(time); 
    // return "" + dte;
	return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;  
  } 

  public void setTime(long t)
  { Date dd = new Date(t); 
    
    time = t;
    year = dd.getYear();
    month = dd.getMonth();
    day = dd.getDate(); // day of month
    weekday = dd.getDay(); 
    hour = dd.getHours(); 
    minute = dd.getMinutes(); 
    second = dd.getSeconds();   
  }


  public long getTime()
  { return time; }

  public int getYear()
  { return year; } 

  public int getMonth()
  { return month; } 

  public int getDate()
  { return day; } 

  public int getDay()
  { return weekday; } 

  public int getHours()
  { return hour; } 

  public int getHour()
  { return hour; } 

  public int getMinutes()
  { return minute; } 

  public int getMinute()
  { return minute; } 

  public int getSeconds()
  { return second; } 

  public int getSecond()
  { return second; } 

  public OclDate addYears(int y)
  { long newtime = time + y*YEARMS;
    return newOclDate_Time(newtime);
  } 

  public OclDate addMonths(int m)
  { long newtime = time + m*MONTHMS;
    return newOclDate_Time(newtime);
  } 

  public OclDate addDays(int d)
  { long newtime = time + d*DAYMS;
    return newOclDate_Time(newtime);
  } 

  public OclDate addHours(int h)
  { long newtime = time + h*HOURMS;
    return newOclDate_Time(newtime);
  } 

  public OclDate addMinutes(int m)
  { long newtime = time + m*MINUTEMS;
    return newOclDate_Time(newtime);
  } 

  public OclDate addSeconds(int s)
  { long newtime = time + s*SECONDMS;
    return newOclDate_Time(newtime);
  } 

  public long yearDifference(OclDate d)
  { long newtime = time - d.time;
    return newtime/YEARMS;
  } 

  public long monthDifference(OclDate d)
  { long newtime = time - d.time;
    return newtime/MONTHMS;
  } 

  public long dayDifference(OclDate d)
  { long newtime = time - d.time;
    return newtime/DAYMS;
  } 

  public long hourDifference(OclDate d)
  { long newtime = time - d.time;
    return newtime/HOURMS;
  } 

  public long minuteDifference(OclDate d)
  { long newtime = time - d.time;
    return newtime/MINUTEMS;
  } 

  public long secondDifference(OclDate d)
  { long newtime = time - d.time;
    return newtime/SECONDMS;
  } 
  
  public boolean dateBefore(OclDate d)
  {
    boolean result = false;
    if (time < d.time)
    {
      result = true;
    }
    else {
      result = false;
    }
    return result;
  }

  public boolean dateAfter(OclDate d)
  {
    boolean result = false;
    if (time > d.time)
    {
      result = true;
    }
    else {
      result = false;
    }
    return result;
  }
  
  public int compareTo(Object obj)
  { if (obj instanceof OclDate)
    { OclDate dd = (OclDate) obj; 
	  if (time < dd.time) { return -1; }
	  if (time > dd.time) { return 1; }
	  return 0; 
    }
	return 0; 
  }

  public static long getSystemTime()
  { Date d = new Date(); 
    long result = d.getTime();
    OclDate.systemTime = result;
    return result;
  }

  public static void setSystemTime(long t)
  { OclDate.systemTime = t; }
  
  public static void main(String[] args)
  { OclDate d = OclDate.newOclDate_String("2022/09/29 01:00:00"); 
    System.out.println(d); 
	
	System.out.println(new Date()); 
  } 
}

