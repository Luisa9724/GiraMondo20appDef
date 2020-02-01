package com.example.giramondo20app.Model;

public class OrderModel {

    public enum OrdinaPer {
        qualitàPrezzo, posizione, pulizia, servizio
    }

    OrdinaPer tipo;


    public OrderModel(OrdinaPer tipo) {
        this.tipo = tipo;
    }
    public OrdinaPer getTipo() {
        return tipo;
    }

    public void setTipo(OrdinaPer tipo) {
        this.tipo = tipo;
    }
}