using namespace std;

/* To be included in Controller.cpp code file

Assumes libraries: 
#include <string>
#include <vector>
#include <set>
#include <map>
#include <iostream>
#include <fstream>
#include <cmath>
#include <algorithm>
#include <regex> */ 


template<class _T>
class UmlRsdsLib { 
public: 


    static bool isIn(_T x, set<_T>* st)
    { return (st->find(x) != st->end()); }

    static bool isIn(_T x, vector<_T>* sq)
    { return (find(sq->begin(), sq->end(), x) != sq->end()); }

    static bool isSubset(set<_T>* s1, set<_T>* s2)
    { bool res = true; 
      for (set<_T>::iterator _pos = s1->begin(); _pos != s1->end(); ++_pos)
      { if (isIn(*_pos, s2)) { } else { return false; } }
      return res; }


    static bool isSubset(set<_T>* s1, vector<_T>* s2)
    { bool res = true; 
      for (set<_T>::iterator _pos = s1->begin(); _pos != s1->end(); ++_pos)
      { if (isIn(*_pos, s2)) { } else { return false; } }
      return res; }


    static set<_T>* makeSet(_T x)
    { set<_T>* res = new set<_T>();
      if (x != NULL) { res->insert(x); }
      return res;
    }

    static vector<_T>* makeSequence(_T x)
    { vector<_T>* res = new vector<_T>();
      if (x != NULL) { res->push_back(x); } return res;
    }

    static set<_T>* addSet(set<_T>* s, _T x)
    { if (x != NULL) { s->insert(x); }
      return s; }

    static vector<_T>* addSequence(vector<_T>* s, _T x)
    { if (x != NULL) { s->push_back(x); }
      return s; }

    static vector<_T>* asSequence(set<_T>* c)
    { vector<_T>* res = new vector<_T>();
      for (set<_T>::iterator _pos = c->begin(); _pos != c->end(); ++_pos)
      { res->push_back(*_pos); } 
      return res; }

    static set<_T>* asSet(vector<_T>* c)
    { set<_T>* res = new set<_T>(); 
      for (vector<_T>::iterator _pos = c->begin(); _pos != c->end(); ++_pos)
      { res->insert(*_pos); } 
      return res; 

    }


  static vector<string>* tokenise(vector<string>* res, string str)
  { bool inspace = true; 
    string* current = new string(""); 
    for (int i = 0; i < str.length(); i++)
    { if (str[i] == '.' || isspace(str[i]) > 0)
      { if (inspace) {}
        else 
        { res->push_back(*current);
          current = new string(""); 
          inspace = true;
        }
      }
      else 
      { if (inspace) { inspace = false; }
        current->append(str.substr(i,1)); 
      }
    }
    if (current->length() > 0) { res->push_back(*current); }
    delete current;
    return res;
  }



  static int oclRound(double d)
  { int f = (int) floor(d);
    if (d >= f + 0.5)
    { return f+1; }
    else 
    { return f; }
  }



  static _T max(set<_T>* l)
  { return *std::max_element(l->begin(), l->end()); }
  static _T max(vector<_T>* l)
  { return *std::max_element(l->begin(), l->end()); }


  static _T min(set<_T>* l)
  { return *std::min_element(l->begin(), l->end()); }
  static _T min(vector<_T>* l)
  { return *std::min_element(l->begin(), l->end()); }


  static set<_T>* unionSet(set<_T>* a, set<_T>* b)
  { set<_T>* res = new set<_T>(); 
    res->insert(a->begin(),a->end()); 
    res->insert(b->begin(),b->end()); 
    return res; }
  static set<_T>* unionSet(vector<_T>* a, set<_T>* b)
  { set<_T>* res = new set<_T>(); 
    res->insert(a->begin(),a->end()); 
    res->insert(b->begin(),b->end()); 
    return res; }
  static set<_T>* unionSet(set<_T>* a, vector<_T>* b)
  { set<_T>* res = new set<_T>(); 
    res->insert(a->begin(),a->end()); 
    res->insert(b->begin(),b->end()); 
    return res; }


  static vector<_T>* subtract(vector<_T>* a, vector<_T>* b)
  { vector<_T>* res = new vector<_T>(); 
    for (int i = 0; i < a->size(); i++)
    { if ((*a)[i] == NULL || UmlRsdsLib<_T>::isIn((*a)[i],b)) { }
      else { res->push_back((*a)[i]); }
    }
    return res;
  }

