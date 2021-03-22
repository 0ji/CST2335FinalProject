package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

public class GameActivity extends TriviaActivity {
    String a;
    String d;
    String t;
    MyListAdapter listAdapter = new MyListAdapter();
    private ArrayList<Question> question_list =  new ArrayList<>();
    Question new_question;
    boolean is_multiple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SharedPreferences sharedPreferences = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
        a = sharedPreferences.getString("amount", "");
        d = sharedPreferences.getString("difficulty", "");
        t = sharedPreferences.getString("type", "");

        String trivia_url = "https://opentdb.com/api.php?amount="+a+"&difficulty="+d+"&type="+t;
        TriviaResults query = new TriviaResults();
        // jumps to doInbackgroudn()
        query.execute(trivia_url);

        // assign screen view
        ListView lv = findViewById(R.id.triviaListView);
        lv.setAdapter(listAdapter);


    }

    public class TriviaResults extends AsyncTask<String, Integer, String> {

        String question_data;
        String category_data;
        String difficulty_data;
        String correct_answer_data;
        String incorrect_answers_data;
        String type_data;

        @Override
        protected String doInBackground(String... args) {

           /* SharedPreferences sharedPreferences = getSharedPreferences("TriviaDetails", Context.MODE_PRIVATE);
            a = sharedPreferences.getString("amount", "");
            d = sharedPreferences.getString("difficulty", "");
            t = sharedPreferences.getString("type", "");
*/
            {
                try {
                    // url objects for which server to connect to once users have entere the trivia they want
                    // TODO: need to put this in if statements to change url based on number of feilds user has entered, will not work if amount is not explicit : use snackbar to notify
                    String triviaUrl = "https://opentdb.com/api.php?amount="+a+"&difficulty="+d+"&type="+t; // this works becasue it is in the right order
                    URL url = new URL(args[0]);

                    //https://opentdb.com/api.php?amount=10&difficulty=medium&type=multiple ONLY URL THAT IS WORKING
                    //open the connection for trivia api
                    HttpURLConnection trivia_connection = (HttpURLConnection) url.openConnection();
                    //trivia_url_connection.connect();

                    //wait for data
                    // BUILDING A JSON POST REQUEST
                  /*  trivia_url_connection.setRequestMethod("POST");
                    // breaks here unknownHostException

                    trivia_url_connection.setRequestProperty("Content-Type", "application/json; utf-8");
                    trivia_url_connection.setRequestProperty("Accept", "application/json");
                    trivia_url_connection.setDoOutput(true);*/
                    InputStream trivia_response = trivia_connection.getInputStream();


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

                    // CURRENT - just finished this one
                    // now i need to get the listAdapter working // use Question class to hold questions
                    for(int i = 0; i < j_array.length(); i++){
                        JSONObject json_data = j_array.getJSONObject(i);
                        String category_data = json_data.getString("category");
                        String type_data = json_data.getString("type");
                        String difficulty_data = json_data.getString("difficulty");
                        String question_data = json_data.getString("question");
                        String correct_answer_data = json_data.getString("correct_answer");
                        String incorrect_answers_data = json_data.getString("incorrect_answers");
                        // create new question in ArrayList
                        question_list.add(new Question(question_data, category_data, difficulty_data, correct_answer_data, incorrect_answers_data , type_data, i));
                        while(!question_list.isEmpty()){
                            if(type_data == "multiple"){

                            }
                        }
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

    class MyListAdapter extends BaseAdapter {

        // size of the linked list
        @Override
        public int getCount() {
            return question_list.size();
        }
        // return the question list postiion
        @Override
        public Object getItem(int position) {
            return question_list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            inflater.inflate(R.layout.activity_game, parent, false);
            return null;
        }
    }
}