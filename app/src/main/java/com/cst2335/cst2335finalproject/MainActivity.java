package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.cst2335.cst2335finalproject.soccer.SoccerMainActiv;

public class MainActivity extends AppCompatActivity {
    Button goToSoccer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Jin is working on soccerAPI
        goToSoccer = (Button) findViewById(R.id.goToSoccer);
        goToSoccer.setOnClickListener(v->{
            showDialog();

            changePage(SoccerMainActiv.class);
        });

    }
    private void showDialog(){
        Log.i(ACTIVITY_SERVICE, "FISRT");}
    private void changePage(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}