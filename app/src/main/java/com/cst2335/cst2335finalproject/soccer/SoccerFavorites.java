package com.cst2335.cst2335finalproject.soccer;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.cst2335finalproject.MainActivity;
import com.cst2335.cst2335finalproject.R;
import com.cst2335.cst2335finalproject.SoccerFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class SoccerFavorites extends AppCompatActivity {
    protected ArrayList<Article> articleList = new ArrayList();
    private Toolbar toolbar;
    private ImageView soccer_headlineImage_db;
    DBAdapter dbAdapter;
    SQLiteDatabase db;
    //SoccerFragment dbFragment;
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

        ListView myList = findViewById(R.id.list_item_db);
        dbAdapter = new DBAdapter();
        myList.setAdapter(dbAdapter);

        myList.setOnItemLongClickListener((parent, view, position, id) ->{
            AlertDialog.Builder alterBuilder = new AlertDialog.Builder(this);
            alterBuilder.setTitle("Do you want to delete this?").setMessage("The selected row is:"+(position+1)+"\nThe database id is:"+id)
                    .setPositiveButton("Yes",(click, arg)->{
                        db.execSQL("DELETE FROM "+SoccerDBHelper.TABLE_NAME+" WHERE "+SoccerDBHelper.Id+" = "+id);
                        articleList.remove(position);
                        dbAdapter.notifyDataSetChanged();

                    }).setNeutralButton("Cancel",(click,arg)->{

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

    class DBAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return articleList.size();
        }

        @Override
        public Object getItem(int position) {
            return articleList.get(position);
        }

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

    private void loadData(){
        SoccerDBHelper dbHelper = new SoccerDBHelper(this);
        db = dbHelper.getWritableDatabase();

        String[] columns = {SoccerDBHelper.Id, SoccerDBHelper.COL_TITLE, SoccerDBHelper.COL_LINK,SoccerDBHelper.COL_DATE,SoccerDBHelper.COL_DESCRIPTION,SoccerDBHelper.COL_THUMBNAIL};
        Cursor results = db.query(false, SoccerDBHelper.TABLE_NAME, columns, null,null,null,null,null,null);

        int idPrimaryColumnIndex = results.getColumnIndex(SoccerDBHelper.Id);
        int titleColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_TITLE);
        int linkColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_LINK);
        int dateColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_DATE);
        int descriptionColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_DESCRIPTION);
        int thumbnailColumnIndex = results.getColumnIndex(SoccerDBHelper.COL_THUMBNAIL);


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

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }
}
