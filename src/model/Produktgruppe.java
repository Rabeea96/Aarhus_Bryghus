package model;

import java.util.ArrayList;

public class Produktgruppe {

    private String navn;
    private ArrayList<Produkt> produkter = new ArrayList<>();

    public Produktgruppe(String navn) {
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void addProdukt(Produkt produkt) {
        produkter.add(produkt);
    }

    public void removeProdukt(Produkt produkt) {
        produkter.remove(produkt);
    }

    public ArrayList<Produkt> getProdukter() {
        return new ArrayList<>(produkter);
    }

    @Override
    public String toString() {
        return navn;
    }

}
