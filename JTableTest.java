import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class DatabaseTableModel implements TableModel
{ Square[][] cells = new Square[9][9];

  DatabaseTableModel(Square[][] squares)
  { cells = squares; }

  public void addTableModelListener(TableModelListener l) { } 

  public void removeTableModelListener(TableModelListener l) { } 


  public Class getColumnClass(int cind)  
  { try
    { return Class.forName("java.lang.String"); }
    catch (ClassNotFoundException e) 
    { System.out.println("Class not found"); 
      return null; } 
  }

  public int getColumnCount()  { return 9; }

  public String getColumnName(int cind)
  { // if (cind == 0)  { return "Name"; }
    // if (cind == 1)  { return "Address"; }
    // if (cind == 2)  { return "Phone"; } 
    return null; }

  public int getRowCount()  {  return 9; }

  public Object getValueAt(int rind, int cind)
  { if (inRange(rind,cind))
    { return cells[rind][cind].getvalue() + ""; } 
    return null;
  }
  
  public boolean isCellEditable(int rind, int cind)
  { return inRange(rind,cind); }

  public void setValueAt(Object val, int rind, int cind)
  { if (inRange(rind,cind))
    { try 
      { int v = Integer.parseInt(val + ""); 
        cells[rind][cind].setvalue(v); 
      }
      catch (Exception e) { } 
    }
  }


  private boolean inRange(int rind, int cind)
  { return (0 <= rind && rind < 9 &&
            0 <= cind && cind < 9); }
}

public class JTableTest extends JFrame
{ 
  
  public JTableTest(Square[][] squares) 
  { 
    TableModel tm = 
      new DatabaseTableModel(squares); 

    DefaultTableColumnModel dtcm = 
      new DefaultTableColumnModel(); 
    // String[] colHeadings = { "Name", "Address", "Phone"}; 
    for (int i = 0; i < 9; i++) 
    { TableColumn tc = 
        new TableColumn(i); 
      tc.setWidth(100); 
      dtcm.addColumn(tc); }
    JTableHeader header = new JTableHeader(dtcm);  

    JTable p = new JTable(tm,dtcm); 
    header.setTable(p);

    getContentPane().add(p, BorderLayout.CENTER); 
    addWindowListener(new WindowAdapter()
        { public void windowClosing(WindowEvent e)
          { 
            System.exit(0); } 
        }); 
  }

}    
