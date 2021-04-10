package com.cst2335.cst2335finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

/**
 * empty activity to load either database or car details.
 */
public class CarEmptyFragmentView extends AppCompatActivity {

    /**
     * loads appropriate fragment based on the data passed -- add/remove/dbview.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_fragment_view);

        Bundle dataToPass = getIntent().getExtras(); // get data passed from main, if any

        if (dataToPass.getString("DB").contains("add")) {
            FragmentCarItem itemFragment = new FragmentCarItem();
            itemFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentDB, itemFragment)
                    .commit();
        } else if (dataToPass.getString("DB").contains("dbview")) {
            FragmentDatabase itemFragment = new FragmentDatabase();
            itemFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentDB, itemFragment)
                    .commit();

        } else if (dataToPass.getString("DB").contains("remove")) {
            FragmentCarItem itemFragment = new FragmentCarItem();
            itemFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentDB, itemFragment)
                    .commit();
        }

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(x -> finish());
    }
}