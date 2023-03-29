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
	{ creator = f; }

    void setattributes(vector<OclAttribute*>* attributesxx)
    {
        attributes->clear();
        attributes->insert(attributes->end(), attributesxx->begin(), attributesxx->end());
    }

    void setattributes(int ind_x, OclAttribute* attributesxx)
    {
        if (ind_x >= 0 && ind_x < attributes->size()) 
        { (*attributes)[ind_x] = attributesxx; }
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
        { (*operations)[ind_x] = operationsxx; }
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
        { (*superclasses)[ind_x] = superclassesxx; }
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
    { if (c->oclname == oclname)
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
	{ if (creator == NULL)
	  { return NULL; }
	  return creator(); 
	} 
};


