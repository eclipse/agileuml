import Foundation
import Darwin


class OclFile
{ private static var instance : OclFile? = nil

  var name : String = ""
  var position : Int = 0
  var markedPosition : Int = 0
  var linePosition : Int = 0
  var eof : Bool = false
  var lastRead : String = ""
  var lines : [String] = []
  var characters : [Character] = []
  var fileLength : Int64 = 0
  var outputBuffer : String = ""
  var writeMode : Bool = false

  static var OclFile_index :
           Dictionary<String,OclFile> = [String:OclFile]()

  static func getByPKOclFile(index : String) -> OclFile?
  { return OclFile_index[index] }

  init() { }

  static func defaultInstance() -> OclFile
  { if instance != nil
    { return instance! }
    instance = createByPKOclFile(key: "")
    return instance!
  }

  func clear()
  { position = 0
    linePosition = 0
    markedPosition = 0
    eof = false
    lastRead = ""
    lines = []
    characters = []
    fileLength = 0
    outputBuffer = ""
    writeMode = false 
  }

  func getName() -> String
  { return name }

  static func newOclFile(nme : String) -> OclFile
  { let res = createByPKOclFile(key: nme)
    return res
  }

  static func newOclFile_Read(f : OclFile) -> OclFile
  { f.openRead()  /* new */
    f.writeMode = false
    return f
  }

  static func newOclFile_Write(f : OclFile) -> OclFile
  { let fname = f.getName()
    OclFile.createFile(filename: fname)
    f.writeMode = true
    return f
  }

  static func newOclFile_ReadB(f : OclFile) -> OclFile
  { f.openRead()  /* new */
    f.writeMode = false
    return f
  }

  static func newOclFile_WriteB(f : OclFile) -> OclFile
  { let fname = f.getName()
    OclFile.createFile(filename: fname)
    f.writeMode = true
    return f
  }

  func compareTo(f : OclFile) -> Int
  { let fname = f.getName()
    if name < fname
    { return -1 }
    if fname < name
    { return 1 }
    return 0
  }

  func canRead() -> Bool
  { return OclFile.fileIsReadable(filename: name) }
  
  func canWrite() -> Bool
  { return OclFile.fileIsWritable(filename: name) }
  
  func openRead()
  { writeMode = false

    if lines.count > 0 
    { return }

    let fm = FileManager.default
    do
    { let docpath =
        try fm.url(for: .documentDirectory,
                   in: .allDomainsMask,
                   appropriateFor: nil, create: false)
      let furl = docpath.appendingPathComponent(name)
      let text =
          try String(contentsOf: furl, encoding: .utf8)
      lines = Ocl.toLineSequence(str: text)
      characters = Ocl.chars(str: text)
      fileLength = Int64(characters.count)
      writeMode = false
    }
    catch
    { }
  }



  func openWrite()
  { writeMode = true }

  func openReadB()
  { writeMode = false }

  func openWriteB()
  { writeMode = true }

  func closeFile()
  { if (name == "System.in" ||
        name == "System.out" || name == "System.err")
    { return }

    let filemgr = FileManager.default
    let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask)
    let docsDir = dirPaths[0]
    let path = docsDir.appendingPathComponent(name)
    
