class OclComparator
{

  public:
    OclComparator()
    {

    }

    friend ostream& operator<<(ostream& s, OclComparator& x)
    {
        return s << "(OclComparator)  " << endl;
    }

    virtual string toString() {
        return "(OclComparator) ";
    }

    int compare(void* lhs, void* rhs);

    static vector<void*>* lowerSegment(vector<void*>* col, void* x, OclComparator* cmp);

    static vector<void*>* sortWith(vector<void*>* col, OclComparator* cmp);

    static void* maximumWith(vector<void*>* col, OclComparator* cmp);

    static void* minimumWith(vector<void*>* col, OclComparator* cmp);

    static int binarySearch(vector<void*>* col, void* x, OclComparator* cmp);

    ~OclComparator() {
    }

};

