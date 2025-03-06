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
};

