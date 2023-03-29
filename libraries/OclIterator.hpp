
template<class T>
class OclIteratorResult
{
    bool done; 
    T value; 

    static OclIteratorResult* newOclIteratorResult(T v)
    {
        OclIteratorResult* res = new OclIteratorResult(); 
        res->value = v; 
        res->done = false; 
        if (v == NULL) { res->done = true; }
        return res; 
    }
};

template<class T>
class OclIterator
{
private:
    int position;
    int markedPosition;
    vector<T>* elements;
    function<T(int)> generatorFunction;
    vector<string>* columnNames;

public:
    OclIterator()
    {
        position = 0;
        markedPosition = 0;
        elements = (new vector<T>());
        columnNames = (new vector<string>());
        generatorFunction = NULL;
    }

    void setColumnNames(vector<string>* cols)
    { columnNames = cols; } 

    void setPosition(int position_x)
    {
        position = position_x;
    }

    void markPosition()
    {
        markedPosition = position;
    }

    void moveToMarkedPosition()
    {
        position = markedPosition;
    }

    void setelements(vector<T>* elements_x)
    {
        elements = elements_x;
    }

    void setelements(int _ind, void* elements_x)
    {
        (*elements)[_ind] = elements_x;
    }

    void addelements(T elements_x)
    {
        elements->push_back(elements_x);
    }

    int getPosition()
    {
        return position;
    }

    vector<T>* getelements()
    {
        return elements;
    }

    bool hasNext();

    bool hasPrevious();

    bool isAfterLast();

    bool isBeforeFirst();

    int nextIndex();

    int previousIndex();

    void moveForward();

    void moveBackward();

    void moveTo(int i);

    void moveToStart();

    void moveToEnd();

    void moveToFirst();

    void moveToLast();

    void movePosition(int i);

    static OclIterator* newOclIterator_Sequence(vector<T>* sq);

    static OclIterator* newOclIterator_Set(set<T>* st);

    static OclIterator* newOclIterator_String(string st);

    static OclIterator* newOclIterator_String_String(string st, string sep);

    static OclIterator* newOclIterator_Function(function<T(int)> f);

    T getCurrent();

    void set(T x);

    void insert(T x);

    void remove();

    T next();

    OclIteratorResult<T> nextResult();

    T previous();

    T at(int i);

    int length();

    void close();

    int getColumnCount(); 

    string getColumnName(int i);

    void* getCurrentFieldByIndex(int i); 

    void setCurrentFieldByIndex(int i, void* v);

    ~OclIterator() {
    }
}; 