  static vector<_T>* subtract(vector<_T>* a, set<_T>* b)
  { vector<_T>* res = new vector<_T>(); 
    for (int i = 0; i < a->size(); i++)
    { if ((*a)[i] == NULL || UmlRsdsLib<_T>::isIn((*a)[i],b)) { }
      else { res->push_back((*a)[i]); }
    }
    return res;
  }

  static set<_T>* subtract(set<_T>* a, set<_T>* b)
  { set<_T>* res = new set<_T>(); 
    for (set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)
    { if (*_pos == NULL || UmlRsdsLib<_T>::isIn(*_pos,b)) { }
      else { res->insert(*_pos); }
    }
    return res;
  }

  static set<_T>* subtract(set<_T>* a, vector<_T>* b)
  { set<_T>* res = new set<_T>(); 
    for (set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)
    { if (*_pos == NULL || UmlRsdsLib<_T>::isIn(*_pos,b)) { }
      else { res->insert(*_pos); }
    }
    return res;
  }

  static string subtract(string a, string b)
  { string res = ""; 
    for (int i = 0; i < a.length(); i++)
    { if (b.find(a[i]) == string::npos) { res = res + a[i]; } }
    return res; }



  static set<_T>* intersection(set<_T>* a, set<_T>* b)
  { set<_T>* res = new set<_T>(); 
    for (set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)
    { if (*_pos != NULL && UmlRsdsLib<_T>::isIn(*_pos, b)) { res->insert(*_pos); } }
    return res; }

  static set<_T>* intersection(set<_T>* a, vector<_T>* b)
  { set<_T>* res = new set<_T>(); 
    for (set<_T>::iterator _pos = a->begin(); _pos != a->end(); ++_pos)
    { if (*_pos != NULL && UmlRsdsLib<_T>::isIn(*_pos, b)) { res->insert(*_pos); } }
    return res; }

  static vector<_T>* intersection(vector<_T>* a, set<_T>* b)
  { vector<_T>* res = new vector<_T>(); 
    for (int i = 0; i < a->size(); i++)
    { if ((*a)[i] != NULL && UmlRsdsLib<_T>::isIn((*a)[i], b)) { res->push_back((*a)[i]); } } 
    return res; }

  static vector<_T>* intersection(vector<_T>* a, vector<_T>* b)
  { vector<_T>* res = new vector<_T>(); 
    for (int i = 0; i < a->size(); i++)
    { if ((*a)[i] != NULL && UmlRsdsLib<_T>::isIn((*a)[i], b)) { res->push_back((*a)[i]); } } 
    return res; }



     static set<_T>* symmetricDifference(vector<_T>* a, vector<_T>* b)
    { set<_T>* res = new set<_T>();
      for (int i = 0; i < a->size(); i++)
      { if (UmlRsdsLib<_T>::isIn((*a)[i], b)) { }
        else { res->insert((*a)[i]); }
      }
      for (int i = 0; i < b->size(); i++)
      { if (UmlRsdsLib<_T>::isIn((*b)[i], a)) { }
        else { res->insert((*b)[i]); }
      }
      return res;
    }

    static set<_T>* symmetricDifference(set<_T>* a, vector<_T>* b)
    { set<_T>* res = new set<_T>();
      for (set<_T>::iterator _pos = a->begin(); _pos != a->end(); _pos++)
      { if (UmlRsdsLib<_T>::isIn(*_pos, b)) { }
        else { res->insert(*_pos); }
      }
      for (int i = 0; i < b->size(); i++)
      { if (UmlRsdsLib<_T>::isIn((*b)[i], a)) { }
        else { res->insert((*b)[i]); }
      }
      return res;
    }

     static set<_T>* symmetricDifference(vector<_T>* a, set<_T>* b)
    { set<_T>* res = new set<_T>();
      for (int i = 0; i < a->size(); i++)
      { if (UmlRsdsLib<_T>::isIn((*a)[i], b)) { }
        else { res->insert((*a)[i]); }
      }
      for (set<_T>::iterator _pos = b->begin(); _pos != b->end(); _pos++)
      { if (UmlRsdsLib<_T>::isIn(*_pos, a)) { }
        else { res->insert(*_pos); }
      }
      return res;
    }

