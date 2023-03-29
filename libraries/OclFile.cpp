map<string, OclFile*>* OclFile::oclfilenameindex = new map<string, OclFile*>();

OclFile* OclFile::newOclFile(string nme)
{
    OclFile* res = new OclFile(); 
    res->name = nme;
    (*oclfilenameindex)[nme] = res;
    res->readable = false; 
    res->writable = false; 
    res->position = 0;
    res->markedPosition = 0; 
    res->lastRead = "";
    res->isRemote = FALSE; 
    res->socket = NULL; 
    return res;
}

OclFile* OclFile::newOclFile_Write(OclFile* f)
{
    f->openWrite(); 
    return f;
}

OclFile* OclFile::newOclFile_Read(OclFile* f)
{
    f->openRead();
    return f;
}

OclFile* OclFile::newOclFile_WriteB(OclFile* f)
{
    f->openWriteB();
    return f;
}

OclFile* OclFile::newOclFile_ReadB(OclFile* f)
{
    f->openReadB();
    return f;
}

OclFile* OclFile::newOclFile_Remote(string nme, SOCKET sess)
{ struct OclFile* res = newOclFile(nme);
  res->isRemote = TRUE; 
  res->socket = sess; 
  return res; 
} 


OclFile* OclFile::createTemporaryFile(string nme, string ext)
{ string cname = nme + "." + ext; 
  OclFile* f = OclFile::newOclFile(cname); 
  return OclFile::newOclFile_Write(f); 
} 

OclFile* OclFile::getOclFileByPK(string nme)
{
    OclFile* res = oclfilenameindex->at(nme);
    return res; 
}


OclFile* OclFile::getInputStream()
{
    return OclFile::newOclFile_Read(this);
}

OclFile* OclFile::getOutputStream()
{
    return OclFile::newOclFile_Write(this);
}

string OclFile::getName()
{
    return name; 
}

string OclFile::toString()
{
    return "(OclFile) " + name;
}

string OclFile::getPath()
{
    return name;
}

string OclFile::getInetAddress()
{
    return name; 
}

string OclFile::getLocalAddress()
{
    return name;
}

int OclFile::getPort()
{
    return port;
}

int OclFile::getLocalPort()
{
    return port;
}

void OclFile::setPort(int p)
{
    port = p; 
}

int OclFile::compareTo(OclFile* f)
{ if (name < f->name) { return -1; }
  else if (name > f->name) { return 1; }
  return 0; 
}

bool OclFile::canRead()
{
    return readable; 
}

bool OclFile::canWrite()
{
    return writable;
}

void OclFile::openWrite()
{  if (isRemote) 
   { writable = TRUE; 
     return; 
   } 

   outFileStream = ofstream(name);
   if (!outFileStream) 
   {
      writable = false; 
   }
   else
   {
      writable = true;
   }
}

void OclFile::openRead()
{ if (isRemote) 
  { readable = TRUE; 
    return; 
  } 
    inFileStream = ifstream(name);
    if (!inFileStream) 
    {
        readable = false; 
    }
    else
    {
        readable = true;
    }
}

void OclFile::openWriteB()
{ if (isRemote) 
   { writable = TRUE; 
     return; 
   } 

    outFileStream = ofstream(name, ios::binary);
    if (!outFileStream) 
    {
        writable = false; 
    }
    else
    {
        writable = true;
    }
}

void OclFile::openReadB()
{ if (isRemote) 
  { readable = TRUE; 
    return; 
  } 
  
    inFileStream = ifstream(name, ios::binary);
    if (!inFileStream) 
    { readable = false; }
    else
    {
        readable = true;
    }
}

void OclFile::closeFile()
{ if (isRemote) 
  { readable = FALSE;
    writable = FALSE;
	if (socket != NULL)
	{ closesocket(socket); }  
    return; 
  } 
  
  if (readable)
  {
    inFileStream.close();
	readable = false; 
  }

  if (writable)
  {
    outFileStream.close();
	writable = false; 
  }
}

