OclProcess* OclProcess::newOclProcess(Runnable* obj, string nme)
{
 
    OclProcess* res = new OclProcess(); 
    res->name = nme; 
    res->executes = obj;
    res->alive = false;
    res->daemon = false; 
    return res; 
}

OclProcess* OclProcess::getRuntime()
{
    OclProcess* res = new OclProcess();
    res->name = "Runtime";
    res->executes = NULL;
    res->alive = false;
    res->daemon = false;
    return res;
}

void OclProcess::notify(condition_variable* obj)
{
    obj->notify_one();
}

void OclProcess::notifyAll(condition_variable* obj)
{
    obj->notify_all();
}

void OclProcess::wait(condition_variable* obj, double t)
{
    sleep((long) t);
}

void OclProcess::sleep(long n)
{
    std::this_thread::sleep_for(std::chrono::milliseconds(n)); 
}

string OclProcess::getName()
{
    return name; 
}

void OclProcess::setName(string nme)
{
    name = nme;
}

void OclProcess::start()
{ if (executes != NULL)
  {
    std::function<void()> f = [=](void) { return executes->run(); };

    actual_thread = std::thread(f);
    alive = true; 
    daemon = true; 
    actual_thread.detach(); 
  }
  else // system process
  {
    alive = true;
    daemon = true; 
    system(name.c_str());
  }
}

void OclProcess::run()
{
    if (executes != NULL)
    {
        std::function<void()> f = [=](void) { return executes->run(); };
        actual_thread = std::thread(f);
        alive = true;
        daemon = false; 
        actual_thread.join();
    }
    else // system process
    {
        alive = true;
        daemon = true;
        system(name.c_str());
    }
}

void OclProcess::waitFor()
{
    OclProcess::run();
}

void OclProcess::join()
{
    if (actual_thread.joinable())
    {
        alive = true;
        daemon = false;
        actual_thread.join();
    }
}

void OclProcess::destroy()
{
        alive = false;
        daemon = false;
        actual_thread = std::thread();
}

void OclProcess::cancel()
{
    alive = false;
    daemon = false;
    actual_thread = std::thread();
}

void OclProcess::interrupt()
{
    std::this_thread::sleep_for(std::chrono::milliseconds(100));
}

OclProcess* OclProcess::currentThread()
{
    OclProcess* res = new OclProcess;
    res->name = "Main thread"; 
    return res; 
}

bool OclProcess::isAlive()
{
    return alive; 
}

bool OclProcess::isDaemon()
{
    return daemon;
}

void OclProcess::exit(int n)
{
    std::exit(n);
}

string OclProcess::getEnvironmentProperty(string v)
{
    char* res = getenv(v.c_str()); 
    if (res == NULL)
    {
        return "";
    }
    return string(res); 
}

string OclProcess::setEnvironmentProperty(string v, string val)
{
    char* res = getenv(v.c_str()); 

    setenv(v.c_str(), val.c_str(), 1); 

    if (res == NULL)
    {
        return "";
    }
    return string(res); 
}

string OclProcess::clearEnvironmentProperty(string v)
{
    char* res = getenv(v.c_str()); 

    unsetenv(v.c_str()); 

    if (res == NULL)
    {
        return "";
    }
    return string(res); 
}

map<string,string>* OclProcess::getEnvironmentProperties()
{  map<string,string>* res = new map<string,string>(); 
    char* os = getenv("OS"); 
    if (os != NULL)
    {
       (*res)["OS"] = string(os);
    }
	char* cwd = getenv("COMPUTERNAME"); 
    if (cwd != NULL)
    {
       (*res)["COMPUTERNAME"] = string(cwd);
    }
    char* path = getenv("PATH"); 
    if (path != NULL)
    {
       (*res)["PATH"] = string(path);
    }
    char* hpath = getenv("HOMEPATH");
    if (hpath != NULL)
    {
        (*res)["HOMEPATH"] = string(hpath);
    }
    char* nproc = getenv("NUMBER_OF_PROCESSORS");
    if (nproc != NULL)
    {
        (*res)["NUMBER_OF_PROCESSORS"] = string(nproc);
    }
    char* procarch = getenv("PROCESSOR_ARCHITECTURE");
    if (procarch != NULL)
    {
        (*res)["PROCESSOR_ARCHITECTURE"] = string(procarch);
    }
    char* sysroot = getenv("SYSTEMROOT");
    if (sysroot != NULL)
    {
        (*res)["SYSTEMROOT"] = string(sysroot);
    }
    char* user = getenv("USERNAME");
    if (user != NULL)
    {
        (*res)["USERNAME"] = string(user);
    }


    return res; 
}

OclProcess::~OclProcess()
{
    delete executes; 
}

