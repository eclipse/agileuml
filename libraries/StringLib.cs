    class StringLib
    {
       public static string newString(object s)
       { return "" + s; }  

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

        public static string sumStringsWithSeparator(ArrayList lst, string sep)
        {
            string res = "";
            for (int i = 0; i < lst.Count; i++)
            {
                string xx = (string) lst[i];
                res = res + xx;
                if (i < lst.Count - 1)
                { res = res + sep; }
            }
            return res; 
        } 

        public static string nCopies(string s, int n)
        { String result = "";
          for (int _icollect = 0; _icollect < n; _icollect++)
          {
            result = result + s;
          }
          return result;
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

        public static string padRightWithInto(string s, string c, int n)
        {
            string result = "";

            result = s + StringLib.pad(n - (s).Length, c);
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


        public static string capitalise(string str)
        {
            if (str.Length > 0)
            {
                String s1 = str[0] + "";
                return s1.ToUpper() + str.Substring(1, str.Length - 1);
            }
            return str;
        }

        public static string toTitleCase(string s)
        {
            string prev = " ";
            int ind = 1;
            string res = "";

            while (ind <= s.Length)
            {
                String chr = "" + s[ind - 1];
                if (prev.Equals(" "))
                { res = res + chr.ToUpper(); }
                else
                { res = res + chr; }
                prev = chr;
                ind = ind + 1;
            }

            return res;
        }

        public static string swapCase(string s)
        {
            int ind = 1;
            string res = "";

            while (ind <= s.Length)
            {
                string chr = "" + s[ind - 1];
                if (chr.Equals(chr.ToUpper()))
                { res = res + chr.ToLower(); }
                else
                { res = res + chr.ToUpper(); }
                ind = ind + 1;
            }

            return res;
        }

        public static string format(string f, ArrayList sq)
        {
            object[] args = new object[sq.Count];
            for (int i = 0; i < sq.Count; i++)
            { args[i] = sq[i]; }
            string fmt = StringLib.convertConversionFormat(f);
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

        public static string interpolateStrings(string f, ArrayList sq)
        {
            object[] args = new object[sq.Count];
            for (int i = 0; i < sq.Count; i++)
            { args[i] = sq[i]; }
            string formattedString = String.Format(f, args);
            return formattedString; 
        }

        public static string convertConversionFormat(string cstyle)
        {
            String res = cstyle;
            int argindex = 0;
            int pind = res.IndexOf("%");
            Regex dpatt = new Regex("%d");
            Regex epatt = new Regex("%e");
            Regex fpatt = new Regex("%f");
            Regex spatt = new Regex("%s");
            Regex xpatt = new Regex("%x");
            Regex gpatt = new Regex("%g");


            while (pind >= 0)
            {
                if (pind + 1 < res.Length && "s".Equals(res.Substring(pind + 1, 1)))
                { res = spatt.Replace(res, "{" + argindex + ":S}", 1); }
                else if (pind + 1 < res.Length && "d".Equals(res.Substring(pind + 1, 1)))
                { res = dpatt.Replace(res, "{" + argindex + ":D}", 1); }
                else if (pind + 1 < res.Length && "f".Equals(res.Substring(pind + 1, 1)))
                { res = fpatt.Replace(res, "{" + argindex + ":F}", 1); }
                else if (pind + 1 < res.Length && "e".Equals(res.Substring(pind + 1, 1)))
                { res = epatt.Replace(res, "{" + argindex + ":E}", 1); }
                else if (pind + 1 < res.Length && "g".Equals(res.Substring(pind + 1, 1)))
                { res = gpatt.Replace(res, "{" + argindex + ":G}", 1); }
                else if (pind + 1 < res.Length && "x".Equals(res.Substring(pind + 1, 1)))
                { res = xpatt.Replace(res, "{" + argindex + ":X}", 1); }


                argindex++;
                pind = res.IndexOf("%");
            }
            return res;
        }

    }
