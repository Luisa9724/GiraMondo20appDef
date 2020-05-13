package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.MySQLUserDAO;
import com.example.giramondo20app.Controller.DAO.UserDAO;
import com.example.giramondo20app.Model.UserModel;

public class AsyncLogin extends AsyncTask<String,Void, UserModel> {

    private String email;
    private String password;
    private UserModel user;
    private OnTaskCompletedLogin listener;

    AsyncLogin(OnTaskCompletedLogin listener){
        this.listener = listener;
    }

    @Override
    protected UserModel doInBackground(String... strings) {
       email = strings[0];
       password = strings[1];

        UserDAO userSQL = new MySQLUserDAO();
       return user = userSQL.getUserByEmailAndPassword(email,password);
    }

    @Override
    protected void onPostExecute(UserModel result) {
        if(result != null && listener !=null)
            listener.onTaskComplete(result);
    }
}
