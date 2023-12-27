import java.io.*; 
import java.nio.file.Files; 
import java.nio.file.Path;
import java.util.Map; 
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;

class OclFile { 
  static 
  { OclFile.OclFile_index = new HashMap<String,OclFile>();
    OclFile systemIn = OclFile.createByPKOclFile("System.in"); 
    systemIn.inputStream = System.in; 
    systemIn.inputStreamReader = 
          new BufferedReader(
            new InputStreamReader(System.in)); 

    OclFile systemOut = OclFile.createByPKOclFile("System.out"); 
    systemOut.outputStream = System.out; 
    OclFile systemErr = OclFile.createByPKOclFile("System.err"); 
    systemErr.outputStream = System.err; 
  } 

  public File actualFile = null; 
  public InputStream inputStream = null; 
  public OutputStream outputStream = null; 
  public Reader inputStreamReader = null; 
  public Writer outputStreamWriter = null; 

  private long position = 0L; 
  private long markedPosition = 0L; 
  private boolean eof = false; 

  String name = ""; /* primary */
  static Map<String,OclFile> OclFile_index;


  OclFile() { }

  OclFile(String nme)
  { name = nme; 
    OclFile.OclFile_index.put(name,this);
  } 

  OclFile(File c) 
  { this(c.getName());
    actualFile = c; 
  }


  static OclFile createOclFile() 
  { OclFile result = new OclFile();
    return result; 
  }


  static OclFile createByPKOclFile(String namex)
  { OclFile result = OclFile.OclFile_index.get(namex);
    if (result != null) 
    { return result; }
    result = new OclFile();
    OclFile.OclFile_index.put(namex,result);
    result.name = namex;
    return result; 
  }

  static OclFile getOclFileByPK(String namex)
  { OclFile result = OclFile.OclFile_index.get(namex);
    if (result != null) 
    { return result; }
    result = new OclFile();
    OclFile.OclFile_index.put(namex,result);
    result.name = namex;
    return result; 
  }

  static void killOclFile(String namex)
  { OclFile rem = OclFile_index.get(namex);
    if (rem == null) { return; }
    ArrayList<OclFile> remd = new ArrayList<OclFile>();
    remd.add(rem);
    OclFile_index.remove(namex);
  }

  public static OclFile newOclFile(String nme)
  { OclFile res = createByPKOclFile(nme); 
    res.actualFile = new File(nme); 
    return res; 
  } 

  public static OclFile newOclFile_Read(OclFile f)
  { /* System.out.println("Creating file for " + f.inputStream + " " + f.inputStreamReader); */ 

    if (f.inputStream == null) // create stream from file 
    { try 
      { f.inputStream = new FileInputStream(f.actualFile); 
        f.inputStreamReader = 
            new BufferedReader(new FileReader(f.actualFile));
        f.inputStream.mark((int) f.length());
        f.position = 0;   
      } 
      catch (Exception e) { } 
      return f; 
    }
    else if (f.inputStreamReader == null) // reader from stream
    { try { 
        f.inputStreamReader = 
          new BufferedReader(
            new InputStreamReader(f.inputStream)); 
        f.position = 0;
        return f; 
      } catch (Exception _ex) { _ex.printStackTrace(); }
    }  
    return f;
  } 

  // public void openRead() is the same

  public static OclFile newOclFile_Write(OclFile f)
  { if (f.outputStream == null) 
    { try 
      { f.outputStream = new FileOutputStream(f.actualFile);
        f.outputStreamWriter = new BufferedWriter(new FileWriter(f.actualFile));
        f.position = 0;  
      } 
      catch (Exception e) 
      { e.printStackTrace(); }
    }   
    else if (f.outputStreamWriter == null) // writer from stream
    { try { 
        f.outputStreamWriter = 
          new BufferedWriter(
            new OutputStreamWriter(f.outputStream)); 
        f.position = 0;
      } catch (Exception _ex) { }
    }  
    return f;
  }

  // public void openWrite() is the same

