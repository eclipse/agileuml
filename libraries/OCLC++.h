#pragma once

// Controller.h

#undef max
#undef min

using namespace std;

class Runnable
{
public:
    virtual void run() { };
}; // Interface for <<active>> classes


template<class _T>
class UmlRsdsLib {
public:
    static long long getTime()
    {
        return 1000 * time(NULL);
    }

    static struct tm* getDate()
    {
        time_t t = time(NULL);
        struct tm* res = localtime(&t);
        return res;
    }







    static bool isIn(_T x, std::set<_T>* st)
    {
        return (st->find(x) != st->end());
    }

    static bool isIn(_T x, vector<_T>* sq)
    {
        return (find(sq->begin(), sq->end(), x) != sq->end());
    }

    static bool isSubset(std::set<_T>* s1, set<_T>* s2)
    {
        bool res = true;
        for (auto _pos = s1->begin(); _pos != s1->end(); ++_pos)
        {
            if (isIn(*_pos, s2)) {}
            else { return false; }
        }
        return res;
    }

    static bool isSubset(std::set<_T>* s1, vector<_T>* s2)
    {
        bool res = true;
        for (auto _pos = s1->begin(); _pos != s1->end(); ++_pos)
        {
            if (isIn(*_pos, s2)) {}
            else { return false; }
        }
        return res;
    }

    static bool isSubset(std::vector<_T>* s1, vector<_T>* s2)
    {
        bool res = true;
        for (auto _pos = s1->begin(); _pos != s1->end(); ++_pos)
        {
            if (isIn(*_pos, s2)) {}
            else { return false; }
        }
        return res;
    }

    static bool isSubset(std::vector<_T>* s1, set<_T>* s2)
    {
        bool res = true;
        for (auto _pos = s1->begin(); _pos != s1->end(); ++_pos)
        {
            if (isIn(*_pos, s2)) {}
            else { return false; }
        }
        return res;
    }

    static std::set<_T>* makeSet(_T x)
    {
        std::set<_T>* res = new std::set<_T>();
        res->insert(x);
        return res;
    }

    static vector<_T>* makeSequence(_T x)
    {
        vector<_T>* res = new vector<_T>();
        res->push_back(x);
        return res;
    }

    static std::set<_T>* addSet(std::set<_T>* s, _T x)
    {
        s->insert(x);
        return s;
    }

    static vector<_T>* addSequence(vector<_T>* s, _T x)
    {
        s->push_back(x);
        return s;
    }

    static vector<string>* addSequenceString(vector<string>* s, string x)
    {
        s->push_back(x);
        return s;
    }

    static vector<_T>* asSequence(std::set<_T>* c)
    {
        vector<_T>* res = new vector<_T>();
        for (auto _pos = c->begin(); _pos != c->end(); ++_pos)
        {
            res->push_back(*_pos);
        }
        return res;
    }

    static vector<_T>* asSequence(std::vector<_T>* c)
    {
        return c;
    }


    static vector<string>* tokenise(vector<string>* res, string str)
    {
        bool inspace = true;
        string* current = new string("");
        for (int i = 0; i < str.length(); i++)
        {
            if (str[i] == '.' || isspace(str[i]) > 0)
            {
                if (inspace) {}
                else
                {
                    res->push_back(*current);
                    current = new string("");
                    inspace = true;
                }
            }
            else
            {
                if (inspace) { inspace = false; }
                current->append(str.substr(i, 1));
            }
        }
        if (current->length() > 0) { res->push_back(*current); }
        delete current;
        return res;
    }



    static set<_T>* copySet(set<_T>* a)
    {
        set<_T>* res = new set<_T>();
        res->insert(a->begin(), a->end());
        return res;
    }

