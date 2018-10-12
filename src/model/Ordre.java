package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Ordre {

    private double pris;
    private Betalingsmiddel betalingsmiddel;
    private LocalDate dato;
    private ArrayList<Ordrelinje> ordrelinjer = new ArrayList<>();
    private Prisliste prisliste;
    private Strategy_giv_rabat strategy;
    private boolean rabat_angivet;
    private double rabat;

    // ordre uden rabat
    public Ordre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste) {
        this.betalingsmiddel = betalingsmiddel;
        this.dato = dato;
        setPrisliste(prisliste);
    }

    // ordre med rabat
    public Ordre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste, Strategy_giv_rabat strategy,
            double rabat) {
        this.betalingsmiddel = betalingsmiddel;
        this.dato = dato;
        setPrisliste(prisliste);
        this.strategy = strategy;
        this.rabat = rabat;
        setRabat_angivet(true);
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

    // bruges til at angive rabat
    public double executeStrategy(double rabat, Ordre ordre) {
        return strategy.giv_rabat(rabat, ordre);

    }

    public Strategy_giv_rabat getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy_giv_rabat strategy) {
        this.strategy = strategy;
    }

    public boolean isRabat_angivet() {
        return rabat_angivet;
    }

    public void setRabat_angivet(boolean rabat_angivet) {
        this.rabat_angivet = rabat_angivet;
    }

    public double getRabat() {
        return rabat;
    }

    public void setRabat(double rabat) {
        this.rabat = rabat;
    }

    public double samletpris() {
        pris = 0;

        for (Ordrelinje o : ordrelinjer) {

            if (prisliste.getNavn().equals(o.getProduktpris().getPrisliste().getNavn())) {
                pris = pris + o.getAntal() * o.getProduktpris().getPris();
            }
        }

        return pris;
    }

    // denne metode kører kun hvis rabat_angivet == true - det bliver tjekket i
    // controlleren når salget skal registreres
    public double samletpris_med_rabat() {

        pris = executeStrategy(rabat, this);

        return pris;
    }

}
