import java.util.List; 
import java.util.ArrayList; 
import java.util.Map; 
import java.util.Comparator; 
import java.util.Collections; 
import java.util.Vector; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 


public class AuxMath
{ static double meanx = 0; 
  static double meany = 0; 
  static double sumprods = 0; 
  static double sumdiffxsq = 0; 
  static double sumdiffysq = 0; 

  public static double to3dp(double val) 
  { int x = (int) Math.round(val*1000); 
    return x/1000.0; 
  } 

  public static String dequote(String x)
  { if (x.endsWith("\"") && x.startsWith("\""))
    { return x.substring(1,x.length()-1); } 
    return x; 
  } 

  public static String after(String s, String prefix)
  { // s = prefix + result 
    int plen = prefix.length(); 
	return s.substring(plen,s.length()); 
  }
  
  public static int gcd(int x, int y)
  { int l = x; 
    int k = y;  
    while (l != 0 && k != 0)
    { if (l == k) 
      { return l; } 

      if (l < k) 
      { k = k % l; } 
      else 
      { l = l % k; } 
    } 
    
    if (l == 0) 
    { return k; }
    else 
    { return l; }
  }       

  public static double numericSum(Vector xvect)
  { double sum = 0; 
    for (int j = 0; j < xvect.size(); j++) 
    { if (xvect.get(j) instanceof Double)
      { sum += ((Double) xvect.get(j)).doubleValue(); } 
      else if (xvect.get(j) instanceof Integer)
      { sum += ((Integer) xvect.get(j)).intValue(); } 
      else if (xvect.get(j) instanceof String) 
      { try 
        { double xx = Double.parseDouble((String) xvect.get(j)); 
          sum = sum+xx; 
        }
        catch (Exception _x) 
        { } 
      }
    }
    return sum; 
  } 

  public static String stringSum(Vector xvect)
  { String sum = ""; 
    for (int j = 0; j < xvect.size(); j++) 
    { sum = sum + dequote("" + xvect.get(j)); }
    return "\"" + sum + "\""; 
  } 

  public static double numericPrd(Vector xvect)
  { double sum = 1; 
    for (int j = 0; j < xvect.size(); j++) 
    { if (xvect.get(j) instanceof Double)
      { sum *= ((Double) xvect.get(j)).doubleValue(); } 
      else if (xvect.get(j) instanceof Integer)
      { sum *= ((Integer) xvect.get(j)).intValue(); } 
      else if (xvect.get(j) instanceof String) 
      { try 
        { double xx = Double.parseDouble((String) xvect.get(j)); 
          sum = sum*xx; 
        }
        catch (Exception _x) 
        { } 
      }
    }
    return sum; 
  } 

  public static double numericMin(Vector xvect)
  { double sum = 1.7976931348623157E308; 
    for (int j = 0; j < xvect.size(); j++) 
    { if (xvect.get(j) instanceof Double)
      { sum = Math.min(sum,((Double) xvect.get(j)).doubleValue()); } 
      else if (xvect.get(j) instanceof Integer)
      { sum = Math.min(sum,((Integer) xvect.get(j)).intValue()); } 
      else if (xvect.get(j) instanceof String) 
      { try 
        { double xx = Double.parseDouble((String) xvect.get(j)); 
          sum = Math.min(sum,xx); 
        }
        catch (Exception _x) 
        { } 
      }
    }
    return sum; 
  } 

  public static String stringMin(Vector xvect)
  { if (xvect.size() == 0) 
    { return null; } 

    String res = xvect.get(0) + ""; 
    for (int j = 1; j < xvect.size(); j++) 
    { String xx = "" + xvect.get(j); 
      if (xx.compareTo(res) < 0)
      { res = xx; }
    }
    return res; 
  } 

  public static String stringMax(Vector xvect)
  { if (xvect.size() == 0) 
    { return null; } 

    String res = xvect.get(0) + ""; 
    for (int j = 1; j < xvect.size(); j++) 
    { String xx = "" + xvect.get(j); 
      if (xx.compareTo(res) > 0)
      { res = xx; }
    }
    return res; 
  } 

  public static double numericMax(Vector xvect)
  { double sum = -1.7976931348623157E308; 
    for (int j = 0; j < xvect.size(); j++) 
    { if (xvect.get(j) instanceof Double)
      { sum = Math.max(sum,((Double) xvect.get(j)).doubleValue()); } 
      else if (xvect.get(j) instanceof Integer)
      { sum = Math.max(sum,((Integer) xvect.get(j)).intValue()); } 
      else if (xvect.get(j) instanceof String) 
      { try 
        { double xx = Double.parseDouble((String) xvect.get(j)); 
          sum = Math.max(sum,xx); 
        }
        catch (Exception _x) 
        { } 
      }
    }
    return sum; 
  } 

  public static Vector setUnion(Vector v1, Vector v2)
  { if (v1 == null) 
    { return v2; } 
    else if (v2 == null) 
    { return v1; } 

    Vector res = new Vector(); 
    
    for (int i = 0; i < v1.size(); i++)
    { Object obj = v1.get(i); 
      if (res.contains(obj)) { } 
      else 
      { res.add(obj); }
    } 

    for (int i = 0; i < v2.size(); i++)
    { Object obj = v2.get(i); 
      if (res.contains(obj)) { } 
      else 
      { res.add(obj); }
    }

    return res; 
  } 

  public static Vector subrange(Vector v, int st, int en)
  { Vector res = new Vector(); 

    if (v == null) 
    { return res; } 
    else if (st > en) 
    { return res; } 

    if (st < 1) 
    { st = 1; } 
    
    for (int i = st-1; i < en && i < v.size(); i++)
    { Object obj = v.get(i); 
      res.add(obj); 
    } 

    return res; 
  } 

  public static Vector tailSequences(Vector[] v)
  { Vector res = new Vector(); 
    
    for (int i = 0; i < v.length; i++)
    { Vector obj = v[i]; 
      Vector tobj = subrange(obj,2,obj.size());
      res.add(tobj); 
    } 

    return res; 
  } 

  public static Vector frontSequences(Vector[] v)
  { Vector res = new Vector(); 
    
    for (int i = 0; i < v.length; i++)
    { Vector obj = v[i]; 
      Vector fobj = subrange(obj,1,obj.size()-1);
      res.add(fobj); 
    } 

    return res; 
  } 

  public static boolean equalsSequence(Vector v1, Vector v2)
  { if (v1.size() != v2.size())
    { return false; } 

    for (int i = 0; i < v1.size(); i++) 
    { Object v1x = v1.get(i); 
      Object v2x = v2.get(i); 
      if (v1x == null && v2x == null) { } 
      else if (v1x != null && v1x.equals(v2x)) { } 
      else if (v2x != null && v2x.equals(v1x)) { } 
      else 
      { return false; } 
    } 
    return true; 
  } 

        
  public static boolean isFunctional(double[] xs, double[] ys) 
  { // For each x : xs, only one y : ys
    java.util.HashMap valueSets = new java.util.HashMap(); 

    for (int i = 0; i < xs.length; i++) 
    { Double xval = new Double(xs[i]); 
      java.util.HashSet yvals = (java.util.HashSet) valueSets.get(xval); 
      if (yvals == null) 
      { yvals = new java.util.HashSet(); } 
      yvals.add(new Double(ys[i])); 
      valueSets.put(xval,yvals); 
      if (yvals.size() > 1) 
      { return false; }
      // System.out.println(valueSets);  
    } 
    return true; 
  } 

  public static boolean isFunctional(boolean[] xs, String[] ys) 
  { // For each x : xs, only one y : ys
    java.util.HashMap valueSets = new java.util.HashMap(); 

    for (int i = 0; i < xs.length; i++) 
    { Boolean xval = new Boolean(xs[i]); 
      java.util.HashSet yvals = (java.util.HashSet) valueSets.get(xval); 
      if (yvals == null) 
      { yvals = new java.util.HashSet(); } 
      yvals.add(ys[i]); 
      valueSets.put(xval,yvals); 
      if (yvals.size() > 1) 
      { return false; }
      // System.out.println(valueSets);  
    } 
    return true; 
  } 

  public static boolean isFunctionalToSingletons(double[] xs, Vector[] ys) 
  { // For each x : xs, each ys.size() == 1, and unique ys for x

    java.util.HashMap valueSets = new java.util.HashMap(); 

    for (int i = 0; i < xs.length; i++) 
    { Double xval = new Double(xs[i]); 
      java.util.HashSet yvals = (java.util.HashSet) valueSets.get(xval); 
      if (yvals == null) 
      { yvals = new java.util.HashSet(); } 

      if (ys[i].size() != 1) 
      { return false; } 

      Object yy = ys[i].get(0); 

      yvals.add(yy); 
      valueSets.put(xval,yvals); 
      if (yvals.size() > 1) 
      { return false; }
      // System.out.println(valueSets);  
    } 
    return true; 
  } 

  public static boolean isFunctional(String[] xs, boolean[] ys) 
  { // For each x : xs, only one y : ys
    java.util.HashMap valueSets = new java.util.HashMap(); 

    for (int i = 0; i < xs.length; i++) 
    { String xval = xs[i];
 
      java.util.HashSet yvals = (java.util.HashSet) valueSets.get(xval); 

      if (yvals == null) 
      { yvals = new java.util.HashSet(); } 

      yvals.add(new Boolean(ys[i])); 
      valueSets.put(xval,yvals); 

      if (yvals.size() > 1) 
      { return false; }
      // System.out.println(valueSets);  
    } 
    return true; 
  } 

  public static boolean isFunctional(double[] xs, String[] ys) 
  { // For each x : xs, only one y : ys
    java.util.HashMap valueSets = new java.util.HashMap(); 

    for (int i = 0; i < xs.length; i++) 
    { Double xval = new Double(xs[i]); 
      java.util.HashSet yvals = (java.util.HashSet) valueSets.get(xval); 

      if (yvals == null) 
      { yvals = new java.util.HashSet(); } 
      yvals.add(ys[i]); 

      valueSets.put(xval,yvals); 

      if (yvals.size() > 1) 
      { return false; }
      // System.out.println(valueSets);  
    } 
    return true; 
  } 

