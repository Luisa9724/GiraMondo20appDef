package com.example.giramondo20app.Controller.DAO;

import com.example.giramondo20app.Controller.DatabaseController;
import com.example.giramondo20app.Model.ReviewModel;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLReviewDAO implements ReviewDAO {

    @Override
    public String insertReview(String accommodationName,String useremail, String comment, String travelType, float quality, float position, float cleaning, float service) {
        DatabaseController.connect();
        String response="something went wrong";
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("INSERT INTO REVIEW(QUALITYPRICE,POSITION,CLEANING,SERVICE,COMMENT,TRAVELTYPE,USEREMAIL,ACCOMMODATIONNAME) VALUES(?,?,?,?,?,?,?,?)");
            ps.setFloat(1, quality);
            ps.setFloat(2,position);
            ps.setFloat(3,cleaning);
            ps.setFloat(4,service);
            ps.setString(5, comment);
            ps.setString(6, travelType);
            ps.setString(7, useremail);
            ps.setString(8, accommodationName);

            int count = ps.executeUpdate();
            if(count == 1){
                response = "operation ends successfully";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return response;
    }

    @Override
    public List<ReviewModel> getReviewsByAccommodation(String accommodationName) {
        DatabaseController.connect();
        List<ReviewModel> results = new ArrayList<>();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT R.COMMENT,R.TOTALAMOUNT,R.DATE,U.USERID,U.FILEIMAGE,U.PHOTOAPPROVED FROM REVIEW AS R JOIN USER AS U ON U.EMAIL = R.USEREMAIL WHERE (R.ACCOMMODATIONNAME = ?) AND (R.APPROVED = '1')");
            ps.setString(1,accommodationName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Date dateReview = new Date(rs.getDate(3).getTime());
                Blob imageBlob = rs.getBlob(5);
                if(imageBlob != null) {
                    byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    results.add(new ReviewModel(rs.getFloat(2), rs.getString(1), rs.getString(4), imageBytes, dateReview,rs.getBoolean(6)));
                }else{
                    results.add(new ReviewModel(rs.getFloat(2), rs.getString(1), rs.getString(4), dateReview,rs.getBoolean(6)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return results;
    }

    @Override
    public List<ReviewModel> getReviewsByUserEmail(String userEmail) {
        DatabaseController.connect();
        List<ReviewModel> results = new ArrayList<>();
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("SELECT R.ID,R.COMMENT,R.TOTALAMOUNT,R.DATE,A.NAME,A.PHOTO FROM REVIEW AS R JOIN ACCOMMODATION AS A ON A.NAME = R.ACCOMMODATIONNAME WHERE (R.USEREMAIL = ?) AND (R.APPROVED = '1')");
            ps.setString(1,userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Date dateReview = new Date(rs.getDate(4).getTime());
                Blob imageBlob = rs.getBlob(6);
                if(imageBlob != null) {
                    byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    results.add(new ReviewModel(rs.getInt(1),rs.getFloat(3), rs.getString(2),imageBytes,rs.getString(5),dateReview));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return results;
    }

    @Override
    public String updateReview(Integer idReview, String accommodationName, String userEmail, String comment, String travelType, float quality, float position, float cleaning, float service) {
        DatabaseController.connect();
        String response="something went wrong";
        try {
            PreparedStatement ps = DatabaseController.getConnection().prepareStatement("UPDATE REVIEW SET QUALITYPRICE = ?,POSITION = ?, CLEANING = ?,SERVICE = ?,COMMENT = ?, TRAVELTYPE = ?, APPROVED = '0' WHERE (ID = ?) AND (USEREMAIL = ?) AND (ACCOMMODATIONNAME = ?)");
            ps.setFloat(1, quality);
            ps.setFloat(2,position);
            ps.setFloat(3,cleaning);
            ps.setFloat(4,service);
            ps.setString(5, comment);
            ps.setString(6, travelType);
            ps.setInt(7,idReview);
            ps.setString(8, userEmail);
            ps.setString(9, accommodationName);

            int count = ps.executeUpdate();
            if(count == 1){
                response = "operation ends successfully";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseController.disconnect();
        return response;
    }
}
