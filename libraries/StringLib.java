import java.util.ArrayList; 
import java.util.Scanner; 
import java.util.regex.Pattern; 


public class StringLib
{ 

  public static String nCopies(String s, int n)
  { String result = "";

    for (int i = 0; i < n; i++) 
    { result = result + s; } 
  
    return result;
  }

  public static String leftTrim(String s)
  { String result = "";
  
    result = "" + Ocl.subrange(s,(s.indexOf(s.trim()) + 1),(s).length());
  
    return result;
  }


  public static String rightTrim(String s)
  { String result = "";
    String trm = s.trim();   
    result = "" + Ocl.before(s, trm) + trm;
  
    return result;
  }


  public static String padLeftWithInto(String s,String c,int n)
  { String result = "";
  
    ArrayList<Integer> rng = Ocl.integerSubrange(1,n - (s).length()); 
    ArrayList<String> _results_0 = new ArrayList<String>();
    for (Integer _i : rng)
    { 
      _results_0.add(c);
    }

    result = "" + Ocl.sumString(_results_0) + s;
  
    return result;
  }

  public static String padRightWithInto(String s,String c,int n)
  { String result = "";
  
    ArrayList<Integer> rng = Ocl.integerSubrange(1,n - (s).length()); 
    ArrayList<String> _results_0 = new ArrayList<String>();
    for (Integer _i : rng)
    { 
      _results_0.add(c);
    }

    result = s + "" + Ocl.sumString(_results_0);
  
    return result;
  }


  public static String leftAlignInto(String s,int n)
  { String result = "";
  
    int k = s.length(); 
    if (n <= k) 
    { result = "" + Ocl.subrange(s,1,n); } 
    else
    { ArrayList<Integer> rng = Ocl.integerSubrange(1,n - k); 
      ArrayList<String> _results_0 = new ArrayList<String>();
      for (Integer _i : rng)
      { 
        _results_0.add(" ");
      }
      result = "" + s + Ocl.sumString(_results_0);
    }     
    return result;
  }


  public static String rightAlignInto(String s,int n)
  { String result = "";
  
    int k = s.length(); 
    if (n <= k) 
    { result = "" + Ocl.subrange(s,1,n); }
    else
    { ArrayList<Integer> rng = Ocl.integerSubrange(1,n - k); 
      ArrayList<String> _results_0 = new ArrayList<String>();
      for (Integer _i : rng)
      { 
        _results_0.add(" ");
      }
      result = "" + Ocl.sumString(_results_0) + s;
    }     
    return result;
  }

  public static String toTitleCase(String s)
  { String prev = " "; 
    int ind = 1;
    String res = ""; 
 
    while (ind <= s.length()) 
    { String chr = "" + s.charAt(ind-1); 
      if (prev.equals(" "))
      { res = res + chr.toUpperCase(); }
      else 
      { res = res + chr; } 
      prev = chr; 
      ind = ind + 1;
    } 

    return res; 
  }

  public static String swapCase(String s)
  { int ind = 1;
    String res = ""; 
 
    while (ind <= s.length()) 
    { String chr = "" + s.charAt(ind-1); 
      if (chr.equals(chr.toUpperCase()))
      { res = res + chr.toLowerCase(); }
      else 
      { res = res + chr.toUpperCase(); } 
      ind = ind + 1;
    } 

    return res; 
  }

  public static String format(String s, ArrayList sq)
  { Object[] args = new Object[sq.size()]; 
    for (int i = 0; i < sq.size(); i++) 
    { args[i] = sq.get(i); }
    String formattedString = String.format(s,args);  
    return formattedString; 
  } 

  public static ArrayList<Object> scan(String s, String fmt)
  { ArrayList<Object> result = new ArrayList<Object>();
    Scanner scanner = new Scanner(s);
    
    int ind = 0; // s upto ind has been consumed

    for (int i = 0; i < fmt.length(); i++) 
    { char c = fmt.charAt(i); 
      if (c == '%' && i < fmt.length() - 1)
      { char d = fmt.charAt(i+1); 
        if (d == 's') 
        { scanner = new Scanner(s.substring(ind)); 
          try { 
            String v = scanner.next(); 
            ind = ind + v.length(); 
            result.add(v); 
          } 
          catch (Exception _ex) { 
            _ex.printStackTrace(); 
          }  
          i++; 
        }
        else if (d == 'f')
        { String fchars = ""; 
          for (int j = ind; j < s.length(); j++) 
          { char x = s.charAt(j); 
            if (x == '.' || Character.isDigit(x))
            { fchars = fchars + x; } 
            else 
            { break; } 
          } 

          try { 
            double v = Double.parseDouble(fchars); 
            ind = ind + (v + "").length(); 
            result.add(v); 
          } 
          catch (Exception _ex) { 
            _ex.printStackTrace(); 
          }  
          i++;  
        }
        else if (d == 'd') 
        { String inchars = ""; 
          for (int j = ind; j < s.length(); j++) 
          { char x = s.charAt(j); 
            if (Character.isDigit(x))
            { inchars = inchars + x; } 
            else 
            { break; } 
          } 
          
          try { 
            int v = Integer.parseInt(inchars); 
            ind = ind + (v + "").length(); 
            result.add(v); 
          } 
          catch (Exception _ex) { 
            _ex.printStackTrace(); 
          }  
          i++;  
        }
      } 
      else if (s.charAt(ind) == c) 
      { ind++; } 
      else 
      { return result; }

      // System.out.println(result); 
      // System.out.println(s.substring(ind));   
    } 
    return result; 
  } 

  public static void main(String[] args)
  { // ArrayList res = StringLib.scan("100##3.3::20\n", "%d##%f::%d\n"); 
    // System.out.println(res); 

    System.out.println(StringLib.swapCase("A long String")); 
  }  
} 
