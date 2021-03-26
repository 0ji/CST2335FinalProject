package com.cst2335.cst2335finalproject.soccer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
/**
 * SoccerDBHelper
 * This class inherits properties from SQLiteOpenHelper class
 * This class contains static values to set database properties.
 * */
public class SoccerDBHelper extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "messageDB";
    protected final static int VERSION_NUM = 1;
    public final static String Id = "_id";
    public final static String TABLE_NAME = "SOCCER_RECORD";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_LINK = "LINK";
    public final static String COL_DATE = "DATE";
    public final static String COL_DESCRIPTION = "DESCRIPTION";
    public final static String COL_THUMBNAIL = "THUMBNAIL";
    /**
     *SoccerDBHelper is a constructor
     * @param ctx is a Context.
     * */
    public SoccerDBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    /**
     *SoccerDBHelper is another constructor
     * @param context is a Context.
     * @param name is a name.
     * @param factory is a CursorFactory from SQLiteDatabase.
     * @param version is a current database version.
     * */
    public SoccerDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    /**
     * onCreate will be executed to create a new database when the version is new
     * @param db is a SQLiteDatabase
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE +" text, "+ COL_LINK+" text, "+ COL_DATE+" text, "+ COL_DESCRIPTION+" text, "+ COL_THUMBNAIL+" BLOB);");
    }
    /**
     * onUpgrade will be executed to return existed database when the old version is the same as the new version
     * @param db is a SQLiteDatabase.
     * @param oldVersion indicates an old version databse.
     * @param newVersion indicates an new version databse.
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
}

