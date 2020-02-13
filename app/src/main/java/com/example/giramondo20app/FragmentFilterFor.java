package com.example.giramondo20app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class FragmentFilterFor extends Fragment {

    FragmentFilteredResurchResults fragRes= new FragmentFilteredResurchResults();

    public FragmentFilterFor() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final TextView textView1;
        final SeekBar seekBar;
        final Button myButton;

        View rootView = inflater.inflate(R.layout.fragment_filter_for, container, false);

        seekBar = rootView.findViewById(R.id.seekBar);
        textView1 = rootView.findViewById(R.id.textView1);
        myButton = rootView.findViewById(R.id.button);
        seekBar.setMax(500);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int myProgress = progress;
                textView1.setText("" + myProgress);


            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer priceRange = seekBar.getProgress();

                AsyncAccommodations(priceRange);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.container,fragRes);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });
        return rootView;

    }

    public void AsyncAccommodations(Integer price) {
        AsyncAccommodations task = new AsyncAccommodations(getActivity(),fragRes);
        task.execute(price);
    }
}

