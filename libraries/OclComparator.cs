    public class OclComparator
    {

        public OclComparator()
        {

        }



        public override string ToString()
        {
            string _res_ = "(OclComparator) ";
            return _res_;
        }

        public int compare(object lhs, object rhs)
        {
            int result = 0;

            result = ((IComparable)lhs).CompareTo(rhs);
            return result;
        }


        public static ArrayList lowerSegment(ArrayList col, object x, OclComparator cmp)
        {
            ArrayList _results_0 = new ArrayList();
            for (int _iselect = 0; _iselect < col.Count; _iselect++)
            {
                object y = (object) col[_iselect];
                if (cmp.compare(y, x) < 0)
                { _results_0.Add(y); }
            }

            return _results_0;
        }


        public static ArrayList sortWith(ArrayList col, OclComparator cmp)
        {
            ArrayList result = new ArrayList();
            // Implements: col->collect( x | OclComparator.lowerSegment(col,x,cmp)->size() )
            ArrayList _results_1 = new ArrayList();
            for (int _icollect = 0; _icollect < col.Count; _icollect++)
            {
                object x = (object) col[_icollect];
                _results_1.Add((OclComparator.lowerSegment(col, x, cmp)).Count);
            }

            result = SystemTypes.sortedBy(col, _results_1);
            return result;
        }

        public static bool isUpperBound(ArrayList _l, OclComparator cmp, object x)
        {
            for (int _iforall = 0; _iforall < _l.Count; _iforall++)
            {
                object y = (object)_l[_iforall];
                if (cmp.compare(y, x) <= 0) { }
                else { return false; }
            }
            return true;
        }


        public static object maximumWith(ArrayList col, OclComparator cmp)
        {
            object result = null;
            if ((col).Count <= 0) { return result; }

            ArrayList _results_3 = new ArrayList();
            for (int _iselect = 0; _iselect < col.Count; _iselect++)
            {
                object x = (object) col[_iselect];
                if (OclComparator.isUpperBound(col, cmp, x))
                { return x; }
            }
            
            return result;
        }

        public static bool isLowerBound(ArrayList _l, OclComparator cmp, object x)
        {
            for (int _iforall = 0; _iforall < _l.Count; _iforall++)
            {
                object y = (object)_l[_iforall];
                if (cmp.compare(y, x) >= 0) { }
                else { return false; }
            }
            return true;
        }


        public static object minimumWith(ArrayList col, OclComparator cmp)
        {
            object result = null;
            if ((col).Count <= 0) { return result; }

            for (int _iselect = 0; _iselect < col.Count; _iselect++)
            {
                object x = (object) col[_iselect];
                if (OclComparator.isLowerBound(col, cmp, x))
                { return x; }
            }
            return result;
        }


        public static int binarySearch(ArrayList col, object x, OclComparator cmp)
        {
            int result = 0;

            result = (OclComparator.lowerSegment(col, x, cmp)).Count;
            return result;
        }


    }


