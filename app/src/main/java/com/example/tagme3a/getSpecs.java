package com.example.tagme3a;

import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getSpecs {
    public getSpecs(String zz,String itemID) {
        Database DB = new Database();
        Connection conn = DB.connect();
        ResultSet rs = DB.getData("Select * from Full_Specs where ItemID = '"+ itemID +"'");
        try {
            if (rs.next()) {
                //specs ii = new specs();
                ResultSetMetaData rsMetaData = rs.getMetaData();
                //String tempp = rs.getString(i);
                for (int i=1;i<=rsMetaData.getColumnCount();i++){       //code to display specs of item

                    String temp2 = rs.getString(35);
                    if (rs.wasNull()) {
                        temp2 = ""; // set it to empty string as you desire.
                    }
                    if(rs.getString(i)!=null) {
                        if (!rsMetaData.getColumnName(i).contains("ID") && i!=28 && i!=30 && i!=40 && i!=41) {
                            zz += (rsMetaData.getColumnName(i)+": " + rs.getString(i) + "\n");
                        }
                        else if ((i==28 || i==30)) {  //socket 31
                            rs.getString(31);
                            if (!rs.wasNull()) {
                                if(rs.getString(i).equals("1")) {
                                    zz += (rsMetaData.getColumnName(i)+": ✅" + "\n");
                                }
                                else if (rs.getString(i).equals("0") && rs.getString(27)!=null ) {
                                    zz += (rsMetaData.getColumnName(i)+": ❌" + "\n");
                                }
                            }
                        }
                        else if ((i==40 || i==41) && !temp2.equals(null)) {     //chipset 35,
                            rs.getString(35);
                            if (!rs.wasNull()) {
                                if(rs.getString(i).equals("1")) {
                                    zz += (rsMetaData.getColumnName(i)+": ✅" + "\n");
                                }
                                else if (rs.getString(i).equals("0") && rs.getString(27)!=null ) {
                                    zz += (rsMetaData.getColumnName(i)+": ❌" + "\n");
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*public String getData(String no){

    }*/
}
