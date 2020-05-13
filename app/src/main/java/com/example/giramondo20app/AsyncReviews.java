package com.example.giramondo20app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.giramondo20app.Controller.DAO.MySQLReviewDAO;
import com.example.giramondo20app.Controller.DAO.ReviewDAO;
import com.example.giramondo20app.Model.ReviewModel;


import java.util.List;

public class AsyncReviews extends AsyncTask<String,Void, List<ReviewModel>> {

    FragmentReviews fragmentReviews;

    AsyncReviews(FragmentReviews fragmentReviews) {
        this.fragmentReviews = fragmentReviews;
    }


    @Override
    protected List<ReviewModel> doInBackground(String... strings) {
        List<ReviewModel> results;
        String acmName = strings[0];

        ReviewDAO revSQL = new MySQLReviewDAO();
        results = revSQL.getReviewsByAccommodation(acmName);
        return results;
    }

    @Override
    protected void onPostExecute(List<ReviewModel> results) {

        if (results != null && fragmentReviews != null) {
            fragmentReviews.onTaskCompleted(results);
        }

    }
}
