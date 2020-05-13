package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

import java.util.ArrayList;
import java.util.List;

public class AsyncFavouriteAccommodations extends AsyncTask<String,Void, List<AccommodationModel>> {

    String userEmail;
    OnTaskCompletedFavouriteAccommodations listener;

    AsyncFavouriteAccommodations(OnTaskCompletedFavouriteAccommodations listener){
        this.listener = listener;
    }

    @Override
    protected List<AccommodationModel> doInBackground(String... strings) {
        List<AccommodationModel> results = new ArrayList<>();
        userEmail = strings[0];
        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        if(!userEmail.isEmpty()){
            results = acmSQL.getAcmsByUserEmail(userEmail);
        }
        return results;
    }

    @Override
    protected void onPostExecute(List<AccommodationModel> results) {
        if(listener != null)
            listener.onTaskComplete(results,userEmail);
    }
}
