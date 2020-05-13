package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.MySQLUserDAO;
import com.example.giramondo20app.Controller.DAO.UserDAO;

public class AsyncUserProfile extends AsyncTask<String,Void,Object[]> {

    String userEmail;
    OnTaskCompletedUserProfile listener;

    AsyncUserProfile(OnTaskCompletedUserProfile listener){
        this.listener = listener;
    }

    @Override
    protected Object[] doInBackground(String... strings) {

        userEmail = strings[0];
        UserDAO userSQL = new MySQLUserDAO();
        return userSQL.getAmountWrittenReviewsAndAverageRatingbyUserEmail(userEmail);
    }

    @Override
    protected void onPostExecute(Object[] results) {
        if(listener != null)
            listener.onTaskCompletedRefreshUserInfo(results);
    }
}
