package com.example.giramondo20app.Controller.DAO;

import com.example.giramondo20app.Model.ReviewModel;

import java.util.List;

public interface ReviewDAO {

    String insertReview(String accommodationName,String userEmail,String comment,String travelType,float quality,float position,float cleaning, float service);
    List<ReviewModel> getReviewsByAccommodation(String accommodationName);
    List<ReviewModel> getReviewsByUserEmail(String userEmail);
    String updateReview(Integer idReview,String accommodationName,String userEmail, String comment, String travelType, float quality, float position, float cleaning, float service);

}
