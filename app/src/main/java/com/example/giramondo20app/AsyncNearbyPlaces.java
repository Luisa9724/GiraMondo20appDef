package com.example.giramondo20app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;
import com.google.android.gms.maps.GoogleMap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncNearbyPlaces extends AsyncTask<String,Void, HashMap<String,List<AccommodationModel>>> {
    private WeakReference<Activity> mActivity;
    OnTaskCompletedPlacesOnMap listener;
    String selectedItem;
    GoogleMap mMap;
    AccommodationModel currentAcm;

    public AsyncNearbyPlaces(Activity activity,Object map,OnTaskCompletedPlacesOnMap listener, String selectedItem, AccommodationModel accommodation){
        mActivity = new WeakReference<>(activity);
        this.listener = listener;
        this.selectedItem = selectedItem;
        mMap = (GoogleMap) map;
        currentAcm = accommodation;

    }

    @Override
    protected HashMap<String,List<AccommodationModel>> doInBackground(String... strings) {

        String city = strings[0];

        HashMap<String,List<AccommodationModel>> results = new HashMap<>();
        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        results.put("hotels", acmSQL.getNearbyHotels(city));
        results.put("restaurants", acmSQL.getNearbyRestaurants(city));
        results.put("attractions", acmSQL.getNearbyAttractions(city));

        return results;
    }

    @Override
    protected void onPostExecute(HashMap<String, List<AccommodationModel>> results) {
        final Activity activity = mActivity.get();
        if(activity != null && results != null)
        listener.onTaskCompletePlacesOnMap(results,selectedItem,activity,mMap,currentAcm);
    }
}
