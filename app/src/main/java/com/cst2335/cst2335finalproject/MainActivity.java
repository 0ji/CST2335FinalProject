package com.cst2335.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cst2335.cst2335finalproject.soccer.SoccerMainActiv;
import com.google.android.material.navigation.NavigationView;

/**
 * MainActivity
 * This activity is a hub to cross to each activity.
 * */
public class MainActivity extends AppCompatActivity {
    /**
     * Button goToSoccer
     * This button is used for moving to the soccer activity
     * */
    ImageButton goToSoccer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Button goToSoccer
         * This button is used for moving to the soccer activity
         * */
        goToSoccer = (ImageButton) findViewById(R.id.goToSoccer);
        goToSoccer.setOnClickListener(v->{
            changePage(SoccerMainActiv.class);
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.soccer_toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.soccer_drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.soccer_nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.soccer_goToMain:
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.soccer_goToTrivia:
                    Intent intent2 = new Intent(this, MainActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.soccer_goToCarDB:
                    Intent intent3 = new Intent(this, MainActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.soccer_goToSoccerMain:
                    Intent intent4 = new Intent(this, SoccerMainActiv.class);
                    startActivity(intent4);
                case R.id.soccer_previous:
                    Intent intent5 = new Intent(this, MainActivity.class);
                    setResult(500, intent5);
                    finish();
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }
    /**
     * changePage
     * @param c is new class which a user wants
     * This method is used for jumping into another page.
     * */
    private void changePage(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    /**
     * onCreateOptionsMenu
     * This method creates and gives functionality to each menu item.
     * @param menu is a Menu instance
     * @return true
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_from_jin, menu);

        return true;
    }

    /**
     * onOptionsItemSelected
     * This method indicates how each item works when items are clicked.
     * @param item
     * @return true
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.soccer_about:
                Toast.makeText(getApplicationContext(),"soccer_about",Toast.LENGTH_LONG).show();
                AlertDialog.Builder alterBuilder = new AlertDialog.Builder(this);
                alterBuilder.setTitle("INFORMATION").setMessage("Final Project CST2335\nSoccer Articles is made by Jin\n2021-04-01")
                        .setPositiveButton("Yes",(click, arg)->{

                        }).setNeutralButton("Cancel",(click,arg)->{

                }).create().show();
                break;
            case R.id.soccer_activity:
                changePage(SoccerMainActiv.class);
                break;
        }
        return true;//super.onOptionsItemSelected(item);
    }
}