  public static OclFile newOclFile_RW(OclFile f)
  { if (f.inputStream == null) // create stream from file 
    { try 
      { f.inputStream = new FileInputStream(f.actualFile); 
        f.inputStreamReader = 
            new BufferedReader(new FileReader(f.actualFile));
        f.inputStream.mark((int) f.length());
        f.position = 0;   
      } 
      catch (Exception e) { } 
    }
    else if (f.inputStreamReader == null) // reader from stream
    { try { 
        f.inputStreamReader = 
          new BufferedReader(
            new InputStreamReader(f.inputStream)); 
        f.position = 0;
      } catch (Exception _ex) { _ex.printStackTrace(); }
    }  

    if (f.outputStream == null) 
    { try 
      { f.outputStream = new FileOutputStream(f.actualFile);
        f.outputStreamWriter = new BufferedWriter(new FileWriter(f.actualFile));
        f.position = 0;  
      } 
      catch (Exception e) 
      { e.printStackTrace(); }
    }   
    else if (f.outputStreamWriter == null) // writer from stream
    { try { 
        f.outputStreamWriter = 
          new BufferedWriter(
            new OutputStreamWriter(f.outputStream)); 
        f.position = 0;
      } catch (Exception _ex) { }
    }  

    return f;
  }

  public static OclFile newOclFile_ReadB(OclFile f)
  { if (f.inputStream == null && f.actualFile != null) 
    { try 
      { f.inputStream = new ObjectInputStream(new FileInputStream(f.actualFile)); 
        f.position = 0; 
      } 
      catch (Exception e) { } 
      return f; 
    }
    
    if (f.inputStream != null && f.actualFile == null) 
    { try 
      { f.inputStream = new ObjectInputStream(f.inputStream); 
        f.position = 0; 
      } 
      catch (Exception e) { } 
      return f; 
    }

    return f;
  } 

  // public void openReadB() is the same

  public static OclFile newOclFile_WriteB(OclFile f)
  { if (f.outputStream == null && f.actualFile != null) 
    { try 
      { f.outputStream = 
          new ObjectOutputStream(
            new FileOutputStream(f.actualFile)); 
      } 
      catch (Exception e) { }  
      return f; 
    }

    if (f.outputStream != null && f.actualFile == null) 
    { try 
      { f.outputStream = 
             new ObjectOutputStream(f.outputStream); 
        f.position = 0; 
      } 
      catch (Exception e) { } 
      return f; 
    }

    return f;
  }

  // public void openWriteB() is the same

  public void setFile(File f)
  { actualFile = f; } 

  public String getName()
  { return name; }

  public int compareTo(OclFile f)
  { return name.compareTo(f.getName()); }


  public boolean canRead()
  { if (actualFile != null) 
    { return actualFile.canRead(); } 
    return false;
  }

  public boolean canWrite()
  { if (actualFile != null) 
    { return actualFile.canRead(); } 
    return false;
  }

  public boolean isOpen()
  { if (outputStream == null && 
        outputStreamWriter == null && 
        inputStream == null && 
        inputStreamReader == null) 
    { return false; } 
    return true; 
  } 

  public void closeFile()
  { if (outputStreamWriter != null) 
    { try { outputStreamWriter.close(); } 
      catch (Exception _x) { } 
    } 
    if (inputStreamReader != null) 
    { try { inputStreamReader.close(); } 
      catch (Exception _x) { } 
    } 

    outputStreamWriter = null; 
    inputStreamReader = null; 

    if (outputStream != null)
    { try { outputStream.close(); }
      catch (Exception _e) { } 
    } 
    if (inputStream != null)
    { try { inputStream.close(); }
      catch (Exception _f) { } 
    } 

    inputStream = null; 
    outputStream = null; 

    position = 0; 
  }

  public boolean exists()
  { if (actualFile != null) 
    { return actualFile.exists(); }
    return false; 
  } 

  public boolean isFile()
  { if (actualFile != null) 
    { return actualFile.isFile(); }
    return false; 
  } 

  public boolean isDirectory()
  { if (actualFile != null) 
    { return actualFile.isDirectory(); }
    return false; 
  } 

  public boolean isHidden()
  { if (actualFile != null) 
    { return actualFile.isHidden(); }
    return false; 
  } 

  public void setHidden(boolean b)
  { if (actualFile != null) 
    { try { Files.setAttribute(actualFile.toPath(), "dos:hidden", b); }
      catch (Exception _io) { } 
    } 
  } 

