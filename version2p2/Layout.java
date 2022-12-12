import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

abstract class Layout
{ static final int xoffset = 10;
  static final int yoffset = 10;
 
  public static Layout getLayout(int n, Vector sensors)
  { if (n <= 4)
    { return new Layout4(); }
    if (n <= 8)
    { return new Layout8(); }
    if (n <= 16) 
    { return new Layout16(); }
    else // fold one sensor within anothers states: 
    { return new FoldedLayout(sensors); } 
  }

  abstract int getX(int i);

  abstract int getY(int i);
}

class Layout4 extends Layout
{ int[] fourx = { 0, 100, 0, 100 };
  int[] foury = { 0, 0, 100, 100 };

  int getX(int i)
  { return fourx[i] + xoffset; }

  int getY(int i)
  { return foury[i] + yoffset; }
}

class Layout8 extends Layout
{ int[] eightx = { 300, 0, 200, 100, 300, 0, 200, 100 };
  int[] eighty = { 0, 0, 100, 100, 300, 300, 200, 200 };

  int getX(int i)
  { return eightx[i] + xoffset; }

  int getY(int i)
  { return eighty[i] + yoffset; }
}

class Layout16 extends Layout
{ int[] sixteenx = { 600, 650, 0, 50, 
                     400, 450, 200, 250,
                     600, 650, 0, 50, 
                     400, 450, 200, 250 };
  int[] sixteeny = { 0, 100, 0, 100, 
                     200, 300, 200, 300,
                     600, 700, 600, 700, 
                     400, 500, 400, 500 };

  int getX(int i)
  { return sixteenx[i] + xoffset; }

  int getY(int i)
  { return sixteeny[i] + yoffset; }
}

class FoldedLayout extends Layout
{ private int[] scalex; 
  private int[] scaley; 
  private int[] substates; 
  private Vector senstates = new Vector(); 
  private Vector senvisuals = new Vector(); 
  private int sumx = 0; 
  private int sumy = 0; 
  private int xdiff = 0; // amount to move diagram up because of scaling
  private int ydiff = 0; // amount to move diagram left because of scaling

  FoldedLayout(Vector sensors) 
  { int n = sensors.size(); 
    scalex = new int[n]; 
    scaley = new int[n]; 
    substates = new int[n]; 
    scalex[n-1] = 1; 
    scaley[n-1] = 1; 
    substates[n-1] = 1; 

    for (int i = 0; i < n; i++)
    { Statemachine sm = (Statemachine) sensors.get(i);
      senstates.add(sm.getStates());
      StatechartArea gui = sm.getGui();
      senvisuals.add(gui.getVisuals());
    } 

    for (int i = n-1; i > 0; i--)
    { Statemachine sm = (Statemachine) sensors.get(i); 
      StatechartArea gui = sm.getGui();
      int mx = gui.getMaxX(); 
      scalex[i-1] = (mx/60)*scalex[i]; 
          // x-expansion of states of sensor(i-1) needed
      int my = gui.getMaxY(); 
      scaley[i-1] = (my/60)*scaley[i]; 
      substates[i-1] = sm.states.size()*substates[i]; 
          // number of states contained in each sensor(i-1) state
      // System.out.println(scale[i-1]); 
      // System.out.println(substates[i-1]); 
      int minxi = gui.getMinX();
      int minyi = gui.getMinY();
      xdiff += minxi*scalex[i-1] - minxi; 
      ydiff += minyi*scaley[i-1] - minyi; 
    }  
    // StatechartArea g = ((Statemachine) sensors.get(0)).getGui(); 
    // int minx = g.getMinX(); 
    // int miny = g.getMinY(); 
    // xdiff += minx*scalex[0] - minx; 
    // ydiff += miny*scaley[0] - miny; 
  } 

 int getX(int k)
 { int n = scalex.length; // n >= 2
   int[] coords = new int[n];
   // System.out.println(n); 
   for (int i = 0; i < n-1; i++)
   { coords[i] = k/substates[i];   // Number of substates[i] groups before me
     k = k%substates[i];           // My position in the next substates[i] 
   } 
   coords[n-1] = k%substates[n-2]; // Position in the final bottom level
   sumx = 0;
   sumy = 0;
   for (int j = 0; j < n; j++)
   { Vector states = (Vector) senstates.get(j);
     // System.out.println(states); 
     State st = (State) states.get(coords[j]);
     Vector visuals = (Vector) senvisuals.get(j);
     VisualData vd = 
       (VisualData) VectorUtil.lookup(st.label,visuals);
     sumx += vd.getx()*scalex[j];
     sumy += vd.gety()*scaley[j];
   }
   return sumx - xdiff;
 }

  int getY(int i)  // assumes done immediately after getX(i)
  { return sumy - ydiff; }

} 
        




