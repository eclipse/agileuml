package oclfile

import "container/list"
import "os"
// import "io"
// import "io/fs"
import "fmt"
import "path"
import "path/filepath"
import "bufio"
import "../ocl"


type OclFile struct {
  name string
  position int64
  markedPosition int64
  actualFile *os.File
  reader *bufio.Reader
  readable bool
  writable bool
  eof bool
}


func NewOclFile(nme string) *OclFile { 
  res := &OclFile{}
  res.name = nme
  res.position = 0
  res.markedPosition = 0
  res.readable = false
  res.writable = false
  return res
} 

var System_in = NewOclFile("System.in")
var System_out = NewOclFile("System.out")
var System_err = NewOclFile("System.err")


func (self *OclFile) GetName() string { 
  return self.name
} 

func (self *OclFile) GetParent() string { 
  return path.Dir(self.name)
} 

func (self *OclFile) GetParentFile() *OclFile { 
  return NewOclFile(path.Dir(self.name))
} 

func (self *OclFile) GetAbsolutePath() string { 
  pth, err := filepath.Abs(self.name)
  if err == nil { 
    return pth
  }
  return self.name
} 


func NewOclFile_Read(fle *OclFile) *OclFile {
  f, err := os.Open(fle.name)
  if err == nil { 
    fle.actualFile = f
    fle.readable = true
    fle.writable = false
    fle.eof = false
    fle.reader = bufio.NewReader(f)
  } 
  return fle
}

func NewOclFile_Write(fle *OclFile) *OclFile { 
  f, err := os.Create(fle.name)
  if err == nil { 
    fle.actualFile = f
    fle.writable = true
    fle.eof = false
  } 
  return fle
}

func (self *OclFile) IsFile() bool { 
  finfo, err := os.Lstat(self.name)
  if err != nil { 
    return false
  } 

  mde := finfo.Mode()
  if mde.IsRegular() { 
    return true
  } 
  return false
} 

func (self *OclFile) IsDirectory() bool { 
  finfo, err := os.Lstat(self.name)
  if err != nil { 
    return false
  } 

  mde := finfo.Mode()
  if mde.IsDir() { 
    return true
  } 
  return false
} 

func (self *OclFile) LastModified() int64 { 
  finfo, err := os.Lstat(self.name)
  if err != nil { 
    return 0
  } 

  mde := finfo.ModTime()
  return mde.Unix()
} 


func (self *OclFile) CloseFile() {
  if self.actualFile != nil { 
    self.actualFile.Close()
    self.readable = false
    self.writable = false
  } 
}

func (self *OclFile) Length() int64 { 
  finfo, err := os.Lstat(self.name)
  if err != nil { 
    return 0
  } 
  return finfo.Size()
}

func (self *OclFile) SetPosition(pos int64) { 
  if self.actualFile != nil { 
    self.actualFile.Seek(pos,0)
    self.position = pos
  } 
} 

func (self *OclFile) GetPosition() int64 { 
  return self.position 
} 

func (self *OclFile) SkipBytes(n int) { 
  if self.actualFile != nil { 
    self.actualFile.Seek(int64(n),1)
    self.position = self.position + int64(n)
  } 
} 

func (self *OclFile) Mark(p int64) { 
  self.markedPosition = p 
} 

func (self *OclFile) Reset() { 
  if self.actualFile != nil { 
    self.actualFile.Seek(self.markedPosition,0)
    self.position = self.markedPosition
  } 
} 



func (self *OclFile) IsAbsolute() bool {
  return path.IsAbs(self.name)
}

func (self *OclFile) GetEof() bool { 
  return self.eof 
} 
 
func (self *OclFile) Read() string {
  if self.name == "System.in" { 
    var res string = ""
    var sptr *string = &res
    fmt.Scanf("%s", sptr)
    return *sptr
  } 

  if self.readable && self.actualFile != nil { 
    b := make([]byte, 1)
    n, err := self.actualFile.Read(b)
    if n > 0 && err == nil { 
      self.position = self.position + int64(n)
      return string(b[0])
    } else { 
      self.eof = true
    } 
  }
  return ""
}

func (self *OclFile) ReadByte() int {
  if self.readable && self.actualFile != nil { 
    b := make([]byte, 1)
    n, err := self.actualFile.Read(b)
    if n > 0 && err == nil { 
      self.position = self.position + int64(n)
      return int(b[0])
    } else { 
      self.eof = true
    } 
  }
  return -1
}

func (self *OclFile) ReadN(n int) *list.List {
  res := list.New()

  if self.readable && self.actualFile != nil { 
    b := make([]byte, n)
    readbytes, err := self.actualFile.Read(b)
    if err == nil { 
    } else { 
      self.eof = true
    } 

    self.position = self.position + int64(readbytes)
    for i := 0; i < readbytes; i++ { 
      res.PushBack(string(b[i]))
    } 
     
  }
  return res
}

