package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class FadølsAnlægsUdlejning_ordre extends Ordre {

    private ArrayList<Produkt> fustager = new ArrayList<>();
    private ArrayList<Produkt> kulsyrer = new ArrayList<>();
    private ArrayList<Produkt> anlæg = new ArrayList<>();
    private int pant = 0;

    // ordre uden rabat - der kan ikke gives rabat på fadølsanlægs-udlejninger
    public FadølsAnlægsUdlejning_ordre(Betalingsmiddel betalingsmiddel, LocalTime tidspunkt, LocalDate startDato,
            LocalDate slutDato, Prisliste prisliste, ArrayList<Produkt> fustager, ArrayList<Produkt> kulsyrer,
            ArrayList<Produkt> anlæg) {
        super(betalingsmiddel, tidspunkt, startDato, slutDato, prisliste);
        this.fustager = fustager;
        this.kulsyrer = kulsyrer;
        this.anlæg = anlæg;
        setPant((fustager.size() * 200) + (kulsyrer.size() * 1000));
    }

    public int getPant() {
        return pant;
    }

    public void setPant(int pant) {
        this.pant = pant;
    }

    // fustage
    public ArrayList<Produkt> getFustager() {
        return new ArrayList<>(fustager);
    }

    public void removeFustage(Fustage fustage) {
        fustager.remove(fustage);
    }

    public void addFustage(Fustage fustage) {
        fustager.add(fustage);
    }

    // kulsyre
    public ArrayList<Produkt> getKulsyrer() {
        return new ArrayList<>(kulsyrer);
    }

    public void removeKulsyre(Kulsyre kulsyre) {
        kulsyrer.remove(kulsyre);
    }

    public void addKulsyre(Kulsyre kulsyre) {
        kulsyrer.add(kulsyre);
    }

    // anlæg
    public ArrayList<Produkt> getAnlæg() {
        return new ArrayList<>(anlæg);
    }

    public void removeAnlæg(Anlæg anlæg) {
        this.anlæg.remove(anlæg);
    }

    public void addAnlæg(Anlæg anlæg) {
        this.anlæg.add(anlæg);
    }

}
