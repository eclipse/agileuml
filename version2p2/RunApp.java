import java.io.*; 
import java.lang.Runtime; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class RunApp implements Runnable 
{ String appName = ""; 
  String outFile = ""; 

  public RunApp(String nme)
  { appName = nme; } 

  public synchronized void setApp(String f) 
  { appName = f; } 

  public synchronized void setFile(String f) 
  { outFile = f; } 

  public synchronized void run()
  { String dir = appName; 
    try { Runtime proc = Runtime.getRuntime(); 
          Process p2 = proc.exec("java -jar " + dir + "/" + dir + ".jar"); 


          InputStream sin2 = p2.getInputStream(); 
          InputStreamReader inr2 = new InputStreamReader(sin2); 
          BufferedReader ibr2 = new BufferedReader(inr2); 

          InputStream stderr = p2.getErrorStream(); 
          StreamGobble egb = new StreamGobble(stderr); 
          egb.start(); // igb.start();   

          File file = new File(outFile); 
          PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          

          String oline2 = ibr2.readLine(); 
          // System.out.println("java ....");
          while (oline2 != null) 
          { out.println(oline2); 
            oline2 = ibr2.readLine(); 
          }  
          int exitjar2 = p2.waitFor(); 
          // System.out.println("Java exit code: " + exitjar2); 
          out.close(); 
          
        } 
        catch (Exception ee1) 
        { System.err.println("Unable to run application " + appName); }     
  } 
}   