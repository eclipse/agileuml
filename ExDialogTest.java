import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

public class ExDialogTest extends JFrame
{ public static void main(String[] args)
  { ExDialogTest d = new ExDialogTest();
    d.setSize(500, 400);
    d.setVisible(true);  
    // Vector labs = new Vector();
    // labs.add("Name");
    // labs.add("Address");
    // labs.add("Phone");
    // labs.add("Postcode"); 
    String[] labs = {"Name", "Address", "Phone", "Postcode"}; 
    String[] res = {"", "", "", ""}; 
    ExDialog2 d1 = new ExDialog2(d,"Customer Details",
                               labs,res);
    d1.pack();
    d1.setLocationRelativeTo(d); 
    d1.setVisible(true);
    d1.show();
    String nme = d1.getField(0);
    String adr = d1.getField(1);
    String ph = d1.getField(2);
    System.out.println(nme + " " + adr + " " + ph);
  }
}

