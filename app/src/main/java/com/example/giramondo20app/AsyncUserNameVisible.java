package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.MySQLUserDAO;
import com.example.giramondo20app.Controller.DAO.UserDAO;
import com.example.giramondo20app.Model.UserModel;

public class AsyncUserNameVisible extends AsyncTask<UserModel,Void,String> {

    OnTaskCompletedUserProfile listener;
    boolean nameIsVisibleLoggedUser;
    String userEmailLogged;

    AsyncUserNameVisible(OnTaskCompletedUserProfile listener,boolean nameIsVisibleLoggedUser,String userEmailLogged){
        this.listener = listener;
        this.nameIsVisibleLoggedUser = nameIsVisibleLoggedUser;
        this.userEmailLogged = userEmailLogged;
    }

    @Override
    protected String doInBackground(UserModel... userModels) {
        UserDAO userSQL = new MySQLUserDAO();
        return userSQL.setNameIsVisible(nameIsVisibleLoggedUser,userEmailLogged);
    }

    @Override
    protected void onPostExecute(String response) {
        if(listener !=null){
            listener.onTaskCompletedNameIsVisible(response);
        }
    }
}
