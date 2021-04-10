package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static final String COL_ID = "_id";
    public static final String QUESTION_SELECTED = "Question"; // name
    public static final String QUESTION_ANSWER = "ANSWERS";
    public static final String QUESTION_CATEGORY = "CATEGORY";
    public static final String QUESTION_DIFFICULTY = "DIFFICULTY";
    public static final String QUESTION_IS_MULTIPLE = "IS_MULTIPLE";
    private static final String ACTIVITY_CHAT = "GAME_ACTIVITY" ;
    String a;
    String d;
    String t;
    SQLiteDatabase db;
    MyListAdapter listAdapter = new MyListAdapter();
    private ArrayList<Question> question_list =  new ArrayList<>();
    Question new_question;
    boolean is_multiple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ProgressBar pb = findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        a = sharedPreferences.getString("amount", "");
        d = sharedPreferences.getString("difficulty", "");
        t = sharedPreferences.getString("type", "");

        String trivia_url = "https://opentdb.com/api.php?amount="+a+"&difficulty="+d+"&type="+t;
        // assign screen view
        ListView lv = findViewById(R.id.triviaListView);
        lv.setAdapter(listAdapter);
        Button submitB = findViewById(R.id.submit_button);

        // these are the values we will insert into the database
        ContentValues dbValues = new ContentValues();

        // open Databases
        MyDbOpener dbOpener = new MyDbOpener(this);
        db = dbOpener.getWritableDatabase();





        // query all results form the data base
        String [] columns = {MyDbOpener.COL_ID,MyDbOpener.COL_QUESTION, MyDbOpener.COL_CATEGORY , MyDbOpener.COL_DIFFICULTY, MyDbOpener.COL_IS_MULTIPLE,
                MyDbOpener.COL_INCORRECT_ANSWER, MyDbOpener.COL_CORRECT_ANSWER};
        Cursor results = db.query(false, MyDbOpener.TABLE_NAME, columns, null, null,
                null, null, null, null);
        printCursor(results, db.getVersion());

        // find the column indices
        int isMultipleColumnIndex = results.getColumnIndex(MyDbOpener.COL_IS_MULTIPLE);
        int questionColIndex = results.getColumnIndex(MyDbOpener.COL_QUESTION);
        int categoryColIndex = results.getColumnIndex(MyDbOpener.COL_CATEGORY);
        int difficultyColIndex = results.getColumnIndex(MyDbOpener.COL_DIFFICULTY);
        int incorrectAnswerColIndex = results.getColumnIndex(MyDbOpener.COL_INCORRECT_ANSWER);
        int correctAnswerColIndex = results.getColumnIndex(MyDbOpener.COL_CORRECT_ANSWER);
        int idColIndex = results.getColumnIndex(MyDbOpener.COL_ID);

        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Do you want to delete this ?");
            // displays row position and id in database

            //TODO: see if the right position is getting deleted
            alert.setMessage("The selected row is " + position + "\nThe database ID is " + question_list.get(position).getId());
            listAdapter.notifyDataSetChanged();
            alert.setCancelable(false);
            // sets positive button to delete a row
            alert.setPositiveButton("+", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    question_list.remove(position);
                    // todo - changed this to position-1 to see if i can fix the last item in list view not deleting
                    // NOTES : -1 DOES WORK but it changes the bug to if you delete the first one the program crashes
                    deleteQuestion(question_list.get(position-1));
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

        TriviaResults query = new TriviaResults();
        // jumps to doInbackgroudn()
        query.execute(trivia_url);



    }

    public class TriviaResults extends AsyncTask<String, Integer, String> {

        String question_data;
        String category_data;
        String difficulty_data;
        String correct_answer_data;
        String incorrect_answers_data;
        String type_data;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // this has to be done in ui thread
            listAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... args) {

           /* SharedPreferences sharedPreferences = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
            a = sharedPreferences.getString("amount", "");
            d = sharedPreferences.getString("difficulty", "");
            t = sharedPreferences.getString("type", "");
*/
            {
                try {
                    // url objects for which se2rver to connect to once users have entere the trivia they want
                    // TODO: need to put this in if statements to change url based on number of feilds user has entered, will not work if amount is not explicit : use snackbar to notify
                    String triviaUrl = "https://opentdb.com/api.php?amount="+a+"&difficulty="+d+"&type="+t; // this works becasue it is in the right order
                    URL url = new URL(args[0]);

                    //https://opentdb.com/api.php?amount=10&difficulty=medium&type=multiple ONLY URL THAT IS WORKING
                    //open the connection for trivia api
                    HttpURLConnection trivia_connection = (HttpURLConnection) url.openConnection();
                    InputStream trivia_response = trivia_connection.getInputStream();
                    publishProgress(25);
                    // create a new reader and give it the inputStream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(trivia_response, "UTF-8"), 8);
                    // string builder to g
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String trivia_result = sb.toString(); // trivia result is the whole string

                    // convert string to JSON // JSON read about it
                    // json object start { ends with } json array starts with [ ends with ]

                    JSONObject json = new JSONObject(trivia_result);
                    JSONArray j_array = json.getJSONArray("results");
                    System.out.println("******j_array*******" + j_array.length()); // TESTING
                    Log.i("size", String.valueOf(j_array.length()));
                    publishProgress(25);


                    // CURRENT - just finished this one
                    // now i need to get the listAdapter working // use Question class to hold questions
                    for(int i = 0; i < j_array.length(); i++){
                         JSONObject json_data = j_array.getJSONObject(i);
                         category_data = json_data.getString("category");

                         type_data = json_data.getString("type");
                         difficulty_data = json_data.getString("difficulty");
                         question_data = json_data.getString("question");
                         publishProgress(50);
                         correct_answer_data = json_data.getString("correct_answer");
                         incorrect_answers_data = json_data.getString("incorrect_answers");
                         publishProgress(75);
                        // create new question in ArrayList
                         question_list.add(new Question(question_data, category_data, difficulty_data, correct_answer_data, incorrect_answers_data , type_data, i));

                         if(type_data.equals("multiple")){
                            is_multiple = true;
                             publishProgress(100);
                        } else if(type_data.equals("boolean")){
                            is_multiple = false;
                             publishProgress(100);
                        }


                        // WAS USING THIS WHE USING LINKED LIST FOR QUESTION_LIST
                      /*  while(!question_list.isEmpty()){
                            if(type_data == "multiple"){
                                    is_multiple = true;
                            } else is_multiple = false;
                        }*/
                       // Question q = new Question(question_data, category_data, difficulty_data, correct_answer_data, incorrect_answers_data , type_data, i);
                        Log.i("question" , question_list.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "done";
        }
    }

    protected void deleteQuestion(Question q){
        db.delete(MyDbOpener.TABLE_NAME, MyDbOpener.COL_ID + "= ?", new String[] {Long.toString(q.getId())});

    }

    public void printCursor(Cursor c , int version){

        int isMultipleColumnIndex = c.getColumnIndex(MyDbOpener.COL_IS_MULTIPLE);
        int questionColIndex = c.getColumnIndex(MyDbOpener.COL_QUESTION);
        int categoryColIndex = c.getColumnIndex(MyDbOpener.COL_CATEGORY);
        int difficultyColIndex = c.getColumnIndex(MyDbOpener.COL_DIFFICULTY);
        int incorrectAnswerColIndex = c.getColumnIndex(MyDbOpener.COL_INCORRECT_ANSWER);
        int correctAnswerColIndex = c.getColumnIndex(MyDbOpener.COL_CORRECT_ANSWER);

        int idColIndex = c.getColumnIndex(MyDbOpener.COL_ID);



        Log.i("CHATROOM_ACTIVITY", "Database version number:" + version);
        Log.i(ACTIVITY_CHAT, "\nNumber of columns in the cursor :" + c.getColumnCount());
        Log.i(ACTIVITY_CHAT, "\nNames of columns in the cursor = \n id:"
                + c.getColumnName(idColIndex) + "\ncontent:" + c.getColumnName(questionColIndex) + "\nType:" + c.getColumnName(isMultipleColumnIndex)
                + "\nDifficulty:" + c.getColumnName(difficultyColIndex) + "\nCategory:" + c.getColumnName(categoryColIndex)
                + "\nIncorrectAnswer:" + c.getColumnName(incorrectAnswerColIndex) + "\nCorrectAnswer:" + c.getColumnName(correctAnswerColIndex));
        Log.e(ACTIVITY_CHAT, "Number of row in the cursor :" + c.getCount());
        // DUMPS Database contents into logcat
        Log.v(ACTIVITY_CHAT, DatabaseUtils.dumpCursorToString(c));
        // c.moveToFirst();
    }



    class MyListAdapter extends BaseAdapter {

        // size of the linked list
        @Override
        public int getCount() {
            return question_list.size();
        }
        // return the question list postiion
        @Override
        public Question getItem(int position) {
            return question_list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return question_list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            // ASK QUESITON WHY THIS NEED
            Question thisRow = getItem(position);
            Random r = new Random();
            int random =  r.nextInt(4);
            View boolean2;

            if(is_multiple){
                View multiple = inflater.inflate(R.layout.multiple_questions, parent, false);
                TextView multiple_tv = multiple.findViewById(R.id.question_multiple);

                TextView answer1_tv = multiple.findViewById(R.id.Answer1);
                TextView answer2_tv = multiple.findViewById(R.id.Answer2);
                TextView answer3_tv = multiple.findViewById(R.id.Answer3);
                TextView answer4_tv = multiple.findViewById(R.id.Answer4);
                multiple_tv.setText(thisRow.getQuestion());

                //
                String [] incorrect_answer_list = thisRow.getIncorrect_answers().split(",");

                if(random == 0){
                    answer4_tv.setText(thisRow.getCorrect_answer());
                    answer3_tv.setText(incorrect_answer_list[0]);
                    answer2_tv.setText(incorrect_answer_list[1]);
                    answer1_tv.setText(incorrect_answer_list[2]);
                } else if(random ==1){
                    answer2_tv.setText(thisRow.getCorrect_answer());
                    answer1_tv.setText(incorrect_answer_list[0]);
                    answer3_tv.setText(incorrect_answer_list[1]);
                    answer4_tv.setText(incorrect_answer_list[2]);
                } else if(random ==2){
                    answer3_tv.setText(thisRow.getCorrect_answer());
                    answer2_tv.setText(incorrect_answer_list[0]);
                    answer4_tv.setText(incorrect_answer_list[1]);
                    answer3_tv.setText(incorrect_answer_list[2]);
                } else {
                    answer1_tv.setText(thisRow.getCorrect_answer());
                    answer3_tv.setText(incorrect_answer_list[0]);
                    answer2_tv.setText(incorrect_answer_list[1]);
                    answer4_tv.setText(incorrect_answer_list[2]);
                }
                // add to answers
                return multiple;
            } else
                boolean2 = inflater.inflate(R.layout.boolean_questions , parent, false);
                TextView boolean_tv = boolean2.findViewById(R.id.question_boolean);
                boolean_tv.setText(thisRow.getQuestion());
            return boolean2;
        }
    }
}