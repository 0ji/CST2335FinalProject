package com.cst2335.cst2335finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class handles all the code in car_db_main.xml.
 */
public class CarDBActivity extends AppCompatActivity {

    String[] carBrands = {"Honda", "Toyota", "Tesla", "Ford"};
    private ArrayList<CarItem> carsList = new ArrayList<>();
    private CarListAdapter carsAdapter = new CarListAdapter();
    SQLiteDatabase db;

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



        loadCarsFromDatabase();

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

        // set onClickListeners for buttons
        ProgressBar searchProgBar = findViewById(R.id.progressBar);

        // search Button
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener( e -> {
            String searchText = searchView.getText().toString();

            // toast to show something
            Toast.makeText(getApplicationContext(), "You searched `" + searchText + "`",
                    Toast.LENGTH_SHORT).show();
            searchProgBar.setProgress(100);
        });

        // database view button
        Button btnDB = findViewById(R.id.btnDatabase);
        // TODO: change functionality to view database fragment
        btnDB.setOnClickListener( e -> {

        });

        // help button
        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(e -> {
            helpAlert.show();
        });

        // car list view
        carListView.setOnItemClickListener( (parent, view, pos, id) -> {
            AlertDialog.Builder carDialogBuilder = new AlertDialog.Builder(this);
            CarItem selectedItem = carsList.get(pos);

            carDialogBuilder.setTitle("Car details")

                .setMessage("The selected row is " + pos)

                .setPositiveButton("Add", (click, arg) -> {
                   // add item to database
                    Snackbar.make(findViewById(R.id.constraintView), "Added to database.",
                            Snackbar.LENGTH_LONG)
                            .setAction("Undo", e2 -> {
                                // TODO: remove car from database
                            })
                            .show();
                })

                .setNegativeButton("Close", (click, arg) -> {})

                .create().show();
        });
    }

    /**
     * Function to load cars from existing database, if any.
     */
    private void loadCarsFromDatabase() {
        // TODO: implement this!! make load from sharedprefs database?

        for (int i = 0; i < 15; i++) {
        carsList.add(new CarItem(1, 1, "make_test", 1 + " :model_test"));
        }
        carsAdapter.notifyDataSetChanged();
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
            inflaterMakeText.setText(newCarItem.getMake());
            inflaterMakeText.setText(newCarItem.getModel());

            //return it to be put in the table
            return newView;
        }
    }

    private class CarQuery extends AsyncTask<String, Integer, String> {
        String carModel, carMake, carMakeID = null;

        @Override
        protected String doInBackground(String... args) {
            try {
                //TODO: make url based off user brand search
                // create url
                URL url = new URL("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/HONDA?format=XML");

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                // create xml pull parser factory
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                //parse through xml

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        // pointing to start tag
                        if (xpp.getName().equals("Make_ID")) {

                        }
                    }
                }
            } catch (Exception e) {
                //TODO: error handling if url not found
            }
            return "Finished car Asynctask.";
        }

        public void onProgressUpdate(Integer ... args) {

        }

        public void onPostExecute(String fromDoInBackground) {

        }

    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}