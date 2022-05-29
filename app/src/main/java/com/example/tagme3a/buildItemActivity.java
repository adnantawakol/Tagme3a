package com.example.tagme3a;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tagme3a.ui.gallery.GalleryFragment;
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

public class buildItemActivity extends AppCompatActivity {
    ListView lvBuildItems;
    buildItem model;
    getBuildItem gg = new getBuildItem();
    AdapterBuildItem adapterBuildItem;
    ArrayList<buildItem> dd;
    //String code;
    public static String buildID;
    public static String ID = LoginActivity.ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_item);

        if(!GalleryFragment.buildID.isEmpty())
            buildID = GalleryFragment.buildID;
        else
            buildID = buildItemActivity.buildID;

        lvBuildItems = findViewById(R.id.lvBuildItems);
        dd = new ArrayList<>(gg.getData(buildID));
        adapterBuildItem = new AdapterBuildItem(this,dd);
        lvBuildItems.setAdapter(adapterBuildItem);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.fadein), 0.5f); //0.5f == time between appearance of listview items.
        lvBuildItems.setLayoutAnimation(lac);
        lvBuildItems.startLayoutAnimation();
        SwipeRefreshLayout swpBuildItems = findViewById(R.id.swpBuildItems);
        swpBuildItems.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvBuildItems.startLayoutAnimation();
                dd = new ArrayList<>(gg.getData(GalleryFragment.buildID));
                adapterBuildItem = new AdapterBuildItem(buildItemActivity.this,dd);
                lvBuildItems.setAdapter(adapterBuildItem);
                swpBuildItems.setRefreshing(false);
            }
        });
        lvBuildItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                model = dd.get(position);
                LayoutInflater inflater = LayoutInflater.from(buildItemActivity.this);
                View vv = inflater.inflate(R.layout.build_item_details,null);
                ImageView imgItem2 = vv.findViewById(R.id.imgItem2);
                TextView txtvName2 = vv.findViewById(R.id.txtvItemName2);
                TextView txtvDescription = vv.findViewById(R.id.txtvDescription);
                TextView txtvspec = vv.findViewById(R.id.txtvspec);
                TextView txtvPrice2 = vv.findViewById(R.id.txtvPrice2);
                ImageView imgBrandBuild = vv.findViewById(R.id.imgBrandBuild);
                EditText txtQty = vv.findViewById(R.id.txtQty);
                Button btnUpdate = vv.findViewById(R.id.btnUpdate);
                Button btnDelete = vv.findViewById(R.id.btnDelete);
                if(Integer.parseInt(model.getiCat()) == 2 || Integer.parseInt(model.getiCat()) == 3 || Integer.parseInt(model.getiCat()) == 5 || Integer.parseInt(model.getiCat()) == 6 || Integer.parseInt(model.getiCat()) == 7 || Integer.parseInt(model.getiCat()) == 9 )
                    txtQty.setEnabled(false);

                txtvName2.setText(model.getiName());
                PicassoClient.downloadImage(buildItemActivity.this,model.getiBrandLogo(),imgBrandBuild);
                Database DB = new Database();
                Connection conn = DB.connect();
                ResultSet rs = DB.getData("Select * from Full_Specs where ItemID = '"+ model.getItemID() +"'");
                String zz = "";
                try {
                    if (rs.next()) {
                        ResultSetMetaData rsMetaData = rs.getMetaData();
                        for (int i=1;i<=rsMetaData.getColumnCount();i++){       //code to display specs of item
                            if(rs.getString(i)!=null && rs.getString(i)!="") {
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
                txtvDescription.setText(model.getiDescription());
                txtvspec.setText(zz);
                txtQty.setText(model.getiQty());
                txtvPrice2.setText(model.getiPrice() + " LE");
                PicassoClient.downloadImage(buildItemActivity.this,model.getiImage(),imgItem2);

                BottomSheetDialog bt = new BottomSheetDialog(buildItemActivity.this);
                bt.setContentView(vv);
                bt.show();
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDelete= new AlertDialog.Builder(buildItemActivity.this)
                                .setTitle("Delete Item")
                                .setMessage("Are you sure you'd like to delete this item from build?")
                                .setNegativeButton("No",null)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String dml = DB.Rundml("DELETE from listOfItems where LID = '"+ model.getLID()+"'");
                                        if(dml == "ok") {
                                            Toast.makeText(buildItemActivity.this, "Item deleted from build successfully", Toast.LENGTH_SHORT).show();
                                            //startActivity(new Intent(buildItemActivity.this,buildItemActivity.class));
                                            bt.dismiss();
                                            finish();
                                            startActivity(getIntent());
                                        }
                                        else
                                            Toast.makeText(buildItemActivity.this, "Delete failed "+ dml, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        alertDelete.create().show();
                    }
                });
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Integer.parseInt(model.getiCat()) == 4)
                            {
                                ResultSet rsSimilarExists = DB.getData("SELECT * FROM listOfItems INNER JOIN item ON listofitems.itemid = item.itemid WHERE listofitems.buildid ='" + buildID + "' AND item.categoryID = '" + model.getiCat() + "' AND listOfItems.itemID <> '"+model.getItemID()+"'");
                                ResultSet MBSpecs = DB.getData("SELECT * FROM listOfItems INNER JOIN specifications on listOfItems.itemID = specifications.itemID WHERE specifications.RAM_Slots IS NOT NULL AND buildID = '" + buildID + "'");
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

                                if (Integer.parseInt(txtQty.getText().toString()) + ramsInBuild <= maxRamSlots )
                                {
                                    String ss = DB.Rundml("UPDATE listOfItems set quantity = '" + Integer.valueOf(txtQty.getText().toString()) + "' where LID = '" + model.getLID() + "'");
                                    if (ss.equals("ok")) {
                                        Toast.makeText(buildItemActivity.this, "Quantity updated succesfully", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(buildItemActivity.this, "ERROR Item not inserted: " + ss, Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(buildItemActivity.this, "The Motherboard in this build doesn't This number of RAM sticks, Max is: "+maxRamSlots, Toast.LENGTH_SHORT).show();
                                }
                            }
                        else{
                            if (!txtQty.getText().toString().matches("") && Integer.parseInt(txtQty.getText().toString()) > 0) {
                                String dml = DB.Rundml("UPDATE listOfItems set quantity = '" + Integer.valueOf(txtQty.getText().toString()) + "' where LID = '" + model.getLID() + "'");
                                if (dml == "ok")
                                    Toast.makeText(buildItemActivity.this, "Quantity updated succesfully", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(buildItemActivity.this, "Update failed " + dml, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(buildItemActivity.this, "Item quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
                        }
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
