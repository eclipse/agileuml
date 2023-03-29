using System;
using System.Collections;

// Requires SystemTypes OCL library class. 

    class OclIteratorResult
    {
        static OclIteratorResult createOclIteratorResult()
        {
            OclIteratorResult result = new OclIteratorResult();
            return result;
        }

        bool done = false;
        object value = null;

        public static OclIteratorResult newOclIteratorResult(object v)
        {
            OclIteratorResult res = createOclIteratorResult();
            res.value = v;
            if (v == null)
            { res.done = true; }
            else
            { res.done = false; }
            return res;
        }

    }


        class OclIterator
    {
        private int position; // internal
        private int markedPosition; // internal
        private Func<int, object> generatorFunction; 

        public ArrayList elements; // internal
        public ArrayList columnNames; 

        public OclIterator()
        {
            position = 0;
            markedPosition = 0; 
            elements = (new ArrayList());
            generatorFunction = null;
            columnNames = (new ArrayList());
        }

        public override string ToString()
        {
            string _res_ = "(OclIterator) ";
            _res_ = _res_ + position + ",";
            _res_ = _res_ + elements;
            return _res_;
        }

        public void setPosition(int position_x) { position = position_x; }

        public void markPosition() { markedPosition = position; }

        public void movePosition(int i)
        { position = i + position; }

        void moveToMarkedPosition()
        { position = markedPosition; } 

        public void setelements(ArrayList elements_x) { elements = elements_x; }

        public void setelements(int _ind, object elements_x) { elements[_ind] = elements_x; }

        public void addelements(object elements_x)
        { elements.Add(elements_x); }

        public void removeelements(object elements_x)
        { elements = SystemTypes.subtract(elements, elements_x); }

        public int getPosition() { return position; }

        public ArrayList getelements() { return elements; }

        public bool isAfterLast()
        { if (position > elements.Count)
           { return true; }
           else
           { return false; }
        }

        public bool isBeforeFirst()
        { if (position <= 0) 
            { return true; } 
            else 
            { return false; }
        }

        public bool hasNext()
        {
            bool result = false;

            if (position >= 0 && position < elements.Count) { result = true; }
            else { result = false; }
            return result;
        }


        public bool hasPrevious()
        {
            bool result = false;

            if (position > 1 && position <= elements.Count + 1) { result = true; }
            else { result = false; }
            return result;
        }


        public int nextIndex()
        { return position + 1; }


        public int previousIndex()
        {
            return position - 1;
        }


        public void moveForward()
        {
            position++;
        }

        public void moveBackward()
        {
            position--;
        }

        public void moveTo(int i)
        {   position = i;
        }

        public void moveToStart()
        { position = 0; }

        public void moveToEnd()
        {
            position = this.getelements().Count + 1;
        }

        public void moveToFirst()
        { position = 1; }

        public void moveToLast()
        {
            position = this.getelements().Count;
        }

        public static OclIterator newOclIterator_Sequence(ArrayList sq)
        {
            OclIterator ot = new OclIterator();
            ot.setelements(sq);
            ot.setPosition(0);
            return ot;
        }


        public static OclIterator newOclIterator_Set(ArrayList st)
        {
            OclIterator ot = new OclIterator();
            ot.setelements(SystemTypes.sort(st));
            ot.setPosition(0);
            return ot;
        }

        public static OclIterator newOclIterator_String(string str)
        {
            OclIterator ot = new OclIterator();
            ot.elements = SystemTypes.split(str, "[ \n\t\r]+");
            ot.position = 0;
            return ot;
        }

        public static OclIterator newOclIterator_String_String(string str, String chrs)
        {
            OclIterator ot = new OclIterator();
            ot.elements = SystemTypes.split(str, "[" + chrs + "]+");
            ot.position = 0;
            return ot;
        }

        public static OclIterator newOclIterator_Function(Func<int,object> f)
        {
            OclIterator ot = new OclIterator();
            ot.setelements(new ArrayList());
            ot.generatorFunction = f; 
            ot.setPosition(0);
            return ot;
        }

        public object getCurrent()
        {
            object result = null;
            if (position < 1 || position > elements.Count)
            { return result; }

            result = ((object)elements[position - 1]);
            return result;
        }


        public void set(object x)
        {
            if (position < 1 || position > elements.Count)
            { return; }
            elements[position - 1] = x;
        }

        public void insert(object x)
        {
            if (position < 1 || position > elements.Count)
            { return; }

            elements.Insert(position - 1, x);
        }


        public void remove()
        {
            if (position < 1 || position > elements.Count)
            { return; }

            elements.RemoveAt(position - 1);
        }

        public OclIteratorResult nextResult()
        {
            if (generatorFunction == null)
            {
                Object v = next();
                return OclIteratorResult.newOclIteratorResult(v);
            }

            object res = generatorFunction(position);
            position++;
            if (position <= elements.Count)
            { set(res); }
            else
            { elements.Add(res); }
            return OclIteratorResult.newOclIteratorResult(res);
        }


        public object next()
        {
            this.moveForward();
            return this.getCurrent();
        }


        public object previous()
        {
            this.moveBackward();
            return this.getCurrent();
        }

        public object at(int i)
        { return elements[i - 1]; }

        public int length()
        { return elements.Count; }

        public void close()
        {
            position = 0;
            markedPosition = 0;
            elements = new ArrayList();
            columnNames = new ArrayList();
        } 

        public int getColumnCount()
        { return columnNames.Count; }

        public String getColumnName(int i)
        {
            if (columnNames.Count >= i & i >= 1)
            { return (String) columnNames[i-1]; }
            return null;
        }

        public object getCurrentFieldByIndex(int i)
        {
            if (columnNames.Count >= i & i >= 1)
            {
                Hashtable mm = (Hashtable)getCurrent();
                String fld = (String)columnNames[i - 1];
                if (mm != null && fld != null)
                { return mm[fld]; }
            }
            return null;
        }

        public void setCurrentFieldByIndex(int i, object v)
        {
            if (columnNames.Count >= i & i >= 1)
            {
                Hashtable mm = (Hashtable)getCurrent();
                String fld = (String)columnNames[i - 1];
                if (mm != null && fld != null)
                { mm[fld] = v; }
            }
        }
    }

