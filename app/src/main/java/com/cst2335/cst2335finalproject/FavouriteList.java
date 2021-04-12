package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class FavouriteList extends AppCompatActivity {
    ArrayList<Artist> favList=new ArrayList<>();
    private Button backButton;
    MySongListAdapter myAdapter;
    static SQLiteDatabase db;
    ListView listview;
    int positionSaved;
    boolean isTablet;
    SwipeRefreshLayout pullRefresher=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        listview=findViewById(R.id.Listid);
        backButton = findViewById(R.id.GoBackButton);
        loadDataFromDB();
        myAdapter=new MySongListAdapter();
        listview.setAdapter(myAdapter);
        pullRefresher=findViewById(R.id.refresher);

        backButton.setOnClickListener((e)->
        {
            finish();

        });

        isTablet=findViewById(R.id.frameLayout)!=null;
        String str=getString(R.string.youSelected);
        if(isTablet){
            pullRefresher.setOnRefreshListener(() -> {
                favList.clear();
                loadDataFromDB();
                myAdapter.notifyDataSetChanged();
                pullRefresher.setRefreshing(false);
            });
        }

        listview.setOnItemLongClickListener((parent, view1, position, id) -> {
            positionSaved=position;
            Bundle dataTopass=new Bundle();
            Artist selectedArtist=favList.get(position);

            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            alert.setTitle(R.string.Songster_moreInfoAboutSong).
                    setMessage(str+" "+selectedArtist.getSongTitle()).
                    setPositiveButton(R.string.confirm,(click,arg)->
                    {
                        dataTopass.putString("id",String.valueOf(selectedArtist.getId()));
                        dataTopass.putString("ArtistName",selectedArtist.getArtistName());
                        dataTopass.putString("ArtistId",selectedArtist.getArtistId());
                        dataTopass.putString("SongId",selectedArtist.getSongId());
                        dataTopass.putString("SongTitle",selectedArtist.getSongTitle());
                        dataTopass.putBoolean("SaveButton",true);
                        dataTopass.putBoolean("isTablet",isTablet);

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
                            startActivityForResult(nextActivity,1); //start activity
                        }
                    }).setNegativeButton(R.string.decline,(click,arg)->{

                Toast toast= Toast.makeText(getApplicationContext(), R.string.nothingChanged, Toast.LENGTH_LONG);
               View view = toast.getView();
               view.setBackgroundColor(Color.parseColor("#FF000000"));
               TextView text = (TextView) view.findViewById(android.R.id.message);
               text.setTextColor(Color.parseColor("#FFFFFFFF"));
               text.setTextSize(20);
                toast.show();

            }).create().show();
            return true;

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode== Activity.RESULT_OK) {
                favList.remove(positionSaved);
                myAdapter.notifyDataSetChanged();
            }
        }
    }
    private void loadDataFromDB(){

        SongOpener songDB=new SongOpener(this);
        db=songDB.getWritableDatabase();
        String [] columns={ SongOpener.COL_ID, SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID, SongOpener.COL_SONGID, SongOpener.COL_SONGTITLE};
        Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int idColIndex = results.getColumnIndex( SongOpener.COL_ID);
        int artistNameColIndex = results.getColumnIndex( SongOpener.COL_ARTISTNAME);
        int artistIdColIndex = results.getColumnIndex(SongOpener.COL_ARTISTID);
        int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
        int songTitleIndex = results.getColumnIndex(SongOpener.COL_SONGTITLE);

        while(results.moveToNext()){
            long id=results.getLong(idColIndex);
            String artistName=results.getString(artistNameColIndex);
            String artistId=results.getString(artistIdColIndex);
            String songId=results.getString(songIdColIndex);
            String songTitle=results.getString(songTitleIndex);

            favList.add(new Artist(id,artistName,artistId,songId,songTitle));
        }
    }

    private class MySongListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favList.size();
        }

        @Override
        public Artist getItem(int position) {
            return favList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            Artist artist= (Artist) getItem(position);

            View artistView=inflater.inflate(R.layout.artist_row,parent,false);
            TextView songId=artistView.findViewById(R.id.SongIdRow);
            TextView title=artistView.findViewById(R.id.artistTitleRow);
            TextView artistId=artistView.findViewById(R.id.artistIdRow);
            TextView artistName=artistView.findViewById(R.id.artistName);
            songId.setText(artist.getSongId());
            title.setText(artist.getSongTitle());
            artistId.setText(artist.getArtistId());
            artistName.setText(artist.getArtistName());

            return artistView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
