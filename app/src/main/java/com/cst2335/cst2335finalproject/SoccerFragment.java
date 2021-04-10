package com.cst2335.cst2335finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst2335.cst2335finalproject.soccer.Article;
import com.cst2335.cst2335finalproject.soccer.BitmapUtility;
import com.cst2335.cst2335finalproject.soccer.SoccerDetailActivity;
import com.cst2335.cst2335finalproject.soccer.SoccerFavorites;
import com.google.android.material.snackbar.Snackbar;

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
    private Article tArticle;
    //private Button soccer_saveButton, soccer_urlButton;


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
    /**
     *onCreateView is a method to create a new view.
     * @param inflater is an inflater.
     * @param container is a view group.
     * @param savedInstanceState is a bundle to pass data.
     * This method is used for placing data of an article object to proper components.
     * @return view.
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataFromChat = getArguments();
        tArticle = (Article) dataFromChat.getSerializable("Article");

        View view = inflater.inflate(R.layout.fragment_soccer, container, false);
        TextView idText = (TextView) view.findViewById(R.id.soccer_id);
        TextView titleText = (TextView) view.findViewById(R.id.soccer_title);

        TextView descriptionText = (TextView) view.findViewById(R.id.soccer_description);
        TextView dateText = (TextView) view.findViewById(R.id.soccer_date);
        ImageView soccer_thumbnailImage = (ImageView) view.findViewById(R.id.soccer_fragment_headlineImage);

        idText.setText("Article NO. "+tArticle.getId());
        titleText.setText(tArticle.getTitle());

        descriptionText.setText(tArticle.getDescription());
        dateText.setText(tArticle.getPubDate());
        soccer_thumbnailImage.setImageBitmap(BitmapUtility.getBitmapImage(tArticle.getThumbnailUrl()));

        return view;
    }

}