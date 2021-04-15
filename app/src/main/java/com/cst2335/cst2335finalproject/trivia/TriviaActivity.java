package com.cst2335.cst2335finalproject.trivia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cst2335.cst2335finalproject.R;


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
        // save everything into the shared pref
        SharedPreferences sp = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("amount", amount.getText().toString());
        editor.putString("difficulty", difficulty.getText().toString());
        editor.putString("type", type.getText().toString());
        editor.commit();
    }

    // inflate the menu when clicked
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        // get views
        Button createGame = findViewById(R.id.createGame);
        Toolbar tb = findViewById(R.id.myToolB);
        // set action bar
        setSupportActionBar(tb);

        amount = findViewById(R.id.numberOfQuestions);
        difficulty = findViewById(R.id.difficulty);
        type = findViewById(R.id.type);

        // Set the edit texts to the last input it had
        SharedPreferences sp = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        amount.setText(sp.getString("amount", ""));
        difficulty.setText(sp.getString("difficulty", ""));
        type.setText(sp.getString("type", ""));

        // when you create game none of the edit text can be left empty
        createGame.setOnClickListener(v -> {
            if (TextUtils.isEmpty(difficulty.getText())) {
                Toast.makeText(this, "Difficulty cannot be left empty",
                        Toast.LENGTH_LONG).show();
            }
            if (TextUtils.isEmpty(type.getText())) {
                Toast.makeText(this, "Please type in boolean or multiple make sure there are no spaces ",
                        Toast.LENGTH_LONG).show();
            }
            if (TextUtils.isEmpty(amount.getText())) {
                Toast.makeText(this,
                        "Amount cannot be left empty",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent goToGame = new Intent(TriviaActivity.this, GameActivity.class);
                startActivity(goToGame);
            }
        });
    }
    // on optionItemSelected for the toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);//Look at your menu XML file. Put a case for every id in that file:

                switch (item.getItemId()){
                    case R.id.itemid:
                        alert.setTitle("How to use Trivia API");
                        // todo: change message to help with yours **************************************************&&&&&&&&&&&&&&&&&&&&&&&&&*************************^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                        alert.setMessage("Please enter the number of question you want, difficulty and the type of questions (Please use the exact words that have been hinted at in the views)." +
                                "\n\nStart quiz by clicking START TRIVIA button");
                        alert.setCancelable(false);
                        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                        break;
    }
        return true;
    }//Look at your menu XML file. Put a case for every id in that file:
}