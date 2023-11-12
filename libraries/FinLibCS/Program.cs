using System;

namespace FinLibCS
{
        using System;
        using System.Collections;
        using System.IO;
        using System.Text;
        using System.Text.RegularExpressions;
        using System.Linq;
        using System.Reflection;
        using System.Diagnostics;
        using System.Threading;
        using System.Threading.Tasks;
        using System.Xml.Serialization;
        using System.Text.Json;
        using System.Text.Json.Serialization;
        using System.Data;
        using System.Data.Common;
        using System.Data.SqlClient;
        using System.Net.Sockets;


        public class OclAttribute
        {
            string name = "";
            OclType type = null;

            public OclAttribute(string nme)
            { name = nme; }

            public void setType(OclType t)
            { type = t; }

            public string getName()
            { return name; }

            public OclType getType()
            { return type; }

            public override string ToString()
            {
                return name + " : " + type;
            }
        }

        public class OclOperation
        {
            string name = "";
            OclType type = null;
            ArrayList parameters = new ArrayList();

            public OclOperation(string nme)
            { name = nme; }

            public void setReturnType(OclType rt)
            { type = rt; }

            public void setParameters(ArrayList pars)
            { parameters = pars; }

            public string getName()
            { return name; }

            public OclType getType()
            { return type; }

            public OclType getReturnType()
            { return type; }

            public ArrayList getParameters()
            { return parameters; }

            public override string ToString()
            {
                string res = name + "(";
                for (int i = 0; i < parameters.Count; i++)
                {
                    OclAttribute par = (OclAttribute)parameters[i];
                    res = res + par.getName() + " : " + par.getType();
                    if (i < parameters.Count - 1)
                    { res = res + ", "; }
                }
                return res + ") : " + type;
            }
        }

        public class OclType
        {
            string name = "";
            public Type actualMetatype = null;
            ArrayList attributes = new ArrayList();
            ArrayList operations = new ArrayList();
            ArrayList constructors = new ArrayList();
            ArrayList superclasses = new ArrayList();

            public static Hashtable ocltypenameindex = new Hashtable(); // String --> OclType

            static OclType intType = OclType.newOclType("int", 0.GetType());

            static OclType longType = OclType.newOclType("long", 0L.GetType());

            static OclType doubleType = OclType.newOclType("double", (0.0).GetType());

            static OclType booleanType = OclType.newOclType("boolean", typeof(bool));

            static OclType stringType = OclType.newOclType("String", "".GetType());

            static OclType voidType = OclType.newOclType("void", typeof(void));

            static OclType sequenceType = OclType.newOclType("Sequence", (new ArrayList()).GetType());

            static OclType setType = OclType.newOclType("Set", (new ArrayList()).GetType());

            static OclType mapType = OclType.newOclType("Map", (new Hashtable()).GetType());

            public static OclType newOclType(string nme)
            { return OclType.createOclType(nme); }

            public static OclType newOclType(string nme, Type typ)
            {
                OclType res = OclType.createOclType(nme);
                res.actualMetatype = typ;
                return res;
            }


            public static OclType createOclType(string namex)
            {
                if (ocltypenameindex[namex] != null)
                { return (OclType)ocltypenameindex[namex]; }
                OclType ocltypex = new OclType();
                ocltypex.name = namex;
                ocltypenameindex[namex] = ocltypex;

                return ocltypex;
            }

            public static OclType getOclTypeByPK(string namex)
            { return (OclType)ocltypenameindex[namex]; }

            public static OclType getOclTypeByMetatype(Type t)
            {
                foreach (string k in ocltypenameindex.Keys)
                {
                    OclType tx = (OclType)ocltypenameindex[k];
                    if (t.Equals(tx.actualMetatype))
                    { return tx; }
                }
                return newOclType(t.Name, t);
            }

            public void setMetatype(Type t)
            { actualMetatype = t; }

            public string getName()
            { return name; }

            public ArrayList getClasses()
            { return new ArrayList(); }

            public ArrayList getDeclaredClasses()
            { return new ArrayList(); }

            public OclType getComponentType()
            {
                Type[] etypes = actualMetatype.GenericTypeArguments;
                if (etypes.Length > 0)
                {
                    Type ctype = etypes[0];
                    OclType etype = OclType.getOclTypeByMetatype(ctype);
                    return etype;
                }
                return null;
            }

            public ArrayList getFields()
            {
                if (actualMetatype != null)
                {
                    attributes.Clear();
                    FieldInfo[] fs =
                      actualMetatype.GetFields(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                    for (int i = 0; i < fs.Length; i++)
                    {
                        OclAttribute att = new OclAttribute(fs[i].Name);
                        Type t = fs[i].FieldType;
                        OclType ot = OclType.getOclTypeByMetatype(t);
                        att.setType(ot);
                        attributes.Add(att);
                    }
                }
                return attributes;
            }

            public ArrayList getDeclaredFields()
            { return getFields(); }

            public OclAttribute getField(string s)
            {
                if (actualMetatype != null)
                {
                    FieldInfo[] fs =
                      actualMetatype.GetFields(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                    for (int i = 0; i < fs.Length; i++)
                    {
                        if (s.Equals(fs[i].Name))
                        {
                            OclAttribute att = new OclAttribute(fs[i].Name);
                            Type t = fs[i].FieldType;
                            OclType ot = OclType.getOclTypeByMetatype(t);
                            att.setType(ot);
                            return att;
                        }
                    }
                }
                return null;
            }

            public OclAttribute getDeclaredField(string s)
            { return getField(s); }

            public ArrayList getMethods()
            {
                if (actualMetatype != null)
                {
                    operations.Clear();
                    MethodInfo[] fs =
                      actualMetatype.GetMethods(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                    for (int i = 0; i < fs.Length; i++)
                    {
                        OclOperation opr = new OclOperation(fs[i].Name);
                        Type rt = fs[i].ReturnType;
                        OclType ort = OclType.getOclTypeByMetatype(rt);
                        opr.setReturnType(ort);
                        ParameterInfo[] parinf = fs[i].GetParameters();
                        ArrayList pars = new ArrayList();
                        for (int j = 0; j < parinf.Count(); j++)
                        {
                            string pname = parinf[j].Name;
                            Type ptype = parinf[j].ParameterType;
                            OclAttribute paratt = new OclAttribute(pname);
                            paratt.setType(OclType.getOclTypeByMetatype(ptype));
                            pars.Add(paratt);
                        }
                        opr.setParameters(pars);
                        operations.Add(opr);
                    }
                }
                return operations;
            }

            public ArrayList getDeclaredMethods()
            { return getMethods(); }

            public ArrayList getConstructors()
            {
                if (actualMetatype != null)
                {
                    constructors.Clear();
                    ConstructorInfo[] fs =
                      actualMetatype.GetConstructors(BindingFlags.Public | BindingFlags.Static | BindingFlags.Instance | BindingFlags.NonPublic);
                    for (int i = 0; i < fs.Length; i++)
                    {
                        OclOperation opr = new OclOperation(name);
                        opr.setReturnType(this);
                        ParameterInfo[] parinf = fs[i].GetParameters();
                        ArrayList pars = new ArrayList();
                        for (int j = 0; j < parinf.Count(); j++)
                        {
                            string pname = parinf[j].Name;
                            Type ptype = parinf[j].ParameterType;
                            OclAttribute paratt = new OclAttribute(pname);
                            paratt.setType(OclType.getOclTypeByMetatype(ptype));
                            pars.Add(paratt);
                        }
                        opr.setParameters(pars);
                        constructors.Add(opr);
                    }
                }
                return constructors;
            }

            public OclType getSuperclass()
            {
                if (actualMetatype != null &&
                     actualMetatype.BaseType != null)
                {
                    OclType sup = OclType.getOclTypeByMetatype(actualMetatype.BaseType);
                    superclasses.Clear();
                    superclasses.Add(sup);
                    return sup;
                }
                return null;
            }

            public override string ToString()
            {
                if (actualMetatype != null)
                { return actualMetatype.ToString(); }
                return name;
            }

            public static bool hasAttribute(object obj, string att)
            {
                if (obj == null)
                { return false; }
                Type mt = obj.GetType();
                FieldInfo[] fs =
                  mt.GetFields(BindingFlags.Public |
                               BindingFlags.Static |
                               BindingFlags.Instance |
                               BindingFlags.NonPublic);
                for (int i = 0; i < fs.Length; i++)
                {
                    if (att.Equals(fs[i].Name))
                    { return true; }
                }
                return false;
            }

            public static object getAttributeValue(object obj, string att)
            {
                if (obj == null)
                { return null; }
                Type mt = obj.GetType();
                FieldInfo[] fs =
                  mt.GetFields(BindingFlags.Public |
                               BindingFlags.Static |
                               BindingFlags.Instance |
                               BindingFlags.NonPublic);
                for (int i = 0; i < fs.Length; i++)
                {
                    if (att.Equals(fs[i].Name))
                    { return fs[i].GetValue(obj); }
                }
                return null;
            }

            public static void setAttributeValue(
                     object obj, string att, object value)
            {
                if (obj == null)
                { return; }
                Type mt = obj.GetType();
                FieldInfo[] fs =
                  mt.GetFields(BindingFlags.Public |
                               BindingFlags.Static |
                               BindingFlags.Instance |
                               BindingFlags.NonPublic);
                for (int i = 0; i < fs.Length; i++)
                {
                    if (att.Equals(fs[i].Name))
                    { fs[i].SetValue(obj, value); }
                }
            }

            public bool isArray()
            { return name.Equals("Sequence"); }

            public bool isPrimitive()
            {
                if (name.Equals("int") ||
                    name.Equals("long") ||
                    name.Equals("double") ||
                    name.Equals("boolean"))
                { return true; }
                return false;
            }

            public bool isInterface()
            {
                if (actualMetatype != null)
                { return actualMetatype.IsInterface; }
                return false;
            }

            public object newInstance()
            {
                if (actualMetatype != null)
                {
                    MethodInfo mm = actualMetatype.GetMethod("new" + name);
                    if (mm == null) { return null; }
                    return mm.Invoke(null, null);
                }
                return null;
            }

            public bool isAssignableFrom(OclType c)
            {
                if (actualMetatype != null && c != null && c.actualMetatype != null)
                { return actualMetatype.IsAssignableFrom(c.actualMetatype); }
                return false;
            }

            public bool isInstance(object obj)
            {
                if (actualMetatype != null && obj != null)
                {
                    return actualMetatype.IsInstanceOfType(obj);
                }
                else
                { return false; }
            }

        }




        public class OclDate
        {
            private static long systemTime = 0; // internal
            private long time; // internal
            private int year; // internal
            private int month; // internal
            private int day; // internal
            private int weekday; // internal
            private int hour; // internal
            private int minute; // internal
            private int second; // internal

        const long YEARMS = 31536000000L;
        const long MONTHMS = 2628000000L;
        const long DAYMS = 86400000L;
        const long HOURMS = 3600000L;
        const long MINUTEMS = 60000L;
        const long SECONDMS = 1000L;

        public OclDate()
            {
                time = 0;
                year = 0;
                month = 0;
                day = 0;
                weekday = 0;
                hour = 0;
                minute = 0;
                second = 0;

            }



            public override string ToString()
            {
                string _res_ = "(OclDate) ";
                _res_ = _res_ + systemTime + ",";
                _res_ = _res_ + time + ",";
                _res_ = _res_ + year + ",";
                _res_ = _res_ + month + ",";
                _res_ = _res_ + day + ",";
                _res_ = _res_ + weekday + ",";
                _res_ = _res_ + hour + ",";
                _res_ = _res_ + minute + ",";
                _res_ = _res_ + second;
                return _res_;
            }

            public static void setsystemTime(long systemTime_x) { systemTime = systemTime_x; }

            public void localSetsystemTime(long systemTime_x) { }


            public static void setAllsystemTime(ArrayList ocldate_s, long val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().setsystemTime(ocldatex, val);
                }
            }


            public void settime(long time_x) { time = time_x; }


            public static void setAlltime(ArrayList ocldate_s, long val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().settime(ocldatex, val);
                }
            }


            public void setyear(int year_x) { year = year_x; }


            public static void setAllyear(ArrayList ocldate_s, int val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().setyear(ocldatex, val);
                }
            }


            public void setmonth(int month_x) { month = month_x; }


            public static void setAllmonth(ArrayList ocldate_s, int val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().setmonth(ocldatex, val);
                }
            }


            public void setday(int day_x) { day = day_x; }


