package model;

import java.util.ArrayList;

public abstract class Produkt {

    private String navn;
    private ArrayList<Produktpris> produktpriser = new ArrayList<>();

    public Produkt(String navn, Produktgruppe produktgruppe) {
        this.navn = navn;
        produktgruppe.addProdukt(this);
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Produktpris createProduktpris(Prisliste prisliste, double pris, Produkt produkt) {
        Produktpris produktpris = new Produktpris(prisliste, pris, produkt);
        produktpriser.add(produktpris);
        return produktpris;
    }

    public ArrayList<Produktpris> getProduktpriser() {
        return new ArrayList<>(produktpriser);
    }
    
    public int getPant()
    {
    	return 0;
    }

    // abstract metode der ændrer produktpris
    public abstract void beregnPris();
    
  

    @Override
    public String toString() {
        return navn;
    }

}
