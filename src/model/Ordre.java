package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Ordre {

    private double pris;
    private Betalingsmiddel betalingsmiddel;
    private LocalDate dato;
    private ArrayList<Ordrelinje> ordrelinjer = new ArrayList<>();
    private Prisliste prisliste;

    public Ordre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste) {
        this.betalingsmiddel = betalingsmiddel;
        this.dato = dato;
        setPrisliste(prisliste);
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }

    public Betalingsmiddel getBetalingsmiddel() {
        return betalingsmiddel;
    }

    public void setBetalingsmiddel(Betalingsmiddel betalingsmiddel) {
        this.betalingsmiddel = betalingsmiddel;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public void addOrdrelinje(Ordrelinje ordrelinje) {
        ordrelinjer.add(ordrelinje);
    }

    public void removeOrdrelinje(Ordrelinje ordrelinje) {
        ordrelinjer.remove(ordrelinje);
    }

    public ArrayList<Ordrelinje> getOrdrelinjer() {
        return new ArrayList<>(ordrelinjer);
    }

    public Prisliste getPrisliste() {
        return prisliste;
    }

    public void setPrisliste(Prisliste prisliste) {
        this.prisliste = prisliste;
    }

    public double samletpris() {
        for (Ordrelinje o : ordrelinjer) {

            if (prisliste.getNavn().equals(o.getProduktpris().getPrisliste().getNavn())) {
                pris = pris + o.getAntal() * o.getProduktpris().getPris();
            }
        }

        return pris;
    }

}
