package model;

public class Anlæg extends Produkt {

    private int antalHaner;

    public Anlæg(String navn, Produktgruppe produktgruppe, int antalHaner) {
        super(navn, produktgruppe);
        this.antalHaner = antalHaner;
        if (antalHaner == 1) {
            super.setNavn(navn + ", " + antalHaner + "-hane");
        } else {
            super.setNavn(navn + ", " + antalHaner + "-haner");
        }
    }

    public int getAntalHaner() {
        return antalHaner;
    }

    public void setAntalHaner(int antalHaner) {
        this.antalHaner = antalHaner;
    }

}
