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

interface TableCell
{ public String getcellvalue();
  public void setcellvalue(String cellvaluex); 
}

class DatabaseTableModel implements TableModel
{ TableCell[][] cells;
  int cols;
  int rows;

  DatabaseTableModel(TableCell[][] cellsx, int colsx, int rowsx)
  { cells = cellsx; 
    rows = rowsx;
    cols = colsx;
  }

  public void addTableModelListener(TableModelListener l) { } 

  public void removeTableModelListener(TableModelListener l) { } 


  public Class getColumnClass(int cind)  
  { try
    { return Class.forName("java.lang.String"); }
    catch (ClassNotFoundException e) 
    { System.out.println("Class not found"); 
      return null; } 
  }

  public int getColumnCount()  { return cols; }

  public String getColumnName(int cind)
  { return null; }

  public int getRowCount()  { return rows; }

  public Object getValueAt(int cind, int rind)
  { if (inRange(cind,rind))
    { return cells[cind][rind].getcellvalue(); } 
    return null;
  }
  
  public boolean isCellEditable(int cind, int rind)
  { return inRange(cind,rind); }

  public void setValueAt(Object val, int cind, int rind)
  { if (inRange(cind,rind))
    { try 
      { cells[cind][rind].setcellvalue(val + ""); }
      catch (Exception e) { } 
    }
  }

  private boolean inRange(int cind, int rind)
  { return (0 <= rind && rind < rows &&
            0 <= cind && cind < cols); }
}


public class JTableBuilder
{ 
  public JTable buildTable(TableCell[][] cells, int cols, int rows) 
  { TableModel tm = 
      new DatabaseTableModel(cells,cols,rows); 

    DefaultTableColumnModel dtcm = 
      new DefaultTableColumnModel(); 
    for (int i = 0; i < cols; i++) 
    { TableColumn tc = 
        new TableColumn(i); 
      dtcm.addColumn(tc); 
    }
    JTableHeader header = new JTableHeader(dtcm);  

    JTable p = new JTable(tm,dtcm); 
    header.setTable(p);
    return p;
  }
}    
