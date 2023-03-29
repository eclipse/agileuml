class OclDate {
private:
    long long time;
    long long systemTime;
    struct tm* actualDate;
    int year; 
    int month; 
    int day; 
    int weekday; 
    int hour; 
    int minute; 
    int second; 

public:
    OclDate()
    {
        time = 0;
        systemTime = 0;
        actualDate = NULL;
        year = 0; 
        month = 0; 
        day = 0; 
        weekday = 0; 
        hour = 0; 
        minute = 0; 
        second = 0; 
    }

    static long long getCurrentTime()
    {
        return (long long) 1000 * std::time(NULL);
    }

    static struct tm* getDate(long long t)
    {
        time_t tx = (time_t) t / 1000;
        struct tm* res = localtime(&tx);
        // res->tm_year = res->tm_year + 1900; 
        return res;
    }

    static OclDate* newOclDate_Time(long long t)
    {
        OclDate* res = new OclDate();
        res->time = t;
        res->systemTime = getCurrentTime();
        res->actualDate = getDate(t);
        res->year = res->actualDate->tm_year + 1900; 
        res->month = res->actualDate->tm_mon + 1;
        res->day = res->actualDate->tm_mday;
        res->weekday = res->actualDate->tm_wday;
        res->hour = res->actualDate->tm_hour;
        res->minute = res->actualDate->tm_min;
        res->second = res->actualDate->tm_sec;

        return res;
    }

    static OclDate* newOclDate()
    {
        OclDate* res = new OclDate();
        res->systemTime = getCurrentTime();
        res->time = res->systemTime;
        res->actualDate = getDate(res->time);
        res->year = res->actualDate->tm_year + 1900;
        res->month = res->actualDate->tm_mon + 1;
        res->day = res->actualDate->tm_mday;
        res->weekday = res->actualDate->tm_wday;
        res->hour = res->actualDate->tm_hour;
        res->minute = res->actualDate->tm_min;
        res->second = res->actualDate->tm_sec;
        return res;
    }

    static OclDate* newOclDate_YMD(int y, int m, int d)
    {
        OclDate* res = new OclDate();
        res->systemTime = getCurrentTime();
        res->year = y;
        res->month = m;
        res->day = m;

        res->actualDate = getDate(0);
        res->actualDate->tm_year = y - 1900;
        res->actualDate->tm_mon = m - 1;
        res->actualDate->tm_mday = d;
        res->time = mktime(res->actualDate);
        return res;
    }

    static OclDate* newOclDate_YMDHMS(int y, int m, int d, int h, int mn, int s)
    {
        OclDate* res = new OclDate();
        res->systemTime = getCurrentTime();

        res->year = y; 
        res->month = m; 
        res->day = m;
        res->hour = h;
        res->minute = mn; 
        res->second = s; 

        res->actualDate = getDate(0);
        res->actualDate->tm_year = y - 1900;
        res->actualDate->tm_mon = m - 1;
        res->actualDate->tm_mday = d;
        res->actualDate->tm_hour = h;
        res->actualDate->tm_min = mn;
        res->actualDate->tm_sec = s;
        res->time = mktime(res->actualDate);
        return res;
    }

    static OclDate* newOclDate_String(string s)
    {
        vector<string>* items = UmlRsdsLib<string>::allMatches(s, "[0-9]+");

        OclDate* dd = OclDate::newOclDate_Time(0);
        if (items->size() >= 3)
        {
            dd->year = std::stoi(items->at(0), 0, 10);
            dd->month = std::stoi(items->at(1), 0, 10);
            dd->day = std::stoi(items->at(2), 0, 10);
        }
        if (items->size() >= 6)
        {
            dd->hour = std::stoi(items->at(3), 0, 10);
            dd->minute = std::stoi(items->at(4), 0, 10);
            dd->second = std::stoi(items->at(5), 0, 10);
        }
        return OclDate::newOclDate_YMDHMS(dd->getYear(), dd->getMonth(), dd->getDay(), 
                                          dd->getHour(), dd->getMinute(), dd->getSecond());
    } 

    bool operator<(const OclDate* right) const 
    {
        return time < right->time; 
    }

    bool operator<=(const OclDate* right) const
    {
        return time <= right->time;
    }

    bool operator>(const OclDate* right) const
    {
        return time > right->time;
    }

    bool operator>=(const OclDate* right) const
    {
        return time >= right->time;
    }

    bool operator!=(const OclDate* right) const
    {
        return time != right->time;
    }

    bool operator==(const OclDate* right) const
    {
        return time == right->time;
    }

    void setTime(long long t)
    {
        time = t;
    }

    long long getTime()
    {
        return time;
    }

    long long getSystemTime()
    {
        systemTime = getCurrentTime();
        return systemTime;
    }

    bool dateBefore(OclDate* d)
    {
        if (time < d->time)
        {
            return true;
        }
        return false;
    }

    bool dateAfter(OclDate* d)
    {
        if (time > d->time)
        {
            return true;
        }
        return false;
    }

    int getYear()
    {
        return year;
    }

    int getMonth()
    {
        return month;
    }

    int getDate()
    {
        return day;
    }

    int getDay()
    {
        return weekday;
    }

    int getHour()
    {
        return hour;
    }

    int getMinute()
    {
        return minute;
    }

    int getMinutes()
    {
        return minute;
    }

    int getSecond()
    {
        return second;
    }

    int getSeconds()
    {
        return second;
    }

    OclDate* addYears(int y)
    {
        long long newtime = time + (long long) y * (30758400000L);
        return newOclDate_Time(newtime);
    }

    OclDate* addMonths(int y)
    {
        long long newtime = time + (long long) y * (2563200000L);
        return newOclDate_Time(newtime);
    }

    OclDate* addDays(int y)
    {
        long long newtime = time + (long long) y * (86400000L);
        return newOclDate_Time(newtime);
    }

    OclDate* addHours(int y)
    {
        long long newtime = time + (long long) y * (3600000L);
        return newOclDate_Time(newtime);
    }

    OclDate* addMinutes(int y)
    {
        long long newtime = time + (long long) y * (60000);
        return newOclDate_Time(newtime);
    }

    OclDate* addSeconds(int y)
    {
        long long newtime = time + (long long) y * (1000);
        return newOclDate_Time(newtime);
    }

    long yearDifference(OclDate* d)
    {
        long result = (time - d->time) / 31536000000L;
        return result;
    }

    long monthDifference(OclDate* d)
    {
        long result = (time - d->time) / 2563200000L;
        return result;
    }

    long dayDifference(OclDate* d)
    {
        long result = (time - d->time) / 86400000L;
        return result;
    }

    long hourDifference(OclDate* d)
    {
        long result = (time - d->time) / 3600000L;
        return result;
    }

    long minuteDifference(OclDate* d)
    {
        long result = (time - d->time) / 60000;
        return result;
    }

    long secondDifference(OclDate* d)
    {
        long result = (time - d->time) / 1000;
        return result;
    }

    friend ostream& operator<<(ostream& s, OclDate* d)
    {
        return s << d->getYear() << "/" << d->getMonth() << "/" << d->getDay() << " " <<
            d->getHour() << ":" << d->getMinute() << ":" << d->getSecond();
    }

    virtual string toString() {
        return "" + std::to_string(this->getYear()) + "/" + std::to_string(this->getMonth()) + "/" + 
            std::to_string(this->getDay()) + " " + std::to_string(this->getHour()) + ":" + 
            std::to_string(this->getMinute()) + ":" + std::to_string(this->getSecond());
    }
};
