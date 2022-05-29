package com.example.tagme3a;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getBuild {
    public List<build> getData(String UserID){
        List<build> dd = new ArrayList<>();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getData("select build.buildID, build.date, build.userID, build.buildName, [user].name  from [build] inner join [user] on build.userID = [user].userID where [user].userID ='"+ UserID +"'");
        try {
            while (rs.next()) {
                build bb = new build();
                bb.setBuildID(rs.getString(1));
                bb.setDate(rs.getString(2));
                bb.setUserID(rs.getString(3));
                bb.setBuildName(rs.getString(4));
                bb.setUserName(rs.getString(5));
                ResultSet rs2 = db.getData("select item.price, listOfItems.quantity from listOfItems inner join item on listOfItems.itemID =  item.itemID where buildID = '"+rs.getString(1) +"'");
                Float total = 0F;
                while (rs2.next()){
                    total+=rs2.getFloat(1)*rs2.getInt(2);
                }
                bb.setTotal(total.toString());

                dd.add(bb);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dd;
    }
}
