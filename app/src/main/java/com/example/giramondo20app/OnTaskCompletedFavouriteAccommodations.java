package com.example.giramondo20app;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.List;

public interface OnTaskCompletedFavouriteAccommodations {

    void onTaskComplete(List<AccommodationModel> results,String userEmail);
}
