package com.example.tagme3a;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    public static String ID, displayName, displayEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText txtuser,txtpass;
        CheckBox cb;
        Button btnlogin;
        btnlogin = findViewById(R.id.btnlogin);
        cb = findViewById(R.id.cb);
        txtuser = findViewById(R.id.txtuser);
        txtpass = findViewById(R.id.txtpass);

        SharedPreferences h = getSharedPreferences("MR",MODE_PRIVATE);
        String u = h.getString("username",null);
        if(u != null) {
            ID = h.getString("id",null);
            displayName = h.getString("name",null);
            displayEmail = h.getString("email",null);
            startActivity(new Intent(LoginActivity.this, MainUserActivity.class));
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtuser.getText().toString().isEmpty()) {
                    txtuser.setError("Please enter your username");
                    txtuser.requestFocus();
                }
                else {
                    if (txtpass.getText().toString().isEmpty())
                    {
                        txtpass.setError("Please enter your username");
                        txtpass.requestFocus();
                    }
                    else{
                        Database db = new Database();
                        Connection conn = db.connect();
                        if(conn == null){
                            AlertDialog.Builder m = new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Connection failed")
                                    .setMessage("Can't connect to database please enable your wifi network or check your internet access")
                                    .setIcon(R.drawable.wifi)
                                    .setPositiveButton("Enable Wifi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) { //enable wifi
                                            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                            wifi.setWifiEnabled(true);
                                        }
                                    })
                                    .setNegativeButton("Cancel",null);
                            m.create().show();
                        }
                        else {
                            ResultSet rs = db.getData("select * from [user] where (email ='"+txtuser.getText().toString()+"' or username = '"+txtuser.getText().toString()+"')and password ='"+txtpass.getText().toString()+"'");
                            try {
                                if(rs.next()){
                                    if(cb.isChecked()){
                                        getSharedPreferences("MR",MODE_PRIVATE)
                                                .edit()
                                                .putString("username", txtuser.getText().toString())
                                                .putString("id",rs.getString(1))
                                                .putString("email",rs.getString(4))
                                                .putString("name", rs.getString(8))
                                                .apply();
                                    }
                                    ID = rs.getString(1);
                                    displayName = rs.getString(8);
                                    displayEmail = rs.getString(4);
                                    startActivity(new Intent(LoginActivity.this, MainUserActivity.class));
                                }
                                else{
                                    AlertDialog.Builder n = new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Login failed")
                                            .setMessage("Invalid username or password")
                                            .setPositiveButton("Ok",null);
                                    n.create().show();
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder mm = new AlertDialog.Builder(this)
                .setTitle("Close App")
                .setMessage("Are you sure you want to exit Tagme3a?")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
        mm.create().show();
    }


    public void goToReg(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }
}