bool OclFile::isOpen()
{ return readable || writable; } 


void OclFile::flush()
{ if (isRemote) { return; } 

  if (writable)
  {
    outFileStream.flush();
  }
}

bool OclFile::isFile()
{ if (isRemote) { return false; } 

    struct stat stbuf;
    
    if (stat(name.c_str(), &stbuf) != 0)
    {
        return false;
    }

    if ((S_IFREG & stbuf.st_mode) > 0)
    {
        return true;
    }
    return false; 
}

bool OclFile::isDirectory()
{ if (isRemote) { return false; } 
   
  struct stat stbuf;

    if (stat(name.c_str(), &stbuf) != 0)
    {
        return false;
    }

    if ((S_IFREG & stbuf.st_mode) > 0)
    {
        return false;
    }
    else if ((S_IFDIR & stbuf.st_mode) > 0)
    {
        return true;
    }
    return false;
}

bool OclFile::exists()
{ if (isRemote) { return false; } 

    struct stat stbuf;

    if (stat(name.c_str(), &stbuf) != 0)
    {
        return false;
    }
    return true;
}

long OclFile::lastModified()
{ if (isRemote) { return 0; } 

    struct stat stbuf;

    if (stat(name.c_str(), &stbuf) == 0)
    {
        return stbuf.st_mtime;
    }
    return 0;
}

bool OclFile::isHidden()
{ if (isRemote) { return false; } 

  if (name.length() == 0)
  {
    return false;
  }

  if ('.' == name[0])
  {
      return true;
  }

  return false; 
}

bool OclFile::isAbsolute()
{ if (isRemote) { return false; } 

    if (name.length() == 0)
    {
        return false;
    }
    if ('\\' == name[0])
    {
        return true;
    }
    if (name.length() > 1 && ':' == name[1])
    {
        return true;
    }
    return false;
}

string OclFile::getParent()
{ if (isRemote) { return string(""); } 

    char* buff;
    buff = _getcwd(NULL, 0);
    if (buff == 0)
    {
        return string("");
    }
    return string(buff); 
}

OclFile* OclFile::getParentFile()
{ if (isRemote) { return this; } 

    char* buff;
    buff = _getcwd(NULL, 0);
    if (buff == 0)
    {
      return NULL;
    }
    OclFile* res = OclFile::newOclFile(string(buff));
    return res; 
}

long OclFile::length()
{ if (isRemote) { return 0; } 

    struct stat stbuf;

    if (stat(name.c_str(), &stbuf) != 0)
    {
        return 0;
    }

    return stbuf.st_size; 
}

bool OclFile::deleteFile()
{ readable = false; 
  writable = false; 

    if (remove(name.c_str()) != 0)
    {
        return false;
    }
    else
    {
        return true;
    }
}

bool OclFile::deleteFile(string nme)
{
    if (remove(nme.c_str()) != 0)
    {
        return false;
    }
    else
    {
        return true;
    }
}

vector<string>* OclFile::list()
{
    vector<string>* res = new vector<string>();

    char* buff;
    buff = _getcwd(NULL, 0);
    if (buff == 0)
    {
        return res;
    }
    
    char* completePath = concatenateStrings(buff, (char*) "\\*.*");
    WIN32_FIND_DATAA data;
    HANDLE h;
    
    h = FindFirstFileA(completePath, &data);
    
    if (h != INVALID_HANDLE_VALUE)
    {
       
        res->push_back(string(data.cFileName));
        while (FindNextFileA(h, &data))
        {
            res->push_back(string(data.cFileName));
        }
        FindClose(h);
    }
    return res;
} 

vector<OclFile*>* OclFile::listFiles()
{
    vector<OclFile*>* res = new vector<OclFile*>(); 
    vector<string>* fnames = list(); 
    for (int i = 0; i < fnames->size(); i++)
    {
        string fname = fnames->at(i); 
        OclFile* f = OclFile::newOclFile(fname); 
        res->push_back(f); 
    }
    return res; 
}

