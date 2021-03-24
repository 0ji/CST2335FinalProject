package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.cst2335.cst2335finalproject.soccer.SoccerMainActiv;
/**
 * MainActivity
 * This activity is a hub to cross to each activity.
 * */
public class MainActivity extends AppCompatActivity {
    /**
     * Button goToSoccer
     * This button is used for moving to the soccer activity
     * */
    Button goToSoccer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Button goToSoccer
         * This button is used for moving to the soccer activity
         * */
        goToSoccer = (Button) findViewById(R.id.goToSoccer);
        goToSoccer.setOnClickListener(v->{
            changePage(SoccerMainActiv.class);
        });

    }
    /**
     * changePage
     * @param c is new class which a user wants
     * This method is used for jumping into another page.
     * */
    private void changePage(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}