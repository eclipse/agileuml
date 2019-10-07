import java.util.Vector;
import java.io.*;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class SkipOperationSpec extends AbstractOpSpec
{ String eventName;
  String sensorName;

  SkipOperationSpec(String e, String sen)
  { eventName = e;
    sensorName = sen; 
  }

  public void display()
  { if (prefix != null && !prefix.equals(""))
    { System.out.print(prefix + "_"); } 
    System.out.println(eventName + " = ");
    System.out.print("  " + sensorName + "_" + eventName);
  }

  public void displayJava()
  { System.out.println("  public void " + eventName + "()");
    System.out.println("  { M" + sensorName + "." +
                   sensorName + "_" + eventName + "();");
    System.out.println("  }\n");
  }

  public void displayImp()
  { display(); } 

  public void display(PrintWriter out)
  { if (prefix != null && !prefix.equals(""))
    { out.print(prefix + "_"); }

    out.println(eventName + " = "); 
    out.print("  " + sensorName + "_" + eventName);
  }

  public void displayJava(PrintWriter out)
  { out.println("public void " + eventName + "()");
    out.println("{ M" + sensorName + "." +
                   sensorName + "_" + eventName + "();"); 
    out.println("}\n"); 
  }

  public void displayImp(PrintWriter out)
  { display(out); }
}

