import java.util.Vector; 
import java.util.List; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class SortTest
{ public static List sort(List a)
  { int i = a.size()-1;
    return mergeSort(a,0,i); 
  } 

  private static List mergeSort(List a, int ind1, int ind2)
  { List res = new Vector(); 
    if (ind1 > ind2) 
    { return res; } 
    if (ind1 == ind2)
    { res.add(a.get(ind1)); 
      return res; 
    } 
    int mid = (ind1 + ind2)/2; 
    List a1; 
    List a2;
    if (mid == ind1)
    { a1 = new Vector(); 
      a1.add(a.get(ind1)); 
      a2 = mergeSort(a,mid+1,ind2); 
    } 
    else 
    { a1 = mergeSort(a,ind1,mid-1);  
      a2 = mergeSort(a,mid,ind2);
    }
 
    int i = 0; 
    int j = 0; 
    while (i < a1.size() && j < a2.size())
    { Comparable e1 = (Comparable) a1.get(i); 
      Comparable e2 = (Comparable) a2.get(j); 
      if (e1.compareTo(e2) < 0) // e1 < e2
      { res.add(e1);
        i++; // get next e1
      } 
      else 
      { res.add(e2); 
        j++; 
      } 
    } 
    if (i == a1.size())
    { for (int k = j; k < a2.size(); k++) 
      { res.add(a2.get(k)); } 
    } 
    else 
    { for (int k = i; k < a1.size(); k++) 
      { res.add(a1.get(k)); } 
    } 
    return res; 
  } 

  public static void main(String[] args)
  { Vector vv = new Vector(); 
    System.out.println("eewe".length()); 
    vv.add("ee"); 
    System.out.println(vv); 
    System.out.println(sort(vv)); 
  
    vv.add("ee"); vv.add("vr"); vv.add("gh"); 
    vv.add("ff"); vv.add("ab"); 
    vv.set(1,"zzzszz"); 
    vv.set(3,"Irene"); 
    System.out.println(vv); 
    System.out.println(sort(vv)); 
  } 
}

          

