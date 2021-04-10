package com.cst2335.cst2335finalproject;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDbOpener extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 5;
    public static final String TABLE_NAME = "Trivia";
    public static final String COL_ID = "_id";
    public static final String COL_QUESTION = "Question"; // name
    public static final String COL_CORRECT_ANSWER = "CORRECT_ANSWER";
    public static final String COL_INCORRECT_ANSWER = "INCORRECT_ANSWERS";
    public static final String COL_CATEGORY = "CATEGORY";
    public static final String COL_DIFFICULTY = "DIFFICULTY";
    public static final String COL_IS_MULTIPLE = "IS_MULTIPLE"; // CHECK WHAT TYPE OF QUESTION

    public MyDbOpener(Activity ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_QUESTION + " TEXT, "+ COL_CORRECT_ANSWER + " TEXT, "+ COL_INCORRECT_ANSWER + " TEXT, "
                + COL_CATEGORY + " TEXT, "+ COL_DIFFICULTY + " TEXT, "+ COL_IS_MULTIPLE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
