package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.MySQLReviewDAO;
import com.example.giramondo20app.Controller.DAO.ReviewDAO;

public class AsyncReviewForm extends AsyncTask<Void,Void,String> {

    String accommodationName,userEmail,comment,travelType;
    float quality,position,cleaning,service;
    Integer idReview;

    OnTaskCompletedReviewForm listener;

    AsyncReviewForm(OnTaskCompletedReviewForm listener,Integer idReview,String accommodationName,String userEmail,String comment,String travelType,float quality, float position, float cleaning, float service){
        this.idReview = idReview;
        this.accommodationName = accommodationName;
        this.userEmail = userEmail;
        this.comment = comment;
        this.travelType = travelType;
        this.quality = quality;
        this.position = position;
        this.cleaning = cleaning;
        this.service = service;
        this.listener = listener;
    }

    AsyncReviewForm(OnTaskCompletedReviewForm listener,String accommodationName,String userEmail,String comment,String travelType,float quality, float position, float cleaning, float service){
        this.accommodationName = accommodationName;
        this.userEmail = userEmail;
        this.comment = comment;
        this.travelType = travelType;
        this.quality = quality;
        this.position = position;
        this.cleaning = cleaning;
        this.service = service;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {

        String response;

        ReviewDAO revSQL = new MySQLReviewDAO();

        if(idReview == null) {
            response = revSQL.insertReview(accommodationName, userEmail, comment, travelType, quality, position, cleaning, service);
        }else{
            response = revSQL.updateReview(idReview,accommodationName,userEmail,comment,travelType,quality,position,cleaning,service);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {

        listener.onTaskCompleteReviewForm(response);
    }
}
