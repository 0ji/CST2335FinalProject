package com.cst2335.cst2335finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class SongDetail extends Fragment {
    private TextView artistNameView;
    private TextView artistIdView;
    private TextView songIdView;
    private TextView songTitleView;
    public Button SaveOrDeleteButton;
    Button searchSong;
    Button searchArtist;

    public SQLiteDatabase db;
    Bundle prevData;
    private AppCompatActivity parentApp;

    String artistName;
    String artistId;
    String songId;
    String songTitle;
    private static final String SearchSongUrl="http://www.songsterr.com/a/wa/song?id=";
    private static final String SearchArtistUrl="http://www.songsterr.com/a/wa/artist?id=";
    boolean isDeleteButton;
    boolean isTablet;



    @SuppressWarnings( "deprecation" )

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prevData=getArguments();
        artistName=prevData.getString("ArtistName");
        artistId=prevData.getString("ArtistId");
        songId=prevData.getString("SongId");
        songTitle=prevData.getString("SongTitle");
        isDeleteButton = prevData.getBoolean("SaveButton");
        isTablet=prevData.getBoolean("isTablet");


        View view=inflater.inflate(R.layout.activity_song_details_page,container,false);


        artistNameView=view.findViewById(R.id.artistName);
        artistIdView=view.findViewById(R.id.artistId);
        songIdView=view.findViewById(R.id.songId);
        songTitleView=view.findViewById(R.id.SongTitle);
        SaveOrDeleteButton =view.findViewById(R.id.SaveDB);
        searchSong=view.findViewById(R.id.searchButton);
        searchArtist=view.findViewById(R.id.ArtistIdbtn);
        artistNameView.setText("ArtistName: "+artistName);
        artistIdView.setText("ArtistId: "+artistId);
        songIdView.setText("SongId: "+songId);
        songTitleView.setText("Song title: "+songTitle);

        searchSong.setOnClickListener(d->{
                    String url=SearchSongUrl+songId;
                    Intent searchGoogle=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(searchGoogle);

                }
        );

        searchArtist.setOnClickListener(d->{
                    String urlArtist=SearchArtistUrl+artistId;
                    Intent searchGoogleArtist=new Intent(Intent.ACTION_VIEW, Uri.parse(urlArtist));
                    startActivity(searchGoogleArtist);

                }
        );

        if(isDeleteButton ==false){
            SaveOrDeleteButton.setText(R.string.Songster_Save);
            SaveOrDeleteButton.setOnClickListener(v->{

                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.SongDetail_FavouriteListConfirmation_Add).
                        setPositiveButton(R.string.confirm,(Click,args)->{
                            //create instance of SongsterList
                            SongsterList songster= new SongsterList();
                            ContentValues cv= new ContentValues();
                            SongOpener songDB=new SongOpener(getActivity());
                            db=songDB.getWritableDatabase();

                            boolean isSaved=false;
                            String [] columns={ SongOpener.COL_ID, SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID, SongOpener.COL_SONGID, SongOpener.COL_SONGTITLE};
                            Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);
                            int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
                            while(results.moveToNext()){
                                String songIdOld=results.getString(songIdColIndex);
                                if(songIdOld.equals(songId)){
                                    Toast.makeText(getActivity(),R.string.duplicateMessage,Toast.LENGTH_LONG).show();
                                    isSaved=true;
                                    break;
                                }

                            }
                            if(!isSaved) {
                                cv.put(SongOpener.COL_ARTISTNAME, artistName);
                                cv.put(SongOpener.COL_ARTISTID, artistId);
                                cv.put(SongOpener.COL_SONGID, songId);
                                cv.put(SongOpener.COL_SONGTITLE, songTitle);
                                db.insert(SongOpener.TABLE_NAME, null, cv);
                                Toast toast=Toast.makeText(getActivity(),R.string.ConfirmationMessageofSave,Toast.LENGTH_LONG);
                               View view1 = toast.getView();
                               view1.setBackgroundColor(Color.parseColor("#FF000000"));
                               TextView text = (TextView) view1.findViewById(android.R.id.message);
                               text.setTextColor(Color.parseColor("#FFFFFFFF"));
                               text.setTextSize(20);
                                toast.show();
                            }

                        }).
                        setNegativeButton(R.string.decline,(click,args)->{
                            Toast toast=Toast.makeText(getActivity(),R.string.nothingChanged,Toast.LENGTH_LONG);
                           View view1 = toast.getView();
                           view1.setBackgroundColor(Color.parseColor("#FF000000"));
                           TextView text = (TextView) view1.findViewById(android.R.id.message);
                           text.setTextColor(Color.parseColor("#FFFFFFFF"));
                           text.setTextSize(20);
                            toast.show();


                        }).create().show();
            });


        }else{
            SaveOrDeleteButton.setText(R.string.Songster_Delete);

            SaveOrDeleteButton.setOnClickListener(s->
            {

                AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.AskingMessageToDelete).
                        setPositiveButton(R.string.confirm,(Click,args)->{
                            SongOpener songDB=new SongOpener(getActivity());
                            db=songDB.getWritableDatabase();

                            songDB.deleteFromDB(songId);

                            if(!isTablet){


                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", 1);
                                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                                getActivity().finish();


                            }
                            String str = getString(R.string.ConfirmationMessage_Of_Deletion);
                            Toast.makeText(getActivity(), songTitle + " " + str, Toast.LENGTH_LONG).show();
                            parentApp.getSupportFragmentManager().beginTransaction().remove(this).commit();
                        }).
                        setNegativeButton(R.string.decline,(click,args)->{
                            Toast toast=Toast.makeText(getActivity(),R.string.nothingChanged,Toast.LENGTH_LONG);
                           View view1 = toast.getView();
                           view1.setBackgroundColor(Color.parseColor("#FF000000"));
                           TextView text = (TextView) view1.findViewById(android.R.id.message);
                           text.setTextColor(Color.parseColor("#FFFFFFFF"));
                           text.setTextSize(20);
                            toast.show();
                        }).create().show();
            });
        }
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentApp = (AppCompatActivity)context;
    }




}
