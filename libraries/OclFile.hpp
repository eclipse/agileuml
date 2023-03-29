
char* concatenateStrings(char* st1, char* st2)
{
    if (st1 == NULL)
    {
        return st2;
    }
    if (st2 == NULL)
    {
        return st1;
    }

    int n = strlen(st1);
    int m = strlen(st2);
    char* result = (char*)calloc(n + m + 1, sizeof(char));
    strcat(strcat(result, st1), st2);
    result[n + m] = '\0';
    return result;
}


vector<void*>* addCSequence(vector<void*>* sq, string x)
{
    int n = x.length(); 
    char* buff = (char*) calloc(n+1, sizeof(char)); 
    for (int i = 0; i < n; i++)
    {
        buff[i] = x[i];
    }
    buff[n] = '\0'; 

    sq->push_back(buff);
    return sq;
}

vector<void*>* addCSequence(vector<void*>* sq, int x)
{
    int* elem = (int*) malloc(sizeof(int));
    *elem = x;
    sq->push_back((void*) elem);
    return sq;
}

vector<void*>* addCSequence(vector<void*>* sq, long x)
{
    long* elem = (long*) malloc(sizeof(long));
    *elem = x;
    sq->push_back((void*) elem);
    return sq;
}

vector<void*>* addCSequence(vector<void*>* sq, double x)
{
    double* elem = (double*) malloc(sizeof(double));
    *elem = x;
    sq->push_back((void*) elem);
    return sq;
}

vector<void*>* addCSequence(vector<void*>* sq, bool x)
{
    bool* elem = (bool*) malloc(sizeof(bool));
    *elem = x;
    sq->push_back((void*) elem);
    return sq;
}

vector<void*>* addCSequence(vector<void*>* sq, void* x)
{
    sq->push_back(x);
    return sq;
}


class OclFile
{
private:
    string name;
    long position; 
    long markedPosition; 
    ofstream outFileStream; 
    ifstream inFileStream; 
    bool readable; 
    bool writable;
    int port; 
    static map<string, OclFile*>* oclfilenameindex;
    string lastRead;
    bool isRemote; 
    SOCKET socket; 

public:
    static OclFile* newOclFile(string nme); 
    static OclFile* newOclFile_Write(OclFile* f); 
    static OclFile* newOclFile_Read(OclFile* f);
    static OclFile* newOclFile_WriteB(OclFile* f);
    static OclFile* newOclFile_ReadB(OclFile* f);
    static OclFile* newOclFile_Remote(string url, SOCKET skt); 
    static OclFile* getOclFileByPK(string namex);
    static void copyFromTo(OclFile* source, OclFile* target);
    static OclFile* createTemporaryFile(string prefx, string extensn);

    OclFile* getInputStream(); 
    OclFile* getOutputStream();
    string getName();
    virtual string toString(); 
    string getPath(); 
    string getInetAddress();
    string getLocalAddress(); 
    int getPort(); 
    int getLocalPort(); 
    void setPort(int p); 
    int compareTo(OclFile* f); 
    bool canRead(); 
    bool canWrite(); 
    void openRead(); 
    void openWrite(); 
    void openReadB(); 
    void openWriteB();
    void writeln(string s); 
    void println(string s);
    void write(string s); 
    void print(string s);
    void printf(string f, vector<void*>* sq); 

    template<class E>
    void writeObject(E* obj); 

    string readLine(); 
    string readAll(); 
    string read();
    int readByte(); 
    vector<string>* readN(int n); 
    vector<string>* readAllLines(); 
    void writeN(vector<string>* arr, int n); 
    vector<int>* readNbytes(int n); 
    vector<int>* readAllBytes(); 
    void writeByte(int c); 
    void writeNbytes(vector<int>* arr, int n); 
    void writeAllBytes(vector<int>* arr); 
    void skipBytes(int n);
    void mark(); 
    void reset(); 
    void flush();
    void closeFile(); 
    bool isOpen(); 
    bool isFile(); 
    bool isDirectory(); 
    bool exists(); 
    bool isHidden(); 
    bool isAbsolute();
    long lastModified(); 
    long length(); 
    bool deleteFile();
    static bool deleteFile(string nme);

    bool mkdir();
    vector<string>* list(); 
    vector<OclFile*>* listFiles(); 
    string getParent(); 
    OclFile* getParentFile(); 
    bool hasNext(); 
    string getCurrent();
    long getPosition(); 
    bool getEof(); 
};


