package com.cst2335.cst2335finalproject.trivia;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *  @author Akshay Gokani
 * TriviaDBOpener to open and set uesry for database
 */
public class TriviaDbOpener extends SQLiteOpenHelper {


    protected  final static String DATABASE_NAME = "TriviaDB";
    protected final static int VERSION_NUM = 3;
    public final static String TABLE_QUESTION_STATE = "QUESTION_STATE";
    public final static String TABLE_PLAYER = "tbl_Player";
    public final static String COL_PLAYER = "Player";
    public final static String COL_SCORE = "Score";
    public final static String COL_ID="id";
    public final static String COL_UNASWERED = "COL_UNASWERED";
    public final static String COL_CORRECT = "COL_CORRECT";
    public final static String COL_INCORRET = "Score";

    public TriviaDbOpener(Activity ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    public TriviaDbOpener(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PLAYER + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PLAYER + " text,"
                + COL_SCORE  + " text);");
     /*   db.execSQL("CREATE TABLE " + TABLE_QUESTION_STATE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PLAYER + " text," + COL_SCORE  + " INTEGER, "
                + COL_ID  + " INTEGER);");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);
        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        //Create a new table:
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);

        //Create a new table:
        onCreate(db);
    }


}

