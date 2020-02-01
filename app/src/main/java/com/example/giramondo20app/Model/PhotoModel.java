package com.example.giramondo20app.Model;

public class PhotoModel {
    String url;
    String tipo;

    public PhotoModel(String url, String tipo) {
        this.url = url;
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
