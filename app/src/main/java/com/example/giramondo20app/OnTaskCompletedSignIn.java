package com.example.giramondo20app;

import java.util.Date;

public interface OnTaskCompletedSignIn {

    void onTaskComplete(String status, String username, String surname, String nick, String email, String password, String date, boolean nameVisible);
}
