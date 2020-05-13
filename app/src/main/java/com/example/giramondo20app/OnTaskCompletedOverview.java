package com.example.giramondo20app;

import com.example.giramondo20app.Model.AccommodationModel;
import com.example.giramondo20app.Model.AccommodationPhotoModel;

import java.util.List;

public interface OnTaskCompletedOverview {

    void onTaskComplete(List<AccommodationPhotoModel> results, AccommodationModel selectedAccommodation);
}
