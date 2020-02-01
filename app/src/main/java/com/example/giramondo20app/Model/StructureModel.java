package com.example.giramondo20app.Model;


import java.util.List;

public class StructureModel {
    private String name;
    private int[] valutazione={1,2,3,4,5};
    private float prezzo=1;
    private String descrizione;
    private String servizi;
    private boolean preferita;
    private String città;
    private String stato;
    private String regione;
    private List<PhotoModel> immagini;

    public StructureModel(String name, int[] valutazione, float prezzo, String descrizione, String servizi, boolean preferita, String città, String stato, String regione, List<PhotoModel> immagini) {
        this.name = name;
        this.valutazione = valutazione;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.servizi = servizi;
        this.preferita = preferita;
        this.città = città;
        this.stato = stato;
        this.regione = regione;
        this.immagini = immagini;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getValutazione() {
        return valutazione;
    }

    public void setValutazione(int[] valutazione) {
        this.valutazione = valutazione;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getServizi() {
        return servizi;
    }

    public void setServizi(String servizi) {
        this.servizi = servizi;
    }

    public boolean isPreferita() {
        return preferita;
    }

    public void setPreferita(boolean preferita) {
        this.preferita = preferita;
    }

    public String getCittà() {
        return città;
    }

    public void setCittà(String città) {
        this.città = città;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public List<PhotoModel> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<PhotoModel> immagini) {
        this.immagini = immagini;
    }
}