    do
    { if writeMode == true
      { let wpath =
          try filemgr.url(for: .documentDirectory,
                   in: .allDomainsMask,
                   appropriateFor: nil, create: false)
        let furl = wpath.appendingPathComponent(name)
        try outputBuffer.write(to: furl,
                atomically: true, encoding: .utf8)
      }
    
      let pathtext =
          try String(contentsOf: path, encoding: .utf8)

      let file: FileHandle? = FileHandle(forUpdatingAtPath: pathtext)
      if file != nil
      {
        file?.closeFile()
      }
      clear()
    }
    catch
    { Swift.print("Error writing file: " + error.localizedDescription)
      return
    }
  }

  func exists() -> Bool
  { return OclFile.fileExists(filename: name) }

  func isFile() -> Bool
  { let fileatts =
      OclFile.fileAttributes(filename: name) as NSDictionary
    return "NSFileTypeRegular" == fileatts.fileType()
  }

  func isDirectory() -> Bool
  { let fileatts =
      OclFile.fileAttributes(filename: name) as NSDictionary
    return "NSFileTypeDirectory" == fileatts.fileType()
  }

  func isAbsolute() -> Bool
  { return OclFile.fileExistsAbsolutePath(filename: name) }

  func getAbsolutePath() -> String
  { let fm = FileManager.default
    do
    { let path = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)
      let furl = path.appendingPathComponent(name)
      return furl.path
    }
    catch { }
    return name
  }

  func getPath() -> String
  { return self.getAbsolutePath() }

  func getParent() -> String
  { let fm = FileManager.default
    do
    { let filepath = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)
      return filepath.path
    }
    catch { }
    return ""
  }

  func getParentFile() -> OclFile
  { let pname = getParent()
    return OclFile.newOclFile(nme: pname)
  }

  func lastModified() -> Int64
  { let fileatts =
      OclFile.fileAttributes(filename: name) as NSDictionary
    let dte = fileatts.fileModificationDate()
    if dte == nil
    { return 0 }
    return Int64(dte!.timeIntervalSince1970 * 1000)
  }

  func length() -> Int64
  { var fileSize : Int64

   do {
      let attr = 
        try FileManager.default.attributesOfItem(atPath: name)
      fileSize = attr[FileAttributeKey.size] as! Int64
      return fileSize
    } catch {
      return 0
    }
  }

  func delete() -> Bool
  { let res = OclFile.deleteFile(filename: name)
    if "Success" == res
    { return true }
    return false
  }

  func list() -> [String]
  { return OclFile.listFiles(path: name) }

  func listFiles() -> [OclFile]
  { var res : [OclFile] = []
    let names = list()
    for nme in names
    { let fle = OclFile.newOclFile(nme: nme)
      res.append(fle)
    }
    return res
  }

    func print(s : String)
    { if ("System.out" == name || "System.err" == name)
      { Swift.print(s) }
      else
      { write(s: s) }
    }
    
  func println(s : String)
  { if ("System.out" == name || "System.err" == name)
    { Swift.print(s) }
    else
    { write(s: s + "\n") }
  }

  func printf(f : String, sq : [CVarArg])
  { let swiftfmt = f.replacingOccurrences(of: "%s", with: "%@")
    let txt = String(format: swiftfmt, arguments: sq)
    if ("System.out" == name || "System.err" == name)
    { Swift.print(txt) }
    else
    { write(s: txt) }
  }

  func write(s: String)
  { /* let fm = FileManager.default
    do
    { let path =
        try fm.url(for: .documentDirectory,
                   in: .allDomainsMask,
                   appropriateFor: nil, create: false)
      let furl = path.appendingPathComponent(name)
      try s.write(to: furl, atomically: true, encoding: .utf8)
    }
    catch { print("Unable to write to file " + name); } */
    outputBuffer = outputBuffer + s
  }

  func writeObject<T : Encodable>(s : T)
  {
    let encoder = JSONEncoder()
    let data = try! encoder.encode(s)
    var jsonString : String =
                     String(data: data, encoding: .utf8)!
    jsonString =
       String(describing: type(of: s)) + " " + jsonString
    println(s: jsonString)
  }

  func writeln(s : String)
  { write(s: s + "\n") }

  func flush()
  { if writeMode == false
    { return }

    if (name == "System.in" ||
        name == "System.out" || name == "System.err")
    { return }

    let fm = FileManager.default
    do
    { let path =
        try fm.url(for: .documentDirectory,
                   in: .allDomainsMask,
                   appropriateFor: nil, create: false)
      let furl = path.appendingPathComponent(name)
      try outputBuffer.write(to: furl,
                atomically: true, encoding: .utf8)
      // outputBuffer = ""
    }
    catch { Swift.print("Unable to write to file " + name); }
  }

  func hasNext() -> Bool
  { return position < fileLength }

    func hasNextLine() -> Bool
    { return linePosition < lines.count }

  func getCurrent() -> String
  { if position < characters.count
    { return String(characters[position]) }
    eof = true
    return ""
  }

  func read() -> String
  { if name == "System.in"
    { let res = Swift.readLine(strippingNewline: true)
      if res == nil
      { return "" }
      return res!
    }

    if position < characters.count
    { let res = String(characters[position])
      position = position + 1
      return res
    }
    else
    { eof = true
      return ""
    }
  }
  
  func readByte() -> Int
  { if position < characters.count
    { if let sc = Unicode.Scalar(String(characters[position]))
      { 
        position = position + 1
        return Int(sc.value)
      }
      return -1
    }
    else
    { eof = true
      return -1
    }
  }

  func readN(n : Int) -> [String]
  { var ind : Int = 0
    var res : [String] = [String]()
    
    while ind < n && position < characters.count
    { let chr = String(characters[position])
      position = position + 1
      ind = ind + 1
      res.append(chr)
    }
    
    if ind < n
    { eof = true }
    
    return res
  }


  func readNbytes(n : Int) -> [Int]
  { var ind : Int = 0
    var res : [Int] = [Int]()
    
    while ind < n && position < characters.count
    { if let sc = Unicode.Scalar(String(characters[position]))
      { 
        position = position + 1
        res.append(Int(sc.value))
        ind = ind + 1
      }
      else 
      { eof = true; 
        return res; 
      } 
    }
    
    if ind < n
    { eof = true }
    
    return res
  }

  func readAllBytes() -> [Int]
  { 
    var res : [Int] = [Int]()
    
    while position < characters.count
    { if let sc = Unicode.Scalar(String(characters[position]))
      { 
        position = position + 1
        res.append(Int(sc.value))
      }
      else 
      { eof = true; 
        return res; 
      } 
    }
        
    return res
  }

  static func copyFromTo(source: OclFile, target: OclFile)
  { let contents = source.readAll()
    target.outputBuffer = contents
    target.flush()
  }
  

  func readAllLines() -> [String]
  { return lines }

