package com.example.giramondo20app.Controller.DAO;

import android.util.Log;

import com.example.giramondo20app.Controller.DatabaseController;
import com.example.giramondo20app.Model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MySQLUserDAO implements UserDAO {


    @Override
    public UserModel getUserByEmailAndPassword(String email, String password) {
        DatabaseController.connect();
        UserModel user = null;
        try {
            CallableStatement call = DatabaseController.getConnection().prepareCall("SELECT NAME, SURNAME,BIRTHDAY, NICKNAME, EMAIL, PASSWORD,ISVISIBLENAME,FILEIMAGE,PHOTOAPPROVED FROM USER WHERE (EMAIL = ?) && (PASSWORD = ?)", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            call.setString(1, email);
            call.setString(2, password);
            ResultSet rs = call.executeQuery();
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            if(count == 1) {
                while (rs.next()){
                    Date userBirthday = new Date(rs.getDate(3).getTime());
                    Blob imageBlob = rs.getBlob("FileImage");

                    if(imageBlob == null) {
                        user = new UserModel(rs.getString(1), rs.getString(2), userBirthday, rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7));
                    }else{
                        byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                        user = new UserModel(rs.getString(1), rs.getString(2), userBirthday, rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7),imageBytes,rs.getBoolean(9));
                    }
               }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return user;
    }

    @Override
    public String signInUser(String name, String surname, String nick, Date bDay, String email, String password, boolean nameVisible,byte[] userPhoto) {
        DatabaseController.connect();
        String status = "Something went wrong";
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("INSERT IGNORE INTO USER(NAME,SURNAME,BIRTHDAY,NICKNAME,EMAIL,PASSWORD,ISVISIBLENAME,FILEIMAGE) VALUES(?,?,?,?,?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setDate(3, new java.sql.Date(bDay.getTime()));
            ps.setString(4, nick);
            ps.setString(5, email);
            ps.setString(6, password);
            ps.setBoolean(7,nameVisible);
            ps.setBlob(8,new ByteArrayInputStream(userPhoto),userPhoto.length);
            int count = ps.executeUpdate();
            if(count >0){
                status = "Successful sign in";
            }else{
                PreparedStatement pst = DatabaseController.getConnection().prepareStatement("SELECT NICKNAME FROM USER WHERE NICKNAME = ?");
                pst.setString(1,nick);
                ResultSet rs = pst.executeQuery();
                if(rs.next())
                    status = "Nick already exists";

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return status;
    }

    @Override
    public String updateUserPhoto(byte[] userImage, String userEmail) {
        DatabaseController.connect();
        String status= "photo not updated";
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("UPDATE USER SET FILEIMAGE = ?, PHOTOAPPROVED = ? WHERE EMAIL = ?");
            ps.setBlob(1, new ByteArrayInputStream(userImage),userImage.length);
            ps.setBoolean(2, false);
            ps.setString(3, userEmail);
            int count = ps.executeUpdate();
            if(count >0) {
                status = "photo updated";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return status;
    }

    @Override
    public String setNameIsVisible(boolean nameIsVisible,String userEmail) {
        DatabaseController.connect();
        String response= "something went wrong";
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("UPDATE USER SET ISVISIBLENAME = ? WHERE EMAIL = ?");
            ps.setBoolean(1, nameIsVisible);
            ps.setString(2, userEmail);
            int count = ps.executeUpdate();
            if(count >0) {
                response = "operation ends successfully";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return response;
    }

    @Override
    public Object[] getAmountWrittenReviewsAndAverageRatingbyUserEmail(String userEmail) {
        DatabaseController.connect();
        Integer count = 0;
        Float avg = 0F;
        Object[] result={0,0F};
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT AMOUNTWRITTENREVIEWS,AVGUSERRATING FROM USER WHERE (EMAIL = ?)");
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
                avg = rs.getFloat(2);
            }
            result[0]=count;
            result[1]=avg;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return result;
    }
}