long OclFile::getPosition()
{ if (isRemote) { return 0; } 
  if ("System.out" == name || "System.in" == name || "System.err" == name)
  {
    position = 0;
  }
  else if (writable)
  {
    position = outFileStream.tellp();
  }
  else if (readable)
  { position = inFileStream.tellg(); }
  return position;
}

bool OclFile::getEof()
{ if ("System.in" == name) 
  { return cin.eof(); }

  if ("System.out" == name) 
  { return cout.eof(); }

  if ("System.err" == name) 
  { return cerr.eof(); }

  if (isRemote) { return false; } 

  if (writable) 
  { return outFileStream.eof(); } 

  if (readable) 
  { return inFileStream.eof(); } 

  return true; 
}

void OclFile::writeln(string s)
{
  if (isRemote)
  { send(socket, (s + "\n").c_str(), s.length() + 1, 0); 
    return; 
  }
  else if ("System.out" == name)
  {
     cout << s << endl;
  }
  else if ("System.err" == name)
  {
    cerr << s << endl;
  }
  else if (writable)
    {
        outFileStream << s << endl;
    }
}

void OclFile::println(string s)
{
    OclFile::writeln(s);
}

void OclFile::write(string s)
{ if (isRemote)
  { send(socket, s.c_str(), s.length(), 0); 
    return; 
  }

    if ("System.out" == name)
    {
        cout << s;
    }
    else if ("System.err" == name)
    {
        cerr << s;
    }
    else if (writable)
    {
        outFileStream << s;
    }
}

void OclFile::writeN(vector<string>* sq, int n)
{ 
  for (int i = 0; i < sq->size() && i < n; i++)
  { string s = sq->at(i);  
    if (isRemote)
    { send(socket, s.c_str(), 1, 0); }
    else if ("System.out" == name)
    {
        cout << s;
    }
    else if ("System.err" == name)
    {
        cerr << s;
    }
    else if (writable)
    {
        outFileStream << s;
    }
  }
}


void OclFile::writeByte(int s)
{
  if (isRemote)
  { char* buff = new char[2];
    buff[0] = s; 
    buff[1] = 0; 
    send(socket, buff, 1, 0);  
  }
  else if ("System.out" == name)
  {
    cout << s;
  }
  else if ("System.err" == name)
  {
    cerr << s;
  }
  else if (writable)
  {
    outFileStream << s;
  }
}

void OclFile::writeNbytes(vector<int>* sq, int n)
{ for (int i = 0; i < sq->size() && i < n; i++)
  { int s = sq->at(i);  
    if (isRemote)
    { char* buff = new char[2];
      buff[0] = s; 
      buff[1] = 0; 
      send(socket, buff, 1, 0);  
    }
    else if ("System.out" == name)
    {
        cout << s;
    }
    else if ("System.err" == name)
    {
        cerr << s;
    }
    else if (writable)
    {
        outFileStream << s;
    }
  }
}

void OclFile::writeAllBytes(vector<int>* sq)
{ for (int i = 0; i < sq->size(); i++)
  { int s = sq->at(i);  
    if (isRemote)
    { char* buff = new char[2];
      buff[0] = s; 
	  buff[1] = 0; 
      send(socket, buff, 1, 0);  
    }
    else if ("System.out" == name)
    {
        cout << s;
    }
    else if ("System.err" == name)
    {
        cerr << s;
    }
    else if (writable)
    {
        outFileStream << s;
    }
  }
}

void OclFile::print(string s)
{
    OclFile::write(s);
} 

string OclFile::readLine()
{ string res = "";
  char buff[512]; 
  if (isRemote)
  { recv(socket, buff, 512, 0);
    return string(buff); 
  } 
    
  if ("System.in" == name)
  {
      cin.getline(buff, 512); 
      res = string(buff); 
  }
  else if (readable)
  {
      inFileStream.getline(buff,512);
      res = string(buff); 
      position = position + res.length(); 
  }
  return res; 
}

