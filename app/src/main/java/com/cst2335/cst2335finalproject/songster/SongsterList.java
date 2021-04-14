
package com.cst2335.cst2335finalproject.songster;
/**
 * @Author: Trung Nguyen
 * Date:11/04/2021
 */

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cst2335.cst2335finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SongsterList extends AppCompatActivity {
    private Button backButton;
    private ArrayList<Artist> artistList=new ArrayList<> ();
    private String artistName;
    private Artist artist;
    private MySongListAdapter artistAdapter;
    private ListView artistListView;
    private ProgressBar progressBar;
   private static SQLiteDatabase db;
    private static final String SONG_URL="https://www.songsterr.com/a/ra/songs.xml?pattern=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songster_list);
        backButton = findViewById(R.id.GoBackButton);
        progressBar = findViewById(R.id.progressBar);
        artistListView = findViewById(R.id.SongView);
        artistAdapter = new MySongListAdapter();
        artistListView.setAdapter(artistAdapter);
        boolean isTablet=findViewById(R.id.frameLayout)!=null;// check if it is tablet


        Bundle searchPage = getIntent().getExtras();
        artistName = searchPage.getString(SongsterSearch.ARTIST_KEYWORD);

        String artistNameResult=null;
        String [] artistNameTwo=new String[2];
        for(int i=0;i<artistName.length();i++){
            if(Character.isWhitespace(artistName.charAt(i))){
                artistNameTwo= artistName.split("\\s+");

                artistNameResult=artistNameTwo[0]+"+"+artistNameTwo[1];
                Log.e("url",artistNameResult);
                break;

            }else{
                artistNameResult=artistName;
            }
        }
        String artistURL = SONG_URL+artistNameResult;
        ArtistQuery qs = new ArtistQuery();
        qs.execute(artistURL);
        View view = findViewById(R.id.SongView);

        String str=getString(R.string.youSelected);
        backButton.setOnClickListener((e)->
        {
                finish();

        });

        artistListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            Bundle dataTopass=new Bundle();
           Artist selectedArtist=artistList.get(position);
            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            alert.setTitle(R.string.Songster_moreInfoAboutSong).
                    setMessage(str+" "+selectedArtist.getSongTitle()).
                    setPositiveButton(R.string.confirm,(click, arg)->
                    {
                        dataTopass.putString("id",String.valueOf(selectedArtist.getId()));
                        dataTopass.putString("ArtistName",selectedArtist.getArtistName());
                        dataTopass.putString("ArtistId",selectedArtist.getArtistId());
                        dataTopass.putString("SongId",selectedArtist.getSongId());
                        dataTopass.putString("SongTitle",selectedArtist.getSongTitle());
                        dataTopass.putBoolean("SaveButton",false);

                        if(isTablet){
                            SongDetail dfragment= new SongDetail();
                            dfragment.setArguments(dataTopass);
                            getSupportFragmentManager().
                                    beginTransaction().
                                    replace(R.id.frameLayout,dfragment)
                                    .commit();
                        }else {

                            Intent nextActivity = new Intent(this, FragmentPhoneActivity.class);
                            nextActivity.putExtras(dataTopass);
                            startActivity(nextActivity); //start activity
                        }
                    }).setNegativeButton(R.string.decline,(click, arg)->{
                        Snackbar snackbar= Snackbar.make(view, R.string.nothingChanged, Snackbar.LENGTH_LONG);
                        View snackbarView=snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.black));

                             TextView textView= (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                             textView.setTextSize(20);
                             textView.setTextColor(getResources().getColor(R.color.white));
                             snackbar.show();
            }).create().show();
            return true;

        });
    }

   public class ArtistQuery extends AsyncTask<String, Integer, String> {
       public ArtistQuery(){

       }
       @Override
       protected String doInBackground(String... params) {

           try {

               URL url = new URL(params[0]);
               Log.e("url", String.valueOf(url));
               Log.e("url2", params[0]);
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

               Log.e("response", String.valueOf(urlConnection.getResponseCode()));

               InputStream response = urlConnection.getInputStream();

               XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
               factory.setNamespaceAware(false);
               XmlPullParser xpp = factory.newPullParser();
               xpp.setInput(response, "UTF-8");
               int eventType = xpp.getEventType();
               while (xpp.next() != XmlPullParser.END_DOCUMENT) {

                   Artist artist = new Artist();
                   if ( xpp.getEventType() == XmlPullParser.START_TAG) {
                       if(xpp.getName().equals("Song")) {
                            artist.setSongId(xpp.getAttributeValue(null, "id"));
                           xpp.nextTag();
                           artist.setSongTitle(xpp.nextText());
                           xpp.nextTag();
                           artist.setArtistId(xpp.getAttributeValue(null, "id"));
                           xpp.nextTag();
                           artist.setArtistName(xpp.nextText());
                           artistList.add(artist);
                       }


                   }

               }


               } catch(Exception e){
                       Log.e("Exceptions",e.getMessage());
                       e.printStackTrace();
               }


           return "finish";
       }




       @Override
       protected void onPostExecute(String s) {
              super.onPostExecute(s);
              artistAdapter.notifyDataSetChanged();
              progressBar.setVisibility(View.INVISIBLE);

       }

       @Override
       protected void onPreExecute() {

           super.onPreExecute();
           progressBar.setVisibility(View.VISIBLE);
       }

       @Override
       protected void onProgressUpdate(Integer... values) {
           progressBar.setProgress(values[0]);
           artistAdapter.notifyDataSetChanged();

       }
   }

    private class MySongListAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return artistList.size();
        }

        @Override
        public Artist getItem(int position) {

            return artistList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            Artist artist= getItem(position);
            View artistView=inflater.inflate(R.layout.artist_row,parent,false);
            TextView songId=artistView.findViewById(R.id.SongIdRow);
            TextView title=artistView.findViewById(R.id.artistTitleRow);
            TextView artistId=artistView.findViewById(R.id.artistIdRow);
            TextView artistName=artistView.findViewById(R.id.artistName);
            songId.setText(artist.getSongId()+"");
            title.setText(artist.getSongTitle());
            artistId.setText(artist.getArtistId()+"");
            artistName.setText(artist.getArtistName());


            return artistView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SongList","in the function onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SongList","in the function onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SongList","in the function onDestory");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("SongList","in the function onPause");
    }
}