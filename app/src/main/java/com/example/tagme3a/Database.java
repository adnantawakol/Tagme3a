package com.example.tagme3a;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    Connection conn = null;
    public Connection connect() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:jtds:sqlserver://sql5092.site4now.net/DB_A6B616_tagme3a","DB_A6B616_tagme3a_admin","a123456789");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }


    public String Rundml(String st)
    {
        //function for insert delete update phrases
        ResultSet rs = null;
        try {
            Statement hh = conn.createStatement();
            hh.executeUpdate(st);
            return "ok";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return throwables.getMessage(); //in case fail y2olly eh el error
        }

    }


    public ResultSet getData(String st)
    {
        ResultSet rs = null;
        try {
            Statement hh = conn.createStatement();
            rs = hh.executeQuery(st);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }
}
