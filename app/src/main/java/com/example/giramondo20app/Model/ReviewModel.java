package com.example.giramondo20app.Model;

import java.util.Date;

public class ReviewModel {
    int idReview;
    float qualityPrice;
	float position;
	float cleaning;
	float service;
    float totalAmount;
    String travelType;
	String comment;
	boolean approved;
	String userId;
	String acmName;
	byte[] userImage;
	byte[] acmImage;
	Date dateReview;
	boolean userPhotoApproved;

   public ReviewModel(int idReview, float qualityPrice, float position, float cleaning, float service, float totalAmount, String travelType, String comment,boolean approved) {
        this.idReview = idReview;
        this.qualityPrice = qualityPrice;
        this.position = position;
        this.cleaning = cleaning;
        this.service = service;
        this.totalAmount = totalAmount;
        this.travelType = travelType;
        this.comment = comment;
        this.approved = approved;
    }

    public ReviewModel(float totalAmount, String comment, String userId, byte[] userImage, Date dateReview,boolean userPhotoApproved) {
        this.totalAmount = totalAmount;
        this.comment = comment;
        this.userId = userId;
        this.userImage = userImage;
        this.dateReview = dateReview;
        this.userPhotoApproved = userPhotoApproved;
    }

    public ReviewModel(float totalAmount, String comment, String userId, Date dateReview,boolean userPhotoApproved) {
        this.totalAmount = totalAmount;
        this.comment = comment;
        this.userId = userId;
        this.dateReview = dateReview;
        this.userPhotoApproved = userPhotoApproved;
    }

    public ReviewModel(int idReview,float totalAmount, String comment, byte[] acmImage,String acmName,Date dateReview) {
        this.idReview = idReview;
        this.totalAmount = totalAmount;
        this.comment = comment;
        this.acmImage = acmImage;
        this.acmName = acmName;
        this.dateReview = dateReview;
    }


    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int id) {
        idReview = id;
    }

    public float getQualityPrice() {
        return qualityPrice;
    }

    public void setQualityPrice(float qualityPrice) {
        this.qualityPrice = qualityPrice;
    }

    public float getPositionRev() {
        return position;
    }

    public void setPositionRev(float position) {
        this.position = position;
    }

    public float getCleaning() {
        return cleaning;
    }

    public void setCleaning(float cleaning) {
        cleaning = cleaning;
    }

    public float getService() {
        return service;
    }

    public void setService(float service) {
        this.service = service;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTravelTypeRev() {
        return travelType;
    }

    public void setTravelTypeRev(String travelType) {
        this.travelType = travelType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public Date getDateReview() {
        return dateReview;
    }

    public void setDateReview(Date dateReview) {
        this.dateReview = dateReview;
    }

    public boolean isUserPhotoApproved() {
        return userPhotoApproved;
    }

    public void setUserPhotoApproved(boolean userPhotoApproved) {
        this.userPhotoApproved = userPhotoApproved;
    }

    public String getAcmName() {
        return acmName;
    }

    public void setAcmName(String acmName) {
        this.acmName = acmName;
    }

    public byte[] getAcmImage() {
        return acmImage;
    }

    public void setAcmImage(byte[] acmImage) {
        this.acmImage = acmImage;
    }
}
