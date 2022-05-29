package com.example.tagme3a;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class    getBuildItem {
    public List<buildItem> getData(String BuildID){
        List<buildItem> dd = new ArrayList<>();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getData("select * from item left join item_image on item.itemID = item_image.itemID inner join brand on item.brandid = brand.brandid inner join listOfItems on listOfItems.itemID = item.itemID where BuildID = '"+ BuildID +"'");
        try {
            while (rs.next()) {
                buildItem ii = new buildItem();
                ii.setItemID(rs.getString(1));
                ii.setiPrice(rs.getString(2));
                ii.setiName(rs.getString(3));
                ii.setiDescription(rs.getString(4));
                ii.setiCat(rs.getString(5));
                ii.setiImage(rs.getString(7));
                ii.setiBrandLogo(rs.getString(11));
                ii.setiQty(rs.getString(15));
                ii.setLID(rs.getString(16));

                dd.add(ii);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dd;
    }
}
