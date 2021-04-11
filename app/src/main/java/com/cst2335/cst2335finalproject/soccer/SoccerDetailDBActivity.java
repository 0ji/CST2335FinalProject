package com.cst2335.cst2335finalproject.soccer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.cst2335.cst2335finalproject.MainActivity;
import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.carDB.CarDBActivity;
import com.google.android.material.navigation.NavigationView;

/**
 * SoccerDetailDBActivity
 * This class is another version of SoccerDetailActivity but This gets data from the database.
 * The class provide details of an selected article object.
 * Button soccer_urlButton_db is a class member to provide article's url connection through a web application.
 * Button soccer_backToDBList is a button to lead a user to previous activity.
 * Toolbar toolbar is a class member that provide a tool bar.
 * {@link AppCompatActivity}
 * */
public class SoccerDetailDBActivity extends AppCompatActivity {
    private Button soccer_urlButton_db, soccer_backToDBList;
    private Toolbar toolbar;
    /**
     * onCreate
     * @param savedInstanceState is a bundle to pass data.
     *
     * This method will be executed when the activity is created.
     * In this method, tool bar, navigationView, Button, and multiple components are initialized
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_detail_d_b);

        toolbar = (Toolbar) findViewById(R.id.soccer_toolbar_db_detail);
        setSupportActionBar(toolbar);
        /**
         * This drawer and toggle are created for the navigationView.
         * */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.soccer_drawer_layout_db_detail);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /**
         * This code block is creating a navigation functionality which provides a different functions when a user click each items.
         * */
        NavigationView navigationView = (NavigationView) findViewById(R.id.soccer_nav_view_db_detail);
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
                    Intent intent3 = new Intent(this, CarDBActivity.class);
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

        /**
         * dataFromChat is a bundle which contains an article object.
         * the bundle will be passed through setArguments by detailsFragment.
         * */
        Bundle dataFromChat = getIntent().getExtras();
        SoccerFragment detailsFragment = new SoccerFragment();
        detailsFragment.setArguments(dataFromChat);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_db, detailsFragment).commit();
        Article article = (Article) dataFromChat.getSerializable("Article");
        Log.d("Article title is", "onCreate: "+article.toString());

        soccer_backToDBList = (Button)findViewById(R.id.soccer_backToDBList);
        soccer_urlButton_db = (Button) findViewById(R.id.soccer_urlButton_db);
        /**
         * Reference: Johan Jurrius, 33-Reading data from RSS feed(Android), Aug 20 2017, https://www.youtube.com/watch?v=Lnan_DJU7DI
         * */
        soccer_backToDBList.setOnClickListener(c->{
            Intent intent = new Intent(this, SoccerFavorites.class);
            startActivity(intent);
        });
        /**
         * uri is a Uri class object.
         * the intent will get internet browser applications
         * Reference: Johan Jurrius, 33-Reading data from RSS feed(Android), Aug 20 2017, https://www.youtube.com/watch?v=Lnan_DJU7DI
         * */
        soccer_urlButton_db.setOnClickListener(c->{
            Uri uri = Uri.parse(article.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
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
        inflater.inflate(R.menu.soccer_toolbar_menu, menu);

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
                AlertDialog.Builder alterBuilder = new AlertDialog.Builder(this);
                alterBuilder.setTitle("How To Use")
                        .setMessage("-Back To List button makes you back to the list\n\n" +
                        "-GO To WEB button shows the article of this\n\n" +
                                "-Soccer Icon is for heading to the soccer main")
                        .setPositiveButton("Okay",(click, arg)->{}).create().show();
                break;
            case R.id.soccer_activity_main:
                Intent intent = new Intent(this, SoccerMainActiv.class);
                startActivity(intent);
                break;
            case R.id.itemCar:
                Intent intent1 = new Intent(this, CarDBActivity.class);
                startActivity(intent1);
                break;
            case R.id.itemSong:
                break;
            case R.id.itemTrivia:
                break;
        }
        return true;//super.onOptionsItemSelected(item);
    }
}