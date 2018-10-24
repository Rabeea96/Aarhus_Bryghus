package model;

public class Kulsyre extends Produkt {

    private int kg;

	public Kulsyre(String navn, Produktgruppe produktgruppe, int kg) {
        super(navn, produktgruppe);
        super.setNavn(navn + " (Pant: 1000kr.)");
        this.kg = kg;
    }

    public int getKg() {
        return kg;
    }

    public void setKg(int kg) {
        this.kg = kg;
    }

    @Override
    public void beregnPris() {

    }

}
