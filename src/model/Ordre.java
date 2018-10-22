package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public abstract class Ordre {

    private double pris;
    private Betalingsmiddel betalingsmiddel;
    private LocalDate dato;
    private LocalDate startDato;
    private LocalDate slutDato;
    private LocalTime tidspunkt;
    private ArrayList<Ordrelinje> ordrelinjer = new ArrayList<>();
    private Prisliste prisliste;
    private Strategy_giv_rabat strategy;
    private boolean rabat_angivet;
    private double rabat;

    // den bliver brugt til at tælle salg og samtidig som en ID for hver ordre
    private static int counter = 0;
    private int ordreCounter;

    // ordre uden rabat
    public Ordre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste) {
        this.betalingsmiddel = betalingsmiddel;
        this.dato = dato;
        setPrisliste(prisliste);

        counter++;
        setOrdreCounter(counter);
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

        counter++;
        setOrdreCounter(counter);
    }

    // ordre uden rabat - med tidspunkt og datoer for udlejningen
    public Ordre(Betalingsmiddel betalingsmiddel, LocalTime tidspunkt, LocalDate startDato, LocalDate slutDato,
            Prisliste prisliste) {
        this.betalingsmiddel = betalingsmiddel;
        // ordren/salget registreres først når udlejningen er afsluttet
        setDato(slutDato);
        this.tidspunkt = tidspunkt;
        this.startDato = startDato;
        this.slutDato = slutDato;
        setPrisliste(prisliste);

        counter++;
        setOrdreCounter(counter);
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

    public LocalTime getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(LocalTime tidspunkt) {
        this.tidspunkt = tidspunkt;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
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

    public static int getCounter() {
        return counter;
    }

    public int getOrdreCounter() {
        return ordreCounter;
    }

    public void setOrdreCounter(int ordreCounter) {
        this.ordreCounter = ordreCounter;
    }

    public abstract double samletpris();

    // denne metode kører kun hvis rabat_angivet == true - det bliver tjekket i
    // controlleren når salget skal registreres
    public abstract double samletpris_med_rabat();

    @Override
    public String toString() {
        return "UdlejningsID: " + counter;
    }

}
