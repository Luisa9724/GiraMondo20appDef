package com.example.giramondo20app;

import android.os.AsyncTask;
import android.util.Log;

import com.example.giramondo20app.Controller.DAO.AccommodationDAO;
import com.example.giramondo20app.Controller.DAO.MySQLAccommodationDAO;
import com.example.giramondo20app.Model.AccommodationModel;

public class AsyncFavouriteDestination extends AsyncTask<Void,Void,Void> {

    String userEmailLogged;
    String acmName;
    boolean favourite;

    AsyncFavouriteDestination(String userEmailLogged, String acmName,boolean favourite){
        this.userEmailLogged = userEmailLogged;
        this.favourite = favourite;
        this.acmName = acmName;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        AccommodationDAO acmSQL = new MySQLAccommodationDAO();
        if(acmName != null && favourite) {
            acmSQL.insertFavourite(userEmailLogged, acmName);
        }else if(acmName != null && !favourite){
            acmSQL.deleteFavourite(userEmailLogged,acmName);
        }

        return null;
    }
}
