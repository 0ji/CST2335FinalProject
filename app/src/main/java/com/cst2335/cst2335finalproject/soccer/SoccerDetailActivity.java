package com.cst2335.cst2335finalproject.soccer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cst2335.cst2335finalproject.trivia.MainActivity;
import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.carDB.CarDBActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * SoccerDetailActivity is a class which inherits AppCompactActivity class.
 * This class is designed for showing details of one soccer article.
 * {@link AppCompatActivity}
 * */
public class SoccerDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button soccer_saveButton, soccer_urlButton;
    RelativeLayout relativeLayout;
    SQLiteDatabase db;
    private ImageView soccer_headImage;
    private Article article;
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
        setContentView(R.layout.activity_soccer_detail);
        /**
         * relativeLayout is a layout variable to be used by SnackBar.
         * */
        relativeLayout = (RelativeLayout)findViewById(R.id.soccer_detail);
        soccer_headImage = (ImageView) findViewById(R.id.soccer_headImage);
        toolbar = (Toolbar) findViewById(R.id.soccer_toolbar);
        setSupportActionBar(toolbar);
        /**
         * This drawer and toggle are created for the navigationView.
         * */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.soccer_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /**
         * This code block is creating a navigation functionality which provides a different functions when a user click each items.
         * */
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
                    Intent intent4 = new Intent(this, CarDBActivity.class);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, detailsFragment).commit();
        /**
         * checking article object.
         * */
        article = (Article) dataFromChat.getSerializable("Article");
        Log.d("Article title is", "onCreate: "+article.toString());
        Log.d("Article imageUrl is", "onCreate: "+article.getImageUrl());
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        SoccerArticleImage soccerArticleImage = new SoccerArticleImage(article.getImageUrl(), connMgr);
        soccerArticleImage.execute();

        soccer_saveButton = (Button) findViewById(R.id.soccer_saveButton);
        soccer_urlButton = (Button) findViewById(R.id.soccer_urlButton);

        soccer_saveButton.setOnClickListener(c->{

            /**
             * insertingData method is called when a user clicks this button
             * */
            insertingData(article.getTitle(), article.getLink(), article.getPubDate(),article.getDescription(),article.getThumbnailUrl());
            /**
             * snackbar is a Snacbar which shows the article object is saved in the database.
             * */
            Snackbar snackbar = Snackbar.make(relativeLayout,R.string.soccer_save_button,Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.soccer_undo_button, click->{
                Intent intent = new Intent(this, SoccerFavorites.class);
                startActivity(intent);
            }).show();
        });
        /**
         * uri is a Uri class object.
         * the intent will get internet browser applications
         * Reference: Johan Jurrius, 33-Reading data from RSS feed(Android), Aug 20 2017, https://www.youtube.com/watch?v=Lnan_DJU7DI
         * */
        soccer_urlButton.setOnClickListener(c->{
            Uri uri = Uri.parse(article.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
    class SoccerArticleImage extends AsyncTask<String, Void, Void>{
        private String urlString;
        ConnectivityManager connMgr;
        Bitmap headlineImage;

        public SoccerArticleImage(String urlString,ConnectivityManager connMgr){
            this.urlString = urlString;
            this.connMgr = connMgr;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            soccer_headImage.setImageBitmap(headlineImage);
            article.setThumbnailUrl(BitmapUtility.getImageBytes(headlineImage));
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL newUrl = null;
            HttpURLConnection connImage = null;
            headlineImage = null;
            try{
                newUrl = new URL(urlString);
                connImage = (HttpURLConnection) newUrl.openConnection();
                connImage.connect();
                int responseCode = connImage.getResponseCode();
                if(responseCode == 200){
                    headlineImage = BitmapFactory.decodeStream(connImage.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /**
     * insertingData
     * @param title is an article's title to save
     * @param link is an article's link to save
     * @param date is an article's date to save
     * @param description is an article's description to save
     * @param thumbnail is an article's thumbnail to save
     * This method will save the article data to the database.
     * */
    private long insertingData(String title, String link, String date, String description, byte[] thumbnail){
        SoccerDBHelper dbHelper = new SoccerDBHelper(this);
        db = dbHelper.getWritableDatabase();
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(SoccerDBHelper.COL_TITLE, title);
        newRowValues.put(SoccerDBHelper.COL_LINK, link);
        newRowValues.put(SoccerDBHelper.COL_DATE, date);
        newRowValues.put(SoccerDBHelper.COL_DESCRIPTION, description);
        newRowValues.put(SoccerDBHelper.COL_THUMBNAIL, thumbnail);
        long newId = db.insert(SoccerDBHelper.TABLE_NAME, null, newRowValues);
        Log.d("Soccer Database", "insertingData: "+newId);
        return newId;
    }
    /**
     * onCreateOptionsMenu is used for creating menu options
     * @param menu is a menu view.
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_toolbar_menu, menu);
        /*
        MenuItem searchItem = menu.findItem(R.id.soccer_search);
        searchItem.setVisible(false);

        SearchView soccer_searchView = (SearchView) searchItem.getActionView();
        soccer_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),"Searching: "+query.toString(),Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        */

        return true;
    }
    /**
     * onOptionsItemSelected is used to react when a user click one of items in menu
     * @param item is a selected item by a user.
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.soccer_about:

                AlertDialog.Builder alterBuilder = new AlertDialog.Builder(this);
                alterBuilder.setTitle(getString(R.string.soccer_howToUse))
                        .setMessage(getString(R.string.soccer_howToUse1)+"\n\n" +
                                getString(R.string.soccer_howToUse5)+"\n\n" +
                                getString(R.string.soccer_howToUse3))
                        .setPositiveButton("Okay",(click, arg)->{}).create().show();
                break;
            case R.id.soccer_activity_main:
                finish();
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
        return super.onOptionsItemSelected(item);
    }

}