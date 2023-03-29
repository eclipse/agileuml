import ocl

class StringLib : 
  def leftTrim(s) :
    result = s[s.find(ocl.trim(s)):]
    return result

  def rightTrim(s) :
    result = ocl.before(s, ocl.trim(s)) + ocl.trim(s)
    return result

  def padLeftWithInto(s,c,n) :
    result = ocl.sumString([c for self in range(1, n - len(s) +1)]) + s
    return result

  def leftAlignInto(s,n) :
    result = ""
    if n <= len(s) :
      result = s[0:n]
    else :
      if n > len(s) :
        result = s + ocl.sumString([" " for self in range(1, n - len(s) +1)])
    return result

  def rightAlignInto(s,n) :
    result = ""
    if n <= len(s) :
      result = s[0:n]
    else :
      if n > len(s) :
        result = ocl.sumString([" " for self in range(1, n - len(s) +1)]) + s
    return result

  def format(f,sq) : 
    return (f % tuple(sq))

  def scan(s, fmt) :
    result = []
    
    ind = 0; # s upto ind has been consumed
    slen = len(s)

    i = 0
    while i < len(fmt) :  
      c = fmt[i] 
      if c == "%" and i < len(fmt) - 1 : 
        d = fmt[i+1] 
        if d == "s" : 
          schars = ""
          for j in range(ind, slen) : 
            z = s[j] 
            if z.isspace() :
              break
            else :
              schars = schars + z
          result.append(schars)
          ind = ind + len(schars)
          i = i + 1  
        else : 
          if d == "d" : 
            inchars = "" 
            for j in range(ind, slen) : 
              x = s[j] 
              if x.isdecimal() :
                inchars = inchars + x
              else : 
                break
            result.append(int(inchars))
            ind = ind + len(inchars)
            i = i + 1
          else : 
            if d == "f" : 
              fchars = "" 
              for j in range(ind, slen) : 
                y = s[j] 
                if y.isdecimal() or y == "." :
                  fchars = fchars + y
                else : 
                  break
              result.append(float(fchars))
              ind = ind + len(fchars)
              i = i + 1
      else :
        if s[ind] == c :  
          ind = ind+1 
        else :  
          return result
      i = i + 1
    return result 


# print(StringLib.scan("30##text\t", "%d##%s\t"))
