package com.example.giramondo20app;

import android.os.AsyncTask;


import com.example.giramondo20app.Controller.DAO.MySQLAccommodationPhotoDAO;
import com.example.giramondo20app.Controller.DAO.AccommodationPhotoDAO;
import com.example.giramondo20app.Model.AccommodationModel;
import com.example.giramondo20app.Model.AccommodationPhotoModel;

import java.util.ArrayList;
import java.util.List;

public class AsyncAccommodationPhotos extends AsyncTask<String,Void, List<AccommodationPhotoModel>> {
    OnTaskCompletedOverview listener;
    AccommodationModel selectedAccommodation;

    AsyncAccommodationPhotos(OnTaskCompletedOverview listener, AccommodationModel selectedAccommodation) {
        this.listener = listener;
        this.selectedAccommodation = selectedAccommodation;
    }
    @Override
    protected List<AccommodationPhotoModel> doInBackground(String... strings) {
        String name = strings[0];
        ArrayList<AccommodationPhotoModel> results;
        AccommodationPhotoDAO phSQL = new MySQLAccommodationPhotoDAO();
        results = (ArrayList<AccommodationPhotoModel>) phSQL.getAccommodationPhotos(name);
        return results;

    }

    @Override
    protected void onPostExecute(List<AccommodationPhotoModel> accommodationPhotoModels) {
        listener.onTaskComplete(accommodationPhotoModels,selectedAccommodation);
    }
}
