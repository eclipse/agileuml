    class StringLib
    {
        public static string leftTrim(string s)
        {
            string result = "";
            result = (string)(SystemTypes.subrange(s, (s.IndexOf(s.Trim()) + 1), (s).Length));
            return result;
        }


        public static string rightTrim(string s)
        {
            string result = "";
            string trimmeds = s.Trim(); 
            result = (string)(SystemTypes.before(s, trimmeds) + trimmeds);
            return result;
        }

        private static ArrayList nCopies(int n, string c)
        { ArrayList _results_0 = new ArrayList();
          for (int _icollect = 0; _icollect < n; _icollect++)
          {
             _results_0.Add(c);
          }
          return _results_0;
        }

        private static String pad(int n, string c)
        {
            string res = ""; 
            for (int _icollect = 0; _icollect < n; _icollect++)
            {
                res = res + c;
            }
            return res;
        }

        public static string padLeftWithInto(string s, string c, int n)
        {
            string result = "";

            result = StringLib.pad( n - (s).Length, c) + s;
            return result;
        }


        public static string leftAlignInto(string s, int n)
        {
            string result = "";

            if (n <= (s).Length)
            {
                result = (string)(SystemTypes.subrange(s, 1, n));
            }
            else
              if (n > (s).Length)
            {
                result = (string)(s + StringLib.pad( n - (s).Length, " "));
            }
            return result;
        }


        public static string rightAlignInto(string s, int n)
        {
            string result = "";

            if (n <= (s).Length)
            {
                result = (string)(SystemTypes.subrange(s, 1, n));
            }
            else
              if (n > (s).Length)
            {
                result = (string)(StringLib.pad(n - (s).Length, " ") + s);
            }
            return result;
        }

        public static string format(string f, ArrayList sq)
        {
            object[] args = new object[sq.Count];
            for (int i = 0; i < sq.Count; i++)
            { args[i] = sq[i]; }
            string fmt = OclFile.convertConversionFormat(f);
            string formattedString = String.Format(fmt, args);
            return formattedString; 
        }

        public static ArrayList scan(string s, string fmt)
        {
            ArrayList result = new ArrayList();

            int ind = 0; // s upto ind has been consumed

            for (int i = 0; i < fmt.Length; i++)
            {
                char c = fmt[i];
                if (c == '%' && i < fmt.Length - 1)
                {
                    char d = fmt[i + 1];
                    if (d == 's')
                    {
                        string schars = ""; 
                        for (int j = ind; j < s.Length; j++)
                        {  if (Char.IsWhiteSpace(s[j]))
                           { break; }
                           else 
                           { schars = schars + s[j]; }
                        }
                        result.Add(schars);
                        ind = ind + schars.Length; 
                        i++;
                    }
                    else if (d == 'f')
                    {
                        String fchars = "";
                        for (int j = ind; j < s.Length; j++)
                        {
                            Char x = s[j];
                            if (x == '.' || Char.IsDigit(x))
                            { fchars = fchars + x; }
                            else
                            { break; }
                        }

                        try
                        {
                            double v = double.Parse(fchars);
                            ind = ind + fchars.Length;
                            result.Add(v);
                        }
                        catch (Exception _ex)
                        { Console.WriteLine("!! Error in double format: " + fchars);  }
                        i++;
                    }
                    else if (d == 'd')
                    {
                        String inchars = "";
                        for (int j = ind; j < s.Length; j++)
                        {
                            Char x = s[j];
                            if (Char.IsDigit(x))
                            { inchars = inchars + x; }
                            else
                            { break; }
                        }

                        try
                        {
                            int v = int.Parse(inchars);
                            ind = ind + inchars.Length;
                            result.Add(v);
                        }
                        catch (Exception _ex)
                        { Console.WriteLine("!! Error in integer format: " + inchars); }
                        i++;
                    }
                }
                else if (s[ind] == c)
                { ind++; }
                else
                { return result; }
            }
            return result;
        }
    }
