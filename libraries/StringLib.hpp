// StringLib.h


using namespace std;


class StringLib
{ public: 

    static string leftTrim(string s)
    {
        string result = string("");
        string trimmeds = UmlRsdsLib<string>::trim(s);
        result = UmlRsdsLib<string>::subrange(s, UmlRsdsLib<string>::indexOf(trimmeds, s), (s).length());
        return result;
    }


    static string rightTrim(string s)
    {
        string result = string("");
        string trimmeds = UmlRsdsLib<string>::trim(s);
        result = string(UmlRsdsLib<string>::before(s,trimmeds).append(trimmeds));
        return result;
    }

    static string nCopies(int n, string c)
    { string res = "";
   
      for (int i = 0; i < n; i++)
      {
        res = res + c;
      }

      return res;
    }

    static string padLeftWithInto(string s, string c, int n)
    {
        string result = string("");
        result = string(StringLib::nCopies(n - s.length(), c)).append(s);
        return result;
    }

    static string leftAlignInto(string s, int n)
    {
        int slen = s.length(); 
        string result = string("");
        if (n <= slen)
        {
            result = UmlRsdsLib<string>::subrange(s, 1, n);
        }
        else
        {
            result = string(s).append(StringLib::nCopies(n - slen, " "));
        }
        return result;
    }

    static string rightAlignInto(string s, int n)
    {
        int slen = s.length();
        string result = string("");
        if (n <= slen)
        {
            result = UmlRsdsLib<string>::subrange(s, 1, n);
        }
        else
        { result = string(StringLib::nCopies(n - slen, " ")).append(s); }
        return result;
    }


   static string format(string f, vector<void*>* sq);
};

