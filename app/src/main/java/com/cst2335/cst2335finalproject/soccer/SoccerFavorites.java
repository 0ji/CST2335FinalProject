package com.cst2335.cst2335finalproject.soccer;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cst2335.cst2335finalproject.MainActivity;
import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.SoccerFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
/**
 * SoccerFavorites
 * This class is a main class of my part of this project.
 * This activity provides users an interface which contains listview, toolbar and navigation view.
 * ImageView soccer_headlineImage will be the top article's thumbnail of all the articles.
 * The progressBar will be showed up as the first and the list will come up after that.
 * {@link AppCompatActivity}
 * */
public class SoccerFavorites extends AppCompatActivity {
    protected ArrayList<Article> articleList = new ArrayList();
    private Toolbar toolbar;
    private ImageView soccer_headlineImage_db;
    SharedPreferences prefs = null;
    /**
     * dbAdapter
     * this class member is an instance of DBAdapter which class helps to get connected to SQLiteDatabase.
     * */
    DBAdapter dbAdapter;
    SQLiteDatabase db;
    /**
     * onCreate
     * @param savedInstanceState
     * This method creates essential components of this activity.
     * progress bar, ImageView, listView and navigation View has come up.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_favorites);

        toolbar = (Toolbar) findViewById(R.id.soccer_toolbar_db);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.soccer_drawer_layout_db);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.soccer_nav_view_db);
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

        ListView myList = findViewById(R.id.list_item_db);
        dbAdapter = new DBAdapter();
        myList.setAdapter(dbAdapter);

        myList.setOnItemLongClickListener((parent, view, position, id) ->{
            AlertDialog.Builder alterBuilder = new AlertDialog.Builder(this);

            alterBuilder.setTitle(R.string.soccer_db_delete).setMessage(getString(R.string.soccer_db_id)+(position+1)+"\n"+getString(R.string.soccer_db_id2)+id)
                    .setPositiveButton(R.string.soccer_confirm,(click, arg)->{
                        db.execSQL("DELETE FROM "+SoccerDBHelper.TABLE_NAME+" WHERE "+SoccerDBHelper.Id+" = "+id);
                        articleList.remove(position);
                        dbAdapter.notifyDataSetChanged();

                    }).setNeutralButton(R.string.soccer_undo_button,(click,arg)->{

            }).setView(getLayoutInflater().inflate(R.layout.soccer_row_layout,null))
                    .create().show();
            dbAdapter.notifyDataSetChanged();
            return true;
        });
        myList.setOnItemClickListener((parent, view, position, id) -> {
            Bundle data = new Bundle();
            data.putSerializable("Article", articleList.get(position));
            Intent intent = new Intent(this, SoccerDetailDBActivity.class);
            intent.putExtras(data);
            startActivity(intent);
        });
        soccer_headlineImage_db = (ImageView) findViewById(R.id.soccer_headlineImage_db);

    }
    /**
     * DBAdapter class
     * This class is designed to give a view to the listView in this layout.
     * This class takes a list which contains contents that should be showed up on the list.
     * This class is a subclass of {@link BaseAdapter}
     * */
    class DBAdapter extends BaseAdapter{
        /**
         * getCount
         * This method returns the size of the list.
         * @return list.size()
         * */
        @Override
        public int getCount() {
            return articleList.size();
        }
        /**
         * getItem
         * This method returns a specific object inside the list by a position.
         * @param position is a location of an object in the list.
         * @return list.get(position)
         * */
        @Override
        public Object getItem(int position) {
            return articleList.get(position);
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
            try{
                container = (Article) this.getItem(position);
            }
            catch (Exception e){
                e.printStackTrace();
            }
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

            View newView = inflater.inflate(R.layout.soccer_row_layout, parent,false);

            Article container = (Article) getItem(position);
            TextView tView = newView.findViewById(R.id.tViewSend);

            tView.setText(container.getTitle());
            soccer_headlineImage_db.setImageBitmap(BitmapUtility.getBitmapImage(articleList.get(0).getThumbnailUrl()));
            return newView;

        }
    }
    /**
     * loadData
     * This method is executed in the onStart() which means this occurs before the onCreate function.
     * This gets article data from the SQLite database and move the data to list as article objects.
     * */
    private void loadData(){
        SoccerDBHelper dbHelper = new SoccerDBHelper(this);
        db = dbHelper.getWritableDatabase();
        /**
         * Article data components
         * */
        String[] columns = {SoccerDBHelper.Id, SoccerDBHelper.COL_TITLE, SoccerDBHelper.COL_LINK,SoccerDBHelper.COL_DATE,SoccerDBHelper.COL_DESCRIPTION,SoccerDBHelper.COL_THUMBNAIL};
        Cursor results = db.query(false, SoccerDBHelper.TABLE_NAME, columns, null,null,null,null,null,null);

        int idPrimaryColumnIndex = results.getColumnIndex(SoccerDBHelper.Id);
        int titleColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_TITLE);
        int linkColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_LINK);
        int dateColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_DATE);
        int descriptionColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_DESCRIPTION);
        int thumbnailColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_THUMBNAIL);

        /**
         * The process to move data to list as new article objects
         * */
        while (results.moveToNext()){

            long idPrimary = results.getLong(idPrimaryColumnIndex);
            String title = results.getString(titleColumnIndex);
            String link = results.getString(linkColumnIndex);
            String date = results.getString(dateColumnIndex);
            String description = results.getString(descriptionColumnIndex);
            byte[] thumbnail = results.getBlob(thumbnailColumnIndex);
            articleList.add(new Article(idPrimary, title, link, date, description, thumbnail));
        }
        results.moveToFirst();
       //printCursor(results, db.getVersion());
        Log.e("SOCCER DATABASE", "loadData: "+articleList.size());
    }
    /**
     * onStart
     * This method is executed before the onCreate().
     * The rating dialog box is provided to make user rates this app.
     * Once the rating has been finished, sharedpreferences stores the star values and never ask this again.
     * This method invokes loadData().
     * */
    @Override
    protected void onStart() {
        super.onStart();
        /**
         * prefs is pre-defined as a class member and invoked here.
         * */
        prefs = getSharedPreferences("SOCCER", Context.MODE_PRIVATE);
        /**
         * rankDialog contains a soccer_rating layout which has textView, RatingBar, and Button.
         * */
        Dialog rankDialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        rankDialog.setContentView(R.layout.soccer_rating);
        rankDialog.setCancelable(true);
        RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        /**
         * set the initial value of the rating bar.
         * */
        ratingBar.setRating(2);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText(R.string.soccer_rating_main);

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float i = ratingBar.getRating();
                Log.d("Rating count", "onClick: "+i);
                /**
                 * This method stores the rating value as "SOCCER_RATE"
                 * */
                saveSharedPreference("SOCCER_RATE",i);
                rankDialog.dismiss();
            }
        });
        /**
         * If there is a rate value that stored already, then the dialog box does not show up
         * */
        float rate = prefs.getFloat("SOCCER_RATE", -1);
        if(rate < 0){
            rankDialog.show();
        }
        loadData();
    }
    /**
     * saveSharedPreference
     * @param s is a name of the value
     * @param rate is a value that you want to store
     * when the editing of sharedPreferences has done, edit will commit the changes.
     * */
    private void saveSharedPreference(String s, float rate){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat(s, rate);
        edit.commit();
    }
}