vector<string>* OclFile::readAllLines()
{ vector<string>* res = new vector<string>();
  char buff[512]; 
  if (isRemote)
  { int nbytes = recv(socket, buff, 512, 0);
    while (nbytes > 0 && buff != NULL)
    { res->push_back(string(buff)); 
      nbytes = recv(socket, buff, 512, 0);
    } 
    return res;
  } 

  if ("System.in" == name)
  {
    cin.getline(buff, 512); 
    while (buff != NULL && buff[0] != '\0')
    { res->push_back(string(buff)); 
      cin.getline(buff, 512); 
    }
    return res;
  }
  else if (readable)
  {
    inFileStream.getline(buff,512);
    while (buff != NULL && buff[0] != '\0')
    { string sbuff = string(buff); 
      res->push_back(sbuff); 
      position = position + sbuff.length();
      inFileStream.getline(buff,512);
    }
    return res;
  }
  return res; 
}

string OclFile::readAll()
{
    string res = "";
    ostringstream contents; 
	 
    if (isRemote)
    { char buff[512]; 
      int nbytes = recv(socket, buff, 512, 0);
      while (nbytes > 0 && buff != NULL)
	  { contents << string(buff); 
	    nbytes = recv(socket, buff, 512, 0);
	  } 
      res = contents.str();
    } 
    else if ("System.in" == name)
    {
        char c;
        while ( ( c = cin.get() ) != EOF )
        {
            contents << c; 
        }
        res = contents.str(); 
    }
    else if (readable)
    { char c;
      inFileStream.seekg(0); 
      while ((c = inFileStream.get()) != EOF)
      {
        contents << c;
      }
      res = contents.str();
   }
   return res;
}

string OclFile::read()
{
    string res = ""; 

    if (isRemote)
    { char buff[] = { ' ', '\0' }; 
      recv(socket, buff, 1, 0);
      if (buff != NULL && buff[0] != '\0')
	  { return string(buff); } 
    } 
    else if ("System.in" == name)
    {
        char buff[] = { ' ', '\0' }; 
        buff[0] = cin.get();
        if (buff[0] == EOF)
        { return ""; } 
         
        res = string(buff);
    }
    else if (readable)
    {
        char buff[] = { ' ', '\0' };
        buff[0] = inFileStream.get();
        if (buff[0] == EOF)
        { return ""; }
        position++; 
  
        res = string(buff);
    }
    return res;
}

int OclFile::readByte()
{
    if (isRemote)
    { char buff[] = { ' ', '\0' }; 
      recv(socket, buff, 1, 0);
      if (buff != NULL)
	  { return buff[0]; } 
    } 
    else if ("System.in" == name)
    { return cin.get(); }
    else if (readable)
    {
        char buff[] = { ' ', '\0' };
        buff[0] = inFileStream.get();
        position++; 
        return (int) buff[0]; 
    }
    return -1;
}

vector<string>* OclFile::readN(int n)
{ vector<string>* res = new vector<string>(); 

  int ind = 0; 
  if (isRemote)
  { char buff[] = { ' ', '\0' }; 
    int nbytes = recv(socket, buff, 1, 0);
    while (nbytes > 0 && ind < n)
	{ res->push_back(string(buff)); 
	  ind++; 
	  nbytes = recv(socket, buff, 1, 0);
	} 

	return res; 
  } 
  else if ("System.in" == name)
  { char buff[] = { ' ', '\0' }; 
    buff[0] = cin.get(); 
        
    while (buff[0] != EOF && ind < n)
    { res->push_back(string(buff)); 
      ind++; 
      buff[0] = cin.get();
    } 
	
    return res;
  }
  
  if (readable)
  {
     char buff[] = { ' ', '\0' };
     buff[0] = inFileStream.get();
     while (buff[0] != EOF && ind < n)
     { res->push_back(string(buff)); 
       ind++; 
       position++; 
       buff[0] = inFileStream.get();
     }
     return res; 
   }

   return res;
}

