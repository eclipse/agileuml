import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.util.function.Function;
import java.io.Serializable;

import java.sql.Connection; 
import java.sql.Statement; 
import java.sql.PreparedStatement; 
import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.ResultSetMetaData;
import java.sql.SQLException; 
import java.sql.Timestamp;
import java.sql.Types; 


class SQLStatement { 
  static ArrayList<SQLStatement> SQLStatement_allInstances = new ArrayList<SQLStatement>();

  SQLStatement() { SQLStatement_allInstances.add(this); }

  static SQLStatement createSQLStatement() 
  { SQLStatement result = new SQLStatement();
    return result; 
  }

  String text = "";
  String sqlstatementId = ""; /* primary */
  static Map<String,SQLStatement> SQLStatement_index = new HashMap<String,SQLStatement>();

  Statement statement = null;  // The actual statement
  Connection connection = null; 
  OclIterator resultSet = null; // most-recent results
  OclDatasource database = null; 

  static SQLStatement createByPKSQLStatement(String sqlstatementIdx)
  { SQLStatement result = SQLStatement.SQLStatement_index.get(sqlstatementIdx);
    if (result != null) { return result; }
    result = new SQLStatement();
    SQLStatement.SQLStatement_index.put(sqlstatementIdx,result);
    result.sqlstatementId = sqlstatementIdx;
    return result; 
  }

  static void killSQLStatement(String sqlstatementIdx)
  { SQLStatement rem = SQLStatement_index.get(sqlstatementIdx);
    if (rem == null) { return; }
    ArrayList<SQLStatement> remd = new ArrayList<SQLStatement>();
    remd.add(rem);
    SQLStatement_index.remove(sqlstatementIdx);
    SQLStatement_allInstances.removeAll(remd);
  }

  public static int typeToSQLType(Object obj)
  { if (obj instanceof Integer) 
    { return Types.INTEGER; } 
    if (obj instanceof Double) 
    { return Types.DOUBLE; } 
    if (obj instanceof Boolean) 
    { return Types.BOOLEAN; } 
    if (obj instanceof String) 
    { return Types.VARCHAR; } 
    return Types.OTHER; 
  } 

  public void close()
  { if (statement != null) 
    { try { 
        statement.close(); 
      } 
      catch (SQLException ex) { } 
    } 
  }


  public void closeOnCompletion()
  { if (statement != null) 
    { try { 
        statement.closeOnCompletion(); 
      } 
      catch (SQLException ex) { } 
    }
  }


  public static void setParameters(PreparedStatement s, 
                                   ArrayList pars)
  { try { 
      for (int i = 0; i < pars.size(); i++) 
      { s.setObject(i+1, pars.get(i)); } 
    } 
    catch (Exception _ex) { } 
  }  

  public void setObject(int field, Object value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setObject(
                               field,value);
      } 
      catch (SQLException ex) { }
    }
  }

  public void setString(int field, String value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setString(
                               field,value);
      } 
      catch (SQLException ex) { }
    }
  }


  public void setInt(int field, int value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setInt(
                               field,value);
      } 
      catch (SQLException ex) { }
    }
  }


  public void setByte(int field, int value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setByte(
                               field, (byte) value);
      } 
      catch (SQLException ex) { }
    }
  }


  public void setShort(int field, int value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setShort(
                               field, (short) value);
      } 
      catch (SQLException ex) { }
    }
  }


  public void setBoolean(int field, boolean value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setBoolean(
                               field, value);
      } 
      catch (SQLException ex) { }
    }
  }


  public void setLong(int field, long value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setLong(
                               field, value);
      } 
      catch (SQLException ex) { }
    }
  }


  public void setDouble(int field, double value)
  { if (statement instanceof PreparedStatement)
    { try { 
        ((PreparedStatement) statement).setDouble(
                               field, value);
      } 
      catch (SQLException ex) { }
    }
  }


  public void setTimestamp(int field, OclDate value)
  { if (statement instanceof PreparedStatement)
    { long t = value.getTime(); 
      try { 
        ((PreparedStatement) statement).setTimestamp(
                               field, 
                               new Timestamp(t));
      } 
      catch (SQLException ex) { }
    }
  }


  public void setNull(int field, Object value)
  { if (statement instanceof PreparedStatement)
    { int typ = SQLStatement.typeToSQLType(value); 
      try { 
        ((PreparedStatement) statement).setNull(
                               field, 
                               typ);
      } 
      catch (SQLException ex) { }
    }
  }


  public void executeUpdate()
  {
    if (statement != null) 
    { try { 
        statement.executeUpdate(text);
      } 
      catch (SQLException ex) { } 
    }  
  }


  public OclIterator executeQuery(String stat)
  {
    OclIterator result = null;

    ArrayList<String> cnames = new ArrayList<String>(); 
    ArrayList<Map<String,Object>> records = 
        new ArrayList<Map<String,Object>>(); 

    if (statement != null) 
    { try { 
        ResultSet rs = statement.executeQuery(stat); 

        ResultSetMetaData md = rs.getMetaData(); 
        int nc = md.getColumnCount(); 
        
        for (int i = 1; i <= nc; i++) 
        { cnames.add(md.getColumnName(i)); } 

        while (rs.next())
        { Map<String,Object> record = 
            new HashMap<String,Object>();
          for (int j = 1; j <= nc; j++) 
          { Object val = rs.getObject(j); 
            record.put(md.getColumnName(j), val); 
          } 
          records.add(record); 
        }  
      } 
      catch (SQLException ex) 
      { ex.printStackTrace(); } 
    } 

    result = OclIterator.newOclIterator_Sequence(records); 
    result.columnNames = cnames;  
    resultSet = result; 
    return result;
  }



  public OclIterator executeQuery()
  { return executeQuery(text); }


  public void execute(String stat)
  {
    if (statement != null) 
    { try { 
        statement.execute(stat);
      } catch (SQLException ex) { } 
    }  
  }


  public void execute()
  { execute(text); }


  public void cancel()
  {
  }


  public OclDatasource getConnection()
  { return database; }


  public OclIterator getResultSet()
  { return resultSet;  }

}

