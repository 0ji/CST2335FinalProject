package com.cst2335.cst2335finalproject.soccer;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.cst2335.cst2335finalproject.MainActivity;
import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.SoccerFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.soccer_drawer_layout_db_detail);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
                    Intent intent3 = new Intent(this, MainActivity.class);
                    setResult(500, intent3);
                    finish();
                    break;
                case R.id.soccer_goToSoccerMain:
                    Intent intent4 = new Intent(this, SoccerMainActiv.class);
                    startActivity(intent4);
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });


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
        soccer_urlButton_db.setOnClickListener(c->{
            Uri uri = Uri.parse(article.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}