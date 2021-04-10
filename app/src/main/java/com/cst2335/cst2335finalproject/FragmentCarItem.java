package com.cst2335.cst2335finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class FragmentCarItem extends Fragment {
    private Bundle dataFromActivity;
    private int model_id, make_id, _id;
    private String model, make;
    private AppCompatActivity parentActivity;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate fragment with view
        dataFromActivity = getArguments();

        View result = inflater.inflate(R.layout.fragment_car_item_view, container, false);

        _id = dataFromActivity.getInt(CarDBActivity.CAR_ID);
        model_id = dataFromActivity.getInt(CarDBActivity.CAR_MODEL_ID);
        make_id = dataFromActivity.getInt(CarDBActivity.CAR_MAKE_ID);
        model = dataFromActivity.getString(CarDBActivity.CAR_MODEL);
        make = dataFromActivity.getString(CarDBActivity.CAR_MAKE);

        // set model and make/brand
        TextView makeView = result.findViewById(R.id.make);
        TextView modelView = result.findViewById(R.id.model);
        makeView.setText(make);
        modelView.setText(model);

        // set info details
        TextView infoView = result.findViewById(R.id.info);
        infoView.setText("Model ID: " + model_id + "\nMake ID: " + make_id);

        // set onclick listeners for buttons
        Button shopBtn = result.findViewById(R.id.btnShop);
        Button detailBtn = result.findViewById(R.id.btnDetail);
        Button databaseBtn = result.findViewById(R.id.btnDB);
        String dbOption = dataFromActivity.getString("DB");
        if (dbOption.contains("add")) {
            databaseBtn.setText("Add to database");
        } else if (dbOption.contains("remove")) {
            databaseBtn.setText("Remove from database");
        }

        databaseBtn.setOnClickListener( e -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(CarOpener.COL_ID, _id);
            newRowValues.put(CarOpener.COL_MAKE_ID, make_id);
            newRowValues.put(CarOpener.COL_MODEL_ID, model_id);
            newRowValues.put(CarOpener.COL_MAKE, make);
            newRowValues.put(CarOpener.COL_MODEL, model);

            databaseBtnOption(dbOption, newRowValues);
        });

        detailBtn.setOnClickListener( e -> openWebPage("http://www.google.com/search?q=" + make + "+" + model));
        shopBtn.setOnClickListener( e -> openWebPage("https://www.autotrader.ca/cars/?mdl=" + model + "&make=" + make + "&loc=K2G1V8"));

        // return view
        return result;
    }

    public void databaseBtnOption(String option, ContentValues newRowValues) {
        // TODO: add to database function
        CarOpener dbOpener = new CarOpener(parentActivity);
        db = dbOpener.getWritableDatabase();

        if (option.contains("add")) {
            AlertDialog.Builder carDialogBuilder = new AlertDialog.Builder(parentActivity);

            carDialogBuilder.setTitle("Add to database?")

                .setPositiveButton("Yes", (click, arg) -> {
                    db.insert(CarOpener.TABLE_NAME, null, newRowValues);

                    Snackbar.make(parentActivity.findViewById(R.id.fragmentLayout), "Added to database.",
                            Snackbar.LENGTH_LONG)
                            .setAction("Undo", e2 -> {
                                // TODO: remove car from database
                                db.delete(CarOpener.TABLE_NAME, CarOpener.COL_ID + "= ?", new String[] {newRowValues.getAsString(CarOpener.COL_ID)});
                            })
                            .show();
                })

                .setNegativeButton("No", (click, arg) -> {})

                .create().show();
        } else if (option.contains("remove")) {
            AlertDialog.Builder carDialogBuilder = new AlertDialog.Builder(parentActivity);

            carDialogBuilder.setTitle("Remove from database?")

                    .setPositiveButton("Yes", (click, arg) -> {
                        // TODO: remove item from database
                        db.delete(CarOpener.TABLE_NAME, CarOpener.COL_ID + "= ?", new String[] {newRowValues.getAsString(CarOpener.COL_ID)});

                        Snackbar.make(parentActivity.findViewById(R.id.fragmentLayout), "Removed from database.",
                                Snackbar.LENGTH_LONG)
                                .setAction("Undo", e2 -> {
                                    // TODO: add car to database
                                    db.insert(CarOpener.TABLE_NAME, null, newRowValues);
                                })
                                .show();
                    })

                    .setNegativeButton("No", (click, arg) -> {})

                    .create().show();
        }
    }

    protected void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(parentActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}

