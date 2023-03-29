    public class OrderedMap<K, T>
    {
        private ArrayList elements; // internal
        private Hashtable items; // internal

        public OrderedMap()
        {
            elements = (new ArrayList());
            items = (new Hashtable());

        }



        public override string ToString()
        {
            string _res_ = "(OrderedMap) ";
            _res_ = _res_ + elements + ",";
            _res_ = _res_ + items;
            return _res_;
        }

        public void setelements(ArrayList elements_x) { elements = elements_x; }


        public void setelements(int _ind, K elements_x) { elements[_ind] = elements_x; }


        public void addelements(K elements_x)
        { elements.Add(elements_x); }

        public void removeelements(K elements_x)
        { elements = SystemTypes.subtract(elements, elements_x); }





        public void setitems(Hashtable items_x) { items = items_x; }




        public ArrayList getelements() { return elements; }





        public Hashtable getitems() { return items; }





        public T getByIndex(int i)
        {
            T result = default(T);

            if (i > 0 && i <= (elements).Count)
            {
                result = ((T)items[((K)elements[i - 1])]);
            }
            return result;
        }


        public T getByKey(K k)
        {
            T result = default(T);

            result = ((T)items[k]);
            return result;
        }


        public void add(K k, T t)
        {
            OrderedMap<K, T> orderedmapx = this;
            elements = SystemTypes.append(orderedmapx.getelements(), k);
            orderedmapx.items[k] = t;

        }


        public void remove(int i)
        {
            OrderedMap<K, T> orderedmapx = this;
            K k = ((K)elements[i - 1]);

            elements = SystemTypes.removeAt(orderedmapx.getelements(), i);
            items = SystemTypes.antirestrictMap(orderedmapx.getitems(), SystemTypes.addSet((new ArrayList()), k));


        }
    } 