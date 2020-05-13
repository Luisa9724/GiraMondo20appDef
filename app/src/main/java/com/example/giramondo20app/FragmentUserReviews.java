package com.example.giramondo20app;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.giramondo20app.Model.ReviewModel;

import java.util.List;


public class FragmentUserReviews extends Fragment implements  OnTaskCompletedUserReviews {

    RecyclerView userReviews;
    Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userReviews = view.findViewById(R.id.user_reviewList);
    }

    @Override
    public void onTaskCompletedGetOwnReviewList(List<ReviewModel> results) {
        if(results != null && !results.isEmpty()) {
            AdapterUserReviews adapter = new AdapterUserReviews(mContext, results, getFragmentManager());
            if (userReviews != null) {
                userReviews.setAdapter(adapter);
                userReviews.setLayoutManager(new LinearLayoutManager(getContext()));
            }
      }else{
            new AlertDialog.Builder(getContext())
                    .setTitle("Avviso")
                    .setMessage("Nessun risultato." +
                            "Comincia a scrivere recensioni " +
                            "e aiuterai altri utenti nelle scelte " +
                            "del loro prossimo viaggio!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace(R.id.fragment_container, getFragmentManager().findFragmentByTag("frag_profile"));
                            fr.commit();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