vector<int>* OclFile::readNbytes(int n)
{ vector<int>* res = new vector<int>(); 

  int ind = 0; 
  if (isRemote)
  { char buff[] = { ' ', '\0' }; 
    int nbytes = recv(socket, buff, 1, 0);
    while (nbytes > 0 && ind < n)
	{ res->push_back(buff[0]); 
	  ind++; 
	  nbytes = recv(socket, buff, 1, 0);
	} 

	return res; 
  } 
  else if ("System.in" == name)
  { char buff[] = { ' ', '\0' }; 
    buff[0] = cin.get(); 
        
    while (buff[0] != EOF && ind < n)
    { res->push_back(buff[0]); 
      ind++; 
      buff[0] = cin.get();
    } 
	
    return res;
  }
  
  if (readable)
  {
     char buff[] = { ' ', '\0' };
     buff[0] = inFileStream.get();
     while (buff[0] != EOF && ind < n)
     { res->push_back(buff[0]); 
       ind++; 
       position++; 
       buff[0] = inFileStream.get();
     }
     return res; 
   }

   return res;
}

vector<int>* OclFile::readAllBytes()
{ vector<int>* res = new vector<int>(); 

  if (isRemote)
  { char buff[] = { ' ', '\0' }; 
    int nbytes = recv(socket, buff, 1, 0);
    while (nbytes > 0)
	{ res->push_back(buff[0]); 
	  nbytes = recv(socket, buff, 1, 0);
	} 

	return res; 
  } 
  else if ("System.in" == name)
  { char buff[] = { ' ', '\0' }; 
    buff[0] = cin.get(); 
        
    while (buff[0] != EOF)
    { res->push_back(buff[0]); 
       
      buff[0] = cin.get();
    } 
	
    return res;
  }
  
  if (readable)
  {
     char buff[] = { ' ', '\0' };
     buff[0] = inFileStream.get();
     while (buff[0] != EOF)
     { res->push_back(buff[0]); 
       
       position++; 
       buff[0] = inFileStream.get();
     }
     return res; 
   }

   return res;
}

void OclFile::copyFromTo(OclFile* source, OclFile* target)
{ 
  if (source == NULL || target == NULL || source->isRemote || target->isRemote) 
  { return; }

  if ("System.in" == source->name)
  { char buff[] = { ' ', '\0' }; 
    buff[0] = cin.get(); 
        
    while (buff[0] != EOF)
    { target->writeByte(buff[0]); 
       
      buff[0] = cin.get();
    } 
	
    return;
  }
  
  if (source->readable)
  {
     char buff[] = { ' ', '\0' };
     buff[0] = source->inFileStream.get();
     while (buff[0] != EOF)
     { target->writeByte(buff[0]); 
       
       source->position++; 
       buff[0] = source->inFileStream.get();
     }
     return; 
   }
}

void OclFile::skipBytes(int n)
{
	if (isRemote) { return; }

    if ("System.in" == name)
    {
        cin.ignore(n); 
    }
    else if (readable)
    { inFileStream.ignore(n);
      position = position + n; 
    }
}

void OclFile::mark()
{
    if ("System.in" == name || "System.out" == name ||
        "System.err" == name || isRemote) {
    }
    else if (readable)
    {
        markedPosition = inFileStream.tellg(); 
    }
    else if (writable)
    {
        markedPosition = outFileStream.tellp();
    }
}

void OclFile::reset()
{
    if ("System.in" == name || "System.out" == name ||
        "System.err" == name || isRemote) {
    }
    else if (readable)
    {
        inFileStream.seekg(markedPosition);
        position = markedPosition; 
    }
    else if (writable)
    {
        outFileStream.seekp(markedPosition);
        position = markedPosition; 
    }
}

bool OclFile::mkdir()
{
    int rc = _mkdir(name.c_str()); 
    if (rc == 0)
    {
        return true;
    }
    return false; 
}

