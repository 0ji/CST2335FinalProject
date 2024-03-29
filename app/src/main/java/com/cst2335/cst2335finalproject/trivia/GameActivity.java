package com.cst2335.cst2335finalproject.trivia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cst2335.cst2335finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {


    String pickQuestion;
    String correctAns;
    String[] incorrectAnsArray;
    String a;
    String d;
    String t;
    SQLiteDatabase db;

    // list adapters and list for both listview
    QuestionListAdapter listAdapter = new QuestionListAdapter();

    private ArrayList<Question> question_list = new ArrayList<>();


    String trivia_url;
    boolean is_multiple;

    int unansweredByUser;
    int correctByUser;
    int incorrectByUser;

    RadioGroup boolean_group;
    RadioGroup multiple_group;

    RadioButton true_radio;
    RadioButton false_radio;

    RadioButton answer1;
    RadioButton answer2;
    RadioButton answer3;
    RadioButton answer4;

    String question_data;
    String category_data;
    String difficulty_data;
    String correct_answer_data;
    String incorrect_answers_data;
    String type_data;

    // column headers for fquestion database
    public static final String QUESTION_SELECTED = "Question";
    public static final String QUESTION_CORRECT_ANSWER = "ANSWER";
    public static final String QUESTION_ID = "ID";
    public static final String IS_ANSWERED = "is answered";

    private RadioButton listRadioButton = null;
    int listIndex = -1;

    @Override
    protected void onPause() {
        super.onPause();

        TextView unanswered = findViewById(R.id.unanswered);
        TextView incorrect = findViewById(R.id.correct_answer);
        TextView correct = findViewById(R.id.incorrect_answer);

        SharedPreferences sp_user = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_user.edit();
        editor.putString("unanswered", unanswered.getText().toString());
        editor.putString("correct", correct.getText().toString());
        editor.putString("incorrect", incorrect.getText().toString());

        editor.commit();
        Log.i("ResultsActivity", "in onPause ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ProgressBar pb = findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolB);
        setSupportActionBar(toolbar);

        // user sp to fill our url for API
        SharedPreferences sharedPreferences = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        a = sharedPreferences.getString("amount", "").toLowerCase();
        d = sharedPreferences.getString("difficulty", "").toLowerCase();
        t = sharedPreferences.getString("type", "").toLowerCase();

        if (t.equals("both")) {
            trivia_url = "https://opentdb.com/api.php?amount=" + a + "&difficulty=" + d;
        } else {
            trivia_url = "https://opentdb.com/api.php?amount=" + a + "&difficulty=" + d + "&type=" + t;
        }

        View boolean_view = getLayoutInflater().inflate(R.layout.boolean_questions, null);
        // TODO : TOOK OUT FROM XML FOR CHANGING TO BUTTONS
        //  boolean_group = boolean_view.findViewById(R.id.boolean_group);
        View multiple_View = getLayoutInflater().inflate(R.layout.multiple_questions, null);
        multiple_group = multiple_View.findViewById(R.id.rMultipleGroup);

        // assign list view
        ListView lv_trivia = findViewById(R.id.triviaListView);


        // counter views
        TextView tv_correct = findViewById(R.id.correct_answer);
        TextView tv_incorrect = findViewById(R.id.incorrect_answer);
        TextView tv_unanswered = findViewById(R.id.unanswered);

        // set unaswered to intial size of list
        unansweredByUser = Integer.parseInt(a.toString());
        correctByUser = 0;
        incorrectByUser = 0;

        // list to keep track for counter
        tv_unanswered.setText( String.valueOf(unansweredByUser));
        tv_correct.setText("Correct: " +String.valueOf(correctByUser));
        tv_incorrect.setText("Wrong: " +String.valueOf(incorrectByUser));

        answer1 = multiple_View.findViewById(R.id.Answer1);
        answer2 = multiple_View.findViewById(R.id.Answer2);
        answer3 = multiple_View.findViewById(R.id.Answer3);
        answer4 = multiple_View.findViewById(R.id.Answer4);

        true_radio = boolean_view.findViewById(R.id.trueRadio);

        lv_trivia.setAdapter(listAdapter);

        // query
        TriviaResults query = new TriviaResults();
        // jumps to doInbackgroudn()
        query.execute(trivia_url);

        // trying to send results to next page
        Button submitB = findViewById(R.id.submit_button);

        // send to results coiunter info when submitting
        Intent results_page = new Intent(this, ResultsActivity.class);
        submitB.setOnClickListener(v -> {
            results_page.putExtra("Amount un-asnwered", unansweredByUser);
            results_page.putExtra("Amount Answered Correct", correctByUser);
            results_page.putExtra("Amount of incorrect answered", incorrectByUser);
            startActivity(results_page);
        });


    }

    /**
     * onOptionItemSelected - helpw with navbar
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);//Look at your menu XML file. Put a case for every id in that file:

        switch (item.getItemId()){
            case R.id.itemid:
                alert.setTitle("How to use Trivia API");
                alert.setMessage("Please press the radio buttons the counter on the top left will keep track of how many question you have answered and if they are wrong or correct");
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
    }

    /**
     * Trvia results handles background task and parses json data
     */
    public class TriviaResults extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listAdapter.notifyDataSetChanged();
        }

        /**
         * doInBackground: method connects to the servers and parses json data
         *
         * @param args
         * @return
         */
        @Override
        protected String doInBackground(String... args) {
            {
                try {

                    URL url = new URL(args[0]);

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

                    // Loads json object
                    for (int i = 0; i < j_array.length(); i++) {
                        JSONObject json_data = j_array.getJSONObject(i);
                        category_data = json_data.getString("category");
                        type_data = json_data.getString("type");
                        difficulty_data = json_data.getString("difficulty");
                        question_data = json_data.getString("question");
                        publishProgress(50);
                        correct_answer_data = json_data.getString("correct_answer");
                        incorrect_answers_data = json_data.getString("incorrect_answers");
                        incorrect_answers_data = incorrect_answers_data.substring(2, incorrect_answers_data.length() - 2);
                        incorrect_answers_data = incorrect_answers_data.replaceAll("\",\"", ",");
                        incorrectAnsArray = incorrect_answers_data.split(",");

                        // create new question in ArrayList
                        question_list.add(new Question(question_data, category_data, difficulty_data, correct_answer_data, incorrect_answers_data, type_data, i));
                        Question question = question_list.get(i);
                        // create a bundle to pass data to the new fragment
                        // todo: CURRENT use this bundle to pass data to next activity get category difficulty and
                        Bundle dataToPass = new Bundle();
                        dataToPass.putString(QUESTION_SELECTED, question.getQuestion());
                        dataToPass.putLong(QUESTION_ID, question.getId());
                        dataToPass.putString(QUESTION_CORRECT_ANSWER, question.getCorrect_answer());
                        if (type_data.equals("multiple")) {
                            is_multiple = true;
                        } else if (type_data.equals("boolean")) {
                            is_multiple = false;
                        }
                        publishProgress(75);
                        Log.i("question", question_list.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            publishProgress(100);
            return "done";
        }
    }

    /*
     * QuestionListAdapter : shows how to view the question in the list adapter
     * */
    class QuestionListAdapter extends BaseAdapter {
        int selectedPosition = 0;

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
            int random = r.nextInt(4);
            View trueOrFalse;
            TextView tv_correct = findViewById(R.id.correct_answer);
            TextView tv_incorrect = findViewById(R.id.incorrect_answer);
            TextView tv_unanswered = findViewById(R.id.unanswered);

            if (is_multiple) {
                View multiple = inflater.inflate(R.layout.multiple_questions, parent, false);
                TextView multiple_tv = multiple.findViewById(R.id.question_multiple);

                RadioButton answer1_tv = multiple.findViewById(R.id.Answer1);
                RadioButton answer2_tv = multiple.findViewById(R.id.Answer2);
                RadioButton answer3_tv = multiple.findViewById(R.id.Answer3);
                RadioButton answer4_tv = multiple.findViewById(R.id.Answer4);
                multiple_tv.setText(thisRow.getQuestion());
                String[] incorrect_answer_list = thisRow.getIncorrect_answers().split(",");
                answer4_tv.setText(thisRow.getCorrect_answer());
                answer3_tv.setText(incorrect_answer_list[0].trim());
                answer2_tv.setText(incorrect_answer_list[1].trim());
                answer1_tv.setText(incorrect_answer_list[2].trim());
                multiple_group = multiple.findViewById(R.id.rMultipleGroup);

                // multiple group radio listener
                    multiple_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton rb = multiple_group.findViewById(checkedId);
                            String multipleCorrect = thisRow.getCorrect_answer();
                            if (rb.getText().toString().equals(multipleCorrect)){
                                unansweredByUser--;
                                correctByUser++;
                            } else {
                                unansweredByUser--;
                                incorrectByUser++;
                            }
                        }
                    });
                    correctAns = thisRow.getCorrect_answer();
                    return multiple;
                } else
                    trueOrFalse = inflater.inflate(R.layout.boolean_questions, parent, false);
                    TextView boolean_tv = trueOrFalse.findViewById(R.id.question_boolean);
                    boolean_group = trueOrFalse.findViewById(R.id.boolean_group);
                    true_radio = trueOrFalse.findViewById(R.id.trueRadio);
                    false_radio = trueOrFalse.findViewById(R.id.falseRadio);
                    // boolean group radio listener
                    boolean_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton rb = trueOrFalse.findViewById(checkedId);
                            if (thisRow.getCorrect_answer().equals("True")){
                                if(rb.getText() == true_radio.getText()){
                                    unansweredByUser--;
                                    correctByUser++;
                                } else {
                                    unansweredByUser--;
                                    incorrectByUser++;
                                }
                            } else if(thisRow.getCorrect_answer().equals("False")) {
                                if(rb.getText() == false_radio.getText()) {
                                    unansweredByUser--;
                                    correctByUser++;
                                } else {
                                    unansweredByUser--;
                                    incorrectByUser++;
                                }
                            }
                        }
                    });
                correctAns = thisRow.getCorrect_answer();
                boolean_tv.setText(thisRow.getQuestion());
                return trueOrFalse;
            }
        }

    public void myOnClick(View v) {
        Log.d("DEBUG", "CLICKED " + v.getId());
        //unansweredByUser--
        TextView tv_unanswered = findViewById(R.id.unanswered);
        TextView tv_correct_answers = findViewById(R.id.correct_answer);
        TextView tv_incorrect_answers = findViewById(R.id.incorrect_answer);
        tv_unanswered.setText(String.valueOf(unansweredByUser));
        tv_correct_answers.setText(String.valueOf(correctByUser));
        tv_incorrect_answers.setText(String.valueOf(incorrectByUser));
    }
}