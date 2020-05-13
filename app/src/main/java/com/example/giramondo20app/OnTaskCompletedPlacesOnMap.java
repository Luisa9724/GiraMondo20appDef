package com.example.giramondo20app;

import android.app.Activity;

import com.example.giramondo20app.Model.AccommodationModel;


import java.util.HashMap;
import java.util.List;

public interface OnTaskCompletedPlacesOnMap {

    void onTaskCompletePlacesOnMap(HashMap<String, List<AccommodationModel>> results, String selectedItem, Activity activity, Object mMap, AccommodationModel accommodation);
}
