// class Runnable
// { public: 
//     virtual void run() { };
// };


class OclProcess : public Runnable
{ private: 
    string name; 
    Runnable* executes;
    std::thread actual_thread;
    bool alive; 
    bool daemon;
    long delay; 
    long deadline; 
    long period; 
public:
    static OclProcess* newOclProcess(Runnable* obj, string nme);
    static void sleep(long n); 
    static OclProcess* currentThread(); 
    static void exit(int n); 
    static string getEnvironmentProperty(string v); 
    static string setEnvironmentProperty(string v, string val); 
    static string clearEnvironmentProperty(string v); 
    static map<string,string>* getEnvironmentProperties(); 
    static OclProcess* getRuntime(); 
    static void notify(condition_variable* obj); 
    static void notifyAll(condition_variable* obj); 
    static void wait(condition_variable* obj, double t); 
    static int activeCount() { return 1; }
    string getName();
    void setName(string nme); 
    void start(); 
    virtual void run();
    bool isAlive(); 
    bool isDaemon();
    void join(); 
    void waitFor(); 
    void destroy();
    void cancel(); 
    void interrupt(); 
    void setDeadline(long d) { deadline = d; }
    long getDeadline() { return deadline; }
    void setDelay(long d) { delay = d; }
    void setPeriod(long p) { period = p; }
    ~OclProcess(); 
};

