package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Salg {

    private ArrayList<String> produktNavn = new ArrayList<>();
    private ArrayList<Integer> produktPris = new ArrayList<>();
    private ArrayList<Integer> produktAntal = new ArrayList<>();

    // en arrayliste der indeholder produtnavn, produktpris og produktantal i hvert
    // index
    private ArrayList<String> navn_pris_antal = new ArrayList<>();
    private double samletPris;
    private Betalingsmiddel betalingsmiddel;
    private LocalDate dato;
    private Ordre ordre;

    public Salg(ArrayList<String> produktNavn, ArrayList<Integer> produktPris, ArrayList<Integer> produktAntal,
            double samletPris, Betalingsmiddel betalingsmiddel, LocalDate dato, Ordre ordre) {
        this.produktNavn = produktNavn;
        this.produktPris = produktPris;
        this.produktAntal = produktAntal;
        this.samletPris = samletPris;
        this.betalingsmiddel = betalingsmiddel;
        this.dato = dato;
        this.ordre = ordre;

        // arrayliste der samler navn, pris og antal
        for (int i = 0; i < produktNavn.size(); i++) {
            navn_pris_antal.add("Produktnavn: " + produktNavn.get(i) + " | Produktpris: " + produktPris.get(i)
                    + " | Produktantal: " + produktAntal.get(i));
        }
    }

    // produkt-navn
    public ArrayList<String> getProduktNavn() {
        return new ArrayList<>(produktNavn);
    }

    public void addProduktNavn(String navn) {
        produktNavn.add(navn);
    }

    public void removeProduktNavn(String navn) {
        produktNavn.remove(navn);
    }

    // produkt-pris
    public ArrayList<Integer> getProduktPris() {
        return new ArrayList<>(produktPris);
    }

    public void addProduktPris(int pris) {
        produktPris.add(pris);
    }

    public void removeProduktPris(int pris) {
        produktPris.remove(pris);
    }

    // produkt-antal
    public ArrayList<Integer> getProduktAntal() {
        return new ArrayList<>(produktAntal);
    }

    public void addProduktAntal(int antal) {
        produktAntal.add(antal);
    }

    public void removeProduktAntal(int antal) {
        produktAntal.remove(antal);
    }

    public double getSamletPris() {
        return samletPris;
    }

    public void setSamletPris(double samletPris) {
        this.samletPris = samletPris;
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

    public Ordre getOrdre() {
        return ordre;
    }

    public void setOrdre(Ordre ordre) {
        this.ordre = ordre;
    }

    @Override
    public String toString() {
        return navn_pris_antal + "\n SamletPris=" + samletPris + "\n Betalingsmiddel=" + betalingsmiddel + "\n Dato="
                + dato + "\n";
    }

}
