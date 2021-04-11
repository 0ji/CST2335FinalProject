package com.cst2335.cst2335finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cst2335.cst2335finalproject.R;

public class FragmentPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_phone);
        Bundle dataToPass = getIntent().getExtras();

        SongDetail dFragment= new SongDetail();

        dFragment.setArguments(dataToPass);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,dFragment,"songlist").commit();


    }
}