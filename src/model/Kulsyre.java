package model;

public class Kulsyre extends Produkt {

    private int kg;

    public Kulsyre(String navn, Produktgruppe produktgruppe, int kg) {
        super(navn, produktgruppe);
        this.kg = kg;
        super.setNavn(navn + ", " + kg + " kg " + "(Pant: 1000kr.)");
    }

    public int getKg() {
        return kg;
    }

    public void setKg(int kg) {
        this.kg = kg;
    }

}
