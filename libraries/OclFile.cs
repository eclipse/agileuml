
    public class OclFile
    {
        private string name = ""; // internal
        private int port = 0; // internal
        private long position = 0; // internal
        private long markedPosition = 0;
        private bool eof = true; // internal
        private bool closed = true; 
        private string lastRead = ""; // internal

        private StreamReader inputStream = null;
        private StreamWriter outputStream = null;
        private Stream objectStream = null;

        private static Hashtable oclfilenameindex = new Hashtable(); // String --> OclFile

        public static OclFile System_in = OclFile.createOclFile("System.in");
        public static OclFile System_out = OclFile.createOclFile("System.out");
        public static OclFile System_err = OclFile.createOclFile("System.err");

        public static OclFile createOclFile(string namex)
        {
            if (oclfilenameindex[namex] != null)
            { return (OclFile)oclfilenameindex[namex]; }
            OclFile oclfilex = new OclFile();
            oclfilex.name = namex;
            oclfilex.port = 0;
            oclfilex.position = 0L;
            oclfilex.markedPosition = 0L;
            oclfilex.eof = true;
            oclfilex.closed = true;
            oclfilex.lastRead = "";
            oclfilenameindex[namex] = oclfilex;

            return oclfilex;
        }


        public OclFile()
        {
            name = "";
            port = 0;
            position = 0L;
            markedPosition = 0L;
            eof = false;
            closed = true; 
            lastRead = "";
        }

        public static OclFile getOclFileByPK(string namex)
        { return (OclFile)oclfilenameindex[namex]; }


        public override string ToString()
        {
            string _res_ = "(OclFile) ";
            _res_ = _res_ + name + ",";
            _res_ = _res_ + port + ",";
            _res_ = _res_ + position + ",";
            _res_ = _res_ + eof + ",";
            _res_ = _res_ + lastRead;
            return _res_;
        }

        public void setname(string name_x) { name = name_x; }

        public void setport(int port_x) { port = port_x; }

        public void setInputStream(Stream s)
        { inputStream = new StreamReader(s); }

        public void setOutputStream(Stream s)
        { outputStream = new StreamWriter(s); }


        public void setPosition(long position_x)
        {
            if (inputStream != null)
            {
                Stream bstream = inputStream.BaseStream;
                bstream.Seek(position_x, SeekOrigin.Begin);
                position = position_x;
            }
            else if (outputStream != null)
            {
                Stream bstream = outputStream.BaseStream;
                bstream.Seek(position_x, SeekOrigin.Begin);
                position = position_x;
            }
            else if (objectStream != null)
            {
                objectStream.Seek(position, SeekOrigin.Begin);
                position = position_x;
            }
        }

        public void seteof(bool eof_x) { eof = eof_x; }

        public void setlastRead(string lastRead_x) { lastRead = lastRead_x; }

        public string getname() { return name; }

        public int getport() { return port; }

        public long getposition() { return position; }

        public bool geteof() { return eof; }

        public string getlastRead() { return lastRead; }

        public bool isOpen() { return !closed; }

        public static OclFile newOclFile(string nme)
        {
            OclFile f = new OclFile();
            f.setname(nme);
            return f;
        }


        public static OclFile newOclFile_Remote(string nme, int portNumber)
        {
            OclFile f = new OclFile();
            f.setname(nme);
            f.setport(portNumber);
            return f;
        }


        public static OclFile newOclFile_Read(OclFile f)
        {
            f.closed = false;

            if (f.getname().Equals("System.in"))
            { return f; }

            if (f.inputStream == null && f.name != null) 
            { f.inputStream = File.OpenText(f.getname()); } 
            return f;
        }

        public static OclFile newOclFile_Write(OclFile f)
        {
            f.closed = false;

            if (f.getname().Equals("System.out") ||
                f.getname().Equals("System.err"))
            { return f; }
            
            if (f.outputStream == null && f.name != null)
            { f.outputStream = File.CreateText(f.getname()); } 
            return f;
        }


        public static OclFile newOclFile_ReadB(OclFile f)
        { // f.objectStream = File.Open(f.getname(), FileMode.Open); 
            OclFile.newOclFile_Read(f);
            f.closed = false;
            return f;
        }

        public static OclFile newOclFile_WriteB(OclFile f)
        { // f.objectStream = File.Create(f.getname());
            OclFile.newOclFile_Write(f);
            f.closed = false;
            return f;
        }

        public static OclFile createTemporaryFile(string nme, string ext)
        {
            OclFile f = OclFile.newOclFile(nme + "." + ext);
            return OclFile.newOclFile_Write(f);
        }


        public OclFile getInputStream()
        { return OclFile.newOclFile_Read(this); }

        public OclFile getOutputStream()
        { return OclFile.newOclFile_Write(this); }

        public void setPort(int portNumber)
        { port = portNumber; }

        public int compareTo(OclFile f)
        { return name.CompareTo(f.getname()); }


        public bool canRead()
        {
            if (isFile())
            { return true; }
            return false;
        }

        public bool canWrite()
        {
            bool result = false;
            if (isFile())
            {
                FileAttributes atts = File.GetAttributes(name);
                if (atts.HasFlag(FileAttributes.ReadOnly))
                { return false; }
                return true;
            }
            return result;
        }

        public void openRead()
        {
            closed = false;

            if (name.Equals("System.in"))
            { return; }

            if (inputStream == null && name != null)
            { inputStream = File.OpenText(getname()); }
        }

        public void openWrite()
        {
            closed = false;

            if (name.Equals("System.out") ||
                name.Equals("System.err"))
            { return; }

            if (outputStream == null && name != null)
            { outputStream = File.CreateText(getname()); }

        }

        public void openReadB()
        { openRead(); }

        public void openWriteB()
        { openWrite(); }

        public void closeFile()
        {
            closed = true;

            if (outputStream != null)
            { outputStream.Close(); }
            if (inputStream != null)
            { inputStream.Close(); }
            if (objectStream != null)
            { objectStream.Close(); }
            position = 0;
        }

        public bool exists()
        {
            if (File.Exists(name))
            { return true; }
            return Directory.Exists(name);
        }


        public bool isFile()
        {
            FileAttributes atts = File.GetAttributes(name);
            if (atts.HasFlag(FileAttributes.Directory))
            { return false; }
            return true;
        }


        public bool isDirectory()
        {
            FileAttributes atts = File.GetAttributes(name);
            if (atts.HasFlag(FileAttributes.Directory))
            { return true; }
            return false;
        }

        public bool isHidden()
        {
            bool result = false;
            FileAttributes atts = File.GetAttributes(name);
            if (atts.HasFlag(FileAttributes.Hidden))
            { return true; }
            return result;
        }

        public bool isAbsolute()
        {
            bool result = false;
            if (name.StartsWith("/"))
            { return true; }
            if (name.Length > 1 && name[1].Equals(":"))
            { return true; }
            return result;
        }


        public string getAbsolutePath()
        { return name; }


        public string getPath()
        {
            return name;
        }


        public string getParent()
        {
            int ind = name.LastIndexOf("\\");
            string result = ".";
            if (ind > 0)
            { result = name.Substring(0, ind); }
            return result;
        }


        public OclFile getParentFile()
        {
            OclFile result = OclFile.newOclFile(getParent());
            return result;
        }

        public long lastModified()
        {
            long result = 0;
            DateTime mdate = File.GetLastWriteTime(name);
            result = SystemTypes.getTime(mdate);
            return result;
        }


        public long length()
        {
            long result = new System.IO.FileInfo(name).Length;

            return result;
        }


        public bool delete()
        {
            try
            {
                File.Delete(name);
                position = 0;
                markedPosition = 0; 
                closed = true;

                return true;
            }
            catch (Exception e) { return false; }
        }

        public static bool deleteFile(string nme)
        {
            try
            {
                File.Delete(nme);
                return true;
            }
            catch (Exception e) { return false; }
        }


        public long getPosition()
        {
            // if (inputStream != null)
            // { position = ((FileStream)inputStream.BaseStream).Position; }
            // if (objectStream != null)
            // { position = objectStream.Position; }
            return position;
        }

        public string getName()
        {
            return name;
        }


        public string getInetAddress()
        {
            return name;
        }

        public string getLocalAddress()
        { return name; }

        public int getPort()
        { return port; }

        public int getLocalPort()
        {
            return port;
        }


        public ArrayList list()
        {
            ArrayList result = new ArrayList();
            if (isDirectory())
            {
                string[] fnames = Directory.GetFiles(name);
                for (int i = 0; i < fnames.Length; i++)
                { result.Add(fnames[i]); }
                return result;
            }
            return result;
        }


        public ArrayList listFiles()
        {
            ArrayList result = new ArrayList();
            if (isDirectory())
            {
                string[] fnames = Directory.GetFiles(name);
                for (int i = 0; i < fnames.Length; i++)
                { result.Add(OclFile.newOclFile(fnames[i])); }
                return result;
            }
            return result;
        }


        public void print(string s)
        {
            if ("System.out".Equals(name) ||
                "System.err".Equals(name))
            { Console.Write(s); }
            else
            { write(s); }
        }

        public void println(string s)
        {
            if ("System.out".Equals(name) ||
                "System.err".Equals(name))
            { Console.WriteLine(s); }
            else
            { writeln(s); }
        }

        public void writeAllLines(ArrayList sq)
        {
            if ("System.out".Equals(name) ||
                "System.err".Equals(name))
            { for (int i = 0; i < sq.Count; i++) 
              { Console.WriteLine(sq[i] + ""); }
            } 
            else
            { for (int i = 0; i < sq.Count; i++) 
              { writeln(sq[i] + ""); }
            }
        }

        public void printf(string f, ArrayList sq)
        {
            object[] args = new object[sq.Count];
            for (int i = 0; i < sq.Count; i++)
            { args[i] = sq[i]; }
            string fmt = OclFile.convertConversionFormat(f);
            string formattedString = String.Format(fmt, args);
            print(formattedString);
        }

        public void write(string s)
        {
            if (outputStream != null)
            { outputStream.Write(s); }
        }


        public void writeN(ArrayList sq, int n)
        {
            if (outputStream != null)
            {
                for (int i = 0; i < sq.Count && i < n; i++)
                {
                    string s = (string)sq[i];
                    outputStream.Write(s);
                }
            }

        }

        public void writeByte(int x)
        {
            if (outputStream != null)
            { outputStream.Write(x); }
        }

        public void writeNbytes(ArrayList sq, int n)
        {
            if (outputStream != null)
            {
                for (int i = 0; i < sq.Count && i < n; i++)
                {
                    int x = (int)sq[i];
                    outputStream.Write(x);
                }
            }

        }

        public void writeAllBytes(ArrayList sq)
        {
            if (outputStream != null)
            {
                for (int i = 0; i < sq.Count; i++)
                {
                    int x = (int)sq[i];
                    outputStream.Write(x);
                }
            }

        }

        public void writeObject<T>(T s)
        {
            if (outputStream != null)
            {
                string jsonString =
                  JsonSerializer.Serialize<T>(s);
                outputStream.WriteLine(jsonString);
            }
        }

        public void writeln(string s)
        {
            if (outputStream != null)
            { outputStream.WriteLine(s); }
        }

        public void flush()
        {
            if (outputStream != null)
            { outputStream.Flush(); }
        }

       public bool hasNext()
        {
            bool result = false;
            if ("System.in".Equals(name))
            {
                lastRead = Console.ReadLine();
                if (lastRead != null)
                { position = position + lastRead.Length; 
                  return true; 
                }
                eof = true;
                return result;
            }
            else if (inputStream != null &&
                     inputStream.Peek() > -1)
            {
                int c = inputStream.Read();
                while (c != -1 && Char.IsWhiteSpace((char)c))
                {
                    position++;
                    c = inputStream.Read();
                } 

                if (c == -1) 
                { eof = true; return false; }
                
                lastRead = "";
                while (c != -1 && !Char.IsWhiteSpace((char) c))
                { lastRead = lastRead + SystemTypes.byte2char(c);
                  position++;
                  c = inputStream.Read(); 
                }

                if (c == -1) 
                { eof = true; return true; }

                return true; 
            }
            else
            { eof = true; }
            return result;
        }

        public string getCurrent()
        { return lastRead; }

        public string read()
        {
            if (inputStream != null)
            {
                int x = inputStream.Read();
                string result = SystemTypes.byte2char(x);
                if (x == -1)
                { eof = true; }
                position++;
                return result;
            }
            return "";
        }

        public ArrayList readN(int n)
        {
            ArrayList result = new ArrayList();
            if (inputStream != null)
            {
                int ind = 0;
                int x = inputStream.Read();
                while (ind < n && x != -1)
                {
                    result.Add(SystemTypes.byte2char(x));
                    position++;
                    x = inputStream.Read();
                    ind++;
                }
                if (x == -1)
                { eof = true; }
                return result;
            }
            return result;
        }



        public int readByte()
        {
            if (inputStream != null)
            {
                int x = inputStream.Read();
                position++;
                if (x == -1)
                { eof = true; }
                return x;
            }
            return -1;
        }

        public ArrayList readNbytes(int n)
        {
            ArrayList result = new ArrayList();
            if (inputStream != null)
            {
                int ind = 0;
                int x = inputStream.Read();
                while (ind < n && x != -1)
                {
                    result.Add(x);
                    position++;
                    x = inputStream.Read();
                    ind++;
                }
                if (x == -1)
                { eof = true; }
                return result;
            }
            return result;
        }

        public ArrayList readAllBytes()
        {
            ArrayList result = new ArrayList();
            if (inputStream != null)
            {
                int x = inputStream.Read();
                while (x != -1)
                {
                    result.Add(x);
                    position++;
                    x = inputStream.Read();
                }
                if (x == -1)
                { eof = true; }
                return result;
            }
            return result;
        }

        public static void copyFromTo(OclFile source, OclFile target)
        {   if (source == null || target == null)
            { return; }

            if (source.inputStream != null)
            {
                int x = source.inputStream.Read();
                while (x != -1)
                {
                    target.writeByte(x);
                    source.position++;
                    x = source.inputStream.Read();
                }

                if (x == -1)
                { source.eof = true; }
                
            }
        }

        public T readObject<T>()
        {
            if (inputStream != null)
            {
                string jsonString = inputStream.ReadLine();
                if (jsonString != null)
                {
                    position = position + jsonString.Length;
                    T res =
                      JsonSerializer.Deserialize<T>(jsonString);
                    return res;
                }
            }
            return default(T);
        }

        public string readLine(string prompt)
        { Console.WriteLine(prompt); 
          return readLine(); 
        } 

        public string readLine()
        {
            string result = "";
            if (inputStream != null)
            {
                result = inputStream.ReadLine();
                if (result == null)
                { eof = true;
                  return result; 
                }
                position = position + result.Length;
                return result;
            }
            else if ("System.in".Equals(name))
            {
                lastRead = Console.ReadLine();

                if (lastRead == null)
                {
                    eof = true;
                    return null;

                }
                position = position + lastRead.Length;
                result = lastRead;
                return result;
            }
            return result;
        }

        public ArrayList readAllLines()
        {
            ArrayList res = new ArrayList();
            if (inputStream != null || "System.in".Equals(name))
            {
                String lne = readLine();
                while (lne != null)
                {
                    res.Add(lne);
                    lne = readLine();
                }
                return res;
            }

            return res; 
        }

        public string readAll()
        {
            string result = "";
            if (inputStream != null)
            {
                result = inputStream.ReadToEnd();
                position = position + result.Length;
            }

            return result;
        }


        public void mark()
        {
            if (inputStream != null)
            { markedPosition = ((FileStream)inputStream.BaseStream).Position; }
        }

        public void reset()
        {
            if (inputStream != null)
            {
                ((FileStream)inputStream.BaseStream).Seek(markedPosition, SeekOrigin.Begin);
                position = markedPosition;
            }
        }

        public void skipBytes(int n)
        {
            if (inputStream != null)
            {
                for (int i = 0; i < n; i++)
                {
                    int x = inputStream.Read();
                    if (x < 0)
                    {
                        eof = true;
                        return;
                    }
                    position++;
                }
            }
        }

        public bool mkdir()
        {
            bool result = false;
            DirectoryInfo inf = 
               Directory.CreateDirectory(name);
            if (inf != null)
            { return true; }
            return result;
        }

        public bool mkdirs()
        { return mkdir(); } 

        public static string convertConversionFormat(string cstyle)
        {
            String res = cstyle;
            int argindex = 0;
            int pind = res.IndexOf("%");
            Regex dpatt = new Regex("%d");
            Regex epatt = new Regex("%e");
            Regex fpatt = new Regex("%f");
            Regex spatt = new Regex("%s");
            Regex xpatt = new Regex("%x");
            Regex gpatt = new Regex("%g");


            while (pind >= 0)
            {
                if (pind + 1 < res.Length && "s".Equals(res.Substring(pind + 1, 1)))
                { res = spatt.Replace(res, "{" + argindex + ":S}", 1); }
                else if (pind + 1 < res.Length && "d".Equals(res.Substring(pind + 1, 1)))
                { res = dpatt.Replace(res, "{" + argindex + ":D}", 1); }
                else if (pind + 1 < res.Length && "f".Equals(res.Substring(pind + 1, 1)))
                { res = fpatt.Replace(res, "{" + argindex + ":F}", 1); }
                else if (pind + 1 < res.Length && "e".Equals(res.Substring(pind + 1, 1)))
                { res = epatt.Replace(res, "{" + argindex + ":E}", 1); }
                else if (pind + 1 < res.Length && "g".Equals(res.Substring(pind + 1, 1)))
                { res = gpatt.Replace(res, "{" + argindex + ":G}", 1); }
                else if (pind + 1 < res.Length && "x".Equals(res.Substring(pind + 1, 1)))
                { res = xpatt.Replace(res, "{" + argindex + ":X}", 1); }


                argindex++;
                pind = res.IndexOf("%");
            }
            return res;
        }

    }