    static vector<_T>* copySequence(vector<_T>* a)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        return res;
    }

    static map<string, _T>* copyMap(map<string, _T>* m)
    {
        map<string, _T>* res = new map<string, _T>();
        for (auto iter = m->begin(); iter != m->end(); ++iter)
        {
            string key = iter->first;
            (*res)[key] = iter->second;
        }
        return res;
    }

    static string collectionToString(vector<_T>* c)
    {
        ostringstream buff;
        buff << "Sequence{";
        for (auto _pos = c->begin(); _pos != c->end(); ++_pos)
        {
            buff << *_pos;
            if (_pos + 1 < c->end())
            {
                buff << ", ";
            }
        }
        buff << "}";
        return buff.str();
    }

    static string collectionToString(set<_T>* c)
    {
        ostringstream buff;
        buff << "Set{";
        for (auto _pos = c->begin(); _pos != c->end(); ++_pos)
        {
            buff << *_pos;
            if (_pos + 1 < c->end())
            {
                buff << ", ";
            }
        }
        buff << "}";
        return buff.str();
    }

    static string collectionToString(map<string, _T>* c)
    {
        ostringstream buff;
        buff << "Map{";
        int sze = c->size();
        int count = 0;
        for (auto it = c->begin(); it != c->end(); it++)
        {
            buff << (*it).first;
            buff << " |-> ";
            buff << (*it).second;
            if (count + 1 < sze)
            {
                buff << ", ";
            }
            count++;
        }
        buff << "}";
        return buff.str();
    }



    static int oclRound(double d)
    {
        int f = (int)floor(d);
        if (d >= f + 0.5)
        {
            return f + 1;
        }
        else
        {
            return f;
        }
    }



    static _T max(std::set<_T>* l)
    {
        return *std::max_element(l->begin(), l->end());
    }
    static _T max(vector<_T>* l)
    {
        return *std::max_element(l->begin(), l->end());
    }


    static _T min(std::set<_T>* l)
    {
        return *std::min_element(l->begin(), l->end());
    }
    static _T min(vector<_T>* l)
    {
        return *std::min_element(l->begin(), l->end());
    }


    static std::set<_T>* unionSet(std::set<_T>* a, std::set<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        res->insert(a->begin(), a->end());
        res->insert(b->begin(), b->end());
        return res;
    }

    static std::set<_T>* unionSet(vector<_T>* a, std::set<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        res->insert(a->begin(), a->end());
        res->insert(b->begin(), b->end());
        return res;
    }

    static std::set<_T>* unionSet(std::set<_T>* a, vector<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        res->insert(a->begin(), a->end());
        res->insert(b->begin(), b->end());
        return res;
    }

    static std::set<_T>* unionSet(std::vector<_T>* a, vector<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        res->insert(a->begin(), a->end());
        res->insert(b->begin(), b->end());
        return res;
    }



    static vector<_T>* subtract(vector<_T>* a, vector<_T>* b)
    {
        vector<_T>* res = new vector<_T>();
        for (int i = 0; i < a->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*a)[i], b)) {}
            else { res->push_back((*a)[i]); }
        }
        return res;
    }

    static vector<_T>* subtract(vector<_T>* a, std::set<_T>* b)
    {
        vector<_T>* res = new vector<_T>();
        for (int i = 0; i < a->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*a)[i], b)) {}
            else
            {
                res->push_back((*a)[i]);
            }
        }
        return res;
    }

    static std::set<_T>* subtract(std::set<_T>* a, std::set<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (auto _pos = a->begin(); _pos != a->end(); ++_pos)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, b)) {}
            else
            {
                res->insert(*_pos);
            }
        }
        return res;
    }

    static std::set<_T>* subtract(std::set<_T>* a, vector<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (auto _pos = a->begin(); _pos != a->end(); ++_pos)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, b)) {}
            else
            {
                res->insert(*_pos);
            }
        }
        return res;
    }

    static string subtract(string a, string b)
    {
        string res = "";
        for (int i = 0; i < a.length(); i++)
        {
            if (b.find(a[i]) == string::npos) { res = res + a[i]; }
        }
        return res;
    }



    static std::set<_T>* intersection(std::set<_T>* a, std::set<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (auto _pos = a->begin(); _pos != a->end(); ++_pos)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, b))
            {
                res->insert(*_pos);
            }
        }
        return res;
    }

    static std::set<_T>* intersection(std::set<_T>* a, vector<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (auto _pos = a->begin(); _pos != a->end(); ++_pos)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, b))
            {
                res->insert(*_pos);
            }
        }
        return res;
    }

    static vector<_T>* intersection(vector<_T>* a, std::set<_T>* b)
    {
        vector<_T>* res = new vector<_T>();
        for (int i = 0; i < a->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*a)[i], b))
            {
                res->push_back((*a)[i]);
            }
        }
        return res;
    }

    static vector<_T>* intersection(vector<_T>* a, vector<_T>* b)
    {
        vector<_T>* res = new vector<_T>();
        for (int i = 0; i < a->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*a)[i], b))
            {
                res->push_back((*a)[i]);
            }
        }
        return res;
    }



    static std::set<_T>* symmetricDifference(vector<_T>* a, vector<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (int i = 0; i < a->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*a)[i], b)) {}
            else { res->insert((*a)[i]); }
        }
        for (int i = 0; i < b->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*b)[i], a)) {}
            else { res->insert((*b)[i]); }
        }
        return res;
    }

    static std::set<_T>* symmetricDifference(std::set<_T>* a, vector<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (auto _pos = a->begin(); _pos != a->end(); _pos++)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, b)) {}
            else { res->insert(*_pos); }
        }
        for (int i = 0; i < b->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*b)[i], a)) {}
            else { res->insert((*b)[i]); }
        }
        return res;
    }

    static std::set<_T>* symmetricDifference(vector<_T>* a, std::set<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (int i = 0; i < a->size(); i++)
        {
            if (UmlRsdsLib<_T>::isIn((*a)[i], b)) {}
            else { res->insert((*a)[i]); }
        }
        for (auto _pos = b->begin(); _pos != b->end(); _pos++)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, a)) {}
            else { res->insert(*_pos); }
        }
        return res;
    }

    static std::set<_T>* symmetricDifference(std::set<_T>* a, std::set<_T>* b)
    {
        std::set<_T>* res = new std::set<_T>();
        for (auto _pos = a->begin(); _pos != a->end(); _pos++)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, b)) {}
            else { res->insert(*_pos); }
        }
        for (auto _pos = b->begin(); _pos != b->end(); _pos++)
        {
            if (UmlRsdsLib<_T>::isIn(*_pos, a)) {}
            else { res->insert(*_pos); }
        }
        return res;
    }



    static bool isUnique(vector<_T>* evals)
    {
        std::set<_T> vals;
        for (int i = 0; i < evals->size(); i++)
        {
            _T ob = (*evals)[i];
            if (vals.find(ob) != vals.end()) { return false; }
            vals.insert(ob);
        }
        return true;
    }
    static bool isUnique(std::set<_T>* evals)
    {
        return true;
    }


    static long gcd(long xx, long yy)
    {
        long x = labs(xx);
        long y = labs(yy);
        while (x != 0 && y != 0)
        {
            long z = y;
            y = x % y;
            x = z;
        }
        if (y == 0)
        {
            return x;
        }
        if (x == 0)
        {
            return y;
        }
        return 0;
    }


    static string byte2char(int b)
    {
        int arr[] = { 0 };
        arr[0] = b;
        string str = string((char*)arr);
        return str;
    }

    static int char2byte(string str)
    {
        if (str.length() == 0)
        {
            return -1;
        }
        char x = str[0];
        return (int)x;
    }


    static string sumString(vector<string>* a)
    {
        string _sum("");
        for (int i = 0; i < a->size(); i++)
        {
            _sum.append((*a)[i]);
        }
        return _sum;
    }

    static string sumString(std::set<string>* a)
    {
        string _sum("");
        std::set<string>::iterator _pos;
        for (_pos = a->begin(); _pos != a->end(); ++_pos)
        {
            _sum.append(*_pos);
        }
        return _sum;
    }

    static _T sum(vector<_T>* a)
    {
        _T _sum(0);
        for (int i = 0; i < a->size(); i++)
        {
            _sum += (*a)[i];
        }
        return _sum;
    }

    static _T sum(std::set<_T>* a)
    {
        _T _sum(0);
        for (auto _pos = a->begin(); _pos != a->end(); ++_pos)
        {
            _sum += *_pos;
        }
        return _sum;
    }



    static _T prd(vector<_T>* a)
    {
        _T _prd(1);
        for (int i = 0; i < a->size(); i++)
        {
            _prd *= (*a)[i];
        }
        return _prd;
    }

    static _T prd(std::set<_T>* a)
    {
        _T _prd(1);
        for (auto _pos = a->begin(); _pos != a->end(); ++_pos)
        {
            _prd *= *_pos;
        }
        return _prd;
    }



    static vector<_T>* concatenate(vector<_T>* a, vector<_T>* b)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        res->insert(res->end(), b->begin(), b->end());
        return res;
    }

    static vector<_T>* concatenate(vector<_T>* a, std::set<_T>* b)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        res->insert(res->end(), b->begin(), b->end());
        return res;
    }




    static std::set<_T>* asSet(vector<_T>* c)
    {
        std::set<_T>* res = new std::set<_T>();
        res->insert(c->begin(), c->end());
        return res;
    }

    static std::set<_T>* asSet(std::set<_T>* c)
    {
        return c;
    }

    static vector<_T>* asOrderedSet(vector<_T>* c)
    {
        vector<_T>* res = new vector<_T>();
        for (auto _pos = c->begin(); _pos != c->end(); ++_pos)
        {
            if (isIn(*_pos, res)) {}
            else
            {
                res->push_back(*_pos);
            }
        }
        return res;
    }

    static vector<_T>* asOrderedSet(set<_T>* c)
    {
        vector<_T>* res = new vector<_T>();
        for (auto _pos = c->begin(); _pos != c->end(); ++_pos)
        {
            res->push_back(*_pos);
        }
        return res;
    }

    static vector<_T>* randomiseSequence(vector<_T>* sq)
    {
        vector<_T>* res = new vector<_T>();
        for (auto _pos = sq->begin(); _pos != sq->end(); ++_pos)
        {
            res->push_back(*_pos);
        }
        std::random_shuffle(res->begin(), res->end());
        return res;
    }



    static vector<_T>* reverse(vector<_T>* a)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        std::reverse(res->begin(), res->end());
        return res;
    }

    static string reverse(string a)
    {
        string res("");
        for (int i = a.length() - 1; i >= 0; i--)
        {
            res = res + a[i];
        }
        return res;
    }



    static vector<_T>* front(vector<_T>* a)
    {
        vector<_T>* res = new vector<_T>();
        if (a->size() == 0) { return res; }
        auto _pos = a->end();
        _pos--;
        res->insert(res->end(), a->begin(), _pos);
        return res;
    }


    static vector<_T>* tail(vector<_T>* a)
    {
        vector<_T>* res = new vector<_T>();
        if (a->size() == 0) { return res; }
        auto _pos = a->begin();
        _pos++;
        res->insert(res->end(), _pos, a->end());
        return res;
    }


    static vector<_T>* sort(vector<_T>* a)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        std::sort(res->begin(), res->end());
        return res;
    }

    static vector<_T>* sort(std::set<_T>* a)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        std::sort(res->begin(), res->end());
        return res;
    }


    static vector<_T>* sort(vector<_T>* a, std::function<bool(_T,_T)> f)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        std::sort(res->begin(), res->end(), f);
        return res;
    }

    static vector<_T>* sort(std::set<_T>* a, std::function<bool(_T, _T)> f)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), a->begin(), a->end());
        std::sort(res->begin(), res->end(), f);
        return res;
    }

    static vector<int>* integerSubrange(int i, int j)
    {
        vector<int>* tmp = new vector<int>();
        for (int k = i; k <= j; k++)
        {
            tmp->push_back(k);
        }
        return tmp;
    }

    static string subrange(string s, int i, int j)
    {
        if (i < 1) { i = 1; }
        return s.substr(i - 1, j - i + 1);
    }

    static vector<_T>* subrange(vector<_T>* l, int i, int j)
    {
        if (i < 1) { i = 1; }
        if (j > l->size()) { j = l->size(); }
        vector<_T>* tmp = new vector<_T>();
        tmp->insert(tmp->end(), (l->begin()) + (i - 1), (l->begin()) + j);
        return tmp;
    }



    static vector<_T>* prepend(vector<_T>* l, _T ob)
    {
        vector<_T>* res = new vector<_T>();
        res->push_back(ob);
        res->insert(res->end(), l->begin(), l->end());
        return res;
    }


    static vector<_T>* append(vector<_T>* l, _T ob)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), l->begin(), l->end());
        res->push_back(ob);
        return res;
    }


    static int count(std::set<_T>* l, _T obj)
    {
        if (l->find(obj) != l->end()) { return 1; }
        else { return 0; }
    }

    static int count(vector<_T>* l, _T obj)
    {
        return std::count(l->begin(), l->end(), obj);
    }

    static int count(string s, string x)
    {
        int res = 0;
        if (s.length() == 0) { return res; }
        int ind = s.find(x);
        if (ind == string::npos) { return res; }
        string ss = s.substr(ind + 1, s.length() - ind - 1);
        res++;
        while (ind != string::npos)
        {
            ind = ss.find(x);
            if (ind == string::npos || ss.length() == 0) { return res; }
            res++;
            ss = ss.substr(ind + 1, ss.length() - ind - 1);
        }
        return res;
    }



    static vector<string>* characters(string str)
    {
        vector<string>* _res = new vector<string>();
        for (int i = 0; i < str.size(); i++)
        {
            _res->push_back(str.substr(i, 1));
        }
        return _res;
    }



    static _T any(vector<_T>* v)
    {
        if (v->size() == 0) { return 0; }
        return v->at(0);
    }

    static _T any(std::set<_T>* v)
    {
        if (v->size() == 0) { return 0; }
        auto _pos = v->begin();
        return *_pos;
    }



    static _T first(vector<_T>* v)
    {
        if (v->size() == 0) { return 0; }
        return v->at(0);
    }

    static _T first(std::set<_T>* v)
    {
        if (v->size() == 0) { return 0; }
        auto _pos = v->begin();
        return *_pos;
    }



    static _T last(vector<_T>* v)
    {
        if (v->size() == 0) { return 0; }
        return v->at(v->size() - 1);
    }

    static _T last(std::set<_T>* v)
    {
        if (v->size() == 0) { return 0; }
        auto _pos = v->end();
        _pos--;
        return *_pos;
    }



    static vector<_T>* maximalElements(vector<_T>* s, vector<int>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        int largest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            int next = (*v)[i];
            if (next > largest)
            {
                largest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (largest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }

    static vector<_T>* maximalElements(vector<_T>* s, vector<long>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        long largest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            long next = (*v)[i];
            if (next > largest)
            {
                largest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (largest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }

    static vector<_T>* maximalElements(vector<_T>* s, vector<string>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        string largest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            string next = (*v)[i];
            if (next > largest)
            {
                largest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (largest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }

    static vector<_T>* maximalElements(vector<_T>* s, vector<double>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        double largest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            double next = (*v)[i];
            if (next > largest)
            {
                largest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (largest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }


    static vector<_T>* minimalElements(vector<_T>* s, vector<int>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        int smallest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            int next = (*v)[i];
            if (next < smallest)
            {
                smallest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (smallest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }

    static vector<_T>* minimalElements(vector<_T>* s, vector<long>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        long smallest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            long next = (*v)[i];
            if (next < smallest)
            {
                smallest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (smallest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }

    static vector<_T>* minimalElements(vector<_T>* s, vector<string>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        string smallest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            string next = (*v)[i];
            if (next < smallest)
            {
                smallest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (smallest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }

    static vector<_T>* minimalElements(vector<_T>* s, vector<double>* v)
    {
        vector<_T>* res = new vector<_T>();
        if (s->size() == 0) { return res; }
        double smallest = (*v)[0];
        res->push_back((*s)[0]);

        for (int i = 1; i < s->size(); i++)
        {
            double next = (*v)[i];
            if (next < smallest)
            {
                smallest = next;
                res->clear();
                res->push_back((*s)[i]);
            }
            else if (smallest == next)
            {
                res->push_back((*s)[i]);
            }
        }
        return res;
    }


    static std::set<_T>* intersectAll(std::set<std::set<_T>*>* se)
    {
        std::set<_T>* res = new std::set<_T>();
        if (se->size() == 0) { return res; }
        auto _pos = se->begin();
        std::set<_T>* frst = *_pos;
        res->insert(frst->begin(), frst->end());
        ++_pos;
        for (; _pos != se->end(); ++_pos)
        {
            res = UmlRsdsLib<_T>::intersection(res, *_pos);
        }
        return res;
    }

    static std::set<_T>* intersectAll(std::set<vector<_T>*>* se)
    {
        std::set<_T>* res = new std::set<_T>();
        if (se->size() == 0) { return res; }
        auto _pos = se->begin();
        vector<_T>* frst = *_pos;
        res->insert(frst->begin(), frst->end());
        ++_pos;
        for (; _pos != se->end(); ++_pos)
        {
            res = UmlRsdsLib<_T>::intersection(res, *_pos);
        }
        return res;
    }

    static std::set<_T>* intersectAll(vector<std::set<_T>*>* se)
    {
        std::set<_T>* res = new std::set<_T>();
        if (se->size() == 0) { return res; }
        std::set<_T>* frst = (*se)[0];
        res->insert(frst->begin(), frst->end());
        for (int i = 1; i < se->size(); ++i)
        {
            res = UmlRsdsLib<_T>::intersection(res, (*se)[i]);
        }
        return res;
    }

    static vector<_T>* intersectAll(vector<vector<_T>*>* se)
    {
        vector<_T>* res = new vector<_T>();
        if (se->size() == 0) { return res; }
        vector<_T>* frst = (*se)[0];
        res->insert(res->end(), frst->begin(), frst->end());
        for (int i = 1; i < se->size(); ++i)
        {
            res = UmlRsdsLib<_T>::intersection(res, (*se)[i]);
        }
        return res;
    }



    static std::set<_T>* unionAll(std::set<set<_T>*>* se)
    {
        std::set<_T>* res = new std::set<_T>();
        if (se->size() == 0) { return res; }
        
        for (auto _pos = se->begin(); _pos != se->end(); ++_pos)
        {
            res = UmlRsdsLib<_T>::unionSet(res, *_pos);
        }
        return res;
    }

    static std::set<_T>* unionAll(std::set<vector<_T>*>* se)
    {
        std::set<_T>* res = new std::set<_T>();
        if (se->size() == 0) { return res; }
        for (auto _pos = se->begin(); _pos != se->end(); ++_pos)
        {
            res = UmlRsdsLib<_T>::unionSet(res, *_pos);
        }
        return res;
    }

    static std::set<_T>* unionAll(vector<set<_T>*>* se)
    {
        std::set<_T>* res = new std::set<_T>();
        if (se->size() == 0) { return res; }
        for (int i = 0; i < se->size(); ++i)
        {
            res = UmlRsdsLib<_T>::unionSet(res, (*se)[i]);
        }
        return res;
    }



    static vector<_T>* insertAt(vector<_T>* l, int ind, _T ob)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), l->begin(), l->end());
        res->insert(res->begin() + (ind - 1), ob);
        return res;
    }
    static string insertAt(string l, int ind, string ob)
    {
        string res(l);
        res.insert(ind - 1, ob);
        return res;
    }


    static int indexOf(_T x, vector<_T>* a)
    {
        int res = 0;
        for (int i = 0; i < a->size(); i++)
        {
            if (x == (*a)[i])
            {
                return i + 1;
            }
        }
        return res;
    }

    static int indexOf(vector<_T>* a, vector<_T>* b)
    { /* Index of a subsequence a of sequence b in b */

        if (a->size() == 0 || b->size() == 0)
        {
            return 0;
        }
        int i = 0;
        while (i < b->size() && (*b)[i] != (*a)[0])
        {
            i++;
        }

        if (i >= b->size())
        {
            return 0;
        }

        int j = 0;
        while (j < a->size() && i + j < b->size() && (*b)[i + j] == (*a)[j])
        {
            j++;
        }

        if (j >= a->size())
        {
            return i + 1;
        }

        vector<_T>* subr = subrange(b, i + 2, b->size());
        int res1 = indexOf(a, subr);
        if (res1 == 0)
        {
            return 0;
        }
        return res1 + i + 1;
    }

    static int lastIndexOf(vector<_T>* a, vector<_T>* b)
    {
        int res = 0;
        if (a->size() == 0 || b->size() == 0)
        {
            return res;
        }

        vector<_T>* arev = reverse(a);
        vector<_T>* brev = reverse(b);
        int i = indexOf(arev, brev);
        if (i == 0)
        {
            return res;
        }
        res = b->size() - i - a->size() + 2;
        return res;
    }

    static int indexOf(string x, string str)
    {
        int res = str.find(x);
        if (res == string::npos) { return 0; }
        return res + 1;
    }

    static int lastIndexOf(_T x, vector<_T>* a)
    {
        int res = 0;
        for (int i = a->size() - 1; i >= 0; i--)
        {
            if (x == (*a)[i])
            {
                return i + 1;
            }
        }
        return res;
    }

    static int lastIndexOf(string x, string str)
    {
        int res = str.rfind(x);
        if (res == string::npos) { return 0; }
        return res + 1;
    }



    static vector<_T>* setAt(vector<_T>* l, int ind, _T ob)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), l->begin(), l->end());
        if (ind >= 1 && ind <= res->size())
        {
            (*res)[(ind - 1)] = ob;
        }
        return res;
    }

    static string setAt(string st, int ind, string ch)
    {
        string res = "";
        if (ind >= 1 && ind <= st.length())
        {
            res = st.substr(0, ind - 1).append(ch).append(st.substr(ind, st.length() - ind));
        }
        else
        {
            res = st;
        }
        return res;
    }

    static vector<_T>* removeAt(vector<_T>* l, int ind)
    {
        if (ind >= 1 && ind <= l->size())
        {
            vector<_T>* res = new vector<_T>();
            res->insert(res->end(), l->begin(), l->begin() + (ind - 1));
            res->insert(res->end(), l->begin() + ind, l->end());
            return res;
        }
        return l;
    }

    static string removeAt(string ss, int ind)
    {
        if (ind >= 1 && ind <= ss.length())
        {
            string res = ss.substr(0, ind - 1);
            res = res + ss.substr(ind);
            return res;
        }
        return ss;
    }
    static vector<_T>* removeFirst(vector<_T>* sq, _T x)
    {
        vector<_T>* res = new vector<_T>();
        res->insert(res->end(), sq->begin(), sq->end());
        auto iter = find(res->begin(), res->end(), x);
        if (iter != res->end())
        {
            res->erase(iter);
        }
        return res;
    }



    static string toLowerCase(string str)
    {
        string res(str);
        for (int i = 0; i < str.length(); i++)
        {
            res[i] = tolower(str[i]);
        }
        return res;
    }

    static string toUpperCase(string str)
    {
        string res(str);
        for (int i = 0; i < str.length(); i++)
        {
            res[i] = toupper(str[i]);
        }
        return res;
    }

    static bool equalsIgnoreCase(string str1, string str2)
    {
        int len1 = str1.length();
        int len2 = str2.length();
        if (len1 != len2) { return false; }
        for (int i = 0; i < len1; i++)
        {
            if (tolower(str1[i]) == tolower(str2[i]))
            {
            }
            else
            {
                return false;
            }
        }
        return true;
    }



    static bool startsWith(string s1, string s2)
    {
        int l1 = s1.length();
        int l2 = s2.length();
        if (l1 < l2) { return false; }
        if (s1.substr(0, l2) == s2) { return true; }
        return false;
    }

    static bool endsWith(string s1, string s2)
    {
        int l1 = s1.length();
        int l2 = s2.length();
        if (l1 < l2) { return false; }
        if (s1.substr(l1 - l2, l2) == s2) { return true; }
        return false;
    }



    static bool isInteger(string str)
    {
        try { std::stoi(str); return true; }
        catch (exception _e) { return false; }
    }


    static bool isReal(string str)
    {
        try { std::stod(str); return true; }
        catch (exception _e)
        {
            return false;
        }
    }

    static bool toBoolean(string str)
    {
        if ("true" == str || "1" == str)
        {
            return true;
        }
        return false;
    }

    static int toInteger(string str)
    {
        if (str.length() == 0)
        {
            return 0;
        }

        if (str[0] == '0' && str.length() > 1 && str[1] == 'x')
        {
            try {
                int x = std::stoi(str, 0, 16);
                return x;
            }
            catch (exception e) { return 0; }
        }
        else if (str[0] == '0' && str.length() > 1)
        {
            try {
                int y = std::stoi(str, 0, 8);
                return y;
            }
            catch (exception f)
            {
                return 0;
            }
        }
        try {
            int z = std::stoi(str, 0, 10);
            return z;
        }
        catch (exception g) { return 0; }
        return 0;
    }

    static long toLong(string str)
    {
        if (str.length() == 0)
        {
            return 0;
        }

        if (str[0] == '0' && str.length() > 1 && str[1] == 'x')
        {
            try {
                long x = std::stol(str, 0, 16);
                return x;
            }
            catch (exception e) { return 0; }
        }
        else if (str[0] == '0' && str.length() > 1)
        {
            try {
                long y = std::stol(str, 0, 8);
                return y;
            }
            catch (exception f)
            {
                return 0;
            }
        }
        try {
            long z = std::stol(str, 0, 10);
            return z;
        }
        catch (exception g) { return 0; }
        return 0;
    }

    static double toReal(string str)
    {
        if (str.length() == 0)
        {
            return 0.0;
        }

        try {
            double x = std::stod(str);
            return x;
        }
        catch (exception e) { return 0.0; }
    }



    static bool isLong(string str)
    {
        try { std::stol(str); return true; }
        catch (exception _e) { return false; }
    }


    static bool hasMatch(string str, string patt)
    {
        std::regex rr(patt);
        return std::regex_search(str, rr);
    }



    static bool isMatch(string str, string patt)
    {
        std::regex rr(patt);
        return std::regex_match(str, rr);
    }



    static vector<string>* allMatches(string s, string patt)
    {
        int slen = s.length();
        vector<string>* res = new vector<string>();
        if (slen == 0)
        {
            return res;
        }
        std::regex patt_regex(patt);
        auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);
        auto words_end = std::sregex_iterator();

        for (std::sregex_iterator i = words_begin; i != words_end; ++i)
        {
            std::smatch match = *i;
            std::string match_str = match.str();
            if (match_str.length() > 0)
            {
                res->push_back(match_str);
            }
        }
        return res;
    }


    static string firstMatch(string s, string patt)
    {
        int slen = s.length();
        string res = "";
        if (slen == 0)
        {
            return res;
        }
        std::regex patt_regex(patt);
        auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);
        auto words_end = std::sregex_iterator();

        for (std::sregex_iterator i = words_begin; i != words_end; ++i)
        {
            std::smatch match = *i;
            std::string match_str = match.str();
            if (match_str.length() > 0)
            {
                return match_str;
            }
        }
        return res;
    }


    static string replaceAll(string text, string patt, string rep)
    {
        std::regex patt_re(patt);
        std::string res = std::regex_replace(text, patt_re, rep);
        return res;
    }


    static string replaceFirstMatch(string text, string patt, string rep)
    {
        std::regex patt_re(patt);
        std::regex_constants::match_flag_type fonly =
            std::regex_constants::format_first_only;
        std::string res = std::regex_replace(text, patt_re, rep, fonly);
        return res;
    }


    static vector<string>* split(string s, string patt)
    {
        int slen = s.length();
        vector<string>* res = new vector<string>();
        if (slen == 0)
        {
            res->push_back(s);
            return res;
        }
        std::regex patt_regex(patt);
        auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);
        auto words_end = std::sregex_iterator();
        int prev = 0;
        for (std::sregex_iterator i = words_begin; i != words_end; ++i)
        {
            std::smatch match = *i;
            int pos = match.position(0);
            int ln = match.length(0);
            if (ln > 0)
            {
                string subst = s.substr(prev, pos - prev + 1);
                res->push_back(subst);
                prev = pos + ln;
            }
        }
        if (prev <= slen)
        {
            string lastst = s.substr(prev, slen - prev + 1);
            res->push_back(lastst);
        }
        return res;
    }


    static string replace(string s1, string s2, string rep)
    {
        int s1len = s1.length();
        int s2len = s2.length();
        int replen = rep.length();
        if (s1len == 0 || s2len == 0 || replen == 0)
        {
            return s1;
        }
        string result = "";
        int prev = 0;
        int m1 = s1.find(s2);
        if (m1 >= 0)
        {
            result = result + s1.substr(prev, m1 - prev) + rep;
            string remainder = s1.substr(m1 + s2len, s1len - (m1 + s2len));
            return result + replace(remainder, s2, rep);
        }
        return s1;
    }


    static string trim(string str)
    {
        int i = str.find_first_not_of("\n\t ");
        int j = str.find_last_not_of("\n\t ");
        if (i > j)
        {
            return "";
        }
        return str.substr(i, j - i + 1);
    }


    static bool includesAllMap(map<string, _T>* sup, map<string, _T>* sub)
    {
        for (auto iter = sub->begin(); iter != sub->end(); ++iter)
        {
            string key = iter->first;
            auto f = sup->find(key);
            if (f != sup->end())
            {
                if (iter->second == f->second) {}
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }


    static bool excludesAllMap(map<string, _T>* sup, map<string, _T>* sub)
    {
        for (auto iter = sub->begin(); iter != sub->end(); ++iter)
        {
            string key = iter->first;
            auto f = sup->find(key);
            if (f != sup->end())
            {
                if (iter->second == f->second)
                {
                    return false;
                }
            }
        }
        return true;
    }


    static map<string, _T>* includingMap(map<string, _T>* m, string src, _T trg)
    {
        map<string, _T>* copy = new map<string, _T>();
        for (auto iter = m->begin(); iter != m->end(); ++iter)
        {
            string key = iter->first;
            (*copy)[key] = iter->second;
        }
        (*copy)[src] = trg;
        return copy;
    }


    static map<string, _T>* excludeAllMap(map<string, _T>* m1, map<string, _T>* m2)
    {
        map<string, _T>* res = new map<string, _T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            string key = iter->first;
            auto f = m2->find(key);
            if (f != m2->end())
            {
                if (iter->second == f->second) {}
                else
                {
                    (*res)[key] = iter->second;
                }
            }
            else
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }


    static map<string, _T>* excludingMapKey(map<string, _T>* m, string k)
    { // m - { k |-> m(k) }  
        map<string, _T>* res = new map<string, _T>();
        for (auto iter = m->begin(); iter != m->end(); ++iter)
        {
            string key = iter->first;
            if (key == k) {}
            else
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }


    static map<string, _T>* excludingMapValue(map<string, _T>* m, _T v)
    { // m - { k |-> v }  
        map<string, _T>* res = new map<string, _T>();
        for (auto iter = m->begin(); iter != m->end(); ++iter)
        {
            string key = iter->first;
            if (iter->second == v) {}
            else
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }


    static map<string, _T>* unionMap(map<string, _T>* m1, map<string, _T>* m2)
    {
        map<string, _T>* res = new map<string, _T>();
        
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            string key = iter->first;
            if (m2->count(key) == 0)
            {
                (*res)[key] = iter->second;
            }
        }

        for (auto iter = m2->begin(); iter != m2->end(); ++iter)
        {
            string key = iter->first;
            (*res)[key] = iter->second;
        }
        return res;
    }


    static map<string, _T>* intersectionMap(map<string, _T>* m1, map<string, _T>* m2)
    {
        map<string, _T>* res = new map<string, _T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            string key = iter->first;
            if (m2->count(key) > 0)
            {
                if (m2->at(key) == iter->second)
                {
                    (*res)[key] = iter->second;
                }
            }
        }
        return res;
    }


    static std::set<string>* keys(map<string, _T>* s)
    {
        std::set<string>* res = new std::set<string>();

        for (auto iter = s->begin(); iter != s->end(); ++iter)
        {
            string key = iter->first;
            res->insert(key);
        }
        return res;
    }


    static vector<_T>* values(map<string, _T>* s)
    {
        vector<_T>* res = new vector<_T>();

        for (auto iter = s->begin(); iter != s->end(); ++iter)
        {
            _T value = iter->second;
            res->push_back(value);
        }
        return res;
    }


    static map<string, _T>* restrict(map<string, _T>* m1, std::set<string>* ks)
    { map<string, _T>* res = new map<string, _T>();
      for (auto iter = m1->begin(); iter != m1->end(); ++iter)
      {
        string key = iter->first;
        if (ks->find(key) != ks->end())
        {
            (*res)[key] = iter->second;
        }
      }
      return res;
    }

    static map<string, _T>* antirestrict(map<string, _T>* m1, std::set<string>* ks)
    {
        map<string, _T>* res = new map<string, _T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            string key = iter->first;
            if (ks->find(key) == ks->end())
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }

};


template<class S, class T, class R>
class UmlRsdsOcl {
public:

    static T iterate(vector<S>* _s, T initialValue, std::function<T(S, T)> _f)
    {
        T acc = initialValue;
        for (auto _pos = _s->begin(); _pos != _s->end(); ++_pos)
        {
            S _x = *_pos;
            acc = _f(_x, acc);
        }
        return acc;
    }

    static map<S, T>* includingMap(map<S, T>* m, S src, T trg)
    {
        map<S, T>* copy = new map<S, T>();
        for (auto iter = m->begin(); iter != m->end(); ++iter)
        {
            S key = iter->first;
            (*copy)[key] = iter->second;
        }
        (*copy)[src] = trg;
        return copy;
    }


    static map<S, T>* unionMap(map<S, T>* m1, map<S, T>* m2)
    {
        map<S, T>* res = new map<S, T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            S key = iter->first;
            if (m2->count(key) == 0)
            {
                (*res)[key] = iter->second;
            }
        }
        for (auto iter = m2->begin(); iter != m2->end(); ++iter)
        {
            S key = iter->first;
            (*res)[key] = iter->second;
        }
        return res;
    }


    static bool includesAllMap(map<S, T>* sup, map<S, T>* sub)
    {
        for (auto iter = sub->begin(); iter != sub->end(); ++iter)
        {
            S key = iter->first;
            auto f = sup->find(key);
            if (f != sup->end())
            {
                if (iter->second == f->second) {}
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }


    static bool excludesAllMap(map<S, T>* sup, map<S, T>* sub)
    {
        for (auto iter = sub->begin(); iter != sub->end(); ++iter)
        {
            S key = iter->first;
            auto f = sup->find(key);
            if (f != sup->end())
            {
                if (iter->second == f->second)
                {
                    return false;
                }
            }
        }
        return true;
    }


    static map<S, T>* excludeAllMap(map<S, T>* m1, map<S, T>* m2)
    {
        map<S, T>* res = new map<S, T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            S key = iter->first;
            auto f = m2->find(key);
            if (f != m2->end())
            {
                if (iter->second == f->second) {}
                else
                {
                    (*res)[key] = iter->second;
                }
            }
            else
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }


    static map<S, T>* excludingMapKey(map<S, T>* m, string k)
    { // m - { k |-> m(k) }  
        map<S, T>* res = new map<S, T>();
        for (auto iter = m->begin(); iter != m->end(); ++iter)
        {
            S key = iter->first;
            if (key == k) {}
            else
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }


    static map<S, T>* excludingMapValue(map<S, T>* m, T v)
    { // m - { k |-> v }  
        map<S, T>* res = new map<S, T>();
        for (auto iter = m->begin(); iter != m->end(); ++iter)
        {
            S key = iter->first;
            if (iter->second == v) {}
            else
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }




    static map<S, T>* intersectionMap(map<S, T>* m1, map<S, T>* m2)
    {
        map<S, T>* res = new map<S, T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            S key = iter->first;
            if (m2->count(key) > 0)
            {
                if (m2->at(key) == iter->second)
                {
                    (*res)[key] = iter->second;
                }
            }
        }
        return res;
    }

    static std::set<S>* keys(map<S, T>* s)
    {
        std::set<S>* res = new std::set<S>();

        for (auto iter = s->begin(); iter != s->end(); ++iter)
        {
            S key = iter->first;
            res->insert(key);
        }
        return res;
    }


    static vector<T>* values(map<S, T>* s)
    {
        vector<T>* res = new vector<T>();

        for (auto iter = s->begin(); iter != s->end(); ++iter)
        {
            T value = iter->second;
            res->push_back(value);
        }
        return res;
    }


    static map<S, T>* restrict(map<S, T>* m1, std::set<S>* ks)
    { map<S, T>* res = new map<S, T>();
      for (auto iter = m1->begin(); iter != m1->end(); ++iter)
      {
        S key = iter->first;
        if (ks->find(key) != ks->end())
        {
            (*res)[key] = iter->second;
        }
      }
      return res;
    }

    static map<S, T>* antirestrict(map<S, T>* m1, std::set<S>* ks)
    {
        map<S, T>* res = new map<S, T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            S key = iter->first;
            if (ks->find(key) == ks->end())
            {
                (*res)[key] = iter->second;
            }
        }
        return res;
    }

    static map<S, T>* selectMap(map<S, T>* m1, std::function<bool(T)> f)
    {
        map<S, T>* res = new map<S, T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            S key = iter->first;
            T val = iter->second;
            if (f(val))
            {
                (*res)[key] = val;
            }
        }
        return res;
    }

    static map<S, T>* rejectMap(map<S, T>* m1, std::function<bool(T)> f)
    {
        map<S, T>* res = new map<S, T>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            S key = iter->first;
            T val = iter->second;
            if (f(val)) {}
            else
            {
                (*res)[key] = val;
            }
        }
        return res;
    }

    static map<S, R>* collectMap(map<S, T>* m1, std::function<R(T)> f)
    {
        map<S, R>* res = new map<S, R>();
        for (auto iter = m1->begin(); iter != m1->end(); ++iter)
        {
            S key = iter->first;
            T val = iter->second;

            (*res)[key] = f(val);
        }
        return res;
    }


};




class OclAttribute;
class OclOperation;
class OclType;


class OclAttribute
{
private:
    string name;
    OclType* type;

public:
    OclAttribute(OclType* type_x)
    {
        name = "";
        type = type_x;
    }

    OclAttribute() { }


    friend ostream& operator<<(ostream& s, OclAttribute& x)
    {
        return s << "(OclAttribute) " << x.getName() << endl;
    }


    void setname(string name_x) { name = name_x; }

    void settype(OclType* typexx) { type = typexx; }

    string getName() { return name; }

    OclType* getType() { return type; }
};

class OclOperation
{
private:
    string name;
    OclType* type;
    vector<OclAttribute*>* parameters;

public:
    OclOperation(OclType* type_x)
    {
        name = "";
        type = type_x;
        parameters = new vector<OclAttribute*>();
    }

    OclOperation() { }


    friend ostream& operator<<(ostream& s, OclOperation& x)
    {
        return s << "(OclOperation) " << x.getname() << endl;
    }

    void setname(string name_x) { name = name_x; }

    void settype(OclType* typexx) { type = typexx; }

    void setparameters(vector<OclAttribute*>* parametersxx)
    {
        parameters->clear();
        parameters->insert(parameters->end(), parametersxx->begin(), parametersxx->end());
    }

    void setparameters(int ind_x, OclAttribute* parametersxx)
    {
        if (ind_x >= 0 && ind_x < parameters->size()) { (*parameters)[ind_x] = parametersxx; }
    }

    void addparameters(OclAttribute* parametersxx)
    {
        parameters->push_back(parametersxx);
    }

    string getname() { return name; }

    OclType* gettype() { return type; }

    vector<OclAttribute*>* getparameters() { return parameters; }

    string getName() { return name; }

    OclType* getType() { return type; }

    OclType* getReturnType() { return type; }

    vector<OclAttribute*>* getParameters()
    {
        return parameters;
    }

};


class OclType
{
private:
    string name;
    string oclname;
    vector<OclAttribute*>* attributes;
    vector<OclOperation*>* operations;
    vector<OclOperation*>* constructors;
    vector<OclType*>* innerClasses;
    vector<OclType*>* componentType;
    vector<OclType*>* superclasses;
    vector<OclType*>* subclasses;
    bool isinterface;
    void* (*creator)();
    static map<string, OclType*>* ocltypenameindex;

public:
    OclType()
    {
        oclname = "";
        name = "";
        attributes = new vector<OclAttribute*>();
        operations = new vector<OclOperation*>();
        constructors = new vector<OclOperation*>();
        innerClasses = new vector<OclType*>();
        componentType = new vector<OclType*>();
        superclasses = new vector<OclType*>();
        subclasses = new vector<OclType*>();
        isinterface = false;
    }

    static OclType* createOclType(string nme)
    {
        OclType* res = new OclType();
        res->oclname = nme;
        (*ocltypenameindex)[nme] = res;
        return res;
    }

    static OclType* getOclTypeByPK(string namex)
    {
        if (ocltypenameindex->find(namex) == ocltypenameindex->end())
        {
            return 0;
        }
        return ocltypenameindex->at(namex);
    }

    friend ostream& operator<<(ostream& s, OclType& x)
    {
        return s << "(OclType) " << x.getname() << endl;
    }

    void setname(string name_x)
    {
        name = name_x;
        (*OclType::ocltypenameindex)[name_x] = this;
    }

    void setoclname(string name_x)
    {
        oclname = name_x;
    }

    void setcreator(void* (*f)())
    {
        creator = f;
    }

    void setattributes(vector<OclAttribute*>* attributesxx)
    {
        attributes->clear();
        attributes->insert(attributes->end(), attributesxx->begin(), attributesxx->end());
    }

    void setattributes(int ind_x, OclAttribute* attributesxx)
    {
        if (ind_x >= 0 && ind_x < attributes->size())
        {
            (*attributes)[ind_x] = attributesxx;
        }
    }

    void addattributes(OclAttribute* attributesxx)
    {
        attributes->push_back(attributesxx);
    }

    void setoperations(vector<OclOperation*>* operationsxx)
    {
        operations->clear();
        operations->insert(operations->end(), operationsxx->begin(), operationsxx->end());
    }

    void setoperations(int ind_x, OclOperation* operationsxx)
    {
        if (ind_x >= 0 && ind_x < operations->size())
        {
            (*operations)[ind_x] = operationsxx;
        }
    }

    void addoperations(OclOperation* operationsxx)
    {
        operations->push_back(operationsxx);
    }

    void setsuperclasses(vector<OclType*>* superclassesxx)
    {
        superclasses->clear();
        superclasses->insert(superclasses->end(), superclassesxx->begin(), superclassesxx->end());
    }

    void setsuperclasses(int ind_x, OclType* superclassesxx)
    {
        if (ind_x >= 0 && ind_x < superclasses->size())
        {
            (*superclasses)[ind_x] = superclassesxx;
        }
    }

    void setisinterface(bool intf)
    {
        isinterface = intf;
    }

    void addsuperclasses(OclType* superclassesxx)
    {
        superclasses->push_back(superclassesxx);
        superclassesxx->subclasses->push_back(this);
    }

    string getname() { return name; }

    vector<OclAttribute*>* getattributes()
    {
        return attributes;
    }

    vector<OclOperation*>* getoperations()
    {
        return operations;
    }

    string getName()
    {
        return oclname;
    }

    // vector<OclType*>* getClasses();

    // vector<OclType*>* getDeclaredClasses();

    // OclType* getComponentType();

    vector<OclAttribute*>* getFields()
    {
        return attributes;
    }

    OclAttribute* getDeclaredField(string s)
    {
        return getField(s);
    }

    OclAttribute* getField(string s)
    {
        for (int i = 0; i < attributes->size(); i++)
        {
            OclAttribute* att = attributes->at(i);
            if (att->getName() == s)
            {
                return att;
            }
        }
        return NULL;
    }

    vector<OclOperation*>* getConstructors()
    {
        return constructors;
    }

    OclType* getSuperclass()
    {
        if (superclasses->size() > 0)
        {
            return superclasses->at(0);
        }
        return NULL;
    }


    vector<OclAttribute*>* getDeclaredFields()
    {
        return attributes;
    }

    vector<OclOperation*>* getMethods()
    {
        return operations;
    }

    template<class X>
    static bool hasAttribute(X obj, string nme)
    {   // cout << "Type of obj is: " << typeid(obj).name() << endl;

        OclType* typ = getOclTypeByPK(typeid(obj).name());
        if (typ == NULL)
        {
            return false;
        }

        OclAttribute* fld = typ->getField(nme);
        if (fld == NULL)
        {
            return false;
        }
        return true;
    }

    /* getAttributeValue and setAttributeValue and removeAttribute are unavailable */

    bool isArray()
    {
        return "Sequence" == oclname;
    }

    bool isPrimitive()
    {
        return "int" == oclname || "double" == oclname || "long" == oclname ||
            "boolean" == oclname;
    }

    bool isInterface()
    {
        return isinterface;
    }

    bool isAssignableFrom(OclType* c)
    {
        if (c->oclname == oclname)
        {
            return true;
        }

        for (int i = 0; i < subclasses->size(); i++)
        {
            OclType* sub = subclasses->at(i);
            if (sub->isAssignableFrom(c))
            {
                return true;
            }
        }

        return false;
    }

    template<class X>
    bool isInstance(X obj)
    {
        OclType* typ = getOclTypeByPK(typeid(obj).name());
        if (typ == NULL)
        {
            return false;
        }

        return isAssignableFrom(typ);
    }

    void* newInstance()
    {
        if (creator == NULL)
        {
            return NULL;
        }
        return creator();
    }
};




class io_exception : exception
{
public:
    io_exception() : message("IO exception") { }

    io_exception* copyio_exception(io_exception* self)
    {
        io_exception* ex = new io_exception();
        ex->message = self->message;
        return ex;
    }

    const char* what() const { return message; }

    string getMessage() { return string(message); }

    void printStackTrace()
    {
        cout << what() << endl;
    }

    exception* getCause()
    {
        return this;
    }

    static io_exception* newIOException(string m)
    {
        io_exception* res = new io_exception();
        res->message = m.c_str();
        return res;
    }
private:
    const char* message;
};

class null_access_exception : exception
{
public:
    null_access_exception() : message("Null access exception") { }

    null_access_exception* copynull_access_exception(null_access_exception* self)
    {
        null_access_exception* ex = new null_access_exception();
        ex->message = self->message;
        return ex;
    }

    const char* what() const { return message; }

    string getMessage() { return string(message); }

    void printStackTrace()
    {
        cout << what() << endl;
    }

    exception* getCause()
    {
        return this;
    }

    static null_access_exception* newNullAccessException(string m)
    {
        null_access_exception* res = new null_access_exception();
        res->message = m.c_str();
        return res;
    }
private:
    const char* message;
};

class assertion_exception : exception
{
public:
    assertion_exception() : message("Assertion exception") { }

    assertion_exception* copyassertion_exception(assertion_exception* self)
    {
        assertion_exception* ex = new assertion_exception();
        ex->message = self->message;
        return ex;
    }

    const char* what() const { return message; }

    string getMessage() { return string(message); }

    void printStackTrace()
    {
        cout << what() << endl;
    }

    exception* getCause()
    {
        return this;
    }

    static assertion_exception* newAssertionException(string m)
    {
        assertion_exception* res = new assertion_exception();
        res->message = m.c_str();
        return res;
    }
private:
    const char* message;
};

class accessing_exception : exception
{
public:
    accessing_exception() : message("Accessing exception") { }

    accessing_exception* copyaccessing_exception(accessing_exception* self)
    {
        accessing_exception* ex = new accessing_exception();
        ex->message = self->message;
        return ex;
    }

    const char* what() const { return message; }

    string getMessage() { return string(message); }

    void printStackTrace()
    {
        cout << what() << endl;
    }

    exception* getCause()
    {
        return this;
    }

    static accessing_exception* newAccessingException(string m)
    {
        accessing_exception* res = new accessing_exception();
        res->message = m.c_str();
        return res;
    }
private:
    const char* message;
};



