package com.cst2335.cst2335finalproject.soccer;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    TextView test1;

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
        setContentView(R.layout.activity_soccer_main);
        pgBar = (ProgressBar) findViewById(R.id.pgBar);
        pgBar.setVisibility(View.VISIBLE);
        test1 = (TextView) findViewById(R.id.test1);
        test1.setVisibility(View.VISIBLE);

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
            test1.setText("Prepare to extract data...");
            onProgressUpdate(10);
        }
        @Override
        protected void onPostExecute(String e) {
            super.onPostExecute(e);
            pgBar.setVisibility(View.INVISIBLE);
            test1.setVisibility(View.INVISIBLE);

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
            test1.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            InputStream stream;
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                Log.i(this.toString(), "Device is connected to network");
            }
            else{ Log.e(this.toString(), "No network connection is available"); }

            test1.setText("Start to connect to the web...");
            onProgressUpdate(15);
            try{
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000); conn.setConnectTimeout(60000);
                conn.setRequestMethod("GET"); conn.setDoInput(true);

                Log.d(this.toString(), "url connection starts");

                conn.connect();
                test1.setText("Reading a packet of stream...");
                onProgressUpdate(25);
                Log.d(this.toString(), "reading a stream");

                stream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, "UTF-8");
                test1.setText("Data extraction starts...");
                onProgressUpdate(45);
                boolean insideItem = false;
                int eventType = parser.getEventType();


                ArrayList<String> listTitle = new ArrayList<>();
                ArrayList<String> listLink = new ArrayList<>();
                ArrayList<String> listDesc = new ArrayList<>();
                ArrayList<String> listDate = new ArrayList<>();
                while(eventType != XmlPullParser.END_DOCUMENT){

                    if(eventType == XmlPullParser.START_TAG){
                        if(parser.getName().equalsIgnoreCase("item")){
                            insideItem = true;
                        }
                        else if(parser.getName().equalsIgnoreCase("title")){
                            if(insideItem){
                                listTitle.add(parser.nextText());
                            }
                        }
                        else if(parser.getName().equalsIgnoreCase("link")){
                            if(insideItem){
                                listLink.add(parser.nextText());
                            }
                        }
                        else if(parser.getName().equalsIgnoreCase("pubDate")){
                            if(insideItem){
                                listDate.add(parser.nextText());
                            }
                        }
                        else if(parser.getName().equalsIgnoreCase("description")){
                            if(insideItem){
                                listDesc.add(parser.nextText());
                            }
                        }
                    }
                    else if(eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")){
                        insideItem = false;
                    }
                    eventType = parser.next();
                    test1.setText("Almost done ...");
                    onProgressUpdate(70);
                }
                for(int i = 0; i<listTitle.size(); i++){
                    list.add(new Article(listTitle.get(i),listLink.get(i),listDesc.get(i),listDate.get(i)));
                    if(i==listTitle.size()/2){
                        test1.setText("Store the data...");
                        onProgressUpdate(90);
                    }
                }
                test1.setText("Process has been done...");
                onProgressUpdate(100);
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