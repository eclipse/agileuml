template<class T>
bool OclIterator<T>::hasNext()
{
    bool result = false;
    if (position >= 0 && position < elements->size())
    {
        result = true;
    }
    else
    {
        result = false;
    }
    return result;
}

template<class T>
bool OclIterator<T>::hasPrevious()
{
    bool result = false;
    if (position > 1 && position <= elements->size() + 1)
    {
        result = true;
    }
    else
    {
        result = false;
    }
    return result;
}

template<class T>
bool OclIterator<T>::isAfterLast()
{
    bool result = false;
    if (position > elements->size())
    {
        result = true;
    }
    else
    {
        result = false;
    }
    return result;
}

template<class T>
bool OclIterator<T>::isBeforeFirst()
{
    bool result = false;
    if (position < 1)
    {
        result = true;
    }
    else
    {
        result = false;
    }
    return result;
}

template<class T>
int OclIterator<T>::nextIndex()
{
    int result = 0;
    result = position + 1;
    return result;
}

template<class T>
int OclIterator<T>::previousIndex()
{
    int result = 0;
    result = position - 1;
    return result;
}

template<class T>
void OclIterator<T>::moveForward()
{
    int pre_position0 = position;
    //  if (position + 1 > elements->size())) { return; } 
    this->setPosition(pre_position0 + 1);
}

template<class T>
void OclIterator<T>::moveBackward()
{
    int pre_position1 = position;
    //  if (position <= 1)) { return; } 
    this->setPosition(pre_position1 - 1);
}

template<class T>
void OclIterator<T>::moveTo(int i)
{ //  if ((!(0 <= i) || i > elements->size() + 1))) { return; } 
    this->setPosition(i);
}

template<class T>
void OclIterator<T>::movePosition(int i)
{
    int pos = this->getPosition();
    this->setPosition(i + pos);
}

template<class T>
void OclIterator<T>::moveToStart()
{
    this->setPosition(0);
}

template<class T>
void OclIterator<T>::moveToEnd()
{
    this->setPosition(this->elements->size() + 1);
}

template<class T>
void OclIterator<T>::moveToFirst()
{
    this->setPosition(1);
}

template<class T>
void OclIterator<T>::moveToLast()
{
    this->setPosition(this->elements->size());
}

template<class T>
OclIterator<T>* OclIterator<T>::newOclIterator_Sequence(vector<T>* sq)
{
    OclIterator<T>* result = NULL;
    OclIterator<T>* ot = new OclIterator<T>();
    ot->setelements(sq);
    ot->setPosition(0);
    ot->markedPosition = 0;
    ot->columnNames = new vector<string>();
    result = ot;
    return result;
}
 
template<class T>
OclIterator<T>* OclIterator<T>::newOclIterator_Set(std::set<T>* st)
{
    OclIterator<T>* ot = new OclIterator<T>();
    vector<T>* elems = UmlRsdsLib<T>::concatenate((new vector<T>()), st);

    ot->elements = UmlRsdsLib<T>::sort(elems);
    ot->setPosition(0);
    ot->markedPosition = 0;
    ot->columnNames = new vector<string>();
    return ot;
} // sort the elements, to account for sorted sets

template<class T>
OclIterator<T>* OclIterator<T>::newOclIterator_String(string st)
{
    OclIterator<string>* res = new OclIterator<string>();
    res->elements = UmlRsdsLib<string>::split(st, "[ \n\t\r]");
    res->position = 0;
    res->markedPosition = 0;
    res->columnNames = new vector<string>();
    return res;
}

template<class T>
OclIterator<T>* OclIterator<T>::newOclIterator_String_String(string st, string sep)
{
    OclIterator<string>* res = new OclIterator<string>();
    res->elements =
        UmlRsdsLib<string>::split(st,
            string("[").append(sep).append(string("]")));
    res->position = 0;
    res->markedPosition = 0;
    res->columnNames = new vector<string>();
    return res;
}
  

