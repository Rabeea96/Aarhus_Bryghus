package model;

public class Klippekort extends Produkt {

    private int antal_klip;

    public Klippekort(String navn, Produktgruppe produktgruppe, int antal_klip) {
        super(navn, produktgruppe);
        this.antal_klip = antal_klip;
        super.setNavn(navn + ", " + antal_klip + " klip");
    }

    public int getAntal_klip() {
        return antal_klip;
    }

    public void setAntal_klip(int antal_klip) {
        this.antal_klip = antal_klip;
    }

}
