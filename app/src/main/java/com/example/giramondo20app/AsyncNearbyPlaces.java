package com.example.giramondo20app;

import android.app.Activity;
import android.os.AsyncTask;


import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;
import com.google.android.gms.maps.GoogleMap;

import java.lang.ref.WeakReference;

import java.util.HashMap;
import java.util.List;

public class AsyncNearbyPlaces extends AsyncTask<String,Void, HashMap<String,List<AccommodationModel>>> {

    private WeakReference<Activity> mActivity;
    private OnTaskCompletedPlacesOnMap listener;
    private String selectedItem;
    private GoogleMap mMap;
    private AccommodationModel currentAcm;
    private AccommodationDAO acmSQL;

    AsyncNearbyPlaces(Activity activity, Object map, OnTaskCompletedPlacesOnMap listener, String selectedItem, AccommodationModel accommodation){
        mActivity = new WeakReference<>(activity);
        this.listener = listener;
        this.selectedItem = selectedItem;
        mMap = (GoogleMap) map;
        currentAcm = accommodation;

    }

    public AsyncNearbyPlaces() {

    }

    @Override
    protected HashMap<String,List<AccommodationModel>> doInBackground(String... strings) {

        String city = strings[0];

        setDAO(null);

        return getNearbyAccommodations(city);
    }

    @Override
    protected void onPostExecute(HashMap<String, List<AccommodationModel>> results) {
        final Activity activity = mActivity.get();
        if(activity != null && results != null)
        listener.onTaskCompletePlacesOnMap(results,selectedItem,activity,mMap,currentAcm);
    }

    public void setDAO(AccommodationDAO acmDAO){
        if(acmDAO != null) {
            acmSQL = acmDAO;
        }else{
            acmSQL = new MySQLAccommodationDAO();
        }
    }

    public HashMap<String,List<AccommodationModel>> getNearbyAccommodations(String location){

        HashMap<String,List<AccommodationModel>> results = new HashMap<>();

        if(location == null)
            throw new IllegalArgumentException();

        results.put("hotels", acmSQL.getNearbyHotels(location));
        results.put("restaurants", acmSQL.getNearbyRestaurants(location));
        results.put("attractions", acmSQL.getNearbyAttractions(location));

        return results;
    }
}
