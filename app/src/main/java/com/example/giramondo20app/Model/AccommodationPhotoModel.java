package com.example.giramondo20app.Model;

public class AccommodationPhotoModel {
    byte[] file;

    public AccommodationPhotoModel(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