    static set<_T>* symmetricDifference(set<_T>* a, set<_T>* b)
    { set<_T>* res = new set<_T>();
      for (set<_T>::iterator _pos = a->begin(); _pos != a->end(); _pos++)
      { if (UmlRsdsLib<_T>::isIn(*_pos, b)) { }
        else { res->insert(*_pos); }
      }
      for (set<_T>::iterator _pos = b->begin(); _pos != b->end(); _pos++)
      { if (UmlRsdsLib<_T>::isIn(*_pos, a)) { }
        else { res->insert(*_pos); }
      }
      return res;
    }



  static bool isUnique(vector<_T>* evals)
  { set<_T> vals; 
    for (int i = 0; i < evals->size(); i++)
    { _T ob = (*evals)[i]; 
      if (vals.find(ob) != vals.end()) { return false; }
      vals.insert(ob);
    }
    return true;
  }
  static bool isUnique(set<_T>* evals)
  { return true; }


  static string sumString(vector<string>* a)
  { string _sum(""); 
    for (int i = 0; i < a->size(); i++)
    { _sum.append( (*a)[i] ); }
    return _sum; }

  static string sumString(set<string>* a)
  { string _sum(""); 
    set<string>::iterator _pos;
    for (_pos = a->begin(); _pos != a->end(); ++_pos)
    { _sum.append( *_pos ); }
    return _sum; }

  static _T sum(vector<_T>* a)
  { _T _sum(0); 
    for (int i = 0; i < a->size(); i++)
    { _sum += (*a)[i]; }
    return _sum; }

  static _T sum(set<_T>* a)
  { _T _sum(0); 
    set<_T>::iterator _pos;
    for (_pos = a->begin(); _pos != a->end(); ++_pos)
    { _sum += *_pos; }
    return _sum; }



  static _T prd(vector<_T>* a)
  { _T _prd(1); 
    for (int i = 0; i < a->size(); i++)
    { _prd *= (*a)[i]; }
    return _prd; }

  static _T prd(set<_T>* a)
  { _T _prd(1); 
    set<_T>::iterator _pos;
    for (_pos = a->begin(); _pos != a->end(); ++_pos)
    { _prd *= *_pos; }
    return _prd; }



  static vector<_T>* concatenate(vector<_T>* a, vector<_T>* b)
  { vector<_T>* res = new vector<_T>(); 
    res->insert(res->end(), a->begin(),a->end()); 
    res->insert(res->end(), b->begin(),b->end()); 
    return res; }




  static vector<_T>* reverse(vector<_T>* a)
  { vector<_T>* res = new vector<_T>(); 
    res->insert(res->end(), a->begin(), a->end()); 
    std::reverse(res->begin(), res->end()); 
    return res; }

  static string reverse(string a)
  { string res(""); 
    for (int i = a.length() - 1; i >= 0; i--)
    { res = res + a[i]; } 
    return res; }



  static vector<_T>* front(vector<_T>* a)
  { vector<_T>* res = new vector<_T>(); 
    if (a->size() == 0) { return res; } 
    vector<_T>::iterator _pos = a->end(); 
    _pos--; 
    res->insert(res->end(), a->begin(), _pos); 
    return res; }


  static vector<_T>* tail(vector<_T>* a)
  { vector<_T>* res = new vector<_T>(); 
    if (a->size() == 0) { return res; } 
    vector<_T>::iterator _pos = a->begin(); 
    _pos++; 
    res->insert(res->end(), _pos, a->end()); 
    return res; }


  static vector<_T>* sort(vector<_T>* a)
  { vector<_T>* res = new vector<_T>();
    res->insert(res->end(), a->begin(), a->end());
    std::sort(res->begin(), res->end());
    return res;
  }

  static vector<_T>* sort(set<_T>* a)
  { vector<_T>* res = new vector<_T>();
    res->insert(res->end(), a->begin(), a->end());
    std::sort(res->begin(), res->end());
    return res;
  }



  static vector<int>* integerSubrange(int i, int j)
  { vector<int>* tmp = new vector<int>(); 
    for (int k = i; k <= j; k++)
    { tmp->push_back(k); } 
    return tmp;
  }

  static string subrange(string s, int i, int j)
  { return s.substr(i-1,j-i+1); }