    public static int modFunction(double[] xs, double[] ys) 
    { // the xs[i] - y[i] have a common divisor > 1
      int oldgcd = 0; 

      for (int i = 0; i < xs.length && i < ys.length; i++) 
      { double diff = xs[i] - ys[i]; 

        // System.out.println("!!! GCD with " + oldgcd + " " + diff); 

        if (diff >= 0) 
        { oldgcd = AuxMath.gcd(oldgcd, (int) diff); } 
        else 
        { return 1; } 

        if (oldgcd == 1)
        { return 1; } 
      }

      for (int j = 0; j < ys.length; j++)
      { if (ys[j] < oldgcd) { } 
        else 
        { return 1; } 
      }  
      return oldgcd; 
    } 

    public static int divFunction(double[] xs, double[] ys) 
    { // ys[i] = xs[i] div K
      double upperBound = Integer.MAX_VALUE; 
      double lowerBound = 0; 
	  
      for (int i = 0; i < xs.length && i < ys.length; i++) 
      { if (ys[i] == 0) 
        { lowerBound = Math.max(xs[i],lowerBound); } 
        else if (ys[i] > 0) 
        { double diff = xs[i]/ys[i]; 
          double diff1 = xs[i]/(ys[i] + 1); 
          upperBound = Math.min(upperBound,diff); 
          lowerBound = Math.max(lowerBound,diff1); 
          System.out.println(">> Upper bound = " + upperBound); 
          System.out.println(">> Lower bound = " + lowerBound); 
        } 
		else 
		{ return 0; } 

        if (lowerBound > upperBound)
        { return 0; } 
      }

      System.out.println(">> Upper bound = " + upperBound); 
      System.out.println(">> Lower bound = " + lowerBound); 
	  
	  int possK1 = (int) Math.floor(upperBound); 
	  int possK2 = (int) Math.ceil(lowerBound); 
	  
	  if (possK1 == possK2) 
	  { return possK1; }

	  for (int k = possK2; k <= possK1; k++)
	  { boolean validK = true; 
        for (int i = 0; i < xs.length && i < ys.length; i++) 
        { int xint = (int) xs[i]; 
          int yint = (int) ys[i]; 
          if (yint == xint/k) {}
          else 
          { validK = false; } 
        } 
        if (validK) { return k; }
      } 
	  
      return 0;   // search in the range for a valid K. 
    } 

  public static boolean isFunctional(String[] xs, String[] ys) 
  { // For each x : xs, only one y : ys
    java.util.HashMap valueSets = new java.util.HashMap(); 

    for (int i = 0; i < xs.length; i++) 
    { java.util.HashSet yvals = (java.util.HashSet) valueSets.get(xs[i]); 
      if (yvals == null) 
      { yvals = new java.util.HashSet(); } 
      yvals.add(ys[i]); 
      valueSets.put(xs[i],yvals); 
      if (yvals.size() > 1) 
      { return false; }
      // System.out.println(valueSets);  
    } 
    return true; 
  } 

  public static boolean isIdentity(String[] xs, String[] ys) 
  { // Each xs[i] = ys[i]

    if (xs.length != ys.length) 
    { return false; } 

    for (int i = 0; i < xs.length; i++) 
    {  
      if (xs[i] == null) 
      { return false; } 
      if (ys[i] == null)
      { return false; }

      if (xs[i].equals(ys[i])) { } 
      else 
      { return false; } 
    } 
    return true; 
  } 