  public void setExecutable(boolean h)
  { if (actualFile != null) 
    { actualFile.setExecutable(h); }
  } 

  public boolean isExecutable()
  { if (actualFile != null) 
    { return actualFile.getName().endsWith(".exe"); }
    return false; 
  } 
  
  public void setReadOnly()
  { if (actualFile != null) 
    { actualFile.setReadOnly(); }
  } 

  public boolean isAbsolute()
  { if (actualFile != null) 
    { return actualFile.isAbsolute(); }
    return false; 
  }


  public String getAbsolutePath()
  { if (actualFile != null) 
    { return actualFile.getAbsolutePath(); }
    return name; 
  }

  public String getPath()
  { if (actualFile != null) 
    { return actualFile.getPath(); }
    return name;
  }

  public String getParent()
  { if (actualFile != null) 
    { return actualFile.getParent(); } 
    return ""; 
  } 

  public OclFile getParentFile()
  { if (actualFile != null && 
        actualFile.getParentFile() != null) 
    { OclFile result = 
         new OclFile(actualFile.getParentFile());
      return result; 
    }  
    OclFile f = new OclFile(getParent());
    return f;
  }

  public long lastModified()
  { if (actualFile != null) 
    { return actualFile.lastModified(); } 
    return 0; 
  } 

  public long length()
  { if (actualFile != null) 
    { return actualFile.length(); } 
    return 0; 
  } 

  public boolean delete()
  { if (actualFile != null) 
    { return actualFile.delete(); }
    return false; 
  } // and close everything

  public static boolean deleteFile(String nme)
  { File fle = new File(nme); 
    if (fle != null) 
    { return fle.delete(); }
    return false; 
  } 
  
  public static boolean renameFile(String from, String to)
  { File f1 = new File(from); 
    File f2 = new File(to); 
	if (f1 != null && f2 != null)
	{ Path p1 = f1.toPath(); 
	  Path p2 = f2.toPath(); 
	  try { Files.move(p1,p2); } catch (Exception _m) { return false; }
	  return true; 
	} 
	return false; 
  } 
  
  public static OclFile createTemporaryFile(String f, String ext)
  { File f1 = new File(f + "." + ext); 
    if (f1 == null) { return null; }
    Path p1 = f1.toPath();
    try { Files.createFile(p1); } 
	catch (Exception _ex) { return null; }
    OclFile res = createByPKOclFile(f + "." + ext); 
	res.actualFile = f1; 
	return res; 
  } 
   	
  public ArrayList<String> list()
  { ArrayList<String> result = new ArrayList<String>();
    if (actualFile != null) 
    { String[] fnames = actualFile.list();
      if (fnames == null) 
      { return result; } 
      for (int i = 0; i < fnames.length; i++)
      { result.add(fnames[i]); }
      return result;
    }
    return result;
  }

  public ArrayList<OclFile> listFiles()
  { ArrayList<OclFile> result = new ArrayList<OclFile>();
    if (actualFile != null) 
    { File[] fnames = actualFile.listFiles();
      for (int i = 0; i < fnames.length; i++)
      { result.add(new OclFile(fnames[i])); }
      return result;
    }
    return result;
  }

  public boolean mkdir()
  { boolean result = false;
    if (actualFile != null)
    { return actualFile.mkdir(); }
    return result;
  }

  public boolean mkdirs()
  { boolean result = false;
    if (actualFile != null)
    { return actualFile.mkdirs(); }
    return result;
  }

  public void write(String s)
  { 
    if (outputStreamWriter != null)
    { try { outputStreamWriter.write(s); }
      catch (Exception _e) 
      { _e.printStackTrace(); } 
    } 
  }

  public OclFile append(String s)
  { write(s); 
    return this; 
  }

  public void writeN(ArrayList<String> sq, int n)
  { 
    if (outputStreamWriter != null)
    { try { 
        for (int i = 0; i < n && i < sq.size(); i++) 
        { String s = sq.get(i); 
          outputStreamWriter.write(s); 
        }
      } 
      catch (Exception _e) 
      { _e.printStackTrace(); } 
    } 
  }

  public void writeByte(int c)
  { 
    if (outputStreamWriter != null)
    { try { outputStreamWriter.write(c); }
      catch (Exception _e) 
      { _e.printStackTrace(); } 
    } 
  }

