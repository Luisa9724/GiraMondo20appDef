package com.example.giramondo20app;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.giramondo20app.Controller.DAO.MySQLUserDAO;
import com.example.giramondo20app.Controller.DAO.UserDAO;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;


public class AsyncUserPhoto extends AsyncTask<Void,Void,String> {

    Uri imageUri;
    byte[] userImage;
    String userEmail;
    private WeakReference<Activity> mActivity;

    AsyncUserPhoto(Activity activity,Uri imageUri, String userEmail){
        mActivity = new WeakReference<>(activity);
        this.imageUri = imageUri;
        this.userEmail = userEmail;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(mActivity != null) {
            Activity activity = mActivity.get();
            try {
                InputStream isStream = (activity).getContentResolver().openInputStream(imageUri);
                userImage = getBytes(isStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UserDAO userMySQL = new MySQLUserDAO();

        return userMySQL.updateUserPhoto(userImage,userEmail);
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
