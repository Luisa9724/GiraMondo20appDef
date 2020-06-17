package com.example.giramondo20app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class FragmentFilters extends Fragment {
    private TextView mytextView;
    private SeekBar myseekBar;
    private RatingBar myRatingBar;
    private Spinner myspinner;
    private RadioGroup radioGroup;
    private String clickedItem = "";
    private Integer priceRange = 0;
    private Float myRating = 0F;
    private String travelType = "";
    private int myProgress;
    private int buttonPressed;
    private String cityName;

    private Animation animRotate;
    Bundle bundle;

    private FragmentFilteredResearchResults fragRes= new FragmentFilteredResearchResults();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        bundle = getArguments();
        if(bundle != null) {
            cityName = bundle.getString("cityName", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        myseekBar = view.findViewById(R.id.seekBar);
        mytextView = view.findViewById(R.id.textView1);
        myRatingBar = view.findViewById(R.id.ratingBar2);
        final Button myButton = view.findViewById(R.id.buttonFind);

        myspinner = view.findViewById(R.id.spinner);
        radioGroup = view.findViewById(R.id.radioGroup);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressViewOffset(false,10,100);

        animRotate = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);

        myseekBar.setMax(500);
        setSpinner(buttonPressed);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                myRatingBar.setRating(0F);
                radioGroup.clearCheck();
                myseekBar.setProgress(0);
                myspinner.setSelection(0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        myseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                priceRange = myseekBar.getProgress();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                myProgress = progress;
                mytextView.setText("" + myProgress);
            }
        });

        myRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myRating = myRatingBar.getRating();
            }
        });

        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    clickedItem = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioButton();

                AsyncAccommodations(priceRange, myRating, travelType, clickedItem, cityName);
                resetValues();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction;
                if (fragmentManager != null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragRes);
                    fragmentTransaction.commit();
                }

            }
        });
    }

    private void AsyncAccommodations(Integer price,Float rating, String travelType, String subCategory,String city) {
        if(buttonPressed==R.id.buttonHotel) {
            if(city != null) {
                AsyncHotels task = new AsyncHotels(fragRes, price, rating, travelType, subCategory,city);
                task.execute();
            }
        }else if(buttonPressed==R.id.buttonRestaurant){
            if(city != null) {
                AsyncRestaurants task = new AsyncRestaurants(fragRes, price, rating, travelType, subCategory, city);
                task.execute();
            }
        }else if(buttonPressed==R.id.buttonAttraction){
            if(city != null) {
                AsyncAttractions task = new AsyncAttractions(fragRes, price, rating, travelType, subCategory, city);
                task.execute();
            }
        }
    }

    private void resetValues(){
        priceRange = 0;
        myRating = 0F;
        clickedItem = "";
    }

    public void setButton(int button){
        buttonPressed = button;
    }

    private void setSpinner(int viewId){
        myspinner.setSelection(0);
        switch (viewId){
            case R.id.buttonHotel : {
                String[] arraySpinner = new String[] {
                        "Seleziona sotto-categoria","Hotel", "Ostelli", "B&B", "Appartamenti"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                myspinner.setAdapter(adapter);
                break;
            }
            case R.id.buttonRestaurant : {
                String[] arraySpinner = new String[]{
                        "Seleziona sotto-categoria","Ristoranti", "Bar e pub", "Pasticcerie e gelaterie"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                myspinner.setAdapter(adapter);
                break;
            }
            case R.id.buttonAttraction : {
                String[] arraySpinner = new String[]{
                        "Seleziona sotto-categoria","Musei", "Shopping", "Stadio", "Parchi", "Teatri","Zoo e acquari","Parchi divertimento","Centri benessere"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                myspinner.setAdapter(adapter);
                break;

            }
        }

}

    private void setRadioButton(){
        travelType = "";
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if(selectedId != 0) {
            RadioButton radioButton = getView().findViewById(selectedId);
            if(radioButton !=null)
            travelType = radioButton.getText().toString();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu,menu);
        ImageView imgRefresh = new ImageView(getContext());
        imgRefresh.setPadding(0,0,15,0);
        if(imgRefresh != null){
            imgRefresh.setImageResource(R.drawable.ic_refresh_26dp);
        }
        menu.findItem(R.id.option_refresh).setActionView(imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animRotate);

                myRatingBar.setRating(0F);
                radioGroup.clearCheck();
                myseekBar.setProgress(0);
                myspinner.setSelection(0);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option_refresh){

            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    void onBackPressed(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fr = fragmentManager.beginTransaction();
        fr.replace(R.id.fragment_container,fragmentManager.findFragmentByTag("frag_home"));
        fr.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        bundle = getArguments();
        if (bundle != null) {
            cityName = bundle.getString("cityName", "");
        }
    }
}

