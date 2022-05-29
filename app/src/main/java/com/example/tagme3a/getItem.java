package com.example.tagme3a;

import com.example.tagme3a.ui.home.HomeFragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getItem {
    public List<item> getData(String no){
        List<item> dd = new ArrayList<>();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getData("select * from item left join item_image on item.itemID = item_image.itemID inner join brand on item.brandid = brand.brandid where categoryID ='"+ no +"'");
        try {
            while (rs.next()) {
                item ii = new item();
                ii.setItemID(rs.getString(1));
                ii.setiPrice(rs.getString(2));
                ii.setiName(rs.getString(3));
                ii.setiDescription(rs.getString(4));
                ii.setiImage(rs.getString(7));
                ii.setiBrandLogo(rs.getString(11));

                dd.add(ii);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dd;
    }
}
