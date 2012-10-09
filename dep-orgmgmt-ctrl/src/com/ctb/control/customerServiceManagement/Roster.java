package com.ctb.control.customerServiceManagement; 

import java.sql.Array;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class Roster implements SQLData { 
  
  private static String SQLType = "ROSTER";
  private long rosterId;
  private Array subtestIds;
  
  public Roster(long rosterId, Array subtestIds) {
	  this.rosterId = rosterId;
	  this.subtestIds = subtestIds;
  }
  
//define a get method to return the SQL type of the object  line 24
  public String getSQLTypeName() throws SQLException
  { 
    return SQLType; 
  }                                                         // line 28
 
  // define the required readSQL() method                      line 30
  public void readSQL(SQLInput stream, String typeName)
    throws SQLException
  {
	  SQLType = typeName;
  
	  rosterId = stream.readLong();
	  subtestIds = stream.readArray();
  }  
  // define the required writeSQL() method                     line 39
  public void writeSQL(SQLOutput stream)
    throws SQLException
  { 
    stream.writeLong(rosterId);
    stream.writeArray(subtestIds);
  }
} 