            public static void setAllday(ArrayList ocldate_s, int val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().setday(ocldatex, val);
                }
            }


            public void setweekday(int weekday_x) { weekday = weekday_x; }


            public static void setAllweekday(ArrayList ocldate_s, int val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().setweekday(ocldatex, val);
                }
            }


            public void sethour(int hour_x) { hour = hour_x; }


            public static void setAllhour(ArrayList ocldate_s, int val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().sethour(ocldatex, val);
                }
            }


            public void setminute(int minute_x) { minute = minute_x; }


            public static void setAllminute(ArrayList ocldate_s, int val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().setminute(ocldatex, val);
                }
            }


            public void setsecond(int second_x) { second = second_x; }


            public static void setAllsecond(ArrayList ocldate_s, int val)
            {
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    Controller.inst().setsecond(ocldatex, val);
                }
            }


            public static long getsystemTime() { return systemTime; }

            public static ArrayList getAllsystemTime(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                if (ocldate_s.Count > 0)
                { result.Add(OclDate.systemTime); }
                return result;
            }

            public static ArrayList getAllOrderedsystemTime(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(OclDate.systemTime);
                }
                return result;
            }

            public long gettime() { return time; }

            public static ArrayList getAlltime(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.gettime())) { }
                    else { result.Add(ocldatex.gettime()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedtime(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.gettime());
                }
                return result;
            }

            public int getyear() { return year; }

            public static ArrayList getAllyear(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.getyear())) { }
                    else { result.Add(ocldatex.getyear()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedyear(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.getyear());
                }
                return result;
            }

            public int getmonth() { return month; }

            public static ArrayList getAllmonth(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.getmonth())) { }
                    else { result.Add(ocldatex.getmonth()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedmonth(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.getmonth());
                }
                return result;
            }

            public int getday() { return day; }

            public static ArrayList getAllday(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.getday())) { }
                    else { result.Add(ocldatex.getday()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedday(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.getday());
                }
                return result;
            }

            public int getweekday() { return weekday; }

            public static ArrayList getAllweekday(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.getweekday())) { }
                    else { result.Add(ocldatex.getweekday()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedweekday(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.getweekday());
                }
                return result;
            }

            public int gethour() { return hour; }

            public static ArrayList getAllhour(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.gethour())) { }
                    else { result.Add(ocldatex.gethour()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedhour(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.gethour());
                }
                return result;
            }

            public int getminute() { return minute; }

            public static ArrayList getAllminute(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.getminute())) { }
                    else { result.Add(ocldatex.getminute()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedminute(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.getminute());
                }
                return result;
            }

            public int getsecond() { return second; }

            public static ArrayList getAllsecond(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[_i];
                    if (result.Contains(ocldatex.getsecond())) { }
                    else { result.Add(ocldatex.getsecond()); }
                }
                return result;
            }

            public static ArrayList getAllOrderedsecond(ArrayList ocldate_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    result.Add(ocldatex.getsecond());
                }
                return result;
            }

        private static long getUnixTime(DateTime dd)
        {
            DateTimeOffset doff = new DateTimeOffset(dd);
            return doff.ToUnixTimeMilliseconds();
        }

        public static OclDate newOclDate()
        {
            DateTime dd = DateTime.Now;
            OclDate d = new OclDate();
            d.time = OclDate.getUnixTime(dd);
            d.year = dd.Year;
            d.month = dd.Month;
            d.day = dd.Day;
            d.weekday = (int)dd.DayOfWeek;
            d.hour = dd.Hour;
            d.minute = dd.Minute;
            d.second = dd.Second;
            OclDate.systemTime = d.time;
            return d;
        }

        public static OclDate newOclDate_String(string s)
        {
            ArrayList items = SystemTypes.allMatches(s, "[0-9]+");
            OclDate d = new OclDate();
            if (items.Count >= 3)
            {
                d.year = int.Parse((string)items[0]);
                d.month = int.Parse((string)items[1]);
                d.day = int.Parse((string)items[2]);
            }
            if (items.Count >= 6)
            {
                d.hour = int.Parse((string)items[3]);
                d.minute = int.Parse((string)items[4]);
                d.second = int.Parse((string)items[5]);
            }
            return newOclDate_YMDHMS(d.year, d.month, d.day, d.hour, d.minute, d.second);
        }

        public static OclDate newOclDate_Time(long t)
        {
            DateTime dd = DateTimeOffset.FromUnixTimeMilliseconds(t).DateTime;
            OclDate d = new OclDate();
            d.time = t;
            d.year = dd.Year;
            d.month = dd.Month;
            d.day = dd.Day;
            d.weekday = (int)dd.DayOfWeek;
            d.hour = dd.Hour;
            d.minute = dd.Minute;
            d.second = dd.Second;

            return d;
        }

        public static OclDate newOclDate_YMD(int y, int m, int d)
        {
            DateTime dt = new DateTime(y, m, d);
            OclDate dd = new OclDate();
            dd.time = OclDate.getUnixTime(dt);
            dd.year = y;
            dd.month = m;
            dd.day = d;
            dd.weekday = (int)dt.DayOfWeek;
            dd.hour = 0;
            dd.minute = 0;
            dd.second = 0;

            return dd;
        }


        public static OclDate newOclDate_YMDHMS(int y, int m, int d, int h, int mn, int sec)
        {
            DateTime dt = new DateTime(y, m, d, h, mn, sec);
            OclDate dd = new OclDate();
            dd.time = OclDate.getUnixTime(dt);
            dd.year = y;
            dd.month = m;
            dd.day = d;
            dd.weekday = (int)dt.DayOfWeek;
            dd.hour = h;
            dd.minute = mn;
            dd.second = sec;

            return dd;
        }


            public void setTime(long t)
            {
                Controller.inst().settime(this, t);
            }

            public long getTime()
            {
                long result = 0;

                result = time;
                return result;
            }


            public bool dateBefore(DateTime d)
            {
                bool result = false;

                if (time < SystemTypes.getTime(d)) { result = (bool)(true); }
                else { result = (bool)(false); }
                return result;
            }


            public bool dateAfter(DateTime d)
            {
                bool result = false;

                if (time > SystemTypes.getTime(d)) { result = (bool)(true); }
                else { result = (bool)(false); }
                return result;
            }


            public int compareToYMD(OclDate d)
            {
                int result = 0;

                if (year != d.getyear())
                {
                    result = (year - d.getyear()) * 365;
                }
                else
                {
                    if (year == d.getyear() && month != d.getmonth())
                    {
                        result = (month - d.getmonth()) * 30;
                    }
                    else if (year == d.getyear() && month == d.getmonth())
                    {
                        result = (day - d.getday());
                    }
                }
                return result;
            }

        public int CompareTo(object obj)
        {
            if (obj is OclDate)
            {
                OclDate dd = (OclDate)obj;
                if (time < dd.time) { return -1; }
                if (time > dd.time) { return 1; }
                return 0;
            }
            return 0;
        }

        public static OclDate maxDateYMD(OclDate d1, OclDate d2)
            {
                OclDate result = null;

                if (0 < d1.compareToYMD(d2))
                {
                    result = d2;
                }
                else
                    result = d1;
                return result;
            }


            public long yearDifference(DateTime d)
            {
                long result = 0;

                result = (time - SystemTypes.getTime(d)) / 31536000000;
                return result;
            }


            public long monthDifference(DateTime d)
            {
                long result = 0;

                result = (time - SystemTypes.getTime(d)) / 2628000000;
                return result;
            }


            public static int differenceMonths(OclDate d1, OclDate d2)
            {
                int result = 0;

                result = (d1.getyear() - d2.getyear()) * 12 + (d1.getmonth() - d2.getmonth());
                return result;
            }


            public long dayDifference(DateTime d)
            {
                long result = 0;

                result = (time - SystemTypes.getTime(d)) / 86400000;
                return result;
            }


            public long hourDifference(DateTime d)
            {
                long result = 0;

                result = (time - SystemTypes.getTime(d)) / 3600000;
                return result;
            }


            public long minuteDifference(DateTime d)
            {
                long result = 0;

                result = (time - SystemTypes.getTime(d)) / 60000;
                return result;
            }


            public long secondDifference(DateTime d)
            {
                long result = 0;

                result = (time - SystemTypes.getTime(d)) / 1000;
                return result;
            }


            public static long getSystemTime()
            {
                long result = 0;

                result = SystemTypes.getTime();
                return result;
            }


            public static void setSystemTime(long t)
            {
                OclDate.setsystemTime(t);
            }

            public int getYear()
            {
                int result = 0;

                result = year;
                return result;
            }


            public int getMonth()
            {
                int result = 0;

                result = month;
                return result;
            }


            public int getDate()
            {
                int result = 0;

                result = day;
                return result;
            }


            public int getDay()
            {
                int result = 0;

                result = weekday;
                return result;
            }


            public int getHour()
            {
                int result = 0;

                result = hour;
                return result;
            }


            public int getHours()
            {
                int result = 0;

                result = hour;
                return result;
            }


            public int getMinute()
            {
                int result = 0;

                result = minute;
                return result;
            }


            public int getMinutes()
            {
                int result = 0;

                result = minute;
                return result;
            }


            public int getSecond()
            {
                int result = 0;

                result = second;
                return result;
            }


            public int getSeconds()
            {
                int result = 0;

                result = second;
                return result;
            }

        public OclDate addYears(int y)
        {
            long newtime = time + y * YEARMS;
            return newOclDate_Time(newtime);
        }

        public OclDate addMonths(int m)
        {
            long newtime = time + m * MONTHMS;
            return newOclDate_Time(newtime);
        }

        public OclDate addDays(int d)
        {
            long newtime = time + d * DAYMS;
            return newOclDate_Time(newtime);
        }

        public OclDate addHours(int h)
        {
            long newtime = time + h * HOURMS;
            return newOclDate_Time(newtime);
        }

        public OclDate addMinutes(int m)
        {
            long newtime = time + m * MINUTEMS;
            return newOclDate_Time(newtime);
        }

        public OclDate addSeconds(int s)
        {
            long newtime = time + s * SECONDMS;
            return newOclDate_Time(newtime);
        }


            public OclDate addMonthYMD(int m)
            {
                OclDate result = this;

                if (month + m > 12)
                {
                    result = (OclDate.newOclDate_YMD(year + 1, (month + m) % 12, day));
                }
                else
                  if (month + m <= 12)
                {
                    result = (OclDate.newOclDate_YMD(year, month + m, day));
                }
                return result;
            }


            public OclDate subtractMonthYMD(int m)
            {
                OclDate result = this;

                if (month - m <= 0)
                {
                    result = (OclDate.newOclDate_YMD(year - 1, 12 - (m - month), day));
                }
                else
                  if (month + m <= 12)
                {
                    result = (OclDate.newOclDate_YMD(year, month - m, day));
                }
                return result;
            }



            public string toString()
            {
                string result = "";

                result = "" + day + "-" + month + "-" + year + " " + hour + ":" + minute + ":" + second;
                return result;
            }


            public static bool leapYear(int yr)
            {
                bool result = false;

                if (yr % 4 == 0 && yr % 100 != 0)
                {
                    result = true;
                }
                else
                {
                    if (yr % 400 == 0)
                    {
                        result = true;
                    }
                    else {
                        result = false;
                    }
                }
                return result;
            }


            public bool isLeapYear()
            {
                bool result = false;

                result = OclDate.leapYear(year);
                return result;
            }


            public static int monthDays(int mn, int yr)
            {
                int result = 0;

                if (4 == mn || 6 == mn || 9 == mn || 11 == mn)
                {
                    result = 30;
                }
                else
                {
                    if (mn == 2 && OclDate.leapYear(yr))
                    {
                        result = 29;
                    }
                    else
                    {
                        if (mn == 2 && !(OclDate.leapYear(yr)))
                        {
                            result = 28;
                        }
                        else
                        {
                            result = 31;
                        }
                    }
                }
                return result;
            }


            public int daysInMonth()
            {
                int result = 0;

                result = OclDate.monthDays(month, year);
                return result;
            }


            public bool isEndOfMonth()
            {
              bool result = false;

              if (day == OclDate.monthDays(month, year))
              {
                result = true;
              }
              else
              { 
                result = false; 
              }

              return result;
            }


            public static int daysBetweenDates(OclDate d1, OclDate d2)
            {
                int result = 0;

                int startDay = d1.getday();
                int startMonth = d1.getmonth();
                int startYear = d1.getyear();
                int endDay = d2.getday();
                int endMonth = d2.getmonth();
                int endYear = d2.getyear();

                int days = 0;

                while (startYear < endYear || (startYear == endYear && startMonth < endMonth))
                {
                    int daysinmonth = OclDate.monthDays(startMonth, startYear);

                    days = days + daysinmonth - startDay + 1;
                    startDay = 1;
                    startMonth = startMonth + 1;
                    if (startMonth > 12)
                    {
                        startMonth = 1;
                        startYear = startYear + 1;
                    }

                }
                days = days + endDay - startDay;
                return days;
            }

        }



        public class MathLib
        {
            private static int ix = 0; // internal
            private static int iy = 0; // internal
            private static int iz = 0; // internal
            private static double defaultTolerance = 0.0; // internal
            private static ArrayList hexdigit = (new ArrayList()); // internal

            public MathLib()
            {

            }



            public override string ToString()
            {
                string _res_ = "(MathLib) ";
                _res_ = _res_ + ix + ",";
                _res_ = _res_ + iy + ",";
                _res_ = _res_ + iz + ",";
                _res_ = _res_ + defaultTolerance + ",";
                _res_ = _res_ + hexdigit;
                return _res_;
            }

            public static void setix(int ix_x) { ix = ix_x; }

            public void localSetix(int ix_x) { }


            public static void setAllix(ArrayList mathlib_s, int val)
            {
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    Controller.inst().setix(mathlibx, val);
                }
            }


            public static void setiy(int iy_x) { iy = iy_x; }

            public void localSetiy(int iy_x) { }


            public static void setAlliy(ArrayList mathlib_s, int val)
            {
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    Controller.inst().setiy(mathlibx, val);
                }
            }


            public static void setiz(int iz_x) { iz = iz_x; }

            public void localSetiz(int iz_x) { }


            public static void setAlliz(ArrayList mathlib_s, int val)
            {
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    Controller.inst().setiz(mathlibx, val);
                }
            }


            public static void setdefaultTolerance(double defaultTolerance_x) { defaultTolerance = defaultTolerance_x; }

            public void localSetdefaultTolerance(double defaultTolerance_x) { }


            public static void setAlldefaultTolerance(ArrayList mathlib_s, double val)
            {
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    Controller.inst().setdefaultTolerance(mathlibx, val);
                }
            }


            public static void sethexdigit(ArrayList hexdigit_x) { hexdigit = hexdigit_x; }

            public void localSethexdigit(ArrayList hexdigit_x) { }


            public static void sethexdigit(int _ind, string hexdigit_x) { hexdigit[_ind] = hexdigit_x; }

            public void localSethexdigit(int _ind, string hexdigit_x) { }


            public static void addhexdigit(string hexdigit_x)
            { hexdigit.Add(hexdigit_x); }

            public static void removehexdigit(string hexdigit_x)
            { hexdigit = SystemTypes.subtract(hexdigit, hexdigit_x); }





            public static int getix() { return ix; }

            public static ArrayList getAllix(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                if (mathlib_s.Count > 0)
                { result.Add(MathLib.ix); }
                return result;
            }

            public static ArrayList getAllOrderedix(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    result.Add(MathLib.ix);
                }
                return result;
            }

            public static int getiy() { return iy; }

            public static ArrayList getAlliy(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                if (mathlib_s.Count > 0)
                { result.Add(MathLib.iy); }
                return result;
            }

            public static ArrayList getAllOrderediy(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    result.Add(MathLib.iy);
                }
                return result;
            }

            public static int getiz() { return iz; }

            public static ArrayList getAlliz(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                if (mathlib_s.Count > 0)
                { result.Add(MathLib.iz); }
                return result;
            }

            public static ArrayList getAllOrderediz(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    result.Add(MathLib.iz);
                }
                return result;
            }

            public static double getdefaultTolerance() { return defaultTolerance; }

            public static ArrayList getAlldefaultTolerance(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                if (mathlib_s.Count > 0)
                { result.Add(MathLib.defaultTolerance); }
                return result;
            }

            public static ArrayList getAllOrdereddefaultTolerance(ArrayList mathlib_s)
            {
                ArrayList result = new ArrayList();
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    result.Add(MathLib.defaultTolerance);
                }
                return result;
            }

            public static ArrayList gethexdigit() { return hexdigit; }





            public static void initialiseMathLib()
            {
                MathLib.sethexdigit(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), "0"), "1"), "2"), "3"), "4"), "5"), "6"), "7"), "8"), "9"), "A"), "B"), "C"), "D"), "E"), "F"));
                MathLib.setSeeds(1001, 781, 913);
                MathLib.setdefaultTolerance(0.001);



            }


            public static double pi()
            {
                double result = 0.0;

                result = 3.14159265;
                return result;
            }


            public static double piValue()
            {
                double result = 0.0;

                result = 3.14159265;
                return result;
            }


            public static double e()
            {
                double result = 0.0;

                result = Math.Exp(1);
                return result;
            }


            public static double eValue()
            {
                double result = 0.0;

                result = Math.Exp(1);
                return result;
            }


            public static void setSeeds(int x, int y, int z)
            {
                MathLib.setix(x);
                MathLib.setiy(y);
                MathLib.setiz(z);
            }

            public static void setSeed(int r)
            {
                MathLib.setSeeds((r % 30269), (r % 30307), (r % 30323));
            }

            public static double nrandom()
            {
                double result;
                MathLib.setix((MathLib.getix() * 171) % 30269);
                MathLib.setiy((MathLib.getiy() * 172) % 30307);
                MathLib.setiz((MathLib.getiz() * 170) % 30323);
                return (MathLib.getix() / 30269.0 + MathLib.getiy() / 30307.0 + MathLib.getiz() / 30323.0);




            }


            public static double random()
            {
                double result = 0.0;

                double r = MathLib.nrandom();
                result = (r - ((int)Math.Floor(r)));
                return result;
            }


            public static long combinatorial(int n, int m)
            {
                long result = 0;
                if (n < m || m < 0) { return result; }

                if (n - m < m)
                {
                    result = SystemTypes.prdint(SystemTypes.integerSubrange(m + 1, n)) / SystemTypes.prdint(SystemTypes.integerSubrange(1, n - m));
                }
                else
                  if (n - m >= m)
                {
                    result = SystemTypes.prdint(SystemTypes.integerSubrange(n - m + 1, n)) / SystemTypes.prdint(SystemTypes.integerSubrange(1, m));
                }
                return result;
            }


            public static long factorial(int x)
            {
                long result = 0;

                if (x < 2)
                {
                    result = 1;
                }
                else
                  if (x >= 2)
                {
                    result = SystemTypes.prdint(SystemTypes.integerSubrange(2, x));
                }
                return result;
            }


            public static double asinh(double x)
            {
                double result = 0.0;

                result = Math.Log((x + Math.Sqrt((x * x + 1))));
                return result;
            }


            public static double acosh(double x)
            {
                double result = 0.0;
                if (x < 1) { return result; }

                result = Math.Log((x + Math.Sqrt((x * x - 1))));
                return result;
            }


            public static double atanh(double x)
            {
                double result = 0.0;
                if (x == 1) { return result; }

                result = 0.5 * Math.Log(((1 + x) / (1 - x)));
                return result;
            }


            public static string decimal2bits(long x)
            {
                string result = "";

                if (x == 0) { result = "" + ""; }
                else { result = "" + MathLib.decimal2bits(x / 2) + "" + (x % 2); }
                return result;
            }


            public static string decimal2binary(long x)
            {
                string result = "";

                if (x < 0) { result = "" + "-" + MathLib.decimal2bits(-x); }
                else
                {
                    if (x == 0) { result = "" + "0"; }
                    else { result = "" + MathLib.decimal2bits(x); }
                }
                return result;
            }


            public static string decimal2oct(long x)
            {
                string result = "";

                if (x == 0) { result = "" + ""; }
                else { result = "" + MathLib.decimal2oct(x / 8) + "" + (x % 8); }
                return result;
            }


            public static string decimal2octal(long x)
            {
                string result = "";

                if (x < 0) { result = "" + "-" + MathLib.decimal2oct(-x); }
                else
                {
                    if (x == 0) { result = "" + "0"; }
                    else { result = "" + MathLib.decimal2oct(x); }
                }
                return result;
            }


            public static string decimal2hx(long x)
            {
                string result = "";

                if (x == 0) { result = "" + ""; }
                else { result = "" + MathLib.decimal2hx(x / 16) + ("" + ((string)MathLib.gethexdigit()[((int)(x % 16)) + 1 - 1])); }
                return result;
            }


            public static string decimal2hex(long x)
            {
                string result = "";

                if (x < 0) { result = "" + "-" + MathLib.decimal2hx(-x); }
                else
                {
                    if (x == 0) { result = "" + "0"; }
                    else { result = "" + MathLib.decimal2hx(x); }
                }
                return result;
            }


            public static long bytes2integer(ArrayList bs)
            {
                long result = 0;

                if ((bs).Count == 0)
                {
                    result = 0;
                }
                else
                {
                    if ((bs).Count == 1)
                    {
                        result = ((int)bs[1 - 1]);
                    }
                    else
                    {
                        if ((bs).Count == 2)
                        {
                            result = 256 * ((int)bs[1 - 1]) + ((int)bs[2 - 1]);
                        }
                        else
                    if ((bs).Count > 2)
                        {
                            result = 256 * MathLib.bytes2integer(SystemTypes.front(bs)) + ((int)SystemTypes.last(bs));
                        }
                    }
                }
                return result;
            }


            public static ArrayList integer2bytes(long x)
            {
                ArrayList result = new ArrayList();

                if ((x / 256) == 0) { result = (ArrayList)(SystemTypes.addSet((new ArrayList()), ((long)(x % 256)))); }
                else { result = (ArrayList)(SystemTypes.append(MathLib.integer2bytes(x / 256), x % 256)); }
                return result;
            }


            public static ArrayList integer2Nbytes(long x, int n)
            {
                ArrayList result = new ArrayList();

                ArrayList bs = MathLib.integer2bytes(x);
                if ((bs).Count < n)
                {
                    result = SystemTypes.concatenate(SystemTypes.collect_0(SystemTypes.integerSubrange(1, n - (bs).Count)), bs);
                }
                else
                 if ((bs).Count >= n)
                {
                    result = bs;
                }
                return result;
            }


            public static int bitwiseAnd(int x, int y)
            {
                int result;
                int x1;
                x1 = x;
                int y1;
                y1 = y;
                int k;
                k = 1;
                int res;
                res = 0;
                while ((x1 > 0 && y1 > 0))
                {
                    if (x1 % 2 == 1 && y1 % 2 == 1)
                    { res = res + k; }
                    else { { } /* No update form for: skip */ }

                    k = k * 2;
                    x1 = x1 / 2;
                    y1 = y1 / 2;



                }
                return res;









            }


            public static int bitwiseOr(int x, int y)
            {
                int result;
                int x1;
                x1 = x;
                int y1;
                y1 = y;
                int k;
                k = 1;
                int res;
                res = 0;
                while ((x1 > 0 || y1 > 0))
                {
                    if (x1 % 2 == 1 || y1 % 2 == 1)
                    { res = res + k; }
                    else { { } /* No update form for: skip */ }

                    k = k * 2;
                    x1 = x1 / 2;
                    y1 = y1 / 2;



                }
                return res;









            }


            public static int bitwiseXor(int x, int y)
            {
                int result;
                int x1;
                x1 = x;
                int y1;
                y1 = y;
                int k;
                k = 1;
                int res;
                res = 0;
                while ((x1 > 0 || y1 > 0))
                {
                    if ((x1 % 2) != (y1 % 2))
                    { res = res + k; }
                    else { { } /* No update form for: skip */ }

                    k = k * 2;
                    x1 = x1 / 2;
                    y1 = y1 / 2;



                }
                return res;









            }


            public static int bitwiseNot(int x)
            {
                int result = 0;

                result = -(x + 1);
                return result;
            }


            public static ArrayList toBitSequence(long x)
            {
                ArrayList result;
                long x1;
                x1 = x;
                ArrayList res;
                res = (ArrayList)((new ArrayList()));
                while (x1 > 0)
                {
                    if (x1 % 2 == 0)
                    { res = SystemTypes.prepend(res, false); }
                    else { res = SystemTypes.prepend(res, true); }

                    x1 = x1 / 2;

                }
                return res;





            }


            public static long modInverse(long n, long p)
            {
                long result;
                long x;
                x = (n % p);
                int i;
                i = 1;
                while (i < p)
                {
                    if (((i * x) % p) == 1)
                    { return i; }
                    else { { } /* No update form for: skip */ }

                    i = i + 1;

                }
                return 0;





            }


            public static long modPow(long n, long m, long p)
            {
                long result;
                long res;
                res = 1;
                long x;
                x = (n % p);
                int i;
                i = 1;
                while (i <= m)
                {
                    res = ((res * x) % p);
                    i = i + 1;

                }
                return res;







            }


            public static long doubleToLongBits(double d)
            {
                long result = 0;


                return result;
            }


            public static double longBitsToDouble(long x)
            {
                double result = 0.0;


                return result;
            }


            public static double roundN(double x, int n)
            {
                double result;
                double y;
                y = x * Math.Pow(10, n);
                return ((int)Math.Round(y)) / Math.Pow(10, n);


            }


            public static double truncateN(double x, int n)
            {
                double result;
                double y;
                y = x * Math.Pow(10, n);
                return ((int)y) / Math.Pow(10.0, n);


            }


            public static double toFixedPoint(double x, int m, int n)
            {
                double result;
                int y;
                y = ((int)(x * Math.Pow(10, n)));
                int z;
                z = y % ((int)Math.Pow(10, m + n));
                return z / Math.Pow(10, n);




            }


            public static double toFixedPointRound(double x, int m, int n)
            {
                double result;
                int y;
                y = ((int)((int)Math.Round((x * Math.Pow(10, n)))));
                int z;
                z = y % ((int)Math.Pow(10, m + n));
                return z / Math.Pow(10, n);




            }


            public static bool isIntegerOverflow(double x, int m)
            {
                bool result = false;

                int y = ((int)x);
                if (y > 0)
                {
                    result = (bool)((((int)Math.Log10(y)) + 1 > m));
                }
                else
                {
                    if (y < 0)
                    {
                        result = (bool)((((int)Math.Log10((-y))) + 1 > m));
                    }
                    else
                if (true)
                    {
                        result = (bool)((m < 1));
                    }
                }
                return result;
            }


            public static double mean(ArrayList sq)
            {
                double result = 0.0;
                if ((sq).Count <= 0) { return result; }

                result = SystemTypes.sumdouble(sq) / (sq).Count;
                return result;
            }


            public static double median(ArrayList sq)
            {
                double result = 0.0;
                if ((sq).Count <= 0) { return result; }

                ArrayList s1 = SystemTypes.sort(sq);
                int sze = (sq).Count;
                if (sze % 2 == 1)
                {
                    result = ((double)s1[(1 + sze) / 2 - 1]);
                }
                else
                 if (sze % 2 == 0)
                {
                    result = (((double)s1[sze / 2 - 1]) + ((double)s1[1 + (sze / 2) - 1])) / 2.0;
                }
                return result;
            }


            public static double variance(ArrayList sq)
            {
                double result = 0.0;
                if ((sq).Count <= 0) { return result; }

                double m = MathLib.mean(sq);
                result = SystemTypes.sumdouble(SystemTypes.collect_1(sq, m)) / (sq).Count;
                return result;
            }


            public static double standardDeviation(ArrayList sq)
            {
                double result = 0.0;

                if ((sq).Count == 0)
                {
                    result = 0;
                }
                else
                  if ((sq).Count > 0)
                {
                    result = Math.Sqrt(MathLib.variance(sq));
                }
                return result;
            }


            public static int lcm(int x, int y)
            {
                int result = 0;

                if (x == 0 && y == 0)
                {
                    result = 0;
                }
                else
                  if (true)
                {
                    result = ((int)((x * y) / SystemTypes.gcd(x, y)));
                }
                return result;
            }


            public static double bisectionAsc(double r, double rl, double ru, Func<double, double> f, double tol)
            {
                double result = 0.0;

                double v = f.Invoke(r);
                if (v < tol && v > -tol)
                {
                    result = r;
                }
                else
                {
                    if (v > 0)
                    {
                        result = MathLib.bisectionAsc((rl + r) / 2.0, rl, r, f, tol);
                    }
                    else
                if (v < 0)
                    {
                        result = MathLib.bisectionAsc((r + ru) / 2.0, r, ru, f, tol);
                    }
                }
                return result;
            }


            public static ArrayList rowMult(ArrayList s, ArrayList m)
            {
                ArrayList result = new ArrayList();

                result = SystemTypes.collect_3(SystemTypes.integerSubrange(1, s.Count), m, s);
                return result;
            }


            public static ArrayList matrixMultiplication(ArrayList m1, ArrayList m2)
            {
                ArrayList result = new ArrayList();

                result = SystemTypes.collect_4(m1, m2);
                return result;
            }


            public static Func<double, double> differential(Func<double, double> f)
            {
                Func<double, double> result = null;

                result = (Func<double, double>)(x => (((1.0 / (2.0 * MathLib.getdefaultTolerance())) * (f.Invoke(x + MathLib.getdefaultTolerance()) - f.Invoke(x - MathLib.getdefaultTolerance())))));
                return result;
            }


            public static double definiteIntegral(double st, double en, Func<double, double> f)
            {
                double result;
                double tol = MathLib.getdefaultTolerance();

                double area = 0.0;

                double d = tol * (en - st);

                double cum = st;

                while (cum < en)
                {
                    double next = cum + d;

                    area = area + d * (f.Invoke(cum) + f.Invoke(next)) / 2.0;
                    cum = next;


                }
                return area;





            }


            public static Func<double, double> indefiniteIntegral(Func<double, double> f)
            {
                Func<double, double> result = null;

                result = (Func<double, double>)(x => (MathLib.definiteIntegral(0, x, f)));
                return result;
            }



        }



        public class FinanceLib
        {

            public FinanceLib()
            {

            }



            public override string ToString()
            {
                string _res_ = "(FinanceLib) ";
                return _res_;
            }

            public static double discountDiscrete(double amount, double rate, double time)
            {
                double result = 0.0;
                if (rate <= -1) { return result; }

                result = amount / Math.Pow((1 + rate), time);
                return result;
            }


            public static double netPresentValueDiscrete(double rate, ArrayList values)
            {
                double result = 0.0;
                if (rate <= -1) { return result; }

                result = SystemTypes.sumdouble(SystemTypes.collect_5(SystemTypes.integerSubrange(1, (values).Count), values, rate));
                return result;
            }


            public static double presentValueDiscrete(double rate, ArrayList values)
            {
                double result = 0.0;
                if (rate <= -1) { return result; }

                result = SystemTypes.sumdouble(SystemTypes.collect_6(SystemTypes.integerSubrange(1, (values).Count), values, rate));
                return result;
            }


            public static double irrDiscrete(ArrayList values)
            {
                double result = 0.0;

                return result;
            }


            public static ArrayList straddleDates(OclDate d1, OclDate d2, int period)
            {
                OclDate cd = d1;

                while (cd.compareToYMD(d2) <= 0)
                {
                    cd = cd.addMonthYMD(period);
                }
                return SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), cd.subtractMonthYMD(period)), cd);

            }


            public static int numberOfPeriods(OclDate settle, OclDate matur, int period)
            {
                int result = 0;

                double monthsToMaturity = OclDate.differenceMonths(matur, settle) * 1.0;
                result = (int) Math.Ceiling((monthsToMaturity / period));
                return result;
            }


            public static ArrayList sequenceOfPeriods(OclDate sett, OclDate mat, int period)
            {
                ArrayList result = new ArrayList();

                int numPeriods = FinanceLib.numberOfPeriods(sett, mat, period); 
                result = SystemTypes.integerSubrange(1, numPeriods);
                return result;
            }


            public static ArrayList couponDates(OclDate matur, int period, int numPeriods)
            {
                ArrayList cpdates = SystemTypes.addSet((new ArrayList()), matur);

                OclDate cpdate = matur;

                for (int _i10 = 0; _i10 < numPeriods - 1; _i10++)
                {
                    int mo = cpdate.getMonth() - period;

                    int prevMonth = mo;
                    int prevYear = cpdate.getYear();
                    int prevDay = cpdate.getDate();

                    if (mo <= 0)
                    {
                        prevMonth = 12 + mo;
                        prevYear = cpdate.getYear() - 1;
                    }

                    cpdate = OclDate.newOclDate_YMD(prevYear, prevMonth, prevDay);
                    cpdates = SystemTypes.append(cpdates, cpdate);
                }

                cpdates = SystemTypes.reverse(cpdates);
                return cpdates;
            }


        public static int days360(OclDate d1, OclDate d2, string num, OclDate mat)
        { /* num is the dayCount convention */
            
            int dd1 = d1.getDate();
            int dd2 = d2.getDate();
            int mm1 = d1.getMonth();
            int mm2 = d2.getMonth();
            int yy1 = d1.getYear();
            int yy2 = d2.getYear();
            if (num.Equals("30/360"))
            {
                return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
            }
            else if (num.Equals("30/360B"))
            {
                dd1 = Math.Min(dd1, 30);
                if (dd1 > 29)
                { dd2 = Math.Min(dd2, 30); }
                return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
            }
            else if (num.Equals("30/360US"))
            {
                if (mm1 == 2 && (dd1 == 28 || dd1 == 29) && mm2 == 2 && (dd2 == 28 || dd2 == 29))
                {
                    dd2 = 30;
                }
                if (mm1 == 2 && (dd1 == 28 || dd1 == 29))
                { dd1 = 30; }
                if (dd2 == 31 && (dd1 == 30 || dd1 == 31))
                { dd2 = 30; }
                if (dd1 == 31) { dd1 = 30; }
                return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
            }
            else if (num.Equals("30E/360"))
            {
                if (dd1 == 31) { dd1 = 30; }
                if (dd2 == 31) { dd2 = 30; }
                return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
            }
            else if (num.Equals("30E/360ISDA"))
            {
                if (d1.isEndOfMonth()) { dd1 = 30; }
                if (!(d2 == mat && mm2 == 2) && d2.isEndOfMonth()) { dd2 = 30; }
                return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
            }
            else
            { return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1); }
          }


          public static ArrayList numberOfMonths(OclDate pd, OclDate settle, double cd, string dayCount, OclDate matur)
          {
            double sv = 0.0;
            ArrayList result = new ArrayList();

            if (dayCount.Equals("Actual/360") || dayCount.Equals("Actual/365F") ||
                dayCount.Equals("Actual/ActualICMA") ||
                dayCount.Equals("Actual/364") || dayCount.Equals("Actual/ActualISDA"))
            {
                int daysBetween = OclDate.daysBetweenDates(pd, settle);
                sv = (cd - daysBetween) / cd;
                result.Add(sv); result.Add(cd - daysBetween);
                return result;
            }
            else
            {
                int daysBetween360 = FinanceLib.days360(pd, settle, dayCount, matur);
                sv = (cd - daysBetween360) / cd;
                result.Add(sv); result.Add(cd - daysBetween360);
                return result;
            }
          }


                            public static ArrayList calculateCouponPayments(ArrayList paymentDates, double annualCouponRate, string dayCountC, int freq)
                            {
                                ArrayList result = new ArrayList();


                                return result;
                            }


                            public static ArrayList bondCashFlows(OclDate settle, OclDate matur, double coupon, string dayCount, int freq)
                            {
                                ArrayList result = new ArrayList();


                                return result;
                            }


                            public static double bondPrice(double yld, OclDate settle, OclDate matur, double coup, string dayCount, int freq)
                            {
                                ArrayList bcfs = FinanceLib.bondCashFlows(settle, matur, coup, dayCount, freq);

                                ArrayList coupRates = (ArrayList)bcfs[0];

                                ArrayList timePoints = (ArrayList)bcfs[2];

                                ArrayList discountFactors = new ArrayList();
                                for (int _icollect = 0; _icollect < timePoints.Count; _icollect++)
                                {
                                    double x = (double)timePoints[_icollect];
                                    discountFactors.Add(Math.Pow(1.0 / (1 + (yld / freq)), x));
                                }

                                coupRates = SystemTypes.append(SystemTypes.front(coupRates), ((double)SystemTypes.last(coupRates)) + 1);
                                int sp = 0;

                                ArrayList _range13 = SystemTypes.integerSubrange(1, (coupRates).Count);
                                for (int _i12 = 0; _i12 < _range13.Count; _i12++)
                                {
                                    int i = (int)_range13[_i12];
                                    sp = (int)(sp + (((double)discountFactors[i - 1]) * ((double)coupRates[i - 1])));
                                }
                                return sp;

                            }


                            public static double accInterest(OclDate issue, OclDate settle, int freq, double coup)
                            {
                                int period = ((int)(12 / freq));

                                ArrayList st = FinanceLib.straddleDates(issue, settle, period);

                                double aif = (1.0 * OclDate.daysBetweenDates((OclDate)st[0], settle)) / OclDate.daysBetweenDates((OclDate)st[0], (OclDate)st[1]);

                                return aif * (coup / freq);
                            }


                            public static double accumulatedInterest(OclDate issue, OclDate settle, int freq, double coup, string dayCount, OclDate matur)
                            {
                                double result = 0.0;


                                return result;
                            }


                            public static double bondPriceClean(double Y, OclDate I, OclDate S, OclDate M, double c, string dcf, int f)
                            {
                                double result = 0.0;

                                result = FinanceLib.bondPrice(Y, S, M, c, dcf, f) - FinanceLib.accumulatedInterest(I, S, f, c, dcf, M);
                                return result;
                            }

                        }



        class Controller
        {
            Hashtable objectmap = new Hashtable();
            Hashtable classmap = new Hashtable();
            ArrayList ocldate_s = new ArrayList();
            ArrayList mathlib_s = new ArrayList();
            ArrayList financelib_s = new ArrayList();
            private static Controller uniqueInstance;


            private Controller() { }


            public static Controller inst()
            {
                if (uniqueInstance == null)
                {
                    uniqueInstance = new Controller();
                    uniqueInstance.initialiseOclTypes();
                }
                return uniqueInstance;
            }


            public void initialiseOclTypes()
            {
                OclType.newOclType("OclDate", typeof(OclDate));
                OclType.newOclType("MathLib", typeof(MathLib));
                OclType.newOclType("FinanceLib", typeof(FinanceLib));
            }


            public void loadModel()
            {
                char[] delims = { ' ', '.' };
                StreamReader str = new StreamReader("in.txt");
                string line = "";
                try
                {
                    while (true)
                    {
                        line = str.ReadLine();
                        if (line == null) { return; }
                        string[] words = line.Split(delims);
                        if (words.Length == 3 && words[1].Equals(":"))  // a : A
                        { addObjectToClass(words[0], words[2]); }
                        else if (words.Length == 4 && words[1].Equals(":")) // a : b.role
                        { addObjectToRole(words[2], words[0], words[3]); }
                        else if (words.Length >= 4 && words[2].Equals("="))  // a.f = val
                        {
                            int eqind = line.IndexOf("=");
                            if (eqind < 0) { continue; }
                            string value = line.Substring(eqind + 1, line.Length - eqind - 1);
                            setObjectFeatureValue(words[0], words[1], value.Trim());
                        }
                    }
                }
                catch (Exception e) { return; }
            }

            public void addObjectToClass(string a, string c)
            {
                if (c.Equals("OclDate"))
                {
                    OclDate ocldatex = new OclDate();
                    objectmap[a] = ocldatex;
                    classmap[a] = c;
                    addOclDate(ocldatex);
                    return;
                }

                if (c.Equals("MathLib"))
                {
                    MathLib mathlibx = new MathLib();
                    objectmap[a] = mathlibx;
                    classmap[a] = c;
                    addMathLib(mathlibx);
                    return;
                }

                if (c.Equals("FinanceLib"))
                {
                    FinanceLib financelibx = new FinanceLib();
                    objectmap[a] = financelibx;
                    classmap[a] = c;
                    addFinanceLib(financelibx);
                    return;
                }
            }

            public void addObjectToRole(string a, string b, string role)
            { }

            public void setObjectFeatureValue(string a, string f, string val)
            {
                if ("OclDate".Equals(classmap[a]) && f.Equals("time"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    settime(ocldatex, Int64.Parse(val));
                    return;
                }

                if ("OclDate".Equals(classmap[a]) && f.Equals("year"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    setyear(ocldatex, int.Parse(val));
                    return;
                }

                if ("OclDate".Equals(classmap[a]) && f.Equals("month"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    setmonth(ocldatex, int.Parse(val));
                    return;
                }

                if ("OclDate".Equals(classmap[a]) && f.Equals("day"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    setday(ocldatex, int.Parse(val));
                    return;
                }

                if ("OclDate".Equals(classmap[a]) && f.Equals("weekday"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    setweekday(ocldatex, int.Parse(val));
                    return;
                }

                if ("OclDate".Equals(classmap[a]) && f.Equals("hour"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    sethour(ocldatex, int.Parse(val));
                    return;
                }

                if ("OclDate".Equals(classmap[a]) && f.Equals("minute"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    setminute(ocldatex, int.Parse(val));
                    return;
                }

                if ("OclDate".Equals(classmap[a]) && f.Equals("second"))
                {
                    OclDate ocldatex = (OclDate)objectmap[a];
                    setsecond(ocldatex, int.Parse(val));
                    return;
                }
            }


            public void saveModel(string f)
            {
                StreamWriter outfile = new StreamWriter(f);
                for (int _i = 0; _i < ocldate_s.Count; _i++)
                {
                    OclDate ocldatex_ = (OclDate)ocldate_s[_i];
                    outfile.WriteLine("ocldatex_" + _i + " : OclDate");
                    outfile.WriteLine("ocldatex_" + _i + ".time = " + ocldatex_.gettime());
                    outfile.WriteLine("ocldatex_" + _i + ".year = " + ocldatex_.getyear());
                    outfile.WriteLine("ocldatex_" + _i + ".month = " + ocldatex_.getmonth());
                    outfile.WriteLine("ocldatex_" + _i + ".day = " + ocldatex_.getday());
                    outfile.WriteLine("ocldatex_" + _i + ".weekday = " + ocldatex_.getweekday());
                    outfile.WriteLine("ocldatex_" + _i + ".hour = " + ocldatex_.gethour());
                    outfile.WriteLine("ocldatex_" + _i + ".minute = " + ocldatex_.getminute());
                    outfile.WriteLine("ocldatex_" + _i + ".second = " + ocldatex_.getsecond());
                }

                for (int _i = 0; _i < mathlib_s.Count; _i++)
                {
                    MathLib mathlibx_ = (MathLib)mathlib_s[_i];
                    outfile.WriteLine("mathlibx_" + _i + " : MathLib");
                }

                for (int _i = 0; _i < financelib_s.Count; _i++)
                {
                    FinanceLib financelibx_ = (FinanceLib)financelib_s[_i];
                    outfile.WriteLine("financelibx_" + _i + " : FinanceLib");
                }

                outfile.Close();
            }



            public void addOclDate(OclDate oo) { ocldate_s.Add(oo); }

            public ArrayList getocldate_s() { return ocldate_s; }

            public void addMathLib(MathLib oo) { mathlib_s.Add(oo); }

            public ArrayList getmathlib_s() { return mathlib_s; }

            public void addFinanceLib(FinanceLib oo) { financelib_s.Add(oo); }

            public ArrayList getfinancelib_s() { return financelib_s; }



            public void createAllOclDate(ArrayList ocldatex)
            {
                for (int i = 0; i < ocldatex.Count; i++)
                {
                    OclDate ocldatex_x = new OclDate();
                    ocldatex[i] = ocldatex_x;
                    addOclDate(ocldatex_x);
                }
            }


            public OclDate createOclDate()
            {
                OclDate ocldatex = new OclDate();
                addOclDate(ocldatex);
                setsystemTime(ocldatex, 0);
                settime(ocldatex, 0);
                setyear(ocldatex, 0);
                setmonth(ocldatex, 0);
                setday(ocldatex, 0);
                setweekday(ocldatex, 0);
                sethour(ocldatex, 0);
                setminute(ocldatex, 0);
                setsecond(ocldatex, 0);

                return ocldatex;
            }

            public void createAllMathLib(ArrayList mathlibx)
            {
                for (int i = 0; i < mathlibx.Count; i++)
                {
                    MathLib mathlibx_x = new MathLib();
                    mathlibx[i] = mathlibx_x;
                    addMathLib(mathlibx_x);
                }
            }


            public MathLib createMathLib()
            {
                MathLib mathlibx = new MathLib();
                addMathLib(mathlibx);
                setix(mathlibx, 0);
                setiy(mathlibx, 0);
                setiz(mathlibx, 0);
                setdefaultTolerance(mathlibx, 0.0);
                sethexdigit(mathlibx, (new ArrayList()));

                return mathlibx;
            }

            public void createAllFinanceLib(ArrayList financelibx)
            {
                for (int i = 0; i < financelibx.Count; i++)
                {
                    FinanceLib financelibx_x = new FinanceLib();
                    financelibx[i] = financelibx_x;
                    addFinanceLib(financelibx_x);
                }
            }


            public FinanceLib createFinanceLib()
            {
                FinanceLib financelibx = new FinanceLib();
                addFinanceLib(financelibx);

                return financelibx;
            }


            public void setsystemTime(long systemTime_x)
            {
                OclDate.setsystemTime(systemTime_x);
                for (int i = 0; i < ocldate_s.Count; i++)
                {
                    OclDate ocldatex = (OclDate)ocldate_s[i];
                    setsystemTime(ocldatex, systemTime_x);
                }
            }

            public void setsystemTime(OclDate ocldatex, long systemTime_x)
            {
                ocldatex.localSetsystemTime(systemTime_x);
            }


            public void settime(OclDate ocldatex, long time_x)
            {
                ocldatex.settime(time_x);
            }


            public void setyear(OclDate ocldatex, int year_x)
            {
                ocldatex.setyear(year_x);
            }


            public void setmonth(OclDate ocldatex, int month_x)
            {
                ocldatex.setmonth(month_x);
            }


            public void setday(OclDate ocldatex, int day_x)
            {
                ocldatex.setday(day_x);
            }


            public void setweekday(OclDate ocldatex, int weekday_x)
            {
                ocldatex.setweekday(weekday_x);
            }


            public void sethour(OclDate ocldatex, int hour_x)
            {
                ocldatex.sethour(hour_x);
            }


            public void setminute(OclDate ocldatex, int minute_x)
            {
                ocldatex.setminute(minute_x);
            }


            public void setsecond(OclDate ocldatex, int second_x)
            {
                ocldatex.setsecond(second_x);
            }


            public void setix(int ix_x)
            {
                MathLib.setix(ix_x);
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    setix(mathlibx, ix_x);
                }
            }

            public void setix(MathLib mathlibx, int ix_x)
            {
                mathlibx.localSetix(ix_x);
            }


            public void setiy(int iy_x)
            {
                MathLib.setiy(iy_x);
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    setiy(mathlibx, iy_x);
                }
            }

            public void setiy(MathLib mathlibx, int iy_x)
            {
                mathlibx.localSetiy(iy_x);
            }


            public void setiz(int iz_x)
            {
                MathLib.setiz(iz_x);
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    setiz(mathlibx, iz_x);
                }
            }

            public void setiz(MathLib mathlibx, int iz_x)
            {
                mathlibx.localSetiz(iz_x);
            }


            public void setdefaultTolerance(double defaultTolerance_x)
            {
                MathLib.setdefaultTolerance(defaultTolerance_x);
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    setdefaultTolerance(mathlibx, defaultTolerance_x);
                }
            }

            public void setdefaultTolerance(MathLib mathlibx, double defaultTolerance_x)
            {
                mathlibx.localSetdefaultTolerance(defaultTolerance_x);
            }


            public void sethexdigit(ArrayList hexdigit_x)
            {
                MathLib.sethexdigit(hexdigit_x);
                for (int i = 0; i < mathlib_s.Count; i++)
                {
                    MathLib mathlibx = (MathLib)mathlib_s[i];
                    sethexdigit(mathlibx, hexdigit_x);
                }
            }

            public void sethexdigit(MathLib mathlibx, ArrayList hexdigit_x)
            {
                mathlibx.localSethexdigit(hexdigit_x);
            }


            public void addhexdigit(string hexdigit_x)
            { MathLib.addhexdigit(hexdigit_x); }


            public void removehexdigit(string hexdigit_x)
            { MathLib.removehexdigit(hexdigit_x); }



            public void setTime(OclDate ocldatex, long t)
            {
                ocldatex.setTime(t);
            }

            public ArrayList AllOclDatesetTime(ArrayList ocldatexs, long t)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    setTime(ocldatex, t);
                }
                return result;
            }

            public ArrayList AllOclDategetTime(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getTime());
                }
                return result;
            }

            public ArrayList AllOclDatedateBefore(ArrayList ocldatexs, DateTime d)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.dateBefore(d));
                }
                return result;
            }

            public ArrayList AllOclDatedateAfter(ArrayList ocldatexs, DateTime d)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.dateAfter(d));
                }
                return result;
            }




            public static void setSystemTime(long t)
            { OclDate.setSystemTime(t); }

            public ArrayList AllOclDategetYear(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getYear());
                }
                return result;
            }

            public ArrayList AllOclDategetMonth(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getMonth());
                }
                return result;
            }

            public ArrayList AllOclDategetDate(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getDate());
                }
                return result;
            }

            public ArrayList AllOclDategetDay(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getDay());
                }
                return result;
            }

            public ArrayList AllOclDategetHour(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getHour());
                }
                return result;
            }

            public ArrayList AllOclDategetHours(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getHours());
                }
                return result;
            }

            public ArrayList AllOclDategetMinute(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getMinute());
                }
                return result;
            }

            public ArrayList AllOclDategetMinutes(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getMinutes());
                }
                return result;
            }

            public ArrayList AllOclDategetSecond(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getSecond());
                }
                return result;
            }

            public ArrayList AllOclDategetSeconds(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.getSeconds());
                }
                return result;
            }

            public ArrayList AllOclDateaddYears(ArrayList ocldatexs, int y)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.addYears(y));
                }
                return result;
            }

            public ArrayList AllOclDateaddMonths(ArrayList ocldatexs, int m)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.addMonths(m));
                }
                return result;
            }

            public ArrayList AllOclDateaddMonthYMD(ArrayList ocldatexs, int m)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.addMonthYMD(m));
                }
                return result;
            }

            public ArrayList AllOclDatesubtractMonthYMD(ArrayList ocldatexs, int m)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.subtractMonthYMD(m));
                }
                return result;
            }

            public ArrayList AllOclDateaddDays(ArrayList ocldatexs, int d)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.addDays(d));
                }
                return result;
            }

            public ArrayList AllOclDateaddHours(ArrayList ocldatexs, int h)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.addHours(h));
                }
                return result;
            }

            public ArrayList AllOclDateaddMinutes(ArrayList ocldatexs, int n)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.addMinutes(n));
                }
                return result;
            }

            public ArrayList AllOclDateaddSeconds(ArrayList ocldatexs, int s)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.addSeconds(s));
                }
                return result;
            }

            public ArrayList AllOclDatetoString(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.toString());
                }
                return result;
            }

            public ArrayList AllOclDateisLeapYear(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.isLeapYear());
                }
                return result;
            }

            public ArrayList AllOclDatedaysInMonth(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.daysInMonth());
                }
                return result;
            }

            public ArrayList AllOclDateisEndOfMonth(ArrayList ocldatexs)
            {
                ArrayList result = new ArrayList();
                for (int _i = 0; _i < ocldatexs.Count; _i++)
                {
                    OclDate ocldatex = (OclDate)ocldatexs[_i];
                    result.Add(ocldatex.isEndOfMonth());
                }
                return result;
            }

            public static void initialiseMathLib()
            { MathLib.initialiseMathLib(); }

            public static void setSeeds(int x, int y, int z)
            { MathLib.setSeeds(x, y, z); }

            public static void setSeed(int r)
            { MathLib.setSeed(r); }



            public void killAllOclDate(ArrayList ocldatexx)
            {
                for (int _i = 0; _i < ocldatexx.Count; _i++)
                { killOclDate((OclDate)ocldatexx[_i]); }
            }

            public void killOclDate(OclDate ocldatexx)
            {
                ocldate_s.Remove(ocldatexx);
            }



            public void killAllMathLib(ArrayList mathlibxx)
            {
                for (int _i = 0; _i < mathlibxx.Count; _i++)
                { killMathLib((MathLib)mathlibxx[_i]); }
            }

            public void killMathLib(MathLib mathlibxx)
            {
                mathlib_s.Remove(mathlibxx);
            }



            public void killAllFinanceLib(ArrayList financelibxx)
            {
                for (int _i = 0; _i < financelibxx.Count; _i++)
                { killFinanceLib((FinanceLib)financelibxx[_i]); }
            }

            public void killFinanceLib(FinanceLib financelibxx)
            {
                financelib_s.Remove(financelibxx);
            }





        }






        public class SystemTypes
        {
            public static long getTime()
            {
                DateTimeOffset d = new DateTimeOffset(DateTime.Now);
                return d.ToUnixTimeMilliseconds();
            }

            public static long getTime(DateTime d)
            {
                DateTimeOffset doff = new DateTimeOffset(d);
                return doff.ToUnixTimeMilliseconds();
            }

            public static double roundTo(double x, int n)
            {
                if (n == 0)
                { return Math.Round(x); }
                double y = x * Math.Pow(10, n);
                return Math.Round(y) / Math.Pow(10, n);
            }

            public static double truncateTo(double x, int n)
            {
                if (n < 0)
                { return (int)x; }
                double y = x * Math.Pow(10, n);
                return ((int)y) / Math.Pow(10, n);
            }







            public static ArrayList collect_0(ArrayList _l)
            { // Implements: Integer.subrange(1,n - bs->size())->collect( int_0_xx | 0 )
                ArrayList _results_0 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    int int_0_xx = (int)_l[_icollect];
                    _results_0.Add(0);
                }
                return _results_0;
            }

            public static ArrayList collect_1(ArrayList _l, double m)
            { // Implements: sq->collect( x | ( x - m )->sqr() )
                ArrayList _results_1 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    double x = (double)_l[_icollect];
                    _results_1.Add((((x - m)) * ((x - m))));
                }
                return _results_1;
            }

            public static ArrayList collect_2(ArrayList _l, ArrayList s, ArrayList m, int i)
            { // Implements: Integer.subrange(1,m.size)->collect( k | s[k] * ( m[k]->at(i) ) )
                ArrayList _results_2 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    int k = (int)_l[_icollect];
                    _results_2.Add(((double)s[k - 1]) * ((double)((ArrayList)m[k - 1])[i - 1]));
                }
                return _results_2;
            }

            public static ArrayList collect_3(ArrayList _l, ArrayList m, ArrayList s)
            { // Implements: Integer.subrange(1,s.size)->collect( i | Integer.Sum(1,m.size,k,s[k] * ( m[k]->at(i) )) )
                ArrayList _results_3 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    int i = (int)_l[_icollect];
                    _results_3.Add(SystemTypes.sumdouble(SystemTypes.collect_2(SystemTypes.integerSubrange(1, m.Count), s, m, i)));
                }
                return _results_3;
            }

            public static ArrayList collect_4(ArrayList _l, ArrayList m2)
            { // Implements: m1->collect( row | MathLib.rowMult(row,m2) )
                ArrayList _results_4 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    ArrayList row = (ArrayList)_l[_icollect];
                    _results_4.Add(MathLib.rowMult(row, m2));
                }
                return _results_4;
            }

            public static ArrayList collect_5(ArrayList _l, ArrayList values, double rate)
            { // Implements: Integer.subrange(1,values->size())->collect( _ind | FinanceLib.discountDiscrete(values->at(_ind),rate,( _ind - 1 )) )
                ArrayList _results_5 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    int _ind = (int)_l[_icollect];
                    _results_5.Add(FinanceLib.discountDiscrete(((double)values[_ind - 1]), rate, (_ind - 1)));
                }
                return _results_5;
            }

            public static ArrayList collect_6(ArrayList _l, ArrayList values, double rate)
            { // Implements: Integer.subrange(1,values->size())->collect( _ind | FinanceLib.discountDiscrete(values->at(_ind),rate,_ind) )
                ArrayList _results_6 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    int _ind = (int)_l[_icollect];
                    _results_6.Add(FinanceLib.discountDiscrete(((double)values[_ind - 1]), rate, _ind));
                }
                return _results_6;
            }

            public static ArrayList collect_7(ArrayList _l, double yld, int freq)
            { // Implements: timePoints->collect( x | ( 1.0 / ( 1 + ( yld / freq ) ) )->pow(x) )
                ArrayList _results_7 = new ArrayList();
                for (int _icollect = 0; _icollect < _l.Count; _icollect++)
                {
                    double x = (double)_l[_icollect];
                    _results_7.Add(Math.Pow((1.0 / (1 + (yld / freq))), x));
                }
                return _results_7;
            }



            public static bool isSubset(ArrayList a, ArrayList b)
            {
                bool res = true;
                for (int i = 0; i < a.Count; i++)
                {
                    if (a[i] != null && b.Contains(a[i])) { }
                    else { return false; }
                }
                return res;
            }

            public static bool equalsSet(ArrayList a, ArrayList b)
            { return isSubset(a, b) && isSubset(b, a); }


            public static ArrayList addSet(ArrayList a, object x)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a); if (x != null) { res.Add(x); }
                return res;
            }

            public static ArrayList makeSet(object x)
            {
                ArrayList res = new ArrayList();
                if (x != null) { res.Add(x); }
                return res;
            }

            public static ArrayList removeSet(ArrayList a, object x)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                while (res.Contains(x)) { res.Remove(x); }
                return res;
            }


            public static object max(ArrayList l)
            {
                IComparable res = null;
                if (l.Count == 0) { return res; }
                res = (IComparable)l[0];
                for (int i = 1; i < l.Count; i++)
                {
                    IComparable e = (IComparable)l[i];
                    if (res.CompareTo(e) < 0) { res = e; }
                }
                return res;
            }


            public static object min(ArrayList l)
            {
                IComparable res = null;
                if (l.Count == 0) { return res; }
                res = (IComparable)l[0];
                for (int i = 1; i < l.Count; i++)
                {
                    IComparable e = (IComparable)l[i];
                    if (res.CompareTo(e) > 0) { res = e; }
                }
                return res;
            }


            public static ArrayList copyCollection(ArrayList a)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                return res;
            }

            public static Hashtable copyMap(Hashtable m)
            {
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m)
                { res.Add(pair.Key, pair.Value); }
                return res;
            }

            public static ArrayList collectSequence(ArrayList col, Func<object, object> f)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < col.Count; i++)
                { res.Add(f(col[i])); }
                return res;
            }

            public static object iterate(ArrayList col, object init, Func<object, Func<object, object>> f)
            {
                object res = init;
                for (int i = 0; i < col.Count; i++)
                { res = f(col[i])(res); }
                return res;
            }

            public unsafe static T* resizeTo<T>(T* arr, int n) where T : unmanaged
            {
                T* tmp = stackalloc T[n];
                for (int i = 0; i < n; i++)
                { tmp[i] = arr[i]; }
                return tmp;
            }

            public unsafe static ArrayList sequenceRange<T>(T* arr, int n) where T : unmanaged
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < n; i++)
                { res.Add(arr[i]); }
                return res;
            }

            public static int sequenceCompare(ArrayList sq1, ArrayList sq2)
            {
                int res = 0;
                for (int i = 0; i < sq1.Count && i < sq2.Count; i++)
                {
                    object elem1 = sq1[i];
                    if (((IComparable)elem1).CompareTo(sq2[i]) < 0)
                    { return -1; }
                    else if (((IComparable)elem1).CompareTo(sq2[i]) > 0)
                    { return 1; }
                }

                if (sq1.Count > sq2.Count)
                { return 1; }
                if (sq2.Count > sq1.Count)
                { return -1; }
                return res;
            }



            public static ArrayList union(ArrayList a, ArrayList b)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count; i++)
                { if (a[i] == null || res.Contains(a[i])) { } else { res.Add(a[i]); } }
                for (int j = 0; j < b.Count; j++)
                { if (b[j] == null || res.Contains(b[j])) { } else { res.Add(b[j]); } }
                return res;
            }


            public static ArrayList subtract(ArrayList a, ArrayList b)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count; i++)
                {
                    if (a[i] == null || b.Contains(a[i])) { }
                    else { res.Add(a[i]); }
                }
                return res;
            }

            public static ArrayList subtract(ArrayList a, object b)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count; i++)
                {
                    if (a[i] == null || b == a[i]) { }
                    else { res.Add(a[i]); }
                }
                return res;
            }

            public static string subtract(string a, string b)
            {
                string res = "";
                for (int i = 0; i < a.Length; i++)
                { if (b.IndexOf(a[i]) < 0) { res = res + a[i]; } }
                return res;
            }



            public static ArrayList intersection(ArrayList a, ArrayList b)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count; i++)
                { if (a[i] != null && b.Contains(a[i])) { res.Add(a[i]); } }
                return res;
            }



            public static ArrayList symmetricDifference(ArrayList a, ArrayList b)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count; i++)
                {
                    object _a = a[i];
                    if (b.Contains(_a) || res.Contains(_a)) { }
                    else { res.Add(_a); }
                }
                for (int j = 0; j < b.Count; j++)
                {
                    object _b = b[j];
                    if (a.Contains(_b) || res.Contains(_b)) { }
                    else { res.Add(_b); }
                }
                return res;
            }



            public static bool isUnique(ArrayList evals)
            {
                ArrayList vals = new ArrayList();
                for (int i = 0; i < evals.Count; i++)
                {
                    object ob = evals[i];
                    if (vals.Contains(ob)) { return false; }
                    vals.Add(ob);
                }
                return true;
            }


            public static long gcd(long xx, long yy)
            {
                long x = Math.Abs(xx);
                long y = Math.Abs(yy);
                while (x != 0 && y != 0)
                {
                    long z = y;
                    y = x % y;
                    x = z;
                }
                if (y == 0)
                { return x; }
                if (x == 0)
                { return y; }
                return 0;
            }

            public static bool toBoolean(object sx)
            {
                if (sx == null) { return false; }
                if ("".Equals(sx + "") || "false".Equals(sx + "") || "False".Equals(sx + ""))
                { return false; }
                try
                {
                    double d = double.Parse(sx + "");
                    if (Double.IsNaN(d) || (d <= 0.0 && d >= 0.0))
                    { return false; }
                }
                catch { }
                return true;
            }

            public static int toInteger(string sx)
            {
                string tsx = sx.Trim();
                if (tsx.StartsWith("0x"))
                { return Convert.ToInt32(tsx, 16); }
                if (tsx.StartsWith("0b"))
                { return Convert.ToInt32(tsx, 2); }
                if (tsx.StartsWith("0") && tsx.Length > 1)
                { return Convert.ToInt32(tsx, 8); }
                return toInt(tsx);
            }

            public static long toLong(string sx)
            {
                string sxx = sx.Trim();
                if (sxx.StartsWith("0x"))
                { return Convert.ToInt64(sxx, 16); }
                if (sxx.StartsWith("0b"))
                { return Convert.ToInt64(sxx, 2); }
                if (sxx.StartsWith("0") && sxx.Length > 1)
                { return Convert.ToInt64(sxx, 8); }
                return long.Parse(sxx);
            }

            public static int char2byte(string qf)
            {
                if (qf.Length < 1) { return -1; }
                return Char.ConvertToUtf32(qf, 0);
            }

            public static string byte2char(int qf)
            {
                if (qf < 0) { return ""; }
                return Char.ConvertFromUtf32(qf);
            }



            public static int sumint(ArrayList a)
            {
                int sum = 0;
                for (int i = 0; i < a.Count; i++)
                {
                    int x = (int)a[i];
                    sum += x;
                }
                return sum;
            }

            public static double sumdouble(ArrayList a)
            {
                double sum = 0.0;
                for (int i = 0; i < a.Count; i++)
                {
                    double x = double.Parse("" + a[i]);
                    sum += x;
                }
                return sum;
            }

            public static long sumlong(ArrayList a)
            {
                long sum = 0;
                for (int i = 0; i < a.Count; i++)
                {
                    long x = (long)a[i];
                    sum += x;
                }
                return sum;
            }

            public static string sumString(ArrayList a)
            {
                string sum = "";
                for (int i = 0; i < a.Count; i++)
                {
                    object x = a[i];
                    sum = sum + x;
                }
                return sum;
            }



            public static int prdint(ArrayList a)
            {
                int _prd = 1;
                for (int i = 0; i < a.Count; i++)
                {
                    int x = (int)a[i];
                    _prd *= x;
                }
                return _prd;
            }

            public static double prddouble(ArrayList a)
            {
                double _prd = 1;
                for (int i = 0; i < a.Count; i++)
                {
                    double x = (double)a[i];
                    _prd *= x;
                }
                return _prd;
            }

            public static long prdlong(ArrayList a)
            {
                long _prd = 1;
                for (int i = 0; i < a.Count; i++)
                {
                    long x = (long)a[i];
                    _prd *= x;
                }
                return _prd;
            }



            public static ArrayList concatenate(ArrayList a, ArrayList b)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                res.AddRange(b);
                return res;
            }
            public static ArrayList prepend(ArrayList a, object x)
            {
                ArrayList res = new ArrayList();
                res.Add(x);
                res.AddRange(a);
                return res;
            }

            public static ArrayList append(ArrayList a, object x)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                res.Add(x);
                return res;
            }





            public static ArrayList asSet(ArrayList a)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count; i++)
                {
                    object obj = a[i];
                    if (res.Contains(obj)) { }
                    else { res.Add(obj); }
                }
                return res;
            }

            public static ArrayList asOrderedSet(ArrayList a)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count; i++)
                {
                    object obj = a[i];
                    if (res.Contains(obj)) { }
                    else { res.Add(obj); }
                }
                return res;
            }

            public static T[] asReference<T>(ArrayList sq, T[] r)
            {
                for (int i = 0; i < sq.Count && i < r.Length; i++)
                { r[i] = (T)sq[i]; }
                return r;
            }

            public static ArrayList asSequence<T>(T[] r)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < r.Length; i++)
                { res.Add(r[i]); }
                return res;
            }

            public static ArrayList asSequence(Hashtable m)
            {
                ArrayList res = new ArrayList();
                foreach (DictionaryEntry pair in m)
                {
                    object key = pair.Key;
                    Hashtable maplet = new Hashtable();
                    maplet[key] = pair.Value;
                    res.Add(maplet);
                }
                return res;
            }



            public static ArrayList asBag(ArrayList a)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                res.Sort();
                return res;
            }



            public static ArrayList reverse(ArrayList a)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                res.Reverse();
                return res;
            }

            public static string reverse(string a)
            {
                string res = "";
                for (int i = a.Length - 1; i >= 0; i--)
                { res = res + a[i]; }
                return res;
            }



            public static ArrayList front(ArrayList a)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < a.Count - 1; i++)
                { res.Add(a[i]); }
                return res;
            }


            public static ArrayList tail(ArrayList a)
            {
                ArrayList res = new ArrayList();
                for (int i = 1; i < a.Count; i++)
                { res.Add(a[i]); }
                return res;
            }


            public static ArrayList sort(ArrayList a)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                res.Sort();
                return res;
            }



            public static ArrayList sortedBy(ArrayList a, ArrayList f)
            {
                int i = a.Count - 1;
                Hashtable f_map = new Hashtable();
                for (int j = 0; j < a.Count; j++)
                { f_map[a[j]] = f[j]; }
                return mergeSort(a, f_map, 0, i);
            }

            static ArrayList mergeSort(ArrayList a, Hashtable f, int ind1, int ind2)
            {
                ArrayList res = new ArrayList();
                if (ind1 > ind2)
                { return res; }
                if (ind1 == ind2)
                {
                    res.Add(a[ind1]);
                    return res;
                }
                if (ind2 == ind1 + 1)
                {
                    IComparable e1 = (IComparable)f[a[ind1]];
                    IComparable e2 = (IComparable)f[a[ind2]];
                    if (e1.CompareTo(e2) < 0) // e1 < e2
                    { res.Add(a[ind1]); res.Add(a[ind2]); return res; }
                    else
                    { res.Add(a[ind2]); res.Add(a[ind1]); return res; }
                }
                int mid = (ind1 + ind2) / 2;
                ArrayList a1;
                ArrayList a2;
                if (mid == ind1)
                {
                    a1 = new ArrayList();
                    a1.Add(a[ind1]);
                    a2 = mergeSort(a, f, mid + 1, ind2);
                }
                else
                {
                    a1 = mergeSort(a, f, ind1, mid - 1);
                    a2 = mergeSort(a, f, mid, ind2);
                }
                int i = 0;
                int j = 0;
                while (i < a1.Count && j < a2.Count)
                {
                    IComparable e1 = (IComparable)f[a1[i]];
                    IComparable e2 = (IComparable)f[a2[j]];
                    if (e1.CompareTo(e2) < 0) // e1 < e2
                    {
                        res.Add(a1[i]);
                        i++; // get next e1
                    }
                    else
                    {
                        res.Add(a2[j]);
                        j++;
                    }
                }
                if (i == a1.Count)
                {
                    for (int k = j; k < a2.Count; k++)
                    { res.Add(a2[k]); }
                }
                else
                {
                    for (int k = i; k < a1.Count; k++)
                    { res.Add(a1[k]); }
                }
                return res;
            }


            public static ArrayList integerSubrange(int i, int j)
            {
                ArrayList tmp = new ArrayList();
                for (int k = i; k <= j; k++)
                { tmp.Add(k); }
                return tmp;
            }

            public static string subrange(string s, int i, int j)
            {
                if (i < 1)
                { i = 1; }
                if (j > s.Length)
                { j = s.Length; }
                if (i > s.Length || i > j)
                { return ""; }
                return s.Substring(i - 1, j - i + 1);
            }

            public static ArrayList subrange(ArrayList l, int i, int j)
            {
                ArrayList tmp = new ArrayList();
                if (i < 0) { i = l.Count + i; }
                if (j < 0) { j = l.Count + j; }
                if (i < 1) { i = 1; }
                for (int k = i - 1; k < j; k++)
                { tmp.Add(l[k]); }
                return tmp;
            }

            public static int indexOfSubList(ArrayList a, ArrayList b)
            { /* Index of a subsequence a of sequence b in b */
                if (a.Count == 0 || b.Count == 0)
                { return 0; }

                int i = 0;
                while (i < b.Count && b[i] != a[0])
                { i++; }

                if (i >= b.Count)
                { return 0; }

                int j = 0;
                while (j < a.Count && i + j < b.Count && b[i + j] == a[j])
                { j++; }

                if (j >= a.Count)
                { return i + 1; }

                ArrayList subr = subrange(b, i + 2, b.Count);
                int res1 = indexOfSubList(a, subr);
                if (res1 == 0)
                { return 0; }
                return res1 + i + 1;
            }

            public static int lastIndexOfSubList(ArrayList a, ArrayList b)
            {
                int res = 0;
                if (a.Count == 0 || b.Count == 0)
                { return res; }

                ArrayList arev = reverse(a);
                ArrayList brev = reverse(b);
                int i = indexOfSubList(arev, brev);
                if (i == 0)
                { return res; }
                res = b.Count - i - a.Count + 2;
                return res;
            }



            public static int count(ArrayList l, object obj)
            {
                int res = 0;
                for (int _i = 0; _i < l.Count; _i++)
                {
                    if (obj == l[_i]) { res++; }
                    else if (obj != null && obj.Equals(l[_i])) { res++; }
                }
                return res;
            }

            public static int count(string s, string x)
            {
                int res = 0;
                if ("".Equals(s)) { return res; }
                int ind = s.IndexOf(x);
                if (ind == -1) { return res; }
                string ss = s.Substring(ind + 1, s.Length - ind - 1);
                res++;
                while (ind >= 0)
                {
                    ind = ss.IndexOf(x);
                    if (ind == -1 || ss.Equals("")) { return res; }
                    res++;
                    ss = ss.Substring(ind + 1, ss.Length - ind - 1);
                }
                return res;
            }



            public static ArrayList characters(string str)
            {
                ArrayList _res = new ArrayList();
                for (int i = 0; i < str.Length; i++)
                { _res.Add("" + str[i]); }
                return _res;
            }



            public static object any(ArrayList v)
            {
                if (v.Count == 0) { return null; }
                return v[0];
            }


            public static object first(ArrayList v)
            {
                if (v.Count == 0) { return null; }
                return v[0];
            }


            public static object last(ArrayList v)
            {
                if (v.Count == 0) { return null; }
                return v[v.Count - 1];
            }



            public static ArrayList subcollections(ArrayList v)
            {
                ArrayList res = new ArrayList();
                if (v.Count == 0)
                {
                    res.Add(new ArrayList());
                    return res;
                }
                if (v.Count == 1)
                {
                    res.Add(new ArrayList());
                    res.Add(v);
                    return res;
                }
                ArrayList s = new ArrayList();
                object x = v[0];
                s.AddRange(v);
                s.RemoveAt(0);
                ArrayList scs = subcollections(s);
                res.AddRange(scs);
                for (int i = 0; i < scs.Count; i++)
                {
                    ArrayList sc = (ArrayList)scs[i];
                    ArrayList scc = new ArrayList();
                    scc.Add(x);
                    scc.AddRange(sc);
                    res.Add(scc);
                }
                return res;
            }


            public static ArrayList maximalElements(ArrayList s, ArrayList v)
            {
                ArrayList res = new ArrayList();
                if (s.Count == 0) { return res; }
                IComparable largest = (IComparable)v[0];
                res.Add(s[0]);

                for (int i = 1; i < s.Count; i++)
                {
                    IComparable next = (IComparable)v[i];
                    if (largest.CompareTo(next) < 0)
                    {
                        largest = next;
                        res.Clear();
                        res.Add(s[i]);
                    }
                    else if (largest.CompareTo(next) == 0)
                    { res.Add(s[i]); }
                }
                return res;
            }

            public static ArrayList minimalElements(ArrayList s, ArrayList v)
            {
                ArrayList res = new ArrayList();
                if (s.Count == 0) { return res; }
                IComparable smallest = (IComparable)v[0];
                res.Add(s[0]);

                for (int i = 1; i < s.Count; i++)
                {
                    IComparable next = (IComparable)v[i];
                    if (next.CompareTo(smallest) < 0)
                    {
                        smallest = next;
                        res.Clear();
                        res.Add(s[i]);
                    }
                    else if (smallest.CompareTo(next) == 0)
                    { res.Add(s[i]); }
                }
                return res;
            }


            public static ArrayList intersectAll(ArrayList se)
            {
                ArrayList res = new ArrayList();
                if (se.Count == 0) { return res; }
                res.AddRange((ArrayList)se[0]);
                for (int i = 1; i < se.Count; i++)
                { res = SystemTypes.intersection(res, (ArrayList)se[i]); }
                return res;
            }



            public static ArrayList unionAll(ArrayList se)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < se.Count; i++)
                {
                    ArrayList b = (ArrayList)se[i];
                    for (int j = 0; j < b.Count; j++)
                    { if (b[j] == null || res.Contains(b[j])) { } else { res.Add(b[j]); } }
                }
                return res;
            }

            public static Hashtable unionAllMap(ArrayList se)
            {
                Hashtable res = new Hashtable();
                for (int i = 0; i < se.Count; i++)
                {
                    Hashtable b = (Hashtable)se[i];
                    res = SystemTypes.unionMap(res, b);
                }
                return res;
            }

            public static Hashtable intersectionAllMap(ArrayList se)
            {
                Hashtable res = (Hashtable)se[0];
                for (int i = 1; i < se.Count; i++)
                {
                    Hashtable b = (Hashtable)se[i];
                    res = SystemTypes.intersectionMap(res, b);
                }
                return res;
            }



            public static ArrayList insertAt(ArrayList l, int ind, object ob)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < ind - 1 && i < l.Count; i++)
                { res.Add(l[i]); }
                if (ind <= l.Count + 1) { res.Add(ob); }
                for (int i = ind - 1; i < l.Count; i++)
                { res.Add(l[i]); }
                return res;
            }
            public static string insertAt(string l, int ind, object ob)
            {
                string res = "";
                for (int i = 0; i < ind - 1 && i < l.Length; i++)
                { res = res + l[i]; }
                if (ind <= l.Length + 1) { res = res + ob; }
                for (int i = ind - 1; i < l.Length; i++)
                { res = res + l[i]; }
                return res;
            }


            public static ArrayList insertInto(ArrayList l, int ind, ArrayList ob)
            {
                ArrayList res = new ArrayList();
                for (int i = 0; i < ind - 1 && i < l.Count; i++)
                { res.Add(l[i]); }
                for (int j = 0; j < ob.Count; j++)
                { res.Add(ob[j]); }
                for (int i = ind - 1; i < l.Count; i++)
                { res.Add(l[i]); }
                return res;
            }
            public static string insertInto(string l, int ind, object ob)
            {
                string res = "";
                for (int i = 0; i < ind - 1 && i < l.Length; i++)
                { res = res + l[i]; }
                res = res + ob;
                for (int i = ind - 1; i < l.Length; i++)
                { res = res + l[i]; }
                return res;
            }


            public static ArrayList removeFirst(ArrayList a, object x)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                res.Remove(x);
                return res;
            }

            public static ArrayList removeAt(ArrayList a, int i)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                if (i <= res.Count && i >= 1)
                { res.RemoveAt(i - 1); }
                return res;
            }

            public static string removeAtString(string a, int i)
            {
                string res = "";
                for (int x = 0; x < i - 1 && x < a.Length; x++)
                { res = res + a[x]; }
                for (int x = i; x >= 0 && x < a.Length; x++)
                { res = res + a[x]; }
                return res;
            }

            public static ArrayList setAt(ArrayList a, int i, object x)
            {
                ArrayList res = new ArrayList();
                res.AddRange(a);
                if (i <= res.Count && i >= 1)
                { res[i - 1] = x; }
                return res;
            }

            public static string setAt(string a, int i, string x)
            {
                string res = "";
                for (int j = 0; j < i - 1 && j < a.Length; j++)
                { res = res + a[j]; }
                if (i <= a.Length && i >= 1)
                { res = res + x; }
                for (int j = i; j >= 0 && j < a.Length; j++)
                { res = res + a[j]; }
                return res;
            }



            public static bool isInteger(string str)
            {
                try { int.Parse(str); return true; }
                catch (Exception _e) { return false; }
            }

            public static int toInt(String str)
            {
                try
                {
                    int x = int.Parse(str.Trim());
                    return x;
                }
                catch (Exception _e) { return 0; }
            }



            public static bool isReal(string str)
            {
                try
                {
                    double d = double.Parse(str);
                    if (Double.IsNaN(d)) { return false; }
                    return true;
                }
                catch (Exception __e) { return false; }
            }

            public static double toDouble(String str)
            {
                try
                {
                    double x = double.Parse(str.Trim());
                    return x;
                }
                catch (Exception _e) { return 0; }
            }


            public static bool isLong(string str)
            {
                try { long.Parse(str); return true; }
                catch (Exception _e) { return false; }
            }



            public static string before(string s, string sep)
            {
                if (sep.Length == 0) { return s; }
                int ind = s.IndexOf(sep);
                if (ind < 0) { return s; }
                return s.Substring(0, ind);
            }


            public static string after(string s, string sep)
            {
                int ind = s.IndexOf(sep);
                int seplength = sep.Length;
                if (ind < 0) { return ""; }
                if (seplength == 0) { return ""; }
                return s.Substring(ind + seplength, s.Length - (ind + seplength));
            }


            public static bool hasMatch(string s, string patt)
            {
                Regex r = new Regex(patt);
                return r.IsMatch(s);
            }


            public static bool isMatch(string s, string patt)
            {
                Regex r = new Regex(patt);
                Match m = r.Match(s);
                return (m + "").Equals(s);
            }


            public static ArrayList allMatches(string s, string patt)
            {
                Regex r = new Regex(patt);
                MatchCollection col = r.Matches(s);
                ArrayList res = new ArrayList();
                foreach (Match mm in col)
                { res.Add(mm.Value + ""); }
                return res;
            }


            public static string replace(string str, string delim, string rep)
            {
                if (str == null) { return null; }
                String result = "";
                String s = str + "";
                int i = (s.IndexOf(delim) + 1);
                if (i == 0)
                { return s; }

                int sublength = delim.Length;
                if (sublength == 0)
                { return s; }

                while (i > 0)
                {
                    result = result + SystemTypes.subrange(s, 1, i - 1) + rep;
                    s = SystemTypes.subrange(s, i + delim.Length, s.Length);
                    i = (s.IndexOf(delim) + 1);
                }
                result = result + s;
                return result;
            }


            public static string replaceAll(string s, string patt, string rep)
            {
                if (s == null) { return null; }
                Regex r = new Regex(patt);
                return "" + r.Replace(s, rep);
            }


            public static string replaceFirstMatch(string s, string patt, string rep)
            {
                if (s == null) { return null; }
                Regex r = new Regex(patt);
                return "" + r.Replace(s, rep, 1);
            }


            public static string firstMatch(string s, string patt)
            {
                Regex r = new Regex(patt);
                Match m = r.Match(s);
                if (m.Success)
                { return m.Value + ""; }
                return null;
            }


            public static ArrayList split(string s, string patt)
            {
                Regex r = new Regex(patt);
                ArrayList res = new ArrayList();
                string[] wds = r.Split(s);
                for (int x = 0; x < wds.Length; x++)
                {
                    if (wds[x].Length > 0)
                    { res.Add(wds[x]); }
                }
                return res;
            }


            public static bool includesAllMap(Hashtable sup, Hashtable sub)
            {
                foreach (DictionaryEntry pair in sub)
                {
                    if (sup.ContainsKey(pair.Key))
                    {
                        if (sup[pair.Key].Equals(pair.Value)) { }
                        else
                        { return false; }
                    }
                    else
                    { return false; }
                }
                return true;
            }


            public static bool excludesAllMap(Hashtable sup, Hashtable sub)
            {
                foreach (DictionaryEntry pair in sub)
                {
                    if (sup.ContainsKey(pair.Key))
                    {
                        if (pair.Value.Equals(sup[pair.Key]))
                        { return false; }
                    }
                }
                return true;
            }


            public static Hashtable includingMap(Hashtable m, object src, object trg)
            {
                Hashtable copy = new Hashtable(m);
                copy.Add(src, trg);
                return copy;
            }


            public static Hashtable excludeAllMap(Hashtable m1, Hashtable m2)
            { // m1 - m2 
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry x in m1)
                {
                    object key = x.Key;
                    if (m2.ContainsKey(key)) { }
                    else
                    { res[key] = m1[key]; }
                }
                return res;
            }


            public static Hashtable excludingMapKey(Hashtable m, object k)
            { // m - { k |-> m(k) }  
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m)
                {
                    if (pair.Key.Equals(k)) { }
                    else
                    { res.Add(pair.Key, pair.Value); }
                }
                return res;
            }


            public static Hashtable excludingMapValue(Hashtable m, object v)
            { // m - { k |-> v }    
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m)
                {
                    if (pair.Value.Equals(v)) { }
                    else
                    { res.Add(pair.Key, pair.Value); }
                }
                return res;
            }


            public static Hashtable unionMap(Hashtable m1, Hashtable m2)
            { /* Overrides m1 by m2 if they have pairs in common */
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m2)
                { res.Add(pair.Key, pair.Value); }
                foreach (DictionaryEntry pair in m1)
                {
                    if (res.ContainsKey(pair.Key)) { }
                    else { res.Add(pair.Key, pair.Value); }
                }
                return res;
            }


            public static Hashtable intersectionMap(Hashtable m1, Hashtable m2)
            {
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m1)
                {
                    object key = pair.Key;
                    if (m2.ContainsKey(key) && pair.Value != null && pair.Value.Equals(m2[key]))
                    { res.Add(key, pair.Value); }
                }
                return res;
            }

            public static Hashtable restrictMap(Hashtable m1, ArrayList ks)
            {
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m1)
                {
                    object key = pair.Key;
                    if (ks.Contains(key))
                    { res.Add(key, pair.Value); }
                }
                return res;
            }

            public static Hashtable antirestrictMap(Hashtable m1, ArrayList ks)
            {
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m1)
                {
                    object key = pair.Key;
                    if (ks.Contains(key)) { }
                    else
                    { res.Add(key, pair.Value); }
                }
                return res;
            }

            public static ArrayList mapKeys(Hashtable m)
            {
                ArrayList res = new ArrayList();
                res.AddRange(m.Keys);
                return res;
            }

            public static ArrayList mapValues(Hashtable m)
            {
                ArrayList res = new ArrayList();
                res.AddRange(m.Values);
                return res;
            }

            public static Hashtable selectMap(Hashtable m1, Func<object, bool> f)
            {
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m1)
                {
                    object val = pair.Value;
                    if (f(val))
                    { res.Add(pair.Key, val); }
                }
                return res;
            }

            public static Hashtable rejectMap(Hashtable m1, Func<object, bool> f)
            {
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m1)
                {
                    object val = pair.Value;
                    if (f(val)) { }
                    else
                    { res.Add(pair.Key, val); }
                }
                return res;
            }

            public static Hashtable collectMap(Hashtable m1, Func<object, object> f)
            {
                Hashtable res = new Hashtable();
                foreach (DictionaryEntry pair in m1)
                {
                    object val = pair.Value;
                    res.Add(pair.Key, f(val));
                }
                return res;
            }


        }

        class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
        }
    }
}
