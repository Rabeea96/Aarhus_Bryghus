package model;

import java.time.LocalDate;

public class Produktsalg {

    private String produktNavn;
    private double pris;
    private String betalingsmiddel;
    private LocalDate dato;

    public Produktsalg(String produktNavn, double pris, String betalingsmiddel, LocalDate dato) {
        this.produktNavn = produktNavn;
        this.pris = pris;
        this.betalingsmiddel = betalingsmiddel;
        this.dato = dato;
    }

    public String getProduktNavn() {
        return produktNavn;
    }

    public void setProduktNavn(String produktNavn) {
        this.produktNavn = produktNavn;
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }

    public String getBetalingsmiddel() {
        return betalingsmiddel;
    }

    public void setBetalingsmiddel(String betalingsmiddel) {
        this.betalingsmiddel = betalingsmiddel;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

}
