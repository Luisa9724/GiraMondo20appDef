package com.example.giramondo20app;

import android.os.AsyncTask;
import android.util.Log;


import com.example.giramondo20app.Controller.DAO.MySQLUserDAO;
import com.example.giramondo20app.Controller.DAO.UserDAO;
import com.example.giramondo20app.Model.UserModel;

public class AsyncLogin extends AsyncTask<String,Void, UserModel> {

    private OnTaskCompletedLogin listener;

    AsyncLogin(OnTaskCompletedLogin listener){
        this.listener = listener;
    }

    @Override
    protected UserModel doInBackground(String... strings) {
        String email = strings[0];
        String password = strings[1];

        UserDAO userSQL = new MySQLUserDAO();
       return  userSQL.getUserByEmailAndPassword(email, password);
    }

    @Override
    protected void onPostExecute(UserModel result) {
        if(listener !=null)
            listener.onTaskComplete(result);
    }
}