  public void writeNbytes(ArrayList<Integer> sq, int n)
  { 
    if (outputStreamWriter != null)
    { try { 
        for (int i = 0; i < n && i < sq.size(); i++) 
        { int x = sq.get(i); 
          outputStreamWriter.write(x); 
        }
      } 
      catch (Exception _e) 
      { _e.printStackTrace(); } 
    } 
  }

  public void writeln(String s)
  {
    if (outputStreamWriter != null)
    { try { outputStreamWriter.write(s + "\n"); }
      catch (Exception _e) { } 
    } 
  }

  public void writeObject(Object obj)
  { if (outputStream != null && 
        outputStream instanceof ObjectOutputStream) 
    { try { ((ObjectOutputStream) outputStream).writeObject(obj); }
      catch (Exception e) 
      { e.printStackTrace(); } 
    }  
    else if (outputStream != null) 
    { try { 
        ObjectOutputStream objstream = 
          new ObjectOutputStream(outputStream); 
        objstream.writeObject(obj); 
      }
      catch (Exception e) 
      { e.printStackTrace(); } 
    }  

    
  } 

  public void writeMap(Map m)
  { writeObject(m); } 

  public Object readObject()
  { if (inputStream != null && 
        inputStream instanceof ObjectInputStream) 
    { try 
      { Object res = 
             ((ObjectInputStream) inputStream).readObject(); 
        return res; 
      }
      catch (Exception e) 
      { eof = true; } 
    } 
    else if (inputStream != null) 
    { try { 
        ObjectInputStream objstream = 
          new ObjectInputStream(inputStream); 
        Object res = objstream.readObject();
        return res;  
      }
      catch (Exception e) 
      { e.printStackTrace(); } 
    }  


    return null;  
  } 

  public Map readMap()
  { Object res = readObject(); 
    if (res instanceof Map) 
    { return (Map) res; } 
    return new HashMap(); 
  } 


  public String readLine()
  { if (inputStreamReader != null && 
        inputStreamReader instanceof BufferedReader) 
    { try 
      { 
        String ss = ((BufferedReader) inputStreamReader).readLine(); 
        position += ss.length() + 1; 
        return ss; 
      } catch (Exception _e) { 
          _e.printStackTrace(); 
          eof = true; 
          return null; 
      } 
    } 

    return ""; 
  } 

  public String read()
  { if (inputStreamReader != null) 
    { try 
      { 
        int ch = inputStreamReader.read();
        position++;
        if (ch == -1) 
        { eof = true; } 
        return Ocl.byte2char(ch);   
      } catch (Exception _e) 
        { eof = true; } 
    } 
 
    return ""; 
  } 

  public ArrayList<String> readN(int n)
  { ArrayList<String> res = new ArrayList<String>(); 
    if (inputStreamReader != null) 
    { try 
      { int ind = 0; 
        int ch = inputStreamReader.read();
        position++;
        while (ch != -1 && ind < n)
        { res.add(Ocl.byte2char(ch)); 
          ch = inputStreamReader.read();
          position++;
          ind++; 
        } 
        return res;   
      } catch (Exception _e) 
        { eof = true; } 
    } 
    return res; 
  } 

  public int readByte()
  { if (inputStreamReader != null) 
    { try 
      { 
        int ch = inputStreamReader.read();
        position++;
        return ch;   
      } catch (Exception _e) 
        { eof = true; } 
    } 
 
    return -1; 
  } 

  public ArrayList<Integer> readNbytes(int n)
  { ArrayList<Integer> res = new ArrayList<Integer>(); 
    if (inputStreamReader != null) 
    { try 
      { int ind = 0; 
        int ch = inputStreamReader.read();
        position++;
        while (ch != -1 && ind < n)
        { res.add(ch); 
          ch = inputStreamReader.read();
          position++;
          ind++; 
        } 
        return res;   
      } catch (Exception _e) 
        { eof = true; } 
    } 
    return res; 
  } 

  public ArrayList<Integer> readAllBytes()
  { ArrayList<Integer> res = new ArrayList<Integer>(); 
    if (inputStreamReader != null) 
    { try 
      { 
        int ch = inputStreamReader.read();
        position++;
        while (ch != -1)
        { res.add(ch); 
          ch = inputStreamReader.read();
          position++;
        } 
        return res;   
      } catch (Exception _e) 
        { eof = true; } 
    } 
    return res; 
  } 

