// C++19

template<class _T>
class SortedSequence {
private: 
    vector<_T>* elements = new vector<_T>(); 
public:
    SortedSequence()
    { }

    SortedSequence(vector<_T>* sq)
    {
        std::copy(sq->begin(), sq->end(), elements->begin()); 
        std::sort(elements->begin(), elements->end()); 
    }

    bool includes(_T x)
    {
       return std::binary_search(elements->begin(), elements->end(), x); 
    }

    void including(_T x)
    {
        auto pos = std::lower_bound(elements->begin(), elements->end(), x);
        elements->insert(pos, x); 
    }

    _T at(int i)
    {
        return elements->at(i);
    }

    void excluding(_T x)
    {
        auto pos = std::equal_range(elements->begin(), elements->end(), x); 
        elements->erase(pos.first, pos.second); 
    }

    void subtract(vector<_T>* b)
    {
        for (int i = 0; i < b->size(); i++)
        {
            this->excluding((*b)[i]); 
        }
    }

    void subtract(std::set<_T>* b)
    {
        for (auto iter = b->begin(); iter != b->end(); iter++)
        {
            this->excluding(*iter); 
        }
    }

    void unionSortedSequence(vector<_T>* b)
    {
        for (int i = 0; i < b->size(); i++)
        {
            this->including((*b)[i]);
        }
    }

    void unionSortedSequence(std::set<_T>* b)
    {
        for (auto iter = b->begin(); iter != b->end(); iter++)
        {
            this->including(*iter);
        }
    }

    void intersection(vector<_T>* sq)
    {
        std::set<_T>* removed = new std::set<_T>(); 

        for (int i = 0; i < elements->size(); i++)
        {
            _T x = (*elements)[i];  
            if (std::find(sq->begin(), sq->end(), x) != sq->end()) {}
            else
            {
                removed->insert(x);
            }
        }

        for (auto iter = removed->begin(); iter != removed->end(); iter++) 
        {
            this->excluding(*iter); 
        }
    }

        void intersection(std::set<_T>* st)
        {
            std::set<_T>* removed = new std::set<_T>(); 

            for (int i = 0; i < elements->size(); i++)
            {
                _T x = (*elements)[i]; 
                if (st->find(x) != st->end()) {}
                else
                {
                    removed->insert(x);
                }
            }

            for (auto iter = removed->begin(); iter != removed->end(); iter++)
            {
                this->excluding(*iter);
            }
        }

    auto begin() { return elements->begin(); }

    auto end() { return elements->end(); }

    int size() { return elements->size(); }

    _T min() { return elements->at(0); }

    _T max() 
    { int sze = elements->size();
      if (sze == 0) { return NULL; } 
      return elements->at(sze-1);
    }

    SortedSequence<_T>* subSequence(int i, int j)
    { /* OCL indexing i, j: 1..n */

        int n = elements->size();
        if (n == 0)
        {
            return new SortedSequence<_T>();
        }

        if (i < 1) { i = 1; }

        vector<_T>* elems = new vector<_T>();
        SortedSequence<_T>* res = new SortedSequence<_T>(); 

        for (int k = i-1; k < j && k < n; k++)
        {
            elems->push_back(elements->at(k)); 
        }
        res->elements = elems; 
        return res; 
    }

    SortedSequence<_T>* front()
    {
        int n = elements->size(); 
        if (n == 0)
        {
            return new SortedSequence<_T>();
        }
        return subSequence(1, n - 1); 
    }

    SortedSequence<_T>* tail()
    {
        int n = elements->size();
        if (n == 0)
        {
            return new SortedSequence<_T>();
        }
        return subSequence(2, n);
    }

    SortedSequence<_T>* select(std::function<bool(_T)> f)
    {
        vector<_T>* elems = new vector<_T>(); 

        for (int i = 0; i < elements->size(); ++i)
        {
            _T x = elements->at(i); 
            if (f(x))
            {
                elems->push_back(x); 
            }
        }

        SortedSequence<_T>* res = new SortedSequence<_T>();
        res->elements = elems; 
        return res;
    }

    SortedSequence<_T>* reject(std::function<bool(_T)> f)
    {
        vector<_T>* elems = new vector<_T>();

        for (int i = 0; i < elements->size(); ++i)
        {
            _T x = elements->at(i);
            if (f(x)) {} 
            else
            {
                elems->push_back(x);
            }
        }

        SortedSequence<_T>* res = new SortedSequence<_T>();
        res->elements = elems;
        return res;
    }

    template<class R>
    vector<R>* collect(std::function<R(_T)> f)
    {
        vector<R>* res = new vector<R>();
        for (int i = 0; i < elements->size(); i++)
        {
            _T x = elements->at(i); 
            res->push_back(f(x));
        }

        return res;
    }

};
