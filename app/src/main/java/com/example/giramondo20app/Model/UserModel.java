package com.example.giramondo20app.Model;

import java.util.Date;

public class UserModel {

    private byte[] userImage;
    private String username;
    private String surname;
    private Date birthday;
    private String nickname;
    private String email;
    private boolean nameIsVisible;
    private boolean approved;


    public UserModel(String username, String surname, Date birthday, String nickname, String email, boolean nameIsVisible) {
        this.username = username;
        this.surname = surname;
        this.birthday = birthday;
        this.nickname = nickname;
        this.email = email;
        this.nameIsVisible = nameIsVisible;
    }

    public UserModel(String username, String surname, Date birthday, String nickname, String email, boolean nameIsVisible, byte[] userImage,boolean approved) {
        this.userImage = userImage;
        this.username = username;
        this.surname = surname;
        this.birthday = birthday;
        this.nickname = nickname;
        this.email = email;
        this.nameIsVisible = nameIsVisible;
        this.approved = approved;
    }

    public String getUsername() {
        return username;
    }

   /* public void setUsername(String username) {
        this.username = username;
    }*/

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }


    public String getSurname() {
        return surname;
    }

   /* public void setSurname(String surname) {
        this.surname = surname;
    }*/

    public Date getBirthday() {
        return birthday;
    }

    /*public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }*/

    public String getNickname() {
        return nickname;
    }

   /* public void setNickname(String nickname) {
        this.nickname = nickname;
    }*/

    public boolean isNameIsVisible() {
        return nameIsVisible;
    }

    /*public void setNameIsVisible(boolean nameIsVisible) {
        this.nameIsVisible = nameIsVisible;
    }*/

    public byte[] getUserImage() {
        return userImage;
    }

   /* public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }*/

    public boolean isApproved() {
        return approved;
    }

    /*public void setApproved(boolean approved) {
        this.approved = approved;
    }*/

}
