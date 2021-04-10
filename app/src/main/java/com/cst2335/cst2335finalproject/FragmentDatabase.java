package com.cst2335.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.cst2335.cst2335finalproject.CarDBActivity.CAR_ID;
import static com.cst2335.cst2335finalproject.CarDBActivity.CAR_MAKE;
import static com.cst2335.cst2335finalproject.CarDBActivity.CAR_MAKE_ID;
import static com.cst2335.cst2335finalproject.CarDBActivity.CAR_MODEL;
import static com.cst2335.cst2335finalproject.CarDBActivity.CAR_MODEL_ID;
import static com.cst2335.cst2335finalproject.CarOpener.TABLE_NAME;

/**
 * fragment that loads the database.
 */
public class FragmentDatabase extends Fragment {

    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    private ArrayList<CarItem> carsListDB = new ArrayList<>();
    private CarListAdapter carsAdapterDB;
    private SQLiteDatabase db;

    /**
     * creates view of the database.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate fragment with view
        View result = inflater.inflate(R.layout.fragment_db_view, container, false);
        ListView carListView = result.findViewById(R.id.dbListViewCars);
        //carsListDB.add(new CarItem(0, 0, 0, "test", "test"));


        carsAdapterDB = new CarListAdapter();
        carListView.setAdapter(carsAdapterDB);

        carListView.setOnItemClickListener( (parent, view, pos, id) -> {
            CarItem selectedItem = carsListDB.get(pos);
            dataFromActivity = getArguments();
            dataFromActivity.putInt(CAR_ID, selectedItem.get_id());
            dataFromActivity.putInt(CAR_MAKE_ID, selectedItem.getMakeID());
            dataFromActivity.putInt(CAR_MODEL_ID, selectedItem.getModelID());
            dataFromActivity.putString(CAR_MAKE, selectedItem.getMake());
            dataFromActivity.putString(CAR_MODEL, selectedItem.getModel());
            dataFromActivity.putString("DB", "remove");

            Intent nextActivity = new Intent(parentActivity, CarEmptyFragmentView.class);
            nextActivity.putExtras(dataFromActivity);
            startActivity(nextActivity);
        });
        // return view
        return result;
    }

    /**
     * Function to load cars from existing database, if any.
     */
    private void loadCarsFromDatabase() {
        CarOpener dbOpener = new CarOpener(parentActivity);
        db = dbOpener.getWritableDatabase();

        String [] columns = {CarOpener.COL_ID, CarOpener.COL_MAKE_ID, CarOpener.COL_MODEL_ID, CarOpener.COL_MAKE, CarOpener.COL_MODEL};

        Cursor results = db.query(false, TABLE_NAME, columns, null, null, null, null, null, null);

        int idColIndex = results.getColumnIndex(CarOpener.COL_ID);
        int makeIdColIndex = results.getColumnIndex(CarOpener.COL_MAKE_ID);
        int modelIdColIndex = results.getColumnIndex(CarOpener.COL_MODEL_ID);
        int makeColIndex = results.getColumnIndex(CarOpener.COL_MAKE);
        int modelColIndex = results.getColumnIndex(CarOpener.COL_MODEL);

        carsListDB.clear();
        while (results.moveToNext()) {
            int id = results.getInt(idColIndex);
            int makeId = results.getInt(makeIdColIndex);
            int modelId = results.getInt(modelIdColIndex);
            String make = results.getString(makeColIndex);
            String model = results.getString(modelColIndex);

            carsListDB.add(new CarItem(id, makeId, modelId, make, model));
        }
        carsAdapterDB.notifyDataSetChanged();

    }

    /**
     * whenever the fragment layout is loaded again, e.g. from deleting a car item in database view.
     */
    @Override
    public void onResume() {
        loadCarsFromDatabase();
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

    /**
     * CarsListAdapter is made to show a list of cars, for search.
     */
    private class CarListAdapter extends BaseAdapter {
        /**
         * @return the size of the cars database (arrayList).
         */
        public int getCount() { return carsListDB.size();}

        /**
         * @return the carItem is returned at the specific position.
         */
        public Object getItem(int position) { return carsListDB.get(position);
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

}