func (self *OclFile) ReadNbytes(n int) *list.List {
  res := list.New()

  if self.readable && self.actualFile != nil { 
    b := make([]byte, n)
    readbytes, err := self.actualFile.Read(b)
    if err == nil { 
    } else { 
      self.eof = true
    } 

    self.position = self.position + int64(readbytes)
    for i := 0; i < readbytes; i++ { 
      res.PushBack(int(b[i]))
    } 
     
  }
  return res
}

func (self *OclFile) ReadLine() string {
  if self.name == "System.in" { 
    rdr := bufio.NewReader(os.Stdin)
    res, err := rdr.ReadBytes('\n')
    if err != nil { 
      self.eof = true
      return ""
    }
    return string(res)
  } 

  if self.readable && self.actualFile != nil { 
    b := make([]byte, 100)
    b, err := self.reader.ReadBytes('\n')
    if err == nil { 
      self.position = self.position + int64(len(b))
      return string(b)
    } else { 
      self.eof = true
    } 
  }
  return ""
}

/* 
func (self *OclFile) ReadAll() string {
  if self.readable && self.actualFile != nil { 
    contents, _ := io.ReadAll(self.actualFile)
    return string(contents)
  } 
  return ""
} */ 


func (self *OclFile) Flush() { 
  if self.writable && self.actualFile != nil { 
    self.actualFile.Sync()
  } 
}

func (self *OclFile) Write(s string) { 
  if self.name == "System.out" { 
    fmt.Printf("%s", s)
  } else {
    if self.writable && self.actualFile != nil { 
      self.actualFile.Write([]byte(s))
    }
  } 
}

func (self *OclFile) WriteN(sq *list.List, n int) { 
  if self.name == "System.out" { 
    ind := 0
    for e := sq.Front(); e != nil && ind < n; e = e.Next() {
      x := e.Value.(string)
      fmt.Printf("%s", x)
      ind++
    } 
  } else {
    if self.writable && self.actualFile != nil { 
      ind := 0
      for e := sq.Front(); e != nil && ind < n; e = e.Next() {
        x := e.Value.(string)
        self.actualFile.Write([]byte(x))
        ind++
      } 
    }
  } 
}

func (self *OclFile) WriteByte(x int) { 
  if self.name == "System.out" { 
    fmt.Printf("%d", x)
  } else {
    if self.writable && self.actualFile != nil { 
      b := byte(x % 256)
      var bb []byte = make([]byte,1)
      bb[0] = b
      self.actualFile.Write(bb)
    }
  } 
}

func (self *OclFile) WriteNbytes(sq *list.List, n int) { 
  if self.name == "System.out" { 
    ind := 0
    for e := sq.Front(); e != nil && ind < n; e = e.Next() {
      x := e.Value.(int)
      fmt.Printf("%d", x)
      ind++
    } 
  } else {
    if self.writable && self.actualFile != nil { 
      ind := 0
      for e := sq.Front(); e != nil && ind < n; e = e.Next() {
        x := e.Value.(int)
        b := byte(x % 256)
        var bb []byte = make([]byte,1)
        bb[0] = b
        self.actualFile.Write(bb)
        ind++
      } 
    }
  } 
}

func (self *OclFile) Println(s string) { 
  self.Writeln(s)
}

func (self *OclFile) Writeln(s string) { 
  if self.name == "System.out" { 
    fmt.Println(s)
  } else {
    if self.writable && self.actualFile != nil { 
      self.actualFile.Write([]byte(s + "\n"))
    } 
  } 
}

func (self *OclFile) Printf(f string, args *list.List) {
  arglist := ocl.AsArray(args)
  if self.name == "System.out" { 
    fmt.Printf(f, arglist...)
  } else { 
    if self.writable && self.actualFile != nil { 
      fmt.Fprintf(self.actualFile, f, arglist...)
    } 
  }
}
 
// func (self *OclFile) Mkdir() bool { 
//   err := os.Mkdir(self.name, fs.ModeDir)
//   if err != nil { 
//     return false
//   } 
//   return true
// } 

/* 
func (self *OclFile) List() *list.List {
  res := list.New()
  files, err := os.ReadDir(self.name)
  if err != nil { 
    return res
  } 

  for _, fle := range files {
    res.PushBack(fle.Name())
  }
  return res
}

func (self *OclFile) ListFiles() *list.List {
  res := list.New()
  files, err := os.ReadDir(self.name)
  if err != nil { 
    return res
  } 

  for _, fle := range files {
    res.PushBack(NewOclFile(fle.Name()))
  }
  return res
}
*/ 

func (self *OclFile) Delete() bool { 
  err := os.Remove(self.name)
  if err != nil { 
    return false
  } 
  return true
} 

func DeleteFile(nme string) bool { 
  err := os.Remove(nme)
  if err != nil { 
    return false
  } 
  return true
} 

func RenameFile(oldnme string, newnme string) bool { 
  err := os.Rename(oldnme, newnme)
  if err != nil { 
    return false
  } 
  return true
} 