  static vector<_T>* subrange(vector<_T>* l, int i, int j)
  { vector<_T>* tmp = new vector<_T>(); 
    tmp->insert(tmp->end(), (l->begin()) + (i - 1), (l->begin()) + j);
    return tmp; 
  }



  static vector<_T>* prepend(vector<_T>* l, _T ob)
  { vector<_T>* res = new vector<_T>();
    res->push_back(ob);
    res->insert(res->end(), l->begin(), l->end());
    return res;
  }


  static vector<_T>* append(vector<_T>* l, _T ob)
  { vector<_T>* res = new vector<_T>();
    res->insert(res->end(), l->begin(), l->end());
    res->push_back(ob);
    return res;
  }


  static int count(set<_T>* l, _T obj)
  { if (l->find(obj) != l->end()) { return 1; } else { return 0; } 
  }

  static int count(vector<_T>* l, _T obj)
  { return std::count(l->begin(), l->end(), obj); }

  static int count(string s, string x)
  { int res = 0; 
    if (s.length() == 0) { return res; }
    int ind = s.find(x); 
    if (ind == string::npos) { return res; }
    string ss = s.substr(ind+1, s.length() - ind - 1);
    res++; 
    while (ind != string::npos)
    { ind = ss.find(x); 
      if (ind == string::npos || ss.length() == 0) { return res; }
      res++; 
      ss = ss.substr(ind+1, ss.length() - ind - 1);
    } 
    return res;
  }



  static vector<string>* characters(string str)
  { vector<string>* _res = new vector<string>();
    for (int i = 0; i < str.size(); i++)
    { _res->push_back(str.substr(i,1)); }
    return _res;
  }



  static _T any(vector<_T>* v)
    { if (v->size() == 0) { return 0; }
      return v->at(0);
    }

  static _T any(set<_T>* v)
    { if (v->size() == 0) { return 0; }
      set<_T>::iterator _pos = v->begin();
      return *_pos;
    }



  static _T first(vector<_T>* v)
    { if (v->size() == 0) { return 0; }
      return v->at(0);
    }

  static _T first(set<_T>* v)
    { if (v->size() == 0) { return 0; }
      set<_T>::iterator _pos = v->begin();
      return *_pos;
    }



  static _T last(vector<_T>* v)
  { if (v->size() == 0) { return 0; }
    return v->at(v->size() - 1);
  }

  static _T last(set<_T>* v)
  { if (v->size() == 0) { return 0; }
    set<_T>::iterator _pos = v->end();
    _pos--; return *_pos;
  }



