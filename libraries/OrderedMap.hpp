
template< class K, class T>
class OrderedMap
{
  private:
    vector<K>* elements;
    map<string, T>* items;

  public:
    OrderedMap()
    {
        elements = (new vector<void*>());
        items = (new map<string, void*>());
    }


    friend ostream& operator<<(ostream& s, OrderedMap& x)
    {
        return s << "(OrderedMap)  " << "elements = " << x.getelements() << "," << "items = " << x.getitems() << endl;
    }

    virtual string toString() {
        return "(OrderedMap) " + UmlRsdsLib<K>::collectionToString(getelements()) + ", " + UmlRsdsLib<T>::collectionToString(getitems());
    }


    void setelements(vector<K>* elements_x) { elements = elements_x; }


    void setelements(int _ind, K elements_x) { (*elements)[_ind] = elements_x; }


    void addelements(K elements_x)
    {
        elements->push_back(elements_x);
    }

    void removeelements(K elements_x)
    {
        auto _pos = find(elements->begin(), elements->end(), elements_x);
        while (_pos != elements->end())
        {
            elements->erase(_pos);
            _pos = find(elements->begin(), elements->end(), elements_x);
        }
    }

    void setitems(map<string, T>* items_x) { items = items_x; }

    vector<K>* getelements() { return elements; }

    map<string, T>* getitems() { return items; }

    T getByIndex(int i);

    T getByKey(K k);

    void add(K k, T t);

    void remove(int i);


    ~OrderedMap() {
    }

};