void OclFile::printf(string f, vector<void*>* sq)
{
    int n = sq->size();
    int flength = f.length();
    if (n == 0 || flength == 0)
    {
        return;
    }

    const char* format = f.c_str(); 
    void* ap;
    char* p = (char*) format;
    int i = 0;

    char* sval;
    int ival;
    unsigned int uval;
    double dval;

    ostringstream buff; 
    ap = sq->at(0);
    for (; *p != '\0'; p++)
    {
        if (*p != '%')
        {
            buff << *p;
            continue;
        }

        char* tmp = (char*)calloc(flength + 1, sizeof(char));
        tmp[0] = '%';
        int k = 1;
        p++;

        while (*p != 'i' && *p != 'd' && *p != 'g' && *p != 'e' &&
            *p != 'o' && *p != 'x' && *p != 'X' && *p != 'E' &&
            *p != 'G' && *p != 's' && *p != '%' &&
            *p != 'u' && *p != 'c' && *p != 'f' && *p != 'p')
        {
            tmp[k] = *p;
            k++;
            p++;
        }  /* Now p points to flag after % */

        tmp[k] = *p;
        tmp[k + 1] = '\0';

        if (i >= n)
        {
            continue;
        }

        switch (*p)
        {
        case 'i':
            ival = *((int*) sq->at(i));
            i++;
            { char* ibuff0 = 
                (char*) calloc(int(log10(abs(ival) + 1)) + 2, sizeof(char));
              sprintf(ibuff0, tmp, ival);
              buff << ibuff0;
            }
            break;
        case 'o':
        case 'x':
        case 'X':
        case 'u':
            uval = *((unsigned int*) sq->at(i));
            i++;
            { char* ubuff = 
                (char*) calloc(int(log10(uval+1)) + 2, sizeof(char));
              sprintf(ubuff, tmp, uval);
              buff << ubuff;
            }
            break;
        case 'c':
        case 'd':
            ival = *((int*) sq->at(i));
            i++;
            { char* ibuff1 = 
                (char*) calloc(int(log10(abs(ival) + 1)) + 2, sizeof(char));
              sprintf(ibuff1, tmp, ival);
              buff << ibuff1;
            }
            break;
        case 'f':
        case 'e':
        case 'E':
        case 'g':
        case 'G':
            dval = *((double*) sq->at(i));
            i++;
            { char* dbuff = 
                (char*) calloc(int(log10(fabs(dval) + 1)) + 2, sizeof(char));
              sprintf(dbuff, tmp, dval);
              buff << dbuff;
            }
            break;
        case 's':
            sval = ((char*) sq->at(i));
            i++;
            { char* sbuff = (char*) calloc(strlen(sval) + 1, sizeof(char));
              sprintf(sbuff, tmp, sval);
              buff << sbuff;
            }
            break;
        case 'p':
            i++;
            { char* pbuff = (char*)calloc(9, sizeof(char));
              sprintf(pbuff, tmp, sq->at(i));
              buff << pbuff;
            }
            break;
        default:
            buff << *p; 
            break;
        }
    }
    string res = buff.str(); 
    write(res); 
}

bool OclFile::hasNext()
{  if (isRemote) { return false; } 

    char buff[512];
    if ("System.in" == name)
    {
        if (cin.eof()) { return false; }

        cin.getline(buff, 512);
        if (buff != NULL && strlen(buff) > 0)
        {
            lastRead = string(buff);
            return true;
        }
    }
    else if (readable)
    {
        if (inFileStream.eof()) { return false; }

        inFileStream.getline(buff, 512);
        if (buff != NULL && strlen(buff) > 0)
        {
            lastRead = string(buff);
            position = position + lastRead.length(); 
            return true;
        }
    }
    return false;
}

string OclFile::getCurrent()
{
    return lastRead; 
}

template<class E>
void OclFile::writeObject(E* obj)
{
    string text = obj->toString();
    write(text); 
}

