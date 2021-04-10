package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TriviaActivity extends AppCompatActivity {
    EditText amount;
    EditText difficulty;
    EditText type;


    @Override
    protected void onPause() {
        super.onPause();
        amount = findViewById(R.id.numberOfQuestions);
        difficulty = findViewById(R.id.difficulty);
        type = findViewById(R.id.type);       
        SharedPreferences sp = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("amount", amount.getText().toString());
        editor.putString("difficulty", difficulty.getText().toString());
        editor.putString("type", type.getText().toString());
        editor.commit();

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        Button createGame = findViewById(R.id.createGame);
        // need to check if amount is empty is it is cant run
       // amount = findViewById(R.id.numberOfQuestions);
        // should be able to run even when difficulty and type is empty
      //  difficulty = findViewById(R.id.difficulty);
      //  type = findViewById(R.id.type);
        amount = findViewById(R.id.numberOfQuestions);
        difficulty = findViewById(R.id.difficulty);
        type = findViewById(R.id.type);
        SharedPreferences sp = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        amount.setText(sp.getString("amount", ""));
        difficulty.setText(sp.getString("difficulty", ""));
        type.setText(sp.getString("type", ""));

        // ONLY NEED THESE VALUES TO BUILD THE STRING FOR THE URL
        /*String amount = amount.getText().toString();
        String difficulty = difficulty.getText().toString();
        String type = type.getText().toString();*/
        
       // String triviaUrl = "https:/opentdb.com/api.php?amount=" + amount + "&type=" + type + "&difficulty=" + difficulty;
        createGame.setOnClickListener(v -> {
            if(TextUtils.isEmpty(difficulty.getText())){
                Toast.makeText(this, "Difficulty cannot be left empty",
                        Toast.LENGTH_LONG).show();
            }
            if(TextUtils.isEmpty(type.getText())){
                Toast.makeText(this, "Please type in boolean or multiple make sure there are no spaces ",
                        Toast.LENGTH_LONG).show();
            }
            if(TextUtils.isEmpty(amount.getText())){
                Toast.makeText(this,
                        "Amount cannot be left empty",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent goToGame = new Intent(TriviaActivity.this, GameActivity.class);
                startActivity(goToGame);
            }
        });
    }
}