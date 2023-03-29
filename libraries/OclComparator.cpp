int OclComparator::compare(void* lhs, void* rhs)
{
    int result = 0;
    result = (lhs < rhs) ? -1 : ((rhs < lhs) ? 1 : 0);
    return result;
}


vector<void*>* OclComparator::lowerSegment(vector<void*>* col, void* x, OclComparator* cmp)
{
    vector<void*>* result = new vector<void*>();
    result = select_0(col, cmp, x);
    return result;
}

vector<void*>* OclComparator::sortWith(vector<void*>* col, OclComparator* cmp)
{
    vector<void*>* result = new vector<void*>();
    result = UmlRsdsLib<void*>::sort(col, [=](void* _var1, void* _var2) -> bool{ return OclComparator::lowerSegment(col,_var1,cmp)->size() < OclComparator::lowerSegment(col,_var2,cmp)->size(); });
    return result;
}

void* OclComparator::maximumWith(vector<void*>* col, OclComparator* cmp)
{
    void* result = NULL;
    if (col->size() <= 0) { return result; }
    result = UmlRsdsLib<void*>::any(select_2(col, cmp, col));
    return result;
}


void* OclComparator::minimumWith(vector<void*>* col, OclComparator* cmp)
{
    void* result = NULL;
    if (col->size() <= 0) { return result; }
    result = UmlRsdsLib<void*>::any(select_4(col, cmp, col));
    return result;
}


int OclComparator::binarySearch(vector<void*>* col, void* x, OclComparator* cmp)
{
    int result = 0;
    result = OclComparator::lowerSegment(col, x, cmp)->size();
    return result;
}

