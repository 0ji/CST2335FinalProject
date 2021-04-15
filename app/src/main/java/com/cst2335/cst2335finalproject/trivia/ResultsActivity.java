package com.cst2335.cst2335finalproject.trivia;

import androidx.appcompat.app.AlertDialog;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.cst2335finalproject.R;

import java.util.ArrayList;


public class ResultsActivity extends GameActivity {
    public static final String COL_ID = "_id";
    public static final String QUESTION_SELECTED = "Question"; // name
    public static final String QUESTION_ANSWER = "ANSWERS";
    public static final String QUESTION_CATEGORY = "CATEGORY";
    public static final String QUESTION_DIFFICULTY = "DIFFICULTY";
    public static final String QUESTION_IS_MULTIPLE = "IS_MULTIPLE";
    private static final String ACTIVITY_CHAT = "RESULTS_ACTIVITY";
    HighScoreListAdapter listAdapter = new HighScoreListAdapter();
    private ArrayList<user> high_score_list = new ArrayList<>();
    EditText name_input;

    @Override
    protected void onPause() {
        super.onPause();
        /*amount = findViewById(R.id.numberOfQuestions);
        difficulty = findViewById(R.id.difficulty);*/
        name_input = findViewById(R.id.name_input);
       // TextView score_input = findViewById(R.id.score);
        SharedPreferences sp_user = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_user.edit();
        editor.putString("name", name_input.getText().toString());
        //editor.putString("score", score_input.getText().toString());
        editor.commit();
        Log.i("ResultsActivity", "in onPause ");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ListView lv_results = findViewById(R.id.results_list_view);
        lv_results.setAdapter(listAdapter);
        Button add_button = findViewById(R.id.addUser);
        name_input = findViewById(R.id.name_input);
        TextView score = findViewById(R.id.high_score_titles);
        TextView user = findViewById(R.id.user);
        TextView unanswered_results = findViewById(R.id.unanswered_textv);
        TextView correct_ans_results = findViewById(R.id.correct_answer_textv);
        TextView incorrect_ans_results = findViewById(R.id.incorrect_answer_textv);

        SharedPreferences sp_user = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        name_input.setText(sp_user.getString("name", ""));
        unanswered_results.setText("Unanswered: "+sp_user.getString("unanswered", ""));
        correct_ans_results.setText("Correct: "+sp_user.getString("correct",""));
        incorrect_ans_results.setText("Wrong: "+sp_user.getString("incorrect",""));


        SharedPreferences sp_trivia = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        a = sp_trivia.getString("amount", "");
        String correct = sp_user.getString("correct", "");
        int correctCalculate = Integer.parseInt(correct);
        int dividedby = Integer.parseInt(a);
        float finalScore = (correctCalculate * 100) / dividedby;
        String finalScoreString = finalScore + "%";
        score.setText(finalScoreString);

        // these are the values we will insert into the database
        ContentValues dbValues = new ContentValues();

        // open Databases
        TriviaDbOpener dbOpener = new TriviaDbOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {TriviaDbOpener.COL_ID, TriviaDbOpener.COL_PLAYER,// MyDbOpener.COL_CATEGORY, MyDbOpener.COL_DIFFICULTY, MyDbOpener.COL_IS_MULTIPLE,
                TriviaDbOpener.COL_SCORE};//, MyDbOpener.COL_CORRECT_ANSWER , MyDbOpener.COL_USER_SELECTION};
        Cursor results = db.query(false, TriviaDbOpener.TABLE_PLAYER, columns, null, null,
                null, null, null, null);
        printCursor(results, db.getVersion());

        add_button.setOnClickListener(v -> {
            String user_string = name_input.getText().toString();
            // todo : Change the score so that it takes question.size() and divides but the amount that the user got correct * 100 so that it becomes a percentage
            //String score_string =
            dbValues.put(TriviaDbOpener.COL_PLAYER, user_string);
            long newId = db.insert(TriviaDbOpener.TABLE_PLAYER, null, dbValues);
            user newUser = new user (user_string, finalScoreString , newId);
            high_score_list.add(newUser);
            listAdapter.notifyDataSetChanged();
           // name_input.setText("");
        });

        // query the columns you need
        int scoreColIndex = results.getColumnIndex(TriviaDbOpener.COL_SCORE);
        int playerColIndex = results.getColumnIndex(TriviaDbOpener.COL_PLAYER);
        int idColIndex = results.getColumnIndex(TriviaDbOpener.COL_ID);


// todo : THIS WORKS BUT BECAUSE I CANT KEEP THE SCORE I HAVE TO HARD CODE A NUMBER INTO SCORE NULL WILL BREAK THE CODEaKSHAY
        for (int i = 0; i < results.getCount(); i++) {
            if (results != null && results.moveToNext()) {
                String player = results.getString(playerColIndex);
                String scorefromDB = results.getString(scoreColIndex);
                long id = results.getLong(idColIndex);
                high_score_list.add(new user(player, scorefromDB, id));
                //controls how the the database info will be displayed on list view

            }
        }
        lv_results.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Do you want to delete this ?");
            // displays row position and id in database

            //TODO: see if the right position is getting deleted
            alert.setMessage("The selected row is " + position + "\nThe database ID is " + high_score_list.get(position).getId());
            listAdapter.notifyDataSetChanged();
            alert.setCancelable(false);
            // sets positive button to delete a row
            alert.setPositiveButton("+", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    high_score_list.remove(position);
                    // todo - changed this to position-1 to see if i can fix the last item in list view not deleting
                    // NOTES : -1 DOES WORK but it changes the bug to if you delete the first one the program crashes
                    deleteUser(high_score_list.get(position - 1));
                    //printCursor(results, db.getVersion());
                    listAdapter.notifyDataSetChanged();
                    //finish();
                }
            });
            // sets negative button to do nothing
            alert.setNegativeButton("-", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
            return true;
        });


    }
    public void printCursor(Cursor c, int version) {


        int user_col_index = c.getColumnIndex(TriviaDbOpener.COL_PLAYER);
        int score_col_index = c.getColumnIndex(TriviaDbOpener.COL_SCORE);
        int idColIndex = c.getColumnIndex(TriviaDbOpener.COL_ID);


        Log.i("CHATROOM_ACTIVITY", "Database version number:" + version);
        Log.i(ACTIVITY_CHAT, "\nNumber of columns in the cursor :" + c.getColumnCount());
        Log.i(ACTIVITY_CHAT, "\nNames of columns in the cursor = \n id:"
                + c.getColumnName(idColIndex) + "\nUSER:" + c.getColumnName(user_col_index) //+ "\nType:" + c.getColumnName(isMultipleColumnIndex)
                + "\nScore:" + c.getColumnName(score_col_index) + "\nCorrectAnswer:");
        Log.e(ACTIVITY_CHAT, "Number of row in the cursor :" + c.getCount());
        // DUMPS Database contents into logcat
        Log.v(ACTIVITY_CHAT, DatabaseUtils.dumpCursorToString(c));
        // c.moveToFirst();
        // you can choose to delete a question  [ NOT REQUIRED }
    }

    protected void deleteUser(user u) {
        db.delete(TriviaDbOpener.TABLE_PLAYER, TriviaDbOpener.COL_ID + "= ?", new String[]{Long.toString(u.getId())});
    }

    class HighScoreListAdapter extends BaseAdapter {
        int selectedPosition = 0;

        // size of the linked list
        @Override
        public int getCount() {
            return high_score_list.size();
        }

        // return the question list postiion
        @Override
        public user getItem(int position) {
            return high_score_list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return high_score_list.get(position).getId();
        }

        @Override // todo : CURRENT
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            // ASK QUESITON WHY THIS NEED
            user thisRow = getItem(position);

            TextView unanswered_results = findViewById(R.id.unanswered_textv);

            //unanswered_results.setText();

                View highScore = inflater.inflate(R.layout.activity_add_user, parent, false);
                TextView user_tv = highScore.findViewById(R.id.user);
                TextView score_tv = highScore.findViewById(R.id.score);
                user_tv.setText("player: " + thisRow.getName());
                score_tv.setText("HighScore: " + thisRow.getScore());

            return highScore;
        }
    }
}