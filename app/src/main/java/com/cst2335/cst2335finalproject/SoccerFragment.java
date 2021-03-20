package com.cst2335.cst2335finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoccerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoccerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Bundle dataFromChat;
    private long id;
    private String soccer_title, soccer_url, soccer_description, soccer_date;
    private AppCompatActivity prevActivity;

    public SoccerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoccerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SoccerFragment newInstance(String param1, String param2) {
        SoccerFragment fragment = new SoccerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataFromChat = getArguments();
        id = dataFromChat.getLong("Id");
        soccer_title = dataFromChat.getString("Title");
        soccer_url = dataFromChat.getString("Link");
        soccer_description = dataFromChat.getString("Description");
        soccer_date = dataFromChat.getString("Date");

        View view = inflater.inflate(R.layout.fragment_soccer, container, false);
        TextView idText = (TextView) view.findViewById(R.id.soccer_id);
        TextView titleText = (TextView) view.findViewById(R.id.soccer_title);
        TextView urlText = (TextView) view.findViewById(R.id.soccer_link);
        TextView descriptionText = (TextView) view.findViewById(R.id.soccer_description);
        TextView dateText = (TextView) view.findViewById(R.id.soccer_date);

        Button soccer_saveButton = (Button) view.findViewById(R.id.soccer_saveButton);

        idText.setText("ID= "+id);
        titleText.setText(soccer_title);
        urlText.setText(soccer_url);
        descriptionText.setText(soccer_description);
        dateText.setText(soccer_date);

        soccer_saveButton.setOnClickListener(c->{

        });
        return view;
    }
}