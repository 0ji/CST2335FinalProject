package com.cst2335.cst2335finalproject.soccer;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.SoccerFragment;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SoccerMainActiv extends AppCompatActivity {

    private TextView progressStatus;

    protected ArrayList<Article> list = new ArrayList();
    private ProgressBar pgBar;
    private Button goToFavorite;
    private ListView myList;
    private MyAdapter myAdapter;
    private SoccerFragment fragment;
    private ImageView soccer_headlineImage;

    public static final String ADDRESS = "https://www.goal.com/feeds/en/news";

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
        goToFavorite = (Button) findViewById(R.id.goToFavorite);
        goToFavorite.setOnClickListener(c-> {
            Intent intent = new Intent(this, SoccerFavorites.class);
            startActivity(intent);
        });

        myList.setOnItemLongClickListener((parent, view, pos, id)->{
            AlertDialog.Builder alterBuilder = new AlertDialog.Builder(this);
            alterBuilder.setTitle("Do you want to delete this?").setMessage("This is :"+list.get(pos).getTitle())
                    .setPositiveButton("Yes",(click, arg)->{

                    }).setNeutralButton("Cancel",(click,arg)->{

            }).setView(getLayoutInflater().inflate(R.layout.soccer_row_layout,null))
                    .create().show();

            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_toolbar_menu, menu);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.soccer_about:
                Toast.makeText(getApplicationContext(),"soccer_about",Toast.LENGTH_LONG).show();
                break;
            case R.id.soccer_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            Article container = null;

            container = (Article) this.getItem(position);

            return container.getId();
        }

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
    class SoccerAccessLayer extends AsyncTask <String, Integer, String> {
        private String address;
        private Bitmap headlineImage;
        ConnectivityManager connMgr;
        public SoccerAccessLayer(String address, ConnectivityManager connMgr){
            this.address = address;
            list = new ArrayList<>();
            this.connMgr = connMgr;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressStatus.setText("Start accessing to the web...");
            onProgressUpdate(10);
        }
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
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pgBar.setVisibility(View.VISIBLE);
            pgBar.setProgress(values[0]);
            progressStatus.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            InputStream stream;
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                Log.i(this.toString(), "Device is connected to network");
            }
            else{ Log.e(this.toString(), "No network connection is available"); }

            progressStatus.setText("Make a connection...");
            onProgressUpdate(15);
            try{
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000); conn.setConnectTimeout(60000);
                conn.setRequestMethod("GET");
                // This should be 200
                Log.d("getResponseCode", "doInBackground: "+conn.getResponseCode());

                conn.connect();

                progressStatus.setText("Read data from the xml...");
                onProgressUpdate(25);
                Log.d(this.toString(), "reading a stream");

                stream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, Xml.Encoding.UTF_8.toString());

                progressStatus.setText("Start extracting data...");
                onProgressUpdate(45);

                boolean insideItem = false;
                int eventType = parser.getEventType();
                int i = 0;
                boolean firstImage = false;
                Article article = new Article();
                /**
                 * Reference: Johan Jurrius, 33-Reading data from RSS feed(Android), Aug 20 2017, https://www.youtube.com/watch?v=Lnan_DJU7DI
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
                    progressStatus.setText("Downloading Images ...");
                    onProgressUpdate(70);
                }

                if(list.size()>0){
                    headlineImage = BitmapUtility.getBitmapImage(list.get(list.size()-1).getThumbnailUrl());
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

        @Override
        public String toString() {
            return "SoccerAccessLayer";
        }
    }
}




 /*
                if(list.size()>0) {
                    String urlString = list.get(list.size()-1).getThumbnailUrl();
                    URL newUrl = new URL(urlString);
                    conn = (HttpURLConnection) newUrl.openConnection();
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if(responseCode == 200){
                        headlineImage = BitmapFactory.decodeStream(conn.getInputStream());
                    }
                    FileOutputStream outputStream = openFileOutput("headlineImage", Context.MODE_PRIVATE);
                    headlineImage.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
                */