  public String readAll()
  { if (inputStreamReader != null && 
        inputStreamReader instanceof BufferedReader) 
    { StringBuffer br = new StringBuffer(); 
      try 
      { String lne = 
          ((BufferedReader) inputStreamReader).readLine();
        while (lne != null) 
        { br.append(lne + "\n"); 
          lne = 
            ((BufferedReader) inputStreamReader).readLine();
        } 
        position = length(); 
      } catch (Exception _e) {}
      return br.toString();    
    } 
 
    return null; 
  } 

  public ArrayList<String> readAllLines()
  { ArrayList<String> res = new ArrayList<String>(); 

    if (inputStreamReader != null && 
        inputStreamReader instanceof BufferedReader) 
    { StringBuffer br = new StringBuffer(); 
      try 
      { String lne = 
          ((BufferedReader) inputStreamReader).readLine();
        while (lne != null) 
        { res.add(lne); 
          lne = 
            ((BufferedReader) inputStreamReader).readLine();
        } 
        position = length(); 
      } catch (Exception _e) {}
      return res;    
    } 
 
    return res; 
  } 

  public static void copyFromTo(OclFile source, OclFile target)
  { if (source == null || target == null)
    { return; } 

    if (source.inputStreamReader != null) 
    { try 
      { 
        int ch = source.inputStreamReader.read();
        source.position++;
        while (ch != -1)
        { target.writeByte(ch); 
          ch = source.inputStreamReader.read();
          source.position++;
        } 
        
      } catch (Exception _e) 
        { source.eof = true; } 
    } 
  } 
        

  public void flush()
  {
    if (outputStream != null)
    { try { outputStream.flush(); }
      catch (Exception _e) { } 
    } 
    
    if (outputStreamWriter != null) 
    { try { outputStreamWriter.flush(); } 
      catch (Exception _x) { } 
    } 
  }

  public void print(String s)
  { if ("System.out".equals(name))
    { System.out.print(s); } 
    else if ("System.err".equals(name))
    { System.err.print(s); }
    else
    { write(s); }
  }

  public void println(String s)
  { if ("System.out".equals(name))
    { System.out.println(s); } 
    else if ("System.err".equals(name))
    { System.err.println(s); }
    else
    { writeln(s); }
  }

  public void printf(String s, ArrayList sq)
  { Object[] args = new Object[sq.size()]; 
    for (int i = 0; i < sq.size(); i++) 
    { args[i] = sq.get(i); }
    String formattedString = String.format(s,args);  

    if ("System.out".equals(name))
    { System.out.printf(s,args); } 
    else if ("System.err".equals(name))
    { System.err.printf(s,args); }
    else 
    { write(formattedString); }
  }


  public void mark()
  { if (inputStream != null)
    { inputStream.mark((int) length()); 
      markedPosition = position; 
    }
  } 
        
  public void reset()
  { if (inputStream != null)
    { try { inputStream.reset();
            position = markedPosition;
      }
      catch (Exception _e) { } 
    } 
  } 

  public void skipBytes(int n)
  {
    if (inputStream != null)
    { try { inputStream.skip(n); 
            position += n; 
      }
      catch (Exception _e) 
      { eof = true; } 
    }  
  } 

  public void setPosition(long pos)
  { if (inputStream != null)
    { try { inputStream.reset(); // assume, to 0
            position = pos;
            skipBytes((int) pos); 
      }
      catch (Exception _e) 
      { eof = true; } 
    } 
  } 

  public boolean getEof() 
  { return eof; } 

  public long getPosition()
  { return position; } 

  protected void finalize()
  { closeFile(); } 

 /* public static void main(String[] args)
  { OclFile ff = OclFile.newOclFile("ff.txt"); 
    OclFile gg = OclFile.newOclFile_Read(ff); 
    ArrayList<Integer> ss = gg.readNbytes(4);
    System.out.println(ss); 
    // gg.skipBytes(20); 
    ArrayList<String> cc = gg.readN(4);
    cc = gg.readN(4); 
    System.out.println(cc); 
    gg.closeFile(); 
  }  */ 

}

