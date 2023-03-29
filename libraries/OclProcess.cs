
    interface Runnable
    { public void run(); }


    class OclProcess : Runnable
    {
        string name = "";
        int priority = 5;
        Runnable executes = null;
        Thread actualThread = null;
        Process osProcess = null;

        long deadline = 0;
        long delay = 0;
        long period = 0; 

        public static OclProcess newOclProcess(Runnable obj, string s)
        {
            OclProcess p = new OclProcess();
            p.executes = obj; 
            p.name = s;
            if (obj != null)
            {
                ThreadStart st = new ThreadStart(obj.run);
                p.actualThread = new Thread(st);
            }
            else
            {
                Process proc = new Process();
                // proc.StartInfo.UseShellExecute = false;
                proc.StartInfo.FileName = s;
                // proc.StartInfo.CreateNoWindow = true;
                p.osProcess = proc;
            }
            return p;
        }

        public void setDeadline(long d)
        { deadline = d; }

        public void setDelay(long d)
        { delay = d; }

        public void setPeriod(long p)
        { period = p; }

        public long getDeadline()
        { return deadline; }

        public static void notify(object obj)
        { Monitor.Pulse(obj); }

        public static void notifyAll(object obj)
        { Monitor.PulseAll(obj); }

        public static void wait(object obj, int n)
        {
            Monitor.Wait(obj, n);
            // var pause = Task.Delay(n);
            // pause.Wait(); 

            // int tot = (int) ((m * 1000000 + n) / 1000000.0);
            // var pause = Task.Delay(tot);
            // pause.Wait();
        }

        public int waitFor()
        {
            if (osProcess != null)
            {
                osProcess.WaitForExit();
                return 0;
            }
            return 0;
        }

        public static OclProcess currentThread()
        {
            Thread t = Thread.CurrentThread;
            OclProcess pr = new OclProcess();
            pr.name = "Current thread";
            pr.actualThread = t;
            return pr;
        }


        public static OclProcess getRuntime()
        { return OclProcess.currentThread(); }

        public static ArrayList allActiveThreads()
        { return new ArrayList(); }

        public static int activeCount()
        { return ThreadPool.ThreadCount; }

        public static void sleep(long n)
        { Thread.Sleep((int)n); }

        public string getName()
        { return name; }

        public void setName(string nme)
        { name = nme; }

        public int getPriority()
        {
            if (actualThread != null)
            { return (int)actualThread.Priority; }
            return priority;
        }

        public bool isAlive()
        {
            if (actualThread == null)
            { return false; }
            return actualThread.IsAlive;
        }

        public bool isDaemon()
        {
            if (actualThread == null)
            { return false; }
            return actualThread.IsBackground;
        }

        public void run()
        { if (executes is OclProcess)
          {
            OclProcess proc = (OclProcess)executes;
            if (proc.delay > 0)
            { Thread.Sleep((int)proc.delay); }
                if (proc.period > 0)
                {
                    long now = SystemTypes.getTime();
                    long next = now + proc.period;
                    while (true)
                    { proc.run();
                      now = SystemTypes.getTime();
                      next = next + proc.period;
                      if (next > now)
                      { Thread.Sleep((int)(next - now)); }
                      ThreadStart st = new ThreadStart(proc.executes.run);
                      proc.actualThread = new Thread(st);
                    }
                }
                else
                { proc.run(); }

            return; 
          }

            if (actualThread != null)
            { actualThread.Start(); }
            if (osProcess != null)
            { osProcess.Start(); }
        }

        public void start()
        {
            if (actualThread != null)
            { actualThread.Start(); }
            if (osProcess != null)
            { osProcess.Start(); }
        }

        public void join(double ms)
        {
            if (actualThread != null)
            { actualThread.Join((int)ms); }
        }

        public void interrupt()
        {
            if (actualThread != null)
            { actualThread.Interrupt(); }
        }

        public void cancel()
        { destroy(); }

        public void destroy()
        {
            if (actualThread != null)
            {
                actualThread.Abort();
                actualThread = null;
            }

            if (osProcess != null)
            {
                osProcess.Kill();
                osProcess = null;
            }
        }

        public static string getEnvironmentProperty(string var)
        { return System.Environment.GetEnvironmentVariable(var); }

        public static Hashtable getEnvironmentProperties()
        { IDictionary dict = System.Environment.GetEnvironmentVariables(); 
          Hashtable res = new Hashtable(); 
          foreach (object x in dict.Keys)
          { res[x] = dict[x]; }
          return res;
        }

        public static string setEnvironmentProperty(string var, string val)
        { string prop = 
              System.Environment.GetEnvironmentVariable(var);  
          System.Environment.SetEnvironmentVariable(var,val); 
          return prop; 
        }

        public static string clearEnvironmentProperty(string var)
        { string prop = 
              System.Environment.GetEnvironmentVariable(var);  
          System.Environment.SetEnvironmentVariable(var,null); 
          return prop; 
        }

        public static void exit(int n)
        { System.Environment.Exit(n); }
    }
