package com.example.giramondo20app;

import com.example.giramondo20app.Model.ReviewModel;

import java.util.List;

public interface OnTaskCompletedUserProfile {

    void onTaskCompletedNameIsVisible(String response);
    void onTaskCompletedRefreshUserInfo(Object[] results);
}
