package com.example.tagme3a;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.Dialog;
import android.content.DialogInterface;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tagme3a.ui.home.HomeFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemActivity extends AppCompatActivity {
    ListView lv;
    item model;
    getItem gg = new getItem();
    AdapterItem adapterItem;
    ArrayList<item> dd;
    String code;
    public static String dno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        if(!HomeFragment.dno.isEmpty())
            dno = HomeFragment.dno;
        else
            dno = ItemActivity.dno;

        lv = findViewById(R.id.lv);
        dd = new ArrayList<>(gg.getData(dno));
        adapterItem = new AdapterItem(this,dd);
        lv.setAdapter(adapterItem);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(ItemActivity.this, R.anim.fadein), 0.5f); //0.5f == time between appearance of listview items.
        lv.setLayoutAnimation(lac);
        lv.startLayoutAnimation();
        SwipeRefreshLayout swpp = findViewById(R.id.swpp);
        swpp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               /* lv.startLayoutAnimation();
                dd = new ArrayList<>(gg.getData(HomeFragment.dno));
                adapterItem = new AdapterItem(ItemActivity.this,dd);
                lv.setAdapter(adapterItem);*/
                finish();
                startActivity(getIntent());
                swpp.setRefreshing(false);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                model = dd.get(position);
                LayoutInflater inflater = LayoutInflater.from(ItemActivity.this);
                View vv = inflater.inflate(R.layout.item_details,null);
                ImageView imgItem2 = vv.findViewById(R.id.imgItem2);
                TextView txtvName2 = vv.findViewById(R.id.txtvItemName2);
                TextView txtvDescription = vv.findViewById(R.id.txtvDescription);
                TextView txtvspec = vv.findViewById(R.id.txtvspec);
                TextView txtvPrice2 = vv.findViewById(R.id.txtvPrice2);
                ImageView imgBrand = vv.findViewById(R.id.imgBrand);
                EditText txtQty = vv.findViewById(R.id.txtQty);
                if(Integer.parseInt(HomeFragment.dno) == 2 || Integer.parseInt(HomeFragment.dno) == 3 || Integer.parseInt(HomeFragment.dno) == 5 || Integer.parseInt(HomeFragment.dno) == 6 || Integer.parseInt(HomeFragment.dno) == 7 || Integer.parseInt(HomeFragment.dno) == 9 )
                    txtQty.setEnabled(false);
                Button btnAddToBuild = vv.findViewById(R.id.btnDelete);
                txtvName2.setText(model.getiName());
                PicassoClient.downloadImage(ItemActivity.this,model.getiBrandLogo(),imgBrand);
                Database DB = new Database();
                Connection conn = DB.connect();
                ResultSet rs = DB.getData("Select * from Full_Specs where ItemID = '"+ model.getItemID() +"'");
                String zz = "";
                try {
                    if (rs.next()) {
                        ResultSetMetaData rsMetaData = rs.getMetaData();
                        for (int i=1;i<=rsMetaData.getColumnCount();i++){       //code to display specs of item
                            if(rs.getString(i)!=null && !rs.getString(i).equals("")) {
                                if (!rsMetaData.getColumnName(i).contains("ID") && i!=28 && i!=30 && i!=43 && i!=44) {
                                    zz += (rsMetaData.getColumnName(i)+": " + rs.getString(i) + "\n");
                                }
                                else if ((i==28 || i==30)) {  //    No Of Cores 27
                                    rs.getString(27);
                                    if (!rs.wasNull()) {
                                    //if (rs.getInt(31) != 1) {
                                        if(rs.getBoolean(i)) {
                                            zz += (rsMetaData.getColumnName(i)+": ✅" + "\n");
                                        }
                                        else {
                                            zz += (rsMetaData.getColumnName(i)+": ❌" + "\n");
                                        }
                                    }
                                }
                                else if ((i==43 || i==44) && rs.getInt(37) != 1) {     //chipsetID 37
                                    if(rs.getBoolean(i)) {
                                        zz += (rsMetaData.getColumnName(i)+": ✅" + "\n");
                                    }
                                    else {
                                        zz += (rsMetaData.getColumnName(i)+": ❌" + "\n");
                                    }
                                }
                            }
                        }
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if(model.getiDescription() == null)
                    txtvDescription.setText("No Description for this item.");
                else
                    txtvDescription.setText(model.getiDescription());

                txtvspec.setText(zz);
                txtvPrice2.setText(model.getiPrice() + " LE");
                PicassoClient.downloadImage(ItemActivity.this,model.getiImage(),imgItem2);

                BottomSheetDialog bt = new BottomSheetDialog(ItemActivity.this);
                bt.setContentView(vv);
                bt.show();
                btnAddToBuild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!txtQty.getText().toString().matches("") && Integer.parseInt(txtQty.getText().toString()) > 0) {
                            LayoutInflater inflaterr = LayoutInflater.from(ItemActivity.this);
                            View uu = inflaterr.inflate(R.layout.choose_build, null);
                            //BottomSheetDialog bt = new BottomSheetDialog(ItemActivity.this);
                            Dialog di = new Dialog(ItemActivity.this);
                            di.setContentView(uu);
                            di.show();
                            //make dialog match parent width:
                            Window window = di.getWindow();
                            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                            Button btnAddToThisBuild = uu.findViewById(R.id.btnAddToThisBuild);
                            Button btnCreateAdd = uu.findViewById(R.id.btnCreateAdd);
                            EditText txtBName = uu.findViewById(R.id.txtBName);
                            ResultSet rs = DB.getData("select * from build where userID = '" + LoginActivity.ID + "'");  //builds made by this user

                            List<String> spinnerArray = new ArrayList<>();
                            List<String> spinnerArray2 = new ArrayList<>();
                            try {
                                while (rs.next()) {
                                    spinnerArray.add(rs.getString(4));     //build names of this user
                                    spinnerArray2.add(rs.getString(1));     //buildID
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ItemActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Spinner spinner = uu.findViewById(R.id.spinner);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    code = spinnerArray2.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            btnAddToThisBuild.setOnClickListener(new View.OnClickListener() {     //Button add to existing build
                                @Override
                                public void onClick(View v) {
                                    if(Integer.parseInt(HomeFragment.dno) == 3)
                                    {
                                        // When adding a processor
                                        ResultSet rsSimilarExists = DB.getData("SELECT * FROM listOfItems INNER JOIN item ON listofitems.itemid = item.itemid INNER JOIN specifications on listOfItems.itemID = specifications.itemID WHERE listofitems.buildid ='"+code+"' AND item.categoryID = '"+HomeFragment.dno+"'");
                                        try {
                                            if (rsSimilarExists.next())      //if build already has a processor either replace it or leave it
                                            {
                                                Toast.makeText(ItemActivity.this, "hi", Toast.LENGTH_SHORT).show();
                                                AlertDialog.Builder zozo = new AlertDialog.Builder(ItemActivity.this)
                                                        .setTitle("Warning")
                                                        .setMessage("You have already added a processor to this build: '" + rsSimilarExists.getString(7) +"'")
                                                        .setNegativeButton("Cancel", null)
                                                        .setPositiveButton("Remove it and add this", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String ss = null;
                                                                try {
                                                                    ss = DB.Rundml("update listOfItems set itemID ='" + model.getItemID() + "', quantity = 1 where itemID = '" + rsSimilarExists.getString(2) + "' AND buildID = '" + code + "'");
                                                                } catch (SQLException throwables) {
                                                                    throwables.printStackTrace();
                                                                }
                                                                if (ss.equals("ok")) {
                                                                    Toast.makeText(ItemActivity.this, "Item modified", Toast.LENGTH_SHORT).show();
                                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                                    bt.dismiss();
                                                                } else
                                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                zozo.create().show();
                                            }
                                            else{
                                                String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                                if (ss.equals("ok")) {
                                                    Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                    bt.dismiss();
                                                } else
                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    }
                                    else if(Integer.parseInt(HomeFragment.dno) == 4) {
                                        // When adding a RAM
                                        ResultSet rsSimilarExists = DB.getData("SELECT * FROM listOfItems INNER JOIN item ON listofitems.itemid = item.itemid WHERE listofitems.buildid ='" + code + "' AND item.categoryID = '" + HomeFragment.dno + "'");
                                        ResultSet MBSpecs = DB.getData("SELECT * FROM listOfItems INNER JOIN specifications on listOfItems.itemID = specifications.itemID WHERE specifications.RAM_Slots IS NOT NULL AND buildID = '" + code + "'");
                                        int ramsInBuild = 0;
                                        int maxRamSlots = 16;
                                        try {
                                            while (rsSimilarExists.next()) {
                                                ramsInBuild += rsSimilarExists.getInt(3);
                                            }
                                            if (MBSpecs.next()) {
                                                maxRamSlots = MBSpecs.getInt("RAM_Slots");
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }

                                        if (ramsInBuild == 0 && Integer.parseInt(txtQty.getText().toString()) <= maxRamSlots ) {
                                            String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                            if (ss.equals("ok")) {
                                                Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                bt.dismiss();
                                            } else
                                                Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();

                                        }
                                        else if (ramsInBuild >= 0 && ramsInBuild + Integer.parseInt(txtQty.getText().toString()) <= maxRamSlots )
                                        {
                                            ResultSet rs = DB.getData("select * from listOfItems where buildID ='" + code + "'AND itemID = '" + model.getItemID() + "'");
                                            //if item already exists just update its quantity
                                            int countItems = 0;
                                            try {
                                                while (rs.next()) {
                                                    countItems += rs.getInt(3);
                                                }
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                            if (countItems == 0) {
                                                String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                                if (ss.equals("ok")) {
                                                    Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                    bt.dismiss();
                                                } else
                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();

                                            }
                                            //if item already exists just update its quantity
                                            else {
                                                int finalCountItems = countItems;       // To be able to use it inside the alert dialog
                                                AlertDialog.Builder updtQty = new AlertDialog.Builder(ItemActivity.this)
                                                        .setTitle("Item Already exists in this build")
                                                        .setMessage("Would you like to increase its quantity?")
                                                        .setNegativeButton("No",null)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String ss = DB.Rundml("update listOfItems set quantity ='" + (finalCountItems + Integer.parseInt(txtQty.getText().toString())) + "' where itemID = '" + model.getItemID() + "'AND buildID = '" + code + "'");
                                                                if (ss.equals("ok")) {
                                                                    Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                                    bt.dismiss();
                                                                } else
                                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                updtQty.create().show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(ItemActivity.this, "The Motherboard in this build doesn't This number of RAM sticks, Max is: "+maxRamSlots, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else if(Integer.parseInt(HomeFragment.dno) == 5)
                                    {
                                        // When adding a motherboard
                                        ResultSet rsSimilarExists = DB.getData("SELECT * FROM listOfItems INNER JOIN item ON listofitems.itemid = item.itemid WHERE listofitems.buildid ='"+code+"' AND item.categoryID = '"+HomeFragment.dno+"'");
                                        try {
                                            if (rsSimilarExists.next())      //if build already has a Case either replace it or leave it
                                            {

                                                AlertDialog.Builder zozo = new AlertDialog.Builder(ItemActivity.this)
                                                        .setTitle("Warning")
                                                        .setMessage("You have already added a Case in this build: '" + rsSimilarExists.getString(7) +"'")
                                                        .setNegativeButton("Cancel", null)
                                                        .setPositiveButton("Remove it and add this", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String ss = null;
                                                                try {
                                                                    ss = DB.Rundml("update listOfItems set itemID ='" + model.getItemID() + "', quantity = 1 where itemID = '" + rsSimilarExists.getString(2) + "' AND buildID = '" + code + "'");
                                                                } catch (SQLException throwables) {
                                                                    throwables.printStackTrace();
                                                                }
                                                                if (ss.equals("ok")) {
                                                                    Toast.makeText(ItemActivity.this, "Item modified", Toast.LENGTH_SHORT).show();
                                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                                    bt.dismiss();
                                                                } else
                                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                zozo.create().show();
                                            }
                                            else
                                            {
                                                //if not add it directly
                                                String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                                if (ss.equals("ok")) {
                                                    Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                    bt.dismiss();
                                                } else
                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    }
                                    else if(Integer.parseInt(HomeFragment.dno) == 6)
                                    {
                                        // When adding a motherboard
                                        ResultSet rsSimilarExists = DB.getData("SELECT * FROM listOfItems INNER JOIN item ON listofitems.itemid = item.itemid WHERE listofitems.buildid ='"+code+"' AND item.categoryID = '"+HomeFragment.dno+"'");
                                        ResultSet ramsIn = DB.getData("SELECT * FROM listOfItems INNER JOIN specifications on listOfItems.itemID = specifications.itemID WHERE specifications.CAS_Latency IS NOT NULL AND buildID = '" + code + "'");
                                        try {
                                            int MBID = 0;       //motherboard id used in getting ramslots of it
                                            int mbRams = 16;
                                            int countRam = 0;
                                            ResultSet rsMbRams = DB.getData("SELECT RAM_Slots FROM specifications where itemID = '"+model.getItemID()+"'");
                                            if(rsMbRams.next())
                                            {
                                                mbRams = rsMbRams.getInt(1);
                                            }
                                            while(ramsIn.next())
                                            {
                                                countRam += ramsIn.getInt(3);
                                            }
                                            if(countRam > mbRams)
                                            {
                                                Toast.makeText(ItemActivity.this, "Build has more RAM sticks than this motherboard capability", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                if (rsSimilarExists.next())      //if build already has a Motherboard either replace it or leave it
                                                {
                                                    AlertDialog.Builder zozo = new AlertDialog.Builder(ItemActivity.this)
                                                            .setTitle("Warning")
                                                            .setMessage("You have already added a Motherboard to this build: '" + rsSimilarExists.getString(7) +"'")
                                                            .setNegativeButton("Cancel", null)
                                                            .setPositiveButton("Remove it and add this", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    String ss = null;
                                                                    try {
                                                                        ss = DB.Rundml("update listOfItems set itemID ='" + model.getItemID() + "', quantity = 1 where itemID = '" + rsSimilarExists.getString(2) + "' AND buildID = '" + code + "'");
                                                                    } catch (SQLException throwables) {
                                                                        throwables.printStackTrace();
                                                                    }
                                                                    if (ss.equals("ok")) {
                                                                        Toast.makeText(ItemActivity.this, "Item modified", Toast.LENGTH_SHORT).show();
                                                                        di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                                        bt.dismiss();
                                                                    } else
                                                                        Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    zozo.create().show();
                                                }
                                                else
                                                {
                                                    //if not add it directly
                                                    String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                                    if (ss.equals("ok")) {
                                                        Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                        di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                        bt.dismiss();
                                                    } else
                                                        Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    }
                                    else if(Integer.parseInt(HomeFragment.dno) == 7)
                                    {
                                        // When adding a motherboard
                                        ResultSet rsSimilarExists = DB.getData("SELECT * FROM listOfItems INNER JOIN item ON listofitems.itemid = item.itemid WHERE listofitems.buildid ='"+code+"' AND item.categoryID = '"+HomeFragment.dno+"'");
                                        try {
                                            if (rsSimilarExists.next())      //if build already has a Power Supply either replace it or leave it
                                            {

                                                AlertDialog.Builder zozo = new AlertDialog.Builder(ItemActivity.this)
                                                        .setTitle("Warning")
                                                        .setMessage("You have already added a Power Supply to this build: '" + rsSimilarExists.getString(7) +"'")
                                                        .setNegativeButton("Cancel", null)
                                                        .setPositiveButton("Remove it and add this", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String ss = null;
                                                                try {
                                                                    ss = DB.Rundml("update listOfItems set itemID ='" + model.getItemID() + "', quantity = 1 where itemID = '" + rsSimilarExists.getString(2) + "' AND buildID = '" + code + "'");
                                                                } catch (SQLException throwables) {
                                                                    throwables.printStackTrace();
                                                                }
                                                                if (ss.equals("ok")) {
                                                                    Toast.makeText(ItemActivity.this, "Item modified", Toast.LENGTH_SHORT).show();
                                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                                    bt.dismiss();
                                                                } else
                                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                zozo.create().show();
                                            }
                                            else
                                            {
                                                //if not add it directly
                                                String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                                if (ss.equals("ok")) {
                                                    Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                    bt.dismiss();
                                                } else
                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    }
                                    else if(Integer.parseInt(HomeFragment.dno) == 9)
                                    {
                                        // When adding a motherboard
                                        ResultSet rsSimilarExists = DB.getData("SELECT * FROM listOfItems INNER JOIN item ON listofitems.itemid = item.itemid WHERE listofitems.buildid ='"+code+"' AND item.categoryID = '"+HomeFragment.dno+"'");
                                        try {
                                            if (rsSimilarExists.next())      //if build already has a Cooler either replace it or leave it
                                            {

                                                AlertDialog.Builder zozo = new AlertDialog.Builder(ItemActivity.this)
                                                        .setTitle("Warning")
                                                        .setMessage("You have already added a Cooler to this build: '" + rsSimilarExists.getString(7) +"'")
                                                        .setNegativeButton("Cancel", null)
                                                        .setPositiveButton("Remove it and add this", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String ss = null;
                                                                try {
                                                                    ss = DB.Rundml("update listOfItems set itemID ='" + model.getItemID() + "', quantity = 1 where itemID = '" + rsSimilarExists.getString(2) + "' AND buildID = '" + code + "'");
                                                                } catch (SQLException throwables) {
                                                                    throwables.printStackTrace();
                                                                }
                                                                if (ss.equals("ok")) {
                                                                    Toast.makeText(ItemActivity.this, "Item modified", Toast.LENGTH_SHORT).show();
                                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                                    bt.dismiss();
                                                                } else
                                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                zozo.create().show();
                                            }
                                            else
                                            {
                                                //if not add it directly
                                                String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                                if (ss.equals("ok")) {
                                                    Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                    di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                    bt.dismiss();
                                                } else
                                                    Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    }
                                    else {      //If item category is not in the previous categories
                                        ResultSet rs = DB.getData("select * from listOfItems where buildID ='" + code + "'AND itemID = '" + model.getItemID() + "'");
                                        //if item already exists just update its quantity
                                        int countItems = 0;
                                        try {
                                            while (rs.next()) {
                                                countItems += rs.getInt(3);
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                        if (countItems == 0) {
                                            String ss = DB.Rundml("insert into listOfItems values ('" + code /*BuildID*/ + "', '" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                            if (ss.equals("ok")) {
                                                Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                bt.dismiss();
                                            } else
                                                Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();

                                        }
                                        //if item already exists just update its quantity
                                        else {
                                            int finalCountItems = countItems;       // To be able to use it inside the alert dialog
                                            AlertDialog.Builder updtQty = new AlertDialog.Builder(ItemActivity.this)
                                                    .setTitle("Item Already exists in this build")
                                                    .setMessage("Would you like to increase its quantity?")
                                                    .setNegativeButton("No",null)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String ss = DB.Rundml("update listOfItems set quantity ='" + (finalCountItems + Integer.parseInt(txtQty.getText().toString())) + "' where itemID = '" + model.getItemID() + "'AND buildID = '" + code + "'");
                                                            if (ss.equals("ok")) {
                                                                Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                                di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog
                                                                bt.dismiss();
                                                            } else
                                                                Toast.makeText(ItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                            updtQty.create().show();
                                        }
                                    }
                                }
                            });
                            btnCreateAdd.setOnClickListener(new View.OnClickListener() {        //Button Create a new build and add this item to it
                                @Override
                                public void onClick(View v) {
                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                                    String formattedDate = df.format(c);
                                    String zz = DB.Rundml("insert into build values ('" + formattedDate + "','" + LoginActivity.ID + "','" + txtBName.getText() + "')");
                                    if (!txtBName.getText().toString().equals("")) {
                                        if (zz == "ok") {
                                            Toast.makeText(ItemActivity.this, "Build Created", Toast.LENGTH_SHORT).show();
                                            ResultSet rs = DB.getData("select max(buildID) as buildID from build where userID = '" + LoginActivity.ID + "'");

                                            Integer BuildIDInserted = null;

                                            try {
                                                if (rs.next()) {
                                                    BuildIDInserted = Integer.valueOf(rs.getString(1));
                                                }
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                            String zzz = DB.Rundml("insert into listOfItems values ('" + BuildIDInserted + "','" + model.getItemID() + "', '" + txtQty.getText() + "')");
                                            if (zzz == "ok") {
                                                Toast.makeText(ItemActivity.this, "Item added to build", Toast.LENGTH_SHORT).show();
                                                //startActivity(new Intent(ItemActivity.this,ItemActivity.class));  //first way is to refresh page by new intent
                                                //finish(); returns to parent               //second way is to finish activity but it goes back to previous one
                                                di.dismiss();       //correct way is to dismiss both of dialog and bottomsheet dialog with delay
                                                bt.dismiss();
                                            } else
                                                Toast.makeText(ItemActivity.this, "ERROR: Item not inserted" + zzz, Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(ItemActivity.this, "ERROR: Build not created" + zz, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(ItemActivity.this, "Build name cannot be null", Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                        else
                            Toast.makeText(ItemActivity.this, "Item quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