/*
  func readObject() -> AnyObject?
  { let jsonString = readLine()
    let ind = Ocl.indexOf(str: jsonString, ch: " ")
    if ind > 0
    { let typeName = Ocl.stringSubrange(str: jsonString, 
                                        st: 1, en: ind-1)
  
      let json = Ocl.stringSubrange(str: jsonString, 
                                    st: ind+1)
      let res = instanceFromJSON(typeName: typeName, 
                                 json: json)
      return res
    } 
    return nil
  } */ 
  
  func readLine() -> String
  { if name == "System.in"
    { let res = Swift.readLine(strippingNewline: true)
      if res == nil
      { return "" }
      return res!
    }

    if linePosition < lines.count
    { let res = lines[linePosition]
      linePosition = linePosition + 1
      return res
    }
    else
    { eof = true
      return ""
    }
  }
  
  func readAll() -> String
  { if name == "System.in"
    { let res = Swift.readLine(strippingNewline: true)
      if res == nil
      { return "" }
      return res!
    }

    var res : String = ""
    for line in lines
    { res = res + line + "\n" }
    return res
  }

  func mark()
  { markedPosition = position }

  func reset()
  { position = markedPosition }

  func skipBytes(n : Int)
  { position = position + n }

  func mkdir() -> Bool
  { let fm = FileManager.default
    do
    { let path = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)
      let furl = path.appendingPathComponent(name)
      //  print(furl)
      try fm.createDirectory(atPath: furl.path, withIntermediateDirectories: false)
      return true
    }
    catch { Swift.print("Unable to create directory " + name)
            return false
    }
    
  }

  
  static func createFile(filename : String)
  { let fm = FileManager.default
    do
    { let path = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)
      let furl = path.appendingPathComponent(filename)
      //  print(furl)
      try "".write(to: furl, atomically: true, encoding: .utf8)
    }
    catch { Swift.print("Unable to create file " + filename); }
  }
    

  static func fileExists(filename : String) -> Bool
  { let fm = FileManager.default
    do
    { let path =
        try fm.url(for: .documentDirectory,
                   in: .allDomainsMask,
                   appropriateFor: nil, create: false)
      let furl = path.appendingPathComponent(filename)
      return fm.fileExists(atPath: furl.path)
    }
    catch { return false }
  }

  static func fileExistsAbsolutePath(filename : String) -> Bool
  { let filemgr = FileManager.default
    return filemgr.fileExists(atPath: filename)
  }

  static func fileAttributes(filename : String) -> [FileAttributeKey : Any]
  { let filemgr = FileManager.default
    let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask)
    let docsDir = dirPaths[0]
    let furl = docsDir.appendingPathComponent(filename)
    do
    { let res = try filemgr.attributesOfItem(atPath: furl.path)
      return res
    }
    catch { return [:] }
  }


  static func fileIsReadable(filename : String) -> Bool
  { let fm = FileManager.default
    do
    { let docpath = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)
      let furl = docpath.appendingPathComponent(filename)
      return fm.isReadableFile(atPath: furl.path)
    }
    catch { return false }
  }

  static func fileIsWritable(filename : String) -> Bool
  { let fm = FileManager.default
    do
    { let docpath = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)
      let furl = docpath.appendingPathComponent(filename)
      return fm.isWritableFile(atPath: furl.path)
    }
    catch { return false }
  }

  static func deleteFile(filename : String) -> String
  { let fm = FileManager.default
    do
    { let docpath =
        try fm.url(for: .documentDirectory,
                   in: .allDomainsMask,
                   appropriateFor: nil, create: false)
      let furl = docpath.appendingPathComponent(filename)
      try fm.removeItem(atPath: furl.path)
      return "Success"
    }
    catch let error
    { return "Error: " + error.localizedDescription }
  }
  
  static func readFile(filename : String) -> [String]
  { var res : [String] = [String]()
    let filemgr = FileManager.default
    let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask)
    let docsDir = dirPaths[0]
    let path = docsDir.appendingPathComponent(filename)
    do
    { let text =
          try String(contentsOf: path, encoding: .utf8)
      res = Ocl.toLineSequence(str: text)
      return res
    }
    catch
    { return res }
  }
  
  static func writeFile(filename : String, contents : [String])
  { var text : String = ""
    let filemgr = FileManager.default
    let dirPaths = filemgr.urls(for: .documentDirectory, in: .userDomainMask)
    let docsDir = dirPaths[0]
    let path = docsDir.appendingPathComponent(filename)
    for s in contents
    { text = text + s + "\n" }

    do
    { let pathtext =
          try String(contentsOf: path, encoding: .utf8)

      // print(pathtext)
        
      let file: FileHandle? = FileHandle(forUpdatingAtPath: pathtext)
      if file != nil
      { let data = (text as NSString).data(using: String.Encoding.utf8.rawValue)
        file?.write(data!)
        file?.closeFile()
      }
    }
    catch { return }
  }
    
  static func listFiles(path: String) -> [String]
  { let fm = FileManager.default
    do
    { let docpath = try fm.url(for: .documentDirectory, in: .allDomainsMask, appropriateFor: nil, create: false)
      let furl = docpath.appendingPathComponent(path)
      let files = try fm.contentsOfDirectory(atPath: furl.path)
      return files
    } catch { return [] }
  }
    
}

var OclFile_allInstances : [OclFile] = [OclFile]()

func createOclFile() -> OclFile
{ let result : OclFile = OclFile()
  OclFile_allInstances.append(result)
  return result
}

func addOclFile(instance : OclFile)
{ OclFile_allInstances.append(instance) }

func killOclFile(obj: OclFile)
{ OclFile_allInstances = OclFile_allInstances.filter{ $0 !== obj } }


func createByPKOclFile(key : String) -> OclFile
{ var result : OclFile? = OclFile.getByPKOclFile(index: key)
  if result != nil
  { return result! }
  result = OclFile()
  OclFile_allInstances.append(result!)
  OclFile.OclFile_index[key] = result!
  result!.name = key
  return result!
}

func killOclFile(key : String)
{ OclFile.OclFile_index[key] = nil
  OclFile_allInstances.removeAll(where: { $0.name == key })
}



