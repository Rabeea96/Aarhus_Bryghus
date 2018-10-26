package model;

public class Anlæg extends Produkt {

    private int antalHaner;

    public Anlæg(String navn, Produktgruppe produktgruppe, int antalHaner) {
        super(navn, produktgruppe);
        this.antalHaner = antalHaner;
    }

    public int getAntalHaner() {
        return antalHaner;
    }

    public void setAntalHaner(int antalHaner) {
        this.antalHaner = antalHaner;
    }

}
