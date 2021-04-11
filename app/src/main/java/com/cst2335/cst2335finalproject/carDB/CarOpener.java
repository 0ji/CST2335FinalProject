package com.cst2335.cst2335finalproject.carDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * helper class for opening, creating, modifying, and deleting items for the car database
 */
public class CarOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "CarDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "CARS";
    public final static String COL_MAKE_ID = "makeID";
    public final static String COL_MODEL_ID = "modelID";
    public final static String COL_MAKE = "make";
    public final static String COL_MODEL = "model";
    public final static String COL_ID = "_id";


    public CarOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * called when no db exists.
     * @param db the db itself
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MAKE_ID + " integer,"
                + COL_MODEL_ID + " integer,"
                + COL_MAKE + " text,"
                + COL_MODEL + " text);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
