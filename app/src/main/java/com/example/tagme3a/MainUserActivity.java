package com.example.tagme3a;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.Connection;

public class MainUserActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);       //Disable Dark mode in app
        setContentView(R.layout.activity_main_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //change nav header
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView displayName = (TextView) headerView.findViewById(R.id.displayName);
        TextView displayEmail = (TextView) headerView.findViewById(R.id.displayEmail);
        displayName.setText(LoginActivity.displayName);
        displayEmail.setText(LoginActivity.displayEmail);
        /*Passing each menu ID as a set of Ids because each
        menu should be considered as top level destinations.*/
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_user, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_logout){
            AlertDialog.Builder m = new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to Logout?")
                    .setNegativeButton("Cancel",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getSharedPreferences("MR",MODE_PRIVATE)
                                    .edit()
                                    .clear()
                                    .apply();
                            startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
                        }
                    });
            m.create().show();
        }
        if (id == R.id.action_delete){
            AlertDialog.Builder m = new AlertDialog.Builder(this)
                    .setTitle("Delete account")
                    .setMessage("Are you sure you want to delete your account?")
                    .setNegativeButton("Cancel",null)
                    .setPositiveButton("Delete my account", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getSharedPreferences("MR",MODE_PRIVATE)
                                    .edit()
                                    .clear()
                                    .apply();
                            Database DB = new Database();
                            Connection conn = DB.connect();
                            if(conn == null) {
                                Toast.makeText(MainUserActivity.this, "Please check you internet connection", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                DB.Rundml("delete from [user] where userID = '"+LoginActivity.ID+"'");
                                startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
                            }
                        }
                    });
            m.create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}