    public static boolean isConstant(double[] ys)
    { if (ys.length > 1)
	 { double y0 = ys[0]; 
         for (int i = 1; i < ys.length; i++)
	    { if (ys[i] == y0) { }
		 else 
	      { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isConstant(String[] ys)
    { if (ys.length > 1)
	 { String y0 = ys[0]; 
	   for (int i = 1; i < ys.length; i++)
        { if (y0.equals(ys[i])) { }
	     else 
		{ return false; }
        }
	   return true; 
	 } 
	 return false; 
    }

    public static boolean isConstant(boolean[] ys)
    { if (ys.length > 1)
	 { boolean y0 = ys[0]; 
         for (int i = 1; i < ys.length; i++)
	    { if (ys[i] == y0) { }
		 else 
	      { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isConstant(ObjectSpecification[] ys)
    { if (ys.length > 1)
	 { ObjectSpecification y0 = ys[0]; 
         for (int i = 1; i < ys.length; i++)
	    { if (ys[i] == y0) { }
		 else 
	      { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isConstantSets(Vector[] ys)
    { if (ys.length > 1)
	 { Vector y0 = ys[0]; 
         for (int i = 1; i < ys.length; i++)
         { if (ys[i] == null && y0 == null) 
           { }
           else if (ys[i] != null && y0 != null && 
                    ys[i].containsAll(y0) && 
                    y0.containsAll(ys[i]))
           { }  // Assuming they are sets.   
           else { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isConstantSequences(Vector[] ys)
    { if (ys.length > 1)
	 { Vector y0 = ys[0]; 
         for (int i = 1; i < ys.length; i++)
         { if (ys[i] == null && y0 == null) { }
           else if (ys[i] != null && y0 != null && 
                    equalsSequence(ys[i],y0))
           { }  // Assuming they are sequences.   
           else 
           { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean allDifferent(double[] ys)
    { if (ys.length > 1)
      { java.util.HashSet values = new java.util.HashSet(); 
        for (int i = 0; i < ys.length; i++)
        { values.add(new Double(ys[i])); }
        if (values.size() < ys.length)
        { return false; } 
        return true; 
      } 
      return false; 
    }

    public static boolean allDifferent(String[] ys)
    { if (ys.length > 1)
      { java.util.HashSet values = new java.util.HashSet(); 
        for (int i = 0; i < ys.length; i++)
        { values.add(ys[i]); }
        if (values.size() < ys.length)
        { return false; } 
        return true; 
      } 
      return false; 
    }

    public static boolean allDifferent(ObjectSpecification[] ys)
    { if (ys.length > 1)
      { java.util.HashSet values = new java.util.HashSet(); 
        for (int i = 0; i < ys.length; i++)
        { if (ys[i] != null)
		  { values.add(ys[i].getName()); }
		} 
		
        if (values.size() < ys.length)
        { return false; } 
        return true; 
      } 
      return false; 
    }

    // Version for sets: 
    public static boolean allDifferentSets(Vector[] ys)
    { if (ys.length > 1)
      { java.util.HashSet values = new java.util.HashSet(); 
        for (int i = 0; i < ys.length; i++)
        { java.util.HashSet value = new java.util.HashSet(); 
          value.addAll(ys[i]);
          values.add(value); 
        }
        if (values.size() < ys.length)
        { return false; } 
        return true; 
      } 
      return false; 
    }

    // Version for sets: 
    public static boolean allDifferentSequences(Vector[] ys)
    { if (ys.length > 1)
      { java.util.HashSet values = new java.util.HashSet(); 
        for (int i = 0; i < ys.length; i++)
        { java.util.ArrayList value = new java.util.ArrayList(); 
          value.addAll(ys[i]);
          values.add(value); 
        }

        if (values.size() < ys.length)
        { return false; } 

        return true; 
      } 
      return false; 
    }

    public static boolean isNegation(boolean[] xs, boolean[] ys)
    { if (ys.length == xs.length)
	 { for (int i = 0; i < xs.length; i++)
	   { if (ys[i] == !(xs[i])) { }
		else 
	     { return false; }
	   }
	   return true; 
	 } 
	 return false; 
    }

    public static boolean isCopy(double[] xs, double[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
	 { for (int i = 0; i < xs.length; i++)
         { if (xs[i] == ys[i]) { }
           else 
           { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isCopy(double[] xs, Vector[] ys)
    { // Each ys[i] is singleton of xs[i]

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector yval = ys[i];

          // System.err.println(yval + " =? " + xs[i]); 
 
          if (yval.size() == 1)
          { Double dd; 
            if (yval.get(0) instanceof Double)
            { dd = (Double) yval.get(0); } 
            else 
            { dd = new Double(Double.parseDouble(yval.get(0) + "")); } 
 
            if (((1.0*xs[i]) + "").equals((1.0*dd.doubleValue()) + ""))
            { }
            else 
            { return false; } 
          }  
          else 
          { return false; }
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isCopy(String[] xs, Vector[] ys)
    { // Each ys[i] is singleton of xs[i]

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector yval = ys[i];

          System.err.println("???? " + yval + " =? " + xs[i]); 
 
          if (yval.size() == 1)
          { String dd = yval.get(0) + ""; 
            if (xs[i] != null && 
                ("\"" + xs[i] + "\"").equals(dd))
            { } 
            else 
            { return false; }
          } 
          else 
          { return false; }
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isNumericSum(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof Double) 
          { double dd = ((Double) yvect.get(0)).doubleValue(); 
            double sum = 0; 
            for (int j = 0; j < xvect.size(); j++) 
            { if (xvect.get(j) instanceof String) 
              { try 
                { double xx = Double.parseDouble((String) xvect.get(j)); 
                  sum = sum+xx; 
                }
                catch (Exception _x) 
                { return false; } 
              }
            }
 
            if (sum == dd) 
            { System.out.println(">>> Numeric sum of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isNumericSum(double[] xs, double[] xs1, double[] ys)
    { for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++)
      { double xval = xs[i]; 
        double xval1 = xs1[i];
        double yval = to3dp(ys[i]);  
        if (to3dp(xval + xval1) == yval) 
        {  }
        else 
        { return false; }
      } 
      return true; 
    }

    public static boolean isNumericSubtraction(double[] xs, double[] xs1, double[] ys)
    { for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++)
      { double xval = xs[i]; 
        double xval1 = xs1[i];
        double yval = to3dp(ys[i]);  
        if (to3dp(xval - xval1) == yval) 
        {  }
        else 
        { return false; }
      } 
      return true; 
    }

    public static boolean isNumericProduct(double[] xs, double[] xs1, double[] ys)
    { for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++)
      { double xval = xs[i]; 
        double xval1 = xs1[i];
        double yval = to3dp(ys[i]);  
        if (to3dp(xval * xval1) == yval) 
        {  }
        else 
        { return false; }
      } 
      return true; 
    }

    public static boolean isNumericDivision(double[] xs, double[] xs1, double[] ys)
    { for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++)
      { double xval = xs[i]; 
        double xval1 = xs1[i];
        double yval = to3dp(ys[i]);  
        if (xval1 != 0 && to3dp(xval / xval1) == yval) 
        {  }
        else 
        { return false; }
      } 
      return true; 
    }

    public static boolean isNumericPrd(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof Double) 
          { double dd = ((Double) yvect.get(0)).doubleValue(); 
            double sum = 1; 
            for (int j = 0; j < xvect.size(); j++) 
            { if (xvect.get(j) instanceof String) 
              { try 
                { double xx = Double.parseDouble((String) xvect.get(j)); 
                  sum = sum*xx; 
                }
                catch (Exception _x) 
				{ return false; } 
              }
            }
            if (sum == dd) 
            { System.out.println(">>> Numeric product of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isCopy(String[] xs, String[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { if (xs[i].equals(ys[i])) { }
	      else 
          { return false; }
        }
	    return true; 
	  } 
      return false; 
    }

    public static boolean isStringSum(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof String) 
          { String dd = "\"" + yvect.get(0) + "\""; 
            String sum = ""; 
            for (int j = 0; j < xvect.size(); j++) 
            { sum = sum + "" + xvect.get(j); }
   
            if (sum.equals(dd)) 
            { System.out.println(">>> String sum of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isStringSum(Vector[] xs, String[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          String dd = ys[i]; 
          String sum = ""; 
          for (int j = 0; j < xvect.size(); j++) 
          { sum = sum + "" + dequote((String) xvect.get(j)); }
   
          if (sum.equals(dd)) 
          { System.out.println(">>> String sum of " + xvect + " = " + dd); } 
          else 
          { return false; } 
        }
      } 
      return true; 
    }

    public static boolean hasInitialStringSum(Vector[] xs, String[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          String dd = ys[i]; 
          String sum = ""; 
          for (int j = 0; j < xvect.size(); j++) 
          { sum = sum + "" + dequote((String) xvect.get(j)); }
   
          if (dd.startsWith(sum)) 
          { System.out.println(">>> " + xvect + "->sum() + ?? = " + dd); } 
          else 
          { return false; } 
        }
      } 
      return true; 
    }

    public static String separatorStringSum(Vector[] xs, String[] ys)
    { // Try to find a consistent separator K such that 
      // each xs[i][0] + K + ... + K + xs[i][p] = ys[i]
      // where p = xs[i].size()-1
      // Returns K if such is found, otherwise null
      
      String K = null; 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          String dd = ys[i]; 
          String sum = ""; 

          if (xvect.size() == 0) 
          { if (dd.equals("")) { } 
            else 
            { return null; } 
          } 
          else if (xvect.size() == 1) 
          { sum = dequote((String) xvect.get(0));  
            if (dd.equals(sum)) { } 
            else 
            { return null; } 
          } 
          else
          { String fst = "" + dequote((String) xvect.get(0));
            if (dd.startsWith(fst)) 
            { String rem = dd.substring(fst.length(),dd.length()); 
              String scnd = "" + dequote((String) xvect.get(1)); 
              int kend = rem.indexOf(scnd); 
              if (kend < 0)
              { return null; } 
              String k2 = rem.substring(0,kend); 
              System.out.println(">> Found separator " + k2); 
              if (K != null && K.equals(k2)) { } 
              else if (K == null) 
              { K = k2; } 
              else 
              { return null; } // inconsistent separators
            } 
          }
        }
        
        if (K == null) { return null; }
		
		// Check that K is ok for the whole list: 
		
        for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          String dd = ys[i]; 
          String sum = ""; 
 
          if (xvect.size() > 1)
          { sum = dequote((String) xvect.get(0)); 
		  
            for (int j = 1; j < xvect.size(); j++) 
            { sum = sum + K + dequote((String) xvect.get(j)); }
   
            if (sum.equals(dd)) 
            { System.out.println(">>> Separator string sum of " + xvect + " and " + K + " = " + dd); } 
            else 
            { return null; } 
          }
        }
      } 
      return K; 
    }

    public static String initialSeparatorStringSum(Vector[] xs, String[] ys, String[] rems)
    { // Try to find a consistent separator K such that 
      // each xs[i][0] + K + ... + K + xs[i][p] + rems[i] = ys[i]
      // where p = xs[i].size()-1
      // Returns K if such is found, otherwise null
      
      String K = null; 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          String dd = ys[i]; 
          String sum = ""; 

          if (xvect.size() == 0) 
          { // if (dd.equals("")) { } 
            // else 
            // { return null; } 
		 rems[i] = dd; 
          } 
          else if (xvect.size() == 1) 
          { sum = dequote((String) xvect.get(0));  
            if (dd.startsWith(sum)) 
            { rems[i] = after(dd,sum); } 
            else 
            { return null; } 
          } 
          else
          { String fst = "" + dequote((String) xvect.get(0));
            if (dd.startsWith(fst)) 
            { String rem = dd.substring(fst.length(),dd.length()); 
              String scnd = "" + dequote((String) xvect.get(1)); 
              int kend = rem.indexOf(scnd); 
              if (kend < 0)
              { return null; } 
              String k2 = rem.substring(0,kend); 
              System.out.println(">> Found separator " + k2); 
              if (K != null && K.equals(k2)) { } 
              else if (K == null) 
              { K = k2; } 
              else 
              { return null; } // inconsistent separators
            } 
          }
        }
		
        if (K == null) { return null; }
		
		// Check that K is ok for the whole list: 
		
        for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          String dd = ys[i]; 
          String sum = ""; 
 
          if (xvect.size() > 1)
          { sum = dequote((String) xvect.get(0)); 
		  
            for (int j = 1; j < xvect.size(); j++) 
            { sum = sum + K + dequote((String) xvect.get(j)); }
   
            if (dd.startsWith(sum)) 
            { System.out.println(">>> " + xvect + "->separatorSum(" + K + ") + ?? = " + dd); 
              rems[i] = after(dd,sum); 
            } 
            else 
            { return null; } 
          }
        }
      } 
      // System.out.println(">> remainders = " + rems); 
      return K; 
    }

    public static boolean isStringMax(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof String) 
          { String dd = "\"" + yvect.get(0) + "\""; 
            String maxstring = ""; 
            for (int j = 0; j < xvect.size(); j++) 
            { // System.out.println(">>> xvect(j) = " + xvect.get(j) + " " + maxstring + " " + dd); 
              if (xvect.get(j) instanceof String)
			  { String vs = (String) xvect.get(j); 
			    if (maxstring.compareTo(vs) < 0)
			    { maxstring = vs; }
			  } 
			  else 
			  { return false; }
            }
			
            if (maxstring.equals(dd)) 
            { System.out.println(">>> String max of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isStringMin(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof String) 
          { String dd = "\"" + yvect.get(0) + "\""; 
            String minstring = ""; 
            for (int j = 0; j < xvect.size(); j++) 
            { // System.out.println(">>> xvect(j) = " + xvect.get(j) + " " + maxstring + " " + dd); 
              if (xvect.get(j) instanceof String)
              { String vs = (String) xvect.get(j); 
                if (0 < minstring.compareTo(vs))
                { minstring = vs; }
              } 
              else 
              { return false; }
            }
			
            if (minstring.equals(dd)) 
            { System.out.println(">>> String min of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isNumericMax(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof Double) 
          { double dd = ((Double) yvect.get(0)).doubleValue(); 
            double maxd = -1.7976931348623157E308; 
            for (int j = 0; j < xvect.size(); j++) 
            { // System.out.println(">>> xvect(j) = " + xvect.get(j) + " " + maxstring + " " + dd); 
			  if (xvect.get(j) instanceof String) 
              { try 
                { double xx = Double.parseDouble((String) xvect.get(j)); 
                  maxd = Math.max(maxd,xx); 
                }
                catch (Exception _x) 
			{ return false; } 
              }
		  else 
		  { return false; }
            }
			
            if (maxd == dd) 
            { System.out.println(">>> Numeric max of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isNumericMin(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof Double) 
          { double dd = ((Double) yvect.get(0)).doubleValue(); 
            double maxd = 1.7976931348623157E308; 
            for (int j = 0; j < xvect.size(); j++) 
            { // System.out.println(">>> xvect(j) = " + xvect.get(j) + " " + maxstring + " " + dd); 
			  if (xvect.get(j) instanceof String) 
              { try 
                { double xx = Double.parseDouble((String) xvect.get(j)); 
                  maxd = Math.min(maxd,xx); 
                }
                catch (Exception _x) 
				{ return false; } 
              }
			  else 
			  { return false; }
            }
			
            if (maxd == dd) 
            { System.out.println(">>> Numeric min of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isNumericAverage(Vector[] xs, Vector[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 
          if (yvect.size() == 1 && yvect.get(0) instanceof Double) 
          { double dd = ((Double) yvect.get(0)).doubleValue();
		    double dd3dp = to3dp(dd); 
			 
            double averg = 0.0;
			int xsize = xvect.size();  
            for (int j = 0; j < xsize; j++) 
            { // System.out.println(">>> xvect(j) = " + xvect.get(j) + " " + averg + " " + dd); 
			  if (xvect.get(j) instanceof String) 
              { try 
                { double xx = Double.parseDouble((String) xvect.get(j)); 
                  averg = averg + xx/((double) xsize); 
                }
                catch (Exception _x) 
				{ return false; } 
              }
			  else if (xvect.get(j) instanceof Double)
			  { try
			    { double xx = ((Double) xvect.get(j)).doubleValue(); 
				  averg = averg + xx/((double) xsize); 
                }
                catch (Exception _x) 
				{ return false; }
			  }
			  else
			  { return false; }
            }
			
            if (to3dp(averg) == dd3dp) 
            { System.out.println(">>> To 3 decimal places, numeric average of " + xvect + " = " + dd); } 
            else 
            { return false; } 
          } 
        }
      } 
      return true; 
    }

    public static boolean isCopy(boolean[] xs, boolean[] ys)
    { if (ys.length > 1 && xs.length == ys.length)
	 { for (int i = 0; i < xs.length; i++)
	    { if (xs[i] == ys[i]) { }
		 else 
	      { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isCopy(ObjectSpecification[] xs, ObjectSpecification[] ys, ModelSpecification mod)
    { if (ys.length > 1 && xs.length == ys.length)
	 { for (int i = 0; i < xs.length; i++)
	   { if (mod.correspondence.getAll(xs[i]).contains(ys[i])) { }
		 else 
	      { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isCopySets(Vector[] xs, Vector[] ys, ModelSpecification mod)
    { // Assume they are both sets

       if (ys.length > 1 && xs.length == ys.length)
       { for (int i = 0; i < xs.length; i++)
         { Vector xvect = xs[i]; 
           Vector yvect = ys[i]; 

           if (mod.correspondingObjectSets(xvect,yvect)) { }
           else { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean isCopySequences(Vector[] xs, Vector[] ys, ModelSpecification mod)
    { // Assume they both represent sequences

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { Vector xvect = xs[i]; 
          Vector yvect = ys[i]; 

          if (mod.correspondingObjectSequences(xvect,yvect)) { }
          else { return false; }
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isSingletonSequences(ObjectSpecification[] xs, Vector[] ys, ModelSpecification mod)
    { // Is each ys[i] = Sequence{ xs[i]' } ? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ObjectSpecification xx = xs[i]; 
          Vector yvect = ys[i]; 

          if (yvect.size() == 1) 
          { ObjectSpecification yy = (ObjectSpecification) yvect.get(0); 
            if (mod.correspondingObjects(xx,yy)) { }
            else 
            { return false; }
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isSingletonSequences(String[] xs, Vector[] ys, ModelSpecification mod)
    { // Is each ys[i] = Sequence{ xs[i] } ? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { String xx = xs[i]; 
          Vector yvect = ys[i]; 

          if (yvect.size() == 1) 
          { String yy = (String) yvect.get(0); 
            if (xx != null && yy != null && xx.equals(yy)) 
            { }
            else 
            { return false; }
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isPrependSequences(ObjectSpecification[] xs, Vector[] ys, ModelSpecification mod)
    { // Is each ys[i] = Sequence{ xs[i]' } ^ something ? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ObjectSpecification xx = xs[i]; 
          Vector yvect = ys[i]; 

          if (yvect.size() >= 1) 
          { ObjectSpecification yy = (ObjectSpecification) yvect.get(0); 
            if (mod.correspondingObjects(xx,yy)) 
            { }
            else 
            { return false; }
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isPrependSequences(String[] xs, Vector[] ys, ModelSpecification mod)
    { // Is each ys[i] = Sequence{ xs[i] } ^ something ? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { String xx = xs[i]; 
          Vector yvect = ys[i]; 

          if (yvect.size() >= 1) 
          { String yy = (String) yvect.get(0); 
            if (xx != null && yy != null && xx.equals(yy)) 
            { }
            else 
            { return false; }
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isAppendSequences(ObjectSpecification[] xs, Vector[] ys, ModelSpecification mod)
    { // Is each ys[i] = something ^ Sequence{ xs[i]' }  ? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ObjectSpecification xx = xs[i]; 
          Vector yvect = ys[i]; 

          if (yvect.size() >= 1) 
          { ObjectSpecification yy = (ObjectSpecification) yvect.get(yvect.size()-1); 
            if (mod.correspondingObjects(xx,yy)) 
            { }
            else 
            { return false; }
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isAppendSequences(String[] xs, Vector[] ys, ModelSpecification mod)
    { // Is each ys[i] = something ^ Sequence{ xs[i] }  ? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { String xx = xs[i]; 
          Vector yvect = ys[i]; 

          if (yvect.size() >= 1) 
          { String yy = (String) yvect.get(yvect.size()-1); 
            if (xx != null && yy != null && xx.equals(yy)) 
            { }
            else 
            { return false; }
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

    public static boolean allSubsets(Vector[] xs, Vector[] ys, ModelSpecification mod)
    { // Each ys[i] is superset of xs[i]'

       if (ys.length > 1 && xs.length == ys.length)
       { for (int i = 0; i < xs.length; i++)
         { Vector xvect = xs[i]; 
           Vector yvect = ys[i]; 

           if (mod.correspondingObjectSubset(xvect,yvect) != null) { }
           else { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}

    public static boolean allIncludes(Vector[] ys, Type yelementType, ObjectSpecification[] xs, ModelSpecification mod)
    { // Each ys[i] includes 
      // xs[i]'->intersection(yelementType.allInstances())

       if (ys.length > 1 && xs.length == ys.length)
       { for (int i = 0; i < xs.length; i++)
         { ObjectSpecification xx = xs[i]; 
           Vector yvect = ys[i]; 

           Vector xobjs = new Vector(); 
           xobjs.add(xx); // for non-objects
 
           if (yelementType != null && yelementType.isEntity())
           { Entity tElem = yelementType.getEntity(); 
             xobjs = mod.getCorrespondingObjects(xx,tElem);
           }  
           
           System.out.println(">>> Checking if " + xobjs + " <: " + yvect); 

           if (yvect.containsAll(xobjs)) { }
           else 
           { return false; }
	    }
	    return true; 
	  } 
	  return false; 
	}
	
    public static boolean isConstantSequence(Vector[] ys)
    { if (ys.length > 1)
	 { Vector y0 = ys[0]; 
	   for (int i = 1; i < ys.length; i++)
	   { if (equalsSequence(y0,ys[i])) { }
		else 
		{ return false; }
	   }
	   return true; 
	 } 
	 return false; 
    }

    public static boolean isConstantSeq(Vector ys)
    { if (ys.size() > 0)
      { Object y0 = ys.get(0);
        if (y0 == null) 
        { return false; } 
 
        for (int i = 1; i < ys.size(); i++)
        { if (y0.equals(ys.get(i))) { }
          else 
          { return false; }
        }
        return true; 
      } 
      return false; 
    }

    public static boolean isConstantSet(Vector[] ys)
    { if (ys.length > 1)
	 { Vector y0 = ys[0]; 
	   for (int i = 1; i < ys.length; i++)
	   { if (y0.containsAll(ys[i]) && ys[i].containsAll(y0)) { }
		else 
		{ return false; }
	   }
	   return true; 
	 } 
	 return false; 
    }

    public static boolean allSubsets(Vector[] xs, Vector[] ys)
    { for (int i = 0; i < xs.length && i < ys.length; i++)
      { Vector xval = xs[i]; 
        Vector yval = ys[i]; 
        if (yval.containsAll(xval)) { } 
        else { return false; }  
	 }
	 return true; 
     }
	
	public static double isExponential(double[] xs, double[] ys)
	{ // check if e^xs is linear with ys
	  double[] exs = new double[xs.length]; 
	  double[] eys = new double[ys.length]; 
	  for (int i = 0; i < xs.length; i++)
	  { // if (ys[i] <= 0) { return 0; } 
	    // eys[i] = Math.log(ys[i]);
         exs[i] = Math.exp(xs[i]);  
	  }
	  
	  return linearCorrelation(exs,ys); 
	}

    public static void exponentialRelationship(String s, String t)
    { double slope = AuxMath.linearSlope(); 
      double offset = AuxMath.linearOffset(); 
      System.out.println(slope + "*e->pow(" + s + ") + " + offset + " |--> " + t); 
    }  
	
    public static double mean(double[] xs)
    { double res = 0; 
      for (int i = 0; i < xs.length; i++) 
      { res = res + xs[i]; }
      return res/xs.length; 
    } 

    public static double linearCorrelation(double[] xs, double[] ys)
    { if (ys.length < 3)
      { System.err.println("Need 3 or more points to analyse linear correlation"); 
	    return 0; 
      }
	  
      meanx = mean(xs); 
      meany = mean(ys); 

      sumprods = 0; 
      sumdiffxsq = 0; 
      sumdiffysq = 0; 

      for (int i = 0; i < xs.length && i < ys.length; i++) 
      { double diffx = xs[i] - meanx; 
        double diffy = ys[i] - meany;

        sumprods = sumprods + diffx*diffy; 
        sumdiffxsq = sumdiffxsq + diffx*diffx; 
        sumdiffysq = sumdiffysq + diffy*diffy; 
      }
      // System.out.println("sumprods = " + sumprods); 
      // System.out.println("xsq = " + sumdiffxsq); 
      // System.out.println("ysq = " + sumdiffysq); 
	  
      if (sumdiffxsq == 0 || sumdiffysq == 0) { return 0; }

      // double slope = sumprods/sumdiffxsq; 
      // System.out.println("Slope = " + slope); 
      // System.out.println("Offset = " + (meany - slope*meanx)); 
      return sumprods/Math.sqrt(sumdiffxsq*sumdiffysq); 
    } 

    public static double linearSlope()
    { if (sumdiffxsq != 0) 
      { return sumprods/sumdiffxsq; }
      return 0; 
    }  

    public static double linearOffset()
    { if (sumdiffxsq != 0) 
      { double slope = sumprods/sumdiffxsq; 
        return meany - slope*meanx;
      } 
      return 0;  
    } 
    	
    public static boolean slopes(double[] ys)
    { // y values in order of increasing x
    
      if (ys.length < 3)
      { System.err.println("Need 3 or more points to analyse slopes"); 
	    return false; 
	  }
      
      int endpoint = ys.length-2; 
      int maxcount = 0; 
      int mincount = 0; 
      		
      double[] diffs = new double[ys.length-1];
      for (int i = 0; i < ys.length-1; i++)
      { diffs[i] = ys[i+1] - ys[i]; }
      
      for (int j = 1; j < diffs.length; j++)
      { if (diffs[j-1] < 0 && diffs[j] > 0)
        { mincount++; } 
        else if (j+1 < diffs.length && diffs[j-1] < 0 && diffs[j] == 0 && diffs[j+1] > 0)
        { mincount++; } 
        else if (diffs[j-1] > 0 && diffs[j] < 0)
        { maxcount++; } 
        else if (j+1 < diffs.length && diffs[j-1] > 0 && diffs[j] == 0 && diffs[j+1] < 0)
        { maxcount++; } 
      }	
      System.out.println(">> maxima = " + maxcount); 
      System.out.println(">> minima = " + mincount);
      
      if (maxcount == 1 && mincount == 0 && diffs[0] > 0 && diffs[endpoint] < 0)
      { System.out.println("Could be -ve quadratic"); 
	    return true; 
      } 
      else if (mincount == 1 && maxcount == 0 && diffs[0] < 0 && diffs[endpoint] > 0)
      { System.out.println("Could be +ve quadratic");
	    return true; 
      }
      else if (mincount == 1 && maxcount == 1 & diffs[0] < 0 && diffs[endpoint] < 0)
      { System.out.println("Could be -ve cubic");
	    return true; 
      }
      else if (mincount == 1 && maxcount == 1 && diffs[0] > 0 && diffs[endpoint] > 0)
      { System.out.println("Could be +ve cubic");
	    return true; 
      }
      else if (mincount == 0 && maxcount == 0)
      { // analyse the slope to see if polynomial or exponential 
        System.out.println("Increasing/decreasing function with no minima/maxima"); 
      }	
      else if (mincount > 1 && maxcount > 1) 
      { System.out.println("Multiple maxima and minima, could be trignometric"); } 

	 return false;  
    }
    
    public static boolean quadraticRelationship(double[] xs, double[] ys, 
	                                            String s, String t)
    { double[] xsq = new double[xs.length]; 
      for (int i = 0; i < xs.length; i++) 
      { xsq[i] = xs[i]*xs[i]; } 
      
      if (xs.length < 3)
      { System.out.println("ERROR: too few points to evaluate quadratic relationship -- need 3 datapoints"); 
      	return false; 
      }	
      	
      boolean isOk = true; 
      	
      java.util.Set alphas = new java.util.HashSet(); 
      java.util.Set betas = new java.util.HashSet(); 
      double alpha = 0; 
	 double beta = 0; 
	 double gamma = 0; 
	  	
      for (int i = 0; i+2 < xs.length && i+2 < ys.length; i++)
      { double x1 = xs[i]; 
        double x1sq = xsq[i]; 
        double y1 = ys[i]; 

        double x2 = xs[i+1]; 
        double x2sq = xsq[i+1]; 
        double y2 = ys[i+1]; 

        double x3 = xs[i+2]; 
        double x3sq = xsq[i+2]; 
        double y3 = ys[i+2];
      
        double divisor = (x1sq - x2sq)*(x3 - x2) + (x2sq - x3sq)*(x1 - x2);
        // System.out.println("Divisor: " + divisor);
        if (divisor == 0)
        { System.out.println("This is not quadratic");
          return false; 
        }
        
        alpha = to3dp(((y1 - y2)*(x3 - x2) + (y2 - y3)*(x1 - x2))/divisor); 
        beta = -to3dp(((y1 - y2)*(x3sq - x2sq) + (y2 - y3)*(x1sq - x2sq))/divisor); 
        gamma = y1 - alpha*x1sq - beta*x1; 
        alphas.add(new Double(alpha));
	   betas.add(new Double(beta));  
        System.out.println("alpha = " + alpha + " beta = " + beta);  				   
      }
	  
	 if (alphas.size() == 1 && betas.size() == 1)	
      { System.out.println("Consistent with quadratic function.\n" + 
                             "Mapping is " + alpha + "*" + s + "*" + s + " + " +  
		                  beta + "*" + s + " + " + gamma + " |--> " + t); 
        System.out.println(); 
      }
      return (alphas.size() == 1 && betas.size() == 1); 
      	
      // For groups of 3 successive points, solve the quadratic equation
    }

   public static boolean isUpperCased(String[] xs, String[] ys) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.equals(xval.toUpperCase())) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isLowerCased(String[] xs, String[] ys) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.equals(xval.toLowerCase())) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isPrefixed(String[] xs, String[] ys) 
   { // Each ys[i] = something + xs[i]

     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.endsWith(xval)) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static String commonPrefix(String[] xs, String[] ys) 
   { // ys[i] = result + xs[i] each i

     java.util.HashSet prefixes = new java.util.HashSet(); 
     String prefix = ""; 
	 
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.endsWith(xval)) 
       { int j = yval.lastIndexOf(xval); 
         prefix = yval.substring(0,j); 
		 // System.out.println(prefix); 
         prefixes.add(prefix); 
       } 
       else 
       { return null; } 
     } 
	 
     if (prefixes.size() == 1)
     { return prefix; } 
     return null;  
   } 

   public static boolean allPrefixed(String[] xs, String[] ys) 
   { for (int i = 0; i < xs.length; i++) 
     { for (int j = 0; j < ys.length; j++) 
       { String xval = xs[i]; 
         String yval = ys[j]; 
         // System.out.println(">>> Testing prefix: " + xs[i] + " " + ys[j]); 
         if (yval.endsWith(xval)) { } 
         else 
         { return false; }
       }  
     } 
     return true; 
   } 

   public static Vector getPrefixes(String[] xs, String[] ys) 
   { Vector res = new Vector(); 
     for (int i = 0; i < xs.length; i++) 
     { for (int j = 0; j < ys.length; j++) 
       { String xval = xs[i]; 
         String yval = ys[j];
         int lenx = xval.length(); 
         String prefix = yval.substring(0,yval.length() - lenx);  
         if (res.contains(prefix)) { } 
         else 
         { res.add(prefix); }
       }  
     } 
     return res; 
   } 

   public static Vector allPrefixes(String[] xs, String[] ys) 
   { Vector res = new Vector(); 
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i];
       int lenx = xval.length(); 
       String prefix = yval.substring(0,yval.length() - lenx);  
       if (res.contains(prefix)) { } 
       else 
       { res.add(prefix); }
     } 
     return res; 
   } 

   public static String longestCommonPrefix(Vector strs)
   { // each of the s : strs has result as prefix & 
     // no longer common prefix
     String res = ""; 

     if (strs.size() < 1) 
     { return res; } 

     res = (String) strs.get(0); 
     for (int i = 1; i < strs.size(); i++) 
     { res = ModelElement.longestCommonPrefix(res,(String) strs.get(i),0); } 

     return res; 
   } 

   public static Vector removeCommonPrefix(Vector strs, String prefix)
   { // each of the s : strs is prefix + something
     Vector res = new Vector(); 
     int plen = prefix.length(); 

     if (strs.size() < 1) 
     { return res; } 

     for (int i = 0; i < strs.size(); i++) 
     { String str = (String) strs.get(i); 
       int slen = str.length(); 
       res.add(str.substring(plen,slen)); 
     } 

     return res; 
   } 

   public static Vector removePrefix(String[] strs, String[]  prefs)
   { // each strs[i] is prefs[i] + something

     Vector res = new Vector(); 

     if (strs.length < 1) 
     { return res; } 

     for (int i = 0; i < strs.length && i < prefs.length; i++) 
     { String str = strs[i]; 
       int slen = str.length(); 
       int plen = prefs[i].length();
       String remp = str.substring(plen,slen); 
       System.out.println(str + " - prefix " + prefs[i] + " is: " + remp);  
       res.add(remp); 
     } 

     return res; 
   } 

   public static boolean isSuffixed(String[] xs, String[] ys) 
   { // Each ys[i] starts with xs[i]

     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.startsWith(xval)) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean allSuffixed(String[] xs, String[] ys) 
   { for (int i = 0; i < xs.length; i++) 
     { for (int j = 0; j < ys.length; j++) 
       { String xval = xs[i]; 
         String yval = ys[j]; 
         // System.out.println(">>> Testing suffix: " + xs[i] + " " + ys[j]); 
         if (yval.startsWith(xval)) { } 
         else 
         { return false; }
       }  
     } 
     return true; 
   } 

   public static Vector getSuffixes(String[] xs, String[] ys) 
   { Vector res = new Vector(); 
     for (int i = 0; i < xs.length; i++) 
     { for (int j = 0; j < ys.length; j++) 
       { String xval = xs[i]; 
         String yval = ys[j];
         int lenx = xval.length(); 
         String suffix = yval.substring(lenx,yval.length());  
         if (res.contains(suffix)) { } 
         else 
         { res.add(suffix); }
       }  
     } 
     return res; 
   } 

   public static Vector allSuffixes(String[] xs, String[] ys) 
   { Vector res = new Vector(); 
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i];
       int lenx = xval.length(); 
       String suffix = yval.substring(lenx,yval.length());  
       if (res.contains(suffix)) { } 
       else 
       { res.add(suffix); }
     } 
     return res; 
   } 

 
   public static String commonSuffix(String[] xs, String[] ys) 
   { // All the ys[i] = xs[i] + result

     java.util.HashSet suffixes = new java.util.HashSet(); 
     String suffix = ""; 
	 
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.startsWith(xval)) 
       { int j = xval.length(); 
         suffix = yval.substring(j,yval.length()); 
		 // System.out.println(suffix); 
         suffixes.add(suffix); 
       } 
       else 
       { return null; } 
     } 
	 
     if (suffixes.size() == 1)
     { return suffix; } 
	return null;  
   }

   public static String longestCommonSuffix(Vector strs)
   { // each of the s : strs has result as suffix & 
     // no longer common suffix
     String res = ""; 

     if (strs.size() < 1) 
     { return res; } 

     res = (String) strs.get(0); 
     for (int i = 1; i < strs.size(); i++) 
     { res = ModelElement.longestCommonSuffix(res,(String) strs.get(i),0); } 

     return res; 
   } 

   public static Vector removeCommonSuffix(Vector strs, String suffix)
   { // each of the s : strs is something + suffix
     Vector res = new Vector(); 
     int plen = suffix.length(); 

     if (strs.size() < 1) 
     { return res; } 

     for (int i = 0; i < strs.size(); i++) 
     { String str = (String) strs.get(i); 
       int slen = str.length(); 
       res.add(str.substring(0,slen - plen)); 
     } 

     return res; 
   } 

   public static Vector removeSuffix(String[] strs, String[] suffs)
   { // each strs[i] is something + suffs[i]
     Vector res = new Vector(); 
     
     if (strs.length < 1) 
     { return res; } 

     for (int i = 0; i < strs.length && i < suffs.length; i++) 
     { String str = strs[i]; 
       int slen = str.length();
       int plen = suffs[i].length();  
       res.add(str.substring(0,slen - plen)); 
     } 

     return res; 
   } 

   // Suffixed and prefixed can occur 

   public static boolean isConcatenation(String[] x1s, String[] x2s, String[] ys)
   { // each ys[i] starts with x1s[i] and ends with x2s[i]

     for (int i = 0; i < x1s.length && i < x2s.length && i < ys.length; i++) 
     { String x1val = x1s[i]; 
       String x2val = x2s[i]; 
       String yval = ys[i]; 
       if (yval.startsWith(x1val) && yval.endsWith(x2val)) 
       { } 
       else 
       { return false; }
     } 
     return true; 
   } 

   public static String commonInfix(String[] x1s, String[] x2s, String[] ys) 
   { java.util.HashSet infixes = new java.util.HashSet(); 
     String infix = ""; 
	 
     for (int i = 0; i < x1s.length && i < x2s.length && i < ys.length; i++) 
     { String x1val = x1s[i]; 
       String x2val = x2s[i]; 
       String yval = ys[i];
 
       if (yval.startsWith(x1val)) 
       { int j = x1val.length();
         int k = x2val.length(); 
 
         String suffix = yval.substring(j,yval.length());
         if (suffix.endsWith(x2val))
         { infix = suffix.substring(0,suffix.length()-k);   
           infixes.add(infix); 
         } 
       } 
       else 
       { return null; } 
     } 
	 
     System.out.println(">>> Infixes are " + infixes); 

     if (infixes.size() == 1)
     { return infix; } 
	return null;  
   }

   public static boolean isEqualIgnoringCase(String[] xs, String[] ys) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.equalsIgnoreCase(xval)) { } 
       else 
       { return false; } 
     } 
     return true; 
   }

   public static boolean isReversed(String[] xs, String[] ys) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (AuxMath.reverse(yval).equals(xval)) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

  public static String reverse(String a)
  { String res = ""; 
    for (int i = a.length() - 1; i >= 0; i--)
    { res = res + a.charAt(i); } 
    return res; 
  }

  public static boolean isPrefixedSequence(Vector[] xs, Vector[] ys, String sent, String tent, ModelSpecification mod) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize < ysize) 
       { Vector ytail = new Vector(); 
         for (int j = (ysize-xsize); j < ysize; j++) 
         { ytail.add(yval.get(j)); } 
       
         System.out.println(">> xval is: " + xval); 
         System.out.println(">> yval is: " + yval); 
         System.out.println(">> ytail is: " + ytail); 

         if (mod.correspondingObjectSequences(sent,tent,xval,ytail)) { } 
         else 
         { return false; }
       }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static Vector commonSequencePrefix(Vector[] xs, Vector[] ys) 
   { java.util.HashSet prefixes = new java.util.HashSet(); 
     Vector prefix = new Vector(); 
	 
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i]; 
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize < ysize) 
       { Vector yfront = new Vector(); 
         for (int j = 0; j < (ysize-xsize); j++) 
         { yfront.add(yval.get(j)); }

         System.out.println(">> Prefix of " + yval + " before " + xval + " is: " + yfront);  

         prefixes.add(yfront); 
         prefix = yfront; 
	  } 
       else 
       { return null; } 
     } 
	 
	if (prefixes.size() == 1)
      { return prefix; } 
	 return null;  
    } 

   public static boolean isSuffixedSequence(Vector[] xs, Vector[] ys, String sent, String tent, ModelSpecification mod) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize < ysize) 
       { Vector yfront = new Vector(); 
         for (int j = 0; j < xsize; j++) 
         { yfront.add(yval.get(j)); } 
       
         System.out.println(">> xval is: " + xval); 
         System.out.println(">> yval is: " + yval); 
         System.out.println(">> yfront is: " + yfront); 

         if (mod.correspondingObjectSequences(sent,tent,xval,yfront)) { } 
         else 
         { return false; }
       }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isSuffixedSequences(Vector[] xs, Vector[] ys, ModelSpecification mod) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize < ysize) 
       { Vector yfront = new Vector(); 
         for (int j = 0; j < xsize; j++) 
         { yfront.add(yval.get(j)); } 
       
         System.out.println(">> xval is: " + xval); 
         System.out.println(">> yval is: " + yval); 
         System.out.println(">> yfront is: " + yfront); 

         if (mod.correspondingObjectSequences(xval,yfront)) { } 
         else 
         { return false; }
       }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static Vector removePrefixSequences(Vector[] strs, Vector[] prefs)
   { // each strs[i] is prefs[i]^something

     Vector res = new Vector(); 

     if (strs.length < 1) 
     { return res; } 

     for (int i = 0; i < strs.length && i < prefs.length; i++) 
     { Vector str = strs[i]; 
       int slen = str.size(); 
       int plen = prefs[i].size();
       Vector remp = subrange(str,plen+1,slen); 
       System.out.println(">>> " + str + " - prefix " + prefs[i] + " is: " + remp);  
       res.add(remp); 
     } 

     return res; 
   } 

   public static boolean isPrefixedSequences(Vector[] xs, Vector[] ys, ModelSpecification mod) 
   { // Each ys[i] is something ^ xs[i]' 

     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize < ysize) 
       { Vector ytail = new Vector(); 
         for (int j = xsize; j < xsize; j++) 
         { ytail.add(yval.get(j)); } 
       
         System.out.println(">> xval is: " + xval); 
         System.out.println(">> yval is: " + yval); 
         System.out.println(">> ytail is: " + ytail); 

         if (mod.correspondingObjectSequences(xval,ytail)) { } 
         else 
         { return false; }
       }
       else 
       { return false; } 
     } 
     return true; 
   } 


   public static Vector removeSuffixSequences(Vector[] strs, Vector[] suffs)
   { // each strs[i] is something ^ suffs[i]
     Vector res = new Vector(); 
     
     if (strs.length < 1) 
     { return res; } 

     for (int i = 0; i < strs.length && i < suffs.length; i++) 
     { Vector str = strs[i]; 
       int slen = str.size();
       int plen = suffs[i].size();  
       Vector remp = AuxMath.subrange(str,1,slen - plen);
       System.out.println(">>> " + str + " - suffix " + suffs[i] + " is: " + remp);  
       res.add(remp); 
     } 

     return res; 
   } 



   public static Vector commonSequenceSuffix(Vector[] xs, Vector[] ys) 
   { java.util.HashSet suffixes = new java.util.HashSet(); 
     Vector suffix = new Vector(); 
	 
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i]; 
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize < ysize) 
       { Vector ytail = new Vector(); 
         // ytail = sequenceSuffix(xval,yval); 

         for (int j = xsize; j < ysize; j++) 
         { ytail.add(yval.get(j)); } 
         suffixes.add(ytail); 
         suffix = ytail; 
       } 
       else 
       { return null; } 
     } 
	 
     if (suffixes.size() == 1)
     { return suffix; } 
     return null;  
   }

   public static Vector sequenceSuffix(Vector xx, Vector yy)
   { int n = xx.size(); 
     int m = yy.size(); 
     Vector vv = new Vector(); 
     for (int i = n; i < m; i++)
     { vv.add(yy.get(i)); } 
     return vv; 
   }  

   public static Vector sequencePrefix(Vector xx, Vector yy)
   { int n = xx.size(); 
     int m = yy.size(); 
     Vector vv = new Vector(); 
     for (int i = 0; i < m - n; i++)
     { vv.add(yy.get(i)); } 
     return vv; 
   }  

   public static boolean isSubsetSet(Vector[] xs, Vector[] ys, ModelSpecification mod) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize <= ysize) 
       { Vector ytail = mod.correspondingObjectSubset(xval,yval); 
         if (ytail != null) 
         { System.out.println(">>> Corresponding objects of " + xval + " are " + ytail); }
	    else 
         { return false; }
       }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isSequencePrefix(Vector lhs, Vector rhs)
   { int n = lhs.size(); 
     int m = rhs.size(); 
     if (n > m)
     { return false; } 

     for (int i = 0; i < n; i++) 
     { Object lobj = lhs.get(i); 
       if (lobj == null) 
       { return false; } 
       Object robj = rhs.get(i); 
       if (robj == null) 
       { return false; }  
       if (lobj.equals(robj)) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isSequenceSuffix(Vector lhs, Vector rhs)
   { int n = lhs.size(); 
     int m = rhs.size(); 
     if (n > m)
     { return false; } 

     // Elements of lhs are same as rhs[n..m] elements

     for (int i = 0; i < n; i++) 
     { Object lobj = lhs.get(i); 
       if (lobj == null) 
       { return false; } 
       Object robj = rhs.get(i + (m - n)); 
       if (robj == null) 
       { return false; }  
       if (lobj.equals(robj)) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 


   public static boolean isSequencePrefixWithInsertion(Vector lhs1, Vector lhs2, Vector rhs, Vector ins)
   { // lhs1 ^ ins ^ lhs2 is a prefix of rhs

     int n1 = lhs1.size(); 
     int n2 = lhs2.size(); 
     int m = rhs.size(); 
     if (n1 + n2 > m)
     { return false; } 

     for (int i = 0; i < n1; i++) 
     { Object lobj = lhs1.get(i); 
       if (lobj == null) 
       { return false; } 
       Object robj = rhs.get(i); 
       if (robj == null) 
       { return false; }  
       if (lobj.equals(robj)) { } 
       else 
       { return false; } 
     } 

     if (n2 == 0) 
     { return true; } 

     Object lobjx = lhs2.get(0); 
     if (lobjx == null) 
     { return false; } 

     Object robjx = rhs.get(n1); 
     if (robjx == null) 
     { return false; }  

     if (lobjx.equals(robjx)) 
     { for (int i = 0; i < n2; i++) 
       { Object lobj = lhs2.get(i); 
         if (lobj == null) 
         { return false; } 
         Object robj = rhs.get(n1+i); 
         if (robj == null) 
         { return false; }  
         if (lobj.equals(robj)) { } 
         else 
         { return false; } 
       }
     } 
     else 
     { ins.add(robjx); 
       int j = n1 + 1; 
       for ( ; j < m; j++) 
       { Object robj = rhs.get(j); 
         if (!(robj.equals(lobjx)))
         { ins.add(robj); }
         else 
         { break; } 
       }  

       for (int i = 0; i < n2; i++) 
       { Object lobj = lhs2.get(i); 
         if (lobj == null) 
         { return false; } 
         if (j >= m) 
         { return false; } 
         Object robj = rhs.get(j); 
         if (robj == null) 
         { return false; }  
         if (lobj.equals(robj)) { } 
         else 
         { return false; } 
         j++; 
       }
     }

     return true; 
   } 

   public static Vector sequenceComposition(Vector sources, Vector targets, Vector unused)
   { // Elements of sources compose to give all of targets
     // Possibly with extra prefix/suffix/insertions
     // Pattern is list  [_1,K1,_3,_2,K2] etc
     // Unused is list of unused sources [_4,_5] etc

     int n = sources.size();
     int m = targets.size(); 

     Vector used = new Vector(); 
     Vector pattern = new Vector(); 

     int[] startpositions = new int[n]; 
     int[] endpositions = new int[n]; 
     
     for (int i = 0; i < n; i++) 
     { Vector srci = (Vector) sources.get(i);
       String id = "_" + (i+1);  
       int indi = Collections.indexOfSubList(targets,srci); 
       if (indi >= 0)
       { startpositions[i] = indi; 
         endpositions[i] = indi + srci.size() - 1;
         System.out.println(srci + " is a subsequence of " + targets + " from " + indi + " to " + endpositions[i]); 
         used.add(new Integer(i));  
       } 
       else 
       { unused.add(id); }  
     } 

     System.out.println(">> Used sources: " + used); 

     // The ranges [startpositions[i],endpositions[i]]
     // for i : used must be disjoint. 

     for (int j = 0; j < m; j++) 
     { boolean found = false; 

       for (int k = 0; k < used.size() && !found; k++) 
       { Integer iobj = (Integer) used.get(k); 
         int i = iobj.intValue(); 
         String id = "_" + (i+1); 
         if (j >= startpositions[i] && 
             j <= endpositions[i])
         { if (pattern.contains(id))
           { } 
           else 
           { pattern.add(id); } 
           found = true; 
         } 
       }

       if (!found)
       { pattern.add(targets.get(j)); } 
     } 

     System.out.println(pattern); 

     return pattern; 
   } 


   public static boolean isSupsetSet(Vector[] xs, Vector[] ys, ModelSpecification mod) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (ysize <= xsize) 
       { Vector xtail = mod.correspondingObjectSupset(xval,yval); 
         if (xtail != null) 
	    { System.out.println(">>> Corresponding objects of " + yval + " are " + xtail); }
	    else 
         { return false; }
       }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static Vector commonSubsetSet(Vector[] xs, Vector[] ys, ModelSpecification mod) 
   { java.util.Set unionsets = new java.util.HashSet(); 
     java.util.Set added = new java.util.HashSet(); 
	 
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       int xsize = xval.size(); 
       int ysize = yval.size(); 
       if (xsize < ysize) 
       { Vector ytail = mod.correspondingObjectSubset(xval,yval); 
         if (ytail != null) 
	    { java.util.HashSet yrem = new java.util.HashSet(); 
	      yrem.addAll(yval); 
	      yrem.removeAll(ytail); 
	      System.out.println(">>> Added objects are " + yrem);
	      added = yrem; 
	      unionsets.add(yrem);  
	    }
	    else 
         { return null; }
       }
       else 
       { return null; } 
     }
	 
	 if (unionsets.size() == 1)
	 { Vector sourceadded = mod.getSourceObjects(added); 
	   System.out.println(">>> Mapping adds " + sourceadded + " to source collection"); 
	   return sourceadded; 
	 } 
     return null; 
   } 

   public static boolean isConcatenation(Vector[] xs, Vector xs1[], Vector[] ys, ModelSpecification mod) 
   { // assuming they are collections of objects
   
     for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector xval1 = xs1[i]; 
       Vector yval = ys[i];
       Vector vals = new Vector(); 
       vals.addAll(xval);
       vals.addAll(xval1);    
       if (mod.correspondingObjectSequences(vals,yval)) 
       { System.out.println(">>> Concatenation " + xval + " ^ " + xval1 + " |--> " + yval); }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isAppendSequences(Vector[] xs, ObjectSpecification xs1[], Vector[] ys, ModelSpecification mod) 
   { // assuming they are collections of objects
   
     for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       ObjectSpecification xval1 = xs1[i]; 
       Vector yval = ys[i];
       Vector vals = new Vector(); 
       vals.addAll(xval);
       vals.add(xval1);    
       if (mod.correspondingObjectSequences(vals,yval)) 
       { System.out.println(">>> Concatenation " + xval + "->append(" + xval1 + ") |--> " + yval); }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isUnionSets(Vector[] xs, Vector xs1[], Vector[] ys, ModelSpecification mod) 
   { // assuming they are collections of objects
   
     for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector xval1 = xs1[i]; 
       Vector yval = ys[i];
       Vector vals = new Vector(); 
       vals.addAll(xval);
       vals.addAll(xval1);   // assuming disjoint 
       Vector tvals = mod.getCorrespondingElements(vals); 

       if (yval.containsAll(tvals) && tvals.containsAll(yval))
       { System.out.println(">>> Union of " + xval + " and " + xval1 + " |--> " + yval); }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isIncludingSets(Vector[] xs, ObjectSpecification xs1[], Vector[] ys, ModelSpecification mod) 
   { // assuming they are collections of objects
   
     for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       ObjectSpecification xval1 = xs1[i]; 
       Vector yval = ys[i];
       Vector vals = new Vector(); 
       vals.addAll(xval);
       vals.add(xval1);    
       Vector tvals = mod.getCorrespondingElements(vals); 

       if (yval.containsAll(tvals) && tvals.containsAll(yval))
       { System.out.println(">>> Union " + xval + "->including(" + xval1 + ") |--> " + yval); }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static boolean isExcludingSets(Vector[] xs, ObjectSpecification xs1[], Vector[] ys, ModelSpecification mod) 
   { // assuming they are collections of objects
   
     for (int i = 0; i < xs.length && i < xs1.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       ObjectSpecification xval1 = xs1[i]; 
       Vector yval = ys[i];
       Vector vals = new Vector(); 
       vals.addAll(xval);
       Vector xvals1 = new Vector(); 
       xvals1.add(xval1); 
       vals.removeAll(xvals1);    
       Vector tvals = mod.getCorrespondingElements(vals); 

       if (yval.containsAll(tvals) && tvals.containsAll(yval))
       { System.out.println(">>> Subtraction " + xval + "->excluding(" + xval1 + ") |--> " + yval); }
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static Vector removeSubsets(Vector[] ys, Vector[] xs, ModelSpecification mod) 
   { // assuming they are collections of objects
     Vector res = new Vector(); 
   
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { Vector xval = xs[i]; 
       Vector yval = ys[i];
       Vector vals = new Vector(); 
       vals.addAll(xval);
       Vector tvals = mod.getCorrespondingElements(vals); 

       Vector rval = new Vector(); 
       rval.addAll(yval); 
       rval.removeAll(tvals);

       System.out.println(">>> " + yval + " with " + vals + " removed is: " + rval); 
 
       res.add(rval); 
     } 
     return res; 
   } 

   public static Vector removeElements(Vector[] ys, ObjectSpecification[] xs, ModelSpecification mod) 
   { // assuming they are collections of objects
     Vector res = new Vector(); 
   
     for (int i = 0; i < xs.length && i < ys.length; i++) 
     { ObjectSpecification xval = xs[i]; 
       Vector yval = ys[i];
       Vector vals = new Vector(); 
       vals.add(xval);
       Vector tvals = mod.getCorrespondingElements(vals); 

       Vector rval = new Vector(); 
       rval.addAll(yval); 
       rval.removeAll(tvals);

       System.out.println(">>> " + yval + " with " + vals + " removed is: " + rval); 
 
       res.add(rval); 
     } 
     return res; 
   } 

   public static boolean isUnionSets(ObjectSpecification[] xs, ObjectSpecification[] xs1, ObjectSpecification[] ys, String tent, ModelSpecification mod) 
   { // assuming they are collections of objects
     Vector imageofxvals = new Vector(); 

     for (int i = 0; i < xs.length; i++) 
     { ObjectSpecification xobj = xs[i]; 
       Vector tvals = mod.getCorrespondingObjects(xobj,tent); 
       imageofxvals.addAll(tvals);  
     } 

     for (int i = 0; i < xs1.length; i++) 
     { ObjectSpecification xobj = xs1[i]; 
       Vector tvals = mod.getCorrespondingObjects(xobj,tent); 
       imageofxvals.addAll(tvals);
     } 

     Vector yvals = new Vector(); 
     for (int i = 0; i < ys.length; i++) 
     { ObjectSpecification yobj = ys[i]; 
       yvals.add(yobj); 
     } 

     System.out.println("*** Comparing " + imageofxvals + " to " + yvals); 

     if (imageofxvals.containsAll(yvals) && yvals.containsAll(imageofxvals))
     { return true; } 
     return false; 
   } 

   public static void main(String[] args)
   { Vector vvx1 = new Vector(); 
     Vector vvy1 = new Vector(); 
     Vector vvx2 = new Vector(); 
     Vector vvy2 = new Vector(); 

     // vvx1.add("aa"); vvx1.add("bb"); vvx1.add("cc"); 
     vvx1.add("dd"); vvx1.add("cc"); 

     vvy1.add("aa"); vvy1.add("bb"); 
     vvy1.add("cc");  

     // vvx2.add("uu"); vvx2.add("ww"); vvx2.add("aa"); vvx2.add("bb"); 
     vvx2.add("cc"); vvx2.add("dd");
     vvy2.add("zz"); vvy2.add("zz"); vvy2.add("uu"); vvy2.add("ww");  

     Vector[] vvxs = { vvx1, vvx2 }; 
     Vector[] vvys = { vvy1, vvy2 };

     // System.out.println(">> Suffixes: " + AuxMath.removePrefixSequences(vvxs,vvys)); 
 
     System.out.println(">> Suffixes: " + AuxMath.removeSuffixSequences(vvys,vvxs)); 

     System.out.println(">> Tails: " + AuxMath.tailSequences(vvys)); 
     System.out.println(">> Fronts: " + AuxMath.frontSequences(vvys)); 

     System.out.println(AuxMath.isConstantSequences(vvxs)); 
     System.out.println(AuxMath.isConstantSets(vvxs)); 



     String[] strs = { "long", "tilda", "tremble" };  
     String[] xss = { "ng", "da", "ble" }; 

     System.out.println(">> Suffixes: " + AuxMath.removeSuffix(strs,xss)); 
     

     double[] xs = {19601123,19700316,20010101,20001119,19501209,20090101}; 
     double[] ys = {1960,1970,2001,2000,1950,2009};

     Vector v1 = new Vector(); 
     // v1.add(10); v1.add("2"); v1.add("-1.1"); v1.add(-0.9); 
     System.out.println(AuxMath.numericSum(v1)); 
     System.out.println(AuxMath.numericPrd(v1)); 
     System.out.println(AuxMath.numericMin(v1)); 
     System.out.println(AuxMath.numericMax(v1)); 

     Vector v2 = new Vector(); 
     v2.add("abba"); v2.add("ab"); v2.add("xyz"); 
     System.out.println(AuxMath.stringSum(v2)); 
     System.out.println(AuxMath.stringMax(v2)); 
     System.out.println(AuxMath.stringMin(v2)); 


     /* System.out.println(AuxMath.divFunction(xs,ys)); 

     System.out.println(gcd(1,1)); 
     System.out.println(gcd(-1,1)); 
     System.out.println(gcd(0,1)); 
     System.out.println(gcd(0,0)); */ 

	 
	 /* Vector x1 = new Vector(); 
	 x1.add(new Double(1)); x1.add(new Double(3)); x1.add(new Double(5)); 
	 Vector y1 = new Vector(); 
	 y1.add(new Double(3)); 
	 	 
	 
	 Vector x2 = new Vector(); 
	 x2.add(new Double(10)); x2.add(new Double(10)); x2.add(new Double(10)); 
	 Vector y2 = new Vector(); 
	 y2.add(new Double(10)); 
	 
	 
     Vector[] xvs = { x1, x2 }; 
     Vector[] yvs = { y1, y2 }; 
	 
	 
     System.out.println(AuxMath.isNumericAverage(xvs,yvs)); */ 

     Vector x1 = new Vector(); 
	 x1.add("ab"); x1.add("cd"); 
	 Vector x2 = new Vector(); 
	 x2.add("x"); x2.add("yy"); x2.add("try");
	 Vector x3 = new Vector(); 
	 x3.add("ttt"); x3.add("pppp"); 
	 
     Vector[] xstrs = {x1, x2, x3};
     String[] ystrs = {"ab##cd((", "x##yy##try((", "ttt##pppp(("}; 
	 
     String[] rems = new String[3]; 
	 System.out.println(AuxMath.initialSeparatorStringSum(xstrs,ystrs,rems)); 
	  
	 
     /* System.out.println(isFunctional(xs,ys)); 

   	System.out.println(quadraticRelationship(xs,ys,"s","t"));  
   	 	
   	double[] xs2 = {-2,-1,0,1,2,3,4};
     double[] ys2 = {11,5,3,5,11,21,35}; 
   	slopes(ys2); 

   	System.out.println(quadraticRelationship(xs2,ys2,"s","t"));  
	 
	double[] xss = {1,2,3,4,5,6}; 
   	double[] yss = {1,2,6,24,120,720};
   	double[] yss2 = {10,100,1000,10000,100000,1000000}; 
   	 
     System.out.println(isFunctional(xss,yss)); 

   	double ecorr = isExponential(xss,yss);
   	System.out.println(ecorr);  
   	 
	double ecorr2 = isExponential(xss,yss2);
   	System.out.println(ecorr2);  
   	
      double[] xn = {1,2,3,4,3,6}; 
   	 double[] yn = {1,2,6,24,120,720};

      System.out.println(isFunctional(xn,yn)); 

      String[] ss = {"ab", "bc", "cc"}; 
      String[] ts = {"ba", "cb", "cc"}; 

      System.out.println(isFunctional(ss,ts)); 
      System.out.println(isConstant(ts)); 
      System.out.println(isReversed(ss,ts)); 
      
      Vector sq1 = new Vector(); 
      sq1.add("a"); sq1.add("b"); 
      Vector sq2 = new Vector(); 
      sq2.add("c"); sq2.add("d"); 
      Vector[] xv = { sq1, sq2 }; 
      Vector sq3 = new Vector(); 
      sq3.add("a"); sq3.add("b"); 
      sq3.add("c"); sq3.add("d"); 
      sq3.add("e"); sq3.add("f"); 
      Vector[] yv = { sq1, sq3 }; 
      System.out.println(isConstantSequence(yv));
      System.out.println(allSubsets(xv,yv)); 
      */ 	 
      	

   	 /* Double d1 = new Double(12); 
	 Double d2 = new Double(12); 
	 Double d3 = new Double(6); 
	 Double d4 = new Double(18); 
	 
	 List s1 = new ArrayList(); 
	 s1.add(d1); 
	 s1.add(d2); 
	 s1.add(d3);
	 s1.add(d4);  
	 List s2 = new ArrayList(); 
	 s2.add(new Double(1)); 
	 s2.add(new Double(3)); 
	 s2.add(new Double(2));
	 s2.add(new Double(4));  
	 List lll = SystemTypes.Ocl.sortedBy(s1,s2); 
	 System.out.println(lll); 
	 // 12, 6, 12, 18
	 
	 Map f = new java.util.HashMap(); 
	 f.put(d1, new Double(1)); 
	 f.put(d2, new Double(3)); 
	 f.put(d3, new Double(2)); 
	 f.put(d4, new Double(4)); 
	 
	 LComparator comp = new LComparator(f); 
	 Collections.sort(s1,comp);
	 System.out.println(s1);   */

      Vector sbs1 = new Vector(); 
      Vector sbs2 = new Vector(); 
      Vector tbs = new Vector(); 

      sbs1.add("aa"); sbs1.add("bb"); sbs1.add("cc"); 
      sbs2.add("ff");   
      Vector sbs = new Vector(); 
      sbs.add(sbs1); sbs.add(sbs2); 

      tbs.add("xx"); tbs.add("bb"); 
      tbs.add("cc"); tbs.add("dd"); 
      tbs.add("ee"); tbs.add("ff"); 
   
      Vector vv = new Vector(); 
      boolean bb = 
        AuxMath.isSequencePrefixWithInsertion(sbs1,sbs2,
                                              tbs,vv);
      System.out.println(bb);
      System.out.println(vv); 

      Vector vv1 = new Vector(); 
      Vector vv2 = new Vector(); 

      vv2 = sequenceComposition(sbs,tbs,vv1); 
      System.out.println(vv1); 

      Vector sbs3 = new Vector(); 
      sbs3.add("ee"); sbs3.add("ff"); 
      bb = AuxMath.isSequenceSuffix(sbs2,tbs); 
      System.out.println(bb); 
      System.out.println(AuxMath.sequencePrefix(sbs3,tbs)); 

   }
 }
   
/*    class LComparator implements Comparator
   { Map fmap; 
   
     LComparator(Map f)
	 { fmap = f; }
	 
     public int compare(Object x, Object y) 
     { Comparable c1 = (Comparable) fmap.get(x); 
	   Comparable c2 = (Comparable) fmap.get(y); 
	   return c1.compareTo(c2); 
	 }
   } */ 


