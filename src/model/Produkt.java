package model;

import java.util.ArrayList;

public class Produkt {

    private String navn;
    private Produktgruppe produktgruppe;
    private ArrayList<Produktpris> produktpriser = new ArrayList<>();

    public Produkt(String navn) {
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Produktgruppe getProduktgruppe() {
        return produktgruppe;
    }

    // Bemærk: package synlighed
    void setProduktgruppe(Produktgruppe produktgruppe) {
        this.produktgruppe = produktgruppe;
    }

    public Produktpris createProduktpris(Prisliste prisliste, double pris) {
        Produktpris produktpris = new Produktpris(prisliste, pris);
        produktpriser.add(produktpris);
        return produktpris;
    }

    public ArrayList<Produktpris> getProduktpriser() {
        return new ArrayList<>(produktpriser);
    }

    @Override
    public String toString() {
        return navn;
    }

}
