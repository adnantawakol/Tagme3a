package com.example.tagme3a;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getCategory {
    public List<category> getData(){
        List<category> dd = new ArrayList<>();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getData("select * from category");
        try {
            while (rs.next()) {
                category cc = new category();
                cc.setCatID(rs.getString(1));
                cc.setCatName(rs.getString(2));
                cc.setCatImg(rs.getString(3));
                dd.add(cc);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dd;
    }
}
