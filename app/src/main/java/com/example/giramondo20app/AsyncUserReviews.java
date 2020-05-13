package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.MySQLReviewDAO;
import com.example.giramondo20app.Controller.DAO.ReviewDAO;
import com.example.giramondo20app.Model.ReviewModel;

import java.util.List;

public class AsyncUserReviews extends AsyncTask<String,Void, List<ReviewModel>> {

    String userEmail;
    OnTaskCompletedUserReviews listener;

    AsyncUserReviews(OnTaskCompletedUserReviews listener){
        this.listener = listener;
    }

    @Override
    protected List<ReviewModel> doInBackground(String... strings) {
        userEmail = strings[0];
        ReviewDAO revSQL = new MySQLReviewDAO();
        return revSQL.getReviewsByUserEmail(userEmail);
    }

    @Override
    protected void onPostExecute(List<ReviewModel> results) {
        if(listener != null)
            listener.onTaskCompletedGetOwnReviewList(results);
    }
}
