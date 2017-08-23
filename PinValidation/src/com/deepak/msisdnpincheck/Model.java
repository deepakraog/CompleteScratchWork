package com.deepak.msisdnpincheck;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class Model
{
  public static LinkedHashMap<String, Integer> dbDetails = new LinkedHashMap();
  public static Connection con = null;
  public static PreparedStatement ps_sel = null;
  public static ResultSet rs = null;

  public static void loadDB(String select) throws IOException
  {
    System.out.println("inside loadDB() of Model");
    try {
      con = JDBCHelper.getConnection();
      if (con != null) {
        ps_sel = con.prepareStatement(select);
        ps_sel.execute();
        rs = ps_sel.getResultSet();
        while (rs.next()) {
          dbDetails.put(rs.getString("pin"), Integer.valueOf(rs.getInt("param_value")));
        }
      }

    }
    catch (SQLException e)
    {
      e.printStackTrace();
    } finally {
      JDBCHelper.close(rs);
      JDBCHelper.close(ps_sel);
      JDBCHelper.close(con);
    }
  }

  public static int updateDB(String update, String select, String key) throws IOException {
    System.out.println("inside updateDB() of Model");
    int count = 0;
    try {
      con = JDBCHelper.getConnection();
      if (con != null) {
        ps_sel = con.prepareStatement(update);
        ps_sel.setInt(1, 1);
        ps_sel.setString(2, key);
        count = ps_sel.executeUpdate();
        loadDB(select);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      JDBCHelper.close(rs);
      JDBCHelper.close(ps_sel);
      JDBCHelper.close(con);
    }
    return count;
  }

  public String validatefetchedData(String key, String repeatcheck)
  {
    String result = null;
    if (dbDetails.containsKey(key)) {
      if (repeatcheck.equalsIgnoreCase("true")) {
        if (((Integer)dbDetails.get(key)).intValue() == 0)
          result = "SUCCESS";
        else
          result = "USED_PIN";
      }
      else
        result = "SUCCESS";
    }
    else {
      result = "FAILURE";
    }
    return result;
  }

  public String updatefetchedData(String key, String update, String select, String action)
  {
    String result = null;
    int rowsUpdated = 0;

    if (dbDetails.containsKey(key)) {
      if (action.equalsIgnoreCase("update"))
        try {
          rowsUpdated = updateDB(update, select, key);
          System.out.println("Updated Rows " + rowsUpdated);
          return "SUCCESS";
        } catch (IOException e) {
          e.printStackTrace();
        }
      else {
        result = "FAILURE";
      }
      if (rowsUpdated != 0) {
        result = "FAILURE";
      }
    }
    return result;
  }
}