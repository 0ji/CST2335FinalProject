package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DatabaseView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_db_view);

        //Bundle dataToPass = getIntent().getExtras(); // get data passed from main, if any
        // TODO: load db from fragment?
        FragmentDatabase dbFragment = new FragmentDatabase();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentDB, dbFragment)
                .commit();

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(x -> {
            finish();
        });
    }
}