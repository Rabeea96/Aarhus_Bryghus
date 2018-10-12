package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Fadølsanlæg_udlejning extends Produkt {

    private LocalDate startDato;
    private LocalDate slutDato;
    private ArrayList<Fustage> fustager = new ArrayList<>();
    private ArrayList<Kulsyre> kulsyrer = new ArrayList<>();
    private ArrayList<Anlæg> anlæg = new ArrayList<>();

    // en liste der samler produktnavne samt priser for en fustage, anlæg og kulsyre
    private ArrayList<String> liste_for_udlejningsprodukterne = new ArrayList<>();

    public Fadølsanlæg_udlejning(String navn, Produktgruppe produktgruppe, LocalDate startDato, LocalDate slutDato) {
        super(navn, produktgruppe);
        this.startDato = startDato;
        this.slutDato = slutDato;
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

    // fustager
    public Fustage createFustage(int liter, String navn, int pris) {
        Fustage fustage = new Fustage(liter, navn, pris);
        fustager.add(fustage);
        return fustage;
    }

    public ArrayList<Fustage> getFustager() {
        return new ArrayList<>(fustager);
    }

    public void removeFustage(Fustage fustage) {
        fustager.remove(fustage);
    }

    // kulsyrer
    public Kulsyre createKulsyre(String navn, int pris) {
        Kulsyre kulsyre = new Kulsyre(navn, pris);
        kulsyrer.add(kulsyre);
        return kulsyre;
    }

    public ArrayList<Kulsyre> getKulsyrer() {
        return new ArrayList<>(kulsyrer);
    }

    public void removeKulsyre(Kulsyre kulsyre) {
        kulsyrer.remove(kulsyre);
    }

    // anlæg
    public Anlæg createAnlæg(String navn, int pris) {
        Anlæg anlæg = new Anlæg(navn, pris);
        this.anlæg.add(anlæg);
        return anlæg;
    }

    public ArrayList<Anlæg> getAnlæg() {
        return new ArrayList<>(anlæg);
    }

    public void removeAnlæg(Anlæg anlæg) {
        this.anlæg.remove(anlæg);
    }

    // liste for udlejningsprodukterne
    public ArrayList<String> getListe_for_udlejningsprodukterne() {
        return new ArrayList<>(liste_for_udlejningsprodukterne);
    }

    // beregner prisen for en fadølsanlæg - det dækker over fustage, anlæg og
    // kulsyre
    @Override
    public void beregnPris() {
        double pris = 0;
        int antalFustage = 0;

        // hvis der er nogle fustager i udlejningen
        if (fustager.isEmpty() == false) {
            for (Fustage f : fustager) {
                pris = pris + f.getPris();
            }

        }

        // hvis der er nogle kulsyrer i udlejningen
        if (kulsyrer.isEmpty() == false) {
            for (Kulsyre k : kulsyrer) {
                pris = pris + k.getPris();
            }

        }

        // hvis der er nogle anlæg i udlejningen
        if (anlæg.isEmpty() == false) {
            for (Anlæg a : anlæg) {
                pris = pris + a.getPris();
            }
        }

        for (Produktpris p : super.getProduktpriser()) {

            // hvis produktnavnet i produktpriser har samme produktnavn
            if (p.getProdukt().getNavn().equals(super.getNavn())) {
                p.setPris(pris);
            }
        }
    }

}
