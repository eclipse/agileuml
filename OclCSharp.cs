
using System;
using System.Collections;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Linq;
using System.Threading.Tasks;


public class SystemTypes
    {

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
                double x = (double)a[i];
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
        { return s.Substring(i - 1, j - i + 1); }

        public static ArrayList subrange(ArrayList l, int i, int j)
        {
            ArrayList tmp = new ArrayList();
            for (int k = i - 1; k < j; k++)
            { tmp.Add(l[k]); }
            return tmp;
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


        public static bool isInteger(string str)
        {
            try { int.Parse(str); return true; }
            catch (Exception _e) { return false; }
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
            { res.Add(mm + ""); }
            return res;
        }


        public static string replaceAll(string s, string patt, string rep)
        {
            Regex r = new Regex(patt);
            return "" + r.Replace(s, rep);
        }

        public static string replace(string str, string delim, string rep)
        {
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
  } 
