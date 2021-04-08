package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DatabaseView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_db_view);

        Bundle dataToPass = getIntent().getExtras(); // get data passed from main, if any
        // TODO: load db from fragment

    }
}