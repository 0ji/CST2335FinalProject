package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // change content view here^
        // koji was here :)

        Button triviaSelection = findViewById(R.id.goToTrivia);

        triviaSelection.setOnClickListener(v -> {
            Intent goToGame = new Intent(this, TriviaActivity.class);
            startActivity(goToGame);
        });
    }
}