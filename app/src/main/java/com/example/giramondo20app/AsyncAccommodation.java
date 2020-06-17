package com.example.giramondo20app;

import android.os.AsyncTask;
import android.util.Log;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

public class AsyncAccommodation extends AsyncTask<String,Void, AccommodationModel> {

    OnTaskCompletedSearchAccommodationByName listener;

    AsyncAccommodation(OnTaskCompletedSearchAccommodationByName listener){this.listener = listener;}

    @Override
    protected AccommodationModel doInBackground(String... strings) {

        String acmName = strings[0];
        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        return acmSQL.getAccommodationByName(acmName.trim());
    }

    @Override
    protected void onPostExecute(AccommodationModel result) {
        if(result != null)
        listener.onTaskCompleteSearchAccommodationByName(result);
    }
}
