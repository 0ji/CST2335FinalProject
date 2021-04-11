package com.cst2335.cst2335finalproject.soccer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cst2335.cst2335finalproject.MainActivity;
import com.cst2335.cst2335finalproject.R;
import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * SoccerMainActiv
 * This class is a main class of my part of this project.
 * This activity provides users an interface which contains listview, toolbar and navigation view.
 * ImageView soccer_headlineImage will be the top article's thumbnail of all the articles.
 * The progressBar will be showed up as the first and the list will come up after that.
 * {@link AppCompatActivity}
 * */
public class SoccerMainActiv extends AppCompatActivity {

    private TextView progressStatus;

    protected ArrayList<Article> list = new ArrayList();
    private ProgressBar pgBar;
    private Button goToFavorite;
    private ListView myList;
    private MyAdapter myAdapter;
    private SoccerFragment fragment;
    private ImageView soccer_headlineImage;
    /**
     * This static final variable is hard coded as the address of xml api.
     * */
    public static final String ADDRESS = "https://www.goal.com/feeds/en/news";
    /**
     * onCreate
     * @param savedInstanceState
     * This method creates essential components of this activity.
     * progress bar, ImageView, listView and navigation View has come up.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_soccer_main);

        pgBar = (ProgressBar) findViewById(R.id.pgBar);
        pgBar.setVisibility(View.VISIBLE);
        progressStatus = (TextView) findViewById(R.id.progressStatus);
        progressStatus.setVisibility(View.VISIBLE);
        soccer_headlineImage = (ImageView) findViewById(R.id.soccer_headlineImage);
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        SoccerAccessLayer soccerAccessLayer = new SoccerAccessLayer(ADDRESS,connMgr);

        Log.d("t", "onCreate: "+list.size());

        myList = (ListView) findViewById(R.id.list_item);
        myList.setAdapter(myAdapter);
        soccerAccessLayer.execute();

        FrameLayout frame = findViewById(R.id.frame);
        boolean isPhone = frame==null?true:false;
        myList.setOnItemClickListener((parent, view, position, id) -> {
            Bundle data = new Bundle();
            Log.d("Title", "onCreate: "+list.get(position).getTitle());
            data.putSerializable("Article", list.get(position));
            if(!isPhone){// tablet
                fragment = new SoccerFragment();
                fragment.setArguments(data);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
            }
            else{   // phone
                Intent intent = new Intent(this, SoccerDetailActivity.class);
                intent.putExtras(data);
                Log.d("phone", "onCreate: phone");
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.soccer_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.soccer_drawer_layout);
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
        /**
         * Button goToFavorite
         * this button is used for transferring the current activity to SoccerFavorites class
         * */
        goToFavorite = (Button) findViewById(R.id.goToFavorite);
        goToFavorite.setOnClickListener(c-> {
            Intent intent = new Intent(this, SoccerFavorites.class);
            startActivity(intent);
        });

