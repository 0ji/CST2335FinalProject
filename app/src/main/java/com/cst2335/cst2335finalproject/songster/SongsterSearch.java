package com.cst2335.cst2335finalproject.songster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.carDB.CarDBActivity;
import com.cst2335.cst2335finalproject.soccer.SoccerMainActiv;
import com.cst2335.cst2335finalproject.trivia.MainActivity;
import com.google.android.material.navigation.NavigationView;

public class SongsterSearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    static final String ARTIST_KEYWORD="ARTISTNAME";
    private Button searchButton;
    private EditText userInput;
    ProgressBar progressBar;
    public static final String ACTIVITY_NAME = "SEARCH_ACTIVITY";
    public SharedPreferences prefs=null;
    private  String savedArtistName;
    private Button favButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songster_search);
        userInput=findViewById(R.id.textinput);
        searchButton=findViewById(R.id.searchButton);
        progressBar=findViewById(R.id.progressSearch);
        favButton=findViewById(R.id.favouriteSongListButton);

        // create toolbar
        Toolbar tBar = findViewById(R.id.toolbar2);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("Song app");

        //create navdrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);


        prefs=getSharedPreferences("SavedPref",MODE_PRIVATE);
        savedArtistName=prefs.getString("ArtistName","");
        userInput.setText(savedArtistName);


        favButton.setOnClickListener(s->{
         Intent listFav=new Intent(this, FavouriteList.class);
         startActivity(listFav);
        });

         searchButton.setOnClickListener(e->

        {

            if(userInput.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.SongsterSearch_EnterValidName, Toast.LENGTH_LONG).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);

                Intent ListViewPage = new Intent(this, SongsterList.class);
                ListViewPage.putExtra(ARTIST_KEYWORD, userInput.getText().toString());

                startActivity(ListViewPage);


            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.songsters_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        String message = null;
        Intent nextActivity = null;
        switch(item.getItemId()) {
            case R.id.about:
                alertDialog.setMessage(R.string.SongsterSearch_HelpMenuItem)
                        .setPositiveButton(R.string.agree,null).create().show();
                break;
            case R.id.itemCar:
                message = "Launching car app";
                nextActivity = new Intent(this, CarDBActivity.class);
                startActivity(nextActivity);
                break;
            case R.id.itemSoccer:
                message = "Launching soccer app";
                nextActivity = new Intent(this, SoccerMainActiv.class);
                startActivity(nextActivity);
                break;
            case R.id.itemSong:
                message = "Launching song app";
                nextActivity = new Intent(this, SongsterSearch.class);
                startActivity(nextActivity);
                break;
            case R.id.itemTrivia:
                message = "Launching trivia app";
                nextActivity = new Intent(this, CarDBActivity.class);
                startActivity(nextActivity);
                break;

        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String message = null;
        Intent nextActivity = null;
        switch(item.getItemId()) {
            case R.id.itemCar:
                message = "Launching car app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
            case R.id.itemSoccer:
                message = "Launching soccer app";
                nextActivity = new Intent(this, SoccerMainActiv.class);
                break;
            case R.id.itemSong:
                message = "Launching song app";
                nextActivity = new Intent(this, SongsterSearch.class);
                break;
            case R.id.itemTrivia:
                message = "Launching trivia app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(nextActivity);
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME,"in the function onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME,"in the function onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME,"in the function onDestory");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit=prefs.edit();
        savedArtistName= userInput.getText().toString();

        edit.putString("ArtistName", savedArtistName);

        edit.commit();

        Log.e(ACTIVITY_NAME,"in the function onPause");


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);

        Log.e(ACTIVITY_NAME,"in the function onResume");
    }
}
