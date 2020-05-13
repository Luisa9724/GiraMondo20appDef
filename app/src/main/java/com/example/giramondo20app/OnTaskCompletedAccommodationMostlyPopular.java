package com.example.giramondo20app;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.HashMap;
import java.util.List;

public interface OnTaskCompletedAccommodationMostlyPopular {

    void onTaskComplete(HashMap<String,List<AccommodationModel>> results);
}
