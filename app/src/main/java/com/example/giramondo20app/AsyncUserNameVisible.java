package com.example.giramondo20app;

import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.MySQLUserDAO;
import com.example.giramondo20app.Controller.DAO.UserDAO;
import com.example.giramondo20app.Model.UserModel;

public class AsyncUserNameVisible extends AsyncTask<UserModel,Void,String> {

    OnTaskCompletedUserProfile listener;
    private boolean nameIsVisibleLoggedUser;
    private String userEmailLogged;

    AsyncUserNameVisible(OnTaskCompletedUserProfile listener,boolean nameIsVisibleLoggedUser,String userEmailLogged){
        this.listener = listener;
        this.nameIsVisibleLoggedUser = nameIsVisibleLoggedUser;
        this.userEmailLogged = userEmailLogged;
    }

    @Override
    protected String doInBackground(UserModel... userModels) {

        return setIfNameIsVisible(nameIsVisibleLoggedUser,userEmailLogged);
    }

    @Override
    protected void onPostExecute(String response) {
        if(listener !=null){
            listener.onTaskCompletedNameIsVisible(response);
        }
    }

    private String setIfNameIsVisible(boolean nameIsVisible, String userEmail){
        String response;
        UserDAO userSQL = new MySQLUserDAO();
        response = userSQL.setNameIsVisible(nameIsVisible,userEmail);
        return response;
    }
}
