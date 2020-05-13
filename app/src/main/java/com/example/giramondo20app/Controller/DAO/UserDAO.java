package com.example.giramondo20app.Controller.DAO;


import com.example.giramondo20app.Model.UserModel;

import java.util.Date;


public interface UserDAO {

    UserModel getUserByEmailAndPassword(String email,String password);

    String signInUser(String name, String surname, String nick, Date bDay, String email,String password,boolean nameVisible,byte[] userPhoto);

    String updateUserPhoto(byte[] userImage,String userEmail);

    String setNameIsVisible(boolean nameIsVisible,String userEmail);

    Object[] getAmountWrittenReviewsAndAverageRatingbyUserEmail(String userEmail);
}
