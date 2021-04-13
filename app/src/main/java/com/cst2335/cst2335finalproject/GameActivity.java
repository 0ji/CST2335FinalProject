package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;

import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import java.util.zip.Inflater;

public class GameActivity extends AppCompatActivity {


    String pickQuestion;
    String correctAns;
    String[] incorrectAns;
    String a;
    String d;
    String t;
    SQLiteDatabase db;

    // list adapters and list for both listview
    QuestionListAdapter listAdapter = new QuestionListAdapter();

    private ArrayList<Question> question_list = new ArrayList<>();
    private ArrayList<Question> counter_list = new ArrayList<>();

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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ProgressBar pb = findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);


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

        View boolean_view =getLayoutInflater().inflate(R.layout.boolean_questions, null);
        boolean_group = boolean_view.findViewById(R.id.boolean_group);
        View multiple_View = getLayoutInflater().inflate(R.layout.multiple_questions, null);
        multiple_group = multiple_View.findViewById(R.id.rMultipleGroup);

        View counter = getLayoutInflater().inflate(R.layout.activity_counter, null);




        TextView tv_unanswered = findViewById(R.id.unanswered);

       // tv_unanswered.setText(String.valueOf(unanswered));

        // assign screen view
        ListView lv_trivia = findViewById(R.id.triviaListView);

        // counter views
        //TextView tv_unanswered = findViewById(R.id.unanswered);
        // todo: see if you still need this here *************************************************************************************
        TextView tv_correct = findViewById(R.id.correct_answer);
        TextView tv_incorrect = findViewById(R.id.incorrect_answer);

        // set unaswered to intial size of list
        unansweredByUser = Integer.parseInt(a.toString());
        // set correct and incorrect to 0 since no questions have been answered
        correctByUser = 0;
        incorrectByUser = 0;
        // list to keep track for counter
        tv_unanswered.setText(String.valueOf(unansweredByUser));
        tv_correct.setText(String.valueOf(correctByUser));
        tv_incorrect.setText(String.valueOf(incorrectByUser));



      /*  boolean_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("chk", "id" + checkedId);
                if(checkedId == R.id.trueRadio){
                    if(true_radio.isChecked()){
                        if(correctAns.equals("True")){
                            Toast toast = Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_LONG);
                            toast.show();
                            correctByUser++;
                            tv_correct.setText(String.valueOf(correctByUser));

                        } else {
                            incorrectByUser++;
                            Toast toast = Toast.makeText(getApplicationContext(),"Sorry that one is wrong!",Toast.LENGTH_LONG);
                            toast.show();
                            tv_incorrect.setText(String.valueOf(incorrectByUser));
                        }
                        unansweredByUser--;
                        tv_unanswered.setText(String.valueOf(unansweredByUser));
                        Log.d("TRUE_RADIO", true_radio.getText().toString());
                    }
                } else if(checkedId == R.id.falseRadio) {
                    if(correctAns.equals("False")){
                        Toast toast = Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_LONG);
                        toast.show();
                        correctByUser++;
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),"Sorry that one is wrong!",Toast.LENGTH_LONG);
                        toast.show();
                        incorrectByUser++;
                    }
                    Log.d("FALSE_RADIO", false_radio.getText().toString());
                    unansweredByUser--;
                    tv_unanswered.setText(String.valueOf(unansweredByUser));
                }

            }
        });
        */
        // TODO : ************************************************************** THE NEXT 3 ARE ALL TRYING TO COUNT THE CORRECT ANSWERS ******************************************
  /*      int radioButtonID = boolean_group.getCheckedRadioButtonId();
        View true_radio = boolean_group.findViewById(R.id.trueRadio);
        View false_radio = boolean_group.findViewById(R.id.falseRadio);
        int findex = boolean_group.indexOfChild(true_radio);
        int tindex = boolean_group.indexOfChild(false_radio);
        RadioButton r = (RadioButton) boolean_group.getChildAt(findex);
        String selectedText = r.getText().toString();*/

        /*lv_trivia.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final int child= lv_trivia.getChildCount();
                for(int i=0;i<child;i++) {
                    View rgg = lv_trivia.getChildAt(i);

                    RadioGroup radioGroup = (RadioGroup) rgg.findViewById(R.id.boolean_group);

                    int selectedId=radioGroup.getCheckedRadioButtonId();

                    RadioButton radioButton = (RadioButton) rgg.findViewById(selectedId);
                    Snackbar snackbar = Snackbar.make(v, "Correct!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });
*/

            /*boolean_group.clearCheck();
            boolean_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override

                public void onCheckedChanged(RadioGroup group , int checkedId){
                    if(checkedId == R.id.trueRadio){
                        Toast toast = Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_LONG);
                        toast.show();
                        correctByUser++;
                        unansweredByUser--;
                        tv_unanswered.setText(String.valueOf(unansweredByUser));
                        tv_correct.setText(String.valueOf(correctByUser));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),"WRONG!",Toast.LENGTH_LONG);
                        toast.show();
                        correctByUser++;
                        unansweredByUser--;
                        tv_unanswered.setText(String.valueOf(unansweredByUser));
                        tv_correct.setText(String.valueOf(correctByUser));
                    }
                    if(checkedId == R.id.falseRadio) { //&& correctAns.equals("False")){
                        Toast toast = Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_LONG);
                        toast.show();
                        correctByUser++;
                        unansweredByUser--;
                        tv_unanswered.setText(String.valueOf(unansweredByUser));
                        tv_correct.setText(String.valueOf(correctByUser));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),"WRONG!",Toast.LENGTH_LONG);
                        toast.show();
                        correctByUser++;
                        unansweredByUser--;
                        tv_unanswered.setText(String.valueOf(unansweredByUser));
                        tv_correct.setText(String.valueOf(correctByUser));
                    }
                }
            });*/

        answer1 = multiple_View.findViewById(R.id.Answer1);
        answer2 = multiple_View.findViewById(R.id.Answer2);
        answer3 = multiple_View.findViewById(R.id.Answer3);
        answer4 = multiple_View.findViewById(R.id.Answer4);
        true_radio = boolean_view.findViewById(R.id.trueRadio);

        lv_trivia.setAdapter(listAdapter);
        TriviaResults query = new TriviaResults();

        // jumps to doInbackgroudn()
        query.execute(trivia_url);
        //lv_counter.setAdapter(listAdapter);
        Button submitB = findViewById(R.id.submit_button);
        // delete during clean up
    /*    trueB = findViewById(R.id.trueRadio);
        falseB = findViewById(R.id.falseRadio);*/
        Intent go_to_restults = new Intent(this, ResultsActivity.class);
        submitB.setOnClickListener(v -> {
            //Intent go_to_restults = new Intent(this, ResultsActivity.class);
            //go_to_restults.putExtra("correct" , correct_answer_data)
            startActivity(go_to_restults);
        });



    }


    public class TriviaResults extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // this has to be done in ui thread
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

            if(!is_multiple){
                boolean_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(correct_answer_data.equals("True")){

                        }

                    }
                });
            } else{
                multiple_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                    }
                });
            }

           if (is_multiple) {
                    View multiple = inflater.inflate(R.layout.multiple_questions, parent, false);
                    TextView multiple_tv = multiple.findViewById(R.id.question_multiple);

                    RadioButton answer1_tv = multiple.findViewById(R.id.Answer1);
                    RadioButton answer2_tv = multiple.findViewById(R.id.Answer2);
                    RadioButton answer3_tv = multiple.findViewById(R.id.Answer3);
                    RadioButton answer4_tv = multiple.findViewById(R.id.Answer4);
                    multiple_tv.setText(thisRow.getQuestion());

                    String[] incorrect_answer_list = thisRow.getIncorrect_answers().split(",");


                    if (random == 0) {
                        answer4_tv.setText(thisRow.getCorrect_answer());
                        answer3_tv.setText(incorrect_answer_list[0].trim());
                        answer2_tv.setText(incorrect_answer_list[1].trim());
                        answer1_tv.setText(incorrect_answer_list[2].trim());
                    } else if (random == 1) {
                        answer1_tv.setText(thisRow.getCorrect_answer());
                        answer3_tv.setText(incorrect_answer_list[0].trim());
                        answer2_tv.setText(incorrect_answer_list[1].trim());
                        answer4_tv.setText(incorrect_answer_list[2].trim());
                    } else if (random == 2) {
                        answer2_tv.setText(thisRow.getCorrect_answer());
                        answer3_tv.setText(incorrect_answer_list[0].trim());
                        answer4_tv.setText(incorrect_answer_list[1].trim());
                        answer1_tv.setText(incorrect_answer_list[2].trim());
                    } else {
                        answer4_tv.setText(thisRow.getCorrect_answer());
                        answer3_tv.setText(incorrect_answer_list[0].trim());
                        answer2_tv.setText(incorrect_answer_list[1].trim());
                        answer1_tv.setText(incorrect_answer_list[2].trim());
                    }
                    // saves the correctAnswer to correctAns
               correctAns = thisRow.getCorrect_answer();
                    return multiple;
                } else
                    trueOrFalse = inflater.inflate(R.layout.boolean_questions, parent, false);
                TextView boolean_tv = trueOrFalse.findViewById(R.id.question_boolean);

                correctAns = thisRow.getCorrect_answer();
                boolean_tv.setText(thisRow.getQuestion());
                return trueOrFalse;

        }

    }
   /*  * Counter List ADAPTER : shows how to view the question in the list adapter
     * */
    class CounterListAdapter extends BaseAdapter {
        int selectedPosition = 0;

        // size of the linked list
        @Override
        public int getCount() {
            return question_list.size();
        }

        // return the question list postiion
        @Override
        public Question getItem(int position) {
            return counter_list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return question_list.get(position).getId();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View counter = inflater.inflate(R.layout.activity_counter, parent, false);

            TextView unanswered_count = counter.findViewById(R.id.unanswered);
            unanswered_count.setText(String.valueOf(unansweredByUser));

            TextView correct = counter.findViewById(R.id.correct_answer);
            correct.setText(correctByUser);

            TextView incorrect = counter.findViewById(R.id.incorrect_answer);
            incorrect.setText(incorrectByUser);
            listAdapter.notifyDataSetChanged();
            return counter;


        }
    }
    // TODO NEW CODE


}