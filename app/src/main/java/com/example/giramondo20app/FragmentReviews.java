package com.example.giramondo20app;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.giramondo20app.Model.AccommodationModel;
import com.example.giramondo20app.Model.ReviewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentReviews extends Fragment {

AccommodationModel accommodation;
RecyclerView reviewRv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        accommodation = (AccommodationModel) bundle.getSerializable("selectedAccommodation");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        reviewRv = view.findViewById(R.id.reviewList);

        AsyncReviews task = new AsyncReviews(this);
        task.execute(accommodation.getName());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStored;
                SharedPreferences pref = getActivity().getSharedPreferences("loginData",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                emailStored = pref.getString("email","");
                if(emailStored.isEmpty()) {
                    Toast.makeText(getContext(),"Effettua prima il login.",Toast.LENGTH_LONG).show();
                }else if(accommodation != null) {
                    FragmentTransaction fr = getParentFragment().getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, new FragmentReviewForm(accommodation),"frag_rev_form");
                    fr.addToBackStack(null);
                    fr.commit();
                }
            }
        });
    }

    public void onTaskCompleted(List<ReviewModel> results){
        AdapterReviews adapterReviews = new AdapterReviews(getContext(), results);
        reviewRv.setAdapter(adapterReviews);
        reviewRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
