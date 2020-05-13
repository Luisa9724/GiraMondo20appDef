package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncAccommodations extends AsyncTask<Void,Void, HashMap<String,List<AccommodationModel>>>{

    OnTaskCompletedAccommodationMostlyPopular listener;

    AsyncAccommodations(OnTaskCompletedAccommodationMostlyPopular listener){
        this.listener = listener;
    }

    @Override
    protected HashMap<String,List<AccommodationModel>> doInBackground(Void... voids) {
        List<AccommodationModel> results;
        List<AccommodationModel> hotels = new ArrayList<>();
        List<AccommodationModel> attractions = new ArrayList<>();
        List<AccommodationModel> restaurants = new ArrayList<>();
        HashMap<String,List<AccommodationModel>> map = new HashMap<>();

        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        results = acmSQL.getAcmsMostlyPopular();

        for (AccommodationModel item: results) {
            if(item.getAccommodationType().equals("Albergo")){
                hotels.add(item);
            }else if(item.getAccommodationType().equals("Ristorante")){
                restaurants.add(item);
            }else{
                attractions.add(item);
            }
        }
        map.put("hotels",hotels);
        map.put("restaurants",restaurants);
        map.put("attractions",attractions);

        return map;
    }

    @Override
    protected void onPostExecute(HashMap<String, List<AccommodationModel>> results) {
        if(results != null)
        listener.onTaskComplete(results);
    }
}