  static vector<_T>* maximalElements(vector<_T>* s, vector<int>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    int largest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { int next = (*v)[i];
      if (next > largest)
      { largest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (largest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }

  static vector<_T>* maximalElements(vector<_T>* s, vector<long>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    long largest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { long next = (*v)[i];
      if (next > largest)
      { largest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (largest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }

  static vector<_T>* maximalElements(vector<_T>* s, vector<string>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    string largest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { string next = (*v)[i];
      if (next > largest)
      { largest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (largest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }

  static vector<_T>* maximalElements(vector<_T>* s, vector<double>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    double largest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { double next = (*v)[i];
      if (next > largest)
      { largest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (largest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }


  static vector<_T>* minimalElements(vector<_T>* s, vector<int>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    int smallest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { int next = (*v)[i];
      if (next < smallest)
      { smallest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (smallest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }

  static vector<_T>* minimalElements(vector<_T>* s, vector<long>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    long smallest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { long next = (*v)[i];
      if (next < smallest)
      { smallest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (smallest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }

  static vector<_T>* minimalElements(vector<_T>* s, vector<string>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    string smallest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { string next = (*v)[i];
      if (next < smallest)
      { smallest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (smallest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }

  static vector<_T>* minimalElements(vector<_T>* s, vector<double>* v)
  { vector<_T>* res = new vector<_T>();
    if (s->size() == 0) { return res; }
    double smallest = (*v)[0];
    res->push_back((*s)[0]);
    
    for (int i = 1; i < s->size(); i++)
    { double next = (*v)[i];
      if (next < smallest)
      { smallest = next;
        res->clear();
        res->push_back((*s)[i]);
      }
      else if (smallest == next)
      { res->push_back((*s)[i]); }
    }
    return res;
  }


  static set<_T>* intersectAll(set<set<_T>*>* se)
  { set<_T>* res = new set<_T>(); 
    if (se->size() == 0) { return res; }
    set<set<_T>*>::iterator _pos = se->begin();
    set<_T>* frst = *_pos;
    res->insert(frst->begin(), frst->end());
    ++_pos; 
    for (; _pos != se->end(); ++_pos)
    { res = UmlRsdsLib<_T>::intersection(res, *_pos); }
    return res;
  }

  static set<_T>* intersectAll(set<vector<_T>*>* se)
  { set<_T>* res = new set<_T>(); 
    if (se->size() == 0) { return res; }
    set<vector<_T>*>::iterator _pos = se->begin();
    vector<_T>* frst = *_pos;
    res->insert(frst->begin(), frst->end());
    ++_pos; 
    for (; _pos != se->end(); ++_pos)
    { res = UmlRsdsLib<_T>::intersection(res, *_pos); }
    return res;
  }

  static set<_T>* intersectAll(vector<set<_T>*>* se)
  { set<_T>* res = new set<_T>(); 
    if (se->size() == 0) { return res; }
    set<_T>* frst = (*se)[0];
    res->insert(frst->begin(), frst->end());
    for (int i = 1; i < se->size(); ++i)
    { res = UmlRsdsLib<_T>::intersection(res, (*se)[i]); }
    return res;
  }

  static vector<_T>* intersectAll(vector<vector<_T>*>* se)
  { vector<_T>* res = new vector<_T>(); 
    if (se->size() == 0) { return res; }
    vector<_T>* frst = (*se)[0];
    res->insert(res->end(), frst->begin(), frst->end());
    for (int i = 1; i < se->size(); ++i)
    { res = UmlRsdsLib<_T>::intersection(res, (*se)[i]); }
    return res;
  }



  static set<_T>* unionAll(set<set<_T>*>* se)
  { set<_T>* res = new set<_T>(); 
    if (se->size() == 0) { return res; }
    set<set<_T>*>::iterator _pos;
    for (_pos = se->begin(); _pos != se->end(); ++_pos)
    { res = UmlRsdsLib<_T>::unionSet(res, *_pos); }
    return res;
  }

  static set<_T>* unionAll(set<vector<_T>*>* se)
  { set<_T>* res = new set<_T>(); 
    if (se->size() == 0) { return res; }
    set<vector<_T>*>::iterator _pos;
    for (_pos = se->begin(); _pos != se->end(); ++_pos)
    { res = UmlRsdsLib<_T>::unionSet(res, *_pos); }
    return res;
  }

  static set<_T>* unionAll(vector<set<_T>*>* se)
  { set<_T>* res = new set<_T>(); 
    if (se->size() == 0) { return res; }
    for (int i = 0; i < se->size(); ++i)
    { res = UmlRsdsLib<_T>::unionSet(res, (*se)[i]); }
    return res;
  }



  static vector<_T>* insertAt(vector<_T>* l, int ind, _T ob)
  { vector<_T>* res = new vector<_T>();
    res->insert(res->end(), l->begin(), l->end());
    res->insert(res->begin() + (ind - 1), ob);
    return res; 
  }
  static string insertAt(string l, int ind, string ob)
  { string res(l);
    res.insert(ind-1,ob);
    return res;
  }


  static int indexOf(_T x, vector<_T>* a)
  { int res = 0; 
    for (int i = 0; i < a->size(); i++)
    { if (x == (*a)[i]) { return i+1; } }
    return res; 
  }

  static int indexOf(string x, string str)
  { int res = str.find(x); 
    if (res == string::npos) { return 0; }
    return res + 1; 
  } 

  static int lastIndexOf(_T x, vector<_T>* a)
  { int res = 0; 
    for (int i = a->size() - 1; i >= 0; i--)
    { if (x == (*a)[i]) { return i+1; } }
    return res; 
  }

  static int lastIndexOf(string x, string str)
  { int res = str.rfind(x); 
    if (res == string::npos) { return 0; }
    return res + 1; 
  } 



  static string toLowerCase(string str)
  { string res(str);
    for (int i = 0; i < str.length(); i++)
    { res[i] = tolower(str[i]); }
    return res; 
  }

  static string toUpperCase(string str)
  { string res(str);
    for (int i = 0; i < str.length(); i++)
    { res[i] = toupper(str[i]); }
    return res;
  }

  static bool equalsIgnoreCase(string str1, string str2)
  { int len1 = str1.length();
    int len2 = str2.length();
    if (len1 != len2) { return false; }
    for (int i = 0; i < len1; i++)
    { if (tolower(str1[i]) == tolower(str2[i])) { }
      else { return false; }
    }
    return true;
  }



  static bool startsWith(string s1, string s2)
  { int l1 = s1.length(); 
    int l2 = s2.length();
    if (l1 < l2) { return false; }
    if (s1.substr(0,l2) == s2) { return true; }
    return false; 
  }

  static bool endsWith(string s1, string s2)
  { int l1 = s1.length(); 
    int l2 = s2.length();
    if (l1 < l2) { return false; }
    if (s1.substr(l1-l2,l2) == s2) { return true; }
    return false; 
  }



 static bool isInteger(string str)
  { try { std::stoi(str); return true; }
    catch (exception _e) { return false; }
  }


 static bool isReal(string str)
  { try { std::stod(str); return true; }
    catch (exception _e) { return false; }
  }


 static bool isLong(string str)
  { try { std::stol(str); return true; }
    catch (exception _e) { return false; }
  }


  static bool hasMatch(string str, string patt)
  { std::regex rr(patt);
    return std::regex_search(str,rr);
  }



  static bool isMatch(string str, string patt)
  { std::regex rr(patt);
    return std::regex_match(str,rr);
  }

  static string trim(string str)
  { int i = str.find_first_not_of("\n\t "); 
    int j = str.find_last_not_of("\n\t "); 
    if (i > j) 
    { return ""; }
    return str.substr(i, j-i+1); 
  } 

  static vector<string>* allMatches(string s, string patt)
  { int slen = s.length(); 
    vector<string>* res = new vector<string>(); 
    if (slen == 0)  
    { return res; }  
    std::regex patt_regex(patt);
    auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);
    auto words_end = std::sregex_iterator();
    
    for (std::sregex_iterator i = words_begin; i != words_end; ++i)
    { std::smatch match = *i;
      std::string match_str = match.str();
      if (match_str.length() > 0)
      { res->push_back(match_str); }
    }
    return res;
  }


  static string replaceAll(string text, string patt, string rep)
  { std::regex patt_re(patt);
    std::string res = std::regex_replace(text, patt_re, rep);
    return res;
  }

  static string replace(string s1, string s2, string rep)
  { int s1len = s1.length(); 
    int s2len = s2.length(); 
	int replen = rep.length(); 
	if (s1len == 0 || s2len == 0 || replen == 0) 
	{ return s1; } 

	string result = ""; 
	int prev = 0; 

	int m1 = s1.find(s2); 
	if (m1 >= 0) 
	{ result = result + s1.substr(prev, m1 - prev) + rep; 
	  string remainder = s1.substr(m1 + s2len, s1len - (m1 + s2len)); 
	  return result + replace(remainder, s2, rep); 
	} 
	return s1; 
  } 

  static vector<string>* split(string s, string patt)
  { int slen = s.length();
    vector<string>* res = new vector<string>();
    if (slen == 0) 
    { res->push_back(s);
      return res; 
    } 
    std::regex patt_regex(patt);
    auto words_begin = std::sregex_iterator(s.begin(), s.end(), patt_regex);
    auto words_end = std::sregex_iterator();
    int prev = 0; 
    for (std::sregex_iterator i = words_begin; i != words_end; ++i)
    { std::smatch match = *i;
      int pos = match.position(0);
      int ln = match.length(0); 
      if (ln > 0)
      { string subst = s.substr(prev, pos - prev + 1);
        res->push_back(subst);
        prev = pos + ln; 
      } 
    }
    if (prev <= slen)
    { string lastst = s.substr(prev,slen - prev + 1);
      res->push_back(lastst);
    } 
    return res;
  }


  static bool includesAllMap(map<string,_T>* sup, map<string,_T>* sub) 
  { map<string,_T>::iterator iter; 
    for (iter = sub->begin(); iter != sub->end(); ++iter) 
    { string key = iter->first; 
      map<string,_T>::iterator f = sup->find(key); 
      if (f != sup->end())  
      { if (iter->second == f->second) {} 
        else 
        { return false; } 
      } 
      else  
      { return false; } 
    }     
    return true; 
  } 


  static bool excludesAllMap(map<string,_T>*  sup, map<string,_T>* sub) 
  { map<string,_T>::iterator iter; 
    for (iter = sub->begin(); iter != sub->end(); ++iter) 
    { string key = iter->first; 
      map<string,_T>::iterator f = sup->find(key); 
      if (f != sup->end())  
      { if (iter->second == f->second) 
        { return false; } 
      } 
    }     
    return true; 
  } 


   static map<string,_T>* includingMap(map<string,_T>* m, string src, _T trg) 
   { map<string,_T>* copy = new map<string,_T>(); 
     map<string,_T>::iterator iter; 
     for (iter = m->begin(); iter != m->end(); ++iter) 
     { string key = iter->first; 
       (*copy)[key] = iter->second; 
     }     
     (*copy)[src] = trg; 
     return copy; 
   } 


   static map<string,_T>* excludeAllMap(map<string,_T>* m1, map<string,_T>* m2) 
   { map<string,_T>* res = new map<string,_T>(); 
     map<string,_T>::iterator iter; 
     for (iter = m1->begin(); iter != m1->end(); ++iter) 
     { string key = iter->first; 
       map<string,_T>::iterator f = m2->find(key); 
       if (f != m2->end())  
       { if (iter->second == f->second)  {  } 
         else  
  	   { (*res)[key] = iter->second; } 
       } 
       else  
       { (*res)[key] = iter->second; } 
    }     
    return res; 
  }   


  static map<string,_T>* excludingMapKey(map<string,_T>* m, string k) 
  { // m - { k |-> m(k) }  
    map<string,_T>* res = new map<string,_T>(); 
    map<string,_T>::iterator iter; 
    for (iter = m->begin(); iter != m->end(); ++iter) 
    { string key = iter->first; 
      if (key == k) {} 
      else       
      { (*res)[key] = iter->second; } 
    }     
    return res; 
  } 


  static map<string,_T>* excludingMapValue(map<string,_T>* m, _T v) 
  { // m - { k |-> v }  
    map<string,_T>* res = new map<string,_T>(); 
    map<string,_T>::iterator iter; 
    for (iter = m->begin(); iter != m->end(); ++iter) 
    { string key = iter->first; 
      if (iter->second == v) {} 
      else       
      { (*res)[key] = iter->second; } 
    }     
    return res; 
  } 


  static map<string,_T>* unionMap(map<string,_T>* m1, map<string,_T>* m2)  
  { map<string,_T>* res = new map<string,_T>(); 
    map<string,_T>::iterator iter; 
    for (iter = m1->begin(); iter != m1->end(); ++iter) 
    { string key = iter->first; 
      if (m2->count(key) == 0) 
      { (*res)[key] = iter->second; } 
    }     
    for (iter = m2->begin(); iter != m2->end(); ++iter) 
    { string key = iter->first; 
      (*res)[key] = iter->second; 
    }     
    return res; 
  } 


  static map<string,_T>* intersectionMap(map<string,_T>* m1, map<string,_T>* m2) 
  { map<string,_T>* res = new map<string,_T>(); 
    map<string,_T>::iterator iter; 
    for (iter = m1->begin(); iter != m1->end(); ++iter) 
    { string key = iter->first; 
      if (m2->count(key) > 0) 
      { if (m2->at(key) == iter->second) 
        { (*res)[key] = iter->second; } 
      } 
    }     
    return res; 
  } 


  static set<string>* keys(map<string,_T>* s)
  { map<string,_T>::iterator iter;
    set<string>* res = new set<string>();
  
    for (iter = s->begin(); iter != s->end(); ++iter)
    { string key = iter->first;
      res->insert(key);
    }    
    return res;
  }


  static vector<_T>* values(map<string,_T>* s)
  { map<string,_T>::iterator iter;
    vector<_T>* res = new vector<_T>();
  
    for (iter = s->begin(); iter != s->end(); ++iter)
    { _T value = iter->second;
      res->push_back(value);
    }    
    return res;
  }


  static map<string,_T>* restrict(map<string,_T>* m1, set<string>* ks)
  { map<string,_T>* res = new map<string,_T>();
    map<string,_T>::iterator iter;
    for (iter = m1->begin(); iter != m1->end(); ++iter)
    { string key = iter->first;
      if (ks->find(key) != ks->end())
      { (*res)[key] = iter->second; }
    }    
    return res;
  }


};




