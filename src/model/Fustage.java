package model;

public class Fustage extends Produkt {

    private int liter;

    public Fustage(String navn, Produktgruppe produktgruppe, int liter) {
        super(navn, produktgruppe);
        this.liter = liter;
    }

    public int getLiter() {
        return liter;
    }

    public void setLiter(int liter) {
        this.liter = liter;
    }

    @Override
    public void beregnPris() {
        // TODO Auto-generated method stub

    }

}