template<class T>
OclIterator<T>* newOclIterator_Function(function<T (int)> f)
{
    OclIterator<T>* res = new OclIterator<T>();
    res->elements = new vector<T>(); 
    res->generatorFunction = f;
    res->position = 0;
    res->markedPosition = 0;
    res->columnNames = new vector<string>();

    return res;
}

template<class T>
OclIterator<T> OclIterator<T>::trySplit()
{
  vector<T>* firstpart = UmlRsdsLib<T>::subrange(elements, 1, position - 1);
  elements = UmlRsdsLib<T>::subrange(elements, position, elements->size());
  position = 0;
  markedPosition = 0;
  return OclIterator.newOclIterator_Sequence(firstpart);
}


template<class T>
T OclIterator<T>::getCurrent()
{
    T result;
    if (position < 1 || position > elements->size())
    {
        return result;
    }
    result = ((T)elements->at(position - 1));
    return result;
}

template<class T>
void OclIterator<T>::set(T x)
{
    this->setelements(this->getPosition() - 1, x);
}

template<class T>
void OclIterator<T>::insert(T x)
{
    elements->insert(elements->begin() + (position - 1), x);
}

template<class T>
void OclIterator<T>::remove()
{
    elements->erase(elements->begin() + (position - 1));
}

template<class T>
OclIteratorResult<T> OclIterator<T>::nextResult()
{
    if (generatorFunction == NULL)
    {
        OclIterator<T>* ocliteratorx = this;
        ocliteratorx->moveForward();
        return OclIteratorResult<T>::newOclIteratorResult(ocliteratorx->getCurrent());
    } 
    T r = this.generatorFunction(this->position); 
    this->position = this->position + 1;
    if (this->position <= this->elements->size())
    {
       this->set(r);
    } 
    else
    {
       this->elements->push_back(r);
    } 
    return OclIteratorResult<T>::newOclIteratorResult(r);
}

template<class T>
T OclIterator<T>::next()
{
    OclIterator<T>* ocliteratorx = this;
    ocliteratorx->moveForward();
    return ocliteratorx->getCurrent();
}

template<class T>
bool OclIterator<T>::tryAdvance(function<void*(void*)> f)
{
    if (this->position + 1 <= this->elements->size())
    {
        void* x = this->next();
        f(x);
        return true;
    }
    return false;
}


template<class T>
void OclIterator<T>::forEachRemaining(function<void*(void*)> f)
{
  vector<T>* remainingElements = UmlRsdsLib<T>::subrange(elements, position, elements->size());
  for (int i = 0; i < remainingElements->size(); i++)
  {
      T x = remainingElements->at(i); 
      f(x);
  }
}

template<class T>
T OclIterator<T>::previous()
{
    OclIterator<T>* ocliteratorx = this;
    ocliteratorx->moveBackward();
    return ocliteratorx->getCurrent();
}

template<class T>
T OclIterator<T>::at(int i)
{
    return elements->at(i - 1);
}

template<class T>
int OclIterator<T>::length()
{
    return elements->size();
}

template<class T>
void OclIterator<T>::close()
{
    position = 0;
    markedPosition = 0;
    columnNames = new vector<string>(); 
    elements = new vector<T>();
}

template<class T>
int OclIterator<T>::getColumnCount()
{
    return columnNames->size();
}

template<class T>
string OclIterator<T>::getColumnName(int i)
{
    if (columnNames->size() >= i && i >= 1)
    {
        return columnNames->at(i - 1);
    }
    return "";
}

template<class T>
void* OclIterator<T>::getCurrentFieldByIndex(int i)
{
    string nme = getColumnName(i); 
    T curr = getCurrent(); 
    map<string, void*> m = (map<string, void*>) curr; 
    return m->at(nme);
}

template<class T>
void OclIterator<T>::setCurrentFieldByIndex(int i, void* v)
{
    string nme = getColumnName(i);
    T curr = getCurrent();
    map<string, void*> m = (map<string, void*>) curr;
    m[nme] = v;
}



