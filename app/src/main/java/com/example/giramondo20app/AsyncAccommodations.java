package com.example.giramondo20app;

import android.os.AsyncTask;
import android.util.Log;

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

    public AsyncAccommodations() {

    }

    @Override
    protected HashMap<String,List<AccommodationModel>> doInBackground(Void... voids) {

        List<AccommodationModel> results;
        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        results = acmSQL.getAcmsMostlyPopular();

        return getMostPopularAccommodations(results);
    }

    @Override
    protected void onPostExecute(HashMap<String, List<AccommodationModel>> results) {
        if(results != null)
        listener.onTaskComplete(results);
    }

    public HashMap<String,List<AccommodationModel>> getMostPopularAccommodations(List<AccommodationModel> accommodationList){

        List<AccommodationModel> hotels = new ArrayList<>();
        List<AccommodationModel> attractions = new ArrayList<>();
        List<AccommodationModel> restaurants = new ArrayList<>();
        HashMap<String,List<AccommodationModel>> map = new HashMap<>();

        if(accommodationList == null)
            throw new IllegalArgumentException();

        for(AccommodationModel item: accommodationList) {

            if(item.getAccommodationType().equals("Albergo")){
                hotels.add(item);
            }else if(item.getAccommodationType().equals("Ristorante")){
                restaurants.add(item);
            }else{
                attractions.add(item);
            }

        }

        if(!hotels.isEmpty())
        map.put("hotels",hotels);

        if(!restaurants.isEmpty())
        map.put("restaurants",restaurants);

        if(!attractions.isEmpty())
        map.put("attractions",attractions);

        return map;
    }
}
