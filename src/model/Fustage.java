package model;

public class Fustage extends Produkt {

    private int liter;

    public Fustage(String navn, Produktgruppe produktgruppe, int liter) {
        super(navn, produktgruppe);
        this.liter = liter;
        super.setNavn(navn + ", " + liter + " liter " + "(Pant: 200kr.)");
    }

    public int getLiter() {
        return liter;
    }

    public void setLiter(int liter) {
        this.liter = liter;
    }

}
