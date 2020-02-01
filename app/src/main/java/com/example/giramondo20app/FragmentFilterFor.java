package com.example.giramondo20app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class FragmentFilterFor extends Fragment {



    public FragmentFilterFor() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final TextView textView;
        SeekBar seekBar;
        View rootView= inflater.inflate(R.layout.fragment_filter_for, container, false);
         seekBar = rootView.findViewById(R.id.seekBar);
            textView= rootView.findViewById(R.id.textView);
            seekBar.setMax(500);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    int myProgress= progress;
                    textView.setText("progress value"+ myProgress + "+" + seekBar.getMax());

                }
            });

        return  rootView ;

    }



}





