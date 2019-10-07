import java.util.ArrayList; 
import java.util.Random; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class Chromosome
{ int[] bit = new int[12]; 
  Database db;  

  Chromosome(Random rand)
  { for (int i = 0; i < 12; i++) 
    { bit[i] = rand.nextInt(6); }
  } 

  public String toString()
  { String res = ""; 
    for (int i = 0; i < 12; i++) 
    { res = res + bit[i] + ","; }
    return res; 
  } 
    
  public void setDatabase(Database d)
  { db = d; } 

  public double fitness(int share)
  { double prediction = 0; 
    int divisor = 0; 

    Share sh = db.getShare(share); 
    for (int i = 0; i < 12; i++) 
    { prediction = prediction + bit[i]*sh.getPrice(i); 
      divisor = divisor + bit[i]; 
    } 
    if (divisor != 0)
    { prediction = prediction/divisor; }
    else 
    { prediction = 0; } 
    
    double actualPrice = sh.getPrice(12); 
    return Math.abs(prediction - actualPrice); 
  }
} 


class Database
{ ArrayList sharedata = new ArrayList(); 

  public void addShare(Share s)
  { sharedata.add(s); } 

  public Share getShare(int i)
  { return (Share) sharedata.get(i); } 
} 


class Share
{ double[] prices = new double[13]; 
  
  Share(double[] p)
  { prices = p; } 

  double getPrice(int i)
  { return prices[i]; } 
} 

public class GenAlg
{ public static void main(String[] args)
  { Database d = new Database(); 
     
    double[] dd = {8.0, 9.0, 12.5, 11.5, 6, 7, 4, 2, 3, 4, 3, 5, 5.5};
    Share s1 = new Share(dd);
    d.addShare(s1); 

    ArrayList population = new ArrayList(); 
    Random rand = new Random(); 

    for (int i = 0; i < 10; i++)
    { Chromosome c = new Chromosome(rand); 
      c.setDatabase(d); 
      population.add(c); 
      System.out.println(c); 
      System.out.println(c.fitness(0)); 
    } 
  } 
}       