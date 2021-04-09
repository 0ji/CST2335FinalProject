package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;

/**
 * empty activity to load either database or car details.
 */
public class FragmentView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_fragment_view);

        Bundle dataToPass = getIntent().getExtras(); // get data passed from main, if any

        // TODO: load db from fragment?
        if (dataToPass.containsKey(CarDBActivity.CAR_MODEL) != false) {
            FragmentCarItem itemFragment = new FragmentCarItem();
            itemFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentDB, itemFragment)
                    .commit();
        } else if (dataToPass.containsKey("DB")) {
            FragmentDatabase dataFragment = new FragmentDatabase();
            dataFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentDB, dataFragment)
                    .commit();
        }


        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(x -> {
            finish();
        });
    }
}