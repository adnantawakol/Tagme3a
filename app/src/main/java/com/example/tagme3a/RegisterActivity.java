package com.example.tagme3a;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import java.sql.Connection;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText txtuserreg, txtname, txtpass1, txtpass2, txtmail, txtphone, txtgov;
        RadioButton rbmale, rbfemale;
        Button btnreg;
        txtuserreg = findViewById(R.id.txtuserreg);
        txtname = findViewById(R.id.txtname);
        txtpass1 = findViewById(R.id.txtpass1);
        txtpass2 = findViewById(R.id.txtpass2);
        txtmail = findViewById(R.id.txtmail);
        txtphone = findViewById(R.id.txtphone);
        txtgov = findViewById(R.id.txtgov);
        rbmale = findViewById(R.id.rbmale);
        rbfemale = findViewById(R.id.rbfemale);
        btnreg = findViewById(R.id.btnreg);

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!txtuserreg.getText().toString().matches("^[a-zA-Z0-9._-]{3,}$")) {
                    txtuserreg.setError("Please enter a valid username");
                    txtuserreg.requestFocus();
                }
                else {
                    if(txtname.getText().toString().isEmpty()) {
                        txtname.setError("Please enter a Username");
                        txtname.requestFocus();
                    }
                    else{
                        if(!txtpass1.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
                            txtpass1.setError("Password must have minimum eight characters, at least one letter and one number");
                            txtpass1.requestFocus();
                        }
                        else{
                            if(txtpass2.getText().toString().isEmpty() || !txtpass2.getText().toString().equals(txtpass1.getText().toString())) {
                                txtpass2.setError("Please confirm your password");
                                txtpass2.requestFocus();
                            }
                            else{
                                if(!txtmail.getText().toString().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")) {
                                    txtmail.setError("Please enter a valid email");
                                    txtmail.requestFocus();
                                }
                                else{
                                    if(!txtphone.getText().toString().matches("\\d{11}")) {
                                        txtphone.setError("Please enter a valid Phone number");
                                        txtphone.requestFocus();
                                    }
                                    /*else{
                                        if(txtgov.getText().toString().isEmpty()) {
                                            txtgov.setError("Please enter your Government");
                                            txtgov.requestFocus();
                                        }*/
                                        else{
                                            if(!rbmale.isChecked() && !rbfemale.isChecked()){
                                                Toast.makeText(RegisterActivity.this, "Please specify your gender", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                //DB CONN
                                                Database db = new Database();
                                                Connection conn = db.connect();
                                                if(conn == null){
                                                    Toast.makeText(RegisterActivity.this, "connection failed", Toast.LENGTH_SHORT).show();
                                                    AlertDialog.Builder m = new AlertDialog.Builder(RegisterActivity.this)
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
                                                else{
                                                    boolean gender = true;
                                                    if(rbfemale.isChecked())
                                                        gender = false;
                                                    String ss = db.Rundml("insert into [user] values('"+txtuserreg.getText().toString()+"','"+txtpass1.getText().toString()+"','"+txtmail.getText().toString()+"','"+gender+"','"+txtgov.getText().toString()+"','"+txtphone.getText().toString()+"','"+txtname.getText().toString()+"')");
                                                    if(ss.equals("ok")){
                                                        AlertDialog.Builder regdone = new AlertDialog.Builder(RegisterActivity.this)
                                                                .setTitle("Register success")
                                                                .setMessage("Your account has been successfully created")
                                                                .setIcon(R.drawable.logo)
                                                                .setPositiveButton("Go to login", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        finish();
                                                                    }
                                                                });
                                                        regdone.create().show();
                                                    }
                                                    else if (ss.contains("UQusername")){
                                                        Toast.makeText(RegisterActivity.this, "This username is already taken", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if(ss.contains("UQemail")){
                                                        Toast.makeText(RegisterActivity.this, "This email is already taken", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(RegisterActivity.this, "" + ss, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        //}
                                    }
                                }
                            }
                        }
                    }

                }
            }
        });
        txtpass1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    if(!txtpass1.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
                        txtpass1.setError("Password must have minimum eight characters, at least one letter and one number");
                    }

                }
            }
        });
        txtpass2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    if(!txtpass2.getText().toString().equals(txtpass1.getText().toString())){
                        txtpass2.setError("password do not match");
                    }

                }
            }
        });
        txtmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!txtmail.getText().toString().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")){
                    txtmail.setError("Email address is incorrect");
                }
            }
        });

    }


    //public void Register(View view)
}