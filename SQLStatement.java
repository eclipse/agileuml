import java.util.Vector;
import java.util.List;
import java.util.ArrayList;


/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class SQLStatement
{ private String keyword;
  private List tables = new ArrayList();
  private List fields = new ArrayList();
  private List values = new ArrayList();
  String where = "";

  public SQLStatement(String kwd, ArrayList tbls,
                      List flds,
                      ArrayList vals, String pred)
  { keyword = kwd;
    tables = tbls;
    fields = flds;
    values = vals;
    where = pred;
  }

  public void buildWhere(List keylist)
  { where = "";
    for (int i = 0; i < keylist.size(); i++)
    { String key = (String) keylist.get(i);
      where = where + key + " = ?";
      if (i < keylist.size() - 1)
      { where = where + " AND "; }
    }
  }

  public static String buildWhere0(List keylist)
  { String wre = "";
    for (int i = 0; i < keylist.size(); i++)
    { String key = (String) keylist.get(i);
      wre = wre + key + " = ?";
      if (i < keylist.size() - 1)
      { wre = wre + " AND "; }
    }
    return wre;
  }

  public void buildValues()
  { for (int i = 0; i < fields.size(); i++)
    { values.add("?"); }
  }

  public static Vector buildValues(Vector v)
  { Vector res = new Vector();
    for (int i = 0; i < v.size(); i++)
    { res.add("?"); }
    return res;
  }

  private static String commaList0(List l)
  { String res = "";
    for (int i = 0; i < l.size(); i++)
    { String x = (String) l.get(i);
      res = res + x;
      if (i < l.size() - 1)
      { res = res + ","; }
    }
    return res;
  }

  private static String commaList(List l)
  { String res = "";
    for (int i = 0; i < l.size(); i++)
    { String x = (String) l.get(i);
      res = res + x;
      if (i < l.size() - 1)
      { res = res + "\",\""; }
    }
    return res;
  }

  private static String commaEqList0(List l, List v)
  { String res = "";
    for (int i = 0; i < l.size(); i++)
    { String x = (String) l.get(i);
      String y = (String) v.get(i);
      res = res + x + " = " + y;
      if (i < l.size() - 1)
      { res = res + ", "; }
    }
    return res;
  }

  private static String commaEqList(List l, List v)
  { String res = "";
    int p = Math.min(l.size(),v.size()); 
    for (int i = 0; i < p; i++)
    { String x = (String) l.get(i);
      String y = (String) v.get(i);
      res = res + x + " = \"" + y;
      if (i < l.size() - 1)
      { res = res + "\", "; }
    }
    return res;
  }

  public String toString()
  { String res = "\"" + keyword;
    if (keyword.equals("INSERT"))
    { res = res + " INTO " + commaList0(tables) +
            "(" + commaList0(fields) + ") " +
            "VALUES (\"" + commaList(values) + "\")\"";  
    }  // need to put '' round strings/booleans
    else if (keyword.equals("UPDATE"))
    { res = res + " " + commaList0(tables) +
            " SET " + commaEqList(fields,values);
      if (where != null && where.length() > 0)
      { res = res + "\" WHERE " + where +"\""; }  
      else 
      { res = res + "\""; }
    }  // need to put '' round strings/booleans
    else if (keyword.equals("DELETE"))
    { res = res + " FROM " + commaList0(tables);
      if (where != null && where.length() > 0)
      { res = res + " WHERE " + where + "\""; }  
      else 
      { res = res + "\""; }

    }
    else if (keyword.equals("SELECT"))
    { res = res + " " + commaList0(fields) +
            " FROM " + commaList0(tables);
      if (where != null && where.length() > 0)
      { res = res + " WHERE " + where + "\""; }  
      else 
      { res = res + "\""; }
    }  // need to put '' round strings/booleans
    return res;
  }

  public String preparedStatement()
  { String res = "\"" + keyword;
    if (keyword.equals("INSERT"))
    { buildValues();
      res = res + " INTO " + commaList0(tables) +
            " (" + commaList0(fields) + ") " +
            "VALUES (" + commaList0(values) + ")\"";  
    }  
    else if (keyword.equals("UPDATE"))
    { buildValues();
      res = res + " " + commaList0(tables) +
            " SET " + commaEqList0(fields,values);
      if (where != null && where.length() > 0)
      { res = res + " WHERE " + where +"\""; }  
      else 
      { res = res + "\""; }
    }  // need to put '' round strings/booleans
    else if (keyword.equals("DELETE"))
    { res = res + " FROM " + commaList0(tables);
      buildWhere(fields);
      if (where != null && where.length() > 0)
      { res = res + " WHERE " + where + "\""; }  
      else 
      { res = res + "\""; }
    }
    else if (keyword.equals("SELECT"))
    { res = res + " " + commaList0(fields) +
            " FROM " + commaList0(tables);
      if (where != null && where.length() > 0)
      { res = res + " WHERE " + where + "\""; }  
      else 
      { res = res + "\""; }
    }  // need to put '' round strings/booleans
    return res;
  }

  public static void main(String[] args)
  { ArrayList ents = new ArrayList();
    ents.add("User");
    ArrayList flds = new ArrayList();
    flds.add("userId");
    flds.add("name");
    flds.add("email");
    
    SQLStatement ss = 
      new SQLStatement("INSERT",ents,flds,
                       new ArrayList(),"");
    System.out.println(ss.preparedStatement());
  }
}

/*
  public String toSQL()
  { String ls = left.toSQL();
    String rs = right.toSQL();
    String res;
    if (operator.equals("&"))
    { res = ls + " AND " + rs; }
    else if (operator.equals("or"))
    { res = ls + " OR " + rs; }
    else 
    { res = ls + " " + operator + " " + rs; }
    if (needsBracket)
    { res = "( " + res + " )"; }
    return res;
  }
  
   if (umlkind == VALUE)
   { String tname = type.getName();
     if (tname.equals("String") ||
         tname.equals("boolean"))
     { res = "'" + data + "'"; }
   }
   res = data;
*/
