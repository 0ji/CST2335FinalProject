package com.cst2335.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class handles all the code in car_db_main.xml.
 */
public class CarDBActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CAR_MAKE = "MAKE";
    public static final String CAR_MODEL = "MODEL";
    public static final String CAR_MAKE_ID = "MAKE_ID";
    public static final String CAR_MODEL_ID = "MODEL_ID";
    public static final String CAR_ID = "ID";

    String[] carBrands = {"Honda", "Toyota", "Tesla", "Ford"};
    private ArrayList<CarItem> carsList = new ArrayList<>();
    private CarListAdapter carsAdapter = new CarListAdapter();
    SharedPreferences prefs = null;

    /**
     * Create function for when the instance is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_db_main);

        ListView carListView = findViewById(R.id.listViewCars);
        carListView.setAdapter(carsAdapter);

        // create toolbar
        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("Car app");

        //create navdrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        // load any old query preferences, if any
        prefs = getSharedPreferences("CarViewPrefs", Context.MODE_PRIVATE);
        String savedSearch = prefs.getString("Search", "");
        EditText searchField = findViewById(R.id.carSearchView);
        searchField.setText(savedSearch);

        // build the helpAlertBuilder
        // TODO: change alert to be translatable
        AlertDialog.Builder helpAlertBuilder = new AlertDialog.Builder(this);
        helpAlertBuilder.setTitle(R.string.help_title)
        .setMessage(R.string.help_details)
        .setPositiveButton(R.string.help_positive, (click, arg) -> {});

        // create the helpAlert alert dialog
        AlertDialog helpAlert = helpAlertBuilder.create();
        helpAlert.show(); // show the dialog

        // create ArrayAdapter with list of car names, suggests car names when typing
        // taken from https://stackoverflow.com/a/20989919
        ArrayAdapter<String> carBrandAdapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, carBrands);
        AutoCompleteTextView searchView = findViewById(R.id.carSearchView);
        searchView.setThreshold(1);
        searchView.setAdapter(carBrandAdapter);
        searchView.setTextColor(Color.GRAY);

        searchView.setOnClickListener(e -> {
            searchView.showDropDown();
        });

        // set onClickListeners for buttons
        ProgressBar searchProgBar = findViewById(R.id.progressBar);

        // search Button
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener( e -> {
            CarQuery query = new CarQuery();
            String searchText = searchView.getText().toString();
            // toast to show something
            saveSharedPrefs(searchText);
            Toast.makeText(getApplicationContext(), "You searched `" + searchText + "`",
                    Toast.LENGTH_SHORT).show();
            query.execute("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/" + searchText + "?format=JSON");
            carsAdapter.notifyDataSetChanged();
            searchProgBar.setProgress(100);
        });

        // database view button
        Button btnDB = findViewById(R.id.btnDatabase);
        btnDB.setOnClickListener( e -> {
            // TODO: pass something if needed?
            Bundle dataToPass = new Bundle();
            dataToPass.putString("DB", "dbview");
            // go to database view intent
            Intent nextActivity = new Intent(CarDBActivity.this, CarEmptyFragmentView.class);
            nextActivity.putExtras(dataToPass);
            startActivity(nextActivity);
        });

        // help button
        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(e -> {
            helpAlert.show();
        });

        // car list view
        carListView.setOnItemClickListener( (parent, view, pos, id) -> {
            CarItem selectedItem = carsList.get(pos);
            Bundle dataToPass = new Bundle();
            dataToPass.putInt(CAR_ID, selectedItem.get_id());
            dataToPass.putInt(CAR_MAKE_ID, selectedItem.getMakeID());
            dataToPass.putInt(CAR_MODEL_ID, selectedItem.getModelID());
            dataToPass.putString(CAR_MAKE, selectedItem.getMake());
            dataToPass.putString(CAR_MODEL, selectedItem.getModel());
            dataToPass.putString("DB", "add");

            Intent nextActivity = new Intent(CarDBActivity.this, CarEmptyFragmentView.class);
            nextActivity.putExtras(dataToPass);
            startActivity(nextActivity);
        });
    }

    /** Saves and loads most recent search query string.
     *
     * @param searchText the searched text
     */
    private void saveSharedPrefs(String searchText) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Search", searchText);
        editor.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String message = null;
        Intent nextActivity = null;
        switch(item.getItemId()) {
            case R.id.itemCar:
                message = "Launching car app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
            case R.id.itemSoccer:
                message = "Launching soccer app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
            case R.id.itemSong:
                message = "Launching song app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
            case R.id.itemTrivia:
                message = "Launching trivia app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(nextActivity);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String message = null;
        Intent nextActivity = null;
        switch(item.getItemId()) {
            case R.id.itemCar:
                message = "Launching car app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
            case R.id.itemSoccer:
                message = "Launching soccer app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
            case R.id.itemSong:
                message = "Launching song app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
            case R.id.itemTrivia:
                message = "Launching trivia app";
                nextActivity = new Intent(this, CarDBActivity.class);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(nextActivity);
        return true;
    }

    /**
     * CarsListAdapter is made to show a list of cars, for search.
     */
    private class CarListAdapter extends BaseAdapter {
        /**
         * @return the size of the cars database (arrayList).
         */
        public int getCount() { return carsList.size();}

        /**
         * @return the carItem is returned at the specific position.
         */
        public Object getItem(int position) { return carsList.get(position);
        }

        /**
         * @param position the position of the item selected in the list.
         * @return position in type long.
         */
        public long getItemId(int position) { return (long) position; }

        /**
         * creates the view for a specific item.
         * @param position
         * @param old
         * @param parent
         * @return
         */
        public View getView(int position, View old, ViewGroup parent)
        {
            View newView = old;
            // load layout inflater
            LayoutInflater inflater = getLayoutInflater();
            // gets message from array
            CarItem newCarItem = (CarItem) getItem(position);

            //make a new row view:

            //set what the text should be for this row:
            newView = inflater.inflate(R.layout.car_db_item, parent, false);
            TextView inflaterMakeText = newView.findViewById(R.id.carMake);
            TextView inflaterModelText = newView.findViewById(R.id.carModel);
//            TextView inflaterModelIDText = newView.findViewById(R.id.carModelID);
//            TextView inflaterMakeIDText = newView.findViewById(R.id.carMakeID);
//            TextView inflaterCarItemIDText = newView.findViewById(R.id.carItemID);
            inflaterMakeText.setText(newCarItem.getMake());
            inflaterModelText.setText(newCarItem.getModel());
//            inflaterModelIDText.setText(Integer.toString(newCarItem.getModelID()));
//            inflaterMakeIDText.setText(Integer.toString(newCarItem.getMakeID()));
//            inflaterCarItemIDText.setText(Integer.toString(newCarItem.get_id()));

            //return it to be put in the table
            return newView;
        }
    }

    /**
     * Asynctask for the car list. Loads from online json database.
     */
    private class CarQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                carsList.clear();
                // create url
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                // create JSON reader

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                String result = sb.toString();

                // converts string to JSON
                JSONObject carResult = new JSONObject(result);

                //create array from object
                JSONArray carArray = carResult.getJSONArray("Results");

                for (int i = 0; i < carArray.length(); i++) { // iterate through car array
                    JSONObject carObject = carArray.getJSONObject(i);
                    int makeID = carObject.getInt("Make_ID");
                    String make = carObject.getString("Make_Name");
                    int modelID = carObject.getInt("Model_ID");
                    String model = carObject.getString("Model_Name");

                    // create new car in carsList
                    carsList.add(new CarItem(i, makeID, modelID, make, model));
                }
            } catch (Exception e) {
                //TODO: error handling if url not found
                e.printStackTrace();
            }
            Log.i("MainActivity", "Completed Asynctask");
            return "Finished car Asynctask.";
        }

        public void onProgressUpdate(Integer ... args) {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setProgress(args[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String fromDoInBackground) {
            carsAdapter.notifyDataSetChanged();
        }

    }
}