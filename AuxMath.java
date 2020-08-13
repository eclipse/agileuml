import java.util.List; 
import java.util.ArrayList; 
import java.util.Map; 
import java.util.Comparator; 
import java.util.Collections; 
import java.util.Vector; 

/******************************
* Copyright (c) 2003,2020 Kevin Lano
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

    public static boolean isCopy(Vector[] xs, Vector[] ys, ModelSpecification mod)
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
	
    public static boolean isConstantSequence(Vector[] ys)
    { if (ys.length > 1)
	 { Vector y0 = ys[0]; 
	   for (int i = 1; i < ys.length; i++)
	   { if (y0.equals(ys[i])) { }
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
    { double slope = sumprods/sumdiffxsq; 
      return meany - slope*meanx; 
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
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.endsWith(xval)) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static String commonPrefix(String[] xs, String[] ys) 
   { java.util.HashSet prefixes = new java.util.HashSet(); 
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

   public static boolean isSuffixed(String[] xs, String[] ys) 
   { for (int i = 0; i < xs.length && i < ys.length; i++) 
     { String xval = xs[i]; 
       String yval = ys[i]; 
       if (yval.startsWith(xval)) { } 
       else 
       { return false; } 
     } 
     return true; 
   } 

   public static String commonSuffix(String[] xs, String[] ys) 
   { java.util.HashSet suffixes = new java.util.HashSet(); 
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

   // Suffixed and prefixed can occur 

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

   public static void main(String[] args)
   { double[] xs = {-2,-1,0,1,2,3}; 
   	double[] ys = {11,5,3,5,11,21};

     System.out.println(isFunctional(xs,ys)); 

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


