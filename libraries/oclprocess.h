#include <stdlib.h>
#include <process.h>


/* C header for OclProcess library */

struct Runnable {
 void (*runMethod)(void*); 
}; 

struct Runnable* newRunnable(void (*f)(void*))
{ struct Runnable* res = (struct Runnable*) malloc(sizeof(struct Runnable));
  res->runMethod = f; 
  return res; 
}

struct OclProcess {
 int pid;
 int threadid;  
 char* name;
 void (*actualThread)(void*);
 void* executes; 
 unsigned char active;  
 long period; 
 long delay; 
 long deadline; 
 int priority; 
};

void* getOclProcess_executes(struct OclProcess* self)
{ return self->executes; }

int getOclProcess_pid(struct OclProcess* self)
{ return self->pid; }

int getOclProcess_threadid(struct OclProcess* self)
{ return self->threadid; }

char* getOclProcess_name(struct OclProcess* self)
{ return self->name; }

unsigned char getOclProcess_active(struct OclProcess* self)
{ return self->active; }

long getOclProcess_period(struct OclProcess* self)
{ return (self->period); }

long getOclProcess_delay(struct OclProcess* self)
{ return (self->delay); }

long getOclProcess_deadline(struct OclProcess* self)
{ return (self->deadline); }

int getOclProcess_priority(struct OclProcess* self)
{ return self->priority; }

void setOclProcess_executes(struct OclProcess* self, void* executesx)
{ self->executes = executesx; }

void setOclProcess_pid(struct OclProcess* self, int pidx)
{ self->pid = pidx; }

void setOclProcess_threadid(struct OclProcess* self, int threadidx)
{ self->threadid = threadidx; }

void setOclProcess_name(struct OclProcess* self, char* namex)
{ self->name = namex; }

void setOclProcess_active(struct OclProcess* self, unsigned char activex)
{ self->active = activex; }

void setOclProcess_period(struct OclProcess* self, long periodx)
{ self->period = periodx; }

void setOclProcess_delay(struct OclProcess* self, long delayx)
{ self->delay = delayx; }

void setOclProcess_deadline(struct OclProcess* self, long deadlinex)
{ self->deadline = deadlinex; }

void setOclProcess_priority(struct OclProcess* self, int priorityx)
{ self->priority = priorityx; }

void displayOclProcess(struct OclProcess* proc)
{ printf("pid = %d, name = %s, active = %d\n", proc->pid, proc->name, proc->active); }
 
struct OclProcess* newOclProcess(void* obj, char* s)
{ struct OclProcess* res = (struct OclProcess*) malloc(sizeof(struct OclProcess));
  res->name = s; 
  res->executes = obj; /* could itself be an OclProcess */ 
  if (obj == NULL)
  { res->pid = system(s); 
    res->active = TRUE;
  }  /* Or: res->pid = spawnv(P_NOWAIT, s, NULL); */ 
  else 
  { res->actualThread = ((struct Runnable*) obj)->runMethod;
    res->active = FALSE;
  }
  res->deadline = 0LL; 
  res->delay = 0LL; 
  res->period = 0LL; 
  return res; 
}

struct OclProcess* createOclProcess(void)
{ struct OclProcess* res = (struct OclProcess*) malloc(sizeof(struct OclProcess));
  res->name = ""; 
  res->executes = NULL; /* could itself be an OclProcess */ 
  res->active = FALSE;
  res->deadline = 0LL; 
  res->delay = 0LL; 
  res->period = 0LL;
  res->priority = 5;  
  return res; 
}

void killOclProcess(struct OclProcess* obj)
{ free(obj); }

void setDeadline_OclProcess(struct OclProcess* self, long long d)
{ self->deadline = d/1000; } 

void setPeriod_OclProcess(struct OclProcess* self, long long p)
{ self->period = p/1000; } 

void setDelay_OclProcess(struct OclProcess* self, long long d)
{ self->delay = d/1000; } 

/*
long getDeadline_OclProcess(struct OclProcess* self)
{ return self->deadline; } 

long getPeriod_OclProcess(struct OclProcess* self)
{ return self->period; } 

long getDelay_OclProcess(struct OclProcess* self)
{ return self->delay; } */