        /*
        myList.setOnItemLongClickListener((parent, view, pos, id)->{
            AlertDialog.Builder alterBuilder = new AlertDialog.Builder(this);
            alterBuilder.setTitle("Do you want to delete this?").setMessage("This is :"+list.get(pos).getTitle())
                    .setPositiveButton("Yes",(click, arg)->{

                    }).setNeutralButton("Cancel",(click,arg)->{

            }).setView(getLayoutInflater().inflate(R.layout.soccer_row_layout,null))
                    .create().show();

            return true;
        });
         */
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
                        .setMessage("-Click one of items in list to check Articles\n\n" +
                                "-Favorites button shows stored articles\n\n" +
                                "-Soccer Icon is for heading to the soccer main")
                        .setPositiveButton("Okay",(click, arg)->{}).create().show();
                break;
            case R.id.soccer_activity_main:
                Intent intent = new Intent(this, SoccerMainActiv.class);
                startActivity(intent);
                break;
        }
        return true;//super.onOptionsItemSelected(item);
    }
    /**
     * MyAdapter class
     * This class is designed to give a view to the listView in this layout.
     * This class takes a list which contains contents that should be showed up on the list.
     * This class is a subclass of {@link BaseAdapter}
     * */
    class MyAdapter extends BaseAdapter{
        /**
         * getCount
         * This method returns the size of the list.
         * @return list.size()
         * */
        @Override
        public int getCount() {
            return list.size();
        }
        /**
         * getItem
         * This method returns a specific object inside the list by a position.
         * @param position is a location of an object in the list.
         * @return list.get(position)
         * */
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        /**
         * getItemId
         * This method returns a long value which is an id of an object.
         * @param position
         * @return container.getId() is an id of the object in the list.
         * */
        @Override
        public long getItemId(int position) {
            Article container = null;

            container = (Article) this.getItem(position);

            return container.getId();
        }
        /**
         * getView
         * This method returns a view which replaces the current listview in the layout.
         * @param parent is a parent group of components.
         * @param convertView is a convert view.
         * @param position is a position of the object in the list.
         * @return newView is a view that replace the current list view.
         * */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Article container = (Article) getItem(position);
            View newView = inflater.inflate(R.layout.soccer_row_layout, parent, false);
            TextView tView = newView.findViewById(R.id.tViewSend);

            tView.setText(container.getTitle());
            return newView;
        }
    }
    /**
     * SoccerAccessLayer class
     * This class inherits from {@link AsyncTask}
     * This class takes String, Integer, String variable as values to use in each function.
     * */
    class SoccerAccessLayer extends AsyncTask <String, Integer, String> {
        private String address;
        private Bitmap headlineImage;
        ConnectivityManager connMgr;
        /**
         * The constructor of this class
         * @param address
         * @param connMgr
         *
         * */
        public SoccerAccessLayer(String address, ConnectivityManager connMgr){
            this.address = address;
            list = new ArrayList<>();
            this.connMgr = connMgr;
        }
        /**
         * onPreExceute is a method that occurs as the first in the process.
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressStatus.setText("Start accessing to the web...");
            onProgressUpdate(10);
        }
        /**
         * onPostExecute is a method that occurs as the last in the process.
         * In this method, soccer_headlineImage gets a bitmap object and List set an adapter
         * */
        @Override
        protected void onPostExecute(String e) {
            super.onPostExecute(e);
            progressStatus.setText("This is the end!");
            onProgressUpdate(100);
            pgBar.setVisibility(View.INVISIBLE);
            progressStatus.setVisibility(View.INVISIBLE);

            soccer_headlineImage.setImageBitmap(headlineImage);
            Log.d("---------------",""+list.size());
            Log.d("-----", this.getStatus().toString());
            myAdapter = new MyAdapter();
            myList.setAdapter(myAdapter);

        }
        /**
         * onProgressUpdate
         * This method is used for giving a progress bar's view.
         * @param values is an integer array
         * */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pgBar.setVisibility(View.VISIBLE);
            pgBar.setProgress(values[0]);
            progressStatus.setVisibility(View.VISIBLE);

        }
        /**
         * doInBackground
         * @param params is a String array
         * In this method, the application pulls and extracts data from the selected xml address.
         *               the extracted data is stored in the list as article objects.
         * @return null.
         * */
        @Override
        protected String doInBackground(String... params) {

            InputStream stream;
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                Log.i(this.toString(), "Device is connected to network");
            }
            else{ Log.e(this.toString(), "No network connection is available"); }

            progressStatus.setText(R.string.soccer_main_pg_15);
            onProgressUpdate(15);
            try{
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000); conn.setConnectTimeout(60000);
                conn.setRequestMethod("GET");
                // This should be 200
                Log.d("getResponseCode", "doInBackground: "+conn.getResponseCode());

                conn.connect();

                progressStatus.setText(R.string.soccer_main_pg_25);
                onProgressUpdate(25);
                Log.d(this.toString(), "reading a stream");

                stream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, Xml.Encoding.UTF_8.toString());

                progressStatus.setText(R.string.soccer_main_pg_45);
                onProgressUpdate(45);

                boolean insideItem = false;
                int eventType = parser.getEventType();
                int i = 0;
                boolean firstImage = false;
                Article article = new Article();
                /**
                 * Reference: Johan Jurrius, 33-Reading data from RSS feed(Android), Aug 20 2017, https://www.youtube.com/watch?v=Lnan_DJU7DI
                 * This is how extracts data from different tags in the xml address.
                 * The format of the xml address is the rss type which is divided by item tags
                 * */
                while(eventType != XmlPullParser.END_DOCUMENT){

                    if(eventType == XmlPullParser.START_TAG){
                        if(parser.getName().equalsIgnoreCase("item")){
                            insideItem = true;
                            firstImage = true;
                            article.setId(i);
                            i++;
                        }
                        else if(parser.getName().equalsIgnoreCase("title")){
                            if(insideItem){
                                article.setTitle(parser.nextText());
                            }
                        }
                        else if(parser.getName().equalsIgnoreCase("link")){
                            if(insideItem){
                                article.setLink(parser.nextText());
                            }
                        }
                        else if(parser.getName().equalsIgnoreCase("pubDate")){
                            if(insideItem){
                                article.setDate(parser.nextText());
                            }
                        }
                        else if(parser.getName().equalsIgnoreCase("description")){
                            if(insideItem){
                                article.setDescription(parser.nextText());
                            }
                        }
                        else if(parser.getName().equalsIgnoreCase("media:thumbnail")){
                            /**
                             * When the start tag is an item and the thumbnail is the first image out of the sub tags,
                             * this process gets image bytes from the thumbnali url.
                             * */
                            if(insideItem && firstImage){
                                String urlString = parser.getAttributeValue(null,"url");
                                URL newUrl = new URL(urlString);
                                HttpURLConnection connImage = (HttpURLConnection) newUrl.openConnection();
                                connImage.connect();
                                int responseCode = connImage.getResponseCode();
                                if(responseCode == 200){
                                    headlineImage = BitmapFactory.decodeStream(connImage.getInputStream());
                                }
                                article.setThumbnailUrl(BitmapUtility.getImageBytes(headlineImage));
                                firstImage = false;
                            }
                        }
                    }
                    else if(eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")){
                        Log.d("item added", "doInBackground: "+article.toString());
                        list.add(article);
                        insideItem = false;
                        article = new Article();
                    }
                    eventType = parser.next();
                    progressStatus.setText(R.string.soccer_main_pg_70);
                    onProgressUpdate(70);
                }

                if(list.size()>0){
                    headlineImage = BitmapUtility.getBitmapImage(list.get(0).getThumbnailUrl());
                }

                progressStatus.setText("Wait a moment...");
                onProgressUpdate(90);
                Log.d(this.toString(), "extracting data process has been ended..");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Log.d(this.toString(), "end ...");
            return null;
        }
        /**
         * toString
         * This method simply returns its name.
         * @return name of this class.
         * */
        @Override
        public String toString() {
            return "SoccerAccessLayer";
        }
    }
}



/*
MenuItem searchItem = menu.findItem(R.id.soccer_search);
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