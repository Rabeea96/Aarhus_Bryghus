package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rundvisning {

    private LocalDate dato;
    private LocalTime tidspunkt;
    private double pris;

    public Rundvisning(LocalDate dato, LocalTime tidspunkt, double pris) {
        this.dato = dato;
        this.tidspunkt = tidspunkt;
        this.pris = pris;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public LocalTime getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(LocalTime tidspunkt) {
        this.tidspunkt = tidspunkt;
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }

}
