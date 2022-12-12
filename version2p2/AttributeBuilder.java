import java.util.Vector; 
import javax.swing.*; 
import java.awt.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class AttributeBuilder extends SmBuilder
{ private Vector snames = new Vector();
  private static final int XOFFSET = 40; 
  private static final int YOFFSET = 40;
  private String name = ""; 

  public AttributeBuilder() 
  { super(); } 

  public AttributeBuilder(JFrame frame)
  { name = JOptionPane.showInputDialog("Enter name of attribute component: "); 
    String numb = JOptionPane.showInputDialog("Enter number of states (1..10):"); 
    int attsize = 0; 
    while (attsize == 0)
    { try { attsize = Integer.parseInt(numb); } 
      catch (Exception e) 
      { System.err.println("Invalid -- must be an integer in 1..10"); 
        numb = JOptionPane.showInputDialog("Enter number of states (1..10):");
        attsize = 0; 
        continue; 
      } 
      if (attsize <= 0 || attsize > 10) 
      { System.err.println("Invalid -- must be an integer in 1..10"); 
        numb = JOptionPane.showInputDialog("Enter number of states (1..10):");
        attsize = 0; 
      } 
    } 
    snames = Named.namesOfNumberRange(0,attsize-1); 
  } 
     
  public String getName() 
  { return name; } 

  public void build(StatechartArea sa)
  { super.build(sa); 
    build(); 
  } 

  private void build()
  { int n = snames.size();  // n < 10 really. 
    int[] xs = new int[n];  // x coords
    int[] ys = new int[n];  // y coords
    Event[] events = new Event[n];
    RoundRectData[] states = 
      new RoundRectData[n];

    double theta = 0.0;
    double xOffset = n*50;
    double radius = xOffset/2;
    double increment = 2*Math.PI/(double) n;

    for (int i = 0; i < n; i++)
    { String sn = (String) snames.get(i);
      Event e = new Event(name + "set" + sn);
      events[i] = e;
      sarea.addEvent(e);

      theta += increment;
      xs[i] = (int) (radius + (radius*Math.cos(theta))) + XOFFSET;
      ys[i] = (int) (radius + radius*Math.sin(theta)) + YOFFSET;
      RoundRectData rd = 
        new RoundRectData(xs[i],ys[i],
              sarea.getForeground(),i);
      rd.setName(sn);
      states[i] = rd;
    }

    sarea.addInitialRect(states[0]);
    for (int j = 1; j < n; j++)
    { sarea.addRect(states[j]); }

    int k = 0;
    for (int i = 0; i < n; i++)
    { for (int j = i+1; j < n; j++)
      { LineData ld = 
          new LineData(xs[i] + 55 - 5*i,
                       ys[i] + 5, 
                       xs[j] + 55,
                       ys[j] + 5,k,1);
        k++;
        LineData ld2 = 
          new LineData(xs[j] + 5,
                       ys[j] + 55, 
                       xs[i] + 5 + 5*i,
                       ys[i] + 55,k,1);
        k++; 
        ld.setName(name + "set" + snames.get(j));
        ld2.setName(name + "set" + snames.get(i));
        sarea.addLine(ld);
        sarea.addLine(ld2);
      }
    }
  }

  public String saveData() 
  { return "Attribute " + name + " " + snames.size(); }  

  public void setParameters(Vector params) 
  { System.out.println("Attribute params = " + params); 
    if (params.size() >= 2)
    { try 
      { name = (String) params.get(0); 
        String ss = (String) params.get(1); 
        int x = Integer.parseInt(ss); 
        snames = Named.namesOfNumberRange(0,x-1); 
        System.out.println("Recovered states: " + snames); 
      } 
      catch (Exception e) 
      { System.err.println("Attribute component with invalid size " + params.get(1)); 
        snames = new Vector(); 
      } 
    }
    super.setParameters(params); 
  } 

  public static void main(String[] args) 
  { WinHandling wh = new WinHandling(); 
    JFrame jf = new JFrame(); 
    AttributeBuilder ab = new AttributeBuilder(jf); 
    // Vector names = new Vector(); 
    // names.add("one"); 
    // names.add("two"); 
    // names.add("three"); 
    // names.add("four"); 
    // names.add("five"); 
    // ab.setParameters(names); 
    wh.makeNewWindow("sm",ab); 
    System.out.println(wh.state_window.getDrawArea().getMinX()); 
    System.out.println(wh.state_window.getDrawArea().getMinY());
    System.out.println(wh.state_window.getDrawArea().getMaxX());
    System.out.println(wh.state_window.getDrawArea().getMaxY());
  } 
}







