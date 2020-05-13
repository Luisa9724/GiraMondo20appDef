package com.example.giramondo20app;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.List;

public interface OnTaskCompletedResearchResults {
    void sendErrorResponse();
    void onTaskComplete(List<AccommodationModel> results);
    void showProgressBar();
    void hideProgressBar();
}