void sleep_OclProcess(long long t)
{ sleep(t); }

char* getEnvironmentProperty(char* v)
{ return getenv(v); }

struct ocltnode* getEnvironmentProperties(void)
{ char* vars[] = 
    { "OS", "COMPUTERNAME", "PATH", "HOMEPATH", "NUMBER_OF_PROCESSORS",
      "PROCESSOR_ARCHITECTURE", "SYSTEMROOT", "USERNAME" }; 

  struct ocltnode* res = NULL;
  int i = 0; 
  for ( ; i < 8; i++)
  { char* val = getenv(vars[i]); 
    if (val != NULL)
    { res = insertIntoMap(res,vars[i],val); }
  }
  return res; 
} 

char* setEnvironmentProperty(char* key, char* val)
{ char* res = getenv(key); 
  char* data = strcat("setenv ", key); 
  char* cmd = strcat(data, " "); 
  system(strcat(cmd,val)); 
  return res; 
} 

char* clearEnvironmentProperty(char* key)
{ char* res = getenv(key); 
  char* data = strcat("clearenv ", key); 
  system(data); 
  return res; 
} 

struct OclProcess* currentThread(void)
{ int p = _getpid(); 
  struct OclProcess* res = (struct OclProcess*) malloc(sizeof(struct OclProcess));
  res->name = "main thread";
  res->pid = p; 
  return res; 
} 

struct OclProcess* getRuntime(void)
{ return currentThread(); }

struct OclProcess** allActiveThreads(void)
{ struct OclProcess** res = (struct OclProcess**) calloc(2, sizeof(struct OclProcess*));
  res[0] = currentThread(); 
  res[1] = NULL; 
  return res;
} 

int activeCount(void)
{ return 1; }  

int getPriority_OclProcess(struct OclProcess* self)
{ return self->priority; }  

int waitFor_OclProcess(struct OclProcess* self)
{ return 0; }  

char* getName_OclProcess(struct OclProcess* self)
{ return self->name; } 

long long getDeadline_OclProcess(struct OclProcess* self)
{ return (self->deadline)*1000; } 

void setName_OclProcess(struct OclProcess* self, char* nme)
{ self->name = nme; } 

void run_OclProcess(struct OclProcess* self)
{ if (self->actualThread != NULL)
  { beginthread(self->actualThread, 0, NULL); 
    self->active = TRUE; 
  } 
}

void start_OclProcess(struct OclProcess* self)
{ if (self->actualThread != NULL)
  { /* wait until deadline + delay */ 
    /* if period > 0 repeat at period ms intervals */

    long now = (long) time(NULL); 
    if (self->deadline > now)
    { sleep(1000*(self->deadline - now)); } 
    if (self->delay > 0)
    { sleep(1000*(self->delay)); } 
    if (self->period <= 0)
    { /* printf("Running thread "); */ 
      beginthread(self->actualThread, 0, NULL); 
      self->active = TRUE;
    } 
    else 
    { long next = now + self->period; 
      while (TRUE)
      { beginthread(self->actualThread, 0, NULL); 
        self->active = TRUE;
        now = time(NULL);
        next += self->period;  
        if (next > now)
        { sleep(1000*(next - now)); }
      } 
    } 
  } 
}

void exit_OclProcess(int n)
{ exit(n); } 

 
void interrupt_OclProcess(struct OclProcess* self)
{ self->active = FALSE; 
  /* endthread(); */ 
} 

void cancel_OclProcess(struct OclProcess* self)
{ self->active = FALSE; 
  /* endthread(); */ 
} 

void destroy_OclProcess(struct OclProcess* self)
{ self->active = FALSE; 
  /* endthread(); */  
} 

void join_OclProcess(struct OclProcess* self, double ms)
{ /* endthread(); */ } 

unsigned char isAlive_OclProcess(struct OclProcess* self)
{ return self->active; }  

unsigned char isDaemon_OclProcess(struct OclProcess* self)
{ return FALSE; }  
