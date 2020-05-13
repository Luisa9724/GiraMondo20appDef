package com.example.giramondo20app;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.giramondo20app.Controller.DAO.MySQLUserDAO;
import com.example.giramondo20app.Controller.DAO.UserDAO;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsyncSignIn extends AsyncTask<String,Void,String> {

    OnTaskCompletedSignIn listener;
    private WeakReference<Activity> mActivity;

    Uri photoUrl;
    String username;
    String surname;
    String nick;
    String email;
    String password;
    String date;
    String nameVisible;
    Date mDate;
    boolean nameIsVisible;

    AsyncSignIn(Activity activity,Uri photoUrl,OnTaskCompletedSignIn listener){
        mActivity = new WeakReference<>(activity);
        this.listener = listener;
        this.photoUrl = photoUrl;
    }

    @Override
    protected String doInBackground(String... strings) {

        username = strings[0];
        surname = strings[1];
        nick = strings[2];
        email = strings[3];
        password = strings[4];
        date = strings[5];
        nameVisible = strings[6];

        byte[] photoBytes = null;
        String response = "Something went wrong";

        mDate = parseDate(date);

        if(nameVisible.equals("Nome")){
            nameIsVisible = true;
        }else{
            nameIsVisible = false;
        }

        UserDAO userSQL = new MySQLUserDAO();

        if(mActivity != null) {
            Activity activity = mActivity.get();
            try {
                InputStream isStream = (activity).getContentResolver().openInputStream(photoUrl);
                photoBytes = getBytes(isStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (photoBytes != null)
        response = userSQL.signInUser(username, surname, nick, mDate, email, password, nameIsVisible,photoBytes);

        return response;
    }

    @Override
    protected void onPostExecute(String status) {
        if(listener != null)
        listener.onTaskComplete(status,username,surname,nick,email,password,date,nameIsVisible);
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        }finally {
            try{
                byteBuffer.close();
            }catch (IOException ignored){}
        }
        return bytesResult;
    }
}
