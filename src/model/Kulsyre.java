package model;

public class Kulsyre extends Produkt {

    private int kg;
    private int pant = 1000;

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

    public int getPant() {
		return pant;
	}

	@Override
    public void beregnPris() {

    }

}
