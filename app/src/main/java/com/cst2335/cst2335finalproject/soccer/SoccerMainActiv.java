package com.cst2335.cst2335finalproject.soccer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.SoccerFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SoccerMainActiv extends AppCompatActivity {

    TextView progressStatus;

    ArrayList<Article> list = new ArrayList();
    ProgressBar pgBar;

    ListView myList;
    MyAdapter myAdapter;
    SoccerFragment fragment;
    SQLiteDatabase db;

    public static final String address = "https://feeds.24.com/articles/fin24/tech/rss";
    //http://www.goal.com/en/feeds/news?fmt=rss
    //https://www.nasa.gov/rss/dyn/breaking_news.rss
    //https://feeds.24.com/articles/fin24/tech/rss
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

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        SoccerAccessLayer soccerAccessLayer = new SoccerAccessLayer(address,connMgr);

        Log.d("t", "onCreate: "+list.size());

        myList = (ListView) findViewById(R.id.list_item);
        myList.setAdapter(myAdapter);
        soccerAccessLayer.execute();

        FrameLayout frame = findViewById(R.id.frame);
        boolean isPhone = frame==null?true:false;
        myList.setOnItemClickListener((parent, view, position, id) -> {
            Bundle data = new Bundle();
            data.putString("Title", list.get(position).getTitle());
            Log.d("Title", "onCreate: "+list.get(position).getTitle());
            data.putLong("Id",id);
            data.putString("Link",list.get(position).getLink());
            data.putString("Description", list.get(position).getDescription());
            data.putString("Date",list.get(position).getPubDate());

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();
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
            View newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView tView = newView.findViewById(R.id.tViewSend);
            tView.setText(container.getTitle());
            return newView;
        }
    }
    class SoccerAccessLayer extends AsyncTask <String, Integer, String> {
        private String address;

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
                conn.setRequestMethod("GET"); conn.setDoInput(true);

                Log.d(this.toString(), "url connection starts");

                conn.connect();
                progressStatus.setText("Read data from the xml...");
                onProgressUpdate(25);
                Log.d(this.toString(), "reading a stream");

                stream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                //factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, "UTF-8");
                progressStatus.setText("Start extracting data...");
                onProgressUpdate(45);
                boolean insideItem = false;
                int eventType = parser.getEventType();
                int i = 0;
                Article article = new Article();
                while(eventType != XmlPullParser.END_DOCUMENT){

                    if(eventType == XmlPullParser.START_TAG){
                        if(parser.getName().equalsIgnoreCase("item")){
                            insideItem = true;
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
                    }
                    else if(eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")){
                        Log.d("item added", "doInBackground: "+article.toString());
                        list.add(article);
                        insideItem = false;
                        article = new Article();
                    }
                    eventType = parser.next();
                    progressStatus.setText("Almost done ...");
                    onProgressUpdate(70);
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